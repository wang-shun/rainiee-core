package com.dimeng.p2p.pay.servlets.fuyou;

import java.math.BigDecimal;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.p2p.PaymentInstitution;
import com.dimeng.p2p.S50.entities.T5020;
import com.dimeng.p2p.S61.entities.T6141;
import com.dimeng.p2p.order.ChargeOrderExecutor;
import com.dimeng.p2p.pay.service.fuyou.ChargeService;
import com.dimeng.p2p.pay.service.fuyou.entity.ChargeOrder;
import com.dimeng.p2p.pay.service.fuyou.util.FuYouMD5;
import com.dimeng.p2p.pay.service.fuyou.varibles.FuyouPayVaribles;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.BigDecimalParser;
import com.dimeng.util.parser.IntegerParser;

import Decoder.BASE64Decoder;
import Decoder.BASE64Encoder;

public class FuyouChargeServlet extends AbstractFuyouServlet
{
    
    private static final long serialVersionUID = 1L;
    
    private static final String PageUrl_URL =
        "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/pay/fuyou/fuyouPageRetServlet.htm";
        
    private static final String ReturnUrl_URL =
        "${SYSTEM.SITE_REQUEST_PROTOCOL}${SYSTEM.SITE_DOMAIN}/pay/fuyou/fuyouChargeRetServlet.htm";
        
    @Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        ChargeService service = serviceSession.getService(ChargeService.class);
        BigDecimal amount = BigDecimalParser.parse(request.getParameter("amount"));
        String chargeType = request.getParameter("chargeType");
        String bankNum = request.getParameter("banknumber").replaceAll(" ", "");
        int bankId = Integer.parseInt(request.getParameter("bankname"));
        //如果是快捷支付，必须是绑了银行卡的
        if (chargeType.equals("QUICK"))
        {
            boolean isBindleBanked = service.isBindleBanked();
            if (!isBindleBanked)
            {
                throw new LogicalException("您还没有银行卡，请先添加银行卡");
            }
            bankId = IntegerParser.parse(request.getParameter("bankId"));
        }
        String city = request.getParameter("xian");
        String chargeSource = StringHelper.isEmpty(request.getParameter("chargeSource"))?"PC":request.getParameter("chargeSource");
        ChargeOrder order = service.addOrder(amount,
            PaymentInstitution.FUYOUPAY.getInstitutionCode(),
            bankNum,
            chargeType,
            bankId,
            city,chargeSource);
            
        ChargeOrderExecutor chargeOrderExecutor = getResourceProvider().getResource(ChargeOrderExecutor.class);
        chargeOrderExecutor.submit(order.id, null);
        
        // 商户代码 富友分配给各合作商户的唯一识别码	必填	
        String mchnt_cd = this.getConfigureProvider().format(FuyouPayVaribles.MCHNT_CD);
        // 商户订单号 客户支付后商户网站产生的一个唯一的定单号，该订单号应该在相当长的时间内不重复。富友通过订单号来唯一确认一笔订单的重复性。	必填	
        String order_id = mchnt_cd.concat(String.valueOf(order.id));
        // 交易金额 	客户支付订单的总金额，一笔订单一个，以分为单位。不可以为零，必需符合金额标准。	必填
        String order_amt = amount.multiply(new BigDecimal(100)).setScale(0).toString();
        // 页面跳转URL	商户接收支付结果通知地址	必填	       
        String page_notify_url =
            StringHelper.format(PageUrl_URL, this.getResourceProvider().getResource(ConfigureProvider.class));
        // 后台通知URL	商户接收支付结果后台通知地址	非必填   
        String back_notify_url =
            StringHelper.format(ReturnUrl_URL, this.getResourceProvider().getResource(ConfigureProvider.class));
            
        Map<String, String> params = new LinkedHashMap<String, String>();
        params.put("mchnt_cd", mchnt_cd);
        params.put("order_id", order_id);
        params.put("order_amt", order_amt);
        
        String url = this.getConfigureProvider().format(FuyouPayVaribles.CHARGE_URL);
        
