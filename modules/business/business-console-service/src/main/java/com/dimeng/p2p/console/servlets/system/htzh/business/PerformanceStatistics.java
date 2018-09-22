package com.dimeng.p2p.console.servlets.system.htzh.business;

import com.dimeng.framework.http.entity.RoleBean;
import com.dimeng.framework.http.service.RoleManage;
import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S71.entities.T7190;
import com.dimeng.p2p.common.Constant;
import com.dimeng.p2p.console.servlets.AbstractBuisnessServlet;
import com.dimeng.p2p.modules.systematic.console.service.SysUserManage;
import com.dimeng.p2p.repeater.business.SysBusinessManage;
import com.dimeng.p2p.repeater.business.entity.Performance;
import com.dimeng.p2p.repeater.business.query.PerformanceQuery;
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
@Right(id = "P2P_C_SYS_HTZH_YWYGL_YJMX", name = "业绩详情",moduleId="P2P_C_SYS_HTZH_YWYGL",order=1)
public class PerformanceStatistics extends AbstractBuisnessServlet {
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
        
        SysBusinessManage sysUserManage = serviceSession.getService(SysBusinessManage.class);
        RoleManage roleManage = serviceSession.getService(RoleManage.class);
        SysUserManage sysUserM = serviceSession.getService(SysUserManage.class);
        final com.dimeng.p2p.modules.systematic.console.service.entity.SysUser sysU = sysUserM.get(serviceSession.getSession().getAccountId());
        PerformanceQuery query = new PerformanceQuery()
        {
            
            @Override
            public String getEmployNum()
            {
                return Constant.BUSINESS_ROLE_ID == sysU.roleId ? sysU.employNum : request.getParameter("employNum");
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
            public String getName()
            {
                return null;
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
        PagingResult<RoleBean> roles = roleManage.search(null, new Paging()
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
        Performance performance = sysUserManage.findPerformance(query);
        request.setAttribute("roles", roles.getItems());
        PagingResult<T7190> result = sysUserManage.serarchTbjl(query, paging);
        request.setAttribute("performance", performance);
        request.setAttribute("result", result);
        
        forwardView(request, response, PerformanceStatistics.class);
    }
    
    @Override
    protected void onThrowable(HttpServletRequest request, HttpServletResponse response, Throwable throwable)
        throws ServletException, IOException
    {
        super.onThrowable(request, response, throwable);
    }
}
