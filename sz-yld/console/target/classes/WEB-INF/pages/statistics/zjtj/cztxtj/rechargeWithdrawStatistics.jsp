<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.console.servlets.AbstractConsoleServlet" %>
<%@page import="com.dimeng.p2p.console.servlets.statistics.zjtj.cztxtj.RechargeWithdraw"%>
<%@page import="java.util.Calendar"%>
<%@page import="com.dimeng.p2p.console.servlets.statistics.zjtj.cztxtj.RecWitStatisticsExport" %>
<%@page import="com.dimeng.p2p.modules.statistics.console.service.RechargeWithdrawManage" %>
<%@page import="com.dimeng.p2p.console.servlets.statistics.zjtj.cztxtj.RechargeWithdrawStatistics" %>
<%@page import="com.dimeng.p2p.modules.statistics.console.service.entity.RecWitReport" %>
<%@page import="com.dimeng.p2p.modules.statistics.console.service.query.RecWitReportQuery" %>
<%@page import="com.dimeng.p2p.modules.statistics.console.service.entity.RecWitReportStatistics" %>
<%@page import="com.dimeng.p2p.FeeCode" %>
<%@page import="java.sql.Timestamp" %>
<%@page import="com.dimeng.util.ObjectHelper" %>
<%@page import="java.text.DecimalFormat" %>
<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <%@include file="/WEB-INF/include/script.jsp"%>
</head>
<body>
<%
    String startTime = request.getParameter("startTime");
	String endTime = request.getParameter("endTime");
    Calendar calendar = Calendar.getInstance();
    String year = calendar.get(Calendar.YEAR)+"";
    String month = (calendar.get(Calendar.MONTH)>9?(calendar.get(Calendar.MONTH)+1):("0"+(calendar.get(Calendar.MONTH)+1)))+"";
    String day = (calendar.get(Calendar.DAY_OF_MONTH)>9?calendar.get(Calendar.DAY_OF_MONTH):calendar.get(Calendar.DAY_OF_MONTH))+"";
    if (startTime == null || StringHelper.isEmpty(startTime)) {
       startTime=year+"-"+month+"-"+"01";
    }
    if (endTime == null || StringHelper.isEmpty(endTime)) {
       endTime=year+"-"+month+"-"+day;
     }
    PagingResult<RecWitReport> list = ObjectHelper.convert(request.getAttribute("list"), PagingResult.class);
    RecWitReportStatistics statistics = (RecWitReportStatistics)request.getAttribute("statistics");
    RecWitReport[] recWitReportArray = (list == null ? null : list.getItems());
    DecimalFormat format = new DecimalFormat("#,##0.00");

    CURRENT_CATEGORY = "TJGL";
    CURRENT_SUB_CATEGORY = "CZTXSJTJ";
