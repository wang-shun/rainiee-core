$(function(){
	$("#ButtonJrgwc").click(function(){
		var inputVal = $("#number_id").val();
		if(!checkAuth() || isEmpty(inputVal) || isNum(inputVal)){
			return;
		}
		addToCar();
	});
	
	$("#ButtonLjgmSp").click(function(){
		var inputVal = $("#number_id").val();
		if(!checkAuth() || isEmpty(inputVal) || isNum(inputVal)){
			return;
		}
		toPay("balance");
	});
	
	$("#ButtonByScore").click(function(){
		var inputVal = $("#number_id").val();
		if(!checkAuth() || isEmpty(inputVal) || isNum(inputVal)){
			return;
		}
		toPay("score");
	});
	$("#ButtonLjgmHf").click(function(){
		var inputVal = $("#number_id").val();
		if(!checkAuth() || isEmpty(inputVal) || isNum(inputVal)){
			return;
		}
		checkBidLjgmHf("balance");
		$("#payType").val("balance");
	});
	$("#ButtonByScoreHf").click(function(){
		var inputVal = $("#number_id").val();
		if(!checkAuth() || isEmpty(inputVal) || isNum(inputVal)){
			return;
		}
		checkBidLjgmHf("score");
		$("#payType").val("score");
	});
	$(".dialog_close").click(function(){
		var $tran_pwd = $("#tran_pwd");
		var $mobile = $("#mobile");
		$tran_pwd.next("p").hide();
		$mobile.next("p").hide();
		$tran_pwd.val('');
		$mobile.val('');
		$("div.popup_bg").hide();
		$("div.dialog").hide();
	});
	
	$(".payBtn").click(function(){
		if(checkMobile() || $("#ruleId").val()*1 > 0){
			//网关需要验证交易密码
			var isOpenWithPds =  $("#isOpenWithPsd").val();
			if("true" == isOpenWithPds){
				keleyidialog();
			}else{
				buy();
			}
		}
	});
	
});

function checkMobile(){
	var mobile = $("input[name='mobile']").val();
	var myreg = /^(13|14|15|17|18)[0-9]{9}$/;
	if(!mobile){
		$("#mobile").next("p").html("充值手机号码不能为空").css("fontSize","14px").show();
		return false;
	}else{
		if(!myreg.test(mobile)){
			$("#mobile").next("p").html("充值手机号码格式不正确").css("fontSize","14px").show();
			return false;
		}else{
			$("#mobile").next("p").hide();
			return true;
		}
	}
}

function checkPsd(){
	var tran_pwd = $("#tran_pwd").val();
    if(!tran_pwd){
    	$("#tran_pwd").next("p").html("交易密码不能为空").css("fontSize","14px").show();
    }else{
    	$("#tran_pwd").next("p").hide();
    }
}
//减少商品数量
function minusCount() {
	var inputVal = $("#number_id").val();
	if(isEmpty(inputVal) || isNum(inputVal)){
		inputVal = 1;
	}
	if (inputVal > 1) {
		inputVal = parseInt(inputVal) - parseInt(1);
		calc(inputVal);
	}
}

//增加商品数量
function addCount() {
	var inputVal = $("#number_id").val();
	if(isEmpty(inputVal) || isNum(inputVal)){
		inputVal = 0;
	}
	calc(parseInt(inputVal)+1);
	
}

