<%@page import="com.dimeng.p2p.console.servlets.info.UpdateOrder"%>
<%@page import="com.dimeng.p2p.console.servlets.info.yygl.yqlj.*" %>
<%@page import="com.dimeng.p2p.modules.base.console.service.entity.FriendlyLinkRecord" %>
<%@page import="com.dimeng.util.ObjectHelper" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <%@include file="/WEB-INF/include/script.jsp"%>
</head>

<body>
<%
    CURRENT_CATEGORY = "XCGL";
    CURRENT_SUB_CATEGORY = "YQLJ";
    String createTimeStart = request.getParameter("updateTimeStart");
    String createTimeEnd = request.getParameter("updateTimeEnd");
    PagingResult<FriendlyLinkRecord> result = ObjectHelper.convert(request.getAttribute("result"), PagingResult.class);
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <form action="<%=controller.getURI(request, SearchYqlj.class)%>" method="post" id="form1">
                    <div class="p20">
                        <div class="border">
                            <div class="title-container">
                                <i class="icon-i w30 h30 va-middle title-left-icon"></i>友情链接
                            </div>
                            <div class="content-container pl40 pt30 pr40">
                                <ul class="gray6 input-list-container clearfix">
                                    <li><span class="display-ib mr5">名称：</span>
                                        <input type="text" name="name" id="textfield" class="text border pl5 mr20"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("name"));%>"/>
                                    </li>
                                    <li><span class="display-ib mr5">发布者：</span>
                                        <input type="text" name="publisherName" id="textfield4"
                                               class="text border pl5 mr20"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("publisherName"));%>"/>
                                    </li>
                                    <li><span class="display-ib mr5">修改时间：</span>
                                        <input type="text" name="updateTimeStart" readonly="readonly" id="datepicker1"
                                               class="text border pl5 w120 date"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("updateTimeStart"));%>"/>
                                        <span class="pl5 pr5">至</span>
                                        <input type="text" name="updateTimeEnd" readonly="readonly" id="datepicker2"
                                               class="text border pl5 w120 mr20 date"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("updateTimeEnd"));%>"/>
                                    </li>
                                    <li>
                                        <a href="javascript:void(0)" onclick="onSubmit()"
                                           class="btn btn-blue radius-6 mr5 pl1 pr15"><i
                                                class="icon-i w30 h30 va-middle search-icon "></i>搜索</a>
                                    </li>
                                    <li>
                                        <%if (dimengSession.isAccessableResource(AddYqlj.class)) {%>
                                        <a href="<%=controller.getURI(request, AddYqlj.class)%>"
                                           class="btn btn-blue radius-6 mr5 pl1 pr15"><i
                                                class="icon-i w30 h30 va-middle add-icon "></i>新增</a>
                                        <%} else { %>
                                        <a href="javascript:void(0)" class="btn btn-gray radius-6 mr5 pl10 pr10"><i
                                                class="icon-i w30 h30 va-middle add-icon "></i>新增</a>
                                        <%} %>
                                    </li>
                                    <li>
                                        <%if (dimengSession.isAccessableResource(DeleteYqlj.class)) {%>
                                        <a href="javascript:void(0)" id="delAll"
                                           class="btn btn-blue2 radius-6 mr5 pl10 pr10">批量删除</a>
                                        <%} else { %>
                                        <a href="javascript:void(0)"
                                           class="btn btn-gray radius-6 mr5 pl10 pr10">批量删除</a>
                                        <%} %>
                                    </li>
                                    <li>
                                        <%if (dimengSession.isAccessableResource(UpdateOrder.class)) {%>
                                       	<a href="javascript:void(0)" id="updateOrder" class="btn btn-blue2 radius-6 mr5 pl10 pr10">修改排序值</a>
                                        <%} else { %>
                                      		<span class="btn btn-gray radius-6 mr5 pl10 pr10">修改排序值</span>
                                      <%} %>
                                    </li>
                                </ul>
                            </div>
                        </div>
                        <div class="border mt20 table-container">
                            <table class="table table-style gray6 tl">
                                <thead>
                                <tr class="title tc">
                                    <th class="tc"><input id="checkAll" type="checkbox"/></th>
                                    <th class="tc">序号</th>
                                    <th>名称</th>
                                    <th>浏览次数</th>
                                    <th>发布者</th>
                                    <th>链接地址</th>
                                    <th>最后修改时间</th>
                                    <th>排序值</th>
                                    <th>操作</th>
                                </tr>
                                </thead>
                                <tbody class="f12">
                                <% FriendlyLinkRecord[] records = result.getItems();
                                    if (records != null) {
                                        int index = 1;
                                        for (FriendlyLinkRecord record : records) {
                                %>
                                <tr class="title tc">
                                    <td class="tc"><input name="id" type="checkbox" value="<%=record.id %>"/></td>
                                    <td class="tc"><%=index++ %>
                                    </td>
                                    <td title="<%StringHelper.filterHTML(out, record.name); %>"><%
                                        StringHelper.filterHTML(out, StringHelper.truncation(record.name, 15)); %></td>
                                    <td><%=record.viewTimes %>
                                    </td>
                                    <td><%StringHelper.filterHTML(out, record.publisherName); %></td>
                                    <td><a href="<%=record.url%>" target="_blank"><%
                                        StringHelper.filterHTML(out, record.url);%></a></td>
                                    <td><%=DateTimeParser.format(record.updateTime) %>
                                    </td>
                                    <td><%=record.sortIndex %>
                                    </td>
                                    <td class="blue">
                                        <%if (dimengSession.isAccessableResource(UpdateYqlj.class)) {%>
                                        <a href="<%=controller.getURI(request, UpdateYqlj.class)%>?id=<%=record.id %>"
                                           class="link-blue mr20">修改</a>
                                        <%} else { %>
                                        <a class="disabled">修改</a>
                                        <%} %>
                                        <%if (dimengSession.isAccessableResource(DeleteYqlj.class)) {%>
                                        <a href="javascript:void(0)" onclick="delThis('<%=record.id %>')"
                                           class="link-orangered ">删除</a>
                                        <%} else { %>
                                        <a class="disabled">删除</a>
                                        <%} %>
                                    </td>
                                </tr>
                                <%
                                    }
                                } else {
                                %>
                                <tr class="title">
                                    <td colspan="11" class="tc">暂无数据</td>
                                </tr>
                                <%} %>
                                </tbody>
                            </table>
                        </div>
                        <% SearchYqlj.rendPagingResult(out, result);%>
                    </div>
                </form>
            </div>
        </div>
        <div class="popup_bg hide"></div>
    </div>
<div id="info"></div>
<input id="orderTable" type="hidden" name="orderTable" value="T5014"/>
<script type="text/javascript">
    var del_url = '<%=controller.getURI(request, DeleteYqlj.class) %>';
    var reloadUrl = '<%=controller.getURI(request, SearchYqlj.class)%>';
    var upateOrderUrl = '<%=controller.getURI(request, UpdateOrder.class)%>';
</script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/checkAll.js"></script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/dialog.js"></script>
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
        $("#datepicker1").val("<%StringHelper.filterHTML(out, request.getParameter("updateTimeStart"));%>");
        <%}%>
        <%if(!StringHelper.isEmpty(createTimeEnd)){%>
        $("#datepicker2").val("<%StringHelper.filterHTML(out, request.getParameter("updateTimeEnd"));%>");
        <%}%>
        $("#datepicker2").datepicker('option', 'minDate', $("#datepicker1").datepicker().val());
    });
    function onSubmit() {
        $("#form1").submit();
    }
</script>
</body>
</html>