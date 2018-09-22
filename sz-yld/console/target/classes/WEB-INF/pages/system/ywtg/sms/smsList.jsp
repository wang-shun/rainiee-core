<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.console.servlets.system.ywtg.sms.ViewSms" %>
<%@page import="com.dimeng.p2p.console.servlets.system.ywtg.sms.AddSms" %>
<%@page import="com.dimeng.p2p.console.servlets.system.ywtg.sms.SmsList" %>
<%@page import="com.dimeng.p2p.modules.systematic.console.service.entity.Sms" %>
<%@page import="com.dimeng.p2p.console.servlets.AbstractConsoleServlet" %>
<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <%@include file="/WEB-INF/include/script.jsp"%>
</head>
<body>
<%
    CURRENT_CATEGORY = "XTGL";
    CURRENT_SUB_CATEGORY = "DXTG";
    String createTimeStart = request.getParameter("createTimeStart");
    String createTimeEnd = request.getParameter("createTimeEnd");
    PagingResult<Sms> result = (PagingResult<Sms>) request.getAttribute("result");
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <!--加载内容-->
                <form action="<%=controller.getURI(request, SmsList.class)%>" method="post" id="form1">
                    <div class="p20">
                        <div class="border">
                            <div class="title-container"><i class="icon-i w30 h30 va-middle title-left-icon"></i>短信推广
                            </div>
                            <div class="content-container pl40 pt30 pr40">
                                <ul class="gray6 input-list-container clearfix">
                                    <li><span class="display-ib mr5">管理员：</span><input type="text" name="name"
                                                                                       value="<%StringHelper.filterQuoter(out, request.getParameter("name"));%>"
                                                                                       id="textfield7"
                                                                                       class="text border pl5 mr20"/>
                                    </li>
                                    <li><span class="display-ib mr5">时间：</span><input type="text" name="createTimeStart"
                                                                                      id="datepicker1"
                                                                                      readonly="readonly"
                                                                                      class="text border pl5 w120 date"
                                                                                      value="<%StringHelper.filterQuoter(out,
						request.getParameter("createTimeStart"));%>"/>
                                        <span class="pl5 pr5">至</span>
                                        <input type="text" name="createTimeEnd" id="datepicker2" readonly="readonly"
                                               class="text border pl5 w120 mr20 date" value="<%StringHelper.filterQuoter(out,
						request.getParameter("createTimeEnd"));%>"/>
                                    </li>
                                    <li>
                                        <a href="javascript:void(0)" onclick="onSubmit()"
                                           class="btn btn-blue radius-6 mr5 pl1 pr15"><i
                                                class="icon-i w30 h30 va-middle search-icon "></i>搜索</a>
                                    </li>
                                    <li>
                                        <%
                                            if (dimengSession.isAccessableResource(AddSms.class)) {
                                        %>
                                        <a href="<%=controller.getURI(request, AddSms.class)%>"
                                           class="btn btn-blue radius-6 mr5 pl1 pr15"><i
                                                class="icon-i w30 h30 va-middle add-icon "></i>新增</a>
                                        <%} else { %>
                                        <span class="btn btn-gray radius-6 mr5 pl1 pr15"><i
                                                class="icon-i w30 h30 va-middle add-icon "></i>新增</span>
                                        <%} %>
                                    </li>
                                </ul>
                            </div>
                        </div>

                        <div class="border mt20 table-container">
                            <table class="table table-style gray6 tl">
                                <thead>
                                <tr class="title tc">
                                    <th class="tc">序号</th>
                                    <th>内容</th>
                                    <th>管理员</th>
                                    <th>发送对象</th>
                                    <th>发送数量</th>
                                    <th>创建时间</th>
                                    <th>操作</th>
                                </tr>
                                </thead>
                                <tbody class="f12">
                                <%
                                    Sms[] smses = result.getItems();
                                    if (smses != null && smses.length != 0) {
                                        int i = 1;
                                        for (Sms sms : smses) {
                                            if (sms == null) {
                                                continue;
                                            }
                                %>
                                <tr class="tc">
                                    <td class="tc"><%=i++%>
                                    </td>
                                    <td title="<%StringHelper.filterHTML(out, sms.content);%>">
                                        <%StringHelper.filterHTML(out, StringHelper.truncation(sms.content, 15));%>
                                    </td>
                                    <td><%
                                        StringHelper.filterHTML(out, sms.name);
                                    %></td>
                                    <td>
                                        <%=sms.sendType.getName()%>
                                    </td>
                                    <td><%=sms.count%>
                                    </td>
                                    <td><%=DateTimeParser.format(sms.createTime)%>
                                    </td>
                                    <td><span>
										<%
                                            if (dimengSession.isAccessableResource(ViewSms.class)) {
                                        %>
										<a href="<%=controller.getURI(request, ViewSms.class)%>?id=<%=sms.id %>"
                                           class="link-blue  click-link">查看</a>
										<%} else { %>
										<span class="diabled">查看</span>
										<%} %>
										</span></td>
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

    function onSubmit() {
        $("#form1").submit();
    }
</script>
</body>
</html>