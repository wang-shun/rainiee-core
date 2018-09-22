<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.common.RSAUtils" %>
<%@page import="com.dimeng.framework.http.entity.RoleBean" %>
<%@page import="com.dimeng.util.ObjectHelper" %>
<%@page import="com.dimeng.p2p.console.servlets.system.htzh.sys.AddSysUser" %>
<%@page import="com.dimeng.p2p.console.servlets.system.htzh.sys.SysUserList" %>
<%@page import="com.dimeng.p2p.common.RSAUtils" %>
<%@page import="org.bouncycastle.util.encoders.Hex" %>
<%@page import="com.dimeng.p2p.service.PtAccountManage" %>
<%@page import="com.dimeng.p2p.common.DimengRSAPulicKey" %>
<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <%@include file="/WEB-INF/include/script.jsp"%>
</head>
<body>
<%
    RoleBean[] roleBeans = ObjectHelper.convertArray(
            request.getAttribute("roles"), RoleBean.class);
    CURRENT_CATEGORY = "XTGL";
    CURRENT_SUB_CATEGORY = "GLYGL";
    PtAccountManage ptAccountManage = serviceSession.getService(PtAccountManage.class);
    DimengRSAPulicKey publicKey = (DimengRSAPulicKey) ptAccountManage.getPublicKey();
    String modulus = new String(Hex.encode(publicKey.getModulus().toByteArray()));
    String exponent = new String(Hex.encode(publicKey.getPublicExponent().toByteArray()));
    String userName = request.getParameter("accountName");
    if (!StringHelper.isEmpty(userName)) {
        userName = RSAUtils.decryptStringByJs(userName);
    }
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <!--加载内容-->
                <div class="p20">
                    <div class="border">
                        <div class="title-container"><i class="icon-i w30 h30 va-middle title-left-icon"></i>新增管理员</div>
                        <div class="content-container pl40 pt40 pr40 pb20">
                            <input id="newPasswordRegexId" type="hidden"
                                   value="<%configureProvider.format(out,SystemVariable.NEW_PASSWORD_REGEX);%>"/>
                            <input id="passwordRegexContentId" type="hidden"
                                   value="<%configureProvider.format(out,SystemVariable.PASSWORD_REGEX_CONTENT);%>"/>

                            <form action="<%=controller.getURI(request, AddSysUser.class)%>" method="post" class="form1"
                                  onsubmit="return onSubmit();">
                                <ul class="gray6">
                                    <li class="mb10">
											<span class="display-ib w200 tr mr5">
												<span class="red">*</span>用户名：
											</span>
                                        <input id="txtUserId" type="text" maxlength="18" mtest="/^[a-z]([\w]{5,17})$/i"
                                               mtestmsg="6-18个字，可使用字母、数字、下划线、需以字母开头"
                                               value="<%StringHelper.filterQuoter(out,userName);%>"
                                               class="text border w300 yw_w5"/>
                                        <input id="userId" name="accountName" type="hidden"/>
                                        <span tip></span>
                                        <span errortip class="" style="display: none"></span>
                                    </li>
                                    <li class="mb10">
											<span class="display-ib w200 tr mr5">
												<span class="red">*</span>姓名：
											</span>
                                        <input name="name" type="text" maxlength="15"
                                               value="<%StringHelper.filterQuoter(out, request.getParameter("name"));%>"
                                               class="text border w300 yw_w5 required" autocomplete="off"/>
                                        <span tip></span>
                                        <span errortip class="" style="display: none"></span>
                                    </li>
                                    <li class="mb10">
											<span class="display-ib w200 tr mr5">
												<span class="red">*</span>密码：
											</span>
										<!-- 防止浏览器自动填充记住的密码 -->
										<input type="password"  style="display:none"/>
                                        <input id="passwordOne" type="password" maxlength="20" mntest="/^<%=userName %>$/" mntestmsg="密码不能与用户名一致"
                                               class="text border w300 yw_w5 cpassword-a" autocomplete="off"/>
                                        <input id="truePwdOne" name="password" type="hidden"/>
                                        <span tip><%configureProvider.format(out,SystemVariable.PASSWORD_REGEX_CONTENT);%></span>
                                        <span errortip class="" style="display: none"></span>
                                    </li>
                                    <li class="mb10">
											<span class="display-ib w200 tr mr5">
												<span class="red">*</span>确认密码：
											</span>
                                        <input id="passwordTwo" type="password" maxlength="20"
                                               class="text border w300 yw_w5 cpassword-b" autocomplete="off"/>
                                        <input id="truePwdTwo" name="newPassword" type="hidden"/>
                                        <span tip></span>
                                        <span errortip class="" style="display: none"></span>
                                    </li>
                                    <li class="mb10">
											<span class="display-ib w200 tr mr5">
												<span class="red">*</span>手机号码：
											</span>
											<input name="phone" type="text" maxlength="11" value="<%StringHelper.filterQuoter(out, request.getParameter("phone"));%>" class="text border w300 yw_w5 required mobile"/>
											<span tip></span>
											<span errortip class="" style="display: none"></span>
										</li>
										<li class="mb10">
											<span class="display-ib w200 tr mr5">
												职称：
											</span>
											<input name="pos" type="text" maxlength="30" value="<%StringHelper.filterQuoter(out, request.getParameter("pos"));%>" class="text border w300 yw_w5"/>
											<span tip></span>
											<span errortip class="" style="display: none"></span>
										</li>
                                    <li class="mb10">
											<span class="display-ib w200 tr mr5">
												所属部门：
											</span>
                                        <input name="dept" type="text" maxlength="50" value="<%StringHelper.filterQuoter(out, request.getParameter("dept"));%>" class="text border w300 yw_w5"/>
                                    </li>
										<li class="mb10">
											<span class="display-ib w200 tr mr5">
												<span class="red">*</span>用户组：
											</span>
                                        <select name="roleId" class="border">
                                            <%
                                                if (roleBeans != null) {
                                                    for (RoleBean roleBean : roleBeans) {
                                                        if (roleBean == null) {
                                                            continue;
                                                        }
                                            %>
                                            <option value="<%=roleBean.getRoleId()%>">
                                                <%
                                                    StringHelper.filterHTML(out, roleBean.getName());
                                                %>
                                            </option>
                                            <%
                                                    }
                                                }
                                            %>
                                        </select>
                                    </li>
                                    <li class="mb10">
											<span class="display-ib w200 tr mr5">
												<span class="red">*</span>用户状态：
											</span>
                                        <select name="status" class="border">
                                            <option value="QY">启用</option>
                                            <option value="TY">锁定</option>
                                        </select>
                                    </li>
                                    <li>
                                        <div class="pl200 ml5">
                                            <%
                                                if (dimengSession.isAccessableResource(AddSysUser.class)) {
                                            %>
                                            <input type="submit" class="btn btn-blue2 radius-6 pl20 pr20 sumbitForme"
                                                   fromname="form1" value="确认"/>
                                            <%
                                            } else {
                                            %>
                                            <input type="button" class="disabled" value="确认"/>
                                            <%
                                                }
                                            %>
                                            <input type="button"
                                                   onclick="location.href='<%=controller.getURI(request, SysUserList.class) %>'"
                                                   class="btn btn-blue2 radius-6 pl20 pr20 ml40" value="取消"/>
                                        </div>
                                    </li>
                                </ul>
                            </form>
                        </div>
                    </div>
                </div>
                <!--加载内容 结束-->
            </div>
        </div>
    </div>
    <!--右边内容 结束-->
