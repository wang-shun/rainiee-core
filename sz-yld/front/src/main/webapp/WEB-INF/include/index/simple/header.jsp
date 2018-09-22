<%@page import="com.dimeng.p2p.repeater.score.MallChangeManage"%>
<%@page import="com.dimeng.p2p.S61.enums.T6110_F07"%>
<%@page import="com.dimeng.p2p.S61.enums.T6110_F10"%>
<%@page import="com.dimeng.p2p.S61.enums.T6110_F06"%>
<%@page import="com.dimeng.p2p.S61.enums.T6110_F18"%>
<%@page import="com.dimeng.p2p.S61.entities.T6110"%>
<%@page import="com.dimeng.p2p.account.front.service.UserInfoManage"%>
<%@page import="com.dimeng.p2p.account.front.service.UserManage"%>
<%@page import="com.dimeng.util.filter.HTMLFilter"%>
<%@page import="java.util.UUID"%>
<%@page import="com.dimeng.p2p.front.servlets.Logout"%>
<%@page import="com.dimeng.p2p.variables.defines.SystemVariable"%>
<%@page import="com.dimeng.p2p.variables.defines.URLVariable"%>
<%@page import="java.util.regex.Matcher"%>
<%@page import="java.util.regex.Pattern"%>
<%@page import="com.dimeng.p2p.modules.base.front.service.CustomerServiceManage" %>
<%@page import="com.dimeng.p2p.S50.enums.T5012_F03" %>
<%@page import="com.dimeng.p2p.S50.entities.T5012" %>

<%
	//是否允许访问移动端地址
	String isAllowAccessApp=configureProvider.getProperty(SystemVariable.IS_ALLOW_ACCESS_APP_ADDRESS);
//是否有微信端
	String hasWeixin=configureProvider.getProperty(SystemVariable.HAS_WEIXIN);
//是否有app终端
	String hasApp=configureProvider.getProperty(SystemVariable.HAS_APP);

	if("true".equals(isAllowAccessApp)){
		//\b 是单词边界(连着的两个(字母字符 与 非字母字符) 之间的逻辑上的间隔),字符串在编译时会被转码一次,所以是 "\\b"
		//\B 是单词内部逻辑间隔(连着的两个字母字符之间的逻辑上的间隔)
		String androidReg = "\\bandroid|Nexus\\b";
		String iosReg = "ip(hone|od|ad)";
		Pattern androidPat = Pattern.compile(androidReg, Pattern.CASE_INSENSITIVE);
		Pattern iosPat = Pattern.compile(iosReg, Pattern.CASE_INSENSITIVE);
		String path = request.getContextPath();
		String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
		String userAgent = request.getHeader( "USER-AGENT" ).toLowerCase();
		if(null == userAgent){
			userAgent = "";
		}
		if(androidPat.matcher(userAgent).find()||iosPat.matcher(userAgent).find()){
			if("true".equals(hasWeixin)){
				response.sendRedirect(basePath+"app/weixin");
				return;
			}
			if("true".equals(hasApp)){
				response.sendRedirect(basePath+"app/downloadApp.htm");
				return;
			}
		}
	}
	

	String  url  =  "http://"  +  request.getServerName()  +  ":"  +  request.getServerPort()  +  request.getContextPath()+request.getServletPath().substring(0,request.getServletPath().lastIndexOf("/")+1);  
/* 	System.out.println("URL："+url);    */   		
%>

