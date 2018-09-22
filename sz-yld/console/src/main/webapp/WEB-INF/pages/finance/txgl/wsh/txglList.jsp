<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%@page import="com.dimeng.p2p.console.servlets.finance.txgl.wsh.TxglList"%>
<%@page import="com.dimeng.p2p.console.servlets.finance.AbstractFinanceServlet" %>
<%@page import="com.dimeng.p2p.console.servlets.finance.txgl.wsh.Check" %>
<%@page import="com.dimeng.p2p.modules.account.console.service.entity.Bank" %>
<%@page import="com.dimeng.p2p.modules.account.console.service.entity.UserWithdrawals" %>
<%@page import="com.dimeng.util.ObjectHelper" %>
<%@page import="java.math.BigDecimal" %>
<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <%@include file="/WEB-INF/include/datepicker.jsp" %>
    <%@include file="/WEB-INF/include/script.jsp"%>
    <style type="text/css">
        .yhgl_inputtext {
            background: #fff;
            border: 1px #d7dfe3 solid;
            height: 23px;
            line-height: 23px;
            width: 175px;
        }
    </style>
</head>
<%
    BigDecimal txz = ObjectHelper.convert(request.getAttribute("txz"), BigDecimal.class);
    BigDecimal txsxf = ObjectHelper.convert(request.getAttribute("txsxf"), BigDecimal.class);
    Bank[] banks = ObjectHelper.convertArray(request.getAttribute("banks"), Bank.class);
    PagingResult<UserWithdrawals> result = (PagingResult<UserWithdrawals>) request.getAttribute("result");
    int bankId = IntegerParser.parse(request.getParameter("bankId"));
    BigDecimal totalAmount = new BigDecimal(0);
    BigDecimal totalFee = new BigDecimal(0);
    CURRENT_CATEGORY = "CWGL";
    CURRENT_SUB_CATEGORY = "TXGL";
    String createTimeStart = request.getParameter("startExtractonTime");
    String createTimeEnd = request.getParameter("endExtractionTime");
    //托管前缀
    String escrow = configureProvider.getProperty(SystemVariable.ESCROW_PREFIX);
