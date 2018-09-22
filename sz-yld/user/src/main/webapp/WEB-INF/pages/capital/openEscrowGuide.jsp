<%@page import="com.dimeng.util.ObjectHelper"%>
<%@page import="com.dimeng.p2p.modules.account.pay.service.entity.OpenEscrowGuideEntity"%>
<%@page import="com.dimeng.util.StringHelper"%>
<%@page import="com.dimeng.framework.resource.PromptLevel"%>
<%@page import="com.dimeng.p2p.common.FormToken"%>
<%@page import="com.dimeng.p2p.user.servlets.VerifyCommon" %>
<%@page import="com.dimeng.p2p.variables.defines.URLVariable"%>
<%@page import="com.dimeng.p2p.variables.defines.SystemVariable"%>
<%@page import="com.dimeng.p2p.account.user.service.BankCardManage" %>
<%@page import="com.dimeng.p2p.account.user.service.entity.Bank" %>
<%@include file="/WEB-INF/include/authenticatedSession.jsp" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>我的<%configureProvider.format(out, SystemVariable.SITE_NAME);%>_开通托管账户
    </title>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
</head>
<body>
<%
	OpenEscrowGuideEntity entity = ObjectHelper.convert(request.getAttribute("openEscrowGuideEntity"),OpenEscrowGuideEntity.class);

	BankCardManage bankCardManage = serviceSession.getService(BankCardManage.class);
	Bank[] banks = bankCardManage.getBank();
%>
<div id="token">
    <%=FormToken.hidden(serviceSession.getSession()) %>
</div>

