package com.dimeng.p2p.console.servlets.statistics.tjbb.hktj;

import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.console.servlets.statistics.AbstractStatisticsServlet;
import com.dimeng.p2p.modules.statistics.console.service.RepaymentInfoManage;
import com.dimeng.p2p.modules.statistics.console.service.entity.RepaymentStatisticsEntity;
import com.dimeng.p2p.modules.statistics.console.service.query.RepaymentStatisticsQuery;
import com.dimeng.util.parser.IntegerParser;
import com.dimeng.util.parser.TimestampParser;

@Right(id = "P2P_C_STATISTICS_REPAYMENT_STATISTICS", name = "还款统计表", moduleId = "P2P_C_STATISTICS_TJBB_HKTJ", order = 0)
public class AlreadyStatistics extends AbstractStatisticsServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void processGet(HttpServletRequest request,
			HttpServletResponse response, ServiceSession serviceSession)
			throws Throwable {
		processPost(request, response, serviceSession);
	}
	

	@Override
	protected void processPost(final HttpServletRequest request,
			HttpServletResponse response, ServiceSession serviceSession)
			throws Throwable {
        RepaymentInfoManage repaymentInfoManage = serviceSession.getService(RepaymentInfoManage.class);
		RepaymentStatisticsQuery query = new RepaymentStatisticsQuery() {
			@Override
			public String getId() {
				return request.getParameter("id");
			}

			@Override
			public Timestamp getShouldTheDateStart() {
				return TimestampParser.parse(request
						.getParameter("shouldTheDateStart"));
			}

			@Override
			public Timestamp getShouldTheDateEnd() {
				return TimestampParser.parse(request
						.getParameter("shouldTheDateEnd"));
			}
			
			@Override
			public String getLoanAccount() {
				return request.getParameter("loanAccount");
			}

            @Override
            public String getAccountState()
            {
                return request.getParameter("accountType");
            }
            
            @Override
            public Timestamp getActualDateStart()
            {
                return TimestampParser.parse(request.getParameter("actualDateStart"));
            }
            
            @Override
            public Timestamp getActualDateEnd()
            {
                return TimestampParser.parse(request.getParameter("actualDateEnd"));
            }

            @Override
            public int getOverdueDaysMin()
            {
                // TODO Auto-generated method stub
                return 0;
            }
            
            @Override
            public int getOverdueDaysMax()
            {
                // TODO Auto-generated method stub
                return 0;
            }
            
            @Override
            public String getPaymentType()
            {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public String getGuaranteeAgencies()
            {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public Timestamp getPaymentDateStart()
            {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public Timestamp getPaymentDateEnd()
            {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public String getLoanTitle()
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
        PagingResult<RepaymentStatisticsEntity> result = repaymentInfoManage.getAlreadyList(query, new Paging()
        {

			@Override
			public int getSize() {
				return 10;
			}

			@Override
			public int getCurrentPage() {
				return IntegerParser.parse(request.getParameter(PAGING_CURRENT));
			}
		});
		request.setAttribute("result", result);
        request.setAttribute("alreadyTotal", repaymentInfoManage.getAlreadyTotal(query));
		forwardView(request, response, getClass());
	}
}
