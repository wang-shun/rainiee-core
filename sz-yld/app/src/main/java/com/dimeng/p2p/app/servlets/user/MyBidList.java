package com.dimeng.p2p.app.servlets.user;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S62.entities.T6230;
import com.dimeng.p2p.S62.entities.T6231;
import com.dimeng.p2p.S62.enums.T6230_F20;
import com.dimeng.p2p.S62.enums.T6231_F19;
import com.dimeng.p2p.S62.enums.T6231_F21;
import com.dimeng.p2p.app.servlets.AbstractSecureServlet;
import com.dimeng.p2p.app.servlets.platinfo.ExceptionCode;
import com.dimeng.p2p.app.servlets.user.domain.UserBid;
import com.dimeng.p2p.modules.bid.front.service.BidManage;
import com.dimeng.p2p.modules.bid.user.service.JksqcxManage;
import com.dimeng.p2p.modules.bid.user.service.WdjkManage;
import com.dimeng.p2p.modules.bid.user.service.entity.HkEntity;
import com.dimeng.util.parser.IntegerParser;
import com.dimeng.util.parser.LongParser;

/**
 * 
 * 我的借款
 * 
 * @author  zhoulantao
 * @version  [版本号, 2015年12月4日]
 */
public class MyBidList extends AbstractSecureServlet
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
        // 1是还款中的，2是已还清的, 3是我的申请
        String bidStatus = getParameter(request, "bidStatus");
        final int pageIndex = IntegerParser.parse(getParameter(request, "pageIndex"));
        final int pageSize = IntegerParser.parse(getParameter(request, "pageSize"));
        int type = IntegerParser.parse(bidStatus);
        if (type <= 0)
        {
            // 封装返回信息
            setReturnMsg(request, response, ExceptionCode.PARAMETER_ERROR, "parameter error!");
            return;
        }
        // 获取分页信息
        final Paging pageing = getPaging(pageIndex, pageSize);
        
        SimpleDateFormat dateSdf = new SimpleDateFormat("yyyy-MM-dd");
        BidManage bidManage = serviceSession.getService(BidManage.class);
        WdjkManage manage = serviceSession.getService(WdjkManage.class);
        PagingResult<HkEntity> pagingResult = null;
        List<UserBid> userBids = new ArrayList<UserBid>();
        
        if (type == 3) 
        {
        	JksqcxManage jmanage = serviceSession.getService(JksqcxManage.class);
        	PagingResult<T6230> result = jmanage.getApplyLoans(getPaging(pageIndex, pageSize));

        	if (result.getPageCount() < LongParser.parse(pageIndex))
            {
                // 封装返回信息
                setReturnMsg(request, response, ExceptionCode.SUCCESS, "success", userBids);
                return;
            }
        	
        	T6230 [] applyLoans = result.getItems();
            if (applyLoans != null && applyLoans.length > 0)
            {
            	UserBid ub = null;
            	for (T6230 t6230 : applyLoans) 
            	{
            		T6231 t6231 = bidManage.getExtra(t6230.F01);
            		ub = new UserBid();
            		ub.setBidId(t6230.F01);
            		ub.setBidTitle(t6230.F03);
            		// 年化利率
                    ub.setRate(t6230.F06 != null ? String.valueOf(t6230.F06.setScale(4, BigDecimal.ROUND_HALF_UP)): "0");
                    // 标的状态
                    ub.setStatus(t6230.F20.getChineseName());
                    // 标的借款金额
                    ub.setTotalAmount(t6230.F05 != null ? String.valueOf(t6230.F05) : "0");
                    // 借款期限
                    ub.setLoanTerm(t6230.F28.F21 == T6231_F21.F ? (t6230.F09)+"个月" : (t6230.F28.F22)+"天");
                    // 结清日期
                    ub.setCleanTime(t6231.F13 != null ? dateSdf.format(t6231.F13) : "");
                    userBids.add(ub);
    			}
            }
            
            // 封装返回信息
            setReturnMsg(request, response, ExceptionCode.SUCCESS, "success", userBids);
            return;
		}
        else
        {
        	if (type == 1)
            {
                // 1是还款中的
                pagingResult = manage.getHkzJk(pageing);
            }
            else if (type == 2)
            {
                // 2是已还清的
                pagingResult = manage.getYhqJk(pageing);
            }
            
            if (pagingResult == null)
            {
                // 封装返回信息
                setReturnMsg(request, response, ExceptionCode.PARAMETER_ERROR, "parameter error!");
                return;
            }
            
            if (pagingResult.getPageCount() < LongParser.parse(pageIndex))
            {
                // 封装返回信息
                setReturnMsg(request, response, ExceptionCode.SUCCESS, "success", userBids);
                return;
            }
            
            HkEntity[] repayLoans = pagingResult.getItems();
            if (repayLoans != null && repayLoans.length > 0)
            {
                for (HkEntity hk : repayLoans)
                {
                    UserBid ub = new UserBid();
                    
                    // 当期还款金额
                    ub.setCurBackAmount(hk.dqyhje != null ? String.valueOf(hk.dqyhje) : "0");
                    
                    T6231 t6231 = bidManage.getExtra(hk.F01);
                    // 下个还款日期
                    ub.setBackTime(t6231.F06 != null ? dateSdf.format(t6231.F06) : "");
                    // 标的ID
                    ub.setBidId(hk.F01);
                    // 标的名称
                    ub.setBidTitle(hk.F03);
                    
                    // 年化利率
                    ub.setRate(hk.F06 != null ? String.valueOf(hk.F06 != null ? hk.F06.setScale(4, BigDecimal.ROUND_HALF_UP)
                        : 0)
                        : "0");
                    
                    // 标的状态
                    ub.setStatus(hk.F20.getChineseName());
                    
                    // 借款金额 = 标的借款金额 - 可投金额 
                    //ub.setTotalAmount(hk.F05 != null ? String.valueOf(hk.F05) : "0");
                    ub.setTotalAmount(hk.F05 != null ? String.valueOf(hk.F05.subtract(hk.F07)) : "0");
                    
                    // 已还期数
                    ub.setBackTerm(t6231.F03);
                    
                    // 总期数
                    ub.setTotalTerm(t6231.F02);
                    
                    // 借款期限
                    if (T6231_F21.S == t6231.F21)
                    {
                        ub.setTheterm(t6231.F22);
                    }
                    else
                    {
                        ub.setTheterm(hk.F09);
                    }
                    
                    // 总还款金额
                    ub.setTotalBackAmount(hk.hkTotle != null ? String.valueOf(hk.hkTotle) : "0");
                    
                    // 结清日期
                    ub.setCleanTime(t6231.F13 != null ? dateSdf.format(t6231.F13) : "");
                    
                    // 是否按天借款
                    ub.setIsDay(t6231.F21.name());
                    
                    // 还款信息
                    ub.setRepayInfo(hk.repayInfo);
                    
                    // 提前还款信息
                    ub.setForwardRepayInfo(hk.forwardRepayInfo);
                    
                    // 判断是否可以提前还款
                    if (hk.F28.F19 == T6231_F19.F && hk.F20 != T6230_F20.YDF)
                    {
                        ub.setIsPrepayment("true");
                    }
                    
                    userBids.add(ub);
                }
            }
            
            // 如果是还款中的，需要倒序显示
            if (type == 1)
            {
                // 倒序一下,jsp页面append需要
                if (null != userBids && userBids.size() > 0)
                {
                    Collections.reverse(userBids);
                }
            }
            
            // 封装返回信息
            setReturnMsg(request, response, ExceptionCode.SUCCESS, "success", userBids);
            return;
        }
    }
}
