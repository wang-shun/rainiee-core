<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.service.ContractManage"%>
<%@page import="com.dimeng.p2p.console.servlets.bid.htgl.agreement.BlzqzrAgreement"%>
<%@page import="com.dimeng.p2p.S61.enums.T6110_F06"%>
<%@page import="com.dimeng.p2p.common.entities.Dzxy"%>
<%@page import="com.dimeng.p2p.variables.defines.SystemVariable"%>
<%@page import="com.dimeng.util.Formater"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="com.dimeng.util.parser.TimestampParser"%>
<%@page import="com.dimeng.framework.config.Envionment"%>
<%@page import="com.dimeng.util.parser.IntegerParser"%>
<%@page import="java.io.IOException" %>
<%@page import="freemarker.template.TemplateException" %>
<%@page import="freemarker.template.Template" %>
<%@page import="freemarker.template.Configuration" %>
<%@page import="java.util.HashMap" %>
<%@page import="java.util.Map" %>
<%@page import="com.dimeng.p2p.S62.enums.T6231_F21" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html;charset=UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no, minimum-scale=1.0, maximum-scale=1.0"/>
    <meta content="email=no"  name="format-detection" />
    <meta content="telephone=no" name="format-detection" />
    <link rel="stylesheet" type="text/css" href="<%=controller.getStaticPath(request)%>/css/other.css"/>
    <title>协议_不良债权转让</title>
</head>
<body>
<%
    int zqzrjlId = IntegerParser.parse(request.getParameter("id"));
	int userId = IntegerParser.parse(request.getParameter("userId"));
	int zqId = IntegerParser.parse(request.getParameter("zqId"));
    int s = IntegerParser.parse(request.getParameter("s"));
%>
<%if (s != 1) { %>
<p class="clearfix pt10"><a target="_blank" href="<%=controller.getURI(request, BlzqzrAgreement.class)%>?id=<%=zqzrjlId%>&zqId=<%=zqId %>&userId=<%=userId %>&s=1"
                            class="btn btn01 fr">点击下载</a></p>
<%} %>
<%
	ContractManage manage = serviceSession.getService(ContractManage.class);
    Dzxy dzxy = manage.getBlzqzr(zqzrjlId,0,"console");
    if (dzxy == null) {
        response.sendError(HttpServletResponse.SC_NOT_FOUND);
    }
    
    Map<String, Object> valueMap = null;
    if(userId > 0){
	    if(zqId > 0){
	    	valueMap = manage.getBadClaimContentMap(zqzrjlId,zqId,userId,"ZCR","front");
	    }else{
	        valueMap = manage.getBadClaimContentMap(zqzrjlId,zqId,userId,"SRR","front");
	    }
    }else{
        valueMap = manage.getBadClaimContentMap(zqzrjlId,0,0,"","console");
    }

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