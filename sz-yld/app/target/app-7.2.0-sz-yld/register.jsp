<%@page import="com.dimeng.framework.resource.ResourceProvider"%>
<%@page import="com.dimeng.framework.config.ConfigureProvider"%>
<%@page import="com.dimeng.framework.resource.ResourceRegister"%>
<%@page import="com.dimeng.p2p.variables.defines.SystemVariable"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	final ResourceProvider resourceProvider = ResourceRegister
			.getResourceProvider(application);
	final ConfigureProvider configureProvider = resourceProvider
			.getResource(ConfigureProvider.class);
	final String protocolType = configureProvider.format(SystemVariable.SITE_REQUEST_PROTOCOL);
	String url = configureProvider.format(SystemVariable.SITE_DOMAIN);
	final String siteTitle = configureProvider
			.format(SystemVariable.SITE_TITLE);
	
	if (url.indexOf(protocolType) == -1) {
		url = protocolType.concat(url);
	}
	
%>
<html>
<head>
<meta name="viewport"
	content="width=device-width, initial-scale=1, user-scalable=no, minimum-scale=1.0, maximum-scale=1.0">
<meta content="email=no" name="format-detection" />
<meta content="telephone=no" name="format-detection" />
<style type="text/css">
@charset "utf-8";

* {
	margin: 0;
	padding: 0;
}

body {
	height: 100%;
	padding: 0px;
	margin: 0px;
	font-size: 100%;
	color: #666;
	width: 100%;
	height: 100%;
	font-family: "微软雅黑";
}

@media screen and (min-width:0px) and (max-width:240px) {
	body {
		font-size: xx-small;
	}
	html {
		font-size: 9px;
	}
}

@media screen and (min-width:241px) and (max-width:384px) {
	body {
		font-size: x-small;
	}
	html {
		font-size: 11px;
	}
}

@media screen and (min-width:385px) and (max-width:480px) {
	body {
		font-size: small;
	}
	html {
		font-size: 13px;
	}
}

@media screen and (min-width:481px) and (max-width:640) {
	body {
		font-size: medium;
	}
	html {
		font-size: 16px;
	}
}

@media screen and (min-width:640px) {
	body {
		font-size: large;
	}
	html {
		font-size: 19px;
	}
}
/*字体大小*/
.fn-s-11 {
	font-size: 1.1rem;
}

.fn-s-12 {
	font-size: 1.2rem;
}

.fn-s-13 {
	font-size: 1.3rem;
}

.fn-s-15 {
	font-size: 1.5rem;
}

.fn-s-14 {
	font-size: 1.4rem;
}

.fn-s-17 {
	font-size: 1.7rem;
}

.fn-s-19 {
	font-size: 1.9rem;
}

.fn-s-20 {
	font-size: 2rem;
}

.fn-s-25 {
	font-size: 2.5rem;
}

.fn-w-b {
	font-weight: bold;
}

.fn-c-black { #666;
	
}

.fn-c-blue {
	color: #34B8FC;
}

.ub-ac {
	-webkit-box-align: center;
	box-align: center;
}

.ub-ae {
	-webkit-box-align: end;
	box-align: end;
}

.ub-pc {
	-webkit-box-pack: center;
	box-pack: center;
}

.ub-pe {
	-webkit-box-pack: end;
	box-pack: end;
}

.ub-pj {
	-webkit-box-pack: justify;
	box-pack: justify;
}

.ub-ver {
	-webkit-box-orient: vertical;
	box-orient: vertical;
}

.ub-ba {
	-webkit-box-orient: block-axis;
	box-orient: block-axis;
}

.ub-bl {
	/* Firefox */
	-moz-box-lines: multiple;
	/* Safari and Chrome */
	-webkit-box-lines: multiple;
	/* W3C */
	box-lines: multiple;
}

.lub-hori {
	-webkit-box-orient: horizontal;
	box-orient: horizontal;
}

.ub-f1 {
	-webkit-box-flex: 1;
	box-flex: 1;
}

.ub-f2 {
	-webkit-box-flex: 2;
	box-flex: 2;
}

