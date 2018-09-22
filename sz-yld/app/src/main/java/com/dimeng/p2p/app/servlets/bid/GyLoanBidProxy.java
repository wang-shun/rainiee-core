/**
 * 文 件 名:  GyLoanBid.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  zhoulantao
 * 修改时间:  2015年11月25日
 */
package com.dimeng.p2p.app.servlets.bid;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bouncycastle.util.encoders.Base64;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.app.config.Config;
import com.dimeng.p2p.app.servlets.AbstractSecureServlet;
import com.dimeng.p2p.app.servlets.bid.domain.Prefix;
//import com.dimeng.p2p.app.servlets.pay.service.shuangqian.DonationBid;
import com.dimeng.p2p.app.servlets.platinfo.ExceptionCode;
import com.dimeng.p2p.modules.bid.pay.service.DonationService;
import com.dimeng.p2p.order.DonationExecutor;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.util.parser.BigDecimalParser;
import com.dimeng.util.parser.BooleanParser;
import com.dimeng.util.parser.IntegerParser;

/**
 * 公益标投资
 * 
 * @author  zhoulantao
 * @version  [版本号, 2015年11月25日]
 */
public class GyLoanBidProxy extends AbstractSecureServlet
{
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 8982822757890348239L;
    
    @Override
    protected void processGet(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        processPost(request, response, serviceSession);
    }
    
    @Override
    protected void processPost(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        // 获取页面参数
        final BigDecimal amount = BigDecimalParser.parse(getParameter(request, "amount"));
        final int loanId = IntegerParser.parse(getParameter(request, "loanId"));
        
        // 生成订单
        DonationService tenderManage = serviceSession.getService(DonationService.class);
        final int orderId = tenderManage.bid(loanId, amount, null);
        
        // 提交订单
        DonationExecutor executor = getResourceProvider().getResource(DonationExecutor.class);
        executor.submit(orderId, null);
        
        request.setAttribute("orderId", orderId);
        request.setAttribute("amount", amount);
        request.setAttribute("loanId", loanId);
        
        // 判断是否为托管
        final ConfigureProvider configureProvider = getConfigureProvider();
        final boolean tg = BooleanParser.parseObject(configureProvider.getProperty(SystemVariable.SFZJTG));
        
        if (tg)
        {
            // 判断托管模式
            final String prefix = configureProvider.getProperty(SystemVariable.ESCROW_PREFIX);
            
            // 易宝托管
            if (Prefix.YEEPAY.name().equals(prefix.toUpperCase(Locale.ENGLISH)))
            {
               // TODO
            }
            // 双乾托管
            else if (Prefix.SHUANGQIAN.name().equals(prefix.toUpperCase(Locale.ENGLISH)))
            {
//                this.forwardController(request, response, DonationBid.class);
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
            setReturnMsg(request, response, ExceptionCode.UNKNOWN_ERROR, "只处理托管模式公益捐赠!");
            return;
        }
    }
    
    @SuppressWarnings("deprecation")
    @Override
    protected void onThrowable(final HttpServletRequest request, final HttpServletResponse response,
        final Throwable throwable)
        throws ServletException, IOException
    {
        String enRetUrl = Config.buyGyBidRetUrl;
        String url =
            enRetUrl + "?" + "code=000004&description="
                + URLEncoder.encode(new String(Base64.encode(throwable.getMessage().getBytes("UTF-8")), "UTF-8"));
        
        response.sendRedirect(url);
        getResourceProvider().log(throwable);
    }
}
