package com.dimeng.p2p.console.servlets.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.service.UserStatisticsManage;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.S61.enums.T6130_F09;
import com.dimeng.p2p.S62.enums.T6230_F20;
import com.dimeng.p2p.S62.enums.T6260_F07;
import com.dimeng.p2p.S62.enums.T6281_F14;
import com.dimeng.p2p.S62.enums.T6282_F11;
import com.dimeng.p2p.S71.enums.T7150_F05;
import com.dimeng.p2p.console.servlets.AbstractConsoleServlet;
import com.dimeng.p2p.modules.systematic.console.service.SysUserManage;
import com.dimeng.p2p.modules.systematic.console.service.ToDoThings;
import com.dimeng.p2p.modules.systematic.console.service.entity.IndexCount;
import com.dimeng.p2p.modules.systematic.console.service.entity.ToDoThingsEntity;
import com.dimeng.p2p.variables.P2PConst;

public class Index extends AbstractConsoleServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void processGet(HttpServletRequest request,
			HttpServletResponse response, ServiceSession serviceSession)
			throws Throwable {
		processPost(request, response, serviceSession);
	}

	@Override
	protected void processPost(HttpServletRequest request,
			HttpServletResponse response, ServiceSession serviceSession)
			throws Throwable {
		SysUserManage index = serviceSession.getService(SysUserManage.class);
		IndexCount indexCount = index.getIndexCount();
		
		boolean isOneLogin = index.isOneLogin();
		UserStatisticsManage userStatisticsManage = serviceSession
				.getService(UserStatisticsManage.class);
		indexCount.todayLoginUser = userStatisticsManage.getLoginCount(null,
				P2PConst.DB_USER_SESSION);
		indexCount.onlineUser = userStatisticsManage
				.getOnlineCount(P2PConst.DB_USER_SESSION);
		
		//待办事项实体类
		ToDoThingsEntity todoEntity = new ToDoThingsEntity();
		ToDoThings thing = serviceSession.getService(ToDoThings.class);
		
		todoEntity.setDshProCount(thing.queryDshProCount(T6230_F20.DSH.toString()));
		todoEntity.setDfbProCount(thing.queryDshProCount(T6230_F20.DFB.toString()));
		todoEntity.setDfkProCount(thing.queryDshProCount(T6230_F20.DFK.toString()));
		todoEntity.setDshOwnLoanCount(thing.queryDshOwnLoanCountCount(T6282_F11.WCL.toString()));
		todoEntity.setDshEnterpriseLoanCount(thing.queryDshEnterpriseLoanCount(T6281_F14.WCL.toString()));
		todoEntity.setDshAuthCount(thing.queryDshAuthCount(T6230_F20.DSH.toString()));
		todoEntity.setTxTrialCount(thing.queryTxTrialCount(T6130_F09.DSH.toString()));
		todoEntity.setTxReviewCount(thing.queryTxTrialCount(T6130_F09.DFK.toString()));
		todoEntity.setUnderLineChargingCount(thing.queryunderLineChargingCount(T7150_F05.DSH.toString()));
		todoEntity.setAssignmentCount(thing.queryAssignmentCount(T6260_F07.DSH.toString()));
		
		request.setAttribute("indexCount", indexCount);
		request.setAttribute("isOneLogin", isOneLogin);
		request.setAttribute("todoEntity", todoEntity);
		forwardView(request, response, Index.class);
	}

}
