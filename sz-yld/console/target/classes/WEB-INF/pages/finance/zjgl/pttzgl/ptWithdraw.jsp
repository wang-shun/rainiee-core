<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page
	import="com.dimeng.p2p.console.servlets.finance.zjgl.pttzgl.PttzglList"%>
<%@page
	import="com.dimeng.p2p.console.servlets.finance.ptyhkgl.PtyhkglList"%>
<%@page import="com.dimeng.util.ObjectHelper"%>
<%@page import="com.dimeng.p2p.common.FormToken"%>
<%@page
	import="com.dimeng.p2p.console.servlets.finance.zjgl.pttzgl.PtWithdraw"%>
<%@page
	import="com.dimeng.p2p.modules.account.console.service.entity.BankCard"%>
<%@page import="com.dimeng.p2p.common.enums.BankCardStatus"%>
<%@ page
	import="com.dimeng.p2p.modules.account.console.service.BankCardManage"%>
<%@page
	import="com.dimeng.p2p.console.servlets.finance.ptyhkgl.AddBankCard"%>
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
		    Boolean tg = BooleanParser.parseObject(configureProvider.getProperty(SystemVariable.SFZJTG));
			int cardId = IntegerParser.parse(request.getAttribute("cardId"));
			BankCardManage bankCardManage = serviceSession.getService(BankCardManage.class);
			BankCard[] cards = bankCardManage.getBankCars(BankCardStatus.QY.name());
			
			String escrowFinance = configureProvider.getProperty(SystemVariable.ESCROW_PREFIX);
	%>
	<div class="right-container">
		<div class="viewFramework-body">
			<div class="viewFramework-content">
				<div class="p20">
					<div class="border">
						<div class="title-container">
							<i class="icon-i w30 h30 va-middle title-left-icon"></i>平台提现
						</div>
					</div>
					<form action="<%=controller.getURI(request, PtWithdraw.class)%>"
						method="post" class="form1">
						<%=FormToken.hidden(serviceSession.getSession())%>
						<div class="border mt20">
							<%
							    if(tg && !escrowFinance.equalsIgnoreCase("yeepay") && !escrowFinance.equalsIgnoreCase("FUYOU") && !escrowFinance.equalsIgnoreCase("HUIFU")){
							%>
							<%
							    if(cardId>0){
							%>
							<input name="cardId" type="hidden" value="<%=cardId%>">
							<%
							    }else{
							%>
							<input name="cardId" type="hidden"
								value="<%=(cards!=null&&cards.length>0)?cards[0].id:""%>">
							<%
							    }
							%>
							<div class="content-container pl40 pt30 pr40">
								<div class="title-container">
									选择提现银行卡
									<%
								    if(dimengSession.isAccessableResource(PtyhkglList.class)){
								%>
									<a href="<%=controller.getURI(request, PtyhkglList.class)%>"
										class="fr mr10">管理银行卡</a>
									<%
									    }else{
									%>
									<a href="javascript:void(0)" class="fr mr10 disabled">管理银行卡</a>
									<%
									    }
									%>
								</div>
								<ul class="gray6 input-list-container clearfix">
									<%
									    if(cards!=null && cards.length>0){ int i=0;
									%>
									<%
									    for(BankCard bc:cards){
									%>
									<li><a href="javascript:void(0)"
										onclick="checkCard(this,<%=bc.id%>)">
											<div class="item-bank-card">
												<div class="pic f16"
													title="<%StringHelper.filterHTML(out, bc.Bankname);%>">
													<%
													    StringHelper.truncation(out, bc.Bankname, 6, "***");
													%>
												</div>
												<div class="number"><%=bc.BankNumber%></div>
												<div class="delete clearfix">
													<%
													    if(cardId>0){
													%>
													<%
													    if(cardId==bc.id){
													%>
													<i class="icon-i h30 w30 fr bankcard-right-icon"></i>
													<%
													    }
													%>
													<%
													    }else{ 
																																		            	if(i==0){
													%>
													<i class="icon-i h30 w30 fr bankcard-right-icon"></i>
													<%
													    } 
																																		            	i++;
																																		            }
													%>
												</div>
											</div>
									</a></li>
									<%
									    }
									%>
									<%
									    }else{
									%>
									<li>
										<div class="item-bank-card item-bank-card-add">
											<%
											    if(dimengSession.isAccessableResource(AddBankCard.class)){
											%>
											<a href="javascript:void(0)" onclick="addCard(0)">
												<div class="f80 tc pt10 lh80 h80">+</div>
												<div class="tc">新增银行卡</div>
											</a>
											<%
											    }else{
											%>
											<div class="f80 tc pt10 lh80 h80">+</div>
											<div class="tc">新增银行卡</div>
											<%
											    }
											%>
										</div>
									</li>
									<%
									    }
									%>
								</ul>
							</div>
							<%
							    }
							%>
							<div class="tab-content-container pl40 pt30 pr40">
								<%
								    if(tg && !escrowFinance.equals("yeepay")){
								%>
								<div class="title-container">填写提现金额</div>
								<%
								    }
								%>
								<div class="tab-item">
									<input type="hidden" name="token" value="" />
									<ul class="gray6">
										<li class="mb10 red"><span class="display-ib w200 tr mr5">&nbsp;</span>
										</li>
										<li class="mb10"><span class="display-ib w200 tr mr5"><em
												class="red pr5">*</em>可用金额：</span> <span> <%
     StringHelper.filterHTML(out, balance);
 %>
										</span>元</li>
										<li class="mb10"><span class="display-ib w200 tr mr5"><em
												class="red pr5">*</em>提现金额：</span> <input name="amount" type="text"
											class="text border w300 yw_w5 required" maxlength="18"
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

	<%@include file="/WEB-INF/include/dialog.jsp"%>
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
        var msg = '<%=request.getAttribute("EMAIL_ERROR")==null ? "" : request.getAttribute("EMAIL_ERROR")%>';
        if (msg) {
            alert(msg);
        }
    });
    function checkCard(obj,id){
    	$("div.delete").html("");
    	$(obj).children().eq(0).children().eq(2).html('<i class="icon-i h30 w30 fr bankcard-right-icon"></i>');
    	$("input[name='cardId']").val(id);
    }
    
  	//新增银行卡
	function addCard(id) {
		global_art = art.dialog.open("<%=controller.getViewURI(request, AddBankCard.class)%>", {
			id: 'addCard',
			title: '新增银行卡',
			opacity: 0.1,
			width: 783,
			height: 500,
			padding: 0,
			lock: true,
			close: function () {
				window.location.reload();
			}
		}, false);
	}
</script>
</html>