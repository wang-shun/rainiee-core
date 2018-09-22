/**
 * 文 件 名:  DonationBid.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  zhoulantao
 * 修改时间:  2016年3月5日
 *//*
package com.dimeng.p2p.app.servlets.pay.service.shuangqian;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bouncycastle.util.encoders.Base64;

import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.app.config.Config;
import com.dimeng.p2p.app.servlets.AbstractSecureServlet;
import com.dimeng.p2p.escrow.shuangqian.entity.TransferEntity;
import com.dimeng.p2p.escrow.shuangqian.service.BidManage;
import com.dimeng.p2p.escrow.shuangqian.variables.ShuangQianVariable;
import com.dimeng.util.parser.IntegerParser;

*//**
 * 公益标捐赠
 * 
 * @author  zhoulantao
 * @version  [版本号, 2016年3月5日]
 *//*
public class DonationBid extends AbstractSecureServlet
{
    *//**
     * 注释内容
     *//*
    private static final long serialVersionUID = 516066991936810968L;

    @Override
    protected void processPost(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        // 获取页面参数
        final int orderId = IntegerParser.parse(request.getAttribute("orderId"));
        BidManage bidManage=serviceSession.getService(BidManage.class);
        String returnUrl =
            getSiteDomain("/pay/service/shuangqian/ret/donationBidRet.htm" + "?retUrl=" + Config.buyGyBidRetUrl);
        String notifyURL = getConfigureProvider().format(ShuangQianVariable.SQ_DONATIONBIDNOTIFY);
        TransferEntity donationBidEntity = bidManage.getDonationBidEntity(orderId, returnUrl, notifyURL);
        String location = bidManage.createBidForm(donationBidEntity);

        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        
        PrintWriter writer = response.getWriter();
        writer.print(location);
        writer.flush();
        writer.close();
        return;
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
*/