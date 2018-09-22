<%@page import="com.dimeng.p2p.S50.entities.T5010"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.dimeng.p2p.modules.base.front.service.ArticleManage"%>
<%@page import="com.dimeng.p2p.S50.enums.T5010_F04"%>
<%@page import="com.dimeng.p2p.front.servlets.xxpl.Qtxx"%>
<%@page import="com.dimeng.p2p.front.servlets.xxpl.Zdsx"%>
<%@page import="com.dimeng.p2p.front.servlets.xxpl.Sfbz"%>
<%@page import="com.dimeng.p2p.front.servlets.xxpl.Yybg"%>
<%@page import="com.dimeng.p2p.front.servlets.xxpl.Yysj"%>
<%@page import="com.dimeng.p2p.front.servlets.xxpl.Sjbg"%>
<%@page import="com.dimeng.p2p.front.servlets.xxpl.Zzxx"%>
<%@page import="com.dimeng.p2p.front.servlets.xxpl.Baxx"%>
<%@page import="com.dimeng.p2p.front.servlets.gywm.*"%>
<%@page import="com.dimeng.p2p.front.servlets.zxdt.*"%>
<%@page import="com.dimeng.p2p.common.enums.ArticleType"%>

<%	ArticleManage artManage = serviceSession.getService(ArticleManage.class);
	T5010[] gywms = artManage.getArticleCategoryAll(1, T5010_F04.QY);
	T5010[] aqbzs = artManage.getArticleCategoryAll(2, T5010_F04.QY);
	T5010[] xxpls = artManage.getArticleCategoryAll(34, T5010_F04.QY);
	Map<String,String> urlMap = new HashMap<String,String>();
	urlMap.put("GSJJ", controller.getViewURI(request, Gsjj.class));
	urlMap.put("GLTD", controller.getViewURI(request, Gltd.class));
	urlMap.put("ZJGW", controller.getViewURI(request, Zjgw.class));
	urlMap.put("HZHB", controller.getViewURI(request, Hzhb.class));
	urlMap.put("ZXNS", controller.getViewURI(request, Zxns.class));
	urlMap.put("LXWM", controller.getViewURI(request, Lxwm.class));
	urlMap.put("YJFK", controller.getViewURI(request, Yjfk.class));
	
	urlMap.put("BAXX", controller.getViewURI(request, Baxx.class));
	urlMap.put("ZZXX", controller.getViewURI(request, Zzxx.class));
	urlMap.put("SJBG", controller.getViewURI(request, Sjbg.class));
	urlMap.put("YYSJ", controller.getViewURI(request, Yysj.class));
	urlMap.put("YYBG", controller.getViewURI(request, Yybg.class));
	urlMap.put("SFBZ", controller.getViewURI(request, Sfbz.class));
	urlMap.put("ZDSX", controller.getViewURI(request, Zdsx.class));
	urlMap.put("QTXX", controller.getViewURI(request, Qtxx.class));
	
	urlMap.put("BXDB", controller.getViewURI(request, Bxdb.class));
	urlMap.put("XMFK", controller.getViewURI(request, Xmfk.class));
	urlMap.put("ZJAQ", controller.getViewURI(request, Zjaq.class));
	urlMap.put("FLHG", controller.getViewURI(request, Flhg.class));
	urlMap.put("GCTM", controller.getViewURI(request, Gctm.class));
	urlMap.put("HZJG", controller.getViewURI(request, Hzjg.class));
	

%>

