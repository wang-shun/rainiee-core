package com.dimeng.p2p.console.servlets.base.optsettings.dxz;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.console.servlets.base.AbstractBaseServlet;
import com.dimeng.p2p.modules.bid.console.service.DxzBidManage;
import com.dimeng.p2p.modules.bid.console.service.entity.DxzInfo;
import com.dimeng.p2p.modules.bid.console.service.query.DxzQuery;
import com.dimeng.util.parser.IntegerParser;

/**
 * 定向组列表信息
 * @author  zhongsai
 * @version  [V7.0, 2018年2月8日]
 */
@Right(id = "P2P_C_BASE_DXZLIST", isMenu = true, name = "定向组管理", moduleId = "P2P_C_BASE_OPTSETTINGS_DXZTYPE", order = 0)
public class DxzList extends AbstractBaseServlet
{
    private static final long serialVersionUID = 1L;
    
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
        DxzBidManage dxzBidManage = serviceSession.getService(DxzBidManage.class);
        PagingResult<DxzInfo> result = dxzBidManage.search(new DxzQuery()
        {
            @Override
            public String getDxzId()
            {
                return request.getParameter("dxzId");
            }
            
            @Override
            public String getDxzTitle()
            {
                return request.getParameter("dxzTitle");
            }
            
        }, new Paging()
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
        request.setAttribute("result", result);
        forwardView(request, response, getClass());
    }
}
