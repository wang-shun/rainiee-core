<%@page import="com.dimeng.framework.resource.ResourceProvider"%>
<%@page import="com.dimeng.framework.config.ConfigureProvider"%>
<%@page import="com.dimeng.framework.resource.ResourceRegister"%>
<%@page import="com.dimeng.p2p.variables.defines.SystemVariable"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
ResourceProvider resourceProvider = ResourceRegister.getResourceProvider(application);
ConfigureProvider configureProvider = resourceProvider.getResource(ConfigureProvider.class);
%>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1, user-scalable=no, minimum-scale=1.0, maximum-scale=1.0">
	
	 <title><%=configureProvider.format(SystemVariable.SITE_TITLE)%>APP下载</title> 
<style>
@charset "utf-8";

* {
	font-family: "微软雅黑";
	vertical-align: middle;
}

body {
	font-size: 62.5%;
	color: #676767;
	margin: 0;
	padding: 0;
	border: 0;
}

div {
	margin: 0;
	padding: 0;
	border: 0;
}

div {
	font-size: 1.1em;
}

.ub {
	display: -webkit-box !important;
	display: box !important;
	position: relative;
}

.ub-f1 {
	position: relative;
	-webkit-box-flex: 1;
	box-flex: 1;
}

.back_color_f {
	background-color: #fff;
}

.back_color_c {
	background-color: #f1eff0;
}

.wimg0 {
	width: 3em;
	padding-right: .5em;
}

.wimg1 {
	width: 9em;
}

.fcolor_r {
	color: #ec5741;
}

.fcolor_3 {
	color: #333;
}

.fsize1 {
	font-size: 1.5em;
}

.fsize2 {
	font-size: 1em;
}

.padding0 {
	padding-bottom: 1em;
}

.padding1 {
	padding: 0.5em 1em;
}

.padding2 {
	padding: 3.5em 1em;
}

.ub-ac {
	-webkit-box-align: center;
	box-align: center;
}

.ub-pc {
	-webkit-box-pack: center;
	box-pack: center;
}
</style>
</head>
<body>
	<div class="back_color_f">
		<div class="padding1">
			<div class="ub ub-pc">
				<h2>下载中心</h2>
			</div>
			<div class="padding0">
				<img class="wimg1" src="image/wxts.png" />
			</div>
			<div class="fsize2">如果您通过微信打开此页面，请按以下提示操作：</div>
		</div>
		<div class="ub back_color_c padding1 ">
			<div class="ub-f1 ub fcolor_3 ub-ac">
				<img class="wimg0" src="image/ico_1.png" />点击页面右上角按钮
			</div>
			<div class="ub-f1">
				<img class="wimg1" src="image/download01.png" />
			</div>
		</div>
		<div class="ub padding1">
			<div class="ub-f1 fcolor_3 ub ub-ac">
				<img class="wimg0" src="image/ico_2.png" />选择在浏览器中打开
			</div>
			<div class="ub-f1">
				<img class="wimg1" src="image/download02.png" />
			</div>
		</div>
		<div class="back_color_c fcolor_3 padding2">
			<img class="wimg0" src="image/ico_3.png" />用其它浏览器打开并完成下载！
		</div>
	</div>
</body>
</html>