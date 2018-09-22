<%@page import="com.dimeng.p2p.modules.base.front.service.ArticleManage"%>
<%@page import="com.dimeng.p2p.S50.entities.T5013"%>
<%@page import="com.dimeng.p2p.modules.base.front.service.PartnerManage"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@include file="/WEB-INF/include/meta.jsp"%>
<%
    ArticleManage articleManage = serviceSession.getService(ArticleManage.class);
%>
<title><%=articleManage.getCategoryNameByCode("HZHB")%> 关于我们</title>
<%@include file="/WEB-INF/include/style.jsp"%>
</head>
<body>
	<%@include file="/WEB-INF/include/header.jsp"%>
	<div class="main_bg">
	<div class="disclosure wrap">
		<%
		    CURRENT_CATEGORY = "GYWM";
		    CURRENT_SUB_CATEGORY = "HZHB";
		    T5010_F04 categoryStatus = articleManage.getCategoryStatusByCode(CURRENT_SUB_CATEGORY);
            if(categoryStatus == T5010_F04.TY){
                controller.sendRedirect(request, response,controller.getViewURI(request, Zxns.class));
            }
		         
		%>
		<%@include file="/WEB-INF/include/xxpl/menu.jsp"%>
		<div class="disclosure_con about_partner">
			<h2>
				<span>·</span><%=articleManage.getCategoryNameByCode("HZHB")%></h2>
			<%
			    final int currentPage = IntegerParser.parse(request.getParameter("paging.current"));
			        PartnerManage  partnerManage = serviceSession.getService(PartnerManage.class);
			        PagingResult<T5013> results = partnerManage.getPagedList(new Paging() {
			            public int getCurrentPage() {
			                return currentPage;
			            }

			            public int getSize() {
			                return 10;
			            }
			        });
			        T5013[] articles = results.getItems();
			        if (articles != null && articles.length > 0) {
			            for (T5013 article : articles) {
			%>
			<dl>
				<dt>
					<%
					    StringHelper.filterHTML(out, article.F04);
					%><span><%=DateParser.format(article.F10)%></span>
				</dt>
				<dd>
					<a target="_blank"
						href="<%=controller.getPagingItemURI(request, Hzhb.class,article.F01 )%>">
						<img src="<%=fileStore.getURL(article.F06)%>"
						alt="<%StringHelper.filterHTML(out, article.F04);%>" />
					</a> <span id="connent" class="con">
						<%
						    if (article != null) StringHelper.format(out, StringHelper.truncation(article.F08, 50), fileStore);
						%>
					</span>
				</dd>
				<dd>
					链接地址：<a target="_blank"
						href="<%StringHelper.format(out, article.F05,fileStore);%>"
						class="mr100">
						<%
						    StringHelper.format(out, article.F05, fileStore);
						%>
					</a>
					<%
					    if (article != null) StringHelper.filterHTML(out, article.F07);
					%>
				</dd>
			</dl>
			<%
			    }
			            }
			%>
			<%
			    Hzhb.rendPaging(out, results, controller.getPagingURI(request, Hzhb.class));
			%>
		</div>
	</div>
	</div>
	<%@include file="/WEB-INF/include/footer.jsp"%>
	<script type="text/javascript">
		$(function() {
			var element = $("#connent");
			var temp = element.text().replace(/\n/g, '<br/>');
			element.html(temp);
		});
	</script>
</body>
</html>