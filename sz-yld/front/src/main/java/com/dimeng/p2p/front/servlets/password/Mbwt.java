package com.dimeng.p2p.front.servlets.password;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.session.Session;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.digest.UnixCrypt;

import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.SystemConst;
import com.dimeng.p2p.account.user.service.SafetyManage;
import com.dimeng.p2p.common.FormToken;
import com.dimeng.p2p.common.RSAUtils;
import com.dimeng.util.StringHelper;

public class Mbwt extends AbstractPasswordServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void processPost(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
			throws Throwable {

		logger.info("密保问题 start");

		PrintWriter out = response.getWriter();
		String question1 = request.getParameter("question1");
		String question2 = request.getParameter("question2");
		String question3 = request.getParameter("question3");
		String errorCount = request.getParameter("errorCount");// 当前验证错误次数
		// 安全问题，需要使用session中的用户id
		// String accountId = request.getParameter("accountId");
		String accountId = serviceSession.getSession().getAttribute("PASSWORD_ACCOUNT_ID");
		if(StringHelper.isEmpty(accountId)){
			out.write("{'num':2,'msg':'系统异常，请稍后重试！'}");
			return;
		}
		String[] q1 = question1.split(":");
		String[] q2 = question2.split(":");
		String[] q3 = question3.split(":");
		if (q1.length < 2 ||q2.length < 2 || q3.length < 2) {
			out.write("{'num':1,'msg':'密保问题答案不能为空！'}");
			return;
		}
		String questionId1 = q1[0];
		String answer1 = q1[1];
		String questionId2 = q2[0];
		String answer2 = q2[1];
		String questionId3 = q3[0];
		String answer3 = q3[1];
		StringBuffer log = new StringBuffer("密保问题提交 入参:");
		log.append(",questionId=" + questionId1).append(",answer=" + answer1).append(",errorCount=" + errorCount)
				.append(",accountId=" + accountId);
		logger.info(log.toString());
		// 生成token
		FormToken.hidden(serviceSession.getSession());
		// 获取token的值
		String tokenNew = serviceSession.getSession().getAttribute(SystemConst.TOKEN_NAME);
		SafetyManage safetyManage = serviceSession.getService(SafetyManage.class);
		// int errors = 0;

		String inputPwdValue1 = RSAUtils.decryptStringByJs(answer1);
		String inputPwdValue2 = RSAUtils.decryptStringByJs(answer2);
		String inputPwdValue3 = RSAUtils.decryptStringByJs(answer3);
		inputPwdValue1 = UnixCrypt.crypt(inputPwdValue1, DigestUtils.sha256Hex(inputPwdValue1));
		inputPwdValue2 = UnixCrypt.crypt(inputPwdValue2, DigestUtils.sha256Hex(inputPwdValue2));
		inputPwdValue3 = UnixCrypt.crypt(inputPwdValue3, DigestUtils.sha256Hex(inputPwdValue3));
		String dbValue1 = safetyManage.getUserAnswerByQuestionId(Integer.parseInt(accountId),Integer.parseInt(questionId1));
		String dbValue2 = safetyManage.getUserAnswerByQuestionId(Integer.parseInt(accountId),Integer.parseInt(questionId2));
		String dbValue3 = safetyManage.getUserAnswerByQuestionId(Integer.parseInt(accountId),Integer.parseInt(questionId3));
		if (!inputPwdValue1.equals(dbValue1) || !inputPwdValue2.equals(dbValue2) || !inputPwdValue3.equals(dbValue3)) {
			// errors++;
			out.write("{'num':3,'msg':'输入密保答案不正确','tokenNew':'" + tokenNew + "'}");
			return;
		}
		//校验成功后，标记校验状态
		Session session = serviceSession.getSession();
		session.setAttribute(session.getAttribute("PASSWORD_ACCOUNT_ID"), "CHECK_SUCCESS");
		logger.info("密保问题 end");
		out.write("{'num':0,'msg':'','tokenNew':'" + tokenNew + "'}");
	}
}