function calc(inputVal){
	
	$("#num_error_info").html("");
	var xgslVal = parseInt($("input[name='xgsl']").val());
	var ygmslVal = parseInt($("input[name='buyGoodsNum']").val());//已购买数量
	if (isEmpty(inputVal) || isNum(inputVal)) {
		$("#num_error_info").html("请输入购买数量");
		return;
	}
	var kcsl = $("#totalCount").text();
	if(parseInt(inputVal) > parseInt(kcsl)){
		inputVal = parseInt(kcsl);
	}

	if(0 != xgslVal && (parseInt(inputVal)+ygmslVal) > xgslVal){
		inputVal = (xgslVal-ygmslVal);
		if(inputVal<=0){
			inputVal = 1;
		}
		$("#number_id").val(inputVal);
		$("#num_error_info").html("不能超过商品的限购数："+xgslVal);
		return;
	}
	$("#number_id").val(inputVal);
	var score = $("input[name='singleScore']").val()*inputVal;
	if($("input[name='singlePrice']").val() > 0  && $("#isPrice").val()=="yes"){
		var price = formatMoney($("input[name='singlePrice']").val()*inputVal);
		//$("#castPrice").text('￥'+price);
		$("input[name='price']").val(price);
	}
	if($("#isScore").val()=="yes"){
		$("input[name='score']").val(score);
		/*$("#castScore").text(score);*/
	}
}

function formatMoney(s) {
    if (/[^0-9\.]/.test(s)){
    	return "0.00";
    }
    if (s == null || s == ""){
    	return "0.00";
    }
    s = s.toString().replace(/^(\d*)$/, "$1.");
    s = (s + "00").replace(/(\d*\.\d\d)\d*/, "$1");
    s = s.replace(".", ",");
    var re = /(\d)(\d{3},)/;  
    while (re.test(s)){
    	s = s.replace(re, "$1,$2");
    }
    s = s.replace(/,(\d\d)$/, ".$1");
    return s;  
}

function checkJrgwc(){
	
	$("div.popup_bg").show();
	$("#gwc").show();
	
}

function checkBidLjgmSp(){
	var commNum = $("#number_id").val();
	var commScore = $("input[name='score']").val();
	$("#commScore").text(commScore);
	$("#commNum").text(commNum);
	$("div.popup_bg").show();
	$("#spxqgm").show();
	
}

function checkBidLjgmHf(type){

	var totalCount = $("#totalCount").text();
	if(totalCount*1 <= 0){
		$("#info").html(showDialogInfo("商品的库存不足","doubt"));
		return;
	}
	
	//判断是否有足够的积分兑换和金额购买
	if(!canBuy(type)){
		return;
	}
	
	var freeMoney = $("input[name='price']").val();
	var scoreFree = $("input[name='score']").val();
	
	//var type = $("#payType").val();
	$("#type").val(type);
	var num = $("#number_id").val();
	$("#num").val(num);
	
	$("div.popup_bg").show();
	$("#hfgm").show();
	if(type=="score"){
		$("#needStr").text("所需积分：");
		$("#scoreFree").text(scoreFree);
		$("#pt").show();
		$("#escrow").hide();
	}else{
		$("#needStr").text("所需价格：");
		$("#scoreFree").text("￥"+freeMoney);
		$("#escrow").show();
		$("#pt").hide();
	}
	
}

function keleyidialog() {
	var tran_pwd = $("#tran_pwd").val();
    if(!tran_pwd){
    	$("#tran_pwd").next("p").html("交易密码不能为空").css("fontSize","14px").show();
    	//return;
    }else{
    	var sPwd= RSAUtils.encryptedString(key,tran_pwd);
	    $("#tranPwd").val(sPwd);
    	var data={"password":sPwd,"id":$("#accountId").val()};
    	var flag = true;
		$.ajax({
			type:"post",
			dataType:"html",
			async:false,
			url:_checkUrl,
			data:data,
			success:function(data){
				if(data != "01"){
					$("#tran_pwd").next("p").html(data).css("fontSize","14px").show();
					flag = false;
				}
			}
		});
		if(flag){
	    	$("#tran_pwd").next("p").hide();
	    	$("div.dialog").hide();
			$("div.popup_bg").hide();
		    var sPwd= RSAUtils.encryptedString(key,tran_pwd);
		    $("#tranPwd").val(sPwd);
		    //提交
		    buy();
		}
    }
}


