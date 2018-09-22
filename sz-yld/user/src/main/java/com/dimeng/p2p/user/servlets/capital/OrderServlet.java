package com.dimeng.p2p.user.servlets.capital;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
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
import com.dimeng.p2p.S65.entities.T6501;
import com.dimeng.p2p.account.user.service.OrderManage;
import com.dimeng.util.parser.IntegerParser;
import com.dimeng.util.parser.TimestampParser;

public class OrderServlet extends AbstractCapitalServlet
{
    
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processPost(final HttpServletRequest request, HttpServletResponse response,
        ServiceSession serviceSession)
        throws Throwable
    {
        OrderManage service = serviceSession.getService(OrderManage.class);
        Paging paging = new Paging()
        {
            @Override
            public int getCurrentPage()
            {
                return IntegerParser.parse(request.getParameter("currentPage"));
            }
            
            @Override
            public int getSize()
            {
                return IntegerParser.parse(request.getParameter("pageSize"));
            }
        };
        int type = IntegerParser.parse(request.getParameter("orderType"));
        Timestamp startTime = TimestampParser.parse(request.getParameter("startTime"));
        Timestamp endTime = TimestampParser.parse(request.getParameter("endTime"));
        PagingResult<T6501> orderList = service.search(type, startTime, endTime, paging);
        
        //封装JSON
        ObjectMapper objectMapper = new ObjectMapper();
        PrintWriter out = response.getWriter();
        String pageStr = this.rendPaging(orderList);
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        jsonMap.put("pageStr", pageStr);
        jsonMap.put("pageCount", orderList.getPageCount());
        jsonMap.put("orderList", orderList.getItems());
        out.print(objectMapper.writeValueAsString(jsonMap));
        out.close();
    }
    
    @Override
    protected void processGet(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        processPost(request, response, serviceSession);
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
