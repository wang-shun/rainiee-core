package com.dimeng.p2p.escrow.fuyou.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;

public class HttpClientHandler
{
    private static Log logger = LogFactory.getLog(HttpClientHandler.class);
    
    /**
     * MAP类型数组转换成NameValuePair类型
     */
    public static List<NameValuePair> buildNameValuePair(Map<String, String> params)
    {
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        if (params != null)
        {
            for (Map.Entry<String, String> entry : params.entrySet())
            {
                nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
        }
        
        return nvps;
    }
    
    /**
     * 发送请求
     *
     * @param params
     * @param actionUrl
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static String doPost(Map<String, String> params, String actionUrl)
        throws IOException
    {
        logger.info("httpclient请求信息：" + new Gson().toJson(params));
        logger.info("httpclient请求地址：" + actionUrl);
        String result = null;
        List<NameValuePair> nvps = HttpClientHandler.buildNameValuePair(params);
        CloseableHttpClient httpclient = HttpClients.createDefault();
        UrlEncodedFormEntity uefEntity;
        try
        {
            uefEntity = new UrlEncodedFormEntity(nvps, "UTF-8");
            HttpPost httpPost = new HttpPost(actionUrl);
            httpPost.setEntity(uefEntity);
            CloseableHttpResponse response = httpclient.execute(httpPost);
            
            try
            {
                HttpEntity entity = response.getEntity();
                if (response.getStatusLine().getReasonPhrase().equals("OK")
                    && response.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
                {
                    result = EntityUtils.toString(entity, "UTF-8");
                }
                EntityUtils.consume(entity);
            }
            catch (Exception e)
            {
                logger.error("httpclient返回信息：", e);
            }
            finally
            {
                httpPost.releaseConnection();
                response.close();
            }
        }
        finally
        {
            httpclient.close();
        }
        logger.info("httpclient返回信息：" + result);
        return result;
        
    }
}