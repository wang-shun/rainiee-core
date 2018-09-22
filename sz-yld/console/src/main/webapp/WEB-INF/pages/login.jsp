<%@page import="com.dimeng.p2p.common.RSAUtils" %>
<%@page import="com.dimeng.p2p.console.servlets.LoginVerify" %>
<%@page import="com.dimeng.p2p.console.servlets.common.Index" %>
<%@page import="com.dimeng.p2p.common.RSAUtils" %>
<%@page import="org.bouncycastle.util.encoders.Hex" %>
<%@page import="com.dimeng.p2p.service.PtAccountManage" %>
<%@page import="com.dimeng.p2p.common.DimengRSAPulicKey" %>
<%
    if (dimengSession != null && dimengSession.isAuthenticated()) {
        controller.sendRedirect(request, response, "/console/main.html");
        return;
    }
    PtAccountManage ptAccountManage = serviceSession.getService(PtAccountManage.class);
    DimengRSAPulicKey publicKey = (DimengRSAPulicKey) ptAccountManage.getPublicKey();
    String modulus = new String(Hex.encode(publicKey.getModulus().toByteArray()));
    String exponent = new String(Hex.encode(publicKey.getPublicExponent().toByteArray()));
    String userName = request.getParameter("username");
    if (!StringHelper.isEmpty(userName)) {
        userName = RSAUtils.decryptStringByJs(userName);
    }
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
</head>
<body style="background:#04529d" class="login-body">
<div class="login-top-container">
    <div class="w1002 pr">
        <div class="login-logo pt40"><a><img
                src="<%=fileStore.getURL(configureProvider.getProperty(SystemVariable.HTDLLOGO))%>"/></a></div>
        <div class="pa right0 top-60 login-title-color"><span class="f40 display-ib h40 lh40 va-middle">V7.0<%if("simple".equals(configureProvider.getProperty(SystemVariable.PAGE_TEMPLATE))){%>标准版<%}else{%>豪华版<%}%></span><span
                class="f60 display-ib h40 lh40 pl10 pr10 va-middle">一</span><span
                class="f30 display-ib h40 lh40 va-middle">后台管理系统</span></div>
    </div>
</div>

<!--登录内容-->
<div class="login-content-container">
    <div class="login-c-imac"><img src="images/login_imac.png"/></div>
    <div class="login-form-container">
        <form action="<%=controller.getURI(request, Login.class) %>" method="post" onsubmit="return onSubmit()">
            <div class="form-box">
                <ul class="pl20">
                    <li class="mb30">
                        <span class="title display-ib white w90 f20 va-middle">用户名：</span>

                        <div class="info display-ib radius-4 overflow-h va-middle">
                            <i class="icon-i w30 h36 m5 va-middle login-user-icon"></i>
                            <input type="hidden" id="userId" name="username"/>
                            <input id="txUserId" value="<%StringHelper.filterHTML(out, userName); %>"
                                   type="text" class="text w180 pl10 gray6 border-l-s" onblur="accountCheck();"/>
                            <i class="icon-i w30 h36 mt5 va-middle"></i>
                        </div>

                    </li>
                    <li class="mb30">
                        <span class="title display-ib white w90 f20 va-middle">密　码：</span>

                        <div class="info display-ib radius-4 overflow-h va-middle">
                            <i class="icon-i w30 h36 m5  va-middle login-password-icon"></i>
                            <input id="passwordId" type="password"
                                   class="text w180 pl10 gray6 border-l-s" onselectstart="return false;"
                                   ondragenter="return false;" onpaste="return false;" onfocus="accountCheck();"
                                   onblur="passwordCheck();" autocomplete="off"/>
                            <input id="truePwd" name="password" type="hidden"/>
                            <i class="icon-i w30 h36 mt5 va-middle"></i>
                        </div>
                    </li>
                    <%
                        {
                            if (!configureProvider.getProperty(SystemVariable.SFXYYZM).equalsIgnoreCase("false")) {
                    %>
                    <li class="mb30">
                        <span class="title display-ib white w90 f20 va-middle">验证码：</span>

                        <div class="info display-ib radius-4 overflow-h va-middle info-code">
                            <i class="icon-i w30 h30 m5 mt10 va-middle login-code-icon"></i>
                            <input name="verifyCode" maxlength="<%=systemDefine.getVerifyCodeLength() %>" type="text"
                                   class="text w100 pl10 gray6 border-l-s" onblur="verifyCheck();"/>
                        </div>
                        <span class="display-ib va-middle ml10"><img
                                src="<%=controller.getURI(request, LoginVerify.class) %>" alt="验证码" title="点击刷新"
                                onclick='this.src="<%=controller.getURI(request, LoginVerify.class) %>?"+Math.random()'
                                width="88" height="48"/></span>
                    </li>
                    <%
                            }
                        }
                    %>
                    <li class="mb10">
                        <%String errorMessage = controller.getPrompt(request, response, PromptLevel.ERROR);%>
                        <%String errorMsg = "other".equals(request.getParameter("errormsg"))? "您的帐号已在另一地点登录，您被迫下线" : ""; %>
                        <p class="ml100 red pr20" id="error"><%StringHelper.filterHTML(out, errorMessage==null ? errorMsg : errorMessage);%></p>
                    </li>
                    <li>
                        <div class="pl90">
                            <input name="login" type="submit" class="submit-btn display-ib radius-6" value="登 录">
                        </div>
                    </li>
                </ul>
            </div>
        </form>
    </div>
