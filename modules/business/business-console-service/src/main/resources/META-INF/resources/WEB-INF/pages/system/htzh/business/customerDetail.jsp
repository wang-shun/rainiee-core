<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.console.servlets.AbstractConsoleServlet"%>
<%@page import="com.dimeng.p2p.repeater.business.entity.Performance"%>
<%@page import="com.dimeng.util.ObjectHelper"%>
<%@ page import="com.dimeng.p2p.console.servlets.system.htzh.business.*" %>
<%@ page import="com.dimeng.p2p.repeater.business.entity.CustomerEntity" %>
<%@ page import="com.dimeng.util.parser.BooleanParser" %>
<%@ page import="com.dimeng.p2p.variables.defines.SystemVariable" %>
<html>
<head>
	<%@include file="/WEB-INF/include/meta.jsp"%>
<%@include file="/WEB-INF/include/style.jsp"%>
<%@include file="/WEB-INF/include/script.jsp"%>
</head>
<body>
	<%
		CURRENT_CATEGORY = "XTGL";
		CURRENT_SUB_CATEGORY = "YWYGL";
		PagingResult<CustomerEntity> result = (PagingResult<CustomerEntity>) request.getAttribute("result");
		Performance performance = ObjectHelper.convert(request.getAttribute("performance"),Performance.class);
		String createTimeStart=request.getParameter("createTimeStart");
		String createTimeEnd=request.getParameter("createTimeEnd");
	%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
					<form action="<%=controller.getURI(request,CustomerDetail.class)%>" method="post" name="form1" id="form1">
					<input type="hidden" name="employNum" value="<%=performance.employNum%>"/>
					<div class="p20">
						<div class="border">
							<div class="title-container">
								<i class="icon-i w30 h30 va-middle title-left-icon"></i>客户详情
								<div class="fr mt5 mr10">
									<input type="button" onclick="location.href='<%=controller.getURI(request,BusinessUserList.class)%>'" class="btn btn-blue2 radius-6 pl20 pr20 ml40" value="返回" />
								</div>
							</div>
							<div class="content-container pl40 pt30 pr40">
              					<ul class="gray6 input-list-container clearfix">
									<li>
										<span class="display-ib mr5">业务员姓名：</span>
										<span class="link-blue"><%StringHelper.filterHTML(out, performance.name);%></span></li>
									<li class="ml50">
										<span class="display-ib mr5">名下客户数 ：</span>
										<span class="link-blue"><%=performance.levelCustomerNumber+performance.secondaryCustomerNumber%></span></li>
								</ul>
							</div>
						</div>
						
						<div class="border mt20">
				          	<div class="content-container pl40 pt30 pr40">
				              <ul class="gray6 input-list-container clearfix">
								  <li><span class="display-ib mr5">用户名： </span> <input
										  type="text" name="userName"
										  value="<%StringHelper.filterQuoter(out,request.getParameter("userName"));%>"
										  class="text border pl5 mr20" /></li>
								  <li><span class="display-ib mr5">姓名： </span> <input
										  type="text" name="realName"
										  value="<%StringHelper.filterQuoter(out,request.getParameter("realName"));%>"
										  class="text border pl5 mr20" /></li>
								  <li>
                                    <span class="display-ib mr5">手机号码：</span>
                                    <input type="text" name="mobile" value="<%StringHelper.filterHTML(out, request.getParameter("mobile"));%>" class="text border pl5 mr20" />
                                </li>
								  <%
								  boolean isTg = BooleanParser.parse(configureProvider.getProperty(SystemVariable.SFZJTG));
									  if(isTg){
								  %>
								  <li>
									  <span class="display-ib mr5">第三方注册：</span>
									  <select name="isThird" class="border mr10 h32 mw100">
										  <option value="">--全部--</option>
										  <option value="yes" <%if("yes".equals(request.getParameter("isThird"))){ %>selected="selected"<%} %>>是</option>
										  <option value="no" <%if("no".equals(request.getParameter("isThird"))){ %>selected="selected"<%} %>>否</option>
									  </select>
								  </li>
								  <%}%>
								  <li>
									  <span class="display-ib mr5">注册时间：</span>
									  <input type="text" name="createTimeStart" id="datepicker1" readonly="readonly" class="text border pl5 w120 date" />
									  <span class="pl5 pr5">至</span>
									  <input type="text" name="createTimeEnd" id="datepicker2" readonly="readonly" class="text border pl5 w120 date mr20" />
								  </li>
								<li class="ml10"><a href="javascript:$('#form1').submit();" class="btn btn-blue radius-6 mr5 pl1 pr15"><i class="icon-i w30 h30 va-middle search-icon "></i>搜索</a></li>
								<li>
									<%if (dimengSession.isAccessableResource(ExportCustomerDetail.class)) {%>
										<a href="javascript:void(0)" onclick="showExport()" class="btn btn-blue radius-6 mr5  pl1 pr15"><i class="icon-i w30 h30 va-middle export-icon "></i>导出</a>
									<%}else{ %>
										<span class="btn btn-gray radius-6 mr5 pl1 pr15"><i class="icon-i w30 h30 va-middle export-icon "></i>导出</span>
									<%} %> 
								</li>
							</ul>
						</div>
					</div>
					<div class=" table-container mt20">
			            <table class="table table-style gray6 tl">
			              <thead>
			                <tr class="title tc">
								<th>序号</th>
								<th>用户名</th>
								<th>真实姓名</th>
								<th>手机号码</th>
								<th>可用余额(元)</th>
								<th>借款负债(元)</th>
								<th>注册时间</th>

							</tr>
							</thead>
								<tbody class="f12">
								<%
									CustomerEntity[] results = result.getItems();
											if (results != null) {
												int i = 1;
												for (CustomerEntity r : results) {
													if (r == null) {
														continue;
													}
									%>
									<tr class="tc">
										<td><%=i++%></td>
										<td><%StringHelper.filterHTML(out, r.userName);%>
										</td>

										<td>
											<%StringHelper.filterHTML(out, r.realName);%>
										</td>
										
										
										<td><%StringHelper.filterHTML(out, r.mobile);%></td>
										<td><%=Formater.formatAmount(r.usableAmount)%></td>
										<td><%=Formater.formatAmount(r.loanAmount)%></td>
										<td><%=DateTimeParser.format(r.registeTime,"yyyy-MM-dd HH:mm:ss")%></td>
										<%
											}
												}else{%>
								<tr><td colspan="7"class="tc">暂无数据</td></tr>
								<%} %>
								</tbody>
							</table>
						</div>
						<div class="clear"></div>
							<%
								AbstractConsoleServlet.rendPagingResult(out, result);
							%>
							<div class="box2 clearfix"></div>
						</div>
					</form>
				</div>
			</div>
		</div>

	<div id="info"></div>
	<div class="popup_bg"  style="display: none;"></div>
	<%@include file="/WEB-INF/include/datepicker.jsp"%>
	<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/dialog.js"></script>
	 <script type="text/javascript">
	 $(function() {
			$("#datepicker1").datepicker({
				inline: true,
				onSelect : function(selectedDate) {
	                $("#datepicker2").datepicker("option", "minDate", selectedDate);  }
			});
		    $('#datepicker1').datepicker('option', {dateFormat:'yy-mm-dd'});
		    $("#datepicker2").datepicker({inline: true});
		    $('#datepicker2').datepicker('option', {dateFormat:'yy-mm-dd'});
		    <%if(!StringHelper.isEmpty(createTimeStart)){%>
		   		$("#datepicker1").val("<%StringHelper.filterHTML(out, request.getParameter("createTimeStart"));%>");
		   	<%}%>
		   	<%if(!StringHelper.isEmpty(createTimeEnd)){%>
		    	$("#datepicker2").val("<%StringHelper.filterHTML(out, request.getParameter("createTimeEnd"));%>");
		    <%}%>
		    $("#datepicker2").datepicker('option', 'minDate',$("#datepicker1").datepicker().val());
	});
	function showExport() {
	    document.getElementById("form1").action = "<%=controller.getURI(request, ExportCustomerDetail.class)%>";
	    $("#form1").submit();
	    document.getElementById("form1").action = "<%=controller.getURI(request, CustomerDetail.class)%>";
	}
</script>
</body>
</html>