<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.user.servlets.financing.agreement.Gyb" %>
<%@page import="java.io.IOException" %>
<%@page import="freemarker.template.TemplateException" %>
<%@page import="freemarker.template.Template" %>
<%@page import="freemarker.template.Configuration" %>
<%@page import="java.util.Map" %>
<%@page import="java.util.HashMap" %>
<%@ page import="com.dimeng.p2p.repeater.donation.GyLoanManage" %>
<%@ page import="com.dimeng.p2p.common.entities.Dzxy" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html;charset=UTF-8"></meta>
    <link rel="stylesheet" type="text/css" href="<%=controller.getStaticPath(request)%>/css/other.css"/>
    <title>协议_公益标</title>
</head>
<body>
<%
    //公标益ID
    int creditId = IntegerParser.parse(request.getParameter("id"));
    int s = IntegerParser.parse(request.getParameter("s"));
    GyLoanManage manage = serviceSession.getService(GyLoanManage.class);
//公益标协议模板内容
    Dzxy dzxy = manage.getBidContent(creditId);
%>
<%if (s != 1 && dzxy != null) { %>
<p class="clearfix pt10"><a target="_blank" href="<%=controller.getURI(request, Gyb.class)%>?id=<%=creditId%>&s=1"
                            class="btn btn01 fr">点击下载</a></p>
<%}%>
<% if (dzxy == null) {
    //response.sendError(HttpServletResponse.SC_NOT_FOUND);
%>
<div style="text-align: center;margin-left: auto;margin-right: auto;"><p>该公益标未找到协议内容,请联系客服!</p></div>
<%
    } else {
        //标的进展信息
        final Envionment envionment = configureProvider.createEnvionment();

        Map<String, Object> valueMap = new HashMap<String, Object>() {

            private static final long serialVersionUID = 1L;

            @Override
            public Object get(Object key) {
                Object object = super.get(key);
                if (object == null) {
                    return envionment == null ? null : envionment.get(key.toString());
                }
                return object;
            }
        };
        valueMap.put("site_name", configureProvider.format(SystemVariable.SITE_NAME));
        valueMap.put("site_domain", configureProvider.format(SystemVariable.SITE_DOMAIN));

        Configuration cfg = new Configuration();
        Template template = new Template(dzxy.xymc, dzxy.content, cfg);
        try {
            template.process(valueMap, out);
        } catch (TemplateException e) {
            throw new IOException(e);
        }
    }
%>

</body>
</html> 