<!--内容-->
<div class="popup_bg" style="display: none;"></div>
<div id="info"></div>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/validation.js"></script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/security.js"></script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/dialog.js"></script>
<script type="text/javascript">
    function onSubmit() {
    	if(isNameAndPWDifferent("txtUserId","passwordOne")){
	        var modulus = "<%=modulus%>", exponent = "<%=exponent%>";
	        var key = RSAUtils.getKeyPair(exponent, '', modulus);
	        $("#userId").val(RSAUtils.encryptedString(key, $("#txtUserId").val()));
	        $("#truePwdOne").val(RSAUtils.encryptedString(key, $("#passwordOne").val()));
	        $("#truePwdTwo").val(RSAUtils.encryptedString(key, $("#passwordTwo").val()));
	        return true;
    	}else{
    		var $passwordOne = $("#passwordOne");
    		var $error = $passwordOne.nextAll("span[errortip]");
    		var $tip = $passwordOne.nextAll("span[tip]");
    		if($error){
    			$error.addClass("error_tip");
                $error.html("密码不能与用户名一致！");
                $tip.hide();
                $error.show();
    		}
	    	return false;
    	}
    }
    $(function(){
		$("#passwordOne").keydown(function(d){
			var c = d.keyCode || d.charCode;
            if (c==32) {
                d.preventDefault();
            }
		});
		$("#passwordTwo").keydown(function(d){
			var c = d.keyCode || d.charCode;
            if (c==32) {
                d.preventDefault();
            }
		});
	});
</script>
<%
    String warnMessage = controller.getPrompt(request, response, PromptLevel.WARRING);
    if (!StringHelper.isEmpty(warnMessage)) {
%>
<script type="text/javascript">
    $("#info").html(showDialogInfo("<%=warnMessage%>", "perfect"));
    $("div.popup_bg").show();
</script>
<%}%>
</body>
</html>