</div>
<div class="login-foot-container">
    <p class="pt30">
    	<a href="<%configureProvider.format(out, URLVariable.INDEX);%>"><%configureProvider.format(out, SystemVariable.SITE_NAME); %></a>
        |
        <span class="gray6">轻松借贷 投资无忧 © <%configureProvider.format(out, URLVariable.INDEX);%> All Rights Reserved</span>
    </p>
</div>
<!--登录内容 结束-->
<%@include file="/WEB-INF/include/script.jsp" %>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/security.js"></script>
<script type="text/javascript">
    var verify = /^\d{<%=systemDefine.getVerifyCodeLength() %>}$/;
    var isNull = /^[\s]{0,}$/;
    var loginName = /^[a-z]([\w]*)$/i;
    var zwReg = /^[\u4e00-\u9fa5]/g;
    function accountCheck() {
        var val = $("#txUserId").val();
        var p = $("#error");
        if (isNull.test(val)) {
            p.html("用户名不能为空");
            //$("#txUserId").next("i").removeClass("login-right-icon").addClass("login-error-icon");
            return false;
        } else if (zwReg.test(val))
        {
            p.html("用户名不能包含中文");
            return false;
        }
        else {
        	p.html("");
            //$("#txUserId").next("i").removeClass("login-error-icon").addClass("login-right-icon");
        }
        return true;
    }
    function passwordCheck() {
    	
        if(!accountCheck()){
        	return false;
        }
        var val = $("#passwordId").val();
        var p = $("#error");
        if (isNull.test(val)) {
            p.html("密码不能为空");
            //$("input[name='password']").next("i").removeClass("login-right-icon").addClass("login-error-icon");
            return false;
        } else {
        	p.html("");
            //$("input[name='password']").next("i").removeClass("login-error-icon").addClass("login-right-icon");
        }
        return true;
    }
    function verifyCheck() {
        //是否需要验证码.
        var flag = <%configureProvider.format(out,SystemVariable.SFXYYZM);%>;
        if (flag != "undefined" && flag == false) {
            return true;
        }
        var val = $("input[name='verifyCode']").val();
        var p = $("#error");
        if (isNull.test(val)) {
            p.html("验证码不能为空");
            return false;
        } else if (!verify.test(val)) {
            p.html("您输入的验证码有误");
            return false;
        }else{
        	p.html("");
        }
        return true;
    }
    function onSubmit() {
        if (accountCheck() && passwordCheck() && verifyCheck()) {
            $(".submit-btn").attr("disabled", true);
            var modulus = "<%=modulus%>", exponent = "<%=exponent%>";
            var key = RSAUtils.getKeyPair(exponent, '', modulus);
            $("#userId").val(RSAUtils.encryptedString(key, $("#txUserId").val()));
            $("#truePwd").val(RSAUtils.encryptedString(key, $("#passwordId").val()));
            return true;
        } else {
            return false;
        }
    }
</script>
</body>
</html>
