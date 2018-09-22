package com.dimeng.p2p.console.servlets.info.xxpl.sjbg;

import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S50.enums.T5011_F02;
import com.dimeng.p2p.common.enums.ArticlePublishStatus;
import com.dimeng.p2p.console.servlets.info.xxpl.AbstractXxplServlet;
import com.dimeng.p2p.modules.base.console.service.ArticleManage;
import com.dimeng.p2p.modules.base.console.service.entity.OperateReport;
import com.dimeng.p2p.modules.base.console.service.query.OperateReportQuery;
import com.dimeng.util.parser.EnumParser;
import com.dimeng.util.parser.IntegerParser;
import com.dimeng.util.parser.TimestampParser;

/**
 * 
 * <审计报告>
 * <列表>
 * 
 * @author  liulixia
 * @version  [版本号, 2018年2月6日]
 */
@Right(id = "P2P_C_INFO_XXPL_MENU", name = "信息披露", moduleId = "P2P_C_INFO_XXPL", order = 0)
public class SearchSjbg extends AbstractXxplServlet
{
    
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        processGet(request, response, serviceSession);
    }
    
    @Override
    protected void processGet(final HttpServletRequest request, HttpServletResponse response,
        ServiceSession serviceSession)
        throws Throwable
    {
        ArticleManage manage = serviceSession.getService(ArticleManage.class);
        OperateReportQuery query = new OperateReportQuery()
        {
            
            @Override
            public T5011_F02 getType()
            {
                return T5011_F02.SJBG;
            }
            
            @Override
            public String getTitle()
            {
                return request.getParameter("title");
            }
            
            @Override
            public String getPublisherName()
            {
                return request.getParameter("publisherName");
            }
            
            @Override
            public ArticlePublishStatus getPublishStatus()
            {
                return EnumParser.parse(ArticlePublishStatus.class, request.getParameter("status"));
            }

			@Override
			public Timestamp getLastUpdateTimeStart() {
				return TimestampParser.parse(request
						.getParameter("lastUpdateTimeStart"));
			}

			@Override
			public Timestamp getLastUpdateTimeEnd() {
				return TimestampParser.parse(request
						.getParameter("lastUpdateTimeEnd"));
			}
            
        };
        Paging paging = new Paging()
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
        };
        
        PagingResult<OperateReport> result = manage.searchOperateReport(query, paging);
        request.setAttribute("result", result);
        forwardView(request, response, getClass());
        
    }
    
}
