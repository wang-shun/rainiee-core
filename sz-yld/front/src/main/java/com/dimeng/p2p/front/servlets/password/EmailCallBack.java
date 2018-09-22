package com.dimeng.p2p.front.servlets.password;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.http.session.Session;
import com.dimeng.framework.resource.ResourceRegister;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.account.front.service.PasswordManage;
import com.dimeng.p2p.account.user.service.EmailVerifyCodeManage;
import com.dimeng.p2p.account.user.service.SafetyManage;
import com.dimeng.p2p.common.DESEncryptor;
import com.dimeng.p2p.common.EmailTypeUtil;
import com.dimeng.p2p.variables.defines.MessageVariable;
import com.dimeng.util.StringHelper;

public class EmailCallBack extends AbstractPasswordServlet
{
    
    private static final long serialVersionUID = 1L;
    
    private final String typeFlagEmail = "0"; //邮箱
    
    @Override
    protected void processGet(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        processPost(request, response, serviceSession);
    }
    
    @Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        logger.info("邮箱找回密保回调  start");
        String key = request.getParameter("key");
        if (!StringHelper.isEmpty(key))
        {
            try
            {
                key = DESEncryptor.decrypt(request.getParameter("key"));
            }
            catch (Exception e)
            {
                logger.info(e);
                request.setAttribute("EMAIL_ERROR", "无效的邮件认证链接地址或邮件链接不完整!");
                forward(request, response, getController().getViewURI(request, Index.class));
                return;
            }
        }
        else
        {
            request.setAttribute("EMAIL_ERROR", "无效的邮件认证链接地址!");
            forward(request, response, getController().getViewURI(request, Index.class));
            return;
        }
        logger.info("解密成功,解密后key为：" + key);
        //key解密后,字符串顺序是：email&code&type&accountType
        String[] body = key.split("&");
        if (body.length < 4)
        {
            request.setAttribute("EMAIL_ERROR", "无效的邮件认证链接地址!");
            forward(request, response, getController().getViewURI(request, Index.class));
            return;
        }
        String email = body[0];
        String type = body[2];
        String verifyCode = body[1];
        String acountType = body[3];
        int id = 0;
        
        Session session = serviceSession.getSession();
        PasswordManage passwordManage = serviceSession.getService(PasswordManage.class);
        SafetyManage safetyManage = serviceSession.getService(SafetyManage.class);
        EmailVerifyCodeManage evcManage = serviceSession.getService(EmailVerifyCodeManage.class);
        ConfigureProvider configureProvider =
            ResourceRegister.getResourceProvider(getServletContext()).getResource(ConfigureProvider.class);
        //boolean isPsdSafe = Boolean.parseBoolean(configureProvider.getProperty(SystemVariable.SFXYMBAQNZ));
        if (!typeFlagEmail.equals(type))
        {
            sendRedirect(request, response, getController().getViewURI(request, Index.class));
            return;
        }
        
        //1.1  认证的邮箱不能为空
        if (StringHelper.isEmpty(email))
        {
            request.setAttribute("EMAIL_ERROR", "邮箱地址不能为空");
            forward(request, response, getController().getViewURI(request, Index.class));
            return;
        }
        
        //1.2 判断邮箱地址是否有对应的用户
        id = passwordManage.emailExist(email, acountType);
        if (id <= 0)
        {
            request.setAttribute("EMAIL_ERROR", "邮箱未被注册");
            forward(request, response, getController().getViewURI(request, Index.class));
            return;
        }
        
        //1.3 获取此邮箱今天已发送验证码次数
        Integer count = safetyManage.sendEmailCount(email, EmailTypeUtil.getEmailType("emailpwd"));
        if (count >= Integer.parseInt(configureProvider.getProperty(MessageVariable.PHONE_MAX_COUNT)))
        {
            request.setAttribute("EMAIL_ERROR", "此邮箱当天获取验证码次数已达上限！");
            forward(request, response, getController().getViewURI(request, Index.class));
            return;
        }
        
        //1.4 查询数据库已经错误次数
        Integer ecount = safetyManage.phoneMatchVerifyCodeErrorCount(email, EmailTypeUtil.getEmailType("emailpwd"));
        //和设置的常量比较
        if (ecount >= Integer.parseInt(configureProvider.getProperty(MessageVariable.PHONE_VERIFYCODE_MAX_ERROR_COUNT)))
        {
            request.setAttribute("EMAIL_ERROR", "此邮箱当天匹配验证码错误次数已达上限！");
            forward(request, response, getController().getViewURI(request, Index.class));
            return;
        }
        
        //1.5 判断验证码是否错误或者已经过期
        String codeType = "emailpwd|" + email;
        try
        {
            evcManage.authenticateVerifyCode(id, codeType, verifyCode);
        }
        catch (Exception ex)
        {
            // 插入验证码匹配错误表
            safetyManage.insertPhoneMatchVerifyCodeError(email, EmailTypeUtil.getEmailType("emailpwd"), verifyCode);
            request.setAttribute("EMAIL_ERROR", ex.getMessage());
            forward(request, response, getController().getViewURI(request, Index.class));
            return;
        }
        evcManage.invalidVerifyCode(id, codeType);
        //校验成功后，标记校验状态
        request.setAttribute(Integer.toString(id), "CHECK_SUCCESS");
        request.setAttribute("PASSWORD_ACCOUNT_ID", String.valueOf(id));
        request.setAttribute("RESET_FLAG", "0");
        request.setAttribute("EMAIL_PWD", email);
        
        logger.info("邮箱找回密保回调  end");
        /*if(isPsdSafe){
        	forward(request, response, getController().getViewURI(request, Mmbh.class));
        }else{*/
        forward(request, response, getController().getViewURI(request, Reset.class));
        /*}*/
    }
}
