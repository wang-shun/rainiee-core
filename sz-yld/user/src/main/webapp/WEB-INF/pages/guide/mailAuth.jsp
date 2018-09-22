<%@page import="com.dimeng.p2p.user.servlets.VerifyCommon"%>
<%@page import="com.dimeng.p2p.common.FormToken" %>
<%@page import="com.dimeng.p2p.user.servlets.account.Send" %>
<%@page import="com.dimeng.p2p.account.user.service.SafetyManage"%>
<%@page import="com.dimeng.p2p.account.user.service.entity.Safety"%>
<%@page import="com.dimeng.p2p.S61.enums.T6118_F02" %>
<%@page import="com.dimeng.p2p.variables.defines.pays.PayVariavle" %>
<%@include file="/WEB-INF/include/authenticatedSession.jsp"%>
<%@page import="com.dimeng.p2p.account.user.service.SafetyManage" %>
<%@page import="com.dimeng.p2p.account.user.service.entity.Safety" %>
<%@page import="com.dimeng.p2p.S61.enums.T6118_F05" %>
<%@page import="com.dimeng.p2p.S61.enums.T6118_F04" %>
<%@page import="com.dimeng.p2p.S61.enums.T6118_F03" %>
<%@page import="com.dimeng.p2p.account.user.service.TxManage" %>
<%@ page import="com.dimeng.p2p.S61.entities.T6118" %>
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
<!--顶头部条-->
<!--主体内容-->
<div class="main_bg clearfix">
    <div class="user_wrap w1002 clearfix">
    	<!--左菜单-->
        <%@include file="/WEB-INF/include/menu.jsp"%>
        <!--左菜单-->
        <%
        String mYxrz = configureProvider.getProperty(PayVariavle.CHARGE_MUST_EMAIL);
        TxManage txManage = serviceSession.getService(TxManage.class);
        T6118 t6118 = txManage.getVerifyEntity();//获取认证情况
        SafetyManage safetyManage = serviceSession.getService(SafetyManage.class);
		Safety data = safetyManage.get();
        boolean isRealName = "FUYOU".equals(configureProvider.getProperty(SystemVariable.ESCROW_PREFIX))?true:!StringHelper.isEmpty(data.name);
        boolean isWithPsd = BooleanParser.parse(configureProvider.getProperty(PayVariavle.CHARGE_MUST_WITHDRAWPSD))?(t6118.F05.equals(T6118_F05.YSZ)):true;
        if(!(BooleanParser.parse(mYxrz) && t6118.F04.equals(T6118_F04.BTG) 
            && T6110_F07.HMD != t6110.F07 && t6110.F06 == T6110_F06.ZRR 
            && isRealName && t6118.F03.equals(T6118_F03.TG) && isWithPsd)){
            response.sendRedirect(configureProvider.format(URLVariable.USER_INDEX));
            return;
        }
        
			
			String email=safetyManage.getUnBindEmail();
			String CHARGE_MUST_WITHDRAWPSD = configureProvider.getProperty(PayVariavle.CHARGE_MUST_WITHDRAWPSD);
			String suffix = "";
			String suffixIndex = "4";
			if(!"true".equalsIgnoreCase(CHARGE_MUST_WITHDRAWPSD)){
				suffix = "certification_steps02";
				suffixIndex = "3";
			}
			if("FUYOU".equals(configureProvider.getProperty(SystemVariable.ESCROW_PREFIX))){
				suffix = "certification_steps03";
				suffixIndex = "3";
				if(!"true".equalsIgnoreCase(CHARGE_MUST_WITHDRAWPSD)){
					suffix = "certification_steps04";
					suffixIndex = "2";
				}
			}
			if("yeepay".equalsIgnoreCase(configureProvider.getProperty(SystemVariable.ESCROW_PREFIX))){
			    suffixIndex = "3";
			    suffix = "certification_steps02";
			}
		%>
        <!--右边内容-->
        <div class="r_main">
        	<div class="certification_steps <%=suffix %>">
            	<span class="step_0<%=suffixIndex %>"></span>
            </div>
            <%if(T6118_F02.TG.name().equals(data.isEmil)){%>
            <script type="text/javascript">
	   			window.location.href="<%=controller.getStaticPath(request)%>/index.html";
	   		</script>
            <%}else{%>
            <div class="user_mod border_t15">
                <div class="user_til"><i class="icon"></i><span class="gray3 f18">绑定邮箱</span></div>
                <div class="mt50 mb30 tc"><span class="f16 gray3">绑定邮箱</span><span class="gray9 f12 ml10">绑定后可用邮箱登录</span></div>
                <div class="form_info certification_form">
                    <ul class="cell">
                        <li>
                            <div class="til"><span class="red">*</span> 邮箱：</div>
                            <div class="info">
                              <input type="text" class="text required e-mail" name="bemil" maxlength="26" value="<%StringHelper.filterHTML(out, email);%>"/>
                              <p errortip class="error_tip"></p>
                            </div>
                        </li>
                        <%if (!configureProvider.getProperty(SystemVariable.SFXYYZM).equalsIgnoreCase("false")) {%>
                        <li>
                            <div class="til"><span class="red">*</span> 验证码：</div>
                            <div class="info">
                              <input type="text" name="bindEmailVerify"  maxlength="<%=systemDefine.getVerifyCodeLength()%>" class="text code verify-code" style="width:133px;" />
                              <img alt="验证码" title="点击刷新" id="_verifyImg_6" src="<%=controller.getURI(request, VerifyCommon.class)%>?verifyType=bindEmail" onclick="this.src='<%=controller.getURI(request, VerifyCommon.class)%>?'+Math.random()+'&verifyType=bindEmail'" style="cursor: pointer;" width="100" height="28" class="border mr5">看不清楚？<a href="javascript:void(0)" onclick="$('#_verifyImg_6').attr('src','<%=controller.getURI(request, VerifyCommon.class)%>?'+Math.random()+'&verifyType=bindEmail')" class="highlight">换一张</a>
                              <p errortip class="error_tip"></p>
                            </div>
                        </li>
                        <%}%>
                    </ul>
                    <div class="tc mt30"><a href="javascript:void(0);" onclick="bandEmil(this);" class="btn01 sumbitForme">发送验证邮件</a></div>
                </div>
                <div class="border_t_d mt50 lh24 pt20 pl20">
                    <span class="highlight">温馨提示：</span><br />
                    1、请填写真实有效的邮箱地址，邮箱地址将作为验证用户身份的重要手段。<br />
					2、邮箱地址认证过程遇到问题，请联系在线客服，或电话<%=configureProvider.getProperty(SystemVariable.SITE_CUSTOMERSERVICE_TEL) %>。
                </div>
            </div>
            <%} %>
        </div>
        <!--右边内容-->
    </div>        
