package com.dimeng.p2p.console.servlets.bid.lcgl.zqgl;

import java.sql.Timestamp;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.console.servlets.bid.AbstractBidServlet;
import com.dimeng.p2p.modules.financial.console.service.CreditorManage;
import com.dimeng.p2p.modules.financial.console.service.entity.Creditor;
import com.dimeng.p2p.modules.financial.console.service.query.CreditorQuery;
import com.dimeng.util.parser.DateParser;
import com.dimeng.util.parser.IntegerParser;

@Right(id = "P2P_C_BUSI_ZQGL_EXPORT",  name = "导出",moduleId="P2P_C_BID_LCGL_ZQGL",order=1)
public class YjqExport extends AbstractBidServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void processGet(HttpServletRequest request,
			HttpServletResponse response, ServiceSession serviceSession)
			throws Throwable {
		processPost(request, response, serviceSession);
	}
	
	@Override
	protected void processPost(final HttpServletRequest request,final HttpServletResponse response,
			final ServiceSession serviceSession) throws Throwable {
		CreditorManage creditorManage = serviceSession.getService(CreditorManage.class);
		response.setHeader("Content-disposition", "attachment;filename="
				+ new Timestamp(System.currentTimeMillis()).getTime() + ".csv");
		response.setContentType("application/csv");
		PagingResult<Creditor> creditors = creditorManage.creditorYjqSearch(new CreditorQuery() {
			
		    @Override
            public String getUserName() {
                return request.getParameter("userName");
            }
            
            @Override
            public String getCreditorId() {
                return request.getParameter("creditorId");
            }
            
            @Override
            public Timestamp getCreateTimeStart() {
                Date date = DateParser.parse(request
                        .getParameter("createTimeStart"));
                return date == null ? null : new Timestamp(date.getTime());
            }
            
            @Override
            public Timestamp getCreateTimeEnd() {
                Date date = DateParser.parse(request
                        .getParameter("createTimeEnd"));
                return date == null ? null : new Timestamp(date.getTime());
            }
            
			@Override
			public String getLoanRecordTitle() {
				return request.getParameter("loanRecordTitle");
			}
            
            @Override
            public String getInvestUserType()
            {
                return request.getParameter("investUserType");
            }
            
            @Override
            public String getCreditorType()
            {
                return request.getParameter("creditorType");
            }
            
            @Override
            public String getSellUserType()
            {
                return request.getParameter("sellUserType");
            }
            
            @Override
            public String getBugUserType()
            {
                return request.getParameter("buyUserType");
            }
        }, new Paging() {
            
            @Override
            public int getSize() {
                return Integer.MAX_VALUE;
            }

            @Override
            public int getCurrentPage() {
                return IntegerParser.parse(request.getParameter(PAGING_CURRENT));
            }
        });
		creditorManage.exportYjq(creditors.getItems(), response.getOutputStream(), "");
	}

}
