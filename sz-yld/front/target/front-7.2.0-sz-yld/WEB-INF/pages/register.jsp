<%@page import="com.dimeng.p2p.front.servlets.CheckEnterpriseExists"%>
<%@page import="com.dimeng.p2p.S50.entities.T5016"%>
<%@page import="com.dimeng.p2p.S50.enums.T5016_F12"%>
<%@page import="com.dimeng.p2p.modules.base.front.service.AdvertisementManage"%>
<%@page import="com.dimeng.p2p.common.DimengRSAPulicKey" %>
<%@page import="com.dimeng.p2p.common.FormToken" %>
<%@page import="com.dimeng.p2p.common.enums.TermType" %>
<%@page import="com.dimeng.p2p.common.enums.UserType" %>
<%@page import="com.dimeng.p2p.service.PtAccountManage" %>
<%@page import="org.bouncycastle.util.encoders.Hex" %>
<%@page import="com.dimeng.p2p.front.servlets.RegisterVerify" %>
<%@page import="com.dimeng.p2p.front.servlets.Term" %>
<%@page import="com.dimeng.p2p.front.servlets.CheckNameExists" %>
<%@page import="com.dimeng.p2p.front.servlets.CheckVerifyCode" %>
<%@page import="com.dimeng.p2p.front.servlets.CheckRecommendExists" %>
<%@page import="com.dimeng.p2p.modules.base.front.service.TermManage" %>
<%@page import="com.dimeng.p2p.S50.entities.T5017" %>
<%
    if (dimengSession != null && dimengSession.isAuthenticated()) {
        controller.sendRedirect(request, response, configureProvider.format(URLVariable.USER_INDEX));
        return;
    }
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>注册</title>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
</head>
<body>
<%
    String loginNameExists = ObjectHelper.convert(request.getAttribute("loginNameExists"), String.class);
	String enterpriseExists = ObjectHelper.convert(request.getAttribute("enterpriseExists"), String.class);
    String mobileExists = ObjectHelper.convert(request.getAttribute("mobileExists"), String.class);
    String mobile = request.getParameter("mobile");
    String type = request.getParameter("type");
    String userType = request.getParameter("userType");
    PtAccountManage ptAccountManage = serviceSession.getService(PtAccountManage.class);
    DimengRSAPulicKey publicKey = ptAccountManage.getPublicKey();
    String modulus = new String(Hex.encode(publicKey.getModulus().toByteArray()));
    String exponent = new String(Hex.encode(publicKey.getPublicExponent().toByteArray()));
    if (StringHelper.isEmpty(type)) {
        type = UserType.LC.name();
    }
    boolean has_business = Boolean.parseBoolean(configureProvider.getProperty(BusinessVariavle.IS_BUSINESS));
		/* String openId = dimengSession.getAttribute("openId");
	    String qqToken =dimengSession.getAttribute("qqToken");

	    dimengSession.setAttribute("openId","");
	    dimengSession.setAttribute("qqToken","");
	    //记录下用户到达过注册页面,之后则会去掉 用户中心的QQ自动登录和首页的QQ自动登录 
	    dimengSession.setAttribute("fromRegister","true"); */
	AdvertisementManage advertisementManage = serviceSession.getService(AdvertisementManage.class);
	T5016[] advertisements = advertisementManage.getAll_BZB(T5016_F12.PCREGISTER.name());
%>
<%@include file="/WEB-INF/include/registerHeader.jsp" %>
<form action="<%configureProvider.format(out,URLVariable.INDEX); %>/user/login.htm" method="post"
      id="loginForm" name="loginForm" target="_self">
    <input type="hidden" id="openId" name="openId" value=""/>
    <input type="hidden" id="accessToken" name="accessToken" value=""/>
    <input type="hidden" id="_z" name="_z" value="<%configureProvider.format(out,URLVariable.INDEX); %><%=controller.getStaticPath(request)%>/index.html"/>
