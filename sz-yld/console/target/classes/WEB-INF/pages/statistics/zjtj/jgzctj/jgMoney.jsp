<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.console.servlets.statistics.zjtj.jgzctj.ExportJgMoney"%>
<%@page import="com.dimeng.p2p.console.servlets.statistics.zjtj.jgzctj.JgMoney"%>
<%@page import="com.dimeng.p2p.console.servlets.statistics.zjtj.qyzctj.ExportQyMoney"%>
<%@page import="com.dimeng.p2p.console.servlets.statistics.zjtj.qyzctj.QyMoney"%>
<%@page import="com.dimeng.p2p.modules.account.console.service.QyManage"%>
<%@page import="com.dimeng.p2p.modules.statistics.console.service.entity.PropertyStatisticsEntity"%>
<%@page import="com.dimeng.p2p.console.servlets.statistics.zjtj.grzctj.GrMoney"%>
<%@page import="com.dimeng.p2p.console.servlets.AbstractConsoleServlet" %>
<%@page import="com.dimeng.p2p.console.servlets.statistics.zjtj.grzctj.ExportGrMoney" %>
<%@page import="com.dimeng.p2p.modules.statistics.console.service.entity.GrMoneyEntity" %>
<%@page import="com.dimeng.util.ObjectHelper" %>
<%@page import="com.dimeng.util.Formater" %>
<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <%@include file="/WEB-INF/include/script.jsp"%>
</head>
<body>
<%
    CURRENT_CATEGORY = "TJGL";
    CURRENT_SUB_CATEGORY = "JGZCTJ";
    PagingResult<PropertyStatisticsEntity> list = ObjectHelper.convert(request.getAttribute("list"), PagingResult.class);
    PropertyStatisticsEntity[] qyMoneyArray = (list == null ? null : list.getItems());
    PropertyStatisticsEntity searchAmount = (PropertyStatisticsEntity)request.getAttribute("searchAmount");
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <form id="form_loan" action="<%=controller.getURI(request, JgMoney.class)%>" method="post">
                    <div class="p20">
                        <div class="border">
                            <div class="title-container"><i class="icon-i w30 h30 va-middle title-left-icon"></i>机构资产统计
                            </div>
                            <div class="content-container pl40 pt30 pr40">
                                <ul class="gray6 input-list-container clearfix">
                                    <li><span class="display-ib mr5">机构账户： </span>
                                        <input type="text" name="userName"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("userName"));%>"
                                               class="text border pl5 mr20"/>
                                    </li>
                                    <li><span class="display-ib mr5">机构名称： </span>
                                        <input type="text" name="businessName"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("businessName"));%>"
                                               class="text border pl5 mr20"/>
                                    </li>
                                    <li><span class="display-ib mr5">手机号码： </span>
                                        <input type="text" name="phone"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("phone"));%>"
                                               class="text border pl5 mr20"/>
                                    </li>
                                    <li><span class="display-ib mr5">邮箱： </span>
                                        <input type="text" name="email"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("email"));%>"
                                               class="text border pl5 mr20"/>
                                        <input type="hidden" name="dshFlg"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("dshFlg"));%>"/>
                                    </li>
									<li><span class="display-ib mr5">可用余额： </span>
                                        <input type="text" name="balanceMin" id="balanceMin"
                                               onKeyUp="value=value.replace(/\D/g,'')"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("balanceMin"));%>"
                                               class="text border pl5 w120"/>
                                        <span class="pl5 pr5">至</span>
                                        <input type="text" name="balanceMax" id="balanceMax"
                                               onKeyUp="value=value.replace(/\D/g,'')"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("balanceMax"));%>"
                                               class="text border pl5 w120 mr20"/>
                                    </li>
                                    <li>
                                        <a href="javascript:void(0);" onclick="$('#form_loan').submit();"
                                           class="btn btn-blue radius-6 mr5 pl1 pr15"><i
                                                class="icon-i w30 h30 va-middle search-icon "></i>搜索</a>
                                    </li>
                                    <li>
                                        <%if (dimengSession.isAccessableResource(ExportJgMoney.class)) {%>
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
                                    <th class="tc">机构账户</th>
                                    <th class="tc">机构名称</th>
                                    <th class="tc">法人身份证</th>
                                    <th class="tc">法人手机号码</th>
                                    <th class="tc">法人邮箱地址</th>
                                    <th class="tc">可用余额(元)</th>
                                    <th class="tc">冻结金额(元)</th>
                                    <th class="tc">账户余额(元)</th>
                                    <th class="tc">风险保证金余额(元)</th>
                                    <th class="tc">垫付总金额(元)</th>
                                    <th class="tc">垫付待还总金额(元)</th>
                                    <th class="tc">理财资产(元)</th>
                                    <th class="tc">总收益(元)</th>
                                    <th class="tc">总充值(元)</th>
                                    <th class="tc">总提现(元)</th>
                                    <th class="tc">注册时间</th>
                                </tr>
                                </thead>
                                <tbody class="f12">
                                <%
                                    if (qyMoneyArray != null && qyMoneyArray.length > 0) {
                                        int index = 1;
                                        for (PropertyStatisticsEntity qyMoney : qyMoneyArray) {
                                            if (qyMoney == null) {
                                                continue;
                                            }
                                %>
                                <tr class="dhsbg">
                                    <td class="tc"><%=index++ %>
                                    </td>
                                    <td class="tc"><%StringHelper.filterHTML(out, qyMoney.userName);%></td>
                                    <td class="tc"><%StringHelper.filterHTML(out, qyMoney.businessName);%></td>
                                    <td class="tc"><%StringHelper.filterHTML(out, StringHelper.decode(qyMoney.idCardNo));%></td>
                                    <td class="tc"><%StringHelper.filterHTML(out, qyMoney.phone); %></td>
                                    <td class="tc"><%StringHelper.filterHTML(out, qyMoney.email);%></td>
                                    <td class="tc"><%=Formater.formatAmount(qyMoney.balance)%>
                                    </td>
                                    <td class="tc"><%=Formater.formatAmount(qyMoney.freezeFunds)%>
                                    </td>
                                    <td class="tc"><%=Formater.formatAmount(qyMoney.balance.add(qyMoney.freezeFunds))%>
                                    </td>
                                    <td class="tc"><%=Formater.formatAmount(qyMoney.riskAssureAmount)%>
                                    </td>
                                    <td class="tc"><%=Formater.formatAmount(qyMoney.advanceAmount)%>
                                    </td>
                                    <td class="tc"><%=Formater.formatAmount(qyMoney.advanceUnpaidAmount)%></td>
                                    <td class="tc"><%=Formater.formatAmount(qyMoney.tzzc)%>
                                    </td>
                                    <td class="tc"><%=Formater.formatAmount(qyMoney.earningsAmount)%>
                                    </td>
                                    <td class="tc"><%=Formater.formatAmount(qyMoney.payAmount)%>
                                    </td>
                                    <td class="tc"><%=Formater.formatAmount(qyMoney.withdrawAmount)%>
                                    </td>
                                    <td class="tc"><%=TimestampParser.format(qyMoney.startTime)%>
                                    </td>
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
                        <p class="mt5">
                            <span class="mr30">可用总金额：<em class="red"><%=Formater.formatAmount(searchAmount.balance)%>
                            </em> 元</span>
                            <span class="mr30">冻结总金额：<em class="red"><%=Formater.formatAmount(searchAmount.freezeFunds)%>
                            </em> 元</span>
                            <span class="mr30">账户总余额：<em class="red"><%=Formater.formatAmount(searchAmount.balance.add(searchAmount.freezeFunds))%>
                            </em> 元</span>
                            <span class="mr30">垫付总金额：<em class="red"><%=Formater.formatAmount(searchAmount.advanceAmount)%>
                            </em> 元</span>
                            <span class="mr30">垫付待还总金额：<em class="red"><%=Formater.formatAmount(searchAmount.advanceUnpaidAmount)%>
                            </em> 元</span>
                            <span class="mr30">理财总资产：<em class="red"><%=Formater.formatAmount(searchAmount.tzzc)%>
                            </em> 元</span>
                        </p>
                        <!--分页-->
                        <%AbstractConsoleServlet.rendPagingResult(out, list);%>
                        <!--分页 end-->
                    </div>
                </form>
            </div>
        </div>
    </div>
    <!--右边内容 结束-->
<%@include file="/WEB-INF/include/datepicker.jsp" %>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/validation.js"></script>
<script type="text/javascript">
    $(function () {

    });

    function showExport() {
        document.getElementById("form_loan").action = "<%=controller.getURI(request, ExportJgMoney.class)%>";
        $("#form_loan").submit();
        document.getElementById("form_loan").action = "<%=controller.getURI(request, JgMoney.class)%>";
    }
</script>
</body>
</html>