.ub-f3 {
	-webkit-box-flex: 3;
	box-flex: 3;
}

.ub-f4 {
	-webkit-box-flex: 4;
	box-flex: 4;
}

.ub-f6 {
	-webkit-box-flex: 6;
	box-flex: 6;
}

.ub {
	display: -webkit-box !important;
	display: box !important;
	position: relative;
}

.bg-c-gray {
	background-color: #F8F8F8;
}

.bg-c-withe {
	background: #fff;
}

.bg-c-blue {
	background-color: #34B8FC;
}

.pd-lr-10 {
	padding: 0rem 1rem;
}

.ld {
	padding: 1rem 0.5rem 1rem 1.5rem;
	line-height: 3rem;
	font-size: 1.5rem;
}

.rd {
	padding: 1rem 0.5rem;
}

.rd input {
	line-height: 2rem;
	border: none;
	outline: medium;
	padding: 0.5rem;
	font-size: 1.5rem;
	width: 90%;
}

.tx-l-r {
	text-align: right;
}

.yzm-wid {
	width: 35%;
}

.bd-t {
	border-top: 1px solid #EEEEEE;
}

.bd-b {
	border-bottom: 1px solid #EEEEEE;
}

.yzm {
	color: white;
	padding: 0.3rem 1.1rem;
	height: 2rem;
	line-height: 2rem;
	font-size: 1.3rem;
	-webkit-border-radius: 4px;
	border-radius: 4px;
	position: absolute;
	right: 1rem;
	top: 1.2rem;
}

#check {
	width: 3rem;
	height: 3rem;
	background-image: url('./image/icon_b1.png');
	-webkit-background-size: auto 1.5rem;
	background-size: auto 1.5rem;
	background-repeat: no-repeat;
	background-position: center;
}

#register {
	margin: 3rem 2%;
	height: 4rem;
	line-height: 4rem;
	font-size: 1.5rem;
	-webkit-border-radius: 4px;
	border-radius: 4px;
	background-color: #34B8FC;
	text-align: center;
	color: white;
}

.banner {
	height: 8rem;
	background-image: url('./image/base.png');
	-webkit-background-size: 100% 100%;
	background-size: 100% 100%;
	background-repeat: no-repeat;
	background-position: center;
}

.hide {
	display: none;
}

.erro {
	color: red;
	padding: 0.5rem 1rem 0.5rem 1.5rem;
	display: none;
}

a {
	text-decoration: none;
}

.gz {
	padding: 1rem 0.5rem 1rem 1.5rem;
	color: #34B8FC;
}

.gzr {
	padding: 1rem 0.5rem 0.5rem 1.5rem;
	color: red;
}

.mt5 {
	margin-top: 5px
}

.fl {
	float: left !important;
}

</style>
</head>

