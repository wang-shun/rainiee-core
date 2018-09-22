var isNull = /^[\s]{0,}$/;
//防止SQL注入
var re= /select|update|delete|exec|count|’|"|=|;|>|<|%/i;

var passwordRegex = /^(?![a-zA-Z0-9]+$)(?![^a-zA-Z/D]+$)(?![^0-9/D]+$).{8,12}$/;
var errorPwdMsg = "数字+字母+特殊字符，8-12位长度";

var newPasswordRegex = eval($("#newPasswordRegexId").val());
var passwordRegexContent = $("#passwordRegexContentId").val()+"";

if(newPasswordRegex !='' && newPasswordRegex != undefined && newPasswordRegex != "undefined" ){
	passwordRegex = newPasswordRegex;
}

if(passwordRegexContent !='' && passwordRegexContent != undefined && passwordRegexContent != "undefined"){
	errorPwdMsg = passwordRegexContent;
}
$(function(){
	$('#passwordId').bind('input propertychange', function() {
		checkStrength();
	});
});


function phoneCheck() {
	var myreg = /^(13|14|15|17|18)[0-9]{9}$/;
	var ipt = $("#phoneId");
	var val = ipt.val();
	$error = ipt.parent().nextAll("p[errortip]");
	$tip = ipt.parent().nextAll("p[tip]");
	if (isNull.test(val)) {
		$error.html("不能为空！");
		$tip.hide();
		$error.show();
		return false;
	}else if(re.test(val)) {
        $error.html("输入存在特殊字符！");
        $tip.hide();
        $error.show();
        return false;
    }else if(val.length>11){
		$error.html("超过输入限制"+11+"，当前长度为"+val.length);
		$tip.hide();
		$error.show();
		return false;
	}else if(val.length < parseInt(11)){
		$error.html("小于输入限制"+11+"，当前长度为"+val.length);
		$tip.hide();
		$error.show();
		return false;
	}else if(!myreg.test(val)){
		$error.html("请输入正确手机号!");
		$tip.hide();
		$error.show();
		return false;
	}else{
		$error.hide();
		$tip.addClass("gray");
		$tip.html("请输入11位手机号");
		$tip.show();
		return true;
	}
}

function emailCheck() {
	var myreg = /^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/;
	var ipt = $("input[name='email']");
	var val = ipt.val();
	$error = ipt.parent().nextAll("p[errortip]");
	$tip = ipt.parent().nextAll("p[tip]");
	if (isNull.test(val)) {
		$error.html("不能为空！");
		$tip.hide();
		$error.show();
		return false;
	}else if(re.test(val)) {
        $error.html("输入存在特殊字符！");
        $tip.hide();
        $error.show();
        return false;
    }else if(!myreg.test(val)){
		$error.html("邮箱地址不正确!");
		$tip.hide();
		$error.show();
		return false;
	}else{
		$error.hide();
		$tip.addClass("gray");
		$tip.html("请输入邮箱地址");
		$tip.show();
		return true;
	}
}

function verifyCodeCheck($eve) {
	var val = $($eve).val();
	$error = $($eve).parent().parent().nextAll("p[errortip]");
	$tip = $($eve).parent().parent().nextAll("p[tip]");
	if (isNull.test(val)) {
		$error.html("验证码不能为空");
		$tip.hide();
		$error.show();
		return false;
	} else if(re.test(val)) {
        $error.html("输入存在特殊字符！");
        $tip.hide();
        $error.show();
        return false;
    }else if (!/^[0-9]{6}$/.test(val)) {
		$error.html("您输入的验证码有误");
		$tip.hide();
		$error.show();
		return false;
	}else{
		$error.hide();
		$tip.addClass("gray");
		$tip.html("请输入6位验证码");
		$tip.show();
		return true;
	}
}

function codeCheck() {
	var ipt = $("input[name='code']");
	var val = ipt.val();
	$error = ipt.parent().parent().nextAll("p[errortip]");
	if (isNull.test(val)) {
		$error.removeClass("prompt");
		$error.addClass("error_tip red");
		$error.html("手机验证码不能为空!");
		$error.show();
		return false;
	} else if(re.test(val)) {
		$error.removeClass("prompt");
		$error.addClass("error_tip red");
        $error.html("输入存在特殊字符！");
        $error.show();
        return false;
    }else{
    	$error.removeClass("error_tip red");
    	$error.addClass("prompt");
		$error.html("");
		return true;
	}
}

function answerCheck(){
	var ipt = $("input[name='answer']");
	var val = ipt.val();
	$error = ipt.nextAll("p[errortip]");
	if (isNull.test(val)) {
		$error.removeClass("prompt");
		$error.addClass("error_tip red");
		$error.html("密保答案不能为空!");
		$error.show();
		return false;
	} else if(re.test(val)) {
		$error.removeClass("prompt");
		$error.addClass("error_tip red");
        $error.html("输入存在特殊字符！");
        $tip.hide();
        $error.show();
        return false;
    }else{
    	$error.removeClass("error_tip red");
    	$error.addClass("prompt");
		$error.html("");
		return true;
	}
}

