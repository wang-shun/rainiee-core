<%@page import="com.dimeng.p2p.front.servlets.AssociatedRegister" %>
<%@page import="com.dimeng.p2p.front.servlets.AssociatedAccount" %>
<%@page import="com.dimeng.p2p.front.servlets.VerifyCommon" %>
<%@page import="com.dimeng.p2p.front.servlets.Login" %>
<%@page import="com.dimeng.p2p.variables.defines.SystemVariable" %>
<%@page import="java.net.URLDecoder" %>
<%@page import="com.dimeng.util.StringHelper" %>
<%@page import="com.dimeng.p2p.common.RSAUtils" %>
<%@page import="org.bouncycastle.util.encoders.Hex" %>
<%@page import="com.dimeng.p2p.service.PtAccountManage" %>
<%@page import="com.dimeng.p2p.common.DimengRSAPulicKey" %>
<%
    if (dimengSession != null && dimengSession.isAuthenticated()) {
        controller.sendRedirect(request, response, configureProvider.format(URLVariable.USER_INDEX));
        return;
    }
    PtAccountManage ptAccountManage = serviceSession.getService(PtAccountManage.class);
    DimengRSAPulicKey publicKey = (DimengRSAPulicKey) ptAccountManage.getPublicKey();
    String modulus = new String(Hex.encode(publicKey.getModulus().toByteArray()));
    String exponent = new String(Hex.encode(publicKey.getPublicExponent().toByteArray()));
    String openId = request.getParameter("openId");
    String qqToken = request.getParameter("qqToken");
    String loginType = request.getParameter("loginType");
    if (StringHelper.isEmpty(openId)) {
        controller.redirectLogin(request, response, configureProvider.format(URLVariable.LOGIN));
        return;
    }
    //记录下用户到达过注册页面,之后则会去掉 用户中心的QQ自动登录和首页的QQ自动登录 
    dimengSession.setAttribute("fromRegister", "true");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html dir="ltr" xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN">
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <meta property="qc:admins" content="36215751376367246375"/>
    <meta property="wb:webmaster" content="e38aeae607353b18"/>
    <title><%configureProvider.format(out, SystemVariable.SITE_NAME);%> - 关联账号</title>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <script src="http://tjs.sjs.sinajs.cn/open/api/js/wb.js?appkey=<%configureProvider.format(out,SystemVariable.INDEX_SINA_KEY);%>"
            type="text/javascript" charset="utf-8"></script>
</head>
<body>
<%@include file="/WEB-INF/include/header.jsp" %>

