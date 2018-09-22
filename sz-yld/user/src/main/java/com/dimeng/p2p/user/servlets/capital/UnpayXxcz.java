package com.dimeng.p2p.user.servlets.capital;

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
import com.dimeng.p2p.account.user.service.TxManage;
import com.dimeng.p2p.account.user.service.entity.OrderXxcz;
import com.dimeng.util.parser.IntegerParser;

public class UnpayXxcz extends AbstractCapitalServlet
{
    
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processPost(final HttpServletRequest request, HttpServletResponse response,
        ServiceSession serviceSession)
        throws Throwable
    {
        TxManage manage = serviceSession.getService(TxManage.class);
        //分页参数
        Paging paging = new Paging()
        {
            @Override
            public int getSize()
            {
                return IntegerParser.parse(request.getParameter("pageSize"));
            }
            
            @Override
            public int getCurrentPage()
            {
                return IntegerParser.parse(request.getParameter("currentPage"));
            }
        };
        PagingResult<OrderXxcz> pagingOrder = manage.searchXxcz(paging);
        
        //封装JSON
        ObjectMapper objectMapper = new ObjectMapper();
        PrintWriter out = response.getWriter();
        String pageStr = this.rendPaging(pagingOrder);
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        jsonMap.put("pageStr", pageStr);
        jsonMap.put("pageCount", pagingOrder.getPageCount());
        jsonMap.put("orderList", pagingOrder.getItems());
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
