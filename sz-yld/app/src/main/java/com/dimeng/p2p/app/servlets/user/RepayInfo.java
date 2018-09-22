package com.dimeng.p2p.app.servlets.user;

import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S62.enums.T6252_F09;
import com.dimeng.p2p.app.servlets.AbstractSecureServlet;
import com.dimeng.p2p.app.servlets.bid.domain.Repay;
import com.dimeng.p2p.app.servlets.platinfo.ExceptionCode;
import com.dimeng.p2p.modules.bid.front.service.BidManage;
import com.dimeng.p2p.modules.bid.front.service.entity.Hkjllb;
import com.dimeng.p2p.modules.bid.user.service.WdjkManage;
import com.dimeng.p2p.modules.bid.user.service.entity.RepayLoanDetail;
import com.dimeng.util.parser.IntegerParser;
import com.dimeng.util.parser.LongParser;

/**
 * 
 * 还款详情
 * 
 * @author  zhoulantao
 * @version  [版本号, 2015年12月4日]
 */
public class RepayInfo extends AbstractSecureServlet
{
    
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processGet(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        processPost(request, response, serviceSession);
    }
    
    @Override
    protected void processPost(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        // 标的ID
        final String bidId = getParameter(request, "bidId");
        
        // 查询类型  
        final String type = getParameter(request, "type");
        
        // 分页信息
        final int pageIndex = Integer.parseInt(getParameter(request, "pageIndex"));
        final int pageSize = Integer.parseInt(getParameter(request, "pageSize"));
        
        // 分页信息
        final Paging paging = getPaging(pageIndex, pageSize);
        
        int id = IntegerParser.parse(bidId);
        if (id == 0)
        {
            // 封装返回信息
            setReturnMsg(request, response, ExceptionCode.PARAMETER_ERROR, "fail");
            return;
        }
        
        BidManage bidManage = serviceSession.getService(BidManage.class);
        List<Repay> repayList = new ArrayList<Repay>();
        
        if (type.equalsIgnoreCase("WDJKHKZ"))
        {
            // 我的借款还款中的
            WdjkManage creditManage = serviceSession.getService(WdjkManage.class);
            PagingResult<RepayLoanDetail> results = creditManage.getRepayLoanDetail(id, paging);
            
            RepayLoanDetail[] details = null;
            if (null != results)
            {
                // 判断是否超出记录集
                if (results.getPageCount() >= LongParser.parse(pageIndex))
                {
                    details = results.getItems();
                }
            }
            
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            if (details != null && details.length > 0)
            {
                for (RepayLoanDetail detail : details)
                {
                    Repay repay = new Repay();
                    repay.setAmount(String.valueOf(detail.F07.setScale(2, RoundingMode.HALF_UP)));
                    repay.setRepayDate(detail.F08 != null ? sdf.format(detail.F08) : "");
                    repay.setRealDate(detail.F10 != null ? sdf.format(detail.F10) : "");
                    repay.setRepayType(detail.typeName);
                    repay.setTerm(detail.F06);
                    repay.setStatus(detail.paymentStatus == null ? "" : detail.paymentStatus.getName());
                    repayList.add(repay);
                }
            }
        }
        else if (type.equalsIgnoreCase("WDJKYJQ"))
        {
            // 我的借款已结清的
            Hkjllb[] details = bidManage.getHk(id);
            
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            if (details != null && details.length > 0)
            {
                for (Hkjllb detail : details)
                {
                    if (detail.F03 != T6252_F09.TQH)
                    {
                        Repay repay = new Repay();
                        repay.setAmount(String.valueOf(detail.F01.setScale(2, RoundingMode.HALF_UP)));
                        repay.setRepayDate(detail.F02 != null ? sdf.format(detail.F02) : "");
                        repay.setRealDate(detail.F04 != null ? sdf.format(detail.F04) : "");
                        repay.setStatus(detail.F03.getChineseName());
                        repay.setRepayType(detail.F05);
                        repayList.add(repay);
                    }
                }
            }
        }
        else
        {
            Hkjllb[] details = bidManage.getHk(id);
            
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            if (details != null && details.length > 0)
            {
                for (Hkjllb detail : details)
                {
                    Repay repay = new Repay();
                    repay.setAmount(String.valueOf(detail.F01.setScale(2, RoundingMode.HALF_UP)));
                    repay.setRepayDate(detail.F02 != null ? sdf.format(detail.F02) : "");
                    repay.setRealDate(detail.F04 != null ? sdf.format(detail.F04) : "");
                    repay.setStatus(detail.F03.getChineseName());
                    repay.setRepayType(detail.F05);
                    repayList.add(repay);
                }
            }
        }
        
        // 封装返回信息
        setReturnMsg(request, response, ExceptionCode.SUCCESS, "success", repayList);
        return;
    }
}
