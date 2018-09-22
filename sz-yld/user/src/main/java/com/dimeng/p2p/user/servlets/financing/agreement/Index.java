package com.dimeng.p2p.user.servlets.financing.agreement;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.S62.enums.T6230_F11;
import com.dimeng.p2p.modules.bid.user.service.BidManage;
import com.dimeng.p2p.modules.bid.user.service.entity.Bdxq;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.IntegerParser;

public class Index extends AbastractAgreementServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void processPost(HttpServletRequest request,
			HttpServletResponse response, ServiceSession serviceSession)
			throws Throwable {
		int creditId = IntegerParser.parse(request.getParameter("id"));

		//是否公益标
		String isGyb = request.getParameter("g");
		//如果是公益标,跳到公益标页面
		if(!StringHelper.isEmpty(isGyb)){
			forward(request, response, getController().getViewURI(request, Gyb.class));
			return;
		}
        
        //是否垫付
        String isDf = request.getParameter("df");
        //如果是垫付，跳到垫付协议页面
        if (!StringHelper.isEmpty(isDf))
        {
            forward(request, response, getController().getViewURI(request, Dfxy.class));
            return;
        }

		BidManage bidManage =	serviceSession.getService(BidManage.class);
		Bdxq bdxq = bidManage.get(creditId);
		if(bdxq==null){
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		if(bdxq.F11 == T6230_F11.S){
			forward(request, response, getController().getViewURI(request, Xydb.class));
		}else{
			forward(request, response, getController().getViewURI(request, Xyb.class));
		}
		

	}
	
	@Override
	protected void processGet(HttpServletRequest request,
			HttpServletResponse response, ServiceSession serviceSession)
			throws Throwable {
		processPost(request, response, serviceSession);
	}
}