/*
 * 文 件 名:  BindCallBackServlet.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  zhoulantao
 * 修改时间:  2016年4月12日
 */
package com.dimeng.p2p.app.servlets.platinfo;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.http.servlet.Controller;
import com.dimeng.framework.http.session.Session;
import com.dimeng.framework.http.session.SessionManager;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.app.service.AppWeixinManage;
import com.dimeng.p2p.app.servlets.AbstractAppServlet;
import com.dimeng.p2p.app.servlets.platinfo.domain.OAuthInfo;
import com.dimeng.p2p.app.servlets.user.Account;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.p2p.weixin.util.HttpCore;
import com.dimeng.util.StringHelper;
import com.google.gson.JsonObject;

/**
 * 绑定微信公众号回调
 * 
 * @author  zhoulantao
 * @version  [版本号, 2016年4月12日]
 */
public class BindCallBackServlet extends AbstractAppServlet
{
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 3921067314376812606L;
    
    @Override
    protected void processPost(final HttpServletRequest request, HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        // 获取微信公众号回调的code
        final String code = getParameter(request, "code");
        
        // 鉴权成功的话，根据code查询openId
        if (!StringHelper.isEmpty(code))
        {
            // 获取鉴权信息
            final OAuthInfo oAuthInfo = getOAuthOpenId(code);
            
            final String openId = oAuthInfo.getOpenId();
            
            // 获取用户微信账号信息
//            final String accessToken = oAuthInfo.getAccessToken();
//            
//            getOAuthUserInfo(openId, accessToken);
            
            final Controller controller = getController();
            
            // 判断此openId是否已经在平台绑定过账号
            final AppWeixinManage appWeixinManage = serviceSession.getService(AppWeixinManage.class);
            final int accountId = appWeixinManage.searchAppWeixin(openId);
            
            if (accountId > 0)
            {
                // 说明这个用户已经绑定过平台账号了，判断是否已经登录。没登录则跳转到第三方登录操作
                final Session session =
                    getResourceProvider().getResource(SessionManager.class).getSession(request, response);
                
                // 判断session中登录标识
                if (session != null && session.isAuthenticated())
                {
                    // 跳转到用户信息页面
                    controller.forward(request, response, controller.getURI(request, Account.class));
                }
                else
                {
                    // 未登录则跳转到登录页面
                    request.setAttribute("accountId", accountId);
                    request.setAttribute("openId", openId);
                    
                    this.forwardController(request, response, WXAccountLogin.class);
                }
            }
            else
            {
                // 返回到微信关联页面
                setReturnMsg(request, response, ExceptionCode.WEIXIN_NO_REGISTER, "微信账号未绑定平台账号！", openId);
                return;
            }
        }
    }
    
    @Override
    protected void processGet(final HttpServletRequest request, HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        processPost(request, response, serviceSession); 
    }
    
    /**
     * 根据授权返回的code查询用户的鉴权信息
     * 
     * @param code 授权返回的code
     * @return 用户的鉴权信息
     * @throws IOException 异常信息
     */
    private OAuthInfo getOAuthOpenId(final String code)
        throws IOException
    {
        // 获取微信公众号的APPID和秘钥
        final ConfigureProvider configureProvider = getConfigureProvider();
        final String appId = configureProvider.format(SystemVariable.WEIXIN_APP_ID);
        final String secret = configureProvider.format(SystemVariable.WEIXIN_APP_SECRET);
        
        // 封装请求的URL
        String access_token_url =
            "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
        access_token_url = access_token_url.replace("APPID", appId).replace("SECRET", secret).replace("CODE", code);
        
        // 发送请求消息
        JsonObject jsonObject = HttpCore.getJSON(access_token_url);
        
        OAuthInfo oAuthInfo = null;
        // 如果请求成功
        if (null != jsonObject)
        {
            oAuthInfo = new OAuthInfo();
            oAuthInfo.setAccessToken(jsonObject.get("access_token").getAsString());
            oAuthInfo.setExpiresIn(jsonObject.get("expires_in").getAsInt());
            oAuthInfo.setRefreshToken(jsonObject.get("refresh_token").getAsString());
            oAuthInfo.setOpenId(jsonObject.get("openid").getAsString());
            oAuthInfo.setScope(jsonObject.get("scope").getAsString());
        }
        
        return oAuthInfo;
    }
    
    /**
     * 根据授权返回的code查询用户的鉴权信息
     * 
     * @param code 授权返回的code
     * @return 用户的鉴权信息
     * @throws IOException 异常信息
     */
    private void getOAuthUserInfo(final String openId, final String accessToken)
        throws IOException
    {
        // 封装请求的URL
        String access_token_url =
            "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";
        access_token_url = access_token_url.replace("ACCESS_TOKEN", accessToken).replace("OPENID", openId);
        
        // 发送请求消息
        JsonObject jsonObject = HttpCore.getJSON(access_token_url);
        
        // 如果请求成功
        StringBuffer sb = new StringBuffer();
        if (null != jsonObject)
        {
            sb.append("nickname = " + jsonObject.get("nickname").getAsString());
            sb.append("sex = " + jsonObject.get("sex").getAsString());
            sb.append("headimgurl = " + jsonObject.get("headimgurl").getAsString());
        }
        
        System.out.println("str = " + sb.toString());
    }
    
}
