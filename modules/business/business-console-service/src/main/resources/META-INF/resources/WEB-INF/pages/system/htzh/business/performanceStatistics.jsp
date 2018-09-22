<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.framework.http.entity.RoleBean"%>
<%@page import="com.dimeng.p2p.S71.entities.T7190"%>
<%@page import="com.dimeng.p2p.console.servlets.AbstractConsoleServlet"%>
<%@page import="com.dimeng.p2p.console.servlets.system.htzh.business.BusinessUserList"%>
<%@page import="com.dimeng.p2p.console.servlets.system.htzh.business.ExportPerformance"%>
<%@page import="com.dimeng.p2p.console.servlets.system.htzh.business.PerformanceStatistics"%>
<%@page import="com.dimeng.p2p.repeater.business.entity.Performance"%>
<%@page import="com.dimeng.util.ObjectHelper"%>
<%@ page import="com.dimeng.p2p.console.servlets.system.htzh.business.ResultsDetail" %>
<html>
<head>
	<%@include file="/WEB-INF/include/meta.jsp"%>
<%@include file="/WEB-INF/include/style.jsp"%>
<%@include file="/WEB-INF/include/script.jsp"%>
</head>
<body>
	<%
		PagingResult<T7190> result = (PagingResult<T7190>) request.getAttribute("result");
	RoleBean[] roleBeans = ObjectHelper.convertArray(
			request.getAttribute("roles"), RoleBean.class);
	%>
	<%
		CURRENT_CATEGORY = "XTGL";
		CURRENT_SUB_CATEGORY = "YWYGL";
		Performance performance = ObjectHelper.convert(request.getAttribute("performance"),Performance.class);
		String createTimeStart=request.getParameter("createTimeStart");
		String createTimeEnd=request.getParameter("createTimeEnd");
	%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
					<form action="<%=controller.getURI(request,PerformanceStatistics.class)%>?employNum=<%=performance.employNum%>" method="post" name="form1" id="form1">
					<div class="p20">
						<div class="border">
							<div class="title-container">
								<i class="icon-i w30 h30 va-middle title-left-icon"></i>业绩统计
								<div class="fr mt5 mr10">
									<input type="button" onclick="location.href='<%=controller.getURI(request,BusinessUserList.class)%>'" class="btn btn-blue2 radius-6 pl20 pr20 ml40" value="返回" />
								</div>
							</div>
							<div class="content-container pl40 pt30 pr40">
              					<ul class="gray6 input-list-container clearfix">
									<li>
										<span class="display-ib mr5">业务员：</span>
										<span class="link-blue"><%StringHelper.filterHTML(out, performance.name);%></span></li>
									<li class="ml50">
										<span class="display-ib mr5">名下客户数 ：</span>
										<span class="link-blue"><%=performance.levelCustomerNumber+performance.secondaryCustomerNumber%></span></li>
									<li class="ml50">
									<span class="display-ib mr5">用户累计投资金额 ：</span>
									<span class="link-blue"><%=Formater.formatAmount(performance.levelInvestmentAmount+performance.secondaryInvestmentAmount)%></span>元
								</li>
									<li class="ml50">
										<span class="display-ib mr5">用户累计借款金额 ：</span>
										<span class="link-blue"><%=Formater.formatAmount(performance.levelLoanAmount+performance.secondaryLoanAmount)%></span>元
									</li>
									<li class="ml50">
										<span class="display-ib mr5">用户累计充值金额 ：</span>
										<span class="link-blue"><%=Formater.formatAmount(performance.levelChargeAmount+performance.secondChargeAmount)%></span>元
									</li>
									<li class="ml50">
										<span class="display-ib mr5">用户累计提现金额 ：</span>
										<span class="link-blue"><%=Formater.formatAmount(performance.levelWithDrawAmount+performance.secondWithDrawAmount)%></span>元
									</li>
								</ul>
							</div>
						</div>
						
						<div class="border mt20">
				          	<div class="content-container pl40 pt30 pr40">
				              <ul class="gray6 input-list-container clearfix">
				              	
								<li>
									<span class="display-ib mr5">成交时间：</span>
									<input type="text" name="createTimeStart" id="datepicker1" readonly="readonly" class="text border pl5 w120 date" />
									<span class="pl5 pr5">至</span>
									<input type="text" name="createTimeEnd" id="datepicker2" readonly="readonly" class="text border pl5 w120 date" />
								</li>
							
								<li class="ml10"><a href="javascript:$('#form1').submit();" class="btn btn-blue radius-6 mr5 pl1 pr15"><i class="icon-i w30 h30 va-middle search-icon "></i>搜索</a></li>
								<li>
									<%if (dimengSession.isAccessableResource(ExportPerformance.class)) {%>
										<a href="javascript:void(0)" onclick="toExport('<%=controller.getURI(request, ExportPerformance.class)%>?employNum=<%=performance.employNum%>')" class="btn btn-blue radius-6 mr5  pl1 pr15"><i class="icon-i w30 h30 va-middle export-icon "></i>导出</a>
									<%}else{ %>
										<span class="btn btn-gray radius-6 mr5 pl1 pr15"><i class="icon-i w30 h30 va-middle export-icon "></i>导出</span>
									<%} %> 
								</li>
							</ul>
						</div>
					</div>

						<div class="border mt20">
							<div class="tabnav-container">
								<ul class="clearfix">
									<li><a href="javascript:void(0);" class="tab-btn-click select-a">业绩统计<i class="icon-i tab-arrowtop-icon"></i></a></li>
									<li><a href="<%=controller.getURI(request,ResultsDetail.class)%>?employNum=<%=performance.employNum%>"  class="tab-btn-click">业绩明细</a></li>
								</ul>
							</div>
							<div class=" table-container p20">
			            <table class="table table-style gray6 tl">
			              <thead>
			                <tr class="title tc">
								<th>序号</th>
								<th>成交时间</th>
								<th>一级用户投资金额(元)</th>
								<th>一级用户借款金额(元)</th>
								<th>一级用户充值金额(元)</th>
								<th>一级用户提现金额(元)</th>
								<th>二级用户投资金额(元)</th>
								<th>二级用户借款金额(元)</th>
								<th>二级用户充值金额(元)</th>
								<th>二级用户提现金额(元)</th>
							</tr>
							</thead>
								<tbody class="f12">
								<%
									  T7190[] t7190 = result.getItems();
											if (t7190 != null && t7190.length > 0) {
												int i = 1;
												for (T7190 t : t7190) {
													if (t == null) {
														continue;
													}
													//if(t.F02!=0||t.F03!=0||t.F04!=0||t.F05!=0){
									%>
									<tr class="tc">
										<td><%=i++%></td>
										<td><%=DateTimeParser.format(t.F01,"yyyy-MM-dd") %></td>
										<td>
											<%=Formater.formatAmount(t.F02)%>
										</td>
										<td>
											<%=Formater.formatAmount(t.F03)%>
										</td>
										
										
										<td><%=Formater.formatAmount(t.F04)%></td>
										<td><%=Formater.formatAmount(t.F05)%></td>
										<td><%=Formater.formatAmount(t.F06)%></td>
										<td><%=Formater.formatAmount(t.F07)%></td>
										<td><%=Formater.formatAmount(t.F08)%></td>
										<td><%=Formater.formatAmount(t.F09)%></td>
										
										<%
													//}
											}
												}else{%>
								<tr><td colspan="10"class="tc">暂无数据</td></tr>
								<%} %>
								</tbody>
							</table>
						</div>
						</div>
						<div class="clear"></div>
							<div class="mb10">
								<span class="mr30">一级用户投资金额共计：<em class="red"><%=Formater.formatAmount(performance.levelInvestmentAmount)%></em> 元</span>
								<span class="mr30">一级用户借款金额共计：<em class="red"><%=Formater.formatAmount(performance.levelLoanAmount)%></em> 元</span>
								<span class="mr30">一级用户充值金额共计：<em class="red"><%=Formater.formatAmount(performance.levelChargeAmount)%></em> 元</span>
								<span class="mr30">一级用户提现金额共计：<em class="red"><%=Formater.formatAmount(performance.levelWithDrawAmount)%></em> 元</span>
							</div>
							<div class="mb10">
								<span class="mr30">二级用户投资金额共计：<em class="red"><%=Formater.formatAmount(performance.secondaryInvestmentAmount)%></em> 元</span>
								<span class="mr30">二级用户借款金额共计：<em class="red"><%=Formater.formatAmount(performance.secondaryLoanAmount)%></em> 元</span>
								<span class="mr30">二级用户充值金额共计：<em class="red"><%=Formater.formatAmount(performance.secondChargeAmount)%></em> 元</span>
								<span class="mr30">二级用户提现金额共计：<em class="red"><%=Formater.formatAmount(performance.secondWithDrawAmount)%></em> 元</span>
							</div>
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
		
	<%--
	function resetLoginError(userName)
	{
		$.ajax({
			type:"post",
			url:"<%=controller.getURI(request, ResetConsoleLoginError.class)%>",
			data:{"userName" : userName},
		    dataType:"text",
		    success: function(returnData){
		    	$("#info").html(showDialogInfo(returnData,"perfect"));
				$("div.popup_bg").show();
			},
			error: function(XMLHttpRequest, textStatus, errorThrown){
			}
		});
	}--%>
	function toExport(url){
		$("#form1").attr("action",url);
		$("#form1").submit();
		$("#form1").attr("action",'<%=controller.getURI(request,PerformanceStatistics.class)%>?employNum=<%=performance.employNum%>');
	}
</script> 
</body>
</html>