<body class="bg-c-gray">
	<div id="h">
		<div class="banner"></div>
		<div>
			<div class="gzr hide" id="erro_tip"></div>
			<div class="ub bg-c-withe bd-t " style="margin-top: 1rem;">
				<div class="ld tx-l-r">手机号&nbsp;&nbsp;&nbsp;</div>
				<div class="rd">

					<input placeholder="请输入手机号码" id="phone" maxlength="11" type="tel"
						data-id="sj_eo" data-reg="/[0-9]{11}$/" name="phone" />
				</div>
			</div>
			<div class="erro" id="sj_eo">手机号错误</div>
		</div>
		<div>
			<div class="ub bg-c-withe bd-t ub-ac">
				<div class="ld tx-l-r">验证码&nbsp;&nbsp;&nbsp;</div>
				<div class="rd yzm-wid">
					<input placeholder="请输入验证码" maxlength="6" id="code" name="verifyCode" />
				</div>
				<div class="yzm bg-c-blue" id="yzm" data-info='yzm_info'>发送验证码</div>
				<div class="yzm bg-c-blue hide" id="yzm_info">
					<span id="yzm_count">60</span>秒可以重发
				</div>
			</div>
			<div class="erro" id="yzm_eo">手机验证码错误</div>
		</div>
		<div>
			<div class="ub bg-c-withe bd-t">
				<div class="ld tx-l-r">用户名&nbsp;&nbsp;&nbsp;</div>
				<div class="rd">
					<input placeholder="请输入用户名" name="accountName" id="accountName"
						data-id="yhm_eo" data-reg="/.+/" />
				</div>
			</div>
			<div class="gz bd-t" id="accountName_gz"></div>
			<div class="erro" id="yhm_eo">用户名错误</div>
		</div>
		<div>
			<div class="ub bg-c-withe bd-t">
				<div class="ld tx-l-r">密码&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</div>
				<div class="rd">
					<input placeholder="请输入密码 " name="password" type="password"
						data-reg="/.+/" data-id="mm_eo" id="passwd1" />
				</div>
			</div>
			<div class="gz bd-t" id="mm_gz"></div>
			<div class="erro" id="mm_eo">密码错误</div>
		</div>
		<div>
			<div class="ub bg-c-withe bd-t">
				<div class="ld tx-l-r">确认密码</div>
				<div class="rd">
					<input placeholder="请确认密码" type="password" name="newPassword"
						id="passwd2" data-id="mm2_eo" data-eq='passwd1' />
				</div>

			</div>
			<div class="erro" id="mm2_eo">密码不一致</div>
		</div>
		<div class="ub bg-c-withe bd-t">
			<div class="ld tx-l-r">邀请码&nbsp;&nbsp;&nbsp;</div>
			<div class="rd">
				<input readonly="readonly" placeholder="请输入邀请码" name="code" id="yqm" />
			</div>
		</div>
		<div class="hide bd-t" id="zcxy">
			<div class="ub pd-lr-10 ">
				<div class="" id="check" data-check='false'></div>
				<div class="ub-f1 " style="line-height: 3rem; font-size: 1.2rem;">
					我已阅读并同意<a class="fn-c-blue" href="javascript:void(0)" id="xy">《注册协议》</a>
				</div>
			</div>
		</div>
		<div id="register">马上注册</div>
	</div>
	<div id="rt" class="hide"></div>

	<div id="layer" style="width: 100%; position: fixed;  top :0; display:none;">
		<div class="ub ub-ac ub-pc" style="height:100%; ">
	    	<div style="width:18rem;   margin-left; background-color: white;border-radius: 6px;-webkit-border-radius: 6px;"> 
		       <div class="ub ub-ac ub-pc" style="padding: 2rem 1rem;font-size:1rem" id="content"></div>
		       <div id="btn1" class="ub ub-ac ub-pc" style="padding: 1rem 1rem; border-top:1px solid #EEEEEE;font-size:1.3rem">确认</div>
		    </div>
		</div>
	</div>
	
</body>

