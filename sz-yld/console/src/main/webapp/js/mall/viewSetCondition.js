$(function(){
	
	$("#addScoreRange").click(function(){
		if($("#scoreRangeUl li").length>5){
			$("#info").html(showDialogInfo("最多能增加6个区域输入范围","wrong"));
			return;
		}
		var liHtml = '<li class="mb10">';
		liHtml += '<span class="display-ib w200 tr mr5">\n&nbsp;</span>';
		liHtml += '\n<input name="minScore" type="text" maxlength="11" class="text border w150 yw_w5 required" onKeyUp="value=value.replace(/\\D/g,\'\')" mtest="/^((([1-9][0-9]*)|0)|((([1-9][0-9]*)|0)))$/" mtestmsg="必须为整数"/>\n';
		liHtml += '<span class="ml5 mr5">\n至\n</span>';
		liHtml += '<input name="maxScore" type="text" maxlength="11" class="text border w150 yw_w5 required" onKeyUp="value=value.replace(/\\D/g,\'\')" mtest="/^((([1-9][0-9]*)|0)|((([1-9][0-9]*)|0)))$/" mtestmsg="必须为整数"/><a href="javascript:void(0);" onclick="delLi(this,0);" class="ml10 display-ib va-middle w30 h30 icon-i remove-radiusbtn-icon"></a>';
		liHtml += '<span tip></span>';
		liHtml += '<span errortip class="" style="display: none"></span>';
		liHtml += '</li>';
		$("#scoreRangeUl").append(liHtml);
	});
	
	$("#addAmountRange").click(function(){
		if($("#amountRangeUl li").length>5){
			$("#info").html(showDialogInfo("最多能增加6个区域输入范围","wrong"));
			return;
		}
		var liHtml = '<li class="mb10">';
		liHtml += '<span class="display-ib w200 tr mr5">&nbsp;</span>';
		liHtml += '\n<input name="minAmount" type="text" maxlength="11" class="text border w150 yw_w5 required" onKeyUp="value=value.replace(/\\D/g,\'\')" mtest="/^((([1-9][0-9]*)|0)|((([1-9][0-9]*)|0)))$/" mtestmsg="必须为整数"/>';
		liHtml += '<span class="ml5 mr5">\n至\n</span>';
		liHtml += '<input name="maxAmount" type="text" maxlength="11" class="text border w150 yw_w5 required" onKeyUp="value=value.replace(/\\D/g,\'\')" mtest="/^((([1-9][0-9]*)|0)|((([1-9][0-9]*)|0)))$/" mtestmsg="必须为整数"/>';
		liHtml += '<span class="ml5">\n元</span><a href="javascript:void(0);" onclick="delLi(this,0);" class="ml10 display-ib va-middle w30 h30 icon-i remove-radiusbtn-icon"></a>';
		liHtml += '<span tip></span>';
		liHtml += '<span errortip class="" style="display: none"></span>';
		liHtml += '</li>';
		$("#amountRangeUl").append(liHtml);
	});
	
});

var scoreErrorMsg = "积分范围输入错误，后一个输入框的值必须大于前一个输入框的值！";
var priceErrorMsg = "价格范围输入错误，后一个输入框的值必须大于前一个输入框的值！";
function checkCondition(){
	return checkScoreRange() && checkAmountRange();
}

function checkScoreRange(){
	var firstScore = parseInt($("#firstScoreRange li input[name='maxScore']").val());
	var lastScore = parseInt($("#lastScoreRange li input[name='minScore']").val());
	var scoreRangeUls = $("#scoreRangeUl li input");
	var scoreLen = scoreRangeUls.length;
	var flag = true;
	$("#scoreRangeUl li input").each(function(index){
		var obj = $(this);
		var val = parseInt(obj.val());
		if(index == 0){
			if(firstScore >= val){
				$("#info").html(showDialogInfo(scoreErrorMsg,"wrong"));
				$("div.popup_bg").show();
				flag = false;
				return;
			}
		}
		var prev = index + 1;
		var next = index + 2;
		var prevVal = parseInt(scoreRangeUls.eq(prev).val());
		var nextVal = parseInt(scoreRangeUls.eq(next).val());
		if(prevVal <= val){
			$("#info").html(showDialogInfo(scoreErrorMsg,"wrong"));
			$("div.popup_bg").show();
			flag = false;
			return;
		}
		if(prevVal >= nextVal){
			$("#info").html(showDialogInfo(scoreErrorMsg,"wrong"));
			$("div.popup_bg").show();
			flag = false;
			return;
		}
		if(scoreLen = index + 1){
			if(val >= lastScore){
				$("#info").html(showDialogInfo(scoreErrorMsg,"wrong"));
				$("div.popup_bg").show();
				flag = false;
				return;
			}
		}
	});
	if(firstScore >= lastScore){
		$("#info").html(showDialogInfo(scoreErrorMsg,"wrong"));
		$("div.popup_bg").show();
		flag = false;
	}
	return flag;
}

function checkAmountRange(){
	var firstAmount = parseInt($("#firstAmountRange li input[name='maxAmount']").val());
	var lastAmount = parseInt($("#lastAmountRange li input[name='minAmount']").val());
	var amountRangeUls = $("#amountRangeUl li input");
	var amountLen = amountRangeUls.length;
	var flag = true;
	$("#amountRangeUl li input").each(function(index){
		var obj = $(this);
		var val = parseInt(obj.val());
		if(index == 0){
			if(firstAmount >= val){
				$("#info").html(showDialogInfo(priceErrorMsg,"wrong"));
				$("div.popup_bg").show();
				flag = false;
				return;
			}
		}
		var prev = index + 1;
		var next = index + 2;
		var prevVal = parseInt(amountRangeUls.eq(prev).val());
		var nextVal = parseInt(amountRangeUls.eq(next).val());
		if(prevVal <= val){
			$("#info").html(showDialogInfo(priceErrorMsg,"wrong"));
			$("div.popup_bg").show();
			flag = false;
			return;
		}
		if(prevVal >= nextVal){
			$("#info").html(showDialogInfo(priceErrorMsg,"wrong"));
			$("div.popup_bg").show();
			flag = false;
			return;
		}
		if(amountLen = index + 1){
			if(val >= lastAmount){
				$("#info").html(showDialogInfo(priceErrorMsg,"wrong"));
				$("div.popup_bg").show();
				flag = false;
				return;
			}
		}
	});
	if(firstAmount >= lastAmount){
		$("#info").html(showDialogInfo(priceErrorMsg,"wrong"));
		$("div.popup_bg").show();
		flag = false;
	}
	return flag;
}

function delLi(obj, id){
	$(obj).parent().remove();
	if(id > 0){
		var data = {"t6353Id":id};
		$.ajax({
			type:"post",
			dataType:"html",
			url:delT6353URL,
			data:data,
			success: function (returnData){
				if (returnData) {
					var ct = eval('(' + returnData + ')');
					if(ct.length > 0 && ct[0].num != 1){
						return false;
					}
				}
			}
		});
	}
}