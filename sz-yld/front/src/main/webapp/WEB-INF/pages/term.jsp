<%@page import="com.dimeng.p2p.S50.entities.T5017" %>
<%@page import="com.dimeng.p2p.modules.base.front.service.TermManage" %>
<%@ page import="com.dimeng.p2p.common.enums.TermType" %>
<%
    TermType termType = TermType.parse(request.getParameter("id"));
    if (termType == null) {
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        return;
    }
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html dir="ltr" xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN">
<head>
    <title><%=termType.getName() %> <%configureProvider.format(out, SystemVariable.SITE_TITLE);%></title>
    <%@include file="/WEB-INF/include/meta.jsp" %>
</head>
<body style="background:#f4f9fc;">
<%
    TermManage termManage = serviceSession.getService(TermManage.class);
    T5017 term = termManage.get(termType);
%>
<div class="protocol tc">
    <div class="" style="text-align: center;">
        <h2><strong><%=termType.getName() %>
        </strong></h2>

        <p>最后更新时间：<%if (term != null) {%><%=DateParser.format(term.F04)%><%}%></p>
    </div>
    <div class="info_cont">
        <%if (term != null) {%>
        <%
            StringHelper.format(out, term.F02, fileStore);
        %>
        <%}%>
    </div>
</div>
</body>
</html>
