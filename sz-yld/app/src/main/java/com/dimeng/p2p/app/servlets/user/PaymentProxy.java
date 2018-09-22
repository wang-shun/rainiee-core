/*
 * 文 件 名:  PaymentProxy.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  zhoulantao
 * 修改时间:  2016年1月15日
 
package com.dimeng.p2p.app.servlets.user;

import java.io.IOException;
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
import com.dimeng.p2p.app.servlets.pay.service.shuangqian.ShuangqianPayment;
import com.dimeng.p2p.app.servlets.platinfo.ExceptionCode;
import com.dimeng.p2p.modules.bid.user.service.TenderRepaymentManage;
import com.dimeng.p2p.order.TenderRepaymentExecutor;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.util.parser.BooleanParser;
import com.dimeng.util.parser.IntegerParser;

*//**
 * 手工还款(托管)
 * 
 * @author  zhoulantao
 * @version  [版本号, 2016年1月15日]
 *//*
public class PaymentProxy extends AbstractSecureServlet
{
    *//**
     * 注释内容
     *//*
    private static final long serialVersionUID = -1670166280736810148L;
    
    @Override
    protected void processPost(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        // 标ID
        final int loanId = IntegerParser.parse(getParameter(request, "loanId"));
        
        // 当前期数
        final int currentTerm = IntegerParser.parse(getParameter(request, "currentTerm"));
        
        // 查询订单列表
        TenderRepaymentManage manage = serviceSession.getService(TenderRepaymentManage.class);
        final int[] orderIds = manage.repayment(loanId, currentTerm);
        
        request.setAttribute("loanId", loanId);
        request.setAttribute("number", currentTerm);
        request.setAttribute("orderIds", orderIds);
        
        // 提交订单
        TenderRepaymentExecutor executor = getResourceProvider().getResource(TenderRepaymentExecutor.class);
        if (orderIds != null && orderIds.length > 0)
        {
            for (int orderId : orderIds)
            {
                executor.submit(orderId, null);
            }
        }
        
        // 是否是第三方托管
        final ConfigureProvider configureProvider = getConfigureProvider();
        boolean tg = BooleanParser.parseObject(configureProvider.getProperty(SystemVariable.SFZJTG));
        
        if (tg)
        {
            try
            {
                // 判断托管模式
                final String prefix = configureProvider.getProperty(SystemVariable.ESCROW_PREFIX);
                
                // 易宝托管
                if (Prefix.YEEPAY.name().equals(prefix.toUpperCase(Locale.ENGLISH)))
                {
                    // 跳转到易宝的投标
                    // this.forwardController(request, response, RepaymentServlet.class);
                    return;
                }
                // 双乾托管
                else if (Prefix.SHUANGQIAN.name().equals(prefix.toUpperCase(Locale.ENGLISH)))
                {
                    this.forwardController(request, response, ShuangqianPayment.class);
                    return;
                }
                // 富友托管
                else if (Prefix.FUYOU.name().equals(prefix.toUpperCase(Locale.ENGLISH)))
                {
                    // TODO
                }
            }
            catch (Throwable e)
            {
                // 还款出现异常时，将还款计划更新为未还
                manage.updateT6252(loanId, currentTerm);
                throw e;
            }
        }
        else
        {
            setReturnMsg(request, response, ExceptionCode.UNKNOWN_ERROR, "只处理托管模式手工还款!");
            return;
        }
    }
    
    @SuppressWarnings("deprecation")
    @Override
    protected void onThrowable(final HttpServletRequest request, final HttpServletResponse response,
        final Throwable throwable)
        throws ServletException, IOException
    {
        String enRetUrl = Config.repaymentRetUrl;
        String url =
            enRetUrl + "?" + "code=000004&description="
                + URLEncoder.encode(new String(Base64.encode(throwable.getMessage().getBytes("UTF-8")), "UTF-8"));
        
        response.sendRedirect(url);
        getResourceProvider().log(throwable);
    }
}
*/