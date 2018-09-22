package com.dimeng.p2p.front.servlets;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;

import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S50.enums.T5011_F02;
import com.dimeng.p2p.modules.base.front.service.ArticleManage;
import com.dimeng.p2p.modules.base.front.service.NoticeManage;

public class Info extends AbstractFrontServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void processPost(final HttpServletRequest request,
			HttpServletResponse response, ServiceSession serviceSession)
			throws Throwable {
		ArticleManage articleManage = serviceSession.getService(ArticleManage.class);
		NoticeManage noticeManage = serviceSession.getService(NoticeManage.class);
		String type = request.getParameter("type");
		PagingResult<?> infos = null;
		if("WZGG".equals(type)){
			infos = noticeManage.search(new Paging() {
				
				@Override
	            public int getCurrentPage()
	            {
	                return 1;
	            }
	            
	            @Override
	            public int getSize()
	            {
	                return 5;
	            }
			});
		}else{
			infos = articleManage.search(T5011_F02.parse(type),new Paging() {
				
				@Override
	            public int getCurrentPage()
	            {
	                return 1;
	            }
	            
	            @Override
	            public int getSize()
	            {
	                return 5;
	            }
			});
		}
		
        PrintWriter out = response.getWriter();
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        jsonMap.put("infoList", infos.getItems());
        out.print(objectMapper.writeValueAsString(jsonMap));
        out.close();
	}
}