%>
<body>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <form action="<%=controller.getURI(request, TxglList.class)%>" method="post" id="form1" name="form1">
                    <div class="p20">
                        <div class="border">
                            <div class="title-container">
                                <i class="icon-i w30 h30 va-middle title-left-icon"></i>未审核
                            </div>
                            <div class="content-container pl40 pt30 pr40">
                                <ul class="gray6 input-list-container clearfix">
                                    <li><span class="display-ib mr5">提现总额 ：</span><span
                                            class="blue"><%=Formater.formatAmount(txz)%></span>元
                                    </li>
                                    <li class="ml50"><span class="display-ib mr5">提现总手续费 ：</span><span
                                            class="blue"><%=Formater.formatAmount(txsxf)%></span>元
                                    </li>
                                    <li class="ml50"><span class="display-ib mr5">更新时间 ：</span><span
                                            class="blue"><%=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())%></span>
                                    </li>
                                </ul>
                            </div>
                        </div>
                        <div class="border mt20">
                            <div class="content-container pl40 pt30 pr40">
                                <ul class="gray6 input-list-container clearfix">
                                    <%--  <li><span class="display-ib mr5">流水号:</span>
                                         <input type="text" class="text border pl5 mr20" name="lsh" value="<%StringHelper.filterQuoter(out, request.getParameter("lsh"));%>" />
                                     </li> --%>
                                    <li><span class="display-ib mr5">用户名:</span>
                                        <input type="text" class="text border pl5 mr20" name="yhm"
                                               value="<%StringHelper.filterQuoter(out, request.getParameter("yhm"));%>"/>
                                    </li>
                                    <li><span class="display-ib mr5">提现银行：</span>
                                        <select name="bankId" class="border mr20 h32 mw100" style="width: 150px">
                                            <option value="">全部</option>
                                            <%
                                                for (Bank bank : banks) {
                                                    if (bank == null) {
                                                        continue;
                                                    }
                                            %>
                                            <option value="<%=bank.id%>" <%if (bank.id == bankId) {%>
                                                    selected="selected" <%}%>>
                                                <%StringHelper.filterHTML(out, bank.name);%>
                                            </option>
                                            <%}%>
                                        </select>
                                    </li>
                                    <li>
                                        <span class="display-ib mr5">申请时间：</span>
                                        <input type="text" name="startExtractonTime" id="datepicker1"
                                               readonly="readonly" class="text border pl5 w120 date"/>
                                        <span class="pl5 pr5">至</span>
                                        <input type="text" name="endExtractionTime" id="datepicker2" readonly="readonly"
                                               class="text border pl5 w120 date"/>
                                    </li>
                                    <li><a href="javascript:$('#form1').submit();"
                                           class="btn btn-blue radius-6 mr5 pl1 pr15 ml20"><i
                                            class="icon-i w30 h30 va-middle search-icon "></i>搜索</a></li>
                                </ul>
                            </div>
                        </div>
                        <div class="border mt20 table-container">
                                <table class="table table-style gray6 tl">
                                    <thead>
                                    <tr class="title tc">
                                        <!-- <th class="tc">流水号</th> -->
                                        <th>用户名</th>
                                        <th>提现银行</th>
                                        <th>开户名</th>
                                        <%
                                        if(!"huifu".equalsIgnoreCase(escrow))
                                        { 
                                        %>
                                        <th>省</th>
                                        <th>市</th>
                                        <%
                                        	} 
                                        %>
                                        <th>所在支行</th>
                                        <th>银行卡号</th>
                                        <th>提现金额(元)</th>
                                        <th>手续费(元)</th>
                                        <th>是否到账</th>
                                        <th>申请时间</th>
                                        <th>操作</th>
                                    </tr>
                                    </thead>
                                    <tbody class="f12">
                                    <%
                                        UserWithdrawals[] txglRecords = result.getItems();
                                        if (txglRecords != null && txglRecords.length > 0) {
                                            for (UserWithdrawals txglItem : txglRecords) {
                                                if (txglItem == null) {
                                                    continue;
                                                }
                                                totalAmount = totalAmount.add(txglItem.F04);
                                                totalFee = totalFee.add(txglItem.F07);
                                    %>
                                    <tr class="tc">
                                        <%-- <td><%=txglItem.F01%></td> --%>
                                        <td>
                                            <%StringHelper.filterHTML(out, txglItem.userName);%>
                                        </td>
                                        <td>
                                            <%StringHelper.filterHTML(out, txglItem.extractionBank);%>
                                        </td>
                                        <td><%StringHelper.filterHTML(out, txglItem.realName);%></td>
                                        <%
                                        if(!"huifu".equalsIgnoreCase(escrow))
                                        { 
                                        %>
                                        <td><%StringHelper.filterHTML(out, txglItem.shengName);%></td>
                                        <td><%StringHelper.filterHTML(out, txglItem.shiName);%></td>
                                        <%
                                        	} 
                                        %>
                                        <td>
                                            <%StringHelper.filterHTML(out, txglItem.subbranch);%>
                                        </td>
                                        <td>
                                            <%StringHelper.filterHTML(out, StringHelper.decode(txglItem.bankId));%>
                                        </td>
                                        <td><%=Formater.formatAmount(txglItem.F04)%>
                                        </td>
                                        <td><%=Formater.formatAmount(txglItem.F07)%>
                                        </td>
                                        <td><%=txglItem.F16.getChineseName() %>
                                        </td>
                                        <td><%=Formater.formatDateTime(txglItem.F08)%>
                                        </td>
                                        <td>
                                            <%
                                                if (dimengSession.isAccessableResource(Check.class)) {
                                            %>
                                            <a href="<%=controller.getURI(request, Check.class)%>?id=<%=txglItem.F01%>"
                                               class="link-blue mr20">审核</a>
                                            <%} else { %>
                                            <span class="disabled">审核</span>
                                            <%} %>
                                        </td>
                                    </tr>
                                    <%
                                        }
                                    } else {
                                    %>
                                    <tr>
                                        <td colspan="12" class="tc">暂无记录</td>
                                    </tr>
                                    <%}%>
                                    </tbody>
                                </table>
                        </div>
                        <div class="mb10">
                            <span class="mr20">总计：提现金额<em class="red"><%=Formater.formatAmount(totalAmount) %>
                            </em> 元</span>
                            <span class="mr20">手续费<em class="red"><%=Formater.formatAmount(totalFee) %>
                            </em> 元</span>
                            <span class="mr20">共<em class="red"><%=txglRecords != null ? txglRecords.length : 0 %>
                            </em> 笔</span>
                        </div>
                        <%AbstractFinanceServlet.rendPagingResult(out, result);%>
                        <div class="clear"></div>
                    </div>
                </form>
            </div>
        </div>
    </div>
<%
    String warringMessage = controller.getPrompt(request, response, PromptLevel.WARRING);
    if (!StringHelper.isEmpty(warringMessage)) {
%>
<div class="w440 thickbox thickpos" style="margin:-80px 0 0 -220px;" id="showDiv">
    <div class="info clearfix">
        <div class="clearfix">
            <span class="fl tips"><%StringHelper.filterHTML(out, warringMessage); %></span>
        </div>
        <div class="dialog_btn"><input type="button" name="button2" onclick="$('#showDiv').hide()" value="确认"
                                       class="btn4 ml50"/></div>
    </div>
</div>
<%} %>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/txgl/withdrawals.js"></script>
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
        $("#datepicker1").val("<%StringHelper.filterHTML(out, request.getParameter("startExtractonTime"));%>");
        <%}%>
        <%if(!StringHelper.isEmpty(createTimeEnd)){%>
        $("#datepicker2").val("<%StringHelper.filterHTML(out, request.getParameter("endExtractionTime"));%>");
        <%}%>
        $("#datepicker2").datepicker('option', 'minDate', $("#datepicker1").datepicker().val());
    });
</script>
</body>
</html>