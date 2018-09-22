<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.S70.entities.T7052" %>
<%@page import="com.dimeng.p2p.console.servlets.AbstractConsoleServlet" %>
<%@page import="com.dimeng.p2p.console.servlets.statistics.yhtj.ptyhsjtj.UserData" %>
<%@ page import="com.dimeng.util.ObjectHelper" %>
<%@ page import="java.util.Map" %>
<%@ page import="com.dimeng.p2p.console.servlets.statistics.yhtj.ptyhsjtj.UserDataReport" %>
<%@ page import="com.dimeng.p2p.console.servlets.statistics.yhtj.ptyhsjtj.ExportUserDataReport" %>
<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <%@include file="/WEB-INF/include/script.jsp"%>
</head>
<body>
<%
    Map<String,Integer> result = ObjectHelper.convert(request.getAttribute("userData"), Map.class);
    PagingResult<T7052> pagingResult = (PagingResult<T7052>) request.getAttribute("pagingResult");
    String createTimeStart = null == request.getAttribute("startDate") ? request.getParameter("startDate") : request.getAttribute("startDate").toString();
    String createTimeEnd = null == request.getAttribute("endDate") ? request.getParameter("endDate") : request.getAttribute("endDate").toString();
    T7052 totalUserData = (T7052) request.getAttribute("totalUserData");
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <div class="p20">
                    <div class="border">
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
                                <li><a href="<%=controller.getURI(request,UserData.class)%>" class="tab-btn">趋势图</a></li>
                                <li><a href="javascript:void(0)" class="tab-btn  select-a">统计报表<i class="icon-i tab-arrowtop-icon"></i></a></li>
                            </ul>
                        </div>
                        <form
                                action="<%=controller.getURI(request, UserDataReport.class)%>"
                                method="post" name="form1" id="form1">
                        <div class="tab-content-container p20">
                            <!--box2-->
                            <div class="tab-item">
                                <div class="content-container">
                                    <ul class="gray6 input-list-container clearfix">
                                        <li><span class="display-ib mr5">日期</span>
                                            <input type="text" name="startDate" class="text border pl5 w120 date" value="" id="datepicker1" readonly="readonly"/>
                                            <span class="pl5 pr5">至</span>
                                            <input type="text" name="endDate" class="text border pl5 w120 mr20 date" value="" id="datepicker2" readonly="readonly"/>
                                        </li>
                                        <li><a class="btn btn-blue radius-6 mr5 pl1 pr15" onclick="$('#form1').submit()" href="javascript:void(0);"><i class="icon-i w30 h30 va-middle search-icon "></i>搜索</a></li>

                                        <li>

                                            <%
                                                if (dimengSession.isAccessableResource(ExportUserDataReport.class)) {
                                            %>
                                            <a class="btn btn-blue radius-6 mr5 pl1 pr15" onclick="toExport('<%=controller.getURI(request, ExportUserDataReport.class)%>');" href="javascript:void(0);"><i class="icon-i w30 h30 va-middle export-icon "></i>导出</a>
                                            <%
                                        }else{
                                        %>
											<span class="btn btn-gray radius-6 mr5 pl1 pr15"><i
                                                    class="icon-i w30 h30 va-middle export-icon "></i>导出</span> <%
                                            }
                                        %>
                                        </li>
                                    </ul>
                                </div>
                                <div class="table-container">
                                    <table class="table table-style gray6 tl">
                                        <thead>
                                        <tr class="title tc">
                                            <th>序号</th>
                                            <th>日期</th>
                                            <th>注册用户数</th>
                                            <th>PC端注册用户数</th>
                                            <th>APP端注册用户数</th>
                                            <th>微信端注册用户数</th>
                                            <th>后台注册用户数</th>
                                            <th>登录用户数</th>
                                            <th>充值用户数</th>
                                            <th>提现用户数</th>
                                            <th>投资用户数</th>
                                            <th>借款用户数</th>
                                        </tr>
                                        </thead>
                                        <tbody class="f12">
                                        <%
                                        T7052[] t7052s = pagingResult.getItems();
                                            int i = 0;
                                            if(t7052s != null && t7052s.length > 0){
                                                for(T7052 t7052 : t7052s){
                                                i++;
                                        %>
                                            <tr class="title tc">
                                                <td><%=i%></td>
                                                <td><%StringHelper.filterHTML(out,t7052.F01);%></td>
                                                <td><%=t7052.F02%></td>
                                                <td><%=t7052.F03%></td>
                                                <td><%=t7052.F04%></td>
                                                <td><%=t7052.F05%></td>
                                                <td><%=t7052.F06%></td>
                                                <td><%=t7052.F07%></td>
                                                <td><%=t7052.F08%></td>
                                                <td><%=t7052.F09%></td>
                                                <td><%=t7052.F10%></td>
                                                <td><%=t7052.F11%></td>
                                            </tr>
                                        <%}}else{%>
                                            <tr class="tc">
                                                <td colspan="13">暂无数据</td>
                                            </tr>
                                        <%}%>
                                        </tbody>
                                    </table>
                                </div>
                                <div class="clear"></div>
		                        <div class="mb10 table-yhs">
		                        	<ul>
		                            <li class="mr30">注册总人数：<em
		                                    class="red"><%=totalUserData.F02 %>
		                            </em> </li>
		                            <li class="mr30">PC端注册总用户数：<em
		                                    class="red"><%=totalUserData.F03 %>
		                            </em> </li>
		                            <li class="mr30">APP端注册总用户数：<em
		                                    class="red"><%=totalUserData.F04 %>
		                            </em> </li>
		                            <li class="mr30">微信端注册总用户数：<em
		                                    class="red"><%=totalUserData.F05 %>
		                            </em> </li>
		                            <li class="mr30">后台注册总用户数：<em
		                                    class="red"><%=totalUserData.F06 %>
		                            </em> </li>
		                            </ul>
		                        </div>
                                <%AbstractConsoleServlet.rendPagingResult(out,pagingResult);%>
                            </div>
                            <!--box2 end-->
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
        <%if(!StringHelper.isEmpty(createTimeStart)){%>
        $("#datepicker1").val("<%StringHelper.filterHTML(out, createTimeStart);%>");
        <%}%>
        <%if(!StringHelper.isEmpty(createTimeEnd)){%>
        $("#datepicker2").val("<%StringHelper.filterHTML(out, createTimeEnd);%>");
        <%}%>
        $("#datepicker2").datepicker('option', 'minDate',$("#datepicker1").datepicker().val());
    });
    function toExport(url){
        $("#form1").attr("action",url);
        $("#form1").submit();
        $("#form1").attr("action",'<%=controller.getURI(request, UserDataReport.class)%>');
    }
</script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/highcharts.js"></script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/exporting.js"></script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/userData.js"></script>
</html>
