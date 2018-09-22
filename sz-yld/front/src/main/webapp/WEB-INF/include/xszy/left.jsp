<%@page import="com.dimeng.p2p.common.enums.ArticleType"%>
<%@page import="com.dimeng.p2p.front.servlets.xszy.*"%>
<div class="bar fl clearfix">
	<span class="tit_info clearfix"><i class="b_ui"></i>投资攻略</span>
	<ul class="barlist clearfix">
		<li<%="XSZY".equals(CURRENT_SUB_CATEGORY)?" class=\"on\"":"" %>><a href="<%=controller.getViewURI(request, Index.class)%>"><i></i><%=ArticleType.XSZY.getName()%></a></li>
		<li<%="MCJS".equals(CURRENT_SUB_CATEGORY)?" class=\"on\"":"" %>><a href="<%=controller.getViewURI(request, Mcjs.class)%>"><i></i><%=ArticleType.MCJS.getName()%></a></li>
		<li<%="PTJS".equals(CURRENT_SUB_CATEGORY)?" class=\"on\"":"" %>><a href="<%=controller.getViewURI(request, Ptjs.class)%>"><i></i><%=ArticleType.PTJS.getName()%></a></li>
		<li<%="LXHFY".equals(CURRENT_SUB_CATEGORY)?" class=\"on\"":"" %>><a href="<%=controller.getViewURI(request, Lxhfy.class)%>"><i></i><%=ArticleType.LXHFY.getName()%></a></li>
		<li<%="WYJK".equals(CURRENT_SUB_CATEGORY)?" class=\"on\"":"" %>><a href="<%=controller.getViewURI(request, Wyjk.class)%>"><i></i><%=ArticleType.WYJK.getName()%></a></li>
		<li<%="WYLC".equals(CURRENT_SUB_CATEGORY)?" class=\"on\"":"" %>><a href="<%=controller.getViewURI(request, Wylc.class)%>"><i></i><%=ArticleType.WYLC.getName()%></a></li>
		<li<%="WDWZH".equals(CURRENT_SUB_CATEGORY)?" class=\"on\"":"" %>><a href="<%=controller.getViewURI(request, Ptzh.class)%>"><i></i><%=ArticleType.PTZH.getName()%></a></li>
	</ul>

	<div class="clear"></div>
	<div class="online_customer clearfix">
		<div class="tit">
			<a href="<%=controller.getViewURI(request, Zxkf.class)%>">更多&gt;&gt;</a>
		</div>
		<div class="info">
			<img src="<%=controller.getStaticPath(request) %>/images/lead_pic01.jpg" width="171" height="96" />
		</div>
	</div>
</div>
