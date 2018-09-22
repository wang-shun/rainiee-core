package com.dimeng.p2p.console.servlets.bid.htgl.agreementSave;

import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.repeater.preservation.AgreementSaveManage;
import com.dimeng.p2p.repeater.preservation.entity.AgreementSave;
import com.dimeng.p2p.repeater.preservation.query.AgreementSaveQuery;
import com.dimeng.util.parser.IntegerParser;
import com.dimeng.util.parser.TimestampParser;

@Right(id = "P2P_C_BID_HTGL_XYBQ_LIST", isMenu = true, name = "协议保全列表", moduleId = "P2P_C_BID_HTGL_XYBQLB", order = 0)
public class AgreementSaveList extends AbstractPreserveServlet
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
        AgreementSaveManage agreementSaveManage = serviceSession.getService(AgreementSaveManage.class);
        AgreementSaveQuery query = new AgreementSaveQuery()
        {
            
            @Override
            public String getUserName()
            {
                return request.getParameter("userName");
            }
            
            @Override
            public String getName()
            {
                return request.getParameter("name");
            }
            
            @Override
            public Timestamp getAgreementTimeStart()
            {
                return TimestampParser.parse(request.getParameter("agreementTimeStart"));
            }
            
            @Override
            public Timestamp getAgreementTimeEnd()
            {
                return TimestampParser.parse(request.getParameter("agreementTimeEnd"));
            }
            
            @Override
            public String getAgreementState()
            {
                return request.getParameter("agreementState");
            }
            
            @Override
            public String getAgreementNum()
            {
                return request.getParameter("agreementNum");
            }
            
            @Override
            public String getAgreementId()
            {
                return request.getParameter("agreementId");
            }
        };
        PagingResult<AgreementSave> result = agreementSaveManage.searchAgreementSaveList(query, new Paging()
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
        forwardView(request, response, getClass());
    }
}