<%@include file="/WEB-INF/include/header.jsp" %>
<div class="main_bg clearfix">
    <div class="user_wrap w1002 clearfix">
        <%@include file="/WEB-INF/include/menu.jsp" %>
        <div class="r_main">
            <div class="user_mod border_t15">
                <div class="user_til"><i class="icon"></i><span class="gray3 f18">协议支付签约</span></div>
                <div class="topup_problem mt20"><i class="problem_icon"></i>开通协议支付功能才可以进行充值操作,否则不能进行充值业务,请您认真填写以下信息,确保您开户成功</div>
                <form action="<%=configureProvider.format(URLVariable.ESCROW_URL_USERREGISTER)%>" method="post" onsubmit="return onSubmit();">
	                <div class="mt20 mb30"><span class="f16 gray3">身份信息</span></div>
	                <div class="form_info certification_form">
	                	<%if(t6110.F06.equals(T6110_F06.ZRR)) { %>
	                    <ul class="cell">
	                        <li>
	                            <div class="til"><span class="red">*</span>真实姓名：</div>
	                            <div class="info">
		                            	<input type="text" value="<%=entity.getRealName()%>" onkeyup="paramCheck();" name="realName" 
		                            		class="text yhgl_ser required max-length-10"
                               				mtest="/^[\u4e00-\u9fa5]{2,}$/" mtestmsg="请输入合法的姓名" />
				                        <p tip></p>
				                        <p errortip class="" style="display: none"></p>
			                    </div>
		                    <div class="clear"></div>
	                        </li>
	                         <li>
	                            <div class="til"><span class="red">*</span>身份证号：</div>
	                            <div class="info">
	                            	<input type="text" value="<%=entity.getIdentificationNo() %>" onkeyup="paramCheck();" name="identificationNo" 
	                            		maxlength="<%=entity.getMaxLength()%>" class="text yhgl_ser required idcard"/>
	                            	<p tip></p>
                        			<p errortip class="" style="display: none"></p>
	                            </div>
	                            <div class="clear"></div>
	                        </li>
	                    </ul>
	                    <%} else { %>
	                    	<ul class="cell">
	                        <li>
	                            <div class="til"><span class="red">*</span>法人姓名：</div>
	                            <div class="info">
		                            	<input type="text" value="<%=entity.getRealName()%>" disabled="disabled" name="realName" 
		                            		class="text yhgl_ser required"/>
				                        <p tip></p>
				                        <p errortip class="" style="display: none"></p>
			                    </div>
		                    <div class="clear"></div>
	                        </li>
	                         <li>
	                            <div class="til"><span class="red">*</span>法人身份证号：</div>
	                            <div class="info">
	                            	<input type="text" value="<%=entity.getIdentificationNo() %>" disabled="disabled" name="identificationNo" 
	                            		maxlength="<%=entity.getMaxLength()%>" class="text yhgl_ser required"/>
	                            	<p tip></p>
                        			<p errortip class="" style="display: none"></p>
	                            </div>
	                            <div class="clear"></div>
	                        </li>
	                    </ul>
	                    <%} %>
	                </div>
	                
	                <div class="mt20 mb30"><span class="f16 gray3">银行卡信息</span></div>
	                <div class="form_info certification_form">
	                	<%-- <%if(t6110.F06.equals(T6110_F06.ZRR)) { %> --%>
	                    <ul class="cell">
	                        <li>
	                            <div class="til"><span class="red">*</span>开户银行：</div>
	                            <div class="info">
		                            <%-- 	<input type="text" value="<%=entity.getRealName()%>" onkeyup="paramCheck();" name="bankName" 
		                            		class="text yhgl_ser required max-length-10"
                               				mtest="/^[\u4e00-\u9fa5]{2,}$/" mtestmsg="请输入合法的姓名" /> --%>
                               				<select name="bankCode" id="bankCode" style="width:179px;" class="select" > <!-- onchange="getSubbrank(this);"  -->
                               				<%if(StringHelper.isEmpty(entity.getBankCode())){ %>
                               				  <option value="0">请选择</option>
                               				<%} %>
								           <%for(Bank bank: banks) {%>
								           <option value="<%=bank.id %>"  <%if(!StringHelper.isEmpty(entity.getBankCode()) && bank.code.equals(entity.getBankCode())) {%>   selected="selected"  <%} %> ><%=bank.name%></option>
								           <%} %> 
								        </select>
				                        <p tip></p>
				                        <p errortip class="" style="display: none"></p>
			                    </div>
		                    <div class="clear"></div>
	                        </li>
	                         <li>
	                            <div class="til"><span class="red">*</span>银行卡号：</div>
	                            <div class="info">
	                            	<input type="text" value="<%=StringHelper.isEmpty(entity.getBankCard())?"":entity.getBankCard() %>"  id="bankCard" name="bankCard" 
	                            		maxlength="30" class="text yhgl_ser required max-length-30" onkeyup="this.value=this.value.replace(/\D/g,'').replace(/....(?!$)/g,'$& ')"/>
	                            	<p tip></p>
                        			<p errortip class="" style="display: none"></p>
	                            </div>
	                            <div class="clear"></div>
	                        </li>
	                    </ul>
	                    <%-- <%} else { %>
	                    	<ul class="cell">
	                        <li>
	                            <div class="til"><span class="red">*</span><%=entity.getRealNameLabel() %>：</div>
	                            <div class="info">
		                            	<input type="text" value="<%=entity.getRealName()%>" disabled="disabled" name="realNameFZRR" 
		                            		class="text yhgl_ser required"/>
				                        <p tip></p>
				                        <p errortip class="" style="display: none"></p>
			                    </div>
		                    <div class="clear"></div>
	                        </li>
	                         <li>
	                            <div class="til"><span class="red">*</span><%=entity.getIdentificationNoLabel() %>：</div>
	                            <div class="info">
	                            	<input type="text" value="<%=entity.getIdentificationNo() %>" disabled="disabled" name="identificationNoFZRR" 
	                            		maxlength="<%=entity.getMaxLength()%>" class="text yhgl_ser required"/>
	                            	<p tip></p>
                        			<p errortip class="" style="display: none"></p>
	                            </div>
	                            <div class="clear"></div>
	                        </li>
	                    </ul>
	                    <%} %> --%>
	                </div>
	                
	                <div class="mt20 mb30"><span class="f16 gray3">验证手机</span></div>
	                <div class="form_info certification_form">
	                    <ul class="cell">
	                        <li>
	                            <div class="til"><span class="red">*</span>手机号：</div>
	                            <div class="info">
	                            	<input type="text" name="mobile" maxlength="11" onkeyup="paramCheck();" class="text required phonenumber " value="<%=entity.getMobile()%>"/>
	                            	<input name="input" id="btnVerifyCode" type="button" onclick="onSubmitLines(this)" value="获取签约验证码" class="btn05"/>
	                            	<p tip>银行卡绑定的预留手机号可以提高签约成功率</p>
                        			<p errortip class="" style="display: none"></p>
	                            </div>
	                            <div class="clear"></div>
	                        </li>
	                     	<%if (!configureProvider.getProperty(SystemVariable.SFXYYZM).equalsIgnoreCase("false")) { %>
	                        <li>
	                            <div class="til"><span class="red">*</span>验证码：</div>
	                            <div class="info">
	                                <input name="eUpdateVerify" type="text" onkeyup="paramCheck();" 
	                                       maxlength="<%=systemDefine.getVerifyCodeLength()%>" class="text code required verify-code"
	                                       style="width:153px;"/>
										<span>
											<img alt="验证码" id="_verifyImg_1"
	                                             src="<%=controller.getURI(request, VerifyCommon.class)%>?verifyType=bindPhone"
	                                             alt="验证码" title="点击刷新" width="80" height="31"
	                                             onclick="this.src='<%=controller.getURI(request, VerifyCommon.class)%>?'+Math.random()+'&verifyType=bindPhone'"
	                                             style="cursor: pointer;"/>
											<a href="javascript:void(0)"
	                                           onclick="$('#_verifyImg_1').attr('src','<%=controller.getURI(request, VerifyCommon.class)%>?'+Math.random()+'&verifyType=bindPhone')"
	                                           class="blue ml10 blue_line">换一张</a>
										</span>
	                                <p tip></p>
	                                <p errortip class="" style="display: none"></p>
	                            </div>
	                            <div class="clear"></div>
	                        </li>
	                        <%} else {%>
	                        <li style="display: none">
	                            <div class="clear"></div>
	                        </li>
	                        <%}%>
	                        <li>
	                            <div class="til"><span class="red">*</span>手机验证码：</div>
	                            <div class="info">
	                            	<input type="text" name="verifyCode" class="text required p_text verify-code" onkeyup="paramCheck();" style="width:133px;" maxlength="<%=systemDefine.getVerifyCodeLength()%>"/>
                            		
                            		<p tip></p>
                        			<p errortip class="" style="display: none"></p>
	                            </div>
	                            <div class="clear"></div>
	                        </li>
	                    </ul>
	                    
	                </div>
	                <div class="tc mt30">
	                	<input type="submit" id="openSumbit" style="cursor: pointer;" value="立即签约"
	                		disabled="disabled" class="btn03 btn03_gray"/>
                  	</div>
                </form>
            </div>
        </div>
    </div>
