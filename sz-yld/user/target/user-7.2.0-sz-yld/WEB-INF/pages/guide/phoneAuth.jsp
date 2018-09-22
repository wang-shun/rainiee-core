<%@page import="com.dimeng.p2p.user.servlets.VerifyCommon"%>
<%@page import="com.dimeng.p2p.common.FormToken" %>
<%@page import="com.dimeng.p2p.user.servlets.account.Send" %>
<%@page import="com.dimeng.p2p.user.servlets.account.BindPhone" %>
<%@page import="com.dimeng.p2p.S61.enums.T6118_F03" %>
<%@page import="com.dimeng.p2p.S61.enums.T6118_F02" %>
<%@page import="com.dimeng.p2p.user.servlets.guide.TradePwdAuth" %>
<%@page import="com.dimeng.p2p.account.user.service.TxManage" %>
<%@page import="com.dimeng.p2p.S61.entities.T6118" %>
<%@page import="com.dimeng.p2p.variables.defines.pays.PayVariavle" %>
<%@page import="com.dimeng.p2p.account.user.service.SafetyManage" %>
<%@page import="com.dimeng.p2p.account.user.service.entity.Safety" %>
<%@include file="/WEB-INF/include/authenticatedSession.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>我的<%configureProvider.format(out,SystemVariable.SITE_NAME);%></title>
<%@include file="/WEB-INF/include/meta.jsp"%>
<%@include file="/WEB-INF/include/style.jsp"%>
</head>
<body>
<div id="token"><%=FormToken.hidden(serviceSession.getSession()) %></div>
<!--顶头部条-->
<%@include file="/WEB-INF/include/header.jsp"%>
<!--顶头部-->

