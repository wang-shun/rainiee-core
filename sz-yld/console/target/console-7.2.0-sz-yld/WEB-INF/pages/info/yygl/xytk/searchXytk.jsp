<%@page import="com.dimeng.p2p.console.servlets.info.yygl.xytk.SearchXytk" %>
<%@page import="com.dimeng.p2p.console.servlets.info.yygl.xytk.UpdateXytk" %>
<%@page import="com.dimeng.p2p.console.servlets.info.yygl.xytk.DeleteXytk" %>
<%@page import="com.dimeng.p2p.modules.base.console.service.entity.TermRecord" %>
<%@page import="com.dimeng.util.ObjectHelper" %>
<%@ page import="com.dimeng.p2p.S50.enums.T5017_F05" %>
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
    CURRENT_SUB_CATEGORY = "XYTK";
    PagingResult<TermRecord> result = ObjectHelper.convert(request.getAttribute("result"), PagingResult.class);
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <form action="<%=controller.getURI(request, SearchXytk.class)%>" method="post" id="form1">
                    <div class="p20">
                        <div class="border">
                            <div class="title-container">
                                <i class="icon-i w30 h30 va-middle title-left-icon"></i>协议条款
                            </div>
                            <%--<div class="content-container pl40 pt30 pr40">
                                <ul class="gray6 input-list-container clearfix">
                                    <li>
                                        <span>发布者：</span><span><input type="text" name="publisherName" id="textfield4"
                                                                      class="text border pl5 mr20"
                                                                      value="<%StringHelper.filterHTML(out, request.getParameter("publisherName"));%>"/></span>
                                    </li>
                                    <li>
                                        <a href="javascript:void(0)" onclick="onSubmit()"
                                           class="btn btn-blue radius-6 mr5 pl1 pr15"><i
                                                class="icon-i w30 h30 va-middle search-icon "></i>搜索</a>
                                    </li>
                                    <li>
                                        <%if (dimengSession.isAccessableResource(AddXytk.class)) {%>
                                        <a href="<%=controller.getURI(request, AddXytk.class)%>"
                                           class="btn btn-blue radius-6 mr5 pl1 pr15"><i
                                                class="icon-i w30 h30 va-middle add-icon "></i>新增</a>
                                        <%} else { %>
                                        <span class="btn btn-gray radius-6 mr5 pl1 pr15"><i
                                                class="icon-i w30 h30 va-middle add-icon "></i>新增</span>
                                        <%} %>
                                    </li>
                                    <li>
                                        <%if (dimengSession.isAccessableResource(DeleteXytk.class)) {%>
                                        <a href="javascript:void(0)" id="delAll"
                                           class="btn btn-blue2 radius-6 mr5 pl10 pr10">批量删除</a>
                                        <%} else { %>
                                        <a href="javascript:void(0)"
                                           class="btn btn-gray radius-6 mr5 pl10 pr10">批量删除</a>
                                        <%} %>
                                    </li>
                                </ul>
                            </div>--%>
                        </div>
                        <div class="border mt20 table-container">
                            <table class="table table-style gray6 tl">
                                <thead>
                                <tr class="title" align="center">
                                    <th class="tc">序号</th>
                                    <th>协议类型</th>
                                    <th>最后修改人</th>
                                    <th>最后修改时间</th>
                                    <th>状态</th>
                                    <th>操作</th>
                                </tr>
                                </thead>
                                <tbody class="f12">
                                <% TermRecord[] records = result.getItems();
                                    if (records != null) {
                                        int index = 1;
                                        for (TermRecord record : records) {
                                %>
                                <tr class="title" align="center">
                                    <td class="tc"><%=index++ %>
                                    </td>
                                    <td><%StringHelper.filterHTML(out, record.type.getName()); %></td>
                                    </td>
                                    <td><%StringHelper.filterHTML(out, record.updaterName); %></td>
                                    <td><%=DateTimeParser.format(record.updateTime) %>
                                    <td><%=record.status.getChineseName() %>
                                    </td>
                                    <td class="blue">
                                        <%if (dimengSession.isAccessableResource(UpdateXytk.class)) {%>
                                        <a href="<%=controller.getURI(request, UpdateXytk.class)%>?id=<%StringHelper.filterHTML(out, record.type.name()); %>"
                                           class="link-blue mr20">修改</a>
                                        <%} else { %>
                                        <span class="disabled">修改</span>
                                        <%} %>

                                        <%if (dimengSession.isAccessableResource(UpdateXytk.class)) {
                                            if(T5017_F05.QY.name().equals(record.status.name())){ %>
                                            <a href="javascript:void(0);" onclick="showTy('<%StringHelper.filterHTML(out, record.type.name()); %>')"
                                               class="link-blue mr20">停用</a>
                                            <%}else{%>
                                            <a href="javascript:void(0);" onclick="showQy('<%StringHelper.filterHTML(out, record.type.name()); %>')"
                                               class="link-blue mr20">启用</a>
                                        <%}} else { %>
                                            <%if(T5017_F05.QY.name().equals(record.status.name())){ %>
                                             <span class="disabled">停用</span>
                                            <%}else{%>
                                            <span class="disabled">启用</span>
                                        <%}} %>
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
                    </div>
                </form>
            </div>
        </div>
        <div class="popup_bg hide"></div>
    </div>
<div class="popup-box hide" id="qyDivId">
    <form action="<%=controller.getURI(request, UpdateXytk.class) %>" id="sjForm" method="post" class="form1">
        <input type="hidden" name="id" id="qy_cid"/>
        <input type="hidden" name="status" value="<%=T5017_F05.QY.name()%>"/>
        <div class="popup-title-container">
            <h3 class="pl20 f18">提示</h3>
            <a class="icon-i popup-close2" onclick="closeInfo()"></a></div>
        <div class="popup-content-container pb20 clearfix">
            <div class="tc mb30 mt40"><span class="icon-i w30 h30 va-middle radius-question-icon"></span><span
                    class="f20 h30 va-middle ml10">确定要启用此协议吗？</span></div>
            <div class="tc f16">
                <input name="button" type="submit" class="btn-blue2 btn white radius-6 pl20 pr20 sumbitForme"
                       id="button3" fromname="form1" value="确认"/>
                <!-- <a class="btn btn-blue2 radius-6 pl20 pr20 ml40" onclick="closeInfo()">取消</a> -->
                <input type="button" value="取消" class="btn-blue2 btn white radius-6 pl20 pr20 ml40" onclick="closeInfo();"/>
            </div>
        </div>
    </form>
</div>

<div class="popup-box hide" id="tyDivId">
    <form action="<%=controller.getURI(request, UpdateXytk.class) %>" id="xjForm" method="post" class="form1">
        <input type="hidden" name="id" id="ty_cid"/>
        <input type="hidden" name="status" value="<%=T5017_F05.TY.name()%>"/>
        <div class="popup-title-container">
            <h3 class="pl20 f18">提示</h3>
            <a class="icon-i popup-close2" onclick="closeInfo()"></a></div>
        <div class="popup-content-container pb20 clearfix">
            <div class="tc mb30 mt40"><span class="icon-i w30 h30 va-middle radius-question-icon"></span><span
                    class="f20 h30 va-middle ml10">确定要停用此协议吗？</span></div>
            <div class="tc f16">
                <input name="button" type="submit" class="btn-blue2 btn white radius-6 pl20 pr20 sumbitForme"
                       id="button4" fromname="form1" value="确认"/>
                <!-- <a class="btn btn-blue2 radius-6 pl20 pr20 ml40" onclick="closeInfo()">取消</a> -->
                <input type="button" value="取消" class="btn-blue2 btn white radius-6 pl20 pr20 ml40" onclick="closeInfo();"/>
            </div>
        </div>
    </form>
</div>
<div class="popup_bg hide"></div>
<div id="info"></div>
<script type="text/javascript">
    var del_url = '<%=controller.getURI(request, DeleteXytk.class) %>';
    function onSubmit() {
        $("#form1").submit();
    }
</script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/dialog.js"></script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/checkAll.js"></script>
<script type="text/javascript">
    //启用操作
    function showQy(cid) {
        $(".popup_bg").show();
        $("#qy_cid").attr("value", cid);
        $("#qyDivId").show();
    }

    //停用操作
    function showTy(cid) {
        $(".popup_bg").show();
        $("#ty_cid").attr("value", cid);
        $("#tyDivId").show();
    }

</script>
</body>
</html>