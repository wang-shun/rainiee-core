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
import com.dimeng.p2p.S61.entities.T6114;
import com.dimeng.p2p.S65.entities.T6503;
import com.dimeng.p2p.app.config.Config;
import com.dimeng.p2p.app.servlets.AbstractSecureServlet;
import com.dimeng.p2p.escrow.shuangqian.service.WithdrawManage;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.IntegerParser;

*//**
* 
* 双乾第三方提现
* 
* @author  zhoulantao
* @version  [版本号, 2016年2月23日]
*/
/*
public class ThirdWithdraw extends AbstractSecureServlet
{
 *//**
* 注释内容
*/
/*
private static final long serialVersionUID = 4911101776797519468L;

@Override
protected void processPost(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
 throws Throwable
{
 response.setContentType("text/html");
 response.setCharacterEncoding("UTF-8");
 
 // 银行卡ID
 final int cardId = IntegerParser.parse(request.getAttribute("cardId"));
 
 // 提现金额
 final BigDecimal amount = new BigDecimal((String)request.getAttribute("amount"));
 
 // 查询银行卡信息
 final WithdrawManage manage = serviceSession.getService(WithdrawManage.class);
 final T6114 t6114 = manage.getBankCard(cardId);
 final String bankCard = StringHelper.decode(t6114.F07).replace(" ", "");
 
 // 增加提现订单
 final T6503 t6503 = manage.addOrder(bankCard, amount);
 String returnURL = getSiteDomain("/pay/service/shuangqian/ret/withdrawRet.htm?retUrl=" + Config.withdrawRetUrl);
 // 封装发送给双乾的请求消息
 final String location = manage.createWithdrawForm(t6503.F01, amount, t6114, bankCard, true, returnURL);
 
 PrintWriter writer = response.getWriter();
 writer.print(location);
 writer.flush();
 writer.close();
 return;
}

*//**
* 将异常消息返回给页面
*/
/*
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