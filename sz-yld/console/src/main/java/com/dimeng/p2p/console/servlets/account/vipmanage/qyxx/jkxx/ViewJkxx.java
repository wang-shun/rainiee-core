package com.dimeng.p2p.console.servlets.account.vipmanage.qyxx.jkxx;

import java.sql.Timestamp;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S62.enums.T6230_F20;
import com.dimeng.p2p.console.servlets.account.AbstractAccountServlet;
import com.dimeng.p2p.modules.account.console.service.QyManage;
import com.dimeng.p2p.modules.account.console.service.entity.LoanRecord;
import com.dimeng.p2p.modules.account.console.service.entity.XyrzTotal;
import com.dimeng.p2p.modules.account.console.service.query.LoanRecordQuery;
import com.dimeng.util.parser.DateParser;
import com.dimeng.util.parser.EnumParser;
import com.dimeng.util.parser.IntegerParser;

@Right(id = "P2P_C_ACCOUNT_QYXX_VIEW", name = "查看", moduleId = "P2P_C_ACCOUNT_QYXX", order = 1)
public class ViewJkxx extends AbstractAccountServlet
{
    
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processGet(final HttpServletRequest request, HttpServletResponse response,
        ServiceSession serviceSession)
        throws Throwable
    {
        processPost(request, response, serviceSession);
    }
    
    @Override
    protected void processPost(final HttpServletRequest request, HttpServletResponse response,
        ServiceSession serviceSession)
        throws Throwable
    {
        QyManage manage = serviceSession.getService(QyManage.class);
        int id = IntegerParser.parse(request.getParameter("id"));
        PagingResult<LoanRecord> loanRecords = manage.getJkxx(new LoanRecordQuery()
        {
            
            @Override
            public T6230_F20 getState()
            {
                
                return EnumParser.parse(T6230_F20.class, request.getParameter("state"));
            }
            
            @Override
            public int getUserId()
            {
                return IntegerParser.parse(request.getParameter("id"));
            }
            
            @Override
            public String getLoanRecordTitle()
            {
                return request.getParameter("loanRecordTitle");
            }
            
            @Override
            public String getLoanRecordId()
            {
                return request.getParameter("loanRecordId");
            }
            
            @Override
            public Timestamp getCreateTimeStart()
            {
                Date date = DateParser.parse(request.getParameter("createTimeStart"));
                return date == null ? null : new Timestamp(date.getTime());
            }
            
            @Override
            public Timestamp getCreateTimeEnd()
            {
                Date date = DateParser.parse(request.getParameter("createTimeEnd"));
                return date == null ? null : new Timestamp(date.getTime());
            }
            
            @Override
            public String getLoanNum()
            {
                return request.getParameter("loanNum");
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
        XyrzTotal xyrzTotal = manage.getXyrzTotal(id);
        request.setAttribute("xyrzTotal", xyrzTotal);
        request.setAttribute("info", loanRecords);
        forwardView(request, response, getClass());
    }
    
}
