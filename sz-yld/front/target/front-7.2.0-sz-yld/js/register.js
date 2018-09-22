var checkName = true;
var checkmobile = true;
$(function() {
	showAuto();
	if(userType == "qy"){
		showQyDiv();
	}else{
		showGrDiv();
	}
	$(".prompt span").hide();
	$("#iAgree").attr("checked", false);
	$("#iAgree").click(function() {
		var iAgree = $(this).attr("checked");
		var register = $("#sub-btn");
		if (iAgree) {
			register.removeClass("btn_gray btn_disabled");
		} else {
			register.addClass("btn_gray btn_disabled");
		}
	});
	
	$('#passwordFirstId').bind('input propertychange', function() { 
		checkStrength("gr");
	});
	
	$("#iAgree2").attr("checked", false);
	$("#iAgree2").click(function() {
		var iAgree = $(this).attr("checked");
		var register = $("#sub-btn2");
		if (iAgree) {
			register.removeClass("btn_gray btn_disabled");
		} else {
			register.addClass("btn_gray btn_disabled");
		}
	});
	
	$('#qyPasswordFirstId').bind('input propertychange', function() { 
		checkStrength("qy");
	});
});
var isNull = /^[\s]{0,}$/;
var loginName = newUserNameRegex;
var codeVal=/^[A-Za-z0-9]{0,20}$/;
var numVal = /^[A-Za-z0-9]{0,6}$/;

function showQyDiv(){
	$("#qyToken").html(getTokenStr());
	$("#grId").hide();
	$("#qyId").show();
	$(".prompt").html("");
	$(".correct_ico").hide();
	anotherImg(_tURL+'?'+Math.random(),"form2");
	document.getElementById("form2").reset();
	var iAgree = $("#iAgree2").attr("checked");
	var register = $("#sub-btn2");
	if (iAgree) {
		register.removeClass("btn_gray btn_disabled");
	} else {
		register.addClass("btn_gray btn_disabled");
	}
}

function showGrDiv(){
	$("#grToken").html(getTokenStr());
	$("#qyId").hide();
	$("#grId").show();
	$(".correct_ico").hide();
	$(".prompt").html("");
	anotherImg(_tURL+'?'+Math.random(),"form1");
	document.getElementById("form1").reset();
	var iAgree = $("#iAgree").attr("checked");
	var register = $("#sub-btn");
	if (iAgree) {
		register.removeClass("btn_gray btn_disabled");
	} else {
		register.addClass("btn_gray btn_disabled");
	}
}

function onSubmit()
{
	try{
		if(is_check_agree == "true"){
			var iAgree = $("#iAgree").attr("checked");
			if(iAgree != "checked"){
				return false;
			}
		}
		if (checkName && checkmobile
			 && nameCheck("gr")
			&& rePasswordCheck("gr")&& verifyCodeCheck("form1")&&codeCheck()&&passwordCheck("gr") ) {
			$("#hdnUserId").val(RSAUtils.encryptedString(key,$("#userId").val()));
			$("#passwordFirst").val(RSAUtils.encryptedString(key,$("#passwordFirstId").val()));
			$("#passwordSecond").val(RSAUtils.encryptedString(key,$("#passwordSecondId").val()));
			return true;
		}
	}catch(err){
		return false;
	}
	return false;
}

function onAssSubmit()
{
	try{
		var iAgree = $("#iAgree").attr("checked");
		if (checkName && checkmobile && iAgree == "checked"
			 && passwordCheck("gr") && rePasswordCheck("gr")&& nameCheck("gr")) {
			$("#hdnUserId").val(RSAUtils.encryptedString(key,$("#userId").val()));
			$("#passwordFirstId").val(RSAUtils.encryptedString(key,$("#passwordFirstId").val()));
			$("#passwordSecondId").val(RSAUtils.encryptedString(key,$("#passwordSecondId").val()));
			return true;
		}
	}catch(err){
		return false;
	}
	return false;
}

