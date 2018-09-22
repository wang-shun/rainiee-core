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
import com.dimeng.p2p.account.user.service.SafetyManage;
import com.dimeng.p2p.common.EmailTypeUtil;
import com.dimeng.p2p.common.PhoneTypeUtil;
import com.dimeng.p2p.variables.defines.MessageVariable;
import com.dimeng.util.StringHelper;

public class CheckCode extends AbstractAccountServlet
{
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processPost(final HttpServletRequest request, HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        PrintWriter out = response.getWriter();
        final ConfigureProvider configureProvider =
            ResourceRegister.getResourceProvider(getServletContext()).getResource(ConfigureProvider.class);
        //校验对象
        String codeType = request.getParameter("ctp");
        if (StringHelper.isEmpty(codeType))
        {
            out.write("01");
            return;
        }
        String evenString = request.getParameter("evencheck");
        String realVal = request.getParameter("realVal");
        String code = request.getParameter("code");
        if (StringHelper.isEmpty(evenString))
        {
            out.write("01");
            return;
        }
        if (StringHelper.isEmpty(code))
        {
            out.write("02");
            return;
        }
        
        SafetyManage safetyManage = serviceSession.getService(SafetyManage.class);
        Integer sendType = null;
        if ("emil".equals(evenString))
        {
            sendType = EmailTypeUtil.getEmailType(codeType);
        }
        if ("phone".equals(evenString))
        {
            sendType = PhoneTypeUtil.getPhoneType(codeType);
        }
        if (sendType != null)
        {
            //当日该手机与验证码匹配错误次数
            Integer ecount = safetyManage.phoneMatchVerifyCodeErrorCount(realVal, sendType);
            if (ecount >= Integer.parseInt(configureProvider.getProperty(MessageVariable.PHONE_VERIFYCODE_MAX_ERROR_COUNT)))
            {
                out.write("04");
                return;
            }
        }
        Session session = serviceSession.getSession();
        VerifyCodeAuthentication verfycode = new VerifyCodeAuthentication();
        codeType = codeType + "|" + realVal;
        verfycode.setVerifyCodeType(codeType);
        verfycode.setVerifyCode(code);
        try
        {
            session.authenticateVerifyCode(verfycode);
        }
        catch (AuthenticationException e)
        {
            out.write("03");
            if (sendType != null)
            {
                //插入手机验证码匹配错误表
                safetyManage.insertPhoneMatchVerifyCodeError(realVal, sendType, code);
            }
            return;
        }
        session.invalidVerifyCode(codeType);
        serviceSession.getSession().setAttribute(codeType + "is", "true");
    }
    
}