<%{String warning_id=UUID.randomUUID().toString();%>
<div style="position:fixed; background:#fff; z-index:9999;width:100%;height:40px; overflow:hidden; display:block;" id='<%=warning_id%>'>
	<div style='padding-left:28%;color: #c30;font-size:18px;'>
		警告: 如果看到该信息,可能是因为您的浏览器没有启用JavaScript功能，请开启该功能并在重启后访问网站。
	</div>
</div>
<script type="text/javascript">try{document.getElementById('<%=warning_id%>').style.display='none';}catch(e){}</script>
<%}%>
<%@include file="/WEB-INF/include/script.jsp"%>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/dialog.js"></script>
<script type="text/javascript" src="http://qzonestyle.gtimg.cn/qzone/openapi/qc_loader.js" data-appid="<%configureProvider.format(out,SystemVariable.INDEX_QQ_KEY);%>"  data-redirecturi="<%=configureProvider.format(URLVariable.REGISTER_QQ_LOGIN) %>"  charset="utf-8"> </script>
<script type="text/javascript" src="http://qzonestyle.gtimg.cn/qzone/openapi/qc_loader.js" charset="utf-8"  data-callback="true"></script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/jquery-select-list.js"></script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/simple/js/common.js"></script>
<script type="text/javascript">

	function pageSubmit(_obj){
		if(setCurrentPage(_obj)){
			toAjaxPage();
		}
	}

	//输入分页，点确定查询时，设置当前页
	function setCurrentPage(_obj){
		var currentPage1 = $(_obj).prev().val();
		var re =  /^[1-9]+[0-9]*]*$/;
		if(!re.test(currentPage1)){
			return false;
		}
		currentPage = currentPage1;
		if(currentPage>$(_obj).prev().attr("maxSize")*1){
			currentPage = $(_obj).prev().attr("maxSize")*1;
		}
		currentPage = parseInt(currentPage);
		return true;
	}

	function goPageSubmit(obj, pagingPath) {

		var goPage = $(obj).prev().val();
		var re = /^[1-9]+[0-9]*]*$/;
		if (!re.test(goPage)) {
			return;
		}
		if (goPage > $(obj).prev().attr("maxSize") * 1) {
			goPage = $(obj).prev().attr("maxSize") * 1;
		}
		var _pageUrl = pagingPath + goPage;
		if (pagingPath.indexOf(".html") > 0 || pagingPath.indexOf(".htm") > 0) {
			if (pagingPath.indexOf("?") > 0) {
				_pageUrl = pagingPath + "&paging.current=" + goPage;
			} else {
				_pageUrl = pagingPath + "?paging.current=" + goPage;
			}
		}
		location.href = _pageUrl;
	}

	function DMQQLogout() {
		//1 QQ退出
		/* QC.Login.signOut(); */
		//2 本地退出
		location.href ="<%=controller.getURI(request, Logout.class)%>";
	}
</script>
<%@include file="/WEB-INF/include/float.jsp"%>
<!--顶部-->
<div class="top_bar">
    <div class="wrap clearfix">
        <div class="phone fl">
            <span class="f14 gray87">客服电话 :</span>
            <span class="f14 gray87"><%configureProvider.format(out, SystemVariable.SITE_CUSTOMERSERVICE_TEL);%></span>
        </div>
        <div class="focus fl">
            <div class="fl gray87 f14 title">关注我们 :</div>
            <div class="wx icon">
                <div class="code">
                    <i class="arrow"></i>
                    <div class="border">
                        <img src="<%="".equals(fileStore.getURL(configureProvider.getProperty(SystemVariable.QTWXEWM))) ? controller.getStaticPath(request)+"/images/weixin.png" : fileStore.getURL(configureProvider.getProperty(SystemVariable.QTWXEWM))%>"><br />
                        关注雨莅贷微信
                    </div>
                </div>
            </div>
            <a href="#" class="wb icon"></a>
            <div class="app icon">
                <div class="code">
                    <i class="arrow"></i>
                    <div class="border">
                        <img src="<%="".equals(fileStore.getURL(configureProvider.getProperty(SystemVariable.QTSJKHD))) ? controller.getStaticPath(request)+"/images/weixin.png" : fileStore.getURL(configureProvider.getProperty(SystemVariable.QTSJKHD))%>"><br />
                        关注雨莅贷APP
                    </div>
                </div>
            </div>
        </div>
        <!--未登录-->
        <%if(dimengSession == null || !dimengSession.isAuthenticated()){ %>
	        <div class="nav fr">
	            <ul> 
	                <li><a href="<%configureProvider.format(out, URLVariable.LOGIN);%>" class="orange_fb">登录</a></li>
	                <li class="last"><a href="<%configureProvider.format(out, URLVariable.REGISTER);%>">注册</a></li>
	            </ul>
	        </div>
		<%}else{
								UserInfoManage uManage = serviceSession.getService(UserInfoManage.class);
								T6110 t6110 =uManage.getUserInfo(serviceSession.getSession().getAccountId());
								T6110_F06 userType = t6110.F06;
								int count = serviceSession.getService(UserManage.class).getUnReadLetter();
								String letterCnt = String.valueOf(count);
								if(count>99){
									letterCnt= 99+"+";
								}
							%>
		<!--未登录 end-->
			       <!--已登录-->
        <div class="nav_user fr">
            <ul class="clearfix">
            	<li>你好，</li>
                <li class="user">
                    <a href="<%=configureProvider.format(URLVariable.USER_INDEX) %>" class="name item">
                        <span class="highlight"><%StringHelper.truncation(new HTMLFilter(out), serviceSession.getService(UserManage.class).getAccountName(), 6);%></span>
                    </a>
                    <div class="child">
                     			<a href="<%configureProvider.format(out,URLVariable.USER_CHARGE);%>">充值</a>
									<%if(T6110_F07.HMD == t6110.F07){%>
									<%if(userType==T6110_F06.ZRR || t6110.F18 == T6110_F18.S){%>
									<a href="<%configureProvider.format(out,URLVariable.USER_INDEX);%>/financing/zqzr/zqzrz.html">理财管理</a>
									<%}%>
									<%if(t6110.F10==T6110_F10.F){%>
									<a href="<%configureProvider.format(out,URLVariable.USER_CREDIT);%>">借款管理</a>
									<%}%>
									<%}else{%>
									<a href="<%configureProvider.format(out,URLVariable.USER_WITHDRAW);%>">提现</a>
									<a href="<%configureProvider.format(out,URLVariable.USER_CAPITAL);%>">资金管理</a>
									<%if(userType==T6110_F06.ZRR || t6110.F18 == T6110_F18.S){%>
									<a href="<%configureProvider.format(out,URLVariable.USER_FINANCING);%>">理财管理</a>
									<%}%>
									<%if(t6110.F10==T6110_F10.F){%>
									<a href="<%configureProvider.format(out,URLVariable.USER_CREDIT);%>">借款管理</a>
									<%} %>
									<%}%>
									<a href="javascript:void(0);" onclick="DMQQLogout();">安全退出</a>
                    </div>
                </li>
                <li class="letter last">
                    <a href="<%configureProvider.format(out,URLVariable.USER_LETTER);%>" class='item'>站内信<span class="number"><%=letterCnt %></span>
                    </a>
                </li>
            </ul>
        </div>
        <%}%>
    </div>
