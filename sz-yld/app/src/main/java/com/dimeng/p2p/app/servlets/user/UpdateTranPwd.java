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
 * 修改交易密码
 * @author tanhui
 */
public class UpdateTranPwd extends AbstractSecureServlet
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
        
        SafetyManage safetyManage = serviceSession.getService(SafetyManage.class);
        Safety sa = safetyManage.get();
        
        String pwd = getParameter(request, "pwd");
        String onePwd = getParameter(request, "onePwd");
        String twoPwd = getParameter(request, "twoPwd");
        
        if (sa.username.equals(onePwd))
        {
            setReturnMsg(request, response, ExceptionCode.ACCOUNT_PASSWORD_ERROR, "交易密码与用户名不能相同,请重新输入！");
            return;
        }
        
        if (StringHelper.isEmpty(pwd))
        {
            // 封装返回信息
            setReturnMsg(request, response, ExceptionCode.PASSWORD_ISEMPTY_ERROR, "原密码不能为空！");
            return;
        }
        if (StringHelper.isEmpty(onePwd))
        {
            // 封装返回信息
            setReturnMsg(request, response, ExceptionCode.PASSWORD_ISEMPTY_ERROR, "密码不能为空！");
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
            pwd = UnixCrypt.crypt(pwd, DigestUtils.sha256Hex(pwd));
            if (sa.password.equals(onePwd))
            {
                // 封装返回信息
                setReturnMsg(request, response, ExceptionCode.SAME_LOGINPASSWORD_ERROR, "新密码不能和登录密码相同！");
                return;
            }
            else if (onePwd.equals(sa.txpassword))
            {
                // 封装返回信息
                setReturnMsg(request, response, ExceptionCode.PASSWORD_ERROR, "新密码不能和原密码相同！");
                return;
            }
            else if (onePwd.equals(sa.txpassword))
            {
                // 封装返回信息
                setReturnMsg(request, response, ExceptionCode.PASSWORD_ERROR, "新密码不能和原密码相同！");
                return;
            }
            else if (!pwd.equals(sa.txpassword))
            {
                safetyManage.udpatetxSize();
            }
            else
            {
                safetyManage.updateTxpassword(onePwd);
            }
        }
        
        // 封装返回信息
        setReturnMsg(request, response, ExceptionCode.SUCCESS, "success！");
        return;
    }
}
