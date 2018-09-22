/*package com.dimeng.p2p.app.servlets.pay.service.fuyou;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bouncycastle.util.encoders.Base64;

import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.PaymentInstitution;
import com.dimeng.p2p.S61.entities.T6110;
import com.dimeng.p2p.app.config.Config;
// import com.dimeng.p2p.app.servlets.pay.service.fuyou.service.ChargeManage;
import com.dimeng.p2p.app.servlets.platinfo.ExceptionCode;
import com.dimeng.p2p.common.enums.FrontLogType;
import com.dimeng.p2p.escrow.fuyou.cond.ChargeCond;
import com.dimeng.p2p.escrow.fuyou.service.ChargeManage;
import com.dimeng.p2p.escrow.fuyou.service.UserManage;
import com.dimeng.p2p.escrow.fuyou.variables.FuyouVariable;
import com.dimeng.p2p.variables.defines.pays.PayVariavle;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.BigDecimalParser;
import com.dimeng.util.parser.BooleanParser;

*//**
 * 
 * 向富友托管充值
 * <功能详细描述>
 * 
 * @author  suwei
 * @version  [版本号, 2016年03月02日]
 *//*
public class FyouCharge extends AbstractFuyouServlet
{
    
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        logger.info("APP向富友托管充值,开始——IP:" + request.getRemoteAddr());
        // 充值金额
        UserManage userManage = serviceSession.getService(UserManage.class);
        ChargeManage manage = serviceSession.getService(ChargeManage.class);
        // 获取平信个人信息记录
        T6110 userInfo = userManage.selectT6110();
        // 平台 —— 后端校验是否已经绑定手机、实名认证
        String mPhone = getConfigureProvider().getProperty(PayVariavle.CHARGE_MUST_PHONE);
        String mRealName = getConfigureProvider().getProperty(PayVariavle.CHARGE_MUST_NCIIC);
        // 是否托管项目
        String misTg = "true";//getConfigureProvider().getProperty(SystemVariable.SFZJTG);
        // 是否实名认证
        boolean smrz = userManage.isSmrz();
        
        boolean mAuth = true;
        if (BooleanParser.parse(mPhone) && userInfo.F04 == null)
        {
            mAuth = false;
        }
        if (BooleanParser.parse(mRealName) && !smrz)
        {
            mAuth = false;
        }
        if (!BooleanParser.parse(misTg))
        {
            mAuth = false;
        }
        if (!mAuth)
        {
            // 跳转到错误页面
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        String source = getTerminalType(request);
        int payCompanyCode = PaymentInstitution.FUYOU.getInstitutionCode();
        // 充值金额
        final BigDecimal amount = BigDecimalParser.parse(request.getParameter("amount"));
       
        String payType = getParameter(request,"payTpye");
        if(StringHelper.isEmpty(payType)){
        	payType = "500002";
        }
        //        String payTpye = request.getParameter("payTpye");
        // 充值手续费<单笔计>
        BigDecimal fee = BigDecimal.ZERO;
        if (amount.compareTo(BigDecimal.ZERO) <= 0 || payCompanyCode <= 0)
        {
            setReturnMsg(request, response, ExceptionCode.AMOUNT_ERROR_PAY, "金额或支付类型错误");
            return;
        }
        
        // 充值手续费<比例>
        if ("ON".equals(trimBlank(getConfigureProvider().format(FuyouVariable.FUYOU_CHAREFEE_ONOFF))))
        {
            fee = new BigDecimal(getConfigureProvider().format(PayVariavle.CHARGE_RATE));
            if (fee.compareTo(BigDecimal.ZERO) > 0)
            {
                fee = amount.multiply(fee);
                BigDecimal pfee = new BigDecimal(getConfigureProvider().format(PayVariavle.CHARGE_MAX_POUNDAGE));
                if (fee.compareTo(pfee) > 0)
                {
                    fee = pfee;
                }
            }
        }
        // 富友 —— 用户信息查询
        final ChargeManage fyManage = serviceSession.getService(ChargeManage.class);
        // 用于获取第三方托管账户
        final String userId = userManage.selectT6119();
        // 添加订单，与充值订单
        final String mchnt_txn_ssn = manage.addOrder(amount, source, fee,payType);
        String formUrl;
        if ("500001".equals(payType))
        {
            logger.info("快速充值,userId = "+userInfo.F01);
            //            formUrl = "https://jzh-test.fuiou.com/jzh/app/500001.action";
            formUrl = trimBlank(getConfigureProvider().format(FuyouVariable.FUYOU_APP_500001_URL));
        }
        else
        {
            logger.info("网银充值,userId = "+userInfo.F01);
            //            formUrl = "https://jzh-test.fuiou.com/jzh/app/500002.action";
            formUrl = trimBlank(getConfigureProvider().format(FuyouVariable.FUYOU_APP_500002_URL));
        }
        // 充值信息Map
        Map<String, String> map = fyManage.createChargeUrI(new ChargeCond()
        {
            
            @Override
            public String signature()
            {
                return null;
            }
            
            @Override
            public String pageNotifyUrl()
                throws Throwable
            {
                return getSiteDomain("/pay/service/fuyou/ret/chargeRet.htm");
            }
            
            @Override
            public String mchntTxnSsn()
            {
                return mchnt_txn_ssn;
            }
            
            @Override
            public String mchntCd()
            {
                try
                {
                    return trimBlank(getConfigureProvider().format(FuyouVariable.FUYOU_ACCOUNT_ID));
                }
                catch (IOException e)
                {
                    logger.info("获取商户代码出错");
                    e.printStackTrace();
                }
                return "";
            }
            
            @Override
            public String loginId()
            {
                return trimBlank(userId);
            }
            
            @Override
            public String backNotifyUrl()
                throws Throwable
            {
                return trimBlank(getConfigureProvider().format(FuyouVariable.FUYOU_CHARGENOTICE));
            }
            
            @Override
            public String amt()
            {
                return getAmt(amount);
            }
            
            @Override
            public String mchntAmt()
            {
                return "";
            }
        }, fee);
        
        // 向第三方发送请求
        sendHttp(map, formUrl, response, true);
        logger.info("充值信息发送第三方");
        manage.writeFrontLog(FrontLogType.CZ.getName(), "前台用户充值:" + amount);
    }
    
    *//**
     * 将异常消息返回给页面
     *//*
    @SuppressWarnings("deprecation")
    @Override
    protected void onThrowable(HttpServletRequest request, HttpServletResponse response, Throwable throwable)
        throws ServletException, IOException
    {
        String enRetUrl = Config.chargeRetUrl;
        String url =
            enRetUrl + "?" + "code=000004&description="
                + URLEncoder.encode(new String(Base64.encode(throwable.getMessage().getBytes("UTF-8")), "UTF-8"));
        
        response.sendRedirect(url);
        getResourceProvider().log(throwable);
    }
}
*/