</div>
<!--顶部-->

<!--头部-->
<div class="header">
    <div class="wrap clearfix pr">
    	<div class="logo logo_location"><a href="<%configureProvider.format(out,URLVariable.INDEX);%>"><img src="<%="".equals(fileStore.getURL(configureProvider.getProperty(SystemVariable.QTLOGO))) ? controller.getStaticPath(request)+"/images/logo.jpg" : fileStore.getURL(configureProvider.getProperty(SystemVariable.QTLOGO))%>" /></a></div>
    	<div class="main_nav">
        <ul class="clearfix">
            <li class="item nav1"><div class="nav"><a href="<%configureProvider.format(out,URLVariable.INDEX);%>">首页</a></div></li>
            <li class="item nav2">
                <div class="nav"><a href="<%configureProvider.format(out,URLVariable.FINANCING_CENTER);%>">我要投资</a></div>
                <ul class="subnav">
                    <div class="arrow"></div>
                    <li><a href="<%configureProvider.format(out,URLVariable.FINANCING_SBTZ);%>">投资项目</a></li>
                    <li><a href="<%configureProvider.format(out,URLVariable.FINANCING_ZQZR);%>">债权转让</a></li>
                </ul>
            </li>
            
            <li class="item nav3">
                <div class="nav"><a href="<%configureProvider.format(out,URLVariable.CREDIT_CENTER);%>">我要借款</a></div>
                <ul class="subnav">
                    <div class="arrow"></div>
                    <li><a href="<%configureProvider.format(out,URLVariable.CREDIT_XJD);%>">信用贷</a></li>
                    <li><a href="<%configureProvider.format(out,URLVariable.CREDIT_DKYX);%>">个人借款</a></li>
                    <li><a href="<%configureProvider.format(out,URLVariable.CREDIT_QYDKYX);%>" class="last">企业借款</a></li>
                </ul>
            </li>
        <%--     <li class="item"><div class="nav"><a href="<%configureProvider.format(out, URLVariable.MALLINDEX_URL);%>">积分商城</a></div></li> --%>
    <%--         <li class="item"><div class="nav"><a href="<%configureProvider.format(out,URLVariable.FINANCING_GYJZ);%>">公益捐赠</a></div></li> --%>
            <li class="item nav4"><div class="nav"><a href="<%configureProvider.format(out, URLVariable.GYWM_GSJJ);%>">关于我们</a></div></li>           
        </ul>
    </div>       
    </div>
