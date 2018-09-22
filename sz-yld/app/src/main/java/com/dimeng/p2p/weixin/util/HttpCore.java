package com.dimeng.p2p.weixin.util;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.Logger;

import com.google.gson.JsonObject;

/**
 * 
 * 发送http请求工具类
 * 
 * @author  zhoulantao
 * @version  [版本号, 2016年4月11日]
 */
public abstract class HttpCore
{
    private static Logger logger = Logger.getLogger(HttpCore.class);
    
    /**
     * 发送请求，获取响应的json消息
     * 
     * @param url 待发送的请求消息
     * @return 响应的JSON消息
     */
    public static JsonObject getJSON(String url)
    {
        // 创建HttpClient实例  
        final CloseableHttpClient httpclient = HttpClients.createDefault();
        
        // 创建Get方法实例     
        final HttpGet httpgets = new HttpGet(url);
        
        JsonObject jsonObj = null;
        InputStream instreams = null;
        try
        {
            // 执行请求消息
            final HttpResponse response = httpclient.execute(httpgets);
            
            // 获取响应消息的消息实体
            final HttpEntity entity = response.getEntity();
            
            // 解析消息实体
            if (entity != null)
            {
                instreams = entity.getContent();
                jsonObj = JSONUtils.parseJsonByStream(instreams);
                instreams.close();
                return jsonObj;
            }
        }
        catch (IllegalStateException | IOException e)
        {
            logger.error("请求" + url + "时，获取响应消息失败", e);
        }
        finally
        {
            if (null != instreams)
            {
                try
                {
                    instreams.close();
                }
                catch (IOException e)
                {
                    logger.error("关闭输出流异常", e);
                }
            }
            
            if (httpgets != null && !httpgets.isAborted())
            {
                httpgets.abort();
            }
        }
        
        return jsonObj;
    }
}
