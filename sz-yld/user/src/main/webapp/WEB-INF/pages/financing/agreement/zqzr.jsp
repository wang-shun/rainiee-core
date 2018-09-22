<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.user.servlets.financing.agreement.Zqzr" %>
<%@page import="java.io.IOException" %>
<%@page import="freemarker.template.TemplateException" %>
<%@page import="freemarker.template.Template" %>
<%@page import="freemarker.template.Configuration" %>
<%@page import="java.util.HashMap" %>
<%@page import="java.util.Map" %>

<%@page import="com.dimeng.p2p.service.ContractManage" %>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html;charset=UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no, minimum-scale=1.0, maximum-scale=1.0"/>
    <meta content="email=no"  name="format-detection" />
    <meta content="telephone=no" name="format-detection" />
    <link rel="stylesheet" type="text/css" href="<%=controller.getStaticPath(request)%>/css/other.css"/>
    <title>协议_债权转让</title>
</head>
<body>
<%
    int zqzrjlId = IntegerParser.parse(request.getParameter("id"));
    int s = IntegerParser.parse(request.getParameter("s"));
%>
<%if (s != 1) { %>
<p class="clearfix pt10"><a target="_blank" href="<%=controller.getURI(request, Zqzr.class)%>?id=<%=zqzrjlId%>&s=1"
                            class="btn btn01 fr">点击下载</a></p>
<%} %>
<%
    ContractManage manage = serviceSession.getService(ContractManage.class);
    Map<String, Object> valueMap = manage.getClaimContentMap(zqzrjlId, serviceSession.getSession().getAccountId());
    if (null == valueMap)
    {
        response.sendError(HttpServletResponse.SC_NOT_FOUND);
    }
    Configuration cfg = new Configuration();
    Template template = new Template((String)valueMap.get("dzxy_xymc"), (String)valueMap.get("dzxy_content"), cfg);
    try {
        template.process(valueMap, out);
    } catch (TemplateException e) {
        throw new IOException(e);
    }
%>
</body>
</html>