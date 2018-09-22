/**
* 版权所有：深圳法大大网络科技有限公司
* Copyright 2015 fadada.com Inc.
* All right reserved. 
*====================================================
* 文件名称: FddClient.java
* 修订记录：
* No    日期				作者(操作:具体内容)
* 1.    Dec 18, 2015			Mocuishle(创建:创建文件)
*====================================================
* 类描述：(说明未实现或其它不应生成javadoc的内容)
* 
*/
package com.dimeng.p2p.signature.fdd.utils;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.p2p.signature.fdd.variables.FddVariable;

/**
 * <h3>概要:</h3> 
 *    法大大接口调用
 * <br>
 * <h3>功能:</h3>
 * <ol>
 * 		<li>个人注册免审核</li>
 * 		<li>上传合同</li>
 * 		<li>手动签章</li>
 * 		<li>自动签章</li>
 * 		<li>合同归档</li>
 * 		<li>修改用户信息</li>
 * </ol>
 * <h3>履历:</h3>
 * <ol>
 * 		<li>2015年12月17日[zhouxw] 新建</li>
 * </ol>
 */
public class FddClient {
	
	private static final Logger logger = Logger.getLogger(FddClient.class);
	
	protected static ConfigureProvider configureProvider;
    
    public void init(ConfigureProvider configureProvider)
    {
    	FddClient.configureProvider = configureProvider;
    }
	
	/**
	 * <b>概要：</b>
	 * 个人注册免审核
	 * <b>作者：</b>zhouxw </br>
	 * <b>日期：</b>2015年12月17日 </br>
	 * @param customer_name 名称
	 * @param email 邮箱
	 * @param id_card 身份证号码
	 * @param mobile 手机号码
	 * @return 接口处理结果
	 */
	public static String invokeSyncPersonAuto(String customer_name,String email,String id_card,String mobile){
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		try {
			String timeStamp = getTimeStamp();
			String msgDigest = "";
			//Base64(SHA1(appid+md5(timestamp)+SHA1(appsecret)))
			String sha1 = FddEncryptTool.sha1(getFddAppID()+FddEncryptTool.md5Digest(timeStamp)+FddEncryptTool.sha1(getFddScriet()));
			msgDigest = new String(FddEncryptTool.Base64Encode(sha1.getBytes()));

			params.add(new BasicNameValuePair("app_id", getFddAppID()));
			params.add(new BasicNameValuePair("timestamp", timeStamp));
			params.add(new BasicNameValuePair("v", getFddV()));
			params.add(new BasicNameValuePair("customer_name", customer_name));
			params.add(new BasicNameValuePair("email", email));
			String id_mobile = FddEncryptTool.encrypt(id_card + "|" + mobile, getFddScriet());
			params.add(new BasicNameValuePair("id_mobile", id_mobile));
			params.add(new BasicNameValuePair("msg_digest", msgDigest));
			return doPost(getFddApiUrl()+getFddUrlSyncpersonAuto(), params, charset);
		} catch (Exception e) {
			logger.error("个人注册免审核失败：" + e);
		}
		return null;
	}

	
	/**
	 * <b>概要：</b>
	 * 上传合同
	 * <b>作者：</b>zhouxw </br>
	 * <b>日期：</b>2015年12月17日 </br>
	 * @param contract_id 合同编号
	 * @param doc_title 合同标题
	 * @param file 合同文件,与doc_url两个只传一个
	 * @param doc_url 合同文件url地址
	 * @param docType 合同类型：.html .doc .docx .pdf ……
	 * @return 接口处理结果
	 */
	public static String invokeUploadDocs(String contract_id,String doc_title,File file,String doc_url,String docType){
		String result = "";
		try {
			String timeStamp = getTimeStamp();
			String msgDigest = "";
			//Base64(SHA1(app_id+md5(timestamp)+SHA1(app_secret+  contract_id )))
			String sha1 = FddEncryptTool.sha1(getFddAppID()+FddEncryptTool.md5Digest(timeStamp)+FddEncryptTool.sha1(getFddScriet()+contract_id));
			msgDigest = new String(FddEncryptTool.Base64Encode(sha1.getBytes()));
			
			HttpClient httpclient = new DefaultHttpClient();  
	        HttpPost httppost = new HttpPost(getFddApiUrl()+getFddUrlUploaddocs());  
	        MultipartEntity reqEntity = new MultipartEntity();  //对请求的表单域进行填充  
	        if(file!=null){
	        	FileBody fileBody = new FileBody(file); //创建待处理的文件  
	        	reqEntity.addPart("file", fileBody);  
	        }
	        reqEntity.addPart("doc_url", new StringBody(doc_url));
	        reqEntity.addPart("contract_id", new StringBody(contract_id));  
	        reqEntity.addPart("doc_title", new StringBody(doc_title));  
	        reqEntity.addPart("doc_type", new StringBody(docType)); 
	        
	        reqEntity.addPart("app_id", new StringBody(getFddAppID()));  //创建待处理的表单域内容文本  
	        reqEntity.addPart("v", new StringBody(getFddV()));  
	        reqEntity.addPart("timestamp", new StringBody(timeStamp));  
	        reqEntity.addPart("msg_digest", new StringBody(msgDigest));
	        httppost.setEntity(reqEntity);  
	        HttpResponse response = httpclient.execute(httppost);
	        if(HttpStatus.SC_OK==response.getStatusLine().getStatusCode()){  
	            HttpEntity entity = response.getEntity();  
	            //显示内容  
	            if (entity != null) {  
	            	result = EntityUtils.toString(entity);
	            }  
	        }
		} catch (Exception e) {
			logger.error("上传合同失败：" + e);
		}
		return result;
	}
	
