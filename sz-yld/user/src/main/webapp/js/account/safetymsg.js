$(function(){
    $("input[name='bphoneCode']").next().removeAttr("disabled");
});
/*--绑定邮箱时--*/
var wait = 60;
/*--新邮箱时--*/
var wait1 = 60;
/*--手机修改邮箱时--*/
var wait2 = 60;
/*--绑定手机时--*/
var wait3 = 60;
/*--新手机时--*/
var wait4 = 60;
/*--找回交易密码时--*/
var wait5 = 60;
/*--修改邮箱时--*/
var wait6 = 60;
/*--修改手机时--*/
var wait7 = 60;
/*--找回密保时--*/
var wait8 = 60;

var key = RSAUtils.getKeyPair(exponent, '', modulus);
var myregCode = /^[a-zA-Z0-9]+$/;//验证码

//绑定邮箱获取邮箱验证码
function submitBandEmil() {
    var bemil = $("input[name='bemil']");
    if (!checkEmailFormat(bemil)) {
        return false;
    }

    var bindEmailVerify;
    var bindEmailVerify1;
    if (!(flagSfxyyzm != "undefined" && flagSfxyyzm == false)) {
        bindEmailVerify1 = $("input[name='bindEmailVerify']");
        bindEmailVerify = bindEmailVerify1.val();
        if (bindEmailVerify.length <= 0) {
            showError(bindEmailVerify1, "不能为空！");
            return false;
        }
        if (!myregCode.test(bindEmailVerify)) {
            showError(bindEmailVerify1, "请输入正确的验证码！");
            return false;
        }
    }
    var tokenValue = $("input[name='"+_tokenName+"']").val();
    var data3 = {
        "tokenKey": tokenValue,
        "type": "bind",
        "emil": bemil.val(),
        "phone": "",
        "verifyType": "BIND_EMAIL",
        "verifyCode": bindEmailVerify
    };
    $.ajax({
        type: "post",
        dataType: "html",
        url: _sendUrl,
        data: data3,
        success: function (data) {
            if (data) {
                var ct = eval('(' + data + ')');
                //替换token
                setToken(ct);
                if (ct.length > 0 && ct[0].num != 1) {
                    flushVerifyCode(bindEmailVerify1);
                    if (ct[0].num == 0) {
                        showError(bemil, ct[0].msg);
                        return false;
                    }else if (ct[0].num == 2) {
                        showError(bindEmailVerify1, ct[0].msg);
                        return false;
                    }else if (ct[0].num == 4) {
                        showError(bemil, ct[0].msg);
                        return false;
                    }else if (ct[0].num == 101) {
                    	window.location.href=loginUrl;
                    	return false;
                    }
                }
                if (ct.length > 0 && ct[0].num == 1) {
                    $("#emailSendSuccess1").show();
                    $("#emailSendSuccess1Email").text(bemil.val());
                    $("#emailSend1").hide();
                }
            }
        }
    });
}

function checkEmailFormat(bemil) {
    if (bemil.val().length <= 0) {
        showError(bemil, "请输入邮箱地址！");
        return false;
    }
    /* var myreg = /^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,4}$/; */
    var myreg = /^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/;
    if (!myreg.test(bemil.val())) {
        showError(bemil, "邮箱地址不正确！");
        return false;
    }
    return true;
}


//重新加载页面
function reload() {
    setTimeout(function () {
        window.location.reload(true);
    }, 3000);
}

//获取手机验证码 绑定手机
function bandPhone(evn) {
    var bemil = $("input[name='binphpne']");
    if (!checkBandPhone(bemil)) {
        return false;
    }
    var bindPhoneVerify;
    var bindPhoneVerify1;
    if (!(flagSfxyyzm != "undefined" && flagSfxyyzm == false)) {
        bindPhoneVerify1 = $("input[name='bindPhoneVerify']");
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
    var tokenValue = $("input[name='"+_tokenName+"']").val();
    var data3 = {
        "tokenKey": tokenValue,
        "type": "bind",
        "emil": "",
        "phone": bemil.val(),
        "verifyType": "BIND_PHONE",
        "verifyCode": bindPhoneVerify
    };
    $.ajax({
        type: "post",
        dataType: "html",
        url: _sendUrl,
        data: data3,
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
                	window.location.href=_loginUrl;
                	return false;
                }
            }
            if (ct.length > 0 && ct[0].num == 1) {
                sendclick3(evn);
            }
        }
    });
}

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

function submitBandPhone(evn) {
    var bindPhone = $("input[name='binphpne']");
    var bphoneCode = $("input[name='bphoneCode']");
    if (!checkBandPhone(bindPhone)) {
        return false;
    }
    if (bphoneCode.val().length <= 0) {
        showError(bphoneCode, "验证码不能为空！");
        return false;
    }
    if (!myregCode.test(bphoneCode.val())) {
        showError(bphoneCode, "请输入正确的验证码！");
        return false;
    }
    var data3 = {"bphoneCode": bphoneCode.val(), "binphpne": bindPhone.val()};
    $.ajax({
        type: "post",
        dataType: "html",
        url: _bindPhoneUrl,
        data: data3,
        success: function (data) {
            var ct = eval('(' + data + ')');
            if (ct.length > 0 && ct[0].num == 0) {
                showError(bphoneCode, ct[0].msg);
                return false;
            }
            if (ct.length > 0 && ct[0].num == 1) {
                $("#sbPhoneLi").show();
                hideError(bindPhone);
                hideError(bphoneCode);
                reload();
            }
        }
    });
}

//异步的修改交易密码提交
function updatetxPassword() {
    if (!flgs) {
        return;
    }
    var oldPsw = $("div#box6 input[name='old']");
    var newPsw = $("div#box6 input[name='new']");
    var cnewPsw = $("div#box6 input[name='cnew']");
    if (oldPsw.val().length <= 0) {
        showError(oldPsw, "不能为空！");
        return false;
    }
    if (newPsw.val().length <= 0) {
        showError(newPsw, "不能为空！");
        return false;
    }

    if(newPsw.val()==userName){
    	showError(newPsw, "密码不能与用户名一致！");
        return false;
    }

    var myreg = txPasswordRegex;///^[a-zA-Z](?![a-zA-Z]*$)[a-zA-Z0-9]{7}$/;
    if (!myreg.test(newPsw.val())) {
        showError(newPsw, txErrorPwdMsg);
        return false;
    }

    if (cnewPsw.val().length <= 0) {
        showError(cnewPsw, "不能为空！");
        return false;
    }
    if (oldPsw.val() == newPsw.val()) {
        showError(cnewPsw, "新交易密码不能与原交易密码相同！");
        return false;
    }
    var data = {
        "old": RSAUtils.encryptedString(key, oldPsw.val()),
        "new": RSAUtils.encryptedString(key, newPsw.val()),
        "cnewPsw": RSAUtils.encryptedString(key, cnewPsw.val())
    };
    $.ajax({
        type: "post",
        dataType: "html",
        url: _updateTxPassword,
        data: data,
        success: function (data) {
            if (data) {
                var ct = eval('(' + data + ')');
                if (ct.length > 0 && ct[0].num == 2) {
                    showError(oldPsw, ct[0].msg);
                    return false;
                }
                if (ct.length > 0 && ct[0].num == 3) {
                    showError(newPsw, ct[0].msg);
                    return false;
                }
                if (ct.length > 0 && ct[0].num == 4) {
                    showError(cnewPsw, ct[0].msg);
                    return false;
                }
                if (ct.length > 0 && ct[0].num == 1) {
                    $("#sbUpdPswLi").show();
                    hideError(oldPsw);
                    hideError(newPsw);
                    hideError(cnewPsw);
                    reload();
                }
            }
        }
    });
}

//异步的设置交易密码提交
function settxPassword() {
    if (!flgs) {
        return;
    }
    var newPsw = $("div#box5 input[name='new']");
    var cnewPsw = $("div#box5 input[name='cnew']");
    if (newPsw.val().length <= 0) {
        showError(newPsw, "不能为空！");
        return false;
    }

    var myreg = txPasswordRegex;///^[a-zA-Z](?![a-zA-Z]*$)[a-zA-Z0-9]{7}$/;
    if (!myreg.test(newPsw.val())) {
        showError(newPsw, txErrorPwdMsg);
        return false;
    }
	
    if(newPsw.val()==userName){
    	showError(newPsw, "密码不能与用户名一致！");
        return false;
    }
    if (cnewPsw.val().length <= 0) {
        showError(cnewPsw, "不能为空！");
        return false;
    }
    var data = {
        "new": RSAUtils.encryptedString(key, newPsw.val()),
        "cnewPsw": RSAUtils.encryptedString(key, cnewPsw.val())
    };
    $.ajax({
        type: "post",
        dataType: "html",
        url: _setTxPassword,
        data: data,
        success: function (data) {
            if (data) {
                var ct = eval('(' + data + ')');
                if (ct.length > 0 && ct[0].num == 2) {
                    showError(newPsw, ct[0].msg);
                    return false;
                }
                if (ct.length > 0 && ct[0].num == 3) {
                    showError(cnewPsw, ct[0].msg);
                    return false;
                }
                if (ct.length > 0 && ct[0].num == 1) {
                    $("#sbSetPswLi").show();
                    hideError(newPsw);
                    hideError(cnewPsw);
                    reload();
                }
            }
        }
    });
}

//异步的实名认证提交
function updateName(evn) {
    var name = $("input[name='name']");
    var idCard = $("input[name='idCard']");
    if (name.val().length <= 0) {
        showError(name, "不能为空！");
        return false;
    }

    $err = $(idCard).nextAll("p[errortip]");
    if(idCard.val().length<=0){
    	showError(idCard,"不能为空！");
    	return false;
    }
    if (!isIdCardNot(idCard.val(), $err)) {
        $tip = $(idCard).nextAll("p[tip]");
        $tip.hide();
        $err.show();
        return false;
    }
    var data = {"idCard": idCard.val(), "name": name.val()};
    $.ajax({
        type: "post",
        dataType: "html",
        url: _updateName,
        data: data,
        success: function (data) {
            if (data) {
                var ct = eval('(' + data + ')');
                if (ct.length > 0 && ct[0].num == 3) {
                    showError(name, ct[0].msg);
                    return false;
                }
                if (ct.length > 0 && ct[0].num == 0) {
                    showError(idCard, ct[0].msg);
                    return false;
                }
                if (ct.length > 0 && ct[0].num == 1) {
                    $("#sbNameLi").show();
                    hideError(name);
                    hideError(idCard);
                    reload();
                }
            }
        }
    });
}

