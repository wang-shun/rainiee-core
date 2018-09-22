<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.console.servlets.statistics.tjbb.hktj.AlreadyStatistics"%>
<%@page import="com.dimeng.p2p.console.servlets.statistics.tjbb.hktj.PaymentAccountStatistics"%>
<%@page import="com.dimeng.p2p.console.servlets.statistics.tjbb.hktj.ExportOverdueStatistics"%>
<%@page import="com.dimeng.p2p.console.servlets.statistics.tjbb.hktj.OverdueStatistics"%>
<%@page import="com.dimeng.p2p.console.servlets.statistics.tjbb.hktj.RepaymentStatistics"%>
<%@page import="com.dimeng.p2p.FeeCode" %>
<%@page import="com.dimeng.p2p.S62.enums.T6252_F09" %>
<%@page import="java.math.BigDecimal" %>
<%@page import="com.dimeng.p2p.console.servlets.statistics.tjbb.hktj.ExportRepaymentStatistics" %>
<%@page import="com.dimeng.p2p.modules.statistics.console.service.entity.RepaymentStatisticsEntity" %>
<%@page import="com.dimeng.util.ObjectHelper" %>
<%@page import="com.dimeng.util.Formater" %>
<%@page import="com.dimeng.p2p.console.servlets.AbstractConsoleServlet" %>
<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <%@include file="/WEB-INF/include/script.jsp"%>
</head>
<body>
<%
    CURRENT_CATEGORY = "TJGL";
    CURRENT_SUB_CATEGORY = "HKTJB";
    PagingResult<RepaymentStatisticsEntity> result = ObjectHelper.convert(request.getAttribute("result"), PagingResult.class);
    RepaymentStatisticsEntity total = (RepaymentStatisticsEntity)request.getAttribute("overdueTotal");
    RepaymentStatisticsEntity[] repayments = result.getItems();
    String shouldTheDateStart = request.getParameter("shouldTheDateStart");
    String shouldTheDateEnd = request.getParameter("shouldTheDateEnd");

