$(function() {
	$("input[name='amount']").focusout(function() {
		if($(this).val()== ""){
			return;
		}
		var amount = $(this).val() * 1;
		amount = amount.toFixed(2) * 1;
		$(this).val(amount);
		if (!checkAmount(amount)) {
			return;
		}
		amount = amount * 1;
		$("span.error_tip").text("");
		var p = poundage(amount);
		$("#poundage").text(p);
		$("#paySum").text(amount * 1);
	});
	$("div.dialog_close").click(function() {
		$("#dialog").hide();
	});
	$("a.tx_an1").mouseover(function(){
		$(this).parent().parent().find("div.pop-con").show();
	});
	$("a.tx_an1").mouseout(function(){
		$(this).parent().parent().find("div.pop-con").hide();
	});
	var a = $("input[name='amount']").val();
	if(a != null && "" != a && a.length != 0){
		var aa = a*1;
		var pp = poundage(aa);
		$("#poundage").text(pp);
		$("#paySum").text(aa);
	}
});
function poundage(amount){
	if(punWay == 'BL') {
		withdrawMinFee *= 1;
		feeRate *= 1;
		var fee = amount * feeRate;
		if(feeRate <=0){
			return 0;
		}
		if(withdrawMinFee > 0 && fee <= withdrawMinFee){
			return withdrawMinFee;
		}else{
			return acculatePoundage(fee, 2);
		}
	} else {
		if (1 <= amount && amount < 50000) {
			return p1;
		} else if(amount >= 50000 ){
			return p2;
		} else {
			return 0;
		}
	}
}
function checkCard(obj, id) {
	$("li.cards a").removeClass();
	$(obj).addClass("curr");
	$("div.delete").html("");
	$(obj).children().eq(2).html('<i class="ico"></i>');
	$("input[name='cardId']").val(id);
}
function checkAmount(amount) {
	if (isNaN(amount)) {
		$("input[name='amount']").val("");
		$("#con_error").html("对不起，您输入的金额不为数字");
		$("span[tip]").hide();
		$("#dialog").show();
		return false;
	}
	if(amount<=2){
		$("#poundage").text("0.00");
		$("#paySum").text("0.00");
		$("#con_error").html("提现金额需要大于2元");
		$("span[tip]").hide();
		$("#dialog").show();
		return false;
	}
	if(amount<=p1){
		$("#poundage").text("0.00");
		$("#paySum").text("0.00");
		$("#con_error").html("提现金额需要大于提现手续费金额");
		$("span[tip]").hide();
		$("#dialog").show();
		return false;
	}
	var aFunds = $("#aFunds").html().replace(",","") * 1;
	if(amount > aFunds){
		$("#poundage").text("0.00");
		$("#paySum").text("0.00");
		$("#con_error").html("账户可用余额不足");
		$("span[tip]").hide();
		$("#dialog").show();
		return false;
	}
	return true;
}
function checkRange(amount) {
	if (amount >= min && amount <= max) {
		return true;
	}
	$("#con_error").html(
			"提现金额不能低于<span class='red'>" + min
					+ "</span>元，且不能高于<span class='red'>" + max / 10000
					+ "</span>万元");
	$("span[tip]").hide();
	$("#dialog").show();
	return false;
}
function onSubmit() {
	$("input[name='amount']").nextAll("p[tip]").html("").hide();
	var a = $("input[name='amount']").val();
	if(a == '' || a == null || a.length == 0){
		$("#con_error").html("请输入提现金额");
		$("span[tip]").hide();
		$("#dialog").show();
		return false;
	}
	var a = $("input[name='amount']").val();
	if(!getCard() || !checkAmount(a)){
		return false;
	}
	var amount = $("input[name='amount']").val() * 1;
	if(ye<amount){
		$("#con_error").html("提现金额不能大于可用余额");
		$("span[tip]").hide();
		$("#dialog").show();
		return false;
	}
	if(!checkRange(amount)){
		return false;
	}
	return true;
}
function getCard(){
	if(len <= 0){
		$("#con_error").html("您还没有银行卡，请先添加银行卡");
		$("#dialog").show();
		return false;
	}
	return true;
}


function acculatePoundage(amount, len) {
	var add = 0;
	var s1 = amount + "";
	var start = s1.indexOf(".");
	if(start == -1) {
		return amount;
	}
	if(s1.substr(start + len + 1, 1) >= 5) {
		add = 1;
	}
	var t = Math.pow(10, len);
	var s = Math.floor(amount * t) + add;
	return s/t;
}