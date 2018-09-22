package com.dimeng.p2p.app.servlets.platinfo;

import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.config.Envionment;
import com.dimeng.framework.message.email.EmailSender;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.account.user.service.EmailVerifyCodeManage;
import com.dimeng.p2p.account.user.service.SafetyManage;
import com.dimeng.p2p.app.servlets.AbstractSecureServlet;
import com.dimeng.p2p.common.DESEncryptor;
import com.dimeng.p2p.common.EmailTypeUtil;
import com.dimeng.p2p.variables.defines.EmailVariavle;
import com.dimeng.p2p.variables.defines.MessageVariable;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.DateTimeParser;

/**
 * 
 * 发送绑定邮箱请求
 * 
 * @author  zhoulantao
 * @version  [版本号, 2015年12月18日]
 */
public class GetEmailCode extends AbstractSecureServlet
{
    private static final long serialVersionUID = 1L;
    
    /**发送邮箱认证验证码*/
    private final String RZ = "RZ";
    
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
        String email = getParameter(request, "email");
        String type = getParameter(request, "type");
        ConfigureProvider configureProvider = getResourceProvider().getResource(ConfigureProvider.class);
        EmailSender emailSender = serviceSession.getService(EmailSender.class);
        
        if (StringHelper.isEmpty(email))
        {
            // 封装返回信息
            setReturnMsg(request, response, ExceptionCode.PARAMETER_ERROR, "邮箱不能为空！");
            return;
        }
        
        SafetyManage safetyManage = serviceSession.getService(SafetyManage.class);
        
        String template = configureProvider.getProperty(EmailVariavle.RIGISTER_VERIFIY_LINK);
        String eType = "";
        if (RZ.equals(type))
        {
            eType = "bind";
            if (safetyManage.isEmil(email))
            {
                // 封装返回信息
                setReturnMsg(request, response, ExceptionCode.EMAIL_EXIST_ERRROR, "邮箱已经存在");
                return;
            }
        }
        else
        {
            // 封装返回信息
            setReturnMsg(request, response, ExceptionCode.VERIFY_TYPE_ERRROR, "验证码类型错误");
            return;
        }
        
        // 获取用户今天已发送邮箱验证码次数
        final int emailType = EmailTypeUtil.getEmailType(eType);
        Integer ucount = safetyManage.userSendEmailCount(emailType);
        
        if (ucount >= Integer.valueOf(configureProvider.getProperty(MessageVariable.USER_SEND_MAX_COUNT)))
        {
            // 封装返回信息
            setReturnMsg(request, response, ExceptionCode.UNKNOWN_ERROR, "此功能今天发送邮箱验证码次数已达上限！");
            return;
        }
        
        // 获取验证码
        final String senType = "bind|" + email;
        EmailVerifyCodeManage evcManage = serviceSession.getService(EmailVerifyCodeManage.class);
        String verifyCode = evcManage.getVerifyCode(serviceSession.getSession().getAccountId(), senType);
        
        // 绑定邮箱发送验证码更新邮箱地址
        safetyManage.updateT6118Email(email);
        
        // 封装邮箱的回调地址
        final String key =
            DESEncryptor.encrypt(email + "&" + serviceSession.getSession().getAccountId() + "&" + verifyCode);
        final String url = getSiteDomain("/email/bindEmailCallBack.htm?key=" + key);
        
        // 封装邮件模板，发送邮件
        Envionment envionment = configureProvider.createEnvionment();
        envionment.set("date", DateTimeParser.format(new Timestamp(System.currentTimeMillis())));
        envionment.set("url", url);
        logger.info(url);
        String title =
            configureProvider.getProperty(SystemVariable.SITE_NAME)
                + EmailVariavle.RIGISTER_VERIFIY_LINK.getDescription();
        emailSender.send(emailType, title, StringHelper.format(template, envionment), email);
        
        // 封装返回信息
        setReturnMsg(request, response, ExceptionCode.SUCCESS, "success");
        return;
    }
}
