package com.dimeng.p2p.user.servlets.credit;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;

import com.dimeng.framework.http.session.authentication.AuthenticationException;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.repeater.claim.SubscribeBadClaimManage;
import com.dimeng.p2p.repeater.claim.entity.YZRLoanEntity;
import com.dimeng.p2p.user.servlets.AbstractUserBadClaimServlet;
import com.dimeng.util.parser.IntegerParser;

/**
 * 分页查询已转让的借款信息
 *
 */
public class AlreadyTransferLoan extends AbstractUserBadClaimServlet
{
    private static final long serialVersionUID = -97427727135165216L;
    
    @Override
    protected void processGet(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        processPost(request, response, serviceSession);
    }
    
    @Override
    protected void processPost(final HttpServletRequest request, HttpServletResponse response,
        ServiceSession serviceSession)
    {
        SubscribeBadClaimManage manage = serviceSession.getService(SubscribeBadClaimManage.class);
        // 分页参数
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
                return 10;
            }
        };
        PrintWriter out = null;
        try
        {
            out = response.getWriter();
            Map<String, Object> jsonMap = new HashMap<String, Object>();
            
            PagingResult<YZRLoanEntity> resultList = manage.getAlreadyTransferLoan(paging);
            // 封装JSON
            ObjectMapper objectMapper = new ObjectMapper();
            
            if (null != resultList)
            {
                
                String pageStr = rendPaging(resultList);
                jsonMap.put("pageStr", pageStr);
                jsonMap.put("pageCount", resultList.getPageCount());
                jsonMap.put("yzrList", resultList.getItems());
            }
            out.print(objectMapper.writeValueAsString(jsonMap));
            out.close();
        }
        catch (Throwable throwable)
        {
            logger.error("AlreadyTransferLoan error:" + throwable.toString(), throwable);
            if (null != out)
            {
                out.close();
            }
        }
        
    }
    
    @Override
    protected void onThrowable(HttpServletRequest request, HttpServletResponse response, Throwable throwable)
        throws ServletException, IOException
    {
        logger.error("AlreadyTransferLoan error:" + throwable.toString(), throwable);
        if (throwable instanceof AuthenticationException)
        {
            PrintWriter out = response.getWriter();
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> jsonMap = new HashMap<String, Object>();
            jsonMap.put("loginTimeOut", "未登录或会话超时,请重新登录");
            out.print(objectMapper.writeValueAsString(jsonMap));
            out.close();
        }
    }
    
}
