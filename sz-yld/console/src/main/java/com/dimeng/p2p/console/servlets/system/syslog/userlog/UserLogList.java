package com.dimeng.p2p.console.servlets.system.syslog.userlog;

import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.common.enums.FrontLogType;
import com.dimeng.p2p.console.servlets.system.AbstractSystemServlet;
import com.dimeng.p2p.modules.systematic.console.service.UserLoginLogManage;
import com.dimeng.p2p.modules.systematic.console.service.entity.UserLog;
import com.dimeng.p2p.modules.systematic.console.service.query.UserLogQuery;
import com.dimeng.util.parser.EnumParser;
import com.dimeng.util.parser.IntegerParser;
import com.dimeng.util.parser.TimestampParser;

@Right(id = "P2P_C_SYS_USERLOGLIST", isMenu = true, name = "前台日志",moduleId="P2P_C_SYS_SYSLOG_QTRZ",order=0)
public class UserLogList extends AbstractSystemServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void processPost(final HttpServletRequest request,
			HttpServletResponse response, ServiceSession serviceSession)
			throws Throwable {
		UserLoginLogManage userLoginLogManage = serviceSession
				.getService(UserLoginLogManage.class);
		UserLogQuery query = new UserLogQuery() {

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
			public String getAccountName() {
				return request.getParameter("accountName");
			}
            
            @Override
            public FrontLogType getType()
            {
                return EnumParser.parse(FrontLogType.class, request.getParameter("type"));
            }
		};
		Paging paging = new Paging() {

			@Override
			public int getSize() {
				return 10;
			}

			@Override
			public int getCurrentPage() {
				return IntegerParser
						.parse(request.getParameter(PAGING_CURRENT));
			}
		};
		PagingResult<UserLog> result = userLoginLogManage
				.seacrch(query, paging);
		request.setAttribute("result", result);
		forwardView(request, response, getClass());
	}

	@Override
	protected void processGet(final HttpServletRequest request,
			HttpServletResponse response, ServiceSession serviceSession)
			throws Throwable {
		processPost(request, response, serviceSession);
	}
}