</div>
<!--头部-->






<%-- <!--顶部条-->
<div class="top-container">
	<div class="wrap">
		<div class="top-center">
			<div class="logo-container"><a href="<%configureProvider.format(out,URLVariable.INDEX);%>"><img src="<%="".equals(fileStore.getURL(configureProvider.getProperty(SystemVariable.QTLOGO))) ? controller.getStaticPath(request)+"/images/logo.jpg" : fileStore.getURL(configureProvider.getProperty(SystemVariable.QTLOGO))%>" title="<%configureProvider.format(out,SystemVariable.SITE_NAME);%>"></a></div>
			<div class="clearfix">
				<div class="head-container clearfix">
					<div class="head-login-container">
						<ul>
							<!--未登录-->
							<%if(dimengSession == null || !dimengSession.isAuthenticated()){ %>
							<!--登录前-->
							<li class="li-btn"><a href="<%configureProvider.format(out, URLVariable.REGISTER);%>" class="reg-btn">免费注册</a></li>
							<li class="li-btn"><a href="<%configureProvider.format(out, URLVariable.LOGIN);%>" class="login-btn">立即登录</a></li>
							<%}else{
								UserInfoManage uManage = serviceSession.getService(UserInfoManage.class);
								T6110 t6110 =uManage.getUserInfo(serviceSession.getSession().getAccountId());
								T6110_F06 userType = t6110.F06;
								int count = serviceSession.getService(UserManage.class).getUnReadLetter();
								String letterCnt = String.valueOf(count);
								if(count>99){
									letterCnt= 99+"+";
								}
							%>
							<!--未登录 end-->

							<!--已登录--2><!--已登录-->
							<li>您好，</li>
							<li class="user">
								<a href="<%=configureProvider.format(URLVariable.USER_INDEX) %>" title="<%StringHelper.filterHTML(out, serviceSession.getService(UserManage.class).getAccountName());%>" class="highlight"><%StringHelper.truncation(new HTMLFilter(out), serviceSession.getService(UserManage.class).getAccountName(), 6);%></a>
								<div class="child">
									<div class="arrow"></div>
									<a href="<%configureProvider.format(out,URLVariable.USER_CHARGE);%>">充值</a>
									<%if(T6110_F07.HMD == t6110.F07){%>
									<%if(userType==T6110_F06.ZRR || t6110.F18 == T6110_F18.S){%>
									<a href="<%configureProvider.format(out,URLVariable.USER_INDEX);%>/financing/zqzr/zqzrz.html">理财管理</a>
									<%}%>
									<%if(t6110.F10==T6110_F10.F){%>
									<a href="<%configureProvider.format(out,URLVariable.USER_CREDIT);%>">借款管理</a>
									<%}%>
									<%}else{%>
									<a href="<%configureProvider.format(out,URLVariable.USER_WITHDRAW);%>">提现</a>
									<a href="<%configureProvider.format(out,URLVariable.USER_CAPITAL);%>">资金管理</a>
									<%if(userType==T6110_F06.ZRR || t6110.F18 == T6110_F18.S){%>
									<a href="<%configureProvider.format(out,URLVariable.USER_FINANCING);%>">理财管理</a>
									<%}%>
									<%if(t6110.F10==T6110_F10.F){%>
									<a href="<%configureProvider.format(out,URLVariable.USER_CREDIT);%>">借款管理</a>
									<%} %>
									<%}%>
									<a href="javascript:void(0);" onclick="DMQQLogout();">安全退出</a>
								</div>
							</li>
							<%}%>

						</ul>
					</div>
					<div class="top_bar"><a href="<%configureProvider.format(out, URLVariable.COMPANY_SINA_URL); %>" class="icon sine-icon mr15"></a>
						<a href="<%configureProvider.format(out, URLVariable.COMPANY_WECHAT_URL); %>" class="icon wechat-icon mr15">
							<div class="code tc"><i class="arrow"></i>
								<div class="border"><img src="<%="".equals(fileStore.getURL(configureProvider.getProperty(SystemVariable.QTWXEWM))) ? controller.getStaticPath(request)+"/images/weixin.png" : fileStore.getURL(configureProvider.getProperty(SystemVariable.QTWXEWM))%>"><br />
									关注迪蒙网贷微信 </div>
							</div>
						</a>
						<a href="<%configureProvider.format(out, URLVariable.INDEX_TZGL);%>">新手指引</a> |
						<a href="<%configureProvider.format(out, URLVariable.HELP_CENTER);%>">帮助中心</a> |
						<a href="<%configureProvider.format(out, URLVariable.APP_DOWNLOAD);%>">手机客户端
							<div class="code tc"><i class="arrow"></i>
								<div class="border">
									<img src="<%="".equals(fileStore.getURL(configureProvider.getProperty(SystemVariable.QTSJKHD))) ? controller.getStaticPath(request)+"/images/weixin.png" : fileStore.getURL(configureProvider.getProperty(SystemVariable.QTSJKHD))%>"><br />下载客户端APP
									</div>
							</div>
						</a> </div>
				</div>
			</div>
			<div class="top-nav clearfix">
				<ul class="clearfix">
					<li><a href="<%configureProvider.format(out,URLVariable.INDEX);%>" class="nav-a">首页</a></li>
					<li>
						<%if(BooleanParser.parse(configureProvider.getProperty(SystemVariable.IS_HAS_INVEST_PRODUCT_INTRODUCE))){%>
						<a class="nav-a" href="<%configureProvider.format(out,URLVariable.FINANCING_CENTER);%>">我要投资</a>
						<%} else {%>
						<a class="nav-a" href="<%configureProvider.format(out,URLVariable.FINANCING_SBTZ);%>">我要投资</a>
						<%}%>
						<dl class="subnav">
							<dd><a href="<%configureProvider.format(out,URLVariable.FINANCING_SBTZ);%>">投资项目</a></dd>
							<dd><a href="<%configureProvider.format(out,URLVariable.FINANCING_ZQZR);%>">债权转让</a></dd>
						</dl>
					</li>
					<li><a href="<%configureProvider.format(out,URLVariable.CREDIT_CENTER);%>" class="nav-a">我要借款</a>
						<dl class="subnav">
							<%if (dimengSession == null || !dimengSession.isAuthenticated()) {%>
							<dd><a href="<%configureProvider.format(out,URLVariable.CREDIT_XJD);%>">信用贷</a></dd>
							<% if(BooleanParser.parse(configureProvider.getProperty(SiteSwitchVariable.GUARANTEE_DBD_SWITCH))){ %>
							<dd><a href="<%configureProvider.format(out,URLVariable.GUARANTEE_DBD);%>">担保贷</a></dd>
							<%} %>
							<%}else{
								UserInfoManage uManage = serviceSession.getService(UserInfoManage.class);
								T6110 t6110 =uManage.getUserInfo(serviceSession.getSession().getAccountId());
								if(T6110_F07.HMD != t6110.F07 && t6110.F06 == T6110_F06.ZRR){
							%>
							<dd><a href="<%configureProvider.format(out,URLVariable.CREDIT_XJD);%>">信用贷</a></dd>
							<%}}%>
							<dd><a href="<%configureProvider.format(out,URLVariable.CREDIT_DKYX);%>">个人借款</a></dd>
							<dd><a href="<%configureProvider.format(out,URLVariable.CREDIT_QYDKYX);%>">企业借款</a></dd>
						</dl>
					</li>
					<li><a href="<%configureProvider.format(out, URLVariable.GYWM_GSJJ);%>" class="nav-a">关于我们</a></li>
					<li><a href="<%configureProvider.format(out, URLVariable.XXPL_BAXX);%>" class="nav-a">信息披露</a></li>
					<li><a href="<%configureProvider.format(out,URLVariable.USER_INDEX);%>" class="nav-a">我的账户</a></li>
				</ul>
			</div>
		</div>
	</div>
</div>

<div id="info"></div>
<div class="popup_bg" style="display:none"></div>
<%if(dimengSession!=null&&dimengSession.isAuthenticated()&&dimengSession.isOtherLogin()){%>
<script type="text/javascript">
	$("#info").html(showSuccInfo("您的帐号已在另一地点登录，您被迫下线","error","<%=configureProvider.format(URLVariable.LOGIN)%>"));
	$("div.popup_bg").show();
</script>
<%}%>
<!--头部--> --%>