<!--主体内容-->
<div class="main_bg clearfix">
    <div class="user_wrap w1002 clearfix">
    	<!--左菜单-->
        <%@include file="/WEB-INF/include/menu.jsp"%>
        <!--左菜单-->
        <%
            String mPhone = configureProvider.getProperty(PayVariavle.CHARGE_MUST_PHONE);
            TxManage txManage = serviceSession.getService(TxManage.class);
            T6118 t6118 = txManage.getVerifyEntity();// 获取认证情况
            SafetyManage userManage = serviceSession.getService(SafetyManage.class);
            Safety safety = userManage.get();
            
            boolean isSmed = false;
            if("FUYOU".equals(configureProvider.getProperty(SystemVariable.ESCROW_PREFIX))){
                isSmed = true;
            }else if(!StringHelper.isEmpty(safety.name)){
                isSmed = true;
            }
            if (!(BooleanParser.parse(mPhone) && t6118.F03.equals(T6118_F03.BTG)
                && T6110_F07.HMD != t6110.F07 && t6110.F06 == T6110_F06.ZRR && isSmed)) {
                response.sendRedirect(configureProvider.format(URLVariable.USER_INDEX));
                return;
            }
            
        
			String CHARGE_MUST_WITHDRAWPSD = configureProvider.getProperty(PayVariavle.CHARGE_MUST_WITHDRAWPSD);
			String suffix = "";
			String step = "step_02";
			if(!"true".equalsIgnoreCase(CHARGE_MUST_WITHDRAWPSD)){
				suffix = "certification_steps02";
				step = "step_02";
			}
			if("FUYOU".equals(configureProvider.getProperty(SystemVariable.ESCROW_PREFIX))){
				suffix = "certification_steps03";
				step = "step_01";
				if(!"true".equalsIgnoreCase(CHARGE_MUST_WITHDRAWPSD)){
					suffix = "certification_steps04";
				}
			}
			if("yeepay".equalsIgnoreCase(configureProvider.getProperty(SystemVariable.ESCROW_PREFIX))){
			    step = "step_02";
				if("true".equalsIgnoreCase(configureProvider.getProperty(PayVariavle.CHARGE_MUST_EMAIL))){
					suffix = "certification_steps02";
				}else{
				    suffix = "certification_steps05";
				}
			}
		%>
        <!--右边内容-->
        <div class="r_main">
        	<div class="certification_steps <%=suffix %>">
            	<span class="<%=step%>"></span>
            </div>
            <%if(t6118.F03==T6118_F03.BTG && t6110.F06 == T6110_F06.ZRR){ %>
            <div class="user_mod border_t15">
                <div class="user_til"><i class="icon"></i><span class="gray3 f18">手机认证</span></div>
                <div class="mt50 mb30 tc"><span class="f16 gray3">设置手机号信息</span><span class="gray9 f12 ml10">用于登录、接收充值、投资、项目到期、提现等重要通知提醒</span></div>
                <div class="form_info certification_form">
                    <ul class="cell">
                        <li>
                            <div class="til"><span class="red">*</span> 手机号码：</div>
                            <div class="info">
                              <input type="text" class="text required phonenumber" name="binphpne" maxlength="11"/>
                              <p errortip class="error_tip"></p>
                            </div>
                        </li>
                        <%if (!configureProvider.getProperty(SystemVariable.SFXYYZM).equalsIgnoreCase("false")) {%>
                        <li>
                            <div class="til"><span class="red">*</span> 验证码：</div>
                            <div class="info">
                              <input type="text" name="bindPhoneVerify"  maxlength="<%=systemDefine.getVerifyCodeLength()%>" class="text code verify-code" style="width:133px;" />
                              <img alt="验证码" title="点击刷新" id="_verifyImg_5" src="<%=controller.getURI(request, VerifyCommon.class)%>?verifyType=bindPhone" onclick="this.src='<%=controller.getURI(request, VerifyCommon.class)%>?'+Math.random()+'&verifyType=bindPhone'" style="cursor: pointer;" width="100" height="28" class="border mr5">看不清楚？<a href="javascript:void(0)" onclick="$('#_verifyImg_5').attr('src','<%=controller.getURI(request, VerifyCommon.class)%>?'+Math.random()+'&verifyType=bindPhone')" class="highlight">换一张</a>
                              <p errortip class="error_tip"></p>
                            </div>
                        </li>
                        <%}%>
                        <li>
                            <div class="til"><span class="red">*</span> 手机验证码：</div>
                            <div class="info">
                              <input type="text" maxlength="6" class="text required p_text verify-code" style="width:133px;" name="bphoneCode"/><input type="button" onclick="bandPhone(this);" value="获取验证码" class="btn05"/>
                              <p errortip class="error_tip"></p>
                            </div>
                        </li>
                    </ul>
                    <div class="tc mt30"><a href="javascript:void(0);" onclick="submitBandPhone(this);" class="btn01 sumbitForme">提交认证</a></div>
                </div>
                <div class="border_t_d mt50 lh24 pt20 pl20">
                    <span class="highlight">温馨提示：</span><br />
                    1、请填写真实有效的手机号，手机号将作为验证用户身份的重要手段。<br />
					2、手机认证过程遇到问题，请联系在线客服，或电话<%=configureProvider.getProperty(SystemVariable.SITE_CUSTOMERSERVICE_TEL) %>。
                </div>
            </div>
            <%}else if(!"true".equalsIgnoreCase(CHARGE_MUST_WITHDRAWPSD)){ %>
            <script type="text/javascript">
            	window.location.href="<%=controller.getStaticPath(request)%>/guide/mailAuth.html";            
            </script>
            <%}else{ %>
            <script type="text/javascript">
            	window.location.href="<%=controller.getStaticPath(request)%>/guide/tradePwdAuth.html";            
            </script>
            <%} %>
        </div>
        <!--右边内容-->
    </div>        
    </div>
<!--主体内容-->

