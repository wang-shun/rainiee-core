package com.dimeng.p2p.app.servlets.platinfo;

import java.sql.Timestamp;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.digest.UnixCrypt;
import org.apache.commons.lang.StringUtils;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.config.Envionment;
import com.dimeng.framework.http.servlet.Controller;
import com.dimeng.framework.http.session.Session;
import com.dimeng.framework.http.session.SessionManager;
import com.dimeng.framework.http.session.authentication.AuthenticationException;
import com.dimeng.framework.http.session.authentication.PasswordAuthentication;
import com.dimeng.framework.message.sms.SmsSender;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.S61.entities.T6110;
import com.dimeng.p2p.S61.enums.T6110_F06;
import com.dimeng.p2p.S63.enums.T6340_F03;
import com.dimeng.p2p.S63.enums.T6340_F04;
import com.dimeng.p2p.account.user.service.UserInfoManage;
import com.dimeng.p2p.account.user.service.entity.UserInfo;
import com.dimeng.p2p.app.servlets.AbstractAppServlet;
import com.dimeng.p2p.app.servlets.user.User;
import com.dimeng.p2p.app.tool.MD5Util;
import com.dimeng.p2p.modules.base.console.service.AppWeixinManage;
import com.dimeng.p2p.service.ActivityCommon;
import com.dimeng.p2p.variables.defines.MsgVariavle;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.DateTimeParser;
import com.dimeng.util.parser.LongParser;

/**
 * 
 * 登录
 * 
 * @author  zhoulantao
 * @version  [版本号, 2015年12月22日]
 */
public class Login extends AbstractAppServlet
{
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processGet(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        processPost(request, response, serviceSession);
    }
    
    @Override
    protected void processPost(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        String accountName = getParameter(request, "accountName");
        String password = getParameter(request, "password");
        String verCode = getParameter(request, "verCode");
        //logger.info("login->login accountName:" + accountName + "|password:" + password + "|verCode:" + verCode);
        PasswordAuthentication authentication = new PasswordAuthentication();
        authentication.setAccountName(accountName);
        authentication.setPassword(StringHelper.isEmpty(password) ? "" : UnixCrypt.crypt(password,
            DigestUtils.sha256Hex(password)));
        authentication.setVerifyCode(verCode);
        authentication.setVerifyCodeType("LOGIN");
        authentication.setTerminal(getTerminalType(request));
        
        Controller controller = getController();
        UserInfoManage uManage1 = serviceSession.getService(UserInfoManage.class);
        try
        {
            
            Session session = serviceSession.getSession();
            if (session == null)
            {
                session = getResourceProvider().getResource(SessionManager.class).getSession(request, response, true);
            }
            
            String flag = getParameter(request, "flag");
            // 无验证码登录
            if (!StringHelper.isEmpty(flag) && StringHelper.isEmpty(verCode))
            {
                long time = LongParser.parse(getParameter(request, "time"));
                long nowTime = new Date().getTime();
                String sign = MD5Util.string2MD5(accountName + "|" + password + "|" + time);
                if (!sign.equalsIgnoreCase(flag))
                {
                    // 封装返回信息
                    setReturnMsg(request, response, ExceptionCode.AUTO_LOGIN_SIGN_ERRROR, "自动登录失败,加密串错误");
                    return;
                }
                if (Math.abs(nowTime - time) / 1000 / 60 > 15)
                {
                    // 封装返回信息
                    setReturnMsg(request, response, ExceptionCode.LOGING_FAIL_ERROR, "自动登录超时");
                    return;
                }
                // 验证通过自动设置验证码
                final String verifyCode = session.getVerifyCode("LOGIN");
                authentication.setVerifyCode(verifyCode);
            }
            if (session != null)
            {
                session.invalidate(request, response);
            }
            int accountId = session.checkIn(request, response, authentication);
            
            // 生日登录送红包和加息券
            T6110 user1 = uManage1.getUserInfo(accountId);
            // 生日登录送红包和加息券
            ActivityCommon activityCommon = serviceSession.getService(ActivityCommon.class);
            activityCommon.sendActivity(user1.F01, T6340_F03.redpacket.name(), T6340_F04.birthday.name());
            activityCommon.sendActivity(user1.F01, T6340_F03.interest.name(), T6340_F04.birthday.name());
            
            if (T6110_F06.ZRR.equals(user1.F06))
            {
                String weixinId = getParameter(request, "weixinId");
                if (StringUtils.isNotEmpty(weixinId))
                {
                    serviceSession.getService(AppWeixinManage.class).updateAppWeixin(weixinId, accountId);
                }
                
                controller.forward(request, response, controller.getURI(request, User.class));
            }
            else
            {
                session.invalidate(request, response);
                // 封装返回信息
                setReturnMsg(request, response, ExceptionCode.ZRR_LOGIN_FAIL, "企业或机构用户不能在客户端登录！");
                return;
            }
        }
        catch (AuthenticationException e)
        {
            final ConfigureProvider configureProvider = getConfigureProvider();
            StringBuffer sb = new StringBuffer(e.getMessage());
            try
            {
                UserInfo userInfo = uManage1.search(accountName);
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
            }
            finally
            {
                // 封装返回信息
                setReturnMsg(request, response, ExceptionCode.LOGING_FAIL_ERROR, sb.toString());
            }
        }
    }
}
