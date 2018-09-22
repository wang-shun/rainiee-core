/*
 * 文 件 名:  CheckPwd.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  zhoulantao
 * 修改时间:  2015年12月24日
 */
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

/**
 * 检查登录密码是否正确
 * 
 * @author  zhoulantao
 * @version  [版本号, 2015年12月24日]
 */
public class CheckPwd extends AbstractSecureServlet
{
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 5039751999432631763L;
    
    @Override
    protected void processPost(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        // 获取用户输入的密码
        String password = getParameter(request, "password");
        
        // 查询用户的登录密码
        SafetyManage safetyManage = serviceSession.getService(SafetyManage.class);
        Safety sa = safetyManage.get();
        String jypsd = sa.password;
        
        // 加密后比较
        password = UnixCrypt.crypt(password, DigestUtils.sha256Hex(password));
        if (!password.equals(jypsd))
        {
            // 登录密码错误
            setReturnMsg(request, response, ExceptionCode.LOGING_FAIL_ERROR, "登录密码错误!");
            return;
        }
        
        // 登录正确
        setReturnMsg(request, response, ExceptionCode.SUCCESS, "success!");
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
