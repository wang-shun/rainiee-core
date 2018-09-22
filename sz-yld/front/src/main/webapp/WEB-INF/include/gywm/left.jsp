<%@page import="com.dimeng.p2p.front.servlets.gywm.*"%>
<%@page import="com.dimeng.p2p.front.servlets.zxdt.*"%>
<%@page import="com.dimeng.p2p.common.enums.ArticleType"%>
<div class="sidebar fl ">
	<span class="tit_info clearfix"><i class="a_ui a_ui_tit"></i>关于我们</span>
	<ul class="barlist">
		<li<%="GSJJ".equals(CURRENT_SUB_CATEGORY)?" class=\"on\"":"" %>><a href="<%=controller.getViewURI(request, Gsjj.class)%>"><i></i><%=ArticleType.GSJJ.getName() %></a></li>
		<li<%="GLTD".equals(CURRENT_SUB_CATEGORY)?" class=\"on\"":"" %>><a href="<%=controller.getViewURI(request, Gltd.class)%>"><i></i><%=ArticleType.GLTD.getName() %></a></li>
		<li<%="ZJGW".equals(CURRENT_SUB_CATEGORY)?" class=\"on\"":"" %>><a href="<%=controller.getViewURI(request, Zjgw.class)%>"><i></i><%=ArticleType.ZJGW.getName() %></a></li>
		<li<%="HZHB".equals(CURRENT_SUB_CATEGORY)?" class=\"on\"":"" %>><a href="<%=controller.getViewURI(request, Hzhb.class)%>"><i></i><%=ArticleType.HZHB.getName() %></a></li>
		<li<%="ZXNS".equals(CURRENT_SUB_CATEGORY)?" class=\"on\"":"" %>><a href="<%=controller.getViewURI(request, Zxns.class)%>"><i></i><%=ArticleType.ZXNS.getName() %></a></li>
		<li<%="LXWM".equals(CURRENT_SUB_CATEGORY)?" class=\"on\"":"" %>><a href="<%=controller.getViewURI(request, Lxwm.class)%>"><i></i><%=ArticleType.LXWM.getName() %></a></li>
		
		<li class="a_tit"><span><i class="a_ui a_ui_icon1"></i>安全保障</span></li>
		<li<%="BXDB".equals(CURRENT_SUB_CATEGORY)?" class=\"on\"":"" %>><a href="<%=controller.getViewURI(request, Bxdb.class)%>"><i></i><%=ArticleType.BXDB.getName() %></a></li>
		<li<%="XMFK".equals(CURRENT_SUB_CATEGORY)?" class=\"on\"":"" %>><a href="<%=controller.getViewURI(request, Xmfk.class)%>"><i></i><%=ArticleType.XMFK.getName() %></a></li>
		<li<%="ZJAQ".equals(CURRENT_SUB_CATEGORY)?" class=\"on\"":"" %>><a href="<%=controller.getViewURI(request, Zjaq.class)%>"><i></i><%=ArticleType.ZJAQ.getName() %></a></li>
		<li<%="FLHG".equals(CURRENT_SUB_CATEGORY)?" class=\"on\"":"" %>><a href="<%=controller.getViewURI(request, Flhg.class)%>"><i></i><%=ArticleType.FLHG.getName() %></a></li>
		<li<%="GCTM".equals(CURRENT_SUB_CATEGORY)?" class=\"on\"":"" %>><a href="<%=controller.getViewURI(request, Gctm.class)%>"><i></i><%=ArticleType.GCTM.getName() %></a></li>
		<li<%="HZJG".equals(CURRENT_SUB_CATEGORY)?" class=\"on\"":"" %>><a href="<%=controller.getViewURI(request, Hzjg.class)%>"><i></i><%=ArticleType.HZJG.getName() %></a></li>
		
		<li class="a_tit"><span><i class="a_ui a_ui_icon"></i>最新动态</span></li>
		<li<%="MTBD".equals(CURRENT_SUB_CATEGORY)?" class=\"on\"":"" %>><a href="<%=controller.getViewURI(request, Mtbd.class)%>"><i></i><%=ArticleType.MTBD.getName() %></a></li>
		<li<%="SHZR".equals(CURRENT_SUB_CATEGORY)?" class=\"on\"":"" %>><a href="<%=controller.getViewURI(request, Shzr.class)%>"><i></i><%=ArticleType.SHZR.getName() %></a></li>
		<li<%="HLWJRYJ".equals(CURRENT_SUB_CATEGORY)?" class=\"on\"":"" %>><a href="<%=controller.getViewURI(request, Hlwjryj.class)%>"><i></i><%=ArticleType.HLWJRYJ.getName() %></a></li>
		<li<%="WDHYZX".equals(CURRENT_SUB_CATEGORY)?" class=\"on\"":"" %>><a href="<%=controller.getViewURI(request, Wdhyzx.class)%>"><i></i><%=ArticleType.WDHYZX.getName() %></a></li>
		<li<%="WZGG".equals(CURRENT_SUB_CATEGORY)?" class=\"b_tit on\"":" class=\"b_tit\"" %>><a href="<%=controller.getViewURI(request, Wzgg.class)%>"><i></i>网站公告</a></li>
	</ul>
</div>