package com.dimeng.p2p.user.servlets.account;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.digest.UnixCrypt;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.http.servlet.Controller;
import com.dimeng.framework.http.session.authentication.AuthenticationException;
import com.dimeng.framework.resource.ResourceProvider;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.account.user.service.SafetyManage;
import com.dimeng.p2p.account.user.service.entity.Safety;
import com.dimeng.p2p.common.RSAUtils;
import com.dimeng.p2p.user.servlets.Login;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.util.StringHelper;

public class pdatetxPasswordajx extends AbstractAccountServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void processPost(final HttpServletRequest request,
			HttpServletResponse response, final ServiceSession serviceSession)
			throws Throwable {
		SafetyManage safetyManage = serviceSession.getService(SafetyManage.class);
		Safety sa = safetyManage.get();
		PrintWriter out=response.getWriter();
		String newpassword = StringHelper.isEmpty(request.getParameter("new")) ? "" : request.getParameter("new");
        newpassword = RSAUtils.decryptStringByJs(newpassword);
		String utype = request.getParameter("utp");
		String number = request.getParameter("number");
		utype = utype+"|"+number;
		String istrue = serviceSession.getSession().getAttribute(utype+"is");

		
		ConfigureProvider configureProvider = getResourceProvider().getResource(ConfigureProvider.class);
		String mtest = configureProvider.getProperty(SystemVariable.NEW_TXPASSWORD_REGEX).substring(1,configureProvider.getProperty(SystemVariable.NEW_TXPASSWORD_REGEX).length()-1);//"^[a-zA-Z](?![a-zA-Z]*$)[a-zA-Z0-9]{7}$";
		//String mtest = "^[a-zA-Z](?![a-zA-Z]*$)[a-zA-Z0-9]{7}$";
		if (!newpassword.matches(mtest))
		{
			StringBuilder sb = new StringBuilder();
			//sb.append("密码需由八个字符组成：字母+数字，且首个字符必须是字母");
			sb.append(configureProvider.getProperty(SystemVariable.TXPASSWORD_REGEX_CONTENT));
			out.write(sb.toString());
			return;
		}

		if(StringHelper.isEmpty(istrue)){
			 out.write("09");
			return;
		}else if (StringHelper.isEmpty(newpassword)) {
			out.write("00");
			return;
		} else {
			if(sa.password.equals(UnixCrypt.crypt(newpassword,DigestUtils.sha256Hex(newpassword)))){
				out.write("01");
				return;
            }
            else if (newpassword.equals(sa.username))
            {
                out.write("04");
				return;
            }
            else
            {
                safetyManage.updateTxpassword(UnixCrypt.crypt(newpassword, DigestUtils.sha256Hex(newpassword)));
                out.write("03");
                return;
            }
		}
	}
	
	@Override
	protected void onThrowable(HttpServletRequest request,
			HttpServletResponse response, Throwable throwable)
			throws ServletException, IOException {
		
		ResourceProvider resourceProvider = getResourceProvider();
		resourceProvider.log(throwable);
		PrintWriter out=response.getWriter();
		out.write(throwable.getMessage());
		
		if (throwable instanceof AuthenticationException) {
			Controller controller = getController();
			controller.redirectLogin(request, response,
					controller.getViewURI(request, Login.class));
		}
		
	}

}
