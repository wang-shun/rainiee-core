<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.console.servlets.statistics.tjbb.zqzrtj.TransferCreditorList"%>
<%@page import="com.dimeng.p2p.console.servlets.AbstractConsoleServlet" %>
<%@page import="com.dimeng.p2p.console.servlets.statistics.tjbb.zqzrtj.ExportTransferCreditor" %>
<%@page import="com.dimeng.p2p.modules.statistics.console.service.entity.TransferCreditorEntity" %>
<%@page import="com.dimeng.util.ObjectHelper" %>
<%@page import="java.util.Map" %>
<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <%@include file="/WEB-INF/include/script.jsp"%>
</head>
<body>
<%
    CURRENT_CATEGORY = "TJGL";
    CURRENT_SUB_CATEGORY = "ZQZRTJB";
    PagingResult<TransferCreditorEntity> result = ObjectHelper.convert(request.getAttribute("result"), PagingResult.class);
    Map totalMap = ObjectHelper.convert(request.getAttribute("creditorTotalMap"), Map.class);
    TransferCreditorEntity[] creditors = result.getItems();
    String applyTimeStart = request.getParameter("applyTimeStart");
    String applyTimeEnd = request.getParameter("applyTimeEnd");
    String buyTimeStart = request.getParameter("buyTimeStart");
    String buyTimeEnd = request.getParameter("buyTimeEnd");
    //托管前缀
    String ESCROW_PREFIX = configureProvider.getProperty(SystemVariable.ESCROW_PREFIX);

