/*
 * 文 件 名:  WXAccountLogin.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  zhoulantao
 * 修改时间:  2016年4月14日
 */
package com.dimeng.p2p.app.servlets.platinfo;

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
import com.dimeng.p2p.app.service.AppWeixinManage;
import com.dimeng.p2p.app.servlets.AbstractAppServlet;
import com.dimeng.p2p.app.servlets.user.Account;
import com.dimeng.p2p.service.ActivityCommon;
import com.dimeng.p2p.variables.TerminalType;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.IntegerParser;

/**
 * 通过微信账号登录(适用于已经绑定过平台账号的登录)
 * 
 * @author  zhoulantao
 * @version  [版本号, 2016年4月14日]
 */
public class WXAccountLogin extends AbstractAppServlet
{
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 2619704422296253842L;
    
    @Override
    protected void processPost(final HttpServletRequest request, HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        // 用户平台注册的ID
        Object id = request.getAttribute("accountId");
        String accountId = "";
        if (null != id)
        {
            accountId = String.valueOf(id);
        }
        else
        {
            // 处理微信免登陆问题
            accountId = getParameter(request, "accountId");
        }
        
        int userId;
        final AppWeixinManage appWeixinManage = serviceSession.getService(AppWeixinManage.class);
        
        // 如果用户平台注册ID不为空
        if (StringHelper.isEmpty(accountId))
        {
            // 微信用户唯一标识
            final String openId = String.valueOf(request.getAttribute("openId"));
            
            userId = appWeixinManage.searchAppWeixin(openId);
        }
        
        userId = IntegerParser.parse(accountId);
        
        // 说明这个用户已经绑定过平台账号了，判断是否已经登录。没登录则跳转到第三方登录操作
        final Session session = getResourceProvider().getResource(SessionManager.class).getSession(request, response, true);
        
        Controller controller = getController();
        // 判断session中登录标识
        if (null == session || !session.isAuthenticated())
        {
            // 通过accountId获取平台账号及密码
            Map<String, String> userInfo = appWeixinManage.getWeixinAccountInfo(userId);
            String accountName = userInfo.get("accountName");
            String password = userInfo.get("password");
            
            // 用第三方绑定账号登录
            PasswordAuthentication authentication = new PasswordAuthentication();
            authentication.setAccountName(accountName);
            authentication.setPassword(password);
            authentication.setVerifyCodeType("LOGIN");
            authentication.setVerifyCode(session.getVerifyCode("LOGIN"));
            authentication.setTerminal(TerminalType.wx.name());
            
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
                activityCommon.sendActivity(user1.F01, T6340_F03.redpacket.name(), T6340_F04.birthday.name());
                activityCommon.sendActivity(user1.F01, T6340_F03.interest.name(), T6340_F04.birthday.name());
            }
            catch (AuthenticationException e)
            {
                logger.error(e);
                
                // 封装返回信息
                setReturnMsg(request, response, ExceptionCode.LOGING_FAIL_ERROR, e.getMessage());
                return;
            }
        }
        
        // 跳转到用户信息页面
        controller.forward(request, response, controller.getURI(request, Account.class));
    }
    
}
