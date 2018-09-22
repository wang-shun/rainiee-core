<%@page import="com.dimeng.p2p.front.servlets.xszy.Zxkf"%>
<%@page import="com.dimeng.p2p.front.servlets.bzzx.*"%>
<%@page import="com.dimeng.p2p.common.enums.ArticleType"%>
<div class="w1002 clearfix">
	<a href="<%=controller.getViewURI(request, Czytx.class)%>" <%="CZYTX".equals(CURRENT_SUB_CATEGORY)?" class=\"cur\"":"" %>><%=ArticleType.CZYTX.getName() %></a>
	<a href="<%=controller.getViewURI(request, Tzyhk.class)%>" <%="TZYHK".equals(CURRENT_SUB_CATEGORY)?" class=\"cur\"":"" %>><%=ArticleType.TZYHK.getName() %></a>
	<a href="<%=controller.getViewURI(request, Zhyaq.class)%>" <%="ZHYAQ".equals(CURRENT_SUB_CATEGORY)?" class=\"cur\"":"" %>><%=ArticleType.ZHYAQ.getName() %></a>
	<a href="<%=controller.getViewURI(request, Zcydl.class)%>" <%="ZCYDL".equals(CURRENT_SUB_CATEGORY)?" class=\"cur\"":"" %>><%=ArticleType.ZCYDL.getName() %></a>
	<%--<a href="<%=controller.getViewURI(request, Zxkf.class)%>" <%="ZXKF".equals(CURRENT_SUB_CATEGORY)?" class=\"cur\"":"" %>><%=ArticleType.ZXKF.getName() %></a>--%>
</div>