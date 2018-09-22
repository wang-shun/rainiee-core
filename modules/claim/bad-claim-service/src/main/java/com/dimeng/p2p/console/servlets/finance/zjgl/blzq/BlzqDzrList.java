/*
 * 文 件 名:  BlzqzrglList.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  God
 * 修改时间:  2016年6月14日
 */
package com.dimeng.p2p.console.servlets.finance.zjgl.blzq;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.console.servlets.AbstractBadClaimServlet;
import com.dimeng.p2p.repeater.claim.BadClaimTransferManage;
import com.dimeng.p2p.repeater.claim.entity.BadClaimZr;
import com.dimeng.p2p.repeater.claim.query.DzrQuery;
import com.dimeng.util.parser.IntegerParser;

/**
 * <不良债权待转让>
 * <功能详细描述>
 * 
 * @author  zhoucl
 * @version  [版本号, 2016年6月14日]
 */
@Right(id = "P2P_C_FINANCE_BLZQDZRLIST", name = "待转让", moduleId = "P2P_C_FINANCE_BLZQZRGL", order = 0)
public class BlzqDzrList extends AbstractBadClaimServlet
{
    
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 4505990551668575742L;
    
    @Override
    protected void processGet(final HttpServletRequest request, HttpServletResponse response,
        ServiceSession serviceSession)
        throws Throwable
    {
        processPost(request, response, serviceSession);
    }
    
    @Override
    protected void processPost(final HttpServletRequest request, HttpServletResponse response,
        ServiceSession serviceSession)
        throws Throwable
    {
        
        BadClaimTransferManage badClaimTransferManage = serviceSession.getService(BadClaimTransferManage.class);
        DzrQuery dzrQuery = new DzrQuery()
        {
            
            @Override
            public String getBidNo()
            {
                return request.getParameter("bidNo");
            }
            
            @Override
            public String getLoanTitle()
            {
                return request.getParameter("loanTitle");
            }
            
            @Override
            public String getLoanName()
            {
                return request.getParameter("loanName");
            }
            
            @Override
            public String getYuqiFromDays()
            {
                return request.getParameter("yuqiFromDays");
            }
            
            @Override
            public String getYuqiEndDays()
            {
                return request.getParameter("yuqiEndDays");
            }
            
            @Override
            public String getLoanAttribute()
            {
                return request.getParameter("loanAttribute");
            }
            
        };
        Paging paging = new Paging()
        {
            
            @Override
            public int getSize()
            {
                return 10;
            }
            
            @Override
            public int getCurrentPage()
            {
                return IntegerParser.parse(request.getParameter(PAGING_CURRENT));
            }
        };
        PagingResult<BadClaimZr> badClaimDzrPagingResult = badClaimTransferManage.badClaimDzrSearch(dzrQuery, paging);
        request.setAttribute("badClaimDzrPagingResult", badClaimDzrPagingResult);
        forwardView(request, response, BlzqDzrList.class);
    }
    
    @Override
    protected void onThrowable(HttpServletRequest request, HttpServletResponse response, Throwable throwable)
        throws ServletException, IOException
    {
        super.onThrowable(request, response, throwable);
    }
    
}
