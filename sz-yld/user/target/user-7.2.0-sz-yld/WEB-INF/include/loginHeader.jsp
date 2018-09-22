<%@page import="com.dimeng.p2p.repeater.score.MallChangeManage"%>
<%@page import="java.util.Date"%>
<%@page import="freemarker.template.utility.DateUtil"%>
<%@page import="com.dimeng.p2p.account.user.service.UserManage"%>
<%@page import="com.dimeng.p2p.S61.enums.T6110_F10"%>
<%@page import="com.dimeng.p2p.S61.enums.T6110_F06"%>
<%@page import="com.dimeng.p2p.S61.enums.T6110_F07"%>
<%@page import="com.dimeng.p2p.S61.entities.T6110"%>
<%@page import="com.dimeng.p2p.account.user.service.UserInfoManage"%>
<%@page import="com.dimeng.p2p.account.user.service.LetterManage"%>
<%@page import="com.dimeng.util.filter.HTMLFilter"%>
<%@page import="java.util.UUID"%>
<%@page import="com.dimeng.p2p.user.servlets.Logout"%>
<%@page import="com.dimeng.p2p.variables.defines.URLVariable"%>
<%@page import="com.dimeng.p2p.variables.defines.SystemVariable"%>
<%@page import="java.util.regex.Matcher"%>
<%@page import="java.util.regex.Pattern"%>
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
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+"/";
	String userAgent = request.getHeader( "USER-AGENT" ).toLowerCase();
	if(null == userAgent){
	    userAgent = "";
	}
	if(androidPat.matcher(userAgent).find()||iosPat.matcher(userAgent).find()){
	    if("true".equals(hasWeixin)){
	    	response.sendRedirect(basePath+"app");	
	    	return;
	    }
	    if("true".equals(hasApp)){
	    	response.sendRedirect(basePath+"app.html");	
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
<script type="text/javascript" src="http://qzonestyle.gtimg.cn/qzone/openapi/qc_loader.js"  data-appid="<%configureProvider.format(out,SystemVariable.INDEX_QQ_KEY);%>" data-redirecturi="<%configureProvider.format(out,URLVariable.INDEX); %>/index.html"  charset="utf-8"> </script>
<script type="text/javascript" src="http://qzonestyle.gtimg.cn/qzone/openapi/qc_loader.js" charset="utf-8" data-callback="true"></script>
<script type="text/javascript">
<%-- $(function(){
	$(function(){
		$('.main_nav li').mouseover(function(){
	        $(this).children().next().show();
			  $(this).addClass("cur");
	   }); 
		 $('.main_nav li').mouseout(function(){
	        $(this).children().next().hide();
			  $(this).removeClass("cur");
	   }); 
		//首页介绍
		$('.intro li').mouseover(function(){
			  $(this).addClass("cur");
	   }); 
		 $('.intro li').mouseout(function(){
			  $(this).removeClass("cur");
	   }); 
		 //关注
		 $('.top_bar .wb').mouseover(function(){
	         $(this).children().show();
	    }); 
		 $('.top_bar .wb').mouseout(function(){
	         $(this).children().hide();
	    }); 
		 $('.top_bar .wx').mouseover(function(){
	         $(this).children().show();
	    }); 
		 $('.top_bar .wx').mouseout(function(){
	         $(this).children().hide();
	    }); 
		  $('.top_bar .tration').mouseover(function(){
	         $(this).children().show();
	    }); 
		 $('.top_bar .tration').mouseout(function(){
	         $(this).children().hide();
	    }); 
		 //顶部用户登录菜单
		 $('.top_bar .user').mouseover(function(){
	         $(this).children().next().show();
	    }); 
		 $('.top_bar .user').mouseout(function(){
	         $(this).children().next().hide();
	    }); 
	});

});
 --%>
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

function DMQQLogout(){
	  //1 QQ退出
	 /*  QC.Login.signOut(); */
	  //2 本地退出
	  location.href = "<%=controller.getURI(request, Logout.class)%>";
	}

</script>
<%
boolean is_mall = Boolean.parseBoolean(configureProvider.getProperty(MallVariavle.IS_MALL));
%>
<!--头部2-->
<div class="login_header w1002 clearfix">
	<div class="logo"><a href="<%configureProvider.format(out,URLVariable.INDEX);%>"><img src="<%="".equals(fileStore.getURL(configureProvider.getProperty(SystemVariable.QTLOGO))) ? controller.getStaticPath(request)+"/images/logo.jpg" : fileStore.getURL(configureProvider.getProperty(SystemVariable.QTLOGO))%>" title="<%configureProvider.format(out,SystemVariable.SITE_NAME);%>"></a></div>
   	<div class="logo_line fl" style="margin-top: 35px;"></div>
    <div class="fl f24 gray6 welcome_t" style="margin-top: 30px;">欢迎登录</div>
</div>
<!--头部2-->