/*
 * 文 件 名:  WxCallback.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  xiaoqi
 * 修改时间:  2016年5月23日
 */
package com.dimeng.p2p.user.servlets.wechat;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.http.servlet.Controller;
import com.dimeng.framework.http.session.Session;
import com.dimeng.framework.http.session.authentication.AuthenticationException;
import com.dimeng.framework.http.session.authentication.PasswordAuthentication;
import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.S61.entities.T6110;
import com.dimeng.p2p.S63.enums.T6340_F03;
import com.dimeng.p2p.S63.enums.T6340_F04;
import com.dimeng.p2p.account.user.service.UserInfoManage;
import com.dimeng.p2p.account.user.service.UserThirdPartyLoginManage;
import com.dimeng.p2p.account.user.service.entity.ThirdUser;
import com.dimeng.p2p.common.entities.ThirdPartyUser;
import com.dimeng.p2p.service.ActivityCommon;
import com.dimeng.p2p.user.servlets.AbstractUserServlet;
import com.dimeng.p2p.user.servlets.Login;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.p2p.variables.defines.URLVariable;
import com.dimeng.util.StringHelper;

/**
* <微信二维码登录回调处理>
* @author  xiaoqi
* @version  [版本号, 2016年5月23日]
*/

public class WxCallback extends AbstractUserServlet
{
    /**
    * 注释内容
    */
    
    private static final long serialVersionUID = 1L;
    
    @Override
    protected boolean mustAuthenticated()
    {
        return false;
    }
    
    @Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        ConfigureProvider configureProvider = getResourceProvider().getResource(ConfigureProvider.class);
        PasswordAuthentication authentication = new PasswordAuthentication();
        authentication.setVerifyCodeType("LOGIN");
        Controller controller = getController();
        String state = request.getParameter("state");
        Session session = serviceSession.getSession();
        if (!serviceSession.getSession().getToken().replaceAll("-", "").equals(state))
        {
            getController().prompt(request, response, PromptLevel.ERROR, "操作失败");
            sendRedirect(request, response, getController().getURI(request, Login.class));
            return;
        }
        try
        {
            String code = request.getParameter("code");
            // 如果不为空
            if (!StringHelper.isEmpty(code))
            {
                String appid = configureProvider.getProperty(SystemVariable.WX_APPID);
                String secret = configureProvider.getProperty(SystemVariable.WX_SECRET);
                // 获取token和openid
                Map<String, String> map = WxLoginHelper.getWxTokenAndOpenid(appid, secret, code);
                String openId = map.get("openId");
                // 如果openID存在
                if (!StringHelper.isEmpty(openId))
                {
                    // 获取第三方用户信息存放到session中
                    ThirdPartyUser thirdPartyUser = WxLoginHelper.getWxUserinfo(map.get("access_token"), openId);
                    // 跳转到登录成功界面
                    UserThirdPartyLoginManage userThirdPartyLoginManage =
                        serviceSession.getService(UserThirdPartyLoginManage.class);
                    ThirdUser thirdUser = userThirdPartyLoginManage.getUserInfoByThirdID(openId);
                    if (null == thirdUser)
                    {
                        //首次微信登录 --注册信息
                        session.setAttribute("openId", openId);
                        session.setAttribute("loginType", "weixin");
                        session.setAttribute("nickName", thirdPartyUser.nickName);
                        session.setAttribute("headImgUrl", thirdPartyUser.headImgUrl);
                        sendRedirect(request, response, configureProvider.format(URLVariable.ASSOCIATED_REGISTER));
                        return;
                    }
                    /*设置登录信息*/
                    authentication.setAccountName(thirdUser.F02);
                    authentication.setPassword(thirdUser.F10);
                    authentication.setVerifyCode(session.getVerifyCode("LOGIN"));
                    //更新登录时间
                    userThirdPartyLoginManage.updateUserLoginTime(openId, "wx");
                    
                    UserInfoManage uManage1 = serviceSession.getService(UserInfoManage.class);
                    try
                    {
                        session.invalidate(request, response);
                        session.checkIn(request, response, authentication);
                        T6110 user1 = uManage1.getUserInfo(session.getAccountId());
                        //生日登录送红包和加息券
                        ActivityCommon activityCommon = serviceSession.getService(ActivityCommon.class);
                        activityCommon.sendActivity(user1.F01, T6340_F03.redpacket.name(), T6340_F04.birthday.name());
                        activityCommon.sendActivity(user1.F01, T6340_F03.interest.name(), T6340_F04.birthday.name());
                        
                        //更新累计登陆次数和最后登陆时间
                        uManage1.udpateT6198F05F07(user1.F01);
                    }
                    catch (AuthenticationException e)
                    {
                        getResourceProvider().log(e.getMessage());
                        controller.prompt(request, response, PromptLevel.ERROR, e.getMessage());
                        sendRedirect(request, response, controller.getViewURI(request, Login.class));
                        return;
                    }
                    /*调整用户中心首页*/
                    sendRedirect(request, response, configureProvider.format(URLVariable.USER_INDEX));
                }
                else
                {
                    controller.prompt(request, response, PromptLevel.ERROR, "操作失败");
                    // 如果未获取到OpenID,跳转到登录页面
                    sendRedirect(request, response, controller.getURI(request, Login.class));
                }
            }
            else
            {
                controller.prompt(request, response, PromptLevel.ERROR, "操作失败");
                // 如果没有返回令牌，则直接返回到登录页面
                sendRedirect(request, response, controller.getURI(request, Login.class));
            }
        }
        catch (Exception e)
        {
            logger.info(e);
            controller.prompt(request, response, PromptLevel.ERROR, "操作失败");
            sendRedirect(request, response, controller.getURI(request, Login.class));
        }
    }
    
    @Override
    protected void processGet(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        this.processPost(request, response, serviceSession);
    }
    
}
