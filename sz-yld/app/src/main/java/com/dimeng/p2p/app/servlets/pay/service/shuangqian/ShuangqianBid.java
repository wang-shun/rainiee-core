/**
 * 文 件 名:  ShuangqianBid.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  zhoulantao
 * 修改时间:  2016年3月5日
 *//*
package com.dimeng.p2p.app.servlets.pay.service.shuangqian;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bouncycastle.util.encoders.Base64;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.app.config.Config;
import com.dimeng.p2p.app.servlets.AbstractSecureServlet;
import com.dimeng.p2p.escrow.shuangqian.entity.TransferEntity;
import com.dimeng.p2p.escrow.shuangqian.service.BidManage;
import com.dimeng.p2p.escrow.shuangqian.variables.ShuangQianVariable;
import com.dimeng.util.parser.BigDecimalParser;
import com.dimeng.util.parser.IntegerParser;

*//**
 * 双乾托管投标操作
 * 
 * @author  zhoulantao
 * @version  [版本号, 2016年3月5日]
 *//*
public class ShuangqianBid extends AbstractSecureServlet
{
    *//**
    * 注释内容
    *//*
    private static final long serialVersionUID = 2242347712884034898L;
    
    @Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        
        final ConfigureProvider configureProvider = getConfigureProvider();
        
        // 标的ID
        int loanId = IntegerParser.parse(request.getAttribute("loanId"));
        BigDecimal amount = BigDecimalParser.parse(String.valueOf(request.getAttribute("amount")));
        String myRewardType = (String)request.getAttribute("myRewardType");
        String hbRule = (String)request.getAttribute("hbRule");
        String jxqRule = (String)request.getAttribute("jxqRule");
        String userReward = (String)request.getAttribute("userReward");
        int orderId = IntegerParser.parse(request.getAttribute("orderId"));
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("orderId", orderId + "");
        paramMap.put("userReward", userReward);
        paramMap.put("myRewardType", myRewardType);
        paramMap.put("hbRule", hbRule);
        paramMap.put("jxqRule", jxqRule);
        
        BidManage bidManage = serviceSession.getService(BidManage.class);
        
        String returnUrl = getSiteDomain("/pay/service/shuangqian/ret/bidRet.htm" + "?retUrl=" + Config.buyBidRetUrl);
        String notifyURL = configureProvider.format(ShuangQianVariable.SQ_BIDNOTIFY);
        TransferEntity entity = bidManage.getBidEntity(paramMap, loanId, amount, returnUrl, notifyURL);
        String location = bidManage.createBidForm(entity);
        
        PrintWriter writer = response.getWriter();
        writer.print(location);
        writer.flush();
        writer.close();
        return;
    }
    
    *//**
     * 将异常消息返回给页面
     *//*
    @SuppressWarnings("deprecation")
    @Override
    protected void onThrowable(HttpServletRequest request, HttpServletResponse response, Throwable throwable)
        throws ServletException, IOException
    {
        String enRetUrl = Config.buyBidRetUrl;
        String url =
            enRetUrl + "?" + "code=000004&description="
                + URLEncoder.encode(new String(Base64.encode(throwable.getMessage().getBytes("UTF-8")), "UTF-8"));
        
        response.sendRedirect(url);
        getResourceProvider().log(throwable);
    }
}*/