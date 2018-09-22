<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.S50.entities.T5011" %>
<%@page import="com.dimeng.p2p.modules.base.front.service.ArticleManage" %>
<%@page import="com.dimeng.p2p.S50.enums.T5011_F02" %>
<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <title><%=ArticleType.MCJS.getName()%>
    </title>
    <%@include file="/WEB-INF/include/style.jsp" %>
</head>
<body>
<%@include file="/WEB-INF/include/header.jsp" %>
<div class="contain clearfix">
    <div class="lead">
        <%
            CURRENT_SUB_CATEGORY = "MCJS";
        %>
        <%@include file="/WEB-INF/include/xszy/left.jsp" %>
        <%
            ArticleManage articleManage = serviceSession.getService(ArticleManage.class);
            PagingResult<T5011> result = articleManage.search(T5011_F02.MCJS, new Paging() {
                public int getCurrentPage() {
                    return 1;
                }

                public int getSize() {
                    return 5;
                }
            });
            T5011[] articles = result.getItems();
        %>
        <%if (articles != null && articles.length > 0) {%>
        <%if (articles.length == 1) {%>
        <div class="fr clearfix container">
            <div class="pub_title clearfix"><%StringHelper.filterHTML(out, articles[0].F06);%></div>
            <div id="nav" class="clearfix image_style" style="word-break:break-all;">
                <%
                    StringHelper.format(out, articleManage.getContent(articles[0].F01), fileStore);
                    articleManage.view(articles[0].F01);
                %>
            </div>
        </div>
        <%} else {%>
        <div class="fr clearfix ">
            <div class="lead_tab clearfix ">
                <ul>
                    <%for (int i = 0, idx = 1, max = articles.length + 1; i < articles.length; i++, idx++) {%>
                    <li id="one<%=idx %>" onclick="setTab('one',<%=idx %>,<%=max %>)" <%if(i==0){%>class="hover"<%}%>><%
                        StringHelper.filterHTML(out, articles[i].F06); %></li>
                    <%}%>
                    <li style="border-left:1px solid #d1dfea;padding:0;"></li>
                </ul>
            </div>
            <%for (int i = 0; i < articles.length; i++) {%>
            <div class="container clearfix image_style" id="con_one_<%=i+1%>" <%if(i>0){%>style="display:none;"<%}%>>
                <%
                    StringHelper.format(out, articleManage.getContent(articles[i].F01), fileStore);
                    articleManage.view(articles[i].F01);
                %>
            </div>
            <%}%>
        </div>
        <%}%>
        <%} else {%>
        <div class="fr clearfix container">
            <div class="pub_title clearfix"></div>
            <div id="nav" class="clearfix">
            </div>
        </div>
        <%}%>
    </div>
</div>
<%@include file="/WEB-INF/include/footer.jsp" %>
</body>
</html>