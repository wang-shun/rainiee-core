package com.dimeng.p2p;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.config.SystemDefine;
import com.dimeng.framework.data.sql.SQLConnectionProvider;
import com.dimeng.framework.http.upload.FileInformation;
import com.dimeng.framework.http.upload.FileStore;
import com.dimeng.framework.http.upload.UploadFile;
import com.dimeng.framework.resource.AchieveVersion;
import com.dimeng.framework.resource.ResourceAnnotation;
import com.dimeng.framework.resource.ResourceProvider;
import com.dimeng.framework.resource.ResourceRetention;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.BooleanParser;

import java.io.*;
import java.nio.channels.FileChannel;
import java.sql.*;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@ResourceAnnotation({ResourceRetention.PRE_PRODUCTION, ResourceRetention.PRODUCTION})
@AchieveVersion(version = 20160806)
public class ZipLocalFileStore extends FileStore
{

    protected final char separatorChar;

    protected final Pattern pattern;

    protected final String prefix;

    public ZipLocalFileStore(ResourceProvider resourceProvider)
    {
        super(resourceProvider);
        separatorChar = File.separatorChar;
        StringBuilder builder = new StringBuilder();
        builder.append(resourceProvider.getContextPath());
        builder.append(getUploadURI());
        builder.append('/');
        prefix = builder.toString();
        builder.append("\\d+/\\d+/\\d+/\\d+/\\d+(\\.\\w+)?");
        pattern = Pattern.compile(builder.toString());
        log();
    }
    
    private void log()
    {
        File file = getHome();
        String contextPath = resourceProvider.getContextPath();
        resourceProvider.log(String.format("部署路径: %s, 运行模式: %s, 上传文件存储路径: %s, 上传文件映射路径: %s.",
                StringHelper.isEmpty(contextPath) ? "/" : contextPath,
                resourceProvider.getResourceRetention().name(),
                file == null ? "无" : file.getPath(),
                getUploadURI()));
    }
    
