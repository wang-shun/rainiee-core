package com.dimeng.p2p.user.servlets.credit;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;

import com.dimeng.framework.http.session.authentication.AuthenticationException;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S62.entities.T6231;
import com.dimeng.p2p.modules.bid.user.service.BidManage;
import com.dimeng.p2p.modules.bid.user.service.WdjkManage;
import com.dimeng.p2p.modules.bid.user.service.entity.HkEntity;
import com.dimeng.p2p.modules.bid.user.service.entity.LoanCount;
import com.dimeng.util.parser.IntegerParser;

/**
 * 还款中的借款查询
 *
 */
public class Repaying extends AbstractCreditServlet
{
    
    private static final long serialVersionUID = -4958563159932273865L;
    
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
        WdjkManage manage = serviceSession.getService(WdjkManage.class);
        BidManage bidManage = serviceSession.getService(BidManage.class);
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
                return IntegerParser.parse(request.getParameter("pageSize"));
            }
        };
        PrintWriter out = null;
        // 封装JSON
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        try
        {
            out = response.getWriter();
            PagingResult<HkEntity> resultList = manage.getHkzJk(paging);
            
            LoanCount loanCount = manage.getMyLoanCount();
            if (null != resultList)
            {
                HkEntity[] hkEntities = null;
                List<T6231> t6231List = new ArrayList<>();
                if (null != resultList.getItems())
                {
                    // hkEntities = new HkEntity[resultList.getItems().length];
                    hkEntities = resultList.getItems();
                    for (HkEntity hkEntity : hkEntities)
                    {
                        t6231List.add(bidManage.getExtra(hkEntity.F01));
                    }
                    if (t6231List.size() == 0)
                    {
                        t6231List = null;
                    }
                }
                String pageStr = rendPaging(resultList);
                jsonMap.put("pageStr", pageStr);
                jsonMap.put("pageCount", resultList.getPageCount());
                // 倒序一下,jsp页面append需要
                List<HkEntity> wdjkList = new ArrayList<HkEntity>();
                if (null != hkEntities && hkEntities.length > 0)
                {
                    wdjkList = Arrays.asList(hkEntities);
                    Collections.reverse(wdjkList);
                }
                jsonMap.put("wdjkList", wdjkList);
                jsonMap.put("t6231List", t6231List);
                jsonMap.put("loanCount", loanCount);
                //jsonMap.put("isYuqi", manage.isYuqi());
                
                out.print(objectMapper.writeValueAsString(jsonMap));
                out.close();
            }
        }
        catch (Throwable throwable)
        {
            logger.error("repaying error:" + throwable.toString(), throwable);
            try
            {
                out = response.getWriter();
            }
            catch (IOException e)
            {
                logger.error("repaying IOException:" + throwable.toString(), throwable);
            }
            
            if (null != out)
            {
                jsonMap.put("pageStr", null);
                jsonMap.put("pageCount", 0);
                jsonMap.put("wdjkList", null);
                jsonMap.put("t6231List", null);
                jsonMap.put("loanCount", null);
                try
                {
                    out.print(objectMapper.writeValueAsString(jsonMap));
                }
                catch (IOException e)
                {
                    logger.error("repaying  out.print error:" + throwable.toString(), throwable);
                }
                out.close();
            }
        }
    }
    
    @Override
    protected void onThrowable(HttpServletRequest request, HttpServletResponse response, Throwable throwable)
        throws ServletException, IOException
    {
        logger.error("repaying error:" + throwable.toString(), throwable);
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
