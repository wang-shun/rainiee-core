package com.dimeng.p2p.console.servlets.bid.loanmanage.jkgl;

import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S62.entities.T6211;
import com.dimeng.p2p.S62.entities.T6216;
import com.dimeng.p2p.S62.enums.T6216_F04;
import com.dimeng.p2p.S62.enums.T6230_F20;
import com.dimeng.p2p.S62.enums.T6231_F35;
import com.dimeng.p2p.console.servlets.bid.AbstractBidServlet;
import com.dimeng.p2p.modules.base.console.service.ProductManage;
import com.dimeng.p2p.modules.base.console.service.query.ProductQuery;
import com.dimeng.p2p.modules.bid.console.service.BidManage;
import com.dimeng.p2p.modules.bid.console.service.entity.Bid;
import com.dimeng.p2p.modules.bid.console.service.query.LoanExtendsQuery;
import com.dimeng.util.parser.EnumParser;
import com.dimeng.util.parser.IntegerParser;
import com.dimeng.util.parser.TimestampParser;

@Right(id = "P2P_C_LOAN_LOANLIST", name = "借款管理", isMenu = true, moduleId = "P2P_C_BID_JKGL_LOANMANAGE", order = 0)
public class LoanList extends AbstractBidServlet
{
    
    /**
     *
     */
    private static final long serialVersionUID = -268955794485557959L;
    
    @Override
    protected void processGet(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        processPost(request, response, serviceSession);
    }
    
    @Override
    protected void processPost(final HttpServletRequest request, HttpServletResponse response,
        ServiceSession serviceSession)
        throws Throwable
    {
        BidManage bidManage = serviceSession.getService(BidManage.class);
        ProductManage productManage = serviceSession.getService(ProductManage.class);
        
        Paging paging = new Paging()
        {
            
            @Override
            public int getSize()
            {
                return 100;
            }
            
            @Override
            public int getCurrentPage()
            {
                return IntegerParser.parse(request.getParameter(PAGING_CURRENT));
            }
        };
        PagingResult<T6216> resultProduct = productManage.search(new ProductQuery()
        {
            
            @Override
            public T6216_F04 getStatus()
            {
                //return T6216_F04.QY;
                return null;
            }
            
            @Override
            public String getProductName()
            {
                return "";
            }
            
            @Override
            public String getBidType()
            {
                
                return "";
            }
            
            @Override
            public String getRepaymentWay()
            {
                return "";
            }
            
        }, paging);
        request.setAttribute("resultProduct", resultProduct);
        
        T6211[] t6211s = bidManage.getT6211s();
        
        LoanExtendsQuery loanExtendsQuery = new LoanExtendsQuery()
        {
            
            @Override
            public int getType()
            {
                return IntegerParser.parse(request.getParameter("type"));
            }
            
            @Override
            public T6230_F20 getStatus()
            {
                return EnumParser.parse(T6230_F20.class, request.getParameter("status"));
            }
            
            @Override
            public String getName()
            {
                return request.getParameter("name");
            }
            
            @Override
            public String getLoanTitle()
            {
                return request.getParameter("title");
            }
            
            @Override
            public Timestamp getCreateTimeStart()
            {
                return TimestampParser.parse(request.getParameter("createTimeStart"));
            }
            
            @Override
            public Timestamp getCreateTimeEnd()
            {
                return TimestampParser.parse(request.getParameter("createTimeEnd"));
            }
            
            @Override
            public String getBidNo()
            {
                return request.getParameter("bidNo");
            }
            
            @Override
            public String getUserType()
            {
                return request.getParameter("userType");
            }
            
            @Override
            public int getProductId()
            {
                return IntegerParser.parse(request.getParameter("productId"));
            }
            
            @Override
            public String getBidFlag()
            {
                return request.getParameter("bidFlag");
            }
            
            @Override
            public String getBidAttr()
            {
                return request.getParameter("bidAttr");
            }
            
            @Override
            public T6231_F35 getSource()
            {
                return EnumParser.parse(T6231_F35.class, request.getParameter("source"));
            }
        };
        PagingResult<Bid> result = bidManage.search(loanExtendsQuery, new Paging()
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
        Bid searchAmount = bidManage.searchAmount(loanExtendsQuery);
        request.setAttribute("searchAmount", searchAmount);
        request.setAttribute("result", result);
        request.setAttribute("t6211s", t6211s);
        forwardView(request, response, getClass());
    }
}
