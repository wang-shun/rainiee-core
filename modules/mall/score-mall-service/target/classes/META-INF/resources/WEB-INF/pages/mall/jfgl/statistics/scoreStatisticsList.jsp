<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.console.servlets.mall.jfgl.statistics.ScoreStatisticsList"%>
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
    CURRENT_SUB_CATEGORY = "JFTJLB";
    PagingResult<T6105> t6105List = ObjectHelper.convert(request.getAttribute("t6105List"), PagingResult.class);
    T6105[] t6105Array = (t6105List == null ? null : t6105List.getItems());
    ScoreCountExt scoreCountExt = ObjectHelper.convert(request.getAttribute("scoreCountExt"), ScoreCountExt.class);
    String createTimeStart = request.getParameter("startTime");
    String createTimeEnd = request.getParameter("endTime");
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
      <form id="form1" action="<%=controller.getURI(request, ScoreStatisticsList.class)%>" method="post">
		<div class="p20">
			<div class="border">
			    <div class="title-container"><i class="icon-i w30 h30 va-middle title-left-icon"></i>积分统计列表</div>
			    <div class="content-container pl40 pt30 pr40">
			      <ul class="gray6 input-list-container clearfix">
			        <li><span class="display-ib mr5">用户名</span>
			          <input type="text" name="loginName" value="<%StringHelper.filterHTML(out, request.getParameter("loginName"));%>" class="text border pl5 mr20" />
			        </li>
			        <li><span class="display-ib mr5">真实姓名</span>
			          <input type="text" name="realName" value="<%StringHelper.filterHTML(out, request.getParameter("realName"));%>" class="text border pl5 mr20" />
			        </li>
			        <li><span class="display-ib mr5">注册时间</span>
			          <input type="text" name="startTime" readonly="readonly" id="datepicker1" class="text border pl5 w120 date" />
			          <span class="pl5 pr5">至</span>
			          <input readonly="readonly" type="text" name="endTime" id="datepicker2" class="text border pl5 w120 mr20 date" />
			        </li>
			        <li> <a href="javascript:onSubmit();" class="btn btn-blue radius-6 mr5 pl1 pr15"><i class="icon-i w30 h30 va-middle search-icon "></i>搜索</a></li>
			        <li>
                	<%
                     		if (dimengSession.isAccessableResource(ExportScoreStatisticsList.class)) {
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
			          <th>用户名</th>
			          <th>真实姓名</th>
			          <th>总积分</th>
			          <th>可用积分</th>
			          <th>已用积分</th>
			          <th>兑换次数</th>
			          <th>注册时间</th>
			          <th>操作</th>
			        </tr>
			      </thead>
			      <tbody class="f12">
			        <%
                        	if (t6105Array != null && t6105Array.length>0) {
                        		int index = 1;
                        		for (T6105 t6105:t6105Array)
                        		{if (t6105 == null) {continue;}
                        %>
                        <tr class="title tc">
                          <td><%=index++ %></td>
                          <td><%StringHelper.filterHTML(out, t6105.F08);%></td>
                          <td><%StringHelper.filterHTML(out, t6105.F09);%></td>
                          <td><%=t6105.F03%></td>
                          <td><%=t6105.F10%></td>
                          <td><%=t6105.F04%></td>
                          <td><%=t6105.F05%></td>
                          <td><%=TimestampParser.format(t6105.F06)%></td>
                          <td>
                          	<%if (dimengSession.isAccessableResource(ScoreGetRecord.class)) {%>
                          	<a href="<%=controller.getURI(request,ScoreGetRecord.class)%>?userId=<%=t6105.F02%>" class="link-blue mr20 click-link">详情</a>
                          	<%}else{ %>
                          	<span class="disabled">详情</span>
                          	<%} %>
                          </td>
                        </tr>
						<%}%>
						<tr class="tc">
                            <td>总计：</td>
                            <td></td>
                            <td></td>
                            <td class="red"><%=scoreCountExt.SumScore%></td>
                            <td class="red"><%=scoreCountExt.SumUnUsedScore%></td>
                            <td class="red"><%=scoreCountExt.SumUsedScore%></td>
                            <td class="red"><%=scoreCountExt.SumExchangeNum%></td>
                            <td></td>
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
                            AbstractConsoleServlet.rendPagingResult(out, t6105List);
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
        document.getElementById("form1").action = "<%=controller.getURI(request, ExportScoreStatisticsList.class)%>";
        $("#form1").submit();
        document.getElementById("form1").action = "<%=controller.getURI(request, ScoreStatisticsList.class)%>";
    }
    /* function onSubmit(){
     $("#form1").submit();
     } */
</script>
</body>
</html>