function nameCheck() {
	var ipt = $("input[name='accountName']");
	var val = ipt.val();
	$error = ipt.parent().nextAll("p[errortip]");
	$tip = ipt.parent().nextAll("p[tip]");
	if (isNull.test(val)) {
		$error.html("用户名不能为空");
		$tip.hide();
		$error.show();
		return false;
	}else if(re.test(val)) {
        $error.html("输入存在特殊字符！");
        $tip.hide();
        $error.show();
        return false;
    }else{
		$error.hide();
		$tip.addClass("gray");
		$tip.html("请输入正确的用户名");
		$tip.show();
		return true;
	}
}

function passwordCheck() {
	var myreg = passwordRegex;
	var ipt = $("input[name='password']");
	var _ipt = $("input[name='repassword']");
	var val = ipt.val();
	$error = ipt.parent().parent().nextAll("p[errortip]");
	$tip = ipt.parent().parent().nextAll("p[tip]");
	_$error = _ipt.parent().nextAll("p[errortip]");
	_$tip = _ipt.parent().nextAll("p[tip]");
	var div = ipt.parent().next();

	div.find("span").removeClass("cur");
	//输入确认密码后又修改密码的判断
	if (!isNull.test(_ipt.val())){
		if (_ipt.val() == val){
			_$tip.show();
			_$error.hide();
		} else {
			_$error.html("您两次输入的密码不一致");
			_$tip.hide();
			_$error.show();
		}
	}
	if (isNull.test(val)) {
		$error.html("密码不能为空！");
		$tip.hide();
		$error.show();
		return false;
	}/* else if(re.test(val)) {
        $error.html("输入存在特殊字符！");
        $tip.hide();
        $error.show();
        return false;
    }*/else if(!/^\S+$/.test(val)){
		$error.html("密码不能包含空格！");
		$tip.hide();
		$error.show();
		return false;
		
	} else if(!myreg.test(val)){
		$error.html(errorPwdMsg);
		$tip.hide();
		$error.show();
		return false;
	} else if (/\d+/.test(val) && /[A-Za-z]+/.test(val) && /\W+/.test(val)) {
		div.find("span.strong").addClass("cur");
	} else if (/[a-zA-Z]+/.test(val) && /[0-9]+/.test(val)) {
		div.find("span.medium").addClass("cur");
	} else if (/[a-zA-Z]+/.test(val) && /[\W_]/.test(val)) {
		div.find("span.medium").addClass("cur");
	} else if (/[0-9]+/.test(val) && /[\W_]/.test(val)) {
		div.find("span.medium").addClass("cur");
	} else {
		div.find("span.weak").addClass("cur");
	}
	$error.hide();
	$tip.addClass("gray");
	$tip.html(errorPwdMsg);
	$tip.show();
	return true;
}

function checkStrength(){
	var ipt = $("#passwordId");
	var val = ipt.val();
	var div = ipt.parent().next();
	div.find("span").removeClass("cur");
	if(!val){
		return;
	}
	if (/\d+/.test(val) && /[A-Za-z]+/.test(val) && /\W+/.test(val)) {
		div.find("span.strong").addClass("cur");
	} else if (/[a-zA-Z]+/.test(val) && /[0-9]+/.test(val)) {
		div.find("span.medium").addClass("cur");
	} else if (/[a-zA-Z]+/.test(val) && /[\W_]/.test(val)) {
		div.find("span.medium").addClass("cur");
	} else if (/[0-9]+/.test(val) && /[\W_]/.test(val)) {
		div.find("span.medium").addClass("cur");
	} else {
		div.find("span.weak").addClass("cur");
	}

}
function rePasswordCheck() {
	var ipt = $("input[name='password']");
	var _ipt = $("input[name='repassword']");
	var val = _ipt.val();
	_$error = _ipt.parent().nextAll("p[errortip]");
	_$tip = _ipt.parent().nextAll("p[tip]");
	if (isNull.test(ipt.val())) {
		_$error.html("请先输入密码！");
		_$tip.hide();
		_$error.show();
		return false;
	} else if (isNull.test(val)) {
		_$error.html("确认密码不能为空！");
		_$tip.hide();
		_$error.show();
		return false;
	}/* else if(re.test(val)) {
        _$error.html("输入存在特殊字符！");
        _$tip.hide();
        _$error.show();
        return false;
    }*/else if (val != ipt.val()) {
		_$error.html("您两次输入的密码不一致！");
		_$tip.hide();
		_$error.show();
		return false;
	}else if(!passwordCheck()){
		return false;
	}
	return true;
}