%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <form id="form_loan" action="<%=controller.getURI(request, TransferCreditorList.class)%>" method="post">
                    <div class="p20">
                        <div class="border">
                            <div class="title-container"><i class="icon-i w30 h30 va-middle title-left-icon"></i>债权转让统计表
                            </div>
                            <div class="content-container pl40 pt30 pr40">
                                <ul class="gray6 input-list-container clearfix">
                                    <li><span class="display-ib mr5">债权ID：</span>
                                        <input type="text" name="creditorId"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("creditorId"));%>"
                                               class="text border pl5 mr20"/>
                                    </li>
                                    <li><span class="display-ib mr5">借款ID： </span>
                                        <input type="text" name="loanId"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("loanId"));%>"
                                               class="text border pl5 mr20"/>
                                    </li>
                                    <li><span class="display-ib mr5">卖出账户： </span>
                                        <input type="text" name="sellAccount"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("sellAccount"));%>"
                                               class="text border pl5 mr20"/>
                                    </li>
                                    <li><span class="display-ib mr5">买入账户： </span>
                                        <input type="text" name="buyAccount"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("buyAccount"));%>"
                                               class="text border pl5 mr20"/>
                                    </li>
                                    <li><span class="display-ib mr5">申请时间：</span>
                                        <input type="text" name="applyTimeStart" readonly="readonly" id="datepicker1"
                                               class="text border pl5 w120 date"/>
                                        <span class="pl5 pr5">至</span>
                                        <input readonly="readonly" type="text" name="applyTimeEnd" id="datepicker2"
                                               class="text border pl5 w120 mr20 date"/>
                                    </li>
                                    <li><span class="display-ib mr5">购买时间：</span>
                                        <input type="text" name="buyTimeStart" readonly="readonly" id="datepicker3"
                                               class="text border pl5 w120 date"/>
                                        <span class="pl5 pr5">至</span>
                                        <input readonly="readonly" type="text" name="buyTimeEnd" id="datepicker4"
                                               class="text border pl5 w120 mr20 date"/>
                                    </li>
                                    <li><span class="display-ib mr5">来源：</span>
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
                                    <li><span class="display-ib mr5">卖出账户类型：</span>
                                        <select name="sellUserType" class="border mr20 h32 mw100">
                                            <option value="">全部</option>
                                            <option value="ZRR" <%if ("ZRR".equals(request.getParameter("sellUserType"))) {%>
                                                    selected="selected" <%}%>>个人</option>
                                            <option value="FZRR" <%if ("FZRR".equals(request.getParameter("sellUserType"))) {%>
                                                    selected="selected" <%}%>>企业</option>
                                            <%if(!"huifu".equals(ESCROW_PREFIX)){ %>
                                            <option value="FZRRJG" <%if ("FZRRJG".equals(request.getParameter("sellUserType"))) {%>
                                                    selected="selected" <%}%>>机构</option>
                                            <%} %>
                                        </select>
                                    </li>
                                    <li><span class="display-ib mr5">买入账户类型：</span>
                                        <select name="buyUserType" class="border mr20 h32 mw100">
                                            <option value="">全部</option>
                                            <option value="ZRR" <%if ("ZRR".equals(request.getParameter("buyUserType"))) {%>
                                                    selected="selected" <%}%>>个人</option>
                                            <option value="FZRR" <%if ("FZRR".equals(request.getParameter("buyUserType"))) {%>
                                                    selected="selected" <%}%>>企业</option>
                                            <%if(!"huifu".equals(ESCROW_PREFIX)){ %>
                                            <option value="FZRRJG" <%if ("FZRRJG".equals(request.getParameter("buyUserType"))) {%>
                                                    selected="selected" <%}%>>机构</option>
                                            <%} %>
                                        </select>
                                    </li>
                                    <li><a href="javascript:void(0);" onclick="search1();"
                                           class="btn btn-blue radius-6 mr5 pl1 pr15"><i
                                            class="icon-i w30 h30 va-middle search-icon "></i>搜索</a></li>
                                    <li>
                                        <%if (dimengSession.isAccessableResource(ExportTransferCreditor.class)) {%>
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
                                    <th class="tc">债权ID</th>
                                    <th class="tc">借款ID</th>
                                    <th class="tc">剩余<br>期数</th>
                                    <th class="tc">卖出<br>账户</th>
                                    <th class="tc">卖出<br>账户<br>类型</th>
                                    <th class="tc">卖出<br>姓名</th>
                                    <th class="tc">申请<br>卖出时间</th>
                                    <th class="tc">待收<br>本息(元)</th>
                                    <th class="tc">债权<br>价值 (元)</th>
                                    <th class="tc">成交<br>金额(元)</th>
                                    <th class="tc">交易<br>费率</th>
                                    <th class="tc">交易<br>费用(元)</th>
                                    <th class="tc">买入<br>账户</th>
                                    <th class="tc">买入<br>账户<br>类型</th>
                                    <th class="tc">买入<br>姓名</th>
                                    <th class="tc">购买<br>时间</th>
                                    <th class="tc">转出<br>盈亏 (元)</th>
                                    <th class="tc">转入<br>盈亏 (元)</th>
                                    <th class="tc">来源</th>
                                </tr>
                                </thead>
                                <tbody class="f12">
                                <%
                                    if (creditors != null && creditors.length > 0) {
                                        int index = 1;
                                        for (TransferCreditorEntity creditor : creditors) {
                                %>
                                <tr class="tc">
                                    <td><%=index++ %></td>
                                    <td><%StringHelper.filterHTML(out, creditor.creditorId); %></td>
                                    <td><%StringHelper.filterHTML(out, creditor.loanId); %></td>
                                    <td><%StringHelper.filterHTML(out, creditor.surplusLimit); %></td>
                                    <td><%StringHelper.filterHTML(out, creditor.sellAccount); %></td>
                                    <td><%StringHelper.filterHTML(out, creditor.sellUserType); %></td>
                                    <td><%StringHelper.filterHTML(out, creditor.sellName); %></td>
                                    <td><%=DateTimeParser.format(creditor.applyTime) %>
                                    </td>
                                    <td><%=Formater.formatAmount(creditor.dueInBX) %>
                                    </td>
                                    <td><%=Formater.formatAmount(creditor.creditorWorth) %>
                                    </td>
                                    <td><%=Formater.formatAmount(creditor.lastMoney) %>
                                    </td>
                                    <td><%=Formater.formatRate(creditor.dealRate) %>
                                    </td>
                                    <td><%=Formater.formatAmount(creditor.dealMoney) %>
                                    </td>
                                    <td><%StringHelper.filterHTML(out, creditor.buyAccount); %></td>
                                    <td><%StringHelper.filterHTML(out, creditor.buyUserType); %></td>
                                    <td><%StringHelper.filterHTML(out, creditor.buyName); %></td>
                                    <td><%=DateTimeParser.format(creditor.buyTime) %>
                                    </td>
                                    <td><%=Formater.formatAmount(creditor.transferEarn) %>
                                    </td>
                                    <td><%=Formater.formatAmount(creditor.IntoEarn) %>
                                    </td>
                                    <td><%=creditor.source %>
                                    </td>
                                </tr>
                                <%
                                    }
                                } else {
                                %>
                                <tr>
                                    <td colspan="20" class="tc">暂无数据</td>
                                </tr>
                                <%} %>
                                </tbody>
                            </table>
                        </div>
                        <p class="mt5">
                            <span class="mr30">债权成交金额共计：<em class="red"><%=totalMap.get("lastMoneyTotal")%>
                            </em> 元</span>
                            <span class="mr30">交易费用：<em class="red"><%=totalMap.get("dealMoneyTotal")%>
                            </em> 元</span>
                            <span class="mr30">转出盈亏：<em class="red"><%=totalMap.get("receiveMoneyTotal")%>
                            </em> 元</span>
                            <span class="mr30">转入盈亏：<em class="red"><%=totalMap.get("soldMoneyTotal")%>
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
            if(!StringHelper.isEmpty(applyTimeStart))
            {
        %>
        $("#datepicker1").val("<%StringHelper.filterHTML(out, request.getParameter("applyTimeStart"));%>");
        <%}%>
        <%
        if(!StringHelper.isEmpty(applyTimeEnd))
        {
        %>
        $("#datepicker2").val("<%StringHelper.filterHTML(out, request.getParameter("applyTimeEnd"));%>");
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
           if(!StringHelper.isEmpty(buyTimeStart))
           {
       %>
        $("#datepicker3").val("<%StringHelper.filterHTML(out, request.getParameter("buyTimeStart"));%>");
        <%}%>
        <%
        if(!StringHelper.isEmpty(buyTimeEnd))
        {
        %>
        $("#datepicker4").val("<%StringHelper.filterHTML(out, request.getParameter("buyTimeEnd"));%>");
        <%}%>

        $("#datepicker4").datepicker('option', 'minDate', $("#datepicker3").datepicker().val());
    });

    function search1() {
        document.getElementsByName("paging.current")[0].value = 1;
        $("#form_loan").submit();
    }

    function showExport() {
        document.getElementById("form_loan").action = "<%=controller.getURI(request, ExportTransferCreditor.class)%>";
        $("#form_loan").submit();
        document.getElementById("form_loan").action = "<%=controller.getURI(request, TransferCreditorList.class)%>";
    }
</script>
</body>
</html>