<%@page import="com.dimeng.p2p.S50.entities.T5011" %>
<%@page import="com.dimeng.p2p.modules.base.front.service.ArticleManage" %>
<%@page import="com.dimeng.p2p.S50.enums.T5011_F02" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <% ArticleManage articleManage = serviceSession.getService(ArticleManage.class);%>
    <title><%=articleManage.getCategoryNameByCode("MTBD") %>  最新动态</title>
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
            CURRENT_SUB_CATEGORY = "MTBD";
        %>
        <%@include file="/WEB-INF/include/mtbd/menu.jsp" %>
    </div>
    <div class="about_content">
        <div class="safety_list">
            <ul>
                <%
                    final int currentPage = IntegerParser.parse(request
                            .getParameter("paging.current"));
                    PagingResult<T5011> results = articleManage.search(T5011_F02.MTBD, new Paging() {
                        public int getCurrentPage() {
                            return currentPage;
                        }

                        public int getSize() {
                            return 10;
                        }
                    });
                    T5011[] articles = results.getItems();
                    if (articles != null && articles.length > 0) {
                        for (T5011 article : articles) {
                %>
                <li>
                    <div class="pic">
                        <%if (!StringHelper.isEmpty(article.F09)) {%><img src="<%=fileStore.getURL(article.F09)%>"/>
                        <%} else {%><%}%>
                    </div>
                    <div class="name">
                        <a target="_blank" href="<%=controller.getPagingItemURI(request, Mtbd.class,article.F01)%>"><%
                            StringHelper.filterHTML(out, article.F06); %></a>
                    </div>
                    <div class="label"><%StringHelper.filterHTML(out, article.F07);%><span
                            class="date"><%=DateParser.format(article.F12)%></span></div>
                    <div class="introduce">
                        <%StringHelper.format(out, article.F08, fileStore);%>
                    </div>
                    <div class="details"><a target="_blank"
                                            href="<%=controller.getPagingItemURI(request, Mtbd.class,article.F01)%>">查看详情</a>
                    </div>
                </li>
                <%
                        }
                    }
                %>
            </ul>
            <%Mtbd.rendPaging(out, results, controller.getPagingURI(request, Mtbd.class)); %>
        </div>
    </div>
</div>
<%@include file="/WEB-INF/include/footer.jsp" %>
</body>
</html>