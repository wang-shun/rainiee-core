<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page
	import="com.dimeng.p2p.console.servlets.finance.zjgl.pttzgl.PttzglList"%>
<%@page import="com.dimeng.p2p.S61.enums.T6110_F06"%>
<%@page import="com.dimeng.p2p.S61.enums.T6110_F10"%>
<%@page import="com.dimeng.p2p.S61.enums.T6104_F07"%>
<%@page
	import="com.dimeng.p2p.console.servlets.finance.AbstractFinanceServlet"%>
<%@page
	import="com.dimeng.p2p.console.servlets.finance.zjgl.pttzgl.ExportPttzgl"%>
<%@page
	import="com.dimeng.p2p.console.servlets.finance.zjgl.pttzgl.PtCharge"%>
<%@page
	import="com.dimeng.p2p.console.servlets.finance.zjgl.pttzgl.PtWithdraw"%>
<%@page
	import="com.dimeng.p2p.modules.account.console.service.entity.CheckBalanceRecord"%>
<%@page
	import="com.dimeng.p2p.modules.account.console.service.entity.CheckBalanceTotalAmount"%>
<%@page import="com.dimeng.util.ObjectHelper"%>
<%@page import="java.math.BigDecimal"%>
<html>
<head>
<%@include file="/WEB-INF/include/meta.jsp"%>
<%@include file="/WEB-INF/include/style.jsp"%>
<%@include file="/WEB-INF/include/script.jsp"%>
</head>
<%
    CURRENT_CATEGORY = "CWGL";
    CURRENT_SUB_CATEGORY = "PTTZGL";
    BigDecimal ljsr = ObjectHelper.convert(request.getAttribute("totalIncomeAmount"), BigDecimal.class);
    BigDecimal ljzc = ObjectHelper.convert(request.getAttribute("totalExpendAmount"), BigDecimal.class);
    PagingResult<CheckBalanceRecord> result = (PagingResult<CheckBalanceRecord>) request.getAttribute("result");
    CheckBalanceRecord searchAmount = (CheckBalanceRecord)request.getAttribute("searchAmount");
    CheckBalanceTotalAmount cbta = ObjectHelper.convert(request.getAttribute("cbta"), CheckBalanceTotalAmount.class);
    String startTime = request.getParameter("startTime");
    String endTime = request.getParameter("endTime");
    Boolean tg = BooleanParser.parseObject(configureProvider.getProperty(SystemVariable.SFZJTG));