//异步的修改登录密码提交
function updatePassword(username) {
    var oldPsw = $("input[name='old']");
    var newPsw = $("input[name='new']");
    var confPsw = $("input[name='news']");
    if (oldPsw.val().length <= 0) {
        showError(oldPsw, "不能为空！");
        return false;
    }
    if (newPsw.val().length <= 0) {
        showError(newPsw, "不能为空！");
        return false;
    }
    if (newPsw.val() == username) {
        showError(newPsw, "密码不能与用户名一致！");
        return false;
    }
    if (confPsw.val().length <= 0) {
        showError(confPsw, "不能为空！");
        return false;
    }
    //var myregPsw = /^(?![a-zA-Z0-9]+$)(?![^a-zA-Z/D]+$)(?![^0-9/D]+$).{8,12}$/;
    if (!myregPsw.test(newPsw.val())) {
        showError(newPsw, myregPswMsg);
        return false;
    }

    var data = {
        "old": RSAUtils.encryptedString(key, oldPsw.val()),
        "new": RSAUtils.encryptedString(key, newPsw.val()),
        "news": RSAUtils.encryptedString(key, confPsw.val())
    };
    $.ajax({
        type: "post",
        dataType: "html",
        url: _updatePassword,
        data: data,
        success: function (data) {
            if (data) {
                var ct = eval('(' + data + ')');
                if (ct.length > 0 && ct[0].num == 3) {
                    showError(newPsw, ct[0].msg);
                    return false;
                }
                if (ct.length > 0 && ct[0].num == 2) {
                    showError(oldPsw, ct[0].msg);
                    return false;
                }
                if (ct.length > 0 && ct[0].num == 4) {
                    showError(confPsw, ct[0].msg);
                    return false;
                }
                if (ct.length > 0 && ct[0].num == 1) {
                    $("#sbPswLi").show();
                    hideError(oldPsw);
                    hideError(newPsw);
                    hideError(confPsw);
                    reload();
                }
            }
        }
    });
}

function hideError(obj) {
    $error = $(obj).nextAll("p[errortip]");
    $error.removeClass();
    $error.html("");
    //$error.hide();
}
function showError(obj, msg) {
    $error = $(obj).nextAll("p[errortip]");
    $tip = $(obj).nextAll("p[tip]");
    $error.addClass("error_tip");
    $error.html(msg);
    $tip.hide();
    $error.show();
}

//通过邮箱修改旧的邮箱
function eupdate(evn) {
    var eupdateVerify;
    var eupdateVerify1;
    if (!(flagSfxyyzm != "undefined" && flagSfxyyzm == false)) {
        eupdateVerify1 = $("input[name='eUpdateVerify']");
        eupdateVerify = eupdateVerify1.val();
        if (eupdateVerify.length <= 0) {
            showError(eupdateVerify1, "不能为空！");
            flushVerifyCode(eupdateVerify1);
            return false;
        }
        if (!myregCode.test(eupdateVerify)) {
        	showError(eupdateVerify1, "请输入正确的验证码！");
            flushVerifyCode(eupdateVerify1);
            return false;
        }
    }
    var tokenValue = $("input[name='"+_tokenName+"']").val();
    var data = {
        "tokenKey": tokenValue,
        "type": "update",
        "emil": "YES",
        "phone": "",
        "verifyCode": eupdateVerify,
        "verifyType": "UPDATE_EMAIL"
    };
    $.ajax({
        type: "post",
        dataType: "html",
        url: _sendUrl,
        data: data,
        success: function (data) {
            if (data) {
                var ct = eval('(' + data + ')');
                //替换token
                setToken(ct);
                if (ct.length > 0 && ct[0].num != 1) {
                    if (ct[0].num == 0) {
                        $error = $(evn).nextAll("p[errortip]");
                        $tip = $(evn).nextAll("p[tip]");
                    }else if (ct[0].num == 2) {
                        $error = eupdateVerify1.nextAll("p[errortip]");
                        $tip = eupdateVerify1.nextAll("p[tip]");
                    }else if (ct[0].num == 101) {
                    	window.location.href=_loginUrl;
                    	return false;
                    }
                    flushVerifyCode(eupdateVerify1);
                    $error.addClass("error_tip");
                    $error.html(ct[0].msg);
                    $tip.hide();
                    $error.show();
                    return false;
                }
                if (ct.length > 0 && ct[0].num == 1) {
                    sendclick6(evn);
                }
            }
        }
    });
}

//通过手机修改旧的手机,获取验证码
function pupdate(evn) {
    var phoneUpdateVerify;
    var phoneUpdateVerify1;
    if (!(flagSfxyyzm != "undefined" && flagSfxyyzm == false)) {
        phoneUpdateVerify1 = $("input[name='phoneUpdateVerify']");
        phoneUpdateVerify = phoneUpdateVerify1.val();
        if (phoneUpdateVerify.length <= 0) {
            showError(phoneUpdateVerify1, "不能为空！");
            flushVerifyCode(phoneUpdateVerify1);
            return false;
        }
        if (!myregCode.test(phoneUpdateVerify)) {
        	showError(phoneUpdateVerify1, "不能为空！");
            flushVerifyCode(phoneUpdateVerify1);
            return false;
        }
    }
    var tokenValue = $("input[name='"+_tokenName+"']").val();
    var data = {
        "tokenKey": tokenValue,
        "type": "update",
        "emil": "",
        "phone": phoneNumber,
        "verifyCode": phoneUpdateVerify,
        "verifyType": "UPDATE_PHONE"
    };
    $.ajax({
        type: "post",
        dataType: "html",
        url: _sendUrl,
        data: data,
        success: function (data) {
            if (data) {
                var ct = eval('(' + data + ')');
                //替换token
                setToken(ct);
                if (ct.length > 0 && ct[0].num != 1) {
                    if (ct[0].num == 0) {
                        $error = $(evn).nextAll("p[errortip]");
                        $tip = $(evn).nextAll("p[tip]");
                    }else if (ct[0].num == 2) {
                        $error = phoneUpdateVerify1.nextAll("p[errortip]");
                        $tip = phoneUpdateVerify1.nextAll("p[tip]");
                    }else if (ct[0].num == 101) {
                    	window.location.href=_loginUrl;
                    	return false;
                    }
                    flushVerifyCode(phoneUpdateVerify1);
                    $error.addClass("error_tip");
                    $error.html(ct[0].msg);
                    $tip.hide();
                    $error.show();
                    return false;
                }
                if (ct.length > 0 && ct[0].num == 1) {
                    sendclick7(evn);
                }
            }
        }
    });
}

//通过手机找回交易密码，获取验证码
function getoldcode(evn) {
    var updatePasVerify;
    var updatePasVerify1;
    if (!(flagSfxyyzm != "undefined" && flagSfxyyzm == false)) {
        updatePasVerify1 = $("input[name='updatePasVerify']");
        updatePasVerify = updatePasVerify1.val();
        if (updatePasVerify.length <= 0) {
            showError(updatePasVerify1, "不能为空！");
            flushVerifyCode(updatePasVerify1);
            return false;
        }
        if (!myregCode.test(updatePasVerify)) {
        	showError(updatePasVerify1, "请输入正确的验证码！");
            flushVerifyCode(updatePasVerify1);
            return false;
        }
    }
    var tokenValue = $("input[name='"+_tokenName+"']").val();
    var data = {
        "tokenKey": tokenValue,
        "type": "getoldpas",
        "emil": "",
        "phone": phoneNumber,
        "verifyCode": updatePasVerify,
        "verifyType": "UPDATE_PASW"
    };
    $.ajax({
        type: "post",
        dataType: "html",
        url: _sendUrl,
        data: data,
        success: function (data) {
            if (data) {
                var ct = eval('(' + data + ')');
                //替换token
                setToken(ct);
                if (ct.length > 0 && ct[0].num != 1) {
                    if (ct[0].num == 0) {
                        $error = $(evn).nextAll("p[errortip]");
                        $tip = $(evn).nextAll("p[tip]");
                    }else if (ct[0].num == 2) {
                        $error = updatePasVerify1.nextAll("p[errortip]");
                        $tip = updatePasVerify1.nextAll("p[tip]");
                    }else if (ct[0].num == 101) {
                    	window.location.href=_loginUrl;
                    	return false;
                    }
                    flushVerifyCode(updatePasVerify1);
                    $error.addClass("error_tip");
                    $error.html(ct[0].msg);
                    $tip.hide();
                    $error.show();
                    return false;
                }
                if (ct.length > 0 && ct[0].num == 1) {
                    sendclick5(evn);
                }
            }
        }
    });
}

//通过手机修改旧的邮箱
function epupdate(evn) {
    var epUpdateVerify;
    var epUpdateVerify1;
    if (!(flagSfxyyzm != "undefined" && flagSfxyyzm == false)) {
        epUpdateVerify1 = $("input[name='epUpdateEmailVerify']");
        epUpdateVerify = epUpdateVerify1.val();
        if (epUpdateVerify.length <= 0) {
            showError(epUpdateVerify1, "不能为空！");
            flushVerifyCode(epUpdateVerify1);
            return false;
        }
        if (!myregCode.test(epUpdateVerify)) {
        	showError(epUpdateVerify1, "请输入正确的验证码！");
            flushVerifyCode(epUpdateVerify1);
            return false;
        }
    }
    var tokenValue = $("input[name='"+_tokenName+"']").val();
    var data = {
        "tokenKey": tokenValue,
        "type": "phoneemil",
        "emil": "",
        "phone": phoneNumber,
        "verifyType": "EP_UPDATE_EMAIL",
        "verifyCode": epUpdateVerify
    };
    $.ajax({
        type: "post",
        dataType: "html",
        url: _sendUrl,
        data: data,
        success: function (data) {
            if (data) {
                var ct = eval('(' + data + ')');
                //替换token
                setToken(ct);
                if (ct.length > 0 && ct[0].num != 1) {
                    if (ct[0].num == 0) {
                        $error = $(evn).nextAll("p[errortip]");
                        $tip = $(evn).nextAll("p[tip]");
                    }else if (ct[0].num == 2) {
                        $error = epUpdateVerify1.nextAll("p[errortip]");
                        $tip = epUpdateVerify1.nextAll("p[tip]");
                    }else if (ct[0].num == 101) {
                    	window.location.href=_loginUrl;
                    	return false;
                    }
                    $error.addClass("error_tip");
                    flushVerifyCode(epUpdateVerify1);
                    $error.html(ct[0].msg);
                    $tip.hide();
                    $error.show();
                    return false;
                }
                if (ct.length > 0 && ct[0].num == 1) {
                    sendclick2(evn);
                }
            }
        }
    });
}
/*通过手机验证码找回密保问题*/
function getSecurityCode(evn){
	var epUpdateVerify;
    var epUpdateVerify1;
    if (!(flagSfxyyzm != "undefined" && flagSfxyyzm == false)) {
        epUpdateVerify1 = $("input[name='updateSecurityVerify']");
        epUpdateVerify = epUpdateVerify1.val();
        if (epUpdateVerify.length <= 0) {
            showError(epUpdateVerify1, "不能为空！");
            flushVerifyCode(epUpdateVerify1);
            return false;
        }
        if (!myregCode.test(epUpdateVerify)) {
        	showError(epUpdateVerify1, "请输入正确的验证码！");
            flushVerifyCode(epUpdateVerify1);
            return false;
        }
    }
    var tokenValue = $("input[name='"+_tokenName+"']").val();
    var data = {
        "tokenKey": tokenValue,
        "type": "securitypwd",
        "emil": "",
        "phone": phoneNumber,
        "verifyType": "UPDATE_SECURITY",
        "verifyCode": epUpdateVerify
    };
    $.ajax({
        type: "post",
        dataType: "html",
        url: _sendUrl,
        data: data,
        success: function (data) {
            if (data) {
                var ct = eval('(' + data + ')');
                //替换token
                setToken(ct);
                if (ct.length > 0 && ct[0].num != 1) {
                    if (ct[0].num == 0) {
                        $error = $(evn).nextAll("p[errortip]");
                        $tip = $(evn).nextAll("p[tip]");
                    }else if (ct[0].num == 2) {
                        $error = epUpdateVerify1.nextAll("p[errortip]");
                        $tip = epUpdateVerify1.nextAll("p[tip]");
                    }else if (ct[0].num == 101) {
                    	window.location.href=_loginUrl;
                    	return false;
                    }
                    $error.addClass("error_tip");
                    flushVerifyCode(epUpdateVerify1);
                    $error.html(ct[0].msg);
                    $tip.hide();
                    $error.show();
                    return false;
                }
                if (ct.length > 0 && ct[0].num == 1) {
                    sendclick8(evn);
                }
            }
        }
    });
}

