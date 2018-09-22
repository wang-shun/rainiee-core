<%if("simple".equals(configureProvider.getProperty(SystemVariable.PAGE_TEMPLATE))){%>
<%@include file="/WEB-INF/include/index/simple/header.jsp" %>
<%}else{%>
<%@include file="/WEB-INF/include/index/header.jsp" %>
<%}%>