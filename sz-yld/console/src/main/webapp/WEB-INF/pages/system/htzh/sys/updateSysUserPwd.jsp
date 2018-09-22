<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.console.servlets.system.htzh.sys.SysUserList"%>
<%@page import="com.dimeng.p2p.repeater.business.entity.BusinessOptLog"%>
<%@page import="com.dimeng.p2p.common.Constant"%>
<%@page import="com.dimeng.framework.http.entity.RoleBean"%>
<%@page import="com.dimeng.p2p.common.enums.SysAccountStatus"%>
<%@page import="com.dimeng.p2p.console.servlets.system.htzh.sys.UpdateSysUser"%>
<%@page import="com.dimeng.p2p.console.servlets.system.htzh.sys.UpdateSysUserPwd"%>
<%@page import="com.dimeng.p2p.modules.systematic.console.service.entity.SysUser"%>
<%@page import="com.dimeng.util.ObjectHelper"%>
<%@page import="com.dimeng.p2p.common.RSAUtils" %>
<%@page import="org.bouncycastle.util.encoders.Hex" %>
<%@page import="com.dimeng.p2p.service.PtAccountManage" %>
<%@page import="com.dimeng.p2p.common.DimengRSAPulicKey" %>
<%@page import="com.dimeng.p2p.modules.systematic.console.service.SysUserManage" %>
<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <%@include file="/WEB-INF/include/script.jsp"%>
</head>
<body>
	<%
		CURRENT_CATEGORY = "XTGL";
		CURRENT_SUB_CATEGORY = "GLYGL";
		int id = IntegerParser.parse(request.getParameter("id"));
        SysUserManage sysUserManage = serviceSession.getService(SysUserManage.class);
        SysUser sysUser = sysUserManage.get(id);
		
		//SysUser sysUser = ObjectHelper.convert(request.getAttribute("sysUser"), SysUser.class);
		if (sysUser == null) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		PtAccountManage ptAccountManage = serviceSession.getService(PtAccountManage.class);
		DimengRSAPulicKey publicKey = (DimengRSAPulicKey)ptAccountManage.getPublicKey();
		String modulus = new String(Hex.encode(publicKey.getModulus().toByteArray()));
		String exponent = new String(Hex.encode(publicKey.getPublicExponent().toByteArray()));
	%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
					<!--加载内容-->
					<div class="p20">
						<div class="border">
							<div class="title-container"><i class="icon-i w30 h30 va-middle title-left-icon"></i>修改管理员密码</div>
							<div class="content-container pl40 pt40 pr40 pb20">
								<input id="newPasswordRegexId" type="hidden" value="<%configureProvider.format(out,SystemVariable.NEW_PASSWORD_REGEX);%>" />
								<input id="passwordRegexContentId" type="hidden" value="<%configureProvider.format(out,SystemVariable.PASSWORD_REGEX_CONTENT);%>"/>
								<form action="<%=controller.getURI(request, UpdateSysUserPwd.class)%>" method="post" class="form1" onsubmit="return onSubmit();">
									<input type="hidden" value="<%=sysUser.id %>" name="id">
									<input type="hidden" value="<%=sysUser.employNum %>" name="employNum">
									<ul class="gray6">
										<li class="mb10">
											<span class="display-ib w200 tr mr5">
												<span class="red">*</span>用户名：
											</span>
                                        	<%StringHelper.filterQuoter(out, sysUser.accountName);%>
                                    	</li>
                                    	<li class="mb10">
											<span class="display-ib w200 tr mr5">
												<span class="red">*</span>密码：
											</span>
	                                        <input id="passwordOne" type="password" maxlength="20" mntest="/^<%=sysUser.accountName %>$/" mntestmsg="密码不能与用户名一致"
	                                               class="text border w300 yw_w5 required cpassword-a" autocomplete="off"/>
											<input id="truePwdOne" name="password" type="hidden"/>
	                                        <span tip><%configureProvider.format(out,SystemVariable.PASSWORD_REGEX_CONTENT);%></span>
	                                        <span errortip class="" style="display: none"></span>
	                                    </li>
	                                    <li class="mb10">
												<span class="display-ib w200 tr mr5">
													<span class="red">*</span>确认密码：
												</span>
	                                        <input id="passwordTwo" type="password" maxlength="20"
	                                               class="text border w300 yw_w5 required cpassword-b" autocomplete="off"/>
											<input id="truePwdTwo" name="newPassword" type="hidden"/>
	                                        <span tip></span>
	                                        <span errortip class="" style="display: none"></span>
	                                    </li>
										<li>
											<div class="pl200 ml5">
												<%
													if (dimengSession.isAccessableResource(UpdateSysUserPwd.class)) {
												%>
												<input type="submit" class="btn btn-blue2 radius-6 pl20 pr20 sumbitForme" fromname="form1" value="确认" />
												<%
													} else {
												%>
												<input type="button" class="disabled" value="确认" />
												<%
													}
												%>
												<input type="button" onclick="location.href='<%=controller.getURI(request, SysUserList.class) %>'" class="btn btn-blue2 radius-6 pl20 pr20 ml40" value="取消" />
											</div>
										</li>			
									</ul>
								</form>
								<div class="clear"></div>
							</div>
						<div class="box2 clearfix"></div>
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
			var modulus = "<%=modulus%>", exponent = "<%=exponent%>";
			var key = RSAUtils.getKeyPair(exponent, '', modulus);
			$("#truePwdOne").val(RSAUtils.encryptedString(key,$("#passwordOne").val()));
			$("#truePwdTwo").val(RSAUtils.encryptedString(key,$("#passwordTwo").val()));
			return true;
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
</body>
</html>