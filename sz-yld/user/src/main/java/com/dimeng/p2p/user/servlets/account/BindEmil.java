package com.dimeng.p2p.user.servlets.account;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.http.session.Session;
import com.dimeng.framework.http.session.authentication.AuthenticationException;
import com.dimeng.framework.http.session.authentication.VerifyCodeAuthentication;
import com.dimeng.framework.resource.ResourceRegister;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.S61.enums.T6118_F04;
import com.dimeng.p2p.account.user.service.SafetyManage;
import com.dimeng.p2p.common.EmailTypeUtil;
import com.dimeng.p2p.variables.defines.MessageVariable;
import com.dimeng.util.StringHelper;

public class BindEmil extends AbstractAccountServlet
{
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processPost(final HttpServletRequest request, HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        
        PrintWriter out = response.getWriter();
        ConfigureProvider configureProvider =
                ResourceRegister.getResourceProvider(getServletContext()).getResource(ConfigureProvider.class);
        SafetyManage safetyManage = serviceSession.getService(SafetyManage.class);
        String emil = request.getParameter("bemil");
        String code = request.getParameter("bemilCode");
        if (StringHelper.isEmpty(emil))
        {
            StringBuilder sb = new StringBuilder();
            sb.append("[{'num':0,'msg':'");
            sb.append("邮箱地址错误" + "'}]");
            out.write(sb.toString());
            return;
            
            //getController().prompt(request, response, PromptLevel.ERROR, "邮箱地址错误");
            //sendRedirect(request, response, getController().getViewURI(request, Safetymsg.class));
            //return;
        }
        else if (StringHelper.isEmpty(code))
        {
            StringBuilder sb = new StringBuilder();
            sb.append("[{'num':0,'msg':'");
            sb.append("邮箱验证码错误" + "'}]");
            out.write(sb.toString());
            return;
            /* getController().prompt(request, response, PromptLevel.ERROR, "验证码错误");
             sendRedirect(request, response, getController().getViewURI(request, Safetymsg.class));
             return;*/
        }
        else
        {
            Integer sendType = EmailTypeUtil.getEmailType("bind");
            if (sendType != null ) {
               //当日该邮箱与验证码匹配错误次数
               Integer ecount = safetyManage.phoneMatchVerifyCodeErrorCount(emil, sendType);
               if (ecount>= Integer.parseInt(configureProvider.getProperty(MessageVariable.PHONE_VERIFYCODE_MAX_ERROR_COUNT))) {
                   StringBuilder sb = new StringBuilder();
                   sb.append("[{'num':0,'msg':'");
                   sb.append("此邮箱当天匹配验证码错误次数已达上限！" + "'}]");
                   out.write(sb.toString());
                   return;
               }
           }
            // serviceSession.getSession().getAttribute("ebandsendCode");
            String codeType = "bind|"+emil;
            Session session = serviceSession.getSession();
            VerifyCodeAuthentication verfycode = new VerifyCodeAuthentication();
            verfycode.setVerifyCodeType(codeType);
            verfycode.setVerifyCode(code);
            try
            {
                session.authenticateVerifyCode(verfycode);
            }
            catch (AuthenticationException e)
            {
                if (sendType != null ) {
                    //插入验证码匹配错误表
                    safetyManage.insertPhoneMatchVerifyCodeError(emil,sendType, code);
                }
                StringBuilder sb = new StringBuilder();
                sb.append("[{'num':0,'msg':'");
                sb.append("邮箱验证码错误" + "'}]");
                out.write(sb.toString());
                return;
                /*getController().prompt(request, response, PromptLevel.ERROR, "验证码错误");
                sendRedirect(request, response, getController().getViewURI(request, Safetymsg.class));
                return;*/
            }
            if (safetyManage.isEmil(emil))
            {
                StringBuilder sb = new StringBuilder();
                sb.append("[{'num':0,'msg':'");
                sb.append("邮箱已存在" + "'}]");
                out.write(sb.toString());
                return;
                /* getController().prompt(request, response, PromptLevel.ERROR, "邮箱已存在");
                 sendRedirect(request, response, getController().getViewURI(request, Safetymsg.class));
                 return;*/
            }
            session.invalidVerifyCode(codeType);
            safetyManage.bindEmil(emil, T6118_F04.TG.name());
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append("[{'num':1,'msg':'");
        sb.append("sussess" + "'}]");
        out.write(sb.toString());
        //sendRedirect(request, response, getController().getViewURI(request, Safetymsg.class));
    }
    
}
