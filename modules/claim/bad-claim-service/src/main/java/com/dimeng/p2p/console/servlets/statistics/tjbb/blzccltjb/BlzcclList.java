/*
 * 文 件 名:  Blzccl.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  God
 * 修改时间:  2016年6月20日
 */
package com.dimeng.p2p.console.servlets.statistics.tjbb.blzccltjb;

import java.io.IOException;
import java.sql.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S61.entities.T6161;
import com.dimeng.p2p.console.servlets.AbstractBadClaimServlet;
import com.dimeng.p2p.repeater.claim.BadClaimTransferManage;
import com.dimeng.p2p.repeater.claim.entity.BadClaimYzr;
import com.dimeng.p2p.repeater.claim.query.YzrQuery;
import com.dimeng.util.parser.IntegerParser;
import com.dimeng.util.parser.SQLDateParser;

/**
 * <不良资产处理统计>
 * <功能详细描述>
 * 
 * @author  zhoucl
 * @version  [版本号, 2016年6月20日]
 */
@Right(id = "P2P_C_FINANCE_BLZCCLTJBLIST", name = "不良资产处理统计表", moduleId = "P2P_C_STATISTICS_TJBB_BLZCCLTJB", order = 0)
public class BlzcclList extends AbstractBadClaimServlet
{
    
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 2064356773089695188L;
    
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
        YzrQuery yzrQuery = new YzrQuery()
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
            
            @Override
            public Date getStartTime()
            {
                return SQLDateParser.parse(request.getParameter("startTime"));
            }
            
            @Override
            public Date getEndTime()
            {
                return SQLDateParser.parse(request.getParameter("endTime"));
            }
            
            @Override
            public int getClaimReceiver()
            {
                return IntegerParser.parse(request.getParameter("claimReceiver"));
            }
            
            @Override
            public String getClaimNo()
            {
                return request.getParameter("claimNo");
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
        PagingResult<BadClaimYzr> badClaimYzrPagingResult = badClaimTransferManage.badClaimYzrSearch(yzrQuery, paging);
        List<T6161> t6161List = badClaimTransferManage.getClaimReceiverList();
        request.setAttribute("t6161List", t6161List);
        request.setAttribute("badClaimYzrPagingResult", badClaimYzrPagingResult);
        request.setAttribute("badAssetsTotal", badClaimTransferManage.getBadAssetsTotal(yzrQuery));
        forwardView(request, response, BlzcclList.class);
    }
    
    @Override
    protected void onThrowable(HttpServletRequest request, HttpServletResponse response, Throwable throwable)
        throws ServletException, IOException
    {
        super.onThrowable(request, response, throwable);
    }
    
}
