<%@page import="com.dimeng.p2p.S50.entities.T5011" %>
<%@page import="com.dimeng.p2p.modules.base.front.service.ArticleManage" %>
<%@page import="com.dimeng.p2p.S50.enums.T5011_F02" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
	<% ArticleManage articleManage = serviceSession.getService(ArticleManage.class);%>
    <title><%=articleManage.getCategoryNameByCode("XSZY") %>  投资攻略</title>
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
            CURRENT_SUB_CATEGORY = "XSZY";
        %>
        <%@include file="/WEB-INF/include/bzzx/xszyMenu.jsp" %>
    </div>
    <div class="about_content clearfix">
        <div class="online_info">
            <div class="list">
                <ul>
                    <%
                        final int currentPage = IntegerParser.parse(request.getParameter("paging.current"));
                        PagingResult<T5011> results = articleManage.search(
                                T5011_F02.XSZY, new Paging() {
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
                    <li><a target="_blank"
                           href="<%=controller.getPagingItemURI(request, Xszy.class,article.F01 )%>"><span
                            class="lbt"> <%StringHelper.filterHTML(out, article.F06); %>
							</span><span class="ldt"><%=DateParser.format(article.F12) %></span></a></li>
                    <%
                            }
                        }
                    %>
                </ul>
                <%Xszy.rendPaging(out, results, controller.getPagingURI(request, Xszy.class)); %>
            </div>
        </div>
    </div>
</div>
<%@include file="/WEB-INF/include/footer.jsp" %>
</body>
</html>