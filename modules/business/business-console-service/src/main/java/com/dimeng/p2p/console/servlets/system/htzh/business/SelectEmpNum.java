package com.dimeng.p2p.console.servlets.system.htzh.business;

import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.common.enums.SysAccountStatus;
import com.dimeng.p2p.console.servlets.AbstractBuisnessServlet;
import com.dimeng.p2p.modules.account.console.service.ZhglManage;
import com.dimeng.p2p.repeater.business.SysBusinessManage;
import com.dimeng.p2p.repeater.business.query.BusinessUserQuery;
import com.dimeng.p2p.repeater.business.query.SysUserQuery;
import org.codehaus.jackson.map.ObjectMapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.sql.Timestamp;

/**
 * 
 * 查询业务员
 * <功能详细描述>
 * 
 * @author  heluzhu
 * @version  [版本号, 2016年05月19日]
 */
public class SelectEmpNum extends AbstractBuisnessServlet {
    private static final long serialVersionUID = 1L;
    
    @Override
    protected boolean mustAuthenticated()
    {
        return false;
    }
    
    @Override
    protected void processGet(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        processPost(request, response, serviceSession);
    }
    
    @Override
    protected void processPost(final HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        SysBusinessManage sysBusinessManage = serviceSession.getService(SysBusinessManage.class);
        BusinessUserQuery sysUserQueryquery = new BusinessUserQuery()
        {

            @Override
            public String getEmployNum() {
                return request.getParameter("employNum");
            }

            /**
             * 所属部门
             *
             * @return
             */
            @Override
            public String getDept() {
                return null;
            }

            @Override
            public SysAccountStatus getStatus()
            {
                return SysAccountStatus.QY;
            }

            @Override
            public int getRoleId()
            {
                return 0;
            }

            @Override
            public String getName()
            {
                return null;
            }

            @Override
            public Timestamp getCreateTimeStart()
            {
                return null;
            }

            @Override
            public Timestamp getCreateTimeEnd()
            {
                return null;
            }

            @Override
            public String getAccountName()
            {
                return null;
            }
        };
        ObjectMapper objectMapper = new ObjectMapper();
        PrintWriter out = response.getWriter();
        out.print(objectMapper.writeValueAsString(sysBusinessManage.getEmployUsers(sysUserQueryquery)));
        out.flush();
    }
    
}
