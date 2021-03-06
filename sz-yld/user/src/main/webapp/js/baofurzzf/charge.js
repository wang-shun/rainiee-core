$(function() {
	$("#amount").blur(function() {
		
		var $tip = $(this).nextAll("p[tip]");
		$tip.html("").hide();
		if($(this).val() == ''){
			$("#poundage").text("0.00");
			$("#paySum").text("0.00");
			return;
		}
		var amount = $(this).val() * 1;
		amount = amount.toFixed(2) * 1;
		$(this).val(amount);
		/*
		 * if(!checkAmount(amount)){ return ; }
		 */
		var $errortip = $(this).nextAll("p[errortip]");
		amount = amount * 1;
		if (amount < min)
		{
			//$("#con_error").html("对不起，您充值的金额不能小于" + min + "元");
			//$("#dialog").show();
			$errortip.html("充值金额不能小于" + fmoney(min,2) + "元");
			return;
		}
		if (amount > max)
		{
			//$("#con_error").html("对不起，您充值的金额不能大于" + max + "元");
			//$("#dialog").show();
			$errortip.html("充值金额不能大于" + fmoney(max,2) + "元");
			return;
		}
		$errortip.html("");
		/*
		 * if(!checkRange(amount)){ return ; }
		 */
		var poundage = amount * p;
		if (poundage > pMax) {
			$("#poundage").text(pMax.toFixed(2));
			$("#paySum").text((amount - toDecimal(pMax)).toFixed(2));
		} else {
			$("#poundage").text(toDecimal(poundage).toFixed(2));
			$("#paySum").text((amount - toDecimal(poundage)).toFixed(2));
		}
		$tip.html(chinaCost(amount)).show();
	});
	$("#amountLine").keyup(function(){
		var amount = $(this).val() * 1;
		amount = amount.toFixed(2) * 1;
		$(this).val(amount);
		setDMKeyup(this);
	});
	$("#amountLine").blur(function() {
		$(this).nextAll("p[tip]").html(chinaCost($(this).val())).show();
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
		$("#amount").val("");
	});
});
function checkAmount(amount,obj) {
	if (isNaN(amount)) {
		$("#con_error").html("输入金额不为数字");
		$("#doalogClick").attr("href","javascript:void(0);");
		$("#dialog").show();
		return false;
	}else if(amount == 0){
		$("#con_error").html("充值金额不能为0");
		$("#doalogClick").attr("href","javascript:void(0);");
		$("#dialog").show();
		return false;
	}
	if (amount < min){
		$("#con_error").html("充值金额不能小于" + fmoney(min,2) + "元");
		$("#doalogClick").attr("href","javascript:void(0);");
		$("#dialog").show();
		return false;
	}
	if (amount > max){
		$("#con_error").html("充值金额不能大于" + fmoney(max,2) + "元");
		$("#doalogClick").attr("href","javascript:void(0);");
		$("#dialog").show();
		return false;
	}
	return true;
}
function checkRange(amount) {
	if (amount >= min && amount <= max) {
		return true;
	}
	$("#chargeTipInfo").hide();
	$("#chargeErrorInfo").html("充值金额不能低于" + min
			+ "元，且不能高" + max / 10000
			+ "万元").show();
	return false;
}
function checkAuth() {
	if (auth) {
		return true;
	}
	$("#rzcon_error").html(authText);
	$("#dialogRz").show();
	return false;
}

function checkRemark(){
	var remarks = $("#remarks").val();
	if(remarks.length < 0 || remarks == null || remarks == ""){
		$("#con_error").html("对不起，您充值的备注不能为空");
		$("#doalogClick").attr("href","javascript:void(0);");
		$("#dialog").show();
		return false;
	}else if(remarks.length > 50){
		$("#con_error").html("超过输入限制50，当前长度为"+remarks.length);
		$("#doalogClick").attr("href","javascript:void(0);");
		$("#dialog").show();
		return false;
	}
	return true;
}

