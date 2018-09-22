/*package com.dimeng.p2p.app.servlets.pay.shuangqian;

import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.PaymentInstitution;
import com.dimeng.p2p.app.servlets.AbstractSecureServlet;
import com.dimeng.p2p.order.ChargeOrderExecutor;
import com.dimeng.p2p.pay.service.shuangqian.ChargeManage;
import com.dimeng.p2p.pay.service.shuangqian.entity.ChargeOrder;
import com.dimeng.p2p.pay.service.shuangqian.util.MD5Util;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.p2p.variables.defines.pays.ShuangqianVaribles;
import com.dimeng.util.parser.BigDecimalParser;
import com.dimeng.util.parser.DateTimeParser;

*//**
 * 
 * 双乾网关支付
 * 
 * @author  zhoulantao
 * @version  [版本号, 2016年2月23日]
 *//*
public class ShuangqianCharge extends AbstractSecureServlet
{
    
    *//**
     * 注释内容
     *//*
    private static final long serialVersionUID = 1911455477214540529L;
    
    @Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        BigDecimal amount = BigDecimalParser.parse(request.getParameter("amount"));
        ChargeManage manage = serviceSession.getService(ChargeManage.class);
        ChargeOrder order = manage.addOrder(amount, PaymentInstitution.SHUANGQIAN.getInstitutionCode());
        String location = createShuangqianUrl(order);
        getResourceProvider().getResource(ChargeOrderExecutor.class).submit(order.id, null);
        PrintWriter writer = response.getWriter();
        writer.print(location);
        writer.flush();
        writer.close();
    }
    
    *//**
     * 创建双乾的请求URL
     * 
     * @param order
     * @return
     * @throws Throwable
     *//*
    protected String createShuangqianUrl(ChargeOrder order)
        throws Throwable
    {
        ConfigureProvider configureProvider = getConfigureProvider();
        String MerNo = configureProvider.format(ShuangqianVaribles.SHUANGQIANPAY_MERNO);
        String MD5key = configureProvider.format(ShuangqianVaribles.SHUANGQIANPAY_MD5KEY);
        String ReturnURL = configureProvider.format(ShuangqianVaribles.SHUANGQIANPAY_RETURN_URL);
        String NotifyURL = configureProvider.format(ShuangqianVaribles.SHUANGQIANPAY_NOTIFY_URL);
        double orderAmount = order.amount.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        String Amount = Double.toString(orderAmount);
        String BillNo = Integer.toString(order.id);
        String MerRemark = "charge " + orderAmount;
        String Products = configureProvider.format(SystemVariable.SITE_DOMAIN);
        MD5Util md5util = new MD5Util();
        String MD5info = md5util.signMap(new String[] {Amount, BillNo, MerNo, ReturnURL}, MD5key, "REQ");
        
        StringBuilder builder = new StringBuilder();
        builder.append("<form action=\"");
        builder.append(configureProvider.format(ShuangqianVaribles.SHUANGQIANPAY_URL));
        builder.append("\" method=\"post\">");
        builder.append("<input type=\"hidden\" name=\"MerNo\" value=\"");
        builder.append(MerNo);
        builder.append("\" />");
        builder.append("<input type=\"hidden\" name=\"BillNo\" value=\"");
        builder.append(order.id);
        builder.append("\" />");
        builder.append("<input type=\"hidden\" name=\"Amount\" value=\"");
        builder.append(orderAmount);
        builder.append("\" />");
        builder.append("<input type=\"hidden\" name=\"PayType\" value=\"");
        builder.append("KJPAY");
        builder.append("\" />");
        builder.append("<input type=\"hidden\" name=\"ReturnURL\" value=\"");
        builder.append(ReturnURL);
        builder.append("\" />");
        builder.append("<input type=\"hidden\" name=\"NotifyURL\" value=\"");
        builder.append(NotifyURL);
        builder.append("\" />");
        builder.append("<input type=\"hidden\" name=\"MD5info\" value=\"");
        builder.append(MD5info);
        builder.append("\" />");
        builder.append("<input type=\"hidden\" name=\"terminalKind\" value=\"");
        builder.append("WAP");
        builder.append("\" />");
        builder.append("<input type=\"hidden\" name=\"MerRemark\" value=\"");
        builder.append(MerRemark);
        builder.append("\" />");
        builder.append("<input type=\"hidden\" name=\"Products\" value=\"");
        builder.append(Products);
        builder.append("\" />");
        builder.append("</form>");
        builder.append("<script type=\"text/javascript\">");
        builder.append("document.forms[0].submit();");
        builder.append("</script>");
        logger.info(String.format("%s,平台充值请求双乾参数：%s", DateTimeParser.format(new Date()), builder.toString()));
        return builder.toString();
    }
}
*/