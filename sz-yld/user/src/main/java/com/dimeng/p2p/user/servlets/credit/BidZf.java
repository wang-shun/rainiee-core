package com.dimeng.p2p.user.servlets.credit;

import java.io.PrintWriter;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.modules.bid.user.service.BidManage;
import com.dimeng.util.parser.IntegerParser;

@MultipartConfig
public class BidZf extends AbstractCreditServlet {

	private static final long serialVersionUID = -268955794485557959L;

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
		PrintWriter out = response.getWriter();
		try {
			BidManage bidManage = serviceSession.getService(BidManage.class);
			int loanId = IntegerParser.parse(request.getParameter("loanId"));
			int num = bidManage.updateBidStatus(loanId);
			if(num!=0){
			    out.print("success");
			}else{
			    out.print("failed");
			}
			
			
		} catch (Exception e) {
			logger.info(e.getMessage());
			out.print("failed");
		}finally{
			out.close();
		}
	}
}