function onSubmit() {
	var $amout =  $("#amount");
	var amount = $amout.val() * 1;
	//if (checkAuth() && checkAmount(amount) && checkRange(amount)) {
	if(isHmd && !checkAuth())
	{
		$("#dialogHmd").show();
		return false;
	}
	if (checkAuth() && checkAmount(amount,$amout) && checkRange(amount)) {
		$("#problem").show();
		return true;
	}
	return false;
}

function onSubmitLine() {
	var $amout =  $("#amountLine");
	if($amout.val() == ""){
		$("#con_error").html("充值金额不能为空");
		$("#doalogClick").attr("href","javascript:void(0);");
		$("#dialog").show();
		return false;
	}
	var amount = $amout.val() * 1;
	var remarks = $.trim($("#remarks").val());
	var pattern = new RegExp("[<>&]"); 
	if(isHmd && !checkAuth()){
		$("#dialogHmd").show();
		return false;
	}
	if(pattern.test(remarks)){  
        $("#con_error").html("当前请求中存在非法字符，请重新输入");
		$("#doalogClick").attr("href","javascript:void(0);");
		$("#dialog").show();
		return false;
    }  
	if (checkAuth()  && checkAmount(amount,$amout) && checkRemark() ) {
		
		$.ajax({
			type:"post",
			url:chargeSubmitUrl,
			data:{"amount":amount,"remarks":remarks},
			async: false ,
	        dataType:"html",
	        success: function(returnData){
	        	if(returnData){
	        		if(returnData.indexOf("存在非法字符")>0){
	        			$("#errorico").removeClass();
	    				$("#errorico").addClass("error");
	        			$("#con_error").html("当前请求中存在非法字符，请重新输入");
	        			$("#dialog").show();
	        			return false;
	        		}
	        		var dataMsg = eval('('+returnData+')');
	        		if(dataMsg[0].num == "00"){
	        			$("#errorico").removeClass();
	    				$("#errorico").addClass("successful");
	        			$("#con_error").html(dataMsg[0].msg);
	        			$("#dialog").show();
	        			$("#doalogClick").attr("href","javascript:reloadCharge();");
						$(".out").bind("click",reloadCharge);
	        			return true;
					}else if(dataMsg[0].num == "01"){
						$("#con_error").html(dataMsg[0].msg);
						$("#doalogClick").attr("href","javascript:void(0);");
	        			$("#dialog").show();
	        			return false;
					}else if(dataMsg[0].num == "101"){
						window.location.href = loginUrl;
                    	return false;
					}/*else if(dataMsg.length>0 && dataMsg[0].num == "02"){
						$("#con_error").html(dataMsg[0].msg);
						$("#doalogClick").attr("href","javascript:void(0);");
	        			$("#dialog").show();
	        			return false;
					}*/
	        	}
	        },
	        error: function(XMLHttpRequest, textStatus, errorThrown){
	        	$("#errorico").removeClass();
				$("#errorico").addClass("error");
	        	$("#con_error").html("系统繁忙，请稍后重试！");
    			$("#dialog").show();
	        }
		});
		return true;
	}
	return false;
}

function reloadCharge(){
	window.location.href= chargeUrl + "?i=2";
	$("#doalogClick").attr("href","javascript:void(0);");
	$("input[name='amount']").val("");
	$("#remarks").val("");
	
}

function fmoney(s, n) {
    n = n > 0 && n <= 20 ? n : 2;
    var f = s < 0 ? "-" : ""; //判断是否为负数
    s = parseFloat((Math.abs(s) + "").replace(/[^\d\.-]/g, "")).toFixed(n) + "";//取绝对值处理, 更改这里n数也可确定要保留的小数位
    var l = s.split(".")[0].split("").reverse(),
        r = s.split(".")[1];
    var t = "";
    for(var i = 0; i < l.length; i++ ) {
        t += l[i] + ((i + 1) % 3 == 0 && (i + 1) != l.length ? "," : "");
    }
    return f + t.split("").reverse().join("") + "." + r.substring(0,2);//保留2位小数  如果要改动 把substring 最后一位数改动就可
}

