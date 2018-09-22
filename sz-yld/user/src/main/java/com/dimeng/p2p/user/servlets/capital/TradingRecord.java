package com.dimeng.p2p.user.servlets.capital;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
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
import com.dimeng.p2p.S51.entities.T5122;
import com.dimeng.p2p.S61.entities.T6110;
import com.dimeng.p2p.S61.enums.T6101_F03;
import com.dimeng.p2p.account.user.service.UserInfoManage;
import com.dimeng.p2p.account.user.service.ZjlsManage;
import com.dimeng.p2p.account.user.service.entity.CapitalLs;
import com.dimeng.p2p.modules.base.front.service.TradeTypeManage;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.DateParser;
import com.dimeng.util.parser.IntegerParser;

public class TradingRecord extends AbstractCapitalServlet
{
    
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processPost(final HttpServletRequest request, HttpServletResponse response,
        ServiceSession serviceSession)
        throws Throwable
    {
        ZjlsManage manage = serviceSession.getService(ZjlsManage.class);
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
        int tradingType = IntegerParser.parse(request.getParameter("tradingType"));
        String startTime = request.getParameter("startTime");
        String endTime = request.getParameter("endTime");
        String zhlx =
            StringHelper.isEmpty(request.getParameter("accountType")) ? "WLZH" : request.getParameter("accountType");
        Date sTime = DateParser.parse(startTime);
        Date eTime = DateParser.parse(endTime);
        T6101_F03 f03 = null;
        if (!StringHelper.isEmpty(zhlx))
        {
            f03 = T6101_F03.valueOf(zhlx);
        }
        else
        {
            f03 = T6101_F03.WLZH;
        }
        
        PagingResult<CapitalLs> tradingRecords = manage.searchLs(tradingType, sTime, eTime, f03, paging);
        
        //封装JSON
        ObjectMapper objectMapper = new ObjectMapper();
        PrintWriter out = response.getWriter();
        String pageStr = this.rendPaging(tradingRecords);
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        jsonMap.put("pageStr", pageStr);
        jsonMap.put("pageCount", tradingRecords.getPageCount());
        jsonMap.put("curPage", request.getParameter("currentPage"));
        jsonMap.put("pageSize", request.getParameter("pageSize"));
        jsonMap.put("tradingRecords", tradingRecords.getItems());
        out.print(objectMapper.writeValueAsString(jsonMap));
        out.close();
    }
    
    @Override
    protected void processGet(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        TradeTypeManage tradeTypeManage = serviceSession.getService(TradeTypeManage.class);
        UserInfoManage uManage = serviceSession.getService(UserInfoManage.class);
        T6110 t6110 = uManage.getUserInfo(serviceSession.getSession().getAccountId());
        T5122[] t5122s = tradeTypeManage.search(t6110.F06, t6110.F10);
        //封装JSON
        ObjectMapper objectMapper = new ObjectMapper();
        PrintWriter out = response.getWriter();
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        jsonMap.put("tradingTypes", t5122s);
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
