<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.jgxx.JgList" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.qyxx.QyList" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.jgxx.ViewJgxx" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.qyxx.ViewQyxx" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.zhgl.UnBlack" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.grxx.GrList" %>
<%@page import="com.dimeng.p2p.S61.enums.T6110_F10" %>
<%@page import="com.dimeng.p2p.S61.enums.T6110_F06" %>
<%@page import="com.dimeng.p2p.console.servlets.bid.csgl.hmd.HmdList" %>
<%@page import="com.dimeng.p2p.modules.bid.console.service.entity.BlacklistDetails" %>
<%@page import="com.dimeng.p2p.console.servlets.AbstractConsoleServlet" %>
<%@page import="com.dimeng.p2p.console.servlets.bid.csgl.hmd.Hmdxq" %>
<%@page import="com.dimeng.p2p.console.servlets.bid.csgl.hmd.DcHmd" %>
<%@page import="com.dimeng.p2p.modules.account.console.service.UserManage" %>
<%@page import="com.dimeng.p2p.modules.bid.console.service.CollectionManage" %>
<%@page import="com.dimeng.p2p.modules.bid.console.service.entity.BlacklistInfo" %>
<%@page import="com.dimeng.util.Formater" %>
<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <%@include file="/WEB-INF/include/script.jsp" %>
</head>
<body>
<%
    CURRENT_CATEGORY = "YWGL";
    CURRENT_SUB_CATEGORY = "HMD";
    PagingResult<BlacklistInfo> blacklistInfos = (PagingResult<BlacklistInfo>) request.getAttribute("blacklistInfos");
    BlacklistInfo[] blacklistInfoArray = blacklistInfos.getItems();
    CollectionManage collectionManage = serviceSession.getService(CollectionManage.class);
    UserManage userManage = serviceSession.getService(UserManage.class);
    String createTimeStart = request.getParameter("createTimeStart");
    String createTimeEnd = request.getParameter("createTimeEnd");
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <form id="form1" action="<%=controller.getURI(request, HmdList.class)%>" method="post">
                    <div class="p20">
                        <div class="border">
                            <div class="title-container">
                                <i class="icon-i w30 h30 va-middle title-left-icon"></i>黑名单
                            </div>
                            <div class="content-container pl40 pt30 pr40">
                                <ul class="gray6 input-list-container clearfix">
                                    <li><span class="display-ib mr5">用户名</span>
                                        <input type="text" name="userName" class="text border pl5 mr20"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("userName"));%>"/>
                                    </li>
                                    <li><span class="display-ib mr5">姓名/企业名称</span>
                                        <input type="text" name="realName" class="text border pl5 mr20"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("realName"));%>"/>
                                    </li>
                                    <li><span class="display-ib mr5">手机号码</span>
                                        <input type="text" name="msisdn" class="text border pl5 mr20"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("msisdn"));%>"/>
                                    </li>
                                    <li><span class="display-ib mr5">证件号</span>
                                        <input type="text" name="idCard" class="text border pl5 mr20"
                                               style="width: 145px;"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("idCard"));%>"/>
                                    </li>

                                    <li><span class="display-ib mr5">拉黑时间</span> <input
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
                                            if (dimengSession.isAccessableResource(DcHmd.class)) {
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

                        <div class="border table-container mt20">
                            <table class="table table-style gray6 tl">
                                <thead>
                                <tr class="title tc">
                                    <th>序号</th>
                                    <th>用户名</th>
                                    <th>姓名/企业名称</th>
                                    <th>手机号码</th>
                                    <th>邮箱地址</th>
                                    <th>证件号</th>
                                    <th>待还总额(元)</th>
                                    <th>拉黑时间</th>
                                    <th>操作人</th>
                                    <th class="w200">操作</th>
                                </tr>
                                </thead>
                                <tbody class="f12">
                                <%
                                    if (blacklistInfoArray != null && blacklistInfoArray.length > 0) {
                                        for (int i = 0; i < blacklistInfoArray.length; i++) {
                                            BlacklistInfo blacklistInfo = blacklistInfoArray[i];
                                            if (blacklistInfo == null) {
                                                continue;
                                            }
                                %>
                                <tr class="tc">
                                    <td><%=i + 1%>
                                    </td>
                                    <td><%StringHelper.filterHTML(out, blacklistInfo.accountName);%></td>
                                    <td><%StringHelper.filterHTML(out, blacklistInfo.realName);%></td>
                                    <td><%StringHelper.filterHTML(out, blacklistInfo.phone);%></td>
                                    <td><%StringHelper.filterHTML(out, blacklistInfo.email); %></td>
                                    <td><%StringHelper.filterHTML(out, blacklistInfo.idCard);%></td>
                                    <td><%=Formater.formatAmount(blacklistInfo.whMoney)%>
                                    </td>
                                    <td><%=TimestampParser.format(blacklistInfo.F05) %>
                                    </td>
                                    <td><%StringHelper.filterHTML(out, blacklistInfo.lhName);%></td>
                                    <td>
                                        <%if (T6110_F06.ZRR == blacklistInfo.userType) { %>
                                        <%
                                            if (dimengSession.isAccessableResource(com.dimeng.p2p.console.servlets.account.vipmanage.grxx.JbxxView.class)&&
                                                dimengSession.isAccessableResource(GrList.class)) {
                                        %>
                                        <a href="<%=controller.getURI(request,com.dimeng.p2p.console.servlets.account.vipmanage.grxx.JbxxView.class)%>?userId=<%=blacklistInfo.F02%>&status=1"
                                           class="link-blue mr10 link_url" showObj="GRXX" data-title="user">个人资料</a>
                                        <%} else { %>
                                        <a href="javascript:void(0)" class="disabled">个人资料</a>
                                        <%} %>
                                        <%} else if (T6110_F06.FZRR == blacklistInfo.userType && T6110_F10.F == blacklistInfo.dbf) { %>
                                        <%
                                            if (dimengSession.isAccessableResource(ViewQyxx.class)&&dimengSession.isAccessableResource(QyList.class)) {
                                        %>
                                        <a href="<%=controller.getURI(request,ViewQyxx.class)%>?id=<%=blacklistInfo.F02%>&status=1"
                                           class="link-blue mr10 link_url" showObj="QY" data-title="user">企业资料</a>
                                        <%} else { %>
                                        <a href="javascript:void(0)" class="disabled">企业资料</a>
                                        <%} %>
                                        <%} else if (T6110_F10.S == blacklistInfo.dbf) { %>
                                        <%
                                            if (dimengSession.isAccessableResource(ViewJgxx.class)&&dimengSession.isAccessableResource(JgList.class)) {
                                        %>
                                        <a href="<%=controller.getURI(request,ViewJgxx.class)%>?id=<%=blacklistInfo.F02%>&status=1"
                                           class="link-blue mr10 link_url" showObj="JG" data-title="user">机构资料</a>
                                        <%} else { %>
                                        <a href="javascript:void(0)" class="disabled">机构资料</a>
                                        <%} %>
                                        <%} %>
                                        <%if (dimengSession.isAccessableResource(Hmdxq.class)) {%>
                                        <a href="javascript:void(0)" onclick="showLhxq('<%=i %>')"
                                           class="link-blue mr10 popup-link" data-wh="<%=T6110_F10.S == blacklistInfo.dbf ? "500*300" : "500*550"%>">拉黑详情</a>
                                        <%} else { %>
                                        <a href="javascript:void(0)" class="disabled">拉黑详情</a>
                                        <%} %>
                                        <%if (dimengSession.isAccessableResource(UnBlack.class)) { %>
                                        <a href="javascript:void()" onclick="showQxlh(<%=i %>)"
                                           class="link-blue popup-link" data-wh="350*200">取消拉黑</a>
                                        <%} else { %>
                                        <a href="javascript:void(0)" class="disabled">取消拉黑</a>
                                        <%} %>
                                    </td>
                                </tr>
                                <%
                                    }
                                } else {
                                %>
                                <tr class="tc">
                                    <td colspan="10" style="border-bottom: 0px;">暂无数据</td>
                                </tr>
                                <%} %>
                                </tbody>
                            </table>
                        </div>
                        <!--分页-->
                        <%
                            AbstractConsoleServlet.rendPagingResult(out, blacklistInfos);
                        %>
                        <!--分页 end-->

                    </div>
                </form>
                <%
                    if (blacklistInfoArray != null) {
                        for (int i = 0; i < blacklistInfoArray.length; i++) {
                            BlacklistInfo blacklistInfo = blacklistInfoArray[i];
                            if (blacklistInfo == null) {
                                continue;
                            }
                            BlacklistDetails entity = collectionManage.findBlacklistDetails(blacklistInfo.F01);
                %>

                <div class="popup-box hide" id="qxlh_<%=i%>" style="min-height: 200px;">
                    <form action="<%=controller.getURI(request, UnBlack.class)%>" method="post">
                        <input type="hidden" name="id" value="<%=blacklistInfo.F01%>"/>
                        <input type="hidden" name="userId" value="<%=blacklistInfo.F02%>"/>
                        <input type="hidden" name="state" value="QXLH"/>
                        <input type="hidden" name="blacklistDesc" value=""/>

                        <div class="popup-title-container">
                            <h3 class="pl20 f18">取消拉黑</h3>
                            <a class="icon-i popup-close2" href="javascript:void(0);" onclick="closeInfo()"></a>
                        </div>
                        <div class="popup-content-container pt20 pb20 clearfix">
                            <div class="tc mb10"><span class="icon-i w30 h30 va-middle radius-question-icon"></span><span
                                    class="f20 h30 va-middle ml10">是否取消拉黑<span id="a"><%
                                StringHelper.filterHTML(out, entity.userName);%></span>？</span></div>
                            <div class="tc f16 mt20">
                                <input type="submit" value="确定 " class="btn-blue2 btn white radius-6 pl20 pr20"/>
                                <!-- <a class="btn btn-blue2 radius-6 pl20 pr20 ml40" href="javascript:closeInfo();">取消</a> -->
                                <input type="button" value="取消" class="btn-blue2 btn white radius-6 pl20 pr20 ml40" onclick="closeInfo();">
                            </div>
                        </div>
                    </form>
                </div>
                <div id="lhxq_<%=i %>" class="popup-box hide">
                    <div class="popup-title-container">
                        <h3 class="pl20 f18">拉黑详情</h3>
                        <a class="icon-i popup-close2" onclick="closeInfo()"></a></div>
                    <div class="popup-content-container-2" style="max-height:600px;">
                        <div class="p30">
                            <ul class="gray6">
                                <li class="mb15"><span class="display-ib tr mr5 w120 fl">用户名：</span>

                                    <div class="pl120"><%StringHelper.filterHTML(out, entity.userName); %></div>
                                </li>
                                <li class="mb15 clearfix"><span class="display-ib tr mr5 w120 fl">真实姓名：</span>

                                    <div class="pl120"><%StringHelper.filterHTML(out, entity.realName); %></div>
                                </li>
                                <%if(blacklistInfo.dbf != T6110_F10.S){ %>
                                <li class="mb15"><span class="display-ib tr mr5 w120 fl">申请借款（笔）：</span>

                                    <div class="pl120"><%=entity.applyLoanCount %>
                                    </div>
                                </li>
                                <li class="mb15"><span class="display-ib tr mr5 w120 fl">成功借款（笔）：</span>

                                    <div class="pl120">
                                        <%=entity.sucLoanCount %>
                                    </div>
                                </li>
                                <li class="mb15"><span class="display-ib tr mr5 w120 fl">还清借款（笔）：</span>

                                    <div class="pl120">
                                        <%=entity.payOffCount %>
                                    </div>
                                </li>
                                <%} %>
                                <li class="mb15"><span class="display-ib tr mr5 w120 fl">信用额度（元）：</span>

                                    <div class="pl120">
                                        <%=Formater.formatAmount(entity.creditLine)%>
                                    </div>
                                </li>
                                <%if(blacklistInfo.dbf != T6110_F10.S){ %>
                                <li class="mb15"><span class="display-ib tr mr5 w120 fl">借款总额（元）</span>

                                    <div class="pl120">
                                        <%=Formater.formatAmount(entity.loanMoney)%>
                                    </div>
                                </li>
                                <li class="mb15"><span class="display-ib tr mr5 w120 fl">待还本息（元）：</span>

                                    <div class="pl120">
                                        <%=Formater.formatAmount(entity.borrowingLiability) %>
                                    </div>
                                </li>
                                <li class="mb15"><span class="display-ib tr mr5 w120 fl">逾期次数（次）：</span>

                                    <div class="pl120">
                                        <%=entity.overdueCount%>
                                    </div>
                                </li>
                                <li class="mb15"><span class="display-ib tr mr5 w120 fl">严重逾期（笔）：</span>

                                    <div class="pl120">
                                        <%=entity.seriousOverdue%>
                                    </div>
                                </li>
                                <%} %>
                                <li class="mb15"><span class="display-ib tr mr5 w120 fl">拉黑说明 ：</span>

                                    <div class="pl120">
                                        <%StringHelper.filterHTML(out, entity.registrationDesc); %>
                                    </div>
                                </li>
                            </ul>
                            <div class="tc f16">
                                <a class="btn btn-blue2 radius-6 pl20 pr20 ml40" href="javascript:closeInfo();">关闭</a>
                            </div>


                        </div>
                    </div>
                </div>


                <%
                        }
                    }
                %>
            </div>
        </div>
        <div class="popup_bg hide"></div>
    </div>
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

    function showExport() {
        document.getElementById("form1").action = "<%=controller.getURI(request, DcHmd.class)%>";
        $("#form1").submit();
        document.getElementById("form1").action = "<%=controller.getURI(request, HmdList.class)%>";
    }

    function showLhxq(i) {
        $(".popup_bg").show();
        $("#lhxq_" + i).show();
    }

    function showQxlh(i) {
        $(".popup_bg").show();
        $("#qxlh_" + i).show();
    }
</script>
</body>
</html>