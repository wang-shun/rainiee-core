<%@page import="com.dimeng.p2p.S50.entities.T5016"%>
<%@page import="com.dimeng.p2p.S50.enums.T5016_F12"%>
<%@page import="com.dimeng.p2p.modules.base.front.service.AdvertisementManage"%>
<%@page import="com.dimeng.p2p.user.servlets.VerifyCommon" %>
<%@page import="com.dimeng.p2p.user.servlets.Login" %>
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
    AdvertisementManage advertisementManage = serviceSession.getService(AdvertisementManage.class);
	T5016[] advertisements = advertisementManage.getAll_BZB(T5016_F12.PCLOGIN.name());
    int loginFailCount = IntegerParser.parse(configureProvider.getProperty(SystemVariable.LOGIN_FAIL_COUNT));
    boolean isShowVcode = IntegerParser.parse(request.getSession().getAttribute("exceptionCount")) >= loginFailCount;
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html dir="ltr" xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN">
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <meta property="qc:admins" content="36215751376367246375"/>
    <meta property="wb:webmaster" content="e38aeae607353b18"/>
    <title><%configureProvider.format(out, SystemVariable.SITE_NAME);%> - 用户登录</title>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <%-- <script src="http://tjs.sjs.sinajs.cn/open/api/js/wb.js?appkey=<%configureProvider.format(out,SystemVariable.INDEX_SINA_KEY);%>" type="text/javascript" charset="utf-8"></script>
    <script src="http://res.wx.qq.com/connect/zh_CN/htmledition/js/wxLogin.js"></script> --%>
</head>
<body>
<%@include file="/WEB-INF/include/loginHeader.jsp" %>

<form action="<%configureProvider.format(out,URLVariable.INDEX); %>/user/login.htm" method="post"
      id="loginForm" name="loginForm" target="_self">
    <input type="hidden" id="openId" name="openId" value=""/>
    <input type="hidden" id="accessToken" name="accessToken" value=""/>
    <input type="hidden" id="_z" name="_z" value="<%=request.getParameter("_z")==null? "":request.getParameter("_z") %>"/>
    <%-- <input type="hidden" id="_z" name="_z" value="<%configureProvider.format(out,URLVariable.INDEX); %><%=controller.getStaticPath(request)%>/index.html"/> --%>
</form>

