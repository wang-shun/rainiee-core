package com.dimeng.p2p.console.servlets.finance.fuyou.dzgl.chargedzgl;

import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.OrderType;
import com.dimeng.p2p.console.servlets.finance.fuyou.dzgl.AbstractDzglServlet;
import com.dimeng.p2p.escrow.fuyou.cond.TbdzCond;
import com.dimeng.p2p.escrow.fuyou.entity.console.TbdzEntity;
import com.dimeng.p2p.escrow.fuyou.service.BidManage;
import com.dimeng.util.parser.IntegerParser;
import com.dimeng.util.parser.TimestampParser;

/**
 * 
 * 充值对账列表
 * 
 * @author  heshiping
 * @version  [版本号, 2016年3月3日]
 */
@Right(id = "P2P_C_FINANCE_CHARGELIST", isMenu = true, name = "充值对账", moduleId = "P2P_C_FUYOU_CZDZGL", order = 0)
public class CzdzList extends AbstractDzglServlet
{
    
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processGet(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        processPost(request, response, serviceSession);
    }
    
    @Override
    protected void processPost(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        BidManage manage = serviceSession.getService(BidManage.class);
        PagingResult<TbdzEntity> result = manage.search(new TbdzCond()
        {
            
            @Override
            public String tradingType()
            {
                return OrderType.CHARGE.toString();
            }
            
            @Override
            public String userName()
            {
                return request.getParameter("userName");
            }
            
            @Override
            public Timestamp getStartExpireDatetime()
            {
                return TimestampParser.parse(request.getParameter("startExpireDatetime"));
            }
            
            @Override
            public Timestamp getEndExpireDatetime()
            {
                return TimestampParser.parse(request.getParameter("endExpireDatetime"));
            }
            
            @Override
            public String f01()
            {
                return request.getParameter("OrderNo");
            }
            
            @Override
            public String f10()
            {
                return request.getParameter("LoanNo");
            }
            
            @Override
            public String state()
            {
                return request.getParameter("state") == null ? "" : request.getParameter("state");
            }
        }, new Paging()
        {
            @Override
            public int getSize()
            {
                return 10;
            }
            
            @Override
            public int getCurrentPage()
            {
                return IntegerParser.parse(request.getParameter(PAGING_CURRENT));
            }
        });
        request.setAttribute("result", result);
        request.setAttribute("userName", request.getParameter("userName"));
        request.setAttribute("LoanNo", request.getParameter("LoanNo"));
        request.setAttribute("OrderNo", request.getParameter("OrderNo"));
        request.setAttribute("state", request.getParameter("state") == null ? "" : request.getParameter("state"));
        forwardView(request, response, getClass());
    }
}
