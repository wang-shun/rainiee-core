package com.dimeng.p2p.common;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.dimeng.util.StringHelper;

/**
 * 本地数据库配置文件读取
 * @author  XiaoLang 2014 © dimeng.net 
 * @version v3.0
 * @LastModified 
 * 		Created,by XiaoLang 2014年12月22日
 */
@SuppressWarnings(value="unused")
public interface DBConfigurator
{
    String getUrl();
    
    String getDataBaseName();
    
    String getUrl(String dbName);
    
    String getPassword();
    
    String getUser();
    
    String getDriver();
    
    Properties getProperties();
    
    /**
     * 研发库配置文件读取
     * <p>加密文件生成:<code>new DevelopmentConfigurator().exportKeyFile(...)</code></p>
     * @author  XiaoLang 2014 © dimeng.net 
     * @version v3.0
     * @LastModified 
     * 		Created,by XiaoLang 2014年12月22日
     */
    public final class DevelopmentConfigurator implements DBConfigurator
    {
        
        /**
         */
        public DevelopmentConfigurator()
        {
            this(DEFAULT_KEY_FILE_ROOT_PATH);
        }
        
        /**
         * @param keystore 
         *         密钥文件释放及读取位置
         */
        public DevelopmentConfigurator(String keystore)
        {
            this.keystore = keystore + '/' + DEFAULT_FILE_NAME;
            build();
        }
        protected final Logger logger = Logger.getLogger(getClass());
        private final String keystore;
        
        private static final Properties properties = new Properties();
        
        private static final String DEFAULT_DRIVER = "com.mysql.jdbc.Driver";
        
        private static final String DEFAULT_URL = "jdbc:mysql://localhost:3306";
        
        private static final String DEFAULT_DB_NAME = "S10";
        
        private static final String DEFAULT_USER = "root";
        
        private static final String DEFAULT_PASSWORD = "PZZGpAxao4VTFAFGUfatXg==";
        
        private static final String DEFAULT_KEY_FILE_ROOT_PATH = "D:/";
        
        private static final String DEFAULT_FILE_NAME = "development.key";
        
        private final void build()
        {
            FileInputStream fis = null;
            ByteArrayOutputStream bos = null;
            boolean readable = true;
            try
            {
                properties.clear();
                fis = new FileInputStream(new File(getKeystore()));
                bos = new ByteArrayOutputStream();
                byte[] buf = new byte[1024];
                int r;
                while ((r = fis.read(buf, 0, buf.length)) != -1)
                {
                    bos.write(buf, 0, r);
                }
                // bos.flush();
                String encodeKey = new String(bos.toByteArray(), Charset.forName("UTF-8"));
                String decodeKey = StringHelper.decode(encodeKey);
                if (!StringHelper.isEmpty(decodeKey))
                {
                    String[] splits = decodeKey.split(",|;");
                    if (splits.length == 3)
                    {
                        properties.setProperty("url", splits[0]);
                        properties.setProperty("user", splits[1]);
                        properties.setProperty("password", splits[2]);
                    }
                }
            }
            catch (Throwable e)
            {
                System.out.println("Warning: File " + getKeystore() + " is not exists or decode failure.");
                readable = false;
            }
            finally
            {
                try
                {
                    String url = properties.getProperty("url");
                    if (StringHelper.isEmpty(url))
                    {
                        url = DEFAULT_URL;
                    }
                    if (readable)
                    {
                        System.out.println("Keystore within '" + getKeystore() + "'," + "Using " + url.split("//")[1]);
                    }
                    else
                    {
                        System.out.println("No keystore file, Default using " + url.split("//")[1]);
                    }
                }
                catch (Exception e)
                {
                    logger.error(e, e);
                }
                if (fis != null)
                {
                    try
                    {
                        fis.close();
                        fis = null;
                    }
                    catch (IOException e)
                    {
                        logger.error(e, e);
                    }
                }
                if (bos != null)
                {
                    try
                    {
                        bos.close();
                        bos = null;
                    }
                    catch (IOException e)
                    {
                        logger.error(e, e);
                    }
                }
            }
            
        }
        
        @Override
        public Properties getProperties()
        {
            Properties props = new Properties();
            props.setProperty("user", getUser());
            props.setProperty("password", getPassword());
            props.setProperty("useUnicode", "true");
            props.setProperty("characterEncoding", "UTF8");
            props.setProperty("noAccessToProcedureBodies", "true");
            return props;
        }
        
        @Override
        public String getDriver()
        {
            return DEFAULT_DRIVER;
        }
        
        @Override
        public String getUrl()
        {
            String url = properties.getProperty("url");
            return StringHelper.isEmpty(url) ? DEFAULT_URL : url;
        }
        
        @Override
        public String getUrl(String dataBaseName)
        {
            return getUrl() + "/" + dataBaseName;
        }
        
        @Override
        public String getDataBaseName()
        {
            String dbName = properties.getProperty("dataBaseName");
            return StringHelper.isEmpty(dbName) ? DEFAULT_DB_NAME : dbName;
        }
        
        @Override
        public String getPassword()
        {
            String password = properties.getProperty("password");
            try {
				return StringHelper.decode(password);
			} catch (Throwable e) {
				// TODO Auto-generated catch block
                logger.error(e, e);
				return password;
			}
        }
        
        @Override
        public String getUser()
        {
            String user = properties.getProperty("user");
            return StringHelper.isEmpty(user) ? DEFAULT_USER : user;
        }
        
        /**
         * 密钥文件存储位置
         * @param jdbcUrl
         *      数据库连接地址,例 "jdbc:mysql://192.168.0.1:3306"
         * @param user
         *      数据库访问角色名,例 "root"
         * @param password
         *      数据库访问角色密码
         * @comment
         */
        public void exportKeyFile(String jdbcUrl, String user, String password)
        {
            FileOutputStream fos = null;
            StringBuilder builder = new StringBuilder();
            try
            {
                if (!StringHelper.isEmpty(getKeystore()) && getKeystore().endsWith(DEFAULT_FILE_NAME))
                {
                    File outDir = new File(getKeystore().substring(0, getKeystore().length() - 16));
                    if (!outDir.exists())
                    {
                        outDir.mkdirs();
                    }
                    File outFile = new File(getKeystore());
                    if (!outFile.exists())
                    {
                        outFile.createNewFile();
                    }
                    builder.append(jdbcUrl).append(',').append(user).append(',').append(password);
                    fos = new FileOutputStream(outFile);
                    fos.write(StringHelper.encode(builder.toString()).getBytes(Charset.forName("UTF-8")));
                    fos.flush();
                    System.out.println("Generated keystore file success, within '" + getKeystore() + "'");
                }
            }
            catch (Throwable e)
            {
                logger.error(e, e);
            }
            finally
            {
                if (fos != null)
                    try
                    {
                        fos.close();
                    }
                    catch (IOException e)
                    {
                        logger.error(e, e);
                    }
                builder.setLength(0);
                builder = null;
            }
        }
        
        private String getKeystore()
        {
            return keystore;
        }
    }
    
}
