<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="java.math.BigDecimal"%>
<%@page import="com.dimeng.p2p.console.servlets.AbstractConsoleServlet" %>
<%@page import="com.dimeng.p2p.console.servlets.spread.tgjl.tgxq.ExportTgxq" %>
<%@page import="com.dimeng.p2p.modules.spread.console.service.entity.SpreadDetailList" %>
<%@page import="com.dimeng.p2p.console.servlets.spread.tgjl.tgxq.SearchTgxq" %>
<%@page import="com.dimeng.util.ObjectHelper" %>
<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <%@include file="/WEB-INF/include/script.jsp"%>
</head>
<body>
<%
    CURRENT_CATEGORY = "TGGL";
    CURRENT_SUB_CATEGORY = "TGXQ";
    PagingResult<SpreadDetailList> result = ObjectHelper.convert(request.getAttribute("result"), PagingResult.class);
    BigDecimal spreadRewardMoney = (BigDecimal)request.getAttribute("spreadRewardMoney");
    String createTimeStart = request.getParameter("startTime");
    String createTimeEnd = request.getParameter("endTime");
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <form id="form1" action="<%=controller.getURI(request, SearchTgxq.class)%>" method="post">
                    <div class="p20">
                        <div class="border">
                            <div class="title-container"><i class="icon-i w30 h30 va-middle title-left-icon"></i>推广详情列表
                            </div>
                            <div class="content-container pl40 pt30 pr40">
                                <ul class="gray6 input-list-container clearfix">
                                    <li><span class="display-ib mr5">推广人ID</span>
                                        <input type="text" name="id" id="textfield" class="text border pl5 mr20" maxlength="9"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("id"));%>" onkeyup="value=value.replace(/\D/g,'')"/>
                                    </li>
                                    <li><span class="display-ib mr5">推广人用户名</span>
                                        <input type="text" name="userName" id="textfield4" class="text border pl5 mr20"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("userName"));%>"/>
                                    </li>
                                    <li><span class="display-ib mr5">推广人姓名</span>
                                        <input type="text" name="name" id="textfield4" class="text border pl5 mr20"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("name"));%>"/>
                                    </li>
                                    <li><span class="display-ib mr5">被推广人ID</span>
                                        <input type="text" name="personID" id="textfield" class="text border pl5 mr20" maxlength="9"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("personID")); %>" onkeyup="value=value.replace(/\D/g,'')"/>
                                    </li>
                                    <li><span class="display-ib mr5">被推广人用户名</span>
                                        <input type="text" name="personName" id="textfield" class="text border pl5 mr20"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("personName")); %>"/>
                                    </li>
                                    <li><span class="display-ib mr5">被推广人姓名</span>
                                        <input type="text" name="personUserName" id="textfield"
                                               class="text border pl5 mr20"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("personUserName")); %>"/>
                                    </li>
                                    <li><span class="display-ib mr5">首次充值时间</span>
                                        <input type="text" name="startTime" readonly="readonly" id="datepicker1"
                                               class="text border pl5 w120 date"/>
                                        <span class="pl5 pr5">至</span>
                                        <input readonly="readonly" type="text" name="endTime" id="datepicker2"
                                               class="text border pl5 w120 mr20 date"/>
                                    </li>
                                    <li><a href="javascript:$('#form1').submit();"
                                           class="btn btn-blue radius-6 mr5 pl1 pr15"><i
                                            class="icon-i w30 h30 va-middle search-icon "></i>搜索</a></li>
                                    <li>
                                        <%if (dimengSession.isAccessableResource(ExportTgxq.class)) {%>
                                        <a href="javascript:void(0)" onclick="exportList()"
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
                                <tr class="title tc">
                                    <th>序号</th>
                                    <th>推广人ID</th>
                                    <th>推广人用户名</th>
                                    <th>推广人姓名</th>
                                    <th>被推广人ID</th>
                                    <th>被推广人用户名</th>
                                    <th>被推广人姓名</th>
                                    <th>首次充值金额(元)</th>
                                    <th>首次充值时间</th>
                                    <th>有效推广奖励(元)</th>
                                </tr>
                                </thead>
                                <tbody class="f12">
                                <% SpreadDetailList[] lists = result.getItems();
                                    if (lists != null) {
                                        int index = 1;
                                        for (SpreadDetailList list : lists) {
                                %>
                                <tr class="tc">
                                    <td><%=index++ %>
                                    </td>
                                    <td><%=list.id %>
                                    </td>
                                    <td><%StringHelper.filterHTML(out, list.userName); %></td>
                                    <td><%StringHelper.filterHTML(out, list.name); %></td>
                                    <td><%=list.personID %>
                                    </td>
                                    <td><%StringHelper.filterHTML(out, list.personUserName); %></td>
                                    <td><%StringHelper.filterHTML(out, list.personName); %></td>
                                    <td><%=list.firstMoney %>
                                    </td>
                                    <td><%=DateTimeParser.format(list.firstTime) %>
                                    </td>
                                    <td><%=list.spreadRewardMoney %>
                                    </td>
                                </tr>
                                <%
                                    }
                                } else {
                                %>
                                <tr class="tc">
                                    <td colspan="10">暂无数据</td>
                                </tr>
                                <%} %>
                                </tbody>
                            </table>
                        </div>
                        <p class="mt5">
                            <span class="mr30">有效推广奖励总金额：<em class="red"><%=Formater.formatAmount(spreadRewardMoney)%>
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
        $("#datepicker1").datepicker("setDate", "<%StringHelper.filterHTML(out, request.getParameter("startTime"));%>");
        <%}%>
        <%if(!StringHelper.isEmpty(createTimeEnd)){%>
        $("#datepicker2").datepicker("setDate", "<%StringHelper.filterHTML(out, request.getParameter("endTime"));%>");
        <%}%>
        $("#datepicker2").datepicker('option', 'minDate', $("#datepicker1").datepicker().val());
    });
</script>
<script type="text/javascript">
    function exportList() {
        var del_url = '<%=controller.getURI(request, ExportTgxq.class) %>';
        var form = document.forms[0];
        form.action = del_url;
        form.submit();
        form.action = '<%=controller.getURI(request, SearchTgxq.class) %>';
    }

</script>
</body>
</html>