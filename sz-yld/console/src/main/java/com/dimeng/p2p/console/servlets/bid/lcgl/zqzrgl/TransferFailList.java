/*
 * 文 件 名:  TransferFailList.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  zhoucl
 * 修改时间:  2016年1月18日
 */
package com.dimeng.p2p.console.servlets.bid.lcgl.zqzrgl;

import java.sql.Timestamp;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.console.servlets.bid.AbstractBidServlet;
import com.dimeng.p2p.modules.financial.console.service.CreditorTransferManage;
import com.dimeng.p2p.modules.financial.console.service.entity.TransferDshEntity;
import com.dimeng.p2p.modules.financial.console.service.query.TransferDshQuery;
import com.dimeng.util.parser.DateParser;
import com.dimeng.util.parser.IntegerParser;

/**
 * <债权转让失败>
 * <功能详细描述>
 * 
 * @author  zhoucl
 * @version  [版本号, 2016年1月18日]
 */
@Right(id = "P2P_C_BID_BUSI_ZQZR", isMenu = true, name = "线上债权转让管理", moduleId = "P2P_C_BID_LCGL_ZQZRGL", order = 0)
public class TransferFailList extends AbstractBidServlet
{
    
    /**
     * 注释内容
     */
    private static final long serialVersionUID = -2476888339000596106L;
    
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
        CreditorTransferManage creditorTransferManage = serviceSession.getService(CreditorTransferManage.class);
        TransferDshQuery transferDshQuery = new TransferDshQuery()
        {
            
            @Override
            public Timestamp getCreateTimeStart()
            {
                Date date = DateParser.parse(request.getParameter("createTimeStart"));
                return date == null ? null : new Timestamp(date.getTime());
            }
            
            @Override
            public Timestamp getCreateTimeEnd()
            {
                Date date = DateParser.parse(request.getParameter("createTimeEnd"));
                return date == null ? null : new Timestamp(date.getTime());
            }
            
            @Override
            public String getCreditorId()
            {
                return request.getParameter("creditorId");
            }
            
            @Override
            public String getCreditorOwner()
            {
                return request.getParameter("creditorOwner");
            }
            
            @Override
            public String getLoanId()
            {
                return request.getParameter("loanId");
            }
            
            @Override
            public String getLoanTitle()
            {
                return request.getParameter("loanTitle");
            }
        };
        PagingResult<TransferDshEntity> transferZrsbs =
            creditorTransferManage.transferZrsbSearch(transferDshQuery, new Paging()
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
            });
        TransferDshEntity transferZrsbAmount = creditorTransferManage.transferZrsbAmount(transferDshQuery);
        request.setAttribute("transferZrsbAmount", transferZrsbAmount);
        request.setAttribute("transferZrsbs", transferZrsbs);
        request.setAttribute("creditorId", request.getParameter("creditorId"));
        request.setAttribute("loanId", request.getParameter("loanId"));
        request.setAttribute("loanTitle", request.getParameter("loanTitle"));
        request.setAttribute("creditorOwner", request.getParameter("creditorOwner"));
        
        forwardView(request, response, getClass());
    }
}