</div>

<%
    String infoMsg = controller.getPrompt(request, response, PromptLevel.INFO);
%>
<div id="dialog" style="<%=StringHelper.isEmpty(infoMsg)?"display: none;":"" %>">
    <div class="popup_bg"></div>
    <div class="dialog">
        <div class="title"><a href="javascript:void(0);" class="out" onclick="closeDiv()"></a>提示</div>
        <div class="content">
            <div class="tip_information">
                <div class="doubt"></div>
                <div class="tips">
                    <p class="f20 gray33" id="con_error"><%=StringHelper.isEmpty(infoMsg) ? "" : infoMsg %>
                    </p>
                </div>
            </div>
            <div class="tc mt20">
                <a class="btn01"
                   href="javascript:void(0);" onclick="closeDiv()">确定</a>
            </div>
        </div>
    </div>
</div>

<%
    String errorMsg = controller.getPrompt(request, response, PromptLevel.ERROR);
%>
<div id="dialog" style="<%=StringHelper.isEmpty(errorMsg)?"display: none;":"" %>">
    <div class="popup_bg"></div>
    <div class="dialog">
        <div class="title"><a href="javascript:void(0);" class="out" onclick="closeDiv()"></a>提示</div>
        <div class="content">
            <div class="tip_information">
                <div class="error"></div>
                <div class="tips">
                    <p class="f20 gray33" id="con_error"><%=StringHelper.isEmpty(errorMsg) ? "" : errorMsg %>
                    </p>
                </div>
            </div>
            <div class="tc mt20">
                <a class="btn01"
                   href="javascript:void(0);" onclick="closeDiv()">确定</a>
            </div>
        </div>
    </div>
</div>


<%-- <%
	if(t6110.F06.equals(T6110_F06.FZRR) && StringHelper.isEmpty(entity.getIdentificationNo()))
	{%>
	    <div id="dialogTx">
		    <div class="popup_bg"></div>
		    <div class="dialog">
		        <div class="title"><a href="javascript:void(0);" class="out" onclick="closeDiv()"></a>提示</div>
		        <div class="content">
		            <div class="tip_information">
		                <div class="doubt"></div>
		                <div class="tips">
		                    <p class="f20 gray33" id="con_error">请先完善企业用户相关信息，再开通托管账户
		                    </p>
		                </div>
		            </div>
		            <div class="tc mt20">
		                <a class="btn01" href="<%=controller.getViewURI(request, com.dimeng.p2p.user.servlets.account.QyBases.class) %>">确定</a>
		            </div>
		        </div>
		    </div>
		</div>
<%}%> --%>

<%@include file="/WEB-INF/include/footer.jsp" %>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/validation.js"></script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/openEscrowGuide-allin.js"></script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/zhankai.js"></script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/security.js"></script>
<script type="text/javascript">
	<%--获取手机验证码URL--%>
	var _sendUrl="<%=controller.getStaticPath(request)%>/fuyou/send.htm";
	<%--登录URL--%>
	var _loginUrl="<%=controller.getStaticPath(request)%>/login.htm";
	<%--入后台前，校验实名与手机号--%>
	var _escrowCheckUrl = "<%=controller.getStaticPath(request)%>/capital/allinpaySign.htm";
	<%--获取TOKEN的NAME--%>
	var tokenName = "<%=FormToken.parameterName()%>";
	<%--是否需要验证码--%>
	var flagSfxyyzm = <%configureProvider.format(out,SystemVariable.SFXYYZM);%>;
    <%--绑定手机时--%>
    var wait3 = 60;
    <%--验证码--%>
    var myregCode = /^[a-zA-Z0-9]+$/;
    <%--自然人还是非自然人--%>
    var _isFZRR = "<%=t6110.F06 %>";
    
    var _finalFZRR = "<%=T6110_F06.FZRR%>";
    
    //初始化页面，删除获取验证码按钮样式
    var btnVerifyCode = $("#btnVerifyCode");
    btnVerifyCode.removeAttr("disabled");
    btnVerifyCode.removeClass("btn05");
    btnVerifyCode.addClass("btn01");
</script>

<script type="text/javascript">

	function closeDiv() {
		$("#dialog").hide();
		$("#dialogTx").hide();
	}
	
</script>

</body>
</html>