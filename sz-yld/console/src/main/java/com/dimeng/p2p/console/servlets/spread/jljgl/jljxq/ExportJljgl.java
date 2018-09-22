package com.dimeng.p2p.console.servlets.spread.jljgl.jljxq;

import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.console.servlets.spread.AbstractSpreadServlet;
import com.dimeng.p2p.modules.spread.console.service.SpreadManage;
import com.dimeng.p2p.modules.spread.console.service.entity.BonusList;
import com.dimeng.p2p.modules.spread.console.service.entity.BonusQuery;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.IntegerParser;
import com.dimeng.util.parser.TimestampParser;

@Right(id = "P2P_C_SPREAD_JLJGL_JLJGLXQ_EXPORT", name = "导出奖励金详情列表", moduleId = "P2P_C_SPREAD_JLJGL_JLJXQ")
public class ExportJljgl extends AbstractSpreadServlet
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
        
        SpreadManage manage = serviceSession.getService(SpreadManage.class);
        Paging paging = new Paging()
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
        };
        
        BonusQuery query = new BonusQuery()
        {
            
            @Override
            public String loanID()
            {
                return request.getParameter("loanID");
            }
            
            @Override
            public String loanUserName()
            {
                return request.getParameter("loanUserName");
            }
            
            @Override
            public String investUserName()
            {
                return request.getParameter("investUserName");
            }
            
            @Override
            public Timestamp fkStartTime()
            {
                return TimestampParser.parse(request.getParameter("fkStartTime"));
            }
            
            @Override
            public Timestamp fkEndTime()
            {
                return TimestampParser.parse(StringHelper.isEmpty(request.getParameter("fkEndTime")) ? null
                    : request.getParameter("fkEndTime").concat(" 23:59:59"));
            }
        };
        response.setHeader("Content-disposition",
            "attachment;filename=" + new Timestamp(System.currentTimeMillis()).getTime() + ".csv");
        response.setContentType("application/csv");
        PagingResult<BonusList> result = manage.getBonusList(query, paging);
        
        manage.export(result.getItems(), response.getOutputStream());
    }
    
}
