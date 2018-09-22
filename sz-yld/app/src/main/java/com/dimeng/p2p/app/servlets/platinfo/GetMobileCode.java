package com.dimeng.p2p.app.servlets.platinfo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.config.Envionment;
import com.dimeng.framework.http.session.Session;
import com.dimeng.framework.http.session.SessionManager;
import com.dimeng.framework.message.sms.SmsSender;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.S61.entities.T6110;
import com.dimeng.p2p.account.user.service.SafetyManage;
import com.dimeng.p2p.account.user.service.UserInfoManage;
import com.dimeng.p2p.account.user.service.UserManage;
import com.dimeng.p2p.app.service.AbstractVerifyCodeService;
import com.dimeng.p2p.app.servlets.AbstractAppServlet;
import com.dimeng.p2p.common.PhoneTypeUtil;
import com.dimeng.p2p.variables.defines.MessageVariable;
import com.dimeng.p2p.variables.defines.MsgVariavle;
import com.dimeng.util.StringHelper;

/**
 * 
 * 获取手机验证码(注册、找回密码)
 * 
 * @author  zhoulantao
 * @version  [版本号, 2015年11月11日]
 */
public class GetMobileCode extends AbstractAppServlet
{
    private static final long serialVersionUID = 1L;
    
    /**
     * 发送手机认证验证码 
     */
    protected static final String RZ = "RZ";
    
    /**
     * 分享注册发送手机认证验证码 
     */
    protected static final String FXRZ = "FXRZ";
    
    /**
     * 注册类型
     */
    public static final int RZ_TYPE = 1;
    
    /**
     * 发送手机交易密码找回验证码
     */
    protected static final String ZHZF = "ZHZF";
    
    /**
     * 找回交易密码类型
     */
    public static final int ZHZF_TYPE = 4;
    
    /**
     * 发送手机第三方认证验证码 
     */
    protected static final String RZ_USR = "RZKH";

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
        Session session = getResourceProvider().getResource(SessionManager.class).getSession(request, response, true);
        
        // 获取手机号码
        String mobile = getParameter(request, "phone");
        
        String rxp = "^[1]([1|3|4|5|6|7|8|9][0-9]{1})[0-9]{8}$";
        if (!mobile.matches(rxp))
        {
            // 封装返回信息
            setReturnMsg(request, response, ExceptionCode.PARAMETER_ERROR, "输入的手机号不符合规范");
            return;
        }
        
        // 获取验证码类型  RZ|ZHZF
        String type = getParameter(request, "type");
        ConfigureProvider configureProvider = getResourceProvider().getResource(ConfigureProvider.class);
        SmsSender sender = serviceSession.getService(SmsSender.class);
        String verifyCode = "";
        
        String template = configureProvider.getProperty(MsgVariavle.UPDATE_NEW_PHONE_CODE);
        
        if (StringHelper.isEmpty(mobile))
        {
            // 封装返回信息
            setReturnMsg(request, response, ExceptionCode.PARAMETER_ERROR, "手机不能为空！");
            return;
        }
        
        // 判断手机号码是否存在
        SafetyManage safetyManage = serviceSession.getService(SafetyManage.class);
        final boolean isPhone = safetyManage.isPhone(mobile);
        
        String eType = "";
        if (RZ.equals(type))
        {
            eType = "bind";
            // 如果实名认证通过，手机号
            if (isPhone)
            {
                // 封装返回信息
                setReturnMsg(request, response, ExceptionCode.PHONE_EXIST_ERRROR, "手机号码已注册，您可以用它直接登录！");
                return;
            }
            template = configureProvider.getProperty(MsgVariavle.RIGISTER_VERIFIY_CODE);
        }
        else if (FXRZ.equals(type))
        {
            eType = "bind";
            if (isPhone)
            {
                // 封装返回信息
                setReturnMsg(request, response, ExceptionCode.PHONE_EXIST_ERRROR, "手机号码已被注册！");
                return;
            }
            template = configureProvider.getProperty(MsgVariavle.RIGISTER_VERIFIY_CODE);
        }
        else if (ZHZF.equals(type))
        {
            eType = "getoldpas";
            if (!isPhone)
            {
                // 返回手机号码未注册
                setReturnMsg(request, response, ExceptionCode.PHONE_NUM_ERRROR, "手机号码未认证！");
                return;
            }
            
            // 校验手机号码是否是本人认证的手机号码
            if (session != null && session.isAuthenticated() && serviceSession.getSession().getAccountId() > 0)
            {
                UserInfoManage uManage = serviceSession.getService(UserInfoManage.class);
                T6110 user = uManage.getUserInfo(serviceSession.getSession().getAccountId());
                
                if (user != null && !mobile.equals(user.F04))
                {
                    // 返回手机号码非本人认证手机号码
                    setReturnMsg(request, response, ExceptionCode.PHONE_NUM_ERRROR, "非本用户绑定手机号码！");
                    return;
                }
            }
            
            // 判断手机号码是否已存在
            template = configureProvider.getProperty(MsgVariavle.FIND_PASSWORD);
        }
        else if (RZ_USR.equals(type))
        { //第三方开户认证必须要登录
            eType = "bind";
            
            String usrId = null;
            if (session != null && session.isAuthenticated() && serviceSession.getSession().getAccountId() > 0)
            {
                
                UserManage userManage = serviceSession.getService(UserManage.class);
                usrId = userManage.getUsrCustId();
            }
            else
            {
                // 返回手机号码非本人认证手机号码
                setReturnMsg(request, response, ExceptionCode.NOLOGING_ERROR, "用户未登录！");
                return;
            }
            // 如果手机号存在，用户未开户
            if (isPhone && !StringHelper.isEmpty(usrId))
            {
                // 封装返回信息
                setReturnMsg(request, response, ExceptionCode.PHONE_EXIST_ERRROR, "手机号码已注册，您可以用它直接登录！");
                return;
            }
            template = configureProvider.getProperty(MsgVariavle.RIGISTER_VERIFIY_CODE);
        }
        else
        {
            // 封装返回信息
            setReturnMsg(request, response, ExceptionCode.VERIFY_TYPE_ERRROR, "发送验证码类型错误！");
            return;
        }
        
