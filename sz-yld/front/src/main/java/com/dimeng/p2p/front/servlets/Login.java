package com.dimeng.p2p.front.servlets;

import java.net.URLEncoder;
import java.sql.Timestamp;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.p2p.S61.entities.T6110;
import com.dimeng.p2p.S61.enums.T6110_F07;
import com.dimeng.p2p.S63.enums.T6340_F03;
import com.dimeng.p2p.S63.enums.T6340_F04;
import com.dimeng.p2p.account.user.service.UserInfoManage;
import com.dimeng.p2p.account.user.service.entity.UserInfo;
import com.dimeng.p2p.common.RSAUtils;
import com.dimeng.p2p.service.ActivityCommon;
import com.dimeng.p2p.variables.defines.MsgVariavle;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.DateTimeParser;
import com.dimeng.util.parser.IntegerParser;

public class Login extends AbstractFrontServlet
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
        String accoutName =
            StringHelper.isEmpty(request.getParameter("accountName")) ? "" : request.getParameter("accountName");
        String password =
            StringHelper.isEmpty(request.getParameter("password")) ? "" : request.getParameter("password");
        authentication.setAccountName(RSAUtils.decryptStringByJs(accoutName));
        authentication.setPassword(RSAUtils.decryptStringByJs(password));
        authentication.setVerifyCode(request.getParameter("verifyCode"));
        authentication.setVerifyCodeType("LOGIN");
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
                if (_cookies != null)
                {
                    for (Cookie cookie : _cookies)
                    {
                        if (cookie == null)
                        {
                            continue;
                        }
                        if ("ACCOUNT_NAME".equals(cookie.getName()))
                        {
                            cookie.setMaxAge(0);
                            response.addCookie(cookie);
                        }
                    }
                }
            }
        }
        Controller controller = getController();
        UserInfoManage uManage1 = serviceSession.getService(UserInfoManage.class);
        try
        {
            Session session =
                getResourceProvider().getResource(SessionManager.class).getSession(request, response, true);
            String verifyFlag = configureProvider.getProperty(SystemVariable.SFXYYZM);

            //登陆允失败次数（超过显示验证码）
            int loginFailCount = IntegerParser.parse(configureProvider.getProperty(SystemVariable.LOGIN_FAIL_COUNT));
            boolean isShowVcode = IntegerParser.parse(request.getSession().getAttribute("exceptionCount")) >= loginFailCount;
            //是否需要输入验证码:false不需要
            if ("false".equalsIgnoreCase(verifyFlag) || !isShowVcode)
            {
                authentication.setVerifyCode(session.getVerifyCode(authentication.getVerifyCodeType()));
            }
            //锁定用户不允许登录
            T6110 userTemp = uManage1.getUserInfoByAccountName(RSAUtils.decryptStringByJs(accoutName));
            if (null == userTemp)
            {
                throw new LogicalException("用户名或密码错误。");
            }
            if (T6110_F07.SD == userTemp.F07)
            {
                throw new LogicalException("该账号被锁定，禁止登录。");
            }
            session.checkIn(request, response, authentication);
            T6110 user1 = uManage1.getUserInfo(session.getAccountId());
            
            //生日登录送红包和加息券
            ActivityCommon activityCommon = serviceSession.getService(ActivityCommon.class);
            activityCommon.sendActivity(user1.F01, T6340_F03.redpacket.name(), T6340_F04.birthday.name());
            activityCommon.sendActivity(user1.F01, T6340_F03.interest.name(), T6340_F04.birthday.name());
            activityCommon.sendActivity(user1.F01, T6340_F03.experience.name(), T6340_F04.birthday.name());
            
            //清除登录错误次数
            uManage1.clearErrorCount(RSAUtils.decryptStringByJs(request.getParameter("accountName")),
                controller.getRemoteAddr(request));
            //清除session中的登陆异常次数
            request.getSession().removeAttribute("exceptionCount");
            
            //更新累计登陆次数和最后登陆时间
            uManage1.udpateT6198F05F07(user1.F01);
            
            if (T6110_F07.HMD == user1.F07)
            {
                controller.prompt(request, response, PromptLevel.INFO, "您的账号已被拉入黑名单，操作有所限制。如有疑问，请联系客服！");
                controller.sendRedirect(request, response, controller.getViewURI(request, Index.class));
            }
            else
            {
                controller.sendRedirect(request, response, controller.getViewURI(request, Index.class));
            }
        }
        catch (AuthenticationException e)
        {
            //异常登陆，记录一次，超过指定次数，需要输入验证码
            int exceptionCount = IntegerParser.parse(request.getSession().getAttribute("exceptionCount"));
            request.getSession().setAttribute("exceptionCount", exceptionCount+1);

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
            if (errorTimes < allowErrorTimes && e.getMessage().indexOf("密码错误") > -1)
            {
                if (!(userInfo.F01 <= 0))
                {
                    sb.append("您还有");
                    sb.append(allowErrorTimes - errorTimes);
                    sb.append("次机会，否则会禁止登录！");
                }
            }
            if (errorTimes >= allowErrorTimes)
            {
                sb = new StringBuffer();
                sb.append("超过登录最大错误限制数,请明天再尝试");
            }
            getResourceProvider().log(sb.toString());
            controller.prompt(request, response, PromptLevel.ERROR, sb.toString());
            sendRedirect(request, response, controller.getViewURI(request, Index.class));
        }
        catch (LogicalException le)
        {
            //异常登陆，记录一次，超过指定次数，需要输入验证码
            int exceptionCount = IntegerParser.parse(request.getSession().getAttribute("exceptionCount"));
            request.getSession().setAttribute("exceptionCount", exceptionCount+1);

            StringBuffer sb = new StringBuffer(le.getMessage());
            getResourceProvider().log(sb.toString());
            controller.prompt(request, response, PromptLevel.ERROR, sb.toString());
            sendRedirect(request, response, controller.getViewURI(request, Index.class));
        }
    }
    
}
