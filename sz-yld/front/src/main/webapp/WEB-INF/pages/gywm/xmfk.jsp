<%@page import="com.dimeng.p2p.S50.entities.T5011" %>
<%@page import="com.dimeng.p2p.modules.base.front.service.ArticleManage" %>
<%@page import="com.dimeng.p2p.S50.enums.T5011_F02" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
     <% ArticleManage articleManage = serviceSession.getService(ArticleManage.class);%>
    <title><%=articleManage.getCategoryNameByCode("XMFK") %> 安全保障</title>
    <%@include file="/WEB-INF/include/style.jsp" %>
</head>
<body>
<%@include file="/WEB-INF/include/header.jsp" %>
<div class="main_bg">
<div class="disclosure wrap">
	<%
         CURRENT_CATEGORY = "GYWM";
         CURRENT_SUB_CATEGORY = "XMFK";
         T5010_F04 categoryStatus = articleManage.getCategoryStatusByCode(CURRENT_SUB_CATEGORY);
         if(categoryStatus == T5010_F04.TY){
             controller.sendRedirect(request, response,controller.getViewURI(request, Zjaq.class));
         }
     %>
	<%@include file="/WEB-INF/include/xxpl/menu.jsp" %>
	<div class="disclosure_con content">
		<h2><span>·</span><%=articleManage.getCategoryNameByCode("XMFK") %></h2>
		<div class="bd">
			 <%
                T5011 article = articleManage.get(T5011_F02.XMFK);
                if (article != null) {
                    articleManage.view(article.F01);
                    StringHelper.format(out, articleManage.getContent(article.F01), fileStore);
                }
            %>
		</div>
	</div>
</div>
</div>
<%@include file="/WEB-INF/include/footer.jsp" %>
</body>
</html>