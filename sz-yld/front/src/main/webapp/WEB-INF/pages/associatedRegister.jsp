<%@page import="com.dimeng.p2p.front.servlets.AssociatedAccount" %>
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
<%
    if (dimengSession != null && dimengSession.isAuthenticated()) {
        controller.sendRedirect(request, response, configureProvider.format(URLVariable.USER_INDEX));
        return;
    }
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>注册关联</title>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
</head>
<body>
<%
    String loginNameExists = ObjectHelper.convert(request.getAttribute("loginNameExists"), String.class);
    String mobileExists = ObjectHelper.convert(request.getAttribute("mobileExists"), String.class);
    String mobile = request.getParameter("mobile");
    String type = request.getParameter("type");
    PtAccountManage ptAccountManage = serviceSession.getService(PtAccountManage.class);
    DimengRSAPulicKey publicKey = ptAccountManage.getPublicKey();
    String modulus = new String(Hex.encode(publicKey.getModulus().toByteArray()));
    String exponent = new String(Hex.encode(publicKey.getPublicExponent().toByteArray()));
    if (StringHelper.isEmpty(type)) {
        type = UserType.LC.name();
    }
    String openId = StringHelper.isEmpty(request.getParameter("openId")) ? dimengSession.getAttribute("openId") : request.getParameter("openId");
    String qqToken = StringHelper.isEmpty(request.getParameter("qqToken")) ? dimengSession.getAttribute("qqToken") : request.getParameter("qqToken");
    String loginType = StringHelper.isEmpty(request.getParameter("loginType")) ? dimengSession.getAttribute("loginType") : request.getParameter("loginType");
    if (StringHelper.isEmpty(openId)) {
        controller.redirectLogin(request, response, configureProvider.format(URLVariable.REGISTER));
        return;
    }
    dimengSession.setAttribute("openId", "");
    dimengSession.setAttribute("qqToken", "");
    dimengSession.setAttribute("loginType","");
    //记录下用户到达过注册页面,之后则会去掉 用户中心的QQ自动登录和首页的QQ自动登录
    dimengSession.setAttribute("fromRegister", "true");