        // 获取发送手机验证码类型
        final int phoneType = PhoneTypeUtil.getPhoneType(eType);
        
        // 校验用户手机号码获取验证码次数
        if (serviceSession.getSession().isAuthenticated())
        {
            final Integer ucount = safetyManage.userSendPhoneCount(phoneType);
            if (ucount >= Integer.valueOf(configureProvider.getProperty(MessageVariable.USER_SEND_MAX_COUNT)))
            {
                // 封装返回信息
                setReturnMsg(request, response, ExceptionCode.UNKNOWN_ERROR, "此手机号码当天获取验证码次数已达上限！");
                return;
            }
        }
        else
        {
            // 获取用户今天已发送邮箱验证码次数
            final Integer count = safetyManage.bindPhoneCount(mobile, phoneType);
            if (count >= Integer.valueOf(configureProvider.getProperty(MessageVariable.PHONE_MAX_COUNT)))
            {
                // 封装返回信息
                setReturnMsg(request, response, ExceptionCode.UNKNOWN_ERROR, "此手机号码当天获取验证码次数已达上限！");
                return;
            }
        }
        
        try
        {
            // 注册时用重写的获取验证码的方法
            if (RZ.equals(type) || RZ_USR.equals(type))
            {
                session.invalidVerifyCode("P" + type + "|" + mobile);
                //                verifyCode = session.getVerifyCode("P" + type + "|" + mobile);
                AbstractVerifyCodeService verifyCodeService =
                    serviceSession.getService(AbstractVerifyCodeService.class);
                //                Cookie[] cookies = request.getCookies();
                //                String cookie = verifyCodeService.getCookie(cookies);
                String cookie = serviceSession.getSession().getToken();
                verifyCode = verifyCodeService.getVerifyCode("P" + type + "|" + mobile, null, cookie);
                if ("repeat".equals(verifyCode))
                {
                    setReturnMsg(request, response, ExceptionCode.PRODUCE_VERIFY_ERRROR, "请勿在短时间内重复获取验证码.");
                    return;
                }
            }
            
            // 找回密码时用原框架的获取验证码的方法
            else if (ZHZF.equals(type) || FXRZ.equals(type))
            {
                session.invalidVerifyCode("P" + type + "|" + mobile);
                verifyCode = session.getVerifyCode("P" + type + "|" + mobile);
            }
            
        }
        catch (Exception e)
        {
            log(e.getMessage());
            // 封装返回信息
            setReturnMsg(request, response, ExceptionCode.PRODUCE_VERIFY_ERRROR, "验证码错误：" + e.getMessage());
            return;
        }
        logger.info("GetMobileCode->type:" + "P" + type + "|" + mobile + "|MobileCode_verifyCode:" + verifyCode);
        if (StringHelper.isEmpty(verifyCode))
        {
            // 封装返回信息
            setReturnMsg(request, response, ExceptionCode.PRODUCE_VERIFY_ERRROR, "验证码生成错误，请联系后台工作人员.");
            return;
        }
        
        if (template == null)
        {
            template = "${code},请验证!";
        }
        Envionment envionment = configureProvider.createEnvionment();
        envionment.set("code", verifyCode);
        sender.send(phoneType, StringHelper.format(template, envionment), mobile);
        
        // 封装返回信息
        setReturnMsg(request, response, ExceptionCode.SUCCESS, "success");
        return;
    }
}
