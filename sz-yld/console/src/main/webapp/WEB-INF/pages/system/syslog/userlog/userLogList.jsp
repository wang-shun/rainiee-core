<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.console.servlets.system.syslog.userlog.UserLogList"%>
<%@page import="com.dimeng.p2p.console.servlets.AbstractConsoleServlet" %>
<%@page import="com.dimeng.p2p.common.enums.FrontLogType"%>
<%@page import="com.dimeng.p2p.modules.systematic.console.service.entity.UserLog" %>
<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <%@include file="/WEB-INF/include/script.jsp"%>
</head>
<body>
<%
    PagingResult<UserLog> result = (PagingResult<UserLog>) request
            .getAttribute("result");
%>
<%
    CURRENT_CATEGORY = "XTGL";
    CURRENT_SUB_CATEGORY = "QTDLRZ";
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <!--加载内容-->
                <form id="form1" action="<%=controller.getURI(request, UserLogList.class)%>"
                      method="post">
                    <div class="p20">
                        <div class="border">
                            <div class="title-container"><i class="icon-i w30 h30 va-middle title-left-icon"></i>前台日志
                            </div>
                            <div class="content-container pl40 pt30 pr40">
                                <ul class="gray6 input-list-container clearfix">
                                    <li>
                                        <span class="display-ib mr5">用户名</span>
                                        <input type="text" name="accountName"
                                               value="<%StringHelper.filterQuoter(out, request.getParameter("accountName"));%>"
                                               id="textfield" class="text border pl5 mr20"/>
                                    </li>
                                    <li>
                                        <span class="display-ib mr5">时间</span>
                                        <input type="text" name="createTimeStart" id="datepicker1" readonly="readonly"
                                               class="text border pl5 w120 date"
                                               value="<%StringHelper.filterQuoter(out, request.getParameter("createTimeStart"));%>"/>
                                        <span class="pl5 pr5">至</span>
                                        <input type="text" name="createTimeEnd" id="datepicker2" readonly="readonly"
                                               class="text border pl5 w120 mr20 date"
                                               value="<%StringHelper.filterQuoter(out, request.getParameter("createTimeEnd"));%>"/>
                                    </li>
                                    <li>
                                        <span class="display-ib mr5">日志类型：</span> 
                                        <select name="type" class="border mr20 h32 mw100">
	                                        <option value="">--全部--</option>
	                                        <%
	                                        for(FrontLogType type : FrontLogType.values()){
	                                        %>
	                                        <option value="<%=type.name() %>"
	                                                <%if(type.name().equals(request.getParameter("type"))){ %>
	                                                selected="selected" <%}%>><%=type.getName() %>
	                                        </option>
	                                        <%} %>
	                                    </select>
                                    </li>
                                    <li>
                                        <a class="btn btn-blue radius-6 mr5 pl1 pr15"
                                           href="javascript:$('#form1').submit();"><i
                                                class="icon-i w30 h30 va-middle search-icon"></i>搜索</a>
                                    </li>
                                </ul>
                            </div>
                        </div>

                        <div class="border mt20 table-container">
                            <table class="table table-style gray6 tl">
                                <thead>
                                <tr class="title tc">
                                    <th class="tc">序号</th>
                                    <th>用户名</th>
                                    <th>操作时间</th>
                                    <th>登录IP</th>
                                    <th>日志类型</th>
                                    <th>操作描述</th>
                                </tr>
                                </thead>
                                <tbody class="f12">
                                <%
                                    UserLog[] userlogs = result.getItems();
                                    if (userlogs != null && userlogs.length != 0) {
                                        int i = 1;
                                        for (UserLog userLog : userlogs) {
                                            if (userLog == null) {
                                                continue;
                                            }
                                %>
                                <tr class="tc">
                                    <td class="tc"><%=i++%>
                                    </td>
                                    <td>
                                        <%
                                            StringHelper.filterHTML(out, userLog.accountName);
                                        %>
                                    </td>
                                    <td><%=DateTimeParser.format(userLog.F03)%>
                                    </td>
                                    <td>
                                        <%
                                            StringHelper.filterHTML(out, userLog.F06);
                                        %>
                                    </td>
                                    <td>
                                        <%
                                            StringHelper.filterHTML(out, userLog.F04);
                                        %>
                                    </td>
                                    <td>
                                        <%
                                            StringHelper.filterHTML(out, userLog.F05);
                                        %>
                                    </td>
                                </tr>
                                <%
                                    }
                                } else {
                                %>
                                <tr class="dhsbg">
                                    <td colspan="11" class="tc">暂无数据</td>
                                </tr>
                                <%} %>
                                </tbody>
                            </table>
                        </div>
                        <%
                            AbstractConsoleServlet.rendPagingResult(out, result);
                        %>
                    </div>
                </form>
                <!--加载内容 结束-->
            </div>
        </div>
    </div>
    <!--右边内容 结束-->
<!--内容-->
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
        <% if(!StringHelper.isEmpty(request.getParameter("createTimeStart"))){%>
        $("#datepicker1").datepicker("setDate", "<%StringHelper.filterHTML(out, request.getParameter("createTimeStart"));%>");
        <%}%>
        <% if(!StringHelper.isEmpty(request.getParameter("createTimeEnd"))){%>
        $("#datepicker2").datepicker("setDate", "<%StringHelper.filterHTML(out, request.getParameter("createTimeEnd"));%>");
        <%}%>
        $("#datepicker2").datepicker('option', 'minDate', $("#datepicker1").datepicker().val());
    });
</script>
</body>
</html>