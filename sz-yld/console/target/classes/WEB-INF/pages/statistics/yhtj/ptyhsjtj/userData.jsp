<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.console.servlets.statistics.yhtj.ptyhsjtj.UserDataReport" %>
<%@page import="com.dimeng.util.ObjectHelper" %>
<%@page import="java.util.Map" %>
<%@ page import="java.util.List" %>
<%@ page import="com.sun.org.apache.bcel.internal.generic.NEW" %>
<%@ page import="com.dimeng.p2p.console.servlets.statistics.yhtj.ptyhsjtj.UserData" %>
<%@ page import="java.util.Calendar" %>
<%@ page import="java.util.Date" %>
<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <%@include file="/WEB-INF/include/script.jsp"%>
</head>
<body>
<%
    Map<String,Integer> result = ObjectHelper.convert(request.getAttribute("userData"), Map.class);
    Map<String,Object> data = ObjectHelper.convert(request.getAttribute("result"), Map.class);
    String createTimeStart = request.getParameter("startDate");
    String createTimeEnd = request.getParameter("endDate");
    CURRENT_CATEGORY = "TJGL";
    CURRENT_SUB_CATEGORY = "PTYHSJTJ";
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <div class="p20">
                    <div class="border">
                        <div class="title-container"><i class="icon-i w30 h30 va-middle title-left-icon"></i>平台用户数据统计
                        </div>
                        <%--<div class="content-container pl40 pt10 pr40">
                            <form method="post" action="<%=controller.getURI(request, UserData.class) %>"
                                  id="search_form">
                                <div class="flat-line-container pt10 pb10">
                                    <ul class="gray6 input-list-container clearfix">
                                        <li>年份：
                                            <select name="year" class="border mr20 h32 mw100">

                                                <option value="0" <%=year <= 0 ? "selected=\"selected\"" : "" %>>全部</option>
                                                <%
                                                        for (int option : options) {
                                                %>
                                                <option value="<%=option %>" <%=year == option ? "selected=\"selected\"" : "" %>><%=option %>
                                                    年
                                                </option>
                                                <%}%>
                                            </select>
                                        </li>
                                        <li><a class="btn btn-blue radius-6 mr5 pl1 pr15" href="javascript:void(0);"
                                               onclick='$("#search_form").submit();'><i
                                                class="icon-i w30 h30 va-middle search-icon "></i>搜索</a></li>
                                    </ul>
                                </div>
                            </form>
                        </div>--%>

                        <div class="content-container pl20 pt10 pr40">
                        <div class="mb10">
                            <ul class="gray6 input-list-container clearfix">
                                <li>
                                    <div class="border mr10 mb10">
                                        <div class="title-container pr10">平台用户总数</div>
                                        <div class="tc pt20 pb20"><%=result.get("TOTAL") == null ? 0 : result.get("TOTAL")%></div>
                                    </div>
                                </li>
                                <li>
                                    <div class="border mr10 mb10">
                                        <div class="title-container pr10">PC端注册用户数</div>
                                        <div class="tc pt20 pb20"><%=result.get("PCZC") == null ? 0 : result.get("PCZC")%></div>
                                    </div>
                                </li>
                                <li>
                                    <div class="border mr10 mb10">
                                        <div class="title-container pr10">APP端注册用户数</div>
                                        <div class="tc pt20 pb20"><%=result.get("APPZC") == null ? 0 : result.get("APPZC")%></div>
                                    </div>
                                </li>
                                <li>
                                    <div class="border mr10 mb10">
                                        <div class="title-container pr10">微信端注册用户数</div>
                                        <div class="tc pt20 pb20"><%=result.get("WXZC") == null ? 0 : result.get("WXZC")%></div>
                                    </div>
                                </li>
                                <li>
                                    <div class="border mr50 mb10">
                                        <div class="title-container pr10">后台注册用户数</div>
                                        <div class="tc pt20 pb20"><%=result.get("HTTJ") == null ? 0 : result.get("HTTJ")%></div>
                                    </div>
                                </li>
                                <li>
                                    <div class="border mr10 mb10">
                                        <div class="title-container pr10">充值用户数</div>
                                        <div class="tc pt20 pb20"><%=result.get("CHARGE") == null ? 0 : result.get("CHARGE")%></div>
                                    </div>
                                </li>
                                <li>
                                    <div class="border mr10 mb10">
                                        <div class="title-container pr10">提现用户数</div>
                                        <div class="tc pt20 pb20"><%=result.get("WITHDRAW") == null ? 0 : result.get("WITHDRAW")%></div>
                                    </div>
                                </li>
                                <li>
                                    <div class="border mr10 mb10">
                                        <div class="title-container pr10">投资用户数</div>
                                        <div class="tc pt20 pb20"><%=result.get("INVEST") == null ? 0 : result.get("INVEST")%></div>
                                    </div>
                                </li>
                                <li>
                                    <div class="border mr10 mb10">
                                        <div class="title-container pr10">借款用户数</div>
                                        <div class="tc pt20 pb20"><%=result.get("LOAN") == null ? 0 : result.get("LOAN")%></div>
                                    </div>
                                </li>
                            </ul>
                        </div>
                    </div>

                    <div class="border mt15">
                        <div class="tabnav-container">
                            <ul class="clearfix">
                                <li><a href="javascript:void(0)" class="tab-btn  select-a">趋势图<i class="icon-i tab-arrowtop-icon"></i></a></li>
                                <li><a href="<%=controller.getURI(request,UserDataReport.class)%>" class="tab-btn ">统计报表</a></li>
                            </ul>
                        </div>
                        <form method="post" action="<%=controller.getURI(request, UserData.class) %>"
                              id="search_form">
                            <input type="hidden" id="type" name="type" value="<%=request.getParameter("type")%>"/>
                        <div class="tab-content-container p20">

                            <!--box1-->
                            <div class="tab-item">
                                <div class="content-container">
                                    <ul class="gray6 input-list-container clearfix">
                                        <li><span class="display-ib mr5">注册时间</span>
                                            <input type="text" name="startDate" class="text border pl5 w120 date" value="" id="datepicker1" readonly="readonly"/>
                                            <span class="pl5 pr5">至</span>
                                            <input type="text" name="endDate" class="text border pl5 w120 mr20 date" value="" id="datepicker2" readonly="readonly"/>
                                        </li>
                                        <li>
                                            <div class="mr20">
                                                <table class="table table-time-tab">
                                                    <tr>
                                                        <td <%if(StringHelper.isEmpty(request.getParameter("type")) || "day".equals(request.getParameter("type"))){%>class="cur"<%}%> onclick="searchData('day')">天</td>
                                                        <td <%if("week".equals(request.getParameter("type"))){%>class="cur"<%}%> onclick="searchData('week')">周</td>
                                                        <td <%if("month".equals(request.getParameter("type"))){%>class="cur"<%}%> onclick="searchData('month')">月</td>
                                                        <td <%if("quarter".equals(request.getParameter("type"))){%>class="cur"<%}%> onclick="searchData('quarter')">季</td>
                                                        <td <%if("year".equals(request.getParameter("type"))){%>class="cur"<%}%> onclick="searchData('year')">年</td></tr>
                                                </table>
                                            </div>
                                        </li>
                                        <li><a class="btn btn-blue radius-6 mr5 pl1 pr15" href="javascript:void(0)" onclick="$('#search_form').submit()"><i class="icon-i w30 h30 va-middle search-icon "></i>搜索</a></li>
                                    </ul>
                                </div>
                                <div class="tc p20" id="register"></div>
                            </div>
                            <!--box1 end-->
                        </div>
                        </form>
                    </div>

                </div>
            </div>
        </div>
    </div>
    <!--右边内容 结束-->
