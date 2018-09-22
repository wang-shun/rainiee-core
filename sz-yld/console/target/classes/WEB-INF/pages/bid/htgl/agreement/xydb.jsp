<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.service.ContractManage"%>
<%@page import="com.dimeng.p2p.common.entities.Dzxy"%>
<%@page import="com.dimeng.p2p.S61.enums.T6110_F06" %>
<%@page import="com.dimeng.p2p.console.servlets.bid.htgl.agreement.Xydb" %>
<%@page import="java.io.IOException" %>
<%@page import="freemarker.template.TemplateException" %>
<%@page import="freemarker.template.Configuration" %>
<%@page import="java.util.Calendar" %>
<%@page import="java.util.HashMap" %>
<%@page import="java.util.Map" %>
<%@page import="freemarker.template.Template" %>
<%@page import="com.dimeng.p2p.S62.enums.T6231_F21" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html;charset=UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no, minimum-scale=1.0, maximum-scale=1.0"/>
    <meta content="email=no"  name="format-detection" />
    <meta content="telephone=no" name="format-detection" />
    <link rel="stylesheet" type="text/css" href="<%=controller.getStaticPath(request)%>/css/other.css"/>
    <title>协议_抵押担保</title>
</head>
<body>
<%
    int creditId = IntegerParser.parse(request.getParameter("id"));
%>
<p class="clearfix pt10"><a target="_blank" href="<%=controller.getURI(request, Xydb.class)%>?id=<%=creditId%>&s=1"
                            class="btn btn01 fr">点击下载</a></p>
<%
	ContractManage contractManage = serviceSession.getService(ContractManage.class);
    Dzxy dzxy = contractManage.getBidContent(creditId);
    if (dzxy == null) {
        response.sendError(HttpServletResponse.SC_NOT_FOUND);
    }
    Map<String, Object> valueMap = contractManage.getValueMap(creditId,0);
    Configuration cfg = new Configuration();
    Template template = new Template(dzxy.xymc, dzxy.content, cfg);
    try {

        template.process(valueMap, out);
    } catch (TemplateException e) {
        throw new IOException(e);
    }
%>
</body>
</html>