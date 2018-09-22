package com.dimeng.p2p.user.servlets.credit;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;

import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S62.entities.T6230;
import com.dimeng.p2p.modules.bid.user.service.JksqcxManage;
import com.dimeng.util.parser.IntegerParser;

/**
 * 借款申请查询
 *
 */
public class Apply extends AbstractCreditServlet{

	private static final long serialVersionUID = 1L;

	@Override
	protected void processPost(final HttpServletRequest request,
			HttpServletResponse response, ServiceSession serviceSession)
			throws Throwable {
		JksqcxManage manage = serviceSession.getService(JksqcxManage.class);
		PagingResult<T6230> applyLoans = manage.getApplyLoans(new Paging() {
			
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
		String pageStr = rendPaging(applyLoans);
        PrintWriter out = response.getWriter();
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        jsonMap.put("pageStr", pageStr);
        jsonMap.put("pageCount", applyLoans.getPageCount());
        jsonMap.put("applyList", applyLoans.getItems());
        out.print(objectMapper.writeValueAsString(jsonMap));
        out.close();
	}
	
}
