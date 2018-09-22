<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.console.servlets.spread.hdgl.tyjjstj.ExperienceStaticsDH"%>
<%@page import="com.dimeng.p2p.console.servlets.AbstractConsoleServlet" %>
<%@ page import="com.dimeng.p2p.console.servlets.spread.hdgl.tyjjstj.ExperienceStaticsYH" %>
<%@ page import="com.dimeng.p2p.console.servlets.spread.hdgl.tyjjstj.ExportStaticsYH" %>
<%@ page import="com.dimeng.p2p.modules.account.console.experience.service.entity.ExperienceStaticTotal" %>
<%@ page import="com.dimeng.p2p.modules.account.console.experience.service.entity.ExperienceStatistics" %>
<%@ page import="com.dimeng.util.ObjectHelper" %>
<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <%@include file="/WEB-INF/include/script.jsp"%>
</head>
<body>
<%
    CURRENT_CATEGORY = "TGGL";
    CURRENT_SUB_CATEGORY = "TYJTJ";
    PagingResult<ExperienceStatistics> result = ObjectHelper.convert(request.getAttribute("result"), PagingResult.class);
    ExperienceStaticTotal totalInfo = ObjectHelper.convert(request.getAttribute("experienceStaticTotal"), ExperienceStaticTotal.class);
    ExperienceStatistics searchYhAmount = ObjectHelper.convert(request.getAttribute("searchYhAmount"), ExperienceStatistics.class);
    String repaymentStartTime = request.getParameter("repaymentStartTime");
    String repaymentEndTime = request.getParameter("repaymentEndTime");
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <form id="form1" action="<%=controller.getURI(request, ExperienceStaticsYH.class)%>" method="post">
                    <div class="p20">
                        <div class="border">
                            <div class="title-container"><i class="icon-i w30 h30 va-middle title-left-icon"></i>体验金结算统计
                            </div>
                            <div class="content-container pl40 pt30 pr40">
                                <ul class="gray6 input-list-container clearfix">
                                    <li><span class="display-ib mr5">待还利息总计</span>
                                        <span class="link-blue"><%=totalInfo.totalDh%></span>元
                                    </li>
                                    <li class="ml50"><span class="display-ib mr5">已还利息共计</span>
                                        <span class="link-blue"><%=totalInfo.totalYh%></span>元
                                    </li>
                                </ul>
                            </div>
                        </div>
                        <div class="border mt20">
                            <div class="content-container pl40 pt30 pr40">
                                <ul class="gray6 input-list-container clearfix">
                                    <li><span class="display-ib mr5">用户名</span>
                                        <input type="text" name="userName" id="textfield" class="text border pl5 mr20"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("userName"));%>"/>
                                    </li>


                                    <li><span class="display-ib mr5">标编号</span>
                                        <input type="text" name="bid" id="bid" class="text border pl5 mr20"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("bid"));%>"/>
                                    </li>
                                    <li><span class="display-ib mr5">还款日</span>
                                        <input type="text" name="repaymentStartTime" readonly="readonly"
                                               id="datepicker3" class="text border pl5 pr5 date"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("repaymentStartTime"));%>"/>
                                        <span class="pr5">至</span>
                                        <input type="text" name="repaymentEndTime" readonly="readonly"
                                               id="datepicker4" class="text border pl5 mr20 date"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("repaymentEndTime"));%>"/>
                                    </li>

                                    <li><a href="javascript:$('#form1').submit();"
                                           class="btn btn-blue radius-6 mr5 pl1 pr15"><i
                                            class="icon-i w30 h30 va-middle search-icon "></i>搜索</a></li>

                                    <li>
                                        <%
                                            if (dimengSession.isAccessableResource(ExportStaticsYH.class)) {
                                        %>
                                        <a href="javascript:void(0)" onclick="showExport()"
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
                        <div class="border mt20">
                            <div class="tabnav-container">
                                <ul class="clearfix">
                                    <li><a href="<%=controller.getURI(request, ExperienceStaticsDH.class)%>"
                                           class="tab-btn">待付</a></li>
                                    <li><a href="javascript:void(0)" class="tab-btn select-a">已付<i class="icon-i tab-arrowtop-icon"></i></a></li>
                                </ul>
                            </div>
                            <div class=" table-container p20">
                                <table class="table table-style gray6 tl">
                                    <thead>
                                    <tr class="title tc">
                                        <th>序号</th>
                                        <th>标编号</th>
                                        <th>用户名</th>
                                        <th>姓名</th>
                                        <th>体验金(元)</th>
                                        <th>本期体验金利息(元)</th>
                                        <th>还款时间</th>
                                    </tr>
                                    </thead>
                                    <tbody class="f12">
                                    <%
                                        if (result != null && result.getItemCount() > 0) {
                                            ExperienceStatistics[] lists = result.getItems();
                                            if (lists != null) {
                                                int index = 1;
                                                for (ExperienceStatistics list : lists) {
                                    %>
                                    <tr class="tc">
                                        <td><%=index++%>
                                        </td>
                                        <td><%StringHelper.filterHTML(out, list.bidNo); %></td>
                                        <td><%StringHelper.filterHTML(out, list.accountName); %></td>
                                        <td><%StringHelper.filterHTML(out, list.name); %></td>
                                        <td><%=Formater.formatAmount(list.experience)%>
                                        </td>
                                        <td><%=Formater.formatAmount(list.amount)%>
                                        </td>
                                        <td><%=DateParser.format(list.fhTime)%>
                                        </td>
                                    </tr>
                                    <%
                                            }
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
                        </div>
                        <p class="mt5">
	                        <span class="mr30">体验金总金额：<em class="red"><%=Formater.formatAmount(searchYhAmount.experience)%>
	                        </em> 元</span>
	                        <span class="mr30">已还总金额：<em class="red"><%=Formater.formatAmount(searchYhAmount.amount)%>
	                        </em> 元</span>
	                    </p>
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

        $("#datepicker3").datepicker({
            inline: true,
            onSelect: function (selectedDate) {
                $("#datepicker4").datepicker("option", "minDate", selectedDate);
            }
        });
        $('#datepicker3').datepicker('option', {dateFormat: 'yy-mm-dd'});
        $("#datepicker4").datepicker({inline: true});
        $('#datepicker4').datepicker('option', {dateFormat: 'yy-mm-dd'});
        <%if(!StringHelper.isEmpty(repaymentStartTime)){%>
        $("#datepicker3").datepicker("setDate", "<%StringHelper.filterHTML(out, request.getParameter("repaymentStartTime"));%>");
        <%}%>
        <%if(!StringHelper.isEmpty(repaymentEndTime)){%>
        $("#datepicker4").datepicker("setDate", "<%StringHelper.filterHTML(out, request.getParameter("repaymentEndTime"));%>");
        <%}%>
        $("#datepicker4").datepicker('option', 'minDate', $("#datepicker3").datepicker().val());

    });
    function showExport() {
        var del_url = '<%=controller.getURI(request, ExportStaticsYH.class)%>';
        var form = document.forms[0];
        form.action = del_url;
        form.submit();
        form.action = '<%=controller.getURI(request, ExperienceStaticsYH.class)%>';
    }

</script>
</body>
</html>