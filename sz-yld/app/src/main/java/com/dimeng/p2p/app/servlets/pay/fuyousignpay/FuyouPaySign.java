package com.dimeng.p2p.app.servlets.pay.fuyousignpay;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.p2p.S63.enums.T6340_F03;
import com.dimeng.p2p.S63.enums.T6340_F04;
import com.dimeng.p2p.account.user.service.SafetyManage;
import com.dimeng.p2p.app.entity.NewProtocolBindXmlBeanReq;
import com.dimeng.p2p.app.entity.SignResult;
import com.dimeng.p2p.app.service.FuYouPayManageService;
import com.dimeng.p2p.app.servlets.AbstractFuyoupayServlet;
import com.dimeng.p2p.app.servlets.AbstractSecureServlet;
import com.dimeng.p2p.app.servlets.platinfo.ExceptionCode;
import com.dimeng.p2p.service.ActivityCommon;
import com.dimeng.p2p.signature.fdd.service.FddSignatureServiceV25;
import com.dimeng.p2p.variables.defines.RealNameAuthVarivale;
import com.dimeng.p2p.variables.defines.URLVariable;
import com.fuiou.mpay.encrypt.RSAUtils;
import com.fuiou.util.MD5;


/**
 * 
 * <一句话功能简述> 
 * <功能详细描述> 协议支付签约
 * 
 * @version [版本号, 2018年6月28日]
 */
public class FuyouPaySign extends AbstractSecureServlet 
{
    
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        ConfigureProvider configureProvider = getResourceProvider().getResource(ConfigureProvider.class);
        
        SafetyManage safetyManage = serviceSession.getService(SafetyManage.class);
        // 通联协议支付签约
        int accountId = serviceSession.getSession().getAccountId();      
        //用户编号
        String userid = String.valueOf(accountId);
        //手机验证码
        String msg = getParameter(request, "verifyCode");
        //请求参数赋值
		NewProtocolBindXmlBeanReq beanReq = new NewProtocolBindXmlBeanReq();
		String userId1="YLD"+userid;
		beanReq.setUserId(userId1);
		beanReq.setMsgCode(msg);
		FuYouPayManageService manage = serviceSession.getService(FuYouPayManageService.class);
		SignResult signResult = manage.sign(beanReq,accountId);
        if (null != signResult && signResult.getRESPONSECODE().equals("0000"))
        {
			// 法大大电子签章- CA证书申请
			FddSignatureServiceV25 fdd = serviceSession.getService(FddSignatureServiceV25.class);
			fdd.syncPersonAuto(accountId);
            ActivityCommon activityCommon = serviceSession.getService(ActivityCommon.class);
            int userId = serviceSession.getSession().getAccountId();
            activityCommon.sendActivity(userId, T6340_F03.redpacket.name(), T6340_F04.birthday.name());
            activityCommon.sendActivity(userId, T6340_F03.interest.name(), T6340_F04.birthday.name());
            activityCommon.sendActivity();
            
            boolean is_realNameAuth =
                Boolean.parseBoolean(configureProvider.getProperty(RealNameAuthVarivale.IS_REALNAMEAUTH));
            if (is_realNameAuth)
            {
                //实名认证通过时间
                safetyManage.updateT6198F06(serviceSession.getSession().getAccountId());
            }
            logger.info("协议支付签约成功");
       		setReturnMsg(request, response, ExceptionCode.SUCCESS, "协议支付签约成功");
            return;
        }
        else
        {
            logger.info("协议支付签约失败：" + signResult.getRESPONSEMSG());     
  			setReturnMsg(request, response, ExceptionCode.UNKNOWN_ERROR, "协议支付签约失败" + signResult.getRESPONSEMSG());
  			return;
        }
        
    }
    
    @Override
    protected void onThrowable(HttpServletRequest request, HttpServletResponse response, Throwable throwable)
        throws ServletException, IOException
    {
        logger.error("", throwable);
        String retUrl = request.getHeader("Referer");
        if (throwable instanceof ParameterException || throwable instanceof LogicalException)
        {
            getController().prompt(request, response, PromptLevel.WARRING, throwable.getMessage());
        }
        else
        {
            getController().prompt(request, response, PromptLevel.ERROR, "系统繁忙，请您稍后再试！");
        }
        sendRedirect(request, response, retUrl);
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
