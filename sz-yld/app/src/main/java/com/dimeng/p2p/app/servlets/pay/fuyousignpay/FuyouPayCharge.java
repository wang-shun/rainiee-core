package com.dimeng.p2p.app.servlets.pay.fuyousignpay;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.p2p.PaymentInstitution;
import com.dimeng.p2p.app.entity.NewProtocolOrderXmlBeanReq;
import com.dimeng.p2p.app.entity.PayResult;
import com.dimeng.p2p.app.service.FuYouPayManageService;
import com.dimeng.p2p.app.servlets.AbstractFuyoupayServlet;
import com.dimeng.p2p.app.servlets.AbstractSecureServlet;
import com.dimeng.p2p.app.servlets.platinfo.ExceptionCode;
import com.dimeng.p2p.modules.account.pay.service.entity.ChargeOrder;
import com.dimeng.p2p.order.ChargeOrderExecutor;
import com.dimeng.p2p.variables.defines.URLVariable;
import com.dimeng.util.parser.BigDecimalParser;



public class FuyouPayCharge extends AbstractSecureServlet 
{
    
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
	{
		//富友协议支付
		ConfigureProvider configureProvider = getResourceProvider().getResource(ConfigureProvider.class);
		int accountId = serviceSession.getSession().getAccountId();
		BigDecimal amount = BigDecimalParser.parse(getParameter(request, "amount"));
		//用户id
		String userid = String.valueOf(accountId);
		//订单号
		String mchntorderid = "YLDCZ"+new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + userid;
        //用户ip地址
		String ip = serviceSession.getController().getRemoteAddr(request);
		NewProtocolOrderXmlBeanReq beanReq = new NewProtocolOrderXmlBeanReq();
		beanReq.setAmt(amount.toString());
		String userId="YLD"+userid;
		beanReq.setUserId(userId);
		beanReq.setUserIp(ip);
		beanReq.setMchntOrderId(mchntorderid);
		FuYouPayManageService manage = serviceSession.getService(FuYouPayManageService.class);
		ChargeOrderExecutor executor = getResourceProvider().getResource(ChargeOrderExecutor.class);
		ChargeOrder order = manage.addOrder(amount, PaymentInstitution.FUYOUPAY.getInstitutionCode());
		PayResult payresult = manage.signPay(beanReq,accountId);
		executor.submit(order.id, null);
		// 更新订单流水号
		manage.updateT6501(order.id, payresult.getMCHNTORDERID());
		if ("0000".equals(payresult.getRESPONSECODE()))
	    {
			logger.info("协议支付成功");
			manage.updateT6502(order.id, mchntorderid, "pc");
			executor.confirm(order.id, null);
			setReturnMsg(request, response, ExceptionCode.SUCCESS, "充值成功");
			return;
	    }
	    else
	    {
			logger.info("协议支付失败：" + payresult.getRESPONSEMSG());
			logger.info("开始执行，结束时间：" + new java.util.Date());
			setReturnMsg(request, response, ExceptionCode.UNKNOWN_ERROR, "充值失败:" + payresult.getRESPONSEMSG());
	    }
    }
   
}
