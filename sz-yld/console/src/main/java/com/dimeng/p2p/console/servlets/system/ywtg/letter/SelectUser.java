package com.dimeng.p2p.console.servlets.system.ywtg.letter;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;

import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.console.servlets.account.AbstractAccountServlet;
import com.dimeng.p2p.modules.account.console.service.ZhglManage;
import com.dimeng.p2p.modules.account.console.service.entity.User;
import com.dimeng.p2p.modules.account.console.service.query.UserQuery;
import com.dimeng.util.parser.IntegerParser;



public class SelectUser extends AbstractAccountServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void processGet(HttpServletRequest request,
			HttpServletResponse response, ServiceSession serviceSession)
			throws Throwable {
		processPost(request, response, serviceSession);
	}

	@Override
	protected void processPost(final HttpServletRequest request,
			HttpServletResponse response,
			ServiceSession serviceSession) throws Throwable {
		ZhglManage manage = serviceSession.getService(ZhglManage.class);
		//分页参数
        Paging paging = new Paging()
        {
            @Override
            public int getSize()
            {
                return IntegerParser.parse(request.getParameter("pageSize"));
            }
            
            @Override
            public int getCurrentPage()
            {
                return IntegerParser.parse(request.getParameter("currentPage"));
            }
        };
		UserQuery query = new UserQuery();
		query.parse(request);
		PagingResult<User> list = manage.searchUsers(query, paging);
		String pageStr = this.rendPaging(list);
		PrintWriter out = response.getWriter();
	    ObjectMapper objectMapper = new ObjectMapper();
	    Map<String, Object> jsonMap = new HashMap<String, Object>();
	    jsonMap.put("userList", list.getItems());
	    jsonMap.put("pageStr", pageStr);
        jsonMap.put("pageCount", list.getPageCount());
	    out.print(objectMapper.writeValueAsString(jsonMap));
	    out.close();
	}

}
