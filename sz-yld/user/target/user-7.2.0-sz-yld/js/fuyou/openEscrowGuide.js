function paramCheck() {
	var realName = $("input[name='realName']");
	var mobile = $("input[name='mobile']");
	var verifyCode = $("input[name='verifyCode']");
	var identificationNo =$("input[name='identificationNo']");
	var eUpdateVerify =$("input[name='eUpdateVerify']");
	
	var realNameFZRR = $("input[name='realNameFZRR']");
	var identificationNoFZRR =$("input[name='identificationNoFZRR']");
	
	$error = $(this).nextAll("p[errortip]");
	var openSumbit = $("#openSumbit");
    if(_isFZRR != _finalFZRR) 
    {
		if (isNull.test(realName.val())||isNull.test(identificationNo.val())||isNull.test(mobile.val())
				||isNull.test(verifyCode.val())||(!(flagSfxyyzm != "undefined" && flagSfxyyzm == false)&&isNull.test(eUpdateVerify.val()))) {
			openSumbit.attr('disabled',true);
			openSumbit.removeClass("btn01");
			openSumbit.addClass("btn03 btn03_gray");
			return false;
		}
        if(!checkName.test(realName.val())) {
        	showError(realName,"请输入合法的姓名");
			openSumbit.attr('disabled',true);
			openSumbit.removeClass("btn01");
			openSumbit.addClass("btn03 btn03_gray");
            return false;
        }
        if(realName.val().length > 20){
        	showError(realName,"姓名最多为20个字符");;
			openSumbit.attr('disabled',true);
			openSumbit.removeClass("btn01");
			openSumbit.addClass("btn03 btn03_gray");
            return false;
        }
        if (!isIdCardNot(identificationNo.val(), $error)) {
            $tip = $(identificationNo).nextAll("p[tip]");
            $tip.hide();
            $err.show();
			openSumbit.attr('disabled',true);
			openSumbit.removeClass("btn01");
			openSumbit.addClass("btn03 btn03_gray");
            return false;
        }
        $err = $(identificationNo).nextAll("p[errortip]");
        if(identificationNo.val().length<=0){
        	showError(idCard,"不能为空！");
        	return false;
        }
    } 
    else 
    {
		if (isNull.test(realNameFZRR.val())||isNull.test(identificationNoFZRR.val())||isNull.test(mobile.val())
				||isNull.test(verifyCode.val())||(!(flagSfxyyzm != "undefined" && flagSfxyyzm == false)&&isNull.test(eUpdateVerify.val()))) {
			openSumbit.attr('disabled',true);
			openSumbit.removeClass("btn01");
			openSumbit.addClass("btn03 btn03_gray");
			return false;
		}
    }
    
    if (!checkBandPhone(mobile)) {
		openSumbit.attr('disabled',true);
		openSumbit.removeClass("btn01");
		openSumbit.addClass("btn03 btn03_gray");
        return false;
    }
    
    if (verifyCode.val().length <= 0) {
        showError(verifyCode, "不能为空！");
        return false;
    }
    if(!(flagSfxyyzm != "undefined" && flagSfxyyzm == false)) {
        if (eUpdateVerify.val().length <= 0) {
            showError(eUpdateVerify, "不能为空！");
            return false;
        }
    }

	openSumbit.attr('disabled',false);
	openSumbit.removeClass("btn03 btn03_gray");
	openSumbit.addClass("btn01");
	return true;
}
var isNull = /^[\s]{0,}$/;
//var checkName = ^[\u4e00-\u9fa5]+(·[\u4e00-\u9fa5]+)*$; 
var checkName = /^[\u4e00-\u9fa5]{2,}$/;
function closeDiv() {
    $("#dialog").hide();
}

function isIdCardNot(num,$error) {            
	num = num.toUpperCase();           //身份证号码为15位或者18位，15位时全为数字，18位前17位为数字，最后一位是校验位，可能为数字或字符X。        
    if (!(/(^\d{15}$)|(^\d{17}([0-9]|X)$)/.test(num))) {     
    	$error.addClass("red");
		$error.html('输入的身份证号不符合规定！');
        return false;         
    } //校验位按照ISO 7064:1983.MOD 11-2的规定生成，X可以认为是数字10。 
    //下面分别分析出生日期和校验位 
    var len, re; len = num.length; if (len == 15) { 
        re = new RegExp(/^(\d{6})(\d{2})(\d{2})(\d{2})(\d{3})$/); 
        var arrSplit = num.match(re);  //检查生日日期是否正确
        var dtmBirth = new Date('19' + arrSplit[2] + '/' + arrSplit[3] + '/' + arrSplit[4]); 
        var bGoodDay; bGoodDay = (dtmBirth.getYear() == Number(arrSplit[2])) && ((dtmBirth.getMonth() + 1) == Number(arrSplit[3])) && (dtmBirth.getDate() == Number(arrSplit[4]));
        if (!bGoodDay) {         
            $error.addClass("red");
    		$error.html('输入的身份证号里出生日期不对！');
            return false;
        } else { //将15位身份证转成18位 //校验位按照ISO 7064:1983.MOD 11-2的规定生成，X可以认为是数字10。        
            var arrInt = [7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2];
            var arrCh = ['1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2'];
            var nTemp = 0, i;            
            num = num.substr(0, 6) + '19' + num.substr(6, num.length - 6);           
            for(i = 0; i < 17; i ++) {                 
                nTemp += num.substr(i, 1) * arrInt[i];        
            }
            num += arrCh[nTemp % 11]; 
            return true;
        }
    }
    if (len == 18) {
        re = new RegExp(/^(\d{6})(\d{4})(\d{2})(\d{2})(\d{3})([0-9]|X)$/); 
        var arrSplit = num.match(re);  //检查生日日期是否正确 
        var dtmBirth = new Date(arrSplit[2] + "/" + arrSplit[3] + "/" + arrSplit[4]); 
        var bGoodDay; bGoodDay = (dtmBirth.getFullYear() == Number(arrSplit[2])) && ((dtmBirth.getMonth() + 1) == Number(arrSplit[3])) && (dtmBirth.getDate() == Number(arrSplit[4])); 
        if (!bGoodDay) { 
            $error.addClass("red");
    		$error.html('输入的身份证号里出生日期不对！');
            return false; 
        }
        else { //检验18位身份证的校验码是否正确。 //校验位按照ISO 7064:1983.MOD 11-2的规定生成，X可以认为是数字10。 
            var valnum; 
            var arrInt = [7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2];
            var arrCh = ['1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2'];
            var nTemp = 0, i; 
            for(i = 0; i < 17; i ++) { 
                nTemp += num.substr(i, 1) * arrInt[i];
            } 
            valnum = arrCh[nTemp % 11]; 
            if (valnum != num.substr(17, 1)) { 
                $error.addClass("red");
        		$error.html('18位身份证号校验失败！');
                return false; 
            } 
            return true; 
        } 
    } 
    return false;
}

