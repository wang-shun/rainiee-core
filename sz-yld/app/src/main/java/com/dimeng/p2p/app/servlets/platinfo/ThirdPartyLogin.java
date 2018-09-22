/*
 * 文 件 名:  ThirdPartyLogin.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  zhoulantao
 * 修改时间:  2015年12月15日
 */
package com.dimeng.p2p.app.servlets.platinfo;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.servlet.Controller;
import com.dimeng.framework.http.session.Session;
import com.dimeng.framework.http.session.SessionManager;
import com.dimeng.framework.http.session.authentication.AuthenticationException;
import com.dimeng.framework.http.session.authentication.PasswordAuthentication;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.S61.entities.T6110;
import com.dimeng.p2p.S63.enums.T6340_F03;
import com.dimeng.p2p.S63.enums.T6340_F04;
import com.dimeng.p2p.account.user.service.UserInfoManage;
import com.dimeng.p2p.account.user.service.UserThirdPartyLoginManage;
import com.dimeng.p2p.account.user.service.entity.ThirdUser;
import com.dimeng.p2p.app.servlets.AbstractAppServlet;
import com.dimeng.p2p.app.servlets.user.Account;
import com.dimeng.p2p.service.ActivityCommon;
import com.dimeng.util.StringHelper;

/**
 * 第三方登录
 * 
 * @author  zhoulantao
 * @version  [版本号, 2015年12月15日]
 */
public class ThirdPartyLogin extends AbstractAppServlet
{
    
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 8703676504643751980L;
    
    @Override
    protected void processPost(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        // 获取第三方登录信息
        final String openId = getParameter(request, "openId");
        final String qqToken = getParameter(request, "accessToken");
        
        // 判断是否在平台注册，为注册则跳转到注册页面
        if (!StringHelper.isEmpty(openId))
        {
            // 根据第三方用户ID查询关联的平台注册账号
            UserThirdPartyLoginManage userThirdPartyLoginManage =
                serviceSession.getService(UserThirdPartyLoginManage.class);
            ThirdUser thirdUser = userThirdPartyLoginManage.getUserInfoByThirdID(openId);
            
            // 如果用户信息为空，则跳转到注册页面
            if (null == thirdUser)
            {
                Map<String, String> params = new HashMap<String, String>();
                params.put("openId", openId);
                params.put("qqToken", qqToken);
                
                // 返回页面跳转到平台注册页面
                setReturnMsg(request, response, ExceptionCode.NOLOGING_ERROR, "未绑定平台账号!", params);
                return;
            }
            
            // 用第三方绑定账号登录
            Session session =
                getResourceProvider().getResource(SessionManager.class).getSession(request, response, true);
            PasswordAuthentication authentication = new PasswordAuthentication();
            authentication.setAccountName(thirdUser.F02);
            authentication.setPassword(thirdUser.F10);
            //aaaaaaa.
            authentication.setVerifyCodeType("LOGIN");
            authentication.setVerifyCode(session.getVerifyCode("LOGIN"));
            
            //如果用户的accesstoken 已用3个月则更新accesstoken 90*24*60*60*1000 = 7776000000
            if (null == thirdUser.tokenTime || System.currentTimeMillis() - thirdUser.tokenTime > 7776000000L)
            {
                //更新accesstoken,tokentime和登陆时间
                userThirdPartyLoginManage.updateUserAccessTokenAndLoginTime(openId, qqToken);
            }
            else
            {
                //更新登录时间
                userThirdPartyLoginManage.updateUserLoginTime(openId);
            }
            
            try
            {
                // 执行登录操作
                session.invalidate(request, response);
                session.checkIn(request, response, authentication);
                
                // 查询用户信息
                UserInfoManage uManage1 = serviceSession.getService(UserInfoManage.class);
                T6110 user1 = uManage1.getUserInfo(session.getAccountId());
                
                //生日登录送红包和加息券
                ActivityCommon activityCommon = serviceSession.getService(ActivityCommon.class);
                activityCommon.sendActivity( user1.F01, T6340_F03.redpacket.name(), T6340_F04.birthday.name());
                activityCommon.sendActivity(user1.F01, T6340_F03.interest.name(), T6340_F04.birthday.name());
            }
            catch (AuthenticationException e)
            {
                logger.error(e);
                
                // 封装返回信息
                setReturnMsg(request, response, ExceptionCode.LOGING_FAIL_ERROR, e.getMessage());
                return;
            }
            
            // 跳转到用户信息页面
            Controller controller = getController();
            controller.forward(request, response, controller.getURI(request, Account.class));
        }
    }
    
    @Override
    protected void processGet(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        processPost(request, response, serviceSession);
    }
    
}
