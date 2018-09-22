<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.repeater.business.entity.SysUser"%>
<%@page import="com.dimeng.util.ObjectHelper"%>
<%@page import="com.dimeng.p2p.common.RSAUtils"%>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.zhgl.ZhList"%>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.zhgl.AddGr"%>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.zhgl.CheckNameExist"%>
<%@page import="org.bouncycastle.util.encoders.Hex" %>
<%@page import="com.dimeng.p2p.service.PtAccountManage" %>
<%@page import="com.dimeng.p2p.common.DimengRSAPulicKey" %>
<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <%@include file="/WEB-INF/include/script.jsp" %>
</head>
<body>
<%
    CURRENT_CATEGORY = "YHGL";
    CURRENT_SUB_CATEGORY = "ZHGL";
    String userName = request.getParameter("userName");
    PtAccountManage ptAccountManage = serviceSession.getService(PtAccountManage.class);
    DimengRSAPulicKey publicKey = ptAccountManage.getPublicKey();
    String modulus = new String(Hex.encode(publicKey.getModulus().toByteArray()));
    String exponent = new String(Hex.encode(publicKey.getPublicExponent().toByteArray()));
    if (!StringHelper.isEmpty(userName)) {
        userName = RSAUtils.decryptStringByJs(userName);
    }
		SysUser[] users = ObjectHelper.convertArray(request.getAttribute("ywUsers"), SysUser.class);
		boolean is_business = Boolean.parseBoolean(configureProvider.getProperty(BusinessVariavle.IS_BUSINESS));
	%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
      	 <div class="p20">
      	 <form action="<%=controller.getURI(request, AddGr.class)%>" method="post" class="form1" onsubmit="return onSubmit();">
          <div class="border">
            <div class="title-container"><i class="icon-i w30 h30 va-middle title-left-icon"></i>新增个人账号</div>
            <div class="content-container pl40 pt40 pr40 pb20">
              <ul class="gray6">
                <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>登录账号：</span>
                	<input id="txUserId" type="text" maxlength="18" value="<%StringHelper.filterQuoter(out,userName);%>" onblur="nameCheck()" class="text border w300 yw_w5"/>
					<input type="hidden" id="userId" name="userName"/>
					<span class="gray9"><%configureProvider.format(out, SystemVariable.USERNAME_REGEX_CONTENT);%></span>
                </li>
                <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>密码：</span>
                	<input id="passwordOne" type="password" class="text border w300 yw_w5 password-a" onblur="passwordCheck()" maxlength="20" autocomplete="off"/>
                    <input id="truePwdOne" name="password" type="hidden"/>
					<span class="gray9"><%configureProvider.format(out,SystemVariable.PASSWORD_REGEX_CONTENT);%></span>
                </li>
                <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>确认密码：</span>
                	<input id="passwordTwo" type="password" class="text border w300 yw_w5 password-b" onblur="repasswordCheck()" maxlength="20" autocomplete="off"/>
                    <input id="truePwdTwo" name="newPassword" type="hidden"/>
					<span class="gray9">请再次输入密码</span>
                </li>
                <%if(is_business){ %>
                <li class="mb10"><span class="display-ib w200 tr mr5">业务员工号：</span>
                	<select name="employNum" class="border mr20 h32 mw100"  id="staff" >
						<option value=""></option>
						<%if(users!=null && users.length>0){ for (SysUser user : users) {%>
						<option value="<%=user.employNum%>" ><%=user.employNum%>/<%=user.name %></option>
						<%}}%>
					</select>
                </li>
                <%} %>
                <li><div class="pl200 ml5">
                <%
											if (dimengSession.isAccessableResource(AddGr.class)) {
										%>
                <input type="submit" value="提交 " class="btn btn-blue2 radius-6 pl20 pr20 sumbitForme" />
                <%
											} else {
										%>
										<input type="button" class="disabled" value="确认" />
										<%
											}
										%>
                <input type="button" class="btn btn-blue2 radius-6 pl20 pr20 ml40" onclick="location.href='<%=controller.getURI(request, ZhList.class) %>'" value="取消"></div></li>
              </ul>

                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
