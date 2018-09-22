<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.console.servlets.AbstractConsoleServlet" %>
<%@page import="com.dimeng.p2p.console.servlets.base.optsettings.task.UpdateTask" %>
<%@page import="com.dimeng.p2p.console.servlets.base.optsettings.task.ExecuteTask" %>
<%@page import="com.dimeng.p2p.console.servlets.base.optsettings.task.UpdateStatus" %>
<%@page import="com.dimeng.p2p.S66.enums.T6601_F06" %>
<%@page import="com.dimeng.p2p.console.servlets.base.optsettings.task.TaskList" %>
<%@page import="com.dimeng.p2p.S66.entities.T6601" %>
<%@page import="com.dimeng.p2p.console.servlets.base.optsettings.task.AddTask" %>
<%@page import="com.dimeng.util.ObjectHelper" %>
<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <%@include file="/WEB-INF/include/script.jsp" %>
    <style type="text/css">
        .btn5 {
            text-align: left;
        }
    </style>
</head>
<body>
<%
    CURRENT_CATEGORY = "JCXXGL";
    CURRENT_SUB_CATEGORY = "DSRWGL";
    PagingResult<T6601> result = ObjectHelper.convert(request.getAttribute("result"), PagingResult.class);
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <form id="form1"
                      action="<%=controller.getURI(request, TaskList.class)%>"
                      method="post">
                    <input type="hidden" name="id"/>
                    <input type="hidden" name="status"/>

                    <div class="p20">
                        <div class="border">
                            <div class="title-container">
                                <i class="icon-i w30 h30 va-middle title-left-icon"></i>定时任务列表
                            </div>
                            <div class="content-container pl40 pt30 pr40">
                                <ul class="gray6 input-list-container clearfix">
                                    <li><span class="display-ib mr5">任务名称</span> <input
                                            type="text" name="name" id="textfield"
                                            class="text border pl5 mr20"
                                            value="<%StringHelper.filterHTML(out, request.getParameter("name"));%>"/>
                                    </li>

                                    <li><a href="javascript:$('#form1').submit()"
                                           class="btn btn-blue radius-6 mr5 pl1 pr15"><i
                                            class="icon-i w30 h30 va-middle search-icon "></i>搜索</a></li>
                                    <li>
                                        <%if (dimengSession.isAccessableResource(AddTask.class)) {%> <a
                                            href="<%=controller.getURI(request, AddTask.class)%>"
                                            class="btn btn-blue radius-6 mr5 pl1 pr15"><i
                                            class="icon-i w30 h30 va-middle add-icon "></i>新增</a> <%} else { %>
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
                                    <th>序号</th>
                                    <th>任务名</th>
                                    <th>执行类</th>
                                    <th>执行方法</th>
                                    <th>执行时间</th>
                                    <th>创建时间</th>
                                    <th>最后开始运行时间</th>
                                    <th>最后结束运行时间</th>
                                    <th>任务状态</th>
                                    <th>备注</th>
                                    <th class="w200" style="text-align: center;">操作</th>
                                </tr>
                                </thead>
                                <tbody class="f12">
                                <%
                                    T6601[] lists = result.getItems();
                                    if (lists != null && lists.length != 0) {
                                        int index = 1;
                                        for (T6601 list : lists) {

                                %>
                                <tr class="title tc">
                                    <td><%=index++ %>
                                    </td>
                                    <td><%=list.F02 %>
                                    </td>
                                    <td>
                                        <%StringHelper.filterHTML(out, list.F03); %>
                                    </td>
                                    <td><%=list.F04 %>
                                    </td>
                                    <td><%=list.F05 %>
                                    </td>
                                    <td><%=DateTimeParser.format(list.F09) %>
                                    </td>
                                    <td><%=DateTimeParser.format(list.F07) %>
                                    </td>
                                    <td><%=DateTimeParser.format(list.F08) %>
                                    </td>
                                    <td><%=list.F06.getChineseName() %>
                                    </td>
                                    <td <%if (!StringHelper.isEmpty(list.F11)) {%>title="<%=list.F11%>"<%}%>>
                                        <%
                                            if (!StringHelper.isEmpty(list.F11)) {
                                                StringHelper.truncation(out, list.F11, 10);
                                            }
                                        %>
                                    </td>
                                    <td>
                                        <%if (dimengSession.isAccessableResource(UpdateTask.class)) {%>
												<span><a
                                                        href="<%=controller.getURI(request, UpdateTask.class)%>?id=<%=list.F01%>"
                                                        class="link-blue mr20">修改</a></span> <%
                                        if (list.F06 == T6601_F06.ENABLE) {
                                    %> <span><a href="javascript:void(0)"
                                                onclick="updateStatus(<%=list.F01%>,'DISABLE')"
                                                class="libk-deepblue mr20">禁用</a> </span><%} else { %> <span><a
                                            href="javascript:void(0)"
                                            onclick="updateStatus(<%=list.F01%>,'ENABLE')"
                                            class="link-blue mr20 click-link">启用</a></span> <%} %> <%} else { %> <span
                                            class="disabled">修改</span> <%
                                        if (list.F06 == T6601_F06.ENABLE) {
                                    %> <span class="disabled">禁用</span> <%} else { %> <span
                                            class="disabled">启用</span> <%} %> <%} %>
                                      <%
									if(list.F06 == T6601_F06.ENABLE){
									if (dimengSession.isAccessableResource(ExecuteTask.class)) {%>
									<a href="javascript:void(0);" onclick="executeTask(<%=list.F01%>)" class="link-blue mr20 click-link">执行</a>
									<%}else{ %>
									<span class="disabled">执行</span>
									<%} }%>
                                    </td>
                                </tr>
                                <%
                                    }
                                } else {
                                %>
                                <tr class="title tc">
                                    <td colspan="11">暂无数据</td>
                                </tr>
                                <%} %>
                                </tbody>
                            </table>
                        </div>

                        <!--分页-->
                        <%
                            AbstractConsoleServlet.rendPagingResult(out, result);
                        %>
                        <!--分页 end-->
                    </div>
                </form>

            </div>
        </div>
    </div>
    <!--右边内容 结束-->

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
<script type="text/javascript">
    var update_url = '<%=controller.getURI(request, UpdateStatus.class) %>';

    var execute_url = '<%=controller.getURI(request, ExecuteTask.class) %>';
    var list_url = '<%=controller.getURI(request, TaskList.class) %>';

    function updateStatus(id, status) {
        var msg = "";
        if (status == "ENABLE") {
            msg = "确定启用此任务？";
        } else {
            msg = "确定禁用此任务？";
        }
        $("#info").html(showConfirmDiv(msg, id, status));
        $("div.popup_bg").show();

    }

    function toConfirm(id, status) {
        $("input[name='id']").val(id);
        $("input[name='status']").val(status);
        var form = document.forms[0];
        form.action = update_url;
        form.submit();
    }

    function executeTask(id) {
        $.ajax({
            type: "post",
            url: execute_url,
            data: {id: id},
            async: false,
            dataType: "html",
            success: function (data) {
                data = eval("(" + data + ")");
                if (data.code == 1) {
                    $("div.popup_bg").show();
                    $("#info").html(showDialogInfo(data.msg, "wrong"));
                } else {
                    $("div.popup_bg").show();
                    $("#info").html(showDialogInfo(data.msg, "yes"));
                }
            }
        });
    }
</script>
</body>
</html>