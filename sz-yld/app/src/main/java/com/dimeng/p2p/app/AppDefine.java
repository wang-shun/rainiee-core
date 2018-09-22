package com.dimeng.p2p.app;

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
import com.dimeng.p2p.account.user.service.UserManage;
import com.dimeng.p2p.app.config.AppConst;
import com.dimeng.p2p.common.P2PSystemDefine;

public class AppDefine extends P2PSystemDefine implements AppConst {
    protected final Logger logger = Logger.getLogger(getClass());
	protected final Rewriter rewriter;


	public AppDefine() {
		super();
		schemas.put(SessionManager.class, DB_USER_SESSION);
		rewriter = new PackageRewriter("com.dimeng.p2p.app.servlets"){
		    @Override
            public String getViewSuffix() {
		        return ".screen";
		    }
		};
	}

	@Override
	public String getGUID() {
		return "136a3d03-9748-4f83-a54f-9b2a93f979a0";
	}

	@Override
	public int readAccountId(ServiceSession serviceSession, String condition,
			String password) throws AuthenticationException, SQLException {
		UserManage userManage = serviceSession.getService(UserManage.class);
		return userManage.readAccountId(condition, password);
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

	@Override
	public Rewriter getRewriter() {
		return rewriter;
	}

	@Override
	public int getVerifyCodeLength() {
		return 4;
	}

}