%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <form id="form_loan" action="<%=controller.getURI(request, OverdueStatistics.class)%>" method="post">
                    <div class="p20">
                        <div class="border">
                            <div class="title-container"><i class="icon-i w30 h30 va-middle title-left-icon"></i>逾期待还统计表
                            </div>
                            <div class="content-container pl40 pt30 pr40">
                                <ul class="gray6 input-list-container clearfix">
                                    <li><span class="display-ib mr5">标的ID： </span>
                                        <input type="text" name="id"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("id"));%>"
                                               class="text border pl5 mr20"/>
                                    </li>
                                    <li><span class="display-ib mr5">借款账户： </span>
                                        <input type="text" name="loanAccount"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("loanAccount"));%>"
                                               class="text border pl5 mr20"/>
                                    </li>
                                    <li><span class="display-ib mr5">逾期天数： </span>
                                        <input type="text" name="yuqiFromDays" onKeyUp="value=value.replace(/\D/g,'')"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("yuqiFromDays"));%>"
                                               class="text border pl5 w120"/>
                                        <span class="pl5 pr5">至</span>
                                        <input type="text" name="yuqiEndDays" " onKeyUp="value=value.replace(/\D/g,'')"
                                        value="<%StringHelper.filterHTML(out, request.getParameter("yuqiEndDays"));%>"
                                        class="text border pl5 w120 mr20" />
                                    </li>
                                    <li><span class="display-ib mr5">合约还款日期： </span>
                                        <input type="text" name="shouldTheDateStart" readonly="readonly"
                                               id="datepicker1" class="text border pl5 w120 date"/>
                                        <span class="pl5 pr5">至</span>
                                        <input readonly="readonly" type="text" name="shouldTheDateEnd" id="datepicker2"
                                               class="text border pl5 w120 mr20 date"/>
                                    </li>
                                    <li><span class="display-ib mr5">账户类型：</span>
                                        <select name="accountType" class="border mr20 h32 mw100">
                                            <option value="">全部</option>
                                            <option value="ZRR" <%if ("ZRR".equals(request.getParameter("accountType"))) {%>
                                                    selected="selected" <%}%>>个人</option>
                                            <option value="FZRR" <%if ("FZRR".equals(request.getParameter("accountType"))) {%>
                                                    selected="selected" <%}%>>企业</option>
                                            <%-- <option value="FZRRJG" <%if ("FZRRJG".equals(request.getParameter("accountType"))) {%>
                                                    selected="selected" <%}%>>机构</option> --%>
                                        </select>
                                    </li>
                                    <li><a href="javascript:void(0)" onclick="search1()"
                                           class="btn btn-blue radius-6 mr5 pl1 pr15"><i
                                            class="icon-i w30 h30 va-middle search-icon "></i>搜索</a></li>
                                    <li>
                                        <%if (dimengSession.isAccessableResource(ExportOverdueStatistics.class)) {%>
                                        <a href="javascript:void(0)" onclick="showExport()"
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
                        <div class="border mt20">
                        	<div class="tabnav-container">
                                <ul class="clearfix">
                                    <li><a href="<%=controller.getURI(request, RepaymentStatistics.class)%>" class="tab-btn">还款中</a></li>
                                    <li><a href="javascript:void(0)" class="tab-btn select-a">逾期待还<i class="icon-i tab-arrowtop-icon"></i></a></li>
                                    <li><a href="<%=controller.getURI(request, PaymentAccountStatistics.class)%>" class="tab-btn ">垫付待还</a></li>
                                    <li><a href="<%=controller.getURI(request, AlreadyStatistics.class)%>" class="tab-btn ">已还</a></li>
                                </ul>
                            </div>
                            <div class=" table-container p20">
                            <table class="table table-style gray6 tl">
                                <thead>
                                <tr class="title">
                                	<th class="tc">序号</th>
                                    <th class="tc">合约还款日期</th>
                                    <th class="tc">逾期天数</th>
                                    <th class="tc">逾期本金(元)</th>
                                    <th class="tc">逾期利息(元)</th>
                                    <th class="tc">逾期罚息(元)</th>
                                    <th class="tc">期数</th>
                                    <th class="tc">借款账户</th>
                                    <th class="tc">账户类型</th>
                                    <th class="tc">借款人姓名</th>
                                    <th class="tc">标的ID</th>
                                </tr>
                                </thead>
                                <tbody class="f12">
                                <%
                                    if (repayments != null && repayments.length > 0) {
                                        for (int i = 0; i < repayments.length; i++) {
                                            RepaymentStatisticsEntity repayment = repayments[i];
                                            if (repayment == null) {
                                                continue;
                                            }
                                %>
                                <tr class="tc">
                                	<td class="tc"><%=i + 1%></td>
                                    <td><%=DateParser.format(repayment.shouldTheDate) %></td>
                                    <td><%=repayment.collectionNumber %></td>
                                    <td><%=Formater.formatAmount(repayment.overdueAmount) %></td>
                                    <td><%=Formater.formatAmount(repayment.overdueInterest) %></td>
                                    <td><%=Formater.formatAmount(repayment.overduePenalty) %></td>
                                    <td><%StringHelper.filterHTML(out, repayment.loandeadline); %></td>
                                    <td><%StringHelper.filterHTML(out, repayment.account); %></td>
                                    <td><%StringHelper.filterHTML(out, repayment.accountType); %></td>
                                    <td><%StringHelper.filterHTML(out, repayment.loanName); %></td>
                                    <td><%StringHelper.filterHTML(out, repayment.id);%></td>
                                </tr>
                                <%
                                    }
                                } else {
                                %>
                                <tr>
                                    <td colspan="11" class="tc">暂无数据</td>
                                </tr>
                                <%} %>
                                </tbody>
                            </table>
                            </div>
                        </div>
                        <div class="mb10">
                            <span class="mr30">逾期本金总金额：<em
                                    class="red"><%=Formater.formatAmount(total.overdueAmount) %>
                            </em> 元</span>
                            <span class="mr30">逾期利息总金额：<em
                                    class="red"><%=Formater.formatAmount(total.overdueInterest) %>
                            </em> 元</span>
                            <span class="mr30">逾期罚息总金额：<em
                                    class="red"><%=Formater.formatAmount(total.overduePenalty) %>
                            </em> 元</span>
                        </div>
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
<%-- <script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/web.js"></script> --%>
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
            if(!StringHelper.isEmpty(shouldTheDateStart))
            {
        %>
        $("#datepicker1").val("<%StringHelper.filterHTML(out, request.getParameter("shouldTheDateStart"));%>");
        <%}%>
        <%
        if(!StringHelper.isEmpty(shouldTheDateEnd))
        {
        %>
        $("#datepicker2").val("<%StringHelper.filterHTML(out, request.getParameter("shouldTheDateEnd"));%>");
        <%}%>

        $("#datepicker2").datepicker('option', 'minDate', $("#datepicker1").datepicker().val());

    });

    function search1() {
        document.getElementsByName("paging.current")[0].value = 1;
        $("#form_loan").submit();
    }

    function showExport() {
        document.getElementById("form_loan").action = "<%=controller.getURI(request, ExportOverdueStatistics.class)%>";
        $("#form_loan").submit();
        document.getElementById("form_loan").action = "<%=controller.getURI(request, OverdueStatistics.class)%>";
    }
</script>
</body>
</html>