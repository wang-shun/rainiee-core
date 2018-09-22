package com.dimeng.p2p.user.servlets.account;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.digest.UnixCrypt;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.http.session.Session;
import com.dimeng.framework.http.session.SessionManager;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.account.user.service.SafetyManage;
import com.dimeng.p2p.account.user.service.entity.Safety;
import com.dimeng.p2p.common.RSAUtils;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.util.StringHelper;

public class UpdatePassword extends AbstractAccountServlet
{
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processPost(final HttpServletRequest request, HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        PrintWriter out = response.getWriter();
        SafetyManage safetyManage = serviceSession.getService(SafetyManage.class);
        ConfigureProvider configureProvider = getResourceProvider().getResource(ConfigureProvider.class);
        Safety sa = safetyManage.get();
        String oldpassword = StringHelper.isEmpty(request.getParameter("old")) ? "" : request.getParameter("old");
        String newpassword = StringHelper.isEmpty(request.getParameter("new")) ? "" : request.getParameter("new");
        String confPsw = StringHelper.isEmpty(request.getParameter("news")) ? "" : request.getParameter("news");
        oldpassword = RSAUtils.decryptStringByJs(oldpassword);
        newpassword = RSAUtils.decryptStringByJs(newpassword);
        confPsw = RSAUtils.decryptStringByJs(confPsw);
        if (StringHelper.isEmpty(oldpassword))
        {
            //getController().prompt(request, response, PromptLevel.ERROR, "输入原密码");
            
            StringBuilder sb = new StringBuilder();
            sb.append("[{'num':02,'msg':'");
            sb.append("输入原密码" + "'}]");
            out.write(sb.toString());
            return;
        }
        
        if (!UnixCrypt.crypt(oldpassword, DigestUtils.sha256Hex(oldpassword)).equals(sa.password))
        {
            //getController().prompt(request, response, PromptLevel.ERROR, "原密码错误");
            StringBuilder sb = new StringBuilder();
            sb.append("[{'num':02,'msg':'");
            sb.append("原密码错误!" + "'}]");
            out.write(sb.toString());
            return;
        }
        
        if (StringHelper.isEmpty(newpassword))
        {
            //getController().prompt(request, response, PromptLevel.ERROR, "输入新密码");
            StringBuilder sb = new StringBuilder();
            sb.append("[{'num':03,'msg':'");
            sb.append("输入新密码" + "'}]");
            out.write(sb.toString());
            return;
        }
        
        if (sa.username.equals(newpassword))
        {
            StringBuilder sb = new StringBuilder();
            sb.append("[{'num':03,'msg':'");
            sb.append("用户名与密码一致!" + "'}]");
            out.write(sb.toString());
            return;
        }
        
        if (sa.password.equals(UnixCrypt.crypt(newpassword, DigestUtils.sha256Hex(newpassword))))
        {
            StringBuilder sb = new StringBuilder();
            sb.append("[{'num':03,'msg':'");
            sb.append("新密码不能与原密码相同！" + "'}]");
            out.write(sb.toString());
            return;
        }
        
        if (!StringHelper.isEmpty(sa.txpassword)
            && sa.txpassword.equals(UnixCrypt.crypt(newpassword, DigestUtils.sha256Hex(newpassword))))
        {
            // getController().prompt(request, response, PromptLevel.ERROR, "不能和交易密码相同");
            StringBuilder sb = new StringBuilder();
            sb.append("[{'num':03,'msg':'");
            sb.append("不能和交易密码相同!" + "'}]");
            out.write(sb.toString());
            return;
        }
        
        String mtest =
            configureProvider.getProperty(SystemVariable.NEW_PASSWORD_REGEX).substring(1,
                configureProvider.getProperty(SystemVariable.NEW_PASSWORD_REGEX).length() - 1);
        if (!newpassword.matches(mtest))
        {
            StringBuilder sb = new StringBuilder();
            sb.append("[{'num':03,'msg':'");
            sb.append(configureProvider.getProperty(SystemVariable.PASSWORD_REGEX_CONTENT) + "'}]");
            out.write(sb.toString());
            return;
        }
        
        if (!newpassword.equals(confPsw))
        {
            StringBuilder sb = new StringBuilder();
            sb.append("[{'num':04,'msg':'");
            sb.append("两次输入密码不一致!" + "'}]");
            out.write(sb.toString());
            return;
        }
        
        safetyManage.updatePassword(UnixCrypt.crypt(newpassword, DigestUtils.sha256Hex(newpassword)));
        Session session = getResourceProvider().getResource(SessionManager.class).getSession(request, response, true);
        if (session != null)
        {
            session.invalidateAll(request, response);
            session.setAttribute("fromLogout", "true");
        }
        // getController().prompt(request, response, PromptLevel.ERROR, "密码修改成功");
        StringBuilder sb = new StringBuilder();
        sb.append("[{'num':01,'msg':'");
        sb.append("sussess" + "'}]");
        out.write(sb.toString());
        return;
        
        //sendRedirect(request, response, getController().getViewURI(request, Safetymsg.class));
    }
}
