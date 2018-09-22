<%@page import="com.dimeng.p2p.console.servlets.info.bzzx.czytx.SearchCzytxWtlx"%>
<%@page import="com.dimeng.p2p.console.servlets.info.yygl.gggl.SearchGggl"%>
<%@page import="com.dimeng.p2p.console.servlets.info.yygl.kfzx.SearchKfzx"%>
<%@page import="com.dimeng.p2p.console.servlets.info.yygl.spsc.SearchSpsc"%>
<%@page import="com.dimeng.p2p.console.servlets.info.bzzx.tzyhk.SearchTzyhkWtlx"%>
<%@page import="com.dimeng.p2p.console.servlets.info.yygl.wzgg.SearchWzgg"%>
<%@page import="com.dimeng.p2p.console.servlets.info.yygl.xytk.SearchXytk"%>
<%@page import="com.dimeng.p2p.console.servlets.info.yygl.yqlj.SearchYqlj"%>
<%@page import="com.dimeng.p2p.console.servlets.info.yygl.yysjgl.UpdateOperateData"%>
<%@page import="com.dimeng.p2p.console.servlets.info.bzzx.zcydl.SearchZcydlWtlx"%>
<%@page import="com.dimeng.p2p.console.servlets.info.bzzx.zhyaq.SearchZhyaqWtlx"%>
<%@page import="com.dimeng.p2p.console.servlets.menu.Xcgl"%>
<%@page import="com.dimeng.p2p.modules.base.console.service.ArticleManage"%>

<%
	ArticleManage xcglmanage = serviceSession.getService(ArticleManage.class);
%>
<div class="item-subnav-box" data-title="propaganda">
    <dl>
      <dt class="f20 fb"><i class="icon-i w30 h30 va-middle nav-xcgl-icon2"></i>宣传管理</dt>
      <dd><a href="javascript:void(0);" class="click-link item-a" ><span class="a-text fl">关于我们</span><i class="icon-i w30 h30 arrow-down-icon mt5 fr"></i></a>
        <ul id="GYWM_UL">
        </ul>
      </dd>
      <dd><a href="javascript:void(0);" class="click-link item-a" >
		   <span class="a-text fl">安全保障</span>
		   <i class="icon-i w30 h30 arrow-down-icon mt5 fr"></i>
	     </a>
        <ul id="AQBZ_UL">
        </ul>
      </dd>
      <dd><a href="javascript:void(0);" class="click-link item-a" ><span class="a-text fl">最新动态</span><i class="icon-i w30 h30 arrow-down-icon mt5 fr"></i></a>
        <ul id="ZXDT_UL">
        </ul>
      </dd>
      <dd><a href="javascript:void(0);" class="click-link item-a" ><span class="a-text fl">运营管理</span><i class="icon-i w30 h30 arrow-down-icon mt5 fr"></i></a>
        <ul>
        <%
						if (dimengSession.isAccessableResource(SearchGggl.class)) {
					%>
					<li><a class="click-link <%if("GGGL".equals(CURRENT_SUB_CATEGORY)){%>select-a<%}%>" href="<%=controller.getURI(request, SearchGggl.class)%>" target="mainFrame">广告管理</a></li>
					<%
						} else {
					%>
					<li><a href="javascript:void(0)" class="disabled">广告管理</a></li>
					<%
						}
					%>	
		<%
					//开关判断，为false，则不显示视频
					if(BooleanParser.parse(configureProvider.getProperty(SiteSwitchVariable.SITE_VIDEO_SWITCH))){
						if (dimengSession.isAccessableResource(SearchSpsc.class)) {
					%>
					<li><a class="click-link <%if("SPSC".equals(CURRENT_SUB_CATEGORY)){%>select-a<%}%>" href="<%=controller.getURI(request, SearchSpsc.class)%>" target="mainFrame">视频管理</a></li>
					<%
						} else {
					%>
					<li><a href="javascript:void(0)" class="disabled">视频管理</a></li>
					<%
						}}
					%>
		<%
						if (dimengSession.isAccessableResource(SearchXytk.class)) {
					%>
					<li><a class="click-link <%if("XYTK".equals(CURRENT_SUB_CATEGORY)){%>select-a<%}%>" href="<%=controller.getURI(request, SearchXytk.class)%>" target="mainFrame">协议条款</a></li>
					<%
						} else {
					%>
					<li><a href="javascript:void(0)" class="disabled">协议条款</a></li>
					<%
						}
					%>
		<%
						if (dimengSession.isAccessableResource(SearchWzgg.class)) {
					%>
					<li><a class="click-link <%if("WZGG".equals(CURRENT_SUB_CATEGORY)){%>select-a<%}%>" href="<%=controller.getURI(request, SearchWzgg.class)%>" target="mainFrame">网站公告</a></li>
					<%
						} else {
					%>
					<li><a href="javascript:void(0)" class="disabled">网站公告</a></li>
					<%
						}
					%>
			<%
						if (dimengSession.isAccessableResource(SearchYqlj.class)) {
					%>
					<li><a class="click-link <%if("YQLJ".equals(CURRENT_SUB_CATEGORY)){%>select-a<%}%>" href="<%=controller.getURI(request, SearchYqlj.class)%>" target="mainFrame">友情链接</a></li>
					<%
						} else {
					%>
					<li><a href="javascript:void(0)" class="disabled">友情链接</a></li>
					<%
						}
					%>	
		            <%
						if (dimengSession.isAccessableResource(SearchKfzx.class)) {
					%>
					<li><a class="click-link <%if("KFZX".equals(CURRENT_SUB_CATEGORY)){%>select-a<%}%>" href="<%=controller.getURI(request, SearchKfzx.class)%>" target="mainFrame">客服中心</a></li>
					<%
						} else {
					%>
					<li><a href="javascript:void(0)" class="disabled">客服中心</a></li>
					<%
						}
					%>
			        <%
				        if (dimengSession.isAccessableResource(UpdateOperateData.class)) {
			        %>
			        <li><a class="click-link <%if("YYSJGL".equals(CURRENT_SUB_CATEGORY)){%>select-a<%}%>" href="<%=controller.getURI(request, UpdateOperateData.class)%>" target="mainFrame">运营数据初始值设置</a></li>
			        <%
			            } else {
			        %>
			        <li><a href="javascript:void(0)" class="disabled">运营数据初始值设置</a></li>
			        <%
				        }
			        %>
			
			
		</ul>
      </dd>
      <dd><a href="javascript:void(0);" class="click-link item-a" ><span class="a-text fl">投资攻略</span><i class="icon-i w30 h30 arrow-down-icon mt5 fr"></i></a>
        <ul id="TZGL_UL">
        </ul>
      </dd>
     <!--  <dd><a href="javascript:void(0);" class="click-link item-a" ><span class="a-text fl">帮助中心</span><i class="icon-i w30 h30 arrow-down-icon mt5 fr"></i></a>
        <ul id="BZZX_UL">
        </ul>
      </dd> -->
       <dd><a href="javascript:void(0);" class="click-link item-a" ><span class="a-text fl">帮助中心</span><i class="icon-i w30 h30 arrow-down-icon mt5 fr"></i></a>
        <ul>
        <%
						if (dimengSession.isAccessableResource(SearchCzytxWtlx.class)) {
					%>
					<li><a class="click-link <%if("CZYTX".equals(CURRENT_SUB_CATEGORY)){%>select-a<%}%>" href="<%=controller.getURI(request, SearchCzytxWtlx.class)%>" target="mainFrame">充值与提现</a></li>
					<%
						} else {
					%>
					<li><a href="javascript:void(0)" class="disabled">充值与提现</a></li>
					<%
						}
					%>	
		<%
						if (dimengSession.isAccessableResource(SearchTzyhkWtlx.class)) {
					%>
					<li><a class="click-link <%if("TZYHK".equals(CURRENT_SUB_CATEGORY)){%>select-a<%}%>" href="<%=controller.getURI(request, SearchTzyhkWtlx.class)%>" target="mainFrame">投资与回款</a></li>
					<%
						} else {
					%>
					<li><a href="javascript:void(0)" class="disabled">投资与回款</a></li>
					<%
						}
					%>
		<%
						if (dimengSession.isAccessableResource(SearchZhyaqWtlx.class)) {
					%>
					<li><a class="click-link <%if("ZHYAQ".equals(CURRENT_SUB_CATEGORY)){%>select-a<%}%>" href="<%=controller.getURI(request, SearchZhyaqWtlx.class)%>" target="mainFrame">账户与安全</a></li>
					<%
						} else {
					%>
					<li><a href="javascript:void(0)" class="disabled">账户与安全</a></li>
					<%
						}
					%>	
		<%
						if (dimengSession.isAccessableResource(SearchZcydlWtlx.class)) {
					%>
					<li><a class="click-link <%if("ZCYDL".equals(CURRENT_SUB_CATEGORY)){%>select-a<%}%>" href="<%=controller.getURI(request, SearchZcydlWtlx.class)%>" target="mainFrame">注册与登录</a></li>
					<%
						} else {
					%>
					<li><a href="javascript:void(0)" class="disabled">注册与登录</a></li>
					<%
						}
					%>	
        </ul>
      </dd>
      
      
      <dd><a href="javascript:void(0);" class="click-link item-a" >
		   <span class="a-text fl">信息披露</span>
		   <i class="icon-i w30 h30 arrow-down-icon mt5 fr"></i>
	     </a>
        <ul id="XXPL_UL">
        </ul>
      </dd>
    </dl>
  </div>
