package com.dimeng.p2p.front.servlets.financing.gyb;

import com.dimeng.framework.http.servlet.annotation.PagingServlet;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.AbstractDonationServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspWriter;
import java.io.IOException;

@PagingServlet(itemServlet = GybXq.class)
public class GybXq extends AbstractDonationServlet
{
    
	
	public static void rendPaging(JspWriter out, PagingResult<?> paging,
			String pagingPath, int bidId) throws IOException {
		if (out == null) {
			return;
		}
		int currentPage = paging.getCurrentPage();
        out.print("<div class='paging'>总共");
        out.print("<span class='total highlight2 ml5 mr5'>" + paging.getItemCount() + "</span>条记录&nbsp;");
		if (currentPage == 1 && paging.getPageCount() > 1) {
            out.print("<a href=\"javascript:;\" class='disabled'>&lt;</a>");
		}
		if (currentPage > 1) {
			out.print("<a href='");
			out.print(pagingPath);
			out.print(bidId);
			out.print(".html?paging.current=");
			out.print(currentPage - 1);
            out.print("' class=''>&lt;</a>");
		}
		if (paging.getPageCount() > 1) {
			int total = 1;
			final int max = 5;
			int index = paging.getPageCount() - currentPage;
			if (index > 2) {
				index = 2;
			} else {
				index = index <= 0 ? (max - 1) : (max - index - 1);
			}
			int i;
			for (i = (currentPage - index); i <= paging.getPageCount()
					&& total <= max; i++) {
				if (i <= 0) {
					continue;
				}
				if (currentPage == i) {
                    out.print("<a href='javascript:;' class='cur'>");
					out.print(i);
					out.print("</a>");
				} else {
					out.print("<a href='");
					out.print(pagingPath);
					out.print(bidId);
					out.print(".html?paging.current=");
					out.print(i);
                    out.print("' class=''>");
					out.print(i);
					out.print("</a>");
				}
				total++;
			}
			if (i < paging.getPageCount()) {
                out.print("...");
				int idx = paging.getPageCount() - 2;
				if (i <= idx) {
					out.print("<a href='");
					out.print(pagingPath);
					out.print(bidId);
					out.print(".html?paging.current=");
					out.print(idx);
                    out.print("' class=''>");
					out.print(idx);
					out.print("</a>");
				}
				idx++;
				if (i <= idx) {
					out.print("<a href='");
					out.print(pagingPath);
					out.print(bidId);
					out.print(".html?paging.current=");
					out.print(idx);
                    out.print("' class=''>");
					out.print(idx);
					out.print("</a>");
				}
			}
		}
		if (currentPage < paging.getPageCount()) {
			out.print("<a href='");
			out.print(pagingPath);
			out.print(bidId);
			out.print(".html?paging.current=");
			out.print(currentPage + 1);
            out.print("' class=''>&gt;</a>");
		}
		if (currentPage == paging.getPageCount() && paging.getPageCount() > 1) {
            out.print("<a href=\"javascript:;\" class='disabled'>&gt;</a>");
		}
        
        if (paging.getPageCount() > 1)
        {
            out.print("到<input type=\"text\"  id=\"goPage\" class=\"page_input\" maxlength=\"7\" onKeyUp=\"value=(parseInt((value=value.replace(/\\D/g,''))==''?'0':value,10))\">页<input type=\"button\"  class=\"btn_ok\" value=\"确定\"  onclick=\"goPageSubmit(this,'"
                + pagingPath + bidId + ".html" + "');\" />");
        }
		//out.print("<a href='");
		//out.print(pagingPath);
		//out.print(bidId);
		//out.print(".html");
		//out.print("' class='page-link'>显示全部</a>");
		out.print("</div>");
	}

	
    /**
     * 注释内容
     */
    private static final long serialVersionUID = -3169057464358772819L;
    
    @Override
    protected void processGet(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        super.processPost(request, response, serviceSession);
    }
    
    @Override
    protected void processPost(final HttpServletRequest request, HttpServletResponse response,
        ServiceSession serviceSession)
        throws Throwable
    {
        //sDonationQuery
        /*GyLoanManage gyLoanManage = serviceSession.getService(GyLoanManage.class);
        PagingResult<Donation> tbList = gyLoanManage.searchTbjl(new DonationQuery()
        {
            
            @Override
            public int getUserId()
            {
                // TODO Auto-generated method stub
                return 0;
            }
            
            @Override
            public Timestamp getCreateTimeStart()
            {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public Timestamp getCreateTimeEnd()
            {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public int getBidId()
            {
                // TODO Auto-generated method stub
                return 0;
            }

        }, new Paging()
        {
            
            @Override
            public int getSize()
            {
                // TODO Auto-generated method stub
                return 0;
            }
            
            @Override
            public int getCurrentPage()
            {
                // TODO Auto-generated method stub
                return 0;
            }
        });*/
    }
}
