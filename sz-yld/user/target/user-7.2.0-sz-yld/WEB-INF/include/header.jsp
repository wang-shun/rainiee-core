<%
	boolean is_mall = Boolean.parseBoolean(configureProvider.getProperty(MallVariavle.IS_MALL));
%>
<%if("simple".equals(configureProvider.getProperty(SystemVariable.PAGE_TEMPLATE))){%>
<%@include file="/WEB-INF/include/simple/header.jsp" %>
<%}else{%>
<%@include file="/WEB-INF/include/standard/header.jsp" %>
<%}%>