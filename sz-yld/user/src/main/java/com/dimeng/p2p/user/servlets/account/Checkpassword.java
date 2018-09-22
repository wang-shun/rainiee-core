package com.dimeng.p2p.user.servlets.account;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.resource.ResourceRegister;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.p2p.variables.defines.pays.PayVariavle;
import com.dimeng.util.parser.BooleanParser;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.digest.UnixCrypt;

import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.account.user.service.SafetyManage;
import com.dimeng.p2p.account.user.service.entity.Safety;
import com.dimeng.util.StringHelper;

public class Checkpassword extends AbstractAccountServlet{
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void processPost(final HttpServletRequest request,
			HttpServletResponse response, final ServiceSession serviceSession)
			throws Throwable {
		//校验对象
		String codeType = request.getParameter("ctp");
		String cardValue = request.getParameter("cardValue");
		final ConfigureProvider configureProvider = ResourceRegister.getResourceProvider(getServletContext()).getResource(ConfigureProvider.class);
		boolean CHARGE_MUST_WITHDRAWPSD = BooleanParser.parse(configureProvider.getProperty(PayVariavle.CHARGE_MUST_WITHDRAWPSD));
		boolean tg = BooleanParser.parse(configureProvider.getProperty(SystemVariable.SFZJTG));
		//系统不需要交易密码或为托管时，不校验交易密码
		if(!CHARGE_MUST_WITHDRAWPSD){
			codeType = codeType + "|" + cardValue;
			serviceSession.getSession().setAttribute(codeType + "is", "true");
			return;
		}
		SafetyManage safetyManage = serviceSession.getService(SafetyManage.class);
		Safety sa = safetyManage.get();
		 PrintWriter out=response.getWriter();
		 String value = request.getParameter("value");
		if(StringHelper.isEmpty(value)){
			out.write("01");
			return;
		}
		String password = UnixCrypt.crypt(value,
				DigestUtils.sha256Hex(value));
		
		if(!password.equals(sa.txpassword)){
			out.write("01");
			return;	
		}
		codeType = codeType + "|" + cardValue;
		serviceSession.getSession().setAttribute(codeType + "is", "true");
	}
	
}