    /**
     * 创建form表单公共方法
     * <功能详细描述>
     * @param param 参数map
     * @param formUrl   form表单action提交地址
     * @return  form表单
     * @throws Throwable
     */
    public static String createSubmitForm(Map<String, String> param, String formUrl)
        throws Throwable
    {
        StringBuilder builder = new StringBuilder();
        builder.append("<form action='");
        builder.append(formUrl);
        builder.append("' method=\"post\">");
        for (String key : param.keySet())
        {
            builder.append("<input type=\"hidden\" name=\"");
            builder.append(key);
            builder.append("\" value='");
            builder.append(param.get(key));
            builder.append("' />");
        }
        builder.append("</form>");
        builder.append("<script type=\"text/javascript\">");
        builder.append("document.forms[0].submit();");
        builder.append("</script>");
        return builder.toString();
    }


    public static void doPrintWriter(HttpServletResponse response, String location, boolean printTag)
        throws Throwable
    {
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        PrintWriter writer = response.getWriter();
        if (printTag)
        {
            logger.info(String.format("发送请求参数：%s", location));
        }
        try
        {
            writer.print(location);
        }
        catch (Exception e)
        {
            logger.error(e.toString());
        }
        finally
        {
            writer.flush();
            writer.close();
        }
    }

	/**
	 * <b>概要：</b>
	 * 手动签章
	 * <b>作者：</b>zhouxw </br>
	 * <b>日期：</b>2015年12月30日 </br>
	 * @param transaction_id 交易号，长度小于等于32位
	 * @param contract_id 合同编号
	 * @param return_url 跳转地址
	 * @param client_type 客户类型：1-个人，2-企业
	 * @param customer_id 客户编号
	 * @param doc_title 文档标题
	 * @param notify_url 异步通知地址
	 * @param sign_keyword 签章关键字
	 * @return 返回拼接好的地址，请重定向到该地址
	 */
    public static String invokeExtSign(String requsetType, String transaction_id, String contract_id,
        String return_url, String client_type, String customer_id, String doc_title, String notify_url,
        String sign_keyword)
    {
		String timeStamp = getTimeStamp();
		try {
			StringBuffer sb = new StringBuffer(getFddApiUrl()+getFddUrlExtsign());
			String msgDigest = "";
			//Base64(SHA1(app_id+md5(transaction_id+timestamp)+SHA1(app_secret+ customer_id +doc_url)))
			String sha1 = FddEncryptTool.sha1(getFddAppID()+FddEncryptTool.md5Digest(transaction_id+timeStamp)+FddEncryptTool.sha1(getFddScriet()+customer_id+""));
			msgDigest = new String(FddEncryptTool.Base64Encode(sha1.getBytes()));
            
            if ("post".equalsIgnoreCase(requsetType))
            {
                Map<String, String> req = new LinkedHashMap<String, String>();
                req.put("timestamp", timeStamp);
                req.put("app_id", getFddAppID());
                req.put("v", getFddV());
                req.put("msg_digest", msgDigest);
                req.put("transaction_id", transaction_id);
                req.put("contract_id", contract_id);
                req.put("client_type", client_type);
                req.put("customer_id", customer_id);
                req.put("doc_title", URLEncoder.encode(doc_title, charset));
                req.put("sign_keyword", URLEncoder.encode(sign_keyword, charset));
                req.put("return_url", return_url);
                req.put("notify_url", notify_url);
                String location = createSubmitForm(req, sb.toString());
                return location;
            }
            else
            {
                sb.append("?timestamp=").append(timeStamp);
                sb.append("&app_id=").append(getFddAppID());
                sb.append("&v=").append(getFddV());
                sb.append("&msg_digest=").append(msgDigest);
                sb.append("&transaction_id=").append(transaction_id);
                sb.append("&contract_id=").append(contract_id);
                sb.append("&client_type=").append(client_type);
                sb.append("&customer_id=").append(customer_id);
                sb.append("&doc_title=").append(URLEncoder.encode(doc_title, charset));
                sb.append("&sign_keyword=").append(URLEncoder.encode(sign_keyword, charset));
                sb.append("&return_url=").append(URLEncoder.encode(return_url, charset));
                sb.append("&notify_url=").append(URLEncoder.encode(notify_url, charset));
                return sb.toString();
            }
            
		} catch (UnsupportedEncodingException e) {
			logger.error("手动签章失败：" + e);
		} catch (Exception e) {
			logger.error("手动签章失败：" + e);
        }catch (Throwable e) {
            logger.error("手动签章失败：" + e);
        }
		return null;
	}

