package com.dimeng.p2p.console.servlets.statistics.tjbb.zqzrtj;

import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.console.servlets.finance.AbstractFinanceServlet;
import com.dimeng.p2p.modules.statistics.console.service.TransferCreditorManage;
import com.dimeng.p2p.modules.statistics.console.service.entity.TransferCreditorEntity;
import com.dimeng.p2p.modules.statistics.console.service.query.TransferCreditorQuery;
import com.dimeng.util.parser.IntegerParser;
import com.dimeng.util.parser.TimestampParser;

@Right(id = "P2P_C_STATISTICS_EXPORT_ZQZRTJ", name = "导出还款统计", moduleId = "P2P_C_STATISTICS_TJBB_ZQZRTJ")
public class ExportTransferCreditor extends AbstractFinanceServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void processGet(HttpServletRequest request,
			HttpServletResponse response, ServiceSession serviceSession)
			throws Throwable {
		processPost(request, response, serviceSession);
	}

	@Override
	protected void processPost(final HttpServletRequest request,
			final HttpServletResponse response,
			final ServiceSession serviceSession) throws Throwable {
		TransferCreditorManage creditorManage = serviceSession.getService(TransferCreditorManage.class);
		response.setHeader("Content-disposition", "attachment;filename="
				+ new Timestamp(System.currentTimeMillis()).getTime() + ".csv");
		response.setContentType("application/csv");
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
				return Integer.MAX_VALUE;
			}

			@Override
			public int getCurrentPage() {
				return IntegerParser.parse(request.getParameter(PAGING_CURRENT));
			}
		});
		creditorManage.export(result.getItems(),creditorManage.getCreditorTotal(query),
				response.getOutputStream(), "");
	}
}