$(".w111").click(function () {
    var ind = $(this).index(".w111");
    /*--通过邮箱修改邮箱--*/
    if (ind == 0) {
        $("#mt1").hide();
        $("#e1").show();
    }
    /*--通过手机号修改邮箱--*/
    if (ind == 1) {
        if ("BTG" == isPhone) {
            /* $("#mt1").hide();
             $("#notBindPhone_div").show(); */
            $("#info").html(showDialogInfo("请先绑定手机。", "doubt"));
            $("div.popup_bg").show();
            return false;
        } else {
            $("#mt1").hide();
            $("#ep1").show();
        }
    }
    /*--通过手机号修改绑定手机--*/
    if (ind == 2) {
        $("#mt2").hide();
        $("#p1").show();
    }
    /*--通过身份证修改绑定手机--*/
    if (ind == 3) {
    	var txp = false;
    	if(isIdCardEmpty==true || isIdCard==false)
    	{
    		txp = true;
    	}
        if (txp) {
            $("#info").html(showDialogInfo("请先设置实名认证。", "doubt"));
            $("div.popup_bg").show();
            return false;
        } 
        if(chargeMustWithdrawPwd==true && isTxPasswordEmpty==true){
        	txp = true;
        }
        if (txp) {
            $("#info").html(showDialogInfo("请先设置交易密码。", "doubt"));
            $("div.popup_bg").show();
            return false;
        } 
        $("#mt2").hide();
        $("#ip1").show();
    }
});

//刷新验证码
function flushVerifyCode(obj){
    if (!(flagSfxyyzm != "undefined" && flagSfxyyzm == false)) {
        obj.next("span").children("img").click();
    }
}

