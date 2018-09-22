/*
 * 文 件 名:  HttpUtil.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  xiaoqi
 * 修改时间:  2016年5月23日
 */
package com.dimeng.p2p.user.servlets.wechat;

import java.io.IOException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

/**
 * <Http请求工具类>
 * 
 * @author  xiaoqi
 * @version  [版本号, 2016年5月23日]
 */
public class HttpUtil
{
    private static final Logger logger = Logger.getLogger(HttpUtil.class);
    
    /**
     * 模拟http GET请求
     * <功能详细描述>
     * @param url
     * @return
     */
    public static final String httpClientGet(String url)
    {
        String result = null;
        try (CloseableHttpClient httpClient = HttpClients.createDefault())
        {
            HttpGet httpGet = new HttpGet(url);
            logger.debug(httpGet.getURI());
            // 执行get请求.    
            try (CloseableHttpResponse response = httpClient.execute(httpGet))
            {
                // 获取响应实体    
                HttpEntity entity = response.getEntity();
                if (entity != null)
                {
                    result = EntityUtils.toString(entity);
                    logger.debug(result);
                }
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return result;
    }
    
    /**
     * 模拟http POST请求
     * <功能详细描述>
     * @param url
     * @param list
     * @return
     */
    public static final String httpClientPost(String url, List<NameValuePair> list)
    {
        String result = null;
        try (CloseableHttpClient httpClient = HttpClients.createDefault())
        {
            HttpPost httpPost = new HttpPost(url);
            UrlEncodedFormEntity uefEntity = new UrlEncodedFormEntity(list, "UTF-8");
            httpPost.setEntity(uefEntity);
            try (CloseableHttpResponse response = httpClient.execute(httpPost))
            {
                HttpEntity entity = response.getEntity();
                if (entity != null)
                {
                    result = EntityUtils.toString(entity, "UTF-8");
                }
            }
        }
        catch (IOException e)
        {
            logger.error(e);
        }
        return result;
    }
    
    public static void main(String[] args)
    {
        String url = "http://www.baidu.com";
        System.out.println(HttpUtil.httpClientGet(url));
    }
}
