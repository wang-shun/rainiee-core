<%@page import="com.dimeng.p2p.S50.entities.T5011" %>
<%@page import="com.dimeng.p2p.modules.base.front.service.ArticleManage" %>
<%@page import="com.dimeng.p2p.S50.enums.T5011_F02" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
	<% ArticleManage articleManage = serviceSession.getService(ArticleManage.class);%>
    <title><%=articleManage.getCategoryNameByCode("GLTD") %> 关于我们</title>
    <%@include file="/WEB-INF/include/style.jsp" %>
</head>
<body>
<%@include file="/WEB-INF/include/header.jsp" %>
<div class="main_bg">
<div class="disclosure wrap">
	<%
         CURRENT_CATEGORY = "GYWM";
         CURRENT_SUB_CATEGORY = "GLTD";
         T5010_F04 categoryStatus = articleManage.getCategoryStatusByCode(CURRENT_SUB_CATEGORY);
         if(categoryStatus == T5010_F04.TY){
             controller.sendRedirect(request, response,controller.getViewURI(request, Zjgw.class));
         }
     %>
	<%@include file="/WEB-INF/include/xxpl/menu.jsp" %>
	<div class="disclosure_con about_team">
		<h2><span>·</span><%=articleManage.getCategoryNameByCode("GLTD") %></h2>

		<div class="bd">
		 <%
                	 final int currentPage = IntegerParser.parse(request.getParameter("paging.current"));
                     PagingResult<T5011> results = articleManage.search(
                             T5011_F02.GLTD, new Paging() {
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
			<dl>
				<dt><%if (!StringHelper.isEmpty(article.F09)) {%><img src="<%=fileStore.getURL(article.F09)%>"/><%}%></dt>
				<dd class="til"><%StringHelper.filterHTML(out, article.F06);%><span></span></dd>
				<dd><%StringHelper.format(out, articleManage.getContent(article.F01), fileStore);%></dd>
            </dl>
            <%}} %>
		</div>

		<%Gltd.rendPaging(out, results, controller.getPagingURI(request, Gltd.class)); %>
	</div>
</div>
</div>
<%@include file="/WEB-INF/include/footer.jsp" %>
<script type="text/javascript">
    $(function () {
        var element = $("#connent");
        var temp = element.text().replace(/\n/g, '<br/>');
        element.html(temp);
    });
</script>
</body>
</html>