<!--主体内容-->
<div class="main_bg" style="padding:0px;">
    <div class="login_bg login_page_box" id="RedBg" style="width:100%; padding:60px 0;height:600px;">
    	<a href="" id="RedBgURL" class="red_click" target="_blank"></a>
            <div class="logindx_k login_mod clearfix">
            	<div class="landing_port" >
                <div class="login_hd"><span class="white f24 bold">用户登录</span></div>
                <form action="<%=controller.getURI(request, Login.class)%>" method="post" onsubmit="return onSubmit();">
                	<input type="hidden" id="_z" name="_z" value="<%=request.getParameter("_z")==null?"":request.getParameter("_z") %>"/>
                    <div class="login_form form_appearance clearfix">
                        <ul>
                            <%
                                {
                                    String errorMessage = controller.getPrompt(request, response, PromptLevel.ERROR);
                                    if (!StringHelper.isEmpty(errorMessage)) {
                            %>
                            <li class="item">
                                <div class="til"></div>
                                <div>
                                    <p class="prompt">
                                        <%StringHelper.filterHTML(out, errorMessage); %>
                                    </p>
                                </div>
                            </li>
                            <%
                                    }
                                }
                            %>
                            <li class="item">
                                <%--<div class="til"><span>用户名</span></div>--%>
                                <div class="con">
                                    <div class="input focus_input">
                                        <i class="name_ico fl"></i>
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
                                        <input id="txtUserId" type="text" class="text text1 focus_text fl" maxlength="30"
                                               value="<%StringHelper.filterHTML(out, _accountName);%>"
                                               onclick="hideError();"/><label for="txtUserId">用户名/邮箱/手机号</label>
                                        <input id="userId" name="accountName" type="hidden"/>
                                    </div>
                                    <p class="prompt">&nbsp;</p>
                                    <!--                         <p class="prompt">用户名错误！</p> -->
                                </div>
                            </li>
                            <li class="item">
                               <%-- <div class="til"><span>密&nbsp;&nbsp;&nbsp;码</span></div>--%>
                                <div class="con">
                                	<div class="wjmm_box">
                                    	<div class="input focus_input">
	                                        <i class="password_ico fl"></i>
	                                        <input id="txtpasswordId" type="password" class="text text3 focus_text fl" maxlength="20"
	                                               onselectstart="return false;" ondragenter="return false;"
	                                               onpaste="return false;" onclick="hideError();" autocomplete="off"/><label for="txtpasswordId">密码</label>
	                                        <input id="passwordId" name="password" type="hidden"/>
                                   		</div>
                                   		<a class="yellow_color wjmm_text agreement_t" href="<%configureProvider.format(out, URLVariable.GET_PASSWORD);%>">忘记密码?</a>
                                    	<p class="prompt">&nbsp;</p>
                                    	</div>
                                </div>
                            </li>
                            <%
                                {
                                    if (!configureProvider.getProperty(SystemVariable.SFXYYZM).equalsIgnoreCase("false")) {
                                        if(isShowVcode){
                            %>
                            <li class="item">
                               <%-- <div class="til"><span>验证码</span></div>--%>
                                <div class="con">
                                    <div class="clearfix">
                                        <div class="input verify_input fl focus_input">
                                            <i class="verify_ico fl"></i><input name="verifyCode" type="text" id="verifyCode"
                                                                             maxlength="<%=systemDefine.getVerifyCodeLength() %>"
                                                                             class="text focus_text fl" onblur="verifyCheck();"  /><label for="verifyCode">验证码</label>
                                        </div>
                                        <div class="fl mr10"><img id="_verifyImg"
                                                                 src="<%=controller.getURI(request, VerifyCommon.class)%>?verifyType=login"
                                                                 alt="验证码" title="点击刷新" class="border" width="78"
                                                                 height="34"
                                                                 onclick="this.src='<%=controller.getURI(request, VerifyCommon.class)%>?'+Math.random()+'&verifyType=login'"
                                                                 style="cursor: pointer;"></div>
                                        <div class="fl"><a href="javascript:void(0)"
                                                                onclick="$('#_verifyImg').attr('src','<%=controller.getURI(request, VerifyCommon.class)%>?'+Math.random()+'&verifyType=login')"
                                                                class="yellow_color agreement_t">换一张</a></div>
                                    </div>
                                    <p class="prompt" id="verifyError">&nbsp;</p>
                                </div>
                            </li>
                            <%
                                        }
                                    }
                                }
                            %>
                            <li class="item pt5">
                                <%--<div class="til">&nbsp;</div>--%>
                                <div class="con">
                                	<label for="rememberId">
                                    	<input id="rememberId" name="remember" type="checkbox" value="1" class="mr5" <%=StringHelper.isEmpty(_accountName) ? "" : "checked=\"checked\""%>/>记住用户名
                                    </label>
                                </div>
                            </li>
                            <li class="item">
                                <%--<div class="til">&nbsp;</div>--%>
                                <div class="con">
                                    <button type="submit" class="login_bt mt10" style="cursor: pointer;">立即登录</button>
                                 </div>
                            </li>
                            <li class="item">
                            	<%--<div class="til">&nbsp;</div>--%>
                            	<div class="con">
                                    <p class="mt20">还不是<%configureProvider.format(out, SystemVariable.SITE_NAME);%>会员？
                                        <a href="<%configureProvider.format(out, URLVariable.REGISTER);%>"
                                           class="yellow_color pl10 agreement_t">立即注册</a>
                                    <%-- <p class="third_party mt15 mb15"></p>
                                    <p>
                                    	<span>第三方账号登录</span>
				                        <span class="other_login pl10">
				                        	<a href="https://graph.qq.com/oauth2.0/authorize?client_id=<%configureProvider.format(out,SystemVariable.INDEX_QQ_KEY);%>&response_type=token&scope=all&redirect_uri=<%configureProvider.format(out,URLVariable.INDEX_QQ_LOGIN);%>"
				                           		class="figure_ico qq_figure"></a>
				                        	<a href="javascript:void(0)" class="figure_ico wb_figure" onclick="javascript:document.getElementById('tpa_login_sina').click();"></a>
				                        	<a href="javascript:void(0);" class="figure_ico wx_figure"></a>
				                        	<div class="fl" style="margin-left: 5px; display:none;"><wb:login-button type="3,2" id="tpa_login_sina" onlogin="login" onlogout="logout"></wb:login-button></div>
				                        </span>
									</p> --%>
                                </div>
                            </li>
                        </ul>
                    </div>
                </form>
            </div>
            <div class="weChat_port" style="display:none;">
            	<div class="login_hd clearfix">
            		<a href="javascript:void(0);" class="fr yellow_color other_port pr50">其他登录方式</a>
            	</div>
            	<div class="weChat_pic clearfix" id="wx_login_container" style="text-align: center;"></div>
            </div>
            </div>
    </div>
