<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.console.servlets.statistics.zjtj.cztxtj.RechargeWithdraw"%>
<%@page import="java.util.Calendar"%>
<%@page import="com.dimeng.p2p.console.servlets.statistics.zjtj.cztxtj.RecWitExport" %>
<%@page import="com.dimeng.p2p.modules.statistics.console.service.RechargeWithdrawManage" %>
<%@page import="com.dimeng.p2p.console.servlets.statistics.zjtj.cztxtj.RechargeWithdrawStatistics" %>
<%@page import="com.dimeng.p2p.modules.statistics.console.service.entity.RecWit" %>
<%@page import="com.dimeng.p2p.modules.statistics.console.service.entity.RecWitTotal" %>
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
    RechargeWithdrawManage manage = serviceSession.getService(RechargeWithdrawManage.class);
    int[] options = manage.getStatisticedYear();
    RecWit[] rws = manage.getRechargeWithdraws(year);
    RecWitTotal recWitTotal = manage.getRecWitTotal(year);

    DecimalFormat format = new DecimalFormat("#,##0.00");

    CURRENT_CATEGORY = "TJGL";
    CURRENT_SUB_CATEGORY = "CZTXSJTJ";
    
    String startTime = "";
	String endTime = "";
    Calendar calendar = Calendar.getInstance();
    String year_now = calendar.get(Calendar.YEAR)+"";
    String month = (calendar.get(Calendar.MONTH)>9?(calendar.get(Calendar.MONTH)+1):("0"+(calendar.get(Calendar.MONTH)+1)))+"";
    String day = (calendar.get(Calendar.DAY_OF_MONTH)>9?calendar.get(Calendar.DAY_OF_MONTH):calendar.get(Calendar.DAY_OF_MONTH))+"";
    if (startTime == null || StringHelper.isEmpty(startTime)) {
       startTime=year_now+"-"+month+"-"+"01";
    }
    if (endTime == null || StringHelper.isEmpty(endTime)) {
       endTime=year_now+"-"+month+"-"+day;
     }
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <div class="p20">
                    <div class="border">
                        <div class="title-container"><i class="icon-i w30 h30 va-middle title-left-icon"></i>充值提现数据统计
                        </div>
                        <div class="content-container pl40 pt10 pr40">
                            <form method="post" action="<%=controller.getURI(request, RechargeWithdraw.class)%>"
                                  id="search_form">
                                <div class="flat-line-container pt10 pb10">
                                    <ul class="gray6 input-list-container clearfix">
                                        <li>年份：
                                            <select name="year" class="border mr20 h32 mw100">
                                                <%
                                                    if (options != null && options.length > 0) {
                                                                                                for (int option : options) {
                                                %>
                                                <option value="<%=option%>" <%=year == option ? "selected=\"selected\"" : ""%>><%=option%>
                                                    年
                                                </option>
                                                <%
                                                    }
                                                                                        } else {
                                                %>
                                                <option><%=year%>年</option>
                                                <%
                                                    }
                                                %>
                                            </select>
                                        </li>
                                        <li><a class="btn btn-blue radius-6 mr5 pl1 pr15" href="javascript:void(0);"
                                               onclick='$("#search_form").submit();'><i
                                                class="icon-i w30 h30 va-middle search-icon "></i>搜索</a></li>
                                        <li>
                                            <%
                                                if (dimengSession.isAccessableResource(RecWitExport.class)) {
                                            %>
                                            <a href="<%=controller.getURI(request, RecWitExport.class)%>?year=<%=year%>"
                                               class="btn btn-blue radius-6 mr5  pl1 pr15"><i
                                                    class="icon-i w30 h30 va-middle export-icon "></i>导出</a>
                                            <%
                                                } else {
                                            %>
                                            <span class="btn btn-gray radius-6 mr5 pl1 pr15"><i
                                                    class="icon-i w30 h30 va-middle export-icon "></i>导出</span>
                                            <%
                                                }
                                            %>
                                        </li>
                                    </ul>
                                </div>
                            </form>
                        </div>
                    </div>

					<div class="border mt20">
					<div class="tabnav-container">
                        <ul class="clearfix">
                            <li><a href="javascript:void(0);" class="tab-btn select-a">趋势图<i
                                class="icon-i tab-arrowtop-icon"></i></a></li>
                            <li><a href="<%=controller.getURI(request, RechargeWithdrawStatistics.class)%>?startTime=<%=startTime %>&endTime=<%=endTime %>"
                                   class="tab-btn ">统计报表</a></li>

                        </ul>
                    </div>
                    <div class="table-container mt15 m10">
                        <table class="table table-style gray6 tl">
                            <thead>
                            <tr class="title">
                                <th class="tc">月份</th>
                                <th class="tc">充值金额(元)</th>
                                <th class="tc">提现金额(元)</th>
                                <th class="tc">充值笔数</th>
                                <th class="tc">提现笔数</th>
                            </tr>
                            </thead>
                            <tbody class="f12">
                            <tr class="title">
                                <td class="tc">一月份</td>
                                <td class="tc"><%=Formater.formatAmount(rws[0].recharge) %>
                                </td>
                                <td class="tc"><%=Formater.formatAmount(rws[0].withdraw) %>
                                </td>
                                <td class="tc"><%=rws[0].rechargeCount %>
                                </td>
                                <td class="tc"><%=rws[0].withdrawCount %>
                                </td>
                            </tr>
                            <tr class="title">
                                <td class="tc">二月份</td>
                                <td class="tc"><%=Formater.formatAmount(rws[1].recharge) %>
                                </td>
                                <td class="tc"><%=Formater.formatAmount(rws[1].withdraw) %>
                                </td>
                                <td class="tc"><%=rws[1].rechargeCount %>
                                </td>
                                <td class="tc"><%=rws[1].withdrawCount %>
                                </td>
                            </tr>
                            <tr class="title">
                                <td class="tc">三月份</td>
                                <td class="tc"><%=Formater.formatAmount(rws[2].recharge) %>
                                </td>
                                <td class="tc"><%=Formater.formatAmount(rws[2].withdraw) %>
                                </td>
                                <td class="tc"><%=rws[2].rechargeCount %>
                                </td>
                                <td class="tc"><%=rws[2].withdrawCount %>
                                </td>
                            </tr>
                            <tr class="title">
                                <td class="tc">四月份</td>
                                <td class="tc"><%=Formater.formatAmount(rws[3].recharge) %>
                                </td>
                                <td class="tc"><%=Formater.formatAmount(rws[3].withdraw) %>
                                </td>
                                <td class="tc"><%=rws[3].rechargeCount %>
                                </td>
                                <td class="tc"><%=rws[3].withdrawCount %>
                                </td>
                            </tr>
                            <tr class="title">
                                <td class="tc">五月份</td>
                                <td class="tc"><%=Formater.formatAmount(rws[4].recharge) %>
                                </td>
                                <td class="tc"><%=Formater.formatAmount(rws[4].withdraw) %>
                                </td>
                                <td class="tc"><%=rws[4].rechargeCount %>
                                </td>
                                <td class="tc"><%=rws[4].withdrawCount %>
                                </td>
                            </tr>
                            <tr class="title">
                                <td class="tc">六月份</td>
                                <td class="tc"><%=Formater.formatAmount(rws[5].recharge) %>
                                </td>
                                <td class="tc"><%=Formater.formatAmount(rws[5].withdraw) %>
                                </td>
                                <td class="tc"><%=rws[5].rechargeCount %>
                                </td>
                                <td class="tc"><%=rws[5].withdrawCount %>
                                </td>
                            </tr>
                            <tr class="title">
                                <td class="tc">七月份</td>
                                <td class="tc"><%=Formater.formatAmount(rws[6].recharge) %>
                                </td>
                                <td class="tc"><%=Formater.formatAmount(rws[6].withdraw) %>
                                </td>
                                <td class="tc"><%=rws[6].rechargeCount %>
                                </td>
                                <td class="tc"><%=rws[6].withdrawCount %>
                                </td>
                            </tr>
                            <tr class="title">
                                <td class="tc">八月份</td>
                                <td class="tc"><%=Formater.formatAmount(rws[7].recharge) %>
                                </td>
                                <td class="tc"><%=Formater.formatAmount(rws[7].withdraw) %>
                                </td>
                                <td class="tc"><%=rws[7].rechargeCount %>
                                </td>
                                <td class="tc"><%=rws[7].withdrawCount %>
                                </td>
                            </tr>

                            <tr class="title">
                                <td class="tc">九月份</td>
                                <td class="tc"><%=Formater.formatAmount(rws[8].recharge) %>
                                </td>
                                <td class="tc"><%=Formater.formatAmount(rws[8].withdraw) %>
                                </td>
                                <td class="tc"><%=rws[8].rechargeCount %>
                                </td>
                                <td class="tc"><%=rws[8].withdrawCount %>
                                </td>
                            </tr>
                            <tr class="title">
                                <td class="tc">十月份</td>
                                <td class="tc"><%=Formater.formatAmount(rws[9].recharge) %>
                                </td>
                                <td class="tc"><%=Formater.formatAmount(rws[9].withdraw) %>
                                </td>
                                <td class="tc"><%=rws[9].rechargeCount %>
                                </td>
                                <td class="tc"><%=rws[9].withdrawCount %>
                                </td>
                            </tr>
                            <tr class="title">
                                <td class="tc">十一月份</td>
                                <td class="tc"><%=Formater.formatAmount(rws[10].recharge) %>
                                </td>
                                <td class="tc"><%=Formater.formatAmount(rws[10].withdraw) %>
                                </td>
                                <td class="tc"><%=rws[10].rechargeCount %>
                                </td>
                                <td class="tc"><%=rws[10].withdrawCount %>
                                </td>
                            </tr>
                            <tr class="title">
                                <td class="tc">十二月份</td>
                                <td class="tc"><%=Formater.formatAmount(rws[11].recharge) %>
                                </td>
                                <td class="tc"><%=Formater.formatAmount(rws[11].withdraw) %>
                                </td>
                                <td class="tc"><%=rws[11].rechargeCount %>
                                </td>
                                <td class="tc"><%=rws[11].withdrawCount %>
                                </td>
                            </tr>
                            <tr class="title">
                                <td class="tc">总计</td>
                                <td class="tc"><%=Formater.formatAmount(recWitTotal.recharge) %>
                                </td>
                                <td class="tc"><%=Formater.formatAmount(recWitTotal.withdraw) %>
                                </td>
                                <td class="tc"><%=recWitTotal.rechargeCount %>
                                </td>
                                <td class="tc"><%=recWitTotal.withdrawCount %>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </div>

                    <div class="border mt15 m10">
                        <div class="content-container pl30 pt10 pr30">
                            <div class="two-title-container h30 lh30 border-b-s">
                                <h3 class="f16 fb"><%=year %>年充值提现金额数据折线图</h3>
                            </div>
                            <div class="flat-line-container pt30 pb30 pr mh250">
                                <div id="line1" style="min-width: 310px; height: 400px; margin: 0 auto"></div>
                            </div>
                        </div>
                    </div>

                    <div class="border mt15 m10">
                        <div class="content-container pl30 pt10 pr30">
                            <div class="two-title-container h30 lh30 border-b-s">
                                <h3 class="f16 fb"><%=year %>年充值提现笔数数据折线图</h3>
                            </div>
                            <div class="flat-line-container pt30 pb30 pr mh250">
                                <div id="line2" style="min-width: 310px; height: 400px; margin: 0 auto"></div>
                            </div>
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
    var d1 = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0];
    var d2 = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0];
    <%for(RecWit rw : rws){if(rw.month>0){%>
    d1[<%=rw.month-1 %>] = <%=rw.recharge %>;
    d2[<%=rw.month-1 %>] = <%=rw.withdraw %>;
    <%}}%>
    var dd1 = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0];
    var dd2 = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0];
    <%for(RecWit rw : rws){if(rw.month>0){%>
    dd1[<%=rw.month-1 %>] = <%=rw.rechargeCount %>;
    dd2[<%=rw.month-1 %>] = <%=rw.withdrawCount %>;
    <%}}%>
</script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/highcharts.js"></script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/rechargeWithdraw.js"></script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/exporting.js"></script>
</html>