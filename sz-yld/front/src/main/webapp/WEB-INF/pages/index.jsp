<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="com.dimeng.p2p.front.servlets.UpdatePassword" %>
<html dir="ltr" xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN">
<%
    String pageTemp = configureProvider.getProperty(SystemVariable.PAGE_TEMPLATE);
%>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <meta name="renderer" content="ie-comp" />  
    <meta property="qc:admins" content="36215751376367246375"/>
    <meta property="wb:webmaster" content="e38aeae607353b18"/>
    <title>
        <%configureProvider.format(out, SystemVariable.SITE_NAME);%>
    </title>
    <%
        if(StringHelper.isEmpty(pageTemp) || "standard".equals(pageTemp)){
    %>
    <link rel="stylesheet" type="text/css" href="<%=controller.getStaticPath(request)%>/css/common.css"/>
    <link rel="stylesheet" type="text/css" href="<%=controller.getStaticPath(request)%>/css/index.css"/>
    <link rel="stylesheet" type="text/css" href="<%=controller.getStaticPath(request)%>/css/base.css"/>
	<link rel="stylesheet" type="text/css" href="<%=controller.getStaticPath(request)%>/css/user.css" />
    <%}else{%>
    <link rel="stylesheet" type="text/css" href="<%=controller.getStaticPath(request)%>/simple/css/common.css"/>
    <link rel="stylesheet" type="text/css" href="<%=controller.getStaticPath(request)%>/simple/css/index.css"/>
    <link rel="stylesheet" type="text/css" href="<%=controller.getStaticPath(request)%>/simple/css/base.css"/>
    <%}%>
    <link rel="stylesheet" type="text/css" href="<%=controller.getStaticPath(request)%>/css/jplayer.blue.monday.min.css"/>
    <script src="http://tjs.sjs.sinajs.cn/open/api/js/wb.js?appkey=<%configureProvider.format(out,SystemVariable.INDEX_SINA_KEY);%>" type="text/javascript" charset="utf-8"></script>
</head>
<body>
<!--浮层-->
<form action="<%configureProvider.format(out,URLVariable.INDEX); %>/user/login.htm" method="post"
      id="loginForm" name="loginForm" target="_self">
    <input type="hidden" id="openId" name="openId" value=""/>
    <input type="hidden" id="accessToken" name="accessToken" value=""/>
    <input type="hidden" id="_z" name="_z" value="<%configureProvider.format(out,URLVariable.INDEX); %><%=controller.getStaticPath(request)%>/index.html"/>
</form>
<!--浮层-->

<%@include file="/WEB-INF/include/header.jsp" %>
<%if(pageTemp.equals("simple")){%>
<!--浮层-->
<%--<%@include file="/WEB-INF/include/index/simple/header.jsp" %>--%>
<%@include file="/WEB-INF/include/index/simple/advertisement.jsp" %>
<%@include file="/WEB-INF/include/index/simple/notice.jsp" %>
<%@include file="/WEB-INF/include/index/simple/feture.jsp" %>
<div class="pro_wrap">
	<div class="wrap clearfix">
   <%@include file="/WEB-INF/include/index/simple/financing.jsp" %>
   <%@include file="/WEB-INF/include/index/simple/invest.jsp" %>
   </div>
</div>
<%@include file="/WEB-INF/include/index/simple/info.jsp" %>
<%@include file="/WEB-INF/include/index/simple/partener.jsp" %>
  
<%@include file="/WEB-INF/include/index/simple/friendlyLink.jsp" %>
<%--<%@include file="/WEB-INF/include/index/simple/footer.jsp"%>--%>
<%}else{%>
<!--浮层-->
<%--<%@include file="/WEB-INF/include/header.jsp" %>--%>
<%@include file="/WEB-INF/include/index/advertisement.jsp" %>
<%@include file="/WEB-INF/include/index/notice.jsp" %>
<%@include file="/WEB-INF/include/index/feture.jsp" %>
<div class="item_bg">
    <div class="item_list w1002" id="investData">
        <%@include file="/WEB-INF/include/index/financing.jsp" %>
        <%@include file="/WEB-INF/include/index/invest.jsp" %>
    </div>
</div>
<%@include file="/WEB-INF/include/index/info.jsp" %>
<%@include file="/WEB-INF/include/index/partener.jsp" %>
<%@include file="/WEB-INF/include/index/friendlyLink.jsp" %>
<%--<%@include file="/WEB-INF/include/footer.jsp" %>--%>
<%}%>
<%@include file="/WEB-INF/include/footer.jsp" %>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/validation.js"></script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/percentage.js"></script>
<%if("simple".equals(pageTemp)){%>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/simple/js/index.js"></script>
<%}else{%>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/index.js"></script>
<%}%>
</body>
</html>