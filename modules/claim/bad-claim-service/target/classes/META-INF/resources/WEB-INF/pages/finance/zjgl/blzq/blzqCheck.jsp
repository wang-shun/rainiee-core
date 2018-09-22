<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.util.ObjectHelper" %>
<%@page import="com.dimeng.p2p.repeater.claim.entity.BadClaimShDetails" %>
<%@page import="com.dimeng.p2p.S62.enums.T6230_F11" %>
<%@page import="com.dimeng.p2p.S62.enums.T6264_F04" %>
<%@page import="com.dimeng.p2p.console.servlets.finance.zjgl.blzq.BlzqDshList" %>
<%@page import="com.dimeng.p2p.console.servlets.finance.zjgl.blzq.BlzqCheck" %>
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
    CURRENT_SUB_CATEGORY = "BLZQCHECK";
    BadClaimTransferManage badClaimTransferManage = serviceSession.getService(BadClaimTransferManage.class);
    BadClaimShDetails badClaimShDetails =
        badClaimTransferManage.getBadClaimShDetailsr(IntegerParser.parse(request.getParameter("id")));
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <!--加载内容-->
                <div class="p20">
                    <div class="border">
                        <div class="title-container"><i class="icon-i w30 h30 va-middle title-left-icon"></i>不良债权转让管理</div>
                        <div class="content-container pt20 pl40 pr40 pb20">
                            <form id="checkForm" action="<%=controller.getURI(request, BlzqCheck.class)%>" method="post">
                            <input type="hidden" name="id" value="<%=request.getParameter("id")%>">
                            <input type="hidden" name="T6264_F04">
                            <input type="hidden" name="claimAmount" value="<%=badClaimShDetails.claimAmount%>">
                            <input type="hidden" name="transferAmount" value="<%=badClaimShDetails.transferAmount%>">
                            <input type="hidden" name="lateDays" value="<%=badClaimShDetails.lateDays%>">
                            <input type="hidden" name="describe">
                                <ul class="cell xc_jcxx ">
                                    <li class="mb10">
                                        <span class="display-ib w200 tr mr5 fl">借款账户：</span>
                                        <div class="pl200 ml5"><%StringHelper.filterHTML(out, badClaimShDetails.loanName);%></div>
                                        <div class="clear"></div>
                                    </li>
                                    <li class="mb10">
                                        <span class="display-ib w200 tr mr5 fl">借款标题：</span>
                                        <div class="pl200 ml5"><%StringHelper.filterHTML(out, badClaimShDetails.loanTitle);%></div>
                                        <div class="clear"></div>
                                    </li>
                                    <li class="mb10">
                                        <span class="display-ib w200 tr mr5 fl">债权价值：</span>
                                        <div class="pl200 ml5"><%=Formater.formatAmount(badClaimShDetails.claimAmount)%>元</div>
                                        <div class="clear"></div>
                                    </li>
                                    <li class="mb10">
                                        <span class="display-ib w200 tr mr5 fl">转让价格：</span>
                                        <div class="pl200 ml5"><%=Formater.formatAmount(badClaimShDetails.transferAmount)%>元</div>
                                        <div class="clear"></div>
                                    </li>
                                    <%if(T6230_F11.S == badClaimShDetails.F11){%>
                                    <li class="mb10">
                                        <span class="display-ib w200 tr mr5 fl">担保方：</span>
                                        <div class="pl200 ml5"><%StringHelper.filterHTML(out, badClaimShDetails.miga);%></div>
                                        <div class="clear"></div>
                                    </li>
                                    <li class="mb10">
                                        <span class="display-ib w200 tr mr5 fl">担保方式：</span>
                                        <div class="pl200 ml5"><%StringHelper.filterHTML(out, badClaimShDetails.F12.getChineseName());%></div>
                                        <div class="clear"></div>
                                    </li>
                                    <%}%>
                                    <li class="mb10">
                                        <span class="display-ib w200 tr mr5 fl">申请时间：</span>
                                        <div class="pl200 ml5"><%=TimestampParser.format(badClaimShDetails.applyTime)%></div>
                                        <div class="clear"></div>
                                    </li>
                                    <li class="mb10">
                                        <div class="pl100 ml5">
                                          <input type="button" onclick="check('<%=T6264_F04.ZRZ.name()%>');" class="btn btn-blue2 radius-6 pl20 pr20" value="审核通过"/>
                                          <input type="button" onclick="check('<%=T6264_F04.ZRSB.name()%>');" class="btn btn-blue2 radius-6 pl20 pr20 ml40" value="审核不通过"/>
                                          <input type="button" onclick="window.location.href='<%=controller.getURI(request, BlzqDshList.class)%>'" class="btn btn-blue2 radius-6 pl20 pr20 ml40" value="取消"/>
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

    <div id="noPassDiv" style="display: none;" class="popup-box" style="min-height: 270px;">
            <div class="popup-title-container">
                <h3 class="pl20 f18">审核不通过</h3>
                <a class="icon-i popup-close2" href="javascript:void(0);"
                   onclick="closeInfo();"></a>
            </div>
            <div class="popup-content-container pt20 ob20 clearfix" >
                <div class="mb20 gray6">
                    <p class="h30 lh30"><em class="red pr5">*</em>审核处理结果描述（50字以内）</p>
                    <div>
                        <textarea class="w400 h120 border p5" rows="4" cols="40" name="des"></textarea>
                        <span id="errortip" class="error_tip pl20"></span>
                    </div>
                </div>
                <div class="tc f16">
                    <input type="button" onclick="noPassCheck();" class="btn-blue2 btn white radius-6 pl20 pr20" value="确定" />
                    <input type="button" value="取消" class="btn-blue2 btn white radius-6 pl20 pr20 ml40" onclick="closeInfo();"/>
                </div>
            </div>
    </div>

<div id="info"></div>
<div class="popup_bg" style="display: none;"></div>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/dialog.js"></script>
<script type="text/javascript">
function check(state){
	
	$(".popup_bg").show();
	$("input[name='T6264_F04']").val(state);
	if("ZRZ" == state){
		$("#info").html(showConfirmDiv("确认审核通过？","",""));
	}else{
		$("#errortip").html('');
		$("textarea[name='des']").val('');
		$("#noPassDiv").show();
	}		
}

function toConfirm(url, type) {
	$("#checkForm").submit();
}

function noPassCheck() {
	var des = $("textarea[name='des']").val();
	var errortip = $("#errortip");
	if(!des){
		errortip.html('不能为空！');
		return;
	}
	des = des.trim();
	if(des.length > 50){
		errortip.html('不能超过50个字！');
		return;
	}
	$("input[name='describe']").val(des);
	toConfirm("", "");
}

</script>
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
</body>
</html>