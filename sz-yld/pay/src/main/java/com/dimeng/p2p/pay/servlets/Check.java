package com.dimeng.p2p.pay.servlets;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.PaymentInstitution;
import com.dimeng.p2p.S65.enums.T6501_F03;
import com.dimeng.p2p.modules.account.pay.service.ChargeManage;
import com.dimeng.p2p.modules.account.pay.service.entity.ChargeOrder;
import com.dimeng.util.parser.IntegerParser;

/**
 * 充值查询
 */
public class Check extends AbstractPayServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void processPost(HttpServletRequest request,
			HttpServletResponse response, ServiceSession serviceSession)
			throws Throwable {
		response.setContentType("text/html; charset=utf-8");
		int o = IntegerParser.parse(request.getParameter("o"));
		ChargeManage manage = serviceSession.getService(ChargeManage.class);
		ChargeOrder order = manage.getLockChargeOrder(o);
		if (order == null || order.payCompanyCode <= 0) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		if (order.status == T6501_F03.CG) {
			PrintWriter out = response.getWriter();
			out.print("订单已支付成功");
			out.flush();
			out.close();
			return;
		}
		manage.updateOrder(o);
		
		PaymentInstitution[] institutions = PaymentInstitution.values();
        if(institutions == null || institutions.length <= 0){
          //跳转到错误页面
           response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
        request.setAttribute("order", order);
        for(PaymentInstitution ins : institutions){
            if(ins.getInstitutionCode() == order.payCompanyCode){
                request.getRequestDispatcher(ins.getCheckUri()).forward(request, response);
                break;
            }
        }
	}
}
