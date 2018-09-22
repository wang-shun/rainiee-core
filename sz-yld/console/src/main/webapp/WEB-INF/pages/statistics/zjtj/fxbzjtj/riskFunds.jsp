<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="java.util.Calendar"%>
<%@page import="com.dimeng.p2p.console.servlets.statistics.zjtj.fxbzjtj.RiskFunds"%>
<%@page import="com.dimeng.p2p.S61.entities.T6161" %>
<%@page import="com.dimeng.p2p.console.servlets.statistics.zjtj.fxbzjtj.RiskFundsExport" %>
<%@page import="com.dimeng.p2p.modules.statistics.console.service.RiskFundsManage" %>
<%@page import="com.dimeng.p2p.modules.statistics.console.service.entity.QuarterFunds" %>
<%-- <%@page import="com.dimeng.p2p.modules.statistics.console.service.entity.YearRiskFunds" %> --%>
<%@page import="java.math.BigDecimal" %>
<%@page import="com.dimeng.p2p.modules.statistics.console.service.entity.Sponsors" %>

<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <%@include file="/WEB-INF/include/script.jsp"%>
</head>
<body>
<%
    int year = IntegerParser.parse(request.getParameter("year"));
    if (year <= 0) {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
    }
    int orgId = request.getParameter("org") == null ? -1 : IntegerParser.parse(request.getParameter("org"));
    RiskFundsManage manage = serviceSession.getService(RiskFundsManage.class);
    QuarterFunds[] quarterFunds = manage.getQuarterFunds(orgId, year);
    /* YearRiskFunds yearRiskFunds = manage.getYearRiskFunds(orgId, year); */
    BigDecimal inTotal = BigDecimal.ZERO;
    BigDecimal outTotal = BigDecimal.ZERO;
    BigDecimal sumTotal = BigDecimal.ZERO;
    for (QuarterFunds funds : quarterFunds) {
        inTotal = inTotal.add(funds.amountIn);
        outTotal = outTotal.add(funds.amountOut);
        sumTotal = sumTotal.add(funds.sum);
    }

    int[] options = manage.getStatisticedYear();
    T6161[] orgList = manage.selectT6161();
    Sponsors[] sponsorsList = manage.selectSponsors();

    CURRENT_CATEGORY = "TJGL";
    CURRENT_SUB_CATEGORY = "FXBZJTJ";
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <div class="p20">
                    <div class="border">
                        <div class="title-container"><i class="icon-i w30 h30 va-middle title-left-icon"></i>风险保证金统计
                        </div>
                        <div class="content-container pl40 pt10 pr40">
                            <form method="post" action="<%=controller.getURI(request, RiskFunds.class) %>"
                                  id="search_form">
                                <div class="flat-line-container pt10 pb10">
                                    <ul class="gray6 input-list-container clearfix">
                                        <li>担保方：
                                            <select name="org" class="border mr20 h32 mw100">
                                                <option value="-1" <%=-1 == orgId ? "selected=\"selected\"" : "" %>>全部
                                                </option>
                                                <%
                                                    if (sponsorsList != null) {
                                                        for (Sponsors sponsors : sponsorsList) {
                                                %>
                                                <option value="<%=sponsors.sponsorsId %>" <%=sponsors.sponsorsId == orgId ? "selected=\"selected\"" : "" %>><%
                                                    StringHelper.filterHTML(out, sponsors.sponsorsName); %></option>
                                                <%
                                                        }
                                                    }
                                                %>
                                                <%
                                                    if (orgList != null) {
                                                        for (T6161 org : orgList) {
                                                            if (org == null) {
                                                                continue;
                                                            }
                                                %>
                                                <option value="<%=org.F01 %>" <%=org.F01 == orgId ? "selected=\"selected\"" : "" %>><%
                                                    StringHelper.filterHTML(out, org.F04); %></option>
                                                <%
                                                        }
                                                    }
                                                %>
                                            </select>
                                        </li>
                                        <li>年份：
                                            <select name="year" class="border mr20 h32 mw100">
                                                <%
                                                    if (options != null && options.length > 0) {
                                                        for (int option : options) {
                                                %>
                                                <option value="<%=option %>" <%=year == option ? "selected=\"selected\"" : "" %>><%=option %>
                                                    年
                                                </option>
                                                <%
                                                    }
                                                } else {
                                                %>
                                                <option><%=year %>年</option>
                                                <%} %>
                                            </select>
                                        </li>
                                        <li><a class="btn btn-blue radius-6 mr5 pl1 pr15" href="javascript:void(0);"
                                               onclick='$("#search_form").submit();'><i
                                                class="icon-i w30 h30 va-middle search-icon "></i>搜索</a></li>
                                        <li>
                                            <%if (dimengSession.isAccessableResource(RiskFundsExport.class)) { %>
                                            <a href="<%=controller.getURI(request, RiskFundsExport.class) %>?orgId=<%=orgId %>&year=<%=year %>"
                                               class="btn btn-blue radius-6 mr5  pl1 pr15"><i
                                                    class="icon-i w30 h30 va-middle export-icon "></i>导出</a>
                                            <%} else {%>
                                            <span class="btn btn-gray radius-6 mr5 pl1 pr15"><i
                                                    class="icon-i w30 h30 va-middle export-icon "></i>导出</span>
                                            <%} %>
                                        </li>
                                    </ul>
                                </div>
                            </form>
                        </div>
                    </div>

                    <div class="border table-container mt15">
                        <table class="table table-style gray6 tl">
                            <thead>
                            <tr class="title">
                                <th class="tc">交易类型</th>
                                <th class="tc">一季度</th>
                                <th class="tc">二季度</th>
                                <th class="tc">三季度</th>
                                <th class="tc">四季度</th>
                                <th class="tc">合计</th>
                            </tr>
                            </thead>
                            <tbody class="f12">
                            <tr class="title">
                                <td class="tc">收入(元)</td>
                                <td class="tc"><%=Formater.formatAmount(quarterFunds[0].amountIn) %>
                                </td>
                                <td class="tc"><%=Formater.formatAmount(quarterFunds[1].amountIn) %>
                                </td>
                                <td class="tc"><%=Formater.formatAmount(quarterFunds[2].amountIn) %>
                                </td>
                                <td class="tc"><%=Formater.formatAmount(quarterFunds[3].amountIn) %>
                                </td>
                                <td class="tc"><%=Formater.formatAmount(inTotal) %>
                                </td>
                            </tr>
                            <tr class="title">
                                <td class="tc">支出(元)</td>
                                <td class="tc"><%=Formater.formatAmount(quarterFunds[0].amountOut) %>
                                </td>
                                <td class="tc"><%=Formater.formatAmount(quarterFunds[1].amountOut) %>
                                </td>
                                <td class="tc"><%=Formater.formatAmount(quarterFunds[2].amountOut) %>
                                </td>
                                <td class="tc"><%=Formater.formatAmount(quarterFunds[3].amountOut) %>
                                </td>
                                <td class="tc"><%=Formater.formatAmount(outTotal) %>
                                </td>
                            </tr>
                            <tr class="title">
                                <td class="tc">盈亏(元)</td>
                                <td class="tc"><%=Formater.formatAmount(quarterFunds[0].sum) %>
                                </td>
                                <td class="tc"><%=Formater.formatAmount(quarterFunds[1].sum) %>
                                </td>
                                <td class="tc"><%=Formater.formatAmount(quarterFunds[2].sum) %>
                                </td>
                                <td class="tc"><%=Formater.formatAmount(quarterFunds[3].sum) %>
                                </td>
                                <td class="tc"><%=Formater.formatAmount(sumTotal) %>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </div>

                    <div class="border mt15">
                        <div class="content-container pl30 pt10 pr30">
                            <div class="two-title-container h30 lh30 border-b-s">
                                <h3 class="f16 fb"><%=year %>年季度风险保证金统计折线图</h3>
                            </div>
                            <div class="flat-line-container pt30 pb30 pr mh250">
                                <div id="line" style="min-width: 310px; height: 400px; margin: 0 auto"></div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <!--右边内容 结束-->
</body>
<script type="text/javascript">
    var amountIn = [0, 0, 0, 0];
    var amountOut = [0, 0, 0, 0];
    var amountSum = [0, 0, 0, 0];
    var amountBalance = [0, 0, 0, 0];
    <%for(QuarterFunds funds : quarterFunds){
        if(funds.quarter == 0) continue;
    %>
    amountIn[<%=funds.quarter-1%>] = <%=funds.amountOut%>;
    amountOut[<%=funds.quarter-1%>] = <%=funds.amountIn%>;
    amountSum[<%=funds.quarter-1%>] = <%=funds.sum%>;
    <%}%>
    var year = <%=year %>;
    <%-- var data = [{name: '垫付', data: [<%=yearRiskFunds.df %>]},
        {name: '垫付返还', data: [<%=yearRiskFunds.dffh %>]},
        {name: '借款成交服务费', data: [<%=yearRiskFunds.jkcjfwf %>]},
        {name: '手动增加保证金', data: [<%=yearRiskFunds.sdzjbzj %>]},
        {name: '手动扣除保证金', data: [<%=yearRiskFunds.sdkcbzj %>]}]; --%>
</script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/highcharts.js"></script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/riskFunds.js"></script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/exporting.js"></script>
</html>