<%@page import="com.dimeng.p2p.common.FormToken" %>
<%@page import="com.dimeng.p2p.front.servlets.password.Mmbh" %>
<%@page import="com.dimeng.p2p.front.servlets.password.SendCheckCode" %>
<%@page import="java.util.Arrays" %>
<%@page import="java.lang.reflect.Array" %>
<%@page import="org.apache.commons.lang3.ArrayUtils" %>
<%@page import="java.util.Collections" %>
<%@page import="java.util.Iterator" %>
<%@page import="java.util.Set" %>
<%@page import="com.dimeng.p2p.account.user.service.entity.PwdSafetyQuestion" %>
<%@page import="java.util.List" %>
<%@page import="java.util.Map" %>
<%@page import="com.dimeng.p2p.account.user.service.achieve.PwdSafeCacheManage" %>
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
	//此方法考虑到通过邮箱链接地址找回密码的跨浏览器取不到session中的用户id
	if(!StringHelper.isEmpty(request.getParameter("key"))){
		//如果是邮箱链接地址获取request请求中的id
		accountId = (String)request.getAttribute("PASSWORD_ACCOUNT_ID");
		//把用户ID放入session中
		serviceSession.getSession().setAttribute("PASSWORD_ACCOUNT_ID", accountId);
	}
    String telPhone = serviceSession.getSession().getAttribute("TEL_PHONE");
    String emailAdress = serviceSession.getSession().getAttribute("EMAIL_PWD");
    String errorCount = serviceSession.getSession().getAttribute("ERROR_CHECK_COUNT");
    String typeFlag = serviceSession.getSession().getAttribute("RESET_FLAG");
    String accountType = serviceSession.getSession().getAttribute("accountType");
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
    PwdSafeCacheManage pwdSafeCacheManage = serviceSession.getService(PwdSafeCacheManage.class);
    List<PwdSafetyQuestion> questions = pwdSafeCacheManage.getPasswordQuestionByUser(Integer.parseInt(accountId));

    //boolean isPsdSafe = Boolean.parseBoolean(configureProvider.getProperty(SystemVariable.SFXYMBAQNZ));
    boolean isPsdSafe = false;
    StringBuffer phone_show = new StringBuffer();
    if ("1".equals(typeFlag)) {
        phone_show.append(telPhone.substring(0, 3)).append("xxxx").append(telPhone.substring(7, 11));
    }
%>
<div class="main_bg">
    <div class="login_mod">
        <input type="hidden" name="phone" value="<%=telPhone%>">
        <input type="hidden" name="typeFlag" value="<%=typeFlag%>"/>
        <input type="hidden" name="accountType" value='<%=accountType%>'/>
        <input type="hidden" name="errorCount" value='<%=errorCount%>'/>
        <input type="hidden" name="tokenEmail" value="">
        <input type="hidden" name="tokenPhone" value="">
        <%=FormToken.hidden(serviceSession.getSession()) %>
        <div class="login_hd"><i class="icon"></i><span class="gray3 f18">找回密码</span></div>
        <div class="login_form mt50">
            <% if (isPsdSafe) { %>
            <div class="tc f18 mb20">请回答密保问题以找回密码！</div>
            <%}%>
            <ul>
                <% if (typeFlag.equals("1")) {%>
                <li class="item">
                    <div class="til"><span class="red">*</span>短信验证码：</div>
                    <div class="con">
                        <div class="clearfix">
                            <div class="input fl">
                                <i class="verify_ico"></i><input name="code" type="text" class="text" maxlength="6"
                                                                 onblur="codeCheck()"/>
                            </div>
                            <div class="fl mr5">
                                <!-- <input type="button" id="sendCheckCode" class="btn05" value="获取验证码"/> -->
                                <a href="javascript:void(0);" id="sendCheckCode" class="btn05">获取验证码</a>
                                <!-- <input name="count" style="display: none;" class="btn05" value="获取验证码"/> -->
                                <a href="javascript:void(0);" id="count" style="display: none;" class="btn05">获取验证码</a>
                            </div>
                        </div>
                        <p errortip class="prompt"></p>
                    </div>
                </li>
                <% } %>
                <% if (isPsdSafe) { %>
                <li class="item">
                    <div class="til"><span class="red">*</span>密保问题：</div>
                    <div class="con">
                        <select name="questionId" type="text" class="select" maxlength="20">
                            <%
                                for (int i = 0; i < questions.size(); i++) {
                                    PwdSafetyQuestion ques = (PwdSafetyQuestion) questions.get(i);
                                    if(ques != null){ %>
                                    <option value="<%=ques.id %>"><%= ques.descr %></option>
                            <%}}%>
                        </select>

                        <p class="prompt"></p>
                    </div>
                </li>
                <li class="item">
                    <div class="til"><span class="red">*</span>答案：</div>
                    <div class="con">
                        <input type="text" class="text2" maxlength="50" name="answer" onblur="answerCheck()"/>

                        <p errortip class="prompt"></p>
                    </div>
                </li>
                <%} %>
            </ul>
            <div class="tc mt40 pb100"><input type="button" id="subBtn" class="btn06 mt10" value="提  交"></div>
        </div>
        <div class="border_t_d pt15 mt100 tc">
            若您操作过程中遇到任何问题，请联系客服<%=configureProvider.getProperty(SystemVariable.SITE_CUSTOMERSERVICE_TEL)  %>
        </div>
    </div>
