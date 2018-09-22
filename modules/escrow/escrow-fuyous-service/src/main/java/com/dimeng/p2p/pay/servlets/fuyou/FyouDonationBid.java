package com.dimeng.p2p.pay.servlets.fuyou;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.digest.UnixCrypt;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.resource.ResourceProvider;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.p2p.AbstractFuyouServlet;
import com.dimeng.p2p.S65.entities.T6554;
import com.dimeng.p2p.S65.enums.T6501_F03;
import com.dimeng.p2p.common.FormToken;
import com.dimeng.p2p.common.RSAUtils;
import com.dimeng.p2p.escrow.fuyou.entity.enums.FuyouRespCode;
import com.dimeng.p2p.escrow.fuyou.entity.enums.FuyouTypeEnum;
import com.dimeng.p2p.escrow.fuyou.service.BidManage;
import com.dimeng.p2p.escrow.fuyou.service.TransferManage;
import com.dimeng.p2p.escrow.fuyou.util.BackCodeInfo;
import com.dimeng.p2p.escrow.fuyou.util.MchntTxnSsn;
import com.dimeng.p2p.escrow.fuyou.variables.FuyouVariable;
import com.dimeng.p2p.modules.bid.pay.service.DonationService;
import com.dimeng.p2p.order.DonationExecutor;
import com.dimeng.p2p.variables.defines.URLVariable;
import com.dimeng.util.parser.BigDecimalParser;
import com.dimeng.util.parser.IntegerParser;

/**
 * 
 * 公益标
 * <功能详细描述>
 * 
 * @author  heshiping
 * @version  [版本号, 2016年1月4日]
 */
public class FyouDonationBid extends AbstractFuyouServlet
{
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession) throws Throwable {
        if (!FormToken.verify(serviceSession.getSession(), request.getParameter(FormToken.parameterName()))) {
            throw new LogicalException("请不要重复提交请求！");
        }
        final BigDecimal amount = BigDecimalParser.parse(request.getParameter("amount1"));
        int loanId = IntegerParser.parse(request.getParameter("loanId1"));
        String tranPwd = request.getParameter("tranPwd");
        tranPwd = RSAUtils.decryptStringByJs(tranPwd);
        tranPwd = UnixCrypt.crypt(tranPwd, DigestUtils.sha256Hex(tranPwd));
        DonationService tenderManage = serviceSession.getService(DonationService.class);
        BidManage bidManage = serviceSession.getService(BidManage.class);
        final int orderId = tenderManage.bid(loanId, amount, tranPwd);
        final ConfigureProvider configureProvider = getConfigureProvider();
        DonationExecutor executor = getResourceProvider().getResource(DonationExecutor.class);
        executor.submit(orderId, null);
        String mchnt_txn_ssn = MchntTxnSsn.getMts(FuyouTypeEnum.GYJK.name());
        bidManage.updateT6501(orderId, mchnt_txn_ssn);
        T6501_F03 t6501_F03 = bidManage.selectT6501(orderId);
        if (T6501_F03.DQR != t6501_F03) {
            return;
        }
        T6554 t6554 = bidManage.selectT6554(orderId);
        String out_cust_no = bidManage.selectT6119(t6554.F02);
        String in_cust_no = configureProvider.format(FuyouVariable.FUYOU_P2P_ACCOUNT_NAME);
        TransferManage manage = serviceSession.getService(TransferManage.class);
        Map<String, Object> param =	manage.createTransferMap(
                mchnt_txn_ssn, out_cust_no, in_cust_no, getAmt(amount), "transferBmu");
        if (param == null) {
            throw new LogicalException("转账接口失败！");            
        } else if (!FuyouRespCode.JYCG.getRespCode().equals(param.get("resp_code"))) {
            throw new LogicalException(BackCodeInfo.info("resp_code"));
        }
        executor.confirm(orderId, null);
        T6501_F03 t6501_F03s = bidManage.selectT6501(orderId);
        if (T6501_F03.CG != t6501_F03s) {
            getController().prompt(request, response, PromptLevel.ERROR, "捐赠失败!");
        } else {
            getController().prompt(request, response, PromptLevel.INFO, "谢谢您的捐赠!");
        }
        sendRedirect(request, response, getURL(loanId));
    }
    
    @Override
    protected void onThrowable(HttpServletRequest request, HttpServletResponse response, Throwable throwable) throws ServletException, IOException {
        getResourceProvider().log(throwable);
        int loanId = IntegerParser.parse(request.getParameter("loanId1"));
        if (throwable instanceof ParameterException || throwable instanceof SQLException) {
            getController().prompt(request, response, PromptLevel.ERROR, "系统繁忙，请您稍后再试!");
            sendRedirect(request, response, getURL(loanId));
        } else if (throwable instanceof LogicalException){
            getController().prompt(request, response, PromptLevel.WARRING, throwable.getMessage());
            sendRedirect(request, response, getURL(loanId));
        } else {
            super.onThrowable(request, response, throwable);
        }
    }
    
    protected String getURL(int loanId) throws IOException {
        ResourceProvider resourceProvider = getResourceProvider();
        final ConfigureProvider configureProvider = resourceProvider.getResource(ConfigureProvider.class);
        StringBuilder url = new StringBuilder(configureProvider.format(URLVariable.GYB_BDXQ));
        url.append(Integer.toString(loanId)).append(resourceProvider.getSystemDefine().getRewriter().getViewSuffix());
        return url.toString();
    }
    
}