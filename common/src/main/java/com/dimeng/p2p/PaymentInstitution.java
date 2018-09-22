package com.dimeng.p2p;

import com.dimeng.util.StringHelper;

import java.util.ArrayList;
import java.util.List;

public enum PaymentInstitution {

	ALLINPAY(100, "通联支付", "allinpay/allinpayCharge.htm", "allinpay/allinpayCheck.htm"), 
	SHUANGQIAN(200, "双乾支付", "shuangqian/shuangqianCharge.htm", "shuangqian/shuangqianCheck.htm"), 
	CHINAPNR(300, "汇付天下", "", ""), 
	TENPAY(400, "财付通", "", ""), 
	HUICHAO(500, "汇潮托管", "", ""), 
	FUYOU(600, "富友托管", "", ""),
	BAOFU(700, "宝付支付", "baofu/baoFuChargeServlet.htm", "baofu/baoFuCheckServlet.htm"), 
	HUICHAOGATE(800, "汇潮网关支付","huichao/huiChaoGateChargeServlet.htm", "huichao/huiChaoGateCheckServlet.htm"),
	LIANLIANGATE(900, "连连支付", "lianlianpay/lianLianPayChagerServlet.htm", "lianlianpay/lianLianCheckServlet.htm"), 
	DINPAY(1000,"智付支付", "dinpay/dinpayCharge.htm", "dinpay/dinpayCheck.htm"), 
	CHINABANK(1100, "网银在线支付", "chinabank/chinaBankCharge.htm", ""), 
	KJTPAY(1200, "快捷通支付", "haier/kjtPayChargeServlet.htm", "haier/kjtCheckServlet.htm"), 
	HEEPAY(1300, "汇付宝支付", "heepay/heepayCharge.htm", "heepay/heepayCheck.htm"), 
	SINAPAY(1400, "新浪网关支付", "sinapay/sinaPayCharge.htm", "sinapay/sinaPayCheck.htm"),
	GFBPAY(1500,"国付宝支付", "gfbpay/gFBCharge.htm", "gfbpay/gFBCheck.htm"),
	BOCOM(1600,"交通银行支付", "bocompay/bOCOMCharge.htm", ""),
	BAOFUTG(1700,"宝付托管","",""),
	LIANLIANAUTHGATE(1800, "连连认证支付", "lianlianpay/lianLianAuthChagerServlet.htm", "lianlianpay/lianLianCheckServlet.htm"),
	YEEPAYTG(1900, "易宝支付托管", "", ""),
	HUANXUN(2000, "环迅支付托管", "huanxun/userCharge.htm", "huanxun/userChargeCheck.htm"),
	UMPAY(1910,"联动优势托管","",""),
	YINLIANPAY(2100,"银联支付","yinlianpay/yinlianpayCharge.htm","yinlianpay/yinlianCheck.htm"),
	YIFUPAY(2300,"易宝支付","yibao/yiBaoCharge.htm","yibao/yiBaoCheck.htm"),
	ZHONGXIN(3000,"中信银行托管","",""),
	CIB(2900,"兴业银行支付代付","cib/cIBCharge.htm",""),
	CIBQUICK(2901,"兴业银行快捷支付","cib/cIBQuickCharge.htm",""),
	KUAIQIAN(3100,"快钱快捷支付","kuaiqian/kQuickCharge.htm","kuaiqian/kQuickChargeCheck.htm"),
	ALLINWAPPAY(3200,"通联手机WAP支付","allinpay/allinpayChargeServlet.htm",""),
	SINA(3300,"新浪存管","",""),
	BAOFURZZF(3400,"宝付认证支付","","baofu/chargeCheckServlet.htm"),
    ZHONGJIN(3500,"中金支付","",""),
    ZHESHANG(3600,"浙商银行存管","",""),
    FUYOUPAY(2600, "富友支付", "fuyou/fuyouChargeServlet.htm", "fuyou/fuyouCheckServlet.htm"),
    UCCB(3700,"乌鲁木齐银行存管","uccb/uCCBCharge.htm", "uccb/uCCBChargeCheck.htm");
	
	private int institutionCode;
	private String chineseName;
	private String visiteUri; // 访问地址
	private String checkUri; // 查询地址

	private PaymentInstitution(int institutionCode, String chineseName,
			String visiteUri, String notifyUri) {
		this.institutionCode = institutionCode;
		this.chineseName = chineseName;
		this.visiteUri = visiteUri;
		this.checkUri = notifyUri;
	}

	public int getInstitutionCode() {
		return institutionCode;
	}

	public String getChineseName() {
		return chineseName;
	}

	public String getVisiteUri() {
		return visiteUri;
	}
	
	public String getCheckUri()
    {
        return checkUri;
    }

    public static final PaymentInstitution parse(String value) {
		if (StringHelper.isEmpty(value)) {
			return null;
		}
		try {
			return valueOf(value);
		} catch (Throwable t) {
			return null;
		}
	}
    
    public static final String getDescription(int code){
    	for (PaymentInstitution paymentInstitution : PaymentInstitution.values()) {
    		if(paymentInstitution.getInstitutionCode() == code){
    			return paymentInstitution.getChineseName();
    		}
		}
    	return null;
    }

	public static final List<String> getCodes(String name)
	{
		List<String> list= new ArrayList<String>();
		for (PaymentInstitution paymentInstitution : PaymentInstitution.values()) {
			if(paymentInstitution.getChineseName().contains(name)){
				list.add(paymentInstitution.getInstitutionCode() + "");
			}
		}
		return list;
	}
}
