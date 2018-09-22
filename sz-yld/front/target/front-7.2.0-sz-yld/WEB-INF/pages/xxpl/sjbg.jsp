<%@page import="com.dimeng.p2p.front.servlets.xxpl.Qtxx"%>
<%@page import="com.dimeng.p2p.S50.entities.T5011"%>
<%@page import="com.dimeng.p2p.modules.base.front.service.ArticleManage"%>
<%@page import="com.dimeng.p2p.S50.enums.T5011_F02"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@include file="/WEB-INF/include/meta.jsp"%>
<%
    ArticleManage articleManage = serviceSession.getService(ArticleManage.class);
%>
<title><%=articleManage.getCategoryNameByCode("SJBG")%> 信息披露</title>
<%@include file="/WEB-INF/include/style.jsp"%>
</head>
<body>
	<%@include file="/WEB-INF/include/header.jsp"%>
	<div class="main_bg">
		<div class="disclosure wrap">
			<%
			    CURRENT_CATEGORY = "XXPL";
			    CURRENT_SUB_CATEGORY = "SJBG";
			    T5010_F04 categoryStatus = articleManage.getCategoryStatusByCode(CURRENT_SUB_CATEGORY);
		         if(categoryStatus == T5010_F04.TY){
		             controller.sendRedirect(request, response,controller.getViewURI(request, Yybg.class));
		         }
			%>
			<%@include file="/WEB-INF/include/xxpl/menu.jsp"%>
			<div class="disclosure_con report report_audit">
				<h2>
					<span>·</span><%=articleManage.getCategoryNameByCode("SJBG")%></h2>
				<div id="list_id" class="bd"></div>
			</div>
		</div>
	</div>
	<%@include file="/WEB-INF/include/footer.jsp"%>
	<script type="text/javascript">
		var _sjbgUrl = '<%=controller.getURI(request, Sjbg.class)%>';
		var url = '<%configureProvider.format(out, URLVariable.INDEX);%>';
		var currentPage = 1;
		var pageSize = 100;
		function initSjbgData() {
			$
					.ajax({
						type : "post",
						dataType : "html",
						url : _sjbgUrl,
						data : {
							currentPage : currentPage,
							pageSize : pageSize
						},
						success : function(data) {
							$("#list_id").html("");
							data = eval("(" + data + ")");

							if (data != null && data.sjbgs != null) {
								var liStr = "";
								var image = "";
								liStr += "<ul>";
								for (var i = 0; i < data.sjbgs.length; i++) {
									if (data.sjbgs[i].F09 == null) {
										image = "style='background-image:url("
												+ url
												+ "/images/report_bg1.jpg)";
									} else {
										image = "style='background-image:url("
												+ url + data.sjbgs[i].F09 + ")";
									}
									liStr += "<li class='bg4'><div class='item'><a title='" + data.sjbgs[i].F06 + "' href='"+url+data.sjbgs[i].path+"' "+image+"'>"
											+ "<p class='ellipsis'>"
											+ subStringLength(
													data.sjbgs[i].F06, 8, "...")
											+ "</p>"
											+ "<p class='f12'>"
											+ formatTime(data.sjbgs[i].F12)
											+ "</p></a>" + "</div></li>";
								}
								liStr += "</ul>";
								$("#list_id").html(liStr);
							} else {
								$("#list_id")
										.html(
												"<p class='pt100 pb100 f16 tc'>暂无数据</p>");
							}
						}
					});
		}

		$(".disclosure_nav")
				.css('height', $(".disclosure_con").height() + 'px');
		$(function() {
			initSjbgData();
		});

		var formatTime = function(time) {
			if (time == null) {
				return "--";
			}
			var date = new Date();
			date.setTime(time);
			var y = date.getFullYear();
			var m = date.getMonth() + 1;
			m = m < 10 ? '0' + m : m;
			var d = date.getDate();
			d = d < 10 ? ('0' + d) : d;
			return y + '-' + m + '-' + d;
		}
		function subStringLength(str, maxLength, replace) {
			if (isEmpty(str)) {
				return;
			}
			if (typeof (replace) == "undefined" || isEmpty(replace)) {
				replace = "...";
			}
			var rtnStr = "";
			var index = 0;
			var end = Math.min(str.length, maxLength);
			for (; index < end; ++index) {
				rtnStr = rtnStr + str.charAt(index);
			}
			if (str.length > maxLength) {
				rtnStr = rtnStr + replace;
			}
			return rtnStr;
		}
		function isEmpty(str) {
			if (str == null || str == "") {
				return true;
			} else {
				return false;
			}
		}
	</script>
</body>
</html>