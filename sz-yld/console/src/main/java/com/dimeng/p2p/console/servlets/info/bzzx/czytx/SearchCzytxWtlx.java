package com.dimeng.p2p.console.servlets.info.bzzx.czytx;

import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S50.enums.T5011_F02;
import com.dimeng.p2p.common.enums.ArticlePublishStatus;
import com.dimeng.p2p.console.servlets.info.AbstractInformationServlet;
import com.dimeng.p2p.modules.base.console.service.ArticleManage;
import com.dimeng.p2p.modules.base.console.service.entity.QuestionTypeRecord;
import com.dimeng.p2p.modules.base.console.service.query.QuestionQuery;
import com.dimeng.util.parser.EnumParser;
import com.dimeng.util.parser.IntegerParser;
import com.dimeng.util.parser.TimestampParser;

@Right(id = "P2P_C_INFO_BZZX_MENU", name = "帮助中心",moduleId="P2P_C_INFO_BZZX",order=0)
public class SearchCzytxWtlx extends AbstractInformationServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void processPost(final HttpServletRequest request,
			HttpServletResponse response, ServiceSession serviceSession)
			throws Throwable {
		ArticleManage manage = serviceSession.getService(ArticleManage.class);
		final Paging paging = new Paging() {

			@Override
			public int getSize() {

				return 10;
			}

			@Override
			public int getCurrentPage() {
				return IntegerParser
						.parse(request.getParameter(PAGING_CURRENT));
			}
		};

		QuestionQuery query = new QuestionQuery() {

			@Override
			public Timestamp getPublishTimeStart() {
				return TimestampParser.parse(request
						.getParameter("publishTimeStart"));
			}

			@Override
			public Timestamp getPublishTimeEnd() {
				return TimestampParser.parse(request
						.getParameter("publishTimeEnd"));
			}

			@Override
			public ArticlePublishStatus getPublishStatus() {
				return EnumParser.parse(ArticlePublishStatus.class,
						request.getParameter("publishStatus"));
			}

			@Override
			public Timestamp getCreateTimeStart() {
				return TimestampParser.parse(request
						.getParameter("createTimeStart"));
			}

			@Override
			public Timestamp getCreateTimeEnd() {
				return TimestampParser.parse(request
						.getParameter("createTimeEnd"));
			}

			@Override
			public String getQuestion() {
				return null;
			}

			@Override
			public Timestamp publishTime() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public String getPublisher() {
				return request.getParameter("publisherName");
			}

			@Override
			public String getQuestionType() {
				return request.getParameter("questionType");
			}

			@Override
			public T5011_F02 getArticleType() {
				return T5011_F02.CZYTX;
			}

			@Override
			public int getQuestionTypeID() {
				// TODO Auto-generated method stub
				return 0;
			}
		};
		PagingResult<QuestionTypeRecord> result = manage.searchCzytxWtlx(query, paging);
		request.setAttribute("result", result);
		forwardView(request, response, getClass());
	}

	@Override
	protected void processGet(HttpServletRequest request,
			HttpServletResponse response, ServiceSession serviceSession)
			throws Throwable {
		processPost(request, response, serviceSession);
	}

}
