<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.console.servlets.statistics.zjtj.ptzjtj.Funds"%>
<%@page import="com.dimeng.p2p.console.servlets.statistics.zjtj.ptzjtj.QuarterStatistics"%>
<%@page import="com.dimeng.p2p.console.servlets.statistics.zjtj.ptzjtj.RosesStatistics"%>
<%@page import="java.util.Calendar"%>
<%@page import="com.dimeng.p2p.modules.statistics.console.service.FundsManage" %>
<%@page import="com.dimeng.p2p.modules.statistics.console.service.entity.YearFunds" %>
<%@page import="java.math.BigDecimal" %>
<%@page import="java.text.DecimalFormat" %>
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
    YearFunds yearFunds = manage.getYearFunds(year);
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
		              <li><a href="javascript:void(0)" class="tab-btn select-a"><i class="icon-i tab-arrowtop-icon"></i>年度统计</a></li>
		              <li><a href="<%=controller.getURI(request, QuarterStatistics.class) %>" class="tab-btn ">季度统计</a></li>
		              <li><a href="<%=controller.getURI(request, RosesStatistics.class) %>" class="tab-btn ">月度统计</a></li>
		            </ul>
		          </div>
                    <div class="border">
                        <div class="title-container"><i class="icon-i w30 h30 va-middle title-left-icon"></i>平台资金统计
                        </div>
                        <div class="content-container pl40 pt10 pr40">
                            <form method="post" action="<%=controller.getURI(request, Funds.class) %>" id="search_form">
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
                                    </ul>
                                </div>
                            </form>
                        </div>
                    </div>

                    <div class="border mt15">
                        <div class="content-container pl30 pt10 pr30">
                            <div class="two-title-container h30 lh30 border-b-s">
                                <h3 class="f16 fb">平台收入统计柱形图</h3>
                            </div>
                            <div class="flat-line-container pt30 pb30 pr mh250">
                                <div id="income" style="min-width: 310px; height: 400px; margin: 0 auto"></div>
                            </div>
                        </div>
                    </div>
                    
                    <div class="border mt15">
                        <div class="content-container pl30 pt10 pr30">
                            <div class="two-title-container h30 lh30 border-b-s">
                                <h3 class="f16 fb">平台支出统计柱形图</h3>
                            </div>
                            <div class="flat-line-container pt30 pb30 pr mh250">
                                <div id="expenditure" style="min-width: 310px; height: 400px; margin: 0 auto"></div>
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
var incomeData = [{name: '成交服务费', data: [<%=yearFunds.jkglf%>]},
            {name: '理财管理费', data: [<%=yearFunds.lcglf%>]},
            {name: '债权转让手续费', data: [<%=yearFunds.zqzrf%>]},
            {name: '提前还款手续费', data: [<%=yearFunds.wyjsxf%>]},
            {name: '提现手续费', data: [<%=yearFunds.txsxf%>]},
            {name: '充值手续费', data: [<%=yearFunds.czsxf%>]}
           <%--  {name: '本金垫付返还', data: [<%=yearFunds.bjdffh%>]},
            {name: '利息垫付返还', data: [<%=yearFunds.lxdffh%>]},
            {name: '罚息垫付返还', data: [<%=yearFunds.fxdffh%>]} --%>
];

var expenditureData = [{name: '持续推广费用', data: [<%=yearFunds.cxtgfy%>]},
                       {name: '有效推广费用', data: [<%=yearFunds.yxtgfy%>]},
                       {name: '加息奖励费用', data: [<%=yearFunds.jxjlfy%>]},
                       {name: '体验金投资费用', data: [<%=yearFunds.tyjtzfy%>]},
                       {name: '红包奖励费用', data: [<%=yearFunds.hbjlfy%>]},
                       {name: '奖励标奖励费用', data: [<%=yearFunds.jlbjlfy%>]},
                       {name: '充值成本', data: [<%=yearFunds.czcb%>]},
                       {name: '提现成本', data: [<%=yearFunds.txcb%>]},
                       {name: '线下充值', data: [<%=yearFunds.xxcz%>]}
                      <%--  {name: '本金垫付支出', data: [<%=yearFunds.bjdfzc%>]},
                       {name: '利息垫付支出', data: [<%=yearFunds.lxdfzc%>]},
                       {name: '罚息垫付支出 ', data: [<%=yearFunds.fxdfzc%>]} --%>
];

</script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/highcharts.js"></script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/tjgl/funds.js"></script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/exporting.js"></script>
</html>