</div>
<!--主体内容-->
<%@include file="/WEB-INF/include/footer.jsp" %>
<!--底部-->
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/security.js"></script>
<script type="text/javascript">
    var isNull = /^[\s]{0,}$/;
    var verify = /^\d{<%=systemDefine.getVerifyCodeLength() %>}$/;
    var loginName = /^[a-z]([\w]*)$/i;
    var testChinese = /.*[\u4e00-\u9fa5]+.*$/;
    function hideError(){
		 var p = $("#txtUserId").parent().parent().find("p");
	        p.html("&nbsp;");
	        p.removeClass("red");
	}
    function accountCheck() {
        var val = $.trim($("#txtUserId").val());
        var p = $("#txtUserId").parent().parent().find("p");
        p.html("&nbsp;");
        p.removeClass("red");
        if (isNull.test(val)) {
            p.html("用户名不能为空");
            p.addClass("red");
            return false;
        }else if(testChinese.test(val)){
            p.html("用户名不能为中文");
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
    function verifyCheck() {
        //是否需要验证码.
        var flag = <%configureProvider.format(out,SystemVariable.SFXYYZM);%>;
        var isShowVcode = '<%=isShowVcode%>';
        if ((flag != "undefined" && flag == false) || isShowVcode=='false') {
            return true;
        }

        var val = $("input[name='verifyCode']").val();
        var p = $("#verifyError");
        p.html("&nbsp;");
        p.removeClass("red");
        if (isNull.test(val)) {
            p.html("验证码不能为空");
            p.addClass("red");
            return false;
        } else if (!verify.test(val)) {
            p.html("您输入的验证码有误");
            p.addClass("red");
            return false;
        }
        return true;
    }
    function onSubmit() {
        if (accountCheck() && passwordCheck() && verifyCheck()) {
            var modulus = "<%=modulus%>", exponent = "<%=exponent%>";
            var key = RSAUtils.getKeyPair(exponent, '', modulus);
            $("#userId").val(RSAUtils.encryptedString(key, $.trim($("#txtUserId").val())));
            $("#passwordId").val(RSAUtils.encryptedString(key, $("#txtpasswordId").val()));
            return true;
        } else {
            return false;
        }
    }
    
</script>
<style type="text/css">
    iframe{margin-left: 90px;}
</style>
<script type="text/javascript">
	/**微信登录**/
	var obj = new WxLogin({
	    id:"wx_login_container", 
	    appid: "<%configureProvider.format(out,SystemVariable.WX_APPID);%>", 
	    scope: "snsapi_login", 
	    redirect_uri: "<%configureProvider.format(out,URLVariable.USER_INDEX);%>wechat/wxCallback.htm",
	    state: "<%=serviceSession.getSession().getToken().replaceAll("-", "")%>",
	    style: "white",
	    href: "<%configureProvider.format(out,URLVariable.USER_INDEX);%>css/weixin.css"
	});
	
    if (QC.Login.check()) {//如果已登录
        QC.Login.signOut();
        QC.Login.getMe(function (openId, accessToken) {
            $("#openId").val(openId);
            $("#accessToken").val(accessToken);
            loginForm.submit();
        });
    }

    WB2.anyWhere(function (W) {
        W.widget.connectButton({
            id: "tpa_login_sina",
            type: "3,2",
            callback: {
                login: function (o) { //登录后的回调函数
                    var status = WB2.checkLogin();
                    if (status) {
                        WB2.logout();
                        $("#openId").val(o.id);
                        clearTime = setInterval(function () {
                            var status = WB2.checkLogin();
                            if (status) {
                                WB2.logout();
                            } else {
                                clearInterval(clearTime);
                                loginForm.submit();
                            }
                        }, 1000);
                    }
                }
            }
        });
    });
</script>

<script type="text/javascript">
	var pic_list=[];
	<%if(advertisements == null){%>
	pic_list.push({"img":"/user/images/dlbg_pic.jpg","URL":"javascript:void(0);"});
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
</body>
</html>
