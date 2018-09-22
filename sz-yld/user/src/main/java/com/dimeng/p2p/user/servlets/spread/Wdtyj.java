package com.dimeng.p2p.user.servlets.spread;

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
import com.dimeng.p2p.account.user.service.MyExperienceManage;
import com.dimeng.p2p.account.user.service.entity.MyExperienceRecod;
import com.dimeng.util.Formater;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.IntegerParser;

public class Wdtyj extends AbstractSpreadServlet
{
    
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processPost(final HttpServletRequest request, HttpServletResponse response,
        ServiceSession serviceSession)
        throws Throwable
    {
        MyExperienceManage service = serviceSession.getService(MyExperienceManage.class);
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
        if (!StringHelper.isEmpty(request.getParameter("status")))
        {
            params.put("status", request.getParameter("status"));
        }
        PagingResult<MyExperienceRecod> reslut = service.searchAll(params, paging);
        // 查询我的可用体验金
        String experAmonut = Formater.formatAmount(service.getExperienceAmount());
        //封装JSON
        ObjectMapper objectMapper = new ObjectMapper();
        PrintWriter out = response.getWriter();
        String pageStr = rendPaging(reslut);
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        jsonMap.put("pageStr", pageStr);
        jsonMap.put("pageCount", reslut.getPageCount());
        jsonMap.put("retList", reslut.getItems());
        jsonMap.put("experAmonut", experAmonut);
        out.print(objectMapper.writeValueAsString(jsonMap));
        out.close();
    }
    
    @Override
    protected void processGet(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        processPost(request, response, serviceSession);
        
    }
    
    /**
     * ajax分页信息返回字符串(当一个页面有多个分页时使用，解决输入页码查询)
     * @param paging
     * @return
     * @throws Throwable
     */
    @Override
    protected String rendPaging(PagingResult<?> paging)
        throws Throwable
    {
        StringBuffer rtnPageStr = new StringBuffer();
        int currentPage = paging.getCurrentPage();
        
        rtnPageStr.append("<div class='paging'>总共");
        rtnPageStr.append("<span class='total highlight2 ml5 mr5'>");
        rtnPageStr.append(paging.getItemCount());
        rtnPageStr.append("</span>条记录 &nbsp;");
        if (currentPage == 1 && paging.getPageCount() > 1)
        {
            rtnPageStr.append("<a href=\"javascript:void(0);\" class='disabled prev'>&lt;</a>");
        }
        if (currentPage > 1)
        {
            rtnPageStr.append("<a href='javascript:void(0);' class='page-link prev'>&lt;</a>");
        }
        if (paging.getPageCount() > 1)
        {
            int total = 1;
            final int max = 5;
            int index = paging.getPageCount() - currentPage;
            if (index > 2)
            {
                index = 2;
            }
            else
            {
                index = index <= 0 ? (max - 1) : (max - index - 1);
            }
            int i;
            for (i = (currentPage - index); i <= paging.getPageCount() && total <= max; i++)
            {
                if (i <= 0)
                {
                    continue;
                }
                if (currentPage == i)
                {
                    rtnPageStr.append("<a href='javascript:void(0);' class='page-link cur'>");
                    rtnPageStr.append(i);
                    rtnPageStr.append("</a>");
                }
                else
                {
                    rtnPageStr.append("<a href='javascript:void(0);' class='page-link'>");
                    rtnPageStr.append(i);
                    rtnPageStr.append("</a>");
                }
                total++;
            }
            if (i < paging.getPageCount())
            {
                rtnPageStr.append("<span>...</span>");
                int idx = paging.getPageCount() - 2;
                if (i <= idx)
                {
                    rtnPageStr.append("<a href='javascript:void(0);' class='page-link'>");
                    rtnPageStr.append(idx);
                    rtnPageStr.append("</a>");
                }
                idx++;
                if (i <= idx)
                {
                    rtnPageStr.append("<a href='javascript:void(0);' class='page-link'>");
                    rtnPageStr.append(idx);
                    rtnPageStr.append("</a>");
                }
            }
        }
        if (currentPage < paging.getPageCount())
        {
            rtnPageStr.append("<a href='javascript:void(0);' class='page-link next'>&gt;</a>");
        }
        if (currentPage == paging.getPageCount() && paging.getPageCount() > 1)
        {
            rtnPageStr.append("<a href='javascript:void(0);' class=' disabled'>&gt;</a>");
        }
        
        if (paging.getPageCount() > 1)
        {
            rtnPageStr.append("到<input type=\"ime-mode:Disabled\"  id=\"goPage\" maxSize="
                + paging.getPageCount()
                + " class=\"page_input\" maxlength=\"7\" onkeydown=\"onlyNum();\">页<input type=\"button\"  class=\"btn_ok page-link cur\" value=\"确定\" onclick=\"pageSubmit1(this);\" />");
        }
        
        rtnPageStr.append("</div>");
        
        return rtnPageStr.toString();
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