%>
<script src="http://tjs.sjs.sinajs.cn/open/api/js/wb.js?appkey=<%configureProvider.format(out,SystemVariable.INDEX_SINA_KEY);%>" type="text/javascript" charset="utf-8"></script>
<%@include file="/WEB-INF/include/header.jsp" %>
<div class="main_bg" style="padding-top:20px;">
    <div class="login_mod">
        <div class="login_hd"><i class="icon"></i><span class="gray3 f18">注册关联</span></div>
        <div class="login_hd02 mt40">新用户注册关联</div>
        <form action="<%=configureProvider.format(URLVariable.ASSREGISTER_SUBMIT) %>" method="post" onsubmit="return onAssSubmit()">
            <input type="hidden" id="openId" name="openId" value="<%=openId ==null ? "" : openId%>"/>
            <input type="hidden" id="accessToken" name="accessToken" value="<%=(StringHelper.isEmpty(qqToken) || "null".equals(qqToken)) ? "" : qqToken%>"/>
            <%=FormToken.hidden(serviceSession.getSession()) %>
            <div class="reg_mod clearfix pt50 pb50 login">
                <%
                    {
                        String errorMessage = controller.getPrompt(request, response, PromptLevel.ERROR);
                        if (!StringHelper.isEmpty(errorMessage)) {
                %>
                <div class="allerror clear">
                    <img src="<%=controller.getStaticPath(request)%>/images/popup_error.gif"/>
                    <%StringHelper.filterHTML(out, errorMessage);%>
                </div>
                <%
                        }
                    }
                %>
                <div class="login_form">
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
                            <div class="til">
                                <span class="red">*</span>用户名：
                            </div>
                            <div class="con">
                                <%if (!StringHelper.isEmpty(loginNameExists)) {%>
                                <div class="clearfix">
                                    <div class="input">
                                        <i class="name_ico"></i>
                                        <input id="userId" type="text" class="text" maxlength="18"
                                               value="<%StringHelper.filterQuoter(out,request.getParameter("accountName"));%>"
                                               onblur="nameCheck()"/>
                                    </div>
                                </div>
                                <p class="red prompt">
                                    <%StringHelper.filterHTML(out, loginNameExists);%>
                                </p>
                                <%} else {%>
                                <div class="clearfix">
                                    <div class="input fl">
                                        <i class="name_ico"></i>
                                        <input id="userId" type="text" class="text" maxlength="18"
                                               value="<%StringHelper.filterHTML(out,request.getParameter("accountName"));%>"
                                               onblur="nameCheck()"/>
                                    </div>
                                    <div class="mt5 fl">
                                        <span id="loginSuccess"></span>
                                    </div>
                                </div>
                                <p class="gray9 prompt" id="loginNameError"><%
                                    configureProvider.format(out, SystemVariable.USERNAME_REGEX_CONTENT);%></p>
                                <%}%>
                                <input id="hdnUserId" name="accountName" type="hidden"/>
                            </div>
                        </li>
                        <li class="item">
                            <div class="til">
                                <span class="red">*</span>密码：
                            </div>
                            <div class="con">
                                <div class="clearfix">
                                    <div class="input fl">
                                        <i class="password_ico"></i>
                                        <input id="passwordFirstId" name="password" type="password" class="text text3"
                                               onblur="passwordCheck()" onpropertychange="checkStrength();"
                                               maxlength="20"/>
                                    </div>
                                    <div class="mt5 fl">
                                        <span id="passwordSuccess"></span>
                                    </div>
                                    <div class="intensity fl ml5">
                                        <span class="weak">弱</span>
                                        <span class="medium">中</span>
                                        <span class="strong">强</span>
                                    </div>
                                </div>
                                <p class="gray9 prompt">
                                    <span id="password-tip"><%configureProvider.format(out, SystemVariable.PASSWORD_REGEX_CONTENT);%></span>
                                </p>
                            </div>
                        </li>
                        <li class="item">
                            <div class="til">
                                <span class="red">*</span>确认密码：
                            </div>
                            <div class="con">
                                <div class="clearfix">
                                    <div class="input fl">
                                        <i class="password_ico"></i>
                                        <input id="passwordSecondId" name="newPassword" type="password" maxlength="20"
                                               class="text text3" onblur="rePasswordCheck()"/>
                                    </div>
                                    <div class="mt5 fl">
                                        <span id="newPasswordSuccess"></span>
                                    </div>
                                </div>
                                <p class="gray9 prompt">请再次输入密码</p>
                            </div>
                        </li>
                    </ul>

                    <div class="ml100 mt30">
                    	<label for="iAgree">
                        	<input name="iAgree" onclick="checkoxBtn();" type="checkbox" id="iAgree"
                               class="m_cb"/>我已阅读并同意<%configureProvider.format(out, SystemVariable.SITE_NAME);%>
                        </label>
                        <a target="_blank"
                           href="<%=controller.getPagingItemURI(request, Term.class, TermType.ZCXY.name())%>"
                           class="highlight">《注册协议》</a>
                    </div>
                    <div class="ml100 mt15 mb20">
                        <input type="submit" id="sub-btn" class="btn06 btn_gray btn_disabled" disabled="disabled"
                               style="border: none;" value="注册关联"/>
                    </div>
                </div>
                <div class="red_right_mod">
                    <div class="ml50 pl10">已有账号？ 
                    	<a href="<%=controller.getViewURI(request, AssociatedAccount.class)%>?openId=<%=openId %>&qqToken=<%=qqToken %>&loginType=<%=loginType %>"
                            class="highlight">关联已有账号</a>
                    </div>
                    <div class="tc mt30"><img src="<%=controller.getStaticPath(request)%>/images/zcgl_pic.jpg"/></div>
                    <div class="tc lh26 mt20 f16">
				                        您已通过<span class="red">
                        <%if (!StringHelper.isEmpty(qqToken) && !"null".equals(qqToken)) { %>
                        	QQ
                        <%} else if(!StringHelper.isEmpty(loginType) && "weixin".equals(loginType)){ %>
                        	微信
                        <%}else{ %>
				                                   新浪微博
				        <%} %></span>账号登录成功，
				        <br/>请关联<%configureProvider.format(out, SystemVariable.SITE_NAME);%>的账号，
				        <br/>以后登录<%configureProvider.format(out, SystemVariable.SITE_NAME);%>会更方便！
                    </div>
                </div>
            </div>
        </form>
    </div>
</div>
<%@include file="/WEB-INF/include/footer.jsp" %>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/security.js"></script>
<script type="text/javascript">
    var _nURL = '<%=controller.getURI(request, CheckNameExists.class)%>';
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

    $(function () {
        runCheckStrength();
    });

    function runCheckStrength() {
        var element = document.getElementById("passwordFirstId");
        if ("\v" == "v") {
            element.onpropertychange = checkStrength;
        } else {
            element.addEventListener("input", checkStrength, false);
        }
    }
</script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/register.js"></script>
</body>
</html>
