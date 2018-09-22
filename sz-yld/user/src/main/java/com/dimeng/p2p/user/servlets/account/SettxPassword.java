package com.dimeng.p2p.user.servlets.account;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.digest.UnixCrypt;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.account.user.service.SafetyManage;
import com.dimeng.p2p.account.user.service.entity.Safety;
import com.dimeng.p2p.common.RSAUtils;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.util.StringHelper;

public class SettxPassword extends AbstractAccountServlet
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
        
        String newpassword = StringHelper.isEmpty(request.getParameter("new")) ? "" : request.getParameter("new");
        String cnewPsw = StringHelper.isEmpty(request.getParameter("cnewPsw")) ? "" : request.getParameter("cnewPsw");
        newpassword = RSAUtils.decryptStringByJs(newpassword);
        cnewPsw = RSAUtils.decryptStringByJs(cnewPsw);
        
        if (!newpassword.equals(cnewPsw))
        {
            StringBuilder sb = new StringBuilder();
            sb.append("[{'num':3,'msg':'");
            sb.append("两次输入密码不一致!" + "'}]");
            out.write(sb.toString());
            return;
        }
        if (!safetyManage.isBindPhone())
        {
            //getController().prompt(request, response, PromptLevel.ERROR, "需绑定手机号后才可以设置交易密码!");
            StringBuilder sb = new StringBuilder();
            sb.append("[{'num':2,'msg':'");
            sb.append("需绑定手机号后才可以设置交易密码!" + "'}]");
            out.write(sb.toString());
            return;
        }
        if (StringHelper.isEmpty(newpassword))
        {
            //getController().prompt(request, response, PromptLevel.ERROR, "输入新密码");
            StringBuilder sb = new StringBuilder();
            sb.append("[{'num':2,'msg':'");
            sb.append("输入新密码" + "'}]");
            out.write(sb.toString());
            return;
        }
        ConfigureProvider configureProvider = getResourceProvider().getResource(ConfigureProvider.class);
        String mtest = configureProvider.getProperty(SystemVariable.NEW_TXPASSWORD_REGEX).substring(1,configureProvider.getProperty(SystemVariable.NEW_TXPASSWORD_REGEX).length()-1);//"^[a-zA-Z](?![a-zA-Z]*$)[a-zA-Z0-9]{7}$";
        if (!newpassword.matches(mtest))
        {
            StringBuilder sb = new StringBuilder();
            sb.append("[{'num':2,'msg':'");
            String errorMsg = configureProvider.getProperty(SystemVariable.TXPASSWORD_REGEX_CONTENT);
            sb.append(errorMsg);
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
        
        if (sa.password.equals(UnixCrypt.crypt(newpassword, DigestUtils.sha256Hex(newpassword))))
        {
            //getController().prompt(request, response, PromptLevel.ERROR, "不能和登录密码相同");
            StringBuilder sb = new StringBuilder();
            sb.append("[{'num':2,'msg':'");
            sb.append("不能和登录密码相同" + "'}]");
            out.write(sb.toString());
            return;
        }
        
        safetyManage.updateTxpassword(UnixCrypt.crypt(newpassword, DigestUtils.sha256Hex(newpassword)));
        // getController().prompt(request, response, PromptLevel.ERROR, "密码设置成功");
        
        StringBuilder sb = new StringBuilder();
        sb.append("[{'num':1,'msg':'");
        sb.append("sussess" + "'}]");
        out.write(sb.toString());
        return;
        // sendRedirect(request, response, getController().getViewURI(request, Safetymsg.class));
    }
    
}