        //网银支付
        if (chargeType.equals("GATE"))
        {
            String payway = request.getParameter("payway");
            
            T5020 t5020 = service.selectT5020(bankId);
            // 支付类型	‘B2C’ – B2C支付        ‘B2B’ – B2B支付        ‘FYCD’ – 预付卡        ‘SXF’ – 随心富	必填	B2C
            String order_pay_type = payway;
            String iss_ins_cd = t5020.F04; // 银行代码        
            String goods_name = ""; // 商品名称 非必填        
            String goods_display_url = ""; // 商品展示网址	非必填
            //32位的商户密钥     
            String mchnt_key = this.getConfigureProvider().format(FuyouPayVaribles.MCHNT_KEY);
            String rem = ""; // 备注 非必填        
            String ver = this.getConfigureProvider().format(FuyouPayVaribles.MCHNT_VER);
            ; // 版本号必填	1.0.1
            String order_valid_time = ""; // 超时时间	1m-15天，m：分钟、h：小时、d天、1c当天有效，	非必填
            
            String signDataStr = mchnt_cd + "|" + order_id + "|" + order_amt + "|" + order_pay_type + "|"
                + page_notify_url + "|" + back_notify_url + "|" + order_valid_time + "|" + iss_ins_cd + "|" + goods_name
                + "|" + goods_display_url + "|" + rem + "|" + ver + "|" + mchnt_key;
                
            /*  MD5摘要数据	md5		mchnt_cd+"|"  +order_id+"|"+order_amt+"|"+order_pay_type+"|"+page_notify_url+"|"+back_notify_url+"|"+order_valid_time+"|"+iss_ins_cd+"|"+goods_name+"|"+"+goods_display_url+"|"+rem+"|"+ver+"|"+mchnt_key 做md5摘要
            	其中mchnt_key 为32位的商户密钥，系统分配	必填	2908283de2814a64dc3b9b12e93915bc
            	详见后MD5摘要说明*/
            logger.info("网银充值签名前参数字符串：" + signDataStr);
            String md5 = FuYouMD5.MD5Encode(signDataStr);
            logger.info("网银充值MD5签名后：" + md5);
            
            params.put("page_notify_url", page_notify_url);
            params.put("back_notify_url", back_notify_url);
            params.put("order_pay_type", order_pay_type);
            params.put("order_valid_time", order_valid_time);
            params.put("iss_ins_cd", iss_ins_cd);
            params.put("goods_name", goods_name);
            params.put("goods_display_url", goods_display_url);
            params.put("rem", rem);
            params.put("ver", ver);
            params.put("md5", md5);
            
            response.setContentType("text/html");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(this.createSendForm(params, url));
        }
        else
        {
            //认证支付(快捷支付)
            T6141 t6141 = service.selectT6141(0, true);
            
            // 支付类型	0 理财版 1 普通版
            String user_type = "0";
            String card_no = bankNum; // 银行卡号	理财版用户必填 普通版不填
            String cert_type = "0"; // 证件类型     0 身份证 理财版用户必填 普通版不填 
            String cert_no = t6141.F07; // 身份证号 理财版用户必填 普通版不填	       
            String cardholder_name = t6141.F02; // 卡持有人姓名	理财版用户必填 普通版不填
            String user_id = String.valueOf(t6141.F01); // 用户ID商户网站产生的一个唯一的区分用户的ID	        必填        
            
            // 商户私钥     
            String private_key = this.getConfigureProvider().format(FuyouPayVaribles.PRIVATE_KEY);
            
            String signDataStr = mchnt_cd + "|" + user_id + "|" + order_id + "|" + order_amt + "|" + card_no + "|"
                + cardholder_name + "|" + cert_type + "|" + cert_no + "|" + back_notify_url + "|";
                
            byte[] bytesKey = (new BASE64Decoder()).decodeBuffer(private_key.trim());
            PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(bytesKey);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PrivateKey priKey = keyFactory.generatePrivate(pkcs8KeySpec);
            Signature signature = Signature.getInstance("MD5WithRSA");
            signature.initSign(priKey);
            signature.update(signDataStr.getBytes("GBK"));
            String RSA = (new BASE64Encoder()).encodeBuffer(signature.sign());
            
            logger.info("快捷支付充值RSA 签名前参数字符串：" + RSA);
            String md5 = FuYouMD5.MD5Encode(signDataStr);
            logger.info("快捷支付充值MD5签名后：" + md5);
            
            url = this.getConfigureProvider().format(FuyouPayVaribles.QUICK_CHARGE_URL);
            
            params.put("page_notify_url", back_notify_url);
            params.put("back_notify_url", "");
            params.put("user_type", user_type);
            params.put("card_no", card_no);
            params.put("cert_type", cert_type);
            params.put("cert_no", cert_no);
            params.put("cardholder_name", cardholder_name);
            params.put("user_id", user_id);
            params.put("RSA", RSA);
            response.setContentType("text/html");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(this.createSendForm(params, url));
        }
      
    }
    
}
