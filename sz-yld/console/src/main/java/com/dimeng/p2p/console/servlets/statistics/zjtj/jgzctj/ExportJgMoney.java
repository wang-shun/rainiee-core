package com.dimeng.p2p.console.servlets.statistics.zjtj.jgzctj;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.console.servlets.account.AbstractAccountServlet;
import com.dimeng.p2p.modules.statistics.console.service.JgMoneyManage;
import com.dimeng.p2p.modules.statistics.console.service.entity.PropertyStatisticsEntity;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.IntegerParser;

@Right(id = "P2P_C_ACCOUNT_JGMONEYEXPORT", name = "导出机构资产统计", moduleId = "P2P_C_STATISTICS_ZJTJ_JG")
public class ExportJgMoney extends AbstractAccountServlet
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
        JgMoneyManage jgMoneyManage = serviceSession.getService(JgMoneyManage.class);
        
        response.setHeader("Content-disposition",
            "attachment;filename=" + new Timestamp(System.currentTimeMillis()).getTime() + ".csv");
        response.setContentType("application/csv");
        PropertyStatisticsEntity query = new PropertyStatisticsEntity();
        query.parse(request);
        query.balanceMin =
            StringHelper.isEmpty(request.getParameter("balanceMin")) ? null : new BigDecimal(
                request.getParameter("balanceMin"));
        query.balanceMax =
            StringHelper.isEmpty(request.getParameter("balanceMax")) ? null : new BigDecimal(
                request.getParameter("balanceMax"));
        PagingResult<PropertyStatisticsEntity> list = jgMoneyManage.search(query, new Paging()
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
        });
        jgMoneyManage.export(list.getItems(), response.getOutputStream(), "");
    }
    
}