$(function () {
    $(".coden").click(function () {
        $intext = $('input[type="text"]');
        $inpassword = $('input[type="password"]');
        var type = $(this).attr("checktype");
        flgs = true;
        if (type == "oldone") {
            /*--通过旧邮箱修改邮箱 1--*/
            var emailValue = $("#oldEmailCheck");
            if (!checkEmailFormat(emailValue)) {
                return false;
            }
            var eNewVerify;
            var eNewVerify1;
            if (!(flagSfxyyzm != "undefined" && flagSfxyyzm == false)) {
                eNewVerify1 = $("input[name='eUpdateVerify']");
                eNewVerify = eNewVerify1.val();
                if (eNewVerify.length <= 0) {
                    showError(eNewVerify1, "验证码不能为空！");
                    flushVerifyCode(eNewVerify1);
                    return false;
                }
                if (!myregCode.test(eNewVerify)) {
                	showError(eNewVerify1, "请输入正确的验证码！");
                    flushVerifyCode(eNewVerify1);
                    return false;
                }
            }
            var data = {"email": emailValue.val(), "verifyType": "UPDATE_EMAIL", "verifyCode": eNewVerify,"ctp":"emil_emil"};
            $.ajax({
                type: "post",
                dataType: "html",
                url: _checkEmailUrl,
                data: data,
                success: function (data) {
                    if (data) {
                        var ct = eval('(' + data + ')');
                        if (ct.length > 0 && ct[0].num == 0) {
                            showError(emailValue, ct[0].msg);
                            flushVerifyCode(eNewVerify1);
                            return false;
                        }
                        if (ct.length > 0 && ct[0].num == 2) {
                            showError(eNewVerify1, ct[0].msg);
                            flushVerifyCode(eNewVerify1);
                            return false;
                        }
                        if (ct.length > 0 && ct[0].num == 1) {
                            $("#emilFlg").val(2);
                            $("#e1").hide();
                            $("#e2").show();
                        }
                    }
                }
            });
        }
        if (type == "epoldone") {
            /*--通过手机修改邮箱 1--*/
            var $cli = $(this).parent().prev().children("li").eq(2);
            var code = $cli.children("div").eq(1).children("input").eq(0);
            var eNewVerify;
            var eNewVerify1;
            if (!(flagSfxyyzm != "undefined" && flagSfxyyzm == false)) {
                eNewVerify1 = $("input[name='epUpdateEmailVerify']");
                eNewVerify = eNewVerify1.val();
                if (eNewVerify.length <= 0) {
                    showError(eNewVerify1, "验证码不能为空！");
                    flushVerifyCode(eNewVerify1);
                    return false;
                }
                if (!myregCode.test(eNewVerify)) {
                	showError(eNewVerify1, "请输入正确的验证码！");
                    flushVerifyCode(eNewVerify1);
                    return false;
                }
            }
            var phoneVal = phoneNumber;
            if (code.val().length <= 0) {
                $error = code.nextAll("p[errortip]");
                $tip = code.nextAll("p[tip]");
                $error.addClass("error_tip");
                $error.html("请输入手机验证码！");
                $tip.hide();
                $error.show();
                return false;
            }
            if (!myregCode.test(code.val())) {
            	$error = code.nextAll("p[errortip]");
                $tip = code.nextAll("p[tip]");
                $error.addClass("error_tip");
                $error.html("请输入正确的验证码！");
                $tip.hide();
                $error.show();
                return false;
            }
            var data = {"evencheck": "phone", "ctp": "phoneemil", "code": code.val(), "realVal": phoneVal};
            $.ajax({
                type: "post",
                dataType: "html",
                url: _checkCodeUrl,
                data: data,
                success: function (data) {
                    if (data == "01") {
                        $error = code.nextAll("p[errortip]");
                        $tip = code.nextAll("p[tip]");
                        $error.addClass("error_tip");
                        $error.html("输入手机号！");
                        $tip.hide();
                        $error.show();
                        return false;
                    } else if (data == "02") {
                        $error = code.nextAll("p[errortip]");
                        $tip = code.nextAll("p[tip]");
                        $error.addClass("error_tip");
                        $error.html("输入验证码有误！");
                        $tip.hide();
                        $error.show();
                        return false;
                    } else if (data == "03") {
                        $error = code.nextAll("p[errortip]");
                        $tip = code.nextAll("p[tip]");
                        $error.addClass("error_tip");
                        $error.html("请输入正确的验证码！");
                        $tip.hide();
                        $error.show();
                        return false;
                    } else if (data == "04") {
                        $error = code.nextAll("p[errortip]");
                        $tip = code.nextAll("p[tip]");
                        $error.addClass("error_tip");
                        $error.html("此手机当天匹配验证码错误次数已达上限！");
                        $tip.hide();
                        $error.show();
                        return false;
                    } else {
                        $("#emilFlg").val(2);
                        $("#ep1").hide();
                        $("#ep2").show();
                    }
                }
            });
        }
        if (type == "poldone") {
            /*--通过短信修改手机号 1--*/
            clearTimes(4);
            var $cli = $(this).parent().prev().children("li").eq(2);
            var code = $cli.children("div").eq(1).children("input").eq(0);
            var realVal = phoneNumber;
            if (code.val().length <= 0) {
                $error = code.nextAll("p[errortip]");
                $tip = code.nextAll("p[tip]");
                $error.addClass("error_tip");
                $error.html("请输入验证码！");
                $tip.hide();
                $error.show();
                return false;
            }
            if (!myregCode.test(code.val())) {
            	$error = code.nextAll("p[errortip]");
                $tip = code.nextAll("p[tip]");
                $error.addClass("error_tip");
                $error.html("请输入正确的验证码！");
                $tip.hide();
                $error.show();
                return false;
            }
            var data = {"evencheck": "phone", "ctp": "update", "code": code.val(), "realVal": realVal};
            $.ajax({
                type: "post",
                dataType: "html",
                url: _checkCodeUrl,
                data: data,
                success: function (data) {
                    if (data == "01") {
                        $error = code.nextAll("p[errortip]");
                        $tip = code.nextAll("p[tip]");
                        $error.addClass("error_tip");
                        $error.html("输入手机号！");
                        $tip.hide();
                        $error.show();
                        return false;
                    } else if (data == "02") {
                        $error = code.nextAll("p[errortip]");
                        $tip = code.nextAll("p[tip]");
                        $error.addClass("error_tip");
                        $error.html("输入验证码有误！");
                        $tip.hide();
                        $error.show();
                        return false;
                    } else if (data == "03") {
                        $error = code.nextAll("p[errortip]");
                        $tip = code.nextAll("p[tip]");
                        $error.addClass("error_tip");
                        $error.html("请输入正确的验证码！");
                        $tip.hide();
                        $error.show();
                        return false;
                    } else if (data == "04") {
                        $error = code.nextAll("p[errortip]");
                        $tip = code.nextAll("p[tip]");
                        $error.addClass("error_tip");
                        $error.html("当前手机号码当天匹配验证码错误次数已达上限！");
                        $tip.hide();
                        $error.show();
                        return false;
                    } else {
                        $("#phoneFlg").val(2);
                        $("#p1").hide();
                        $("#p2").show();
                    }
                }
            });

        }
        if (type == "pnewtwo") {
            /*--通过短信修改手机号 2--*/
            var $cli = $(this).parent().prev().children("li").eq(2);
            var $eli = $(this).parent().prev().children("li").eq(0);
            var emil = $eli.children("div").eq(1).children("input").eq(0);
            var code = $cli.children("div").eq(1).children("input").eq(0);
            var oldVal = phoneNumber;
            var phoneFlgCode = $("input[name='phoneFlg']").val();
            if (phoneFlgCode != 2) {
                return false;
            }
            var myreg = /^(13|14|15|17|18)[0-9]{9}$/;
            if($.trim(emil.val()) == ""){
				$error = emil.nextAll("p[errortip]");
                $tip = emil.nextAll("p[tip]");
                $error.addClass("error_tip");
                $error.html("不能为空!");
                $tip.hide();
                $error.show();
                return false;
			}
            if (!myreg.test(emil.val())) {
                $error = emil.nextAll("p[errortip]");
                $tip = emil.nextAll("p[tip]");
                $error.addClass("error_tip");
                $error.html("手机号码不正确!");
                $tip.hide();
                $error.show();
                return false;
            }
            if (code.val().length <= 0) {
                $error = code.nextAll("p[errortip]");
                $tip = code.nextAll("p[tip]");
                $error.addClass("error_tip");
                $error.html("请输入验证码！");
                $tip.hide();
                $error.show();
                return false;
            }
            if (!myregCode.test(code.val())) {
            	$error = code.nextAll("p[errortip]");
                $tip = code.nextAll("p[tip]");
                $error.addClass("error_tip");
                $error.html("请输入正确的验证码！");
                $tip.hide();
                $error.show();
                return false;
            }
            var data = {
                "evencheck": "phone",
                "utp": "update",
                "ctp": "new",
                "value": emil.val(),
                "code": code.val(),
                "oldVal": oldVal
            };
            $.ajax({
                type: "post",
                dataType: "html",
                url: _checkNewCodeUrl,
                data: data,
                success: function (data) {
                    if (data == "00") {
                    	$error = code.nextAll("p[errortip]");
                        $tip = code.nextAll("p[tip]");
                        $error.addClass("error_tip");
                        $error.html("请先进行第一步操作！");
                        $tip.hide();
                        $error.show();
                        return false;
                    }
                    if (data == "01") {
                        $error = code.nextAll("p[errortip]");
                        $tip = code.nextAll("p[tip]");
                        $error.addClass("error_tip");
                        $error.html("输入手机号有误！");
                        $tip.hide();
                        $error.show();
                        return false;
                    } else if (data == "02") {
                        $error = code.nextAll("p[errortip]");
                        $tip = code.nextAll("p[tip]");
                        $error.addClass("error_tip");
                        $error.html("输入验证码有误！");
                        $tip.hide();
                        $error.show();
                        return false;
                    } else if (data == "03") {
                        $error = code.nextAll("p[errortip]");
                        $tip = code.nextAll("p[tip]");
                        $error.addClass("error_tip");
                        $error.html("请输入正确的验证码！");
                        $tip.hide();
                        $error.show();
                        return false;
                    } else if (data == "04") {
                        $error = code.nextAll("p[errortip]");
                        $tip = code.nextAll("p[tip]");
                        $error.addClass("error_tip");
                        $error.html("手机号码已存在！");
                        $tip.hide();
                        $error.show();
                        return false;
                    } else if (data == "05") {
                        $error = code.nextAll("p[errortip]");
                        $tip = code.nextAll("p[tip]");
                        $error.addClass("error_tip");
                        $error.html("当前手机号码当天匹配验证码错误次数已达上限！");
                        $tip.hide();
                        $error.show();
                        return false;
                    } else {
                        $("#p2").hide();
                        $("#p3").show();
                        $("#p3").find("p").eq(1).html(emil.val().substring(0, 3) + "*****" + emil.val().substring(7, 11));
                        reload();
                    }
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                    alert(textStatus);
                }
            });
        }
        if (type == "ipoldone") {
            /*--通过身份证修改手机号 1 --*/
            clearTimes(4);
            if (!isIdCard) {
                /* $("#ip1").hide();
                 $("#pr2").show(); */
                $("#info").html(showDialogInfo("请先设置实名认证。", "doubt"));
                $("div.popup_bg").show();
                return false;
            }
            if(chargeMustWithdrawPwd==true){
            	 var t = true;
            	 if(isTxPasswordEmpty){
            		 t = false;
            	 }
            	 if (!t) {
                     //$("#ip1").hide();
                     //$("#pr1").show();
                     $("#info").html(showDialogInfo("请先设置交易密码。", "doubt"));
                     $("div.popup_bg").show();
                     return false;
                 }
            }
            var $tli = $(this).parent().prev().children("li").eq(2);
            var $ili = $(this).parent().prev().children("li").eq(1);
            var idcard = $ili.children("div").eq(1).children("input").eq(0).val();
            var idcarderror = $ili.children("div").eq(1).children("input").eq(0);
            var txpassword = $tli.children("div").eq(1).children("input").eq(0);
            if (!isIdCardNo(idcard)) {
                return false;
            }
            if(chargeMustWithdrawPwd==true){
                if (txpassword.val().length <= 0) {
                    $error = txpassword.nextAll("p[errortip]");
                    $tip = txpassword.nextAll("p[tip]");
                    $error.addClass("error_tip");
                    $error.html("请输入交易密码！");
                    $tip.hide();
                    $error.show();
                    return false;
                }
            }
            if (idcard.toLocaleLowerCase() != dataSfzh && idcard.toLocaleUpperCase() != dataSfzh) {
                $error = idcarderror.nextAll("p[errortip]");
                $tip = idcarderror.nextAll("p[tip]");
                $error.addClass("error_tip");
                $error.html("不是本人身份证！");
                $tip.hide();
                $error.show();
                return false;
            }
            var data = {
                "value": txpassword.val(),
                "ctp": "update", "cardValue": dataSfzh
            };
            $.ajax({
                type: "post",
                dataType: "html",
                url: _checkPasswordUrl,
                data: data,
                success: function (data) {
                    if (data == "01") {
                        $error = txpassword.nextAll("p[errortip]");
                        $tip = txpassword.nextAll("p[tip]");
                        $error.addClass("error_tip");
                        $error.html("交易密码输入错误！");
                        $tip.hide();
                        $error.show();
                        return false;
                    } else {
                        $("#phoneFlg").val(2);
                        $("#ip1").hide();
                        $("#ip2").show();
                    }
                }
            });
        }
        if (type == "ipnewtwo") {
            /*--通过身份证修改手机号 2 --*/
            var $cli = $(this).parent().prev().children("li").eq(2);
            var $eli = $(this).parent().prev().children("li").eq(0);
            var emil = $eli.children("div").eq(1).children("input").eq(0);
            var code = $cli.children("div").eq(1).children("input").eq(0);
            var myreg = /^(13|14|15|17|18)[0-9]{9}$/;
            var phoneFlgCode = $("input[name='phoneFlg']").val();
            if (phoneFlgCode != 2) {
                return false;
            }
            if (!myreg.test(emil.val())) {
                showError(emil, "手机号码不正确！");
                return false;
            }
            if (code.val().length <= 0) {
                $error = code.nextAll("p[errortip]");
                $tip = code.nextAll("p[tip]");
                $error.addClass("error_tip");
                $error.html("请输入手机验证码！");
                $tip.hide();
                $error.show();
                return false;
            }
            if (!myregCode.test(code.val())) {
            	$error = code.nextAll("p[errortip]");
                $tip = code.nextAll("p[tip]");
                $error.addClass("error_tip");
                $error.html("请输入正确的验证码！");
                $tip.hide();
                $error.show();
                return false;
            }
            var data = {
                "evencheck": "phone", "utp": "update", "ctp": "cnew", "value": emil.val(),
                "code": code.val(), "oldVal": dataSfzh
            };
            $.ajax({
                type: "post",
                dataType: "html",
                url: _checkNewCodeUrl,
                data: data,
                success: function (data) {
                    if (data == "00") {
                    	$error = code.nextAll("p[errortip]");
                        $tip = code.nextAll("p[tip]");
                        $error.addClass("error_tip");
                        $error.html("请先进行第一步操作！");
                        $tip.hide();
                        $error.show();
                    	return false;
                    }
                    if (data == "01") {
                        $error = code.nextAll("p[errortip]");
                        $tip = code.nextAll("p[tip]");
                        $error.addClass("error_tip");
                        $error.html("输入手机号有误！");
                        $tip.hide();
                        $error.show();
                        return false;
                    } else if (data == "02") {
                        $error = code.nextAll("p[errortip]");
                        $tip = code.nextAll("p[tip]");
                        $error.addClass("error_tip");
                        $error.html("输入验证码有误！");
                        $tip.hide();
                        $error.show();
                        return false;
                    } else if (data == "03") {
                        $error = code.nextAll("p[errortip]");
                        $tip = code.nextAll("p[tip]");
                        $error.addClass("error_tip");
                        $error.html("请输入正确的验证码！");
                        $tip.hide();
                        $error.show();
                        return false;
                    } else if (data == "04") {
                        $error = code.nextAll("p[errortip]");
                        $tip = code.nextAll("p[tip]");
                        $error.addClass("error_tip");
                        $error.html("手机号码已存在！");
                        $tip.hide();
                        $error.show();
                        return false;
                    } else if (data == "05") {
                        $error = code.nextAll("p[errortip]");
                        $tip = code.nextAll("p[tip]");
                        $error.addClass("error_tip");
                        $error.html("当前手机号码当天匹配验证码错误次数已达上限！");
                        $tip.hide();
                        $error.show();
                        return false;
                    } else {
                        $("#ip2").hide();
                        $("#ip3").show();
                        $("#ip3").find("p").eq(1).html(emil.val().substring(0, 3) + "*****" + emil.val().substring(7, 11));
                        reload();
                    }
                }
            });
        }
        if (type == "pswone") {
            /*--找回交易密码 1--*/
            var $cli = $(this).parent().prev().children("li").eq(2);
            var code = $cli.children("div").eq(1).children("input").eq(0);
            var realVal = phoneNumber;
            if (code.val().length <= 0) {
                $error = code.nextAll("p[errortip]");
                $tip = code.nextAll("p[tip]");
                $error.addClass("error_tip");
                $error.html("请输入手机验证码！");
                $tip.hide();
                $error.show();
                return false;
            }
            if (!myregCode.test(code.val())) {
            	$error = code.nextAll("p[errortip]");
                $tip = code.nextAll("p[tip]");
                $error.addClass("error_tip");
                $error.html("请输入正确的验证码！");
                $tip.hide();
                $error.show();
                return false;
            }

            var data = {"evencheck": "phone", "ctp": "getoldpas", "code": code.val(), "realVal": realVal};
            $.ajax({
                type: "post",
                dataType: "html",
                url: _checkCodeUrl,
                data: data,
                success: function (data) {
                    if (data == "01") {
                        $error = code.nextAll("p[errortip]");
                        $tip = code.nextAll("p[tip]");
                        $error.addClass("error_tip");
                        $error.html("输入手机号！");
                        $tip.hide();
                        $error.show();
                        return false;
                    } else if (data == "02") {
                        $error = code.nextAll("p[errortip]");
                        $tip = code.nextAll("p[tip]");
                        $error.addClass("error_tip");
                        $error.html("输入验证码有误！");
                        $tip.hide();
                        $error.show();
                        return false;
                    } else if (data == "03") {
                        $error = code.nextAll("p[errortip]");
                        $tip = code.nextAll("p[tip]");
                        $error.addClass("error_tip");
                        $error.html("请输入正确的验证码！");
                        $tip.hide();
                        $error.show();
                        return false;
                    } else if (data == "04") {
                        $error = code.nextAll("p[errortip]");
                        $tip = code.nextAll("p[tip]");
                        $error.addClass("error_tip");
                        $error.html("当前手机号码当天匹配验证码错误次数已达上限！");
                        $tip.hide();
                        $error.show();
                        return false;
                    } else {
                        $("#ps1").hide();
                        $("#ps2").show();
                    }
                }
            });
        }
        if (type == "pswtwo") {
            /*--找回交易密码 2--*/
            var $cli = $(this).parent().prev().children("li").eq(1);
            var $eli = $(this).parent().prev().children("li").eq(0);
            var code = $cli.children("div").eq(1).children("input").eq(0);
            var emil = $eli.children("div").eq(1).children("input").eq(0);

            //var myreg = /^[a-zA-Z](?![a-zA-Z]*$)[a-zA-Z0-9]{7}$/;
            if (emil.val().length <= 0) {
                $error = emil.nextAll("p[errortip]");
                $tip = emil.nextAll("p[tip]");
                $error.addClass("error_tip");
                $error.html("不能为空！");
                $tip.hide();
                $error.show();
                return false;
            } else if (!txPasswordRegex.test(emil.val())) {
                $error = emil.nextAll("p[errortip]");
                $tip = emil.nextAll("p[tip]");
                $error.addClass("error_tip");
                //$error.html("密码需由八个字符组成：字母+数字，且首个字符必须是字母");
                $error.html(txErrorPwdMsg);
                $tip.hide();
                $error.show();
                return false;
            } else if (emil.val()==userName) {
                $error = emil.nextAll("p[errortip]");
                $tip = emil.nextAll("p[tip]");
                $error.addClass("error_tip");
                $error.html("密码不能与用户名一致！");
                $tip.hide();
                $error.show();
                return false;
            }
            if (code.val() != emil.val()) {
                $error = code.nextAll("p[errortip]");
                $tip = code.nextAll("p[tip]");
                $error.addClass("error_tip");
                $error.html("两次输入密码不一致！");
                $tip.hide();
                $error.show();
                return false;
            }
            var data = {
                "evencheck": "phone",
                "utp": "getoldpas",
                "number": phoneNumber,
                "new": RSAUtils.encryptedString(key, emil.val())
            };
            $.ajax({
                type: "post",
                dataType: "html",
                url: _pdatetxPasswordajxUrl,
                data: data,
                success: function (data) {
                    if (data == "09") {
                        $("#ps1").show();
                        $("#ps2").hide();
                        return false;
                    } else if (data == "00") {
                        $error = code.nextAll("p[errortip]");
                        $tip = code.nextAll("p[tip]");
                        $error.addClass("error_tip");
                        $error.html("输入正确密码！");
                        $tip.hide();
                        $error.show();
                        return false;
                    } else if (data == "04") {
                        $error = code.nextAll("p[errortip]");
                        $tip = code.nextAll("p[tip]");
                        $error.addClass("error_tip");
                        $error.html("密码不能与用户名一致！");
                        $tip.hide();
                        $error.show();
                        return false;
                    }
                    else if (data == "01") {
                        $error = code.nextAll("p[errortip]");
                        $tip = code.nextAll("p[tip]");
                        $error.addClass("error_tip");
                        $error.html("不能和登录密码相同！");
                        $tip.hide();
                        $error.show();
                        return false;
                    } else if (data == "03") {
                        $("#ps2").hide();
                        $("#ps3").show();
                        reload();
                    }
                    else {
                        $error = code.nextAll("p[errortip]");
                        $tip = code.nextAll("p[tip]");
                        $error.addClass("error_tip");
                        $error.html(data);
                        $tip.hide();
                        $error.show();
                        return false;
                    }
                }
            });
        }
        if (type == "securityPhone") {
            /*--手机验证码找回密保问题--*/
            var $cli = $(this).parent().prev().children("li").eq(2);
            var code = $cli.children("div").eq(1).children("input").eq(0);
            var realVal = phoneNumber;
            if (code.val().length <= 0) {
                $error = code.nextAll("p[errortip]");
                $tip = code.nextAll("p[tip]");
                $error.addClass("error_tip");
                $error.html("请输入手机验证码！");
                $tip.hide();
                $error.show();
                return false;
            }
            if (!myregCode.test(code.val())) {
            	$error = code.nextAll("p[errortip]");
                $tip = code.nextAll("p[tip]");
                $error.addClass("error_tip");
                $error.html("请输入正确的验证码！");
                $tip.hide();
                $error.show();
                return false;
            }
            var data = {"evencheck": "phone", "ctp": "securitypwd", "code": code.val(), "realVal": realVal};
            $.ajax({
                type: "post",
                dataType: "html",
                url: _checkCodeUrl,
                data: data,
                success: function (data) {
                    if (data == "01") {
                        $error = code.nextAll("p[errortip]");
                        $tip = code.nextAll("p[tip]");
                        $error.addClass("error_tip");
                        $error.html("输入手机号！");
                        $tip.hide();
                        $error.show();
                        return false;
                    } else if (data == "02") {
                        $error = code.nextAll("p[errortip]");
                        $tip = code.nextAll("p[tip]");
                        $error.addClass("error_tip");
                        $error.html("输入验证码有误！");
                        $tip.hide();
                        $error.show();
                        return false;
                    } else if (data == "03") {
                        $error = code.nextAll("p[errortip]");
                        $tip = code.nextAll("p[tip]");
                        $error.addClass("error_tip");
                        $error.html("请输入正确的验证码！");
                        $tip.hide();
                        $error.show();
                        return false;
                    } else if (data == "04") {
                        $error = code.nextAll("p[errortip]");
                        $tip = code.nextAll("p[tip]");
                        $error.addClass("error_tip");
                        $error.html("当前手机号码当天匹配验证码错误次数已达上限！");
                        $tip.hide();
                        $error.show();
                        return false;
                    } else {
                        $("#securityStep1").hide();
                        $("#securityStep2").show();
                    }
                }
            });
        }
        if (type == "securityPwd") {
            /*--通过密保答案修改回密保问题--*/
            var $cli = $(this).parent().prev().children("li");
            var answer1 = $cli.eq(0).children("div").eq(3).children("input").eq(0);
            var answer2 = $cli.eq(1).children("div").eq(3).children("input").eq(0);
            var answer3 = $cli.eq(2).children("div").eq(3).children("input").eq(0);
            if (answer1.val().length <= 0) {
                $error = answer1.nextAll("p[errortip]");
                $tip = answer1.nextAll("p[tip]");
                $error.addClass("error_tip");
                $error.html("不能为空！");
                $tip.hide();
                $error.show();
                return false;
            }
            if (answer2.val().length <= 0) {
                $error = answer2.nextAll("p[errortip]");
                $tip = answer2.nextAll("p[tip]");
                $error.addClass("error_tip");
                $error.html("不能为空！");
                $tip.hide();
                $error.show();
                return false;
            }
            if (answer3.val().length <= 0) {
                $error = answer3.nextAll("p[errortip]");
                $tip = answer3.nextAll("p[tip]");
                $error.addClass("error_tip");
                $error.html("不能为空！");
                $tip.hide();
                $error.show();
                return false;
            }
            var data = {
        		"answer1": answer1.attr("qid")+":"+RSAUtils.encryptedString(key,answer1.val()), 
        		"answer2": answer2.attr("qid")+":"+RSAUtils.encryptedString(key,answer2.val()), 
        		"answer3": answer3.attr("qid")+":"+RSAUtils.encryptedString(key,answer3.val())
            };
            $.ajax({
                type: "post",
                dataType: "html",
                url: _checkMbwtUrl,
                data: data,
                success: function (data) {
                    if (data == "01") {
                        $error = answer1.nextAll("p[errortip]");
                        $tip = answer1.nextAll("p[tip]");
                        $error.addClass("error_tip");
                        $error.html("密保问题答案不能为空！");
                        $tip.hide();
                        $error.show();
                        return false;
                    } else if (data == "02") {
                        $error = answer1.nextAll("p[errortip]");
                        $tip = answer1.nextAll("p[tip]");
                        $error.addClass("error_tip");
                        $error.html("密保问题答案错误！");
                        $tip.hide();
                        $error.show();
                        return false;
                    } else if (data == "03") {
                        $error = answer2.nextAll("p[errortip]");
                        $tip = answer2.nextAll("p[tip]");
                        $error.addClass("error_tip");
                        $error.html("密保问题答案错误！");
                        $tip.hide();
                        $error.show();
                        return false;
                    } else if (data == "04") {
                        $error = answer3.nextAll("p[errortip]");
                        $tip = answer3.nextAll("p[tip]");
                        $error.addClass("error_tip");
                        $error.html("密保问题答案错误！");
                        $tip.hide();
                        $error.show();
                        return false;
                    }else {
                        $("#mbwtSteps1").hide();
                        $("#mbwtSteps2").show();
                    }
                }
            });
        }
    });
});
//修改邮箱：通过原邮箱验证新的邮箱
$(function () {
    $(".emilsend_email").click(function () {
        var emailValue = $("#oldEmailCheck").val();
        emilsendCommon($(this), "new", emailValue, "emil_emil");
    });
});

