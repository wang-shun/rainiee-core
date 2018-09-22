package com.dimeng.p2p.console.servlets.finance.zjgl.fksh;

import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.console.servlets.finance.AbstractFinanceServlet;
import com.dimeng.p2p.modules.bid.console.service.FkshManage;
import com.dimeng.p2p.modules.bid.console.service.entity.Fksh;
import com.dimeng.p2p.modules.bid.console.service.query.FkshQuery;
import com.dimeng.util.parser.IntegerParser;
import com.dimeng.util.parser.TimestampParser;

@Right(id = "P2P_C_FINANCE_FKSHLIST", name = "放款管理", moduleId="P2P_C_FINANCE_ZJGL_FKGL", order = 0)
public class FkshList extends AbstractFinanceServlet {
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
		FkshManage fkshManage = serviceSession.getService(FkshManage.class);
        FkshQuery fkshQuery = new FkshQuery()
        {
            
            @Override
            public Timestamp getStartExpireDatetime()
            {
                return TimestampParser.parse(request.getParameter("startExpireDatetime"));
            }
            
            @Override
            public Timestamp getEndExpireDatetime()
            {
                return TimestampParser.parse(request.getParameter("endExpireDatetime"));
            }
            
            @Override
            public String getAccount()
            {
                return request.getParameter("account");
            }
            
            @Override
            public int getType()
            {
                return IntegerParser.parse(request.getParameter("type"));
            }
            
            @Override
            public String getZqId()
            {
                return request.getParameter("zqId");
            }
            
            @Override
            public String getStatus()
            {
                return request.getParameter("fundStatus");
            }
        };
        PagingResult<Fksh> result = fkshManage.search(fkshQuery, new Paging()
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
        Fksh searchAmount = fkshManage.searchAmount(fkshQuery);
		request.setAttribute("result", result);
        request.setAttribute("searchAmount", searchAmount);
		forwardView(request, response, getClass());
	}
}
