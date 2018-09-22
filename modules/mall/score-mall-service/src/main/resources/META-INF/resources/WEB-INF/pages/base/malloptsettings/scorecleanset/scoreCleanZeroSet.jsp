<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.console.servlets.AbstractConsoleServlet" %>
<%@page import="com.dimeng.util.ObjectHelper" %>
<%@page import="com.dimeng.p2p.console.servlets.base.malloptsettings.scorecleanset.ScoreCleanZeroSet" %>
<%@page import="com.dimeng.p2p.repeater.score.entity.ScoreCleanZero" %>
<%@page import="com.dimeng.p2p.console.servlets.base.malloptsettings.scorecleanset.ExportScoreCleanZero" %>
<%@page import="com.dimeng.p2p.repeater.score.SetScoreManage" %>
<%@page import="com.dimeng.p2p.repeater.score.entity.ScoreCleanZeroQuery" %>
<%@page import="com.dimeng.p2p.console.servlets.base.AbstractBaseServlet" %>

<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <%@include file="/WEB-INF/include/script.jsp"%>
</head>
<body>
<%
CURRENT_CATEGORY = "JCXXGL";
CURRENT_SUB_CATEGORY = "JFQLSZ";
String startTime = (String)request.getAttribute("startTime");
String startTime1 = request.getParameter("startTime1");
String endTime1 = request.getParameter("endTime1");
final HttpServletRequest request1 = request;
SetScoreManage manager = serviceSession.getService(SetScoreManage.class);
PagingResult<ScoreCleanZero> result = manager.searchScoreCleanZero(new ScoreCleanZeroQuery() {

    @Override
    public String getStartTime() {
        return request1.getParameter("startTime1");
    }

    @Override
    public String getEndTime() {
        return request1.getParameter("endTime1");
    }

}, new Paging() {
    
    @Override
    public int getSize() {
        return 10;
    }
    
    @Override
    public int getCurrentPage() {
        return IntegerParser.parse(request1
                .getParameter(AbstractBaseServlet.PAGING_CURRENT));
    }
});

%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                    <div class="p20">
                      <form id="formClean" action="<%=controller.getURI(request, ScoreCleanZeroSet.class)%>" method="post">
                      <input type="hidden" name="type" value="cleanZero"/>
                        <div class="border">
                            <div class="title-container"><i class="icon-i w30 h30 va-middle title-left-icon"></i>积分清零设置
                            </div>
                            <div class="content-container pl40 pt10 pr40">
                                <span class="red ml100">注：只能对当天之前的积分进行清零</span>
                                <ul class="gray6 input-list-container clearfix pt5">
                                    <li><span class="display-ib mr5">积分清零范围</span>
                                      <input type="text" name="startTime" readonly="readonly" id="datepicker1" class="text border pl5 w120 date"  value="<%StringHelper.filterHTML(out, startTime);%>"/>
                                      <span class="pl5 pr5">至</span>
                                      <input readonly="readonly" type="text" name="endTime" id="datepicker2" class="text border pl5 w120 mr20 date"  value=""/>
                                    </li>
                                    <li>
                                        <a class="btn btn-blue2 radius-6 mr5 pl10 pr10" href="javascript:void(0);" onclick="clearZero();">执行清零</a>
                                    </li>
                                </ul>
                            </div>
                        </div>
                        </form>
                        <form id="form1" action="<%=controller.getURI(request, ScoreCleanZeroSet.class)%>" method="post">
                        <div class="border-b-s">&nbsp;</div>
                        <div class="mt20">
                            <div class="content-container pl40 pr40">
                                <ul class="gray6 input-list-container clearfix">
                                    <li><span class="display-ib mr5">操作时间</span>
                                      <input type="text" name="startTime1" readonly="readonly" id="datepicker3" class="text border pl5 w120 date"  value="<%StringHelper.filterHTML(out, startTime1);%>"/>
                                      <span class="pl5 pr5">至</span>
                                      <input readonly="readonly" type="text" name="endTime1" id="datepicker4" class="text border pl5 w120 mr20 date"  value="<%StringHelper.filterHTML(out, endTime1);%>"/>
                                    </li>
                                    <li><a href="javascript:$('#form1').submit();"
                                           class="btn btn-blue radius-6 mr5 pl1 pr15"><i
                                            class="icon-i w30 h30 va-middle search-icon "></i>搜索</a></li>

                                    <li>
                                        <%
                                            if (dimengSession.isAccessableResource(ExportScoreCleanZero.class)) {
                                        %>
                                        <a href="javascript:void(0)" onclick="showExport()"
                                           class="btn btn-blue radius-6 mr5 pl1 pr15"><i
                                                class="icon-i w30 h30 va-middle export-icon "></i>导出</a>
                                        <%
                                        } else {
                                        %>
                                        <span class="btn btn-gray radius-6 mr5 pl1 pr15"><i
                                                class="icon-i w30 h30 va-middle export-icon "></i>导出</span>
                                        <%
                                            }
                                        %>
                                    </li>

                                </ul>
                            </div>
                        </div>
                        <div class="border table-container">
                            <table class="table table-style gray6 tl">
                                <thead>
                                <tr class="title tc">
                                    <th>序号</th>
                                    <th>积分清零范围</th>
                                    <th>操作人</th>
                                    <th>操作时间</th>
                                </tr>
                                </thead>
                                <tbody class="f12">
                                <%
                                    if (result != null && result.getItemCount() > 0) {
                                        ScoreCleanZero[] lists = result.getItems();
                                        if (lists != null) {
                                            int index = 1;
                                            for (ScoreCleanZero scoreCleanZero : lists) {
                                %>
                                <tr class="tc">
                                    <td><%=index++%></td>
                                    <td><%=DateTimeParser.format(scoreCleanZero.F03,"yyyy-MM-dd")%> ~ <%=DateTimeParser.format(scoreCleanZero.F04,"yyyy-MM-dd")%></td>
                                    <td><%StringHelper.filterHTML(out, scoreCleanZero.operater);%></td>
                                    <td><%=DateTimeParser.format(scoreCleanZero.F05)%></td>
                                </tr>
                                <%
                                        }
                                    }
                                } else {
                                %>
                                <tr class="tc">
                                    <td colspan="4">暂无数据</td>
                                </tr>
                                <%} %>
                                </tbody>
                            </table>
                        </div>

                        <!--分页-->
                        <%
                            AbstractConsoleServlet.rendPagingResult(out, result);
                        %>
                        <!--分页 end-->
                      </form>
                    </div>
            </div>
        </div>
    </div>
