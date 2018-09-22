package com.dimeng.p2p.console.servlets.statistics.tjbb.tztj;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.console.servlets.finance.AbstractFinanceServlet;
import com.dimeng.p2p.modules.statistics.console.service.InvestmentListManage;
import com.dimeng.p2p.modules.statistics.console.service.entity.InvestmentListEntity;
import com.dimeng.p2p.modules.statistics.console.service.query.InvestmentQuery;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.IntegerParser;
import com.dimeng.util.parser.TimestampParser;

@Right(id = "P2P_C_STATISTICS_EXPORTTZTJ", name = "导出投资统计", moduleId = "P2P_C_STATISTICS_TJBB_TZTJ")
public class ExportInvestmentList extends AbstractFinanceServlet
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
        InvestmentListManage investmentManage = serviceSession.getService(InvestmentListManage.class);
        response.setHeader("Content-disposition",
            "attachment;filename=" + new Timestamp(System.currentTimeMillis()).getTime() + ".csv");
        response.setContentType("application/csv");
        InvestmentQuery query = new InvestmentQuery()
        {
            
            @Override
            public String getId()
            {
                return request.getParameter("id");
            }
            
            @Override
            public String getInvestAccoun()
            {
                return request.getParameter("investAccoun");
            }
            
            @Override
            public String getLoanTitle()
            {
                return request.getParameter("title");
            }
            
            @Override
            public Timestamp getLoanTimeStart()
            {
                return TimestampParser.parse(request.getParameter("loanTimeStart"));
            }
            
            @Override
            public Timestamp getLoanTimeEnd()
            {
                return TimestampParser.parse(request.getParameter("loanTimeEnd"));
            }
            
            @Override
            public Timestamp getFinishTimeStart()
            {
                return TimestampParser.parse(request.getParameter("finishTimeStart"));
            }
            
            @Override
            public Timestamp getFinishTimeEnd()
            {
                return TimestampParser.parse(request.getParameter("finishTimeEnd"));
            }
            
            @Override
            public BigDecimal getInvestPriceStart()
            {
                return  StringHelper.isEmpty(request.getParameter("investPriceStart"))?null: new BigDecimal(request.getParameter("investPriceStart"));
            }
            
            @Override
            public BigDecimal getInvestPriceEnd()
            {
                return  StringHelper.isEmpty(request.getParameter("investPriceEnd"))?null:new BigDecimal(request.getParameter("investPriceEnd"));
            }

			@Override
			public String getSource() {
				return request.getParameter("source");
			}

            @Override
            public String getBidWay()
            {
                return request.getParameter("bidWay");
            }
            
            @Override
            public String getLoanUserType()
            {
                return request.getParameter("loanUserType");
            }
            
            @Override
            public String getInvestUserType()
            {
                return request.getParameter("investUserType");
            }
            
        };
        PagingResult<InvestmentListEntity> result = investmentManage.getInvestmentList(query, new Paging()
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
        investmentManage.export(result.getItems(),
            investmentManage.getInverstmentTotal(query),
            response.getOutputStream(),
            "");
    }
}