	/**
	 * <b>概要：</b>
	 * 自动签署
	 * <b>作者：</b>zhouxw </br>
	 * <b>日期：</b>2015年12月30日 </br>
	 * @param transaction_id 交易号
	 * @param customer_id 客户编号
	 * @param batch_id 批次号（交易号）
	 * @param client_type 客户类型：1-个人，2-企业
	 * @param client_role 客户角色
	 * @param contract_id 合同编号
	 * @param doc_title 文档标题
	 * @param sign_keyword 签章关键字
	 * @param notify_url 异步通知地址
	 * @return 接口处理结果
	 */
	public static String invokeExtSignAuto(String transaction_id,String customer_id,String batch_id,String client_type,String client_role,String contract_id,String doc_title,String sign_keyword,String notify_url){
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		try {
			String timeStamp = getTimeStamp();
			String msgDigest = "";
			//Base64(SHA1(app_id+md5(transaction_id+timestamp) + SHA1(app_secret+ customer_id +doc_url)))
			String sha1 = FddEncryptTool.sha1(getFddAppID()+FddEncryptTool.md5Digest(transaction_id+timeStamp)+FddEncryptTool.sha1(getFddScriet()+customer_id+""));
			msgDigest = new String(FddEncryptTool.Base64Encode(sha1.getBytes()));

			params.add(new BasicNameValuePair("app_id", getFddAppID()));
			params.add(new BasicNameValuePair("timestamp", timeStamp));
			params.add(new BasicNameValuePair("v", getFddV()));
			params.add(new BasicNameValuePair("msg_digest", msgDigest));
			params.add(new BasicNameValuePair("transaction_id", transaction_id)); 
			params.add(new BasicNameValuePair("batch_id", batch_id));
			params.add(new BasicNameValuePair("contract_id", contract_id));
			params.add(new BasicNameValuePair("client_type", client_type));
			params.add(new BasicNameValuePair("client_role", client_role));
			params.add(new BasicNameValuePair("customer_id", customer_id));
			params.add(new BasicNameValuePair("doc_title", doc_title));
			params.add(new BasicNameValuePair("sign_keyword", sign_keyword));
			params.add(new BasicNameValuePair("notify_url", notify_url));
			return doPost(getFddApiUrl()+getFddUrlExtsignAuto(), params, charset);
		} catch (Exception e) {
			logger.error("自动签署失败：" + e);
		}
		return null;
	}
	
	
	/**
	 * <b>概要：</b>
	 * 合同归档
	 * <b>作者：</b>zhouxw </br>
	 * <b>日期：</b>2015年12月18日 </br>
	 * @param contract_id 合同编号
	 * @return 接口处理结果
	 */
	public static String invokeContractFilling(String contract_id){
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		try {
			String timeStamp = getTimeStamp();
			String msgDigest = "";
			//Base64(SHA1(app_id+md5(timestamp)+SHA1(app_secret+  contract_id )))
			String sha1 = FddEncryptTool.sha1(getFddAppID()+FddEncryptTool.md5Digest(timeStamp)+FddEncryptTool.sha1(getFddScriet()+contract_id));
			msgDigest = new String(FddEncryptTool.Base64Encode(sha1.getBytes()));
			params.add(new BasicNameValuePair("app_id", getFddAppID()));
			params.add(new BasicNameValuePair("timestamp", timeStamp));
			params.add(new BasicNameValuePair("v", getFddV()));
			params.add(new BasicNameValuePair("msg_digest", msgDigest));
			params.add(new BasicNameValuePair("contract_id", contract_id));
			return doPost(getFddApiUrl() + getFddUrlContractfiling(), params,
					charset);
		} catch (Exception e) {
			logger.error("合同归档失败：" + e);
		}
		return null;
		
	}
	