<div id="info"></div>
<div class="popup_bg hide"></div>
<%@include file="/WEB-INF/include/datepicker.jsp"%>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/dialog.js"></script>
<script type="text/javascript">
$(function () {
    var date = new Date();
    date.setDate(date.getDate()-1);
    $("#datepicker2").datepicker({inline: true,maxDate:date.Format("yyyy-MM-dd")});
    $('#datepicker2').datepicker('option', {dateFormat: 'yy-mm-dd'});
    $("#datepicker2").datepicker('option', 'minDate', "<%StringHelper.filterHTML(out, startTime);%>");
    $("#datepicker3").datepicker({
        inline: true,
        onSelect: function (selectedDate) {
            $("#datepicker4").datepicker("option", "minDate", selectedDate);
        }
    });
    $('#datepicker3').datepicker('option', {dateFormat: 'yy-mm-dd'});
    $("#datepicker4").datepicker({inline: true});
    $('#datepicker4').datepicker('option', {dateFormat: 'yy-mm-dd'});
    <%if(!StringHelper.isEmpty(startTime1)){%>
    $("#datepicker3").datepicker("setDate", "<%StringHelper.filterHTML(out, startTime1);%>");
    <%}%>
    <%if(!StringHelper.isEmpty(endTime1)){%>
    $("#datepicker4").datepicker("setDate", "<%StringHelper.filterHTML(out,endTime1);%>");
    <%}%>
    $("#datepicker4").datepicker('option', 'minDate', $("#datepicker3").datepicker().val());
});
function showExport() {
        document.getElementById("form1").action = "<%=controller.getURI(request, ExportScoreCleanZero.class)%>";
        $("#form1").submit();
        document.getElementById("form1").action = "<%=controller.getURI(request, ScoreCleanZeroSet.class)%>";
}

function clearZero(){
	var startTime = $("input[name='startTime']").val();
	var endTime = $("input[name='endTime']").val();
	$(".popup_bg").show();
	if(!startTime){
		$("#info").html(showDialogInfo('开始时间不能为空',"wrong"));
		return;
	}
	if(!endTime){
		$("#info").html(showDialogInfo('结束时间不能为空',"wrong"));
		return;
	}
    $("#info").html(showConfirmDiv("确定把"+startTime+"~"+endTime+"<br/>时间范围内获取的积分清零？", 0, "fb"));
}

function toConfirm(msg, i, type) {
    $('#formClean').submit();
}
</script>
<%
    String warringMessage = controller.getPrompt(request, response, PromptLevel.ERROR);
    if (!StringHelper.isEmpty(warringMessage)) {
%>
<script type="text/javascript">
    $("#info").html(showDialogInfo("<%StringHelper.filterHTML(out, warringMessage); %>", "wrong"));
    $("div.popup_bg").show();
</script>
<%} %>
</body>
</html>