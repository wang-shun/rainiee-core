<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.dimeng.p2p.S50.entities.T5010"%>
<%@page import="com.dimeng.p2p.S50.enums.T5010_F04"%>
<%@page import="com.dimeng.p2p.modules.base.front.service.ArticleManage"%>
<%@page import="com.dimeng.p2p.front.servlets.gywm.*"%>
<%	ArticleManage artManage = serviceSession.getService(ArticleManage.class);
	T5010[] gywms = artManage.getArticleCategoryAll(1, T5010_F04.QY);
	Map<String,String> urlMap = new HashMap<String,String>();
	urlMap.put("GSJJ", controller.getViewURI(request, Gsjj.class));
	urlMap.put("GLTD", controller.getViewURI(request, Gltd.class));
	urlMap.put("ZJGW", controller.getViewURI(request, Zjgw.class));
	urlMap.put("HZHB", controller.getViewURI(request, Hzhb.class));
	urlMap.put("ZXNS", controller.getViewURI(request, Zxns.class));
	urlMap.put("LXWM", controller.getViewURI(request, Lxwm.class));
	urlMap.put("YJFK", controller.getViewURI(request, Yjfk.class));
%>
<div class="w1002 clearfix">
	<% if(gywms != null && gywms.length > 0){
	     for(T5010 t5010 : gywms ){
	%>
	<a href="<%=urlMap.get(t5010.F02)%>" <%=t5010.F02.equals(CURRENT_SUB_CATEGORY)?" class=\"cur\"":"" %>><%=t5010.F03 %></a>
	<%}} %>
</div>