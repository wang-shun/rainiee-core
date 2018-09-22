/*
 * 文 件 名:  Index.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  huanggang
 * 修改时间:  2014年11月3日
 */
package com.dimeng.p2p.front.servlets.password;

import java.io.IOException;
import java.sql.Timestamp;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.config.Envionment;
import com.dimeng.framework.http.servlet.Controller;
import com.dimeng.framework.http.session.Session;
import com.dimeng.framework.http.session.authentication.AuthenticationException;
import com.dimeng.framework.http.session.authentication.VerifyCodeAuthentication;
import com.dimeng.framework.message.email.EmailSender;
import com.dimeng.framework.resource.ResourceRegister;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.account.front.service.PasswordManage;
import com.dimeng.p2p.account.user.service.EmailVerifyCodeManage;
import com.dimeng.p2p.account.user.service.SafetyManage;
import com.dimeng.p2p.common.DESEncryptor;
import com.dimeng.p2p.common.EmailTypeUtil;
import com.dimeng.p2p.common.FormToken;
import com.dimeng.p2p.common.PhoneTypeUtil;
import com.dimeng.p2p.variables.defines.EmailVariavle;
import com.dimeng.p2p.variables.defines.MessageVariable;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.p2p.variables.defines.URLVariable;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.DateTimeParser;

/**
 * 
 * <一句话功能简述>
 * <功能详细描述>
 * @update by xiaoqi
 * @version  [版本号, 2015年11月24日]
 */
