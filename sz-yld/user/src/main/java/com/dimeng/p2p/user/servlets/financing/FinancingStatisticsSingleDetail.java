package com.dimeng.p2p.user.servlets.financing;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;

import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S70.entities.T7203;
import com.dimeng.p2p.modules.bid.user.service.LctjManage;
import com.dimeng.p2p.modules.bid.user.service.WdzqManage;
import com.dimeng.p2p.modules.bid.user.service.entity.InvestAmount;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.IntegerParser;

public class FinancingStatisticsSingleDetail extends AbstractFinancingServlet
{

	private static final long serialVersionUID = -7188989878381604866L;
	
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
        LctjManage service = serviceSession.getService(LctjManage.class);
        
        WdzqManage wdzqManage = serviceSession.getService(WdzqManage.class);
        
        Paging paging = new Paging()
        {
            @Override
            public int getCurrentPage()
            {
                return IntegerParser.parse(request.getParameter(PAGING_CURRENT));
            }
            
            @Override
            public int getSize()
            {
                return IntegerParser.parse(request.getParameter("pageSize"));
            }
        };
        String pageStr = null;
        int pageCount = 0;
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        String year = request.getParameter("year");
        String month = request.getParameter("month");
        String type = request.getParameter("type");
        if(!StringHelper.isEmpty(type)){
            if("ljtzje".equals(type)){
                PagingResult<InvestAmount> result = service.searchInvestAmountDetail(year,month,paging);
                pageStr = wdzqManage.rendPaging(result);
                pageCount = result.getPageCount();
                jsonMap.put("lctjSingleDetailList", result.getItems());
            }else{
                PagingResult<T7203> result = service.searchStatistics(year, month, type, paging);
                pageStr = wdzqManage.rendPaging(result);
                pageCount = result.getPageCount();
                jsonMap.put("lctjSingleDetailList", result.getItems());
            }
        }
        
        jsonMap.put("pageStr", pageStr);
        jsonMap.put("pageCount", pageCount);
        // 封装JSON
        ObjectMapper objectMapper = new ObjectMapper();
        PrintWriter out = response.getWriter();
        out.print(objectMapper.writeValueAsString(jsonMap));
        out.close();
    }

}
