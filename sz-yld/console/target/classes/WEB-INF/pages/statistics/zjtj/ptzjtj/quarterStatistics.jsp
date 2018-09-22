<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.console.servlets.statistics.zjtj.ptzjtj.*"%>
<%@page import="com.dimeng.p2p.modules.statistics.console.service.FundsManage"%>
<%@page import="com.dimeng.p2p.modules.statistics.console.service.entity.QuarterFunds"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="java.util.Calendar" %>
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
    FundsManage manage = serviceSession.getService(FundsManage.class);
    QuarterFunds[] quarterFunds = manage.getQuarterFunds(year);
    BigDecimal inTotal = BigDecimal.ZERO;
    BigDecimal outTotal = BigDecimal.ZERO;
    BigDecimal sumTotal = BigDecimal.ZERO;
    for (QuarterFunds funds : quarterFunds) {
        inTotal = inTotal.add(funds.amountIn);
        outTotal = outTotal.add(funds.amountOut);
        sumTotal = sumTotal.add(funds.sum);
    }

    //YearFunds yearFunds = manage.getYearFunds(year);
    int[] options = manage.getStatisticedYear();


    DecimalFormat format = new DecimalFormat("#,##0.00");

    CURRENT_CATEGORY = "TJGL";
    CURRENT_SUB_CATEGORY = "PTZJTJ";
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <div class="p20">
                <div class="tabnav-container mb20">
		            <ul class="clearfix">
		              <li><a href="<%=controller.getURI(request, Funds.class) %>" class="tab-btn">年度统计</a></li>
		              <li><a href="javascript:void(0)" class="tab-btn select-a"><i class="icon-i tab-arrowtop-icon"></i>季度统计</a></li>
		              <li><a href="<%=controller.getURI(request, RosesStatistics.class) %>" class="tab-btn ">月度统计</a></li>
		            </ul>
		          </div>
                    <div class="border">
                        <div class="title-container"><i class="icon-i w30 h30 va-middle title-left-icon"></i>平台资金统计
                        </div>
                        <div class="content-container pl40 pt10 pr40">
                            <form method="post" action="<%=controller.getURI(request, QuarterStatistics.class) %>" id="search_form">
                                <div class="flat-line-container pt10 pb10">
                                    <ul class="gray6 input-list-container clearfix">
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
                                                class="icon-i w30 h30 va-middle search-icon "></i>搜索</a>
                                        </li>
                                        <li>
                                            <%if (dimengSession.isAccessableResource(QuarterExport.class)) { %>
                                            <a href="<%=controller.getURI(request, QuarterExport.class) %>?year=<%=year %>"
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
                            <tr class="dhsbg">
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
                            <tr class="dhsbg">
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
                            <tr class="dhsbg">
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
                                <h3 class="f16 fb"><%=year %>年季度平台资金统计折线图</h3>
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
var year = <%=year %>;
var amountIn = [0, 0, 0, 0];
var amountOut = [0, 0, 0, 0];
var amountSum = [0, 0, 0, 0];
var amountBalance = [0, 0, 0, 0];
<%for(QuarterFunds funds : quarterFunds){
    if(funds.quarter == 0) continue;
%>
amountIn[<%=funds.quarter-1%>] = <%=funds.amountIn%>;
amountOut[<%=funds.quarter-1%>] = <%=funds.amountOut%>;
amountSum[<%=funds.quarter-1%>] = <%=funds.sum%>;
<%}%>
</script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/highcharts.js"></script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/tjgl/quarterStatistics.js"></script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/exporting.js"></script>
</html>
