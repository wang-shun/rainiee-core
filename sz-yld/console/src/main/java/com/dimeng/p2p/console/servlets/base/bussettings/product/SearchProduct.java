package com.dimeng.p2p.console.servlets.base.bussettings.product;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S62.entities.T6211;
import com.dimeng.p2p.S62.entities.T6216;
import com.dimeng.p2p.S62.enums.T6216_F04;
import com.dimeng.p2p.console.servlets.base.AbstractBaseServlet;
import com.dimeng.p2p.modules.base.console.service.ProductManage;
import com.dimeng.p2p.modules.base.console.service.query.ProductQuery;
import com.dimeng.p2p.modules.bid.console.service.BidManage;
import com.dimeng.util.parser.EnumParser;
import com.dimeng.util.parser.IntegerParser;

@Right(id = "P2P_C_BASE_SEARCHPRODUCT", isMenu = true, name = "标产品管理", moduleId = "P2P_C_BASE_OPTSETTINGS_PRODUCT", order = 0)
public class SearchProduct extends AbstractBaseServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void processGet(HttpServletRequest request,
			HttpServletResponse response, ServiceSession serviceSession)
			throws Throwable {
		BidManage bidManage = serviceSession.getService(BidManage.class);
		T6211[] t6211s = bidManage.getBidType();
		request.setAttribute("t6211s", t6211s);
		processPost(request, response, serviceSession);
	}

	@Override
	protected void processPost(final HttpServletRequest request,
			HttpServletResponse response, ServiceSession serviceSession)
			throws Throwable {
		BidManage bidManage = serviceSession.getService(BidManage.class);
		T6211[] t6211s = bidManage.getBidType();
		
		ProductManage productManage = serviceSession.getService(ProductManage.class);
		Paging paging = new Paging() {

			@Override
			public int getSize() {
				return 10;
			}

			@Override
			public int getCurrentPage() {
				return IntegerParser
						.parse(request.getParameter(PAGING_CURRENT));
			}
		};
		PagingResult<T6216> result = productManage.search(new ProductQuery() {

			@Override
			public T6216_F04 getStatus() {
				return EnumParser.parse(T6216_F04.class,
						request.getParameter("status"));
			}

			

			@Override
			public String getProductName() {
				return request.getParameter("name");
			}

			@Override
			public String getBidType() {
				
				return request.getParameter("bidType");
			}

			@Override
			public String getRepaymentWay() {
				return request.getParameter("repaymentWay");
			}
			
			
		}, paging);
		request.setAttribute("result", result);
		request.setAttribute("t6211s", t6211s);
		forwardView(request, response, getClass());
	}

}
