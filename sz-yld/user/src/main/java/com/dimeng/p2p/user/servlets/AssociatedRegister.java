package com.dimeng.p2p.user.servlets;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.digest.UnixCrypt;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.http.servlet.Controller;
import com.dimeng.framework.http.session.Session;
import com.dimeng.framework.http.session.SessionManager;
import com.dimeng.framework.http.session.authentication.PasswordAuthentication;
import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.p2p.S61.enums.T6110_F08;
import com.dimeng.p2p.S61.enums.T6141_F03;
import com.dimeng.p2p.account.user.service.UserThirdPartyRegisterManage;
import com.dimeng.p2p.account.user.service.entity.ThirdUser;
import com.dimeng.p2p.common.FormToken;
import com.dimeng.p2p.common.RSAUtils;
import com.dimeng.p2p.modules.account.pay.service.UserManage;
import com.dimeng.p2p.modules.account.pay.service.entity.UserInsert;
import com.dimeng.p2p.service.PtAccountManage;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.p2p.variables.defines.URLVariable;
import com.dimeng.p2p.variables.defines.pays.PayVariavle;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.BooleanParser;
import com.dimeng.util.parser.EnumParser;

public class AssociatedRegister extends AbstractUserServlet
{
    private static final long serialVersionUID = 1L;
    
    @Override
    protected boolean mustAuthenticated()
    {
        return false;
    }
    
