/**
 * 
 */
package com.dimeng.p2p.app.servlets.user;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.resource.ResourceRegister;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.app.servlets.AbstractSecureServlet;
import com.dimeng.p2p.app.servlets.platinfo.ExceptionCode;
import com.dimeng.p2p.app.servlets.user.domain.RegexInfo;
import com.dimeng.p2p.variables.defines.SystemVariable;

/**
 * @author zhoulantao
 *
 */
public class RegisterInfo extends AbstractSecureServlet
{
    private static final long serialVersionUID = 4513849535978911675L;
    
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
        final ConfigureProvider configureProvider =
            ResourceRegister.getResourceProvider(getServletContext()).getResource(ConfigureProvider.class);
        
        // 注册信息校验   
        final String registerFlage = configureProvider.getProperty(SystemVariable.SFXYYZM);
        // 用户名用正则式匹配
        final String newUserNameRegex = configureProvider.getProperty(SystemVariable.NEW_USERNAME_REGEX);
        final String userNameRegexContent = configureProvider.getProperty(SystemVariable.USERNAME_REGEX_CONTENT);
        // 密码用正则式匹配
        final String newPasswordRegex = configureProvider.getProperty(SystemVariable.NEW_PASSWORD_REGEX);
        final String passwordRegexContent = configureProvider.getProperty(SystemVariable.PASSWORD_REGEX_CONTENT);
        // 交易密码校验
        final String txPwdRegex =  configureProvider.getProperty(SystemVariable.NEW_TXPASSWORD_REGEX);
        final String txPwdContent = configureProvider.getProperty(SystemVariable.TXPASSWORD_REGEX_CONTENT);
        
        final RegexInfo regexInfo = new RegexInfo();
        regexInfo.setRegisterFlage(registerFlage);
        regexInfo.setNewUserNameRegex(newUserNameRegex);
        regexInfo.setUserNameRegexContent(userNameRegexContent);
        regexInfo.setNewPasswordRegex(newPasswordRegex);
        regexInfo.setPasswordRegexContent(passwordRegexContent);
        regexInfo.setTxPwdRegex(txPwdRegex);
        regexInfo.setTxPwdContent(txPwdContent);
        
        // 封装返回信息
        setReturnMsg(request, response, ExceptionCode.SUCCESS, "success", regexInfo);
        return;
    }
    
    @Override
    protected boolean mustAuthenticated()
    {
        return false;
    }
    
}
