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
<title><%=articleManage.getCategoryNameByCode("YYBG")%> 信息披露</title>
<%@include file="/WEB-INF/include/style.jsp"%>
</head>
<body>
	<%@include file="/WEB-INF/include/header.jsp"%>
	<div class="main_bg">
		<div class="disclosure wrap">
			<%
			    CURRENT_CATEGORY = "XXPL";
			    CURRENT_SUB_CATEGORY = "YYBG";
			    T5010_F04 categoryStatus = articleManage.getCategoryStatusByCode(CURRENT_SUB_CATEGORY);
		         if(categoryStatus == T5010_F04.TY){
		             controller.sendRedirect(request, response,controller.getViewURI(request, Sfbz.class));
		         }
			%>
			<%@include file="/WEB-INF/include/xxpl/menu.jsp"%>
			<div class="disclosure_con report">
				<h2>
					<span>·</span><%=articleManage.getCategoryNameByCode("YYBG")%></h2>
				<div id="year_id" class="hd"></div>
				<div id="list_id" class="bd"></div>
			</div>
		</div>
	</div>
	<%@include file="/WEB-INF/include/footer.jsp"%>
	<script type="text/javascript">
	var _yybgUrl = '<%=controller.getURI(request, Yybg.class)%>';
	var url = '<%configureProvider.format(out, URLVariable.INDEX);%>';
		var currentPage = 1;
		var pageSize = 9;

		//获取年份
		function initYearData() {
			$
					.ajax({
						type : "post",
						dataType : "html",
						url : _yybgUrl,
						data : {
							type : "year"
						},
						success : function(data) {
							$("#year_id").html("");
							data = eval("(" + data + ")");
							if (data != null && data.years != null) {
								$("#year_id").show();
								var years = "";
								years += "<ul>";
								for (var i = 0; i < data.years.length; i++) {
									if (i == 0) {
										years += "<li class='on' onclick='initYybgData(this ,"
												+ data.years[i]
												+ ")'>"
												+ data.years[i] + "</li>";
									} else {
										years += "<li onclick='initYybgData(this ,"
												+ data.years[i]
												+ ")'>"
												+ data.years[i] + "</li>";
									}
								}
								initYybgData("",data.years[0]);
								years += "</ul>";
								$("#year_id").html(years)
							} else {
								$("#list_id")
										.html(
												"<p class='pt100 pb100 f16 tc'>暂无数据</p>");
								$("#year_id").hide();
							}
						}
					});
			
			
		}

		function initYybgData(obj, year) {
			$(obj).siblings("li").each(function() {
				$(this).removeClass("on");
			});
			$(obj).addClass("on");
			$("#list_id").find("li").html();
			$
					.ajax({
						type : "post",
						dataType : "html",
						url : _yybgUrl,
						data : {
							type : "list",
							year : year
						},
						success : function(data) {
							$("#list_id").html("");
							data = eval("(" + data + ")");
							if (data != null && data.yybgs != null) {
								var liStr = "";
								var image = "";
								liStr += "<ul>";
								for (var i = 0; i < data.yybgs.length; i++) {
									if (data.yybgs[i].F09 == null) {
										image = " style='background-image:url("
												+ url
												+ "/images/report_bg1.jpg)";
									} else {
										image = " style='background-image:url("
												+ url + data.yybgs[i].F09 + ")";
									}
									liStr += "<li class='bg4'><div class='item'><a title='" + data.yybgs[i].F06 + "' href='"+url+data.yybgs[i].path+"' "+image+"'>"
											+ "<p class='ellipsis'>"
											+ subStringLength(
													data.yybgs[i].F06, 8, "...")
											+ "</p>"
											+ "<p class='ellipsis'>"
											+ formatTime(data.yybgs[i].F12)
											+ "</p></a>" + "</div></li>";
								}
								liStr += "</ul>";
								$("#list_id").html(liStr);
							}
						}
					});
		}

		$(".disclosure_nav").css('height', $(".disclosure_con").height() + 'px');
		$(function() {
			initYearData();
			//运营报告 - 年份
			//var liobj = $("#year_id li");
			
			/* var liobj = $("#year_id").find("li");
			alert(liobj);
			if(liobj.length==7){
				alert(111)
				liobj.css("padding","0 23px");
			}
			else if(liobj.length==8){
				liobj.css("padding","0 17px");
			} */
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