</div>
<!--主体内容-->

<!-- 弹窗开始 -->
<div class="popup_bg"  style="display: none;"></div>
<div class="dialog" style="display: none;" >
	<div class="title"><a href="javascript:void(0)" class="out" onclick="closeInfo()"></a>提交成功</div>
    <div class="content">
      <div class="tip_information">
      	  <div class="successful"></div>
          <div class="tips">
          	<p class="f20 gray33">邮箱验证</p>
          	<p class="pt10">系统已经向您的邮箱(<span id="showEmail" class="red"></span>)发送了一封验证邮件，请进入邮箱点击链接以激活绑定邮箱！</p>
          	<p class="pt10">如果长时间没有收到邮件，请点击再次发送！</p>
          </div>
      </div>  
      <div class="tc mt20"><a href="javascript:void(0)" id="ok" class="btn01">再发一封</a><a href="javascript:void(0)" id="cancel" class="btn01 btn_gray ml20">返回首页</a></div>
    </div>
</div>
<!-- 弹窗结束 -->

<!--底部-->
<%@include file="/WEB-INF/include/footer.jsp"%>
<!--底部-->
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/validation.js"></script>
<script type="text/javascript">
var flagSfxyyzm = <%configureProvider.format(out,SystemVariable.SFXYYZM);%>;

$(function(){
	$("#ok").click(function(){
		closeInfo();
		window.location.reload();
	});
	$("#cancel").click(function(){
		location.href ="<%configureProvider.format(out,URLVariable.INDEX);%>";
	});
});

//绑定邮箱获取邮箱验证码
function bandEmil(evn){
	var bemil = $("input[name='bemil']");
	if(!checkEmailFormat(bemil)){
		return;
	}
	
	var bindEmailVerify;
	var bindEmailVerify1;
	if(!(flagSfxyyzm  != "undefined" && flagSfxyyzm  == false)){
		bindEmailVerify1 =$("input[name='bindEmailVerify']");
		bindEmailVerify = bindEmailVerify1.val();
		if(bindEmailVerify.length<=0){
			showError(bindEmailVerify1,"验证码不能为空!");
			return;
		}
	}
	var tokenValue = $("input[name='<%=FormToken.parameterName()%>']").val();
	var data3={"tokenKey":tokenValue,"type":"bind","emil":bemil.val(),"phone":"","verifyType":"BIND_EMAIL","verifyCode":bindEmailVerify};
	$.ajax({
		type:"post",
		dataType:"html",
		url:"<%=controller.getURI(request, Send.class)%>",
		data:data3,
		success:function(data){
			if(data){
				var ct = eval('('+data+')');
				//替换token
				setToken(ct);
				if(ct.length>0 && ct[0].num != 1){
					if(ct.length>0 && ct[0].num == 0){
						showError(bemil,ct[0].msg);
					}else if(ct.length>0 && ct[0].num == 2){
						showError(bindEmailVerify1,ct[0].msg);
					}else if(ct.length>0 && ct[0].num == 4)
					{
						showError(bemil,ct[0].msg);
					}
					refreshCode();
					return false;
				}
				if(ct.length>0 && ct[0].num == 1){
					$("#showEmail").html(bemil.val());
					$("#showEmail").addClass("error_tip");
					$("div.popup_bg").show();
					$("div.dialog").show();
				}
			}
		}
	});
}

function closeInfo(){
	$("div.popup_bg").hide();
	$("div.dialog").hide();
	refreshCode();
}

function refreshCode(){
	if(!(flagSfxyyzm  != "undefined" && flagSfxyyzm  == false)){
		$("#_verifyImg_6").attr("src","<%=controller.getURI(request, VerifyCommon.class)%>?"+Math.random()+"&verifyType=bindEmail");
		$("input[name='bindEmailVerify']").val("");
	}
}

function checkEmailFormat(bemil){
	if(bemil.val().length<=0){
		showError(bemil,"请输入邮箱地址!");
		return false;
	}
	/* var myreg = /^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,4}$/; */
	var myreg = /^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/;
	if(!myreg.test(bemil.val()))
	{
		showError(bemil,"邮箱地址不正确!");
		return false;
	}
	return true;
}

function showError(obj,msg){
	$error = $(obj).nextAll("p[errortip]");
	$error.addClass("red");
	$error.html(msg);
	$error.show();
}

function setToken(ct){
	if(ct.length>0){
		$("#token").html(ct[0].tokenNew);
	}
}

</script>
</body>
</html>