package com.dimeng.p2p.console.servlets.fuyou;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.http.servlet.AbstractServlet;
import com.dimeng.framework.http.servlet.Controller;
import com.dimeng.framework.http.session.authentication.AuthenticationException;
import com.dimeng.framework.resource.ResourceProvider;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.S65.entities.T6501;
import com.dimeng.p2p.S65.enums.T6501_F03;
import com.dimeng.p2p.order.WithdrawExecutor;
import com.dimeng.p2p.pay.service.fuyou.WithdrawService;
import com.dimeng.p2p.pay.service.fuyou.entity.Qrytransrsp;
import com.dimeng.p2p.pay.service.fuyou.entity.Trans;
import com.dimeng.p2p.pay.service.fuyou.util.Bean2XmlUtils;
import com.dimeng.p2p.pay.service.fuyou.util.HttpClientHandler;
import com.dimeng.p2p.pay.service.fuyou.varibles.FuyouPayVaribles;
import com.dimeng.p2p.variables.defines.URLVariable;

public class FuyouWithdrawCheckServlet extends AbstractServlet {
	
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void processGet(HttpServletRequest request,
			HttpServletResponse response, ServiceSession serviceSession)
			throws Throwable {
		// TODO Auto-generated method stub
		processPost(request, response, serviceSession);
	}

	@Override
	protected void processPost(HttpServletRequest request,
			HttpServletResponse response, ServiceSession serviceSession)
			throws Throwable {
		int orderId = Integer.parseInt(request.getParameter("orderId"));
		WithdrawService service = serviceSession.getService(WithdrawService.class);
				
        Map<String, String> requestParam = service.queryRequestParams(orderId);        
        String actionUrl = this.getConfigureProvider().format(FuyouPayVaribles.DAIFU_URL);
        
        // 发送请求拿到同步返回结果字符串
        String retString = HttpClientHandler.doPost(requestParam, actionUrl);
        logger.info("代付订单查询返回信息：" + retString);
        
        Qrytransrsp qrytransrsp = Bean2XmlUtils.xml2bean(retString, Qrytransrsp.class);        
        
        T6501 t6501 = service.selectT6501(orderId);
        // 返回结果
        if ("000000".equals(qrytransrsp.getRet())) {
        	WithdrawExecutor executor = getResourceProvider().getResource(WithdrawExecutor.class);
        	Trans trans = qrytransrsp.getTrans();
        	
        	if ("0".equals(trans.getState())) {
        		// 0 交易未发送
        		processRequest(request, response, "订单查询结果：交易未发送，无法对账！");
        	} else if ("1".equals(trans.getState()) && t6501.F03 == T6501_F03.CG) {
        		// 交易已发送且成功        		
        		processRequest(request, response, "订单查询结果：交易已发送且成功 ，无需对账！");
        	} else if ("1".equals(trans.getState()) && t6501.F03 != T6501_F03.CG) {
        		// 交易已发送且成功
        		service.updateT6501(orderId, T6501_F03.DQR);
        		executor.confirm(orderId, null);
        		processRequest(request, response, "订单查询结果：交易已发送且成功 ，对账成功！");
        	} else if ("2".equals(trans.getState()) && t6501.F03 == T6501_F03.CG) {
        		// 交易已发送且失败
        		service.trade(orderId, qrytransrsp.getMemo());
        		service.updateT6501(orderId, T6501_F03.SB);        		
        		processRequest(request, response, "订单查询结果：交易已发送且失败，对账成功！");
        	} else if ("2".equals(trans.getState()) && t6501.F03 != T6501_F03.CG) {
        		// 交易已发送且失败
        		service.updateT6501(orderId, T6501_F03.SB);
        		processRequest(request, response, "订单查询结果：交易已发送且失败，对账失败！");
        	} else if ("3".equals(trans.getState())) {
        		// 交易发送中
        		processRequest(request, response, "订单查询结果：交易发送中，请稍后对账！");
        	} else if ("7".equals(trans.getState()) && t6501.F03 == T6501_F03.CG) {
        		// 交易已发送且超时
        		service.trade(orderId, qrytransrsp.getMemo());
        		service.updateT6501(orderId, T6501_F03.SB);        		
        		processRequest(request, response, "订单查询结果：交易已发送且超时，对账成功！");
        	} else if ("7".equals(trans.getState()) && t6501.F03 != T6501_F03.CG) {
        		// 交易已发送且超时
        		service.updateT6501(orderId, T6501_F03.SB);
        		processRequest(request, response, "订单查询结果：交易已发送且超时，对账失败！");
        	}
            logger.info("代付成功返回，返回码：" + qrytransrsp.getRet() + "备注：" + qrytransrsp.getMemo());
            return;
        } else {
        	logger.info("代付失败，返回码：" + qrytransrsp.getRet() + "备注：" + qrytransrsp.getMemo());
        	processRequest(request, response, "订单查询失败，原因：".concat(qrytransrsp.getMemo()));
            return;
        }
	}	
	
	@Override
	protected void onThrowable(HttpServletRequest request,
			HttpServletResponse response, Throwable throwable)
			throws ServletException, IOException {
		ResourceProvider resourceProvider = getResourceProvider();
		if (throwable instanceof AuthenticationException) {
			Controller controller = getController();
			controller.redirectLogin(request, response,
					resourceProvider.getResource(ConfigureProvider.class)
							.format(URLVariable.LOGIN));
		} else {
			resourceProvider.log(throwable);
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}

	protected ConfigureProvider getConfigureProvider() {
		return getResourceProvider().getResource(ConfigureProvider.class);
	}
	
	protected void processRequest(HttpServletRequest request, HttpServletResponse response, String msg) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            response.setContentType("text/html");
            response.setHeader("Cache-Control", "no-store");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);
            if ("OK".equals(msg)) {
                out.write("OK");
            } else {
                out.write(msg);
            }
        } finally {
            out.close();
        }
    }

}
