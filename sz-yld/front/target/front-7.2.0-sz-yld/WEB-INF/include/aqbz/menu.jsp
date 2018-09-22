<%@page import="java.util.Map"%>
<%@page import="com.dimeng.p2p.S50.entities.T5010"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.dimeng.p2p.S50.enums.T5010_F04"%>
<%@page import="com.dimeng.p2p.modules.base.front.service.ArticleManage"%>
<%@page import="com.dimeng.p2p.front.servlets.gywm.*"%>
<%@page import="com.dimeng.p2p.front.servlets.zxdt.*"%>
<%@page import="com.dimeng.p2p.common.enums.ArticleType"%>
<%	ArticleManage artManage = serviceSession.getService(ArticleManage.class);
	T5010[] aqbzs = artManage.getArticleCategoryAll(2, T5010_F04.QY);
	Map<String,String> urlMap = new HashMap<String,String>();
	urlMap.put("BXDB", controller.getViewURI(request, Bxdb.class));
	urlMap.put("XMFK", controller.getViewURI(request, Xmfk.class));
	urlMap.put("ZJAQ", controller.getViewURI(request, Zjaq.class));
	urlMap.put("FLHG", controller.getViewURI(request, Flhg.class));
	urlMap.put("GCTM", controller.getViewURI(request, Gctm.class));
	urlMap.put("HZJG", controller.getViewURI(request, Hzjg.class));
	T5010_F04 categoryStatus = artManage.getCategoryStatusByCode(CURRENT_SUB_CATEGORY);
	if(categoryStatus == T5010_F04.TY){
	    response.sendError(HttpServletResponse.SC_NOT_FOUND);
	}
%>
<div class="w1002 clearfix">
	<% if(aqbzs != null && aqbzs.length > 0){
	     for(T5010 t5010 : aqbzs ){
	%>
	<a href="<%=urlMap.get(t5010.F02)%>" <%=t5010.F02.equals(CURRENT_SUB_CATEGORY)?" class=\"cur\"":"" %>><%=t5010.F03 %></a>
	<%}} %>
</div> 