package com.dimeng.p2p.pay.servlets.fuyou;

import java.io.IOException;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Decoder.BASE64Decoder;

import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.S65.entities.T6502;
import com.dimeng.p2p.S65.enums.T6501_F03;
import com.dimeng.p2p.order.ChargeOrderExecutor;
import com.dimeng.p2p.pay.service.fuyou.ChargeService;
import com.dimeng.p2p.pay.service.fuyou.entity.ChargeOrder;
import com.dimeng.p2p.pay.service.fuyou.util.FuYouMD5;
import com.dimeng.p2p.pay.service.fuyou.util.StringUtils;
import com.dimeng.p2p.pay.service.fuyou.varibles.FuyouPayVaribles;
import com.dimeng.p2p.variables.defines.URLVariable;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.IntegerParser;

public class FuyouChargeRetServlet extends AbstractFuyouServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void processPost(HttpServletRequest request,
			HttpServletResponse response, ServiceSession serviceSession)
			throws Throwable {
		String mchnt_cd = StringUtils.nvl(request.getParameter("mchnt_cd"));
		String order_id = StringUtils.nvl(request.getParameter("order_id"));
		int orderId = Integer.parseInt(order_id.substring(mchnt_cd.length()));
		ChargeService service = serviceSession.getService(ChargeService.class);			
		T6502 t6502 = service.selectT6502(orderId);
		
		Map<String, String> params = new HashMap<String, String>();
		Map<String, String> param = new HashMap<>();
		ChargeOrderExecutor chargeOrderExecutor = getResourceProvider().getResource(ChargeOrderExecutor.class);
		if (t6502.F11.equals("GATE")) {
			params = this.getRequestParams(request);
			if (this.getHandleResults(params)) {
				logger.info("网银支付充值订单返回参数 TransID is " + params.get("fy_ssn") + "  result is " + params.get("order_pay_code"));
				if ("0000".equals(params.get("order_pay_code"))) {
					ChargeOrder order = service.getChargeOrder(orderId);
					if (order != null && order.status != T6501_F03.CG) {
						param.put("paymentOrderId", params.get("fy_ssn"));
						chargeOrderExecutor.confirm(order.id, param);
					}
					response.getWriter().write("ok");
				} else {
					service.updateT6501(orderId, T6501_F03.SB);
					logger.info("网银支付充值订单 charge fail ");
				}
			} else {
				service.updateT6501(orderId, T6501_F03.SB);
				logger.info("网银支付充值订单 sign error");
			}
		} else {
		    String queryString = request.getQueryString();
		    logger.info("快捷支付充值订单返回 FuyouChargeRetServlet 认证支付返回 queryString = "+queryString);
			
			String order_amt = StringUtils.nvl(request.getParameter("order_amt"));
			String order_date = StringUtils.nvl(request.getParameter("order_date"));
			String order_st = StringUtils.nvl(request.getParameter("order_st"));
			String order_pay_code = StringUtils.nvl(request.getParameter("order_pay_code")).replaceAll(" ", "");
			String order_pay_error = StringUtils.nvl(request.getParameter("order_pay_msg"));
			String fy_ssn = StringUtils.nvl(request.getParameter("fy_ssn"));
			String user_id = StringUtils.nvl(request.getParameter("user_id"));
			String card_no = StringUtils.nvl(request.getParameter("card_no"));
			String RSA = StringUtils.nvl(request.getParameter("RSA"));
			String publicKey = this.getConfigureProvider().format(FuyouPayVaribles.PUBLIC_KEY);    
			String	signDataStr = mchnt_cd + "|" + user_id+ "|" +order_id+ "|" +order_amt+ "|" +
							order_date+ "|" +card_no+ "|" +fy_ssn;
			logger.info("快捷支付充值订单返回 返签前参数字符串：" + signDataStr);
			byte[] keyBytes = (new BASE64Decoder()).decodeBuffer(publicKey);
			// 构造X509EncodedKeySpec对象  
			X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
			// KEY_ALGORITHM 指定的加密算法  
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			// 取公钥匙对象  
			PublicKey pubKey = keyFactory.generatePublic(keySpec);
			Signature signature = Signature.getInstance("MD5withRSA");
			signature.initVerify(pubKey);
			signature.update(signDataStr.getBytes("gbk"));
			// 验证签名是否正常  
			boolean ret = signature.verify((new BASE64Decoder()).decodeBuffer(RSA));
			logger.info("快捷支付充值订单返回 RSA验签结果：" + ret);
			String pageUrl = URLVariable.USER_CHARGE.getValue();
			if (null != request.getParameter("type") && !"".equals(request.getParameter("type"))) {
				pageUrl = "http://${SYSTEM.SITE_DOMAIN}/user/capital/unpay.html";
			}
			if(ret){
				if ("0000".equals(order_pay_code) && "11".equals(order_st)) {
					ChargeOrder order = service.getChargeOrder(IntegerParser.parse(orderId));
					if (order != null && order.status != T6501_F03.CG) {
						service.updateT6501(order.id, T6501_F03.DQR);
						param.put("paymentOrderId", fy_ssn);
						chargeOrderExecutor.confirm(order.id, param);
					}
					service.updateT6114(card_no);
					response.getWriter().write("ok");
					pageUrl = URLVariable.USER_CAPITAL.getValue();
					this.prompt(request, response, PromptLevel.INFO, "充值成功！");
				} else {
					service.updateT6501(orderId, T6501_F03.SB);
					logger.info("快捷支付充值订单返回 charge fail " + order_pay_error);
					this.prompt(request, response, PromptLevel.INFO, "充值失败，原因：" + order_pay_error);
				}
			} else {
				service.updateT6501(orderId, T6501_F03.SB);
				this.prompt(request, response, PromptLevel.INFO, "验签失败");
			}
			this.sendRedirect(request, response, StringHelper.format(pageUrl, this.getConfigureProvider()));
		}
	}

	@Override
	protected boolean mustAuthenticated() {
		return false;
	}
	
	/**
     * 同步返回数据接收
     * @param request
     * @return Map 返回集合
     * @throws Throwable 
     * @throws IOException 
     */
    private Map<String, String> getRequestParams(HttpServletRequest request) throws Throwable{        
        Map<String, String> params = new HashMap<String, String>();        
        params.put("mchnt_cd", request.getParameter("mchnt_cd"));    // 商户代码	富友分配给各合作商户的唯一识别码
        params.put("order_id", request.getParameter("order_id"));    // 商户订单号 客户支付后商户网站产生的一个唯一的定单号，该订单号应该在相当长的时间内不重复。富友通过订单号来唯一确认一笔订单的重复性。
        params.put("order_date", request.getParameter("order_date"));// 订单日期
        params.put("order_amt", request.getParameter("order_amt"));  // 交易金额	客户支付订单的总金额，一笔订单一个，以分为单位。不可以为零，必需符合金额标准
        /**
         * 订单状态	 
         * ‘00’ – 订单已生成(初始状态)        
         * ‘01’ – 订单已撤消   
         * ‘02’ – 订单已合并        
         * ‘03’ – 订单已过期        
         * ‘04’ – 订单已确认(等待支付)        
         * ‘05’ – 订单支付失败        
         * ‘11’ – 订单已支付       
         *  ‘18’ – 已发货        
         *  ‘19’ – 已确认收货
         */
        params.put("order_st", request.getParameter("order_st"));
        params.put("order_pay_code", request.getParameter("order_pay_code"));  // 错误代码 0000表示成功 其他失败
        params.put("order_pay_error", request.getParameter("order_pay_error"));// 错误中文描述
        params.put("resv1", request.getParameter("resv1"));                    // 保留字段
        params.put("fy_ssn", request.getParameter("fy_ssn"));                  // 富友流水号 	供商户查询支付交易状态及对账用
        params.put("md5", request.getParameter("md5")); 
        params.put("terminal", request.getParameter("terminal"));
        return params;        
    }
    
    /**
     * 充值结果验签
     * @param params
     * @return
     * @throws Throwable 
     */
    private boolean getHandleResults(Map<String, String> retParams) throws Throwable{
    	String mchnt_key = this.getConfigureProvider().format(FuyouPayVaribles.MCHNT_KEY);
        String signDataStr = retParams.get("mchnt_cd") +       "|" +  retParams.get("order_id") + "|" +
        					 retParams.get("order_date") +     "|" +  retParams.get("order_amt")+ "|" + 
        					 retParams.get("order_st")+        "|" +  retParams.get("order_pay_code")+ "|" + 
        					 retParams.get("order_pay_error")+ "|" +  retParams.get("resv1") + "|"  + 
        					 retParams.get("fy_ssn") +         "|" +  mchnt_key;

        logger.info("返签前参数字符串：" + signDataStr);
        String md5 = FuYouMD5.MD5Encode(signDataStr);
        logger.info("MD5签名后：" + md5);
         // MD5验签
        if(md5.equals(retParams.get("md5"))){
            return true;            
        }
        return false;
    }

}
