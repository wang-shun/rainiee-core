package com.dimeng.p2p.front.servlets.financing.sbtz;

import com.dimeng.framework.http.servlet.annotation.PagingServlet;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S61.entities.T6196;
import com.dimeng.p2p.S62.enums.T6230_F20;
import com.dimeng.p2p.front.servlets.financing.AbstractFinancingServlet;
import com.dimeng.p2p.modules.bid.front.service.BidManage;
import com.dimeng.p2p.modules.bid.front.service.entity.Bdlb;
import com.dimeng.p2p.modules.bid.front.service.entity.Tztjxx;
import com.dimeng.p2p.modules.bid.front.service.query.QyBidQuery;
import com.dimeng.p2p.repeater.policy.OperateDataManage;
import com.dimeng.util.parser.EnumParser;
import com.dimeng.util.parser.IntegerParser;

import org.codehaus.jackson.map.ObjectMapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * 散标列表
 */
@PagingServlet(itemServlet = Bdxq.class)
public class Index extends AbstractFinancingServlet {

	private static final long serialVersionUID = 5455206100909928866L;
    
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
        BidManage bidManage = serviceSession.getService(BidManage.class);
        Tztjxx total = bidManage.getStatisticsQy();
        OperateDataManage manage = serviceSession.getService(OperateDataManage.class);
        T6196 t6196 = manage.getT6196();
        //添加初始值
        total.totleMoney = total.totleMoney.add(t6196.F02);
        total.totleCount = total.totleCount+t6196.F05;
        total.userEarnMoney = total.userEarnMoney.add(t6196.F04);
        QyBidQuery query = new QyBidQuery()
        {
            @Override
            public int getRate()
            {
                int rate = IntegerParser.parse(request.getParameter("yearRate"));
                return rate;
            }
            
            @Override
            public int getJd()
            {
                int progress = IntegerParser.parse(request.getParameter("progress"));
                return progress;
            }
            
            @Override
            public T6230_F20 getStatus()
            {
                return EnumParser.parse(T6230_F20.class, request.getParameter("status"));
            }
            
            @Override
            public int getOrder()
            {
                return IntegerParser.parse(request.getParameter("orderItem"));
            }

            @Override
            public int getProductId()
            {
                return IntegerParser.parse(request.getParameter("productId"));
            }

            @Override
            public int bidType()
            {
                return IntegerParser.parse(request.getParameter("bidType"));
            }
        };
        PagingResult<Bdlb> result = bidManage.searchQy(query, null, new Paging()
        {
            @Override
            public int getCurrentPage()
            {
                return IntegerParser.parse(request.getParameter("currentPage"));
            }
            
            @Override
            public int getSize()
            {
                return IntegerParser.parse(request.getParameter("pageSize"));
            }
        });
        String pageStr = bidManage.rendPaging(result);
        PrintWriter out = response.getWriter();
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        jsonMap.put("total", total);
        jsonMap.put("pageStr", pageStr);
        jsonMap.put("pageCount", result.getPageCount());
        jsonMap.put("bidList", result.getItems());
        out.print(objectMapper.writeValueAsString(jsonMap));
        out.close();
    }
}
