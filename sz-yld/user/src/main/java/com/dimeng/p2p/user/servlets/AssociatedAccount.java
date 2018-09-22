package com.dimeng.p2p.user.servlets;

import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.Date;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.digest.UnixCrypt;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.config.Envionment;
import com.dimeng.framework.http.servlet.Controller;
import com.dimeng.framework.http.session.Session;
import com.dimeng.framework.http.session.SessionManager;
import com.dimeng.framework.http.session.authentication.AuthenticationException;
import com.dimeng.framework.http.session.authentication.PasswordAuthentication;
import com.dimeng.framework.message.sms.SmsSender;
import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.S61.entities.T6110;
import com.dimeng.p2p.S61.enums.T6110_F07;
import com.dimeng.p2p.S63.enums.T6340_F03;
import com.dimeng.p2p.S63.enums.T6340_F04;
import com.dimeng.p2p.account.user.service.UserInfoManage;
import com.dimeng.p2p.account.user.service.UserThirdPartyRegisterManage;
import com.dimeng.p2p.account.user.service.entity.ThirdUser;
import com.dimeng.p2p.account.user.service.entity.UserInfo;
import com.dimeng.p2p.common.RSAUtils;
import com.dimeng.p2p.service.ActivityCommon;
import com.dimeng.p2p.variables.defines.MsgVariavle;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.p2p.variables.defines.URLVariable;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.DateTimeParser;

