package com.dimeng.p2p.console.servlets.statistics.tjbb.hktj;

import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.console.servlets.finance.AbstractFinanceServlet;
import com.dimeng.p2p.modules.statistics.console.service.RepaymentInfoManage;
import com.dimeng.p2p.modules.statistics.console.service.entity.RepaymentStatisticsEntity;
import com.dimeng.p2p.modules.statistics.console.service.query.RepaymentStatisticsQuery;
import com.dimeng.util.parser.IntegerParser;
import com.dimeng.util.parser.TimestampParser;

@Right(id = "P2P_C_STATISTICS_EXPORTHKTJ", name = "导出还款统计", moduleId = "P2P_C_STATISTICS_TJBB_HKTJ")
public class ExportPaymentAccountStatistics extends AbstractFinanceServlet
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
        RepaymentInfoManage repaymentInfoManage = serviceSession.getService(RepaymentInfoManage.class);
        response.setHeader("Content-disposition",
            "attachment;filename=" + new Timestamp(System.currentTimeMillis()).getTime() + ".csv");
        response.setContentType("application/csv");
        RepaymentStatisticsQuery query = new RepaymentStatisticsQuery()
        {
            @Override
            public String getId()
            {
                return request.getParameter("id");
            }

            @Override
            public Timestamp getShouldTheDateStart()
            {
                return TimestampParser.parse(request.getParameter("shouldTheDateStart"));
            }

            @Override
            public Timestamp getShouldTheDateEnd()
            {
                return TimestampParser.parse(request.getParameter("shouldTheDateEnd"));
            }
            
            @Override
            public String getLoanAccount()
            {
                return request.getParameter("loanAccount");
            }

            @Override
            public String getAccountState()
            {
                return request.getParameter("accountType");
            }

            @Override
            public int getOverdueDaysMin()
            {
                return IntegerParser.parse(request.getParameter("yuqiFromDays"));
            }
            
            @Override
            public int getOverdueDaysMax()
            {
                return IntegerParser.parse(request.getParameter("yuqiEndDays"));
            }
            
            @Override
            public String getPaymentType()
            {
                return request.getParameter("paymentType");
            }
            
            @Override
            public String getGuaranteeAgencies()
            {
                return request.getParameter("guaranteeAgencies");
            }
            
            @Override
            public Timestamp getPaymentDateStart()
            {
                return TimestampParser.parse(request.getParameter("actualDateStart"));
            }
            
            @Override
            public Timestamp getPaymentDateEnd()
            {
                return TimestampParser.parse(request.getParameter("actualDateEnd"));
            }

            @Override
            public String getLoanTitle()
            {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public Timestamp getActualDateStart()
            {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public Timestamp getActualDateEnd()
            {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public String getState()
            {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public String getSubject()
            {
                // TODO Auto-generated method stub
                return null;
            }

        };
        PagingResult<RepaymentStatisticsEntity> result = repaymentInfoManage.getPaymentAccountList(query, new Paging()
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
        repaymentInfoManage.exportPaymentAccount(result.getItems(),
            repaymentInfoManage.getPaymentAccountTotal(query),
            response.getOutputStream(),
            "");
    }
}
