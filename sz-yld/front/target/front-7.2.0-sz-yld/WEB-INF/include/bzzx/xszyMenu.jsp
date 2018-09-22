<%@page import="com.dimeng.p2p.S50.enums.T5010_F04"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="com.dimeng.p2p.S50.entities.T5010"%>
<%@page import="com.dimeng.p2p.modules.base.front.service.ArticleManage"%>
<%@page import="com.dimeng.p2p.front.servlets.bzzx.*"%>
<%@page import="com.dimeng.p2p.common.enums.ArticleType"%>
<%	ArticleManage artManage = serviceSession.getService(ArticleManage.class);
	T5010[] xszys = artManage.getArticleCategoryAll(4, T5010_F04.QY);
	Map<String,String> urlMap = new HashMap<String,String>();
	urlMap.put("XSZY", controller.getViewURI(request, Xszy.class));
	urlMap.put("MCJS", controller.getViewURI(request, Mcjs.class));
	urlMap.put("PTJS", controller.getViewURI(request, Ptjs.class));
	urlMap.put("LXHFY", controller.getViewURI(request, Lxhfy.class));
	urlMap.put("WYJK", controller.getViewURI(request, Wyjk.class));
	urlMap.put("WYLC", controller.getViewURI(request, Wylc.class));
	urlMap.put("PTZH", controller.getViewURI(request, Ptzh.class));
	T5010_F04 categoryStatus = artManage.getCategoryStatusByCode(CURRENT_SUB_CATEGORY);
	if(categoryStatus == T5010_F04.TY){
	    response.sendError(HttpServletResponse.SC_NOT_FOUND);
	}
%>
<div class="w1002 clearfix">
	<% if(xszys != null && xszys.length > 0){
	     for(T5010 t5010 : xszys ){
	%>
	<a href="<%=urlMap.get(t5010.F02)%>" <%=t5010.F02.equals(CURRENT_SUB_CATEGORY)?" class=\"cur\"":"" %>><%=t5010.F03 %></a>
	<%}} %>
</div>