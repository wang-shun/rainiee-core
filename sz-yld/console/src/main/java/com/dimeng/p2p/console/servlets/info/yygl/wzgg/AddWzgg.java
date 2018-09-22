package com.dimeng.p2p.console.servlets.info.yygl.wzgg;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.http.upload.FileStore;
import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.p2p.common.FormToken;
import com.dimeng.p2p.common.enums.NoticePublishStatus;
import com.dimeng.p2p.common.enums.NoticeType;
import com.dimeng.p2p.modules.base.console.service.NoticeManage;
import com.dimeng.p2p.modules.base.console.service.entity.Notice;
import com.dimeng.util.parser.EnumParser;

@Right(id = "P2P_C_INFO_YYGL_MENU", name = "运营管理",moduleId="P2P_C_INFO_YYGL",order=0)
public class AddWzgg extends AbstractWzggServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void processPost(final HttpServletRequest request,
			HttpServletResponse response, ServiceSession serviceSession)
			throws Throwable {
		try {
		    if(!FormToken.verify(serviceSession.getSession(), 
                request.getParameter(FormToken.parameterName()))) {
                throw new LogicalException("请不要重复提交请求！");
            }
			NoticeManage manage = serviceSession.getService(NoticeManage.class);

			Notice notice = new Notice() {

				@Override
				public NoticeType getType() {
					return EnumParser.parse(NoticeType.class,
							request.getParameter("type"));
				}

				@Override
				public String getTitle() {
					return request.getParameter("title");
				}

				@Override
				public NoticePublishStatus getPublishStatus() {
					return EnumParser.parse(NoticePublishStatus.class,
							request.getParameter("publishStatus"));
				}

				@Override
				public String getContent() {
					return getResourceProvider().getResource(FileStore.class)
							.encode(request.getParameter("content"));
				}
			};
			manage.add(notice);
			sendRedirect(request, response,
					getController().getURI(request, SearchWzgg.class));
		} catch (ParameterException | LogicalException e) {
			prompt(request, response, PromptLevel.ERROR, e.getMessage());
			processGet(request, response, serviceSession);
		}
	}

}
