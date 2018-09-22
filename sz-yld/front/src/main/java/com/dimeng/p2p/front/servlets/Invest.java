package com.dimeng.p2p.front.servlets;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.p2p.repeater.donation.GyLoanManage;
import org.codehaus.jackson.map.ObjectMapper;

import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.modules.bid.front.service.BidManage;
import com.dimeng.p2p.modules.bid.front.service.TransferManage;

public class Invest extends AbstractFrontServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void processPost(final HttpServletRequest request,
			HttpServletResponse response, ServiceSession serviceSession)
			throws Throwable {
		BidManage bidManage = serviceSession.getService(BidManage.class);
		TransferManage transferManage = serviceSession.getService(TransferManage.class);
		GyLoanManage gyLoanManage=serviceSession.getService(GyLoanManage.class);
		String type = request.getParameter("type");
		PagingResult<?> invests = null;

		if("sbtz".equals(type)){
            Paging paging = null;
            if (bidManage.haveTJB())
            {
                paging = new Paging()
                {
                    
                    @Override
                    public int getCurrentPage()
                    {
                        return 1;
                    }
                    
                    @Override
                    public int getSize()
                    {
                        return 6;
                    }
                };
            }
            else
            {
                new Paging()
                {
                    
                    @Override
                    public int getCurrentPage()
                    {
                        return 1;
                    }
                    
                    @Override
                    public int getSize()
                    {
                        return 6;
                    }
                };
            }
            invests = bidManage.searchAll(null, paging);
		}
		else if("gyjz".equals(type))
		{
			invests = gyLoanManage.search4front(null, new Paging() {
				@Override
				public int getCurrentPage() {
					return 1;
				}

				@Override
				public int getSize() {
					return 6;
				}
			});
		}
		else{
			invests = transferManage.search(null,new Paging() {
				
				@Override
	            public int getCurrentPage()
	            {
	                return 1;
	            }
	            
	            @Override
	            public int getSize()
	            {
	                return 6;
	            }
			});
		}
		
        PrintWriter out = response.getWriter();
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        jsonMap.put("investList", invests.getItems());
        out.print(objectMapper.writeValueAsString(jsonMap));
        out.close();
	}
}
