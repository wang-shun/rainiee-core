package com.dimeng.p2p.front.servlets.financing.sbtz;

import com.dimeng.framework.http.servlet.annotation.PagingServlet;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S62.entities.T6211;
import com.dimeng.p2p.S62.enums.T6230_F20;
import com.dimeng.p2p.common.enums.CreditTerm;
import com.dimeng.p2p.front.servlets.financing.AbstractFinancingServlet;
import com.dimeng.p2p.modules.bid.front.service.BidManage;
import com.dimeng.p2p.modules.bid.front.service.entity.Bdlb;
import com.dimeng.p2p.modules.bid.front.service.entity.Tztjxx;
import com.dimeng.p2p.modules.bid.front.service.query.BidQuery_Order;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.EnumParser;
import com.dimeng.util.parser.IntegerParser;
import org.codehaus.jackson.map.ObjectMapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * 个人列表
 */
@PagingServlet(itemServlet = Bdxq.class)
public class Grlb extends AbstractFinancingServlet {

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
        Tztjxx total = bidManage.getStatisticsGr();
        BidQuery_Order query = new BidQuery_Order()
        {
            @Override
            public T6211[] getType()
            {
                String[] values = request.getParameterValues("bidType");
                if (values == null || values.length == 0)
                {
                    return null;
                }
                T6211[] types = new T6211[values.length];
                for (int i = 0; i < values.length; i++)
                {
                    if (!StringHelper.isEmpty(values[i]))
                    {
                        types[i] = new T6211();
                        types[i].F01 = IntegerParser.parse(values[i]);
                    }
                }
                return types;
            }
            
            @Override
            public int getRate()
            {
                return IntegerParser.parse(request.getParameter("yearRate"));
            }
            
            @Override
            public CreditTerm[] getTerm()
            {
                String[] values = request.getParameterValues("creditTerm");
                if (values == null || values.length == 0)
                {
                    return null;
                }
                CreditTerm[] terms = new CreditTerm[values.length];
                for (int i = 0; i < values.length; i++)
                {
                    terms[i] = EnumParser.parse(CreditTerm.class, values[i]);
                }
                return terms;
            }
            
            @Override
            public T6230_F20[] getStatus()
            {
                String[] values = request.getParameterValues("status");
                T6230_F20[] levels = new T6230_F20[values.length];
                for (int i = 0; i < values.length; i++)
                {
                    levels[i] = EnumParser.parse(T6230_F20.class, values[i]);
                }
                return levels;
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
        };
        PagingResult<Bdlb> result = bidManage.search(query, new Paging()
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
