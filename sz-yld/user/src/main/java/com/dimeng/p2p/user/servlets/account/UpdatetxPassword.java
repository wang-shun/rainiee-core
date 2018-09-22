package com.dimeng.p2p.user.servlets.account;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.digest.UnixCrypt;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.http.servlet.Controller;
import com.dimeng.framework.http.session.authentication.AuthenticationException;
import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.resource.ResourceProvider;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.account.user.service.SafetyManage;
import com.dimeng.p2p.account.user.service.entity.Safety;
import com.dimeng.p2p.common.RSAUtils;
import com.dimeng.p2p.user.servlets.Login;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.util.StringHelper;

public class UpdatetxPassword extends AbstractAccountServlet
{
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processPost(final HttpServletRequest request, HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        PrintWriter out = response.getWriter();
        SafetyManage safetyManage = serviceSession.getService(SafetyManage.class);
        Safety sa = safetyManage.get();
        String oldpassword = StringHelper.isEmpty(request.getParameter("old")) ? "" : request.getParameter("old");
        String newpassword = StringHelper.isEmpty(request.getParameter("new")) ? "" : request.getParameter("new");
        String cnewPsw = StringHelper.isEmpty(request.getParameter("cnewPsw")) ? "" : request.getParameter("cnewPsw");
        oldpassword = RSAUtils.decryptStringByJs(oldpassword);
        newpassword = RSAUtils.decryptStringByJs(newpassword);
        cnewPsw = RSAUtils.decryptStringByJs(cnewPsw);
        safetyManage.isUpdateTxmm();
        if (StringHelper.isEmpty(oldpassword))
        {
            // getController().prompt(request, response, PromptLevel.ERROR, "输入原密码");
            StringBuilder sb = new StringBuilder();
            sb.append("[{'num':02,'msg':'");
            sb.append("输入原密码" + "'}]");
            out.write(sb.toString());
            return;
        }
        if (StringHelper.isEmpty(newpassword))
        {
            // getController().prompt(request, response, PromptLevel.ERROR, "输入新密码");
            StringBuilder sb = new StringBuilder();
            sb.append("[{'num':03,'msg':'");
            sb.append("输入新密码" + "'}]");
            out.write(sb.toString());
            return;
        }
        if (!newpassword.equals(cnewPsw))
        {
            StringBuilder sb = new StringBuilder();
            sb.append("[{'num':04,'msg':'");
            sb.append("两次输入密码不一致!" + "'}]");
            out.write(sb.toString());
            return;
        }

        ConfigureProvider configureProvider = getResourceProvider().getResource(ConfigureProvider.class);
        String mtest = configureProvider.getProperty(SystemVariable.NEW_TXPASSWORD_REGEX).substring(1,configureProvider.getProperty(SystemVariable.NEW_TXPASSWORD_REGEX).length()-1);//"^[a-zA-Z](?![a-zA-Z]*$)[a-zA-Z0-9]{7}$";
        if (!newpassword.matches(mtest))
        {
            StringBuilder sb = new StringBuilder();
            sb.append("[{'num':02,'msg':'");
            sb.append(configureProvider.getProperty(SystemVariable.TXPASSWORD_REGEX_CONTENT));
            sb.append("'}]");
            out.write(sb.toString());
            return;
        }
        if (sa.username.equals(newpassword))
        {
            StringBuilder sb = new StringBuilder();
            sb.append("[{'num':2,'msg':'");
            sb.append("密码不能与用户名一致!" + "'}]");
            out.write(sb.toString());
            return;
        }

        if (!UnixCrypt.crypt(oldpassword, DigestUtils.sha256Hex(oldpassword)).equals(sa.txpassword))
        {
            // getController().prompt(request, response, PromptLevel.ERROR, "原密码错误");
            safetyManage.udpatetxSize();
            StringBuilder sb = new StringBuilder();
            sb.append("[{'num':02,'msg':'");
            sb.append("原密码错误" + "'}]");
            out.write(sb.toString());
            return;
        }
       
        if (sa.password.equals(UnixCrypt.crypt(newpassword, DigestUtils.sha256Hex(newpassword))))
        {
            // getController().prompt(request, response, PromptLevel.ERROR, "不能和登录密码相同");
            StringBuilder sb = new StringBuilder();
            sb.append("[{'num':03,'msg':'");
            sb.append("不能和登录密码相同" + "'}]");
            out.write(sb.toString());
            return;
            
        }
        if (oldpassword.equals(newpassword))
        {
            // getController().prompt(request, response, PromptLevel.ERROR, "不能和原交易密码相同");
            StringBuilder sb = new StringBuilder();
            sb.append("[{'num':03,'msg':'");
            sb.append("不能和原交易密码相同" + "'}]");
            out.write(sb.toString());
            return;
        }
        
        safetyManage.updateTxpassword(UnixCrypt.crypt(newpassword, DigestUtils.sha256Hex(newpassword)));
        // getController().prompt(request, response, PromptLevel.ERROR, "交易密码修改成功");
        StringBuilder sb = new StringBuilder();
        sb.append("[{'num':01,'msg':'");
        sb.append("sussess" + "'}]");
        out.write(sb.toString());
        return;
        
        // }
        // sendRedirect(request, response, getController().getViewURI(request, Safetymsg.class));
    }
    
    @Override
    protected void onThrowable(HttpServletRequest request, HttpServletResponse response, Throwable throwable)
        throws ServletException, IOException
    {
        ResourceProvider resourceProvider = getResourceProvider();
        resourceProvider.log(throwable);
        getController().prompt(request, response, PromptLevel.ERROR, throwable.getMessage());
        if (throwable instanceof AuthenticationException)
        {
            Controller controller = getController();
            controller.redirectLogin(request, response, controller.getViewURI(request, Login.class));
        }
        else
        {
            // sendRedirect(request, response, getController().getViewURI(request, Safetymsg.class));
            PrintWriter out = response.getWriter();
            StringBuilder sb = new StringBuilder();
            sb.append("[{'num':02,'msg':'");
            sb.append(throwable.getMessage() + "'}]");
            out.write(sb.toString());
        }
    }
    
}