function setDMKeyup(obj){
	$(obj).nextAll("p[errortip]").hide();
	$(obj).nextAll("p[tip]").html(chinaCost($(obj).val())).show();
}

//提交绑卡申请
function subBankcard(evn) {
   	var banknumber = $("input[name='banknumber']");
	var mobile = $("input[name='mobile']");
    var bankname = $("select[name='bankname']");
    var userName = $("input[name='userName']");
    var bankNumber1 = $("select[name='bankNumber1']");
    if(userBankSize <= 0){
        if (banknumber.val().length <= 0) {
            showError(banknumber, "银行卡不能为空！");
            return false;
        }
    }
    if (!checkBandPhone(mobile)) {
        return false;
    }
    
    if (!$(evn).hasClass("btn05")) {
    	$(evn).attr("disabled","true");
        evn.value = "绑卡中";
    }
    
    var data = {
            "mobile": mobile.val(),
            "bankname": bankname.val(),
            "banknumber": banknumber.val(),
            "userName": userName.val(),
            "bankNumber1": bankNumber1.val(),
        };
        $.ajax({
            type: "post",
            dataType: "html",
            url: bindBankcardUrl,
            data: data,
            success: function (data) {
                var ct = eval('(' + data + ')');

            if (ct.length > 0) {
                if (ct[0].num == 1) {
                	openButton(evn, "下一步")
                    showError(banknumber, ct[0].msg);
                    return false;
                }else if (ct[0].num == 2) {
                	openButton(evn, "下一步")
                    showError(mobile, ct[0].msg);
                    return false;
                }else if (ct[0].num == 101) {
                	window.location.href= loginUrl;
                	return false;
                }
            }
            if (ct.length > 0 && ct[0].num == 0) {
            	$("#bindId").val(ct[0].bindId);
            	$("#confirmCharge").show();
            	$("#bindBankcard").hide();
            	$("#confirmBanknumber").html(ct[0].banknumber);
            	var mobileNo = ct[0].mobile.substring(0,3)+"****"+ct[0].mobile.substring(8,11);
            	$("#confirmMobile").html(mobileNo);
            	return true;
            }
            //已经认证绑卡的处理
            if(ct.length > 0 && ct[0].num == 3){
            	$("#confirmBanknumber").html(ct[0].banknumber);
            	var mobileNo = ct[0].mobile.substring(0,3)+"****"+ct[0].mobile.substring(8,11);
            	$("#confirmMobile").html(mobileNo);
            	return true;
            }
        }
    });
} 

//确认支付
function confirmCharge(evn){
	
	var code = $("input[name='input']");
	var verifyCode = $("input[name='verifyCode']");
	var orderId = $("input[name='orderId']");
    if (verifyCode.val().length <= 0) {
        showError(code, "请输入验证码！");
        return false;
    }
    
    if (!$(evn).hasClass("btn05")) {
    	$(evn).attr("disabled","true");
        evn.value = "支付中";
    }
    
    var data = {
            "verifyCode": verifyCode.val(),
            "orderId": orderId.val(),
        };
        $.ajax({
            type: "post",
            dataType: "html",
            url: confirmChargeUrl,
            data: data,
            success: function (data) {
                var ct = eval('(' + data + ')');

            if (ct.length > 0) {
                if (ct[0].num == 1) {
                	openButton(evn, "确认支付")
                    showError(code, ct[0].msg);
                    return false;
                }else if (ct[0].num == 2) {
                	openButton(evn, "确认支付")
                	showError(code, ct[0].msg);
                	return false;
                }else if (ct[0].num == 101) {
                	window.location.href= loginUrl;
                	return false;
                }
            }
            if (ct.length > 0 && ct[0].num == 0) {
    		    $("div.popup_bg1").show();
    		    $("#info").html(showSuccessInfo(ct[0].msg,"successful", userTradingRecordUrl));
            }
        }
    });
}