<%
    String warringMessage = controller.getPrompt(request, response, PromptLevel.ERROR);
    if (!StringHelper.isEmpty(warringMessage)) {
%>
<div class="w440 thickbox thickpos" style="margin:-80px 0 0 -220px;" id="showDiv">
    <div class="info clearfix">
        <div class="clearfix">
            <span class="fl tips"><%StringHelper.filterHTML(out, warringMessage); %></span>
        </div>
        <div class="dialog_btn"><input type="button" name="button2" onclick="$('#showDiv').hide()" value="确认"
                                       class="btn4 ml50"/></div>
    </div>
</div>
<%} %>
<div class="popup_bg" style="display: none;"></div>
<div id="info"></div>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/validation.js"></script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/security.js"></script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/dialog.js"></script>
<script type="text/javascript">
    //用户名正则表达式
    var loginNameRex =<%configureProvider.format(out,SystemVariable.NEW_USERNAME_REGEX);%>;
    //密码正则表达式
    var passwordRex =<%configureProvider.format(out,SystemVariable.NEW_PASSWORD_REGEX);%>;
    var isNull = /^[\s]{0,}$/;
    var _nURL = '<%=controller.getURI(request, CheckNameExist.class)%>';

    function nameCheck() {
        var ipt = $("#txUserId");
        var val = ipt.val();
        var p = ipt.parent().find("span").eq(1);
        p.removeClass();
        p.addClass("gray9");
        p.text('<%configureProvider.format(out,SystemVariable.USERNAME_REGEX_CONTENT);%>');
        if (isNull.test(val)) {
            p.addClass("red");
            p.removeClass("gray9");
            p.text("用户名不能为空");
            return false;
        } else if (!loginNameRex.test(val)) {
            p.addClass("red");
            p.removeClass("gray9");
            return false;
        }
    	/* if(!isNameAndPWDifferent("txUserId","passwordOne")){
    		p.addClass("red");
    		p.removeClass("gray9");
    		p.text("密码不能与用户名一致");
            return false;
    	} */
        $.post(_nURL, {
            loginName: val
        }, function (data) {
            if ($.trim(data) == 'true') {
                p.addClass("red");
                p.removeClass("gray9");
                p.text("该用户名已存在，请输入其他用户名");
                return false;
            }
        });
        return true;
    }

    function passwordCheck() {
        var ipt = $("#passwordOne");
        var _ipt = $("#passwordTwo");
        var val = ipt.val();
        var p = ipt.parent().find("span").eq(1);
        var _p = _ipt.parent().find("span").eq(1);
        p.removeClass();
        p.addClass("gray9");
        p.text('<%configureProvider.format(out,SystemVariable.PASSWORD_REGEX_CONTENT);%>');
        //输入确认密码后又修改密码的判断
        if (!isNull.test(_ipt.val())) {
            if (_ipt.val() == val) {
                _p.removeClass();
                _p.addClass("gray9");
                _p.text("请再次输入密码");
            } else {
                _p.removeClass();
                _p.addClass("red");
                _p.text("您两次输入的密码不一致");
                return false
            }
        }
        if (isNull.test(val)) {
            p.addClass("red");
            p.removeClass("gray9");
            p.text("密码不能为空");
            return false;
        } else if(!/^\S+$/.test(val)){
    		
    		p.addClass("red");
    		p.removeClass("gray9");
    		p.text("密码不能包含空格");
    		return false;
    		
    	} else if (!passwordRex.test(val)) {
            p.addClass("red");
            p.removeClass("gray9");
            return false;
        }
    	if(!isNameAndPWDifferent("txUserId","passwordOne")){
    		p.addClass("red");
    		p.removeClass("gray9");
    		p.text("密码不能与用户名一致");
    		return false;
    	}
        return true;
    }

    function repasswordCheck() {
        var ipt = $("#passwordOne");
        var _ipt = $("#passwordTwo");
        var val = _ipt.val();
        var p = _ipt.parent().find("span").eq(1);
        p.removeClass();
        p.addClass("gray9");
        p.text('请再次输入密码');
        if (isNull.test(ipt.val())) {
            p.addClass("red");
            p.removeClass("gray9");
            p.text("请先输入密码");
            return false;
        } else if (isNull.test(val)) {
            p.addClass("red");
            p.removeClass("gray9");
            p.text("密码不能为空");
            return false;
        } else if(!/^\S+$/.test(val)){
    		
    		p.addClass("red");
    		p.removeClass("gray9");
    		p.text("密码不能包含空格");
    		return false;
    		
    	} else if (val != ipt.val()) {
            p.addClass("red");
            p.removeClass("gray9");
            p.text("您两次输入的密码不一致");
            return false;
        }
        return true;
    }

    function onSubmit() {
        if (nameCheck() && passwordCheck() && repasswordCheck()) {
            var modulus = "<%=modulus%>", exponent = "<%=exponent%>";
            var key = RSAUtils.getKeyPair(exponent, '', modulus);
            $("#userId").val(RSAUtils.encryptedString(key, $("#txUserId").val()));
            $("#truePwdOne").val(RSAUtils.encryptedString(key, $("#passwordOne").val()));
            $("#truePwdTwo").val(RSAUtils.encryptedString(key, $("#passwordTwo").val()));
            return true;
        }
        return false;
    }
</script>
<%
    String message = controller.getPrompt(request, response, PromptLevel.INFO);
    if (!StringHelper.isEmpty(message)) {
%>

<script type="text/javascript">
    $("#info").html(showDialogInfo("<%=message%>", "yes"));
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

<%
    String warnMessage = controller.getPrompt(request, response, PromptLevel.WARRING);
    if (!StringHelper.isEmpty(warnMessage)) {
%>
<script type="text/javascript">
    $("#info").html(showDialogInfo("<%=warnMessage%>", "wrong"));
    $("div.popup_bg").show();
</script>
<%
    }
%>
</body>
</html>