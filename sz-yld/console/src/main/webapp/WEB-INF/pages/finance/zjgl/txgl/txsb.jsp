<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.console.servlets.finance.zjgl.txgl.TxglList"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.dimeng.p2p.console.servlets.finance.AbstractFinanceServlet" %>
<%@page import="com.dimeng.p2p.console.servlets.finance.zjgl.txgl.Shtg" %>
<%@page import="com.dimeng.p2p.console.servlets.finance.zjgl.txgl.Txcg" %>
<%@page import="com.dimeng.p2p.console.servlets.finance.zjgl.txgl.TxglView" %>
<%@page import="com.dimeng.p2p.console.servlets.finance.zjgl.txgl.Txsb" %>
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
    
 	// 增加对托管的区分 hsp——20160323
    String escrow = configureProvider.getProperty(SystemVariable.ESCROW_PREFIX);
    boolean escrowBoolean = true;
    if("FUYOU".equals(escrow)){
        escrowBoolean = false;
    }
%>
<body>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <form action="<%=controller.getURI(request, Txsb.class)%>" name="form1" method="post" id="form1">
                    <div class="p20">
                        <div class="border">
                            <div class="title-container">
                                <i class="icon-i w30 h30 va-middle title-left-icon"></i>提现管理
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
                                     <%if (escrowBoolean) { %>
                                   		<span class="display-ib mr5">提现审核时间：</span>
                                    <%} else { %>
                                    	 <span class="display-ib mr5">提现对账时间：</span>
                                    <%} %>
                                        <input type="text" name="startExtractonTime" id="datepicker1"
                                               readonly="readonly" class="text border pl5 date"/>至
                                        <input type="text" name="endExtractionTime" id="datepicker2" readonly="readonly"
                                               class="text border pl5 date mr20"/>
                                    </li>
                                    <li><a href="javascript:$('#form1').submit();"
                                           class="btn btn-blue radius-6 mr5 pl1 pr15"><i
                                            class="icon-i w30 h30 va-middle search-icon "></i>搜索</a></li>
                                </ul>
                            </div>
                        </div>
                        <div class="border mt20">
                            <div class="tabnav-container">
                                <ul class="clearfix">
                                <%if(escrowBoolean){ %>
                                    <li><a href="<%=controller.getURI(request, TxglList.class)%>"
                                           class="tab-btn ">未审核</a></li>
                                    <li><a href="<%=controller.getURI(request, Shtg.class)%>" class="tab-btn ">审核通过</a>
                                    </li><%} %>
                                    <li><a href="<%=controller.getURI(request, Txcg.class)%>" class="tab-btn ">已提现</a>
                                    </li>
                                    <li><a href="javascript:void(0)" class="tab-btn  select-a">提现失败<i class="icon-i tab-arrowtop-icon"></i></a></li>
                                </ul>
                            </div>
                            <div class=" table-container p20">
                                <table class="table table-style gray6 tl">
                                    <thead>
                                    <tr class="title tc">
                                        <!-- <th>流水号</th> -->
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
                                        <%if (escrowBoolean) { %>
                                   		<th>审核时间</th>
                                   		 <%} else { %>
                                    	 <th>对账时间</th>
                                    	<%} %>
                                        <th>审核人</th>
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
                                        <td><%StringHelper.filterHTML(out, txglItem.userName);%></td>
                                        <td><%StringHelper.filterHTML(out, txglItem.extractionBank);%></td>
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
                                        <td><%StringHelper.filterHTML(out, txglItem.subbranch);%></td>
                                        <td><%StringHelper.filterHTML(out, StringHelper.decode(txglItem.bankId));%></td>
                                        <td><%=Formater.formatAmount(txglItem.F04)%>
                                        </td>
                                        <td><%=Formater.formatAmount(txglItem.F07)%>
                                        </td>
                                        <td><%=txglItem.F16.getChineseName() %>
                                        </td>
                                        <td><%=Formater.formatDateTime(txglItem.F14 == null ? txglItem.F11 : txglItem.F14)%>
                                        </td>
                                        <td><%
                                            StringHelper.filterHTML(out, StringHelper.isEmpty(txglItem.txName) ? txglItem.shName : txglItem.txName); %></td>
                                        <td>
                                            <%
                                                if (dimengSession.isAccessableResource(TxglView.class)) {
                                            %>
                                            <a href="<%=controller.getURI(request,TxglView.class)%>?id=<%=txglItem.F01%>"
                                               class="link-blue">查看原因</a>
                                            <%} else { %>
                                            <span class="disabled">查看原因</span>
                                            <%} %>
                                        </td>
                                    </tr>
                                    <%
                                        }
                                    } else {
                                    %>
                                    <tr>
                                        <td colspan="13" class="tc">暂无记录</td>
                                    </tr>
                                    <%}%>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                        <div class="mb10">
								<span style="padding-top: 5px">
						    	总计：提现金额 <span style="color: red;"><%=Formater.formatAmount(totalAmount) %></span> 元,
								手续费<span style="color: red;"><%=Formater.formatAmount(totalFee) %></span> 元,
								共<span style="color: red;"><%=txglRecords != null ? txglRecords.length : 0 %></span> 笔</span>
                        </div>
                        <%AbstractFinanceServlet.rendPagingResult(out, result);%>
                        <div class="clear"></div>
                    </div>
                </form>
            </div>
        </div>
    </div>
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