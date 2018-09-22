package com.dimeng.p2p.console.servlets.info.bzzx.zhyaq;

import java.sql.Timestamp;

import javax.servlet.annotation.MultipartConfig;
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
import com.dimeng.util.parser.EnumParser;
import com.dimeng.util.parser.TimestampParser;

@MultipartConfig
@Right(id = "P2P_C_INFO_BZZX_MENU", name = "帮助中心",moduleId="P2P_C_INFO_BZZX",order=0)
public class AddZhyaqWt extends AbstractInformationServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void processPost(final HttpServletRequest request,
			HttpServletResponse response, ServiceSession serviceSession)
			throws Throwable {

		try {

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

			ArticleManage manage = serviceSession
					.getService(ArticleManage.class);
			manage.addQuestion(T5011_F02.ZHYAQ, question);
			sendRedirect(request, response,
					getController().getURI(request, SearchZhyaqWt.class));
		} catch (ParameterException | LogicalException e) {
			prompt(request, response, PromptLevel.ERROR, e.getMessage());
			processGet(request, response, serviceSession);
		}

	}

}