function showSuccessInfo(msg,type,url){
	return '<div class="dialog">'+
	  '<div class="title" ><a href="javascript:void(0);" class="out" onclick="closeDivInfo()"></a>提示</div>'+
	  '<div class="content">'+
	  '<div class="tip_information">'+
	    '<div class="'+type+'"></div>'+
	      '<div class="tips">' +
	      	'<span class="f20 gray3">'+msg+'</span>'+
	      '</div>'+
	    '</div>'+
     ' <div class="tc mt20"><a href="'+url+'" class="btn01">确 定</a></div> '+
   ' </div>'+
'</div>';
}

function closeDivInfo(){
	$("#info").html("");
	$("div.popup_bg").hide();
	window.location.href = userTradingRecordUrl;
}

//解除按钮致灰
function openButton(evn, str){
	$(evn).removeAttr("disabled");
	$(evn).removeClass("btn05");
	$(evn).addClass("btn01");
	evn.value = str;
}

//绑定手机时计时器
  var wait3 = 60;
  function sendclick(evn) {
      if (wait3 == 0) {
          $(evn).removeAttr("disabled");
          $(evn).removeClass("btn05");
          $(evn).addClass("btn01");
          evn.value = "获取验证码";
          wait3 = 60;
      } else {
          if (!$(evn).hasClass("btn05")) {
              $(evn).addClass("btn05");
              $(evn).removeClass("btn01");
          }
          $(evn).attr("disabled","true");
          evn.value = "(" + wait3 + ")秒重新获取";
          wait3--;
          setTimeout(function () {
                      sendclick(evn);
                  },
                  1000);
      }
  }
  
//获取验证码
function getAuthCode(evn){
	
    if (!$(evn).hasClass("btn05")) {
        $(evn).addClass("btn05");
        $(evn).removeClass("btn01");
        $(evn).attr("disabled","true");
        evn.value = "验证码获取中";
    }
	
	var code = $("input[name='input']");
	var bindId = $("input[name='bindId']");
    var amount = $("input[name='amount']");
    
    if (bindId.val().length <= 0) {
        showError(code, "未找到绑定的银行卡！");
        return false;
    }
    if (amount.val().length <= 0) {
    	showError(code, "充值金额不能为空！");
        return false;
    }
    
    var data = {
            "bindId": bindId.val(),
            "amount": amount.val(),
        };
        $.ajax({
            type: "post",
            dataType: "html",
            url: getAuthCodeUrl,
            data: data,
            success: function (data) {
                var ct = eval('(' + data + ')');

            if (ct.length > 0) {
                if (ct[0].num == 1) {
                	openButton(evn, "获取验证码");
                    showError(code, ct[0].msg);
                    return false;
                }else if (ct[0].num == 2) {
                	openButton(evn, "获取验证码");
                    showError(code, ct[0].msg);
                    return false;
                }else if (ct[0].num == 3) {
                	openButton(evn, "获取验证码");
                    showError(code, ct[0].msg);
                    return false;
                }else if (ct[0].num == 4) {
                	openButton(evn, "获取验证码");
                    showError(code, ct[0].msg);
                    return false;
                }else if (ct[0].num == 101) {
                	window.location.href= loginUrl;
                	return false;
                }
            }
            if (ct.length > 0 && ct[0].num == 0) {
            	$("#orderId").val(ct[0].orderId);
            	sendclick(evn);
            }
        }
    });
}

//校验手机号码
function checkBandPhone(mobile) {
    if (mobile.val().length <= 0) {
        showError(mobile, "请输入手机号码！");
        return false;
    }
    var myreg = /^(13|14|15|17|18)[0-9]{9}$/;
    if (!myreg.test(mobile.val())) {
        showError(mobile, "手机号码不正确！");
        return false;
    }
    return true;
}

//显示异常
function showError(obj, msg) {
    $error = $(obj).nextAll("p[errortip]");
    $tip = $(obj).nextAll("p[tip]");
    $error.addClass("error_tip");
    $error.html(msg);
    $tip.hide();
    $error.show();
}
