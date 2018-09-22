<%@page import="com.dimeng.p2p.S50.entities.T5011" %>
<%@page import="com.dimeng.p2p.modules.base.front.service.ArticleManage" %>
<%@page import="com.dimeng.p2p.S50.enums.T5011_F02" %>
<%@page import="com.dimeng.framework.http.upload.FileStore" %>
<%
    ArticleManage articleManage = serviceSession.getService(ArticleManage.class);
    T5011 article = articleManage.get(IntegerParser.parse(request.getParameter("id")), T5011_F02.HLWJRYJ);
    if (article == null) {
        response.sendError(HttpServletResponse.SC_NOT_FOUND);
        return;
    }
    articleManage.view(article.F01);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html dir="ltr" xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN">
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <title>
        <%
            if (article != null)
                StringHelper.filterHTML(out, article.F06);
        %><%=articleManage.getCategoryNameByCode("HLWJRYJ") %>
        关于我们
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
            CURRENT_SUB_CATEGORY = "HLWJRYJ";
        %>
        <%@include file="/WEB-INF/include/mtbd/menu.jsp" %>
    </div>
    <div class="about_content">

        <div class="article_details_til">
            <div class="fr">
                <%
                    if (article != null) {
                        out.print(Formater.formatDate(article.F12));
                    }
                %>
            </div>
            <h1>
                <%
                    if (article != null)
                        StringHelper.filterHTML(out, article.F06);
                %>
            </h1>
        </div>
        <div class="article_details_con">
            <p>
                <%
                    if (article != null)
                        StringHelper.format(out,
                                articleManage.getContent(article.F01),
                                resourceProvider.getResource(FileStore.class));
                %>
            </p>

            <div class="tr mt20">
                来源：<%
                if (article != null)
                    StringHelper.filterHTML(out, article.F07);
            %>
            </div>
        </div>
    </div>
</div>
<%@include file="/WEB-INF/include/footer.jsp" %>
</body>
</html>