%>
<body>
	<div class="right-container">
		<div class="viewFramework-body">
			<div class="viewFramework-content">
				<form action="<%=controller.getURI(request, PttzglList.class)%>"
					method="post" name="form1" id="form1">
					<div class="p20">
						<div class="border">
							<div class="title-container">
								<i class="icon-i w30 h30 va-middle title-left-icon"></i>平台调账管理
							</div>
							<div class="content-container pl40 pt30 pr40">
								<ul class="gray6 input-list-container clearfix">
									<li><span class="display-ib mr5">累计收入调账：</span> <span
										class="link-blue"><%=Formater.formatAmount(ljsr)%></span>元</li>
									<li class="ml50"><span class="display-ib mr5">累计支出调账
											：</span> <span class="link-blue"><%=Formater.formatAmount(ljzc)%></span>元
									</li>
								</ul>
							</div>
						</div>

						<div class="border mt20">
							<div class="content-container pl40 pt30 pr40">
								<ul class="gray6 input-list-container clearfix">
									<li><span class="display-ib mr5">订单号：</span> <input
										type="text" name="orderId"
										value="<%StringHelper.filterHTML(out, request.getParameter("orderId"));%>"
										class="text border pl5 mr20" /></li>
									<li><span class="display-ib mr5">类型：</span> <select
										name="type" class="border mr20 h32 mw100">
											<option value="">全部</option>
											<option value="1005"
												<%if ("1005".equals(request.getParameter("type"))) {%>
												selected="selected" <%}%>>平台充值</option>
											<option value="2004"
												<%if ("2004".equals(request.getParameter("type"))) {%>
												selected="selected" <%}%>>平台提现</option>
									</select></li>
									<li><span class="display-ib mr5">操作人：</span> <input
										type="text" name="operationer"
										value="<%StringHelper.filterHTML(out, request.getParameter("operationer"));%>"
										class="text border pl5 mr20" /></li>
									<%
									    if (tg) {
									%>
									<li><span class="display-ib mr5">状态：</span> <select
										name="status" class="border mr20 h32 mw100">
											<%
											    String status = request.getParameter("status");
											%>
											<option value="">全部</option>
											<%
											    for(T6104_F07 s : T6104_F07.values()){
											%>
											<option value="<%=s.name()%>"
												<%if (s.name().equals(status)) {%> selected="selected"
												<%}%>><%=s.getChineseName()%>
											</option>
											<%
											    }
											%>
									</select></li>
									<%
									    }
									%>
									<li><span class="display-ib mr5">操作时间：</span> <input
										type="text" name="startTime" id="datepicker1"
										readonly="readonly" class="text border pl5 w120 date" /> <span
										class="pl5 pr5">至</span> <input type="text" name="endTime"
										id="datepicker2" readonly="readonly"
										class="text border mr20 pl5 w120 date" /></li>
									<li><a href="javascript:$('#form1').submit();"
										class="btn btn-blue radius-6 mr5 pl1 pr15"><i
											class="icon-i w30 h30 va-middle search-icon "></i>搜索</a></li>
									<li>
										<%
										    if (dimengSession.isAccessableResource(ExportPttzgl.class)) {
										%>
										<a href="javascript:void(0)" onclick="showExport()"
										class="btn btn-blue radius-6 mr5  pl1 pr15"><i
											class="icon-i w30 h30 va-middle export-icon "></i>导出</a> <%
     } else {
 %>
										<span class="btn btn-gray radius-6 mr5 pl10 pr10"><i
											class="icon-i w30 h30 va-middle export-icon "></i>导出</span> <%
     }
 %>
									</li>
									<li>
										<%
										    if (dimengSession.isAccessableResource(PtCharge.class)) {
										%> <a
										href="<%=controller.getURI(request, PtCharge.class)%>"
										class="btn btn-blue2 radius-6 mr5 pl10 pr10">平台充值</a> <%
     } else {
 %>
										<span class="btn btn-gray radius-6 mr5 pl10 pr10">平台充值</span>
										<%
										    }
										%>
									</li>
									<li>
										<%
										    if (dimengSession.isAccessableResource(PtWithdraw.class)) {
										%>
										<a href="<%=controller.getURI(request,PtWithdraw.class)%>"
										class="btn btn-blue2 radius-6 mr5 pl10 pr10">平台提现</a> <%
     } else {
 %>
										<span class="btn btn-gray radius-6 mr5 pl10 pr10">平台提现</span>
										<%
										    }
										%>
									</li>
								</ul>
							</div>
						</div>

						<div class="border mt20 table-container">
							<table class="table table-style gray6 tl">
								<thead>
									<tr class="title">
										<th class="tc">序号</th>
										<th class="tc">订单号</th>
										<th class="tc">收入(元)</th>
										<th class="tc">支出(元)</th>
										<th class="tc">类型</th>
										<%
										    if (tg) {
										%>
										<th class="tc">状态</th>
										<%
										    }
										%>
										<th class="tc">操作人</th>
										<th class="tc">操作时间</th>
										<th class="tc">备注</th>
									</tr>
								</thead>
								<tbody class="f12">
									<%
									    CheckBalanceRecord[] items = result.getItems();
									                            if (items != null && items.length > 0) {
									                                int i = 1;
									                                for (CheckBalanceRecord cbr : items) {
									                                    if (cbr == null) {
									                                        continue;
									                                    }
									%>
									<tr class="tc">
										<td><%=i++%></td>
										<td><%=cbr.orderId%></td>
										<td><%=Formater.formatAmount(cbr.income)%></td>
										<td><%=Formater.formatAmount(cbr.expend)%></td>
										<td><%=cbr.type%></td>
										<%
										    if (tg) {
										%>
										<td><%=cbr.status.getChineseName()%></td>
										<%
										    }
										%>
										<td><%=cbr.operationer%></td>
										<td><%=DateTimeParser.format(cbr.operationTime)%></td>
										<td title="<%StringHelper.filterHTML(out,cbr.remark);%>">
											<%
											    StringHelper.filterHTML(out, StringHelper.truncation(cbr.remark, 15));
											%>
										</td>
									</tr>
									<%
									    }
									%>
									<%
									    } else {
									%>
									<tr>
										<td colspan="9" class="tc">暂无数据</td>
									</tr>
									<%
									    }
									%>
								</tbody>
							</table>
						</div>
						<div class="clear"></div>
						<div class="mb10">
							<span class="mr30">收入总金额：<em class="red"><%=Formater.formatAmount(searchAmount.income)%>
							</em> 元
							</span> <span class="mr30">支出总金额：<em class="red"><%=Formater.formatAmount(searchAmount.expend)%>
							</em> 元
							</span>
						</div>
						<%
						    AbstractFinanceServlet.rendPagingResult(out, result);
						%>
					</div>
				</form>
			</div>
		</div>
	</div>
	<%@include file="/WEB-INF/include/datepicker.jsp"%>
	<div class="popup_bg hide"></div>
	<div id="info"></div>
	<script type="text/javascript"
		src="<%=controller.getStaticPath(request)%>/js/dialog.js"></script>
	<%
	    String message = controller.getPrompt(request, response, PromptLevel.INFO);
			    if (!StringHelper.isEmpty(message)) {
	%>
	<script type="text/javascript">
	$(".popup_bg").show();
	$("#info").html(showDialogInfo("<%=message%>","yes"));
	</script>
	<%
	    }
	%>
	<script type="text/javascript">
    $(function () {
        $("#datepicker1").datepicker({
            inline: true,
            onSelect: function (selectedDate) {
                $("#datepicker2").datepicker("option", "minDate", selectedDate);
            }
        });
        $('#datepicker1').datepicker('option', {dateFormat: 'yy-mm-dd'});
        $("#datepicker2").datepicker({inline: true});
        $('#datepicker2').datepicker('option', {dateFormat: 'yy-mm-dd'});
        <%if(!StringHelper.isEmpty(startTime)){%>
        $("#datepicker1").val("<%StringHelper.filterHTML(out, request.getParameter("startTime"));%>");
        <%}%>
        <%if(!StringHelper.isEmpty(endTime)){%>
        $("#datepicker2").val("<%StringHelper.filterHTML(out, request.getParameter("endTime"));%>");
        <%}%>
        $("#datepicker2").datepicker('option', 'minDate', $("#datepicker1").datepicker().val());
    });
    function showExport() {
        document.getElementById("form1").action = "<%=controller.getURI(request, ExportPttzgl.class)%>";
        $("#form1").submit();
        document.getElementById("form1").action = "<%=controller.getURI(request, PttzglList.class)%>";
		}
	</script>
</body>
</html>
