package com.dimeng.p2p.app.servlets.user;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.http.session.Session;
import com.dimeng.framework.http.session.authentication.AuthenticationException;
import com.dimeng.framework.http.session.authentication.VerifyCodeAuthentication;
import com.dimeng.framework.resource.ResourceProvider;
import com.dimeng.framework.resource.ResourceRegister;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.S61.enums.T6118_F04;
import com.dimeng.p2p.account.user.service.IndexManage;
import com.dimeng.p2p.account.user.service.SafetyManage;
import com.dimeng.p2p.account.user.service.entity.UserBaseInfo;
import com.dimeng.p2p.app.servlets.AbstractSecureServlet;
import com.dimeng.p2p.app.servlets.platinfo.ExceptionCode;
import com.dimeng.p2p.common.EmailTypeUtil;
import com.dimeng.p2p.variables.defines.MessageVariable;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.BooleanParser;

/**
 * 用户信息
 * 
 * @author tanhui
 */
public class SetUserEmail extends AbstractSecureServlet
{
    
    /**发送邮箱认证验证码*/
    private final String RZ = "RZ";
    
    private static final long serialVersionUID = 1L;
    
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
        ResourceProvider resourceProvider = ResourceRegister.getResourceProvider(request.getServletContext());
        ConfigureProvider configureProvider = resourceProvider.getResource(ConfigureProvider.class);
        SafetyManage safetyManage = serviceSession.getService(SafetyManage.class);
        
        //邮箱设置
        String email = getParameter(request, "email");
        String emailCode = getParameter(request, "emailCode");
        String type = getParameter(request, "type");
        if (StringHelper.isEmpty(type))
        {
            // 封装返回信息
            setReturnMsg(request, response, ExceptionCode.VERIFY_TYPE_NO_EMPTY_ERRROR, "验证码类型不能为空");
            return;
        }
        if (StringHelper.isEmpty(email))
        {
            // 封装返回信息
            setReturnMsg(request, response, ExceptionCode.EMAIL_ERRROR, "邮箱不能为空");
            return;
        }
        else if (StringHelper.isEmpty(emailCode))
        {
            // 封装返回信息
            setReturnMsg(request, response, ExceptionCode.EMAIL_VERIFY_ERRROR, "验证码不能为空");
            return;
        }
        else
        {
            Integer sendType = EmailTypeUtil.getEmailType("bind");
            // 当日该手机与验证码匹配错误次数
            Integer ecount = safetyManage.phoneMatchVerifyCodeErrorCount(email, sendType);
            if (ecount >= Integer.valueOf(configureProvider.getProperty(MessageVariable.PHONE_VERIFYCODE_MAX_ERROR_COUNT)))
            {
                // 封装返回信息
                setReturnMsg(request, response, ExceptionCode.PHONE_VERIFY_ERRROR, "此邮箱当天匹配验证码错误次数已达上限！");
                return;
            }
            
            Session session = serviceSession.getSession();
            VerifyCodeAuthentication verfycode = new VerifyCodeAuthentication();
            verfycode.setVerifyCodeType("E" + RZ + "|" + email);
            verfycode.setVerifyCode(emailCode);
            try
            {
                session.authenticateVerifyCode(verfycode);
            }
            catch (AuthenticationException e)
            {
                if (sendType != null)
                {
                    // 插入验证码匹配错误表
                    safetyManage.insertPhoneMatchVerifyCodeError(email, sendType, emailCode);
                }
                setReturnMsg(request, response, ExceptionCode.EMAIL_VERIFY_ERRROR, e.getMessage());
                return;
            }
            if (safetyManage.isEmil(email))
            {
                // 封装返回信息
                setReturnMsg(request, response, ExceptionCode.EMAIL_EXIST_ERRROR, "邮箱已经存在");
                return;
            }
            safetyManage.bindEmil(email, T6118_F04.TG.name());
            session.invalidVerifyCode("E" + RZ + "|" + email);
        }
        
        // 是否全部认证
        boolean isAllVerify = true;
        IndexManage manage = serviceSession.getService(IndexManage.class);
        
        boolean tg = BooleanParser.parse(configureProvider.getProperty(SystemVariable.SFZJTG));
        UserBaseInfo userBaseInfo = null;
        if (tg)
        {
            userBaseInfo = manage.getUserBaseInfoTx();
        }
        else
        {
            userBaseInfo = manage.getUserBaseInfo();
        }
        if (!userBaseInfo.realName)
        {
            isAllVerify = false;
        }
        if (!userBaseInfo.phone)
        {
            isAllVerify = false;
        }
        if (!userBaseInfo.email)
        {
            isAllVerify = false;
        }
        if (!tg && !userBaseInfo.withdrawPsw)
        {
            isAllVerify = false;
        }
        
        // 封装返回信息
        setReturnMsg(request, response, ExceptionCode.SUCCESS, "success!", isAllVerify);
        return;
    }
}
