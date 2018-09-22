/*package com.dimeng.p2p.app.servlets.creditor;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bouncycastle.util.encoders.Base64;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.S61.entities.T6119;
import com.dimeng.p2p.S65.entities.T6507;
import com.dimeng.p2p.app.config.Config;
import com.dimeng.p2p.app.servlets.AbstractSecureServlet;
import com.dimeng.p2p.app.servlets.platinfo.ExceptionCode;
import com.dimeng.p2p.escrow.shuangqian.entity.TransferEntity;
import com.dimeng.p2p.escrow.shuangqian.service.BidExchangeManage;
import com.dimeng.p2p.escrow.shuangqian.service.BidManage;
import com.dimeng.p2p.escrow.shuangqian.service.UserManage;
import com.dimeng.p2p.escrow.shuangqian.variables.ShuangQianVariable;
import com.dimeng.p2p.modules.bid.user.service.TenderTransferManage;
import com.dimeng.p2p.order.TenderExchangeExecutor;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.IntegerParser;

*//**
 * 
 * 债权转让封装请求消息(双乾托管)
 * 
 * @author  zhoulantao
 * @version  [版本号, 2016年1月11日]
 *//*
public class BidExchangeProxy extends AbstractSecureServlet
{
    
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        
        final ConfigureProvider configureProvider = getConfigureProvider();
        int zcbId = IntegerParser.parse(request.getParameter("creditorId"));
        TenderTransferManage transferManage = serviceSession.getService(TenderTransferManage.class);
        BidExchangeManage manage = serviceSession.getService(BidExchangeManage.class);
        BidManage bidManage = serviceSession.getService(BidManage.class);
        int orderId = transferManage.purchase(zcbId);
        T6507 t6507 = manage.selectT6507(orderId);
        String retUrl = Config.buyCreditorRetUrl;
        
        // 获取用户授权情况
        UserManage userManage = serviceSession.getService(UserManage.class);
        T6119 t6119 = userManage.selectT6119();
        if (StringHelper.isEmpty(t6119.F04))
        {
            String url = getSiteDomain("/pay/shuangqian/authorize.htm");
            
            setReturnMsg(request,
                response,
                ExceptionCode.UNAUTHORIZED_EXCEPTION,
                "您尚未授权还款转账与二次分配转账，不能操作，点击确定跳转到授权页面！",
                url);
            return;
        }
        
        TenderExchangeExecutor executor = getResourceProvider().getResource(TenderExchangeExecutor.class);
        executor.submit(orderId, null);
        
        String returnUrl = getSiteDomain("/pay/service/shuangqian/ret/bidExchangeRet.htm" + "?retUrl=" + retUrl);
        String notifyURL = configureProvider.format(ShuangQianVariable.SQ_BIDEXCHANGE_NOTIFY);
        TransferEntity entity = manage.getBidExchangeEntity(zcbId, t6507, t6119, returnUrl, notifyURL);
        String location = bidManage.createBidExchangeForm(entity);

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
        String enRetUrl = Config.buyCreditorRetUrl;
        String url =
            enRetUrl + "?" + "code=000004&description="
                + URLEncoder.encode(new String(Base64.encode(throwable.getMessage().getBytes("UTF-8")), "UTF-8"));
        
        response.sendRedirect(url);
        getResourceProvider().log(throwable);
    }
    
}*/