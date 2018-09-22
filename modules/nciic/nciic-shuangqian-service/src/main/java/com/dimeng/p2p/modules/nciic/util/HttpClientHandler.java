package com.dimeng.p2p.modules.nciic.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class HttpClientHandler
{
    
    /**
     * MAP类型数组转换成NameValuePair类型
     * 
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
     * @param params
     * @param actionUrl
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static String doPost(Map<String, String> params, String actionUrl)
        throws ClientProtocolException, IOException
    {
        
        String result = null;
        List<NameValuePair> nvps = HttpClientHandler.buildNameValuePair(params);
        CloseableHttpClient httpclient = HttpClients.createDefault();
        EntityBuilder builder = EntityBuilder.create();
        try
        {
            HttpPost httpPost = new HttpPost(actionUrl);
            builder.setParameters(nvps);
            httpPost.setEntity(builder.build());
            CloseableHttpResponse response = httpclient.execute(httpPost);
            
            try
            {
                HttpEntity entity = response.getEntity();
                if (response.getStatusLine().getReasonPhrase().equals("OK")
                    && response.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
                {
                    result = EntityUtils.toString(entity, "utf-8");
                }
                EntityUtils.consume(entity);
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
        //		System.out.println("dopost:" + Common.UrlDecoder(result, charSet));
        return result;
    }
    
}
