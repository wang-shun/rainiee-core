<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.console.servlets.finance.zjgl.txgl.TxglList"%>
<%@page import="com.dimeng.p2p.S61.enums.T6130_F09" %>
<%@page import="com.dimeng.p2p.console.servlets.finance.zjgl.txgl.Check" %>
<%@page import="com.dimeng.p2p.modules.account.console.service.entity.UserWithdrawals" %>
<%@page import="com.dimeng.util.ObjectHelper" %>
<%@page import="com.dimeng.p2p.common.FormToken" %>
<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <%@include file="/WEB-INF/include/script.jsp"%>
</head>
<%
    CURRENT_CATEGORY = "CWGL";
    CURRENT_SUB_CATEGORY = "TXGL";
    UserWithdrawals txglRecord = ObjectHelper.convert(request.getAttribute("txglRecord"), UserWithdrawals.class);
    if (txglRecord == null) {
        response.sendError(HttpServletResponse.SC_NOT_FOUND);
        return;
    }
%>
<body>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <div class="p20">
                    <form action="<%=configureProvider.format(URLVariable.CHECK_URL)%>" method="post" id="form1">
                    <%=FormToken.hidden(serviceSession.getSession()) %>
                        <input type="hidden" name="id" value="<%=txglRecord.F01%>"> <input type="hidden" name="status"
                                                                                           id="status"/>

                        <div class="border">
                            <div class="title-container">
                                <i class="icon-i w30 h30 va-middle title-left-icon"></i>提现审核
                            </div>
                        </div>
                        <div class="content-container pl40 pt40 pr40 pb20">
                            <ul class="gray6">
                                <li class="mb10">
									<span class="display-ib w200 tr mr5">
										<span class="red">&nbsp;</span>
									</span>

                                    <div class="pl200 ml5 orange"></div>
                                    <div class="clear"></div>
                                </li>
                                <li class="mb10">
                                    <span class="display-ib w200 tr mr5">流水号：</span>
                                    <%=txglRecord.F01%>
                                </li>
                                <li class="mb10">
                                    <span class="display-ib w200 tr mr5">用户名：</span>
                                    <%StringHelper.filterHTML(out, txglRecord.userName);%>
                                </li>
                                <li class="mb10">
                                    <span class="display-ib w200 tr mr5">开户名：</span>
                                    <%StringHelper.filterHTML(out, txglRecord.realName);%>
                                </li>
                                <%if(!StringHelper.isEmpty(txglRecord.location)){ %>
                                <li class="mb10">
                                    <span class="display-ib w200 tr mr5">开户地：</span>
                                    <%StringHelper.filterHTML(out, txglRecord.location);%>
                                </li>
                                <%} %>
                                <li class="mb10">
                                    <span class="display-ib w200 tr mr5">所在支行：</span>
                                    <%StringHelper.filterHTML(out, txglRecord.subbranch);%>
                                </li>
                                <li class="mb10">
                                    <span class="display-ib w200 tr mr5">银行卡账号：</span>
                                    <%StringHelper.filterHTML(out, StringHelper.decode(txglRecord.bankId));%>
                                </li>
                                <li class="mb10">
                                    <span class="display-ib w200 tr mr5">提现金额：</span>
                                    <%=Formater.formatAmount(txglRecord.F04)%>元
                                </li>
                                <li class="mb10">
                                    <span class="display-ib w200 tr mr5">手续费：</span>
                                    <%=Formater.formatAmount(txglRecord.F07)%>元
                                </li>
                                <li class="mb10">
                                    <span class="display-ib w200 tr mr5">申请时间：</span>
                                    <%=Formater.formatDateTime(txglRecord.F08)%>
                                </li>
                                <%--<li class="mb10">
                                    <span class="display-ib w200 tr mr5">审核时间：</span>
                                        <%=Formater.formatDateTime(txglRecord.F11)%>
                                </li>--%>
                                <li class="mb10">
                                    <div class="pl200 ml5">
                                        <span class="display-ib display-ib w200 tr pa ml60 left0">审核意见：</span>
                                        <textarea cols="50" class="border ml3 mt3 p5" rows="6" id="reason"
                                                  name="check_reason">如不通过需填写具体原因</textarea>
                                        <span id="errortip" class="error_tip"></span>
                                    </div>
                                    <div class="clear"></div>
                                </li>
                                <li class="mb10">
                                    <div class="pl200 ml5">
                                        <a href="javascript:void(0)" onclick="check('<%=T6130_F09.DFK.name()%>')"
                                           class="btn btn-blue2 radius-6 pl20 pr20">通过</a>
                                        <a href="javascript:void(0)" onclick="check('<%=T6130_F09.TXSB.name()%>')"
                                           class="btn btn-blue2 radius-6 ml40 pl20 pr20">不通过</a>
                                        <a href="<%=controller.getURI(request, TxglList.class)%>"
                                           class="btn btn-blue2 radius-6 pl20 pr20 ml40">取消</a>
                                    </div>
                                </li>
                            </ul>
                            <div class="clear"></div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
<%
    String warringMessage = controller.getPrompt(request, response, PromptLevel.WARRING);
    if (!StringHelper.isEmpty(warringMessage)) {
%>
<div class="w440 thickbox thickpos" style="margin:-80px 0 0 -220px;" id="showDiv">
    <div class="info clearfix">
        <div class="clearfix">
            <span class="fl tips"><%StringHelper.filterHTML(out, warringMessage); %></span>
        </div>
        <div class="dialog_btn"><input type="button" name="button2" onclick="$('#showDiv').hide()" value="确认"
                                       class="btn4 ml50"/></div>
    </div>
</div>
<%} %>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/txgl/withdrawals.js"></script>
<script type="text/javascript">
    reason();
    $("#reason").focus(function () {
    	var reason = $("#reason").val();
    	if(reason == '如不通过需填写具体原因'){
    		$("#reason").attr("value", "");
    		$("#reason").css("color", "");
    	}
    });
    $("#reason").blur(function () {
        reason();
    });
    function reason() {
        var reason = $("#reason").val();
        if (reason == "") {
            $("#reason").attr("value", "如不通过需填写具体原因");
            $("#reason").css("color", "gray");
        } else if (reason == '如不通过需填写具体原因') {
            $("#reason").css("color", "gray");
        }
    }
    function check(status) {
        var reason = $("#reason").val();
        $("#status").attr("value", status);
        if (status == '<%=T6130_F09.TXSB%>' && (reason == "" || reason == '如不通过需填写具体原因')) {
            alert("请填写不通过的原因!");
            return;
        } else if (status == '<%=T6130_F09.DFK%>' && reason == '如不通过需填写具体原因') {
            $("#reason").attr("value", "");
        }
        var reasonLength = reason.length;
        if (reasonLength > 100) {
            $("#errortip").html("超过输入限制100,当前长度为" + reasonLength);
            return;
        }
        $("#form1").submit();
    }
</script>
</body>
</html>