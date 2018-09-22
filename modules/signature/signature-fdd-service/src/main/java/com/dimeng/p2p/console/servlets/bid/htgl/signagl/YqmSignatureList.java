package com.dimeng.p2p.console.servlets.bid.htgl.signagl;

import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.AbstractFddServlet;
import com.dimeng.p2p.S61.enums.T6110_F06;
import com.dimeng.p2p.S62.enums.T6273_F07;

import com.dimeng.p2p.modules.bid.console.service.ElectronManage;
import com.dimeng.p2p.modules.bid.console.service.PtdzxyManage;
import com.dimeng.p2p.modules.bid.console.service.entity.Dzqm;
import com.dimeng.p2p.modules.bid.console.service.entity.Qyjkxy;
import com.dimeng.p2p.modules.bid.console.service.query.DzqmQuery;
import com.dimeng.p2p.modules.bid.console.service.query.DzxyQuery;
import com.dimeng.p2p.modules.bid.user.service.entity.QmjkEntity;
import com.dimeng.util.parser.IntegerParser;
import com.dimeng.util.parser.TimestampParser;

@Right(id = "P2P_C_BID_HTGL_DZQMGL",name = "电子签名管理", moduleId = "P2P_C_BID_HTGL_PTDZXY", order = 0)
public class YqmSignatureList extends AbstractFddServlet {

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
		ElectronManage electronManage = serviceSession.getService(ElectronManage.class);
		PagingResult<Dzqm>  result=electronManage.search(new DzqmQuery() {
			
			@Override
			public String getUserType() {
				return request.getParameter("userType");
			}
			
			@Override
			public String getUserName() {
				return request.getParameter("loginName");
			}
			
			@Override
			public String getTradeCode() {
				return request.getParameter("tradeCode");
			}
			
			@Override
			public String getQmState() {
				return request.getParameter("qmType");
			}
			
			@Override
			public String getName() {
				return request.getParameter("name");
			}
			
			@Override
			public String getHtType() {
				return request.getParameter("htType");
			}
			
			@Override
			public String getHtCode() {
				return request.getParameter("htCode");
			}
			
			@Override
			public Timestamp getCreateTimeStart() {
				return TimestampParser.parse(request.getParameter("createTimeStart"));
			}
			
			@Override
			public Timestamp getCreateTimeEnd() {
				return TimestampParser.parse(request.getParameter("createTimeEnd"));
			}
			
			@Override
			public String getCode() {
				return request.getParameter("code");
			}
			
			@Override
			public String getBidCode() {
				return request.getParameter("bidCode");
			}

			@Override
			public String getHtTitle() {
				return request.getParameter("htTitle");
			}
		}, T6273_F07.YQ, new Paging() {

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
		forwardView(request, response, getClass());
	}
}