	/**
	 * <b>概要：</b>
	 * 签署状态查询接口
	 * <b>作者：</b>guomianyun </br>
	 * <b>日期：</b>2017年10月23日 </br>
	 * @param contract_id 合同编号
	 * * @param customer_id 客户编号
	 * @return 接口处理结果
	 */
	public static String invokeQueryStatus(String contract_id,String customer_id){
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		try {
			String timeStamp = getTimeStamp();
			String msgDigest = "";
			//Base64(SHA1(app_id+md5(timestamp)+SHA1(app_secret+  contract_id )))
			String sha1 = FddEncryptTool.sha1(getFddAppID()+FddEncryptTool.md5Digest(timeStamp)+FddEncryptTool.sha1(getFddScriet()+contract_id+customer_id));
			msgDigest = new String(FddEncryptTool.Base64Encode(sha1.getBytes()));
			params.add(new BasicNameValuePair("app_id", getFddAppID()));
			params.add(new BasicNameValuePair("timestamp", timeStamp));
			params.add(new BasicNameValuePair("v", getFddV()));
			params.add(new BasicNameValuePair("msg_digest", msgDigest));
			params.add(new BasicNameValuePair("contract_id", contract_id));
			params.add(new BasicNameValuePair("customer_id", customer_id));
			return doPost(getFddApiUrl() + getFddUrlSignStatus(), params,
					charset);
		} catch (Exception e) {
			logger.error("合同归档失败：" + e);
		}
		return null;
		
	}
	
	

    /**
     * <b>概要：</b>
     * 查看合同
     * <b>作者：</b>zhangxu </br>
     * @param contract_id 必选 合同编号
     * @return 接口处理结果
     */
    public static String invokeViewContract(String contract_id)
    {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        try
        {
            String timeStamp = getTimeStamp();
            String msgDigest = "";
            //Base64(SHA1(appid+md5(timestamp)+SHA1(appsecret)))
            String sha1 =
                FddEncryptTool.sha1(getFddAppID() + FddEncryptTool.md5Digest(timeStamp)
                    + FddEncryptTool.sha1(getFddScriet()));
            msgDigest = new String(FddEncryptTool.Base64Encode(sha1.getBytes()));
            
            params.add(new BasicNameValuePair("app_id", getFddAppID()));
            params.add(new BasicNameValuePair("timestamp", timeStamp));
            params.add(new BasicNameValuePair("v", getFddV()));
            params.add(new BasicNameValuePair("contract_id", contract_id));//合同编号
            params.add(new BasicNameValuePair("msg_digest", msgDigest));
            return doPost(getFddApiUrl() + getFddUrlSyncpersonAuto(), params, charset);
        }
        catch (Exception e)
        {
            logger.error("合同查看失败：" + e);
        }
        return null;
    }
    
