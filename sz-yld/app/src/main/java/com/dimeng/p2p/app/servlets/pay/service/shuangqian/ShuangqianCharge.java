/*package com.dimeng.p2p.app.servlets.pay.service.shuangqian;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bouncycastle.util.encoders.Base64;

import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.PaymentInstitution;
import com.dimeng.p2p.S61.entities.T6119;
import com.dimeng.p2p.app.config.Config;
import com.dimeng.p2p.app.servlets.AbstractSecureServlet;
import com.dimeng.p2p.escrow.shuangqian.entity.ChargeOrder;
import com.dimeng.p2p.escrow.shuangqian.service.ChargeManage;
import com.dimeng.p2p.escrow.shuangqian.service.UserManage;
import com.dimeng.p2p.order.ChargeOrderExecutor;
import com.dimeng.util.parser.BigDecimalParser;

*//**
 * 
 * 双乾充值
 * 
 * @author  zhoulantao
 * @version  [版本号, 2016年1月11日]
 *//*
public class ShuangqianCharge extends AbstractSecureServlet
{
    *//**
     * 注释内容
     *//*
    private static final long serialVersionUID = 2100937055819243362L;
    
    @Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        // 设置返回消息类型
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        
        // 获取用户授权情况
        final UserManage userManage = serviceSession.getService(UserManage.class);
        final T6119 t6119 = userManage.selectT6119();
        
        // 提交订单
        final ChargeOrderExecutor executor = getResourceProvider().getResource(ChargeOrderExecutor.class);
        final int payCompanyCode = PaymentInstitution.SHUANGQIAN.getInstitutionCode();
        final BigDecimal amount = BigDecimalParser.parse((String)request.getAttribute("amount"));
        final ChargeManage manage = serviceSession.getService(ChargeManage.class);
        final ChargeOrder order = manage.addOrder(amount, payCompanyCode);
        executor.submit(order.id, null);
        
        // 跳转到第三方页面
        final String returnUrl =
            getSiteDomain("/pay/service/shuangqian/ret/chargeRet.htm" + "?retUrl=" + Config.chargeRetUrl);
        final String location = manage.createChargeForm2("2", t6119, order, returnUrl);
        PrintWriter writer = response.getWriter();
        writer.print(location);
        writer.flush();
        writer.close();
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
}*/