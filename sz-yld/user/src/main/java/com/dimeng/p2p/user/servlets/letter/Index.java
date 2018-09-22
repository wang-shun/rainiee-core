package com.dimeng.p2p.user.servlets.letter;

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
import com.dimeng.p2p.account.user.service.LetterManage;
import com.dimeng.p2p.account.user.service.entity.LetterEntity;
import com.dimeng.p2p.common.enums.LetterStatus;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.IntegerParser;

public class Index extends AbstractLetterServlet
{
    
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processPost(final HttpServletRequest request, HttpServletResponse response,
        ServiceSession serviceSession)
        throws Throwable
    {
        LetterManage manage = serviceSession.getService(LetterManage.class);
        String status = request.getParameter("status");
        LetterStatus letterStatus = null;
        if (!StringHelper.isEmpty(status))
        {
            letterStatus = LetterStatus.valueOf(status);
        }
        PagingResult<LetterEntity> letters = manage.search(letterStatus, new Paging()
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
        });
        String pageStr = rendPaging(letters);
        PrintWriter out = response.getWriter();
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        jsonMap.put("pageStr", pageStr);
        jsonMap.put("pageCount", letters.getPageCount());
        jsonMap.put("letterList", letters.getItems());
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
