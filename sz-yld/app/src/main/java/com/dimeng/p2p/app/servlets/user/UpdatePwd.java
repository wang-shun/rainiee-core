package com.dimeng.p2p.app.servlets.user;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.digest.UnixCrypt;

import com.dimeng.framework.http.session.Session;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.account.user.service.SafetyManage;
import com.dimeng.p2p.account.user.service.entity.Safety;
import com.dimeng.p2p.app.servlets.AbstractSecureServlet;
import com.dimeng.p2p.app.servlets.platinfo.ExceptionCode;
import com.dimeng.util.StringHelper;

/**
 * 修改登录密码
 * @author tanhui
 */
public class UpdatePwd extends AbstractSecureServlet
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
        if (StringHelper.isEmpty(pwd))
        {
            // 封装返回信息
            setReturnMsg(request, response, ExceptionCode.PASSWORD_ISEMPTY_ERROR, "原密码不能为空！");
            return;
        }
        if (sa.username.equals(onePwd))
        {
            setReturnMsg(request, response, ExceptionCode.ACCOUNT_PASSWORD_ERROR, "用户名与密码不能相同,请重新输入！");
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
            if (!UnixCrypt.crypt(pwd, DigestUtils.sha256Hex(pwd)).equals(sa.password))
            {
                // 封装返回信息
                setReturnMsg(request, response, ExceptionCode.PASSWORD_ERROR, "原密码错误！");
                return;
            }
            else if (!StringHelper.isEmpty(sa.txpassword)
                && sa.txpassword.equals(UnixCrypt.crypt(onePwd, DigestUtils.sha256Hex(onePwd))))
            {
                // 封装返回信息
                setReturnMsg(request, response, ExceptionCode.SAME_TRANPASSWORD_ERROR, "不能和交易密码相同！");
                return;
            }
            else if (!StringHelper.isEmpty(sa.password)
                && sa.password.equals(UnixCrypt.crypt(onePwd, DigestUtils.sha256Hex(onePwd))))
            {
                // 封装返回信息
                setReturnMsg(request, response, ExceptionCode.SAME_PASSWORD_ERROR, "不能和原密码相同！");
                return;
            }
            else
            {
                safetyManage.updatePassword(UnixCrypt.crypt(onePwd, DigestUtils.sha256Hex(onePwd)));
            }
        }
        
        // 刷新session
        Session session = serviceSession.getSession();
        if (session != null)
        {
            session.invalidateAll(request, response);
        }
        
        // 封装返回信息
        setReturnMsg(request, response, ExceptionCode.SUCCESS, "success!");
        return;
    }
}
