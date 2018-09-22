/*package com.dimeng.p2p.app.servlets.pay.service.fuyou;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bouncycastle.util.encoders.Base64;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.resource.ResourceProvider;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.p2p.S65.entities.T6554;
import com.dimeng.p2p.S65.enums.T6501_F03;
import com.dimeng.p2p.app.config.Config;
import com.dimeng.p2p.escrow.fuyou.entity.enums.FuyouTypeEnum;
import com.dimeng.p2p.escrow.fuyou.service.BidManage;
import com.dimeng.p2p.escrow.fuyou.service.TransferManage;
import com.dimeng.p2p.escrow.fuyou.util.BackCodeInfo;
import com.dimeng.p2p.escrow.fuyou.util.MchntTxnSsn;
import com.dimeng.p2p.escrow.fuyou.variables.FuyouVariable;
import com.dimeng.p2p.modules.bid.pay.service.DonationService;
import com.dimeng.p2p.order.DonationExecutor;
import com.dimeng.p2p.variables.defines.URLVariable;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.BigDecimalParser;
import com.dimeng.util.parser.IntegerParser;

*//**
 * 
 * 公益标
 * <功能详细描述>
 * 
 * @author  suwei
 * @version  [版本号, 2016年03月02日]
 *//*
public class FyouDonationBid extends AbstractFuyouServlet
{
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        final BigDecimal amount = BigDecimalParser.parse(request.getParameter("amount"));
        int loanId = IntegerParser.parse(request.getParameter("loanId"));
        String tranPwd = request.getParameter("tranPwd");
        
        DonationService tenderManage = serviceSession.getService(DonationService.class);
        BidManage bidManage = serviceSession.getService(BidManage.class);
        final int orderId = tenderManage.bid(loanId, amount, tranPwd);
        
        final ConfigureProvider configureProvider = getConfigureProvider();
        
        DonationExecutor executor = getResourceProvider().getResource(DonationExecutor.class);
        executor.submit(orderId, null);
        
        String mchnt_txn_ssn = MchntTxnSsn.getMts(FuyouTypeEnum.GYJK.name());
        bidManage.updateT6501(orderId, mchnt_txn_ssn);
        T6501_F03 t6501_F03 = bidManage.selectT6501(orderId);
        if (T6501_F03.DQR != t6501_F03)
        {
            return;
        }
        T6554 t6554 = bidManage.selectT6554(orderId);
        final String mchnt_cd = configureProvider.format(FuyouVariable.FUYOU_ACCOUNT_ID);
        String actionUrl = configureProvider.format(FuyouVariable.FUYOU_TRANSFERBMU_URL);
        String out_cust_no = bidManage.selectT6119(t6554.F02);
        String in_cust_no = configureProvider.format(FuyouVariable.FUYOU_P2P_ACCOUNT_NAME);
        String amt = getAmt(amount);
        String tran_name = "HK";
        String mchnt_amt = "0";
        String contract_no = "";
        TransferManage manage = serviceSession.getService(TransferManage.class);
        Map<String, Object> param =
            manage.createTransferMap(mchnt_txn_ssn, out_cust_no, in_cust_no, getAmt(amount), "transferBmu");
        if (param == null || !"0000".equals(param.get("resp_code")))
        {
            if (StringHelper.isEmpty((String)param.get("resp_code")))
            {
            	String url = getSiteDomain(Config.buyGyBidRetUrl) + "?code=000004&description=转账接口失败";
                sendRedirect(request, response, url);
//                setReturnMsg(request, response, ExceptionCode.TRAN_INTERFACE_ERROR, "转账接口失败！");
                return;
            }
            throw new LogicalException(BackCodeInfo.info(param.get("resp_code").toString()));
        }
        executor.confirm(orderId, null);
        T6501_F03 t6501_F03s = bidManage.selectT6501(orderId);
        if (T6501_F03.CG != t6501_F03s)
        {
        	String url = getSiteDomain(Config.buyGyBidRetUrl) + "?code=000004&description=捐赠失败";
            sendRedirect(request, response, url);
//            setReturnMsg(request, response, ExceptionCode.ERROR, "捐赠失败");
            return;
        }
        else
        {
        	String url = getSiteDomain(Config.buyGyBidRetUrl) + "?code=000000&description=success";
            sendRedirect(request, response, url);
//            setReturnMsg(request, response, ExceptionCode.SUCCESS, "谢谢您的捐赠");
            return;
        }
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
    
    protected String getURL(int loanId)
        throws IOException
    {
        ResourceProvider resourceProvider = getResourceProvider();
        final ConfigureProvider configureProvider = resourceProvider.getResource(ConfigureProvider.class);
        StringBuilder url = new StringBuilder(configureProvider.format(URLVariable.GYB_BDXQ));
        url.append(Integer.toString(loanId)).append(resourceProvider.getSystemDefine().getRewriter().getViewSuffix());
        return url.toString();
    }
    
}*/