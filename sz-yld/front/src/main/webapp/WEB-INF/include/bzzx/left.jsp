<%@page import="com.dimeng.p2p.front.servlets.bzzx.*"%>
<%@page import="com.dimeng.p2p.common.enums.ArticleType"%>
<div class="sidebar fl ">
	<span class="tit_info clearfix"><i class="a_ui a_ui_icon2"></i>帮助中心</span>
	<ul class="barlist">
		<li<%="CZYTX".equals(CURRENT_SUB_CATEGORY)?" class=\"on\"":"" %>><a href="<%=controller.getViewURI(request, Czytx.class)%>"><i></i><%=ArticleType.CZYTX.getName() %></a></li>
		<li<%="TZYHK".equals(CURRENT_SUB_CATEGORY)?" class=\"on\"":"" %>><a href="<%=controller.getViewURI(request, Tzyhk.class)%>"><i></i><%=ArticleType.TZYHK.getName() %></a></li>
		<li<%="ZHYAQ".equals(CURRENT_SUB_CATEGORY)?" class=\"on\"":"" %>><a href="<%=controller.getViewURI(request, Zhyaq.class)%>"><i></i><%=ArticleType.ZHYAQ.getName() %></a></li>
		<li<%="ZCYDL".equals(CURRENT_SUB_CATEGORY)?" class=\"on\"":"" %>><a href="<%=controller.getViewURI(request, Zcydl.class)%>"><i></i><%=ArticleType.ZCYDL.getName() %></a></li>
		<li class="a_tit"><span><i class="a_ui a_ui_icon3"></i>投资攻略</span></li>
		<li<%="XSZY".equals(CURRENT_SUB_CATEGORY)?" class=\"on\"":"" %>><a href="<%=controller.getViewURI(request, Xszy.class)%>"><i></i><%=ArticleType.XSZY.getName() %></a></li>
		<li<%="MCJS".equals(CURRENT_SUB_CATEGORY)?" class=\"on\"":"" %>><a href="<%=controller.getViewURI(request, Mcjs.class)%>"><i></i><%=ArticleType.MCJS.getName() %></a></li>
		<li<%="PTJS".equals(CURRENT_SUB_CATEGORY)?" class=\"on\"":"" %>><a href="<%=controller.getViewURI(request, Ptjs.class)%>"><i></i><%=ArticleType.PTJS.getName() %></a></li>
		<li<%="LXHFY".equals(CURRENT_SUB_CATEGORY)?" class=\"on\"":"" %>><a href="<%=controller.getViewURI(request, Lxhfy.class)%>"><i></i><%=ArticleType.LXHFY.getName() %></a></li>
		<li<%="WYJK".equals(CURRENT_SUB_CATEGORY)?" class=\"on\"":"" %>><a href="<%=controller.getViewURI(request, Wyjk.class)%>"><i></i><%=ArticleType.WYJK.getName() %></a></li>
		<li<%="WYLC".equals(CURRENT_SUB_CATEGORY)?" class=\"on\"":"" %>><a href="<%=controller.getViewURI(request, Wylc.class)%>"><i></i><%=ArticleType.WYLC.getName() %></a></li>
		<li<%="PTZH".equals(CURRENT_SUB_CATEGORY)?" class=\"on\"":"" %>><a href="<%=controller.getViewURI(request, Ptzh.class)%>"><i></i><%=ArticleType.PTZH.getName() %></a></li>
	</ul>
</div>