</div>
<div id="info"></div>
<div class="popup_bg"  style="display: none;"></div>
<%@include file="/WEB-INF/include/footer.jsp" %>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/security.js"></script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/password.js"></script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/dialog.js"></script>
<script type="text/javascript">
    var timer = null;
    var _i = 60;
    var modulus = "<%=modulus%>", exponent = "<%=exponent%>";
    var key = RSAUtils.getKeyPair(exponent, '', modulus);
    var typeFlag = "<%=typeFlag%>";
    var isPsdSafe = "<%=isPsdSafe %>";
    var hasQuestion="<%= questions.size()>0 ? "true":"false"%>";
    var indexUrl="<%=controller.getViewURI(request, com.dimeng.p2p.front.servlets.Index.class)%>"

    //未设置密保问题，跳转首页
    $(function(){
    	if(isPsdSafe=='true'&&hasQuestion=='false'){
    		$(".popup_bg").show();
    		$("#info").html(showSuccInfo("用户没有设置密保问题或者密保问题设置不全!","error",indexUrl));
    	}
    });
    
    $(function () {
        var tokenValue = $("input[name='<%=FormToken.parameterName()%>']").val();
        $("input[name='tokenEmail']").val(tokenValue);
        $("input[name='tokenPhone']").val(tokenValue);
        //发送验证码
        $("#sendCheckCode").click(function () {
            var param = {
                'phone': $("input[name=phone]").val(),
                'type': $("input[name=typeFlag]").val(),
                'accountType': $("input[name=accountType]").val()
            };
            $("#sendCheckCode").hide();
            $("#count").show();
            timer = setInterval("timers()", 1000);
            $.ajax({
                type: "post",
                dataType: "html",
                url: "<%=controller.getURI(request, SendCheckCode.class)%>" + "?t=" + Math.random(),
                data: param,
                success: function (data) {
                    var jo = eval('(' + data + ')');
                    if (jo && jo.errorCode == '0') {
                        $("#sendShow").show();
                        $("#info").html(showDialogInfo("验证码已发送！", "successful"));
                    } else {
                        initTimer();
                        $("#info").html(showDialogInfo(jo ? jo.errorMsg : '系统异常，请稍后重试！', jo ? 'doubt' : 'error'));
                    }
                }
                , error: function () {
                    initTimer();
                    $("#info").html(showDialogInfo("系统异常，请稍后重试！", "error"));
                }
            });
        });

        //提交
        $("#subBtn").click(function () {
            if (!checkEle() || !codeCheck()){ 
            	return;
            }

            var code;
            var code1;
            //判断是否需要手机验证码
            if (!(typeFlag != "undifined" && typeFlag == "0")) {
                code1 = $("input[name='code']");
                code = code1.val();
                if (code.length <= 0) {
                    code1.parent().parent().next().html("手机验证码不能为空!").show();
                    return;
                }
            }

            var answer1;
            var answer;
            //判断是否需要密保问题
            if (!(isPsdSafe != "undifined" && isPsdSafe == "false")) {
                answer1 = $("input[name='answer']");
                answer = answer1.val();
                if (answer.length <= 0) {
                    answer1.next().html("密保答案不能为空!").show();
                    return;
                }
            }
            var param = {
                'phone': $("input[name=phone]").val(),
                'type': $("input[name=typeFlag]").val(),
                'code': $("input[name=code]").val(),
                'questionId': isPsdSafe && 'true' == isPsdSafe ? $("select[name=questionId]").val()[0] : "",
                'answer': isPsdSafe && 'true' == isPsdSafe ? RSAUtils.encryptedString(key, $("input[name=answer]").val()) : "",
                "accountId": <%=accountId %>,
                "accountType": "<%=accountType %>",
                "tokenEmail": $("input[name=tokenEmail]").val(),
                "tokenPhone": $("input[name=tokenPhone]").val(),
                "errorCount": $("input[name=errorCount]").val()
            };
            $.ajax({
                type: "post",
                dataType: "html",
                url: "<%=controller.getURI(request, Mmbh.class)%>" + "?t=" + Math.random(),
                data: param,
                success: function (data) {
                    var ct = eval('(' + data + ')');
                    //替换token
                    setToken(ct.tokenNew);
                    if (ct.num == '0') {
                        location.href = '<%=controller.getURI(request, Reset.class)%>';
                    } else {
                    	$("#info").html(showDialogInfo(ct.msg, "error"));
                        //alert(ct.msg);
                    }
                },
                error: function () {
                	$("#info").html(showDialogInfo("系统异常，请稍后重试！", "error"));
                    //alert("系统异常，请稍后重试！");
                }
            });
        });

        //元素校验
        function checkEle() {
            var error = $("p[class=error_tip]");
            if (error && error.length > 0) {
                $.each(error, function (i, o) {
                    $(o).html("不能为空").show();
                });
                return false;
            }
            return true;
        }

        function setToken(tokenValue) {
            $("input[name='tokenEmail']").val(tokenValue);
            $("input[name='tokenPhone']").val(tokenValue);
        }

    });

    //计时器
    function timers() {
        $(function () {
            if (_i >= 0) {
                $("#count").text("(" + _i + ")秒后重新获取");
                $("#sendCheckCode").hide();
                _i--;
            } else {
                initTimer();
            }
        });
    }
    //计时器初始化
    function initTimer() {
        $("#count").css("display", "none");
        $("#sendCheckCode").show();
        clearInterval(timer);
        $("#count").text("获取验证码");
        _i = 60;
    }
    
</script>
</body>
</html>