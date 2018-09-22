package com.dimeng.p2p.app.servlets.user;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.digest.UnixCrypt;

import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.account.user.service.SafetyManage;
import com.dimeng.p2p.account.user.service.entity.Safety;
import com.dimeng.p2p.app.servlets.AbstractSecureServlet;
import com.dimeng.p2p.app.servlets.platinfo.ExceptionCode;
import com.dimeng.util.StringHelper;

/**
 * 
 * 找回交易密码
 * 
 * @author  zhoulantao
 * @version  [版本号, 2015年12月11日]
 */
public class ForgotTranPwd extends AbstractSecureServlet
{
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
        // 获取密码信息
        String onePwd = getParameter(request, "onePwd");
        String twoPwd = getParameter(request, "twoPwd");
        
        SafetyManage safetyManage = serviceSession.getService(SafetyManage.class);
        Safety sa = safetyManage.get();
        
        if (sa.username.equals(onePwd))
        {
            setReturnMsg(request, response, ExceptionCode.ACCOUNT_PASSWORD_ERROR, "交易密码与用户名不能相同,请重新输入！");
            return;
        }
        //设置交易密码
        if (StringHelper.isEmpty(onePwd) || StringHelper.isEmpty(twoPwd))
        {
            // 封装返回信息
            setReturnMsg(request, response, ExceptionCode.SAME_LOGINPASSWORD_ERROR, "密码不能为空！");
            return;
        }
        else if (!onePwd.equals(twoPwd))
        {
            // 封装返回信息
            setReturnMsg(request, response, ExceptionCode.PASSWORD_NOSAME_ERROR, "两次密码输入不一样！");
            return;
        }
        else
        {
            onePwd = UnixCrypt.crypt(onePwd, DigestUtils.sha256Hex(onePwd));
            if (sa.password.equals(onePwd))
            {
                // 封装返回信息
                setReturnMsg(request, response, ExceptionCode.SAME_LOGINPASSWORD_ERROR, "新密码不能和登录密码相同！");
                return;
            }
            else
            {
                // 修改交易密码
                safetyManage.updateTxpassword(onePwd);
            }
        }
        
        // 封装返回信息
        setReturnMsg(request, response, ExceptionCode.SUCCESS, "success!");
        return;
    }
}
