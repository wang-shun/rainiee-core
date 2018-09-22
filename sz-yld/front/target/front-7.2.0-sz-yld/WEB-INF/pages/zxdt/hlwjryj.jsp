<%@page import="com.dimeng.p2p.S50.entities.T5011" %>
<%@page import="com.dimeng.p2p.modules.base.front.service.ArticleManage" %>
<%@page import="com.dimeng.p2p.S50.enums.T5011_F02" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
	<% ArticleManage articleManage = serviceSession.getService(ArticleManage.class);%>
    <title><%=articleManage.getCategoryNameByCode("HLWJRYJ") %>  最新动态</title>

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
        <div class="news_list">
            <ul>
                <%
                    final int currentPage = IntegerParser.parse(request.getParameter("paging.current"));
                    PagingResult<T5011> results = articleManage.search(T5011_F02.HLWJRYJ, new Paging() {
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
                <li><span class="fr gray9"><%=Formater.formatDate(article.F12)%></span>
                    <a target="_blank"
                       href="<%=controller.getPagingItemURI(request, Hlwjryj.class,article.F01)%>">
							<span title="<%StringHelper.filterHTML(out, article.F06); %>">
								<%StringHelper.filterHTML(out, StringHelper.truncation(article.F06, 20)); %>
						</span>
                    </a></li>
                <%
                        }
                    }
                %>
            </ul>
            <%Hlwjryj.rendPaging(out, results, controller.getPagingURI(request, Hlwjryj.class)); %>
        </div>
    </div>
</div>
<%@include file="/WEB-INF/include/footer.jsp" %>
</body>
</html>