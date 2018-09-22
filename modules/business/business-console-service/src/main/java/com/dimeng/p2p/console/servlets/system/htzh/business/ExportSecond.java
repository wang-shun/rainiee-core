package com.dimeng.p2p.console.servlets.system.htzh.business;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.common.Constant;
import com.dimeng.p2p.console.servlets.AbstractBuisnessServlet;
import com.dimeng.p2p.modules.systematic.console.service.SysUserManage;
import com.dimeng.p2p.repeater.business.SysBusinessManage;
import com.dimeng.p2p.repeater.business.entity.Results;
import com.dimeng.p2p.repeater.business.query.ResultsQuery;
import com.dimeng.util.io.CVSWriter;
import com.dimeng.util.parser.DateTimeParser;
import com.dimeng.util.parser.IntegerParser;
import com.dimeng.util.parser.TimestampParser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.sql.Timestamp;
import java.util.Map;

/**
 * 
 * 导出业绩明细数据（二级）
 * <功能详细描述>
 * 
 * @author  heluzhu
 * @version  [版本号, 2015年12月2日]
 */
@Right(id = "P2P_C_SYS_HTZH_YWYGL_YJMX", name = "业绩详情",moduleId="P2P_C_SYS_HTZH_YWYGL",order=1)
public class ExportSecond extends AbstractBuisnessServlet
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
        ResultsQuery query = new ResultsQuery()
        {
            
            @Override
            public String getUserType()
            {
                return request.getParameter("zhlx");
            }
            
            @Override
            public String getProject()
            {
                return request.getParameter("project");
            }
            
            @Override
            public String getNamelevel()
            {
                return "2";
            }
            
            @Override
            public String getName()
            {
                return request.getParameter("name");
            }
            
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
            public String getCustomName()
            {
                return request.getParameter("customName");
            }
            
            /**
             * 一级客户姓名
             *
             * @return
             */
            @Override
            public String getUserNameLevel()
            {
                return null;
            }

            /**
             * 客户姓名
             *
             * @return
             */
            @Override
            public String getCustomRealName() {
                return request.getParameter("customRealName");
            }

            /**
             * 交易类型
             *
             * @return
             */
            @Override
            public String getTradeType() {
                return request.getParameter("tradeType");
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
        
        Map<String, Object> map = sysBusinessManage.searchAll(query, paging);
        PagingResult<Results> result = (PagingResult<Results>)map.get("pagItems");
        try (BufferedWriter out = new BufferedWriter(new OutputStreamWriter(response.getOutputStream(), "GBK")))
        {
            CVSWriter writer = new CVSWriter(out);
            writer.write("序号");
            writer.write("用户名");
            /*writer.write("用户类型");*/
            writer.write("项目ID");
            writer.write("项目标题");
            writer.write("项目期限");
            writer.write("产品名称");
            writer.write("项目类型");
            writer.write("投资金额（元）");
            writer.write("借款金额（元）");
            writer.write("所属一级用户");
            writer.write("放款时间");
            writer.newLine();
            int i = 0;
            if (result != null)
            {
                for (Results results : result.getItems())
                {
                    i++;
                    writer.write(i);
                    writer.write(results.customName);
                    /*String j = null;
                    T6110_F06 yhlx = results.F05;
                    T6110_F10 dbf = results.F09;
                    if (yhlx == T6110_F06.ZRR)
                    {
                        j = "个人";
                    }
                    else if (yhlx == T6110_F06.FZRR && dbf == T6110_F10.F)
                    {
                        j = "企业";
                    }
                    else if (yhlx == T6110_F06.FZRR && dbf == T6110_F10.S)
                    {
                        j = "机构";
                    }
                    writer.write(j);*/
                    writer.write(results.projectID);
                    writer.write(results.projectTitle);
                    writer.write(results.term);
                    writer.write(results.productName);
                    writer.write(results.projectType);
                    writer.write(results.investmentAmount);
                    writer.write(results.LoanAmount);
                    writer.write(results.firstCustomName);
                    writer.write(DateTimeParser.format(results.showTime, "yyyy-MM-dd") + "\t");
                    writer.newLine();
                }
            }
        }
        
    }
}
