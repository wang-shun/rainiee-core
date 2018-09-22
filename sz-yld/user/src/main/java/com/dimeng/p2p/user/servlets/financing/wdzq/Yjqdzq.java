package com.dimeng.p2p.user.servlets.financing.wdzq;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;

import com.dimeng.framework.http.servlet.annotation.PagingServlet;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.modules.bid.user.service.WdzqManage;
import com.dimeng.p2p.modules.bid.user.service.entity.ZqxxEntity;
import com.dimeng.p2p.user.servlets.financing.AbstractFinancingServlet;
import com.dimeng.util.parser.IntegerParser;

@PagingServlet(itemServlet = Yjqdzq.class)
public class Yjqdzq extends AbstractFinancingServlet
{
    
    private static final long serialVersionUID = 19842188160942532L;
    
    @Override
    protected void processPost(final HttpServletRequest request, HttpServletResponse response,
        ServiceSession serviceSession)
        throws Throwable
    {
        WdzqManage wdzqManage = serviceSession.getService(WdzqManage.class);
        //分页参数
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
        
        //得到查询结果
        PagingResult<ZqxxEntity> result = wdzqManage.getSettleAssests(paging);
        
        //封装JSON
        ObjectMapper objectMapper = new ObjectMapper();
        PrintWriter out = response.getWriter();
        
        String pageStr = this.rendPaging(result);
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        jsonMap.put("pageStr", pageStr);
        jsonMap.put("pageCount", result.getPageCount());
        jsonMap.put("yjqdzqList", result.getItems());
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
        {
            jsonMap.put("msg", throwable.getMessage());
            out.print(objectMapper.writeValueAsString(jsonMap));
        }
        out.close();
    }
}
