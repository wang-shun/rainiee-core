/*package com.dimeng.p2p.app.servlets.pay.shuangqian;

import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bouncycastle.util.encoders.Base64;

import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.app.servlets.AbstractAppServlet;
import com.dimeng.p2p.order.ChargeOrderExecutor;
import com.dimeng.p2p.pay.service.shuangqian.util.MD5Util;
import com.dimeng.p2p.variables.defines.pays.ShuangqianVaribles;
import com.dimeng.util.parser.DateTimeParser;
import com.dimeng.util.parser.IntegerParser;

*//**
 * 
 * 双乾网关页面回调
 * 
 * @author  zhoulantao
 * @version  [版本号, 2016年3月2日]
 *//*
public class ShuangqianNotify extends AbstractAppServlet
{
    
    *//**
     * 注释内容
     *//*
    private static final long serialVersionUID = -4330366169734237099L;
    
    @SuppressWarnings("deprecation")
    @Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        String MD5key = getConfigureProvider().format(ShuangqianVaribles.SHUANGQIANPAY_MD5KEY);
        try
        {
            response.setContentType("text/html; charset=utf-8");
            printLog(request);
            String MerNo = request.getParameter("MerNo");
            String BillNo = request.getParameter("BillNo");
            String Amount = request.getParameter("Amount");
            String Succeed = request.getParameter("Succeed");
            String Result = request.getParameter("Result");
            String MD5info = request.getParameter("MD5info");
            String retUrl = request.getParameter("retUrl");
            String url = retUrl + "?";
            MD5Util md5util = new MD5Util();
            String md5str = md5util.signMap(new String[] {Amount, BillNo, MerNo, Succeed}, MD5key, "RES");
            if (MD5info.equals(md5str))
            {
                if (Succeed.equals("88"))
                {
                    Map<String, String> params = new HashMap<>();
                    params.put("paymentOrderId", BillNo);
                    getResourceProvider().getResource(ChargeOrderExecutor.class).confirm(IntegerParser.parse(BillNo),
                        params);
                    url += "code=000000&description=success";
                }
                else
                {
                    url +=
                        "code=000004&description="
                            + URLEncoder.encode(new String(Base64.encode((Result + "订单未支付成功，请重新再试").getBytes("UTF-8")),
                                "UTF-8"));
                }
            }
            else
            {
                url +=
                    "code=000004&description="
                        + URLEncoder.encode(new String(Base64.encode(("电子签名错误").getBytes("UTF-8")), "UTF-8"));
            }
            sendRedirect(request, response, url);
        }
        catch (Exception e)
        {
            getResourceProvider().log(e);
        }
    }
    
    private void printLog(HttpServletRequest request)
    {
        StringBuilder printLog = new StringBuilder();
        printLog.append("&MerNo=").append(request.getParameter("MerNo"));
        printLog.append("&BillNo=").append(request.getParameter("BillNo"));
        printLog.append("&Amount=").append(request.getParameter("Amount"));
        printLog.append("&Succeed=").append(request.getParameter("Succeed"));
        printLog.append("&Result=").append(request.getParameter("Result"));
        printLog.append("&MD5info=").append(request.getParameter("MD5info"));
        logger.info(String.format("%s,平台充值双乾返回参数：%s", DateTimeParser.format(new Date()), printLog.toString()));
    }
    
}
*/