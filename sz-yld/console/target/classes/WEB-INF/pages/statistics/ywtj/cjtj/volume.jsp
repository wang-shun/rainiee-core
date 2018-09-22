<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.console.servlets.statistics.ywtj.cjtj.Volume"%>
<%@page import="com.dimeng.p2p.console.servlets.statistics.ywtj.cjtj.VolumeExport" %>
<%@page import="com.dimeng.p2p.modules.statistics.console.service.entity.VolumeRegion" %>
<%@page import="com.dimeng.util.ObjectHelper" %>
<%@page import="com.dimeng.p2p.modules.statistics.console.service.entity.VolumeEntity" %>
<%@page import="com.dimeng.p2p.modules.statistics.console.service.entity.Profile" %>
<%@page import="com.dimeng.p2p.modules.statistics.console.service.entity.VolumeType" %>
<%@page import="com.dimeng.p2p.modules.statistics.console.service.entity.VolumeTimeLimit" %>
<%@page import="java.util.Calendar" %>
<%@page import="java.sql.Date" %>
<%@page import="java.math.BigDecimal" %>
<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <%@include file="/WEB-INF/include/script.jsp"%>
</head>
<body>
<%
    int year = IntegerParser.parse(request.getAttribute("year"));
    VolumeEntity[] vEntities = ObjectHelper.convertArray(request.getAttribute("vEntities"),VolumeEntity.class);
    VolumeEntity[] lvEntities = ObjectHelper.convertArray(request.getAttribute("lvEntities"), VolumeEntity.class);
    Profile profile = ObjectHelper.convert(request.getAttribute("profile"),Profile.class);
    VolumeType[] volumeTypes = ObjectHelper.convertArray(request.getAttribute("volumeTypes"), VolumeType.class);
    VolumeTimeLimit[] volumeTimeLimits = ObjectHelper.convertArray(request.getAttribute("volumeTimeLimits"), VolumeTimeLimit.class);
    VolumeRegion[] volumeRegions = ObjectHelper.convertArray(request.getAttribute("volumeRegions"), VolumeRegion.class);
    int[] options = ObjectHelper.convertIntArray(request.getAttribute("options"));

    CURRENT_CATEGORY = "TJGL";
    CURRENT_SUB_CATEGORY = "CJSJTJ";
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <div class="p20">
                    <div class="border">
                        <div class="title-container"><i class="icon-i w30 h30 va-middle title-left-icon"></i>成交数据统计
                        </div>
                        <div class="content-container pl40 pt10 pr40">
                            <form method="post" action="<%=controller.getURI(request, Volume.class) %>"
                                  id="search_form">
                                <div class="flat-line-container pt10 pb10">
                                    <ul class="gray6 input-list-container clearfix">
                                        <li>年份：
                                            <select name="year" class="border mr20 h32 mw100">
                                                <option value="0" <%=year <= 0 ? "selected=\"selected\"" : "" %>>全部</option>
                                                <%
                                                    if (options != null && options.length > 0) {
                                                        for (int option : options) {
                                                %>
                                                <option value="<%=option %>" <%=year == option ? "selected=\"selected\"" : "" %>><%=option %>
                                                   	 年
                                                </option>
                                                <%
                                                    }
                                                }
                                                %>
                                            </select>
                                        </li>
                                        <li><a class="btn btn-blue radius-6 mr5 pl1 pr15" href="javascript:void(0);"
                                               onclick='$("#search_form").submit();'><i
                                                class="icon-i w30 h30 va-middle search-icon "></i>搜索</a></li>
                                        <li>
                                            <%if (dimengSession.isAccessableResource(VolumeExport.class)) { %>
                                            <a href="<%=controller.getURI(request, VolumeExport.class) %>?year=<%=year %>"
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

                    <div class="border mt15">
                        <div class="content-container pl30 pt10 pr30">
                            <div class="two-title-container h30 lh30 border-b-s">
                                <h3 class="f16 fb">
                                    <%if(year>0){%>
                                        <%=year %>年成交金额同比增长柱形图
                                    <%}else{%>
                                        	平台成交总额<%=Formater.formatAmount(profile.totalAmount) %>
                                    <%}%>
                                </h3>
                                <%if(year>0){%>
                                <div>
                                    <p><%=year %>年成交总额<span
                                            class="blue"><%=Formater.formatAmount(profile.totalAmount) %></span>元
                                        <%if (profile.amountRate != null && profile.amountRate.floatValue() > 1) { %>
                                        	，同比增长<span
                                                class="blue"><%=(profile.amountRate.setScale(3).floatValue() - 1) * 100 %>%</span>
                                        <%} else if (profile.amountRate != null && profile.amountRate.floatValue() < 1) { %>
                                        	，同比下降<span
                                                class="blue"><%=(1 - profile.amountRate.setScale(3).floatValue()) * 100 %>%</span>
                                        <%} %>
                                    </p>
                                </div>
                                <%}%>
                            </div>
                            <div class="flat-line-container pt30 pb30 pr mh250">
                                <div id="column1" style="min-width: 310px; height: 400px; margin: 0 auto"></div>
                            </div>
                        </div>
                    </div>
                    <%if(year>0){%>
                    <div class="border mt15">
                        <div class="content-container pl30 pt10 pr30">
                            <div class="two-title-container h30 lh30 border-b-s">
                                <h3 class="f16 fb"><%=year %>年成交笔数同比增长柱形图</h3>

                                <div>
                                    <p><%=year %>年成交笔数<span
                                            class="blue"><%=profile.totalCount == null ? 0 : profile.totalCount.intValue() %></span>笔
                                        <%if (profile.countRate != null && profile.countRate.floatValue() > 1) { %>
                                        	，同比增长<span
                                                class="blue"><%=(profile.countRate.setScale(3).floatValue() - 1) * 100 %>%</span>
                                        <%} else if (profile.countRate != null && profile.countRate.floatValue() < 1) { %>
                                        	，同比下降<span
                                                class="blue"><%=(1 - profile.countRate.setScale(3).floatValue()) * 100 %>%</span>
                                        <%} %>
                                    </p>
                                </div>
                            </div>

                            <div class="flat-line-container pt30 pb30 pr mh250">
                                <div id="column2" style="min-width: 310px; height: 400px; margin: 0 auto"></div>
                            </div>
                        </div>
                    </div>
                    <%}%>
                    <div class="border mt15">
                        <div class="content-container pl30 pt10 pr30">
                            <div class="two-title-container h30 lh30 border-b-s">
                                <h3 class="f16 fb">
                                    <%if(year>0){%>
                                    <%=year %>年借款数据统计-按标的属性
                                    <%}else{%>
                                   	 平台借款数据统计-按标的属性
                                    <%}%>
                                </h3>
                            </div>
                            <div class="flat-line-container pt30 pb30 pr mh250">
                                <div class="w350 pa left0 top0 bottom0">
                                    <div id="pie1" style="min-width: 360px; height: 300px; margin: 0 auto"></div>
                                </div>
                                <div class="pl400 va-middle">
                                    <div class="border table-container">
                                        <table class="table table-style gray6 tl">
                                            <thead>
                                            <tr class="title">
                                                <th class="tc">类型</th>
                                                <th class="tc">笔数</th>
                                                <th class="tc">金额(元)</th>
                                            </tr>
                                            </thead>
                                            <tbody class="f12">
                                            <%for (int i = 0; i < 4; i++) { %>
                                            <tr>
                                                <td class="tc"><%=volumeTypes[i].type.getName() %>
                                                </td>
                                                <td class="tc"><%=volumeTypes[i].count %>
                                                </td>
                                                <td class="tc"><%=Formater.formatAmount(volumeTypes[i].amount) %>
                                                </td>
                                            </tr>
                                            <%} %>
                                            <tr>
                                                <td class="tc">总计</td>
                                                <td class="tc"><%=volumeTypes[4].count %>
                                                </td>
                                                <td class="tc"><%=Formater.formatAmount(volumeTypes[4].amount) %>
                                                </td>
                                            </tr>
                                            </tbody>
                                        </table>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="border mt15">
                        <div class="content-container pl30 pt10 pr30">
                            <div class="two-title-container h30 lh30 border-b-s">
                                <h3 class="f16 fb">
                                    <%if(year>0){%>
                                    <%=year %>年借款数据统计-按期限
                                    <%}else{%>
                                   		 平台借款数据统计-按期限
                                    <%}%>
                                </h3>
                            </div>
                            <div class="flat-line-container pt30 pb30 pr mh250">
                                <div class="w350 pa left0 top0 bottom0">
                                    <div id="pie2" style="min-width: 360px; height: 300px; margin: 0 auto"></div>
                                </div>
                                <div class="pl400 va-middle">
                                    <div class="border table-container">
                                        <table class="table table-style gray6 tl">
                                            <thead>
                                            <tr class="title">
                                                <th class="tc">期限(月)</th>
                                                <th class="tc">笔数</th>
                                                <th class="tc">金额(元)</th>
                                            </tr>
                                            </thead>
                                            <tbody class="f12">
                                            <tr>
                                                <td class="tc">1-3个月</td>
                                                <td class="tc"><%=volumeTimeLimits[0].count %>
                                                </td>
                                                <td class="tc"><%=Formater.formatAmount(volumeTimeLimits[0].amount) %>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td class="tc">4-6个月</td>
                                                <td class="tc"><%=volumeTimeLimits[1].count %>
                                                </td>
                                                <td class="tc"><%=Formater.formatAmount(volumeTimeLimits[1].amount) %>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td class="tc">7-9个月</td>
                                                <td class="tc"><%=volumeTimeLimits[2].count %>
                                                </td>
                                                <td class="tc"><%=Formater.formatAmount(volumeTimeLimits[2].amount) %>
                                                </td>
                                            </tr>

                                            <tr>
                                                <td class="tc">10-12个月</td>
                                                <td class="tc"><%=volumeTimeLimits[3].count %>
                                                </td>
                                                <td class="tc"><%=Formater.formatAmount(volumeTimeLimits[3].amount) %>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td class="tc">12-24个月</td>
                                                <td class="tc"><%=volumeTimeLimits[4].count %>
                                                </td>
                                                <td class="tc"><%=Formater.formatAmount(volumeTimeLimits[4].amount) %>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td class="tc">24个月以上</td>
                                                <td class="tc"><%=volumeTimeLimits[5].count %>
                                                </td>
                                                <td class="tc"><%=Formater.formatAmount(volumeTimeLimits[5].amount) %>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td class="tc">其他(天标)</td>
                                                <td class="tc"><%=volumeTimeLimits[6].count %>
                                                </td>
                                                <td class="tc"><%=Formater.formatAmount(volumeTimeLimits[6].amount) %>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td class="tc">总计</td>
                                                <td class="tc"><%=volumeTimeLimits[7].count %>
                                                </td>
                                                <td class="tc"><%=Formater.formatAmount(volumeTimeLimits[7].amount) %>
                                                </td>
                                            </tr>
                                            </tbody>
                                        </table>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="border mt15">
                        <div class="content-container pl30 pt10 pr30">
                            <div class="two-title-container h30 lh30 border-b-s">
                                <h3 class="f16 fb">
                                    <%if(year>0){%>
                                    <%=year %>年借款数据统计-按地域
                                    <%}else{%>
                                    平台借款数据统计-按地域
                                    <%}%>
                                </h3>
                            </div>
                            <div class="flat-line-container pt30 pb30 pr mh250">
                                <div class="w350 pa left0 top0 bottom0">
                                    <%if (volumeTimeLimits.length > 0) { %>
                                    <div id="pie3" style="min-width: 360px; height: 300px; margin: 0 auto"></div>
                                    <%} %>
                                </div>
                                <div class="pl400 va-middle">
                                    <div class="border table-container">
                                        <table class="table table-style gray6 tl">
                                            <thead>
                                            <tr class="title">
                                                <th class="tc">地域</th>
                                                <th class="tc">笔数</th>
                                                <th class="tc">金额(元)</th>
                                            </tr>
                                            </thead>
                                            <tbody class="f12">
                                            <%for (VolumeRegion volumeRegion : volumeRegions) { %>
                                            <tr>
                                                <td class="tc"><%=volumeRegion.province %>
                                                </td>
                                                <td class="tc"><%=volumeRegion.count %>
                                                </td>
                                                <td class="tc"><%=Formater.formatAmount(volumeRegion.amount) %>
                                                </td>
                                            </tr>
                                            <%} %>
                                            </tbody>
                                        </table>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                </div>
            </div>
        </div>
    </div>
    <!--右边内容 结束-->
<script type="text/javascript">
    var y = <%=year %>;
    var columnData1 = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0];
    var lastData1 = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0];
    var columnData2 = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0];
    var lastData2 = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0];
    <%if(year>0){for(VolumeEntity entity : vEntities){%>
    columnData1[<%=entity.month-1%>] = <%=entity.amount%>;
    columnData2[<%=entity.month-1%>] = <%=entity.count%>;
    <%}}else{%>
    <%
     String monthDataStr="";
     int flag = 12 - vEntities.length;

     int j =0;
     int k =11;
     for(int i = 0;i<12;i++){
        Calendar calend = Calendar.getInstance();
        calend.add(Calendar.MONTH, -k);
        Date date = new Date(calend.getTime().getTime());
        monthDataStr +="'"+DateParser.format(date,"MM/yy")+"',";
        if(i<flag){
     %>
            columnData1[<%=i%>] = 0;
     <%}else{ %>
         columnData1[<%=i%>] = <%=vEntities[j].amount%>;
     <%  j++;}k--; %>
    <%}%>

    var monthData = [
        <%=monthDataStr%>
    ];
<%}%>
    <%for(VolumeEntity entity : lvEntities){%>
    lastData1[<%=entity.month-1%>] = <%=entity.amount%>;
    lastData2[<%=entity.month-1%>] = <%=entity.count%>;
    <%}%>
    var data1 = [
        <%for(int i=0; i<4; i++){if(0 == i){%>
        ['<%=volumeTypes[i].type.getName() %>', <%=volumeTypes[i].amount %>]
        <%}else{%>
        , ['<%=volumeTypes[i].type.getName() %>', <%=volumeTypes[i].amount %>]
        <%}}%>
    ];
    var data2 = [
        ['1-3个月', <%=volumeTimeLimits[0].amount %>],
        ['4-6个月', <%=volumeTimeLimits[1].amount %>],
        ['7-9个月', <%=volumeTimeLimits[2].amount %>],
        ['10-12个月', <%=volumeTimeLimits[3].amount %>],
        ['12-24个月', <%=volumeTimeLimits[4].amount %>],
        ['24个月以上', <%=volumeTimeLimits[5].amount %>],
        ['其他(天标)', <%=volumeTimeLimits[6].amount %>]
    ];
    <%if(volumeTimeLimits.length > 0){ %>
    var data3 = [
          <%for(int i=0;i<volumeRegions.length-1;i++){%>
          		<%if(i<9){%>
          		['<%=volumeRegions[i].province%>',<%=volumeRegions[i].amount%>]<%if(i<volumeRegions.length-2){out.print(",");}%>
          		<%}else{%>
          			<%
          			BigDecimal amountOth = BigDecimal.ZERO;
          			for(i=9;i<volumeRegions.length-1;i++){
          				amountOth=amountOth.add(volumeRegions[i].amount);
          			}
          			%>
          		['其他',<%=amountOth%>]
          		<%}%>
          <%}%>
	];
    <%}%>
</script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/highcharts.js"></script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/volume.js"></script>
</body>
</html>