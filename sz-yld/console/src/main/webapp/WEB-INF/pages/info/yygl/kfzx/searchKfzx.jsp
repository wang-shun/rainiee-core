<%@page import="com.dimeng.p2p.console.servlets.info.UpdateOrder"%>
<%@page import="com.dimeng.p2p.modules.base.console.service.entity.CustomerServiceRecord" %>
<%@page import="com.dimeng.util.ObjectHelper" %>
<%@ page import="com.dimeng.p2p.S50.enums.T5012_F11" %>
<%@ page import="com.dimeng.p2p.console.servlets.info.yygl.kfzx.*" %>
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
    CURRENT_SUB_CATEGORY = "KFZX";
    PagingResult<CustomerServiceRecord> result = ObjectHelper.convert(request.getAttribute("result"), PagingResult.class);
    Integer allowZxkfCount =IntegerParser.parse(configureProvider.getProperty(SystemVariable.ALLOW_ZXKF_COUNT));
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <form action="<%=controller.getURI(request, SearchKfzx.class)%>" method="post" id="form1">
                    <div class="p20">
                        <div class="border">
                            <div class="title-container">
                                <i class="icon-i w30 h30 va-middle title-left-icon"></i>客服中心
                            </div>
                            <div class="content-container pl40 pt30 pr40">
                                <ul class="gray6 input-list-container clearfix">
                                    <li><span class="display-ib mr5">发布者：</span>
                                        <input type="text" name="publisherName" id="textfield4"
                                               class="text border pl5 mr20"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("publisherName"));%>"/>
                                    </li>
                                    <li>
                                        <a href="javascript:void(0)" onclick="onSubmit()"
                                           class="btn btn-blue radius-6 mr5 pl1 pr15"><i
                                                class="icon-i w30 h30 va-middle search-icon "></i>搜索</a>
                                    </li>
                                    <li>
                                        <%if (dimengSession.isAccessableResource(AddKfzx.class)) {%>
                                        <a href="<%=controller.getURI(request, AddKfzx.class)%>"
                                           class="btn btn-blue radius-6 mr5 pl1 pr15"><i
                                                class="icon-i w30 h30 va-middle add-icon "></i>新增</a>
                                        <%} else { %>
                                        <a href="javascript:void(0)" class="btn btn-gray radius-6 mr5 pl10 pr10"><i
                                                class="icon-i w30 h30 va-middle add-icon "></i>新增</a>
                                        <%} %>
                                    </li>
                                    <li>
                                        <%if (dimengSession.isAccessableResource(DeleteKfzx.class)) {%>
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
                                    <th class="tc"><input type="checkbox" id="checkAll"/></th>
                                    <th class="tc">序号</th>
                                    <th>客服名称</th>
                                    <th>发布者</th>
                                    <th>客服号码/在线代码</th>
                                    <th>客服类型</th>
                                    <th>最后修改时间</th>
                                    <th>状态</th>
                                    <th>排序值</th>
                                    <th>操作</th>
                                </tr>
                                </thead>
                                <tbody class="f12">
                                <% CustomerServiceRecord[] records = result.getItems();
                                    if (records != null) {
                                        int index = 1;
                                        for (CustomerServiceRecord record : records) {
                                %>
                                <tr class="title tc">
                                    <td class="tc"><input name="id" type="checkbox" value="<%=record.id %>"/></td>
                                    <td class="tc"><%=index++ %>
                                    </td>
                                    <td title="<%StringHelper.filterHTML(out, record.name); %>"><%
                                        StringHelper.filterHTML(out, StringHelper.truncation(record.name, 15)); %></td>
                                    <td><%StringHelper.filterHTML(out, record.publisherName); %></td>
                                    <td><%StringHelper.filterHTML(out, record.number); %></td>
                                    <td><%StringHelper.filterHTML(out, record.type.getChineseName()); %></td>
                                    <td><%=DateTimeParser.format(record.updateTime) %>
                                    </td>
                                    <td><%StringHelper.filterHTML(out, record.status.getChineseName());%></td>
                                    <td><%=record.sortIndex %>
                                    </td>
                                    <td class="blue">
                                        <%if (dimengSession.isAccessableResource(UpdateKfzx.class)) {%>
                                            <a href="<%=controller.getURI(request, UpdateKfzx.class)%>?id=<%=record.id %>" class="link-blue mr20">修改</a>
                                            <%if(T5012_F11.TY == record.status){%>
                                                <a href="javascript:void(0)" id="qy_id" onclick="showQy(<%=record.id%>)" class="link-blue mr20">启用</a>
                                            <%}else{%>
                                                <a href="javascript:void(0)" id="ty_id" onclick="showTy(<%=record.id%>)" class="link-blue mr20">停用</a>
                                            <%}%>
                                        <%} else { %>
                                            <a class="disabled">修改</a>
                                            <a class="disabled">删除</a>
                                        <%} %>
                                        <%if (dimengSession.isAccessableResource(DeleteKfzx.class)) {%>
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
                        <%SearchKfzx.rendPagingResult(out, result);%>
                    </div>
                </form>
            </div>
        </div>
        <div class="popup_bg hide"></div>
    </div>
<div id="info"></div>
<input id="orderTable" type="hidden" name="orderTable" value="T5012"/>

<div class="popup-box hide" style="width:400px;min-height:200px;" id="sjDivId">
    <form action="<%=controller.getURI(request, UpdateKfzx.class) %>" id="sjForm" method="post" class="form1">
        <input type="hidden" name="id" id="sj_cid"/>
        <input type="hidden" name="status" value="<%=T5012_F11.QY.name()%>"/>
        <input type="hidden" name="doType" value="updateStatus"/>
        <div class="popup-title-container">
            <h3 class="pl20 f18">提示</h3>
            <a class="icon-i popup-close2" onclick="closeInfo()"></a></div>
        <div class="popup-content-container pb20 clearfix">
            <div class="tc mb30"><span class="icon-i w30 h30 va-middle radius-question-icon"></span><span
                    class="f20 h30 va-middle ml10">确定要启用此客服吗？</span></div>
            <div class="tc f16">
                <a name="button" id="button3" href="javascript:$('#sjForm').submit();" class="btn-blue2 btn white radius-6 pl20 pr20 sumbitForme">确 定</a>
                <a class="btn btn-blue2 radius-6 pl20 pr20 ml40" onclick="closeInfo()">取 消</a>
            </div>
        </div>
    </form>
</div>

<div class="popup-box hide" style="width:400px;min-height:200px;" id="xjDivId">
    <form action="<%=controller.getURI(request, UpdateKfzx.class) %>" id="xjForm" method="post" class="form1">
        <input type="hidden" name="id" id="xj_cid"/>
        <input type="hidden" name="status" value="<%=T5012_F11.TY.name()%>"/>
        <input type="hidden" name="doType" value="updateStatus"/>

        <div class="popup-title-container">
            <h3 class="pl20 f18">提示</h3>
            <a class="icon-i popup-close2" onclick="closeInfo()"></a></div>
        <div class="popup-content-container pb20 clearfix">
            <div class="tc mb30"><span class="icon-i w30 h30 va-middle radius-question-icon"></span><span
                    class="f20 h30 va-middle ml10">确定要停用此客服吗？</span></div>
            <div class="tc f16">
                <a name="button" id="button3" href="javascript:$('#xjForm').submit();" class="btn-blue2 btn white radius-6 pl20 pr20 sumbitForme">确 定</a>
                <a class="btn btn-blue2 radius-6 pl20 pr20 ml40" onclick="closeInfo()">取 消</a>
            </div>
        </div>
    </form>
</div>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/dialog.js"></script>
<script type="text/javascript">
    var del_url = '<%=controller.getURI(request, DeleteKfzx.class) %>';
    var reloadUrl = '<%=controller.getURI(request, SearchKfzx.class)%>';
    var upateOrderUrl = '<%=controller.getURI(request, UpdateOrder.class)%>';
    function onSubmit() {
        $("#form1").submit();
    }

    //启用操作
    function showQy(cid) {
        var bl = true;
        $.ajax({
            type: "post",
            dataType: "json",
            async: false,
            url: '<%=controller.getURI(request, CheckZxkf.class)%>',
            success: function (data) {
                if (data.success) {
                    $(".popup_bg").show();
                    $("#sj_cid").attr("value", cid);
                    $("#sjDivId").show();
                }else {
                    $("#info").html(showDialogInfo("启用的客服数量已达最大限制数：<%=allowZxkfCount%>。","wrong"));
                    $("div.popup_bg").show();
                }
            }
        });
    }

    //停用操作
    function showTy(cid) {
        $(".popup_bg").show();
        $("#xj_cid").attr("value", cid);
        $("#xjDivId").show();
    }

    <%
    String errorMsg = controller.getPrompt(request, response, PromptLevel.ERROR);
    if(!StringHelper.isEmpty(errorMsg)){
    %>
    $("#info").html(showDialogInfo('<%=errorMsg%>',"wrong"));
    $("div.popup_bg").show();
    <%}%>
</script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/checkAll.js"></script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/dialog.js"></script>

</body>
</html>