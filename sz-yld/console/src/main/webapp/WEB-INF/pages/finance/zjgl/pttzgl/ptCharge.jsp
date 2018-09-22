<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page
	import="com.dimeng.p2p.console.servlets.finance.zjgl.pttzgl.PttzglList"%>
<%@page import="com.dimeng.util.ObjectHelper"%>
<%@page import="com.dimeng.p2p.common.FormToken"%>
<%@page
	import="com.dimeng.p2p.console.servlets.finance.zjgl.pttzgl.PtCharge"%>
<html>
<head>
<%@include file="/WEB-INF/include/meta.jsp"%>
<%@include file="/WEB-INF/include/style.jsp"%>
<%@include file="/WEB-INF/include/script.jsp"%>
</head>
<body>
	<%
	    CURRENT_CATEGORY = "CWGL";
	    CURRENT_SUB_CATEGORY = "PTTZGL";
	    String balance = ObjectHelper.convert(request.getAttribute("balance"), String.class);
	%>
	<div class="right-container">
		<div class="viewFramework-body">
			<div class="viewFramework-content">
				<div class="p20">
				
					<div class="border">
						<div class="title-container">
							<i class="icon-i w30 h30 va-middle title-left-icon"></i>平台充值
						</div>
					</div>
					<form action="<%=controller.getURI(request, PtCharge.class)%>"
						method="post" class="form1">
							<%=FormToken.hidden(serviceSession.getSession())%>
						<div class="border mt20">
							<div class="tab-content-container p20">
								<div class="tab-item">
									<input type="hidden" name="token" value="" />
									<ul class="gray6">
										<li class="mb10 red"><span
											class="display-ib w200 tr mr5 red">&nbsp;</span></li>
										<li class="mb10"><span class="display-ib w200 tr mr5">可用余额：</span>
											<span>
												<%
												    StringHelper.filterHTML(out, balance);
												%>
										</span>元</li>
										<li class="mb10"><span class="display-ib w200 tr mr5"><em
												class="red pr5">*</em>充值金额：</span> <input name="amount" type="text"
											class="text border w300 yw_w5 required" maxlength="11"
											value="<%StringHelper.filterHTML(out, request.getParameter("amount"));%>"
											mtest="/^\d+(|\d|(\.[0-9]{1,2}))$/" mtestmsg="金额格式不正确" /> <span>元</span>
											<span tip>(小数位最多只能有两位)</span> <span errortip class=""
											style="display: none"></span></li>
										<li class="mb10"><span class="display-ib w200 tr mr5"
											style="vertical-align: top"><em class="red pr5">*</em>备注：</span>
											<textarea name=remark class="w400 h120 border p5 required max-length-140" cols="100" rows="8" style="width: 700px; height: 200px;"><%  StringHelper.format(out, request.getParameter("remark"), fileStore); %> </textarea> <span tip>最多140个字</span> <span errortip class=""
											style="display: none"></span></li>
										<li class="mb10"><span class="display-ib w200 tr mr5">&nbsp;</span>
											<input type="submit"
											class="btn btn-blue2 radius-6 pl20 pr20 ml40 sumbitForme"
											value="提交" fromname="form1" /> <input type="button"
											onclick="window.location.href='<%=controller.getURI(request, PttzglList.class)%>'"
											class="btn btn-blue2 radius-6 pl20 pr20 ml40" value="返回" /></li>
									</ul>
								</div>
							</div>
						</div>
					</form>
				</div>
			</div>
		</div>
	</div>
	<div class="popup_bg hide"></div>
	<div id="info"></div>

	<script type="text/javascript"
		src="<%=controller.getStaticPath(request)%>/js/validation.js"></script>
	<script type="text/javascript"
		src="<%=controller.getStaticPath(request)%>/js/dialog.js"></script>

	<%
	    String warringMessage = controller.getPrompt(request, response, PromptLevel.WARRING);
		if (!StringHelper.isEmpty(warringMessage)) {
	%>
	<script type="text/javascript">
     $(".popup_bg").show();
     $("#info").html(showDialogInfo('<%=warringMessage%>', "warn"));
	</script>
	<%
	    }
	%>
	
	<%
	    String message = controller.getPrompt(request, response, PromptLevel.INFO);
		    if (!StringHelper.isEmpty(message)) {
	%>
	<script type="text/javascript">
	$(".popup_bg").show();
	$("#info").html(showDialogInfo("<%=message%>","yes"));
	</script>
	<%
	    }
	%>
</body>

<script type="text/javascript">
    $(function () {
        var tokenValue = $("input[name='<%=FormToken.parameterName()%>']").val();
        $("input[name='token']").val(tokenValue);
        var msg = '<%=request.getAttribute("EMAIL_ERROR")==null ? "" : request.getAttribute("EMAIL_ERROR")%>
	';
		if (msg) {
			alert(msg);
		}
	});
</script>
</html>