//修改邮箱：通过手机号验证新的邮箱
$(function () {
    $(".emilsend_phone").click(function () {
        var phone = phoneNumber;
        emilsendCommon($(this), "pnew", phone, "phoneemil");
    });
});

function emilsendCommon(evt, sendType, oldVal, utp) {
    var $eli = evt.parent().prev().children("li").eq(0);
    var bemil = $eli.children("div").eq(1).children("input").eq(0);
    var myreg = /^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,4}$/;
    if($.trim(bemil.val())==""){
    	$error = bemil.nextAll("p[errortip]");
        $tip = bemil.nextAll("p[tip]");
        $error.addClass("error_tip red");
        $error.html("请输入邮箱地址！");
        $tip.hide();
        $error.show();
        return false;
    }
    if (!myreg.test(bemil.val())) {
        $error = bemil.nextAll("p[errortip]");
        $tip = bemil.nextAll("p[tip]");
        $error.addClass("error_tip red");
        $error.html("邮箱地址不正确！");
        $tip.hide();
        $error.show();
        return false;
    }
    var eNewVerify;
    var eNewVerify1;
    if (!(flagSfxyyzm != "undefined" && flagSfxyyzm == false)) {
        eNewVerify1 = $eli.next().children("div").eq(1).children("input").eq(0);
        eNewVerify = eNewVerify1.val();
        if (eNewVerify.length <= 0) {
            showError(eNewVerify1, "不能为空！");
            flushVerifyCode(eNewVerify1);
            return false;
        }
        if (!myregCode.test(eNewVerify)) {
        	showError(eNewVerify1, "请输入正确的验证码！");
            flushVerifyCode(eNewVerify1);
            return false;
        }

    }
    var evn = evt[0];
    var tokenValue = $("input[name='"+_tokenName+"']").val();

    var data = {
        "tokenKey": tokenValue,
        "type": sendType,
        "emil": bemil.val(),
        "phone": "",
        "verifyType": "NEW_EMAIL",
        "verifyCode": eNewVerify,
        "utp":utp,
        "oldVal":oldVal
    };
    $.ajax({
        type: "post",
        dataType: "html",
        url: _sendUrl,
        data: data,
        success: function (data) {
            if (data) {
                var ct = eval('(' + data + ')');
                //替换token
                setToken(ct);
                if (ct.length > 0 && ct[0].num != 1) {
                    if (ct[0].num == 0) {
                        $error = bemil.nextAll("p[errortip]");
                        $tip = bemil.nextAll("p[tip]");
                    }else if (ct[0].num == 2) {
                        $error = eNewVerify1.nextAll("p[errortip]");
                        $tip = eNewVerify1.nextAll("p[tip]");
                    }else if (ct[0].num == 101) {
                    	window.location.href=_loginUrl;
                    	return false;
                    }
                    $error.addClass("error_tip red");
                    $error.html(ct[0].msg);
                    $tip.hide();
                    $error.show();
                    flushVerifyCode(eNewVerify1);
                    return false;
                }
                if (ct.length > 0 && ct[0].num == 1) {
                    //sendclick1(evn);
                    if (sendType == "pnew") {
                        $("#emailSendSuccess2").show();
                        $("#emailSendSuccess2Email").text(bemil.val());
                        $("#ep2 div.form_info").hide();
                    }
                    else {
                        $("#emailSendSuccess3").show();
                        $("#emailSendSuccess3Email").text(bemil.val());
                        $("#e2 div.form_info").hide();
                    }
                }
            }
        }
    });
}

