<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.console.servlets.finance.zjgl.yhxygl.XyList"%>
<%@page import="com.dimeng.p2p.S51.entities.T5122" %>
<%@page import="com.dimeng.p2p.console.servlets.AbstractConsoleServlet" %>
<%@page import="com.dimeng.p2p.console.servlets.finance.zjgl.yhxygl.ExportXYJygl" %>
<%@page import="com.dimeng.p2p.console.servlets.finance.zjgl.yhxygl.XYjlList" %>
<%@page import="com.dimeng.p2p.modules.account.console.service.entity.FundsXYJYView" %>
<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <%@include file="/WEB-INF/include/datepicker.jsp" %>
    <%@include file="/WEB-INF/include/script.jsp"%>
</head>
<%
    T5122[] tradeTypes = (T5122[]) request.getAttribute("tradeTypes");
    PagingResult<FundsXYJYView> result = (PagingResult<FundsXYJYView>) request.getAttribute("result");
    int id = IntegerParser.parse(request.getAttribute("id"));
%>
<%
    CURRENT_CATEGORY = "CWGL";
    CURRENT_SUB_CATEGORY = "XYGL";
    String createTimeStart = request.getParameter("startPayTime");
    String createTimeEnd = request.getParameter("endPayTime");
%>
<body>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <form id="searchForm" name="form1" action="<%=controller.getURI(request, XYjlList.class)%>"
                      method="post">
                    <input type="hidden" name="id" value="<%=id %>">

                    <div class="p20">
                        <div class="border">
                            <div class="title-container"><i class="icon-i w30 h30 va-middle title-left-icon"></i>信用交易记录
                            </div>
                            <div class="content-container pl40 pt30 pr40">
                                <ul class="gray6 input-list-container clearfix">
                                    <li><span class="display-ib mr5">类型明细： </span>
                                        <select name="type" class="border mr20 h32 mw100">
                                            <option value="">全部</option>
                                            <%for (T5122 type : tradeTypes) {%>
                                            <option value="<%=type.F01%>" <%if ((type.F01 + "").equals(request.getParameter("type"))) {%>
                                                    selected="selected" <%} %>><%=type.F02 %>
                                            </option>
                                            <%} %>
                                        </select>
                                    </li>
                                    <li><span class="display-ib mr5">时间： </span>
                                        <input type="text" name="startPayTime" readonly="readonly" id="datepicker1"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("startPayTime")); %>"
                                               class="text border pl5 w120 date"/>
                                        <span class="pl5 pr5">至</span>
                                        <input readonly="readonly" type="text" name="endPayTime" id="datepicker2"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("endPayTime")); %>"
                                               class="text border pl5 w120 mr20 date"/>
                                    </li>
                                    <li><a href="javascript:void(0);" onclick="$('#searchForm').submit();"
                                           class="btn btn-blue radius-6 mr5 pl1 pr15"><i
                                            class="icon-i w30 h30 va-middle search-icon "></i>搜索</a></li>
                                    <li>
                                        <%if (dimengSession.isAccessableResource(ExportXYJygl.class)) {%>
                                        <a href="javascript:void(0);" onclick="showExport();"
                                           class="btn btn-blue radius-6 mr5  pl1 pr15"><i
                                                class="icon-i w30 h30 va-middle export-icon "></i>导出</a>
                                        <%} else {%>
                                        <span class="btn btn-gray radius-6 mr5 pl1 pr15"><i
                                                class="icon-i w30 h30 va-middle export-icon "></i>导出</span>
                                        <%}%>
                                    </li>
                                    <li><a href="<%=controller.getURI(request, XyList.class)%>"
                                           class="btn btn-blue radius-6 pl20 pr20">返回</a></li>
                                </ul>
                            </div>
                        </div>
                        <div class="border mt20 table-container">
                            <table class="table table-style gray6 tl">
                                <thead>
                                <tr class="title">
                                    <th class="tc">序号</th>
                                    <th class="tc">时间</th>
                                    <th class="tc">类型明细</th>
                                    <th class="tc">收入(元)</th>
                                    <th class="tc">支出(元)</th>
                                    <th class="tc">结余(元)</th>
                                    <th class="tc">备注</th>
                                </tr>
                                </thead>
                                <tbody class="f12">
                                <%
                                    FundsXYJYView[] records = result.getItems();
                                    if (records != null && records.length > 0) {
                                        int i = 1;
                                        for (FundsXYJYView record : records) {
                                            if (record == null) {
                                                continue;
                                            }
                                %>
                                <tr class="tc">
                                    <td><%=i++ %>
                                    </td>
                                    <td><%=DateTimeParser.format(record.F03, "yyyy-MM-dd HH:mm") %>
                                    </td>
                                    <td>
                                        <%StringHelper.filterHTML(out, record.F02); %>
                                    </td>
                                    <td><%=Formater.formatAmount(record.F04) %>
                                    </td>
                                    <td><%=Formater.formatAmount(record.F05) %>
                                    </td>
                                    <td><%=Formater.formatAmount(record.F06) %>
                                    </td>
                                    <td><%StringHelper.filterHTML(out, record.F07); %></td>
                                </tr>
                                <%
                                    }
                                } else {
                                %>
                                <tr>
                                    <td colspan="7" class="tc">暂无数据</td>
                                </tr>
                                <%} %>
                                </tbody>
                            </table>
                        </div>
                        <!--分页-->
                        <%AbstractConsoleServlet.rendPagingResult(out, result); %>
                        <!--分页 end-->
                    </div>
                </form>
            </div>
        </div>
    </div>
    <!--右边内容 结束-->
<%@include file="/WEB-INF/include/datepicker.jsp" %>
<script type="text/javascript">
    function showExport() {
        document.getElementById("searchForm").action = "<%=controller.getURI(request, ExportXYJygl.class)%>";
        $("#searchForm").submit();
        document.getElementById("searchForm").action = "<%=controller.getURI(request, XYjlList.class)%>";
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
        $("#datepicker1").datepicker("setDate", "<%StringHelper.filterHTML(out, request.getParameter("startPayTime"));%>");
        <%}%>
        <%if(!StringHelper.isEmpty(createTimeEnd)){%>
        $("#datepicker2").datepicker("setDate", "<%StringHelper.filterHTML(out, request.getParameter("endPayTime"));%>");
        <%}%>
        $("#datepicker2").datepicker('option', 'minDate', $("#datepicker1").datepicker().val());
    });
</script>
</body>
</html>