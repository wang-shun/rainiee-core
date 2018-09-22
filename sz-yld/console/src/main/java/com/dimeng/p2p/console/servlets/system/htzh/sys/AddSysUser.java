package com.dimeng.p2p.console.servlets.system.htzh.sys;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.http.entity.RoleBean;
import com.dimeng.framework.http.service.RoleManage;
import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.common.Constant;
import com.dimeng.p2p.common.RSAUtils;
import com.dimeng.p2p.console.servlets.system.AbstractSystemServlet;
import com.dimeng.p2p.modules.systematic.console.service.SysUserManage;
import com.dimeng.p2p.repeater.business.SysBusinessManage;
import com.dimeng.p2p.variables.defines.BusinessVariavle;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.IntegerParser;

@Right(id = "P2P_C_SYS_ADDSYSUSER", name = "新增", moduleId = "P2P_C_SYS_HTZH_GLYGL", order = 1)
public class AddSysUser extends AbstractSystemServlet
{
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processGet(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        RoleManage roleManage = serviceSession.getService(RoleManage.class);
        PagingResult<RoleBean> result = roleManage.search(null, new Paging()
        {
            
            @Override
            public int getSize()
            {
                return Integer.MAX_VALUE;
            }
            
            @Override
            public int getCurrentPage()
            {
                return 1;
            }
        });
        request.setAttribute("roles", result.getItems());
        forwardView(request, response, getClass());
    }
    
    @Override
    protected void processPost(final HttpServletRequest request, HttpServletResponse response,
        ServiceSession serviceSession)
        throws Throwable
    {
        SysUserManage sysUserManage = serviceSession.getService(SysUserManage.class);
        RoleManage roleManage = serviceSession.getService(RoleManage.class);
        String accountName = request.getParameter("accountName");
        String password = request.getParameter("password");
        String newpassword = request.getParameter("newPassword");
        String name = request.getParameter("name");
        String status = request.getParameter("status");
        String phone = request.getParameter("phone");
        String pos = request.getParameter("pos");
        String dept = request.getParameter("dept");
        int roleId = IntegerParser.parse(request.getParameter("roleId"));
        int businessRoleId = Constant.BUSINESS_ROLE_ID;
        int sysUserId = 0;
        try
        {
            if (StringHelper.isEmpty(accountName))
            {
                throw new LogicalException("用户名不能为空！");
            }
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
            if (password.equals(accountName))
            {
                throw new LogicalException("密码不能与用户名一致！");
            }
            accountName = RSAUtils.decryptStringByJs(accountName);
            password = RSAUtils.decryptStringByJs(password);
            String employNumber = null;
            final ConfigureProvider configureProvider = getResourceProvider().getResource(ConfigureProvider.class);
            boolean is_business = Boolean.parseBoolean(configureProvider.getProperty(BusinessVariavle.IS_BUSINESS));
            if (roleId == businessRoleId && is_business)
            {
                SysBusinessManage sysBusinessManage = serviceSession.getService(SysBusinessManage.class);
                employNumber = produceEmployNum(sysBusinessManage.getMaxEmployNum());
            }
            sysUserId = sysUserManage.add(accountName, password, name, status, phone, pos, employNumber, roleId, dept);
        }
        catch (Throwable e)
        {
            if (e instanceof ParameterException || e instanceof LogicalException)
            {
                getController().prompt(request, response, PromptLevel.WARRING, e.getMessage());
                processGet(request, response, serviceSession);
            }
            else
            {
                super.onThrowable(request, response, e);
            }
        }
        if (sysUserId > 0)
        {
            roleManage.setRoles(sysUserId, roleId);
            sendRedirect(request, response, getController().getURI(request, SysUserList.class));
        }
    }
    
    private String produceEmployNum(int employNum)
    {
        String employNumStr = "";
        String prefix = "YW0000";
        int len = String.valueOf(employNum).length();
        employNumStr = prefix.substring(0, prefix.length() - len) + employNum;
        
        return employNumStr;
    }
}
