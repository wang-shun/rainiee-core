package com.dimeng.p2p.user.servlets.financing;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;

import com.dimeng.framework.http.session.authentication.OtherLoginException;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.modules.bid.user.service.LctjManage;
import com.dimeng.p2p.modules.bid.user.service.entity.EarnFinancingInfo;
import com.dimeng.p2p.modules.bid.user.service.entity.EarnFinancingTotal;
import com.dimeng.util.parser.IntegerParser;

/**
 * 理财统计
 * 
 * @modify luoyi
 *
 */
public class FinancingStatistics extends AbstractFinancingServlet
{
    private static final long serialVersionUID = -6779136188061693736L;
    
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
        /*if("0".equals(searchType)){
            if(StringHelper.isEmpty(sYear)){
                
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(new Date());
                calendar.add(Calendar.MONTH, -9);
                sYear = DateParser.format(calendar.getTime(), "yyyy");
                sMonth = DateParser.format(calendar.getTime(), "MM");
            }
            
            if(StringHelper.isEmpty(eYear)){
                eYear = DateParser.format(new Date(), "yyyy");
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(new Date());
                eMonth = DateParser.format(new Date(), "MM");
            }
        }*/
        // 结果集
        PagingResult<EarnFinancingInfo> result = service.search(sYear, sMonth, eYear, eMonth, paging);
        EarnFinancingTotal total = service.getEarnFinancingTotal(sYear, sMonth, eYear, eMonth);
        
        // 封装JSON
        ObjectMapper objectMapper = new ObjectMapper();
        PrintWriter out = response.getWriter();
        String pageStr = rendPaging(result);
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        jsonMap.put("pageStr", pageStr);
        jsonMap.put("pageCount", result.getPageCount());
        jsonMap.put("lctjList", result.getItems());
        jsonMap.put("sYear", sYear);
        jsonMap.put("sMonth", sMonth);
        jsonMap.put("eYear", eYear);
        jsonMap.put("eMonth", eMonth);
        jsonMap.put("earnFinancingTotal", total);
        
        out.print(objectMapper.writeValueAsString(jsonMap));
        out.close();
    }
    
    @Override
    protected void onThrowable(HttpServletRequest request, HttpServletResponse response, Throwable throwable)
        throws ServletException, IOException
    {
        ObjectMapper objectMapper = new ObjectMapper();
        PrintWriter out = response.getWriter();
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        if (throwable instanceof OtherLoginException)
        {
            jsonMap.put("msg", throwable.getMessage());
            out.print(objectMapper.writeValueAsString(jsonMap));
        }
        out.close();
    }
}
