<%@page import="com.dimeng.p2p.S50.entities.T5015" %>
<%@page import="com.dimeng.p2p.modules.base.front.service.NoticeManage" %>
<%
    NoticeManage noticeManage = serviceSession.getService(NoticeManage.class);
    T5015 notice = noticeManage.get(IntegerParser.parse(request.getParameter("id")));
    if (notice == null) {
        response.sendError(HttpServletResponse.SC_NOT_FOUND);
        return;
    }
    noticeManage.view(notice.F01);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html dir="ltr" xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN">
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <title>
        <%if (notice != null) StringHelper.filterHTML(out, notice.F05);%> 网站公告 关于我们
    </title>
    <%@include file="/WEB-INF/include/style.jsp" %>
</head>
<body>

<%@include file="/WEB-INF/include/header.jsp" %>
<div class="about_white_bg">
    <div class="front_banner"
         style="background:url(<%=controller.getStaticPath(request)%>/images/about_banner.jpg) no-repeat center 0;"></div>
    <div class="about_tab">
        <%
            CURRENT_CATEGORY = "ZXDT";
            CURRENT_SUB_CATEGORY = "WZGG";
        %>
        <%--<%@include file="/WEB-INF/include/mtbd/menu.jsp" %>--%>
    </div>
    <div class="about_content">
        <div class="article_details_til">
            <div class="fr">
                <%=notice == null ? "" : DateParser.format(notice.F09)%>
            </div>
            <h1>
                <%if (notice != null) StringHelper.filterHTML(out, notice.F05);%>
            </h1>
        </div>
        <div class="article_details_con">
            <p>
                <%if (notice != null) StringHelper.format(out, notice.F06, fileStore); %>
            </p>
        </div>
    </div>
</div>
<%@include file="/WEB-INF/include/footer.jsp" %>
</body>
</html>
