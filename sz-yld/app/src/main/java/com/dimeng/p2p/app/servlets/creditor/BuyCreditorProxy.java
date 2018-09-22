/*
 * 文 件 名:  BuyCreditorProxy.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  zhoulantao
 * 修改时间:  2016年3月7日
 */
package com.dimeng.p2p.app.servlets.creditor;

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
import com.dimeng.p2p.app.servlets.platinfo.ExceptionCode;
import com.dimeng.p2p.modules.bid.user.service.TenderTransferManage;
import com.dimeng.p2p.order.TenderExchangeExecutor;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.util.parser.BooleanParser;
import com.dimeng.util.parser.IntegerParser;

/**
 * 第三方购买债券
 * 
 * @author  zhoulantao
 * @version  [版本号, 2016年3月7日]
 */
public class BuyCreditorProxy extends AbstractSecureServlet
{
    /**
     * 注释内容
     */
    private static final long serialVersionUID = -1204241234021233448L;
    
    @Override
    protected void processPost(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        // 获取债券ID
        final int creditorId = IntegerParser.parse(getParameter(request, "creditorId"));
        
        // 生成订单
        TenderTransferManage transferManage = serviceSession.getService(TenderTransferManage.class);
        int orderId = transferManage.purchase(creditorId);
        
        // 提交订单
        TenderExchangeExecutor executor = getResourceProvider().getResource(TenderExchangeExecutor.class);
        executor.submit(orderId, null);
        
        request.setAttribute("orderId", orderId);
        
        //是否是第三方托管
        final ConfigureProvider configureProvider = getConfigureProvider();
        final boolean tg = BooleanParser.parseObject(configureProvider.getProperty(SystemVariable.SFZJTG));
        
        if (tg)
        {
            // 判断托管模式
            final String prefix = configureProvider.getProperty(SystemVariable.ESCROW_PREFIX);
            
            // 易宝托管
            if (Prefix.YEEPAY.name().equals(prefix.toUpperCase(Locale.ENGLISH)))
            {
                
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
    
    @Override
    protected void processGet(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        processPost(request, response, serviceSession);
    }
    
    @SuppressWarnings("deprecation")
    @Override
    protected void onThrowable(final HttpServletRequest request, final HttpServletResponse response,
        final Throwable throwable)
        throws ServletException, IOException
    {
        String enRetUrl = Config.buyCreditorRetUrl;
        String url =
            enRetUrl + "?" + "code=000004&description="
                + URLEncoder.encode(new String(Base64.encode(throwable.getMessage().getBytes("UTF-8")), "UTF-8"));
        
        response.sendRedirect(url);
        getResourceProvider().log(throwable);
    }
    
}
