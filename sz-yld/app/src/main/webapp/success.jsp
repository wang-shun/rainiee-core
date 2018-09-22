<%@page import="com.dimeng.framework.resource.ResourceProvider"%>
<%@page import="com.dimeng.framework.config.ConfigureProvider"%>
<%@page import="com.dimeng.framework.resource.ResourceRegister"%>
<%@page import="com.dimeng.p2p.variables.defines.SystemVariable"%>
<%@page import="java.util.Locale"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	final ResourceProvider resourceProvider = ResourceRegister.getResourceProvider(application);
	final ConfigureProvider configureProvider = resourceProvider.getResource(ConfigureProvider.class);
	final String protocolType = configureProvider.format(SystemVariable.SITE_REQUEST_PROTOCOL);
	String url = configureProvider.format(SystemVariable.SITE_DOMAIN);
	final String siteTitle = configureProvider.format(SystemVariable.SITE_TITLE);

	if (url.indexOf(protocolType) == -1) {
		url = protocolType.concat(url);
	}
	
	final String userAgent = request.getHeader("user-agent").toLowerCase(Locale.ENGLISH);
	
	boolean isWx = false;
	boolean isAndroid = false;
	boolean isIos = false;
	// 微信
	if (userAgent.indexOf("micromessenger") > -1)
	{
		isWx = true;
	} 
	// IOS
	else if (userAgent.indexOf("iphone") > -1)
	{
		isIos = true;	
	} 
	// android
	else 
	{
		isAndroid = true;
	}
