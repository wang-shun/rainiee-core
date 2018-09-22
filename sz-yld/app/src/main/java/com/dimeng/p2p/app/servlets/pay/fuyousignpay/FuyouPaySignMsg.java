package com.dimeng.p2p.app.servlets.pay.fuyousignpay;
/**
 * 富友协议支付签约短信触发
 */
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.S61.entities.T6110;
import com.dimeng.p2p.S61.enums.T6110_F06;
import com.dimeng.p2p.account.user.service.BankCardManage;
import com.dimeng.p2p.account.user.service.SafetyManage;
import com.dimeng.p2p.account.user.service.UserInfoManage;
import com.dimeng.p2p.account.user.service.entity.BankCard;
import com.dimeng.p2p.account.user.service.entity.BankCardQuery;
import com.dimeng.p2p.account.user.service.entity.Safety;
import com.dimeng.p2p.app.entity.NewProtocolBindXmlBeanReq;
import com.dimeng.p2p.app.entity.SignMsgResult;
import com.dimeng.p2p.app.service.FuYouPayManageService;
import com.dimeng.p2p.app.servlets.AbstractFuyoupayServlet;
import com.dimeng.p2p.app.servlets.AbstractSecureServlet;
import com.dimeng.p2p.app.servlets.platinfo.ExceptionCode;
import com.dimeng.p2p.common.CommonUtils;
import com.dimeng.p2p.common.enums.AttestationState;
import com.dimeng.p2p.common.enums.BankCardStatus;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.IntegerParser;
import com.fuiou.util.MD5;
import com.fuiou.mpay.encrypt.RSAUtils;

public class FuyouPaySignMsg extends AbstractSecureServlet 
{
    
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        ConfigureProvider configureProvider = getResourceProvider().getResource(ConfigureProvider.class);
        // 富友协议支付签约短信触发
        int accountId = serviceSession.getSession().getAccountId();
        UserInfoManage umanage = serviceSession.getService(UserInfoManage.class);
        T6110 t6110 = umanage.getUserInfo(accountId);
        if (t6110.F06 == T6110_F06.FZRR)
        {
            setReturnMsg(request, response, ExceptionCode.UNKNOWN_ERROR,  "自然人用户才能进行协议签约");
        }
        //用户编号
        String userid = String.valueOf(accountId);
        //真实姓名
    	String realName = getParameter(request, "realName");
        //银行代号
    	String bankCode = getParameter(request, "bankCode");
        //银行卡号
    	String bankCard = getParameter(request, "bankCard").replaceAll("\\s", "");
        if (StringHelper.isEmpty(bankCode) || StringHelper.isEmpty(bankCard))
        {
        	setReturnMsg(request, response, ExceptionCode.UNKNOWN_ERROR,  "银行卡信息不能为空");
            return;
        }   
        //身份证号码
    	String idCardNo = getParameter(request, "idCardNo");
        //证件类型
    	String idType ="0";
    	//手机号码
    	String mobile = getParameter(request, "mobile");
        //流水号 
      	String mchntSsn = "YLD"+new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + userid;          
        SafetyManage safetyManage = serviceSession.getService(SafetyManage.class);
        Safety safety = safetyManage.get();
        String checkMsg = checkIdCard(safetyManage, safety, idCardNo, realName);
/*       if (!StringHelper.isEmpty(checkMsg))
        {
            logger.info("协议支付签约短信触发失败：" + checkMsg);
            setReturnMsg(request, response, ExceptionCode.UNKNOWN_ERROR, "协议支付签约短信触发失败：" + checkMsg);
            processRequest(request, response, checkMsg);
            return;
        }*/
        checkMsg = checkPhone(serviceSession, mobile, safetyManage, safety);
        if (!StringHelper.isEmpty(checkMsg))
        {
            logger.info("协议支付签约短信触发失败：" + checkMsg);
        	setReturnMsg(request, response, ExceptionCode.UNKNOWN_ERROR, "协议支付签约短信触发失败：" + checkMsg);
            return;
        }
       if (StringHelper.isEmpty(mobile) && !StringHelper.isEmpty(safety.phoneNumber))
        {
            mobile = safety.phoneNumber;
        }
        if (safetyManage.isIdcard(idCardNo, T6110_F06.ZRR))
        {
           
            setReturnMsg(request, response, ExceptionCode.IDCARD_NAME_EXIST_ERRROR, "该身份证已认证过");
            return;
        }
       int number = Integer.parseInt(configureProvider.getProperty(SystemVariable.SM_RZCS));
        //该用户当天超过实名认证错误次数
        if (safetyManage.isMoreThanErrorCount(accountId, number))
        {
            setReturnMsg(request, response, ExceptionCode.SM_RZCS_EXCEPTION, "您今天实名认证太频繁，请明天再试");
            return;
        }
        BankCardManage bankCardManage = serviceSession.getService(BankCardManage.class);
        
