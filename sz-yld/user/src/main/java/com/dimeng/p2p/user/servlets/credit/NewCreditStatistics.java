package com.dimeng.p2p.user.servlets.credit;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;

import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.modules.bid.user.service.JktjManage;
import com.dimeng.p2p.modules.bid.user.service.entity.NewCreditList;
import com.dimeng.p2p.modules.bid.user.service.query.NewCreditListQuery;
import com.dimeng.util.parser.IntegerParser;

public class NewCreditStatistics extends AbstractCreditServlet
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
        JktjManage manage = serviceSession.getService(JktjManage.class);
        
        NewCreditListQuery query = new NewCreditListQuery()
        {
            
            @Override
            public int getYear()
            {
                return IntegerParser.parse(request.getParameter("year"));
            }
            
            @Override
            public int getMonth()
            {
                return IntegerParser.parse(request.getParameter("month"));
            }
            
        };
        PagingResult<NewCreditList> result = manage.getNewCreditList(query, new Paging()
        {
            
            @Override
            public int getSize()
            {
                return IntegerParser.parse(request.getParameter("pageSize"));
            }
            
            @Override
            public int getCurrentPage()
            {
                return IntegerParser.parse(request.getParameter(PAGING_CURRENT));
            }
        });
        
        // 封装JSON
        ObjectMapper objectMapper = new ObjectMapper();
        PrintWriter out = response.getWriter();
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        String pageStr = rendPaging(result);
        jsonMap.put("pageStr", pageStr);
        jsonMap.put("pageCount", result.getPageCount());
        jsonMap.put("jktjList", result.getItems());
        jsonMap.put("year", query.getYear());
        jsonMap.put("month", query.getMonth());
        jsonMap.put("jktjTotal", manage.getNewCreditTotal(null));
        
        out.print(objectMapper.writeValueAsString(jsonMap));
        out.close();
    }
}
