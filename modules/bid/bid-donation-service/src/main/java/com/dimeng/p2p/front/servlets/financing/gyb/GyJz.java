package com.dimeng.p2p.front.servlets.financing.gyb;

import com.dimeng.framework.http.servlet.annotation.PagingServlet;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.AbstractDonationServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@PagingServlet(itemServlet = GyJz.class)
public class GyJz extends AbstractDonationServlet
{
    /**
     * 注释内容
     */
    private static final long serialVersionUID = -3169057464358772819L;
    
    @Override
    protected void processGet(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        // TODO Auto-generated method stub
        super.processGet(request, response, serviceSession);
    }
    
    @Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        /*GyLoanManage gyLoanManage = serviceSession.getService(GyLoanManage.class);
        PagingResult<GyLoan> gyList = gyLoanManage.search(new GyLoanQuery()
        {
            
            @Override
            public T6242_F11 getStatus()
            {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public String getName()
            {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public String getLoanTitle()
            {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public String getGyName()
            {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public Timestamp getCreateTimeStart()
            {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public Timestamp getCreateTimeEnd()
            {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public String getBidNo()
            {
                // TODO Auto-generated method stub
                return null;
            }
        }, new Paging()
        {
            
            @Override
            public int getSize()
            {
                // TODO Auto-generated method stub
                return 0;
            }
            
            @Override
            public int getCurrentPage()
            {
                // TODO Auto-generated method stub
                return 0;
            }
        });*/
        
    }
    
}
