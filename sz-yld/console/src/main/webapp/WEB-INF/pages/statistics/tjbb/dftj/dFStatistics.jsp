<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.console.servlets.AbstractConsoleServlet"%>
<%@page import="com.dimeng.p2p.console.servlets.statistics.tjbb.dftj.DFStatistics" %>
<%@page import="com.dimeng.p2p.console.servlets.statistics.tjbb.dftj.ExportDFStatistics" %>
<%@page import="com.dimeng.p2p.modules.statistics.console.service.entity.DFStatisticsEntity" %>
<%@page import="com.dimeng.util.ObjectHelper" %>
<%@page import="java.util.Map" %>
<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <%@include file="/WEB-INF/include/script.jsp"%>
</head>
<body>
<%
    CURRENT_CATEGORY = "TJGL";
    CURRENT_SUB_CATEGORY = "JGDFTJB";
    PagingResult<DFStatisticsEntity> result = ObjectHelper.convert(request.getAttribute("result"), PagingResult.class);
    Map total = ObjectHelper.convert(request.getAttribute("dfTotal"), Map.class);
    DFStatisticsEntity[] repayments = result.getItems();
    String dfTimeStart = request.getParameter("dfTimeStart");
    String dfTimeEnd = request.getParameter("dfTimeEnd");
    String reMoneyTimeStart = request.getParameter("reMoneyTimeStart");
    String reMoneyTimeEnd = request.getParameter("reMoneyTimeEnd");

%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <form id="form_loan" action="<%=controller.getURI(request, DFStatistics.class)%>" method="post">
                    <div class="p20">
                        <div class="border">
                            <div class="title-container"><i class="icon-i w30 h30 va-middle title-left-icon"></i>垫付统计表
                            </div>
                            <div class="content-container pl40 pt30 pr40">
                                <ul class="gray6 input-list-container clearfix">
                                    <li><span class="display-ib mr5">借款ID： </span>
                                        <input type="text" name="loanId"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("loanId"));%>"
                                               class="text border pl5 mr20"/>
                                    </li>
                                    <li><span class="display-ib mr5">垫付账户名： </span>
                                        <input type="text" name="dfAccount"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("dfAccount"));%>"
                                               class="text border pl5 mr20"/>
                                    </li>
                                    <li><span class="display-ib mr5">垫付方名称： </span>
                                        <input type="text" name="dfAccountName"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("dfAccountName"));%>"
                                               class="text border pl5 mr20"/>
                                    </li>
                                    <li><span class="display-ib mr5">垫付时间： </span>
                                        <input type="text" name="dfTimeStart" readonly="readonly" id="datepicker1"
                                               class="text border pl5 w120 date"/>
                                        <span class="pl5 pr5">至</span>
                                        <input readonly="readonly" type="text" name="dfTimeEnd" id="datepicker2"
                                               class="text border pl5 w120 mr20 date"/>
                                    </li>
                                    <li><a href="javascript:void(0);" onclick="search1();"
                                           class="btn btn-blue radius-6 mr5 pl1 pr15"><i
                                            class="icon-i w30 h30 va-middle search-icon "></i>搜索</a></li>
                                    <li>
                                        <%if (dimengSession.isAccessableResource(ExportDFStatistics.class)) {%>
                                        <a href="javascript:void(0);" onclick="showExport();"
                                           class="btn btn-blue radius-6 mr5  pl1 pr15"><i
                                                class="icon-i w30 h30 va-middle export-icon "></i>导出</a>
                                        <%} else {%>
                                        <span class="btn btn-gray radius-6 mr5 pl1 pr15"><i
                                                class="icon-i w30 h30 va-middle export-icon "></i>导出</span>
                                        <%}%>
                                    </li>
                                </ul>
                            </div>
                        </div>
                            <div class=" table-container mt20">
                                <table class="table table-style gray6 tl">
                                    <thead>
                                    <tr class="title tc">
                                    	<th>序号</th>
                                        <th>借款ID</th>
                                        <th>剩余期数</th>
                                        <th>借款人账号</th>
                                        <th>垫付账户名</th>
                                        <th>垫付方名称</th>
                                        <th>实际垫付金额(元)</th>
                                        <th>垫付时间</th>
                                        <th>返还金额(元)</th>
                                        <th>垫付盈亏(元)</th>

                                    </tr>
                                    </thead>
                                    <tbody class="f12">
                                    <%
                                    	int i = 0;
                                        if (repayments != null && repayments.length > 0) {
                                            for (DFStatisticsEntity repayment : repayments) {
                                                i++;
                                    %>
                                    <tr class="tc">
                                    	<td><%=i %></td>
                                        <td><%=repayment.loanId %></td>
                                        <td><%StringHelper.filterHTML(out, repayment.periods); %></td>
                                        <td><%StringHelper.filterHTML(out, repayment.jkrAccount); %></td>
                                        <td><%StringHelper.filterHTML(out, repayment.dfAccount); %></td>
                                        <td><%StringHelper.filterHTML(out, repayment.dfAccountName); %></td>
                                        <td><%=Formater.formatAmount(repayment.actualMoney) %></td>
                                        <td><%=TimestampParser.format(repayment.dfTime) %></td>
                                        <td><%=Formater.formatAmount(repayment.reMoney) %></td>
                                        <td><%=Formater.formatAmount(repayment.dfEarn) %></td>
                                    </tr>
                                    <%
                                        }
                                    } else {
                                    %>
                                    <tr>
                                        <td colspan="10" class="tc">暂无数据</td>
                                    </tr>
                                    <%} %>
                                    </tbody>
                                </table>
                            </div>
                            <p class="mt5">
                                <span class="mr30">实际垫付金额：<em class="red"><%=total.get("actualMoneySum")%>
                                </em> 元</span>
                                <span class="mr30">返还金额：<em class="red"><%=total.get("reMoneySum")%>
                                </em> 元</span>
                                <span class="mr30">垫付盈亏：<em class="red"><%=total.get("dfEarnSum")%>
                                </em> 元</span>
                            </p>
                            <!--分页-->
                            <%AbstractConsoleServlet.rendPagingResult(out, result);%>
                            <!--分页 end-->
                    </div>
                </form>
            </div>
        </div>
    </div>
    <!--右边内容 结束-->
