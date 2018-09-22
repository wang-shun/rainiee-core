$(function() {
	$("input[name='amount']").focusout(function() {
		var $tip = $("#chargeTipInfo");
		$tip.html("").hide();
		if($(this).val() == '' || isNaN($(this).val())){
			$(this).val('');
			$("#poundage").text("0.00");
			$("#paySum").text("0.00");
			return;
		}
		var amount = $(this).val() * 1;
		var feeType =$("#feeType").val();
		var kjFeeType =$("#kjFeeType").val();
		var companyBank =$("#companyBank").val();
		var rechargeType=$("#rechargeTypes").find("input:checked").val();
		var sqWithdrawRate =$("#sqWithdrawRate").val() * 1;
		amount = amount.toFixed(2) * 1;
		$(this).val(amount);
		
		 /*if(!checkAmount(amount)){ return ; }*/
		 
		amount = amount * 1;
		//var poundage = amount * p;
		var poundage = 0;
		/*if(!checkRange(amount)){ return ; }*/

		if(rechargeType==3 || rechargeType==4){
			
			if(amount - companyBank < 1){
				if(rechargeType==3)
					$("#con_error").html("汇款充值金额必须高于<span class='red'>" + (companyBank-0).toFixed(2) + "</span>元");
				else
					$("#con_error").html("企业网银充值金额必须高于<span class='red'>" + (companyBank-0).toFixed(2) + "</span>元");
				$("#dialog").show();
			}
			if(feeType==1){
				if(amount > 0){
					poundage = (poundage-0) + (companyBank-0);
					$("#poundage").text(poundage.toFixed(2));
					$("#paySum").text((amount - poundage).toFixed(2));
				}else{
					$("#poundage").text("0.00");
					$("#paySum").text("0.00");
				}
			}else{
				$("#poundage").text("0.00");
				$("#paySum").text((amount).toFixed(2));
			}
		}else if(rechargeType==2){
			if(amount > 0 && amount <= 2){////v2.8.4版本 快捷充值最低 2块钱
				$("#con_error").html("快捷支付充值金额必须高于<span class='red'>2.00</span>元");
				$("#dialog").show();
				$("#poundage").text("0.00");
				$("#paySum").text("0.00");
				return;
			}
			if(kjFeeType==1){//用户支付手续费：手续费率 为提现手续费率 + kuaiJieUpRate
				sqWithdrawRate = sqWithdrawRate + kuaiJieUpRate;//v2.8.4版本取消0.1%上浮，2016.11.25恢复上浮百分比
				poundage = sqWithdrawRate * amount;
				if((amount-0) > 0 && (poundage-0)<2){
					$("#poundage").text("2.00");
					$("#paySum").text((amount - 2).toFixed(2));
				}else{
					$("#poundage").text(poundage.toFixed(2));
					$("#paySum").text((amount - poundage).toFixed(2));
				}
			}else if(kjFeeType==3){//用户支付手续费：从充值人账户扣除与提现手续费的差值 //v2.8.4版本feetype=3的情况充值手续费免费2016.11.25恢复上浮百分比
				poundage = (poundage-0) + kuaiJieUpRate * amount;
				if((amount-0) > 0 && (poundage-0)<1){
					$("#poundage").text("1.00");
					$("#paySum").text((amount - 1).toFixed(2));
				}else{
					$("#poundage").text(poundage.toFixed(2));
					$("#paySum").text((amount - poundage).toFixed(2));
				}
			}else{
				$("#poundage").text("0.00");
				$("#paySum").text((amount).toFixed(2));
			}
		}else{
			if(amount > 0 && amount <= 2){////v2.8.4版本 充值最低 2块钱
				$("#con_error").html("充值金额必须高于<span class='red'>2.00</span>元");
				$("#dialog").show();
				$("#poundage").text("0.00");
				$("#paySum").text("0.00");
				return;
			}
			$("#poundage").text("0.00");
			$("#paySum").text((amount).toFixed(2));
		}
		$tip.html(chinaCost(amount)).show();
	});
	$("#dialog div.dialog_close").click(function() {
		$("#dialog").hide();
	});
	function toDecimal(x) {
		var f = parseFloat(x);
		if (isNaN(f)) {
			return;
		}
		f = Math.round(x * 100) / 100;
		return f;
	}
	$("#otherPay").click(function() {
		$("#problem").hide();
	});
	$("#problem div.dialog_close").click(function() {
		$("#problem").hide();
	});
	$("#problem a.btn06").click(function() {
		$("#problem").hide();
	});
	$("a.tx_an1").mouseover(function() {
		$(this).parent().parent().find("div.pop-con").show();
	});
	$("a.tx_an1").mouseout(function() {
		$(this).parent().parent().find("div.pop-con").hide();
	});
	
	$("#reloadChargeUser").click(function() {
		location.reload();
		$("input[name='amount']").val("");
	});
});
function checkAmount(amount) {
	if (isNaN(amount)) {
		$("input[name='amount']").val("");
		$("#con_error").html("对不起，您输入的金额不为数字");
		$("#dialog").show();
		return false;
	}else if(amount == 0){
		$("#con_error").html("对不起，您充值的金额不能为空或0");
		$("#dialog").show();
		return false;
	}
	return true;
}
function checkRange(amount) {
	
	var companyBank =$("#companyBank").val();
	var rechargeType=$("#rechargeTypes").find("input:checked").val();
	if(rechargeType=="3" || rechargeType=="4"){
		if(amount - companyBank < 1){
			if(rechargeType=="3")
				$("#con_error").html("汇款充值金额必须高于<span class='red'>" + (companyBank-0).toFixed(2) + "</span>元");
			else
				$("#con_error").html("企业网银充值金额必须高于<span class='red'>" + (companyBank-0).toFixed(2) + "</span>元");
			$("#dialog").show();
			return false;
		}
	}else if(amount > 0 && amount <= 2){////v2.8.4版本 快捷充值最低 2块钱
		$("#con_error").html("充值金额必须高于<span class='red'>2.00</span>元");
		$("#dialog").show();
		$("#poundage").text("0.00");
		$("#paySum").text("0.00");
		return false;
	}
	
	if (amount >= min && amount <= max) {
		return true;
	}
	$("#con_error").html(
			"充值金额不能低于<span class='red'>" + min
					+ "</span>元，且不能高于<span class='red'>" + max / 10000
					+ "</span>万元");
	$("#dialog").show();
	
	return false;
}
function rechargeTypeChecked(obj){
	
	var feeType =$("#feeType").val();
	var kjFeeType =$("#kjFeeType").val();
	var companyBank =$("#companyBank").val();
	var amount = $("input[name='amount']").val() * 1;
	//var poundage = amount * p;
	var poundage = 0;
	var sqWithdrawRate =$("#sqWithdrawRate").val() * 1;
	$(".pop-con .pop-info").html("");
	if(obj.value=="3" || obj.value=="4"){
		$(".pop-con .pop-info").html("充值金额不能低于<span class='red'>"+companyBank
		+ "</span>元，且不能高于<span class='red'>" + max / 10000
		+ "</span>万元");
		poundage = (poundage-0) + (companyBank-0);
		if(amount > 0 && amount - poundage < 1){
			if(obj.value=="3")
				$("#con_error").html("汇款充值金额必须高于<span class='red'>" + (companyBank-0).toFixed(2) + "</span>元");
			else
				$("#con_error").html("企业网银充值金额必须高于<span class='red'>" + (companyBank-0).toFixed(2) + "</span>元");
			$("#dialog").show();
			return false;
		}
		if(feeType=="1"){
			if(amount > 0){
				$("#poundage").text(poundage.toFixed(2));
				$("#paySum").text((amount - poundage).toFixed(2));
			}else{
				$("#poundage").text("0.00");
				$("#paySum").text("0.00");
			}
		}else{
			$("#poundage").text("0.00");
			$("#paySum").text((amount).toFixed(2));
		}
	}else if(obj.value=="2"){
		if(amount > 0 && amount <= 2){//v2.8.4版本 快捷充值最低 2块钱
			$("#con_error").html("快捷支付充值金额必须高于<span class='red'>2.00</span>元");
			$("#dialog").show();
			$("#poundage").text("0.00");
			$("#paySum").text("0.00");
			return false;
		}
		$(".pop-con .pop-info").html("充值金额不能低于<span class='red'>" + min
				+ "</span>元，且不能高于<span class='red'>" + max / 10000
				+ "</span>万元");
		if(kjFeeType==1){//用户支付手续费：手续费率 为提现手续费率 + kuaiJieUpRate
			sqWithdrawRate = sqWithdrawRate + kuaiJieUpRate; //v2.8.4版本 取消上浮0.1%,2016.11.25恢复上浮百分比
			poundage = sqWithdrawRate * amount;
			if((amount-0) > 0 && (poundage-0)<2){//v2.8.4版本，最低手续费 2元
				$("#poundage").text("2.00");
				$("#paySum").text((amount - 2).toFixed(2));
			}else{
				$("#poundage").text(poundage.toFixed(2));
				$("#paySum").text((amount - poundage).toFixed(2));
			}
		}else if(kjFeeType==3){//用户支付手续费：从充值人账户扣除与提现手续费的差值 //v2.8.4版本 ==3的情况免手续费,2016.11.25恢复上浮百分比
			poundage = (poundage-0) + kuaiJieUpRate * amount;
			if((amount-0) > 0 && (poundage-0)<1){
				$("#poundage").text("1.00");
				$("#paySum").text((amount - 1).toFixed(2));
			}else{
				$("#poundage").text(poundage.toFixed(2));
				$("#paySum").text((amount - poundage).toFixed(2));
			}
		}else{
			$("#poundage").text("0.00");
			$("#paySum").text((amount).toFixed(2));
		}
	}else{
		if(amount > 0 && amount <= 2){////v2.8.4版本 充值最低 2块钱
			$("#con_error").html("充值金额必须高于<span class='red'>2.00</span>元");
			$("#dialog").show();
			$("#poundage").text("0.00");
			$("#paySum").text("0.00");
			return;
		}
		$(".pop-con .pop-info").html("充值金额不能低于<span class='red'>" + min
				+ "</span>元，且不能高于<span class='red'>" + max / 10000
				+ "</span>万元");
		$("#poundage").text("0.00");
		$("#paySum").text((amount).toFixed(2));
	}
}
function onSubmit() {
	var amount = $("input[name='amount']").val() * 1;
	if(isHmd)
	{
		$("#dialogHmd").show();
		return false;
	}
	if (checkAmount(amount) && checkRange(amount) && checkAuthorize()) {//add by yinke20141122
		$("#problem").show();
		return true;
	}
	return false;
}
function checkAuthorize() {
	var isShouQuan=$("#isShouQuan").val();
	if(isShouQuan == "N"){
		var url=$("#Authorize").val();
		$("#con_errorSq").html(showForwardInfo('您尚未授权还款转账与二次分配转账，请授权，点击"确定"，跳到授权页面，点击"取消"返回当前页面',"perfect",url));	
		$("#dialogSq").show();
		return false;
	}
	return true;
}