public class AssociatedAccount extends AbstractUserServlet
{
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
        String accountName = request.getParameter("accountName");
        String password = request.getParameter("password");
        if (StringHelper.isEmpty(accountName) || StringHelper.isEmpty(password))
        {
            throw new IllegalArgumentException("用户名或密码不能为空！");
        }
        String psw = RSAUtils.decryptStringByJs(password);
        authentication.setAccountName(RSAUtils.decryptStringByJs(accountName));
        authentication.setPassword(StringHelper.isEmpty(psw) ? "" : UnixCrypt.crypt(psw, DigestUtils.sha256Hex(psw)));
        String referer = request.getParameter("_z");
        //QQ 第三方登陆
        authentication.setVerifyCodeType("LOGIN");
        String openId = request.getParameter("openId");
        String qqToken = request.getParameter("accessToken");
        String loginType = request.getParameter("loginType");
        {
            boolean rememberName = !StringHelper.isEmpty(request.getParameter("remember"));
            if (rememberName)
            {
                Cookie cookie = new Cookie("ACCOUNT_NAME", URLEncoder.encode(authentication.getAccountName(), "UTF-8"));
                cookie.setMaxAge(2592000);
                response.addCookie(cookie);
            }
            else
            {
                Cookie[] _cookies = request.getCookies();
                if (_cookies != null && _cookies.length > 0)
                {
                    for (Cookie cookie : _cookies)
                    {
                        cookie.setMaxAge(0);
                        response.addCookie(cookie);
                    }
                }
            }
        }
        Controller controller = getController();
        UserInfoManage uManage1 = serviceSession.getService(UserInfoManage.class);
        Session session = getResourceProvider().getResource(SessionManager.class).getSession(request, response, true);
        try
        {
            authentication.setVerifyCode(session.getVerifyCode(authentication.getVerifyCodeType()));
            session.invalidate(request, response);
            session.checkIn(request, response, authentication);
            T6110 user1 = uManage1.getUserInfo(session.getAccountId());
            if (!StringHelper.isEmpty(openId))
            {
                UserThirdPartyRegisterManage userThirdPartyRegisterManage =
                    serviceSession.getService(UserThirdPartyRegisterManage.class);
                ThirdUser thireUser = new ThirdUser();
                thireUser.F01 = user1.F01;
                thireUser.openId = openId;
                //注册则是登录，所以添加注册时间
                thireUser.loginDate = new Timestamp(new Date().getTime());
                thireUser.token = qqToken;
                thireUser.tokenTime = System.currentTimeMillis();
                
                if (!StringHelper.isEmpty(qqToken))
                {
                    thireUser.qqTen = "Y";
                    Long newDateTimes = new Date().getTime();
                    session.setAttribute("qqTokenTime", newDateTimes.toString());
                    session.setAttribute("qqTokens", newDateTimes.toString());
                }
                else
                {
                    thireUser.qqTen = "N";
                    session.setAttribute("qqTokenTime", null);
                }
                if (!StringHelper.isEmpty(loginType) && "weixin".equals(loginType))
                {
                    thireUser.wxAuth = "Y";
                }
                else
                {
                    thireUser.wxAuth = "N";
                }
                userThirdPartyRegisterManage.addT6171(thireUser);
                session.setAttribute("qqToken", null);
            }
            
            //生日登录送红包和加息券
            ActivityCommon activityCommon = serviceSession.getService(ActivityCommon.class);
            activityCommon.sendActivity(user1.F01, T6340_F03.redpacket.name(), T6340_F04.birthday.name());
            activityCommon.sendActivity(user1.F01, T6340_F03.interest.name(), T6340_F04.birthday.name());
            
            if (T6110_F07.HMD == user1.F07)
            {
                controller.prompt(request, response, PromptLevel.INFO, "您的账号已被拉入黑名单，操作有所限制。如有疑问，请联系客服！");
                controller.sendRedirect(request, response, controller.getViewURI(request, Index.class));
            }
            else
            {
                if (StringHelper.isEmpty(referer))
                {
                    controller.sendRedirect(request, response, controller.getViewURI(request, Index.class));
                }
                else
                {
                    controller.sendRedirect(request, response, referer);
                }
            }
        }
        catch (AuthenticationException e)
        {
            StringBuffer sb = new StringBuffer(e.getMessage());
            UserInfo userInfo = uManage1.search(RSAUtils.decryptStringByJs(request.getParameter("accountName")));
            int errorTimes = uManage1.getUserLoginError(userInfo, controller.getRemoteAddr(request));
            int allowErrorTimes =
                Integer.parseInt(configureProvider.getProperty(SystemVariable.ALLWO_LOGIN_ERROR_TIMES));
            if (errorTimes == allowErrorTimes && !StringHelper.isEmpty(userInfo.F04))
            {
                SmsSender emailSender = serviceSession.getService(SmsSender.class);
                Envionment envionment = configureProvider.createEnvionment();
                envionment.set("dateTime",
                    DateTimeParser.format(new Timestamp(System.currentTimeMillis()), "yyyy年MM月dd日  HH:mm:ss"));
                envionment.set("count", String.valueOf(allowErrorTimes));
                envionment.set("serviceTel", configureProvider.getProperty(SystemVariable.SITE_CUSTOMERSERVICE_TEL));
                emailSender.send(0,
                    StringHelper.format(configureProvider.getProperty(MsgVariavle.LOGIN_ERROR_TIMES_MSG), envionment),
                    userInfo.F04);
            }
            if (errorTimes <= allowErrorTimes && e.getMessage().indexOf("密码错误") > -1)
            {
                if (!(userInfo.F01 <= 0))
                {
                    sb.append("您还有");
                    sb.append(allowErrorTimes - errorTimes);
                    sb.append("次机会，否则会禁止登录！");
                }
            }
            if (errorTimes > allowErrorTimes)
            {
                sb = new StringBuffer();
                sb.append("超过登录最大错误限制数,请明天再尝试");
            }
            getResourceProvider().log(sb.toString());
            controller.prompt(request, response, PromptLevel.ERROR, sb.toString());
            sendRedirect(request,
                response,
                getResourceProvider().getResource(ConfigureProvider.class).format(URLVariable.ASSOCIATED_ACCOUNT)
                    + "?openId=" + openId + "&qqToken=" + qqToken);
        }
        
    }
    
}
