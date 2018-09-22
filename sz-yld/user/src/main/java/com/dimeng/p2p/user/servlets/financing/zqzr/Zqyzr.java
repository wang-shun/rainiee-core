package com.dimeng.p2p.user.servlets.financing.zqzr;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;

import com.dimeng.framework.http.session.authentication.OtherLoginException;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S62.entities.T6231;
import com.dimeng.p2p.modules.bid.user.service.BidManage;
import com.dimeng.p2p.modules.bid.user.service.ZqzrManage;
import com.dimeng.p2p.modules.bid.user.service.entity.Bdxq;
import com.dimeng.p2p.modules.bid.user.service.entity.InSellFinacing;
import com.dimeng.p2p.user.servlets.financing.AbstractFinancingServlet;
import com.dimeng.util.parser.IntegerParser;

public class Zqyzr extends AbstractFinancingServlet
{
    
    /**
     * 
     */
    private static final long serialVersionUID = -412483371440734996L;
    
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
        BidManage bidManage = serviceSession.getService(BidManage.class);
        //结果集
        PagingResult<InSellFinacing> isfList = service.getInSellFinacing(paging);
        //封装JSON
        ObjectMapper objectMapper = new ObjectMapper();
        PrintWriter out = response.getWriter();
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        
        List<T6231> t6231List = new ArrayList<T6231>();
        List<Bdxq> bdxqList = new ArrayList<Bdxq>();
        
        if (null != isfList && isfList.getSize() > 0)
        {
            String pageStr = rendPaging(isfList);
            jsonMap.put("pageStr", pageStr);
            jsonMap.put("pageCount", isfList.getPageCount());
            jsonMap.put("zqyzrList", isfList.getItems());
            
            //需要根据isf.jkbId取得bidManage.getExtra(isf.jkbId).F03 和bidManage.getExtra(isf.jkbId).F02
            if (null != isfList.getItems())
            {
                for (InSellFinacing isf : isfList.getItems())
                {
                    T6231 t6231 = bidManage.getExtra(isf.jkbId);
                    t6231List.add(t6231);
                    Bdxq bdxq = bidManage.get(isf.jkbId);
                    bdxqList.add(bdxq);
                }
                jsonMap.put("t6231List", t6231List);
                jsonMap.put("bdxqList", bdxqList);
            }
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
