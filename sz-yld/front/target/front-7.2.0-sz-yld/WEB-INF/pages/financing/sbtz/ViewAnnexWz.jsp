<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.S62.entities.T6233"%>
<%@page import="com.dimeng.util.ObjectHelper" %>
<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
</head>
<body>
<%
    T6233 t6233 = ObjectHelper.convert(request.getAttribute("t6233"), T6233.class);
%>
<img src="<%=fileStore.getURL(t6233.F06)%>" alt="<%StringHelper.filterHTML(out,t6233.F04);%>"/>
</body>
</html>