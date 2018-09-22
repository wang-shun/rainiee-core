<%
    if("simple".equals(configureProvider.getProperty(SystemVariable.PAGE_TEMPLATE))){%>
<link rel="stylesheet" type="text/css" href="<%=controller.getStaticPath(request)%>/simple/css/common.css" />
<link rel="stylesheet" type="text/css" href="<%=controller.getStaticPath(request)%>/simple/css/front.css" />
<link rel="stylesheet" type="text/css" href="<%=controller.getStaticPath(request)%>/simple/css/base.css" />
<link rel="stylesheet" type="text/css" href="<%=controller.getStaticPath(request)%>/simple/css/disclosure.css" />
<%}else{%>
<link rel="stylesheet" type="text/css" href="<%=controller.getStaticPath(request)%>/css/common.css" />
<link rel="stylesheet" type="text/css" href="<%=controller.getStaticPath(request)%>/css/front.css" />
<link rel="stylesheet" type="text/css" href="<%=controller.getStaticPath(request)%>/css/base.css"/>
<link rel="stylesheet" type="text/css" href="<%=controller.getStaticPath(request)%>/css/disclosure.css" />
<%}%>
