/*
 * 文 件 名:  ResetCheckVerifyCode.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  zhoulantao
 * 修改时间:  2015年11月11日
 */
package com.dimeng.p2p.app.servlets.platinfo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.http.session.Session;
import com.dimeng.framework.http.session.SessionManager;
import com.dimeng.framework.http.session.authentication.AuthenticationException;
import com.dimeng.framework.http.session.authentication.VerifyCodeAuthentication;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.account.user.service.SafetyManage;
import com.dimeng.p2p.app.service.AbstractVerifyCodeService;
import com.dimeng.p2p.app.servlets.AbstractAppServlet;
import com.dimeng.p2p.common.PhoneTypeUtil;
import com.dimeng.p2p.variables.defines.MessageVariable;
import com.dimeng.util.StringHelper;

/**
 * 检查验证码是否正确(适用于找回密码、注册)
 * 
 * @author zhoulantao
 * @version [版本号, 2015年11月11日]
 */
public class ResetCheckVerifyCode extends AbstractAppServlet
{
    private static final long serialVersionUID = 5524249215756520750L;
    
    @Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        // 获取验证码
        final String verifyCode = getParameter(request, "verifyCode");
        
        // 获取类型 RZ:认证 ZHZF:找回支付
        final String type = getParameter(request, "type");
        final String phone = getParameter(request, "phone");
        
        // 判断手机号码不能为空
        if (StringHelper.isEmpty(phone))
        {
            // 封装返回信息
            setReturnMsg(request, response, ExceptionCode.PARAMETER_ERROR, "手机不能为空！");
            return;
        }
        
        // 校验验证码是否为空
        if (StringHelper.isEmpty(verifyCode))
        {
            // 封装返回信息
            setReturnMsg(request, response, ExceptionCode.PASSWORD_DIFFERENT_ERROR, "手机验证码错误！");
            return;
        }
        
        SafetyManage safetyManage = serviceSession.getService(SafetyManage.class);
        ConfigureProvider configureProvider = getResourceProvider().getResource(ConfigureProvider.class);
        
        // 注册时需要判断手机号码是否在系统中已存在
        String eType = "";
        if (GetMobileCode.RZ.equals(type) || GetMobileCode.FXRZ.equals(type))
        {
            eType = "bind";
            if (safetyManage.isPhone(phone))
            {
                // 增加判断，防止手机号码被抢注后，重复注册！
                setReturnMsg(request, response, ExceptionCode.PHONE_EXIST_ERRROR, "手机号码已注册，你可以用它直接登录！");
                return;
            }
        }
        else if (GetMobileCode.ZHZF.equals(type))
        {
            eType = "getoldpas";
        }
        
        // 封装验证验证码的对象
        VerifyCodeAuthentication authentication = new VerifyCodeAuthentication();
        
        // 说明:此处是根据在获取验证码时的类型来校验验证码，type不要填错了
        authentication.setVerifyCodeType("P" + type + "|" + phone);
        authentication.setVerifyCode(verifyCode);
        
        Session session = serviceSession.getSession();
        
        // 如果session为空，则重新创建一个
        if (session == null)
        {
            session = getResourceProvider().getResource(SessionManager.class).getSession(request, response, true);
        }
        
        // 获取用户今天已发送邮箱验证码次数
        int phoneType = PhoneTypeUtil.getPhoneType(eType);
        
        //当日该手机与验证码匹配错误次数
        Integer ecount = safetyManage.phoneMatchVerifyCodeErrorCount(phone, phoneType);
        if (ecount >= Integer.valueOf(configureProvider.getProperty(MessageVariable.PHONE_VERIFYCODE_MAX_ERROR_COUNT)))
        {
            // 封装返回信息
            setReturnMsg(request, response, ExceptionCode.PHONE_VERIFYCODE_MAX_ERROR_COUNT, "当前手机号码当天匹配手机验证码错误次数已达上限!");
            return;
        }
        
        try
        {
            // 校验验证码是否正确
//            session.authenticateVerifyCode(authentication);
            
            // 用重写的校验验证码的方法
            AbstractVerifyCodeService verifyCodeService = serviceSession.getService(AbstractVerifyCodeService.class);
//            Cookie[] cookies = request.getCookies();
//            String cookie = verifyCodeService.getCookie(cookies);
            String cookie = serviceSession.getSession().getToken();
            verifyCodeService.authenticateVerifyCode(authentication, cookie);
        }
        catch (AuthenticationException e)
        {
            // 插入手机号码匹配验证码错误次数
            safetyManage.insertPhoneMatchVerifyCodeError(phone, phoneType, verifyCode);
            
            // 封装返回信息
            setReturnMsg(request, response, ExceptionCode.VERIFY_CODE_ERROR, "手机验证码错误");
            return;
        }
        
        // 封装成功信息
        setReturnMsg(request, response, ExceptionCode.SUCCESS, "success!");
        return;
    }
    
    @Override
    protected void processGet(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        processPost(request, response, serviceSession);
    }
    
}
