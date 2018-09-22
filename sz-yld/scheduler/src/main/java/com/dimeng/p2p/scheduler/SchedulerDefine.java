package com.dimeng.p2p.scheduler;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.dimeng.framework.http.servlet.Controller;
import com.dimeng.framework.http.servlet.PackageRewriter;
import com.dimeng.framework.http.servlet.Rewriter;
import com.dimeng.framework.http.session.SessionManager;
import com.dimeng.framework.http.session.authentication.AuthenticationException;
import com.dimeng.framework.http.session.authentication.PasswordAuthentication;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.common.P2PSystemDefine;
import com.dimeng.p2p.modules.account.pay.service.UserManage;

public class SchedulerDefine extends P2PSystemDefine {
	protected final Rewriter rewriter;
	protected final Logger logger = Logger.getLogger(getClass());
	public SchedulerDefine() {
		super();
		schemas.put(SessionManager.class, DB_USER_SESSION);
		rewriter = new PackageRewriter("com.dimeng.p2p.scheduler.servlets");
	}

	@Override
	public String getGUID() {
		return "136a3d03-9748-4f83-a54f-9b2a93f979a0";
	}

	@Override
	public Rewriter getRewriter() {
		return rewriter;
	}

	@Override
	public int readAccountId(ServiceSession serviceSession, String accountName,
			String password) throws AuthenticationException, SQLException {
		UserManage userManage = serviceSession.getService(UserManage.class);
		return userManage.readAccountId(accountName, password);
	}

	@Override
	public void writeLog(HttpServletRequest request,
			HttpServletResponse response, ServiceSession serviceSession,
			PasswordAuthentication authentication, int accountId) {
		UserManage userManage = serviceSession.getService(UserManage.class);
		Controller controller = serviceSession.getController();
		String ip = controller.getRemoteAddr(request);
		try {
			userManage.log(accountId, ip);
		} catch (Throwable e) {
			logger.error(e, e);
		}
	}
}
