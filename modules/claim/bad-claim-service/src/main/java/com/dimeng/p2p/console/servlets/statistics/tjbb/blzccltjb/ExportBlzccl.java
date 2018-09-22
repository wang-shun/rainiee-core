/*
 * 文 件 名:  ExportBlzccl.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  God
 * 修改时间:  2016年6月20日
 */
package com.dimeng.p2p.console.servlets.statistics.tjbb.blzccltjb;

import java.sql.Date;
import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.console.servlets.AbstractBadClaimServlet;
import com.dimeng.p2p.repeater.claim.BadClaimTransferManage;
import com.dimeng.p2p.repeater.claim.entity.BadClaimYzr;
import com.dimeng.p2p.repeater.claim.query.YzrQuery;
import com.dimeng.util.parser.IntegerParser;
import com.dimeng.util.parser.SQLDateParser;

/**
 * <导出不良资产处理统计>
 * <功能详细描述>
 * 
 * @author  zhoucl
 * @version  [版本号, 2016年6月20日]
 */
@Right(id = "P2P_C_FINANCE_BLZCCLTJBEXPORT", name = "导出", moduleId = "P2P_C_STATISTICS_TJBB_BLZCCLTJB", order = 1)
public class ExportBlzccl extends AbstractBadClaimServlet
{
    
    /**
     * 注释内容
     */
    private static final long serialVersionUID = -8120179199536116685L;
    
    @Override
    protected void processGet(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        processPost(request, response, serviceSession);
    }
    
    @Override
    protected void processPost(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        response.setHeader("Content-disposition",
            "attachment;filename=" + new Timestamp(System.currentTimeMillis()).getTime() + ".csv");
        response.setContentType("application/csv");
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
                return Integer.MAX_VALUE;
            }
            
            @Override
            public int getCurrentPage()
            {
                return IntegerParser.parse(request.getParameter(AbstractBadClaimServlet.PAGING_CURRENT));
            }
        };
        PagingResult<BadClaimYzr> result = badClaimTransferManage.badClaimYzrSearch(yzrQuery, paging);
        badClaimTransferManage.exportBlzqYzr(result.getItems(), response.getOutputStream(), "");
    }
    
}