function onQySubmit()
{
	try{
		if(is_check_agree == "true"){
			var iAgree = $("#iAgree2").attr("checked");
			if(iAgree != "checked"){
				return false;
			}
		}
		if (checkName && enterpriseNameCheck()&& nameCheck("qy")
			&& rePasswordCheck("qy")&& verifyCodeCheck("form2")&&passwordCheck("qy") ) {
			$("#hdnQyUserId").val(RSAUtils.encryptedString(key,$("#qyUserId").val()));
			$("#qyPasswordFirst").val(RSAUtils.encryptedString(key,$("#qyPasswordFirstId").val()));
			$("#qyPasswordSecond").val(RSAUtils.encryptedString(key,$("#qyPasswordSecondId").val()));
			return true;
		}
	}catch(err){
		return false;
	}
	return false;
}


function checkoxBtn(type)
{
	var $iAgree = type == "qy" ? $("#iAgree2") : $("#iAgree");
	var $btn = type == "qy" ? $("#sub-btn2") : $("#sub-btn");
	var iAgree = $iAgree.attr("checked");
	if (iAgree != "checked"){
		$btn.attr('disabled',true).addClass("btn_gray btn_disabled");
	}else{
		$btn.attr('disabled',false).removeClass("btn_gray btn_disabled");
	}
	
}

function recoverPassword(type){	
	var ipt = $("#passwordFirstId");	
	var sp = $("#password-tip");
	var passwordSuccess = $("#passwordSuccess");	
	if(type=="qy"){
		ipt = $("#qyPasswordFirstId");	
		sp = $("#password-tip2");
		passwordSuccess = $("#passwordSuccess2");	
	}
	var val = ipt.val();	
	var p = ipt.parent().parent().parent().find("p");
	passwordSuccess.removeClass("success_tip");
	p.removeClass();
	p.addClass("grayd prompt");
	p.text(passwordRegexContent);	
	p.show();
	passwordSuccess.addClass("success_tip");
	checkStrength();
}
function recoverName(type){
	var ipt = type == "qy" ? $("#qyUserId") : $("#userId");
	var val = ipt.val();
	var p = ipt.parent().parent().parent().find("p");
	p.removeClass();
	p.addClass("grayd prompt");
	p.text(userNameRegexContent);	
}
function recoverTo(type){
	var _ipt = type == "qy" ? $("#qyPasswordSecondId") :$("#passwordSecondId");
	var val = _ipt.val();	
	var p = _ipt.parent().parent().parent().find("p");
	p.removeClass();
	p.addClass("grayd prompt");
	p.text("请再次输入密码");
}

function nameCheck(type) {
	var ipt = $("#userId");
	var loginSuccess = $("#loginSuccess");
	var correct = $("#correct");
	if(type == "qy"){
		ipt = $("#qyUserId");
		loginSuccess = $("#loginSuccess2");
		correct = $("#correct2");
	}
	var val = ipt.val();
	var p = ipt.parent().parent().parent().find("p");
	
	loginSuccess.removeClass("success_tip");
	correct.hide();
	p.removeClass();
	p.addClass("grayd prompt");
	p.text("");
	if (isNull.test(val)) {
		p.addClass("red");
		p.removeClass("grayd");
		p.text("用户名不能为空");
		correct.hide();
		return false;
	} else if (!loginName.test(val)) {
		p.addClass("red");
		p.removeClass("grayd");
		correct.hide();
		return false;
	}
	if(type == "qy"){
		if(!isNameAndPWDifferent("qyUserId","qyPasswordFirstId")){
			p.addClass("red");
			p.removeClass("grayd");
			p.text("密码不能与用户名一致");
		}
	}else{
		if(!isNameAndPWDifferent("userId","passwordFirstId")){
			p.addClass("red");
			p.removeClass("grayd");
			p.text("密码不能与用户名一致");
		}
	}

	$.ajax({
		type: 'POST',
		url: _nURL,
		data: {accountName : val},
		dataType: "html",
		async:true,
		success:function(data){
			if ($.trim(data) == 'true') {
				loginSuccess.removeClass("success_tip");
				correct.hide();
				p.addClass("red");
				p.removeClass("grayd");
				p.text("该用户名已存在，请输入其他用户名");
				checkName = false;
				return false;
			}
		},
		error: function(XMLHttpRequest, textStatus, errorThrown){
		}
	});
	loginSuccess.addClass("success_tip");
	correct.show();
	checkName = true;
	return true;
}

