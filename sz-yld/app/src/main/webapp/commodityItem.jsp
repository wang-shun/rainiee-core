<%@page import="com.dimeng.framework.resource.ResourceProvider"%>
<%@page import="com.dimeng.framework.config.ConfigureProvider"%>
<%@page import="com.dimeng.framework.resource.ResourceRegister"%>
<%@page import="com.dimeng.p2p.variables.defines.SystemVariable"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
final ResourceProvider resourceProvider = ResourceRegister.getResourceProvider(application);
final ConfigureProvider configureProvider = resourceProvider.getResource(ConfigureProvider.class);
final String protocolType = configureProvider.format(SystemVariable.SITE_REQUEST_PROTOCOL);
String url = configureProvider.format(SystemVariable.SITE_DOMAIN);

if (url.indexOf(protocolType) == -1)
{
	url = protocolType.concat(url);
}

%>
<html>
	<head>
		<meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no, minimum-scale=1.0, maximum-scale=1.0">
		<meta content="email=no"  name="format-detection" />
        <meta content="telephone=no" name="format-detection" />
	<style type="text/css">
	@charset "utf-8";
*{margin:0;padding:0;}
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
.fn-c-black{
	#666;
}
.xm{
	padding:1rem 1rem 1rem 1.5rem; font-size: 1.5rem;background-color: #e9e9e9;
	margin-top:1rem;
	color:#666666;
}

.ld{
	padding:0.5rem 0.5rem 0.5rem 1.5rem; font-size: 1.3rem;
	word-break: break-word;
	text-align:justify;
	color: #999999;
}
.rd{
	padding:0.5rem 1rem 0.5rem 0.5rem; font-size: 1.2rem;
	word-break: break-word;
	text-align:justify;
	color: #999999;
}
.dy{
	padding:0.5rem 1rem 0.5rem 1.5rem; font-size: 1.2rem;
	word-break: break-word;
	text-align:justify;
	min-height: 5rem;
}
.bd-b{
	border-bottom: 1px solid #e4e0dd;
}
.pd-b-2{padding-bottom: 2rem;}
.udis{
	display: none;
}
	</style>
	</head>
	<body class="bg-c-gray" >
		<div class="bg-c-withe bd-b pd-b-2">
		    <div class="xm">商品详情</div>
			<div class="ub ">
				<div class="rd ub-f1" id="commodityItem"></div>
			</div>
		</div>
	</body>
	<script type="text/javascript">
	
	var query =GetRequest();
	
	var obj={
		"commodityItem":document.getElementById("commodityItem")
	}
			var sc=document.createElement('script');
			sc.src=  '<%=url%>'+'/app/user/mall/buyRecord.htm?callback=callback&id='+query['id']+'&type='+query['type'];
			document.body.appendChild(sc);
			function callback(response){
				console.log(response);
				if(!!response&&response.data&&response.code=="000000"){
					var data=response.data;
					obj['commodityItem'].innerHTML=data.commodityItem;
				}
			}
	function GetRequest() {
	     var url = location.search; //获取url中"?"符后的字串
	     var theRequest = new Object();
	     if (url.indexOf("?") != -1) {
			  var str = url.substr(1);
			  strs = str.split("&");
			  for(var i = 0; i < strs.length; i ++) {
			     theRequest[strs[i].split("=")[0]]=(strs[i].split("=")[1]);
		       }
	     }
	   return theRequest;
	}
	</script>
	
</html>
