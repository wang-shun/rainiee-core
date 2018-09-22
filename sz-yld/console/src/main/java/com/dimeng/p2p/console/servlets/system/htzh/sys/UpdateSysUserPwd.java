/*
 * 文 件 名:  UpdateSysUserPwd.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  xiaoqi
 * 修改时间:  2016年1月4日
 */
package com.dimeng.p2p.console.servlets.system.htzh.sys;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.p2p.common.RSAUtils;
import com.dimeng.p2p.console.servlets.system.AbstractSystemServlet;
import com.dimeng.p2p.modules.systematic.console.service.SysUserManage;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.IntegerParser;

/**
 * 管理员密码修改
 * 
 * @author xiaoqi
 * @version [版本号, 2016年1月4日]
 */
@Right(id = "P2P_C_SYS_HTGL_GLYGL_MMXG", name = "密码修改", moduleId = "P2P_C_SYS_HTZH_GLYGL", order = 3)
public class UpdateSysUserPwd extends AbstractSystemServlet
{
    
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        SysUserManage sysUserManage = serviceSession.getService(SysUserManage.class);
        String password = request.getParameter("password");
        String newpassword = request.getParameter("newPassword");
        int id = IntegerParser.parse(request.getParameter("id"));
        if (StringHelper.isEmpty(password))
        {
            throw new LogicalException("密码不能为空！");
        }
        if (StringHelper.isEmpty(newpassword))
        {
            throw new LogicalException("确认密码不能为空！");
        }
        if (!password.equals(newpassword))
        {
            throw new LogicalException("两次密码输入不一致！");
        }
        password = RSAUtils.decryptStringByJs(password);
        if (password.equals(sysUserManage.get(id).accountName))
        {
            throw new LogicalException("密码不能与用户名一致！");
        }
        sysUserManage.updateUserPwd(id, password);
        sendRedirect(request, response, getController().getURI(request, SysUserList.class));
    }
    
    @Override
    protected void processGet(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        forwardView(request, response, getClass());
    }
    
    @Override
    protected void onThrowable(HttpServletRequest request, HttpServletResponse response, Throwable throwable)
        throws ServletException, IOException
    {
        getResourceProvider().log(throwable.getMessage());
        if (throwable instanceof SQLException)
        {
            logger.error(throwable, throwable);
            getController().prompt(request, response, PromptLevel.ERROR, "系统繁忙，请您稍后再试");
        }
        else if (throwable instanceof LogicalException || throwable instanceof ParameterException)
        {
            getController().prompt(request, response, PromptLevel.WARRING, throwable.getMessage());
        }
        else
        {
            super.onThrowable(request, response, throwable);
        }
        forwardView(request, response, getClass());
    }
    
}
