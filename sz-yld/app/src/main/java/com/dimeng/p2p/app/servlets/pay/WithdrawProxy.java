/*
 * 文 件 名:  WithdrawProxy.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  zhoulantao
 * 修改时间:  2016年3月5日
 
package com.dimeng.p2p.app.servlets.pay;

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
import com.dimeng.p2p.app.servlets.pay.service.shuangqian.ThirdWithdraw;
import com.dimeng.p2p.app.servlets.platinfo.ExceptionCode;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.util.parser.BooleanParser;

*//**
 * 第三方提现
 * 
 * @author  zhoulantao
 * @version  [版本号, 2016年3月5日]
 *//*
public class WithdrawProxy extends AbstractSecureServlet
{
    *//**
     * 注释内容
     *//*
    private static final long serialVersionUID = 8057869449744076133L;
    
    @Override
    protected void processPost(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        // 判断托管模式
        final ConfigureProvider configureProvider = getConfigureProvider();
        final boolean tg = BooleanParser.parse(configureProvider.getProperty(SystemVariable.SFZJTG));
        
        // 提现价格
        final String amount = request.getParameter("amount");
        
        // 银行卡ID
        final String cardId = request.getParameter("cardId");
        
        request.setAttribute("amount", amount);
        request.setAttribute("cardId", cardId);
        
        if (tg)
        {
            // 判断托管模式
            final String prefix = configureProvider.getProperty(SystemVariable.ESCROW_PREFIX);
            
            // 易宝托管
            if (Prefix.YEEPAY.name().equals(prefix.toUpperCase(Locale.ENGLISH)))
            {
                //this.forwardController(request, response, UserRegisterServlet.class);
                return;
            }
            // 双乾托管
            else if (Prefix.SHUANGQIAN.name().equals(prefix.toUpperCase(Locale.ENGLISH)))
            {
                this.forwardController(request, response, ThirdWithdraw.class);
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
            setReturnMsg(request, response, ExceptionCode.UNKNOWN_ERROR, "只处理托管模式提现!");
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
    
    *//**
     * 将异常消息返回给页面
     *//*
    @SuppressWarnings("deprecation")
    @Override
    protected void onThrowable(HttpServletRequest request, HttpServletResponse response, Throwable throwable)
        throws ServletException, IOException
    {
        String enRetUrl = Config.withdrawRetUrl;
        String url =
            enRetUrl + "?" + "code=000004&description="
                + URLEncoder.encode(new String(Base64.encode(throwable.getMessage().getBytes("UTF-8")), "UTF-8"));
        
        response.sendRedirect(url);
        getResourceProvider().log(throwable);
    }
    
}
*/