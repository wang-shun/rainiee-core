
<%  String page_template = configureProvider.format(SystemVariable.PAGE_TEMPLATE);
    if("simple".equals(page_template)){
%>
<link rel="stylesheet" type="text/css" href="<%=controller.getStaticPath(request)%>/simple/css/common.css" />
<link rel="stylesheet" type="text/css" href="<%=controller.getStaticPath(request)%>/simple/css/user.css" />
<link rel="stylesheet" type="text/css" href="<%=controller.getStaticPath(request)%>/simple/css/base.css" />
<%
    }else{
%>
<link rel="stylesheet" type="text/css" href="<%=controller.getStaticPath(request)%>/css/common.css" />
<link rel="stylesheet" type="text/css" href="<%=controller.getStaticPath(request)%>/css/user.css" />
<link rel="stylesheet" type="text/css" href="<%=controller.getStaticPath(request)%>/css/base.css"/>
<%  } %>