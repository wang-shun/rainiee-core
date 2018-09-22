<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.console.servlets.bid.htgl.agreement.Dfxy"%>
<%@page import="com.dimeng.p2p.modules.bid.console.service.entity.DzxyDf"%>
<%@page import="com.dimeng.p2p.S61.enums.T6110_F06" %>
<%@page import="com.dimeng.p2p.S62.enums.T6231_F21" %>
<%@page import="com.dimeng.p2p.service.ContractManage" %>
<%@page import="com.dimeng.p2p.modules.bid.console.service.DzxyManage" %>
<%@page import="com.dimeng.p2p.modules.bid.console.service.entity.Dzxy" %>
<%@page import="com.dimeng.p2p.modules.bid.console.service.entity.DzxyDy" %>
<%@page import="freemarker.template.Configuration" %>
<%@page import="freemarker.template.Template" %>
<%@page import="freemarker.template.TemplateException" %>
<%@page import="java.io.IOException" %>
<%@page import="java.util.Calendar" %>
<%@page import="java.util.HashMap" %>
<%@page import="java.util.Map" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html;charset=UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no, minimum-scale=1.0, maximum-scale=1.0"/>
    <meta content="email=no"  name="format-detection" />
    <meta content="telephone=no" name="format-detection" />
    <link rel="stylesheet" type="text/css" href="<%=controller.getStaticPath(request)%>/css/other.css"/>
    <title>协议_垫付</title>
</head>
<body>
<%
    //垫付记录ID T6230.F01
    int bId = IntegerParser.parse(request.getParameter("bid"));
	//借款人id
	int dfuId = IntegerParser.parse(request.getParameter("dfuid"));
    int s = IntegerParser.parse(request.getParameter("s"));
    ContractManage contractMng = serviceSession.getService(ContractManage.class);
    Map<String, Object> valueMap = contractMng.getAdvanceContentMap(bId, dfuId);
    if (null == valueMap) {
        response.sendError(HttpServletResponse.SC_NOT_FOUND);
    }
    
    //垫付协议模板内容
    String dzxy_content = (String)valueMap.get("dzxy_content");
    String dzxy_xymc = (String)valueMap.get("dzxy_xymc");
%>
<%
    if (s != 1) {
%>
<p class="clearfix pt10"><a target="_blank" href="<%=controller.getURI(request, Dfxy.class)%>?bid=<%=bId%>&s=1"
                            class="btn btn01 fr">点击下载</a></p>
<%} %>
<%
    Configuration cfg = new Configuration();
    Template template = new Template(dzxy_xymc, dzxy_content, cfg);
    try {
        template.process(valueMap, out);
    } catch (TemplateException e) {
        throw new IOException(e);
    }
%>
</body>
</html>