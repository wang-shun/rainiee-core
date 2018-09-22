package com.dimeng.p2p.front.servlets.password;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.digest.UnixCrypt;

import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.account.front.service.PasswordManage;
import com.dimeng.p2p.common.RSAUtils;
import com.dimeng.util.parser.IntegerParser;

public class CheckPsd extends AbstractPasswordServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void processPost(HttpServletRequest request,
			HttpServletResponse response, ServiceSession serviceSession)
			throws Throwable {
		PasswordManage passwordManage = serviceSession.getService(PasswordManage.class);
        String password = RSAUtils.decryptStringByJs(request.getParameter("password"));
		int id = IntegerParser.parse(serviceSession.getSession().getAttribute("PASSWORD_ACCOUNT_ID"));
		String jypsd = passwordManage.getJyPassword(id);
        password = UnixCrypt.crypt(password, DigestUtils.sha256Hex(password));
		if(password.equals(jypsd)){
			response.getWriter().write("01");
			return;	
		}
	}
}