    @Override
    public String newCode(int type, String suffix)
        throws Throwable
    {
        if (type < 0)
        {
            type = 0;
        }
        final int fileId;
        final int year;
        final int month;
        final int day;
        SystemDefine systemDefine = resourceProvider.getSystemDefine();
        try (Connection connection =
            resourceProvider.getDataConnectionProvider(SQLConnectionProvider.class,
                systemDefine.getDataProvider(FileStore.class))
                .getConnection(systemDefine.getSchemaName(FileStore.class)))
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("INSERT INTO _1050 SET F03 = ?, F04 = ?", Statement.RETURN_GENERATED_KEYS))
            {
                pstmt.setInt(1, type);
                pstmt.setString(2, suffix);
                pstmt.execute();
                try (ResultSet resultSet = pstmt.getGeneratedKeys())
                {
                    if (resultSet.next())
                    {
                        fileId = resultSet.getInt(1);
                    }
                    else
                    {
                        throw new SQLException("生成文件ID失败.");
                    }
                }
            }
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT YEAR(F02), MONTH(F02), DAY(F02) FROM _1050 WHERE F01 = ?"))
            {
                pstmt.setInt(1, fileId);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        year = resultSet.getInt(1);
                        month = resultSet.getInt(2);
                        day = resultSet.getInt(3);
                        
                    }
                    else
                    {
                        throw new SQLException("生成文件ID失败.");
                    }
                }
            }
        }
        StringBuilder builder = new StringBuilder();
        builder.append(Integer.toHexString(year))
            .append('-')
            .append(Integer.toHexString(month))
            .append('-')
            .append(Integer.toHexString(day))
            .append('-')
            .append(Integer.toHexString(type < 0 ? 0 : type))
            .append('-')
            .append(Integer.toHexString(fileId));
        if (StringHelper.isEmpty(suffix) || ".".equals(suffix))
        {
        }
        else
        {
            if (suffix.charAt(0) != '.')
            {
                builder.append('.');
            }
            builder.append(suffix);
        }
        return builder.toString();
    }
    
    @Override
    public FileInformation getFileInformation(String fileCode)
    {
        final String suffix;
        int end = fileCode.lastIndexOf('.');
        if (end != -1)
        {
            if (end + 1 < fileCode.length())
            {
                suffix = fileCode.substring(end + 1);
            }
            else
            {
                suffix = null;
            }
            fileCode = fileCode.substring(0, end);
        }
        else
        {
            suffix = null;
        }
        String[] items = fileCode.split("-");
        if (items.length != 5)
        {
            return null;
        }
        try
        {
            final int year = Integer.parseInt(items[0], 16);
            final int month = Integer.parseInt(items[1], 16);
            final int day = Integer.parseInt(items[2], 16);
            final int type = Integer.parseInt(items[3], 16);
            final int id = Integer.parseInt(items[4], 16);
            return new FileInformation()
            {
                
                @Override
                public int getYear()
                {
                    return year;
                }
                
                @Override
                public int getType()
                {
                    return type;
                }
                
                @Override
                public String getSuffix()
                {
                    return suffix;
                }
                
                @Override
                public int getMonth()
                {
                    return month;
                }
                
                @Override
                public int getId()
                {
                    return id;
                }
                
                @Override
                public int getDay()
                {
                    return day;
                }
            };
        }
        catch (Throwable t)
        {
            return null;
        }
    }
    
    @Override
    public String getURL(FileInformation fileInformation)
    {
        if (fileInformation == null)
        {
            return "";
        }
        StringBuilder url = new StringBuilder();
        url.append(resourceProvider.getContextPath())
            .append(getUploadURI())
            .append('/')
            .append(fileInformation.getType())
            .append('/')
            .append(fileInformation.getYear())
            .append('/')
            .append(fileInformation.getMonth())
            .append('/')
            .append(fileInformation.getDay())
            .append('/')
            .append(fileInformation.getId());

        String subffix = fileInformation.getSuffix();
        if (!StringHelper.isEmpty(subffix))
        {
            url.append(".").append(subffix);
        }
        return url.toString();
    }

    private void concatUrl(StringBuilder url, String reUrl, String subffix)
    {
        boolean isZip = false;
        ConfigureProvider configureProvider = resourceProvider.getResource(ConfigureProvider.class);
        if(BooleanParser.parse(configureProvider.getProperty(SystemVariable.IS_OPEN_ZIP_IMAGE)))
        {
            String zipUrl = "_zip."+subffix;
            if(new File(reUrl+zipUrl).canRead())
            {
                isZip = true;
                url.append("_zip");
            }

        }
        if(BooleanParser.parse(configureProvider.getProperty(SystemVariable.IS_OPEN_WATERIMAGE))) {
            String waterUrl = "_w" + "." + subffix;
            if(new File(reUrl+(isZip ? "_zip": "") + waterUrl).canRead())
            {
                url.append("_w");
            }
        }
    }


    private File getFile(FileInformation fileInformation)
        throws IOException
    {
        StringBuilder uri = new StringBuilder();
        uri.append(fileInformation.getType())
            .append(separatorChar)
            .append(fileInformation.getYear())
            .append(separatorChar)
            .append(fileInformation.getMonth())
            .append(separatorChar)
            .append(fileInformation.getDay());
        File path = new File(getHome(), uri.toString());
        path.mkdirs();
        uri.setLength(0);
        uri.append(fileInformation.getId());
        String subffix = fileInformation.getSuffix();
        String reUrl = path.getPath()+separatorChar+fileInformation.getId();
        if (!StringHelper.isEmpty(subffix))
        {
            concatUrl(uri, reUrl, subffix);
            uri.append('.').append(subffix);
        }
        return new File(path, uri.toString());
    }
    
    @Override
    public void write(FileInformation fileInformation, InputStream inputStream)
        throws IOException
    {
        if (inputStream.available() <= 0)
        {
            return;
        }
        try (FileOutputStream outputStream = new FileOutputStream(getFile(fileInformation)))
        {
            if (inputStream instanceof FileInputStream)
            {
                try (FileChannel in = ((FileInputStream)inputStream).getChannel();
                    FileChannel out = outputStream.getChannel())
                {
                    in.transferTo(in.position(), in.size(), out);
                }
            }
            else
            {
                byte[] buf = new byte[8192];
                int len = inputStream.read(buf);
                while (len > 0)
                {
                    outputStream.write(buf, 0, len);
                    len = inputStream.read(buf);
                }
            }
        }
    }
    
    @Override
    public void read(FileInformation fileInformation, OutputStream outputStream)
        throws IOException
    {
        try (FileInputStream inputStream = new FileInputStream(getFile(fileInformation)))
        {
            if (outputStream instanceof FileOutputStream)
            {
                try (FileChannel in = inputStream.getChannel();
                    FileChannel out = ((FileOutputStream)outputStream).getChannel())
                {
                    in.transferTo(in.position(), in.size(), out);
                }
            }
            else
            {
                byte[] buf = new byte[8192];
                int len = inputStream.read(buf);
                while (len > 0)
                {
                    outputStream.write(buf, 0, len);
                    len = inputStream.read(buf);
                }
            }
        }
    }
    
    @Override
    public String[] upload(int type, UploadFile... uploadFiles)
        throws Throwable
    {
        if (uploadFiles == null || uploadFiles.length == 0)
        {
            return null;
        }
        ArrayList<String> codes = null;
        for (UploadFile uploadFile : uploadFiles)
        {
            if (uploadFile == null)
            {
                continue;
            }
            try (InputStream inputStream = uploadFile.getInputStream())
            {
                if (inputStream.available() <= 0)
                {
                    continue;
                }
                String code = newCode(type, uploadFile.getSuffix());
                write(getFileInformation(code), inputStream);
                if (codes == null)
                {
                    codes = new ArrayList<>();
                }
                codes.add(code);
            }
        }
        return codes == null || codes.size() == 0 ? null : codes.toArray(new String[codes.size()]);
    }

    @Override
    public String encode(String value)
    {
        if (StringHelper.isEmpty(value))
        {
            return value;
        }
        StringBuilder out = new StringBuilder();
        Matcher matcher = pattern.matcher(value);
        int startIndex = 0, endIndex = 0;
        int year;
        int month;
        int day;
        int type;
        int fileId;
        String suffix;
        int suffixIndex;
        StringBuilder builder = new StringBuilder();
        while (matcher.find())
        {
            endIndex = matcher.start();
            if (endIndex != startIndex)
            {
                out.append(value, startIndex, endIndex);
            }
            final String v = matcher.group();
            try
            {
                String key = v.substring(prefix.length());
                suffixIndex = key.lastIndexOf('.');
                if (suffixIndex != -1)
                {
                    suffix = key.substring(suffixIndex);
                    key = key.substring(0, suffixIndex);
                }
                else
                {
                    suffix = null;
                }
                String[] items = key.split("/");
                type = Integer.parseInt(items[0]);
                year = Integer.parseInt(items[1]);
                month = Integer.parseInt(items[2]);
                day = Integer.parseInt(items[3]);
                fileId = Integer.parseInt(items[4]);
                builder.setLength(0);
                builder.append("${");
                builder.append(Integer.toHexString(year))
                    .append('-')
                    .append(Integer.toHexString(month))
                    .append('-')
                    .append(Integer.toHexString(day))
                    .append('-')
                    .append(Integer.toHexString(type < 0 ? 0 : type))
                    .append('-')
                    .append(Integer.toHexString(fileId));
                if (!StringHelper.isEmpty(suffix))
                {
                    builder.append(suffix);
                }
                builder.append('}');
                out.append(builder.toString());
            }
            catch (Throwable t)
            {
                out.append(v);
            }
            startIndex = matcher.end();
        }
        endIndex = value.length();
        if (startIndex < endIndex)
        {
            out.append(value, startIndex, endIndex);
        }
        return out.toString();
    }
    
    @Override
    public void initilize(Connection connection)
        throws Throwable
    {
        try (Statement stmt = connection.createStatement())
        {
            stmt.addBatch("CREATE TABLE IF NOT EXISTS _1050 (F01 int(10) unsigned NOT NULL AUTO_INCREMENT,F02 timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,F03 int(10) unsigned NOT NULL, F04 varchar(20) DEFAULT NULL, PRIMARY KEY (F01));");
            stmt.executeBatch();
        }
    }
    
}
