/*
 * 文 件 名:  FileUploader.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  luoxiaoyan
 * 修改时间:  2014年12月8日
 */
package com.dimeng.p2p.common;

import java.io.File;
import java.io.FileInputStream; 
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;  
import com.dimeng.framework.http.upload.UploadFile;
import com.dimeng.framework.resource.ResourceProvider;
import com.dimeng.p2p.common.enums.UploadState;
import com.dimeng.util.StringHelper;
/**
 * <文件上传器>
 * <功能详细描述>
 * 
 * @author  luoxiaoyan
 * @version  [版本号, 2014年12月8日]
 */
public class FileUploader 
{   
    private File home;
    private String uploadURI = null;   
    public ResourceProvider resourceProvider ; 
    public FileUploader(ResourceProvider rp) { 
        resourceProvider = rp; 
    }
    public String getUploadURI() {
        if (uploadURI == null) { 
            String value = resourceProvider.getInitParameter("fileStore.uploadURI");
            if (StringHelper.isEmpty(value)) {
                uploadURI = "/fileStore";
            } else if (value.charAt(0) != '/') {
                uploadURI = '/' + value;
            } else {
                uploadURI = value;
            }
        }
        return uploadURI;
    }
    public File getHome() {
        if (home == null) {
            String stringHome = resourceProvider
                    .getInitParameter("fileStore.home");
            if (StringHelper.isEmpty(stringHome)) {
                home = new File(System.getProperty("user.home"), "fileStore");
            } else {
                File file = new File(stringHome);
                if (file.isAbsolute()) {
                    home = file;
                } else {
                    String contextHome = resourceProvider.getHome();
                    home = new File(contextHome, stringHome);
                }
            }
            home.mkdirs();
        }
        return home;
    } 
    
    public String getURL(String fileName) {
        if (StringHelper.isEmpty(fileName)) {
            return "";
        }
        StringBuilder url = new StringBuilder();
        url.append(resourceProvider.getContextPath()).append(getUploadURI())
                .append("/app/") ;  
        url.append(fileName); 
        return url.toString();
    }
 
    
    private File getFile(String fileName) throws IOException {
        StringBuilder url = new StringBuilder(); 
        url.append("/app") ;   
        File path = new File(getHome(), url.toString());
        path.mkdirs();
        url.setLength(0);  
        url.append(fileName);
        return new File(path, url.toString());
    }
    /**
     * <b>function:</b>通过输入流参数上传文件 
     * @param uploadFileName 文件名称
     * @param savePath 保存路径
     * @param InputStream 上传的文件的输入流
     * @return 是否上传成功
     * @throws Exception
     */
    public  UploadState uploadFile(String fileName, UploadFile uploadFile)
        throws Exception
    { 
        UploadState state = UploadState.UPLOAD_FAILURE; 
        try (InputStream inputStream = uploadFile.getInputStream()) {
            if (inputStream.available() <= 0) { 
                return UploadState.UPLOAD_ZEROSIZE;
            } 
            String prefix=fileName.substring(fileName.lastIndexOf(".")+1); 
            prefix = prefix.toLowerCase();
            if("apk".equals(prefix) || "ipa".equals(prefix)){
	            try (FileOutputStream outputStream = new FileOutputStream(getFile(fileName))) {
	                if (inputStream instanceof FileInputStream) {
	                    try (FileChannel in = ((FileInputStream) inputStream)
	                            .getChannel();
	                            FileChannel out = outputStream.getChannel()) {
	                        in.transferTo(in.position(), in.size(), out);
	                    }
	                } else {
	                    byte[] buf = new byte[8192];
	                    int len = inputStream.read(buf);
	                    while (len > 0) {
	                        outputStream.write(buf, 0, len);
	                        len = inputStream.read(buf);
	                    }
	                }
	             }
            } 
        }
        return state; 
    } 
      
}
