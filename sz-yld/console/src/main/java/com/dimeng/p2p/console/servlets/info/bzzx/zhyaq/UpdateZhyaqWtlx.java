package com.dimeng.p2p.console.servlets.info.bzzx.zhyaq;

import java.sql.Timestamp;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.http.upload.PartFile;
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
import com.dimeng.p2p.modules.base.console.service.entity.QuestionTypeRecord;
import com.dimeng.util.parser.EnumParser;
import com.dimeng.util.parser.IntegerParser;
import com.dimeng.util.parser.TimestampParser;

@MultipartConfig
@Right(id = "P2P_C_INFO_BZZX_MENU", name = "帮助中心",moduleId="P2P_C_INFO_BZZX",order=0)
public class UpdateZhyaqWtlx extends AbstractInformationServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void processPost(final HttpServletRequest request,
			HttpServletResponse response, ServiceSession serviceSession)
			throws Throwable {
		try {
			Question article = new Question() {
					
				@Override
				public ArticlePublishStatus getPublishStatus() {
					return EnumParser.parse(ArticlePublishStatus.class,
							request.getParameter("publishStatus"));
				}

				@Override
				public UploadFile getImage() throws Throwable {
					Part part = request.getPart("image");
					if (part == null || part.getContentType() == null
							|| part.getSize() == 0) {
						return null;
					}
					return new PartFile(part);
				}

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
				public int getQuestionTypeID() {
					// TODO Auto-generated method stub
					return 0;
				}

				@Override
				public String getQuestion() {
					// TODO Auto-generated method stub
					return null;
				}

				@Override
				public String getQuestionAnswer() {
					// TODO Auto-generated method stub
					return null;
				}
			};
			ArticleManage manage = serviceSession
					.getService(ArticleManage.class);
			manage.updateQuestionType(T5011_F02.ZHYAQ,article,IntegerParser.parse(request.getParameter("id")));
			sendRedirect(request, response,
					getController().getURI(request, SearchZhyaqWtlx.class));
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
		QuestionTypeRecord record = manage.getQuestionType(IntegerParser.parse(request
				.getParameter("id")),T5011_F02.ZHYAQ);
		if (record == null) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		request.setAttribute("record", record);
		forwardView(request, response, getClass());
	}

}
