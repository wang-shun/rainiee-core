package com.dimeng.p2p.front.servlets.financing.sbtz;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;

import com.dimeng.framework.http.servlet.annotation.PagingServlet;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S51.entities.T5124;
import com.dimeng.p2p.S62.entities.T6211;
import com.dimeng.p2p.front.servlets.financing.AbstractFinancingServlet;
import com.dimeng.p2p.modules.bid.front.service.BidManage;
import com.dimeng.p2p.modules.bid.front.service.TransferManage;
import com.dimeng.p2p.modules.bid.front.service.entity.Zqzqlb;
import com.dimeng.p2p.modules.bid.front.service.entity.Zqzrtj;
import com.dimeng.p2p.modules.bid.front.service.query.TransferQuery_Order;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.IntegerParser;

/**
 * 线上债权转让列表
 */
@PagingServlet(itemServlet = Zqxq.class)
public class Zqzrlb extends AbstractFinancingServlet {

	private static final long serialVersionUID = 5455206100909928866L;
    
    @Override
    protected void processPost(final HttpServletRequest request, HttpServletResponse response,
        ServiceSession serviceSession)
        throws Throwable
    {
        TransferManage service = serviceSession.getService(TransferManage.class);
        Zqzrtj total = service.getStatistics();
        BidManage bidManage = serviceSession.getService(BidManage.class);
        TransferQuery_Order query = new TransferQuery_Order()
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
            public int getTerm()
            {
                return IntegerParser.parse(request.getParameter("creditTerm"));
                /*String[] values = request.getParameterValues("creditTerm");
                if (values == null || values.length == 0)
                {
                    return null;
                }
                CreditTerm[] terms = new CreditTerm[values.length];
                for (int i = 0; i < values.length; i++)
                {
                    terms[i] = EnumParser.parse(CreditTerm.class, values[i]);
                }
                return terms;*/
            }
            
            @Override
            public T5124[] getCreditLevel()
            {
                return null;
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
        PagingResult<Zqzqlb> result = service.search(query, new Paging()
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
