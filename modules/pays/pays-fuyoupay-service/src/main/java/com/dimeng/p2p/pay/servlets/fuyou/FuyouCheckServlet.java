package com.dimeng.p2p.pay.servlets.fuyou;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Decoder.BASE64Decoder;
import Decoder.BASE64Encoder;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.S65.entities.T6502;
import com.dimeng.p2p.S65.enums.T6501_F03;
import com.dimeng.p2p.order.ChargeOrderExecutor;
import com.dimeng.p2p.pay.service.fuyou.ChargeService;
import com.dimeng.p2p.pay.service.fuyou.entity.APPH5QueryOrderRetrsp;
import com.dimeng.p2p.pay.service.fuyou.entity.ChargeAp;
import com.dimeng.p2p.pay.service.fuyou.entity.ChargeOrder;
import com.dimeng.p2p.pay.service.fuyou.entity.OrderQryByMSsn;
import com.dimeng.p2p.pay.service.fuyou.entity.Plain;
import com.dimeng.p2p.pay.service.fuyou.util.Bean2XmlUtils;
import com.dimeng.p2p.pay.service.fuyou.util.FuYouMD5;
import com.dimeng.p2p.pay.service.fuyou.util.HttpClientHandler;
import com.dimeng.p2p.pay.service.fuyou.varibles.FuyouPayVaribles;
import com.dimeng.p2p.variables.defines.URLVariable;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.IntegerParser;

public class FuyouCheckServlet extends AbstractFuyouServlet {

	private static final long serialVersionUID = 1L;
	
	@Override
	protected void processGet(HttpServletRequest request,
			HttpServletResponse response, ServiceSession serviceSession)
			throws Throwable {
		// TODO Auto-generated method stub
		this.processPost(request, response, serviceSession);
	}

