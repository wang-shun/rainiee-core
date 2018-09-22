package com.dimeng.p2p.app.servlets.bid;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bouncycastle.util.encoders.Base64;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.app.config.Config;
import com.dimeng.p2p.app.servlets.AbstractSecureServlet;
import com.dimeng.p2p.app.servlets.bid.domain.Prefix;
//import com.dimeng.p2p.app.servlets.pay.service.shuangqian.ShuangqianBid;
import com.dimeng.p2p.app.servlets.platinfo.ExceptionCode;
import com.dimeng.p2p.modules.bid.pay.service.TenderManage;
import com.dimeng.p2p.order.TenderOrderExecutor;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.util.parser.BigDecimalParser;
import com.dimeng.util.parser.BooleanParser;
import com.dimeng.util.parser.IntegerParser;

/**
 * 
 * 投标确认
 * 
 * @author  zhoulantao
 * @version  [版本号, 2016年1月9日]
 */
public class BuyBidProxy extends AbstractSecureServlet
{
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 1067152118252300347L;
    
    @Override
    protected void processGet(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        processPost(request, response, serviceSession);
    }
    
    @Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        final String bidId = request.getParameter("bidId");
        final BigDecimal amount = BigDecimalParser.parse(request.getParameter("amount"));
        final String myRewardType = request.getParameter("myRewardType");
        final String hbRule = request.getParameter("hbRule");
        final String jxqRule = request.getParameter("jxqRule");
        final String userReward = request.getParameter("userReward");
        final String source = request.getParameter("source");
        
        Map<String, String> prams = new HashMap<String, String>();
        prams.put("source", source);
        
        //是否是第三方托管
        final ConfigureProvider configureProvider = getConfigureProvider();
        final boolean tg = BooleanParser.parseObject(configureProvider.getProperty(SystemVariable.SFZJTG));
        
        // 标的ID
        int loanId = IntegerParser.parse(bidId);
        TenderManage tenderManage = serviceSession.getService(TenderManage.class);
        Map<String, String> rtnMap = tenderManage.bid(loanId, amount, userReward, myRewardType, null, hbRule, jxqRule, prams);
        TenderOrderExecutor executor = getResourceProvider().getResource(TenderOrderExecutor.class);
        final int orderId = IntegerParser.parse(rtnMap.get("orderId"));
        
        // 余额投标订单
        executor.submit(orderId, rtnMap);
        
        request.setAttribute("orderId", orderId);
        request.setAttribute("userReward", userReward);
        request.setAttribute("myRewardType", myRewardType);
        request.setAttribute("hbRule", hbRule);
        request.setAttribute("jxqRule", jxqRule);
        request.setAttribute("amount", amount);
        request.setAttribute("loanId", loanId);
        
        if (tg)
        {
            // 判断托管模式
            final String prefix = configureProvider.getProperty(SystemVariable.ESCROW_PREFIX);
            
            // 易宝托管
            if (Prefix.YEEPAY.name().equals(prefix.toUpperCase(Locale.ENGLISH)))
            {
                
            }
            // 双乾托管
            else if (Prefix.SHUANGQIAN.name().equals(prefix.toUpperCase(Locale.ENGLISH)))
            {
//                this.forwardController(request, response, ShuangqianBid.class);
                return;
            }
            // 富友托管
            else if (Prefix.FUYOU.name().equals(prefix.toUpperCase(Locale.ENGLISH)))
            {
                // TODO
            }
        }
        else
        {
            setReturnMsg(request, response, ExceptionCode.UNKNOWN_ERROR, "只处理托管模式投标!");
            return;
        }
    }
    
    @SuppressWarnings("deprecation")
    @Override
    protected void onThrowable(final HttpServletRequest request, final HttpServletResponse response,
        final Throwable throwable)
        throws ServletException, IOException
    {
        String enRetUrl = Config.buyBidRetUrl;
        String url =
            enRetUrl + "?" + "code=000004&description="
                + URLEncoder.encode(new String(Base64.encode(throwable.getMessage().getBytes("UTF-8")), "UTF-8"));
        
        response.sendRedirect(url);
        getResourceProvider().log(throwable);
    }
}
