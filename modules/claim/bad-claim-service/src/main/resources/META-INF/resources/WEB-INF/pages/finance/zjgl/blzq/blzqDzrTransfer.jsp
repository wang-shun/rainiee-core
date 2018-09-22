<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.util.ObjectHelper" %>
<%@page import="com.dimeng.p2p.repeater.claim.entity.BadClaimZrDetails" %>
<%@page import="com.dimeng.p2p.S62.enums.T6230_F11" %>
<%@page import="com.dimeng.p2p.console.servlets.finance.zjgl.blzq.BlzqDzrList" %>
<%@page import="com.dimeng.p2p.console.servlets.finance.zjgl.blzq.BlzqZrsbList" %>
<%@page import="com.dimeng.p2p.console.servlets.finance.zjgl.blzq.BlzqDzrTransfer" %>
<%@page import="com.dimeng.p2p.repeater.claim.BadClaimTransferManage" %>
<%@page import="com.dimeng.util.parser.IntegerParser" %>
<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <%@include file="/WEB-INF/include/script.jsp"%>
</head>
<body>
<%
    CURRENT_CATEGORY = "CWGL";
    CURRENT_SUB_CATEGORY = "BLZQZRZR";
    BadClaimTransferManage badClaimTransferManage = serviceSession.getService(BadClaimTransferManage.class);
    BadClaimZrDetails badClaimZrDetails =
        badClaimTransferManage.getBadClaimZrDetailsr(IntegerParser.parse(request.getParameter("loanId")));
    request.setAttribute("badClaimZrDetails", badClaimZrDetails);
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <!--加载内容-->
                <div class="p20">
                    <div class="border">
                        <div class="title-container"><i class="icon-i w30 h30 va-middle title-left-icon"></i>不良债权转让管理</div>
                        <div class="content-container pt20 pl40 pr40 pb20">
                            <form action="<%=controller.getURI(request, BlzqDzrTransfer.class)%>" method="post">
                            <input type="hidden" name="periodId" value="<%=request.getParameter("periodId")%>">
                            <input type="hidden" name="loanId" value="<%=request.getParameter("loanId")%>">
                                <ul class="cell xc_jcxx ">
                                    <li class="mb10">
                                        <span class="display-ib w200 tr mr5 fl">借款账户：</span>
                                        <div class="pl200 ml5"><%StringHelper.filterHTML(out, badClaimZrDetails.loanName);%></div>
                                        <div class="clear"></div>
                                    </li>
                                    <li class="mb10">
                                        <span class="display-ib w200 tr mr5 fl">借款标题：</span>
                                        <div class="pl200 ml5"><%StringHelper.filterHTML(out, badClaimZrDetails.loanTitle);%></div>
                                        <div class="clear"></div>
                                    </li>
                                    <li class="mb10">
                                        <span class="display-ib w200 tr mr5 fl">债权价值：</span>
                                        <div class="pl200 ml5"><%=Formater.formatAmount(badClaimZrDetails.claimAmount)%>元</div>
                                        <div class="clear"></div>
                                    </li>
                                    <li class="mb10">
                                        <span class="display-ib w200 tr mr5 fl">转让价格：</span>
                                        <div class="pl200 ml5"><%=Formater.formatAmount(badClaimZrDetails.transferAmount)%>元</div>
                                        <div class="clear"></div>
                                    </li>
                                    <%if(T6230_F11.S == badClaimZrDetails.F11){%>
                                    <li class="mb10">
                                        <span class="display-ib w200 tr mr5 fl">担保方：</span>
                                        <div class="pl200 ml5"><%StringHelper.filterHTML(out, badClaimZrDetails.miga);%></div>
                                        <div class="clear"></div>
                                    </li>
                                    <li class="mb10">
                                        <span class="display-ib w200 tr mr5 fl">担保方式：</span>
                                        <div class="pl200 ml5"><%StringHelper.filterHTML(out, badClaimZrDetails.F12.getChineseName());%></div>
                                        <div class="clear"></div>
                                    </li>
                                    <%}%>
                                    <li class="mb10">
                                        <div class="pl100 ml5">
                                          <input type="button" onclick="transfer('<%=badClaimZrDetails.loanTitle%>');" class="btn btn-blue2 radius-6 pl20 pr20" value="转让"/>
                                          <input type="button" onclick="window.location.href='<%=controller.getURI(request, BlzqDzrList.class)%>'" class="btn btn-blue2 radius-6 pl20 pr20 ml40" value="取消"/>
                                        </div>
                                    </li>
                                </ul>
                            </form>
                        </div>
                    </div>
                </div>
                <!--加载内容 结束-->
            </div>
        </div>
    </div>
    <!--右边内容 结束-->
<!--内容-->
<div id="info"></div>
<div class="popup_bg" style="display: none;"></div>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/dialog.js"></script>
<%
    String message = controller.getPrompt(request, response, PromptLevel.INFO);
    if (!StringHelper.isEmpty(message)) {
%>

<script type="text/javascript">
    $("#info").html(showDialogInfo("<%=message%>", "yes"));
    $("div.popup_bg").show();
</script>
<%
    }
%>

<%
    String errorMessage = controller.getPrompt(request, response, PromptLevel.ERROR);
    if (!StringHelper.isEmpty(errorMessage)) {

%>
<script type="text/javascript">
    $("#info").html(showDialogInfo("<%=errorMessage%>", "wrong"));
    $("div.popup_bg").show();

</script>
<%
    }
%>

<%
    String warnMessage = controller.getPrompt(request, response, PromptLevel.WARRING);
    if (!StringHelper.isEmpty(warnMessage)) {
%>
<script type="text/javascript">
    $("#info").html(showDialogInfo("<%=warnMessage%>", "wrong"));
    $("div.popup_bg").show();
</script>
<%
    }
%>
<script type="text/javascript">
function transfer(title){
	$(".popup_bg").show();
	$("#info").html(showConfirmDiv("确认对借款标“"+title+"”进行不良债权转让？","",""));
}

function toConfirm(url, type) {
    document.forms[0].submit();
}
</script>
</body>
</html>