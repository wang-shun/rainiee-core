package com.dimeng.p2p.console.servlets.account.riskresult.policy;

import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.console.servlets.AbstractRankServlet;
import com.dimeng.p2p.repeater.policy.RiskQuesManage;
import com.dimeng.p2p.repeater.policy.query.ResultQuery;
import com.dimeng.p2p.repeater.policy.query.RiskQueryResult;
import com.dimeng.util.parser.IntegerParser;
import com.dimeng.util.parser.TimestampParser;

@Right(id = "P2P_C_ACCOUNT_FXPGJG_POLICY_LIST", isMenu = true, name = "风险评估结果", moduleId = "P2P_C_ACCOUNT_FXPGJG_POLICY", order = 0)
public class RiskResultList extends AbstractRankServlet
{
    
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processGet(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        processPost(request, response, serviceSession);
    }
    
    @Override
    protected void processPost(final HttpServletRequest request, HttpServletResponse response,
        ServiceSession serviceSession)
        throws Throwable
    {
        RiskQuesManage manage = serviceSession.getService(RiskQuesManage.class);
        PagingResult<RiskQueryResult> result = manage.queryAllResult(new Paging()
        {
            @Override
            public int getCurrentPage()
            {
                return IntegerParser.parse(request.getParameter(PAGING_CURRENT));
            }
            
            @Override
            public int getSize()
            {
                return 10;
            }
        }, new ResultQuery()
        {
            @Override
            public String getName()
            {
                return request.getParameter("name");
            }
            
            @Override
            public Timestamp getCreateTimeStart()
            {
                return TimestampParser.parse(request.getParameter("createTimeStart"));
            }
            
            @Override
            public Timestamp getCreateTimeEnd()
            {
                return TimestampParser.parse(request.getParameter("createTimeEnd"));
            }
        });
        request.setAttribute("result", result);
        forwardView(request, response, getClass());
    }
}
