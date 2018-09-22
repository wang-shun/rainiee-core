<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.dimeng.p2p.console.servlets.finance.czgl.xsczgl.CzglList"%>
<%@page import="com.dimeng.p2p.S61.enums.T6110_F06" %>
<%@page import="com.dimeng.p2p.S61.enums.T6110_F10" %>
<%@page import="com.dimeng.p2p.console.servlets.finance.AbstractFinanceServlet" %>
<%@page import="com.dimeng.p2p.console.servlets.finance.czgl.xsczgl.ExportCzgl" %>
<%@page import="com.dimeng.p2p.modules.account.console.service.entity.UserRecharge" %>
<%@page import="com.dimeng.util.ObjectHelper" %>
<%@page import="java.math.BigDecimal" %>
<%@page import="com.dimeng.p2p.console.servlets.AbstractConsoleServlet"%>
<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <%@include file="/WEB-INF/include/script.jsp"%>
</head>
<%
    BigDecimal czze = ObjectHelper.convert(request.getAttribute("czze"), BigDecimal.class);
    BigDecimal czsxf = ObjectHelper.convert(request.getAttribute("czsxf"), BigDecimal.class);
    PagingResult<UserRecharge> result = (PagingResult<UserRecharge>) request.getAttribute("result");
    UserRecharge chargeCount = (UserRecharge) request.getAttribute("chargeCount");
    CURRENT_CATEGORY = "CWGL";
    CURRENT_SUB_CATEGORY = "CZGL";
    String startTime = request.getParameter("startTime");
    String endTime = request.getParameter("endTime");
    String finishStartTime = request.getParameter("finishStartTime");
    String finishEndTime = request.getParameter("finishEndTime");
