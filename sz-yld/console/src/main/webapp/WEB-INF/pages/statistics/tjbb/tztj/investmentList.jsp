<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.console.servlets.statistics.tjbb.tztj.InvestmentList"%>
<%@page import="java.math.BigDecimal" %>
<%@page import="com.dimeng.p2p.console.servlets.statistics.tjbb.tztj.ExportInvestmentList" %>
<%@page import="com.dimeng.p2p.modules.statistics.console.service.entity.InvestmentListEntity" %>
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
    CURRENT_SUB_CATEGORY = "TZTJB";
    PagingResult<InvestmentListEntity> result = ObjectHelper.convert(request.getAttribute("result"), PagingResult.class);
    BigDecimal total = ObjectHelper.convert(request.getAttribute("investmentTotal"), BigDecimal.class);
    InvestmentListEntity[] loans = result.getItems();
    String loanTimeStart = request.getParameter("loanTimeStart");
    String loanTimeEnd = request.getParameter("loanTimeEnd");
    String finishTimeStart = request.getParameter("finishTimeStart");
    String finishTimeEnd = request.getParameter("finishTimeEnd");
    //托管前缀
    String ESCROW_PREFIX = configureProvider.getProperty(SystemVariable.ESCROW_PREFIX);
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <form id="form_loan" action="<%=controller.getURI(request, InvestmentList.class)%>" method="post">
                    <div class="p20">
                        <div class="border">
                            <div class="title-container"><i class="icon-i w30 h30 va-middle title-left-icon"></i>投资统计表
                            </div>
                            <div class="content-container pl40 pt30 pr40">
                                <ul class="gray6 input-list-container clearfix">
                                    <li><span class="display-ib mr5">标的ID： </span>
                                        <input type="text" name="id"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("id"));%>"
                                               class="text border pl5 mr20"/>
                                    </li>
                                    <li><span class="display-ib mr5">投资人账户： </span>
                                        <input type="text" name="investAccoun"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("investAccoun"));%>"
                                               class="text border pl5 mr20"/>
                                    </li>
                                    <li><span class="display-ib mr5">放款时间： </span>
                                        <input type="text" name="loanTimeStart" readonly="readonly" id="datepicker1"
                                               class="text border pl5 w120 date"/>
                                        <span class="pl5 pr5">至</span>
                                        <input readonly="readonly" type="text" name="loanTimeEnd" id="datepicker2"
                                               class="text border pl5 w120 mr20 date"/>
                                    </li>
                                    <li><span class="display-ib mr5">完结日期： </span>
                                        <input type="text" name="finishTimeStart" readonly="readonly" id="datepicker3"
                                               class="text border pl5 w120 date"/>
                                        <span class="pl5 pr5">至</span>
                                        <input readonly="readonly" type="text" name="finishTimeEnd" id="datepicker4"
                                               class="text border pl5 w120 mr20 date"/>
                                    </li>
                                    <li><span class="display-ib mr5">投资金额范围： </span>
                                        <input type="text" name="investPriceStart" id="investPriceStart"
                                               onKeyUp="value=value.replace(/\D/g,'')"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("investPriceStart"));%>"
                                               class="text border pl5 w120"/>
                                        <span class="pl5 pr5">至</span>
                                        <input type="text" name="investPriceEnd" id="investPriceEnd"
                                               onKeyUp="value=value.replace(/\D/g,'')"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("investPriceEnd"));%>"
                                               class="text border pl5 w120 mr20"/>
                                    </li>
                                    <li><span class="display-ib mr5">投资来源： </span>
                                        <select name="source" class="border mr20 h32 mw100">
                                            <option value="">全部</option>
                                            <option value="PC" <%if ("PC".equals(request.getParameter("source"))) {%>
                                                    selected="selected" <%}%>>PC
                                            </option>
                                            <option value="APP" <%if ("APP".equals(request.getParameter("source"))) {%>
                                                    selected="selected" <%}%>>APP
                                            </option>
                                            <option value="WEIXIN" <%if ("WEIXIN".equals(request.getParameter("source"))) {%>
                                                    selected="selected" <%}%>>微信
                                            </option>
                                        </select>
                                    </li>
                                    <li><span class="display-ib mr5">投资方式： </span>
                                        <select name="bidWay" class="border mr20 h32 mw100">
                                            <option value="">全部</option>
                                            <option value="S" <%if ("S".equals(request.getParameter("bidWay"))) {%>
                                                    selected="selected" <%}%>>自动
                                            </option>
                                            <option value="F" <%if ("F".equals(request.getParameter("bidWay"))) {%>
                                                    selected="selected" <%}%>>手动
                                            </option>

                                        </select>
                                    </li>
                                    <li><span class="display-ib mr5">借款账户类型：</span>
                                        <select name="loanUserType" class="border mr20 h32 mw100">
                                            <option value="">全部</option>
                                            <option value="ZRR" <%if ("ZRR".equals(request.getParameter("loanUserType"))) {%>
                                                    selected="selected" <%}%>>个人</option>
                                            <option value="FZRR" <%if ("FZRR".equals(request.getParameter("loanUserType"))) {%>
                                                    selected="selected" <%}%>>企业</option>
                                        </select>
                                    </li>
                                    <li><span class="display-ib mr5">投资账户类型：</span>
                                        <select name="investUserType" class="border mr20 h32 mw100">
                                            <option value="">全部</option>
                                            <option value="ZRR" <%if ("ZRR".equals(request.getParameter("investUserType"))) {%>
                                                    selected="selected" <%}%>>个人</option>
                                            <option value="FZRR" <%if ("FZRR".equals(request.getParameter("investUserType"))) {%>
                                                    selected="selected" <%}%>>企业</option>
                                            <%if(!"huifu".equals(ESCROW_PREFIX)){ %>
                                            <option value="FZRRJG" <%if ("FZRRJG".equals(request.getParameter("investUserType"))) {%>
                                                    selected="selected" <%}%>>机构</option>
                                            <%} %>
                                        </select>
                                    </li>
                                    
                                    <li><a href="javascript:void(0);" onclick="search1();"
                                           class="btn btn-blue radius-6 mr5 pl1 pr15"><i
                                            class="icon-i w30 h30 va-middle search-icon "></i>搜索</a></li>
                                    <li>
                                        <%if (dimengSession.isAccessableResource(ExportInvestmentList.class)) {%>
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
                                <tr class="title tc">
                                    <th>序号</th>
                                    <th>投资人<br>账户</th>
                                    <th>投资<br>账户<br>类型</th>
                                    <th>投资人<br>姓名</th>
                                    <th>投资<br>时间</th>
                                    <th>放款<br>时间</th>
                                    <th>完结<br>日期</th>
                                    <th>标的ID</th>
                                    <th>借款<br>账户</th>
                                    <th>借款<br>账户<br>类型</th>
                                    <th>借款<br>金额(元)</th>
                                    <!-- <th>担保<br>机构</th> -->
                                    <th>年化利率</th>
                                    <th>还款<br>方式</th>
                                    <th>期限</th>
                                    <th>投资<br>金额(元)</th>
                                    <th>投资<br>来源</th>
                                    <th>投资<br>方式</th>
                                </tr>
                                </thead>
                                <tbody class="f12">
                                <%
                                    if (loans != null && loans.length > 0) {
                                        int i = 1;
                                        for (InvestmentListEntity loan : loans) {
                                %>
                                <tr class="tc">
                                    <td><%=i++ %></td>
                                    <td><%StringHelper.filterHTML(out, loan.investAccoun); %></td>
                                    <td><%StringHelper.filterHTML(out, loan.investAccountType); %></td>
                                    <td><%StringHelper.filterHTML(out, loan.investName); %></td>
                                    <td><%=DateTimeParser.format(loan.investDate) %></td>
                                    <td><%=DateTimeParser.format(loan.loanTime) %></td>
                                    <td><%=DateTimeParser.format(loan.endDate) %></td>
                                    <td><%StringHelper.filterHTML(out, loan.id); %></td>
                                    <td><%StringHelper.filterHTML(out, loan.account); %></td>
                                    <td><%StringHelper.filterHTML(out, loan.accountType); %></td>
                                    <td><%=Formater.formatAmount(loan.loanPrice) %></td>
                                    <%-- <td><%StringHelper.filterHTML(out, loan.guaranteeOrg); %></td> --%>
                                    <td><%=Formater.formatRate(loan.annualRate) %></td>
                                    <td><%StringHelper.filterHTML(out, loan.wayOfRepayment); %></td>
                                    <td><%StringHelper.filterHTML(out, loan.timeLimit); %></td>
                                    <td><%=Formater.formatAmount(loan.investPrice) %></td>
                                    <td><%=loan.source %></td>
                                    <td><%=loan.bidWay %></td>
                                </tr>
                                <%
                                    }
                                } else {
                                %>
                                <tr>
                                    <td colspan="17" class="tc">暂无数据</td>
                                </tr>
                                <%} %>
                                </tbody>
                            </table>
                        </div>
                        <p class="mt5">投资总金额：<em class="red"><%=Formater.formatAmount(total)%>
                        </em> 元</p>
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
            if(!StringHelper.isEmpty(loanTimeStart))
            {
        %>
        $("#datepicker1").val("<%StringHelper.filterHTML(out, request.getParameter("loanTimeStart"));%>");
        <%}%>
        <%
        if(!StringHelper.isEmpty(loanTimeEnd))
        {
        %>
        $("#datepicker2").val("<%StringHelper.filterHTML(out, request.getParameter("loanTimeEnd"));%>");
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
           if(!StringHelper.isEmpty(finishTimeStart))
           {
       %>
        $("#datepicker3").val("<%StringHelper.filterHTML(out, request.getParameter("finishTimeStart"));%>");
        <%}%>
        <%
        if(!StringHelper.isEmpty(finishTimeEnd))
        {
        %>
        $("#datepicker4").val("<%StringHelper.filterHTML(out, request.getParameter("finishTimeEnd"));%>");
        <%}%>
    });

    function search1() {
        var start = $("#investPriceStart").val();
        var end = $("#investPriceEnd").val();

        if (start != '' && end != '') {
            if (parseFloat(start) > parseFloat(end)) {
                $("#investPriceEnd").focus().select();
            } else {
                document.getElementsByName("paging.current")[0].value = 1;
                $("#form_loan").submit();
            }
        } else {
            document.getElementsByName("paging.current")[0].value = 1;
            $("#form_loan").submit();
        }
    }

    function showExport() {
        document.getElementById("form_loan").action = "<%=controller.getURI(request, ExportInvestmentList.class)%>";
        $("#form_loan").submit();
        document.getElementById("form_loan").action = "<%=controller.getURI(request, InvestmentList.class)%>";
    }
</script>
</body>
</html>