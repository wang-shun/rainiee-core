<%@page import="com.dimeng.p2p.S50.entities.T5011" %>
<%@page import="com.dimeng.p2p.modules.base.front.service.ArticleManage" %>
<%@page import="com.dimeng.p2p.S50.enums.T5011_F02" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <% ArticleManage articleManage = serviceSession.getService(ArticleManage.class);%>
    <title><%=articleManage.getCategoryNameByCode("MCJS") %>  投资攻略</title>
    <%@include file="/WEB-INF/include/style.jsp" %>
</head>
<body>

<%@include file="/WEB-INF/include/header.jsp" %>
<div class="about_white_bg">
    <div class="front_banner"
         style="background: url(<%=controller.getStaticPath(request)%>/images/about_banner.jpg) no-repeat center 0;"></div>
    <div class="about_tab">
        <%
            CURRENT_CATEGORY = "BZZX";
            CURRENT_SUB_CATEGORY = "MCJS";
        %>
        <%@include file="/WEB-INF/include/bzzx/xszyMenu.jsp" %>
    </div>
    <div class="about_content clearfix">
        <div class="info_cont ny_newslist">
            <%
                T5011 article = articleManage.get(T5011_F02.MCJS);
                if (article != null) {
                    articleManage.view(article.F01);
                    StringHelper.format(out, articleManage.getContent(article.F01), fileStore);
                }
            %>
        </div>
    </div>
</div>
<%@include file="/WEB-INF/include/footer.jsp" %>
</body>
</html>