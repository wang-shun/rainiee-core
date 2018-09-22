package com.dimeng.p2p.user.servlets.financing.zqzr;

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
import com.dimeng.p2p.modules.bid.user.service.ZqzrManage;
import com.dimeng.p2p.modules.bid.user.service.entity.OutSellFinacing;
import com.dimeng.p2p.user.servlets.financing.AbstractFinancingServlet;
import com.dimeng.util.parser.IntegerParser;

public class Zqyzc extends AbstractFinancingServlet
{
    
    /**
     * 
     */
    private static final long serialVersionUID = -412483371440734996L;
    
    @Override
    protected void processGet(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        this.processPost(request, response, serviceSession);
    }
    
    @Override
    protected void processPost(final HttpServletRequest request, HttpServletResponse response,
        ServiceSession serviceSession)
        throws Throwable
    {
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
        ZqzrManage service = serviceSession.getService(ZqzrManage.class);
        PagingResult<OutSellFinacing> osfList = service.getOutSellFinacing(paging);
        
        //封装JSON
        ObjectMapper objectMapper = new ObjectMapper();
        PrintWriter out = response.getWriter();
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        
        if (null != osfList && osfList.getSize() > 0)
        {
            String pageStr = rendPaging(osfList);
            jsonMap.put("pageStr", pageStr);
            jsonMap.put("pageCount", osfList.getPageCount());
            jsonMap.put("zqyzcList", osfList.getItems());
        }
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
