package com.dimeng.p2p.user.servlets.account;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.config.Envionment;
import com.dimeng.framework.http.servlet.Controller;
import com.dimeng.framework.http.session.Session;
import com.dimeng.framework.http.session.authentication.AuthenticationException;
import com.dimeng.framework.http.session.authentication.VerifyCodeAuthentication;
import com.dimeng.framework.resource.ResourceProvider;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.p2p.account.user.service.EmailVerifyCodeManage;
import com.dimeng.p2p.account.user.service.SafetyManage;
import com.dimeng.p2p.account.user.service.entity.Safety;
import com.dimeng.p2p.common.DESEncryptor;
import com.dimeng.p2p.common.EmailTypeUtil;
import com.dimeng.p2p.common.FormToken;
import com.dimeng.p2p.common.PhoneTypeUtil;
import com.dimeng.p2p.service.EmailSenderManageExt;
import com.dimeng.p2p.service.SmsSenderManageExt;
import com.dimeng.p2p.variables.defines.EmailVariavle;
import com.dimeng.p2p.variables.defines.MessageVariable;
import com.dimeng.p2p.variables.defines.MsgVariavle;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.DateTimeParser;

public class Send extends AbstractAccountServlet
{
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processPost(final HttpServletRequest request, HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        String currentTokenValue = request.getParameter("tokenKey");
        if (!FormToken.verify(serviceSession.getSession(), currentTokenValue))
        {
            throw new LogicalException("请不要重复提交请求！");
        }
        String tokenNew = FormToken.hidden(serviceSession.getSession());
        
        PrintWriter out = response.getWriter();
        String emil = request.getParameter("emil");
        String phone = request.getParameter("phone");
        String type = request.getParameter("type"); //发送邮件类型
        String verifyType = request.getParameter("verifyType"); //验证码类型
        String verifyCode = request.getParameter("verifyCode"); //验证码
        Session session = serviceSession.getSession();
        
        String utypeTemp1 = "";
        
        VerifyCodeAuthentication verfycode = new VerifyCodeAuthentication();
        verfycode.setVerifyCodeType(verifyType);
        verfycode.setVerifyCode(verifyCode);
        ConfigureProvider configureProvider = getResourceProvider().getResource(ConfigureProvider.class);
        EmailVerifyCodeManage evcManage = serviceSession.getService(EmailVerifyCodeManage.class);
        String verifySfxyyzm = configureProvider.getProperty(SystemVariable.SFXYYZM);
        //是否需要输入验证码:false不需要
        try
        {
            if (!"false".equalsIgnoreCase(verifySfxyyzm))
            {
                session.authenticateVerifyCode(verfycode);
            }
        }
        catch (AuthenticationException e)
        {
            out.write("[{'num':2,'msg':'无效的验证码或验证码已过期','tokenNew':'" + tokenNew + "'}]");
            return;
        }
        session.invalidVerifyCode(verifyType); //校验通过后，置为失效
        SafetyManage safetyManage = serviceSession.getService(SafetyManage.class);
        Safety data = safetyManage.get();
        String senType = "";
        //获取访问者ip地址
        Controller controller = serviceSession.getController();
        String ip = controller.getRemoteAddr(request);
        if (!StringHelper.isEmpty(emil))
        {
            safetyManage.udpateSendTotal("emil");
            //EmailSender emailSender = serviceSession.getService(EmailSender.class);
            EmailSenderManageExt emailSender = serviceSession.getService(EmailSenderManageExt.class);
            String tem = "";
            String title = "";
            Integer eType = null;
            String url = null;
            if (!StringHelper.isEmpty(data.emil) && !"NEW_EMAIL".equals(verifyType))
            {
                emil = data.emil;
            }
            // 绑定邮箱时发送邮件和code
            if ("bind".equals(type))
            {
                eType = EmailTypeUtil.getEmailType(type);
                if (safetyManage.isEmil(emil))
                {
                    out.write("[{'num':4,'msg':'邮箱已存在!','tokenNew':'" + tokenNew + "'}]");
                    return;
                }
                else
                {
                    title =
                        configureProvider.getProperty(SystemVariable.SITE_NAME)
                            + EmailVariavle.RIGISTER_VERIFIY_LINK.getDescription();
                    tem = configureProvider.getProperty(EmailVariavle.RIGISTER_VERIFIY_LINK);
                    senType = type + "|" + emil;
                }
            }
            // 通过旧邮箱修改时给旧邮箱发送邮件和code
            if ("update".equals(type))
            {
                eType = EmailTypeUtil.getEmailType(type);
                title =
                    configureProvider.getProperty(SystemVariable.SITE_NAME)
                        + EmailVariavle.UPDATE_OLD_LINK.getDescription();
                tem = configureProvider.getProperty(EmailVariavle.UPDATE_OLD_LINK);
                senType = type + "|" + data.emil;
                emil = data.emil;
            }
            // 修改邮箱：验证新邮箱(包括通过老邮箱、手机号两种方式)
            if ("new".equals(type) || "pnew".equals(type))
            {
                String value = request.getParameter("oldVal");//第一步操作的手机号或邮箱
                String codeType = request.getParameter("utp");//第u一步操作的标识
                String utypeTemp = codeType + "|" + value;
                utypeTemp1 = utypeTemp + "is";
                String istrue = serviceSession.getSession().getAttribute(utypeTemp1);
                if (!codeType.equals(type) && StringHelper.isEmpty(istrue))
                {
                    out.write("[{'num':0,'msg':'请先进行第一步操作','tokenNew':'" + tokenNew + "'}]");
                    return;
                }
                
                eType = EmailTypeUtil.getEmailType(type);
                if (safetyManage.isEmil(emil))
                {
                    out.write("[{'num':0,'msg':'邮箱已存在','tokenNew':'" + tokenNew + "'}]");
                    return;
                }
                else
                {
                    title =
                        configureProvider.getProperty(SystemVariable.SITE_NAME)
                            + EmailVariavle.UPDATE_NEW_LINK.getDescription();
                    tem = configureProvider.getProperty(EmailVariavle.UPDATE_NEW_LINK);
                    senType = type + "|" + emil;
                }
            }
            if (eType != null)
            {
                //获取用户今天已发送邮箱验证码次数
                Integer ucount = safetyManage.userSendEmailCount(eType);
                if (ucount >= Integer.parseInt(configureProvider.getProperty(MessageVariable.USER_SEND_MAX_COUNT)))
                {
                    out.write("[{'num':0,'msg':'此功能今天发送邮箱验证码次数已达上限!','tokenNew':'" + tokenNew + "'}]");
                    return;
                }
            }
            String code = evcManage.getVerifyCode(serviceSession.getSession().getAccountId(), senType);
            safetyManage.updateT6118Email(emil);
            
            if ("bind".equals(type))
            {
                String key = DESEncryptor.encrypt(emil + "&" + serviceSession.getSession().getAccountId() + "&" + code);
                url =
                    request.getScheme() + "://" + configureProvider.getProperty(SystemVariable.SITE_DOMAIN)
                        + "/email/bindEmailCallBack.htm?key=" + key;
            }
            if ("new".equals(type) || "pnew".equals(type))
            {
                String key =
                    DESEncryptor.encrypt(emil + "&" + serviceSession.getSession().getAccountId() + "&" + code + "&"
                        + type);
                url =
                    request.getScheme() + "://" + configureProvider.getProperty(SystemVariable.SITE_DOMAIN)
                        + "/email/updateEmailCallBack.htm?key=" + key;
            }
            Envionment envionment = configureProvider.createEnvionment();
            //envionment.set("name", safetyManage.get().name);
            envionment.set("date", DateTimeParser.format(new Timestamp(System.currentTimeMillis())));
            envionment.set("url", url);
            logger.info(url);
            if (eType != null)
            {
                emailSender.send(eType, title, StringHelper.format(tem, envionment), emil);
            }
        }
        if (!StringHelper.isEmpty(phone))
        {
            Integer pType = null;
            safetyManage.udpateSendTotal("phone");
            //SmsSender emailSender = serviceSession.getService(SmsSender.class);
            SmsSenderManageExt emailSender = serviceSession.getService(SmsSenderManageExt.class);
            if (!StringHelper.isEmpty(data.phoneNumber) && !"NEW_PHONE".equals(verifyType))
            {
                phone = data.phoneNumber;
            }
            String tem = "";
            // 绑定手机号时发送短信和code
            if ("bind".equals(type))
            {
                pType = PhoneTypeUtil.getPhoneType(type);
                if (safetyManage.isPhone(phone))
                {
                    out.write("[{'num':4,'msg':'手机已存在!','tokenNew':'" + tokenNew + "'}]");
                    return;
                }
                else
                {
                    tem = configureProvider.getProperty(MsgVariavle.RIGISTER_VERIFIY_CODE);
                    senType = type + "|" + phone;
                }
            }
            // 通过手机修改邮箱
            if ("phoneemil".equals(type))
            {
                pType = PhoneTypeUtil.getPhoneType(type);
                tem = configureProvider.getProperty(MsgVariavle.UPDATE_EMAIL_CODE);
                senType = type + "|" + data.phoneNumber;
                phone = data.phoneNumber;
            }
            //修改手机号：验证验手机
            if ("update".equals(type))
            {
                pType = PhoneTypeUtil.getPhoneType(type);
                tem = configureProvider.getProperty(MsgVariavle.RIGISTER_VERIFIY_CODE);
                senType = type + "|" + data.phoneNumber;
                phone = data.phoneNumber;
            }
            //通过手机找回交易密码
            if ("getoldpas".equals(type))
            {
                pType = PhoneTypeUtil.getPhoneType(type);
                tem = configureProvider.getProperty(MsgVariavle.FIND_PASSWORD);
                senType = type + "|" + phone;
            }
            //通过手机找回密保问题
            if ("securitypwd".equals(type))
            {
                pType = PhoneTypeUtil.getPhoneType(type);
                tem = configureProvider.getProperty(MsgVariavle.UPDATE_SCURITY_PHONE_CODE);
                senType = type + "|" + phone;
            }
            if ("".equals(type))
            {
                pType = PhoneTypeUtil.getPhoneType(type);
            }
            //修改手机：验证新手机(包括通过原手机、身份证两种验证方式)
            if ("new".equals(type) || "cnew".equals(type))
            {
                pType = PhoneTypeUtil.getPhoneType(type);
                if (safetyManage.isPhone(phone))
                {
                    out.write("[{'num':0,'msg':'手机号码已存在','tokenNew':'" + tokenNew + "'}]");
                    return;
                }
                else
                {
                    tem = configureProvider.getProperty(MsgVariavle.RIGISTER_VERIFIY_CODE);
                    senType = type + "|" + phone;
                }
            }
            if (pType != null)
            {
                //获取用户今天已发送手机验证码次数
                Integer ucount = safetyManage.userSendPhoneCount(pType);
                if (ucount >= Integer.parseInt(configureProvider.getProperty(MessageVariable.USER_SEND_MAX_COUNT)))
                {
                    out.write("[{'num':0,'msg':'此功能今天发送手机验证码次数已达上限!','tokenNew':'" + tokenNew + "'}]");
                    return;
                }
                
                //获取IP地址今天已发送手机验证码次数
                Integer ipcount = safetyManage.iPAddrSendSmsCount(ip, pType);
                if (ipcount >= Integer.parseInt(configureProvider.getProperty(MessageVariable.IP_SEND_MAX_COUNT)))
                {
                    out.write("[{'num':0,'msg':'当前IP地址，此功能今天发送手机验证码次数已达上限!','tokenNew':'" + tokenNew + "'}]");
                    return;
                }
                
                /*判断了用户一天的发送量，那么下面的判读就可以去掉
                 * //获取今天此手机已发送验证码次数
                Integer count = safetyManage.bindPhoneCount(phone, pType);
                if (count >= Integer.valueOf(configureProvider.getProperty(MessageVariable.PHONE_MAX_COUNT)))
                {
                    StringBuilder sb = new StringBuilder();
                    sb.append("[{'num':00,'msg':'");
                    sb.append("当前手机号码当天获取验证码次数已达上限！" + "'}]");
                    out.write(sb.toString());
                    return;
                }*/
            }
            
            Envionment envionment = configureProvider.createEnvionment();
            String code = session.getVerifyCode(senType);
            envionment.set("date", DateTimeParser.format(new Timestamp(System.currentTimeMillis())));
            envionment.set("code", code);
            if (pType != null)
            {
                emailSender.send(pType, StringHelper.format(tem, envionment), ip, phone);
            }
            logger.info(code);

            //移除“第一步验证”标记
            if (!StringHelper.isEmpty(utypeTemp1))
            {
                serviceSession.getSession().removeAttribute(utypeTemp1);
            }
        }
        out.write("[{'num':1,'msg':'sussess','tokenNew':'" + tokenNew + "'}]");
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
        }
        else
        {
            sb.append("[{'num':0,'msg':'");
            sb.append(throwable.getMessage() + "'}]");
        }
        out.write(sb.toString());
    }
    
}
