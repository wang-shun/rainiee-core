<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.console.servlets.finance.ptyhkgl.PtyhkglList"%>
<%@page import="com.dimeng.p2p.console.servlets.finance.zjgl.pttzgl.PttzglList"%>
<%@page import="com.dimeng.p2p.modules.account.console.service.entity.BankCard" %>
<%@ page import="com.dimeng.p2p.console.servlets.finance.ptyhkgl.EditBankCard" %>
<%@ page import="com.dimeng.p2p.console.servlets.finance.ptyhkgl.AddBankCard" %>
<%@ page import="com.dimeng.p2p.console.servlets.finance.ptyhkgl.Deletebankcard" %>
<%@ page import="com.dimeng.p2p.modules.account.console.service.BankCardManage" %>
<%@ page import="com.dimeng.p2p.common.enums.BankCardStatus" %>
<html>
<head>
<%@include file="/WEB-INF/include/meta.jsp" %>
<%@include file="/WEB-INF/include/style.jsp" %>
<%@include file="/WEB-INF/include/script.jsp"%>
</head>
	<%
	    CURRENT_CATEGORY = "CWGL";
	    CURRENT_SUB_CATEGORY = "PTYHKGL";
	    Boolean tg = BooleanParser.parseObject(configureProvider.getProperty(SystemVariable.SFZJTG));
	    BankCardManage bankCardManage = serviceSession.getService(BankCardManage.class);
		BankCard[] cards = bankCardManage.getBankCars(BankCardStatus.QY.name());
	%>
<body>
<%if(!tg){ %>
    	<script type="text/javascript">
    		history.go(-1);
    	</script>
    <%} %>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <form action="<%=controller.getURI(request, PttzglList.class)%>" method="post" name="form1" id="form1">
					<div class="p20">
					  <div class="border">
					    <div class="title-container"><i class="icon-i w30 h30 va-middle title-left-icon"></i>平台银行卡管理</div>
					    <div class="content-container pl40 pt30 pr40">
					      <ul class="gray6 input-list-container clearfix">
					      	<%for(BankCard bc : cards){ %>
					        <li>
					          <div class="item-bank-card">
								  <div class="pic f16" title="<%StringHelper.filterHTML(out, bc.Bankname);%>">
									  <%StringHelper.truncation(out, bc.Bankname, 6, "***");%>
								  </div>
					            <div class="number"><%=bc.BankNumber %></div>
					            <div class="delete clearfix">
					            	<%if(dimengSession.isAccessableResource(EditBankCard.class)){ %>
					            	<a href="javascript:void(-1);" class="fl" onclick="updateCard('<%=bc.id%>');">修改</a>
					            	<%}else{ %>
					            	<a href="javascript:void(0);" class="fl disabled">修改</a>
					            	<%} %>
					            	<%if(dimengSession.isAccessableResource(Deletebankcard.class)){ %>
					            	<a href="javascript:void(-1);" onclick="deletecard(<%=bc.id %>);" class="fr">删除</a>
					            	<%}else{ %>
					            	<a href="javascript:void(-1);" class="fr disabled">删除</a>
					            	<%} %>
					            </div>
					          </div>
					        </li>
					        <%} %>
					        <li>
					          <div class="item-bank-card item-bank-card-add">
					          	<%if(dimengSession.isAccessableResource(AddBankCard.class)){ %>
					            <a href="javascript:void(-1);" class="" onclick="addCard(0);">
					            	<div class="f80 tc pt10 lh80 h80">+</div>
					            	<div class="tc">新增银行卡</div>
					            </a>
					            <%}else{ %>
					            <a href="javascript:void(0)" class="disabled">
					            	<div class="f80 tc pt10 lh80 h80">+</div>
					            	<div class="tc">新增银行卡</div>
					            </a>
					            <%} %>
					          </div>
					        </li>
					      </ul>
					    </div>
					  </div>
					</div>
                </form>
            </div>
        </div>
    </div>
<div class="popup_bg hide"></div>
<div class="popup-box hide" id="delc">
	<input type="hidden" name="delId" id="delId"/>
	<div class="popup-title-container">
		<h3 class="pl20 f18">提示</h3>
		<a class="icon-i popup-close2" onclick="closeInfo()"></a></div>
	<div class="popup-content-container pb20 clearfix">
		<div class="tc mb30 mt40"><span class="icon-i w30 h30 va-middle radius-question-icon"></span><span
				class="f20 h30 va-middle ml10">是否确认删除银行卡？</span></div>
		<div class="tc f16">
			<input name="button" type="button" class="btn-blue2 btn white radius-6 pl20 pr20 sumbitForme"
				   id="button4" fromname="form1" value="确认" onclick="okDelete();"/>
			<!-- <a class="btn btn-blue2 radius-6 pl20 pr20 ml40" onclick="closeInfo()">取消</a> -->
			<input type="button" value="取消" class="btn-blue2 btn white radius-6 pl20 pr20 ml40" onclick="closeInfo();"/>
		</div>
	</div>
</div>
<%@include file="/WEB-INF/include/datepicker.jsp" %>
<%@include file="/WEB-INF/include/dialog.jsp" %>
<script type="text/javascript">
	//弹出删除提示
	function deletecard(id) {
		$("#delId").val(id);
		$("#delc").show();
		$(".popup_bg").show();
	}

	//删除银行卡
	function okDelete() {
		var id = $("#delId").val();
		var data = {"value": id};
		$.ajax({
			type: "post",
			dataType: "html",
			url: "<%=controller.getURI(request, Deletebankcard.class)%>",
			data: data,
			success: function (data) {
				location.href = "<%=controller.getViewURI(request, PtyhkglList.class)%>";
			}
		});
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

	//修改银行卡
	function updateCard(id) {
		global_art = art.dialog.open("<%=controller.getViewURI(request, EditBankCard.class)%>?id=" + id, {
			id: 'addCard',
			title: '修改银行卡',
			opacity: 0.1,
			width: 700,
			height: 520,
			lock: true,
			close: function () {
				window.location.reload();
			}
		}, false);
	}
</script>
</body>
</html>
