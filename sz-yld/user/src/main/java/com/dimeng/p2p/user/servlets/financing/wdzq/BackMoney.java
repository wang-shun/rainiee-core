package com.dimeng.p2p.user.servlets.financing.wdzq;

import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.servlet.annotation.PagingServlet;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.common.enums.QueryType;
import com.dimeng.p2p.modules.bid.user.service.WdzqManage;
import com.dimeng.p2p.modules.bid.user.service.entity.BackOffList;
import com.dimeng.p2p.modules.bid.user.service.query.BackOffQuery;
import com.dimeng.p2p.user.servlets.financing.AbstractFinancingServlet;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.DateParser;
import com.dimeng.util.parser.EnumParser;
import com.dimeng.util.parser.IntegerParser;

import org.codehaus.jackson.map.ObjectMapper;

@PagingServlet(itemServlet = BackMoney.class)
public class BackMoney extends AbstractFinancingServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 19842188160942532L;

	@Override
	protected void processPost(final HttpServletRequest request,
			final HttpServletResponse response,final ServiceSession serviceSession)
			throws Throwable {
		WdzqManage offManage =  serviceSession.getService(WdzqManage.class);
		
		PagingResult<BackOffList> boList=offManage.searchList(new BackOffQuery() {

			@Override
			public Date getTimeStart() {
				Date ts = DateParser.parse(request.getParameter("timeStart"), "yyyy-MM-dd");
				request.setAttribute("timeStart", ts);
				return ts;
			}

			@Override
			public Date getTimeEnd() {
				Date ts = DateParser.parse(request.getParameter("timeEnd"), "yyyy-MM-dd");
				request.setAttribute("timeEnd", ts);
				return ts;
			}

			@Override
			public QueryType getQueryType() {
				String queryType = request.getParameter("queryType");
				if (!StringHelper.isEmpty(queryType)) {
					request.setAttribute("queryType", queryType);
					return EnumParser.parse(QueryType.class, queryType);
				}
				return QueryType.DS;
			}
		}, new Paging() {
			@Override
			public int getCurrentPage() {
				return IntegerParser.parse(request.getParameter("currentPage"));
			}
			@Override
			public int getSize() {
				return IntegerParser.parse(request.getParameter("pageSize"));
			}
		});

		WdzqManage wdzqManage = serviceSession.getService(WdzqManage.class);
		//封装JSON
		ObjectMapper objectMapper = new ObjectMapper();
		PrintWriter out = response.getWriter();
		BigDecimal totalMoney = offManage.searchList(request.getParameter("queryType"),request.getParameter("timeStart"),request.getParameter("timeEnd"));;
		String pageStr = wdzqManage.rendPaging(boList);
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		jsonMap.put("pageStr", pageStr);
		jsonMap.put("pageCount", boList.getPageCount());
		jsonMap.put("hzcxList", boList.getItems());
		jsonMap.put("totalMoney", totalMoney);
		out.print(objectMapper.writeValueAsString(jsonMap));
		out.close();

//		request.setAttribute("boList", boList);
//		forwardView(request, response, getClass());
		
	}
	
	@Override
	protected void processGet(HttpServletRequest request,
			HttpServletResponse response, ServiceSession serviceSession)
			throws Throwable {
		processPost(request, response, serviceSession);
	}

}