<div class="disclosure_nav">
	<ul>
		<li class="item">
			<a href="<%=controller.getViewURI(request, Gsjj.class)%>" class="itemNav"><i class="icon1"></i>关于我们</a>
			<ul id="GYWM_UL">
				<%-- <li><a <%="GSJJ".equals(CURRENT_SUB_CATEGORY)?" class=\"cur\"":"" %> href="<%=controller.getViewURI(request, Gsjj.class)%>"><span><%=ArticleType.GSJJ.getName() %></span></a></li>
				<li><a <%="GLTD".equals(CURRENT_SUB_CATEGORY)?" class=\"cur\"":"" %> href="<%=controller.getViewURI(request, Gltd.class)%>"><span><%=ArticleType.GLTD.getName() %></span></a></li>
				<li><a <%="ZJGW".equals(CURRENT_SUB_CATEGORY)?" class=\"cur\"":"" %> href="<%=controller.getViewURI(request, Zjgw.class)%>"><span><%=ArticleType.ZJGW.getName() %></span></a></li>
				<li><a <%="HZHB".equals(CURRENT_SUB_CATEGORY)?" class=\"cur\"":"" %> href="<%=controller.getViewURI(request, Hzhb.class)%>"><span><%=ArticleType.HZHB.getName() %></span></a></li>
				<li><a <%="ZXNS".equals(CURRENT_SUB_CATEGORY)?" class=\"cur\"":"" %> href="<%=controller.getViewURI(request, Zxns.class)%>"><span><%=ArticleType.ZXNS.getName() %></span></a></li>
				<li><a <%="LXWM".equals(CURRENT_SUB_CATEGORY)?" class=\"cur\"":"" %> href="<%=controller.getViewURI(request, Lxwm.class)%>"><span><%=ArticleType.LXWM.getName() %></span></a></li> --%>
			<% if(gywms != null && gywms.length > 0){
			     for(T5010 t5010 : gywms ){
			%>
				<li><a class="click-link <%=t5010.F02.equals(CURRENT_SUB_CATEGORY)?" cur\"":"\"" %> href="<%=urlMap.get(t5010.F02)%>"><span><%=t5010.F03 %></span></a></li>
			<%}} %>
			</ul>
		</li>
		<li class="item">
			<a href="<%=controller.getViewURI(request, Baxx.class)%>" class="itemNav"><i class="icon2"></i>信息披露</a>
			<ul id="XXPL_UL">
				<%-- <li><a <%="BAXX".equals(CURRENT_SUB_CATEGORY)?" class=\"cur\"":"" %> href="<%=controller.getViewURI(request, Baxx.class)%>"><span><%=ArticleType.BAXX.getName() %></span></a></li>
				<li><a <%="ZZXX".equals(CURRENT_SUB_CATEGORY)?" class=\"cur\"":"" %> href="<%=controller.getViewURI(request, Zzxx.class)%>"><span><%=ArticleType.ZZXX.getName() %></span></a></li>
				<li><a <%="SJBG".equals(CURRENT_SUB_CATEGORY)?" class=\"cur\"":"" %> href="<%=controller.getViewURI(request, Sjbg.class)%>"><span><%=ArticleType.SJBG.getName() %></span></a></li>
				<li><a <%="YYSJ".equals(CURRENT_SUB_CATEGORY)?" class=\"cur\"":"" %> href="<%=controller.getViewURI(request, Yysj.class)%>"><span><%=ArticleType.YYSJ.getName() %></span></a></li>
				<li><a <%="YYBG".equals(CURRENT_SUB_CATEGORY)?" class=\"cur\"":"" %> href="<%=controller.getViewURI(request, Yybg.class)%>"><span><%=ArticleType.YYBG.getName() %></span></a></li>
				<li><a <%="SFBZ".equals(CURRENT_SUB_CATEGORY)?" class=\"cur\"":"" %> href="<%=controller.getViewURI(request, Sfbz.class)%>"><span><%=ArticleType.SFBZ.getName() %></span></a></li>
				<li><a <%="ZDSX".equals(CURRENT_SUB_CATEGORY)?" class=\"cur\"":"" %> href="<%=controller.getViewURI(request, Zdsx.class)%>"><span><%=ArticleType.ZDSX.getName() %></span></a></li>
				<li><a <%="QTXX".equals(CURRENT_SUB_CATEGORY)?" class=\"cur\"":"" %> href="<%=controller.getViewURI(request, Qtxx.class)%>"><span><%=ArticleType.QTXX.getName() %></span></a></li> --%>
				<% if(xxpls != null && xxpls.length > 0){
			     for(T5010 t5010 : xxpls ){
				%>
					<li><a class="click-link <%=t5010.F02.equals(CURRENT_SUB_CATEGORY)?" cur\"":"\"" %> href="<%=urlMap.get(t5010.F02)%>"><span><%=t5010.F03 %></span></a></li>
				<%}} %>
				<li><a href="<%=controller.getViewURI(request, Yysj.class)%>"><span><%=ArticleType.YYSJ.getName() %></span></a></li>
			</ul>
		</li>
		<li class="item">
			<a href="<%=controller.getViewURI(request, Bxdb.class)%>" class="itemNav"><i class="icon3"></i>安全保障</a>
			<ul id="AQBZ_UL">
				<%-- <li><a <%="BXDB".equals(CURRENT_SUB_CATEGORY)?" class=\"cur\"":"" %> href="<%=controller.getViewURI(request, Bxdb.class)%>"><span><%=ArticleType.BXDB.getName() %></span></a></li>
				<li><a <%="XMFK".equals(CURRENT_SUB_CATEGORY)?" class=\"cur\"":"" %> href="<%=controller.getViewURI(request, Xmfk.class)%>"><span><%=ArticleType.XMFK.getName() %></span></a></li>
				<li><a <%="ZJAQ".equals(CURRENT_SUB_CATEGORY)?" class=\"cur\"":"" %> href="<%=controller.getViewURI(request, Zjaq.class)%>"><span><%=ArticleType.ZJAQ.getName() %></span></a></li>
				<li><a <%="FLHG".equals(CURRENT_SUB_CATEGORY)?" class=\"cur\"":"" %> href="<%=controller.getViewURI(request, Flhg.class)%>"><span><%=ArticleType.FLHG.getName() %></span></a></li>
				<li><a <%="GCTM".equals(CURRENT_SUB_CATEGORY)?" class=\"cur\"":"" %> href="<%=controller.getViewURI(request, Gctm.class)%>"><span><%=ArticleType.GCTM.getName() %></span></a></li>
				<li><a <%="HZJG".equals(CURRENT_SUB_CATEGORY)?" class=\"cur\"":"" %> href="<%=controller.getViewURI(request, Hzjg.class)%>"><span><%=ArticleType.HZJG.getName() %></span></a></li> --%>
				<% if(aqbzs != null && aqbzs.length > 0){
			     for(T5010 t5010 : aqbzs ){
				%>
					<li><a class="click-link <%=t5010.F02.equals(CURRENT_SUB_CATEGORY)?" cur\"":"\"" %> href="<%=urlMap.get(t5010.F02)%>"><span><%=t5010.F03 %></span></a></li>
				<%}} %>
			</ul>
		</li>
	</ul>
