package com.dimeng.p2p.console.servlets.finance.zjgl.dfgl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S51.enums.T5131_F02;
import com.dimeng.p2p.S62.enums.T6230_F10;
import com.dimeng.p2p.console.servlets.finance.AbstractFinanceServlet;
import com.dimeng.p2p.modules.finance.console.service.PtdfManage;
import com.dimeng.p2p.modules.finance.console.service.entity.DfRecord;
import com.dimeng.p2p.modules.finance.console.service.query.DfQuery;
import com.dimeng.util.parser.IntegerParser;

/**
 * @author  zhangshenqiu
 */
//@Right(id = "P2P_C_FINANCE_DFGLLIST", name = "不良资产管理", moduleId = "P2P_C_FINANCE_ZJGL_PTDFGL", order = 0)
public class YjqList extends AbstractFinanceServlet
{
    
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 1681629953797215905L;
    
    /** {@inheritDoc} */
    
    @Override
    protected void processGet(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        processPost(request, response, serviceSession);
    }
    
    /** {@inheritDoc} */
    
    @Override
    protected void processPost(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        PtdfManage collectionManage = serviceSession.getService(PtdfManage.class);
        DfQuery yqddfQuery = new DfQuery()
        {
            
            @Override
            public String getBidNo()
            {
                return request.getParameter("bidNo");
            }
            
            @Override
            public String getLoanName()
            {
                return request.getParameter("loanName");
            }
            
            @Override
            public String getLoanTitle()
            {
                return request.getParameter("loanTitle");
            }
            
            @Override
            public T6230_F10 getHkfs()
            {
                return T6230_F10.parse(request.getParameter("hkfs"));
            }
            
            @Override
            public int getYuqiFromDays()
            {
                return IntegerParser.parse(request.getParameter("yuqiFromDays"));
            }
            
            @Override
            public int getYuqiEndDays()
            {
                return IntegerParser.parse(request.getParameter("yuqiEndDays"));
            }
            
            @Override
            public T5131_F02 getDffs()
            {
                return T5131_F02.parse(request.getParameter("dffs"));
            }
        };
        PagingResult<DfRecord> dfRecord = collectionManage.dfyjqSearch(yqddfQuery, new Paging()
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
        DfRecord dfyjqSearchAmount = collectionManage.dfyjqSearchAmount(yqddfQuery);
        request.setAttribute("dfRecord", dfRecord);
        request.setAttribute("dfyjqSearchAmount", dfyjqSearchAmount);
        forwardView(request, response, getClass());
    }
    
}
