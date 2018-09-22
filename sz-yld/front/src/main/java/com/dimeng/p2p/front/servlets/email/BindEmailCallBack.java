/*
 * 文 件 名:  BindEmailCallBack.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  xiaoqi
 * 修改时间:  2015年11月7日
 */
package com.dimeng.p2p.front.servlets.email;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.http.session.authentication.AuthenticationException;
import com.dimeng.framework.resource.ResourceRegister;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.S61.enums.T6106_F05;
import com.dimeng.p2p.S61.enums.T6118_F04;
import com.dimeng.p2p.account.user.service.EmailVerifyCodeManage;
import com.dimeng.p2p.account.user.service.SafetyManage;
import com.dimeng.p2p.common.DESEncryptor;
import com.dimeng.p2p.common.EmailTypeUtil;
import com.dimeng.p2p.repeater.score.SetScoreManage;
import com.dimeng.p2p.variables.defines.MallVariavle;
import com.dimeng.p2p.variables.defines.MessageVariable;
import com.dimeng.util.StringHelper;

/**
 * 用户中心用户绑定邮箱连接回调
 * 
 * @author xiaoqi
 * @version [版本号, 2015年11月7日]
 */
public class BindEmailCallBack extends AbstractEmailServlet
{
    
    private static final long serialVersionUID = 1L;
    
    /** {@inheritDoc} */
    
    @Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        
        logger.info("绑定邮箱验证邮箱链接地址开始!");
        ConfigureProvider configureProvider =
            ResourceRegister.getResourceProvider(getServletContext()).getResource(ConfigureProvider.class);
        SafetyManage safetyManage = serviceSession.getService(SafetyManage.class);
        EmailVerifyCodeManage evcManage = serviceSession.getService(EmailVerifyCodeManage.class);
        String des = null;
        if (!StringHelper.isEmpty(request.getParameter("key")))
        {
            try
            {
                des = DESEncryptor.decrypt(request.getParameter("key"));
            }
            catch (Exception ex)
            {
                logger.info(ex);
                request.setAttribute("status", "fail");
                request.setAttribute("message", "无效的邮件认证链接地址或者认证链接不完整!");
                forward(request, response, getController().getViewURI(request, BindEmailCallBack.class));
                return;
            }
        }
        String key = StringHelper.isEmpty(request.getParameter("key")) ? "" : des;
        if (StringHelper.isEmpty(key))
        {
            request.setAttribute("status", "fail");
            request.setAttribute("message", "无效的邮件认证链接地址!");
            forward(request, response, getController().getViewURI(request, BindEmailCallBack.class));
            return;
        }
        String[] body = key.split("&");
        if (body.length < 3)
        {
            request.setAttribute("status", "fail");
            request.setAttribute("message", "无效的邮件认证链接地址!");
            forward(request, response, getController().getViewURI(request, BindEmailCallBack.class));
            return;
        }
        int uid = 0;
        String email = body[0];
        String verifyCode = body[2];
        try
        {
            uid = Integer.parseInt(body[1]);
        }
        catch (NumberFormatException e)
        {
            request.setAttribute("status", "fail");
            request.setAttribute("message", "无效的邮件认证链接地址!");
            forward(request, response, getController().getViewURI(request, BindEmailCallBack.class));
            return;
        }
        if (StringHelper.isEmpty(email) || StringHelper.isEmpty(verifyCode) || uid == 0)
        {
            request.setAttribute("status", "fail");
            request.setAttribute("message", "认证邮件链接已过期或当前邮箱已被认证!");
            forward(request, response, getController().getViewURI(request, BindEmailCallBack.class));
            return;
        }
        
        if (!safetyManage.getEmailByUserId(email, uid))
        {
            request.setAttribute("status", "fail");
            request.setAttribute("message", "认证邮件链接已过期或当前邮箱已被认证!");
            forward(request, response, getController().getViewURI(request, BindEmailCallBack.class));
            return;
        }
        
        Integer sendType = EmailTypeUtil.getEmailType("bind");
        if (sendType != null)
        {
            // 当日该邮箱与验证码匹配错误次数
            Integer ecount = safetyManage.phoneMatchVerifyCodeErrorCount(email, sendType);
            if (ecount >= Integer.parseInt(configureProvider.getProperty(MessageVariable.PHONE_VERIFYCODE_MAX_ERROR_COUNT)))
            {
                request.setAttribute("status", "fail");
                request.setAttribute("message", "此邮箱当天匹配验证码错误次数已达上限");
                forward(request, response, getController().getViewURI(request, BindEmailCallBack.class));
                return;
            }
        }
        
        String codeType = "bind|" + email;
        try
        {
            evcManage.authenticateVerifyCode(uid, codeType, verifyCode);
        }
        catch (AuthenticationException e)
        {
            if (sendType != null)
            {
                // 插入验证码匹配错误表
                safetyManage.insertPhoneMatchVerifyCodeError(email, sendType, verifyCode);
            }
            evcManage.invalidVerifyCode(uid, codeType);
            request.setAttribute("status", "fail");
            request.setAttribute("message", "认证邮件链接地址已过期!");
            forward(request, response, getController().getViewURI(request, BindEmailCallBack.class));
            return;
        }
        if (safetyManage.isEmil(email))
        {
            request.setAttribute("status", "fail");
            request.setAttribute("message", "当前邮箱已被认证!");
            forward(request, response, getController().getViewURI(request, BindEmailCallBack.class));
            return;
        }
        
        boolean is_mall = Boolean.parseBoolean(configureProvider.getProperty(MallVariavle.IS_MALL));
        if (is_mall)
        {
            //邮箱认证赠送积分
            SetScoreManage setScoreManage = serviceSession.getService(SetScoreManage.class);
            setScoreManage.giveScore(null, T6106_F05.mailbox, null);
        }
        safetyManage.bindEmail(email, T6118_F04.TG.name(), uid);
        request.setAttribute("status", "success");
        request.setAttribute("message", "绑定邮箱成功!");
        forward(request, response, getController().getViewURI(request, BindEmailCallBack.class));
        logger.info("绑定邮箱连接地址认证结束!");
    }
    
    @Override
    protected void processGet(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        this.processPost(request, response, serviceSession);
    }
    
}
