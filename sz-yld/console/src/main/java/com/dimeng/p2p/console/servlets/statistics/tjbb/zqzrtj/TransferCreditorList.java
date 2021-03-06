package com.dimeng.p2p.console.servlets.statistics.tjbb.zqzrtj;

import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.console.servlets.statistics.AbstractStatisticsServlet;
import com.dimeng.p2p.modules.statistics.console.service.TransferCreditorManage;
import com.dimeng.p2p.modules.statistics.console.service.entity.TransferCreditorEntity;
import com.dimeng.p2p.modules.statistics.console.service.query.TransferCreditorQuery;
import com.dimeng.util.parser.IntegerParser;
import com.dimeng.util.parser.TimestampParser;

@Right(id = "P2P_C_STATISTICS_TRANSFER_CREDITOR", name = "债权转让统计表", moduleId = "P2P_C_STATISTICS_TJBB_ZQZRTJ", order = 0)
public class TransferCreditorList extends AbstractStatisticsServlet {

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
		TransferCreditorManage creditorManage = serviceSession.getService(TransferCreditorManage.class);

		TransferCreditorQuery query = new TransferCreditorQuery() {

			@Override
			public String getLoanId() {
				return request.getParameter("loanId");
			}

			@Override
			public String getCreditorId() {
				return request.getParameter("creditorId");
			}
			
			@Override
			public String getSellAccount() {
				return request.getParameter("sellAccount");
			}
			
			@Override
			public String getBuyAccount() {
				return request.getParameter("buyAccount");
			}

			@Override
			public Timestamp getApplyTimeStart() {
				return TimestampParser.parse(request
						.getParameter("applyTimeStart"));
			}

			@Override
			public Timestamp getApplyTimeEnd() {
				return TimestampParser.parse(request
						.getParameter("applyTimeEnd"));
			}
			
			@Override
			public Timestamp getBuyTimeStart() {
				return TimestampParser.parse(request
						.getParameter("buyTimeStart"));
			}

			@Override
			public Timestamp getBuyTimeEnd() {
				return TimestampParser.parse(request
						.getParameter("buyTimeEnd"));
			}

			@Override
			public String getSource() {
				return request.getParameter("source");
			}
            
            @Override
            public String getSellUserType()
            {
                return request.getParameter("sellUserType");
            }
            
            @Override
            public String getBuyUserType()
            {
                return request.getParameter("buyUserType");
            }

		};
		PagingResult<TransferCreditorEntity> result = creditorManage.getCreditorList(query, new Paging() {

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
		request.setAttribute("creditorTotalMap", creditorManage.getCreditorTotal(query));
		forwardView(request, response, getClass());
	}
}