</body>
<%@include file="/WEB-INF/include/datepicker.jsp"%>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/highcharts.js"></script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/exporting.js"></script>
<%--<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/userData.js"></script>--%>
<script type="text/javascript">

    $(function() {
        $("#datepicker1").datepicker({
            inline: true,
            onSelect : function(selectedDate) {
                $("#datepicker2").datepicker("option", "minDate", selectedDate);  }
        });
        $('#datepicker1').datepicker('option', {dateFormat:'yy-mm-dd'});
        $("#datepicker2").datepicker({inline: true});
        $('#datepicker2').datepicker('option', {dateFormat:'yy-mm-dd'});
        <%
        Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		Date startDate = calendar.getTime();
		Date endDate = new Date();
        if(!StringHelper.isEmpty(createTimeStart)){%>
        $("#datepicker1").val("<%StringHelper.filterHTML(out, createTimeStart);%>");
        <%}else{%>
        $("#datepicker1").val("<%StringHelper.filterHTML(out, DateParser.format(startDate));%>");
        <%}%>
        <%if(!StringHelper.isEmpty(createTimeEnd)){%>
        $("#datepicker2").val("<%StringHelper.filterHTML(out, createTimeEnd);%>");
        <%}else{%>
        $("#datepicker2").val("<%StringHelper.filterHTML(out, DateParser.format(endDate));%>");
        <%}%>
        $("#datepicker2").datepicker('option', 'minDate',$("#datepicker1").datepicker().val());

        var registerData = <%=data.get("datas")%>;

        <%
        StringBuilder areaStr  = new StringBuilder("[");
        int i = 0;
        for(String str : (List<String>)data.get("areas")){
            i++;
            areaStr.append("'");
            areaStr.append(str);
            areaStr.append("'");
            if(i < ((List<String>)data.get("areas")).size()){
                areaStr.append(",");
            }
        }

        areaStr.append("]");
        %>
        var areas = <%=areaStr.toString()%>;
        var data1 = [{name: '注册用户累计',data: registerData}];
        $('#register').highcharts({
            title: {
                text: '<strong>注册用户累计趋势图</strong>'
            },
            colors:['#336699','#CC6666'],
            chart: {
                type: 'spline'
            },
            exporting:{
                enabled: true
            },
            yAxis: {
            	min:0,
            	allowDecimals:false,
                title: {
                    text: '单位：人'
                },
                labels:{
                    formatter : function() {//设置纵坐标值的样式
                        return this.value;
                    }
                },
                plotLines: [{
                    value: 0,
                    width: 1,
                    color: '#808080'
                }]
            },
            xAxis: {
                categories: areas,
                labels:{rotation:-45,step:1}
            },
            credits: {
                enabled: false
            },
            legend: {
                enabled: false
            },
            tooltip: {
                formatter:function(){
                    return '<b>'+this.x + '</b><br/>' + this.series.name + ':' + Highcharts.numberFormat(this.y,0);
                }
            },
            series: data1
        });
    });


    function searchData(type){
        var sDate = $("#datepicker1").val();
        var eDate = $("#datepicker2").val();
        if(sDate == "" || eDate == ""){
            $("div.popup_bg").show();
            $("#info").html(showDialogInfo('注册时间不能为空！', "wrong"));
            return;
        }
        var sTime = new Date(sDate);
        var eTime = new Date(eDate);
        var day = (eTime - sTime)/(1000*3600*24);
        if(type == "day" && day > 31){
            $("div.popup_bg").show();
            $("#info").html(showDialogInfo('按天统计，日期范围不能超过31天', "wrong"));
            return;
        }

        if(type == "week" && day/7 > 30){
            $("div.popup_bg").show();
            $("#info").html(showDialogInfo('按周统计，日期范围不能超过30周', "wrong"));
            return;
        }

        if((type == "month" || type == "quarter") && sTime.getFullYear() != eTime.getFullYear()){
            $("div.popup_bg").show();
            $("#info").html(showDialogInfo('按月或按季度统计，日期必须选择同一年', "wrong"));
            return;
        }
        $("#type").val(type);
        $('#search_form').submit();
    }

</script>
<div id="info"></div>
<div class="popup_bg" style="display: none;"></div>
<script type="text/javascript"
        src="<%=controller.getStaticPath(request)%>/js/dialog.js"></script>
<%
    String warringMessage = controller.getPrompt(request, response, PromptLevel.WARRING);
    if (!StringHelper.isEmpty(warringMessage)) {
%>
<script type="text/javascript">
    $("div.popup_bg").show();
    $("#info").html(showDialogInfo('<%=warringMessage%>', "wrong"));
</script>
<%} %>
<%
    String infoMessage = controller.getPrompt(request, response, PromptLevel.INFO);
    if (!StringHelper.isEmpty(infoMessage)) {
        controller.clearAll(request,response);
%>
<script type="text/javascript">
    $("div.popup_bg").show();
    $("#info").html(showDialogInfo('<%=infoMessage%>', "yes"));
</script>
<%} %>
</html>