function enterpriseNameCheck() {
	var ipt = $("#enterpriseNameId");
	var loginSuccess = $("#enterpriseSuccess");
	var correct = $("#correct3");
	var val = ipt.val();
	var p = ipt.parent().parent().parent().find("p");
	var nameCheck = true;
	
	loginSuccess.removeClass("success_tip");
	correct.hide();
	p.removeClass();
	p.addClass("grayd prompt");
	p.text("");
	if (isNull.test(val)) {
		p.addClass("red");
		p.removeClass("grayd");
		p.text("企业名称不能为空");
		correct.hide();
		return false;
	} else if (val.length < 6) {
		p.addClass("red");
		p.removeClass("grayd");
		p.text("企业名称不能少于6个字符");
		correct.hide();
		return false;
	} 

	$.ajax({
		type: 'POST',
		url: _eURL,
		data: {enterpriseName : val},
		dataType: "html",
		async:false,
		success:function(data){
			if ($.trim(data) == 'true') {
				loginSuccess.removeClass("success_tip");
				correct.hide();
				p.addClass("red");
				p.removeClass("grayd");
				p.text("该企业名称已被注册");
				nameCheck = false;
			}else{
				loginSuccess.addClass("success_tip");
				correct.show();
			}
		},
		error: function(XMLHttpRequest, textStatus, errorThrown){
		}
	});
	return nameCheck;
}

function recoverEnterpriseName(){
	var ipt = $("#enterpriseNameId");
	var val = ipt.val();
	var p = ipt.parent().parent().parent().find("p");
	p.removeClass();
	p.addClass("grayd prompt");
	p.text("请输入企业名称");	
}

function checkVerifyCode(formId) {
	var ipt = $("#"+formId+" input[name='verifyCode']");
	var val = ipt.val();
	var p = ipt.parent().parent().parent().find("p");
	
	var checkVerifyCode = true;
	$.ajaxSetup({async : false});
	$.post(_vURL, {
		verifyCode : val
	}, function(data) {
		if ($.trim(data) == 'true') {
			p.addClass("red");
			p.removeClass("grayd");
			p.text("无效的验证码或验证码已过期.");
			checkVerifyCode = false;
		}
	});
	if(!checkVerifyCode){
		anotherImg(_tURL+'?'+Math.random(),formId);
		return false;
	}
	return true;
}

