package com.dimeng.p2p.console.servlets.system.htzh.business;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.common.enums.SysAccountStatus;
import com.dimeng.p2p.console.servlets.AbstractBuisnessServlet;
import com.dimeng.p2p.modules.systematic.console.service.SysUserManage;
import com.dimeng.p2p.repeater.business.SysBusinessManage;
import com.dimeng.p2p.repeater.business.entity.SysUser;
import com.dimeng.p2p.repeater.business.query.BusinessUserQuery;
import com.dimeng.util.parser.EnumParser;
import com.dimeng.util.parser.IntegerParser;
import com.dimeng.util.parser.TimestampParser;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;

/**
 * 
 * 业务员管理
 * <功能详细描述>
 * 
 * @author  heluzhu
 * @version  [版本号, 2015年12月2日]
 */
@Right(id = "P2P_C_SYS_YWYUSERLIST", isMenu = true, name = "业务员管理",moduleId="P2P_C_SYS_HTZH_YWYGL",order=0)
public class BusinessUserList extends AbstractBuisnessServlet
{
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processGet(final HttpServletRequest request, HttpServletResponse response,
        ServiceSession serviceSession)
        throws Throwable
    {
        processPost(request, response, serviceSession);
    }
    
    @Override
    protected void processPost(final HttpServletRequest request, HttpServletResponse response,
        ServiceSession serviceSession)
        throws Throwable
    {
        
        SysBusinessManage sysBusinessManage = serviceSession.getService(SysBusinessManage.class);
        SysUserManage sysUserM = serviceSession.getService(SysUserManage.class);
        final com.dimeng.p2p.modules.systematic.console.service.entity.SysUser sysU = sysUserM.get(serviceSession.getSession().getAccountId());
        BusinessUserQuery query = new BusinessUserQuery()
        {
            
            @Override
            public SysAccountStatus getStatus()
            {
                return EnumParser.parse(SysAccountStatus.class, request.getParameter("status"));
            }
            
            @Override
            public String getName()
            {
                return request.getParameter("name");
            }
            
            @Override
            public Timestamp getCreateTimeStart()
            {
                return TimestampParser.parse(request.getParameter("createTimeStart"));
            }
            
            @Override
            public Timestamp getCreateTimeEnd()
            {
                return TimestampParser.parse(request.getParameter("createTimeEnd"));
            }
            
            @Override
            public String getAccountName()
            {
                return request.getParameter("accountName");
            }
            
            @Override
            public int getRoleId()
            {
                return sysU.roleId;
            }
            
            @Override
            public String getEmployNum()
            {
                
                return request.getParameter("employNum");
            }

            /**
             * 所属部门
             *
             * @return
             */
            @Override
            public String getDept() {
                return request.getParameter("dept");
            }

        };
        Paging paging = new Paging()
        {
            
            @Override
            public int getSize()
            {
                return 10;
            }
            
            @Override
            public int getCurrentPage()
            {
                return IntegerParser.parse(request.getParameter(PAGING_CURRENT));
            }
        };
        PagingResult<SysUser> result = sysBusinessManage.seachBusinessUser(query, paging);
        request.setAttribute("result", result);
        forwardView(request, response, BusinessUserList.class);
    }
    
    @Override
    protected void onThrowable(HttpServletRequest request, HttpServletResponse response, Throwable throwable)
        throws ServletException, IOException
    {
        super.onThrowable(request, response, throwable);
    }
}
