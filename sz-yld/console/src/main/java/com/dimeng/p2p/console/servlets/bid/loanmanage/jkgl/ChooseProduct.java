package com.dimeng.p2p.console.servlets.bid.loanmanage.jkgl;

import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S62.entities.T6216;
import com.dimeng.p2p.S62.enums.T6216_F04;
import com.dimeng.p2p.console.servlets.base.AbstractBaseServlet;
import com.dimeng.p2p.modules.base.console.service.ProductManage;
import com.dimeng.p2p.modules.base.console.service.query.ProductQuery;
import com.dimeng.util.parser.IntegerParser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ChooseProduct extends AbstractBaseServlet
{
    
    private static final long serialVersionUID = 1L;
    
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
        String userId = request.getParameter("userId");
        
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
        PagingResult<T6216> result = productManage.search(new ProductQuery()
        {
            
            @Override
            public T6216_F04 getStatus()
            {
                return T6216_F04.QY;
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
        request.setAttribute("result", result);
        request.setAttribute("userId", userId);
        forwardView(request, response, getClass());
    }
    
}