<!--主体内容-->
<div class="main_bg" style="padding-top:20px;">
    <div class="login_mod">
        <div class="login_hd"><i class="icon"></i><span class="gray3 f18">关联账号</span></div>
        <div class="login_hd02 mt40">已有账号关联</div>
        <div class="reg_mod clearfix pt50 pb50">
            <form action="<%=configureProvider.format(URLVariable.ASSACCOUNT_SUBMIT)%>" method="post"
                  onsubmit="return onSubmit();">
                <input type="hidden" id="openId" name="openId" value="<%=openId ==null ? "" : openId%>"/>
                <input type="hidden" name="loginType" value="<%=loginType ==null ? "" : loginType%>"/>
                <input type="hidden" id="accessToken" name="accessToken"
                       value="<%=(StringHelper.isEmpty(qqToken) || "null".equals(qqToken)) ? "" : qqToken%>"/>
                <input type="hidden" id="_z" name="_z"
                       value="<%configureProvider.format(out,URLVariable.INDEX); %><%=controller.getStaticPath(request)%>/index.html"/>

                <div class="login_form ">
                    <ul>
                        <li class="item" id="qqnick" style="display: none;">
                            <div class="til"><span class="red">*</span>昵称：</div>
                            <div class="con">
                                <div>
                                    <span id="qqnickname"></span>
                                </div>
                                <p class="prompt"></p>
                            </div>
                        </li>
                        <li class="item">
                            <div class="til"><span class="red">*</span>用户名：</div>
                            <div class="con">
                                <div class="input">
                                    <i class="name_ico"></i>
                                    <%
                                        String _accountName = null;
                                        Cookie[] _cookies = request.getCookies();
                                        if (_cookies != null) {
                                            for (Cookie cookie : _cookies) {
                                                if (cookie == null) {
                                                    continue;
                                                }
                                                if ("ACCOUNT_NAME".equals(cookie.getName())) {
                                                    _accountName = URLDecoder.decode(cookie.getValue(), resourceProvider.getCharset());
                                                }
                                            }
                                        }
                                    %>
                                    <input id="txtUserId" type="text" class="text text1" maxlength="30"
                                           value="<%StringHelper.filterHTML(out, _accountName);%>"
                                           onblur="accountCheck();"/>
                                    <input id="userId" name="accountName" type="hidden"/>
                                </div>
                                <p class="prompt">
                                    <%
                                        {
                                            String errorMessage = controller.getPrompt(request, response, PromptLevel.ERROR);
                                            if (!StringHelper.isEmpty(errorMessage)) {
                                                StringHelper.filterHTML(out, errorMessage);
                                            } else {
                                    %>
                                    &nbsp;
                                    <%
                                            }
                                        }
                                    %>
                                </p>
                                <!--                         <p class="prompt">用户名错误！</p> -->
                            </div>
                        </li>
                        <li class="item">
                            <div class="til"><span class="red">*</span>密码：</div>
                            <div class="con">
                                <div class="input">
                                    <i class="password_ico"></i>
                                    <input id="txtpasswordId" type="password" class="text text3" maxlength="20"
                                           onselectstart="return false;" ondragenter="return false;"
                                           onpaste="return false;" onblur="passwordCheck();" autocomplete="off"/>
                                    <input id="passwordId" name="password" type="hidden"/>
                                </div>
                                <p class="prompt">&nbsp;</p>
                            </div>
                        </li>
                    </ul>
                    <div class="ml100 mt50 mb100">
                        <button type="submit" class="btn06 " style="cursor: pointer;">立即关联</button>
                    </div>
                </div>
            </form>
            <div class="red_right_mod">
                <div class="ml50 pl10">没有账号？
                	<a href="<%=controller.getViewURI(request, AssociatedRegister.class)%>?openId=<%=openId %>&qqToken=<%=qqToken %>&loginType=<%=loginType %>"
                        class="highlight">注册账号</a></div>
                <div class="tc mt30"><img src="<%=controller.getStaticPath(request)%>/images/zcgl_pic.jpg"/></div>
                <div class="tc lh26 mt20 f16">
                	您已通过<span class="red">
                	<%if (!StringHelper.isEmpty(qqToken) && !"null".equals(qqToken)) { %>
                    	QQ
                    <%} else if(!StringHelper.isEmpty(loginType) && "weixin".equals(loginType)){ %>
                    	微信
                    <%}else{ %>
                    	新浪微博
                    <%} %>
                    </span>
                                                            账号登录成功，<br/>请关联<%configureProvider.format(out, SystemVariable.SITE_NAME);%>
                  	  的账号，<br/>以后登录<%configureProvider.format(out, SystemVariable.SITE_NAME);%>会更方便！
                </div>
            </div>

        </div>
    </div>
</div>
<!--主体内容-->
<%@include file="/WEB-INF/include/footer.jsp" %>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/security.js"></script>
<script type="text/javascript">
    var isNull = /^[\s]{0,}$/;
    var verify = /^\d{<%=systemDefine.getVerifyCodeLength() %>}$/;
    var loginName = /^[a-z]([\w]*)$/i;

    function accountCheck() {
        var val = $("#txtUserId").val();
        var p = $("#txtUserId").parent().parent().find("p");
        p.html("&nbsp;");
        p.removeClass("red");
        if (isNull.test(val)) {
            p.html("用户名不能为空");
            p.addClass("red");
            return false;
        }
        return true;
    }
    function passwordCheck() {
        var val = $("#txtpasswordId").val();
        var p = $("#txtpasswordId").parent().parent().find("p");
        p.html("&nbsp;");
        p.removeClass("red");
        if (isNull.test(val)) {
            p.html("密码不能为空");
            p.addClass("red");
            return false;
        }
        return true;
    }
    function onSubmit() {
        if (accountCheck() && passwordCheck()) {
            var modulus = "<%=modulus%>", exponent = "<%=exponent%>";
            var key = RSAUtils.getKeyPair(exponent, '', modulus);
            $("#userId").val(RSAUtils.encryptedString(key, $("#txtUserId").val()));
            $("#passwordId").val(RSAUtils.encryptedString(key, $("#txtpasswordId").val()));
            return true;
        } else {
            return false;
        }
    }
</script>
<script type="text/javascript">
    function showQQInfo(data) {
        //根据返回数据，更换按钮显示状态方法
        $("#qqnick").show();

        var dom = document.getElementById('qqnickname'),
                _logoutTemplate = [
                    //头像
                    '<span><img src="{figureurl}" class="qc_item figure"/></span>',
                    //昵称
                    '<span>{nickname}&nbsp&nbsp</span>'
                ].join("");

        dom && (dom.innerHTML = QC.String.format(_logoutTemplate, {
            nickname: QC.String.escHTML(data.nickname),
            figureurl: QC.String.escHTML(data.figureurl)
        }));
    }

    function checkAndShowQQNick() {
        if (QC.Login.check()) {
            var paras = {};

            //如果已登录
            QC.api("get_user_info", paras)
                    .success(function (s) {//成功回调
                        showQQInfo(s.data);
                    })
        }
    }

    checkAndShowQQNick();


</script>
</body>
</html>