	@Override
	protected void processPost(HttpServletRequest request,
			HttpServletResponse response, ServiceSession serviceSession)
			throws Throwable {
		String orderId = request.getParameter("o");
		ChargeService service = serviceSession.getService(ChargeService.class);
		
		ConfigureProvider configureProvider=this.getConfigureProvider();
		String url = configureProvider.format(FuyouPayVaribles.CHARGE_CHECK_URL);
		
		Map<String, String> params = new HashMap<String, String>();
		String mchnt_cd = configureProvider.format(FuyouPayVaribles.MCHNT_CD);
		String mchnt_key = configureProvider.format(FuyouPayVaribles.MCHNT_KEY); //32位的商户密钥
		String order_id = mchnt_cd.concat(orderId);
		
		params.put("mchnt_cd", mchnt_cd);  // 商户代码 富友分配给各合作商户的唯一识别码
		params.put("order_id", order_id);   // 商户订单号 客户支付后商户网站产生的一个唯一的定单
		T6502 t6502 = service.selectT6502(Integer.parseInt(orderId));
		String pageUrl = "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/user/capital/unpay.html";
		if (t6502.F11.equals("GATE")) {
			/*MD5摘要数据	md5		mchnt_cd + "|"  +order_id + "|"  + mchnt_key
			做md5摘要	其中mchnt_key 为32位的商户密钥，系统分配	*/	
			String signDataStr = mchnt_cd + "|" + order_id+ "|" + mchnt_key;
			logger.info("网银充值订单号".concat(order_id).concat("查询签名前参数字符串：".concat(signDataStr)));
			String md5 = FuYouMD5.MD5Encode(signDataStr);

			params.put("md5", md5);
	        
			
			String retString = HttpClientHandler.doPost(params, url);
			
			logger.info("网银充值订单号".concat(order_id).concat("查询返回的字符串为:".concat(retString)));
			
			String returnDataStr = retString.substring(retString.indexOf("<order_pay_code>"), retString.lastIndexOf("</plain>")) +  mchnt_key;
			
			String returnMd5 = FuYouMD5.MD5Encode(returnDataStr);
			ChargeAp ret = Bean2XmlUtils.xml2bean(retString, ChargeAp.class);
			Plain plain = ret.getPlain();
			
			if (returnMd5.equals(ret.getMd5())) {
				String code = plain.getOrder_pay_code();
				if (null != code && !"".equals(code) && "0000".equals(code)) {
						String status = plain.getOrder_st();
						/* 
						 * ‘00’ – 订单已生成(初始状态)‘01’ – 订单已撤消‘02’ – 订单已合并‘03’ – 订单已过期‘04’ – 订单已确认(等待支付)
						 * ‘05’ – 订单支付失败‘11’ – 订单已支付‘12’ – 订单已退款
						 */		
						if("11".equals(status)){
							ChargeOrder order = service.getChargeOrder(IntegerParser.parse(orderId));
							if (order != null && order.status != T6501_F03.CG) {
								service.updateT6501(order.id, T6501_F03.DQR);
								ChargeOrderExecutor chargeOrderExecutor = getResourceProvider().getResource(ChargeOrderExecutor.class);
								Map<String, String> param = new HashMap<>();
								param.put("paymentOrderId", plain.getFy_ssn());
								chargeOrderExecutor.confirm(order.id, param);
							}						
							this.prompt(request, response, PromptLevel.INFO, "订单状态更正成功，已成功修改您的账户数据，请查看确认！");
							pageUrl = URLVariable.USER_CAPITAL.getValue();
		                }else if("12".equals(status)){
		                	this.prompt(request, response, PromptLevel.INFO, "查询结果：订单已退款！");
		                	service.updateT6501(IntegerParser.parse(orderId), T6501_F03.SB);
		                }else if("05".equals(status)){
		                	this.prompt(request, response, PromptLevel.INFO, "查询结果：订单已支付失败！");
		                	service.updateT6501(IntegerParser.parse(orderId), T6501_F03.SB);
		                }else if("04".equals(status)){
		                	this.prompt(request, response, PromptLevel.INFO, "查询结果：订单已确认(等待支付中)！");
		                }else if("00".equals(status)){
		                	this.prompt(request, response, PromptLevel.INFO, "查询结果：订单已生成(初始状态)，请稍后对账！");
		                }else if("01".equals(status)){
		                	this.prompt(request, response, PromptLevel.INFO, "查询结果：订单已撤销！");
		                	service.updateT6501(IntegerParser.parse(orderId), T6501_F03.SB);
		                }else if("02".equals(status)){
		                	this.prompt(request, response, PromptLevel.INFO, "查询结果：订单已合并！");
		                	service.updateT6501(IntegerParser.parse(orderId), T6501_F03.SB);
		                }else if("03".equals(status)){
		                	this.prompt(request, response, PromptLevel.INFO, "查询结果：订单已过期！");
		                	service.updateT6501(IntegerParser.parse(orderId), T6501_F03.SB);
		                }else{
		                	this.prompt(request, response, PromptLevel.INFO, plain.getOrder_pay_error() + "返回码:["+ code +"]！");
		                }
				} else {
					this.prompt(request, response, PromptLevel.INFO, "查询结果：订单未提交到易宝支付系统！");
					service.updateT6501(IntegerParser.parse(orderId), T6501_F03.SB);
				}
			} else {
				this.prompt(request, response, PromptLevel.INFO, "验签失败");
			}
			this.sendRedirect(request, response, StringHelper.format(pageUrl, this.getConfigureProvider()));
		} else {
			String back_notify_url = StringHelper.format("${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/pay/fuyou/fuyouChargeRetServlet.htm?type=query", this.getResourceProvider().getResource(ConfigureProvider.class));
			// 商户私钥     
	        String private_key = this.getConfigureProvider().format(FuyouPayVaribles.PRIVATE_KEY);
	        if ("PC".equals(t6502.chargeSource))
            {
			String signDataStr = mchnt_cd + "|" + order_id + "|"
			                   + back_notify_url + "|" + "" ;
			byte[] bytesKey = (new BASE64Decoder()).decodeBuffer(private_key.trim());
			PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(bytesKey);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			PrivateKey priKey = keyFactory.generatePrivate(pkcs8KeySpec);
			Signature signature = Signature.getInstance("MD5WithRSA");
			signature.initSign(priKey);
			signature.update(signDataStr.getBytes("GBK"));
			String RSA = (new BASE64Encoder()).encodeBuffer(signature.sign());
			logger.info("快捷支付充值订单查询 RSA==="+RSA);
	
			logger.info("快捷支付充值订单查询 signDataStr==="+signDataStr);
			url = this.getConfigureProvider().format(FuyouPayVaribles.QUICK_CHARGE_CHECK_URL);
	        
			params.put("page_notify_url", back_notify_url);
	        params.put("back_notify_url",  "");
	        params.put("RSA", RSA);
			
			response.setContentType("text/html");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(this.createSendForm(params, url));		
            }else{
            	 //移动端快捷充值对账查询
                params.clear();
                
                String mchnt_cd_H5 = configureProvider.format(FuyouPayVaribles.MCHNT_CD_H5);
                String mchnt_key_H5 = configureProvider.format(FuyouPayVaribles.MCHNT_KEY_H5); //移动端32位的商户密钥
                OrderQryByMSsn data = new OrderQryByMSsn();
                data.setMchntCd(mchnt_cd_H5);
                data.setMchntOrderId(mchnt_cd_H5.concat(orderId));
                data.setRem1("");
                data.setRem2("");
                data.setRem3("");
                data.setVersion("1.0");
                params.put("FM", data.buildXml(mchnt_key_H5));
                url = this.getConfigureProvider().format(FuyouPayVaribles.QUICK_CHARGE_APP_CHECK_URL);
             // 发送请求拿到同步返回结果字符串
                String respStr =   HttpClientHandler.doPost(params, url);//HttpPostUtil.postForward(url, params);
                logger.info("移动端快捷支付充值订单查询返回信息：" + respStr);



                /*params.put("VERSION", version);
                params.put("MCHNTCD", mchnt_cd);
                params.put("MCHNTORDERID", order_id);
                String signDataStr = version + "|" + mchnt_cd + "|" + order_id + "|" + mchnt_key;
                logger.info("移动端快捷支付充值订单查询 signDataStr===" + signDataStr);
                signDataStr = FuYouMD5.MD5Encode(signDataStr);
                params.put("SIGN", signDataStr);
                logger.info("移动端快捷支付充值订单查询MD5 signDataStr===" + signDataStr);
                // 发送请求拿到同步返回结果字符串
                String retString = HttpClientHandler.doPost(params, url);
                logger.info("移动端快捷支付充值订单查询返回信息：" + retString);*/
                APPH5QueryOrderRetrsp retRsp = Bean2XmlUtils.xml2bean(respStr, APPH5QueryOrderRetrsp.class);
                String returnDataStr =
                    retRsp.VERSION + "|" + retRsp.RESPONSECODE + "|" + retRsp.RESPONSEMSG + "|" + retRsp.MCHNTORDERID
                        + "|" + mchnt_key_H5;
                String returnMd5 = FuYouMD5.MD5Encode(returnDataStr);
                if (returnMd5.equals(retRsp.SIGN))
                {
                    if ("0000".equals(retRsp.RESPONSECODE))
                    {
                        ChargeOrder order = service.getChargeOrder(IntegerParser.parse(orderId));
                        if (order != null && order.status != T6501_F03.CG)
                        {
                            service.updateT6501(order.id, T6501_F03.DQR);
                            ChargeOrderExecutor chargeOrderExecutor =
                                getResourceProvider().getResource(ChargeOrderExecutor.class);
                            Map<String, String> param = new HashMap<>();
                            param.put("paymentOrderId", "移动端快捷充值商户订单：" + order_id);
                            chargeOrderExecutor.confirm(order.id, param);
                        }
                        this.prompt(request, response, PromptLevel.INFO, "订单状态更正成功，已成功修改您的账户数据，请查看确认！");
                        pageUrl = URLVariable.USER_CAPITAL.getValue();
                    }
                    else
                    {
                        this.prompt(request, response, PromptLevel.INFO, "查询结果：订单已支付失败！");
                        service.updateT6501(IntegerParser.parse(orderId), T6501_F03.SB);
                    }
                }
                else
                {
                    this.prompt(request, response, PromptLevel.INFO, "验签失败");
                }
                this.sendRedirect(request, response, StringHelper.format(pageUrl, this.getConfigureProvider()));
            }
		}
	}

}
