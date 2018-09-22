package com.dimeng.p2p.user.servlets.capital;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
import com.dimeng.p2p.pay.service.fuyou.util.Bean2XmlUtils;
import com.dimeng.p2p.service.ActivityCommon;
import com.dimeng.p2p.signature.fdd.service.FddSignatureServiceV25;
import com.dimeng.p2p.user.entity.NewProtocolBindXmlBeanReq;
import com.dimeng.p2p.user.entity.SignResult;
import com.dimeng.p2p.user.entity.TerminationResult;
import com.dimeng.p2p.user.service.FuYouPayManageService;
import com.dimeng.p2p.user.utils.HttpPoster;
import com.dimeng.p2p.user.utils.XMapUtil;
import com.dimeng.p2p.variables.defines.RealNameAuthVarivale;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.p2p.variables.defines.URLVariable;
import com.dimeng.util.StringHelper;
import com.fuiou.mpay.encrypt.DESCoderFUIOU;
import com.fuiou.mpay.encrypt.RSAUtils;
import com.fuiou.util.MD5;


/**
 * 
 * <一句话功能简述> 
 * <功能详细描述> 协议支付解绑
 * 
 * @version [版本号, 2018年9月12日]
 */
public class FuyouTermination extends AbstractFuyoupayServlet
{
    
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {  
    	ConfigureProvider configureProvider = getResourceProvider().getResource(ConfigureProvider.class);
    	String version = "1.0";
    	int accountId = serviceSession.getSession().getAccountId();
    	 //商户号
        String mchntcd =configureProvider.getProperty(SystemVariable.MCHNTCD);
		String userid =String.valueOf(accountId);
		NewProtocolBindXmlBeanReq beanReq = new NewProtocolBindXmlBeanReq();
		String userId="YLD"+userid;
		beanReq.setVersion(version);
		beanReq.setMchntCd(mchntcd);
		beanReq.setUserId(userId);
        // 富友协议支付解约      
        FuYouPayManageService manage = serviceSession.getService(FuYouPayManageService.class);
        TerminationResult terminationResult = manage.termination(beanReq,accountId);     
        if ("0000".equals(terminationResult.getRESPONSECODE()))
        {
            logger.info("协议支付解约成功");
            processRequest(request, response, "SUCCESS");
        }
        else
        {
            logger.info("协议支付解约失败：" + terminationResult.getRESPONSEMSG());
            processRequest(request, response, terminationResult.getRESPONSEMSG());
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
