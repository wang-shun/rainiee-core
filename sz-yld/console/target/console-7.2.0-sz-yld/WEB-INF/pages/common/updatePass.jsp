<%@page import="com.dimeng.p2p.modules.systematic.console.service.SysUserManage"%>
<%@page import="com.dimeng.p2p.common.Constant"%>
<%@page import="com.dimeng.p2p.console.servlets.common.UpdatePass"%>
<%@page import="com.dimeng.p2p.console.servlets.common.Index" %>
<%@page import="com.dimeng.p2p.common.RSAUtils" %>
<%@page import="org.bouncycastle.util.encoders.Hex" %>
<%@page import="com.dimeng.p2p.service.PtAccountManage" %>
<%@page import="com.dimeng.p2p.common.DimengRSAPulicKey" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <%@include file="/WEB-INF/include/script.jsp" %>
</head>
<body>
<%
    CURRENT_CATEGORY = "SY";
    PtAccountManage ptAccountManage = serviceSession.getService(PtAccountManage.class);
    DimengRSAPulicKey publicKey = (DimengRSAPulicKey) ptAccountManage.getPublicKey();
    String modulus = new String(Hex.encode(publicKey.getModulus().toByteArray()));
    String exponent = new String(Hex.encode(publicKey.getPublicExponent().toByteArray()));
    SysUserManage sysUserM = serviceSession.getService(SysUserManage.class);
  	com.dimeng.p2p.modules.systematic.console.service.entity.SysUser sysU = sysUserM.get(serviceSession.getSession().getAccountId());
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <div class="p20">
                    <input id="newPasswordRegexId" type="hidden"
                           value="<%configureProvider.format(out,SystemVariable.NEW_PASSWORD_REGEX);%>"/>
                    <input id="passwordRegexContentId" type="hidden"
                           value="<%configureProvider.format(out,SystemVariable.PASSWORD_REGEX_CONTENT);%>"/>

                    <form action="<%=controller.getURI(request, UpdatePass.class)%>" method="post" class="form1"
                          onsubmit="return onSubmit();" >
                        <div class="border">
                            <div class="title-container"><i class="icon-i w30 h30 va-middle title-left-icon"></i>修改密码
                            </div>
                            <div class="content-container pl40 pr40 pb20">
                                <ul class="gray6">
                                    <li class="mb10"><span class="display-ib w200 tr mr5">&nbsp;</span>
                                    </li>
                                    <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>原密码</span>
                                        <input id="oldPassword" type="password" class="text border w300 yw_w5 required"
                                               autocomplete="off"/>
                                        <input type="hidden" id="userId" name="userName"/>
                                        <input id="trueOldPwd" name="oldPassWord" type="hidden"/>
                                        <span tip></span>
                                        <span errortip class="" style="display: none"></span>
                                    </li>
                                    <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>新密码</span>
                                        <input id="passwordOne" type="password" mntest="/^<%=sysU.accountName %>$/" mntestmsg="密码不能与用户名一致"
                                               class="text border w300 yw_w5 required cpassword-a min-length-6 max-length-20"
                                               autocomplete="off"/>
                                        <input id="truePwdOne" name="newPassWord1" type="hidden"/>
                                        <span tip><%configureProvider.format(out,SystemVariable.PASSWORD_REGEX_CONTENT);%></span>
                                        <span errortip class="" style="display: none"></span>
                                    </li>
                                    <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>重复新密码</span>
                                        <input id="passwordTwo" type="password"
                                               class="text border w300 yw_w5 required cpassword-b"
                                               autocomplete="off"/>
                                        <input id="truePwdTwo" name="newPassWord2" type="hidden"/>
                                        <span tip></span>
                                        <span errortip class="" style="display: none"></span>
                                    </li>

                                    <li>
                                        <div class="pl200 ml5">
                                            <input type="submit" class="btn btn-blue2 radius-6 pl20 pr20 sumbitForme"
                                                   fromname="form1" value="确认"/>
                                                   <%if(Constant.BUSINESS_ROLE_ID != sysU.roleId){ %>
                                            <input type="button" class="btn btn-blue2 radius-6 pl20 pr20 ml40"
                                                   onclick="location.href='<%=controller.getURI(request, Index.class) %>?isLoad=true'"
                                                   value="返回">
                                                  <%}else{ %>
                                                  <input type="button" class="btn btn-blue2 radius-6 pl20 pr20 ml40"
                                                   onclick="location.href='<%=controller.getURI(request, com.dimeng.p2p.console.servlets.system.htzh.business.BusinessUserList.class) %>'"
                                                   value="返回">
                                                  <%} %>
                                        </div>
                                    </li>
                                </ul>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
	<div class="popup_bg" style="display: none;"></div>
	<div id="info"></div>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/validation.js"></script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/security.js"></script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/dialog.js"></script>

<%
    String warringMessage = controller.getPrompt(request, response, PromptLevel.WARRING);
    if (!StringHelper.isEmpty(warringMessage)) {
%>
<script type="text/javascript">
    $("#info").html(showDialogInfo("<%=warringMessage%>", "wrong"));
    $("div.popup_bg").show();
</script>
<%
    }
%>

<%
    String infoMessage = controller.getPrompt(request, response, PromptLevel.INFO);
    if (!StringHelper.isEmpty(infoMessage)) {
%>
<script type="text/javascript">
    $("#info").html(showDialogInfo("<%=infoMessage%>", "yes"));
    $("div.popup_bg").show();
</script>
<%
    }
%>

<%
    String errorMessage = controller.getPrompt(request, response, PromptLevel.ERROR);
    if (!StringHelper.isEmpty(errorMessage)) {
%>
<script type="text/javascript">
    $("#info").html(showDialogInfo("<%=errorMessage%>", "wrong"));
    $("div.popup_bg").show();
</script>
<%
    }
%>
<script type="text/javascript">
    function onSubmit() {
        var modulus = "<%=modulus%>", exponent = "<%=exponent%>";
        var key = RSAUtils.getKeyPair(exponent, '', modulus);
        $("#trueOldPwd").val(RSAUtils.encryptedString(key, $("#oldPassword").val()));
        $("#truePwdOne").val(RSAUtils.encryptedString(key, $("#passwordOne").val()));
        $("#truePwdTwo").val(RSAUtils.encryptedString(key, $("#passwordTwo").val()));
        return true;
    }
</script>
</body>
</html>