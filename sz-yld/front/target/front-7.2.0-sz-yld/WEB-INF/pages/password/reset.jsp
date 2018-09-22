<%@page import="com.dimeng.p2p.front.servlets.password.CheckPsd" %>
<%@page import="com.dimeng.p2p.front.servlets.password.Reset" %>
<%@page import="com.dimeng.p2p.variables.defines.SystemVariable" %>
<%@page import="org.bouncycastle.util.encoders.Hex" %>
<%@page import="com.dimeng.p2p.service.PtAccountManage" %>
<%@page import="com.dimeng.p2p.common.DimengRSAPulicKey" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <title><%configureProvider.format(out, SystemVariable.SITE_TITLE);%>-找回密码</title>
    <%@include file="/WEB-INF/include/style.jsp" %>
</head>
<body>
<%@include file="/WEB-INF/include/header.jsp" %>
<%
    String accountId = serviceSession.getSession().getAttribute("PASSWORD_ACCOUNT_ID");
    String check_success = serviceSession.getSession().getAttribute(accountId);
	//此方法考虑到通过邮箱链接地址找回密码的跨浏览器取不到session中的用户id
	if(!StringHelper.isEmpty(request.getParameter("key"))){
		//如果是邮箱链接地址获取request请求中的id
		accountId = (String)request.getAttribute("PASSWORD_ACCOUNT_ID");
		//把用户ID放入session中
		serviceSession.getSession().setAttribute("PASSWORD_ACCOUNT_ID", accountId);
        check_success = (String)request.getAttribute(accountId);
	}
    //“校验成功标志”，只有成功校验了手机验证码或邮箱的才能重置密码
    if(!("CHECK_SUCCESS").equals(check_success)){
        response.sendError(HttpServletResponse.SC_NOT_FOUND);
        return;
    }
    //校验成功后，将“校验成功标志”置为空
    serviceSession.getSession().setAttribute(accountId,"");
    PtAccountManage ptAccountManage = serviceSession.getService(PtAccountManage.class);
    DimengRSAPulicKey publicKey = ptAccountManage.getPublicKey();
    String modulus = new String(Hex.encode(publicKey.getModulus().toByteArray()));
    String exponent = new String(Hex.encode(publicKey.getPublicExponent().toByteArray()));
    if (StringHelper.isEmpty(accountId)) {
        controller.sendRedirect(request, response, controller.getViewURI(request, com.dimeng.p2p.front.servlets.password.Index.class));
    }
    String error = controller.getPrompt(request, response, PromptLevel.ERROR);
    String warring = controller.getPrompt(request, response, PromptLevel.WARRING);
    String pwd = configureProvider.getProperty(SystemVariable.PASSWORD_REGEX_CONTENT);
    String prompInfo = StringHelper.isEmpty(error) ? warring : error;
%>
<div class="main_bg" style="padding-top:20px;">
    <div class="login_mod">
        <input id="newPasswordRegexId" type="hidden" value="<%configureProvider.format(out,SystemVariable.NEW_PASSWORD_REGEX);%>"/>
        <input id="passwordRegexContentId" type="hidden" value="<%configureProvider.format(out,SystemVariable.PASSWORD_REGEX_CONTENT);%>"/>
        <div class="login_hd"><i class="icon"></i><span class="gray3 f18">找回密码</span></div>
        <div class="login_form mt50">
            <div class="tc f18 mb20">重置密码</div>
            <form action="<%=controller.getURI(request, Reset.class) %>" class="form1" method="post" onsubmit="return onSubmit();">
                <ul>
                    <li class="item">
                        <div class="til"><span class="red">*</span>新密码：</div>
                        <div class="con">
                            <div class="clearfix">
                                <div class="input fl">
                                    <i class="password_ico"></i>
                                    <input id="passwordId" name="password" type="password" class="text" onblur="passwordCheck()" maxlength="20" autocomplete="off"/>
                                </div>
                                <div class="intensity fl">
                                    <span class="weak">弱</span>
                                    <span class="medium">中</span>
                                    <span class="strong">强</span>
                                </div>
                            </div>
                            <p class="gray9" tip><%=StringHelper.isEmpty(prompInfo) ? pwd : "" %></p>
                            <p errortip class="error_tip red"><%=StringHelper.isEmpty(prompInfo) ? "" : prompInfo %></p>
                        </div>
                    </li>
                    <li class="item">
                        <div class="til"><span class="red">*</span>确认密码：</div>
                        <div class="con">
                            <div class="input">
                                <i class="password_ico"></i>
                                <input id="repasswordId" name="repassword" type="password" class="text" onblur="rePasswordCheck()" maxlength="20" autocomplete="off"/>
                            </div>
                            <p class="gray9" tip>请再次输入密码</p>
                            <p errortip class="error_tip red" style="display: none"></p>
                        </div>
                    </li>
                </ul>
                <div class="tc mt40">
                    <input class="btn06" type="submit" fromname="form1" value="提 交"/>
                </div>
                <div class="border_t_d pt15 mt100 tc">
                    	若您操作过程中遇到任何问题，请联系客服<%=configureProvider.getProperty(SystemVariable.SITE_CUSTOMERSERVICE_TEL)  %>
                </div>
            </form>
        </div>
    </div>
</div>
<%@include file="/WEB-INF/include/footer.jsp" %>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/validation.js"></script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/security.js"></script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/password.js"></script>
<script type="text/javascript">
    <!--用户名用正则式匹配-->
    var newUserNameRegex = <%configureProvider.format(out,SystemVariable.NEW_USERNAME_REGEX);%>;
    var userNameRegexContent = '<%configureProvider.format(out,SystemVariable.USERNAME_REGEX_CONTENT);%>';
    <!--密码用正则式匹配-->
    var newPasswordRegex = <%configureProvider.format(out,SystemVariable.NEW_PASSWORD_REGEX);%>;
    var passwordRegexContent = '<%configureProvider.format(out,SystemVariable.PASSWORD_REGEX_CONTENT);%>';

    var cUrl = "<%=controller.getURI(request, CheckPsd.class)%>";
    function onSubmit() {
        if (passwordCheck() && rePasswordCheck()) {
            var modulus = "<%=modulus%>", exponent = "<%=exponent%>";
            var key = RSAUtils.getKeyPair(exponent, '', modulus);
            $("#passwordId").val(RSAUtils.encryptedString(key, $("#passwordId").val()));
            $("#repasswordId").val(RSAUtils.encryptedString(key, $("#repasswordId").val()));
            return checkPsd();
        } 
        return false;
    }

    function checkPsd() {
        var $v = $("#passwordId").parent().parent().nextAll("p[errortip]");
        var $t = $("#passwordId").parent().parent().nextAll("p[tip]");
        var data = {"password": $("#passwordId").val()};
        var flag = false;
        $.ajax({
            type: "post",
            dataType: "html",
            async: false,
            url: "<%=controller.getURI(request, CheckPsd.class)%>",
            data: data,
            success: function (data) {
                if (data == "01") {
                    $v.addClass("prompt");
                    $v.html("登录密码不能与交易密码相同！");
                    $t.hide();
                    $v.show();
                    flag = false;
                } else {
                    $v.removeClass("prompt");
                    $v.hide();
                    flag = true;
                }
            }
        });
        return flag;
    }
</script>
</body>
</html>