<%@ page import="com.dimeng.p2p.console.servlets.account.riskresult.policy.RiskResultList" %>
<%@ page import="com.dimeng.p2p.repeater.policy.query.RiskQueryResult" %>
<%@ page import="com.dimeng.p2p.console.servlets.AbstractConsoleServlet" %>
<%@ page import="com.dimeng.p2p.console.servlets.account.riskresult.policy.RiskResultDetail" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <%@include file="/WEB-INF/include/script.jsp" %>
</head>
<body>
<%
    CURRENT_CATEGORY = "YHGL";
    CURRENT_SUB_CATEGORY = "FXPGJG";
    PagingResult<RiskQueryResult> result = (PagingResult<RiskQueryResult>) request.getAttribute("result");
    RiskQueryResult[] riskResultList = result.getItems();
    String createTimeStart = request.getParameter("createTimeStart");
    String createTimeEnd = request.getParameter("createTimeEnd");
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <form id="form2" action="<%=controller.getURI(request, RiskResultList.class)%>" method="post">
                    <div class="p20">
                        <div class="border">
                            <div class="title-container">
                                <i class="icon-i w30 h30 va-middle title-left-icon"></i>风险评估结果
                            </div>
                            <div class="content-container pl40 pt30 pr40">
                                <ul class="gray6 input-list-container clearfix">
                                    <li><span class="display-ib mr5">用户名</span>
                                        <input type="text" name="name" class="text border pl5 mr20"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("name"));%>"/>
                                    </li>
                                    <li><span class="display-ib mr5">评估时间</span> <input
                                            type="text" name="createTimeStart" readonly="readonly"
                                            id="datepicker1" class="text border pl5 w120 date"/> <span
                                            class="pl5 pr5">至</span> <input type="text"
                                                                            readonly="readonly" name="createTimeEnd"
                                                                            id="datepicker2"
                                                                            class="text border pl5 w120 date mr20"/>
                                    </li>

                                    <li><a href="javascript:$('#form2').submit();"
                                           class="btn btn-blue radius-6 mr5 pl1 pr15"><i
                                            class="icon-i w30 h30 va-middle search-icon "></i>搜索</a></li>

                                </ul>
                            </div>
                        </div>
                            <div class=" table-container mt20">
                                <table class="table table-style gray6 tl">
                                    <thead>
                                    <tr class="title tc">
                                        <th>序号</th>
                                        <th>用户名</th>
                                        <th>真实姓名</th>
                                        <th>评估分数</th>
                                        <th>评估等级</th>
                                        <th>评估时间</th>
                                        <th>操作</th>

                                    </tr>
                                    </thead>
                                    <tbody class="f12">
                                    <%
                                        if (riskResultList != null && riskResultList.length > 0) {
                                            int i = 1;
                                            for (RiskQueryResult riskQueryResult : riskResultList) {
                                                if (riskQueryResult == null) {
                                                    continue;
                                                }

                                    %>
                                    <tr class="tc">
                                        <td><%=i++%>
                                        </td>
                                        <td align="center"><%StringHelper.filterHTML(out, riskQueryResult.userName); %></td>
                                        <td align="center"><%StringHelper.filterHTML(out, riskQueryResult.realName); %></td>
                                        <td align="center"><%=riskQueryResult.score%></td>
                                        <td align="center"><%=riskQueryResult.riskType%></td>
                                        <td align="center"><%=TimestampParser.format(riskQueryResult.time) %>
                                        </td>
                                        <td align="center">
                                            <a
                                               href="<%=controller.getURI(request,RiskResultDetail.class)%>?riskId=<%=riskQueryResult.riskId%>"
                                               class="link-blue">详情</a>
                                        </td>
                                    </tr>
                                    <%
                                        }
                                    } else {
                                    %>
                                    <tr class="tc">
                                        <td colspan="7">暂无数据</td>
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

                    </div>
                </form>
            </div>
        </div>
    </div>

<div id="info"></div>

<%@include file="/WEB-INF/include/datepicker.jsp" %>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/dialog.js"></script>
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
        $("#datepicker1").val("<%StringHelper.filterHTML(out, request.getParameter("createTimeStart"));%>");
        <%}%>
        <%if(!StringHelper.isEmpty(createTimeEnd)){%>
        $("#datepicker2").val("<%StringHelper.filterHTML(out, request.getParameter("createTimeEnd"));%>");
        <%}%>
        $("#datepicker2").datepicker('option', 'minDate', $("#datepicker1").datepicker().val());
    });

</script>

</body>
</html>