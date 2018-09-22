<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.console.servlets.spread.hdgl.tyjtj.ExportExpCount"%>
<%@page import="com.dimeng.p2p.console.servlets.spread.hdgl.tyjtj.SearchExpCount"%>
<%@page import="com.dimeng.p2p.console.servlets.AbstractConsoleServlet" %>
<%@page import="com.dimeng.p2p.modules.activity.console.service.entity.ActivityCountEntity" %>
<%@page import="com.dimeng.util.ObjectHelper" %>
<%@ page import="com.dimeng.p2p.modules.account.console.experience.service.entity.ExperienceTotalInfo" %>
<%@ page import="com.dimeng.p2p.S61.enums.T6103_F06" %>
<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <%@include file="/WEB-INF/include/script.jsp"%>
</head>
<body>
<%
    CURRENT_CATEGORY = "TGGL";
    CURRENT_SUB_CATEGORY = "EXPTJ";
    PagingResult<ActivityCountEntity> result = ObjectHelper.convert(request.getAttribute("result"), PagingResult.class);
    ExperienceTotalInfo totalInfo = ObjectHelper.convert(request.getAttribute("totalInfo"), ExperienceTotalInfo.class);
    //赠送时间
    String presentStartTime = request.getParameter("presentStartTime");
    String presentEndTime = request.getParameter("presentEndTime");
    //过期时间
    String outOfStartTime = request.getParameter("outOfStartTime");
    String outOfEndTime = request.getParameter("outOfEndTime");
    //使用时间
    String useStartTime = request.getParameter("useStartTime");
    String useEndTime = request.getParameter("useEndTime");
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <form id="form1" action="<%=controller.getURI(request, SearchExpCount.class)%>" method="post">
                    <div class="p20">
                        <div class="border">
                            <div class="title-container"><i class="icon-i w30 h30 va-middle title-left-icon"></i>体验金统计
                            </div>
                            <div class="content-container pl40 pt30 pr40">
                                <ul class="gray6 input-list-container clearfix">
                                    <li><span class="display-ib mr5">已使用体验金总计</span>
                                        <span class="link-blue"><%=Formater.formatAmount(totalInfo.totalUsedMoney)%></span>元
                                    </li>
                                    <li class="ml50"><span class="display-ib mr5">体验金投资利息总计</span>
                                        <span class="link-blue"><%=Formater.formatAmount(totalInfo.returnMoney)%></span>元
                                    </li>
                                </ul>
                            </div>
                        </div>
                        <div class="border mt20">
                            <div class="content-container pl40 pt30 pr40">
                                <ul class="gray6 input-list-container clearfix">
                                    <li><span class="display-ib mr5">用户名</span>
                                        <input type="text" name="userName" id="userName" class="text border pl5 mr20"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("userName"));%>"/>
                                    </li>
                                    <li><span class="display-ib mr5">体验金状态</span>
                                        <select name="status" class="border mr20 h32 mw100">
                                            <%String status = request.getParameter("status"); %>
                                            <option value="">全部</option>
                                            <%for (T6103_F06 s : T6103_F06.values()) { %>
                                            <option value="<%=s.name() %>"
                                                    <%if (!StringHelper.isEmpty(status) && status.equals(s.name())) { %>
                                                    selected="selected" <%} %>><%=s.getChineseName() %>
                                            </option>
                                            <%}%>
                                        </select>
                                    </li>
                                    <li><span class="display-ib mr5">活动ID</span>
                                        <input type="text" name="activityNum" id="activityNum"
                                               class="text border pl5 mr20"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("activityNum"));%>"/>
                                    </li>
                                    <li><span class="display-ib mr5">标的ID</span>
                                        <input type="text" name="loanNum" id="loanNum" class="text border pl5 mr20"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("loanNum"));%>"/>
                                    </li>
                                    <li><span class="display-ib mr5">赠送时间</span>

                                        <input type="text" name="presentDateBegin" readonly="readonly" id="datepicker1"
                                               class="text border pl5 date"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("presentDateBegin"));%>"/>
                                        <span class="pl5 pr5">至</span>
                                        <input type="text" name="presentDateEnd" readonly="readonly" id="datepicker2"
                                               class="text border pl5 date"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("presentDateEnd"));%>"/>
                                    </li>
                                    <li><span class="display-ib mr5">过期时间</span>

                                        <input type="text" name="outOfDateBegin" readonly="readonly" id="datepicker3"
                                               class="text border pl5 date"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("outOfDateBegin"));%>"/>
                                        <span class="pl5 pr5">至</span>
                                        <input type="text" name="outOfDateEnd" readonly="readonly" id="datepicker4"
                                               class="text border pl5 date mr20"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("outOfDateEnd"));%>"/>
                                    </li>
                                    <li><span class="display-ib mr5">使用时间</span>

                                        <input type="text" name="useDateBegin" readonly="readonly" id="datepicker5"
                                               class="text border pl5 date"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("useDateBegin"));%>"/>
                                        <span class="pl5 pr5">至</span>
                                        <input type="text" name="useDateEnd" readonly="readonly" id="datepicker6"
                                               class="text border pl5 date mr20"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("useDateEnd"));%>"/>
                                    </li>

                                    <li><a href="javascript:$('#form1').submit();"
                                           class="btn btn-blue radius-6 mr5 pl1 pr15"><i
                                            class="icon-i w30 h30 va-middle search-icon "></i>搜索</a></li>

                                    <li>
                                        <%
                                            if (dimengSession.isAccessableResource(ExportExpCount.class)) {
                                        %>
                                        <a href="javascript:void(0)" onclick="exportList()"
                                           class="btn btn-blue radius-6 mr5  pl1 pr15"><i
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
                        <div class="border mt20 table-container">
                            <table class="table table-style gray6 tl">
                                <thead>
                                <tr class="title tc">
                                    <th>序号</th>
                                    <th>用户名</th>
                                    <th>用户姓名</th>
                                    <th>体验金金额(元)</th>
                                    <th>赠送时间</th>
                                    <th>过期时间</th>
                                    <th>活动ID</th>
                                    <th>活动名称</th>
                                    <th>活动类型</th>
                                    <th>预计收益期</th>
                                    <th>状态</th>
                                    <th>使用时间</th>
                                    <th>标的ID</th>
                                </tr>
                                </thead>
                                <tbody class="f12">
                                <%
                                    if (result != null && result.getItemCount() > 0) {
                                        ActivityCountEntity[] list = result.getItems();
                                        if (list != null) {
                                            int index = 1;
                                            for (ActivityCountEntity ace : list) {
                                %>
                                <tr class="tc">
                                    <td><%=index++%>
                                    </td>
                                    <td><%StringHelper.filterHTML(out, ace.userName); %></td>
                                    <td><%StringHelper.filterHTML(out, ace.realName); %></td>
                                    <td><%=Formater.formatAmount(ace.interestRate)%>
                                    </td>
                                    <td><%=TimestampParser.format(ace.presentDate, "yyyy-MM-dd HH:mm")%>
                                    </td>
                                    <td><%=TimestampParser.format(ace.outOfDate, "yyyy-MM-dd")%>
                                    </td>
                                    <td><%StringHelper.filterHTML(out, ace.activityNum); %></td>
                                    <td><%StringHelper.filterHTML(out, ace.activityName);%></td>
                                    <td><%StringHelper.filterHTML(out, ace.activityType.getChineseName());%></td>
                                    <td><%StringHelper.filterHTML(out, ace.expectedTerm);%></td>
                                    <td><%StringHelper.filterHTML(out, ace.expStatus.getChineseName());%></td>
                                    <td><%=TimestampParser.format(ace.useDate)%>
                                    </td>
                                    <td><%StringHelper.filterHTML(out, ace.loanNum);%></td>
                                </tr>
                                <%
                                        }
                                    }
                                } else {
                                %>
                                <tr class="tc">
                                    <td colspan="12">暂无数据</td>
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
        <%if(!StringHelper.isEmpty(presentStartTime)){%>
        $("#datepicker1").datepicker("setDate", "<%StringHelper.filterHTML(out, request.getParameter("presentStartTime"));%>");
        <%}%>
        <%if(!StringHelper.isEmpty(presentEndTime)){%>
        $("#datepicker2").datepicker("setDate", "<%StringHelper.filterHTML(out, request.getParameter("presentEndTime"));%>");
        <%}%>
        $("#datepicker2").datepicker('option', 'minDate', $("#datepicker1").datepicker().val());

        $("#datepicker3").datepicker({
            inline: true,
            onSelect: function (selectedDate) {
                $("#datepicker4").datepicker("option", "minDate", selectedDate);
            }
        });
        $('#datepicker3').datepicker('option', {dateFormat: 'yy-mm-dd'});
        $("#datepicker4").datepicker({inline: true});
        $('#datepicker4').datepicker('option', {dateFormat: 'yy-mm-dd'});
        <%if(!StringHelper.isEmpty(outOfStartTime)){%>
        $("#datepicker3").datepicker("setDate", "<%StringHelper.filterHTML(out, request.getParameter("outOfStartTime"));%>");
        <%}%>
        <%if(!StringHelper.isEmpty(outOfEndTime)){%>
        $("#datepicker4").datepicker("setDate", "<%StringHelper.filterHTML(out, request.getParameter("outOfEndTime"));%>");
        <%}%>
        $("#datepicker4").datepicker('option', 'minDate', $("#datepicker3").datepicker().val());

        $("#datepicker5").datepicker({
            inline: true,
            onSelect: function (selectedDate) {
                $("#datepicker6").datepicker("option", "minDate", selectedDate);
            }
        });
        $('#datepicker5').datepicker('option', {dateFormat: 'yy-mm-dd'});
        $("#datepicker6").datepicker({inline: true});
        $('#datepicker6').datepicker('option', {dateFormat: 'yy-mm-dd'});
        <%if(!StringHelper.isEmpty(useStartTime)){%>
        $("#datepicker5").datepicker("setDate", "<%StringHelper.filterHTML(out, request.getParameter("useStartTime"));%>");
        <%}%>
        <%if(!StringHelper.isEmpty(useEndTime)){%>
        $("#datepicker6").datepicker("setDate", "<%StringHelper.filterHTML(out, request.getParameter("useEndTime"));%>");
        <%}%>
        $("#datepicker6").datepicker('option', 'minDate', $("#datepicker5").datepicker().val());
    });

    function exportList() {
        var del_url = '<%=controller.getURI(request, ExportExpCount.class)%>';
        var form = document.forms[0];
        form.action = del_url;
        form.submit();
        form.action = '<%=controller.getURI(request, SearchExpCount.class)%>';
    }
</script>
</body>
</html>