public class Index extends AbstractPasswordServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void processGet(HttpServletRequest request, HttpServletResponse response,
			ServiceSession serviceSession) throws Throwable {

	}

	@Override
	protected void processPost(HttpServletRequest request, HttpServletResponse response,
			ServiceSession serviceSession) throws Throwable {

		String phoneCode = request.getParameter("phoneCode");
		String emailCode = request.getParameter("emailCode");
		String accountCode = request.getParameter("accountCode");
		String email = request.getParameter("email");
		String phone = request.getParameter("phone");
		String accountName = request.getParameter("accountName");
		String type = request.getParameter("type");
		String key=null;
		request.setAttribute("type", type);
		if ("email".equals(type)) {
			if (!FormToken.verify(serviceSession.getSession(), request.getParameter("tokenEmail"))) {
				request.setAttribute("EMAIL_ERROR", "请不要重复提交请求！");
				forward(request, response, getController().getViewURI(request, Index.class));
				return;
			}
		} else if ("phone".equals(type)) {
			if (!FormToken.verify(serviceSession.getSession(), request.getParameter("tokenPhone"))) {
				request.setAttribute("PHONE_ERROR", "请不要重复提交请求！");
				forward(request, response, getController().getViewURI(request, Index.class));
				return;
			}
		} else if ("accountName".equals(type)) {
			if (!FormToken.verify(serviceSession.getSession(),
					request.getParameter("tokenAccountName"))) {
				request.setAttribute("ACCOUNTNAME_ERROR", "请不要重复提交请求！");
				forward(request, response, getController().getViewURI(request, Index.class));
				return;
			}
		}

		Session session = serviceSession.getSession();
		PasswordManage passwordManage = serviceSession.getService(PasswordManage.class);
		VerifyCodeAuthentication authentication = new VerifyCodeAuthentication();
		SafetyManage safetyManage = serviceSession.getService(SafetyManage.class);
		EmailVerifyCodeManage evcManage=serviceSession.getService(EmailVerifyCodeManage.class);
		ConfigureProvider configureProvider = ResourceRegister.getResourceProvider(getServletContext()).getResource(ConfigureProvider.class);
		String verifySfxyyzm = configureProvider.getProperty(SystemVariable.SFXYYZM);
		if ("email".equals(type)) {
			//1.1 判断邮箱地址是否为空
			String accountType = request.getParameter("radio");
			if (StringHelper.isEmpty(email)) {
				request.setAttribute("EMAIL_ERROR", "邮箱地址不能为空");
				forward(request, response, getController().getViewURI(request, Index.class));
				return;
			}
			
			//1.2 判断图片验证码是否正确
			authentication.setVerifyCode(emailCode);
			authentication.setVerifyCodeType(FIND_PASSWORD_EMAIL);
			if (!"false".equalsIgnoreCase(verifySfxyyzm)) {
				session.authenticateVerifyCode(authentication);
			}

			//1.3 
			int id = passwordManage.emailExist(email, accountType);
			if (id <= 0) {
				request.setAttribute("EMAIL_ERROR", "邮箱未被注册");
				forward(request, response, getController().getViewURI(request, Index.class));
				return;
			}
			// 获取此邮箱今天已发送验证码次数
			Integer count = safetyManage.sendEmailCount(email,EmailTypeUtil.getEmailType("emailpwd"));
			if (count >= Integer.parseInt(configureProvider.getProperty(MessageVariable.PHONE_MAX_COUNT))) {
				request.setAttribute("EMAIL_ERROR", "此邮箱当天获取验证邮件次数已达上限！");
				forward(request, response, getController().getViewURI(request, Index.class));
				return;
			}
			// 查询数据库已经错误次数
			Integer ecount = safetyManage.phoneMatchVerifyCodeErrorCount(email,EmailTypeUtil.getEmailType("emailpwd"));
			// 和设置的常量比较
			if (ecount >= Integer.parseInt(configureProvider
					.getProperty(MessageVariable.PHONE_VERIFYCODE_MAX_ERROR_COUNT))) {
				request.setAttribute("EMAIL_ERROR", "此邮箱当天匹配验证邮件错误次数已达上限！");
				forward(request, response, getController().getViewURI(request, Index.class));
				return;
			}
			
			String senType="emailpwd|"+email;
			String code=evcManage.getVerifyCode(id, senType);
			
			// key= email&code&type&accountType
			key=DESEncryptor.encrypt(email+"&"+code+"&0&"+accountType);
			logger.info("key加密前："+email+"&"+code+"&0&"+accountType);
			String url = configureProvider.getProperty(URLVariable.INDEX) + getController().getURI(request, EmailCallBack.class) + "?key="+key;
			url = StringHelper.format(url,this.getResourceProvider().getResource(ConfigureProvider.class));
			logger.info(url);
			
			//邮件发送
			EmailSender emailSender = serviceSession.getService(EmailSender.class);
			String tem = configureProvider.getProperty(EmailVariavle.FIND_PASSWORD);
			Envionment envionment = configureProvider.createEnvionment();
			envionment.set("date", DateTimeParser.format(new Timestamp(System.currentTimeMillis())));
			envionment.set("url", url);
			emailSender.send(EmailTypeUtil.getEmailType("emailpwd"),EmailVariavle.FIND_PASSWORD.getDescription(),StringHelper.format(tem, envionment), email);
			
			session.setAttribute("PASSWORD_ACCOUNT_ID", Integer.toString(id));
			session.setAttribute("RESET_FLAG", "0");
			session.setAttribute("EMAIL_PWD", email);
			session.invalidVerifyCode(FIND_PASSWORD_EMAIL);
			request.setAttribute("showMsg", "true");
			request.setAttribute("email", email);
			forward(request, response, getController().getViewURI(request, Index.class));
			return;
		} else if ("phone".equals(type)) {
			String accountType = request.getParameter("radio");
			if (StringHelper.isEmpty(phone)) {
				request.setAttribute("PHONE_ERROR", "手机号码不能为空");
				forward(request, response, getController().getViewURI(request, Index.class));
				return;
			}
			authentication.setVerifyCode(phoneCode);
			authentication.setVerifyCodeType(FIND_PASSWORD_PHONE);
			if (!"false".equalsIgnoreCase(verifySfxyyzm)) {
				session.authenticateVerifyCode(authentication);
			}

			int id = passwordManage.phoneExist(phone, accountType);
			if (id <= 0) {
                request.setAttribute("PHONE_ERROR", "手机号码不存在或不匹配");
				forward(request, response, getController().getViewURI(request, Index.class));
				return;
			}
			session.setAttribute("TEL_PHONE", phone);
			session.setAttribute("RESET_FLAG", "1");// 0:表示邮箱 1:表示通过手机

			// 获取此手机今天已发送验证码次数
			Integer count = safetyManage.bindPhoneCount(phone,
					PhoneTypeUtil.getPhoneType("phonepwd"));
			if (count >= Integer.parseInt(configureProvider
					.getProperty(MessageVariable.PHONE_MAX_COUNT))) {
				request.setAttribute("PHONE_ERROR", "此手机号码当天获取验证码次数已达上限！");
				forward(request, response, getController().getViewURI(request, Index.class));
				return;
			}
			// 获取访问者ip地址
			Controller controller = serviceSession.getController();
			String ip = controller.getRemoteAddr(request);
			// 获取IP地址今天已发送手机验证码次数
			Integer ipcount = safetyManage.iPAddrSendSmsCount(ip,
					PhoneTypeUtil.getPhoneType("phonepwd"));
			if (ipcount >= Integer.parseInt(configureProvider
					.getProperty(MessageVariable.IP_SEND_MAX_COUNT))) {
				request.setAttribute("PHONE_ERROR", "当前IP地址，此功能今天获取手机验证码次数已达上限！");
				forward(request, response, getController().getViewURI(request, Index.class));
				return;
			}
			// 当日该手机与验证码匹配错误次数
			Integer pcount = safetyManage.phoneMatchVerifyCodeErrorCount(phone,
					PhoneTypeUtil.getPhoneType("phonepwd"));

			if (pcount >= Integer.parseInt(configureProvider
					.getProperty(MessageVariable.PHONE_VERIFYCODE_MAX_ERROR_COUNT))) {
				request.setAttribute("PHONE_ERROR", "此手机号码当天匹配验证码错误次数已达上限！");

				forward(request, response, getController().getViewURI(request, Index.class));
				return;
			}
			session.setAttribute("accountType", accountType);
			session.setAttribute("PASSWORD_ACCOUNT_ID", Integer.toString(id));
			session.setAttribute("ERROR_CHECK_COUNT", "0");
			sendRedirect(request, response, getController().getViewURI(request, Mmbh.class));
		} else if ("accountName".equals(type)) {
			if (StringHelper.isEmpty(accountName)) {
                request.setAttribute("ACCOUNTNAME_ERROR", "用户名不能为空");
				forward(request, response, getController().getViewURI(request, Index.class));
				return;
			}
			authentication.setVerifyCode(accountCode);
			authentication.setVerifyCodeType(FIND_PASSWORD_ACCOUNTNAME);
			if (!"false".equalsIgnoreCase(verifySfxyyzm)) {
				session.authenticateVerifyCode(authentication);
			}
			int id = passwordManage.accountNameExist(accountName);
			if (id <= 0) {
                request.setAttribute("ACCOUNTNAME_ERROR", "用户名不存在");
				forward(request, response, getController().getViewURI(request, Index.class));
				return;
			}
			int count = passwordManage.questionNum(id);
			if (count < 3) {
                request.setAttribute("ACCOUNTNAME_ERROR", "用户未设置密保问题或者密保问题未设置全");
				forward(request, response, getController().getViewURI(request, Index.class));
				return;
			}
			session.setAttribute("PASSWORD_ACCOUNT_ID", Integer.toString(id));
			session.setAttribute("ERROR_CHECK_COUNT", "0");
			sendRedirect(request, response, getController().getViewURI(request, Mbwt.class));
		} else {
			sendRedirect(request, response, getController().getViewURI(request, Index.class));
			return;
		}
	}

	@Override
	protected void onThrowable(HttpServletRequest request, HttpServletResponse response,
			Throwable throwable) throws ServletException, IOException {
		if (throwable instanceof AuthenticationException) {
			logger.error(throwable,throwable);
			String type = request.getParameter("type");
			if ("email".equals(type)) {
				request.setAttribute("EMAIL_VERIFYCODE_ERROR", "验证码错误");
				forward(request, response, getController().getViewURI(request, Index.class));
			} else if ("phone".equals(type)) {
				request.setAttribute("PHONE_VERIFYCODE_ERROR", "验证码错误");
				forward(request, response, getController().getViewURI(request, Index.class));
			} else {
				request.setAttribute("ACCOUNTNAME_VERIFYCODE_ERROR", "验证码错误");
				forward(request, response, getController().getViewURI(request, Index.class));
			}
		}
	}
}