        BankCard bcd = bankCardManage.getBankCar(bankCard);
        //添加别人的银行卡
        if (bcd != null && bcd.acount != accountId)
        {
        	setReturnMsg(request, response, ExceptionCode.BANK_CARD_EXIST, "当前银行卡号已存在");
            return;
        }
        // 添加用户信息
        safetyManage.updateNameSFZRR(realName, idCardNo, AttestationState.WYZ.name(), T6110_F06.ZRR);
        if (bcd == null)
        {
            addBankCard(bankCardManage, request, accountId);
        }
        if (!safetyManage.isPhone(mobile))
        {
            safetyManage.updatePhone(mobile);
        }
        //组装参数
		NewProtocolBindXmlBeanReq beanReq = new NewProtocolBindXmlBeanReq();
		String userId="YLD"+userid;
		beanReq.setUserId(userId);
		beanReq.setAccount(realName);
		beanReq.setCardNo(bankCard);
		beanReq.setIdCard(idCardNo);
		beanReq.setMobileNo(mobile);
		beanReq.setMchntSsn(mchntSsn);
		FuYouPayManageService manage = serviceSession.getService(FuYouPayManageService.class);
		SignMsgResult signMsgResult = manage.signMsg(beanReq,accountId);            
       if ("0000".equals(signMsgResult.getRESPONSECODE()))
        {
            logger.info("协议支付签约短信触发成功");
			setReturnMsg(request, response, ExceptionCode.SUCCESS, "协议支付签约短信触发成功");
        }
        else
        {
            logger.info("协议支付签约短信触发失败：" + signMsgResult.getRESPONSEMSG());
			setReturnMsg(request, response, ExceptionCode.UNKNOWN_ERROR, "协议支付签约短信触发失败：" +signMsgResult.getRESPONSEMSG());
        }
    }
    
    private void addBankCard(BankCardManage bankCardManage, final HttpServletRequest request, final int accountId)
            throws Throwable
        {
            
            BankCardQuery query = new BankCardQuery()
            {
                @Override
                public String getSubbranch()
                {
                    try {
    					return getParameter(request, "subbranch");
    				} catch (UnsupportedEncodingException e) 
                    {
    					e.printStackTrace();
    				}
    				return null;
                }
                
                @Override
                public String getStatus()
                {
                    return BankCardStatus.QY.name();
                }
                
                @Override
                public String getCity()
                {
    				try {
    					return getParameter(request, "xian");
    				} catch (UnsupportedEncodingException e) {
    					e.printStackTrace();
    				}
    				return null;
                }
                
                @Override
                public String getBankNumber()
                {
    				try {
    					return getParameter(request, "bankCard").replaceAll("\\s", "");
    				} catch (UnsupportedEncodingException e) {
    					e.printStackTrace();
    				}
    				return null;
                }
                
                @Override
                public int getBankId()
                {
    				try {
    					return IntegerParser.parse(getParameter(request, "bankCode"));
    				} catch (UnsupportedEncodingException e) {
    					e.printStackTrace();
    				}
    				return 0;
                }
                
                @Override
                public int getAcount()
                {
                    return accountId;
                }
                
                @Override
                public String getUserName()
                {
    				try {
    					return getParameter(request, "realName");
    				} catch (UnsupportedEncodingException e) {
    					e.printStackTrace();
    				}
    				return null;
                }
                
                @Override
                public int getType()
                {
                    // 开户名类型；1：个人，2：公司
                    return 1;
                }
            };
            
            bankCardManage.AddBankCar(query);
        }
        
        private String checkPhone(final ServiceSession serviceSession, String phone, SafetyManage safetyManage,
            Safety safety)
            throws Throwable
        {
            //如果传入的手机不为空，判断是否和绑定的手机是否一样
            /*if (!StringHelper.isEmpty(phone) && !StringHelper.isEmpty(safety.phoneNumber))
            {
                if (!phone.equals(safety.phoneNumber))
                {
                    StringBuilder sb = new StringBuilder();
                    sb.append("[{'num':03,'msg':'");
                    sb.append("输入手机号和绑定手机号不一致!" + "'}]");
                    return sb.toString();
                }
            }*/
            if (!StringHelper.isEmpty(phone) && StringHelper.isEmpty(safety.phoneNumber))
            {
                if (safetyManage.isPhone(phone))
                {
                    /*  StringBuilder sb = new StringBuilder();
                      sb.append("[{'num':0,'msg':'");
                      sb.append("手机号码已存在" + "'}]");*/
                    return "手机号码已存在";
                }
            }
            else if (StringHelper.isEmpty(phone) && !StringHelper.isEmpty(safety.phoneNumber))
            {
                phone = safety.phoneNumber;
            }
            
            if (StringHelper.isEmpty(phone))
            {
                /* StringBuilder sb = new StringBuilder();
                 sb.append("[{'num':0,'msg':'");
                 sb.append("手机号码错误" + "'}]");*/
                return "手机号码错误";
            }
            else
            {
                int userId = serviceSession.getSession().getAccountId();
                if (!safetyManage.isPhone(phone, userId))
                {
                    if (safetyManage.isPhone(phone))
                    {
                        /*StringBuilder sb = new StringBuilder();
                        sb.append("[{'num':0,'msg':'");
                        sb.append("手机号码已存在" + "'}]");*/
                        return "手机号码已存在";
                    }
                }
                //safetyManage.updatePhone(phpne);
                return "";
            }
        }
        
        private String checkIdCard(SafetyManage safetyManage, Safety safety, String idcard, String name)
            throws Throwable
        {
            if (StringHelper.isEmpty(name))
            {
                /* StringBuilder sb = new StringBuilder();
                 sb.append("[{'num':03,'msg':'");
                 sb.append("姓名不能为空!" + "'}]");*/
                return "姓名不能为空!";
            }
            String mtest = "^[\u4e00-\u9fa5]{2,}$";
            name = name.trim();
            if (!name.matches(mtest))
            {
                /* StringBuilder sb = new StringBuilder();
                 sb.append("[{'num':03,'msg':'");
                 sb.append("请输入合法的姓名!" + "'}]");*/
                return "请输入合法的姓名!";
            }
            if (StringHelper.isEmpty(idcard))
            {
                /*  StringBuilder sb = new StringBuilder();
                  sb.append("[{'num':00,'msg':'");
                  sb.append("身份证号码不能为空!" + "'}]");*/
                return "身份证号码不能为空!";
            }
            
            idcard = idcard.trim();
            if (!CommonUtils.isValidId(idcard))
            {
                /* StringBuilder sb = new StringBuilder();
                 sb.append("[{'num':03,'msg':'");
                 sb.append("无效的身份证号!" + "'}]");*/
                return "无效的身份证号!";
            }
            
            if (name.length() > 20)
            {
                /* StringBuilder sb = new StringBuilder();
                 sb.append("[{'num':03,'msg':'");
                 sb.append("姓名最多为20个字符!" + "'}]");*/
                return "姓名最多为20个字符!";
            }
            
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            int year = calendar.get(Calendar.YEAR);
            int born = Integer.parseInt(idcard.substring(6, 10));
            if ((year - born) < 18)
            {
                /*  StringBuilder sb = new StringBuilder();
                  sb.append("[{'num':03,'msg':'");
                  sb.append("必须年满18周岁!" + "'}]");*/
                return "必须年满18周岁!";
            }
            
            // 判断修改实名认证信息时，该身份证是否正在注册第三方账户
            if (safetyManage.isAuthingUpdate())
            {
                /*  StringBuilder sb = new StringBuilder();
                  sb.append("[{'num':03,'msg':'");
                  sb.append("正在第三方注册账户，不能修改实名认证信息!" + "'}]");*/
                return "正在第三方注册账户，不能修改实名认证信息!";
            }
            
            if (!StringHelper.isEmpty(safety.isIdCard))
            {
                /* StringBuilder sb = new StringBuilder();
                 sb.append("[{'num':03,'msg':'");
                 sb.append("您的账号已通过实名认证，请不要重复认证!" + "'}]");*/
                return "您的账号已通过实名认证，请不要重复认证!";
            }
            
            return "";
        }
    
    /**
	 * 获取签名
	 * @param signStr  签名串
	 * @param signtp   签名类型
	 * @param key      密钥
	 * @return
	 * @throws Exception
	 */
	public static String getSign(String signStr,String signtp,String key) throws  Exception{
		String sign = "";
		if ("md5".equalsIgnoreCase(signtp)) {
			sign = MD5.MD5Encode(signStr);
		} else {
			sign =	RSAUtils.sign(signStr.getBytes("utf-8"), key);
		}
		return sign;
	}

}
