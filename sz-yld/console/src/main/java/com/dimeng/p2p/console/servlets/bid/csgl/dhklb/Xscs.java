/*
 * 文 件 名:  Xscs.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  chengweiqiang
 * 修改时间:  2015年11月9日
 */
package com.dimeng.p2p.console.servlets.bid.csgl.dhklb;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.message.email.EmailSender;
import com.dimeng.framework.message.sms.SmsSender;
import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.p2p.S71.enums.T7152_F04;
import com.dimeng.p2p.S71.enums.T7160_F07;
import com.dimeng.p2p.S71.enums.T7162_F06;
import com.dimeng.p2p.S71.enums.T7164_F07;
import com.dimeng.p2p.console.servlets.bid.AbstractBidServlet;
import com.dimeng.p2p.modules.bid.console.service.CollectionManage;
import com.dimeng.p2p.modules.bid.console.service.entity.StayRefund;
import com.dimeng.p2p.modules.systematic.console.service.EmailManage;
import com.dimeng.p2p.modules.systematic.console.service.LetterManage;
import com.dimeng.p2p.modules.systematic.console.service.SmsManage;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.EnumParser;
import com.dimeng.util.parser.IntegerParser;

/**
 * 线上催收请求处理servlet <功能详细描述>
 * 
 * @author chenweiqiang
 * @update by xiaoqi 2016年1月9日
 * @version [版本号, 2015.11.9]
 */
@Right(id = "P2P_C_BUSI_XSCS_CL", name = "线上催收",moduleId="P2P_C_BID_CSGL_DHKLB",order=3)
public class Xscs extends AbstractBidServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void processGet(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
			throws Throwable {
		processPost(request, response, serviceSession);
	}

	@Override
	protected void processPost(final HttpServletRequest request, final HttpServletResponse response,
			final ServiceSession serviceSession) throws Throwable {
		try {
			int adminId = serviceSession.getSession().getAccountId();
			String userIds = request.getParameter("userId");
			String type = request.getParameter("type");
			final String collectionType = request.getParameter("collectionType");
			final String resultDesc = request.getParameter("resultDesc");
			// String collectionIds = request.getParameter("collectionId");
			String loanRecordIds = request.getParameter("loanRecordId");
			CollectionManage collectionManage = serviceSession.getService(CollectionManage.class);
			final String adminName = collectionManage.getAdminNameById(adminId);
			if (null != userIds) {
				String[] userId = userIds.split("\\,");
				String[] loanRecordId = loanRecordIds.split("\\,");
				String content = request.getParameter("comment");
				if ("ZNX".equals(collectionType)) {
					LetterManage letterManage = serviceSession.getService(LetterManage.class);
					T7160_F07 sendType = T7160_F07.ZDR;
					String title = resultDesc;
					String userName = request.getParameter("userNames");

					String[] userNames = new String[0];
					if (!StringHelper.isEmpty(userName)) {
						userNames = userName.split("\\,");
					}

					List<String> list = Arrays.asList(userNames);
					Set<String> set = new HashSet<String>(list);
					userNames = set.toArray(new String[0]);

					letterManage.addLetter(sendType, title, content, userNames);
				}

				if ("DX".equals(collectionType)) {
					SmsManage smsManage = serviceSession.getService(SmsManage.class);
					SmsSender smsSender = serviceSession.getService(SmsSender.class);

					T7162_F06 sendType = T7162_F06.ZDR;
					String mobile = request.getParameter("mobile");

					String[] mobiles = new String[0];
					if (!StringHelper.isEmpty(mobile)) {
						mobiles = mobile.split("\\,");
					}
					List<String> mobileLists = new ArrayList<>();
					for (String s : mobiles) {
						if (!StringHelper.isEmpty(s) && !mobileLists.contains(s)) {
							Pattern pattern = Pattern.compile("^(13|14|15|17|18)[0-9]{9}$");
							Matcher m = pattern.matcher(s);
							if (m.matches()) {
								mobileLists.add(s);
							}
						}
					}
					smsManage.addSms(sendType, content, mobileLists.toArray(new String[mobileLists.size()]));
					smsSender.send(0, content, mobileLists.toArray(new String[mobileLists.size()]));
				}

				if ("YJ".equals(collectionType)) {
					EmailManage emailManage = serviceSession.getService(EmailManage.class);
					EmailSender emailSender = serviceSession.getService(EmailSender.class);
					T7164_F07 sendType = T7164_F07.ZDR;
					String title = resultDesc;
					String email = request.getParameter("email");
					String[] emails = new String[0];
					if (!StringHelper.isEmpty(email)) {
						emails = email.split("\\,");
					}
					List<String> emailLists = new ArrayList<>();
					for (String s : emails) {
						if (!StringHelper.isEmpty(s) && !emailLists.contains(s)) {
							String str = "^([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)*@([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)+[\\.][A-Za-z]{2,3}([\\.][A-Za-z]{2})?$";
							Pattern pattern = Pattern.compile(str);
							Matcher m = pattern.matcher(s);
							if (m.matches()) {
								emailLists.add(s);
							}
						}
					}
					emailManage.addEmail(sendType, title, content, emailLists.toArray(new String[emailLists.size()]));
					for (String e : emailLists) {
						emailSender.send(0, title, content, e);
					}
				}
				
				for (int i = 0; i < userId.length; i++) {
					final String id = userId[i];
					final String loanId = loanRecordId[i];
					String method = "";
					if (collectionType.equals("DX")) {
						method = "DX";
					}
					//插入催收记录
					collectionManage.stayRefundDispose(new StayRefund() {
						@Override
						public int getUserId() {
							return IntegerParser.parse(id);
						}

						@Override
						public String getResultDesc() {
							return resultDesc;
						}

						@Override
						public int getLoanRecordId() {
							return IntegerParser.parse(loanId);
						}

						@Override
						public T7152_F04 getCollectionType() {
							return EnumParser.parse(T7152_F04.class, collectionType);
						}

						@Override
						public String getCollectionPerson() {

							return adminName;
						}

						@Override
						public Timestamp getCollectionTime() {
							Date date = new Date();
							return new Timestamp(date.getTime());
						}

						@Override
						public String getComment() {
							return request.getParameter("comment");
						}
					}, method, "XSCS");
				}
			}

			if ("near".equals(type)) {
				// 重定向
				sendRedirect(request, response, getController().getURI(request, JsstList.class));
			} else if ("less".equals(type)) {
				// 重定向
				sendRedirect(request, response, getController().getURI(request, YqList.class));
			} else {
				// 重定向
				sendRedirect(request, response, getController().getURI(request, YzyqList.class));
			}
		} catch (Throwable throwable) {
			if (throwable instanceof LogicalException || throwable instanceof ParameterException) {
				getController().prompt(request, response, PromptLevel.ERROR, throwable.getMessage());
				sendRedirect(request, response, getController().getURI(request, JsstList.class));
			} else {
				onThrowable(request, response, throwable);
			}
		}

	}
}