    @Override
    protected void processPost(final HttpServletRequest request, HttpServletResponse response,
        ServiceSession serviceSession)
        throws Throwable
    {
        if (!FormToken.verify(serviceSession.getSession(), request.getParameter(FormToken.parameterName())))
        {
            throw new LogicalException("请不要重复提交请求！");
        }
        Session session = serviceSession.getSession();
        ConfigureProvider configureProvider = getResourceProvider().getResource(ConfigureProvider.class);
        if (session == null)
        {
            session = getResourceProvider().getResource(SessionManager.class).getSession(request, response, true);
        }
        
        //获取访问者ip地址
        Controller controller = serviceSession.getController();
        String agent = controller.getRemoteAgent(request);
        if (StringHelper.isEmpty(agent))
        {
            throw new LogicalException("非法请求");
        }
        String ip = controller.getRemoteAddr(request);
        UserManage userManage = serviceSession.getService(UserManage.class);
        //查询该ip当天注册账号数量，true：允许注册，false：不允许注册
        if (!userManage.ifAllowRegister(ip))
        {
            logger.info(ip + ",当天注册账号数量达到上限");
            throw new LogicalException("您当天注册账号数量达到上限，请明天再注册");
        }
        String registerName = request.getParameter("accountName");
        String regPass = request.getParameter("password");
        String newPassword = request.getParameter("newPassword");
        if (StringHelper.isEmpty(registerName) || StringHelper.isEmpty(regPass) || StringHelper.isEmpty(newPassword))
        {
            throw new IllegalArgumentException("用户名或者密码不能为空！");
        }
        final String accountName = RSAUtils.decryptStringByJs(registerName);
        final String password = RSAUtils.decryptStringByJs(regPass);
        newPassword = RSAUtils.decryptStringByJs(newPassword);
        String openId = request.getParameter("openId");
        String qqToken = request.getParameter("accessToken");
        String loginType = request.getParameter("loginType");
        String location =
            configureProvider.format(URLVariable.ASSOCIATED_REGISTER) + "?openId=" + openId + "&qqToken=" + qqToken;
        if (!password.equals(newPassword))
        {
            getController().prompt(request, response, PromptLevel.ERROR, "两次密码输入不一致!");
            sendRedirect(request, response, location);
            return;
        }
        String accountNameEx =
            configureProvider.getProperty(SystemVariable.NEW_USERNAME_REGEX).substring(1,
                configureProvider.getProperty(SystemVariable.NEW_USERNAME_REGEX).length() - 1);
        if (!accountName.matches(accountNameEx))
        {
            getController().prompt(request,
                response,
                PromptLevel.ERROR,
                configureProvider.getProperty(SystemVariable.USERNAME_REGEX_CONTENT));
            sendRedirect(request, response, location);
            return;
        }
        // 校验密码是否匹配正则规则
        String regEx =
            configureProvider.getProperty(SystemVariable.NEW_PASSWORD_REGEX).substring(1,
                configureProvider.getProperty(SystemVariable.NEW_PASSWORD_REGEX).length() - 1);
        if (!password.matches(regEx))
        {
            getController().prompt(request,
                response,
                PromptLevel.ERROR,
                configureProvider.getProperty(SystemVariable.PASSWORD_REGEX_CONTENT));
            sendRedirect(request, response, location);
            return;
        }
        UserInsert user = new UserInsert()
        {
            @Override
            public T6141_F03 getType()
            {
                return EnumParser.parse(T6141_F03.class, request.getParameter("type"));
            }
            
            @Override
            public String getPassword()
            {
                return password;
            }
            
            @Override
            public String getCode()
            {
                return request.getParameter("code");
            }
            
            @Override
            public String getAccountName()
            {
                return accountName;
            }
            
            @Override
            public String getPhone()
            {
                
                return request.getParameter("phone");
            }
            
            @Override
            public String getName()
            {
                
                return request.getParameter("name");
            }
            
            @Override
            public String getNum()
            {
                return request.getParameter("employNum");
            }
            
            @Override
            public T6110_F08 getRegisterType()
            {
                return EnumParser.parse(T6110_F08.class, request.getParameter("registerType") == null ? "PCZC"
                    : request.getParameter("registerType"));
            }
        };
        //注册前判断平台账号是否存在，否，则增加平台账号
        PtAccountManage ptAccountManage = serviceSession.getService(PtAccountManage.class);
        ptAccountManage.addPtAccount();
        int userId = userManage.addUser(user, serviceSession);
        if (userId > 0)
        {
            if (!StringHelper.isEmpty(openId))
            {
                UserThirdPartyRegisterManage userThirdPartyRegisterManage =
                    serviceSession.getService(UserThirdPartyRegisterManage.class);
                ThirdUser thireUser = new ThirdUser();
                thireUser.F01 = userId;
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
            logger.info("用户注册提交后，登录开始");
            PasswordAuthentication passwordAuthentication = new PasswordAuthentication();
            passwordAuthentication.setAccountName(accountName);
            passwordAuthentication.setPassword(UnixCrypt.crypt(newPassword, DigestUtils.sha256Hex(newPassword)));
            passwordAuthentication.setVerifyCodeType("LOGIN");
            passwordAuthentication.setVerifyCode(session.getVerifyCode("LOGIN"));
            
            //记录注册信息
            userManage.logRegisterInfo(userId, ip);
            if (session != null)
            {
                session.invalidate(request, response);
                session.checkIn(request, response, passwordAuthentication);
            }
            String mRealName = configureProvider.getProperty(PayVariavle.CHARGE_MUST_NCIIC);
            if (BooleanParser.parse(mRealName))
            {
                sendRedirect(request, response, configureProvider.format(URLVariable.REALNAME_GUIDE));
            }
            else
            {
                //跳到用户中心
                sendRedirect(request, response, configureProvider.format(URLVariable.USER_INDEX));
            }
        }
    }
    
    @Override
    protected void onThrowable(HttpServletRequest request, HttpServletResponse response, Throwable throwable)
        throws ServletException, IOException
    {
        logger.error(throwable, throwable);
        String openId = request.getParameter("openId");
        String qqToken = request.getParameter("accessToken");
        getResourceProvider().log(throwable.getMessage());
        ConfigureProvider configureProvider = getResourceProvider().getResource(ConfigureProvider.class);
        String location =
            configureProvider.format(URLVariable.ASSOCIATED_REGISTER) + "?openId=" + openId + "&qqToken=" + qqToken;
        if (throwable instanceof SQLException)
        {
            prompt(request, response, PromptLevel.ERROR, "系统繁忙，请稍后再试");
            sendRedirect(request, response, location);
        }
        else
        {
            prompt(request, response, PromptLevel.ERROR, throwable.getMessage());
            sendRedirect(request, response, location);
        }
    }
    
}
