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
		<meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no, minimum-scale=1.0, maximum-scale=1.0">
		<title><%=configureProvider.format(SystemVariable.SITE_TITLE)%>APP下载页面</title>  
		<link rel="shortcut icon" href="favicon.ico" type="image/x-icon" /> 
	</head>

	<body>
		<div style="text-align: center;top: 15%;position: absolute;left: 10%;">
		<img src="image/noapp.png" style="width: 60%;">
		<p>抱歉，没有最新的包！</p>
		 </div>
	</body>
</html> 