//通过老手机方式修改手机：验证新手机
$(function () {
    $(".phonesend_phone").click(function () {
        phonesendCommon($(this), "new");
    });
});

//通过身份证方式修改手机：验证新手机
$(function () {
    $(".phonesend_card").click(function () {
        phonesendCommon($(this), "cnew");
    });
});

function phonesendCommon(evt, sendType) {
    var $eli = evt.parent().parent().prev();
    var bemil = $eli.prev().children("div").eq(1).children("input").eq(0);
    var myreg = /^(13|14|15|17|18)[0-9]{9}$/;
    if (bemil.val().length <= 0) {
        $error = bemil.nextAll("p[errortip]");
        $tip = bemil.nextAll("p[tip]");
        $error.addClass("error_tip");
        $error.html("不能为空！");
        $tip.hide();
        $error.show();
        return false;
    }
    if (!myreg.test(bemil.val())) {
        $error = bemil.nextAll("p[errortip]");
        $tip = bemil.nextAll("p[tip]");
        $error.addClass("error_tip");
        $error.html("手机号码不正确!");
        $tip.hide();
        $error.show();
        return false;
    }
    var phoneNewVerify;
    var phoneNewVerify1;
    if (!(flagSfxyyzm != "undefined" && flagSfxyyzm == false)) {
        phoneNewVerify1 = $eli.children("div").eq(1).children("input").eq(0);
        phoneNewVerify = phoneNewVerify1.val();
        if (phoneNewVerify.length <= 0) {
            flushVerifyCode(phoneNewVerify1);
            showError(phoneNewVerify1, "不能为空！");
            return false;
        }
        if (!myregCode.test(phoneNewVerify)) {
        	flushVerifyCode(phoneNewVerify1);
            showError(phoneNewVerify1, "请输入正确的验证码！");
            return false;
        }
    }
    var evn = evt[0];
    var tokenValue = $("input[name='"+_tokenName+"']").val();
    var data = {
        "tokenKey": tokenValue,
        "type": sendType,
        "emil": "",
        "phone": bemil.val(),
        "verifyType": "NEW_PHONE",
        "verifyCode": phoneNewVerify
    };
    $.ajax({
        type: "post",
        dataType: "html",
        url: _sendUrl,
        data: data,
        success: function (data) {
            if (data) {
                var ct = eval('(' + data + ')');
                //替换token
                setToken(ct);
                if (ct.length > 0 && ct[0].num != 1) {
                    if (ct[0].num == 0) {
                        $error = bemil.nextAll("p[errortip]");
                        $tip = bemil.nextAll("p[tip]");
                    }else if (ct[0].num == 2) {
                        $error = phoneNewVerify1.nextAll("p[errortip]");
                        $tip = phoneNewVerify1.nextAll("p[tip]");
                    }else if (ct[0].num == 101) {
                    	window.location.href=_loginUrl;
                    	return false;
                    }
                    flushVerifyCode(phoneNewVerify1);
                    $error.addClass("error_tip");
                    $error.html(ct[0].msg);
                    $tip.hide();
                    $error.show();
                    return false;
                }
                if (ct.length > 0 && ct[0].num == 1) {
                    sendclick4(evn);
                }
            }
        }
    });
}


