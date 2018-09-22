package com.dimeng.p2p.user.servlets.account;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.http.session.Session;
import com.dimeng.framework.http.session.authentication.AuthenticationException;
import com.dimeng.framework.http.session.authentication.VerifyCodeAuthentication;
import com.dimeng.framework.resource.ResourceProvider;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.S61.entities.T6110;
import com.dimeng.p2p.account.user.service.UserInfoManage;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.util.StringHelper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * 检查邮箱
 * @version v3.0
 * @LastModified 
 */
public class CheckEmail extends AbstractAccountServlet
{
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processPost(final HttpServletRequest request, HttpServletResponse response,
        final ServiceSession serviceSession) throws Throwable
    {
        PrintWriter out = response.getWriter();
        UserInfoManage userManage = serviceSession.getService(UserInfoManage.class);
        T6110 user = userManage.getUserInfo(serviceSession.getSession().getAccountId());
        String emil = request.getParameter("email");
        String codeType = request.getParameter("ctp");
        codeType = codeType+"|"+emil;
        String verifyType = request.getParameter("verifyType"); //验证码类型
        String verifyCode = request.getParameter("verifyCode"); //验证码
        Session session = serviceSession.getSession();
        
        VerifyCodeAuthentication verfycode = new VerifyCodeAuthentication();
        verfycode.setVerifyCodeType(verifyType);
        verfycode.setVerifyCode(verifyCode);
        ConfigureProvider configureProvider = getResourceProvider().getResource(ConfigureProvider.class);

        String verifySfxyyzm = configureProvider.getProperty(SystemVariable.SFXYYZM);
        //是否需要输入验证码:false不需要
        try{
            if(!"false".equalsIgnoreCase(verifySfxyyzm)){
                session.authenticateVerifyCode(verfycode);
            }
        }catch (AuthenticationException e){
            out.write("[{'num':2,'msg':'无效的验证码或验证码已过期'}]");
            return;
        }
        
        if(!(!StringHelper.isEmpty(emil) && null != user && emil.equalsIgnoreCase(user.F05))){
            out.write("[{'num':0,'msg':'不是本人的邮箱'}]");
            return;
        }
        session.setAttribute(emil,"1");
        serviceSession.getSession().setAttribute(codeType + "is", "true");

        out.write("[{'num':1,'msg':'sussess'}]");
    }

    @Override
    protected void onThrowable(HttpServletRequest request, HttpServletResponse response, Throwable throwable)
            throws ServletException, IOException
    {
        ResourceProvider resourceProvider = getResourceProvider();
        resourceProvider.log(throwable);
        PrintWriter out = response.getWriter();
        StringBuilder sb = new StringBuilder();
        if (throwable instanceof AuthenticationException)
        {
            sb.append("[{'num':101,'msg':'未登录或会话超时,请重新登录'}]");
        }else{
            sb.append("[{'num':0,'msg':'");
            sb.append(throwable.getMessage() + "'}]");
        }
        out.write(sb.toString());

    }
}
