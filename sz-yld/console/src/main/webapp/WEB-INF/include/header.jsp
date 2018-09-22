<%@page import="com.dimeng.p2p.common.Constant"%>
<%@page import="com.dimeng.p2p.modules.systematic.console.service.SysUserManage"%>
<%@page import="com.dimeng.p2p.variables.defines.URLVariable"%>
<%@page import="com.dimeng.p2p.variables.defines.SystemVariable"%>
<%@page import="java.util.UUID"%>
<%@page import="com.dimeng.p2p.console.servlets.common.UpdatePass"%>
<%@page import="java.util.Locale"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.dimeng.p2p.console.servlets.Logout"%>
<%@page import="com.dimeng.p2p.console.config.ConsoleConst"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.util.Date"%>
<%@page import="com.dimeng.p2p.console.servlets.Login"%>
<%
	if (dimengSession == null || !dimengSession.isAuthenticated()) {
		controller.redirectLogin(request, response, controller.getURI(request, Login.class));
		return;
	}
boolean is_business = Boolean.parseBoolean(configureProvider.getProperty(BusinessVariavle.IS_BUSINESS));
boolean is_mall = Boolean.parseBoolean(configureProvider.getProperty(MallVariavle.IS_MALL));
%>

<%{String warning_id=UUID.randomUUID().toString();%>
<div style="position:fixed; background:#fff; z-index:9999;width:100%;height:768px; overflow:hidden; display:block;" id='<%=warning_id%>'>
<div style='padding-top:18%;padding-left:40%;font-size:18px;line-height:30px;color: #c30'>
警告: 如果看到该页面,可能是因为:
<div style="list-style: decimal outside;">
<div><span>1. 您的浏览器没有启用JavaScript功能;</span></div>
<div><span>2. <%configureProvider.format(out,SystemVariable.SITE_NAME);%>官网地址为 <b><a href="<%configureProvider.format(out, URLVariable.INDEX); %>"><%configureProvider.format(out, URLVariable.INDEX); %></a></b>.</span></div>
</div>
</div>
</div>
<script type="text/javascript">try{document.getElementById('<%=warning_id%>').style.display='none';}catch(e){}</script>
<%}%>


<div class="main-head clearfix"><div class="left-v5-box pl30 clearfix"><%configureProvider.format(out,SystemVariable.SITE_NAME); %> V5.0<span class="time-containe ml40 f14"><%=new SimpleDateFormat("yyyy年MM月dd日 E",Locale.SIMPLIFIED_CHINESE).format(new Date(System.currentTimeMillis()))%></span></div>

<div class="main-head-fr ww80 tr">
<ul class="mr100">
<li><i class="icon-i w30 h30 ml10 va-middle user-icon"></i><span class="span-txt">欢迎您，</span><span class="f18"><%StringHelper.filterHTML(out, dimengSession.getAttribute(ConsoleConst.ACCOUNT_NAME));%></span></li>
<!-- <li><a href="#" class="information-a pr"><i class="icon-i w30 h30 ml10 va-middle information-icon"></i><span class="information-num radius-round">99</span></a></li> -->
<li><a href="<%=controller.getURI(request, UpdatePass.class)%>"><i class="icon-i w30 h30 ml10 va-middle xiugaimima-icon"></i>修改密码</a></li>
<li><a href="<%=controller.getURI(request, Logout.class)%>"><i class="icon-i w30 h30 ml10 va-middle exit-icon"></i>安全退出</a></li>
</ul>
</div>
</div>