function isIdCardNo(num) {
    num = num.toUpperCase();           //身份证号码为15位或者18位，15位时全为数字，18位前17位为数字，最后一位是校验位，可能为数字或字符X。        
    if (!(/(^\d{15}$)|(^\d{17}([0-9]|X)$)/.test(num))) {
        $("#info").html(showDialogInfo("输入的身份证号不符合规定！15位号码应全为数字，18位号码末位可以为数字或X。", "error"));
        $("div.popup_bg").show();
        return false;
    } //校验位按照ISO 7064:1983.MOD 11-2的规定生成，X可以认为是数字10。 
    //下面分别分析出生日期和校验位 
    var len, re;
    len = num.length;
    if (len == 15) {
        re = new RegExp(/^(\d{6})(\d{2})(\d{2})(\d{2})(\d{3})$/);
        var arrSplit = num.match(re);  //检查生日日期是否正确
        var dtmBirth = new Date('19' + arrSplit[2] + '/' + arrSplit[3] + '/' + arrSplit[4]);
        var bGoodDay;
        bGoodDay = (dtmBirth.getYear() == Number(arrSplit[2])) && ((dtmBirth.getMonth() + 1) == Number(arrSplit[3])) && (dtmBirth.getDate() == Number(arrSplit[4]));
        if (!bGoodDay) {
            $("#info").html(showDialogInfo("输入的身份证号里出生日期不对！", "error"));
            $("div.popup_bg").show();
            return false;
        } else { //将15位身份证转成18位 //校验位按照ISO 7064:1983.MOD 11-2的规定生成，X可以认为是数字10。        
            var arrInt = [7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2];
            var arrCh = ['1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2'];
            var nTemp = 0, i;
            num = num.substr(0, 6) + '19' + num.substr(6, num.length - 6);
            for (i = 0; i < 17; i++) {
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
        var bGoodDay;
        bGoodDay = (dtmBirth.getFullYear() == Number(arrSplit[2])) && ((dtmBirth.getMonth() + 1) == Number(arrSplit[3])) && (dtmBirth.getDate() == Number(arrSplit[4]));
        if (!bGoodDay) {
            $("#info").html(showDialogInfo("输入的身份证号里出生日期不对！", "error"));
            $("div.popup_bg").show();
            return false;
        }
        else { //检验18位身份证的校验码是否正确。 //校验位按照ISO 7064:1983.MOD 11-2的规定生成，X可以认为是数字10。 
            var valnum;
            var arrInt = [7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2];
            var arrCh = ['1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2'];
            var nTemp = 0, i;
            for (i = 0; i < 17; i++) {
                nTemp += num.substr(i, 1) * arrInt[i];
            }
            valnum = arrCh[nTemp % 11];
            if (valnum != num.substr(17, 1)) {
                $("#info").html(showDialogInfo("18位身份证的校验码不正确！", "error"));
                $("div.popup_bg").show();
                return false;
            }
            return true;
        }
    }
    return false;
}

//关闭弹出窗
function localch(hId, sId) {
    $("#" + hId).hide();
    $("#" + sId).show();
}


//绑定邮箱时计时器
function sendclick(evn) {
    if (wait == 0) {
        $(evn).removeAttr("disabled");
        $(evn).removeClass("btn08");
        $(evn).addClass("btn05");
        evn.value = "获取验证码";
        wait = 60;
    } else {
        if (!$(evn).hasClass("btn08")) {
            $(evn).addClass("btn08");
            $(evn).removeClass("btn03");
        }
        $(evn).attr("disabled","true");
        evn.value = "(" + wait + ")秒重新获取";
        wait--;
        setTimeout(function () {
                    sendclick(evn);
                },
                1000);
    }
}
//修改邮箱时计时器
function sendclick6(evn) {
    if (wait6 == 0) {
        $(evn).removeAttr("disabled");
        $(evn).removeClass("btn08");
        $(evn).addClass("btn03");
        evn.value = "获取验证码";
        wait6 = 60;
    } else {
        if (!$(evn).hasClass("btn08")) {
            $(evn).addClass("btn08");
            $(evn).removeClass("btn03");
        }
        $(evn).attr("disabled","true");
        evn.value = "(" + wait6 + ")秒重新获取";
        wait6--;
        setTimeout(function () {
                    sendclick6(evn);
                },
                1000);
    }
}
//新邮箱时计时器
function sendclick1(evn) {
    if (wait1 == 0) {
        $(evn).removeAttr("disabled");
        $(evn).removeClass("btn08");
        $(evn).addClass("btn03");
        evn.value = "获取验证码";
        wait1 = 60;
    } else {
        if (!$(evn).hasClass("btn08")) {
            $(evn).addClass("btn08");
            $(evn).removeClass("btn03");
        }
        $(evn).attr("disabled","true");
        evn.value = "(" + wait1 + ")秒重新获取";
        wait1--;
        setTimeout(function () {
                    sendclick1(evn);
                },
                1000);
    }
}

var timeOut2;
//手机修改邮箱时
function sendclick2(evn) {
    if (wait2 == 0) {
        $(evn).removeAttr("disabled");
        $(evn).removeClass("btn08");
        $(evn).addClass("btn03");
        evn.value = "获取验证码";
        wait2 = 60;
    } else {
        if (!$(evn).hasClass("btn08")) {
            $(evn).addClass("btn08");
            $(evn).removeClass("btn03");
        }
        $(evn).attr("disabled","true");
        evn.value = "(" + wait2 + ")秒重新获取";
        wait2--;
        timeOut2 = setTimeout(function () {
                    sendclick2(evn);
                },
                1000);
    }
}

//绑定手机时计时器
function sendclick3(evn) {
    if (wait3 == 0) {
        $(evn).removeAttr("disabled");
        $(evn).removeClass("btn08");
        $(evn).addClass("btn03");
        evn.value = "获取验证码";
        wait3 = 60;
    } else {
        if (!$(evn).hasClass("btn08")) {
            $(evn).addClass("btn08");
            $(evn).removeClass("btn03");
        }
        $(evn).attr("disabled","true");
        evn.value = "(" + wait3 + ")秒重新获取";
        wait3--;
        setTimeout(function () {
                    sendclick3(evn);
                },
                1000);
    }
}
var timeout7;
//修改手机时计时器
function sendclick7(evn) {
    if (wait7 == 0) {
        $(evn).removeAttr("disabled");
        $(evn).removeClass("btn08");
        $(evn).addClass("btn03");
        evn.value = "获取验证码";
        wait7 = 60;
    } else {
        if (!$(evn).hasClass("btn08")) {
            $(evn).addClass("btn08");
            $(evn).removeClass("btn03");
        }
        $(evn).attr("disabled","true");
        evn.value = "(" + wait7 + ")秒重新获取";
        wait7--;
        timeout7 = setTimeout(function () {
                    sendclick7(evn);
                },
                1000);
    }
}

//新手机时计时器
var timeOut4;
function sendclick4(evn) {
    if (wait4 == 0) {
        $(evn).removeAttr("disabled");
        $(evn).removeClass("btn08");
        $(evn).addClass("btn03");
        evn.value = "获取验证码";
        wait4 = 60;
    } else {
        if (!$(evn).hasClass("btn08")) {
            $(evn).addClass("btn08");
            $(evn).removeClass("btn03");
        }
        $(evn).attr("disabled","true");
        evn.value = "(" + wait4 + ")秒重新获取";
        wait4--;
        timeOut4 = setTimeout(function () {
                    sendclick4(evn);
                },
                1000);
    }
}
var timeOut5;
//交易密码时计时器
function sendclick5(evn) {
    if (wait5 == 0) {
        $(evn).removeAttr("disabled");
        $(evn).removeClass("btn08");
        $(evn).addClass("btn03");
        evn.value = "获取验证码";
        wait5 = 60;
    } else {
        if (!$(evn).hasClass("btn08")) {
            $(evn).addClass("btn08");
            $(evn).removeClass("btn03");
        }
        $(evn).attr("disabled","true");
        evn.value = "(" + wait5 + ")秒重新获取";
        wait5--;
        timeOut5 = setTimeout(function () {
                    sendclick5(evn);
                },
                1000);
    }
}

var timeOut8;
//交易密码时计时器
function sendclick8(evn) {
  if (wait8 == 0) {
      $(evn).removeAttr("disabled");
      $(evn).removeClass("btn08");
      $(evn).addClass("btn03");
      evn.value = "获取验证码";
      wait8 = 60;
  } else {
      if (!$(evn).hasClass("btn08")) {
          $(evn).addClass("btn08");
          $(evn).removeClass("btn03");
      }
      $(evn).attr("disabled","true");
      evn.value = "(" + wait8 + ")秒重新获取";
      wait8--;
      timeOut8 = setTimeout(function () {
                  sendclick8(evn);
              },
              1000);
  }
}

function setSubmitFlg() {
    flgs = true;
}

function setToken(ct) {
    if (ct.length > 0) {
        $("#token").html(ct[0].tokenNew);
    }
}

function showEmailSend(obj) {
    if (obj == "1") {
        $("#emailSendSuccess1").hide();
        $("#emailSend1").show();
        if (!(flagSfxyyzm != "undefined" && flagSfxyyzm == false)) {
            $("#_verifyImg_6").attr("src", _verifyCommonUrl+ "?" + Math.random() + "&verifyType=bindEmail");
            $("input[name='bindEmailVerify']").val("");
        }
    } else if (obj == "2") {
        $("#emailSendSuccess2").hide();
        $("#ep2 div.form_info").show();
        if (!(flagSfxyyzm != "undefined" && flagSfxyyzm == false)) {
            $("#_verifyImg_4").attr("src", _verifyCommonUrl+ "?" + Math.random() + "&verifyType=newEmail");
            $("input[name='epNewEmailVerify']").val("");
        }
    } else {
        $("#emailSendSuccess3").hide();
        $("#e2 div.form_info").show();
        if (!(flagSfxyyzm != "undefined" && flagSfxyyzm == false)) {
            $("#_verifyImg_2").attr("src", _verifyCommonUrl+ "?" + Math.random() + "&verifyType=newEmail");
            $("input[name='eNewVerify']").val("");
        }
    }
}

function clearTimes(num){
    if(num == 7) {
        clearTimeout(timeout7);
        wait7 = 60;
    }else if(num == 2){
        clearTimeout(timeOut2);
        wait2 = 60;
    }else if(num == 5){
        clearTimeout(timeOut5);
        wait5 = 60;
    }else if(num == 4){
        clearTimeout(timeOut4);
        wait4 = 60;
        $(".clearTimedes").removeAttr("disabled").removeClass("btn08").addClass("btn03").val("获取手机验证码");
        return;
    }
    $("#yzmbtn"+num).removeAttr("disabled");
    $("#yzmbtn"+num).removeClass("btn08");
    $("#yzmbtn"+num).addClass("btn03");
    $("#yzmbtn"+num).val("获取验证码");
}
var selectlist1 = [];
var selectlist2 = [];
var selectlist3 = [];
var selectlist4 = [];
var selectlist5 = [];
var selectlist6 = [];
var selectlist7 = [];
var selectlist8 = [];
var selectlist9 = [];
$(function () {
    /** 输入提示替换 */
    $("div#box8 input[data=show]").focus(function () {
        $(this).nextAll("input[data=hide]").show().focus();
        $(this).hide();
    });
    $("div#box9 input[data=show]").focus(function () {
        $(this).nextAll("input[data=hide]").show().focus();
        $(this).hide();
    });
    $("div#box10 input[data=show]").focus(function () {
        $(this).nextAll("input[data=hide]").show().focus();
        $(this).hide();
    });
    /**密保问题提交 */
    $("#safeSubmit").click(function () {
    	var param = [];
        var checkFlag = true;
        $.each($("div#box8 .question input[type='hidden']"), function (i, o) {
            var p = '';
            // var type = $(o).attr("id").substr("question".length);
            var questionId = o.value;
            if(!questionId){
                $(o).parent().parent().find("p[errortip]").addClass("red").html("请选择密保问题！").show();
                checkFlag = false;
                return false;
            }
            var val = $(o).parent().parent().find("input[data=hide]").val();
            if (!val) {
                $(o).parent().parent().find("p[errortip]").addClass("red").html("不能为空！").show();
                $(o).parent().parent().find("input[data=hide]").focus();
                checkFlag = false;
                return false;
            }
            //防止SQL注入
            var re = /select|update|delete|exec|count|’|"|=|;|>|<|%/i;
            if (re.test(val)) {
                $(o).parent().parent().find("p[errortip]").addClass("red").html("存在特殊字符").show();
                $(o).parent().parent().find("input[data=hide]").focus();
                checkFlag = false;
                return false;
            }
            p.qid = questionId;
            p.value = val;
            param += questionId + ':' + RSAUtils.encryptedString(key, val) + ",";
        })
        if (!checkFlag) return;
        var tokenValue = $("input[name='"+_tokenName+"']").val();
        var data = {"tokenKey": tokenValue,"pwdType":"", "param": param.substr(0, param.length - 1),"realval":phoneNumber};
        $.ajax({
            type: "post",
            dataType: "html",
            url: _pwdSafeServletUrl,
            data: data,
            success: function (data) {
                var ct = eval("(" + data + ")");
                //var ct = eval('('+data+')');
                //替换token
                setToken(ct);
                if (ct.num == '0000') {
                	$("#mbLi").show();
                    reload();
                } else {
                    if (ct.msg != undefined) {
                        alert(ct.msg);
                    }
                }
            }, 
            error: function () {
                alert("系统繁忙，请稍后重试！");
            }
        })
    });
    
    /**验证原密保问题答案修改**/
    $("#safeUpdateSubmit").click(function () {
    	var obj1=$("div#box9 .question input[type='hidden']");
    	submitSecurityPwd(obj1,"update");
    });
    
    /**手机找回密保问题修改**/
    $("#safeFindSubmit").click(function () {
    	var obj1=$("div#box10 .question input[type='hidden']");
    	submitSecurityPwd(obj1,"find");
    });
    
    /**设置密保问题**/
    var obj1 = $("#question1");
	var obj2 = $("#question2");
	var obj3 = $("#question3");

	//下拉框美化
	selectlist1 = obj1.selectlist({
		width: 165,
		optionHeight: 28,
		height: 28,
		onChange: function(){
			initQuestionData("question1","question2","question3",1);
		}
	});
	selectlist2 = obj2.selectlist({
		width: 165,
		optionHeight: 28,
		height: 28,
		onChange: function(){
			initQuestionData("question1","question2","question3",2);
		}
	});
	selectlist3 = obj3.selectlist({
		width: 165,
		optionHeight: 28,
		height: 28,
		onChange: function(){
			initQuestionData("question1","question2","question3",3);
		}
	});
	
	/*obj1.change(function(){
        initQuestionData(obj1,obj2,obj3,1);
    });
	obj2.change(function(){
        initQuestionData(obj1,obj2,obj3,2);
    });
	obj3.change(function(){
        initQuestionData(obj1,obj2,obj3,3);
    });*/
	
	/**通过原密保问题修改密保问题**/
    var obj4 = $("#question4");
    var obj5 = $("#question5");
    var obj6 = $("#question6");
    
    //下拉框美化
    selectlist4 = obj4.selectlist({
		width: 212,
		optionHeight: 28,
		height: 28,
		onChange: function(){
			initQuestionData("question4","question5","question6",1);
		}
	});
    selectlist5 = obj5.selectlist({
		width: 212,
		optionHeight: 28,
		height: 28,
		onChange: function(){
			initQuestionData("question4","question5","question6",2);
		}
	});
    selectlist6 = obj6.selectlist({
		width: 212,
		optionHeight: 28,
		height: 28,
		onChange: function(){
			initQuestionData("question4","question5","question6",3);
		}
	});
    /*obj4.change(function(){
        initQuestionData(obj4,obj5,obj6,1);
    });
    obj5.change(function(){
        initQuestionData(obj4,obj5,obj6,2);
    });
    obj6.change(function(){
        initQuestionData(obj4,obj5,obj6,3);
    });*/
    
    /**通过手机验证码[找回]修改密保问题**/
    var obj7 = $("#question7");
    var obj8 = $("#question8");
    var obj9 = $("#question9");
    
    //下拉框美化
    selectlist7 = obj7.selectlist({
		width: 212,
		optionHeight: 28,
		height: 28,
		onChange: function(){
			initQuestionData("question7","question8","question9",1);
		}
	});
    selectlist8 = obj8.selectlist({
		width: 212,
		optionHeight: 28,
		height: 28,
		onChange: function(){
			initQuestionData("question7","question8","question9",2);
		}
	});
    selectlist9 = obj9.selectlist({
		width: 212,
		optionHeight: 28,
		height: 28,
		onChange: function(){
			initQuestionData("question7","question8","question9",3);
		}
	});
    /*obj7.change(function(){
        initQuestionData(obj7,obj8,obj9,1);
    });
    obj8.change(function(){
        initQuestionData(obj7,obj8,obj9,2);
    });
    obj9.change(function(){
        initQuestionData(obj7,obj8,obj9,3);
    });*/
});

function submitSecurityPwd(obj1,type){
	var param = [];
    var checkFlag = true;
    $.each(obj1, function (i, o) {
        var p = '';
        // var type = $(o).attr("id").substr("question".length);
        var questionId = o.value;
        if(!questionId){
            $(o).parent().parent().parent().find("p[errortip]").addClass("red").html("请选择密保问题！").show();
            checkFlag = false;
            return false;
        }
        var val = $(o).parent().parent().parent().find("input[data=hide]").val();
        if (!val) {
            $(o).parent().parent().parent().find("p[errortip]").addClass("red").html("不能为空！").show();
            $(o).parent().parent().parent().find("input[data=hide]").focus();
            checkFlag = false;
            return false;
        }
        //防止SQL注入
        var re = /select|update|delete|exec|count|’|"|=|;|>|<|%/i;
        if (re.test(val)) {
            $(o).parent().parent().parent().find("p[errortip]").addClass("red").html("存在特殊字符").show();
            $(o).parent().parent().parent().find("input[data=hide]").focus();
            checkFlag = false;
            return false;
        }
        p.qid = questionId;
        p.value = val;
        param += questionId + ':' + RSAUtils.encryptedString(key, val) + ",";
    })
    if (!checkFlag) return;
    var tokenValue = $("input[name='"+_tokenName+"']").val();
    var data = {"tokenKey": tokenValue,"pwdType":type, "param": param.substr(0, param.length - 1),"realval":phoneNumber};
    $.ajax({
        type: "post",
        dataType: "html",
        url: _pwdSafeServletUrl,
        data: data,
        success: function (data) {
            var ct = eval("(" + data + ")");
            //var ct = eval('('+data+')');
            //替换token
            setToken(ct);
            if (ct.num == '0000') {
            	if("update" == type){
            		$("#mbwtSteps2").hide();
            		$("#mbwtSteps3").show();
            	}else if("find" == type){
            		$("#securityStep2").hide();
            		$("#securityStep3").show();
            	}
                reload();
            } else {
                if (ct.msg != undefined) {
                	$("#info").html(showDialogInfo(ct.msg,"error"));
        			$("div.popup_bg").show();
                }
            }
        }, 
        error: function () {
        	$("#info").html(showDialogInfo("系统繁忙，请稍后重试！","error"));
			$("div.popup_bg").show();
        }
    })
}

function initQuestionData(obj1,obj2,obj3,_index){
    $.ajax({
        type: "post",
        dataType: "json",
        url: _queryQuestionUrl,
        data: {"quesId1":$("input[name='"+obj1+"']").val(),"quesId2":$("input[name='"+obj2+"']").val(),"quesId3":$("input[name='"+obj3+"']").val()},
        success: function (data) {
            //data = eval("("+data+")");
            if(_index==1){
                var ques2 = $("input[name='"+obj2+"']").val();
                var ques3 = $("input[name='"+obj3+"']").val();
                var ques2Desc = $("#"+obj2+" input[type='button']").val();
                var ques3Desc = $("#"+obj3+" input[type='button']").val();
                var ques2list = "";
                var ques3list = "";
                if(ques2 > 0){
                	ques2list += "<li data-value='"+ques2+"' class='selected'>"+ques2Desc+"</li>";
                }
                if(ques3 > 0){
                	ques3list += "<li data-value='"+ques3+"' class='selected'>"+ques3Desc+"</li>";
                }
                for(var i = 0;i < data.length ; i++){
                    var obj = data[i];
                    if(ques2 == obj.id){
                    	ques2list +="<li data-value='"+obj.id+"' class='selected'>"+obj.descr+"</li>";
                    }else{
                        if(i==0){
                        	ques2list +="<li data-value=''>请选择密保问题</li>";
                        }
                        ques2list +="<li data-value='"+obj.id+"'>"+obj.descr+"</li>";
                    }
                    if(ques3 == obj.id){
                    	ques3list += "<li data-value='"+obj.id+"'  class='selected'>"+obj.descr+"</li>";
                    }else{
                        if(i==0){
                        	ques3list += "<li data-value=''>请选择密保问题</li>";
                        }
                        ques3list += "<li data-value='"+obj.id+"'>"+obj.descr+"</li>";
                    }
                }
                if(obj1 == "question1"){
                	selectlist2[0].updateSelectList(ques2list);
                	selectlist3[0].updateSelectList(ques3list);
                }else if(obj1 == "question4"){
                	selectlist5[0].updateSelectList(ques2list);
                	selectlist6[0].updateSelectList(ques3list);
                }else if(obj1 == "question7"){
                	selectlist8[0].updateSelectList(ques2list);
                	selectlist9[0].updateSelectList(ques3list);
                }
            }else if(_index==2){
                var ques1 = $("input[name='"+obj1+"']").val();
                var ques3 = $("input[name='"+obj3+"']").val();
                var ques1Desc = $("#"+obj1+" input[type='button']").val();
                var ques3Desc = $("#"+obj3+" input[type='button']").val();
                var ques1list = "";
                var ques3list = "";
                if(ques1 > 0){
                	ques1list += "<li data-value='"+ques1+"'  class='selected'>"+ques1Desc+"</li>";
                }
                if(ques3 > 0){
                	ques3list += "<li data-value='"+ques3+"'  class='selected'>"+ques3Desc+"</li>";
                }
                for(var i = 0;i < data.length ; i++){
                    var obj = data[i];
                    if(ques1 == obj.id){
                    	ques1list += "<li data-value='"+obj.id+"'  class='selected'>"+obj.descr+"</li>";
                    }else{
                        if(i==0){
                        	ques1list += "<li data-value=''>请选择密保问题</li>";
                        }
                        ques1list += "<li data-value='"+obj.id+"'>"+obj.descr+"</li>";
                    }
                    if(ques3 == obj.id){
                    	ques3list += "<li data-value='"+obj.id+"'  class='selected'>"+obj.descr+"</li>";
                    }else{
                        if(i==0){
                        	ques3list += "<li data-value=''>请选择密保问题</li>";
                        }
                        ques3list += "<li data-value='"+obj.id+"'>"+obj.descr+"</li>";
                    }
                }
                if(obj1 == "question1"){
                	selectlist1[0].updateSelectList(ques1list);
                	selectlist3[0].updateSelectList(ques3list);
                }else if(obj1 == "question4"){
                	selectlist4[0].updateSelectList(ques1list);
                	selectlist6[0].updateSelectList(ques3list);
                }else if(obj1 == "question7"){
                	selectlist7[0].updateSelectList(ques1list);
                	selectlist9[0].updateSelectList(ques3list);
                }
            }else if(_index==3){
                var ques1 = $("input[name='"+obj1+"']").val();
                var ques2 = $("input[name='"+obj2+"']").val();
                var ques1Desc = $("#"+obj1+" input[type='button']").val();
                var ques2Desc = $("#"+obj2+" input[type='button']").val();
                var ques1list = "";
                var ques2list = "";
                if(ques1 > 0){
                	ques1list += "<li data-value='"+ques1+"' class='selected'>"+ques1Desc+"</li>";
                }
                if(ques2 > 0){
                	ques2list += "<li data-value='"+ques2+"' class='selected'>"+ques2Desc+"</li>";
                }
                for(var i = 0;i < data.length ; i++){
                    var obj = data[i];
                    if(ques1 == obj.id){
                    	ques1list += "<li data-value='"+obj.id+"' class='selected'>"+obj.descr+"</li>";
                    }else{
                        if(i==0){
                        	ques1list += "<li data-value=''>请选择密保问题</li>";
                        }
                        ques1list += "<li data-value='"+obj.id+"'>"+obj.descr+"</li>";
                    }
                    if(ques2 == obj.id){
                    	ques2list += "<li data-value='"+obj.id+"' class='selected'>"+obj.descr+"</li>";
                    }else{
                        if(i==0){
                        	ques2list += "<li data-value=''>请选择密保问题</li>";
                        }
                        ques2list += "<li data-value='"+obj.id+"'>"+obj.descr+"</li>";
                    }
                }
                if(obj1 == "question1"){
                	selectlist1[0].updateSelectList(ques1list);
                	selectlist2[0].updateSelectList(ques2list);
                }else if(obj1 == "question4"){
                	selectlist4[0].updateSelectList(ques1list);
                	selectlist5[0].updateSelectList(ques2list);
                }else if(obj1 == "question7"){
                	selectlist7[0].updateSelectList(ques1list);
                	selectlist8[0].updateSelectList(ques2list);
                }
            }
        }, 
        error: function () {
        	$("#info").html(showDialogInfo("系统繁忙，请稍后重试！","doubt"));
        	$(".popup_bg").show();
        }
    })
}

function clearInput(){
    $(".answerIn").val("");
    $(".question input[type='hidden']").val("");
    $(".question input[type='button']").val("请选择密保问题");
    /**设置密保问题**/
    if(selectlist1.length > 0){
	    initQuestionData("question1","question2","question3",1);
	    initQuestionData("question1","question2","question3",2);
	    initQuestionData("question1","question2","question3",3);
    }
	
	/**通过原密保问题修改密保问题**/
    if(selectlist4.length > 0){
	    initQuestionData("question4","question5","question6",1);
	    initQuestionData("question4","question5","question6",2);
	    initQuestionData("question4","question5","question6",3);
    }
    
    /**通过手机验证码[找回]修改密保问题**/
    if(selectlist7.length > 0){
	    initQuestionData("question7","question8","question9",1);
	    initQuestionData("question7","question8","question9",2);
	    initQuestionData("question7","question8","question9",3);
    }
}

/**
 * 申请担保
 */

function applyGuarantor(){
    if (!isIdCard) {
        $("#info").html(showDialogInfo("请先<span class='red'>实名认证</span>！", "doubt"));
        $("div.popup_bg").show();
    }
    else if (isMustMail && !isMail) {
        $("#info").html(showDialogInfo("请先<span class='red'>认证邮箱</span>！", "doubt"));
        $("div.popup_bg").show();
    }
    else if (isPhone != 'TG') {
        $("#info").html(showDialogInfo("请先<span class='red'>认证手机号码</span>！", "doubt"));
        $("div.popup_bg").show();
    }
    else if (chargeMustWithdrawPwd==true && isTxPasswordEmpty){
        $("#info").html(showDialogInfo("请先<span class='red'>设置交易密码</span>！", "doubt"));
        $("div.popup_bg").show();
    }
    else if(!_isNetSign)
    {
        $("#info").html(showSuccInfo("申请担保必须先网签担保协议，<a href='"+_netSignUrl+"' class='red'>马上网签</a>", "doubt",""+_netSignUrl+""));
        $("div.popup_bg").show();
    }
    else {
        $("#info").html(showForwardInfo("您希望成为担保人？", "question", "javascript:submitApply()"));
        $(".popup_bg").show();
    }

}

var repeatNum = 0;
function submitApply(){
    if( repeatNum > 0 )
    {
        return;
    }
    repeatNum++;
    $.ajax({
        type: "post",
        dataType: "html",
        url: _applyQuarantorUrl,
        success: function (data) {
            var ct = eval("(" + data + ")");
            if (ct.code == '1000') {
                window.location.reload();
            } else if(ct.code == '1002'){
                if (ct.msg != undefined) {
                    $("#info").html(showSuccInfo("账户逾期借款未还，无法申请，<a href='/user/credit/repaying.html' class='red'>立即还款</a>", "doubt","/user/credit/repaying.html"));
                    $("div.popup_bg").show();
                }
            }else{
                if (ct.msg != undefined) {
                    $("#info").html(showDialogInfo(ct.msg,"error"));
                    $("div.popup_bg").show();
                }
            }
        },
        error: function () {
            $("#info").html(showDialogInfo("系统繁忙，请稍后重试！","error"));
            $("div.popup_bg").show();
        }
    });
}

function cancelGuarantor(){
    $("#info").html(showForwardInfo("您确定取消担保？","question","javascript:submitCancel()"));
    $(".popup_bg").show();
}

function submitCancel(){
    $.ajax({
        type: "post",
        dataType: "html",
        url: _cancelQuarantorUrl,
        success: function (data) {
            var ct = eval("(" + data + ")");
            if (ct.code == '1000') {
                window.location.reload();
            } else {
                if (ct.msg != undefined) {
                    $("#info").html(showDialogInfo(ct.msg,"error"));
                    $("div.popup_bg").show();
                }
            }
        },
        error: function () {
            $("#info").html(showDialogInfo("系统繁忙，请稍后重试！","error"));
            $("div.popup_bg").show();
        }
    });
}

//异步的修改担保码提交
function updateDbm(evn) {
    var $dbm = $("input[name='dbm']");
    var dbm = $.trim($dbm.val());
    if (dbm.length <= 0) {
        showError($dbm, "不能为空！");
        return false;
    }
    var mtest = $dbm.attr("mtest");
    if(mtest != undefined && mtest.length > 0){
    	var myreg = eval(mtest);  
		if(!myreg.test(dbm))
		{
			showError($dbm, $dbm.attr("mtestmsg"));
	        return false;
		}
	}
    
    var data1 = {"dbm": dbm};
    $.ajax({
        type: "post",
        dataType: "html",
        url: _updateDbm,
        data: data1,
        success: function (data) {
            if (data) {
                var ct = eval('(' + data + ')');
                var num = ct[0].num;
                if (num == '02' || num == '03') {
                    showError($dbm, ct[0].msg);
                    return false;
                }
                if (num == '01') {
                    $("#sbDbmLi").show();
                    hideError($dbm);
                    reload();
                }
            }
        }
    });
}