function showForwardInfo(msg,type,url){
	return '<div class="dialog" >'+
	  '<div class="title"><a href="javascript:void(0);" class="out"  onclick="closeInfo()"></a>提示</div>'+
	   '<div class="content">'+
	    	'<div class="tip_information"> '+
	          '<div class="doubt"></div>'+
	          '<div class="tips">'+
	              '<span class="f20 gray3" id="con_error">'+msg+'</span>'+
	          '</div> '+
	        '</div>'+
			'<div class="tc mt20"><a href="'+url+'" class="btn01 sumbitForme">确定</a><a onclick="closeInfo()" class="btn01 btn_gray ml20">取 消</a></div>'+
	    '</div>'+
	  '</div>';
}

//封装信息消息文本.
function showDialogInfo(msg,type){
	return '<div class="dialog w510 thickpos" style="margin:-80px 0 0 -255px;">'+
		  '<div class="dialog_close fr" onclick="closeInfo()"><a></a></div>'+
		  '<div class="con clearfix">'+
		    '<div class="d_'+type+' fl"></div>'+
		    '<div class="info fr">'+
		      '<p class="f20 gray33">'+msg+'</p>'+
		    '</div>'+
		    '<div class="clear"></div>'+
		    '<div class="dialog_btn"><a onclick="closeInfo()"  class="btn btn001">确定</a></div>'+
		  '</div>'+
		'</div>';
}

function showSuccInfo(msg,type,url){
	return '<div class="dialog w510"  style="margin:-150px 0 0 -255px;">'+
	'<div class="dialog_close fr" onclick="closeInfo()"><a></a></div>'+
    '<div class="con clearfix">'+
     ' <div class="d_'+type+' fl"></div>'+
      '<div class="info"><p class="f20 gray33">'+msg+'</p></div>'+
     ' <div class="dialog_btn"><a href="'+url+'" class="btn btn01">确 定</a></div> '+
   ' </div>'+
'</div>';
}

function closeInfo(){
	$("#con_errorSq").html("");
	$("#dialogSq").hide();
}

function setDMKeyup(obj){
	$("#chargeTipInfo").html(chinaCost($(obj).val())).show();
}