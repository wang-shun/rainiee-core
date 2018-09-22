package com.dimeng.p2p.console.servlets.info.bzzx.tzyhk;

import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.http.upload.UploadFile;
import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.p2p.S50.enums.T5011_F02;
import com.dimeng.p2p.common.enums.ArticlePublishStatus;
import com.dimeng.p2p.console.servlets.info.AbstractInformationServlet;
import com.dimeng.p2p.modules.base.console.service.ArticleManage;
import com.dimeng.p2p.modules.base.console.service.entity.Question;
import com.dimeng.p2p.modules.base.console.service.entity.QuestionRecord;
import com.dimeng.util.parser.EnumParser;
import com.dimeng.util.parser.IntegerParser;
import com.dimeng.util.parser.TimestampParser;

@Right(id = "P2P_C_INFO_BZZX_MENU", name = "帮助中心",moduleId="P2P_C_INFO_BZZX",order=0)
public class UpdateTzyhkWt extends AbstractInformationServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void processPost(final HttpServletRequest request,
			final HttpServletResponse response,
			final ServiceSession serviceSession) throws Throwable {
		try {
			ArticleManage manage = serviceSession
					.getService(ArticleManage.class);
			
			Question question = new Question() {
				@Override
				public Timestamp publishTime() {
					return TimestampParser.parse(request
							.getParameter("publishTime"));
				}
				
				@Override
				public String getQuestionType() {
					return request.getParameter("questionType");
				}
				
				@Override
				public ArticlePublishStatus getPublishStatus() {
					return EnumParser.parse(ArticlePublishStatus.class,
							request.getParameter("publishStatus"));
				}
				
				@Override
				public UploadFile getImage() throws Throwable {
					return null;
				}

				@Override
				public int getQuestionTypeID() {
					return request.getParameter("questionTypeID") == null ? 0 : Integer.parseInt(request.getParameter("questionTypeID"));
				}

				@Override
				public String getQuestion() {
					return request.getParameter("question");
				}

				@Override
				public String getQuestionAnswer() {
					return request.getParameter("questionAnswer");
				}
			};
			manage.updateQuestion(T5011_F02.TZYHK,question,IntegerParser.parse(request.getParameter("id")));
			sendRedirect(request, response,getController().getURI(request, SearchTzyhkWt.class));
		} catch (ParameterException | LogicalException e) {
			prompt(request, response, PromptLevel.ERROR, e.getMessage());
			processGet(request, response, serviceSession);
		}

	}

	@Override
	protected void processGet(HttpServletRequest request,
			HttpServletResponse response, ServiceSession serviceSession)
			throws Throwable {
		ArticleManage manage = serviceSession.getService(ArticleManage.class);
		QuestionRecord record = manage.getQuestion(IntegerParser.parse(request
				.getParameter("id")),T5011_F02.TZYHK);
		if (record == null) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		request.setAttribute("record", record);
		forwardView(request, response, getClass());
	}

}
