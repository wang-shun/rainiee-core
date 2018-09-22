package com.dimeng.p2p.user.servlets.financing;

import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S70.entities.T7203;
import com.dimeng.p2p.modules.bid.user.service.LctjManage;
import com.dimeng.p2p.modules.bid.user.service.WdzqManage;
import com.dimeng.util.parser.IntegerParser;

import org.codehaus.jackson.map.ObjectMapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class FinancingStatisticsDetail extends AbstractFinancingServlet
{
    
    private static final long serialVersionUID = 793858856102351827L;
    
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
        String sYear = request.getParameter("sYear");
        String sMonth = request.getParameter("sMonth");
        String eYear = request.getParameter("eYear");
        String eMonth = request.getParameter("eMonth");
        //String searchType = request.getParameter("searchType");
        /*if ("0".equals(searchType))
        {
            if (StringHelper.isEmpty(sYear))
            {
                
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(new Date());
                calendar.add(Calendar.MONTH, -9);
                sYear = DateParser.format(calendar.getTime(), "yyyy");
                sMonth = DateParser.format(calendar.getTime(), "MM");
            }
            
            if (StringHelper.isEmpty(eYear))
            {
                eYear = DateParser.format(new Date(), "yyyy");
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(new Date());
                eMonth = DateParser.format(new Date(), "MM");
            }
        }*/
        PagingResult<T7203> result = service.searchDetail(sYear, sMonth, eYear, eMonth, paging);
        // 封装JSON
        ObjectMapper objectMapper = new ObjectMapper();
        PrintWriter out = response.getWriter();
        String pageStr = wdzqManage.rendPaging(result);
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        jsonMap.put("pageStr", pageStr);
        jsonMap.put("pageCount", result.getPageCount());
        jsonMap.put("lctjDetailList", result.getItems());
        jsonMap.put("sYear", sYear);
        jsonMap.put("sMonth", sMonth);
        jsonMap.put("eYear", eYear);
        jsonMap.put("eMonth", eMonth);
        
        out.print(objectMapper.writeValueAsString(jsonMap));
        out.close();
    }
    
}
