package com.dimeng.p2p.console.servlets.system.htzh.business;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.dimeng.util.io.CVSWriter;
import com.dimeng.util.parser.EnumParser;
import com.dimeng.util.parser.IntegerParser;
import com.dimeng.util.parser.TimestampParser;

/**
 * 
 * 导出业务员数据
 * <功能详细描述>
 * 
 * @author  heluzhu
 * @version  [版本号, 2015年12月2日]
 */
@Right(id = "P2P_C_SYS_HTZH_EXPORT", name = "导出", moduleId = "P2P_C_SYS_HTZH_YWYGL", order = 3)
public class ExportBusinessUser extends AbstractBuisnessServlet
{
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processGet(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        processPost(request, response, serviceSession);
    }
    
    @Override
    protected void processPost(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        SysBusinessManage sysBusinessManage = serviceSession.getService(SysBusinessManage.class);
        response.setHeader("Content-disposition",
            "attachment;filename=" + new Timestamp(System.currentTimeMillis()).getTime() + ".csv");
        response.setContentType("application/csv");
        SysUserManage sysUserM = serviceSession.getService(SysUserManage.class);
        final com.dimeng.p2p.modules.systematic.console.service.entity.SysUser sysU =
            sysUserM.get(serviceSession.getSession().getAccountId());
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
            public String getDept()
            {
                return request.getParameter("dept");
            }
            
        };
        Paging paging = new Paging()
        {
            
            @Override
            public int getSize()
            {
                return Integer.MAX_VALUE;
            }
            
            @Override
            public int getCurrentPage()
            {
                return IntegerParser.parse(request.getParameter(PAGING_CURRENT));
            }
        };
        
        PagingResult<SysUser> result = sysBusinessManage.seachBusinessUser(query, paging);
        try (BufferedWriter out = new BufferedWriter(new OutputStreamWriter(response.getOutputStream(), "GBK")))
        {
            CVSWriter writer = new CVSWriter(out);
            writer.write("序号");
            writer.write("业务员工号");
            writer.write("姓名");
            writer.write("用户名");
            writer.write("手机号码");
            writer.write("职称");
            writer.write("所属部门");
            writer.write("用户状态");
            writer.write("创建时间");
            
            writer.newLine();
            int i = 0;
            if (result != null)
            {
                for (SysUser sysUser : result.getItems())
                {
                    i++;
                    writer.write(i);
                    writer.write(sysUser.employNum);
                    writer.write(sysUser.name);
                    writer.write(sysUser.accountName);
                    writer.write(sysUser.phone);
                    writer.write(sysUser.pos);
                    writer.write(sysUser.dept);
                    writer.write(sysUser.status.getName());
                    writer.write(TimestampParser.format(sysUser.createTime) + "\t");
                    writer.newLine();
                }
            }
        }
        
    }
}
