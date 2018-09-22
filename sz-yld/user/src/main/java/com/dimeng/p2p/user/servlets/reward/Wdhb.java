package com.dimeng.p2p.user.servlets.reward;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
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
import com.dimeng.p2p.S63.enums.T6340_F03;
import com.dimeng.p2p.S63.enums.T6342_F04;
import com.dimeng.p2p.account.user.service.MyRewardManage;
import com.dimeng.p2p.account.user.service.entity.MyRewardRecod;
import com.dimeng.util.parser.IntegerParser;

public class Wdhb extends AbstractRewardServlet
{
    
    private static final long serialVersionUID = -2632876248730429710L;
    
    @Override
    protected void processPost(final HttpServletRequest request, HttpServletResponse response,
        ServiceSession serviceSession)
        throws Throwable
    {
        MyRewardManage service = serviceSession.getService(MyRewardManage.class);
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
        // 参数
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("type", T6340_F03.redpacket);
        params.put("status", request.getParameter("status"));
        PagingResult<MyRewardRecod> reslut = service.searchMyReward(params, paging);
        params.put("useStatus", T6342_F04.WSY);
        // 未使用红包数
        BigDecimal unUserHbAmount = service.getHbAmount(params);
        params.put("useStatus", T6342_F04.YSY);
        // 已使用红包数
        BigDecimal useredHbAmount = service.getHbAmount(params);
        //合计
        params.put("useStatus", request.getParameter("status"));
        BigDecimal totalAmount = service.getHbAmount(params);
        //封装JSON
        ObjectMapper objectMapper = new ObjectMapper();
        PrintWriter out = response.getWriter();
        String pageStr = rendPaging(reslut);
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        jsonMap.put("pageStr", pageStr);
        jsonMap.put("pageCount", reslut.getPageCount());
        jsonMap.put("retList", reslut.getItems());
        jsonMap.put("unUserHbAmount", unUserHbAmount);
        jsonMap.put("useredHbAmount", useredHbAmount);
        jsonMap.put("totalAmount", totalAmount);
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
