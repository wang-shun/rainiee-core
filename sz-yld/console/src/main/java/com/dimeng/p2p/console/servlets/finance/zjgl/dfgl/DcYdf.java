package com.dimeng.p2p.console.servlets.finance.zjgl.dfgl;

import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S51.enums.T5131_F02;
import com.dimeng.p2p.S62.enums.T6230_F10;
import com.dimeng.p2p.console.servlets.system.AbstractSystemServlet;
import com.dimeng.p2p.modules.finance.console.service.PtdfManage;
import com.dimeng.p2p.modules.finance.console.service.entity.DfRecord;
import com.dimeng.p2p.modules.finance.console.service.query.DfQuery;
import com.dimeng.util.parser.IntegerParser;

//@Right(id = "P2P_C_FINANCE_PTDF_EXPORT", name = "导出", moduleId="P2P_C_FINANCE_ZJGL_PTDFGL", order = 1)
public class DcYdf extends AbstractSystemServlet
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
        PtdfManage collectionManage = serviceSession.getService(PtdfManage.class);
        response.setHeader("Content-disposition",
            "attachment;filename=" + new Timestamp(System.currentTimeMillis()).getTime() + ".csv");
        response.setContentType("application/csv");
        PagingResult<DfRecord> dfRecord = collectionManage.ydfSearch(new DfQuery()
        {
            @Override
            public String getBidNo()
            {
                return request.getParameter("bidNo");
            }
            
            @Override
            public String getLoanTitle()
            {
                return request.getParameter("loanTitle");
            }
            
            @Override
            public String getLoanName()
            {
                return request.getParameter("loanName");
            }
            
            @Override
            public T6230_F10 getHkfs()
            {
                return T6230_F10.parse(request.getParameter("hkfs"));
            }
            
            @Override
            public int getYuqiFromDays()
            {
                return IntegerParser.parse(request.getParameter("yuqiFromDays"));
            }
            
            @Override
            public int getYuqiEndDays()
            {
                return IntegerParser.parse(request.getParameter("yuqiEndDays"));
            }
            
            @Override
            public T5131_F02 getDffs()
            {
                return T5131_F02.parse(request.getParameter("dffs"));
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
        collectionManage.exportYdf(dfRecord.getItems(), response.getOutputStream(), "");
    }
    
}
