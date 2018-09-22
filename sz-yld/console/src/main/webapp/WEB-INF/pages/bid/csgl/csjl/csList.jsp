<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.console.servlets.bid.csgl.csjl.CsList"%>
<%@page import="com.dimeng.p2p.console.servlets.bid.csgl.csjl.DcCsList" %>
<%@page import="com.dimeng.p2p.console.servlets.bid.loanmanage.jkgl.ViewProject" %>
<%@page import="com.dimeng.p2p.S61.enums.T6110_F10" %>
<%@page import="com.dimeng.p2p.S61.enums.T6110_F06" %>
<%@page import="com.dimeng.p2p.console.servlets.AbstractConsoleServlet" %>
<%@page import="com.dimeng.p2p.console.servlets.bid.csgl.csjl.Csxq" %>
<%@page import="com.dimeng.p2p.S71.enums.T7152_F04" %>
<%@page import="com.dimeng.p2p.modules.account.console.service.UserManage" %>
<%@page import="com.dimeng.p2p.modules.bid.console.service.CollectionManage" %>
<%@page import="com.dimeng.p2p.modules.bid.console.service.entity.CollectionRecordInfo" %>
<%@page import="com.dimeng.p2p.console.servlets.bid.csgl.csjl.XscsList" %>
<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <%@include file="/WEB-INF/include/script.jsp" %>
</head>
<body>
<%
    CURRENT_CATEGORY = "YWGL";
    CURRENT_SUB_CATEGORY = "CSJL";
    PagingResult<CollectionRecordInfo> collectionRecordInfos = (PagingResult<CollectionRecordInfo>) request.getAttribute("collectionRecords");
    CollectionRecordInfo[] collectionRecordInfoArray = collectionRecordInfos.getItems();
    CollectionManage collectionManage = serviceSession.getService(CollectionManage.class);
    UserManage userManage = serviceSession.getService(UserManage.class);
    String createTimeStart = request.getParameter("createTimeStart");
    String createTimeEnd = request.getParameter("createTimeEnd");
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <form id="form1" action="<%=controller.getURI(request, CsList.class)%>" method="post">
                    <div class="p20">
                        <div class="border">
                            <div class="title-container">
                                <i class="icon-i w30 h30 va-middle title-left-icon"></i>催收记录
                            </div>
                            <div class="content-container pl40 pt30 pr40">
                                <ul class="gray6 input-list-container clearfix">
                                    <li><span class="display-ib mr5">借款者</span>
                                        <input type="text" name="userName" class="text border pl5 mr20"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("userName"));%>"/>
                                    </li>
                                    <li><span class="display-ib mr5">催收方式</span>
                                        <select name="type" class="border mr20 h32 mw100"">
                                        <option value="">全部</option>
                                        <%
                                            for (T7152_F04 collectionType : T7152_F04.values()) {
                                        %>
                                        <option value="<%=collectionType.name()%>" <%if (collectionType.name().equals(request.getParameter("type"))) {%>
                                                selected="selected" <%}%>><%=collectionType.getChineseName()%>
                                        </option>
                                        <%
                                            }
                                        %>
                                        </select>
                                    </li>

                                    <li><span class="display-ib mr5">催收时间</span> <input
                                            type="text" name="createTimeStart" readonly="readonly"
                                            id="datepicker1" class="text border pl5 w120 date"/> <span
                                            class="pl5 pr5">至</span> <input type="text"
                                                                            readonly="readonly" name="createTimeEnd"
                                                                            id="datepicker2"
                                                                            class="text border pl5 w120 date mr20"/>
                                    </li>

                                    <li><a href="javascript:$('#form1').submit();"
                                           class="btn btn-blue radius-6 mr5 pl1 pr15"><i
                                            class="icon-i w30 h30 va-middle search-icon "></i>搜索</a></li>
                                    <li>
                                        <%
                                            if (dimengSession.isAccessableResource(DcCsList.class)) {
                                        %> <a href="javascript:void(0)"
                                              onclick="showExport()"
                                              class="btn btn-blue radius-6 mr5  pl1 pr15"><i
                                            class="icon-i w30 h30 va-middle export-icon "></i>导出</a> <%
                                    } else {
                                    %> <a href="javascript:void(0)" class="btn btn-gray radius-6 mr5  pl1 pr15"><i
                                            class="icon-i w30 h30 va-middle export-icon "></i>导出</a> <%
                                        }
                                    %>
                                    </li>

                                </ul>
                            </div>
                        </div>
                        <div class="border mt20">
                            <div class="tabnav-container">
                                <ul class="clearfix">
                                    <li><a href="javascript:void(0);" class="tab-btn select-a">线下催收<i
                                        class="icon-i tab-arrowtop-icon"></i></a></li>
                                    <li><a href="<%=controller.getURI(request, XscsList.class)%>"
                                           class="tab-btn ">线上催收</a></li>

                                </ul>
                            </div>
                            <div class=" table-container p20">
                                <table class="table table-style gray6 tl">
                                    <thead>
                                    <tr class="title tc">
                                        <th>序号</th>
                                        <th>借款ID</th>
                                        <th>借款者</th>
                                        <th>催收方式</th>
                                        <th>催收人</th>
                                        <th>催收时间</th>
                                        <th>结果概要</th>
                                        <th class="w200">操作</th>
                                    </tr>
                                    </thead>
                                    <tbody class="f12">
                                    <%
                                        if (collectionRecordInfoArray != null && collectionRecordInfoArray.length > 0) {
                                            for (int i = 0; i < collectionRecordInfoArray.length; i++) {
                                                CollectionRecordInfo collectionRecordInfo = collectionRecordInfoArray[i];
                                                if (collectionRecordInfo == null) {
                                                    continue;
                                                }
                                    %>
                                    <tr class="tc">
                                        <td><%=i + 1%>
                                        </td>
                                        <td>
                                            <%
                                                if (dimengSession.isAccessableResource(ViewProject.class)) {
                                            %>
                                            <a href="<%=controller.getURI(request, ViewProject.class)%>?loanId=<%=collectionRecordInfo.F02%>&userId=<%=collectionRecordInfo.F03%>"
                                               class="link-blue mr10 link_url" showObj="BDGL" data-title="business"><%=collectionRecordInfo.bidNum%>
                                            </a>
                                            <%}else{%>
                                            <%=collectionRecordInfo.bidNum%>
                                            <%}%>
                                        </td>
                                        <td>
                                            <%
                                                StringHelper.filterHTML(out, collectionRecordInfo.userName);
                                            %>
                                        </td>
                                        <td><%=collectionRecordInfo.F04.getChineseName()%>
                                        </td>
                                        <td>
                                            <%
                                                StringHelper.filterHTML(out, collectionRecordInfo.F05);
                                            %>
                                        </td>
                                        <td><%=Formater.formatDate(collectionRecordInfo.F08)%>
                                        </td>
                                        <td title="<%StringHelper.filterHTML(out, collectionRecordInfo.F06);%>">
                                            <%
                                                StringHelper.filterHTML(out, StringHelper.truncation(collectionRecordInfo.F06, 35));
                                            %>
                                        </td>
                                        <td>
                                            <%if (T6110_F06.ZRR == collectionRecordInfo.userType) { %>
                                            <%
                                                if (dimengSession.isAccessableResource(com.dimeng.p2p.console.servlets.account.vipmanage.grxx.JbxxView.class)) {
                                            %>
                                            <a href="<%=controller.getURI(request,com.dimeng.p2p.console.servlets.account.vipmanage.grxx.JbxxView.class)%>?userId=<%=collectionRecordInfo.F03%>&status=1"
                                               class="link-blue mr10 link_url" showObj="GRXX" data-title="user">个人资料</a>
                                            <%} else { %>
                                            <a href="javascript:void(0)" class="disabled">个人资料</a>
                                            <%} %>
                                            <%} else if (T6110_F06.FZRR == collectionRecordInfo.userType && T6110_F10.F == collectionRecordInfo.dbf) { %>
                                            <%
                                                if (dimengSession.isAccessableResource(com.dimeng.p2p.console.servlets.account.vipmanage.qyxx.ViewQyxx.class)) {
                                            %>
                                            <a href="<%=controller.getURI(request,com.dimeng.p2p.console.servlets.account.vipmanage.qyxx.ViewQyxx.class)%>?id=<%=collectionRecordInfo.F03%>&status=1"
                                               class="link-blue mr10 link_url" showObj="QY" data-title="user">企业资料</a>
                                            <%} else { %>
                                            <a href="javascript:void(0)" class="disabled">企业资料</a>
                                            <%} %>
                                            <%} else if (T6110_F10.S == collectionRecordInfo.dbf) { %>
                                            <%
                                                if (dimengSession.isAccessableResource(com.dimeng.p2p.console.servlets.account.vipmanage.jgxx.ViewJgxx.class)) {
                                            %>
                                            <a href="<%=controller.getURI(request,com.dimeng.p2p.console.servlets.account.vipmanage.jgxx.ViewJgxx.class)%>?id=<%=collectionRecordInfo.F03%>&status=1"
                                               class="link-blue mr10 link_url" showObj="JG" data-title="user">机构资料</a>
                                            <%} else { %>
                                            <a href="javascript:void(0)" class="disabled">机构资料</a>
                                            <%} %>
                                            <%} %>
                                            <%
                                                if (dimengSession.isAccessableResource(Csxq.class)) {
                                            %>
                                            <a href="javascript:void(0)" onclick="showCsxq('<%=i%>')" class="link-blue popup-link" data-wh="500*500">催款详情</a> <%
                                        } else {
                                        %>
                                            <a href="javascript:void(0)" class="disabled">催款详情</a>
                                            <%
                                                }
                                            %>
                                        </td>
                                    </tr>
                                    <%
                                        }
                                    } else {
                                    %>
                                    <tr class="tc">
                                        <td colspan="8">暂无数据</td>
                                    </tr>
                                    <%} %>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                        <!--分页-->
                        <%
                            AbstractConsoleServlet.rendPagingResult(out, collectionRecordInfos);
                        %>
                        <!--分页 end-->

                    </div>
                </form>
            </div>
        </div>
        <%
            if (collectionRecordInfoArray != null) {
                for (int i = 0; i < collectionRecordInfoArray.length; i++) {
                    CollectionRecordInfo collectionRecordInfo = collectionRecordInfoArray[i];
                    if (collectionRecordInfo == null) {
                        continue;
                    }
                    CollectionRecordInfo entity = collectionManage.findCollectionRecord(collectionRecordInfo.F01);
        %>
        <div id="csxq_<%=i%>" class="popup-box hide">
            <div class="popup-title-container">
                <h3 class="pl20 f18">催收详情</h3>
                <a class="icon-i popup-close2" onclick="closeInfo()"></a></div>
            <div class="popup-content-container-2" style="max-height:550px;">
                <div class="p30">
                    <ul class="gray6">
                        <li class="mb15"><span class="display-ib tr mr5 w120 fl">借款者：</span>

                            <div class="pl120"><%
                                StringHelper.filterHTML(out, entity.userName);
                            %></div>
                        </li>
                        <li class="mb15"><span class="display-ib tr mr5 w120 fl">借款ID：</span>

                            <div class="pl120"><%=collectionRecordInfo.bidNum%>
                            </div>
                        </li>
                        <li class="mb15"><span class="display-ib tr mr5 w120 fl">催收方式：</span>

                            <div class="pl120">
                                <%=entity.F04.getChineseName()%>
                            </div>
                        </li>
                        <li class="mb15"><span class="display-ib tr mr5 w120 fl"><em class="red mr5">*</em>催收人：</span>

                            <div class="pl120">
                                <%
                                    StringHelper.filterHTML(out, entity.F05);
                                %>
                            </div>
                        </li>
                        <li class="mb15"><span class="display-ib tr mr5 w120 fl"><em class="red mr5">*</em>催收时间：</span>

                            <div class="pl120">
                                <%=Formater.formatDate(entity.F08)%>
                            </div>
                        </li>
                        <li class="mb15"><span class="display-ib tr mr5 w120 fl"><em class="red mr5">*</em>结果概要：</span>

                            <div class="pl120">
                                <%
                                    StringHelper.filterHTML(out, entity.F06);
                                %>
                            </div>
                        </li>
                        <li class="mb15"><span class="display-ib tr mr5 w120 fl">备注：</span>

                            <div class="pl120">
                                <%
                                    StringHelper.filterHTML(out, entity.F07);
                                %>
                            </div>
                        </li>
                    </ul>
                    <div class="clear"></div>
                    <div class="tc f16">
                        <a class="btn btn-blue2 radius-6 pl20 pr20" href="javascript:closeInfo();">关闭</a>
                    </div>


                </div>
            </div>
        </div>
        <%
                }
            }
        %>
        <div class="popup_bg hide"></div>
    </div>
<%@include file="/WEB-INF/include/datepicker.jsp" %>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/validation.js"></script>
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

    function showCsxq(id) {
        $(".popup_bg").show();
        $("#csxq_" + id).show();
    }

    function showLh(i) {
        if (confirm("此账号无逾期情况，请慎重操作！")) {
            $("#lh_" + i).show();
        }
    }

    function showYzyqLh(i) {
        if (confirm("此账号有严重逾期情况，是否拉黑！")) {
            $("#lh_" + i).show();
        }
    }

    function showExport() {
        document.getElementById("form1").action = "<%=controller.getURI(request, DcCsList.class)%>";
        $("#form1").submit();
        document.getElementById("form1").action = "<%=controller.getURI(request, CsList.class)%>";
    }
</script>
</body>
</html>