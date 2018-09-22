<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="com.dimeng.p2p.common.Constant"%>
<%@page import="com.dimeng.p2p.modules.systematic.console.service.entity.SysUser"%>
<%@page import="com.dimeng.p2p.modules.systematic.console.service.SysUserManage"%>
<html dir="ltr" xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=9; IE=8;IE=EDGE"/> 
<title>7.0<%if("simple".equals(configureProvider.getProperty(SystemVariable.PAGE_TEMPLATE))){%>标准版<%}else{%>豪华版<%}%>后台</title>
</head>
<%
String indexUrl = configureProvider.getProperty(URLVariable.INDEX_URL);
SysUserManage sysUserM = serviceSession.getService(SysUserManage.class);
SysUser sysU = sysUserM.get(dimengSession.getAccountId());
%>
<frameset rows="132,*,50" cols="*" framespacing="0" frameborder="no" border="0"><!--top-->
  <frame src="header.html" name="topFrame" scrolling="no" noresize="noresize" id="topFrame" title="topFrame" />
  <!--center-->
  <frameset rows="*" cols="0,*" framespacing="0" frameborder="no" border="0" id="ContentFrame1" name="ContentFrame1" title="ContentFrame1">
  		
		<frame src="left.html" name="leftFrame"  scrolling="no" noresize="noresize" id="leftFrame" title="leftFrame" />
		<%
  		if(Constant.BUSINESS_ROLE_ID == sysU.roleId){
  		%>
		<frame src="/console/system/htzh/business/businessUserList.htm" name="mainFrame" id="mainFrame" title="mainFrame" />
		<%}else{ %>
		<frame src="<%=indexUrl %>" name="mainFrame" id="mainFrame" title="mainFrame" />
		<%} %>
	</frameset>
  <!--center-->
  <frame src="footer.html" name="bottomFrame" scrolling="No" noresize="noresize" id="bottomFrame" title="bottomFrame" />
 
</frameset>

<noframes>
<body>
</body></noframes>
</html>
