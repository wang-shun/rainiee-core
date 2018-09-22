<%@ page import="com.dimeng.p2p.user.servlets.account.SettxPassword" %>
<%@page import="org.bouncycastle.util.encoders.Hex" %>
<%@page import="com.dimeng.p2p.service.PtAccountManage" %>
<%@page import="com.dimeng.p2p.common.DimengRSAPulicKey" %>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@ page import="com.dimeng.p2p.account.user.service.TxManage" %>
<%@ page import="com.dimeng.p2p.S61.entities.T6118" %>
<%@ page import="com.dimeng.p2p.S61.enums.T6118_F05" %>
<%@ page import="com.dimeng.p2p.S61.enums.T6118_F03" %>
<%@ page import="com.dimeng.p2p.user.servlets.guide.MailAuth" %>
<%@page import="com.dimeng.p2p.account.user.service.entity.PwdSafetyQuestion" %>
<%@page import="com.dimeng.p2p.account.user.service.achieve.PwdSafeCacheManage" %>
<%@page import="com.dimeng.p2p.variables.defines.pays.PayVariavle" %>
<%@include file="/WEB-INF/include/authenticatedSession.jsp"%>
<%@page import="com.dimeng.p2p.account.user.service.SafetyManage" %>
<%@page import="com.dimeng.p2p.account.user.service.entity.Safety" %>
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
        	    String mWithPsd = configureProvider.getProperty(PayVariavle.CHARGE_MUST_WITHDRAWPSD);
                if(!BooleanParser.parse(mWithPsd)){
                    response.sendRedirect(serviceSession.getController().getViewURI(request, MailAuth.class));
                    return;
                }
                // 是否托管项目
                String misTg = configureProvider.getProperty(SystemVariable.SFZJTG);
                if(BooleanParser.parse(misTg)){
                    response.sendRedirect(serviceSession.getController().getViewURI(request, MailAuth.class));
                    return;
                }
                TxManage txManage = serviceSession.getService(TxManage.class);
                T6118 t6118 = txManage.getVerifyEntity();// 获取认证情况
                SafetyManage userManage = serviceSession.getService(SafetyManage.class);
                Safety safety = userManage.get();
            
                boolean isRealName = "FUYOU".equals(configureProvider.getProperty(SystemVariable.ESCROW_PREFIX))?true:!StringHelper.isEmpty(safety.name);
                if (!(t6118.F05.equals(T6118_F05.WSZ) && T6110_F07.HMD != t6110.F07
                    && t6110.F06 == T6110_F06.ZRR && isRealName
                    && t6118.F03.equals(T6118_F03.TG))) {
                    response.sendRedirect(configureProvider.format(URLVariable.USER_INDEX));
                    return;
                }
                
				PtAccountManage ptAccountManage = serviceSession.getService(PtAccountManage.class);
				DimengRSAPulicKey publicKey = ptAccountManage.getPublicKey();
				String modulus = new String(Hex.encode(publicKey.getModulus().toByteArray()));
				String exponent = new String(Hex.encode(publicKey.getPublicExponent().toByteArray()));
				PwdSafeCacheManage pwdSafeService = serviceSession.getService(PwdSafeCacheManage.class);
				Map<String,List<PwdSafetyQuestion>> pwdSafeQuestions =pwdSafeService.getPasswordQuestionTypeMap();
				String CHARGE_MUST_WITHDRAWPSD = configureProvider.getProperty(PayVariavle.CHARGE_MUST_WITHDRAWPSD);
				String suffix = "";
				String step = "step_03";
				if(!"true".equalsIgnoreCase(CHARGE_MUST_WITHDRAWPSD)){
					suffix = "certification_steps02";
				}
				if("FUYOU".equals(configureProvider.getProperty(SystemVariable.ESCROW_PREFIX))){
					suffix = "certification_steps03";
					step = "step_02";
					if(!"true".equalsIgnoreCase(CHARGE_MUST_WITHDRAWPSD)){
						suffix = "certification_steps04";
					}
				}
			%>
			<input id="newTxPasswordRegexId" type="hidden" value="<%configureProvider.format(out,SystemVariable.NEW_TXPASSWORD_REGEX);%>" />
			<input id="txPasswordRegexContentId" type="hidden" value="<%configureProvider.format(out,SystemVariable.TXPASSWORD_REGEX_CONTENT);%>"/>
        <!--右边内容-->
        <div class="r_main">
        	<div class="certification_steps <%=suffix %>">
            	<span class="<%=step%>"></span>
            </div>
            <%if(t6118.F05==T6118_F05.WSZ && t6110.F06 == T6110_F06.ZRR){ %>
            <div class="user_mod border_t15">
                <div class="user_til"><i class="icon"></i><span class="gray3 f18">交易密码</span></div>
                <div class="mt50 mb30 tc"><span class="f16 gray3">交易密码</span><span class="gray9 f12 ml10">为了您的账户安全，请定期更换交易密码，并确保交易密码设置与登录密码不同</span></div>
                <div class="form_info certification_form">
                    <ul class="cell">
                        <li>
                            <div class="til"><span class="red">*</span> 交易密码：</div>
                            <div class="info">
                              <input type="password" maxlength="20" name="new" onchange="setSubmitFlg();" class="text required cpassword-f"  onselectstart="return false;" ondragenter="return false;" onpaste="return false;" mntest="/^<%=t6110.F02 %>$/" mntestmsg="密码不能与用户名一致" />
                              <p tip><%configureProvider.format(out,SystemVariable.TXPASSWORD_REGEX_CONTENT);%></p>
                              <p errortip class="red error_info"></p>
                              </div>
                        </li>
                        <li>
                            <div class="til"><span class="red">*</span> 重复密码：</div>
                            <div class="info">
                              <input type="password" maxlength="20" name="cnew" onchange="setSubmitFlg();" onselectstart="return false;" ondragenter="return false;" onpaste="return false;" class="text required password-bf"  />
                              <p errortip class="red error_info"></p>
                            </div>
                        </li>
                    </ul>
                    <div class="tc mt30"><a href="javascript:void(0);" onclick="settxPassword()" class="btn01">提交</a></div>
                </div>
                <div class="border_t_d mt50 lh24 pt20 pl20">
                    <span class="highlight">温馨提示：</span><br />
                    1. 请牢记您设置的交易密码，交易密码将用于投资，提现等重要操作。<br />
					2. 使用过程遇到问题，请联系在线客服，或拔打电话<%configureProvider.format(out,SystemVariable.SITE_CUSTOMERSERVICE_TEL);%>。
                </div>
            </div>
            <%}else{ %>
            <script type="text/javascript">
            	window.location.href="<%=controller.getStaticPath(request)%>/guide/mailAuth.html";            
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
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/security.js"></script>
<script type="text/javascript">
var modulus = "<%=modulus%>", exponent = "<%=exponent%>";
var key = RSAUtils.getKeyPair(exponent, '', modulus);
function setSubmitFlg(){
	flgs = true;
}

//异步的设置交易密码提交
function settxPassword(){
	if(!flgs){
		return;
	}
	var newPsw = $("input[name='new']");
	var cnewPsw = $("input[name='cnew']");
	if(newPsw.val().length <=0){
		showError(newPsw,"不能为空！");
		return false;
	}
	
	var myreg = txPasswordRegex;
	if(!myreg.test(newPsw.val()))
	{
		showError(newPsw,txErrorPwdMsg);
		return false;
	}
	
	if(cnewPsw.val().length<=0){
		showError(cnewPsw,"不能为空！");
		return false;
	}
	var data={"new":RSAUtils.encryptedString(key,newPsw.val()),"cnewPsw":RSAUtils.encryptedString(key,cnewPsw.val())};
	$.ajax({
		type:"post",
		dataType:"html",
		
		url:"<%=controller.getURI(request, SettxPassword.class)%>",
		data:data,
		success:function(data){
			if(data){
				var ct = eval('('+data+')');
				if(ct.length>0 && ct[0].num == 2){
					showError(newPsw,ct[0].msg);
					return false;
				}
				if(ct.length>0 && ct[0].num == 3){
					showError(cnewPsw,ct[0].msg);
					return false;
				}
				if(ct.length>0 && ct[0].num == 1){
					window.location.href='<%=controller.getURI(request, MailAuth.class)%>';
				}
			}
		}
	});
}

function showError(obj,msg){
	$error = $(obj).nextAll("p[errortip]");
	$tip = $(obj).nextAll("p[tip]");
	$error.addClass("red");
	$error.html(msg);
	$error.show();
	$tip.hide();
}
</script>
</body>
</html>