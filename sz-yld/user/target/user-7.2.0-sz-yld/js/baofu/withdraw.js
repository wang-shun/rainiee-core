$(function() {
	$("input[name='amount']").focusout(function() {
		var $tip = $(this).nextAll("p[tip]");
		var amount = $(this).val() * 1;
		amount = amount.toFixed(2) * 1;
		$(this).val(amount);
		if (!checkAmount(amount)) {
			return;
		}
		var $errortip = $(this).nextAll("p[errortip]");
		amount = amount * 1;
		if (amount < min)
		{
			$tip.hide();
			$errortip.html("提现金额不能小于" + min + "元").show();
			return;
		}
		if (amount > max)
		{
			$tip.hide();
			$errortip.html("提现金额不能大于" + max + "元").show();
			return;
		}
		$errortip.html("");
		$("span.error_tip").text("");
		var p = poundage(amount);
		$("#poundage").text(p);
		if(txsxfkcfs == true){
			$("#paySum").text(amount.toFixed(2) * 1);
		}else{
			$("#paySum").text((amount * 1 + p * 1).toFixed(2));
		}
		$tip.html(chinaCost(amount)).show();
		//overFlow(amount + p);
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
		$("#paySum").text((aa + pp).toFixed(2));
	}
});
function poundage(amount){
	if(punWay == 'BL') {
		punProportion *= 1;
		return acculatePoundage((amount * punProportion), 2);
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
	$("div.delete").html("");
	$(obj).children().eq(2).html('<i class="ico"></i>');
	$("input[name='cardId']").val(id);
}
function checkAmount(amount) {
	if (isNaN(amount)) {
		$("input[name='amount']").val("");
		$("input[name='amount']").nextAll("p[tip]").hide();
		$("input[name='amount']").nextAll("p[errortip]").html("对不起，您输入的金额不为数字").show();
		return false;
	}
	return true;
}
function checkRange(amount) {
	if (amount >= min && amount <= max) {
		return true;
	}
	$("input[name='amount']").nextAll("p[tip]").hide();
	$("input[name='amount']").nextAll("p[errortip]").html("提现金额不能低于<span class='red'>" + min
			+ "</span>元，且不能高于<span class='red'>" + max / 10000
			+ "</span>万元").show();
	return false;
}
function onSubmit1() {
	$("input[name='amount']").nextAll("p[tip]").html("").hide();
	var a = $("input[name='amount']").val();
	if(a == '' || a == null || a.length == 0){
		//$("#con_error").html("请输入提现金额");
		//$("#dialog").show();
		$("input[name='amount']").nextAll("p[tip]").hide();
		$("input[name='amount']").nextAll("p[errortip]").html("请输入提现金额").show();
		return false;
	}
	var a = $("input[name='amount']").val();
	if(!validAmount() || !getCard() || !checkAmount(a)){
		return false;
	}
	var amount = $("input[name='amount']").val() * 1;
	var p = poundage(amount);
	if(ye<amount){
		$("input[name='amount']").nextAll("p[tip]").hide();
		$("input[name='amount']").nextAll("p[errortip]").html("提现金额不能大于可用余额").show();
		return false;
	}
	
	if(!checkRange(amount) || !withdrawPsd()){
		return false;
	}
	return true;
}
function withdrawPsd() {
	if(_isOpenPsd == 'true'){
		var psd = $("input[name='withdrawPsd']").val();
		if (psd == null || "" == psd || psd.length == 0) {
			//$("#con_error").html("交易密码不能为空");
			//$("#dialog").show();
			$("input[name='withdrawPsd']").nextAll("p[errortip]").html("交易密码不能为空");
			return false;
		}
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
function validAmount(){
	var amount = $("input[name='amount']").val() * 1;
	amount = amount.toFixed(2) * 1;
	$("input[name='amount']").val(amount);
	if (!checkAmount(amount)) {
		return false;
	}
	amount = amount * 1;
	if (amount < min)
	{
		$("input[name='amount']").nextAll("p[tip]").hide();
		$("input[name='amount']").nextAll("p[errortip]").html("提现金额不能小于" + min + "元").show();
		return false;
	}
	if (amount > max)
	{
		$("input[name='amount']").nextAll("p[tip]").hide();
		$("input[name='amount']").nextAll("p[errortip]").html("提现金额不能小于" + max + "元").show();
		return false;
	}
	$("input[name='amount']").nextAll("p[errortip]").html("");
	$("span.error_tip").text("");
	var p = poundage(amount);
	$("#poundage").text(p);
	if(txsxfkcfs == true){
		$("#paySum").text(amount.toFixed(2) * 1);
	}else{
		$("#paySum").text((amount * 1 + p * 1).toFixed(2));
	}
	return true;
}