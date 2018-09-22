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

public class CheckNewCode extends AbstractAccountServlet
{
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processPost(final HttpServletRequest request, HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        
        PrintWriter out = response.getWriter();
        SafetyManage safetyManage = serviceSession.getService(SafetyManage.class);
        final ConfigureProvider configureProvider =
            ResourceRegister.getResourceProvider(getServletContext()).getResource(ConfigureProvider.class);
        String value = request.getParameter("value");
        String codeType = request.getParameter("ctp");
        //上次需要校验的类型
        String utype = request.getParameter("utp");
        String oldVal = request.getParameter("oldVal");
        if (StringHelper.isEmpty(codeType) || StringHelper.isEmpty(utype))
        {
            logger.error("CheckNewCode.processPost code:00");
            out.write("00");
            return;
        }
        String utypeTemp = utype + "|" + oldVal;
        String istrue = serviceSession.getSession().getAttribute(utypeTemp + "is");
        if (!codeType.equals(utype) && StringHelper.isEmpty(istrue))
        {
            logger.error("CheckNewCode.processPost code:00");
            out.write("00");
            return;
        }
        
        //校验对象
        String evenString = request.getParameter("evencheck");
        String code = request.getParameter("code");
        if (StringHelper.isEmpty(evenString) || StringHelper.isEmpty(value))
        {
            logger.error("CheckNewCode.processPost code:01");
            out.write("01");
            return;
        }
        else if (StringHelper.isEmpty(code))
        {
            logger.error("CheckNewCode.processPost code:02");
            out.write("02");
            return;
        }
        else
        {
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
                Integer ecount = safetyManage.phoneMatchVerifyCodeErrorCount(oldVal, sendType);
                if (ecount >= Integer.parseInt(configureProvider.getProperty(MessageVariable.PHONE_VERIFYCODE_MAX_ERROR_COUNT)))
                {
                    logger.error("CheckNewCode.processPost code:05");
                    out.write("05");
                    return;
                }
            }
            Session session = serviceSession.getSession();
            VerifyCodeAuthentication verfycode = new VerifyCodeAuthentication();
            codeType = codeType + "|" + value;
            verfycode.setVerifyCodeType(codeType);
            verfycode.setVerifyCode(code);
            try
            {
                session.authenticateVerifyCode(verfycode);
            }
            catch (AuthenticationException e)
            {
                if (sendType != null)
                {
                    //插入手机验证码匹配错误表
                    safetyManage.insertPhoneMatchVerifyCodeError(oldVal, sendType, code);
                }
                logger.error("CheckNewCode.processPost code:03");
                out.write("03");
                return;
            }
            session.invalidVerifyCode(codeType);
            if ("emil".equals(evenString))
            {
                if (safetyManage.isEmil(value))
                {
                    logger.error("CheckNewCode.processPost code:04");
                    out.write("04");
                    return;
                }
                safetyManage.updateEmil(value);
            }
            else if ("phone".equals(evenString))
            {
                if (safetyManage.isPhone(value))
                {
                    logger.error("CheckNewCode.processPost code:04");
                    out.write("04");
                    return;
                }
                safetyManage.updatePhone(value);
            }
            logger.info("CheckNewCode.processPost success");
            serviceSession.getSession().removeAttribute(utypeTemp + "is");
        }
        
    }
    
}