<!--底部-->
<%@include file="/WEB-INF/include/footer.jsp"%>
<!--底部-->
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/validation.js"></script>
<script type="text/javascript">
var flagSfxyyzm = <%configureProvider.format(out,SystemVariable.SFXYYZM);%>;
var wait3 = 60;
//获取手机验证码 绑定手机
function bandPhone(evn){
	var bemil = $("input[name='binphpne']");
	if(!checkBandPhone(bemil)){
		return false;
	}
	
	var bindPhoneVerify = null;
	var bindPhoneVerify1 = null;
	if(!(flagSfxyyzm  != "undefined" && flagSfxyyzm  == false)) {
		bindPhoneVerify1 = $("input[name='bindPhoneVerify']");
		bindPhoneVerify = bindPhoneVerify1.val();
		if (bindPhoneVerify.length <= 0) {
			showError(bindPhoneVerify1, "不能为空 !");
			flushVerifyCode(bindPhoneVerify1);
			return false;
		}
	}
	var tokenValue = $("input[name='<%=FormToken.parameterName()%>']").val();
	var data3={"tokenKey":tokenValue,"type":"bind","emil":"","phone":bemil.val(),"verifyType":"BIND_PHONE","verifyCode":bindPhoneVerify};
	$.ajax({
		type:"post",
		dataType:"html",
		url:"<%=controller.getURI(request, Send.class)%>",
		data:data3,
		success:function(data){
			var ct = eval('('+data+')');
			//替换token
			setToken(ct);

			if(ct.length>0 && ct[0].num != 1){
				if(ct.length>0 && ct[0].num == 0){
					showError(bemil,ct[0].msg);
					return false;
				}else if(ct.length>0 && ct[0].num == 2){
					showError(bindPhoneVerify1,ct[0].msg);
					flushVerifyCode(bindPhoneVerify1);
					return false;
				}
				else if(ct.length>0 && ct[0].num == 4) {
					showError(bemil,ct[0].msg);
					return false;
				}
			}
			if(ct.length>0 && ct[0].num == 1){
				sendclick3(evn);
			}
		}
	});
}

function showError(obj,msg){
	$error = $(obj).nextAll("p[errortip]");
	$error.addClass("red");
	$error.html(msg);
	$error.show();
}

function setToken(ct)
{
	if(ct.length>0)
	{
		$("#token").html(ct[0].tokenNew);
	}
}

function checkBandPhone(bemil){
	if(bemil.val().length<=0){
		showError(bemil,"请输入手机号码！");
		return false;
	}
	var myreg = /^(13|14|15|17|18)[0-9]{9}$/;
	if(!myreg.test(bemil.val()))
	{
		showError(bemil,"手机号码不正确！");
		return false;
	}
	return true;	
}

<%--绑定手机时计时器 --%>
function sendclick3(obj){
	var evn = $(obj);
	if (wait3 == 0) {
		evn.removeAttr("disabled");
		evn.val("获取验证码");
		wait3 = 60;
	} else {
		evn.attr("disabled", true);
		evn.val("(" + wait3 + ")秒重新获取");
		wait3--;
		setTimeout(function() {
			sendclick3(evn);
		},
		1000);
	}
}

function submitBandPhone(evn){
	
	var bemil = $("input[name='binphpne']");
	var bphoneCode = $("input[name='bphoneCode']");
	if(!checkBandPhone(bemil)){
		return false;
	}
	if(bphoneCode.val().length<=0){
		showError(bphoneCode,"不能为空 !");
		return false;
	}
	var data3={"bphoneCode":bphoneCode.val(),"binphpne":bemil.val()};
	$.ajax({
		type:"post",
		dataType:"html",
		url:"<%=controller.getURI(request, BindPhone.class)%>",
		data:data3,
		success:function(data){
			var ct = eval('('+data+')');
			if(ct.length>0 && ct[0].num == 0){
				showError(bphoneCode,ct[0].msg);
				return false;
			}
			if(ct.length>0 && ct[0].num == 1){
				window.location.href=ct[0].url;
			}
		}
	});
}

//刷新验证码
function flushVerifyCode(obj){
    if (!(flagSfxyyzm != "undefined" && flagSfxyyzm == false)) {
        obj.next("img").click();
    }
}
</script>
</body>
</html>
