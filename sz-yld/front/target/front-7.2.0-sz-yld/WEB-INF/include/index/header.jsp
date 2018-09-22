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
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/common.js"></script>
<script type="text/javascript">
	/*function pageSubmit(_obj) {
		var currentPage1 = $(_obj).prev().val();
		var re =  /^[1-9]+[0-9]*]*$/;
		if(!re.test(currentPage1)){
			return false;
		}
		currentPage = currentPage1;
		if (currentPage > $(_obj).prev().attr("maxSize") * 1) {
			currentPage = $(_obj).prev().attr("maxSize") * 1;
		}
		var $pagelist = $(_obj).prevAll().filter(".page-link")[0];
		currentPage = parseInt(currentPage);
		setParamToAjax(currentPage, $pagelist);
		/!* $pagelist.each(function(){
			if(currentPage==$(this).html()){
				pageParam(this);
			}
		}); *!/

	}*/

	function pageSubmit(_obj){
		if(setCurrentPage(_obj)){
			toAjaxPage(_obj);
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
<%
boolean is_mall = Boolean.parseBoolean(configureProvider.getProperty(MallVariavle.IS_MALL));
%>
<%-- <%@include file="/WEB-INF/include/float.jsp"%> --%>
<!--顶部条-->
<div class="top_bar">
	<div class="w1002 clearfix">
    	<div class="tel fl"><i class="icon"></i><span class="va_m"><%configureProvider.format(out,SystemVariable.SITE_CUSTOMERSERVICE_TEL);%></span></div>
		<div class="service fl ml20">
			<span>在线客服</span>
					<%{
						CustomerServiceManage customerServiceManage = serviceSession.getService(CustomerServiceManage.class);
						T5012[] customerServices = customerServiceManage.getAll(T5012_F03.QQ);
						if (customerServices != null && customerServices.length > 0) {%>
						<div class="code">
							<i class="arrow"></i>
							<ul>
					<%
							int index = 0;
							for (T5012 customerService : customerServices) {
								index++;
								String image = fileStore.getURL(customerService.F07);
								if(T5012_F03.QQ == customerService.F03){
					%>
					<li>
						<a href="http://wpa.qq.com/msgrd?v=3&uin=<%StringHelper.filterHTML(out,customerService.F06);%>&site=qq&menu=yes"
							target="_blank" title="<%StringHelper.filterHTML(out, customerService.F05);%>">
							<%
								if(!StringHelper.isEmpty(image)){
							%>
							<i class="enterprise_qq1"><img src="<%=image%>"/></i>
							<%}else{%>
							<i class="enterprise_qq1"></i>
							<%}%>
						    <%StringHelper.filterHTML(out, StringHelper.truncation(customerService.F05, 5));%>
						</a>
					</li>
					<%
								}else{%>
					<li>
						<a href="<%StringHelper.filterHTML(out,customerService.F06);%>"
						   target="_blank" title="<%StringHelper.filterHTML(out, customerService.F05);%>">
							<%
								if(!StringHelper.isEmpty(image)){
							%>
							<i><i class="enterprise_qq1"><img src="<%=image%>"/></i>
							<%}else{%>
							<i class="enterprise_qq1"></i>
							<%}%>
							<%StringHelper.filterHTML(out, StringHelper.truncation(customerService.F05, 5));%>
						</a>
					</li>
					<%}
							}%>
							</ul>
						</div>
						<%
						}
					}
					%>

		</div>


		<div class="focus fl ml20">
        	<span class="fl">关注我们：</span>
            <a href="<%configureProvider.format(out, URLVariable.COMPANY_SINA_URL); %>" class="icon wb"></a>
            <div class="icon wx" onclick="window.location.href='<%configureProvider.format(out, URLVariable.COMPANY_WECHAT_URL); %>'">
            	<div class="code">
                	<i class="arrow"></i>
                	<div class="border">
                        <img src="<%="".equals(fileStore.getURL(configureProvider.getProperty(SystemVariable.QTWXEWM))) ? controller.getStaticPath(request)+"/images/weixin.png" : fileStore.getURL(configureProvider.getProperty(SystemVariable.QTWXEWM))%>"><br />扫一扫关注微信
                    </div>
				</div>
            </div>
        </div>
        
        <div class="nav fr">
          <ul>
            	<%if(dimengSession == null || !dimengSession.isAuthenticated()){ %>
            	<!--登录前-->    
            	<li><a href="<%configureProvider.format(out, URLVariable.LOGIN);%>" rel="nofollow" >登录</a></li>
	            <li class="line"></li>
	            <li><a href="<%configureProvider.format(out, URLVariable.REGISTER);%>" rel="nofollow">免费注册</a></li>
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
                <!--登录后-->    
                <li class="hello">您好，</li>
             	<li class="user"><a href="<%=configureProvider.format(URLVariable.USER_INDEX) %>" title="<%StringHelper.filterHTML(out, serviceSession.getService(UserManage.class).getAccountName());%>" class="name"><%StringHelper.truncation(new HTMLFilter(out), serviceSession.getService(UserManage.class).getAccountName(), 6);%></a>
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
            <li class="line"></li>
            <%if(userType==T6110_F06.ZRR && is_mall){
             MallChangeManage mallChangeManage = serviceSession.getService(MallChangeManage.class);
             int carCount = mallChangeManage.queryCarNum();
             %>
            <li class="letter"><a href="<%configureProvider.format(out, URLVariable.MALLSHOPPINGCAR_URL);%>"><i class="top_car_ico"></i>购物车<span class="number" id="carCount"><%=carCount>0 ? carCount : "0" %></span></a></li>
            <li class="line"></li>
            <%} %>
            <li class="letter"><a href="<%configureProvider.format(out,URLVariable.USER_LETTER);%>">站内信<span class="number letter"><%=letterCnt %></span></a></li>
           <%}%>
            
            <li class="line"></li>
            <li><a href="<%configureProvider.format(out, URLVariable.GYWM_LXWM);%>" rel="nofollow">联系我们</a></li>
            <li class="line"></li>
            <li><a href="<%configureProvider.format(out, URLVariable.HELP_CENTER);%>" rel="nofollow">帮助中心</a></li>
            <li class="line"></li>
            <li class="app" onclick="window.location.href='<%configureProvider.format(out, URLVariable.APP_DOWNLOAD);%>'">
              <span>手机客户端</span>
              <div class="code">
                <i class="arrow"></i>
                <div class="border">
                  <img src="<%="".equals(fileStore.getURL(configureProvider.getProperty(SystemVariable.QTSJKHD))) ? controller.getStaticPath(request)+"/images/weixin.png" : fileStore.getURL(configureProvider.getProperty(SystemVariable.QTSJKHD))%>"><br />下载客户端APP
                </div>
              </div>
            </li>
          </ul>
        </div>
        
    </div>
</div>
<!--顶部条-->

<!--头部-->
<div class="header w1002 clearfix">
	<div class="logo"><a href="<%configureProvider.format(out,URLVariable.INDEX);%>"><img src="<%="".equals(fileStore.getURL(configureProvider.getProperty(SystemVariable.QTLOGO))) ? controller.getStaticPath(request)+"/images/logo.jpg" : fileStore.getURL(configureProvider.getProperty(SystemVariable.QTLOGO))%>" title="<%configureProvider.format(out,SystemVariable.SITE_NAME);%>"></a></div>
    <div class="main_nav">
        <ul class="clearfix">
            <li class="item"><div class="nav"><a href="<%configureProvider.format(out,URLVariable.INDEX);%>">首页</a></div></li>
            <li class="item">
				<%if(BooleanParser.parse(configureProvider.getProperty(SystemVariable.IS_HAS_INVEST_PRODUCT_INTRODUCE))){%>
                <div class="nav"><a href="<%configureProvider.format(out,URLVariable.FINANCING_CENTER);%>">我要投资</a></div>
				<%} else {%>
				<div class="nav"><a href="<%configureProvider.format(out,URLVariable.FINANCING_SBTZ);%>">我要投资</a></div>
				<%}%>
                <ul class="subnav">
                    <div class="arrow"></div>
                    <li><a href="<%configureProvider.format(out,URLVariable.FINANCING_SBTZ);%>">投资项目</a></li>
                    <li><a href="<%configureProvider.format(out,URLVariable.FINANCING_ZQZR);%>">债权转让</a></li>
                </ul>
            </li>
            <li class="item">
				<%if(BooleanParser.parse(configureProvider.getProperty(SystemVariable.IS_HAS_LOAN_PRODUCT_INTRODUCE))){%>
				<div class="nav"><a href="<%configureProvider.format(out,URLVariable.CREDIT_CENTER);%>">我要借款</a></div>
				<%} else {%>
				<div class="nav"><a href="<%configureProvider.format(out,URLVariable.CREDIT_XJD);%>">我要借款</a></div>
				<%}%>
                <ul class="subnav">
                    <div class="arrow"></div>
                    <%if (dimengSession == null || !dimengSession.isAuthenticated()) {%>
                        <li><a href="<%configureProvider.format(out,URLVariable.CREDIT_XJD);%>">信用贷</a></li>
						<% if(BooleanParser.parse(configureProvider.getProperty(SiteSwitchVariable.GUARANTEE_DBD_SWITCH))){ %>
							<li><a href="<%configureProvider.format(out,URLVariable.GUARANTEE_DBD);%>">担保贷</a></li>
						<%}%>
                    <%}else{
					    UserInfoManage uManage = serviceSession.getService(UserInfoManage.class);
						T6110 t6110 =uManage.getUserInfo(serviceSession.getSession().getAccountId());
						if(T6110_F07.HMD != t6110.F07 && t6110.F06 == T6110_F06.ZRR){
					%>
					    <li><a href="<%configureProvider.format(out,URLVariable.CREDIT_XJD);%>">信用贷</a></li>
						<% if(BooleanParser.parse(configureProvider.getProperty(SiteSwitchVariable.GUARANTEE_DBD_SWITCH))){ %>
							<li><a href="<%configureProvider.format(out,URLVariable.GUARANTEE_DBD);%>">担保贷</a></li>
						<%}%>
					<%}}%>
                    <li><a href="<%configureProvider.format(out,URLVariable.CREDIT_DKYX);%>">个人借款意向</a></li>
                    <li><a href="<%configureProvider.format(out,URLVariable.CREDIT_QYDKYX);%>">企业借款意向</a></li>
                </ul>
            </li>
            <%if(is_mall){%>
            <li class="item"><div class="nav"><a href="<%configureProvider.format(out, URLVariable.MALLINDEX_URL);%>">积分商城</a></div></li>
            <%}%>
            <li class="item"><div class="nav"><a href="<%configureProvider.format(out, URLVariable.XXPL_BAXX);%>">信息披露</a></div></li>
            <%//开关判断，为false，则不显示公益标
                if(BooleanParser.parse(configureProvider.getProperty(SiteSwitchVariable.DONATION_BID_SWITCH))){%>
            <li class="item"><div class="nav"><a href="<%configureProvider.format(out,URLVariable.FINANCING_GYJZ);%>">公益捐赠</a></div></li>
            <%}%>
            <li class="item"><div class="nav"><a href="<%configureProvider.format(out,URLVariable.USER_INDEX);%>">我的账户</a></div></li>
        </ul>
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
<!--头部-->
