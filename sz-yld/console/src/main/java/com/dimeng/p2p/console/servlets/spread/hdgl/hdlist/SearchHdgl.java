package com.dimeng.p2p.console.servlets.spread.hdgl.hdlist;

import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S63.enums.T6340_F03;
import com.dimeng.p2p.S63.enums.T6340_F04;
import com.dimeng.p2p.S63.enums.T6340_F08;
import com.dimeng.p2p.modules.activity.console.service.ActivityManage;
import com.dimeng.p2p.modules.activity.console.service.entity.ActivityList;
import com.dimeng.p2p.modules.activity.console.service.entity.ActivityQuery;
import com.dimeng.p2p.console.servlets.spread.AbstractSpreadServlet;
import com.dimeng.util.parser.EnumParser;
import com.dimeng.util.parser.IntegerParser;
import com.dimeng.util.parser.TimestampParser;
@Right(id = "P2P_C_SPREAD_HDGL_SEARCHHDGL",isMenu=true, name = "活动管理", moduleId = "P2P_C_SPREAD_HDGL_HDLIST", order = 0)
public class SearchHdgl extends AbstractSpreadServlet{

	private static final long serialVersionUID = 1L;

	@Override
	protected void processGet(HttpServletRequest request,
			HttpServletResponse response, ServiceSession serviceSession)
			throws Throwable {
		processPost(request, response, serviceSession);
	}

	@Override
	protected void processPost(final HttpServletRequest request,
			HttpServletResponse response, ServiceSession serviceSession)
			throws Throwable {
		ActivityManage manage = serviceSession.getService(ActivityManage.class);
		Paging paging = new Paging() {
			
			@Override
			public int getSize() {
				return 10;
			}
			
			@Override
			public int getCurrentPage() {
				return IntegerParser.parse(request.getParameter(PAGING_CURRENT));
			}
		};
		
		ActivityQuery query = new ActivityQuery() {

			@Override
			public String code() {
				
				return request.getParameter("code");
			}

			@Override
			public String name() {
				return request.getParameter("name");
			}

			@Override
			public T6340_F03 jlType() {
				return EnumParser.parse(T6340_F03.class, request.getParameter("jlType"));
			}

			@Override
			public T6340_F04 hdType() {
				return EnumParser.parse(T6340_F04.class, request.getParameter("hdType"));
			}

			@Override
			public Timestamp startsTime() {
				return TimestampParser.parse(request.getParameter("startsTime"));
			}

			@Override
			public Timestamp starteTime() {
				return TimestampParser.parse(request.getParameter("starteTime"));
			}

			@Override
			public Timestamp endsTime() {
				return TimestampParser.parse(request.getParameter("endsTime"));
			}

			@Override
			public Timestamp endeTime() {
				return TimestampParser.parse(request.getParameter("endeTime"));
			}

			@Override
			public T6340_F08 status() {
				return EnumParser.parse(T6340_F08.class, request.getParameter("zt"));
			}
			
		};
		PagingResult<ActivityList> result = manage.searchActivity(query, paging);
		request.setAttribute("result", result);
		forwardView(request, response, getClass());
	}

	
	

}
