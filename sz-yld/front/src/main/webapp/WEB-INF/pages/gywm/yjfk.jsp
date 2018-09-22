<%@ page import="com.dimeng.p2p.account.user.service.TzjyManage"%>
<%@ page import="com.dimeng.p2p.S61.entities.T6195_EXT"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@include file="/WEB-INF/include/meta.jsp"%>
<%
    TzjyManage manage = serviceSession.getService(TzjyManage.class);
%>
<title>关于我们-意见反馈</title>
<%@include file="/WEB-INF/include/style.jsp"%>
</head>
<body>
	<%@include file="/WEB-INF/include/header.jsp"%>
	<div class="main_bg">
		<div class="disclosure wrap">
			<%
			    CURRENT_CATEGORY = "GYWM";
			        CURRENT_SUB_CATEGORY = "YJFK";
			        final int currentPage = IntegerParser.parse(request.getParameter("paging.current"));
			        PagingResult<T6195_EXT> t6195_EXTs = manage.search(0, new Paging()
			        {
			            
			            @Override
			            public int getCurrentPage()
			            {
			                return currentPage;
			            }
			            
			            @Override
			            public int getSize()
			            {
			                return 5;
			            }
			        });
			%>
			<%@include file="/WEB-INF/include/xxpl/menu.jsp"%>
			<div class="disclosure_con about_suggest">
				<h2>
					<span>·</span>意见反馈
				</h2>
				<%
				    for (T6195_EXT t6195_ext : t6195_EXTs.getItems())
				        {
				            String userName =
				                t6195_ext.userName.substring(0, 2)
				                    + "***"
				                    + t6195_ext.userName.substring(t6195_ext.userName.length() - 2, t6195_ext.userName.length());
				%>
				<dl>
					<dt><%=userName%>：<%=t6195_ext.F03%><span
							class="gray9 ml10 f14"><%=Formater.formatDate(t6195_ext.F04)%></span>
					</dt>
					<dd>
						<i></i><%=configureProvider.format(SystemVariable.SITE_NAME)%>：<%=t6195_ext.F05%></dd>
				</dl>
				<%
				    }
				%>
				<%
				    Yjfk.rendPaging(out, t6195_EXTs, controller.getPagingURI(request, Yjfk.class));
				%>
			</div>
		</div>
	</div>
	<%@include file="/WEB-INF/include/footer.jsp"%>
</body>
</html>