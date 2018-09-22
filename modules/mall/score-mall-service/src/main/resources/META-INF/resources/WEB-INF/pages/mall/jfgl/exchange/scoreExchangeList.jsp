<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.console.servlets.mall.jfgl.exchange.ScoreExchangeList"%>
<%@page import="com.dimeng.p2p.console.servlets.mall.jfgl.exchange.ExportScoreExchangeList"%>
<%@page import="com.dimeng.p2p.repeater.score.entity.UsedScoreExchangeExt"%>
<%@page import="com.dimeng.p2p.repeater.score.entity.ScoreOrderInfoExt"%>
<%@page import="com.dimeng.p2p.console.servlets.mall.jfgl.statistics.ExportScoreStatisticsList"%>
<%@page import="com.dimeng.p2p.repeater.score.entity.ScoreCountExt"%>
<%@page import="com.dimeng.p2p.console.servlets.mall.jfgl.statistics.ScoreGetRecord"%>
<%@page import="com.dimeng.p2p.S61.entities.T6105"%>
<%@page import="com.dimeng.p2p.console.servlets.AbstractConsoleServlet" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.grxx.ExportGr" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.grxx.JbxxView" %>
<%@page import="com.dimeng.util.ObjectHelper" %>
<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <%@include file="/WEB-INF/include/script.jsp"%>
</head>
<body>
<%
    CURRENT_CATEGORY = "PTSC";
    CURRENT_SUB_CATEGORY = "JFDHJL";
    PagingResult<ScoreOrderInfoExt> scoreOrderInfoExtList = ObjectHelper.convert(request.getAttribute("scoreOrderInfoExtList"), PagingResult.class);
    ScoreOrderInfoExt[] scoreOrderInfoExtArray = (scoreOrderInfoExtList == null ? null : scoreOrderInfoExtList.getItems());
    UsedScoreExchangeExt usedScoreExchangeExt = ObjectHelper.convert(request.getAttribute("usedScoreExchangeExt"), UsedScoreExchangeExt.class);
    String createTimeStart = request.getParameter("startTime");
    String createTimeEnd = request.getParameter("endTime");
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
      <form id="form1" action="<%=controller.getURI(request, ScoreExchangeList.class)%>" method="post">
		<div class="p20">
			<div class="border">
			    <div class="title-container"><i class="icon-i w30 h30 va-middle title-left-icon"></i>积分兑换记录</div>
			    <div class="content-container pl40 pt30 pr40">
			      <ul class="gray6 input-list-container clearfix">
			      	<li><span class="display-ib mr5">订单号</span>
			          <input type="text" name="orderId" value="<%StringHelper.filterHTML(out, request.getParameter("orderId"));%>" class="text border pl5 mr20" />
			        </li>
			        <li><span class="display-ib mr5">用户名</span>
			          <input type="text" name="loginName" value="<%StringHelper.filterHTML(out, request.getParameter("loginName"));%>" class="text border pl5 mr20" />
			        </li>
			        <li><span class="display-ib mr5">真实姓名</span>
			          <input type="text" name="realName" value="<%StringHelper.filterHTML(out, request.getParameter("realName"));%>" class="text border pl5 mr20" />
			        </li>
			        <li><span class="display-ib mr5">成交时间</span>
			          <input type="text" name="startTime" readonly="readonly" id="datepicker1" class="text border pl5 w120 date" />
			          <span class="pl5 pr5">至</span>
			          <input readonly="readonly" type="text" name="endTime" id="datepicker2" class="text border pl5 w120 mr20 date" />
			        </li>
			        <li> <a href="javascript:onSubmit();" class="btn btn-blue radius-6 mr5 pl1 pr15"><i class="icon-i w30 h30 va-middle search-icon "></i>搜索</a></li>
			        <li>
                	<%
                     		if (dimengSession.isAccessableResource(ExportScoreExchangeList.class)) {
                     	%>
                     	<a href="javascript:void(0)" onclick="showExport()" class="btn btn-blue radius-6 mr5  pl1 pr15"><i class="icon-i w30 h30 va-middle export-icon "></i>导出</a>
                     	<%
                     		}else{
                     	%>
                     	<span class="btn btn-gray radius-6 mr5  pl1 pr15"><i class="icon-i w30 h30 va-middle export-icon "></i>导出</span>
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
			        <tr class="title tc">
			          <th>序号</th>
			          <th>订单号</th>
			          <th>用户名</th>
			          <th>真实姓名</th>
			          <th>商品名称</th>
			          <th>数量</th>
			          <th>所需积分</th>
			          <th>成交时间</th>
			        </tr>
			      </thead>
			      <tbody class="f12">
			        <%
                        	if (scoreOrderInfoExtArray != null && scoreOrderInfoExtArray.length>0) {
                        		int index = 1;
                        		for (ScoreOrderInfoExt scoreOrderInfoExt:scoreOrderInfoExtArray)
                        		{if (scoreOrderInfoExt == null) {continue;}
                        %>
                        <tr class="title tc">
                          <td><%=index++ %></td>
                          <td><%=scoreOrderInfoExt.F03%></td>
                          <td><%StringHelper.filterHTML(out, scoreOrderInfoExt.loginName);%></td>
                          <td><%StringHelper.filterHTML(out, scoreOrderInfoExt.realName);%></td>
                          <td><%StringHelper.filterHTML(out, scoreOrderInfoExt.comdName);%></td>
                          <td><%=scoreOrderInfoExt.comdNum%></td>
                          <td><%=scoreOrderInfoExt.comdScore%></td>
                          <td><%=TimestampParser.format(scoreOrderInfoExt.F04)%></td>
                        </tr>
						<%}%>
						<tr class="tc">
                            <td></td>
                            <td></td>
                            <td></td>
                            <td></td>
                            <td></td>
                            <td>商品数量总计：<span class="red"><%=usedScoreExchangeExt.SumCommodity%></span></td>
                            <td>积分兑换总计：<span class="red"><%=usedScoreExchangeExt.SumExchangeScore%></span></td>
                            <td></td>
                        </tr>
						<%}else{%>
                        <tr class="dhsbg"><td colspan="8" class="tc">暂无数据</td></tr>
                        <%} %>
                                </tbody>
                            </table>
                        </div>

                        <!--分页-->
                        <%
                            AbstractConsoleServlet.rendPagingResult(out, scoreOrderInfoExtList);
                        %>
                        <!--分页 end-->
                    </div>
                </form>

            </div>
        </div>
    </div>
    <!--右边内容 结束-->


<%@include file="/WEB-INF/include/datepicker.jsp" %>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/validation.js"></script>
<script type="text/javascript">

function onSubmit(){
	$("input[name='<%=AbstractConsoleServlet.PAGING_CURRENT%>']").val('1');
	$('#form1').submit();
}

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
        <%if(!StringHelper.isEmpty(createTimeStart)){%>
        $("#datepicker1").val("<%StringHelper.filterHTML(out, request.getParameter("startTime"));%>");
        <%}%>
        <%if(!StringHelper.isEmpty(createTimeEnd)){%>
        $("#datepicker2").val("<%StringHelper.filterHTML(out, request.getParameter("endTime"));%>");
        <%}%>
        $("#datepicker2").datepicker('option', 'minDate', $("#datepicker1").datepicker().val());
    });

    function showExport() {
        document.getElementById("form1").action = "<%=controller.getURI(request, ExportScoreExchangeList.class)%>";
        $("#form1").submit();
        document.getElementById("form1").action = "<%=controller.getURI(request, ScoreExchangeList.class)%>";
    }
    /* function onSubmit(){
     $("#form1").submit();
     } */
</script>
</body>
</html>