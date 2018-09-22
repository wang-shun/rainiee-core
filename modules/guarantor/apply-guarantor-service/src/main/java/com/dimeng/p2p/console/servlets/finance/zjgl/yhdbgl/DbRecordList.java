package com.dimeng.p2p.console.servlets.finance.zjgl.yhdbgl;

import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S51.entities.T5122;
import com.dimeng.p2p.console.servlets.finance.AbstractGuarantorServlet;
import com.dimeng.p2p.repeater.guarantor.ApplyGuarantorManage;
import com.dimeng.p2p.repeater.guarantor.entity.DbRecordEntity;
import com.dimeng.p2p.repeater.guarantor.query.DbRecordQuery;
import com.dimeng.util.parser.IntegerParser;
import com.dimeng.util.parser.TimestampParser;

@Right(id = "P2P_C_FINANCE_DBRECORDLIST", name = "额度明细", moduleId = "P2P_C_FINANCE_ZJGL_YHDBGL", order = 5)
public class DbRecordList extends AbstractGuarantorServlet
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
        ApplyGuarantorManage manage = serviceSession.getService(ApplyGuarantorManage.class);
        int id = IntegerParser.parse(request.getParameter("id"));
        T5122[] t5122s = manage.searchTypes();
        PagingResult<DbRecordEntity> result = manage.searchAmountTrandRecords(new DbRecordQuery()
        {
            
            @Override
            public int getType()
            {
                return IntegerParser.parse(request.getParameter("type"));
            }
            
            @Override
            public Timestamp getStartPayTime()
            {
                return TimestampParser.parse(request.getParameter("startPayTime"));
            }
            
            @Override
            public Timestamp getEndPayTime()
            {
                return TimestampParser.parse(request.getParameter("endPayTime") + "23:59:59");
            }
            
            @Override
            public int getUserId()
            {
                return IntegerParser.parse(request.getParameter("id"));
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
        request.setAttribute("id", id);
        request.setAttribute("result", result);
        request.setAttribute("tradeTypes", t5122s);
        forwardView(request, response, getClass());
    }
    
}
