package com.dimeng.p2p.console.servlets.system.htzh.business;

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
import com.dimeng.p2p.common.Constant;
import com.dimeng.p2p.console.servlets.AbstractBuisnessServlet;
import com.dimeng.p2p.modules.business.console.service.achieve.BusTradeType;
import com.dimeng.p2p.modules.systematic.console.service.SysUserManage;
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
 * 导出业绩明细数据（一级）
 * <功能详细描述>
 * 
 * @author  heluzhu
 * @version  [版本号, 2015年12月2日]
 */
@Right(id = "P2P_C_SYS_HTZH_YWYGL_YJMX", name = "业绩详情",moduleId="P2P_C_SYS_HTZH_YWYGL",order=1)
public class ExportResultsDetail extends AbstractBuisnessServlet
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
                return request.getParameter("nameLevel");
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
        Map<String, Object> map = sysBusinessManage.searchAll(query, paging);
        PagingResult<Results> result = (PagingResult<Results>)map.get("pagItems");
        try (BufferedWriter out = new BufferedWriter(new OutputStreamWriter(response.getOutputStream(), "GBK")))
        {
            CVSWriter writer = new CVSWriter(out);
            writer.write("序号");
            writer.write("用户名");
            writer.write("姓名");
            writer.write("客户级别");
            writer.write("所属一级用户");
            writer.write("成交金额(元)");
            writer.write("交易类型");
            //writer.write("订单ID");
            writer.write("成交时间");
            writer.write("备注");
            writer.newLine();
            int i = 0;
            if (result != null)
            {
                for (Results results : result.getItems())
                {
                    i++;
                    writer.write(i);
                    writer.write(results.customName);
                    writer.write(results.customRealName);
                    writer.write(results.namelevel);
                    writer.write(results.firstCustomName);
                    writer.write(Formater.formatAmount(results.amount));
                    writer.write(BusTradeType.valueOf(results.tradeType).getChineseName());
                   // writer.write(results.orderId);
                    writer.write(DateTimeParser.format(results.showTime, "yyyy-MM-dd HH:mm:ss") + "\t");
                    writer.write(results.tradeType.equals("invest") || results.tradeType.equals("loan") ? "标的ID："+results.projectID : "");
                    writer.newLine();
                }
            }
        }
        
    }
}
