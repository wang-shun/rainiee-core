package com.dimeng.p2p.front.servlets.financing.sbtz;

import java.io.IOException;

import javax.servlet.jsp.JspWriter;

import com.dimeng.framework.http.servlet.annotation.PagingServlet;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.front.servlets.financing.AbstractFinancingServlet;

@PagingServlet(itemServlet = Hkjl.class)
public class Hkjl extends AbstractFinancingServlet
{
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 页面分页锚点ID值
     */
    public static final String getListAnchor()
    {
        return "CF124600-371C-402B-B08D-2B536D18E937";
    }

    public static void rendPaging(JspWriter out, PagingResult<?> paging, String pagingPath, int bidId)
        throws IOException
    {
        if (out == null)
        {
            return;
        }
        int currentPage = paging.getCurrentPage();
        out.print("<div class='page'>");
        out.print("<span class='fl pr15 ml50'>第");
        out.print(currentPage);
        out.print("页/共");
        out.print(paging.getPageCount());
        out.print("页");
        out.print("（共" + paging.getItemCount() + "条记录）");
        out.print("</span>");
        if (currentPage == 1 && paging.getPageCount() > 1)
        {
            out.print("<a href=\"#" + getListAnchor() + "\"><span class='page-link prev'></span></a>");
        }
        if (currentPage > 1)
        {
            out.print("<a href='");
            out.print(pagingPath);
            out.print(bidId);
            out.print(".html?paging.current=");
            out.print(1);
            out.print("#");
            out.print(getListAnchor());
            out.print("' class='page-link'>首页</a>");
            
            out.print("<a href='");
            out.print(pagingPath);
            out.print(bidId);
            out.print(".html?paging.current=");
            out.print(currentPage - 1);
            out.print("#");
            out.print(getListAnchor());
            out.print("' class='page-link prev'></a>");
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
                    out.print("<a href='#" + getListAnchor() + "' class='page-link on'>");
                    out.print(i);
                    out.print("</a>");
                }
                else
                {
                    out.print("<a href='");
                    out.print(pagingPath);
                    out.print(bidId);
                    out.print(".html?paging.current=");
                    out.print(i);
                    out.print("#");
                    out.print(getListAnchor());
                    out.print("' class='page-link'>");
                    out.print(i);
                    out.print("</a>");
                }
                total++;
            }
            if (i < paging.getPageCount())
            {
                out.print("<span>...</span>");
                int idx = paging.getPageCount() - 2;
                if (i <= idx)
                {
                    out.print("<a href='");
                    out.print(pagingPath);
                    out.print(bidId);
                    out.print(".html?paging.current=");
                    out.print(idx);
                    out.print("#");
                    out.print(getListAnchor());
                    out.print("' class='page-link'>");
                    out.print(idx);
                    out.print("</a>");
                }
                idx++;
                if (i <= idx)
                {
                    out.print("<a href='");
                    out.print(pagingPath);
                    out.print(bidId);
                    out.print(".html?paging.current=");
                    out.print(idx);
                    out.print("#");
                    out.print(getListAnchor());
                    out.print("' class='page-link'>");
                    out.print(idx);
                    out.print("</a>");
                }
            }
        }
        if (currentPage < paging.getPageCount())
        {
            out.print("<a href='");
            out.print(pagingPath);
            out.print(bidId);
            out.print(".html?paging.current=");
            out.print(currentPage + 1);
            out.print("#");
            out.print(getListAnchor());
            out.print("' class='page-link next'></a>");
            
            out.print("<a href='");
            out.print(pagingPath);
            out.print(bidId);
            out.print(".html?paging.current=");
            out.print(paging.getPageCount());
            out.print("#");
            out.print(getListAnchor());
            out.print("' class='page-link'>尾页</a>");
        }
        if (currentPage == paging.getPageCount() && paging.getPageCount() > 1)
        {
            out.print("<a href=\"#" + getListAnchor() + "\"><span class='page-link next'></span></a>");
        }
        out.print("</div>");
    }

}