    /**
     * <b>概要：</b>
     * 修改用户信息
     * <b>作者：</b>zhouxw </br>
     * <b>日期：</b>2015年12月23日 </br>
     * @param email 邮箱
     * @param mobile 手机号码11位
     * @param customer_id 客户编号
     * @return 接口处理结果
     */
    public static String invokeInfoChange(String email, String mobile, String customer_id)
    {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		try {
			String timeStamp = getTimeStamp();
			String msgDigest = "";
			//Base64(SHA1(app_id+md5(timestamp)+SHA1(app_secret )))
			String sha1 = FddEncryptTool.sha1(getFddAppID()+FddEncryptTool.md5Digest(timeStamp)+FddEncryptTool.sha1(getFddScriet()));
			msgDigest = new String(FddEncryptTool.Base64Encode(sha1.getBytes()));
			params.add(new BasicNameValuePair("app_id", getFddAppID()));
			params.add(new BasicNameValuePair("timestamp", timeStamp));
			params.add(new BasicNameValuePair("v", getFddV()));
			params.add(new BasicNameValuePair("msg_digest", msgDigest));
			params.add(new BasicNameValuePair("email", email));
			params.add(new BasicNameValuePair("mobile", mobile));
			params.add(new BasicNameValuePair("customer_id", customer_id));
			
            return doPost(getFddApiUrl() + getInfoChange(), params, charset);

		} catch (Exception e) {
            logger.error("客户信息修改失败：" + e);
		}
        return null;
    }
	
    /*===================================doPost==============================================*/
	private static String charset = "UTF-8";
	/**
	 * <b>概要：</b>
	 * post请求方法
	 * <b>作者：</b>zhouxw </br>
	 * <b>日期：</b>2015年12月17日 </br>
	 * @param url
	 * @param params
	 * @param charset
	 * @return 链接响应内容
	 */
	public static String doPost(String url,List<NameValuePair> params,String charset){
		HttpClient httpClient = null;
		HttpPost httpPost = null;
		String result = null;
		try{
			httpClient = new SSLClient();
			httpPost = new HttpPost(url);
			//设置参数
			if(null!=params && params.size() > 0){
				UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params,charset);
				httpPost.setEntity(entity);
			}
			
			logger.info("法大大请求路径：" + url + ",参数：" + params);
			
			HttpResponse response = httpClient.execute(httpPost);
			if(response != null){
				HttpEntity resEntity = response.getEntity();
				if(resEntity != null){
					result = EntityUtils.toString(resEntity,charset);
					logger.info("法大大请求返回信息："+result);
				}
			}
		}catch(Exception ex){
			logger.error("POST失败：" + ex);
		}
		return result;
	}

	/*===================================doPost==============================================*/

	/**
	 * <b>概要：</b>
	 * 获取当前时间戳
	 * <b>作者：</b>zhouxw </br>
	 * <b>日期：</b>2015年12月17日 </br>
	 * @return 当前时间：'yyyyMMddHHmmss'格式
	 */
	public static String getTimeStamp(){
		Timestamp ts = new Timestamp(System.currentTimeMillis());
		DateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		return sdf.format(ts);
	}
	
	public static String getFddAppID() throws IOException{
		return configureProvider.format(FddVariable.FDD_API_ID);
	}
	
	public static String getFddApiUrl() throws IOException{
		return configureProvider.format(FddVariable.FDD_API_URL);
	}
	
	public static String getFddV() throws IOException{
		return configureProvider.format(FddVariable.FDD_V);
	}
	
	public static String getFddScriet() throws IOException{
		return configureProvider.format(FddVariable.FDD_APP_SECRET);
	}
	
	public static String getFddUrlContractfiling() throws IOException{
		return configureProvider.format(FddVariable.FDD_URL_CONTRACTFILING);
	}
	
	public static String getFddUrlExtsignAuto() throws IOException{
		return configureProvider.format(FddVariable.FDD_URL_EXTSIGN_AUTO);
	}
	
	public static String getFddUrlExtsign() throws IOException{
		return configureProvider.format(FddVariable.FDD_URL_EXTSIGN);
	}
	
	public static String getFddUrlUploaddocs() throws IOException{
		return configureProvider.format(FddVariable.FDD_URL_UPLOADDOCS);
	}
	
	public static String getFddUrlSignStatus() throws IOException{
		return configureProvider.format(FddVariable.FDD_URL_QUERY_SIGN_STATUS);
	}
	
	public static String getFddUrlSyncpersonAuto() throws IOException{
		return configureProvider.format(FddVariable.FDD_URL_SYNCPERSON_AUTO);
	}
	
	public static String getInfoChange() throws IOException{
        return configureProvider.format(FddVariable.FDD_URL_INFO_CHANGE);
    }
	
}
