package com.dimeng.p2p.console.servlets.statistics.ywtj.ywytj;

import com.dimeng.framework.http.entity.RoleBean;
import com.dimeng.framework.http.service.RoleManage;
import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S71.entities.T7190;
import com.dimeng.p2p.console.servlets.AbstractBuisnessServlet;
import com.dimeng.p2p.repeater.business.SysBusinessManage;
import com.dimeng.p2p.repeater.business.entity.Performance;
import com.dimeng.p2p.repeater.business.query.PerformanceQuery;
import com.dimeng.util.parser.IntegerParser;
import com.dimeng.util.parser.TimestampParser;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
/**
 * 
 * 业务员业绩统计
 * <功能详细描述>
 * 
 * @author  heluzhu
 * @version  [版本号, 2015年12月2日]
 */
@Right(id = "P2P_C_STATISTICS_BUSINESS", name = "业务员业绩统计", moduleId = "P2P_C_STATISTICS_YWTJ_YWYTJ", order = 0)
public class BusinessStatistics extends AbstractBuisnessServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void processGet(final HttpServletRequest request,
			HttpServletResponse response, ServiceSession serviceSession)
			throws Throwable {
		processPost(request, response, serviceSession);
	}

	@Override
	protected void processPost(final HttpServletRequest request,
			HttpServletResponse response, ServiceSession serviceSession)
			throws Throwable {
		
		
	    SysBusinessManage sysBusinessManage = serviceSession
				.getService(SysBusinessManage.class);
		RoleManage roleManage = serviceSession.getService(RoleManage.class);
		
		PerformanceQuery query = new PerformanceQuery() {
			
			
			@Override
			public String getEmployNum() {
				return request.getParameter("employNum");
			}
			
			@Override
			public Timestamp getCreateTimeStart() {
				return TimestampParser.parse(request
						.getParameter("createTimeStart"));
			}
			
			
			@Override
			public Timestamp getCreateTimeEnd() {
				return TimestampParser.parse(request
						.getParameter("createTimeEnd"));
			}

			@Override
			public String getName() {
				return request.getParameter("name");
			}
		};
		
		
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
			PagingResult<RoleBean> roles = roleManage.search(null, new Paging() {

				@Override
				public int getSize() {
					return Integer.MAX_VALUE;
				}

				@Override
				public int getCurrentPage() {
					return 1;
				}
			});
			request.setAttribute("roles", roles.getItems());
			PagingResult<T7190> result = sysBusinessManage.serarchTjgl(query, paging);
			Performance performance=sysBusinessManage.findPerformances();
			request.setAttribute("performance", performance);
			request.setAttribute("performanceDetail",sysBusinessManage.findPerformance(query));
		request.setAttribute("result", result);
		
		forwardView(request, response, BusinessStatistics.class);
	}
	@Override
	protected void onThrowable(HttpServletRequest request,
			HttpServletResponse response, Throwable throwable)
			throws ServletException, IOException {
		super.onThrowable(request, response, throwable);
	}
}