function addToCar(){
	$.ajax({
		type:"post",
		dataType:"html",
		url:_addCarUrl,
		data:{"id":$("input[name='commodityId']").val(),"num":$("#number_id").val()},
		async: false,
		success:function(returnData){
			returnData = eval(""+returnData+"");
			if(returnData[0].num==1){
				checkJrgwc();
			}else{
				$("#info").html(showDialogInfo(returnData[0].msg,"doubt"));
			}
			
		},
		error: function(XMLHttpRequest, textStatus, errorThrown){
			alert(textStatus);
		}
	});
}

function toPay(type){
	var totalCount = $("#totalCount").text();
	if(totalCount*1 <= 0){
		$("#info").html(showDialogInfo("商品的库存不足","doubt"));
		return;
	}

	//判断是否有足够的积分兑换和金额购买
	if(!canBuy(type)){
		return;
	}

	var jsonObj = "[{goodsId:"+$("input[name='commodityId']").val()+",goodsNum:"+$("#number_id").val()+"}]";
	$("input[name='payType']").val(type);
	$("input[name='goodsList']").val(jsonObj);
	$("#mainForm").submit();
}

function onSubmit() {
	if(checkMobile()){
		var num = $("#number_id").val();
		var mobile = $("input[name='mobile']").val();
		var goodsList = "[{goodsId:"+$("input[name='commodityId']").val()+",goodsNum:"+num+",mobile:"+mobile+"}]";
		$("#goodsList").val(goodsList);
	} else {
		return false;
	}
}

function buy(){
	var type = $("#payType").val();
	var mobile = $("input[name='mobile']").val();
	var num = $("#number_id").val();
	var goodsList = "[{goodsId:"+$("input[name='commodityId']").val()+",goodsNum:"+num+",mobile:"+mobile+"}]";
	if($("#ruleId").val()*1 > 0)
	{
		goodsList = "[{goodsId:"+$("input[name='commodityId']").val()+",goodsNum:"+num+"}]";
	}
	$.ajax({
		type:"post",
		dataType:"text",
		url:_buyUrl,
		data:{"payType":type,"goodsList":goodsList,"password":$("#tranPwd").val()},
		async: false,
		success:function(returnData){
			returnData = eval(""+returnData+"");
			$("#hfgm").hide();
			$("div.popup_bg").hide();
			if(returnData[0].result ==1){
				$("#info").html(showSuccInfo(returnData[0].msg,"successful",_myOrderUrl));
			}else if(returnData[0].result ==0){
				$("#info").html(showDialogInfo(returnData[0].msg,"doubt"));
			}else{
				$("#info").html(showDialogInfo(returnData[0].msg,"error"));
			}
			
		},
		error: function(XMLHttpRequest, textStatus, errorThrown){
			alert(textStatus);
		}
	});

}

function isEmpty(inputVal){
	if (inputVal == null || inputVal == "" || inputVal==0) {
		return true;
	}
	return false;
}

function isNum(inputVal){
	var objVal = parseInt(inputVal);
	if (objVal>0) {
		return false;
	}
	return true;
}

//判断是否有足够的积分兑换和金额购买及购买数量是否正确
function canBuy(type){
	
	var num = parseInt($("#number_id").val());
	var xgslVal = parseInt($("input[name='xgsl']").val());
	var ygmslVal = parseInt($("input[name='buyGoodsNum']").val());//已购买数量
	
	if(xgslVal != 0 && xgslVal < (num+ygmslVal)){
		$("#info").html(showDialogInfo("不能超过限购数："+xgslVal+"<br/>已购买数量："+ygmslVal,"doubt"));
		return false;
	}
	
	var totalScore = $("input[name='totalScore']").val();
	var balance = $("input[name='balance']").val();
	var singleScore = $("input[name='singleScore']").val();
	var singlePrice = $("input[name='singlePrice']").val();
	
	if(type=="score" && singleScore*1*num > totalScore){
		$("div.popup_bg").show();
		$("#jfbz").show();
		return false;
	}
	if(type=="balance" && parseFloat(singlePrice*num) > parseFloat(balance)){
		$("div.popup_bg").show();
		$("#yebz").show();
		return false;
	}
	return true;
}