<script type="text/javascript">
	var D = document;
	var xy = D.getElementById('xy');
	var h = D.getElementById('h');
	var rt = D.getElementById('rt');
	var inps = D.getElementsByTagName('input');
	var check = D.getElementById('check');//协议
	var yzm = D.getElementById('yzm');
	var yzm_info = D.getElementById('yzm_info');
	var register = D.getElementById('register');
	var code = document.getElementById('code');
	var zcxy = D.getElementById('zcxy');
	var phone = D.getElementById('phone');
	var pwd = D.getElementById('passwd1');
	var mm_gz = D.getElementById('mm_gz');
	var accountName = D.getElementById('accountName');
	var accountName_gz = D.getElementById('accountName_gz');
	var erro_tip = D.getElementById('erro_tip');
	var pop = document.getElementById('pop');
	var xhr = getXMLHttpRequest();
	
	var layer=D.getElementById('layer');
	layer.style.height=document.documentElement.clientHeight+"px";
	layer.style.background="rgba(32, 13, 13, 0.51)";
	
	var content = D.getElementById('content');
	var btn1 = D.getElementById('btn1');
	
	// 获取url参数
	var query = GetRequest();
	// 添加邀请码
	var yqm = D.getElementById('yqm');
	yqm.value = query['yqm'] == undefined ? "" : query['yqm'];
	var url = '<%=url%>';
	var siteTitle = '<%=siteTitle%>';
	// 获取协议
	window.onload = function() {
		getXy();
	}
	
	btn1.onclick = function(){
		layer.style.display="none";
	}

	// addEvent(code,'click',getCode);
	addEvent(register, 'click', function() {
		if (val()) {
			valiCode()
		}
	});
	addEvent(xy, 'click', function() {
		h.style.display = 'none';
		rt.style.display = 'block';
		location.hash = 'xy';
	});
	addEvent(window, 'hashchange', function() {
		if (location.hash == "" || location.hash == "#") {
			h.style.display = 'block';
			rt.style.display = 'none';
		}
	});
	addEvent(zcxy, 'click', function() {
		if (check.dataset && check.dataset.check == 'true') {
			check.style.backgroundImage = "url('./image/icon_b1.png')";
			check.dataset.check = 'false';
		} else {
			check.style.backgroundImage = "url('./image/icon_b2.png')";
			check.dataset.check = 'true';
		}
	})

	for (var i = 0; i < inps.length; i++) {
		addEvent(inps[i], 'focus', function() {
			var eo = this.parentNode.parentNode.parentNode
					.querySelector('.erro');
			eo.style.display = "none";
		})
	}

	// 获取注册信息
	function registerInfo() {
		jsonp
				.connect(
						url + '/app/user/registerInfo.htm',
						"",
						function(reponse) {
							if (reponse.code == "000000") {
								accountName.dataset.reg = reponse.data.newUserNameRegex;
								accountName_gz.innerHTML = reponse.data.userNameRegexContent;
								pwd.dataset.reg = reponse.data.newPasswordRegex;
								mm_gz.innerHTML = reponse.data.passwordRegexContent;
							}

						}, null);
	}

	function val() {
		for (var i = 0; i < inps.length; i++) {
			if (inps[i].dataset) {
				if (inps[i].dataset.reg) {
					var reg = eval(inps[i].dataset.reg);
					if (!reg.test(inps[i].value)) {
						D.getElementById(inps[i].dataset.id).style.display = "block";
						return false;
					}
				}
				if (inps[i].dataset.eq) {
					var value = D.getElementById(inps[i].dataset.eq).value;
					if (inps[i].value != value) {
						D.getElementById(inps[i].dataset.id).style.display = "block";
						return false;
					}
				}
			}
		}
		return true;
	}

	// 增加事件
	function addEvent(element, type, func) {
		if (element.addEventListener) {
			element.addEventListener(type, func, false);
		} else if (element.attachEvent) {
			element.attachEvent('on' + type, func);
		} else {
			element[type] = func;
		}
	}

	// 增加验证码点击事件
	addEvent(yzm, 'click', function() {
		if (!!phone.value) {
			jsonp.connect(url + '/app/platinfo/getMobileCode.htm', "phone="
					+ phone.value + "&type=RZ", function(reponse) {
				if (reponse && reponse.code == "000000") {
					layer.style.display = "block";
					content.innerHTML = "验证码已发送到你的手机号码，请查收！";

				    yzm.style.display = "none";
					yzm_info.style.display = "block";
					reckTime(D.getElementById('yzm_count'));
				}
			}, null);
		} else {
			var eo = phone.parentNode.parentNode.parentNode
					.querySelector('.erro');
			eo.style.display = "block";
			return false;
		}
	})

	// 增加验证码等待事件
	function reckTime(obj) {
		var times = 60;
		var sid = setInterval(function() {
			times = times - 1;
			obj.innerHTML = times;
			if (times == 0) {
				clearInterval(sid);
				yzm.style.display = "block";
				yzm_info.style.display = "none";
				obj.innerHTML = 60;
			}
		}, 1000);
	}

	// 获取注册参数
	function serilz() {
		var obj = {};
		var parms = "";
		for (var i = 0; i < inps.length; i++) {
			if (inps[i].name) {
				obj[inps[i].name] = inps[i].value;
			}
		}
		for (o in obj) {
			parms += o + "=" + obj[o] + "&";
		}
		return parms.length > 0 ? parms.substring(0, parms.length - 1) : parms;
	}

	function getXMLHttpRequest() {
		return XMLHttpRequest ? new XMLHttpRequest() : new ActiveXObject(
				'Microsoft.XMLHTTP');
	}

	// 获取协议名称
	function getXy() {
		jsonp.connect(url + '/app/platinfo/initTermType.htm', "type=ZC",
				function(reponse) {
					if (reponse && reponse.code == "000000" && reponse.data) {
						zcxy.style.display = "block";
						setTimeout(function() {
							jsonp.connect(url + '/app/platinfo/agreement.htm',
									"type=" + reponse.data.agreementType,
									function(data) {
										if (data.data) {
											rt.innerHTML = data.data.content;
											setTimeout(registerInfo, 200);
										}
									}, null);
						}, 1000)
					} else {
						zcxy.style.display = "none";
					}
				}, null);
	}

	// 获取验证码
	function getCode() {
		if (!!phone.value) {
			jsonp.connect(url + '/app/platinfo/getMobileCode.htm', "phone="
					+ phone.value + "&type=RZ", null, null);
		} else {
			var eo = phone.parentNode.parentNode.parentNode
					.querySelector('.erro');
			eo.style.display = "block";
			return false;
		}
	}

	// 校验验证码是否正确
	function valiCode() {
		// 判断是否勾选了注册协议
		if (check.dataset.check != 'true') {
			content.innerHTML = "请先阅读注册协议并勾选！";
			layer.style.display = "block";
		} else if (!!phone.value) {
			jsonp
					.connect(
							url + '/app/platinfo/resetCheckVerifyCode.htm',
							"phone=" + phone.value + "&type=RZ&verifyCode="
									+ code.value,
							function(reponse) {
								if (reponse.code == "000000") {
									var parms = serilz();
									setTimeout(
											function() {
												jsonp
														.connect(
																url
																		+ '/app/platinfo/register.htm',
																parms,
																function(
																		reponse2) {
																	if (reponse2
																			&& reponse2.code == "000000") {
																		location.href = "success.jsp?phone="
																				+ phone.value;
																	} else {
																		//location.href="error.html";
																		content.innerHTML = reponse2.description;
																		layer.style.display = "block";
																	}
																}, null);
											}, 500)

								} else {
									D.getElementById('yzm_eo').style.display = "block";
								}
							}, null);
		} else {
			var eo = phone.parentNode.parentNode.parentNode
					.querySelector('.erro');
			eo.style.display = "block";
			return false;
		}
	}

	function getData(reponse) {
		jsonp.getData.apply(jsonp,[reponse]);
	}

	var jsonp = {
		el : "",
		func : {},
		connect : function(url, parms, succ, erro) {
			try {
				var sc = D.createElement('script');
				sc.src = url + '?callback=getData&' + parms;
				document.body.appendChild(sc);
				this.el = sc;
				succ ? this.func['succ'] = succ : null;
				erro ? this.func['err'] = erro : null;
			} catch (e) {
				this.error();
			}
		},
		getData : function(reponse) {
			this.success(reponse);
		},
		success : function(reponse) {
			if (reponse.code == "000000") {
				console.log(reponse);
				this.func['succ'] != undefined && this.func['succ'](reponse);
				this.func['succ'] = undefined;
			} else {
				this.error(reponse);
			}
			this.el.remove();

		},
		error : function(reponse) {
			this.func['err'] != undefined && this.func['err'](reponse);
			
			if (reponse.code == "000019")
			{
				content.innerHTML = "手机号码已注册";
			} else {
				content.innerHTML = reponse.description;
			}
			layer.style.display = "block";
			this.func['err'] = undefined;
		}
	}

	// 获取请求参数
	function GetRequest() {
		var url = location.search; //获取url中"?"符后的字串
		var theRequest = new Object();
		if (url.indexOf("?") != -1) {
			var str = url.substr(1);
			strs = str.split("&");
			for (var i = 0; i < strs.length; i++) {
				theRequest[strs[i].split("=")[0]] = (strs[i].split("=")[1]);
			}
		}
		return theRequest;
	}
	
</script>

</html>