<%@include file="/WEB-INF/include/jquery.jsp" %>
<script type="text/javascript">
	showAllMenu();
	function showMenuText(code,menuType,ulId){
		 var data = {"code":code,"CURRENT_SUB_CATEGORY":menuType};
		 $.ajax({
			 type: "post",
			 dataType: "html",
			 url: "<%=controller.getURI(request, Xcgl.class)%>",
			 data: data,
			 success: function (data) {
				 data = eval("(" + data + ")");
				 $("#"+ulId).children().remove();
				 $(data.liStr).appendTo("#"+ulId);
			 }
		 });
	 }
	function showAllMenu(){
		showGYWMMenu("GSJJ");
		showAQBZMenu("BXDB");
		showZXDTMenu("MTBD");
		showTZGLMenu("XSZY");
		//showBZZXMenu("CZYTX");
		showXXPLMenu("BAXX");
	}
	//关于我们
	function showGYWMMenu(menuType){
		showMenuText(1,menuType,"GYWM_UL");
	}
	//安全保障
	function showAQBZMenu(menuType){
		showMenuText(2,menuType,"AQBZ_UL");
	}
	//最新动态
	function showZXDTMenu(menuType){
		showMenuText(3,menuType,"ZXDT_UL");
	}
	//投资攻略
	function showTZGLMenu(menuType){
		showMenuText(4,menuType,"TZGL_UL");
	}
	//帮助中心
	function showBZZXMenu(menuType){
		showMenuText(5,menuType,"BZZX_UL");
	}
	//信息披露
	function showXXPLMenu(menuType){
		showMenuText(34,menuType,"XXPL_UL");
	}
	
</script>
<%%>
 