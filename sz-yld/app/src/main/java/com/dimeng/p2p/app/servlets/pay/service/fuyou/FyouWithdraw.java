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
import com.dimeng.p2p.S61.enums.T6101_F03;
import com.dimeng.p2p.app.config.Config;
//import com.dimeng.p2p.app.servlets.pay.service.fuyou.service.WithDrawManage;
import com.dimeng.p2p.app.servlets.platinfo.ExceptionCode;
import com.dimeng.p2p.common.enums.FrontLogType;
import com.dimeng.p2p.escrow.fuyou.cond.ChargeCond;
import com.dimeng.p2p.escrow.fuyou.service.WithDrawManage;
import com.dimeng.p2p.escrow.fuyou.variables.FuyouVariable;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.BigDecimalParser;
import com.dimeng.util.parser.IntegerParser;

*//**
 * 
 * 用户提现
 * <富友托管>
 * 
 * @author  suwei
 * @version  [版本号, 2016年03月02日]
 *//*
public class FyouWithdraw extends AbstractFuyouServlet
{
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        logger.info("APP用户提现-开始——IP:" + request.getRemoteAddr());
        // 银行卡ID
        int cardId = IntegerParser.parse(request.getParameter("cardId"));
        // 提现的金额
        final String amtStr = request.getParameter("amount");
        //提现方式（T+0; T+1）
        final BigDecimal txfs = BigDecimalParser.parse(request.getParameter("txfs"));
        if (StringHelper.isEmpty(amtStr) || cardId <= 0)
        {
            setReturnMsg(request, response, ExceptionCode.AMOUNT_ERROR, "金额输入错误！");
            return;
        }
        // 提现金额
        BigDecimal amount = new BigDecimal(amtStr);
        WithDrawManage withdrawService = serviceSession.getService(WithDrawManage.class);
        // 查询用户的第三方托管账号
        final String login_id = withdrawService.selectT6119();
        if (login_id == null || StringHelper.isEmpty(login_id))
        {
            setReturnMsg(request, response, ExceptionCode.NO_REGISTER_OTHER_PAY, "用户未注册第三方支付！");
            return;
        }
        final String mchnt_txn_ssn = withdrawService.addOrderId(amount, cardId, T6101_F03.WLZH);
        // 构建提现信息
        Map<String, String> map = withdrawService.createWithdrawUrI(amount, new ChargeCond()
        {
            @Override
            public String mchntCd()
            {
                try
                {
                    return trimBlank(getConfigureProvider().format(FuyouVariable.FUYOU_ACCOUNT_ID));
                }
                catch (IOException e)
                {
                    logger.error(e, e);
                }
                return null;
            }
            
            @Override
            public String mchntTxnSsn()
            {
                return mchnt_txn_ssn;
            }
            
            @Override
            public String loginId()
            {
                return login_id;
            }
            
            @Override
            public String signature()
            {
                return null;
            }
            
            @Override
            public String amt()
            {
                return null;
            }
            
            @Override
            public String pageNotifyUrl()
                throws Throwable
            {
                try
                {
                    return getSiteDomain("/pay/service/fuyou/ret/withdrawRet.htm");
                }
                catch (Exception e)
                {
                    logger.info("查询商户代码异常");
                    throw e;
                }
            }
            
            @Override
            public String backNotifyUrl()
                throws Throwable
            {
                try
                {
                    return getConfigureProvider().format(FuyouVariable.FUYOU_WITHDRANOTICE_URL);
                }
                catch (Exception e)
                {
                    logger.info("查询商户代码异常");
                    throw e;
                }
            }
            
            @Override
            public String mchntAmt()
            {
                return null;
            }
        });
        
        // 提现提交地址
        String formUrl = getConfigureProvider().format(FuyouVariable.FUYOU_APP_500003_URL);
        // 向第三方发送请求
        sendHttp(map, formUrl, response, true);
        withdrawService.writeFrontLog(FrontLogType.TX.getName(), "APP端提现:" + amount);
    }
    
    *//**
     * 将异常消息返回给页面
     *//*
    @SuppressWarnings("deprecation")
    @Override
    protected void onThrowable(HttpServletRequest request, HttpServletResponse response, Throwable throwable)
        throws ServletException, IOException
    {
        String enRetUrl = Config.withdrawRetUrl;
        String url = enRetUrl + "?" + "code=000004&description="
            + URLEncoder.encode(new String(Base64.encode(throwable.getMessage().getBytes("UTF-8")), "UTF-8"));
        
        response.sendRedirect(url);
        getResourceProvider().log(throwable);
    }
}
*/