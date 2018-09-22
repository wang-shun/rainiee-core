package com.dimeng.p2p.user.servlets.ret;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.service.ServiceSession;

/**
 * 双乾实名认证回调
 */
public class NciicCallBack extends AbstractShuangQianServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4317895893733042366L;

	@Override
	protected void processPost(HttpServletRequest request,
			HttpServletResponse response, ServiceSession serviceSession) throws Throwable {
		printMark(response, "SUCCESS");
	}
}
