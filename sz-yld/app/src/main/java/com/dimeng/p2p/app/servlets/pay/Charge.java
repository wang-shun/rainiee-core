package com.dimeng.p2p.app.servlets.pay;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.PaymentInstitution;
import com.dimeng.p2p.S61.entities.T6110;
import com.dimeng.p2p.app.servlets.AbstractSecureServlet;
import com.dimeng.p2p.app.servlets.platinfo.ExceptionCode;
import com.dimeng.p2p.modules.bid.pay.service.UserInfoManage;
import com.dimeng.p2p.variables.defines.pays.PayVariavle;
import com.dimeng.util.parser.BigDecimalParser;
import com.dimeng.util.parser.BooleanParser;

/**
 * 
 * 充值
 * 
 * @author  zhoulantao
 * @version  [版本号, 2015年11月12日]
 */
public class Charge extends AbstractSecureServlet
{
    
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processPost(final HttpServletRequest request, HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        logger.info("|Charge-in|");
        ConfigureProvider configureProvider = getConfigureProvider();
        /**
         * 后端校验是否已经绑定手机、实名认证、交易密码
         */
        UserInfoManage manage = serviceSession.getService(UserInfoManage.class);
        T6110 userInfo = manage.getUserInfo(serviceSession.getSession().getAccountId());
        
        String mEmail = configureProvider.getProperty(PayVariavle.CHARGE_MUST_EMAIL);
        String mPhone = configureProvider.getProperty(PayVariavle.CHARGE_MUST_PHONE);
        String mRealName = configureProvider.getProperty(PayVariavle.CHARGE_MUST_NCIIC);
        String mWithPsd = configureProvider.getProperty(PayVariavle.CHARGE_MUST_WITHDRAWPSD);
        
        // 判断是否实名认证
        boolean smrz = manage.isSmrz();
        
        // 是否设置交易密码
        boolean txmm = manage.isTxmm();
        
        if (BooleanParser.parse(mEmail) && userInfo.F05 == null)
        {
            // 未进行邮箱认证
            setReturnMsg(request, response, ExceptionCode.EMAIL_ERRROR, "未进行邮箱认证！");
            return;
        }
        
        if (BooleanParser.parse(mPhone) && userInfo.F04 == null)
        {
            // 未进行手机号码认证
            setReturnMsg(request, response, ExceptionCode.PHONE_NUM_ERRROR, "未进行手机号码认证！");
            return;
        }
        
        if (BooleanParser.parse(mRealName) && !smrz)
        {
            // 未进行实名认证
            setReturnMsg(request, response, ExceptionCode.NO_IDCARD_NAME, "未进行实名认证！");
            return;
        }
        
        if (BooleanParser.parse(mWithPsd) && !txmm)
        {
            // 未设置交易密码
            setReturnMsg(request, response, ExceptionCode.TRANPASSWORD_ERROR, "未设置交易密码！");
            return;
        }
        
        PaymentInstitution institution = PaymentInstitution.parse(getParameter(request, "paymentInstitution"));
        
        BigDecimal amount = BigDecimalParser.parse(getParameter(request, "amount"));
        if (amount.doubleValue() <= 0)
        {
            // 封装返回信息
            setReturnMsg(request, response, ExceptionCode.AMOUNT_ERROR_PAY, "充值金额输入错误！");
            return;
        }
        
        String location =
            getSiteDomain("/pay/service/fuyou/fyouCharge.htm?paymentInstitution=" + institution + "&amount=" + amount);
        
        Map<String, String> map = new HashMap<String, String>();
        map.put("url", location);
        
        // 封装返回信息
        setReturnMsg(request, response, ExceptionCode.SUCCESS, "success!", map);
        return;
    }
}
