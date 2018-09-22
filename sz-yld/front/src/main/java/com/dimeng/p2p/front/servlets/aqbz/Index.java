package com.dimeng.p2p.front.servlets.aqbz;


import com.dimeng.framework.http.upload.FileStore;
import com.dimeng.framework.resource.ResourceProvider;
import com.dimeng.framework.resource.ResourceRegister;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.S50.entities.T5011;
import com.dimeng.p2p.front.servlets.gywm.AbstractGywmServlet;
import com.dimeng.p2p.modules.base.front.service.ArticleManage;
import com.dimeng.util.StringHelper;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

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

		T5011 article = articleManage.get(articType);
		String articleContent = "";
		if (article != null) {
			articleManage.view(article.F01);
			articleContent = articleManage.getContent(article.F01);
		}

		StringHelper.format(out, articleContent, fileStore);
		out.close();



	}

}
