package com.dimeng.p2p.user.servlets.financing.tyjlc;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.servlet.annotation.PagingServlet;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.account.user.service.MyExperienceManage;
import com.dimeng.p2p.account.user.service.entity.HzEntity;
import com.dimeng.p2p.account.user.service.query.TyjBackAccountQuery;
import com.dimeng.p2p.common.enums.QueryType;
import com.dimeng.p2p.user.servlets.financing.AbstractFinancingServlet;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.DateParser;
import com.dimeng.util.parser.EnumParser;
import com.dimeng.util.parser.IntegerParser;

@PagingServlet(itemServlet = TyjBackAccount.class)
public class TyjBackAccount extends AbstractFinancingServlet
{
    
    /**
     * 
     */
    private static final long serialVersionUID = 19842188160942532L;
    
    @Override
    protected void processPost(final HttpServletRequest request, final HttpServletResponse response, final ServiceSession serviceSession)
        throws Throwable
    {
        MyExperienceManage experManage = serviceSession.getService(MyExperienceManage.class);
        PagingResult<HzEntity> boList = experManage.searchHz(new TyjBackAccountQuery()
        {
            
            @Override
            public Date getTimeStart()
            {
                Date ts = DateParser.parse(request.getParameter("timeStart"), "yyyy-MM-dd");
                request.setAttribute("timeStart", ts);
                return ts;
            }
            
            @Override
            public Date getTimeEnd()
            {
                Date ts = DateParser.parse(request.getParameter("timeEnd"), "yyyy-MM-dd");
                request.setAttribute("timeEnd", ts);
                return ts;
            }
            
            @Override
            public QueryType getQueryType()
            {
                String queryType = request.getParameter("queryType");
                if (!StringHelper.isEmpty(queryType))
                {
                    request.setAttribute("queryType", queryType);
                    return EnumParser.parse(QueryType.class, queryType);
                }
                return QueryType.DS;
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
                return IntegerParser.parse(request.getParameter("paging.current"));
            }
        });
        //全部待收利息
        Map<String, Object> retMap = experManage.getInterestTot();
        //待收笔数
        
        //查询总额
        request.setAttribute("boList", boList);
        request.setAttribute("retMap", retMap);
        forwardView(request, response, getClass());
        
    }
    
    @Override
    protected void processGet(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        processPost(request, response, serviceSession);
    }
    
}
