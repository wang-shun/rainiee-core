package com.dimeng.p2p.console.servlets.info.gywm.gltd;

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
import com.dimeng.p2p.common.FormToken;
import com.dimeng.p2p.common.enums.ArticlePublishStatus;
import com.dimeng.p2p.console.servlets.info.AbstractInformationServlet;
import com.dimeng.p2p.modules.base.console.service.ArticleManage;
import com.dimeng.p2p.modules.base.console.service.entity.Article;
import com.dimeng.util.parser.IntegerParser;
import com.dimeng.util.parser.TimestampParser;

@MultipartConfig
@Right(id = "P2P_C_INFO_GYWM_MENU", name = "关于我们",moduleId="P2P_C_INFO_GYWM",order=0)
public class AddGltd extends AbstractInformationServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void processPost(final HttpServletRequest request,
			HttpServletResponse response, ServiceSession serviceSession)
			throws Throwable {

	    if(!FormToken.verify(serviceSession.getSession(),
            request.getParameter(FormToken.parameterName()))) {
            throw new LogicalException("请不要重复提交请求！");
        }
		try {

			Article article = new Article() {

				@Override
				public String getTitle() {
					return request.getParameter("title");
				}

				@Override
				public String getSummary() {
					return request.getParameter("summary");
				}

				@Override
				public String getSource() {
					return request.getParameter("source");
				}

				@Override
				public int getSortIndex() {
					return IntegerParser.parse(request
							.getParameter("sortindex"));
				}

				@Override
				public ArticlePublishStatus getPublishStatus() {
					return ArticlePublishStatus.YFB;
				}

				@Override
				public UploadFile getImage() throws Throwable {
					Part part = request.getPart("image");
					if (part == null || part.getContentType() == null
							|| part.getSize() == 0) {
						throw new ParameterException("相片不能为空.");
					}
					return new PartFile(part);
				}

				@Override
				public String getContent() {
					return request.getParameter("content");
				}

				@Override
				public Timestamp publishTime() {

					return TimestampParser.parse(request
							.getParameter("publishTime"));
				}
			};

			ArticleManage manage = serviceSession
					.getService(ArticleManage.class);
			manage.add(T5011_F02.GLTD, article);
			sendRedirect(request, response,
					getController().getURI(request, SearchGltd.class));
		} catch (ParameterException | LogicalException e) {
			prompt(request, response, PromptLevel.ERROR, e.getMessage());
			processGet(request, response, serviceSession);
		}

	}

}
