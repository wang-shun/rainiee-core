<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.console.servlets.spread.hdgl.hdlist.SearchHdgl"%>
<%@page import="java.util.Date"%>
<%@page import="com.dimeng.p2p.console.servlets.AbstractConsoleServlet" %>
<%@page import="com.dimeng.p2p.console.servlets.spread.hdgl.hdlist.QueryActNo" %>
<%@page import="com.dimeng.p2p.console.servlets.spread.hdgl.hdlist.UpdateStatus" %>
<%@page import="com.dimeng.p2p.console.servlets.spread.hdgl.hdlist.UpdateHdgl" %>
<%@page import="com.dimeng.p2p.console.servlets.spread.hdgl.hdlist.SearchHdxq" %>
<%@page import="com.dimeng.p2p.console.servlets.spread.hdgl.hdlist.ChooseType" %>
<%@page import="com.dimeng.p2p.console.servlets.spread.hdgl.hdlist.AddHdgl" %>
<%@page import="com.dimeng.p2p.console.servlets.spread.hdgl.hdlist.ExportHdgl" %>
<%@page import="com.dimeng.p2p.modules.activity.console.service.entity.ActivityList" %>
<%@page import="com.dimeng.p2p.S63.enums.T6340_F08" %>
<%@page import="com.dimeng.p2p.S63.enums.T6340_F04" %>
<%@page import="com.dimeng.p2p.S63.enums.T6340_F03" %>
<%@page import="com.dimeng.util.ObjectHelper" %>
<%@ page import="java.math.BigDecimal" %>
<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <%@include file="/WEB-INF/include/script.jsp"%>
</head>
<body>
<%
    CURRENT_CATEGORY = "TGGL";
    CURRENT_SUB_CATEGORY = "HDGL";
    PagingResult<ActivityList> result = ObjectHelper.convert(request.getAttribute("result"), PagingResult.class);
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <form id="form1" action="<%=controller.getURI(request, SearchHdgl.class)%>" method="post">
                    <input type="hidden" name="id"/>
                    <input type="hidden" name="status"/>

                    <div class="p20">
                        <div class="border">
                            <div class="title-container"><i class="icon-i w30 h30 va-middle title-left-icon"></i>活动列表
                            </div>
                            <div class="content-container pl40 pt30 pr40">
                                <ul class="gray6 input-list-container clearfix">
                                    <li><span class="display-ib mr5">活动ID</span>
                                        <input type="text" name="code" id="textfield" class="text border pl5 mr20"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("code"));%>"/>
                                    </li>
                                    <li><span class="display-ib mr5">活动名称</span>
                                        <input type="text" name="name"
                                               id="textfield" class="text border pl5 mr20"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("name"));%>"/>
                                    </li>
                                    <li><span class="display-ib mr5">奖励类型</span>

                                        <select name="jlType" class="border mr20 h32 mw100">
                                            <option>全部</option>
                                            <%
                                                for (T6340_F03 status : T6340_F03.values()) {
                                            %>
                                            <option value="<%=status.name()%>"
                                                    <%if (status.name().equals(request.getParameter("jlType"))) {%>
                                                    selected="selected" <%}%>><%=status.getChineseName()%>
                                            </option>
                                            <%
                                                }
                                            %>
                                        </select>
                                    </li>
                                    <li><span class="display-ib mr5">活动类型</span>

                                        <select name="hdType" class="border mr20 h32 mw100">
                                            <option>全部</option>
                                            <%
                                                for (T6340_F04 status : T6340_F04.values()) {
                                            %>
                                            <option value="<%=status.name()%>"
                                                    <%if (status.name().equals(request.getParameter("hdType"))) {%>
                                                    selected="selected" <%}%>><%=status.getChineseName()%>
                                            </option>
                                            <%
                                                }
                                            %>
                                        </select>
                                    </li>
                                    <li><span class="display-ib mr5">活动状态</span>
                                        <select name="zt" class="border mr20 h32 mw100">
                                            <option>全部</option>
                                            <%
                                                for (T6340_F08 status : T6340_F08.values()) {
                                            %>
                                            <option value="<%=status.name()%>"
                                                    <%if (status.name().equals(request.getParameter("zt"))) {%>
                                                    selected="selected" <%}%>><%=status.getChineseName()%>
                                            </option>
                                            <%
                                                }
                                            %>
                                        </select>
                                    </li>

                                    <li><span class="display-ib mr5">开始时间</span>
                                        <input type="text" name="startsTime"
                                               readonly="readonly" id="datepicker1"
                                               class="text border pl5 w120 date"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("startsTime"));%>"/>
                                        <span class="pl5 pr5">至</span>
                                        <input type="text" name="starteTime" readonly="readonly"
                                               id="datepicker2" class="text border pl5 w120 date mr20"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("starteTime"));%>"/>
                                    </li>
                                    <li><span class="display-ib">结束时间</span>
                                        <input type="text" name="endsTime"
                                               readonly="readonly" id="datepicker3"
                                               class="text border pl5 w120 date"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("endsTime"));%>"/>
                                        <span class="pl5 pr5">至</span>
                                        <input type="text" name="endeTime" readonly="readonly"
                                               id="datepicker4" class="text border pl5 w120 date mr20"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("endeTime"));%>"/>
                                    </li>
                                    <li><a href="javascript:$('#form1').submit();"
                                           class="btn btn-blue radius-6 mr5 pl1 pr15"><i
                                            class="icon-i w30 h30 va-middle search-icon "></i>搜索</a></li>

                                    <li>
                                        <%
                                            if (dimengSession.isAccessableResource(ExportHdgl.class)) {
                                        %>
                                        <a href="javascript:void(0)" onclick="exportHdgl()"
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
                                    <li>

                                        <%if (dimengSession.isAccessableResource(AddHdgl.class)) {%>
                                        <a href="<%=controller.getURI(request, ChooseType.class)%>"
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
                                    <th>序号</th>
                                    <th>活动ID</th>
                                    <th>活动名称</th>
                                    <th>奖励类型</th>
                                    <th>活动类型</th>
                                    <th>活动开始时间</th>
                                    <th>活动结束时间</th>
                                    <th>总数量（个）</th>
                                    <th>总金额（元）</th>
                                    <th>已领取数量（个）</th>
                                    <th>已领取金额（元）</th>
                                    <th>活动状态</th>
                                    <th>操作时间</th>
                                    <th>操作</th>
                                </tr>
                                </thead>
                                <tbody class="f12">
                                <%
                                    ActivityList[] lists = result.getItems();
                                    if (lists != null) {
                                        int index = 1;
                                        for (ActivityList list : lists) {

                                %>
                                <tr class="tc">
                                    <td><%=index++ %>
                                    </td>
                                    <td><%=list.F02 %>
                                    </td>
                                    <td>
                                        <%StringHelper.filterHTML(out, list.F05); %>
                                    </td>
                                    <td><%=list.F03.getChineseName() %>
                                    </td>
                                    <td><%=list.F04.getChineseName() %>
                                    </td>
                                    <td><%=list.F06 == null ? "--" : DateParser.format(list.F06) %>
                                    </td>
                                    <td><%=list.F07 == null ? "--" : DateParser.format(list.F07) %>
                                    </td>
                                    <td><%=list.F08 %>
                                    </td>
                                    <%if(T6340_F03.interest.name().equals(list.F03.name()))
                                    {%>
                                    <td>--</td>
                                    <%}
                                    else
                                    {%>
                                    <td><%=list.F10 %></td>
                                    <%}%>
                                    </td>
                                    <td><%=list.F11 %>
                                    </td>
                                    <%if(T6340_F03.interest.name().equals(list.F03.name()))
                                    {%>
                                        <td>--</td>
                                    <%}
                                    else
                                    {%>
                                        <td><%=list.F14 %></td>
                                    <%}%>
                                    <td><%=list.F12.getChineseName() %>
                                    </td>
                                    <td><%=DateTimeParser.format(list.F13) %>
                                    </td>
                                    <td>
                                        <%if (dimengSession.isAccessableResource(SearchHdxq.class)) {%>
                                        <a
                                                href="<%=controller.getURI(request, SearchHdxq.class) %>?id=<%=list.F01 %>"
                                                class="link-blue">查看</a>
                                        <%} else { %> <a href="javascript:void(0)" class="disabled mr10">查看</a> <%} %>
                                        <%if (list.F12.name().equals(T6340_F08.DSJ.name())) { %>

                                        <%if (dimengSession.isAccessableResource(UpdateHdgl.class)) {%>
                                        <a
                                                href="<%=controller.getURI(request, UpdateHdgl.class) %>?id=<%=list.F01 %>"
                                                class="link-blue">修改</a>
                                        <%} else { %> <a href="javascript:void(0)" class="disabled mr10">修改</a> <%
                                            }
                                        }
                                    %>
                                        <%
                                            if (T6340_F08.DSJ.name().equals(list.F12.name())) {
                                                String status = T6340_F08.JXZ.name();
                                                if (T6340_F04.exchange != list.F04 && T6340_F04.integraldraw != list.F04 && new Date().before(list.F06)) {
                                                    status = T6340_F08.YSJ.name();
                                                }
                                        %>
										<%if (dimengSession.isAccessableResource(UpdateStatus.class)) {%>
                                        <a href="javascript:void(0)"
                                           onclick="updateStatus('<%=list.F01%>','<%=status%>','<%=list.F03.name()%>','<%=list.F04.name()%>','<%=list.F02%>')"
                                           class="link-blue">上架</a>
                                        <a href="javascript:void(0)"
                                           onclick="updateStatus('<%=list.F01%>','<%=T6340_F08.YZF.name()%>','<%=list.F03.name()%>','<%=list.F04.name()%>','<%=list.F02%>')"
                                           class="link-blue">作废</a>
                                        <%}else{ %>
                                        <a class="disabled mr10">上架</a>	
                                        <a class="disabled mr10">作废</a>	
                                        <%} %>
                                        <%} else if (T6340_F08.YSJ.name().equals(list.F12.name()) || T6340_F08.JXZ.name().equals(list.F12.name())) { %>
                                        <%if (dimengSession.isAccessableResource(UpdateStatus.class)) {%>
                                        <a href="javascript:void(0)"
                                           onclick="updateStatus('<%=list.F01%>','<%=T6340_F08.YXJ.name()%>','<%=list.F03.name()%>','<%=list.F04.name()%>','<%=list.F02%>')"
                                           class="link-blue">下架</a>
                                        <%}else{ %>
                                        <a class="disabled mr10">下架</a>
                                        <%} %>
                                        <%} %>

                                    </td>
                                </tr>
                                <%
                                    }
                                } else {
                                %>
                                <tr class="tc">
                                    <td colspan="14">暂无数据</td>
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
<div id="info"></div>
<div class="popup_bg" style="display: none;"></div>
<%@include file="/WEB-INF/include/datepicker.jsp" %>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/dialog.js"></script>
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
        $("#datepicker1").datepicker("setDate", "<%StringHelper.filterHTML(out, request.getParameter("startsTime"));%>");
        $("#datepicker2").datepicker("setDate", "<%StringHelper.filterHTML(out, request.getParameter("starteTime"));%>");
        $("#datepicker2").datepicker('option', 'minDate', $("#datepicker1").datepicker().val());

        $("#datepicker3").datepicker({
            inline: true,
            onSelect: function (selectedDate) {
                $("#datepicker4").datepicker("option", "minDate", selectedDate);
            }
        });
        $('#datepicker3').datepicker('option', {dateFormat: 'yy-mm-dd'});
        $("#datepicker4").datepicker({inline: true});
        $('#datepicker4').datepicker('option', {dateFormat: 'yy-mm-dd'});
        $("#datepicker3").datepicker("setDate", "<%StringHelper.filterHTML(out, request.getParameter("endsTime"));%>");
        $("#datepicker4").datepicker("setDate", "<%StringHelper.filterHTML(out, request.getParameter("endeTime"));%>");
        $("#datepicker4").datepicker('option', 'minDate', $("#datepicker3").datepicker().val());
    });
</script>
<script type="text/javascript">
    function exportHdgl() {
        var del_url = '<%=controller.getURI(request, ExportHdgl.class) %>';
        var form = document.forms[0];
        form.action = del_url;
        form.submit();
        form.action = '<%=controller.getURI(request, SearchHdgl.class) %>';
    }
    var update_url = '<%=controller.getURI(request, UpdateStatus.class) %>';
    function updateStatus(id, status, jlType, hdType, no) {
        var msg = "";
        if (status == "JXZ") {
            var postUrl = '<%=controller.getURI(request, QueryActNo.class)%>';
            $.ajax({
                type: "post",
                url: postUrl,
                data: {jlType: jlType, hdType: hdType},
                async: false,
                dataType: "html",
                success: function (data) {
                    data = eval("(" + data + ")");
                    if (data.result == "" || data.result == null) {
                        msg = "确定上架本次活动？";
                    } else {
                        msg = "确定要上架" + no + "活动，原" + data.result + "将下架处理？";
                    }
                }
            });
        } else if (status == "YXJ") {
            msg = "确定下架本次活动？";
        } else if (status == "YZF") {
            msg = "确定作废本次活动？";
        } else if (status == "YSJ") {
            msg = "确定预上架本次活动？";
        }
        $("#info").html(showForwardInfo(msg, "question", "javascript:update('" + id + "','" + status + "')"));
        $("div.popup_bg").show();

    }

    function update(id, status) {
        $("input[name='id']").val(id);
        $("input[name='status']").val(status);
        var form = document.forms[0];
        form.action = update_url;
        form.submit();
    }

</script>
</body>
</html>