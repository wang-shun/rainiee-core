package com.dimeng.p2p.console.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.http.servlet.Controller;
import com.dimeng.framework.http.session.Session;
import com.dimeng.framework.http.session.SessionManager;
import com.dimeng.framework.http.session.authentication.AuthenticationException;
import com.dimeng.framework.http.session.authentication.PasswordAuthentication;
import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.p2p.S71.entities.T7110;
import com.dimeng.p2p.S71.enums.T7110_F05;
import com.dimeng.p2p.common.RSAUtils;
import com.dimeng.p2p.console.config.ConsoleConst;
import com.dimeng.p2p.modules.account.console.service.UserManage;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.p2p.variables.defines.URLVariable;
import com.dimeng.util.StringHelper;

public class Login extends AbstractConsoleServlet
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
        UserManage userManage = serviceSession.getService(UserManage.class);
        ConfigureProvider configureProvider = getResourceProvider().getResource(ConfigureProvider.class);
        Controller controller = getController();
        try
        {
            //TODO 暂时屏蔽
            //String verifyCode = serviceSession.getSession().getVerifyCode("LOGIN");
            PasswordAuthentication authentication = new PasswordAuthentication();
            String userName =
                StringHelper.isEmpty(request.getParameter("username")) ? "" : request.getParameter("username");
            String password =
                StringHelper.isEmpty(request.getParameter("password")) ? "" : request.getParameter("password");
            authentication.setAccountName(RSAUtils.decryptStringByJs(userName));
            authentication.setPassword(RSAUtils.decryptStringByJs(password));
            //authentication.setVerifyCode(verifyCode);
            //authentication.setVerifyCode(request.getParameter("verifyCode"));
            authentication.setVerifyCodeType(ConsoleConst.LOGIN_VERIFY_CODE_TYPE);
            Session dimengSession =
                getResourceProvider().getResource(SessionManager.class).getSession(request, response, true);
            if (dimengSession != null)
            {
                dimengSession.invalidate(request, response);
            }
            String verifyFlag = configureProvider.getProperty(SystemVariable.SFXYYZM);
            //是否需要输入验证码:false不需要
            if ("false".equalsIgnoreCase(verifyFlag))
            {
                authentication.setVerifyCode(serviceSession.getSession()
                    .getVerifyCode(authentication.getVerifyCodeType()));
            }
            else
            {
                authentication.setVerifyCode(request.getParameter("verifyCode"));
            }
            
            //锁定用户不允许登录
            T7110 userTemp = userManage.getConsoleUserByName(RSAUtils.decryptStringByJs(userName));
            if (null != userTemp && T7110_F05.TY == userTemp.F05)
            {
                throw new LogicalException("该账号被锁定，禁止登录。");
            }
            if (dimengSession != null)
            {
                dimengSession.checkIn(request, response, authentication);
                userManage.clearErrorCount(RSAUtils.decryptStringByJs(userName), controller.getRemoteAddr(request));
                dimengSession.setAttribute(ConsoleConst.ACCOUNT_NAME, authentication.getAccountName());
                
                String url =
                    getResourceProvider().getResource(ConfigureProvider.class)
                        .getProperty(URLVariable.CONSOLE_INDEX_URL);
                sendRedirect(request, response, url);
            }
        }
        catch (Exception e)
        {
            if (e instanceof AuthenticationException)
            {
                StringBuffer sb = new StringBuffer();
                if (e.getCause() instanceof LogicalException)
                {
                    sb.append(e.getMessage());
                }
                else
                {
                    sb.append(e.getMessage());
                    int errorTimes =
                        userManage.getUserLoginError(RSAUtils.decryptStringByJs(request.getParameter("username")),
                            controller.getRemoteAddr(request));
                    int allowErrorTimes =
                        Integer.parseInt(configureProvider.getProperty(SystemVariable.ALLWO_LOGIN_ERROR_TIMES));
                    
                    if (errorTimes < allowErrorTimes && e.getMessage().indexOf("密码错误") > -1)
                    {
                        sb.append("您还有");
                        sb.append(allowErrorTimes - errorTimes);
                        sb.append("次机会，否则会禁止登录！");
                    }
                    if (errorTimes >= allowErrorTimes)
                    {
                        sb = new StringBuffer();
                        sb.append("超过登录最大错误限制数,请明天再尝试");
                    }
                }
                prompt(request, response, PromptLevel.ERROR, sb.toString());
                forwardView(request, response, getClass());
            }
            else if (e instanceof LogicalException)
            {
                StringBuffer sb = new StringBuffer(e.getMessage());
                prompt(request, response, PromptLevel.ERROR, sb.toString());
                forwardView(request, response, getClass());
            }
            else
            {
                prompt(request, response, PromptLevel.ERROR, e.getMessage());
                forwardView(request, response, getClass());
            }
        }
    }
    
    @Override
    protected void onThrowable(HttpServletRequest request, HttpServletResponse response, Throwable throwable)
        throws ServletException, IOException
    {
        if (throwable instanceof AuthenticationException)
        {
            prompt(request, response, PromptLevel.ERROR, throwable.getMessage());
            forwardView(request, response, getClass());
        }
        else
        {
            super.onThrowable(request, response, throwable);
        }
    }
}