<%@include file="/WEB-INF/include/jquery.jsp" %>
<%@include file="/WEB-INF/include/datepicker.jsp" %>
<%
    String warringMessage = controller.getPrompt(request, response, PromptLevel.WARRING);
    if (!StringHelper.isEmpty(warringMessage)) {
%>
<!--弹出框-->
<div class="popup-box">
    <div class="popup-title-container">
        <h3 class="pl20 f18">提示</h3>
        <a class="icon-i popup-close2" href="javascript:void(0);" onclick="closeInfo()"></a>
    </div>
    <div class="popup-content-container pt20 pb20 clearfix">
        <div class="tc mb20 mt40"><span class="icon-i w30 h30 va-middle radius-wrong-icon"></span><span
                class="f20 h30 va-middle ml10"><%StringHelper.filterHTML(out, warringMessage); %></span></div>
        <div class="tc f16"><a href="javascript:void(0);" onclick="closeInfo();"
                               class="btn-blue2 btn white radius-6 pl20 pr20">确定</a></div>
    </div>
</div>
<div class="popup_bg"></div>
<%}%>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/jquery-ui-1.10.4/js/time.js"></script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/validation.js"></script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/web.js"></script>
<script type="text/javascript">
    $(function () {
        $('#datepicker1').datepicker('option', {dateFormat: 'yy-mm-dd'});
        $('#datepicker1').datepicker({
            inline: true,
            onSelect: function (selectedDate) {
                $("#datepicker2").datepicker("option", "minDate", selectedDate);
            }
        });
        $('#datepicker2').datepicker('option', {dateFormat: 'yy-mm-dd'});
        $('#datepicker2').datepicker({inline: true});
        <%
            if(!StringHelper.isEmpty(dfTimeStart))
            {
        %>
        $("#datepicker1").val("<%StringHelper.filterHTML(out, request.getParameter("dfTimeStart"));%>");
        <%}%>
        <%
        if(!StringHelper.isEmpty(dfTimeEnd))
        {
        %>
        $("#datepicker2").val("<%StringHelper.filterHTML(out, request.getParameter("dfTimeEnd"));%>");
        <%}%>

        $("#datepicker2").datepicker('option', 'minDate', $("#datepicker1").datepicker().val());

        $('#datepicker3').datepicker('option', {dateFormat: 'yy-mm-dd'});
        $('#datepicker3').datepicker({
            inline: true,
            onSelect: function (selectedDate) {
                $("#datepicker4").datepicker("option", "minDate", selectedDate);
            }
        });
        $('#datepicker4').datepicker('option', {dateFormat: 'yy-mm-dd'});
        $('#datepicker4').datepicker({inline: true});
        $("#datepicker4").datepicker('option', 'minDate', $("#datepicker3").datepicker().val());

        <%
           if(!StringHelper.isEmpty(reMoneyTimeStart))
           {
       %>
        $("#datepicker3").val("<%StringHelper.filterHTML(out, request.getParameter("reMoneyTimeStart"));%>");
        <%}%>
        <%
        if(!StringHelper.isEmpty(reMoneyTimeEnd))
        {
        %>
        $("#datepicker4").val("<%StringHelper.filterHTML(out, request.getParameter("reMoneyTimeEnd"));%>");
        <%}%>

        $("#datepicker4").datepicker('option', 'minDate', $("#datepicker3").datepicker().val());
    });

    function search1() {
        document.getElementsByName("paging.current")[0].value = 1;
        $("#form_loan").submit();
    }

    function showExport() {
        document.getElementById("form_loan").action = "<%=controller.getURI(request, ExportDFStatistics.class)%>";
        $("#form_loan").submit();
        document.getElementById("form_loan").action = "<%=controller.getURI(request, DFStatistics.class)%>";
    }
</script>
</body>
</html>