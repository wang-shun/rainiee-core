<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.console.servlets.system.syslog.constant.ConstantLogList"%>
<%@page import="com.dimeng.p2p.console.servlets.system.syslog.constant.ViewConstantLog" %>
<%@page import="com.dimeng.util.filter.HTMLFilter" %>
<%@page import="com.dimeng.p2p.console.servlets.AbstractConsoleServlet" %>
<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <%@include file="/WEB-INF/include/script.jsp"%>
</head>
<body>
<%
    PagingResult<com.dimeng.p2p.modules.systematic.console.service.entity.Constant> result = (PagingResult<com.dimeng.p2p.modules.systematic.console.service.entity.Constant>) request
            .getAttribute("result");
%>
<%
    CURRENT_CATEGORY = "XTGL";
    CURRENT_SUB_CATEGORY = "PTCLRZ";
    String createTimeStart = request.getParameter("createTimeStart");
    String createTimeEnd = request.getParameter("createTimeEnd");
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <!--加载内容-->
                <form id="search_form" action="<%=controller.getURI(request, ConstantLogList.class)%>" method="post">
                    <div class="p20">
                        <div class="border">
                            <div class="title-container"><i class="icon-i w30 h30 va-middle title-left-icon"></i>常量日志
                            </div>
                            <div class="content-container pl40 pt30 pr40">
                                <ul class="gray6 input-list-container clearfix">
                                    <li><span class="display-ib mr5">常量名称：</span><input type="text" name="name"
                                                                                        value="<%StringHelper.filterQuoter(out, request.getParameter("name"));%>"
                                                                                        id="textfield"
                                                                                        class="text border pl5 mr20"/>
                                    </li>
                                    <li><span class="display-ib mr5">KEY值：</span><input type="text" name="key"
                                                                                        value="<%StringHelper.filterQuoter(out, request.getParameter("key"));%>"
                                                                                        id="textfield"
                                                                                        class="text border pl5 mr20"/>
                                    </li>
                                    <li><span class="display-ib mr5">修改时间：</span><input type="text" name="timeStart"
                                                                                        id="datepicker1"
                                                                                        readonly="readonly"
                                                                                        class="text border pl5 w120 date"
                                                                                        value="<%StringHelper.filterQuoter(out,
									request.getParameter("timeStart"));%>"/>
                                        <span class="pl5 pr5">至</span>
                                        <input type="text" name="timeEnd" id="datepicker2" readonly="readonly"
                                               class="text border pl5 w120 mr20 date" value="<%StringHelper.filterQuoter(out,
									request.getParameter("timeEnd"));%>"/>
                                    </li>
                                    <li>
                                        <a class="btn btn-blue radius-6 mr5 pl1 pr15"
                                           href="javascript:$('#search_form').submit();"><i
                                                class="icon-i w30 h30 va-middle search-icon"></i>搜索</a>
                                    </li>
                                </ul>
                            </div>
                        </div>

                        <div class="border mt20 table-container">
                            <table class="table table-style gray6 tl">
                                <thead>
                                <tr class="title tc">
                                    <th class="tc">序号
                                    </td>
                                    <th>常量名称</th>
                                    <th>KEY值</th>
                                    <th>修改前值</th>
                                    <th>修改后值</th>
                                    <th>修改人</th>
                                    <th>修改时间</th>
                                    <th>操作</th>
                                </tr>
                                </thead>
                                <tbody class="f12">
                                <%
                                com.dimeng.p2p.modules.systematic.console.service.entity.Constant[] constants = result.getItems();
                                    if (constants != null && constants.length != 0) {
                                        int i = 1;
                                        HTMLFilter htmlFilter = new HTMLFilter(out);
                                        for (com.dimeng.p2p.modules.systematic.console.service.entity.Constant constant : constants) {
                                            if (constant == null) {
                                                continue;
                                            }
                                %>
                                <tr class="tc">
                                    <td class="tc"><%=i++%>
                                    </td>
                                    <td title="<%StringHelper.filterHTML(out, constant.desc);%>">
                                        <%StringHelper.truncation(htmlFilter, constant.desc, 20);%>
                                    </td>
                                    <td title="<%StringHelper.filterHTML(out, constant.key);%>">
                                        <%StringHelper.truncation(htmlFilter, constant.key, 20);%>
                                    </td>
                                    <td title="<%StringHelper.filterHTML(out, constant.value1);%>">
                                        <%StringHelper.truncation(htmlFilter, constant.value1, 20);%>
                                    </td>
                                    <td title="<%StringHelper.filterHTML(out, constant.value2);%>">
                                        <%StringHelper.truncation(htmlFilter, constant.value2, 20);%>
                                    </td>
                                    <td><%StringHelper.filterHTML(out, constant.name); %></td>
                                    <td><%=DateTimeParser.format(constant.updateTime) %>
                                    </td>
                                    <td><a class="link-blue  click-link"
                                           href="<%=controller.getURI(request, ViewConstantLog.class)%>?id=<%=constant.id%>">查看</a>
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
        <%if(!StringHelper.isEmpty(createTimeStart)){%>
        $("#datepicker1").val("<%StringHelper.filterHTML(out, request.getParameter("createTimeStart"));%>");
        <%}%>
        <%if(!StringHelper.isEmpty(createTimeEnd)){%>
        $("#datepicker2").val("<%StringHelper.filterHTML(out, request.getParameter("createTimeEnd"));%>");
        <%}%>
        $("#datepicker2").datepicker('option', 'minDate', $("#datepicker1").datepicker().val());
    });
</script>
</body>
</html>