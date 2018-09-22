package com.dimeng.p2p.user.servlets;

import java.net.URLEncoder;
import java.sql.Timestamp;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.util.parser.IntegerParser;
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
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.p2p.S61.entities.T6110;
import com.dimeng.p2p.S61.enums.T6110_F07;
import com.dimeng.p2p.S63.enums.T6340_F03;
import com.dimeng.p2p.S63.enums.T6340_F04;
import com.dimeng.p2p.account.user.service.UserInfoManage;
import com.dimeng.p2p.account.user.service.UserThirdPartyLoginManage;
import com.dimeng.p2p.account.user.service.entity.ThirdUser;
import com.dimeng.p2p.account.user.service.entity.UserInfo;
import com.dimeng.p2p.common.RSAUtils;
import com.dimeng.p2p.service.ActivityCommon;
import com.dimeng.p2p.variables.defines.MsgVariavle;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.p2p.variables.defines.URLVariable;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.DateTimeParser;

public class Login extends AbstractUserServlet
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
        
        //String verifyCode = serviceSession.getSession().getVerifyCode("LOGIN");
        ConfigureProvider configureProvider = getResourceProvider().getResource(ConfigureProvider.class);
        PasswordAuthentication authentication = new PasswordAuthentication();
        String accoutName =
            StringHelper.isEmpty(request.getParameter("accountName")) ? "" : request.getParameter("accountName");
        String password =
            StringHelper.isEmpty(request.getParameter("password")) ? "" : request.getParameter("password");
        String psw = RSAUtils.decryptStringByJs(password);
        authentication.setAccountName(RSAUtils.decryptStringByJs(accoutName));
        authentication.setPassword(StringHelper.isEmpty(psw) ? "" : UnixCrypt.crypt(psw, DigestUtils.sha256Hex(psw)));
        authentication.setVerifyCode(request.getParameter("verifyCode"));
        String referer = request.getParameter("_z");
        authentication.setVerifyCodeType("LOGIN");
        String accountEncode = URLEncoder.encode(authentication.getAccountName(), "UTF-8");
        {
            boolean rememberName = !StringHelper.isEmpty(request.getParameter("remember"));
            if (rememberName)
            {
                Cookie cookie = new Cookie("ACCOUNT_NAME", accountEncode);
                cookie.setMaxAge(2592000);
                cookie.setPath("/");
                response.addCookie(cookie);
            }
            else
            {
                Cookie[] _cookies = request.getCookies();
                if (_cookies != null && _cookies.length > 0)
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
                            cookie.setPath("/");
                            response.addCookie(cookie);
                        }
                    }
                }
            }
        }
        
        Controller controller = getController();
        UserInfoManage uManage1 = serviceSession.getService(UserInfoManage.class);
        Session session = getResourceProvider().getResource(SessionManager.class).getSession(request, response, true);
        //三方登陆
        String openId = request.getParameter("openId");
        String qqToken = request.getParameter("accessToken");
        if (!StringHelper.isEmpty(openId))
        {
            UserThirdPartyLoginManage userThirdPartyLoginManage =
                serviceSession.getService(UserThirdPartyLoginManage.class);
            ThirdUser thirdUser = userThirdPartyLoginManage.getUserInfoByThirdID(openId);
            if (null == thirdUser)
            {
                //第一次QQ登陆 --注册信息
                session.setAttribute("openId", openId);
                session.setAttribute("qqToken", qqToken);
                sendRedirect(request,
                    response,
                    getResourceProvider().getResource(ConfigureProvider.class).format(URLVariable.ASSOCIATED_REGISTER));
                return;
            }
            
            //锁定用户不允许登录
            if (T6110_F07.SD == thirdUser.F06)
            {
                throw new LogicalException("该账号被锁定，禁止登录。");
            }
            
            authentication.setAccountName(thirdUser.F02);
            authentication.setPassword(thirdUser.F10);
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
                session.invalidate(request, response);
                session.checkIn(request, response, authentication);
                T6110 user1 = uManage1.getUserInfo(session.getAccountId());
                
                //生日登录送红包和加息券
                ActivityCommon activityCommon = serviceSession.getService(ActivityCommon.class);
                activityCommon.sendActivity(user1.F01, T6340_F03.redpacket.name(), T6340_F04.birthday.name());
                activityCommon.sendActivity(user1.F01, T6340_F03.interest.name(), T6340_F04.birthday.name());
                activityCommon.sendActivity(user1.F01, T6340_F03.experience.name(), T6340_F04.birthday.name());
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
            sendRedirect(request,
                response,
                getResourceProvider().getResource(ConfigureProvider.class).format(URLVariable.INDEX));
        }
        else
        {
            try
            {
                String verifyFlag = configureProvider.getProperty(SystemVariable.SFXYYZM);
                int loginFailCount = IntegerParser.parse(configureProvider.getProperty(SystemVariable.LOGIN_FAIL_COUNT));
                boolean isShowVcode = IntegerParser.parse(request.getSession().getAttribute("exceptionCount")) >= loginFailCount;
                //是否需要输入验证码:false不需要
                if ("false".equalsIgnoreCase(verifyFlag) || !isShowVcode)
                {
                    authentication.setVerifyCode(session.getVerifyCode(authentication.getVerifyCodeType()));
                }
                session.invalidate(request, response);
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
                //更新累计登陆次数和最后登陆时间
                uManage1.udpateT6198F05F07(user1.F01);
                
                //清除登录错误次数
                uManage1.clearErrorCount(RSAUtils.decryptStringByJs(request.getParameter("accountName")),
                        controller.getRemoteAddr(request));
                //清除session中的登陆异常次数
                request.getSession().removeAttribute("exceptionCount");
                
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
                        StringHelper.format(configureProvider.getProperty(MsgVariavle.LOGIN_ERROR_TIMES_MSG),
                            envionment),
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
                sendRedirect(request, response, controller.getViewURI(request, Login.class));
            }
            catch (LogicalException le)
            {
                //异常登陆，记录一次，超过指定次数，需要输入验证码
                int exceptionCount = IntegerParser.parse(request.getSession().getAttribute("exceptionCount"));
                request.getSession().setAttribute("exceptionCount", exceptionCount+1);

                StringBuffer sb = new StringBuffer(le.getMessage());
                getResourceProvider().log(sb.toString());
                controller.prompt(request, response, PromptLevel.ERROR, sb.toString());
                sendRedirect(request, response, controller.getViewURI(request, Login.class));
            }
        }
    }
}
