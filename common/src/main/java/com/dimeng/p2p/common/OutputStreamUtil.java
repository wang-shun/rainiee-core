package com.dimeng.p2p.common;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import com.dimeng.framework.service.exception.LogicalException;

/**
 * 
 * 操作文本信息
 * @author  xiaoyaocai
 * @version  [版本号, 2015年3月23日]
 */
public class OutputStreamUtil
{
    /** 
     * 生成数据文件 
     *  
     * @param filePath 写入文件的路径 
     * @param content 写入的字符串内容 
     * @param isAppend 是否覆盖原文件
     * @return 
     */  
    @SuppressWarnings(value="unused")
    public static boolean string2File(String content, String filePath,boolean isAppend) throws Throwable{  
        BufferedReader bufferedReader = null;  
        BufferedWriter bufferedWriter;  
        InputStreamReader reader;  
        
        boolean flag = true;  
        try {  
            File file = new File(filePath);  
            if (!file.exists()) {  
                file.createNewFile();  
            }  
            bufferedReader = new BufferedReader(new StringReader(content));  
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(file, isAppend),"UTF-8"));
            char buffer[] = new char[1024];  
            int len;  
            while ((len = bufferedReader.read(buffer)) != -1) {  
                bufferedWriter.write(buffer, 0, len);  
            }  
            bufferedWriter.flush();  
            bufferedReader.close();  
            bufferedWriter.close();  
        } catch (IOException e) {  
            flag = false;
            throw new LogicalException("写入文本出错");
        } finally {  
            if (bufferedReader != null) {  
                try {  
                    bufferedReader.close();  
                } catch (IOException e) {  
                    throw new LogicalException("关闭字符流出错");
                }  
            }  
        }  
        return flag;  
    }  
    /** 
     * 读取数据文件 
     * @param filePath 读取的文件路径 
     * @param encoding 读取后的字符串编码集设置 
     * @return 
     */  
    @SuppressWarnings(value="unused")
    public static String file2String(String filePath, String encoding) throws Throwable{  
        
        InputStreamReader reader = null;
        
        StringWriter writer = new StringWriter();  
        File file = new File(filePath);  
        try {  
            if (encoding == null || "".equals(encoding.trim())) {  
                reader = new InputStreamReader(new FileInputStream(file),"UTF-8");
            } else {  
                reader = new InputStreamReader(new FileInputStream(file), encoding);  
            }  
            // 将输入流写入输出流  
            char[] buffer = new char[1024];  
            int n = 0;  
            while (-1 != (n = reader.read(buffer))) {  
                writer.write(buffer, 0, n);  
            }  
        } catch (Exception e) {  
            throw new LogicalException("读取文本出错");
        } finally {  
            if (reader != null)  
                try {  
                    reader.close();  
                } catch (IOException e) {  
                    throw new LogicalException("关闭字符流出错");
                }  
        }  
        // 返回转换结果  
        return writer.toString();
    }
    
    public static List<String> file2Strings(String filePath,String encoding) throws Throwable{
        List<String> result = new ArrayList<String>();
        File file=new File(filePath);
        if(file.isFile() && file.exists()){ //判断文件是否存在
        	//考虑到编码格式
            try(InputStreamReader read = new InputStreamReader(new FileInputStream(file),encoding)){
            	try(BufferedReader bufferedReader = new BufferedReader(read)){
		            String lineTxt = null;
		            while((lineTxt = bufferedReader.readLine()) != null){
		                result.add(lineTxt);
		            }
            	}
            }
        }
        return result;
    }
    
    public static void main(String[] args) {  
        try
        {
            new OutputStreamUtil().string2File("测试文件写入中文，TEST STRING TO FILE\n", "E:/UPLOAD/loan/20150324/800075500010003_20150324_FileReplaceRepayment_8000755000100031427161192113.txt",true);
            System.out.println(new OutputStreamUtil().file2String("F:/c.txt", null));  
            System.out.println(new OutputStreamUtil().file2String("F:/c.txt", "UTF-8")); 
            List<String> result = OutputStreamUtil.file2Strings("F:/c.txt", "UTF-8");
            for(String rs : result){
                System.out.println("====================:"+rs);
            }
        }
        catch (Throwable e)
        {
            e.printStackTrace();
        }  
       
    }  
}