%>
<div class="right-container">
    <div class="viewFramework-body">
                            <div class="viewFramework-content">
                                <form method="post" action="<%=controller.getURI(request, RechargeWithdrawStatistics.class)%>" id="search_form">
                                    <div class="p20">
                                        <div class="border">
                                            <div class="title-container"><i class="icon-i w30 h30 va-middle title-left-icon"></i>充值提现数据统计
                                            </div>
                                            <div class="content-container pl40 pt10 pr40">
                                <div class="flat-line-container pt10 pb10">
                                    <ul class="gray6 input-list-container clearfix">
                                        <li>
                                            <input type="text" name="startTime" readonly="readonly" id="datepicker1" 
                                            class="text border pl5 w120 date" value="<%StringHelper.filterHTML(out, startTime);%>"/> 
                                            <span class="pl5 pr5">至</span> 
                                            <input type="text" name="endTime" readonly="readonly" id="datepicker2" 
                                            class="text border pl5 w120 date mr20" value="<%StringHelper.filterHTML(out, endTime);%>"/>
                                        </li>
                                        <li><a class="btn btn-blue radius-6 mr5 pl1 pr15" href="javascript:void(0);"
                                               onclick='$("#search_form").submit();'><i
                                                class="icon-i w30 h30 va-middle search-icon "></i>搜索</a></li>
                                        <li>
                                            <%if (dimengSession.isAccessableResource(RecWitStatisticsExport.class)) { %>
                                            <a href="<%=controller.getURI(request, RecWitStatisticsExport.class) %>?startTime=<%=startTime %>&endTime=<%=endTime %>"
                                               class="btn btn-blue radius-6 mr5  pl1 pr15"><i
                                                    class="icon-i w30 h30 va-middle export-icon "></i>导出</a>
                                            <%} else {%>
                                            <span class="btn btn-gray radius-6 mr5 pl1 pr15"><i
                                                    class="icon-i w30 h30 va-middle export-icon "></i>导出</span>
                                            <%} %>
                                        </li>
                                    </ul>
                                </div>

                        </div>
                    </div>

					<div class="border mt20">
					<div class="tabnav-container">
                        <ul class="clearfix">
                            <li><a href="<%=controller.getURI(request, RechargeWithdraw.class)%>" class="tab-btn">趋势图<i
                                class="icon-i tab-arrowtop-icon"></i></a></li>
                            <li><a href="javascript:void(0);"
                                   class="tab-btn select-a">统计报表</a></li>

                        </ul>
                    </div>
                    <div class="table-container m10">
                        <table class="table table-style gray6 tl">
                            <thead>
                            <tr class="title">
                                <th class="tc">序号</th>
                                <th class="tc">日期</th>
                                <th class="tc">类型</th>
                                <th class="tc">总金额(元)</th>
                                <th class="tc">笔数</th>
                                <th class="tc">用户</th>
                                <th class="tc">线上(元)</th>
                                <th class="tc">线下(元)</th>
                            </tr>
                            </thead>
                            <tbody class="f12">
                            <%if(recWitReportArray !=null && recWitReportArray.length>0)
                            { 
                            	int i = 1;
                            	for(RecWitReport recWitReport : recWitReportArray)
                            	{
                            %>
	                            <tr class="title">
	                                <td class="tc"><%=i++%></td>
	                                <td class="tc"><%=recWitReport.date%></td>
	                                <td class="tc"><%="withdraw".equals(recWitReport.type)?"提现":"充值" %></td>
	                                <td class="tc"><%=Formater.formatAmount(recWitReport.amount) %></td>
	                                <td class="tc"><%=recWitReport.typeCount %></td>
	                                <td class="tc"><%=recWitReport.userCount %></td>
	                                <td class="tc"><%=Formater.formatAmount(recWitReport.onLineRecharge) %></td>
	                                <td class="tc"><%=Formater.formatAmount(recWitReport.offLineRecharge) %></td>
	                            </tr>
                            <%}
                           }else{ %>
                            	<tr>
                            		<td class="tc" colspan="8">暂无数据</td>
                            	</tr>
                            <%} %>
                            </tbody>
                        </table>
                    </div>
                    <p class="mt5 ml10">
                        <span class="mr30">充值总金额：<em class="red"><%=Formater.formatAmount(statistics.rechargeSum)%>
                        </em> 元</span>
                        <span class="mr30">提现总金额：<em class="red"><%=Formater.formatAmount(statistics.withdrawSum)%>
                        </em> 元</span>
                        <span class="mr30">线上充值总金额：<em class="red"><%=Formater.formatAmount(statistics.onLineRecharge)%>
                        </em> 元</span>
                        <span class="mr30">线下充值总金额：<em class="red"><%=Formater.formatAmount(statistics.offLineRecharge)%>
                        </em> 元</span>
                    </p>
                    <!--分页-->
                    <%AbstractConsoleServlet.rendPagingResult(out, list);%>
                    <!--分页 end-->
				</div>
                </div>
          </form>
            </div>
        </div>
    </div>
    <!--右边内容 结束-->
</body>
<%@include file="/WEB-INF/include/datepicker.jsp"%>
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
    <%if(!StringHelper.isEmpty(request.getParameter("startTime"))){%>
   		$("#datepicker1").val("<%StringHelper.filterHTML(out, request.getParameter("startTime"));%>");
   	<%}%>
   	<%if(!StringHelper.isEmpty(request.getParameter("endTime"))){%>
    	$("#datepicker2").val("<%StringHelper.filterHTML(out, request.getParameter("endTime"));%>");
        $("#datepicker2").datepicker('option', 'minDate',$("#datepicker1").datepicker().val());
    <%}%>
    $("#datepicker2").datepicker('option', 'minDate',$("#datepicker1").datepicker().val());
    
    $("#plthywy").click(function() {
    	
    	$(".removeValue").val('');
    	$(".removeHtml").html('');
    	
    	$("div.popup_bg").show();
		$("#replaceEmp").show();
	});
});
</script>
</html>