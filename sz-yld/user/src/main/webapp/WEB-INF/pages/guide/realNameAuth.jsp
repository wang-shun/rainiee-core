<%@ page import="com.dimeng.p2p.user.servlets.account.UpdateName" %>
<%@ page import="com.dimeng.p2p.user.servlets.guide.PhoneAuth" %>
<%@ page import="com.dimeng.p2p.account.user.service.TxManage" %>
<%@ page import="com.dimeng.p2p.S61.entities.T6118" %>
<%@ page import="com.dimeng.p2p.S61.enums.T6118_F02" %>
<%@page import="com.dimeng.p2p.variables.defines.pays.PayVariavle" %>
<%@include file="/WEB-INF/include/authenticatedSession.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>我的<%configureProvider.format(out,SystemVariable.SITE_NAME);%></title>
<%@include file="/WEB-INF/include/meta.jsp"%>
<%@include file="/WEB-INF/include/style.jsp"%>
</head>
<body>
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
			TxManage txManage = serviceSession.getService(TxManage.class);
			T6118 t6118 = txManage.getVerifyEntity();// 获取认证情况
			String CHARGE_MUST_WITHDRAWPSD = configureProvider.getProperty(PayVariavle.CHARGE_MUST_WITHDRAWPSD);
			String suffix = "";
			if(!"true".equalsIgnoreCase(CHARGE_MUST_WITHDRAWPSD)){
				suffix = "certification_steps02";
			}
			if("FUYOU".equals(configureProvider.getProperty(SystemVariable.ESCROW_PREFIX))){
				suffix = "certification_steps03";
			}
			if("yeepay".equalsIgnoreCase(configureProvider.getProperty(SystemVariable.ESCROW_PREFIX))){
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
            	<span class="step_01"></span>
            </div>
            <%if(t6110.F06 == T6110_F06.ZRR && t6118.F02==T6118_F02.BTG && !"FUYOU".equals(configureProvider.getProperty(SystemVariable.ESCROW_PREFIX))){ %>
            <div class="user_mod border_t15">
                <div class="user_til"><i class="icon"></i><span class="gray3 f18">实名认证</span></div>
                <div class="mt50 mb30 tc"><span class="f16 gray3">认证个人信息</span><span class="gray9 f12 ml10">为保障您的账户和资金安全，需确认您的投资身份</span></div>
                <div class="form_info certification_form">
                    <ul class="cell">
                        <li>
                            <div class="til"><span class="red">*</span> 姓名：</div>
                            <div class="info">
                              <input type="text" class="text required" maxlength="10" name="name" mtest="/^[\u4e00-\u9fa5]{2,}$/" mtestmsg="请输入合法的姓名"/>
                              <p errortip class="error_tip"></p>
                            </div>
                        </li>
                        <li>
                            <div class="til"><span class="red">*</span> 身份证号：</div>
                            <div class="info"><input type="text" class="text required idcard" maxlength="18" name="idCard" />
                            <p errortip class="error_tip"></p></div>
                        </li>
                    </ul>
                    <div class="tc mt30"><a href="javascript:void(0);" id="submitBtn" class="btn01">提交认证</a></div>
                </div>
                <div class="border_t_d mt50 lh24 pt20 pl20">
                    <span class="highlight">温馨提示：</span><br />
                    1、认证通过后，资金仅能提现到该姓名下银行卡中，请勿填写他人姓名。<br />
                    2、实名认证过程遇到问题，请联系在线客服，或电话<%=configureProvider.getProperty(SystemVariable.SITE_CUSTOMERSERVICE_TEL) %>。
                </div>
            </div>
            <%}else{ %>
            <script type="text/javascript">
            	window.location.href="<%=controller.getStaticPath(request)%>/guide/phoneAuth.html";
            </script>
            <%}%>
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
$(function(){
	$("#submitBtn").click(function(){
		updateName();
	});
});
//异步的实名认证提交
function updateName(){
	
	var name = $("input[name='name']");
	var idCard = $("input[name='idCard']");
	if(name.val().length<=0){
		showError(name,"不能为空！");
		return false;
	}
	if(idCard.val().length<=0){
		showError(idCard,"不能为空！");
		return false;
	}
	$err = $(idCard).next();
	if(!isIdCardNot(idCard.val(),$err))
	{
		return false;
	}
	var data={"idCard":idCard.val(),"name":name.val()};
	$.ajax({
		type:"post",
		dataType:"html",
		url:"<%=controller.getURI(request, UpdateName.class)%>",
		data:data,
		success:function(data){
			if(data){
				var ct = eval('('+data+')');
				if(ct.length>0 && ct[0].num == 3){
					showError(name,ct[0].msg);
					return false;
				}
				if(ct.length>0 && ct[0].num == 0){
					showError(idCard,ct[0].msg);
					return false;
				}
				if(ct.length>0 && ct[0].num == 1){
					window.location.href='<%=controller.getViewURI(request, PhoneAuth.class)%>';
				}
			}
		}
	});
}

function showError(obj,msg){
	$error = $(obj).next();
	$error.addClass("red");
	$error.html(msg);
	$error.show();
}

</script>
</body>
</html>