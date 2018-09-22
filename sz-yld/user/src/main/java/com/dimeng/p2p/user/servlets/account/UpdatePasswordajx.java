package com.dimeng.p2p.user.servlets.account;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.digest.UnixCrypt;

import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.account.user.service.SafetyManage;
import com.dimeng.util.StringHelper;

public class UpdatePasswordajx extends AbstractAccountServlet{
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void processPost(final HttpServletRequest request,
			HttpServletResponse response, final ServiceSession serviceSession)
			throws Throwable {
		SafetyManage safetyManage = serviceSession.getService(SafetyManage.class);
		String ph = safetyManage.get().phoneNumber;
		PrintWriter out=response.getWriter();
		String newpassword = request.getParameter("code");
		String phonenumber = request.getParameter("number");
		if(StringHelper.isEmpty(newpassword)){
			out.write("02");
			return;
		}
		if(StringHelper.isEmpty(phonenumber) || !ph.equals(phonenumber)){
			out.write("01");
			return;
		}
		safetyManage.updatePassword(UnixCrypt.crypt(newpassword,
				DigestUtils.sha256Hex(newpassword)));
	}
	
}