%>
<body>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <form action="<%=controller.getURI(request, CzglList.class)%>" method="post" name="form1" id="form1">
                    <div class="p20">
                        <div class="border">
                            <div class="title-container">
                                <i class="icon-i w30 h30 va-middle title-left-icon"></i>线上充值管理
                            </div>
                            <div class="content-container pl40 pt30 pr40">
                                <ul class="gray6 input-list-container clearfix">
                                    <li>
                                        <span class="display-ib mr5">成功充值总额：</span>
                                        <span class="link-blue"><%=Formater.formatAmount(czze)%></span>元
                                    </li>
                                    <li class="ml50">
                                        <span class="display-ib mr5">成功充值手续费 ：</span>
                                        <span class="link-blue"><%=Formater.formatAmount(czsxf)%></span>元
                                    </li>
                                    <li class="ml50">
                                        <span class="display-ib mr5">更新时间 ：</span>
                                        <span class="link-blue"><%=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())%></span>
                                    </li>
                                </ul>
                            </div>
                        </div>

                        <div class="border mt20">
                            <div class="content-container pl40 pt30 pr40">
                                <ul class="gray6 input-list-container clearfix">
                                    <li>
                                        <span class="display-ib mr5">订单号：</span>
                                        <input type="text" name="orderNo"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("orderNo")); %>"
                                               class="text border pl5 mr20"/>
                                    </li>
                                    <li>
                                        <span class="display-ib mr5">用户名：</span>
                                        <input type="text" name="loginName"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("loginName")); %>"
                                               class="text border pl5 mr20"/>
                                    </li>
                                    <li>
                                        <span class="display-ib mr5">支付公司名称：</span>
                                        <input type="text" name="payComName"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("payComName")); %>"
                                               class="text border pl5 mr20"/>
                                    </li>
                                    <li>
                                        <span class="display-ib mr5">提交时间：</span>
                                        <input type="text" name="startTime" id="datepicker1" readonly="readonly"
                                               class="text border pl5 w120 date"/>
                                        <span class="pl5 pr5">至</span>
                                        <input type="text" name="endTime" id="datepicker2" readonly="readonly"
                                               class="text border pl5 w120 date mr20"/>
                                    </li>
                                    <li>
                                        <span class="display-ib mr5">完成时间：</span>
                                        <input type="text" name="finishStartTime" id="finishdatepicker1"
                                               readonly="readonly" class="text border pl5 w120 date"/>
                                        <span class="pl5 pr5">至</span>
                                        <input type="text" name="finishEndTime" id="finishdatepicker2"
                                               readonly="readonly" class="text border pl5 w120 date mr20"/>
                                    </li>
                                    <li>
                                        <span class="display-ib mr5">流水单号：</span>
                                        <input type="text" name="serialNumber"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("serialNumber")); %>"
                                               class="text border pl5 mr20"/>
                                    </li>
                                    <li>
                                        <span class="display-ib mr5">用户类型：</span>
                                        <select name="usersType" class="border mr20 h32 mw100">
                                            <option value="">全部</option>
                                            <option value="1" <%if ("1".equals(request.getParameter("usersType"))) {%>
                                                    selected="selected" <%} %>>个人
                                            </option>
                                            <option value="2" <%if ("2".equals(request.getParameter("usersType"))) {%>
                                                    selected="selected" <%} %>>企业
                                            </option>
                                            <option value="3" <%if ("3".equals(request.getParameter("usersType"))) {%>
                                                    selected="selected" <%} %>>机构
                                            </option>
                                        </select>
                                    </li>
                                    <li>
                                        <span class="display-ib mr5">充值状态：</span>
                                        <select name="chargeStatus" class="border mr20 h32 mw100">
                                            <option value="">全部</option>
                                            <option value="0" <%if ("0".equals(request.getParameter("chargeStatus"))) {%>
                                                    selected="selected" <%} %>>未支付
                                            </option>
                                            <option value="1" <%if ("1".equals(request.getParameter("chargeStatus"))) {%>
                                                    selected="selected" <%} %>>支付成功
                                            </option>
                                        </select>
                                    </li>
                                    <li><a href="javascript:onSubmit();"
                                           class="btn btn-blue radius-6 mr5 pl1 pr15"><i
                                            class="icon-i w30 h30 va-middle search-icon "></i>搜索</a></li>
                                    <li>
                                        <%if (dimengSession.isAccessableResource(ExportCzgl.class)) {%>
                                        <a href="javascript:void(0)" onclick="showExport()"
                                           class="btn btn-blue radius-6 mr5  pl1 pr15"><i
                                                class="icon-i w30 h30 va-middle export-icon "></i>导出</a>
                                        <%} else { %>
                                        <span class="btn btn-gray radius-6 mr5 pl1 pr15"><i
                                                class="icon-i w30 h30 va-middle export-icon "></i>导出</span>
                                        <%} %>
                                    </li>
                                </ul>
                            </div>
                        </div>

                        <div class="border mt20 table-container">
                            <table class="table table-style gray6 tl">
                                <thead>
                                <tr class="title tc">
                                    <th>序号</th>
                                    <th>订单号</th>
                                    <th>用户名</th>
                                    <th>姓名或企业名称</th>
                                    <th>充值金额(元)</th>
                                    <th>应收手续费(元)</th>
                                    <th>实收手续费(元)</th>
                                    <th>用户类型</th>
                                    <th>提交时间</th>
                                    <th>完成时间</th>
                                    <th>支付公司名称</th>
                                    <th>充值状态</th>
                                    <th>流水单号</th>
                                    <%--<th>操作</th>--%>
                                </tr>
                                </thead>
                                <tbody class="f12">
                                <%
                                    UserRecharge[] items = result.getItems();
                                    if (items != null && items.length > 0) {
                                        int i = 1;
                                        for (UserRecharge rechargeItem : items) {
                                            if (rechargeItem == null) {
                                                continue;
                                            }
                                %>
                                <tr class="tc">
                                    <td><%=i++%>
                                    </td>
                                    <td><%=rechargeItem.F01 %>
                                    </td>
                                    <td>
                                        <%
                                            StringHelper.filterHTML(out, rechargeItem.userName);
                                        %>
                                    </td>
                                    <td><%=rechargeItem.userRealName %>
                                    </td>
                                    <td><%=Formater.formatAmount(rechargeItem.F03)%>
                                    </td>
                                    <td><%=Formater.formatAmount(rechargeItem.F04)%>
                                    </td>
                                    <td><%=Formater.formatAmount(rechargeItem.F05)%>
                                    </td>
                                    <td>
                                        <%if (T6110_F06.ZRR == rechargeItem.userType) { %>
                                        个人
                                        <%} else if (T6110_F06.FZRR == rechargeItem.userType && T6110_F10.F == rechargeItem.t6110_F10) { %>
                                        企业
                                        <%} else { %>
                                        机构
                                        <%} %>
                                    </td>
                                    <td><%=DateTimeParser.format(rechargeItem.createTime) %>
                                    </td>
                                    <td><%=DateTimeParser.format(rechargeItem.chargeFinishTime) %>
                                    </td>
                                    <td><%=rechargeItem.payComName%>
                                    </td>
                                    <td><%=StringHelper.isEmpty(rechargeItem.F08)?"未支付":"支付成功"%></td>
                                    <td><%StringHelper.filterHTML(out, rechargeItem.F08);%></td>
                                    <%--<td><a target="_blank" href="<%=configureProvider.format(URLVariable.PAY_CHECK) %>?o=<%=rechargeItem.F01 %>">去对账</a> </td>--%>
                                </tr>
                                <%
                                    }
                                } else {
                                %>
                                <tr>
                                    <td colspan="13" class="tc">暂无数据</td>
                                </tr>
                                <%} %>
                                </tbody>
                            </table>
                        </div>
                        <div class="clear"></div>
                        <div class="mb10">
                            <span class="mr30">充值总金额：<em
                                    class="red"><%=Formater.formatAmount(chargeCount.countChargeAmount) %>
                            </em> 元</span>
                            <span class="mr30">应收手续费：<em
                                    class="red"><%=Formater.formatAmount(chargeCount.countReceivableAmount) %>
                            </em> 元</span>
                            <span class="mr30">实收手续费：<em
                                    class="red"><%=Formater.formatAmount(chargeCount.countPaidAmount) %>
                            </em> 元</span>
                        </div>
                        <%
                            AbstractFinanceServlet.rendPagingResult(out, result);
                        %>
                        <div class="box2 clearfix"></div>
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
        <%if(!StringHelper.isEmpty(startTime)){%>
        $("#datepicker1").val("<%StringHelper.filterHTML(out, request.getParameter("startTime"));%>");
        <%}%>
        <%if(!StringHelper.isEmpty(endTime)){%>
        $("#datepicker2").val("<%StringHelper.filterHTML(out, request.getParameter("endTime"));%>");
        <%}%>
        $("#datepicker2").datepicker('option', 'minDate', $("#datepicker1").datepicker().val());

        $("#finishdatepicker1").datepicker({
            inline: true,
            onSelect: function (selectedDate) {
                $("#finishdatepicker2").datepicker("option", "minDate", selectedDate);
            }
        });
        $('#finishdatepicker1').datepicker('option', {dateFormat: 'yy-mm-dd'});
        $("#finishdatepicker2").datepicker({inline: true});
        $('#finishdatepicker2').datepicker('option', {dateFormat: 'yy-mm-dd'});
        <%if(!StringHelper.isEmpty(finishStartTime)){%>
        $("#finishdatepicker1").val("<%StringHelper.filterHTML(out, request.getParameter("finishStartTime"));%>");
        <%}%>
        <%if(!StringHelper.isEmpty(finishEndTime)){%>
        $("#finishdatepicker2").val("<%StringHelper.filterHTML(out, request.getParameter("finishEndTime"));%>");
        <%}%>
        $("#finishdatepicker2").datepicker('option', 'minDate', $("#finishdatepicker1").datepicker().val());
    });
    function showExport() {
        document.getElementById("form1").action = "<%=controller.getURI(request, ExportCzgl.class)%>";
        $("#form1").submit();
        document.getElementById("form1").action = "<%=controller.getURI(request, CzglList.class)%>";
    }
    function onSubmit(){
    	$("input[name='<%=AbstractConsoleServlet.PAGING_CURRENT%>']").val('1');
    	$('#form1').submit();
    }

</script>
</body>
</html>
