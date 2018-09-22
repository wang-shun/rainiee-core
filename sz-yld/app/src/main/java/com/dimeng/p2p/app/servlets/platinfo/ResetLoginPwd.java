package com.dimeng.p2p.app.servlets.platinfo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.digest.UnixCrypt;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.account.front.service.PasswordManage;
import com.dimeng.p2p.app.service.BusinessManage;
import com.dimeng.p2p.app.servlets.AbstractAppServlet;
import com.dimeng.p2p.variables.defines.pays.PayVariavle;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.BooleanParser;

/**
 * 
 * 重置登录密码
 * 
 * @author  zhoulantao
 * @version  [版本号, 2015年11月30日]
 */
public class ResetLoginPwd extends AbstractAppServlet
{
    
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processPost(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        // 获取页面参数
        final String password = getParameter(request, "password");
        final String repassword = getParameter(request, "rePassword");
        final String phone = getParameter(request, "phone");
        
        final BusinessManage businessManage = serviceSession.getService(BusinessManage.class);
        final String username = businessManage.getUserNameByPhone(phone);
        if (username.equals(password))
        {
            setReturnMsg(request, response, ExceptionCode.ACCOUNT_PASSWORD_ERROR, "用户名与密码不能相同,请重新输入！");
            return;
        }
        
        if (StringHelper.isEmpty(password) || password.length() > 20 || password.length() < 6)
        {
            // 封装返回信息
            setReturnMsg(request, response, ExceptionCode.PARAMETER_ERROR, "密码格式输入有误");
            return;
        }
        
        if (!password.equals(repassword))
        {
            // 封装返回信息
            setReturnMsg(request, response, ExceptionCode.PASSWORD_DIFFERENT_ERROR, "两次密码输入不一致");
            return;
        }
        
        // 判断是否启用了交易密码，如果启用了交易密码则需要增加交易密码的校验
        final ConfigureProvider configureProvider = getResourceProvider().getResource(ConfigureProvider.class);
        boolean isOpenWithPsd = BooleanParser.parse(configureProvider.getProperty(PayVariavle.CHARGE_MUST_WITHDRAWPSD));
        
        if (isOpenWithPsd)
        {
            // 获取交易密码
            final String txPwd = businessManage.getTxPwdByPhone(phone);
            
            // 获取交易密码是否与新密码相同
            if (!StringHelper.isEmpty(txPwd)
                && txPwd.equals(UnixCrypt.crypt(password, DigestUtils.sha256Hex(password))))
            {
                // 封装返回信息
                setReturnMsg(request, response, ExceptionCode.SAME_TRANPASSWORD_ERROR, "不能和交易密码相同！");
                return;
            }
        }
        
        // 根据手机号码修改
        PasswordManage passwordManage = serviceSession.getService(PasswordManage.class);
        passwordManage.updatePasswordByPhone(password, phone);
        
        // 封装返回信息
        setReturnMsg(request, response, ExceptionCode.SUCCESS, "密码重置成功");
        return;
    }
    
    @Override
    protected void processGet(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        processPost(request, response, serviceSession);
    }
    
}