<div class="main-nav">
<div class="logo-containe">
<div class="logo"><img src="<%=fileStore.getURL(configureProvider.getProperty(SystemVariable.HTSYLOGO))%>" /></div>
</div>
<div class="top-nav">
  <ul class="clearfix">
  	<%   SysUserManage sysUserM = serviceSession.getService(SysUserManage.class);
  	com.dimeng.p2p.modules.systematic.console.service.entity.SysUser sysU = sysUserM.get(serviceSession.getSession().getAccountId());
	if(Constant.BUSINESS_ROLE_ID != sysU.roleId){ %>
    <li><a href="<%=configureProvider.getProperty(URLVariable.CONSOLE_INDEX_URL)%>" class="main-nav-a <%="SY".equals(CURRENT_CATEGORY)?"select-a":""%>"><i class="icon-i h30 w30 nav-home-icon"></i>首页</a></li>
    <li><a href="<%=controller.getURI(request, com.dimeng.p2p.console.servlets.account.Index.class)%>" class="main-nav-a <%="YHGL".equals(CURRENT_CATEGORY)?"select-a":""%>" data-title="user"><i class="icon-i h30 w30 nav-yhgl-icon"></i>会员管理</a></li>
    <li><a href="<%=controller.getURI(request, com.dimeng.p2p.console.servlets.bid.Index.class)%>" class="main-nav-a <%="YWGL".equals(CURRENT_CATEGORY)?"select-a":""%>" data-title="business"><i class="icon-i h30 w30 nav-ywgl-icon"></i>业务管理</a></li>
    <%if(is_mall){%>
	<li><a href="<%=controller.getURI(request, com.dimeng.p2p.console.servlets.mall.Index.class)%>" class="main-nav-a <%="PTSC".equals(CURRENT_CATEGORY)?"select-a":""%>" data-title="propaganda"><i class="icon-i h30 w30 nav-ptsc-icon"></i>积分商城</a></li>
    <%} %>
    <li><a href="<%=controller.getURI(request, com.dimeng.p2p.console.servlets.info.Index.class)%>" class="main-nav-a <%="XCGL".equals(CURRENT_CATEGORY)?"select-a":""%>" data-title="propaganda"><i class="icon-i h30 w30 nav-xcgl-icon"></i>宣传管理</a></li>
    <li><a href="<%=controller.getURI(request, com.dimeng.p2p.console.servlets.system.Index.class)%>" class="main-nav-a <%="XTGL".equals(CURRENT_CATEGORY)?"select-a":""%>" data-title="system"><i class="icon-i h30 w30 nav-xtgl-icon"></i>系统管理</a></li>
    <li><a href="<%=controller.getURI(request, com.dimeng.p2p.console.servlets.finance.Index.class)%>" class="main-nav-a <%="CWGL".equals(CURRENT_CATEGORY)?"select-a":""%>" data-title="finance"><i class="icon-i h30 w30 nav-cwgl-icon"></i>财务管理</a></li>
    <li><a href="<%=controller.getURI(request, com.dimeng.p2p.console.servlets.statistics.Index.class)%>" class="main-nav-a <%="TJGL".equals(CURRENT_CATEGORY)?"select-a":""%>" data-title="statistics"><i class="icon-i h30 w30 nav-tjgl-icon"></i>统计管理</a></li>
    <li><a href="<%=controller.getURI(request, com.dimeng.p2p.console.servlets.spread.Index.class)%>" class="main-nav-a <%="TGGL".equals(CURRENT_CATEGORY)?"select-a":""%>" data-title="extension"><i class="icon-i h30 w30 nav-tggl-icon"></i>推广管理</a></li>
    <li><a href="<%=controller.getURI(request, com.dimeng.p2p.console.servlets.base.Index.class)%>" class="main-nav-a <%="JCXXGL".equals(CURRENT_CATEGORY)?"select-a":""%>" data-title="basics"><i class="icon-i h30 w30 nav-jbsz-icon"></i>基本设置</a></li>
    <li><a href="<%=controller.getURI(request, com.dimeng.p2p.console.servlets.app.manage.AppVerSearch.class)%>" class="main-nav-a <%="APPVER".equals(CURRENT_CATEGORY)?"select-a":""%>" data-title="app"><i class="icon-i h30 w30 nav-appgl-icon"></i>APP管理</a></li>
    <%}else{ %>
    <li><a href="<%=controller.getURI(request, com.dimeng.p2p.console.servlets.system.htzh.business.BusinessUserList.class)%>" class="main-nav-a <%="XTGL".equals(CURRENT_CATEGORY)?"select-a":""%>" data-title="system"><i class="icon-i h30 w30 nav-xtgl-icon"></i>系统管理</a></li>
    <%} %>
  </ul>
 </div>
</div>
<%@include file="/WEB-INF/include/script.jsp" %>
<script type="text/javascript">
var type = '<%=CURRENT_SUB_CATEGORY%>';
$(function(){
	$(".click-link").each(function(){
		if($(this).hasClass("select-a")){
			$(this).parents("dl").find(".click-link").removeClass("select-a");
		  	$(this).addClass("select-a");
		 	/* $(this).find(".arrow-down-icon").addClass("arrow-up-icon");
			$(this).parents("dd").siblings("dd").find(".arrow-down-icon").removeClass("arrow-up-icon");
			$(this).parents("dd").find("ul").slideDown(400);
			$(this).parents("dd").siblings("dd").find("ul").slideUp(400); */
		}
	});
});

function pageSubmit(_obj){
	currentPage = $(_obj).prev().val();
	var re =  /^[1-9]+[0-9]*]*$/;
	if(!re.test(currentPage)){
		return;
	}
	var $pagelist = $(_obj).prevAll().filter(".number");
	$pagelist.each(function(){
		if(currentPage==$(this).html()){
			pageParam(this);
		}
	});
	
}
</script>