</form>
<div class="main_bg clearfix" style="padding:0px;">
	<div class="red_bg clearfix registered_box" id="RedBg" style="width:100%; padding:40px 0; height:640px;">
		<a href="" id="RedBgURL" class="red_click" target="_blank"></a>
        <div id="grId" class="reg_mod registra_mod login_mod">
            <form id="form1" action="<%=configureProvider.format(URLVariable.REGISTER_SUBMIT) %>" method="post"
                  onsubmit="return onSubmit()">
                <%-- <input type="hidden" id="openId" name="openId" value="<%=openId ==null ? "" : openId%>" />
                <input type="hidden" id="accessToken" name="accessToken" value="<%=qqToken ==null ? "" : qqToken%>" /> --%>
                <input id="registerType" name="registerType"  value="PCZC" type="hidden"/>
                <div id="grToken"></div>
                <div class="reg_process clearfix">
                    <%
                        {
                            String errorMessage = controller.getPrompt(request, response, PromptLevel.ERROR);
                            if (!StringHelper.isEmpty(errorMessage)&& !"qy".equals(userType)) {
                    %>
                    <div class="popup_bg"></div>
					<div class="dialog">
					    <div class="title"><a href="javascript:void(0)" onclick="closeInfo()" class="out"></a>注册失败</div>
					    <div class="content">
					    	<div class="tip_information"> 
					          <div class="error"></div>
					          <div class="tips">
					              <span class="f20 gray3"><%StringHelper.filterHTML(out, errorMessage);%></span>
					          </div> 
					        </div>
							<div class="tc mt20"><a href="javascript:void(0)" onclick="closeInfo()" class="btn01">确定</a></div> 
					    </div>
					</div>
                    <%
                            }
                        }
                    %>
                    <div class="login_hd clearfix">
                    	<span class="white f24 bold fl">个人注册</span>
                    	<a href="javascript:showQyDiv();" class="agreement_t fr">企业注册</a>
                    </div>
                    <div class="login_form form_appearance">
                        <ul>
                            <li class="item">
                                <%--<div class="til">
                                    <span class="red">*</span>用户名：
                                </div>--%>
                                <div class="con">
                                    <%if (!StringHelper.isEmpty(loginNameExists)) {%>
                                            <div class="input focus_input">
                                                <i class="name_ico fl"></i>
                                                <input id="userId" type="text" class="text focus_text fl" maxlength="18"
                                                       value="<%StringHelper.filterQuoter(out,request.getParameter("accountName"));%>"
                                                       onblur="nameCheck('gr')" onfocus="recoverName('gr')"/><label for="userId">用户名</label>
                                                <span id="correct" class="correct_ico" style="display:none;"></span>
                                            </div>
                                    	<p class="red prompt"><%StringHelper.filterHTML(out, loginNameExists);%></p>
                                    <%} else {%>
                                            <div class="input focus_input">
                                                <i class="name_ico fl"></i>
                                                <input id="userId" type="text" class="text focus_text fl" maxlength="18"
                                                       value="<%StringHelper.filterHTML(out,request.getParameter("accountName"));%>"
                                                       onblur="nameCheck('gr')" onfocus="recoverName('gr')"/><label for="userId">用户名</label>
                                                <span id="correct" class="correct_ico" style="display:none;"></span>
                                            </div>
                                        <div class="mt5 fl">
                                            <span id="loginSuccess"></span>
                                        </div>
                                    	<p class="prompt">
                                    		<span class="grayd" id="loginNameError"><%configureProvider.format(out, SystemVariable.USERNAME_REGEX_CONTENT);%></span>
                                    	</p>
                                    <%}%>
                                    <input id="hdnUserId" name="accountName" type="hidden"/>
                                </div>
                            </li>
                            
                            <li class="item">
                                <%--<div class="til">
                                    <span class="red">*</span>密码：
                                </div>--%>
                                <div class="con">
                                    <div class="clearfix">
                                        <div class="input fl intensity_btn focus_input">
                                            <i class="password_ico fl"></i>
                                            <input id="passwordFirstId" type="password" class="text text3 focus_text fl"
                                             	onblur="passwordCheck('gr')" onfocus="recoverPassword('gr')" maxlength="20" autocomplete="off"/><label for="passwordFirstId">密码</label>
                                            <input id="passwordFirst" name="password" type="hidden"/>    
                                        </div>
                                        <div class="mt5 fl">
                                            <span id="passwordSuccess"></span>
                                        </div>
                                        <div class="intensity fl">
                                            <span class="weak">弱</span>
                                            <span class="medium">中</span>
                                            <span class="strong">强</span>
                                        </div>
                                    </div>
                                   	<p class="prompt">
                                        <span class="grayd" id="password-tip"><%configureProvider.format(out, SystemVariable.PASSWORD_REGEX_CONTENT);%></span>
                                    </p>
                                </div>
                            </li>
                            
                            <li class="item">
                                <%--<div class="til">
                                    <span class="red">*</span>确认密码：
                                </div>--%>
                                <div class="con">
                                	<div class="input focus_input">
                                    	<i class="password_ico fl"></i>
                                        <input id="passwordSecondId" type="password" maxlength="20" class="text text3 focus_text fl"
                                           		onblur="rePasswordCheck('gr')" onfocus="recoverTo('gr')" autocomplete="off"/><label for="passwordSecondId">确认密码</label>
                                        <input id="passwordSecond" name="newPassword" type="hidden"/>                  
                                    </div>
                                    <div class="mt5 fl">
                                           <span id="newPasswordSuccess"></span>
                                    </div>
                                    <p class="prompt"><span class="grayd">请再次输入密码</span></p>
                                </div>
                            </li>

                            <li class="item">
                                <%--<div class="til">邀请码<%if(has_business){ %>/工号<%} %>：</div>--%>
                                <div class="con">
                                    <div class="clearfix">
                                        <div class="input invite_input fl focus_input">
                                            <i class="invite_ico fl"></i><input name="code" type="text" class="text focus_text fl" id="invite_code"
                                                                             value="<%StringHelper.filterQuoter(out, request.getParameter("code"));%>"
                                                                             maxlength="11" onblur="codeCheck()" onfocus="recoverCode()"/><label for="invite_code">邀请码<%if(has_business){ %>/工号<%} %></label>
                                        </div>
                                        <div class="fl">有则填写</div>
                                        <div class="fl ml10 pr">
                                        <span class="agreement_t">什么是邀请码？</span>
                                        <span class="hover_tips">
                                            <div class="hover_tips_con">
                                                <div class="arrow"></div>
                                                <div class="border gray6">
                                                    <%configureProvider.format(out, SystemVariable.INVITE_CODE_DESC);%>
                                                </div>
                                            </div>
                                        </span>
                                    </div>
                                        <div class="mt5 fl">
                                            <span id="codeSuccess"></span>
                                        </div>
                                    </div>
                                    <p class="prompt"><span class="grayd">邀请码 /推广人手机号码<%if(has_business){ %>/ 业务员工号<%} %></span></p>
                                </div>
                            </li>

                            <%
                                {
                                    if (!configureProvider.getProperty(SystemVariable.SFXYYZM).equalsIgnoreCase("false")) {
                            %>
                            <li class="item">
                               <%-- <div class="til"><span class="red">*</span>验证码：</div>--%>
                                <div class="con">
                                    <div class="clearfix">
                                        <div class="input verify_input fl focus_input">
                                            <i class="verify_ico fl"></i>
                                            <input name="verifyCode" type="text" class="text focus_text fl" id="verifyCode2"
                                            	value="" onblur="verifyCodeCheck('form1')" onfocus="delErroInfo(this)" maxlength="6"/><label for="verifyCode2">验证码</label>
                                        </div>
                                        <div class="fl mr10">
                                            <img src="<%=controller.getURI(request, RegisterVerify.class)%>" class="border verifyImg" width="78"
                                                 height="34" title="点击刷新"
                                                 onclick='this.src="<%=controller.getURI(request, RegisterVerify.class)%>?"+Math.random()'
                                                 style="cursor: pointer;" id="verify-img"/>
                                        </div>
                                        <div class="fl">
                                            <a href="javascript:void(0)"
                                               onclick="anotherImg('<%=controller.getURI(request, RegisterVerify.class)%>?'+Math.random(),'form1')"
                                               class="agreement_t">换一张</a>
                                        </div>
                                        <div class="mt5 fl">
                                            <span class="verifySuccess" id="verifySuccess"></span>
                                        </div>
                                    </div>
                                    <p class="prompt"><span class="grayd">请填写图片中的字符</span></p>
                                </div>
                            </li>
                            <%
                                    }
                                }
                            %>
	                        <li class="item pt10">
	                        	<div class="con">
		                        <%
		                            String isCheckAgree="";
		                            TermManage termManage = serviceSession.getService(TermManage.class);
		                            T5017 term = termManage.get(TermType.ZCXY);
		                            if(term != null)
		                            {
		                                isCheckAgree= "true";
		                        %>
			                            <input name="iAgree" onclick="checkoxBtn('gr');" type="checkbox" id="iAgree" class="mr10"/><label for="iAgree">我已阅读并同意</label>
			                            <a target="_blank"
			                               href="<%=controller.getPagingItemURI(request, Term.class, TermType.ZCXY.name())%>"
			                               class="agreement_t">《<%=term.F01.getName()%>》</a>
			                            <input type="submit" id="sub-btn" class="login_bt mt5 btn_gray btn_disabled" disabled="disabled"
			                                   style="border: none;" value="立即注册"/>
			                        <%}else{%>
			                            <input type="submit" id="sub-btn" class="login_bt mt5" style="border: none;" value="立即注册"/>
			                        <%}%>
		                        </div>
							</li>
                        </ul>
                    </div>
                </div>
            </form>
        </div>
        <div id="qyId" class="reg_mod registra_mod login_mod" style="display:none;">
            <form id="form2" action="<%=configureProvider.format(URLVariable.QYREGISTER_SUBMIT) %>" method="post"
                  onsubmit="return onQySubmit()">
                <input id="registerType2" name="registerType"  value="PCZC" type="hidden"/>
                <div id="qyToken"></div>
                <div class="reg_process clearfix">
                    <%
                        {
                            String errorMessage = controller.getPrompt(request, response, PromptLevel.ERROR);
                            if (!StringHelper.isEmpty(errorMessage) && "qy".equals(userType)) {
                    %>
                    <div class="popup_bg"></div>
					<div class="dialog">
					    <div class="title"><a href="javascript:void(0)" onclick="closeInfo()" class="out"></a>注册失败</div>
					    <div class="content">
					    	<div class="tip_information"> 
					          <div class="error"></div>
					          <div class="tips">
					              <span class="f20 gray3"><%StringHelper.filterHTML(out, errorMessage);%></span>
					          </div> 
					        </div>
							<div class="tc mt20"><a href="javascript:void(0)" onclick="closeInfo()" class="btn01">确定</a></div> 
					    </div>
					</div>
                    <%
                            }
                        }
                    %>
                    <div class="login_hd clearfix"><span class="white f24 bold fl">企业注册</span>
                    	<a href="javascript:showGrDiv();" class="agreement_t fr">个人注册</a>
                   	</div>
                    <div class="login_form form_appearance">
                        <ul>
                        	<li class="item">
                                <div class="con">
                                    <%if (!StringHelper.isEmpty(enterpriseExists)) {%>
                                            <div class="input focus_input">
                                                <i class="name_ico fl"></i>
                                                <input id="enterpriseNameId" type="text" name="enterpriseName"  class="text focus_text fl" maxlength="18"
                                                       value="<%StringHelper.filterQuoter(out,request.getParameter("enterpriseName"));%>"
                                                       onblur="enterpriseNameCheck()" onfocus="recoverEnterpriseName()"/><label for="enterpriseNameId">企业名称</label>
                                                <span id="correct3" class="correct_ico" style="display:none;"></span>
                                            </div>
                                    	<p class="red prompt"><%StringHelper.filterHTML(out, enterpriseExists);%></p>
                                    <%} else {%>
                                            <div class="input focus_input">
                                                <i class="name_ico fl"></i>
                                                <input id="enterpriseNameId" type="text" name="enterpriseName" class="text focus_text fl" maxlength="18"
                                                       value="<%StringHelper.filterHTML(out,request.getParameter("enterpriseName"));%>"
                                                       onblur="enterpriseNameCheck()" onfocus="recoverEnterpriseName()"/><label for="enterpriseNameId">企业名称</label>
                                                <span id="correct3" class="correct_ico" style="display:none;"></span>
                                            </div>
                                        <div class="mt5 fl">
                                            <span id="enterpriseSuccess"></span>
                                        </div>
                                    	<p class="prompt">
                                    		<span class="grayd" id="enterpriseNameError">请输入企业名称</span>
                                    	</p>
                                    <%}%>
                                </div>
                            </li>
                            <li class="item">
                                <%--<div class="til">
                                    <span class="red">*</span>用户名：
                                </div>--%>
                                <div class="con">
                                    <%if (!StringHelper.isEmpty(loginNameExists)) {%>
                                            <div class="input focus_input">
                                                <i class="name_ico fl"></i>
                                                <input id="qyUserId" type="text" class="text focus_text fl" maxlength="18"
                                                       value="<%StringHelper.filterQuoter(out,request.getParameter("accountName"));%>"
                                                       onblur="nameCheck('qy')" onfocus="recoverName('qy')"/><label for="qyUserId">用户名</label>
                                                <span id="correct2" class="correct_ico" style="display:none;"></span>
                                            </div>
                                    	<p class="red prompt"><%StringHelper.filterHTML(out, loginNameExists);%></p>
                                    <%} else {%>
                                            <div class="input focus_input">
                                                <i class="name_ico fl"></i>
                                                <input id="qyUserId" type="text" class="text focus_text fl" maxlength="18"
                                                       value="<%StringHelper.filterHTML(out,request.getParameter("accountName"));%>"
                                                       onblur="nameCheck('qy')" onfocus="recoverName('qy')"/><label for="qyUserId">用户名</label>
                                                <span id="correct2" class="correct_ico" style="display:none;"></span>
                                            </div>
                                        <div class="mt5 fl">
                                            <span id="loginSuccess2"></span>
                                        </div>
                                    	<p class="prompt">
                                    		<span class="grayd" id="loginNameError2"><%configureProvider.format(out, SystemVariable.USERNAME_REGEX_CONTENT);%></span>
                                    	</p>
                                    <%}%>
                                    <input id="hdnQyUserId" name="accountName" type="hidden"/>
                                </div>
                            </li>
                            
                            <li class="item">
                                <%--<div class="til">
                                    <span class="red">*</span>密码：
                                </div>--%>
                                <div class="con">
                                    <div class="clearfix">
                                        <div class="input fl intensity_btn focus_input">
                                            <i class="password_ico fl"></i>
                                            <input id="qyPasswordFirstId" type="password" class="text text3 focus_text fl"
                                             	onblur="passwordCheck('qy')" onfocus="recoverPassword('qy')" maxlength="20" autocomplete="off"/><label for="qyPasswordFirstId">密码</label>
                                            <input id="qyPasswordFirst" name="password" type="hidden"/>    
                                        </div>
                                        <div class="mt5 fl">
                                            <span id="passwordSuccess2"></span>
                                        </div>
                                        <div class="intensity fl">
                                            <span class="weak">弱</span>
                                            <span class="medium">中</span>
                                            <span class="strong">强</span>
                                        </div>
                                    </div>
                                   	<p class="prompt">
                                        <span class="grayd" id="password-tip2"><%configureProvider.format(out, SystemVariable.PASSWORD_REGEX_CONTENT);%></span>
                                    </p>
                                </div>
                            </li>
                            
                            <li class="item">
                                <%--<div class="til">
                                    <span class="red">*</span>确认密码：
                                </div>--%>
                                <div class="con">
                                	<div class="input focus_input">
                                    	<i class="password_ico fl"></i>
                                        <input id="qyPasswordSecondId" type="password" maxlength="20" class="text text3 focus_text fl"
                                           		onblur="rePasswordCheck('qy')" onfocus="recoverTo('qy')" autocomplete="off"/><label for="qyPasswordSecondId">确认密码</label>
                                        <input id="qyPasswordSecond" name="newPassword" type="hidden"/>                  
                                    </div>
                                    <div class="mt5 fl">
                                           <span id="newPasswordSuccess2"></span>
                                    </div>
                                    <p class="prompt"><span class="grayd">请再次输入密码</span></p>
                                </div>
                            </li>

                            <%
                                {
                                    if (!configureProvider.getProperty(SystemVariable.SFXYYZM).equalsIgnoreCase("false")) {
                            %>
                            <li class="item">
                               <%-- <div class="til"><span class="red">*</span>验证码：</div>--%>
                                <div class="con">
                                    <div class="clearfix">
                                        <div class="input verify_input fl focus_input">
                                            <i class="verify_ico fl"></i>
                                            <input name="verifyCode" type="text" class="text focus_text fl" id="qyVerifyCode"
                                            	value="" onblur="verifyCodeCheck('form2')" onfocus="delErroInfo(this)" maxlength="6"/><label for="qyVerifyCode">验证码</label>
                                        </div>
                                        <div class="fl mr10">
                                            <img src="<%=controller.getURI(request, RegisterVerify.class)%>" class="border verifyImg" width="78"
                                                 height="34" title="点击刷新"
                                                 onclick='this.src="<%=controller.getURI(request, RegisterVerify.class)%>?"+Math.random()'
                                                 style="cursor: pointer;" id="verify-img2"/>
                                        </div>
                                        <div class="fl">
                                            <a href="javascript:void(0)"
                                               onclick="anotherImg('<%=controller.getURI(request, RegisterVerify.class)%>?'+Math.random(),'form2')"
                                               class="agreement_t">换一张</a>
                                        </div>
                                        <div class="mt5 fl">
                                            <span class="verifySuccess" id="verifySuccess2"></span>
                                        </div>
                                    </div>
                                    <p class="prompt"><span class="grayd">请填写图片中的字符</span></p>
                                </div>
                            </li>
                            <%
                                    }
                                }
                            %>
	                        <li class="item pt10">
	                        	<div class="con">
		                        <%
		                            if(term != null)
		                            {
		                                isCheckAgree= "true";
		                        %>
			                            <input name="iAgree" onclick="checkoxBtn('qy');" type="checkbox" id="iAgree2" class="mr10"/><label for="iAgree2">我已阅读并同意</label>
			                            <a target="_blank"
			                               href="<%=controller.getPagingItemURI(request, Term.class, TermType.ZCXY.name())%>"
			                               class="agreement_t">《<%=term.F01.getName()%>》</a>
			                            <input type="submit" id="sub-btn2" class="login_bt mt5 btn_gray btn_disabled" disabled="disabled"
			                                   style="border: none;" value="立即注册"/>
			                        <%}else{%>
			                            <input type="submit" id="sub-btn2" class="login_bt mt5" style="border: none;" value="立即注册"/>
			                        <%}%>
		                        </div>
							</li>
                        </ul>
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>
<%@include file="/WEB-INF/include/footer.jsp" %>
<!--底部-->
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/security.js"></script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/dialog.js"></script>
<%
if("simple".equals(configureProvider.getProperty(SystemVariable.PAGE_TEMPLATE))){
%>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/simple/js/common.js"></script>
<%}else{%>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/common.js"></script>
<%}%>
<script type="text/javascript">
    var _nURL = '<%=controller.getURI(request, CheckNameExists.class)%>';
    var _eURL = '<%=controller.getURI(request, CheckEnterpriseExists.class)%>';
    var _vURL = '<%=controller.getURI(request, CheckVerifyCode.class)%>';
    var _tURL = '<%=controller.getURI(request, RegisterVerify.class)%>';
    var _checkRecodURL = '<%=controller.getURI(request, CheckRecommendExists.class)%>';
    var modulus = "<%=modulus%>", exponent = "<%=exponent%>";
    var key = RSAUtils.getKeyPair(exponent, '', modulus);
    var registerFlage = <%configureProvider.format(out,SystemVariable.SFXYYZM);%>;
    <!--用户名用正则式匹配-->
    var newUserNameRegex = <%configureProvider.format(out,SystemVariable.NEW_USERNAME_REGEX);%>;
    var userNameRegexContent = '<%configureProvider.format(out,SystemVariable.USERNAME_REGEX_CONTENT);%>';
    <!--密码用正则式匹配-->
    var newPasswordRegex = <%configureProvider.format(out,SystemVariable.NEW_PASSWORD_REGEX);%>;
    var passwordRegexContent = '<%configureProvider.format(out,SystemVariable.PASSWORD_REGEX_CONTENT);%>';
	var has_business = '<%=has_business%>';
    var is_check_agree ='<%=isCheckAgree%>';
    var userType = "<%=userType%>";
    
	var pic_list=[];
	<%if(advertisements == null){%>
	pic_list.push({"img":"/images/zcbg_pic.jpg","URL":"javascript:void(0);"});
	<%}else{
		for (T5016 advertisement : advertisements) {
	        if (advertisement == null) {
	            continue;
	        }%>
	        <%if (!StringHelper.isEmpty(advertisement.F04)) {%>
	        	pic_list.push({"img":"<%=fileStore.getURL(advertisement.F05)%>","URL":"http://<%StringHelper.filterHTML(out, advertisement.F04.replaceAll("http://", ""));%>"});
            <%}else{%>
            	pic_list.push({"img":"<%=fileStore.getURL(advertisement.F05)%>","URL":"javascript:void(0);"});
	        <%}%>
	<%}}%>
	var picnum=Math.floor(Math.random()*pic_list.length);
	$("#RedBg").css({"background-image":"url("+pic_list[picnum].img+")"});
	if(pic_list[picnum].URL == "javascript:void(0);"||pic_list[picnum].URL == ""){
		$("#RedBgURL").removeAttr("target");
	}
	$("#RedBgURL").attr("href",pic_list[picnum].URL);
</script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/register.js"></script>
</body>
</html>
