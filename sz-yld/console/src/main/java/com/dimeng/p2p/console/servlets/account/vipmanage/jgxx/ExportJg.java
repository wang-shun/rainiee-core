package com.dimeng.p2p.console.servlets.account.vipmanage.jgxx;

import java.sql.Timestamp;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S61.enums.T6110_F07;
import com.dimeng.p2p.S61.enums.T6110_F17;
import com.dimeng.p2p.S61.enums.T6110_F19;
import com.dimeng.p2p.console.servlets.account.AbstractAccountServlet;
import com.dimeng.p2p.modules.account.console.service.OrganizationManage;
import com.dimeng.p2p.modules.account.console.service.entity.Organization;
import com.dimeng.p2p.modules.account.console.service.query.EnterpriseQuery;
import com.dimeng.util.parser.DateParser;
import com.dimeng.util.parser.EnumParser;
import com.dimeng.util.parser.IntegerParser;

@Right(id = "P2P_C_ACCOUNT_QYEXPORT", name = "导出", moduleId = "P2P_C_ACCOUNT_JGXX", order = 5)
public class ExportJg extends AbstractAccountServlet
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
        OrganizationManage manager = serviceSession.getService(OrganizationManage.class);
        response.setHeader("Content-disposition",
            "attachment;filename=" + new Timestamp(System.currentTimeMillis()).getTime() + ".csv");
        response.setContentType("application/csv");
        PagingResult<Organization> list = manager.enterpriseSearch(new EnterpriseQuery()
        {
            
            @Override
            public String getUserId()
            {
                return null;
            }
            
            @Override
            public String getName()
            {
                return request.getParameter("userName");
            }
            
            @Override
            public String getContacts()
            {
                return request.getParameter("realName");
            }
            
            @Override
            public String getContactsMobile()
            {
                return request.getParameter("msisdn");
            }
            
            @Override
            public String getLicenseNumber()
            {
                return null;
            }
            
            @Override
            public String getOrganizationNumber()
            {
                return null;
            }
            
            @Override
            public String getDutyNumber()
            {
                return null;
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
            public T6110_F07 getUserState()
            {
                return EnumParser.parse(T6110_F07.class, request.getParameter("userState"));
            }
            
            @Override
            public T6110_F17 getInvestType()
            {
                return EnumParser.parse(T6110_F17.class, request.getParameter("investorType"));
            }
            
            @Override
            public String getAccount()
            {
                return request.getParameter("account");
            }
            
            @Override
            public T6110_F19 getBadClaimType()
            {
                return EnumParser.parse(T6110_F19.class, request.getParameter("badClaimType"));
            }
            
        }, new Paging()
        {
            
            @Override
            public int getSize()
            {
                return Integer.MAX_VALUE;
            }
            
            @Override
            public int getCurrentPage()
            {
                return IntegerParser.parse(request.getParameter(PAGING_CURRENT));
            }
        });
        
        manager.export(list.getItems(), response.getOutputStream(), "");
    }
    
}
