package com.dimeng.p2p.user.servlets.financing.tyjlc;

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
import com.dimeng.p2p.account.user.service.MyExperienceManage;
import com.dimeng.p2p.account.user.service.entity.MyExperience;
import com.dimeng.p2p.user.servlets.financing.AbstractFinancingServlet;
@PagingServlet(itemServlet = Index.class)
public class Index extends AbstractFinancingServlet{
	private static final long serialVersionUID = -784201583820142095L;

	@Override
	protected void processPost(final HttpServletRequest request,
			HttpServletResponse response, ServiceSession serviceSession)
			throws Throwable {
		MyExperienceManage service=serviceSession.getService(MyExperienceManage.class);
		String type = request.getParameter("type");
		PagingResult<MyExperience> raList= service.searMyExperience(type,new Paging() {
			
			@Override
			public int getSize() {
				return Integer.parseInt(request.getParameter("pageSize"));
			}
			
			@Override
			public int getCurrentPage() {
				return Integer.parseInt(request.getParameter("currentPage"));
			}
		});
		String pageStr = rendPaging(raList);
		PrintWriter out = response.getWriter();
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        jsonMap.put("pageStr", pageStr);
        jsonMap.put("pageCount", raList.getPageCount());
        jsonMap.put("experienceList", raList.getItems());
        out.print(objectMapper.writeValueAsString(jsonMap));
        out.close();
		
	}

}
