/*
 * 文 件 名:  AssociatedAccount.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  God
 * 修改时间:  2016年4月14日
 */
package com.dimeng.p2p.app.servlets.platinfo;

import java.sql.Timestamp;
import java.util.Date;

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
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.S61.entities.T6110;
import com.dimeng.p2p.S63.enums.T6340_F03;
import com.dimeng.p2p.S63.enums.T6340_F04;
import com.dimeng.p2p.account.user.service.UserInfoManage;
import com.dimeng.p2p.account.user.service.UserThirdPartyRegisterManage;
import com.dimeng.p2p.account.user.service.entity.ThirdUser;
import com.dimeng.p2p.account.user.service.entity.UserInfo;
import com.dimeng.p2p.app.servlets.AbstractAppServlet;
import com.dimeng.p2p.app.servlets.user.Account;
import com.dimeng.p2p.service.ActivityCommon;
import com.dimeng.p2p.variables.defines.MsgVariavle;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.DateTimeParser;

/**
 * 关联第三方账号和平台账号
 * <功能详细描述>
 * 
 * @author  suwei
 * @version  [版本号, 2016年4月14日]
 */
public class AssociatedAccount extends AbstractAppServlet
{
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 5156545916996037138L;

    @Override
    protected void processPost(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        ConfigureProvider configureProvider = getResourceProvider().getResource(ConfigureProvider.class);
        PasswordAuthentication authentication = new PasswordAuthentication();
        
        // 获取用户名、密码、验证码
        String accountName = StringHelper.isEmpty(getParameter(request, "accountName")) ? "" : getParameter(request, "accountName");
        String password = StringHelper.isEmpty(getParameter(request, "password")) ? "" : getParameter(request, "password");
        
        // 组装待校验参数
        authentication.setAccountName(accountName);
        authentication.setPassword(StringHelper.isEmpty(password) ? "" : UnixCrypt.crypt(password,DigestUtils.sha256Hex(password)));
        authentication.setVerifyCode(serviceSession.getSession().getVerifyCode("LOGIN"));
        authentication.setVerifyCodeType("LOGIN");
        
        // 获取第三方登录信息
        final String openId = getParameter(request, "openId");
        final String qqToken = getParameter(request, "accessToken");
        
        // 登录类型
        final String loginType = (String)request.getAttribute("loginType");
        Controller controller = getController();
        UserInfoManage uManage1 = serviceSession.getService(UserInfoManage.class);
        Session session = getResourceProvider().getResource(SessionManager.class).getSession(request, response, true);
        
        try
        {
            if (session!=null) 
            {
                // 获取验证码
                authentication.setVerifyCode(session.getVerifyCode(authentication.getVerifyCodeType()));
                
                session.invalidate(request, response);
            }
            
            session.checkIn(request, response, authentication);
            T6110 user1=  uManage1.getUserInfo(session.getAccountId());
            
            if (!StringHelper.isEmpty(openId)) 
            {
                UserThirdPartyRegisterManage userThirdPartyRegisterManage = serviceSession.getService(UserThirdPartyRegisterManage.class);
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
            activityCommon.sendActivity( user1.F01, T6340_F03.redpacket.name(), T6340_F04.birthday.name());
            activityCommon.sendActivity(user1.F01, T6340_F03.interest.name(), T6340_F04.birthday.name());
        }
        catch (AuthenticationException e)
        {
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
                        StringHelper.format(configureProvider.getProperty(MsgVariavle.LOGIN_ERROR_TIMES_MSG), envionment),
                            userInfo.F04);
                }
                if (errorTimes < allowErrorTimes && e.getMessage().indexOf("密码错误") > -1)
                {
                    if(!(userInfo.F01 <= 0))
                    {
                        sb.append("您还有");
                        sb.append(allowErrorTimes - errorTimes);
                        sb.append("次机会，否则会禁止登录！");
                    }
                }
                if(errorTimes >= allowErrorTimes){
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
        
        // 跳转到用户信息页面
        controller.forward(request, response, controller.getURI(request, Account.class));
    }
    
    @Override
    protected void processGet(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        processPost(request, response, serviceSession);
    }
}
