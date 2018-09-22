package com.dimeng.p2p.console.servlets.statistics.zjtj.grzctj;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.console.servlets.account.AbstractAccountServlet;
import com.dimeng.p2p.modules.statistics.console.service.GrMoneyManage;
import com.dimeng.p2p.modules.statistics.console.service.entity.GrMoneyEntity;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.IntegerParser;

@Right(id = "P2P_C_ACCOUNT_GRMONEYEXPORT", name = "导出个人资产统计", moduleId = "P2P_C_STATISTICS_ZJTJ_GR")
public class ExportGrMoney extends AbstractAccountServlet
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
        GrMoneyManage grMoneyManage = serviceSession.getService(GrMoneyManage.class);
        
        response.setHeader("Content-disposition",
            "attachment;filename=" + new Timestamp(System.currentTimeMillis()).getTime() + ".csv");
        response.setContentType("application/csv");
        GrMoneyEntity query = new GrMoneyEntity();
        query.parse(request);
        query.begin =
            StringHelper.isEmpty(request.getParameter("begin")) ? null : new BigDecimal(request.getParameter("begin"));
        query.end =
            StringHelper.isEmpty(request.getParameter("end")) ? null : new BigDecimal(request.getParameter("end"));
        PagingResult<GrMoneyEntity> list = grMoneyManage.search(query, new Paging()
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
        grMoneyManage.export(list.getItems(), response.getOutputStream(), "");
    }
    
}
