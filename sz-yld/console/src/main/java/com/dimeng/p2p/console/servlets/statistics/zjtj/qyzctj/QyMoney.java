package com.dimeng.p2p.console.servlets.statistics.zjtj.qyzctj;

import java.math.BigDecimal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.console.servlets.account.AbstractAccountServlet;
import com.dimeng.p2p.modules.statistics.console.service.QyMoneyManage;
import com.dimeng.p2p.modules.statistics.console.service.entity.PropertyStatisticsEntity;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.IntegerParser;

@Right(id = "P2P_C_STATISTICS_QYMONEY", isMenu = true, name = "企业资产统计", moduleId = "P2P_C_STATISTICS_ZJTJ_QY", order = 0)
public class QyMoney extends AbstractAccountServlet
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
        QyMoneyManage qyMoneyManage = serviceSession.getService(QyMoneyManage.class);
        PropertyStatisticsEntity query = new PropertyStatisticsEntity();
        query.parse(request);
        query.balanceMin =
            StringHelper.isEmpty(request.getParameter("balanceMin")) ? null : new BigDecimal(
                request.getParameter("balanceMin"));
        query.balanceMax =
            StringHelper.isEmpty(request.getParameter("balanceMax")) ? null : new BigDecimal(
                request.getParameter("balanceMax"));
        PagingResult<PropertyStatisticsEntity> list = qyMoneyManage.search(query, new Paging()
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
        PropertyStatisticsEntity searchAmount = qyMoneyManage.searchAmount(query);
        request.setAttribute("searchAmount", searchAmount);
        request.setAttribute("list", list);
        forwardView(request, response, getClass());
    }
    
}