</div>

<script type="text/javascript">
	$(".disclosure_nav").on("click", ".click-link", function(){
		$(this).parents("li").find(".click-link").removeClass("cur");
	  	$(this).addClass("cur");
	  	$(".disclosure_nav .item").each(function(){
		  	$(this).removeClass("on");
	  	});
	  	$(this).parent().parent().parent().addClass("on"); 
	  	//alert($(this).parent().parent().parent().attr("class"));
	 	//$(this).find(".arrow-down-icon").addClass("arrow-up-icon");
		//$(this).parents("dd").siblings("dd").find(".arrow-down-icon").removeClass("arrow-up-icon");
		//$(this).parents("dd").find("ul").slideDown(400);
		//$(this).parents("dd").siblings("dd").find("ul").slideUp(400);
	 });
	/* $('.disclosure_nav .cur').on('click',function(){
		if ($(this).next().css('display') == "none") {
			//展开未展开
			$('.disclosure_nav .item').children('ul').slideUp(300);
			$(this).next('ul').slideDown(300);
			$(this).parent('li').addClass('on').siblings('li').removeClass('on');
		} else {
			//收缩已展开
			$(this).next('ul').slideUp(300);
			$('.disclosure_nav .item.on').removeClass('on');
		}
	}); */
	
	$(function(){
		$('.disclosure_nav .cur').parent().parent().parent().addClass("on");
	});
	
	
</script>