function checkStrength(type){
	var ipt = type == "qy" ? $("#qyPasswordFirstId") : $("#passwordFirstId");
	var val = ipt.val();
	var div = ipt.parent().next().next();
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

function passwordCheck(type) {
	var ipt = $("#passwordFirstId");
	var _ipt = $("#passwordSecondId");
	var sp = $("#password-tip");
	var passwordSuccess = $("#passwordSuccess");
	var newPasswordSuccess = $("#newPasswordSuccess");
	var userId = "userId";
	var passwordFirstId = "passwordFirstId";
	if(type=="qy"){
		ipt = $("#qyPasswordFirstId");
		_ipt = $("#qyPasswordSecondId");
		sp = $("#password-tip2");
		passwordSuccess = $("#passwordSuccess2");
		newPasswordSuccess = $("#newPasswordSuccess2");
		userId = "qyUserId";
		passwordFirstId = "qyPasswordFirstId";
	}
	var val = ipt.val();
	var p = ipt.parent().parent().parent().find("p");
	var _p = _ipt.parent().parent().parent().find("p");
	var div = ipt.parent().next().next();
	passwordSuccess.removeClass("success_tip");
	p.removeClass();
	p.addClass("grayd prompt");
	//p.text(passwordRegexContent);
	p.text("");
	var pattern = newPasswordRegex;

	div.find("span").removeClass("cur");
	//输入确认密码后又修改密码的判断
	if (!isNull.test(_ipt.val())){
		if (_ipt.val() == val){
			p.removeClass("red");
			p.addClass("grayd");
			p.text("");
			newPasswordSuccess.addClass("success_tip");
		} else {
			p.removeClass("grayd");
			p.addClass("red");
			p.text("您两次输入的密码不一致");
			newPasswordSuccess.removeClass("success_tip");
		}
	}
	if (isNull.test(val)) {
		p.addClass("red");
		p.removeClass("grayd");
		p.text("密码不能为空");
		return false;
	} else if (!pattern.test(val)) {
		p.addClass("red");
		p.removeClass("grayd");
		return false;
	}else if (/\d+/.test(val) && /[A-Za-z]+/.test(val) && /\W+/.test(val)) {
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
	if(!isNameAndPWDifferent(userId,passwordFirstId)){
		p.addClass("red");
		p.removeClass("grayd");
		p.text("密码不能与用户名一致");
	}
	passwordSuccess.addClass("success_tip");
	return true;
}
function rePasswordCheck(type) {
	var ipt = $("#passwordFirstId");
	var _ipt = $("#passwordSecondId");
	var newPasswordSuccess = $("#newPasswordSuccess");
	if(type == "qy"){
		ipt = $("#qyPasswordFirstId");
		_ipt = $("#qyPasswordSecondId");
		newPasswordSuccess = $("#newPasswordSuccess2");
	}
	var val = _ipt.val();
	newPasswordSuccess.removeClass("success_tip");
	var p = _ipt.parent().parent().parent().find("p");
	p.removeClass();
	p.addClass("grayd prompt");
	//p.text("请再次输入密码");
	p.text("");
	if (isNull.test(ipt.val())) {
		p.addClass("red");
		p.removeClass("grayd");
		p.text("请先输入密码");
		return false;
	} else if (isNull.test(val)) {
		p.addClass("red");
		p.removeClass("grayd");
		p.text("确认密码不能为空");
		return false;
	} else if (val != ipt.val()) {
		p.addClass("red");
		p.removeClass("grayd");
		p.text("您两次输入的密码不一致");
		return false;
	}else if(!passwordCheck(type)){
		newPasswordSuccess.removeClass("success_tip");
		return false;
	}
	newPasswordSuccess.addClass("success_tip");
	return true;
}

/**
 * 校验用户名跟密码是否一致
 * userNameId : 用户名id
 * firstPasswordId : 第一个密码框id
 */
function isNameAndPWDifferent(userNameId,firstPasswordId){
	var userNameObj = $("#"+userNameId);
	var firstPasswordObj = $("#"+firstPasswordId);
	if(userNameObj && firstPasswordObj && userNameObj.val()==firstPasswordObj.val()){
		return false;
	}
	return true;
}

function verifyCodeCheck(formId) {

	if(registerFlage != "undefined" && registerFlage == false){
		return true;
	}

	var ipt = $("#"+formId+" input[name='verifyCode']");
	var val = ipt.val();
	var verifySuccess = $("#"+formId+" .verifySuccess");
	verifySuccess.removeClass("success_tip");
	var p = ipt.parent().parent().next();
	p.removeClass();
	p.addClass("grayd prompt");
	//p.text("请填写图片中的字符");
	p.text("");
	if (isNull.test(val)) {
		p.addClass("red");
		p.removeClass("grayd");
		p.text("验证码不能为空");
		return false;
	} else if (!/^[0-9]{6}$/.test(val)) {
		p.addClass("red");
		p.removeClass("grayd");
		p.text("您输入的验证码有误");
		return false;
	}
	if(checkVerifyCode(formId)){
		verifySuccess.addClass("success_tip");
		return true;
	}else{
		return false;
	}
}

function recoverCode(){
	var ipt = $("input[name='code']");
	var p = ipt.parent().parent().parent().find("p");
	p.removeClass();
	p.addClass("grayd prompt");
	p.text("邀请码 /推广人手机号码"+(has_business=="true"?"/ 业务员工号":"")+"");	
}

function codeCheck() {
	var ipt = $("input[name='code']");
	var val = $.trim(ipt.val());
	var codeSuccess = $("#codeSuccess");
	codeSuccess.removeClass("success_tip");
	var p = ipt.parent().parent().parent().find("p");
	if(val ==""){
		codeSuccess.addClass("success_tip");
		p.addClass("grayd prompt");
		//p.text("邀请码 /推广人手机号码"+(has_business=="true"?"/ 业务员工号":"")+"");
		p.text("");
		return true;
	}
	
	p.removeClass();
	p.addClass("grayd prompt");
	if (!codeVal.test(val)) {
		p.addClass("red");
		p.removeClass("grayd");
		p.text("您输入的邀请码/推广人手机号码"+(has_business=="true"?"/业务员工号":"")+"格式不正确");
		return false;
	}
   var codeCheck=true;
	$.ajax({
		type:"post",
		dataType:"html",
		url:_checkRecodURL,
		data:{"code":val},
		async: false,
		success:function(data) {
			if ($.trim(data) == '-1') {
				p.addClass("red");
				p.removeClass("grayd");
				p.text("邀请码/推广人手机号码"+(has_business=="true"?"/业务员工号":"")+"不存在，请重新输入");
				codeCheck = false;
			}else if($.trim(data) == '-2'){
				p.addClass("red");
				p.removeClass("grayd");
				p.text("邀请码/推广人手机号码"+(has_business=="true"?"/业务员工号":"")+"不属于个人用户，请重新输入");
				codeCheck = false;
			} else {
				codeSuccess.addClass("success_tip");
				p.addClass("grayd prompt");
				//p.text("邀请码 /推广人手机号码"+(has_business=="true"?"/ 业务员工号":"")+"");
				p.text("");
			}
		}
	});

	return codeCheck;
}

function numCheck() {
	var ipt = $("input[name='employNum']");
	var val = ipt.val();
	if(val ==""){
		return true;
	}
	var codeSuccess = $("#numSuccess");
	codeSuccess.removeClass("success_tip");
	var p = ipt.parent().parent().parent().find("p");
	p.removeClass();
	if (!numVal.test(val)) {
		p.addClass("red prompt");
		p.removeClass("grayd");
		p.text("您输入的业务员工号格式不正确");
		return false;
	}
   var codeCheck=true;
	$.ajax({
		type:"post",
		dataType:"html",
		url:_checkNumURL,
		data:{"employNum":val},
		async: false,
		success:function(data) {
			if ($.trim(data) == '-1') {
				p.addClass("red prompt");
				p.removeClass("grayd");
				p.text("业务员工号不存在，请重新输入");
				codeCheck = false;
			}else {
				codeSuccess.addClass("success_tip");
				p.addClass("grayd prompt");
			}
		}
	});

	return codeCheck;
}
function anotherImg(contextPath,formId) {
	$("#"+formId+" .verifyImg").attr("src", contextPath);
}

function showAuto() {
	var checked = $("#type2").attr("checked");
	if (checked) {
		$("#autoPayDiv").show();
	} else {
		$("#autoPayDiv").hide();
	}
}

function delErroInfo(obj){
	var ipt = $(obj);
	var p = ipt.parent().parent().next();
	p.removeClass();
	p.addClass("grayd prompt");
	p.text("请填写图片中的字符");
}