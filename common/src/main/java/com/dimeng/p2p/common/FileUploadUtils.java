package com.dimeng.p2p.common;

import java.io.IOException;
import java.io.InputStream;

import org.apache.log4j.Logger;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.resource.ResourceProvider;
import com.dimeng.p2p.common.enums.FileType;
import com.dimeng.p2p.variables.defines.SystemVariable;

public class FileUploadUtils
{
	protected static final Logger logger = Logger.getLogger(FileUploadUtils.class);
    /** 
     * 将文件头转换成16进制字符串 
     *  
     * @param 原生byte 
     * @return 16进制字符串 
     */
    private static String bytesToHexString(byte[] src)
    {
        
        StringBuilder stringBuilder = new StringBuilder();
        if (src == null || src.length <= 0)
        {
            return null;
        }
        for (int i = 0; i < src.length; i++)
        {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2)
            {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }
    
    /** 
     * 得到文件头 
     *  
     * @param inputStream 文件流
     * @return 文件头 
     * @throws IOException 
     */
    private static String getFileContent(InputStream inputStream)
        throws IOException
    {
        
        byte[] b = new byte[512];
        
        try
        {
            inputStream.read(b, 0, b.length);
        }
        catch (IOException e)
        {
            logger.error(e, e);
            throw e;
        }
        finally
        {
            if (inputStream != null)
            {
                try
                {
                    inputStream.close();
                }
                catch (IOException e)
                {
                    logger.error(e, e);
                    throw e;
                }
            }
        }
        return bytesToHexString(b);
    }
    
    /** 
     * 判断文件类型 
     *  
     * @param inputStream 文件流 
     * @return 文件类型 
     */
    public static FileType getType(InputStream inputStream)
        throws IOException
    {
        
        String fileHead = getFileContent(inputStream);
        
        if (fileHead == null || fileHead.length() == 0)
        {
            return null;
        }
        
        fileHead = fileHead.toUpperCase();
        
        FileType[] fileTypes = FileType.values();
        
        for (FileType type : fileTypes)
        {
            if (fileHead.startsWith(type.getValue()))
            {
                return type;
            }
        }
        
        return null;
    }
    
    /** 
     * 判断文件类型 
     *  
     * @param filePath 文件路径 
     * @return 文件类型 
     */
    public static boolean checkFileType(InputStream inputStream, String fileType, ResourceProvider resourceProvider)
        throws IOException
    {
        boolean rtnBoolean = true;
        String byteFileType = String.valueOf(getType(inputStream));
        ConfigureProvider configureProvider = resourceProvider.getResource(ConfigureProvider.class);
        String allowFileType = configureProvider.getProperty(SystemVariable.ALLOW_UPLOAD_FILE_TYPE);
        
        if (FileType.XLS_DOC.toString().equals(byteFileType)
            || FileType.OFFICE_2007.toString().equals(byteFileType) || FileType.JPEG.toString().equals(byteFileType))
        {
            if (allowFileType.toUpperCase().contains(fileType.toUpperCase()))
            {
                rtnBoolean = true;
            }
            else
            {
                rtnBoolean = false;
            }
        }
        else
        {
            if (allowFileType.toUpperCase().contains(byteFileType))
            {
                rtnBoolean = true;
            }
            else
            {
                rtnBoolean = false;
            }
        }
        return rtnBoolean;
    }
    
    /**
     * 生成文件头到控制台
     */
    //    public static void main(String[] args)  
    //    {  
    //       
    //        try
    //        {
    //            System.out.println(getType(new FileInputStream(new File("E:/1.txt"))));
    //        }
    //        catch (Exception e)
    //        {
    //            e.printStackTrace();
    //        }
    //    }
}
