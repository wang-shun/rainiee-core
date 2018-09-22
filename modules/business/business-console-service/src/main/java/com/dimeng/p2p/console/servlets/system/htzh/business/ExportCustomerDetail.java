package com.dimeng.p2p.console.servlets.system.htzh.business;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.common.Constant;
import com.dimeng.p2p.console.servlets.AbstractBuisnessServlet;
import com.dimeng.p2p.modules.systematic.console.service.SysUserManage;
import com.dimeng.p2p.repeater.business.SysBusinessManage;
import com.dimeng.p2p.repeater.business.entity.CustomerEntity;
import com.dimeng.p2p.repeater.business.query.CustomerQuery;
import com.dimeng.util.Formater;
import com.dimeng.util.io.CVSWriter;
import com.dimeng.util.parser.DateTimeParser;
import com.dimeng.util.parser.IntegerParser;
import com.dimeng.util.parser.TimestampParser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.sql.Timestamp;

/**
 * 
 * 导出客户详情
 * <功能详细描述>
 * 
 * @author  heluzhu
 * @version  [版本号, 2016年05月19日]
 */
@Right(id = "P2P_C_SYS_HTZH_YWYGL_KHXQ", name = "客户详情",moduleId="P2P_C_SYS_HTZH_YWYGL",order=2)
public class ExportCustomerDetail extends AbstractBuisnessServlet
{
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processGet(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        processPost(request, response, serviceSession);
    }
    
    @SuppressWarnings(value = "unchecked")
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
        PagingResult<CustomerEntity> result = sysBusinessManage.selectCustomers(new CustomerQuery() {
            @Override
            public String getUserName() {
                return request.getParameter("userName");
            }

            @Override
            public String getRealName() {
                return request.getParameter("realName");
            }

            @Override
            public String getMobile() {
                return request.getParameter("mobile");
            }

            @Override
            public String getIsThird() {
                return request.getParameter("isThird");
            }

            @Override
            public Timestamp getCreateTimeStart() {
                return TimestampParser.parse(request.getParameter("createTimeStart"));
            }

            @Override
            public Timestamp getCreateTimeEnd() {
                return TimestampParser.parse(request.getParameter("createTimeEnd"));
            }

            /**
             * 业务员工号
             *
             * @return
             */
            @Override
            public String getEmployNum() {
                return Constant.BUSINESS_ROLE_ID == sysU.roleId ? sysU.employNum : request.getParameter("employNum");
            }
        }, new Paging() {
            @Override
            public int getCurrentPage() {
                return IntegerParser.parse(request.getParameter(PAGING_CURRENT));
            }

            @Override
            public int getSize() {
                return 10;
            }
        });
        try (BufferedWriter out = new BufferedWriter(new OutputStreamWriter(response.getOutputStream(), "GBK")))
        {
            CVSWriter writer = new CVSWriter(out);
            writer.write("序号");
            writer.write("用户名");
            writer.write("真实姓名");
            writer.write("手机号码");
            writer.write("可用余额(元)");
            writer.write("借款负债(元)");
            writer.write("注册时间");
            writer.newLine();
            int i = 0;
            if (result != null)
            {
                for (CustomerEntity results : result.getItems())
                {
                    i++;
                    writer.write(i);
                    writer.write(results.userName);
                    writer.write(results.realName);
                    writer.write(results.mobile);
                    writer.write(Formater.formatAmount(results.usableAmount));
                    writer.write(Formater.formatAmount(results.loanAmount));
                    writer.write(DateTimeParser.format(results.registeTime, "yyyy-MM-dd HH:mm:ss") + "\t");
                    writer.newLine();
                }
            }
        }
        
    }
}