%>
<html>

	<head>
		<meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no, minimum-scale=1.0, maximum-scale=1.0">
		<meta content="email=no" name="format-detection" />
		<meta content="telephone=no" name="format-detection" />
		<style type="text/css">
			*{margin:0;padding:0;}
			@charset "utf-8";
			body{
				height: 100%;
				padding:0px;
				margin:0px;
				font-size: 100%;
				color:#666;
				width:100%;
				height: 100%;
				font-family: "微软雅黑";
			}	
			@media screen  and (min-width:0px)   and (max-width:240px){
				body{font-size:xx-small;}
				html{font-size: 9px;}
			}
			@media screen  and (min-width:241px) and (max-width:384px){
				body{font-size:x-small;}
				html{font-size: 11px;}
			}
			@media screen  and (min-width:385px) and (max-width:480px){
				body{font-size:small;}
				html{font-size: 13px;}
			}
			@media screen  and  (min-width:481px) and (max-width:640){
				body{font-size:medium;}
				html{font-size: 16px;}
			}
			@media screen  and  (min-width:640px) {
				body{font-size:large;}
				html{font-size: 19px;}
			}
			/*字体大小*/
			.fn-s-11{ font-size: 1.1rem;}
			.fn-s-12{ font-size: 1.2rem;}
			.fn-s-13{ font-size: 1.3rem;}
			.fn-s-15{ font-size: 1.5rem;}
			.fn-s-14{ font-size: 1.4rem;}
			.fn-s-17{ font-size: 1.7rem;}
			.fn-s-19{ font-size: 1.9rem;}
			.fn-s-20{ font-size: 2rem;}
			.fn-s-25{ font-size:2.5rem;}
			
			.fn-w-b{font-weight:bold ;}	
			.fn-c-black{
				#666;
			}
			.fn-c-org{color: #FF7F38;}
			.fn-c-blue{
				color: #34B8FC;
			}
			.ub-ac
			{
				-webkit-box-align:center;
				box-align:center;
			}
			.ub-ae
			{
				-webkit-box-align:end;
				box-align:end;
			}
			
			.ub-pc
			{
				-webkit-box-pack:center;
				box-pack:center;
			}
			.ub-pe
			{
				-webkit-box-pack:end;
				box-pack:end;
			}
			.ub-pj
			{
				-webkit-box-pack:justify;
				box-pack:justify;
			}
			
			.ub-ver
			{
				-webkit-box-orient:vertical;
				box-orient:vertical;
			}
			.ub-ba{
			-webkit-box-orient:	block-axis;
			box-orient:block-axis;
			}
			.ub-bl{
				/* Firefox */
			
			-moz-box-lines:multiple;
			
			/* Safari and Chrome */
			
			-webkit-box-lines:multiple;
			
			/* W3C */
			
			box-lines:multiple;
			}
			.lub-hori
			{
				-webkit-box-orient:horizontal;
				box-orient:horizontal;
			}
			
			.ub-f1
			{
				
				-webkit-box-flex: 1; 
				box-flex: 1;
			}
			
			.ub-f2
			{
				
				-webkit-box-flex: 2;
				box-flex: 2; 
			}
			
			.ub-f3
			{
				
				-webkit-box-flex: 3;
				box-flex: 3;
			}
			
			.ub-f4
			{
				
				-webkit-box-flex: 4;
				box-flex: 4;
			}
			
			.ub-f6
			{
				
				-webkit-box-flex: 6;
				box-flex: 6; 
			}
			.ub
			{
				display: -webkit-box !important;
				display: box !important;
				position:relative;
			}
			.bg-c-gray{
				background-color: #F8F8F8;
			}
			.bg-c-withe{
				background: #fff;
			}
			.bg-c-blue{
				background-color: #34B8FC;
			}
			
			.pd-lr-10{
				padding: 0rem 1rem;
			}
			.ld{
				padding: 1rem 0.5rem 1rem 1.5rem; line-height: 3rem;font-size: 1.5rem;
			}
			
			.rd{
				padding: 1rem 0.5rem;
			}
			.rd input{
				line-height: 2rem;
				border: none;
				outline: medium;
				padding: 0.5rem;
				font-size: 1.5rem;
				width: 90%;
			}
			.tx-l-r{
				text-align: right;
			}
			
			
			.bd-t{border-top: 1px solid #EEEEEE;}
			.bd-b{border-bottom: 1px solid #EEEEEE;}

			.banner{
				height:8rem;
				background-image:url('./image/base.png');
			    -webkit-background-size: 100% 100%;
			    background-size: 100% 100%;
			    background-repeat: no-repeat;
			    background-position: center;
			}
			.hide{
				display: none;
			}
			.wid-b15{
				width:15%
			}
			.wid-b70{
				width:70%
			}
			.img-ios{
				
				height:2.2rem;
				background-image:url('./image/icon_ios.png');
			    -webkit-background-size: 2.0rem 2.2rem;
			    background-size: 2.0rem 2.2rem;
			    background-repeat: no-repeat;
			    background-position: center;
			}
			.img-and{
				height:2.2rem;
				background-image:url('./image/icon_android.png');
			    -webkit-background-size: 2.0rem 2.2rem;
			    background-size: 2.0rem 2.2rem;
			    background-repeat: no-repeat;
			    background-position: center;
			}
			.img-more{
				height:2.2rem;
				background-image:url('./image/pic_jt2.png');
    			-webkit-background-size: auto 100%;
    			background-size: auto 100%;
    			background-repeat: no-repeat;
    			background-position: left;
			}
			.content{
				line-height: 4rem; font-size: 1.3rem;padding-left:1.5rem;
			}
		</style>
	</head>

	<body class="bg-c-gray">
	   <div class="banner"></div>
		<div class="fn-s-13" style="padding: 1rem 1rem;">
			亲爱的<span class="fn-c-org" id="phone" ></span>用户,恭喜你注册成功!  马上下载<%=siteTitle%>APP<span class="fn-c-org">领取1000体验金</span>
		</div>
		<div class="ub ub-ac bd-b bd-t" style="height:4rem">
		    <%if (isIos) {%>
		      <div class="wid-b15 img-ios"></div>
		    <%} else {%>
		       <div class="wid-b15 img-and"></div>
		    <%}%>
			<div class="wid-b70 content" onclick="download()"><%=siteTitle%>APP下载</div>
			<div class="wid-b15 img-more"></div>
		</div>
	</body>
	
<script type="text/javascript">
var query = GetRequest();
var phone = query['phone'];
phone = phone.substring(0, 3) + "****" + phone.substring(7, phone.length);
document.getElementById('phone').innerHTML = phone;

// 获取请求参数
function GetRequest() {
	var url = location.search; //获取url中"?"符后的字串
	var theRequest = new Object();
	if (url.indexOf("?") != -1) {
		var str = url.substr(1);
		strs = str.split("&");
		for (var i = 0; i < strs.length; i++) {
			theRequest[strs[i].split("=")[0]] = (strs[i].split("=")[1]);
		}
	}
	return theRequest;
}

function download(){
	location.href = "/app/downloadApp.htm";
}
</script>
</html>