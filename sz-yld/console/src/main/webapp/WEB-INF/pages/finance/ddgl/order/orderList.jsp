<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.console.servlets.finance.ddgl.order.OrderList"%>
<%@page import="com.dimeng.p2p.OrderType" %>
<%@page import="com.dimeng.p2p.console.servlets.finance.AbstractFinanceServlet" %>
<%@page import="com.dimeng.p2p.console.servlets.finance.ddgl.order.ExportOrder" %>
<%@page import="com.dimeng.p2p.modules.account.console.service.entity.Order" %>
<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <%@include file="/WEB-INF/include/script.jsp"%>
</head>
<%
    PagingResult<Order> result = (PagingResult<Order>) request
            .getAttribute("result");
%>
<%
    CURRENT_CATEGORY = "CWGL";
    CURRENT_SUB_CATEGORY = "DDGL";
    String createTimeStart = request.getParameter("createStart");
    String createTimeEnd = request.getParameter("endStart");
%>
<body>
<div class="right-container">
    <div class="viewFramework-body">
        <div class="viewFramework-content">
            <form id="searchForm" name="form1" action="<%=controller.getURI(request, OrderList.class)%>"
                  method="post">
                <div class="p20">
                    <div class="border">
                        <div class="title-container"><i class="icon-i w30 h30 va-middle title-left-icon"></i>订单管理
                        </div>
                        <div class="content-container pl40 pt30 pr40">
                            <ul class="gray6 input-list-container clearfix">
                                <li><span class="display-ib mr5">订单ID： </span>
                                    <input type="text" name="orderId"
                                           value="<%=!StringHelper.isEmpty(request.getParameter("orderId")) ? request.getParameter("orderId") : "" %>"
                                           class="text border pl5 mr20"/>
                                </li>
                                <li><span class="display-ib mr5">用户名： </span>
                                    <input type="text" name="userName"
                                           value="<%=!StringHelper.isEmpty(request.getParameter("userName")) ? request.getParameter("userName") : "" %>"
                                           class="text border pl5 mr20"/>
                                </li>
                                <li><span class="display-ib mr5">类型： </span>
                                    <select name="type" class="border mr20 h32 mw100">
                                        <option value="">全部</option>
                                        <%
                                            for (OrderType orderType : OrderType.values()) {
                                                if (orderType.orderType() == 20014) {
                                                    continue;
                                                }
                                        %>
                                        <option value="<%=orderType.orderType()%>" <%if (orderType.orderType() == IntegerParser.parse(request.getParameter("type"))) {%>
                                                selected="selected" <%} %>><%=orderType.getChineseName() %>
                                        </option>
                                        <%} %>
                                    </select>
                                </li>
                                <li><span class="display-ib mr5">创建时间： </span>
                                    <input type="text" name="createStart" readonly="readonly" id="datepicker1"
                                           class="text border pl5 w120 date"/>
                                    <span class="pl5 pr5">至</span>
                                    <input readonly="readonly" type="text" name="endStart" id="datepicker2"
                                           class="text border pl5 w120 mr20 date"/>
                                </li>
                                <li><a href="javascript:void(0);" onclick="$('#searchForm').submit();"
                                       class="btn btn-blue radius-6 mr5 pl1 pr15"><i
                                        class="icon-i w30 h30 va-middle search-icon "></i>搜索</a></li>
                                <li>
                                    <%if (dimengSession.isAccessableResource(ExportOrder.class)) {%>
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
                    <div class="border mt20 table-container">
                        <table class="table table-style gray6 tl">
                            <thead>
                            <tr class="title">
                                <th class="tc">序号</th>
                                <th class="tc">订单ID</th>
                                <th class="tc">用户名</th>
                                <th class="tc">类型</th>
                                <th class="tc">金额(元)</th>
                                <th class="tc">状态</th>
                                <th class="tc">创建时间</th>
                                <th class="tc">提交时间</th>
                                <th class="tc">完成时间</th>
                                <th class="tc">订单来源</th>
                            </tr>
                            </thead>
                            <tbody class="f12">
                            <%
                                Order[] items = result.getItems();
                                if (items != null && items.length > 0) {
                                    int i = 1;
                                    for (Order order : items) {
                                        if (order == null) {
                                            continue;
                                        }
                            %>
                            <tr class="tc">
                                <td><%=i++%>
                                </td>
                                <td><%=order.F01 %>
                                </td>
                                <td><%StringHelper.filterHTML(out, order.userName); %></td>
                                <td><%=OrderType.getTypeName(order.F02) == null ? "" : OrderType.getTypeName(order.F02) %>
                                </td>
                                <td><%=Formater.formatAmount(order.amount) %>
                                </td>
                                <td><%=order.F03.getChineseName()%>
                                </td>
                                <td><%=DateTimeParser.format(order.F04)%>
                                </td>
                                <td><%=DateTimeParser.format(order.F05)%>
                                </td>
                                <td><%=DateTimeParser.format(order.F06)%>
                                </td>
                                <td><%=order.F07.getChineseName()%>
                                </td>
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
                    <!--分页-->
                    <%AbstractFinanceServlet.rendPagingResult(out, result);%>
                    <!--分页 end-->
                </div>
            </form>
        </div>
    </div>
</div>
<!--右边内容 结束-->
<%@include file="/WEB-INF/include/jquery.jsp" %>
<%@include file="/WEB-INF/include/datepicker.jsp" %>
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
        <%if(!StringHelper.isEmpty(createTimeStart)){%>
        $("#datepicker1").datepicker("setDate", "<%StringHelper.filterHTML(out, request.getParameter("createStart"));%>");
        <%}%>
        <%if(!StringHelper.isEmpty(createTimeEnd)){%>
        $("#datepicker2").datepicker("setDate", "<%StringHelper.filterHTML(out, request.getParameter("endStart"));%>");
        <%}%>
        $("#datepicker2").datepicker('option', 'minDate', $("#datepicker1").datepicker().val());
    });
    function showExport() {
        document.getElementById("searchForm").action = "<%=controller.getURI(request, ExportOrder.class)%>";
        $("#searchForm").submit();
        document.getElementById("searchForm").action = "<%=controller.getURI(request, OrderList.class)%>";
    }
</script>
</body>
</html>