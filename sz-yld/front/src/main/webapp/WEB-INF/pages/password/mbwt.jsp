<%@page import="java.util.ArrayList" %>
<%@page import="com.dimeng.p2p.front.servlets.password.Mbwt" %>
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
    <title>
        <%configureProvider.format(out, SystemVariable.SITE_TITLE);%>-找回密码
    </title>
    <%@include file="/WEB-INF/include/style.jsp" %>
</head>
<body>
<%@include file="/WEB-INF/include/header.jsp" %>
<%
    String accountId = serviceSession.getSession().getAttribute("PASSWORD_ACCOUNT_ID");
    String errorCount = serviceSession.getSession().getAttribute("ERROR_CHECK_COUNT");
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

    boolean isPsdSafe = Boolean.parseBoolean(configureProvider.getProperty(SystemVariable.SFXYMBAQNZ));
%>
<div class="main_bg" style="padding-top:20px;">
    <div class="login_mod">
        <input type="hidden" name="errorCount" value='<%=errorCount%>'/>
        <input type="hidden" name="tokenAccountName" value="">
        <%=FormToken.hidden(serviceSession.getSession())%>
        <div class="login_hd"><i class="icon"></i><span class="gray3 f18">找回密码</span></div>
        <div class="login_form">
            <ul class="problem">
                <%
                    for (int i = 0; i < questions.size(); i++) {
                        PwdSafetyQuestion ques = (PwdSafetyQuestion) questions.get(i);
                %>
                <li>
                    <input type="hidden" id="qid_<%=i+1%>" value="<%=ques.id%>">
                    <p>问题<%=i + 1%>:<%=ques.descr%></p>
                    <input id="da_<%=i+1%>" type="text" class="text required" maxlength="20" name="answer"/>
                    <p tip class="gray" style="min-height:20px; line-height:20px;font-size:12px; padding-bottom:5px;">请输入密保问题答案</p>
                    <p errortip class="prompt" style="display:none"></p>
                </li>
                <%
                    }
                %>
            </ul>
            <div class="tc mt40">
                <input type="button" id="subBtn" class="btn06 sumbitForme mt10" value="提  交">
            </div>
        </div>
        <div class="border_t_d pt15 mt50 tc">
            	若您操作过程中遇到任何问题，请联系客服<%=configureProvider.getProperty(SystemVariable.SITE_CUSTOMERSERVICE_TEL) %>
        </div>
    </div>
</div>
<div id="info"></div>
<div class="popup_bg"  style="display: none;"></div>
<%@include file="/WEB-INF/include/footer.jsp" %>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/validation.js"></script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/security.js"></script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/dialog.js"></script>
<script type="text/javascript">
    var modulus = "<%=modulus%>", exponent = "<%=exponent%>";
    var key  = RSAUtils.getKeyPair(exponent, '', modulus);
    var flag = 
    
    $(function () {
        var tokenValue = $("input[name='<%=FormToken.parameterName()%>']").val();
        $("input[name='tokenAccountName']").val(tokenValue);

        //提交
        $("#subBtn").click(function () {
            if (!checkEle()) return;
            /* var quests=$("input[id^='da_']");
            if(quests&& quests.length > 0){
            	$.each(quests, function (_j, o) {
            		if($.trim($(o).val())==""){
            			$("#info").html(showDialogInfo("密保问题"+(_j+1)+"答案不能为空！", "doubt"));
            			break;
            		}
            	});
            } */
            var q1 = $("#qid_1").val() + ":" + RSAUtils.encryptedString(key, $("#da_1").val());
            var q2 = $("#qid_2").val() + ":" + RSAUtils.encryptedString(key, $("#da_2").val());
            var q3 = $("#qid_3").val() + ":" + RSAUtils.encryptedString(key, $("#da_3").val());
            if (!checkEle()) return;

            var param = {
                'question1': q1,
                'question2': q2,
                'question3': q3,
                "accountId": <%=accountId%>,
                "tokenEmail": $("input[name=tokenAccountName]").val(),
                "errorCount": $("input[name=errorCount]").val()
            };
            //console.log("param>>"+ JSON.stringify(param));
            $.ajax({
                type: "post",
                dataType: "html",
                url: "<%=controller.getURI(request, Mbwt.class)%>",
                data: param,
                success: function (data) {
                    var ct = eval('(' + data + ')');
                    //替换token
                    setToken(ct.tokenNew);
                    if (ct.num == '0') {
                        location.href = '<%=controller.getURI(request, Reset.class)%>';
                    } else {
                        $("#info").html(showDialogInfo(ct.msg, "doubt"));
                    }
                },
                error: function () {
                    $("#info").html(showDialogInfo("系统异常，请稍后重试！", "error"));
                }
            })
        })
		
        //元素校验
        function checkEle() {
            var error = $("p[class=error_tip]");
            if (error && error.length > 0) {
                $.each(error, function (_i, o) {
                    $(o).html("不能为空").show();
                })
                return false;
            }
            return true;
        }

        function setToken(tokenValue) {
            $("input[name='tokenAccountName']").val(tokenValue);
        }

    })
</script>
</body>
</html>