/*
 * 文 件 名:  GetMsgServlet.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  zhoulantao
 * 修改时间:  2016年4月9日
 */
package com.dimeng.p2p.app.servlets.weixin;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.app.servlets.platinfo.ExceptionCode;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.p2p.weixin.config.Config;
import com.dimeng.p2p.weixin.util.HttpCore;
import com.dimeng.p2p.weixin.util.MySecurity;
import com.dimeng.p2p.weixin.util.SignUtil;
import com.google.gson.JsonObject;

/**
 * 微信验证信息处理接口
 * 
 * @author  zhoulantao
 * @version  [版本号, 2016年4月9日]
 */
public class GetMsgServlet extends AbstractWeiXinServlet
{
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 7919599751292164402L;
    
    private final static String CONTEXT_KEY_TOKEN = "_weixin_token";
    
    private final static String CONTEXT_KEY_JSTICKET = "_weixin_js_ticket";
    
    private final static String CONTEXT_WEIXIN = "_weixin_cache";
    
    /**
     * 处理微信服务器验证
     */
    @Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        processGet(request, response, serviceSession);
    }
    
    /**
     * 处理微信服务器验证
     */
    @Override
    protected void processGet(final HttpServletRequest request, HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        // 获取回调函数
        final String func = request.getParameter("func");
        
        if (StringUtils.isNotEmpty(func))
        {
            // JS JDK鉴权
            onGetJsTicket(request, response);
        }
        else
        {
            PrintWriter out = response.getWriter();
            try
            {
                // 验证签名
                final String checkValue = onInterfaceValidata(request, serviceSession);
                
                if (!"error".equals(checkValue))
                {
                    out.print(checkValue);
                }
                
                out.close();
            }
            catch (Exception e)
            {
                logger.error("验证微信服务器签名失败", e);
            }
            finally
            {
                if (null != out)
                {
                    out.close();
                }
            }
        }
    }
    
    /**
     * 校验微信签名
     * 
     * @param request 请求消息
     * @param serviceSession 消息上下文
     * @return 是否校验成功
     * @throws IOException 异常消息
     */
    private String onInterfaceValidata(final HttpServletRequest request, final ServiceSession serviceSession)
        throws IOException
    {
        // 获取微信公众号的TOKEN
        final ConfigureProvider configureProvider = getConfigureProvider();
        final String serviceToken = configureProvider.format(SystemVariable.WEIXIN_API_SERVICE_TOKEN);
        
        // 微信加密签名
        String signature = request.getParameter("signature");
        // 时间戳
        String timestamp = request.getParameter("timestamp");
        // 随机数
        String nonce = request.getParameter("nonce");
        // 随机字符串
        String echostr = request.getParameter("echostr");
        
        // 通过检验signature对请求进行校验，若校验成功则原样返回echostr，表示接入成功，否则接入失败
        if (SignUtil.checkSignature(serviceToken, signature, timestamp, nonce))
        {
            return echostr;
        }
        else
        {
            return "error";
        }
    }
    
    /**
     * 获取JS SDK鉴权
     * 
     * @param request 请求消息
     * @param response 响应消息
     * @throws IOException 异常消息
     */
    private void onGetJsTicket(final HttpServletRequest request, HttpServletResponse response)
        throws IOException
    {
        // 获取微信公众号的APPID和秘钥
        final ConfigureProvider configureProvider = getConfigureProvider();
        final String appId = configureProvider.format(SystemVariable.WEIXIN_APP_ID);
        final String secret = configureProvider.format(SystemVariable.WEIXIN_APP_SECRET);
        
        final int urlHash = request.getParameter("url").hashCode();
        final JsonObject tokenInfo = getToken(urlHash, request, appId, secret);
        final JsonObject jsticketInfo = getJsTicket(tokenInfo, urlHash, appId, request);
        
        // 返回页面响应
        setReturnMsg(request, response, ExceptionCode.SUCCESS, "success", jsticketInfo);
    }
    
    /**
     * 获取access_token
     * 
     * @param urlHash 请求URLcash值
     * @param request 请求消息
     * @param appId 微信公众号appId
     * @param secret 微信公众号
     * @return access_token
     */
    private JsonObject getToken(final int urlHash, final HttpServletRequest request, final String appId,
        final String secret)
    {
        JsonObject tokenInfo = this.getCache(CONTEXT_KEY_TOKEN + urlHash, request);
        if (tokenInfo == null || tokenInfo.get("expiresTime").getAsLong() < new Date().getTime())
        {
            long _curentTime = new Date().getTime();
            tokenInfo = HttpCore.getJSON(Config.getWeixinAccessTokenUrl(appId, secret));
            long expiresIn = tokenInfo.get("expires_in").getAsLong();
            tokenInfo.addProperty("timestamp", _curentTime);
            tokenInfo.addProperty("expiresTime", (_curentTime + expiresIn * 1000));
            this.syncCache(CONTEXT_KEY_TOKEN + urlHash, tokenInfo, request);
        }
        return tokenInfo;
    }
    
    /**
     * 获取JS的鉴权信息
     * 
     * @param tokenInfo access_token
     * @param urlHash 请求URLcash值
     * @param appId 微信公众号appId
     * @param request 请求消息
     * @return JS的鉴权信息
     */
    private JsonObject getJsTicket(final JsonObject tokenInfo, final int urlHash, final String appId,
        final HttpServletRequest request)
    {
        JsonObject ticketInfo = getCache(CONTEXT_KEY_JSTICKET + urlHash, request);
        String token = tokenInfo.get("access_token").getAsString();
        if (ticketInfo == null || ticketInfo.get("expiresTime").getAsLong() < new Date().getTime())
        {
            long _curentTime = new Date().getTime();
            ticketInfo = HttpCore.getJSON(Config.WEIXIN_ACCESS_JSTICKET_URL + token);
            long expiresIn = ticketInfo.get("expires_in").getAsLong();
            ticketInfo.addProperty("timestamp", _curentTime);
            ticketInfo.addProperty("expiresTime", (_curentTime + expiresIn * 1000));
            ticketInfo.addProperty("noncestr", Config.JSAPI_NONCESTR);
            ticketInfo.addProperty("url", request.getParameter("url"));
            ticketInfo.addProperty("appId", appId);
            buildJsSignature(ticketInfo);
            syncCache(CONTEXT_KEY_JSTICKET + urlHash, ticketInfo, request);
        }
        return ticketInfo;
    }
    
    /**
     * 生成鉴权消息中的加密串
     * 
     * @param ticketInfo
     */
    private void buildJsSignature(JsonObject ticketInfo)
    {
        StringBuffer encryptBase = new StringBuffer();
        encryptBase.append("jsapi_ticket=" + ticketInfo.get("ticket").getAsString());
        encryptBase.append("&noncestr=" + ticketInfo.get("noncestr").getAsString());
        encryptBase.append("&timestamp=" + ticketInfo.get("timestamp").getAsLong());
        encryptBase.append("&url=" + ticketInfo.get("url").getAsString());
        String encryptResult = MySecurity.encode(encryptBase.toString());
        ticketInfo.addProperty("signature", encryptResult);
    }
    
    /**
     * 缓存数据
     * 
     * @param type
     * @param ins
     * @param request
     */
    @SuppressWarnings("unchecked")
    private void syncCache(final String type, final JsonObject ins, final HttpServletRequest request)
    {
        Map<String, JsonObject> cache =
            (Map<String, JsonObject>)request.getServletContext().getAttribute(CONTEXT_WEIXIN);
        if (cache == null)
        {
            cache = new HashMap<String, JsonObject>();
        }
        cache.put(type, ins);
    }
    
    /**
     * 获取缓存数据
     * 
     * @param type
     * @param request
     * @return
     */
    @SuppressWarnings("unchecked")
    private JsonObject getCache(final String type, final HttpServletRequest request)
    {
        Map<String, JsonObject> cache =
            (Map<String, JsonObject>)request.getServletContext().getAttribute(CONTEXT_WEIXIN);
        if (cache == null)
        {
            cache = new HashMap<String, JsonObject>();
        }
        return cache.get(type);
    }
    
}