//获取手机验证码 绑定手机
function bandPhone(evn) {
    var bemil = $("input[name='mobile']");
    var openSumbit = $("#openSumbit");
    if (!checkBandPhone(bemil)) {
		openSumbit.attr('disabled',true);
		openSumbit.removeClass("btn01");
		openSumbit.addClass("btn03");
        return false;
    }
    var bindPhoneVerify;
    var bindPhoneVerify1;
    if (!(flagSfxyyzm != "undefined" && flagSfxyyzm == false)) {
        bindPhoneVerify1 = $("input[name='eUpdateVerify']");
        bindPhoneVerify = bindPhoneVerify1.val();
        if (bindPhoneVerify.length <= 0) {
            showError(bindPhoneVerify1, "不能为空！");
            return false;
        }
        if (!myregCode.test(bindPhoneVerify)) {
            showError(bindPhoneVerify1, "请输入正确的验证码！");
            return false;
        }
    }
    var tokenValue =$("input[name='"+tokenName+"']").val();
    var data = {
        "tokenKey": tokenValue,
        "type": "bind",
        "emil": "",
        "phone": bemil.val(),
        "verifyType": "BIND_PHONE",
        "isFZRR": _isFZRR,
        "verifyCode": bindPhoneVerify,
    };
    $.ajax({
        type: "post",
        dataType: "html",
        url: _sendUrl,
        data: data,
        success: function (data) {
            var ct = eval('(' + data + ')');
            //替换token
            setToken(ct);

            if (ct.length > 0 && ct[0].num != 1) {
                flushVerifyCode(bindPhoneVerify1);
                if (ct[0].num == 0) {
                    showError(bemil, ct[0].msg);
                    return false;
                }else if (ct[0].num == 2) {
                    showError(bindPhoneVerify1, ct[0].msg);
                    return false;
                }else if (ct[0].num == 4) {
                    showError(bemil, ct[0].msg);
                    return false;
                }else if (ct[0].num == 101) {
                	window.location.href= _loginUrl;
                	return false;
                }
            }
            if (ct.length > 0 && ct[0].num == 1) {
                sendclick(evn);
            }
        }
    });
}

//校验手机号码
function checkBandPhone(bemil) {
    if (bemil.val().length <= 0) {
        showError(bemil, "请输入手机号码！");
        return false;
    }
    var myreg = /^(13|14|15|17|18)[0-9]{9}$/;
    if (!myreg.test(bemil.val())) {
        showError(bemil, "手机号码不正确！");
        return false;
    }
    return true;
}

function setToken(ct) {
    if (ct.length > 0) {
        $("#token").html(ct[0].tokenNew);
    }
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

//刷新验证码
function flushVerifyCode(obj){
    if (!(flagSfxyyzm != "undefined" && flagSfxyyzm == false)) {
        obj.next("span").children("img").click();
    }
}

//隐藏异常
function hideError(obj) {
    $error = $(obj).nextAll("p[errortip]");
    $error.removeClass();
    $error.html("");
    //$error.hide();
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

function onSubmit() {
	if(!paramCheck()) {
		return false;
	}
	var result = true;
    var realName = $("input[name='realName']");
    var identificationNo = $("input[name='identificationNo']");
	var mobile = $("input[name='mobile']");
    var verifyCode = $("input[name='verifyCode']")
    var data = {
            "mobile": mobile.val(),
            "verifyCode": verifyCode.val(),
            "identificationNo": identificationNo.val(),
            "realName": realName.val(),
            "userTag": _isFZRR,
        };
    $.ajax({
        type: "post",
        dataType: "html",
        url:_escrowCheckUrl,
        data: data,
		async:false,
		success:function(data) {
            var ct = eval('(' + data + ')');
            
            if (ct.length > 0 && ct[0].num != 1) {
                if (ct[0].type == 'mobile') {
                    showError(mobile, ct[0].msg);
                    result = false;
                } else if(ct[0].type == 'realName'){
                    showError(realName, ct[0].msg);
                    result = false;
                }else if(ct[0].type == 'idcard'){
                    showError(identificationNo, ct[0].msg);
                    result = false;
                }else if(ct[0].type == 'verifyCode'){
                    showError(verifyCode, ct[0].msg);
                    result = false;
                }
            }
		}
	});
	return result;
}






