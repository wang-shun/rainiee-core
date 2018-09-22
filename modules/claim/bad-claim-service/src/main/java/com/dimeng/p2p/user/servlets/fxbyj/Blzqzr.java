package com.dimeng.p2p.user.servlets.fxbyj;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;

import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S61.entities.T6110;
import com.dimeng.p2p.S61.enums.T6110_F19;
import com.dimeng.p2p.repeater.claim.SubscribeBadClaimManage;
import com.dimeng.p2p.repeater.claim.entity.SubscribeBadClaim;
import com.dimeng.p2p.service.UserInfoManage;
import com.dimeng.p2p.user.servlets.AbstractUserBadClaimServlet;
import com.dimeng.util.parser.IntegerParser;

/**
 * 
 * 不良债权转让servlet
 * 
 * @author  huqinfu
 * @version  [版本号, 2016年6月14日]
 */
public class Blzqzr extends AbstractUserBadClaimServlet
{
    
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processPost(final HttpServletRequest request, HttpServletResponse response,
        ServiceSession serviceSession)
        throws Throwable
    {
        SubscribeBadClaimManage service = serviceSession.getService(SubscribeBadClaimManage.class);
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
        
        String type = request.getParameter("type");
        PagingResult<SubscribeBadClaim> result = null;
        if ("yrgzq".equals(type))
        {
            result = service.getAlreadyBuyBadClaimList(paging);
        }
        else
        {
            UserInfoManage uManage = serviceSession.getService(UserInfoManage.class);
            T6110 t6110 = uManage.getUserInfo(serviceSession.getSession().getAccountId());
            if (T6110_F19.S == t6110.F19)
            {
                result = service.getCanBuyBadClaimList(paging);
            }
        }
        ObjectMapper objectMapper = new ObjectMapper();
        PrintWriter out = response.getWriter();
        String pageStr = this.rendPaging(result);
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        jsonMap.put("pageStr", pageStr);
        jsonMap.put("pageCount", result == null ? null : result.getPageCount());
        jsonMap.put("blzqzrList", result == null ? null : result.getItems());
        out.print(objectMapper.writeValueAsString(jsonMap));
        out.close();
    }
    
    @Override
    protected void onThrowable(HttpServletRequest request, HttpServletResponse response, Throwable throwable)
        throws ServletException, IOException
    {
        super.onThrowable(request, response, throwable);
    }
    
}
