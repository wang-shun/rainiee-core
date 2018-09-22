/**
 * 文 件 名:  ShuangqianPrePayment.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  zhoulantao
 * 修改时间:  2016年3月5日
 *//*
package com.dimeng.p2p.app.servlets.pay.service.shuangqian;

import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.p2p.app.config.Config;
import com.dimeng.p2p.app.servlets.AbstractSecureServlet;
import com.dimeng.p2p.escrow.shuangqian.cond.LoanJsonList;
import com.dimeng.p2p.escrow.shuangqian.entity.TransferEntity;
import com.dimeng.p2p.escrow.shuangqian.service.BidManage;
import com.dimeng.p2p.escrow.shuangqian.service.PaymentManage;
import com.dimeng.p2p.escrow.shuangqian.variables.ShuangQianVariable;
import com.dimeng.util.parser.IntegerParser;

*//**
* 手工提前还款(双乾托管)
* 
* @author  zhoulantao
* @version  [版本号, 2016年3月5日]
*//*
public class ShuangqianPrePayment extends AbstractSecureServlet
{
    *//**
    * 注释内容
    *//*
    private static final long serialVersionUID = 6504166786264263902L;
    
    @Override
    protected void processPost(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        // 标ID
        final int loanId = IntegerParser.parse(request.getAttribute("loanId"));
        
        // 订单编号
        final int[] orderIds = (int[])request.getAttribute("orderIds");
        
        if (orderIds != null && orderIds.length > 0)
        {
            BidManage bidManage = serviceSession.getService(BidManage.class);
            PaymentManage paymentManage = serviceSession.getService(PaymentManage.class);
            // 封装第三方订单
            List<LoanJsonList> loanJsonList = paymentManage.getPrepaymentLoanJsonList(orderIds, loanId);
            String returnUrl =
                getSiteDomain("/pay/service/shuangqian/ret/tenderRepaymentRet.htm" + "?retUrl="
                    + Config.repaymentRetUrl);
            String notifyURL = getConfigureProvider().format(ShuangQianVariable.SQ_REPAYMENT_NOTIFY);
            TransferEntity entity = paymentManage.getPaymentEntity(loanJsonList, true, returnUrl, notifyURL);
            String location = bidManage.createPaymentForm(entity);
            
            response.setContentType("text/html");
            response.setCharacterEncoding("UTF-8");
            
            PrintWriter writer = response.getWriter();
            writer.print(location);
            writer.flush();
            writer.close();
        }
        else
        {
            throw new LogicalException("还款订单不存在");
        }
        return;
    }
}
*/