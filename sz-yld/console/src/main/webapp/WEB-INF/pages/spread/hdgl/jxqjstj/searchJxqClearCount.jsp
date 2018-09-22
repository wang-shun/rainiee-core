<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.console.servlets.spread.hdgl.jxqjstj.SearchJxqClearCount"%>
<%@page import="com.dimeng.p2p.console.servlets.AbstractConsoleServlet" %>
<%@page import="com.dimeng.p2p.modules.activity.console.service.entity.JxqClearCountEntity" %>
<%@page import="com.dimeng.p2p.modules.activity.console.service.entity.JxqClearAmountTotalInfo" %>
<%@page import="com.dimeng.util.ObjectHelper" %>
<%@page import="com.dimeng.p2p.console.servlets.spread.hdgl.jxqjstj.ExportJxqClearCount" %>
<%@page import="java.math.BigDecimal" %>
<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <%@include file="/WEB-INF/include/script.jsp"%>
</head>
<body>
<%
    CURRENT_CATEGORY = "TGGL";
    CURRENT_SUB_CATEGORY = "JXQJSTJ";
    PagingResult<JxqClearCountEntity> result = ObjectHelper.convert(request.getAttribute("result"), PagingResult.class);
    JxqClearAmountTotalInfo info = ObjectHelper.convert(request.getAttribute("info"), JxqClearAmountTotalInfo.class);
	BigDecimal totalAmount = ObjectHelper.convert(request.getAttribute("totalAmount"), BigDecimal.class);
    String fkStartTime = request.getParameter("fkStartTime");
    String fkEndTime = request.getParameter("fkEndTime");

    String status = ObjectHelper.convert(request.getAttribute("status"), String.class);
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <form id="form1" action="<%=controller.getURI(request, SearchJxqClearCount.class)%>" method="post">
                    <input type="hidden" value="<%=status%>" name="status"/>

                    <div class="p20">
                        <div class="border">
                            <div class="title-container"><i class="icon-i w30 h30 va-middle title-left-icon"></i>加息券结算统计
                            </div>
                            <div class="content-container pl40 pt30 pr40">
                                <ul class="gray6 input-list-container clearfix">
                                    <li><span class="display-ib mr5">待付加息奖励金额总计</span>
                                        <span class="link-blue"><%=info.unPayAmount%></span>元
                                    </li>
                                    <li class="ml50"><span class="display-ib mr5">已付加息奖励金额总计</span>
                                        <span class="link-blue"><%=info.paidAmount%></span>元
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


                                    <li><span class="display-ib mr5">标的ID</span>
                                        <input type="text" name="loanNum" id="loanNum" class="text border pl5 mr20"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("loanNum"));%>"/>
                                    </li>
                                    <li><span
                                            class="display-ib mr5"><%if ("DF".equals(status)) { %>应付<%} else {%>已付<%}%>时间</span>

                                        <input type="text" name="fkStartTime"
                                               readonly="readonly" id="datepicker3"
                                               class="text border pl5 date"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("fkStartTime"));%>"/>
                                        <span class="pl5 pr5">至</span>
                                        <input type="text" name="fkEndTime" readonly="readonly"
                                               id="datepicker4" class="text border pl5 date mr20"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("fkEndTime"));%>"/>
                                    </li>

                                    <li><a href="javascript:$('#form1').submit();"
                                           class="btn btn-blue radius-6 mr5 pl1 pr15"><i
                                            class="icon-i w30 h30 va-middle search-icon "></i>搜索</a></li>

                                    <li>
                                        <%
                                            if (dimengSession.isAccessableResource(ExportJxqClearCount.class)) {
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
                        <div class="border mt20">
                            <div class="tabnav-container">
                                <ul class="clearfix">
                                    <li><a href="<%=controller.getURI(request, SearchJxqClearCount.class)%>?status=DF"
                                           class="tab-btn <%if("DF".equals(status)){%>select-a<%}%>">待付<i class="icon-i tab-arrowtop-icon"></i></a></li>
                                    <li><a href="<%=controller.getURI(request, SearchJxqClearCount.class)%>?status=YF"
                                           class="tab-btn <%if("YF".equals(status)){%>select-a<%}%>">已付<i class="icon-i tab-arrowtop-icon"></i></a></li>
                                </ul>
                            </div>
                            <div class=" table-container p20">
                                <table class="table table-style gray6 tl">
                                    <thead>
                                    <tr class="title tc">
                                        <th>序号</th>
                                        <th>标的ID</th>
                                        <th>用户名</th>
                                        <th>用户姓名</th>
                                        <th>加息利率</th>
                                        <%if ("DF".equals(status)) {%>
                                        <th>应付加息奖励(元)</th>
                                        <th>应付时间</th>
                                        <%} else {%>
                                        <th>已付加息奖励(元)</th>
                                        <th>已付时间</th>
                                        <%}%>
                                    </tr>
                                    </thead>
                                    <tbody class="f12">
                                    <%
                                        if (result != null && result.getItemCount() > 0) {
                                            JxqClearCountEntity[] list = result.getItems();
                                            if (list != null) {
                                                int index = 1;
                                                for (JxqClearCountEntity jcce : list) {
                                    %>
                                    <tr class="tc">
                                        <td><%=index++%>
                                        </td>
                                        <td>
                                            <%
                                                StringHelper.filterHTML(out, jcce.loanNum);
                                            %>
                                        </td>
                                        <td>
                                            <%
                                                StringHelper.filterHTML(out, jcce.userName);
                                            %>
                                        </td>
                                        <td>
                                            <%
                                                StringHelper.filterHTML(out, jcce.realName);
                                            %>
                                        </td>
                                        <td><%=Formater.formatAmount(jcce.interestRate)%>%</td>
                                        <td><%=Formater.formatAmount(jcce.rewardAmount)%>
                                        </td>
                                        <td><%=TimestampParser.format(jcce.payTime, "yyyy-MM-dd")%>
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
                            <span class="mr30">
                            	<%if ("DF".equals(status)) {%>
                            	应付加息奖励总金额：
                            	<%} else {%>
                            	已付加息奖励总金额：
                            	<%} %>
                            	<em class="red"><%=Formater.formatAmount(totalAmount)%>
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
        <%if(!StringHelper.isEmpty(fkStartTime)){%>
        $("#datepicker3").datepicker("setDate", "<%StringHelper.filterHTML(out, request.getParameter("fkStartTime"));%>");
        <%}%>
        <%if(!StringHelper.isEmpty(fkEndTime)){%>
        $("#datepicker4").datepicker("setDate", "<%StringHelper.filterHTML(out, request.getParameter("fkEndTime"));%>");
        <%}%>
        $("#datepicker4").datepicker('option', 'minDate', $("#datepicker3").datepicker().val());
    });

    function exportList() {
        var del_url = '<%=controller.getURI(request, ExportJxqClearCount.class)%>';
        var form = document.forms[0];
        form.action = del_url;
        form.submit();
        form.action = '<%=controller.getURI(request, SearchJxqClearCount.class)%>';
    }
</script>
</body>
</html>