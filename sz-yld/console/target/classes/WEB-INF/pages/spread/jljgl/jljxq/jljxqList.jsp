<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.console.servlets.AbstractConsoleServlet" %>
<%@page import="com.dimeng.util.ObjectHelper" %>
<%@ page import="com.dimeng.p2p.console.servlets.spread.hdgl.tyjxq.ExportTyjgl" %>
<%@ page import="com.dimeng.p2p.S62.enums.T6231_F21" %>
<%@ page import="com.dimeng.p2p.console.servlets.spread.jljgl.jljxq.JljxqList" %>
<%@ page import="com.dimeng.p2p.modules.spread.console.service.entity.BonusList" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="com.dimeng.p2p.console.servlets.spread.jljgl.jljxq.ExportJljgl" %>
<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <%@include file="/WEB-INF/include/script.jsp"%>
</head>
<body>
<%
    CURRENT_CATEGORY = "TGGL";
    CURRENT_SUB_CATEGORY = "JLJXQLB";
    PagingResult<BonusList> result = ObjectHelper.convert(request.getAttribute("result"), PagingResult.class);
    BigDecimal total = ObjectHelper.convert(request.getAttribute("total"), BigDecimal.class);
    BonusList getBonusListAmount = (BonusList)request.getAttribute("getBonusListAmount");
    String invalidStartTime = request.getParameter("invalidStartTime");
    String invalidEndTime = request.getParameter("invalidEndTime");
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <form id="form1" action="<%=controller.getURI(request, JljxqList.class)%>" method="post">
                    <div class="p20">
                        <div class="border">
                            <div class="title-container"><i class="icon-i w30 h30 va-middle title-left-icon"></i>奖励金详情列表
                            </div>
                            <div class="content-container pl40 pt30 pr40">
                                <ul class="gray6 input-list-container clearfix">
                                    <li><span class="display-ib mr5">投资奖励金额总计</span>
                                        <span class="link-blue"><%=Formater.formatAmount(total)%></span>元
                                    </li>
                                </ul>
                            </div>
                        </div>
                        <div class="border mt20">
                            <div class="content-container pl40 pt30 pr40">
                                <ul class="gray6 input-list-container clearfix">
                                    <li><span class="display-ib mr5">借款ID</span>
                                        <input type="text" name="loanID" id="loanID" class="text border pl5 mr20"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("loanID"));%>"/>
                                    </li>
                                    <li><span class="display-ib mr5">借款账户</span>
                                        <input type="text" name="loanUserName" id="loanUserName"
                                               class="text border pl5 mr20"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("loanUserName"));%>"/>
                                    </li>
                                    <li><span class="display-ib mr5">投资账户</span>
                                        <input type="text" name="investUserName" id="investUserName"
                                               class="text border pl5 mr20"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("investUserName"));%>"/>
                                    </li>
                                    <li><span class="display-ib mr5">放款时间</span>
                                        <input type="text" name="fkStartTime" readonly="readonly" id="datepicker3"
                                               class="text border pl5 w120 date"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("fkStartTime"));%>"/>
                                        <span class="pl5 pr5">至</span>
                                        <input type="text" name="fkEndTime" readonly="readonly" id="datepicker4"
                                               class="text border pl5 mr20 w120 date"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("fkEndTime"));%>"/>
                                    </li>
                                    <li><a href="javascript:$('#form1').submit();"
                                           class="btn btn-blue radius-6 mr5 pl1 pr15"><i
                                            class="icon-i w30 h30 va-middle search-icon "></i>搜索</a></li>

                                    <li>
                                        <%if (dimengSession.isAccessableResource(ExportJljgl.class)) {%>
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
                                <tr class="tc title">
                                    <th>序号</th>
                                    <th>借款ID</th>
                                    <th>借款账户</th>
                                    <th>借款金额</th>
                                    <th>期限</th>
                                    <th>年化利率</th>
                                    <th>放款时间</th>
                                    <th>投资账户</th>
                                    <th>投资金额(元)</th>
                                    <th>奖励利率</th>
                                    <th>奖励金额(元)</th>
                                </tr>
                                </thead>
                                <tbody class="f12">
                                <%
                                    if (result != null && result.getItemCount() > 0) {
                                        BonusList[] lists = result.getItems();
                                        if (lists != null) {
                                            int index = 1;
                                            for (BonusList list : lists) {
                                %>
                                <tr class="tc">
                                    <td><%=index++%>
                                    </td>
                                    <td><%StringHelper.filterHTML(out, list.F25); %></td>
                                    <td><%StringHelper.filterHTML(out, list.loanUserName); %></td>
                                    <td><%=Formater.formatAmount(list.F05)%>
                                    </td>
                                    <%
                                        if (T6231_F21.F.name().equalsIgnoreCase(list.day)) {
                                    %>
                                    <td><%=list.total%>个月</td>
                                    <%
                                    } else {
                                    %>
                                    <td><%=list.total%> 天</td>
                                    <%
                                        }
                                    %>
                                    <td><%=Formater.formatRate(list.F06)%>
                                    </td>
                                    <td><%=TimestampParser.format(list.fkTime, "yyyy-MM-dd HH:mm:ss")%>
                                    </td>
                                    <td><%StringHelper.filterHTML(out, list.investUserName); %></td>
                                    <td><%=Formater.formatAmount(list.investAmount)%>
                                    </td>
                                    <td><%=Formater.formatRate(list.jlRate)%>
                                    </td>
                                    <td><%=Formater.formatAmount(list.jlAmount)%>
                                    </td>
                                </tr>
                                <%
                                        }
                                    }
                                } else {
                                %>
                                <tr class="tc">
                                    <td colspan="11">暂无数据</td>
                                </tr>
                                <%} %>
                                </tbody>
                            </table>
                        </div>
						<p class="mt5">
                            <span class="mr30">投资总金额：<em class="red"><%=Formater.formatAmount(getBonusListAmount.investAmount)%>
                            </em> 元</span>
                            <span class="mr30">奖励总金额：<em class="red"><%=Formater.formatAmount(getBonusListAmount.jlAmount)%>
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
        <%if(!StringHelper.isEmpty(invalidStartTime)){%>
        $("#datepicker3").datepicker("setDate", "<%StringHelper.filterHTML(out, request.getParameter("invalidStartTime"));%>");
        <%}%>
        <%if(!StringHelper.isEmpty(invalidEndTime)){%>
        $("#datepicker4").datepicker("setDate", "<%StringHelper.filterHTML(out, request.getParameter("invalidEndTime"));%>");
        <%}%>
        $("#datepicker4").datepicker('option', 'minDate', $("#datepicker3").datepicker().val());

    });
    function exportList() {
        var del_url = '<%=controller.getURI(request, ExportJljgl.class)%>';
        var form = document.forms[0];
        form.action = del_url;
        form.submit();
        form.action = '<%=controller.getURI(request, JljxqList.class)%>';
    }
</script>
</body>
</html>