package com.dimeng.p2p.console.servlets.bid.csgl.hmd;

import java.sql.Timestamp;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.console.servlets.system.AbstractSystemServlet;
import com.dimeng.p2p.modules.bid.console.service.CollectionManage;
import com.dimeng.p2p.modules.bid.console.service.entity.BlacklistInfo;
import com.dimeng.p2p.modules.bid.console.service.query.BlacklistQuery;
import com.dimeng.util.parser.DateParser;
import com.dimeng.util.parser.IntegerParser;

@Right(id = "P2P_C_BUSI_CS_DCHMD", name = "导出",moduleId="P2P_C_BID_CSGL_HMD",order=2)
public class DcHmd extends AbstractSystemServlet
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
        CollectionManage collectionManage = serviceSession.getService(CollectionManage.class);
        response.setHeader("Content-disposition",
            "attachment;filename=" + new Timestamp(System.currentTimeMillis()).getTime() + ".csv");
        response.setContentType("application/csv");
        PagingResult<BlacklistInfo> blacklistInfos = collectionManage.blacklistSearch(new BlacklistQuery()
        {
            
            @Override
            public String getUserName()
            {
                return request.getParameter("userName");
            }
            
            @Override
            public String getMsisdn()
            {
                return request.getParameter("msisdn");
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
            public String getRealName()
            {
                return request.getParameter("realName");
            }
            
            @Override
            public String getIdCard()
            {
                return request.getParameter("idCard");
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
        collectionManage.exportBlacklist(blacklistInfos.getItems(), response.getOutputStream(), "");
    }
    
}
