package com.dimeng.p2p.front.servlets.bzzx;


import com.dimeng.framework.http.upload.FileStore;
import com.dimeng.framework.resource.ResourceProvider;
import com.dimeng.framework.resource.ResourceRegister;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S50.entities.T5011;
import com.dimeng.p2p.front.servlets.gywm.AbstractGywmServlet;
import com.dimeng.p2p.modules.base.front.service.ArticleManage;
import com.dimeng.p2p.modules.base.front.service.entity.T5011_EXT;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.IntegerParser;
import org.codehaus.jackson.map.ObjectMapper;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class Index extends AbstractGywmServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void processPost(final HttpServletRequest request, final HttpServletResponse response, final ServiceSession serviceSession) throws Throwable {

		String articType = request.getParameter("articType");
		ArticleManage articleManage = serviceSession.getService(ArticleManage.class);
		PrintWriter out = response.getWriter();

		ServletContext application = getServletContext();
		ResourceProvider resourceProvider = ResourceRegister.getResourceProvider(application);
		FileStore fileStore = resourceProvider.getResource(FileStore.class);
		ObjectMapper objectMapper = new ObjectMapper();
		Map<String, Object> jsonMap = new HashMap<String, Object>();

		if(!"XSZY".equals(articType)) {
			T5011 article = articleManage.get(articType);
			String articleContent = "";
			if (article != null) {
				articleManage.view(article.F01);
				articleContent = articleManage.getContent(article.F01);
			}
			StringHelper.format(out, articleContent, fileStore);
			out.close();
		}

		if("XSZY".equals(articType)) {
			PagingResult<T5011_EXT> results = articleManage.search_EXT(articType, new Paging() {
				@Override
				public int getCurrentPage() {
					return IntegerParser.parse(request.getParameter("currentPage"));
				}

				@Override
				public int getSize() {
					return IntegerParser.parse(request.getParameter("pageSize"));
				}
			},fileStore);
			String pageStr = rendPaging(results);
			jsonMap.put("pageStr", pageStr);
			jsonMap.put("pageCount", results.getPageCount());
			jsonMap.put("artics", results.getItems());
			out.print(objectMapper.writeValueAsString(jsonMap));
			out.close();
		}
	}

}
