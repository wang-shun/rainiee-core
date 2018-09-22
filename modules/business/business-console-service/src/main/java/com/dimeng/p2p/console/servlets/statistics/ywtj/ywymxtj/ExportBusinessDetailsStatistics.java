package com.dimeng.p2p.console.servlets.statistics.ywtj.ywymxtj;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.sql.Timestamp;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.console.servlets.AbstractBuisnessServlet;
import com.dimeng.p2p.modules.business.console.service.achieve.BusTradeType;
import com.dimeng.p2p.repeater.business.SysBusinessManage;
import com.dimeng.p2p.repeater.business.entity.Results;
import com.dimeng.p2p.repeater.business.query.ResultsQuery;
import com.dimeng.util.Formater;
import com.dimeng.util.io.CVSWriter;
import com.dimeng.util.parser.DateTimeParser;
import com.dimeng.util.parser.IntegerParser;
import com.dimeng.util.parser.TimestampParser;

/**
 * 
 * 导出业务员业绩明细
 * <功能详细描述>
 * 
 * @author  heluzhu
 * @version  [版本号, 2015年12月2日]
 */
@Right(id = "P2P_C_STATISTICS_EXPORTBUSINESSDETAIL", name = "导出业务员业绩明细", moduleId = "P2P_C_STATISTICS_YWTJ_YWYMXTJ")
public class ExportBusinessDetailsStatistics extends AbstractBuisnessServlet
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
        ResultsQuery query = new ResultsQuery()
        {
            
            @Override
            public String getEmployNum()
            {
                return request.getParameter("employNum");
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
                return request.getParameter("userName");
            }
            
            @Override
            public String getProject()
            {
                return request.getParameter("project");
            }
            
            @Override
            public String getUserType()
            {
                return request.getParameter("zhlx");
            }
            
            @Override
            public String getNamelevel()
            {
                return request.getParameter("nameLevel");
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
                return request.getParameter("belongLevel");
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
        
        Map<String, Object> result = sysBusinessManage.searchAll(query, paging);
        PagingResult<Results> pagItem = (PagingResult<Results>)result.get("pagItems");
        try (BufferedWriter out = new BufferedWriter(new OutputStreamWriter(response.getOutputStream(), "GBK")))
        {
            CVSWriter writer = new CVSWriter(out);
            writer.write("序号");
            writer.write("业务员工号");
            writer.write("业务员姓名");
            writer.write("客户用户名");
            writer.write("客户姓名");
            writer.write("客户级别");
            writer.write("所属一级用户");
            writer.write("成交金额");
            writer.write("交易类型");
            //writer.write("订单ID");
            writer.write("成交时间");
            writer.write("备注");
            writer.newLine();
            int i = 0;
            if (pagItem != null && pagItem.getItems() != null)
            {
                for (Results r : pagItem.getItems())
                {
                    i++;
                    writer.write(i);
                    writer.write(r.employNum);
                    writer.write(r.name);
                    writer.write(r.customName);
                    writer.write(r.customRealName);
                    writer.write(r.namelevel);
                    writer.write(r.firstCustomName);
                    writer.write(Formater.formatAmount(r.amount));
                    writer.write(BusTradeType.valueOf(r.tradeType).getChineseName());
                    //writer.write(r.orderId);
                    writer.write(DateTimeParser.format(r.showTime) + "\t");
                    writer.write(r.tradeType.equals("invest") || r.tradeType.equals("loan") ? "标的ID："+r.projectID : "");
                    writer.newLine();
                }
            }
        }
        
    }
}
