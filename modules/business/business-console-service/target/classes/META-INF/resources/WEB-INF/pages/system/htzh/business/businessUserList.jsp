<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.common.Constant"%>
<%@page import="com.dimeng.p2p.common.enums.SysAccountStatus"%>
<%@page import="com.dimeng.p2p.console.servlets.AbstractConsoleServlet"%>
<%@page import="com.dimeng.p2p.modules.systematic.console.service.SysUserManage"%>
<%@page import="com.dimeng.p2p.repeater.business.entity.SysUser"%>
<%@ page import="com.dimeng.p2p.console.servlets.system.htzh.business.*" %>
<%@ page import="com.dimeng.util.ObjectHelper" %>
<%@page import="com.dimeng.p2p.console.servlets.common.Update" %>
<%@page import="org.bouncycastle.util.encoders.Hex" %>
<%@page import="com.dimeng.p2p.service.PtAccountManage" %>
<%@page import="com.dimeng.p2p.common.DimengRSAPulicKey" %>
<html>
<head>
	<%@include file="/WEB-INF/include/meta.jsp"%>
<%@include file="/WEB-INF/include/style.jsp"%>
<%@include file="/WEB-INF/include/script.jsp"%>
</head>
<body>
	<%
	    PagingResult<SysUser> result = (PagingResult<SysUser>) request.getAttribute("result");
	%>
	<%
	    CURRENT_CATEGORY = "XTGL";
		CURRENT_SUB_CATEGORY = "YWYGL";
		String createTimeStart = request.getParameter("createTimeStart");
		String createTimeEnd=request.getParameter("createTimeEnd");
		SysUserManage sysUserMl = serviceSession.getService(SysUserManage.class);
		com.dimeng.p2p.modules.systematic.console.service.entity.SysUser sysU = sysUserMl.get(serviceSession.getSession().getAccountId());
		SysUser[] users = ObjectHelper.convertArray(request.getAttribute("ywUsers"), SysUser.class);
		boolean isOneLogin = sysUserMl.isOneLogin();
		PtAccountManage ptAccountManage = serviceSession.getService(PtAccountManage.class);
		DimengRSAPulicKey publicKey = (DimengRSAPulicKey) ptAccountManage.getPublicKey();
		String modulus = new String(Hex.encode(publicKey.getModulus().toByteArray()));
		String exponent = new String(Hex.encode(publicKey.getPublicExponent().toByteArray()));
	%>
	
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
					<input type="hidden" name="token" value="" />
					<form
						action="<%=controller.getURI(request, BusinessUserList.class)%>"
						method="post" name="form1" id="form1">
						<div class="p20">
						<%if(Constant.BUSINESS_ROLE_ID != sysU.roleId){ %>
							<div class="border">
								<div class="title-container">
									<i class="icon-i w30 h30 va-middle title-left-icon"></i>业务员管理
								</div>
								
								<div class="content-container pl40 pt30 pr40">
									<ul class="gray6 input-list-container clearfix">
										<li><span class="display-ib mr5">业务员工号： </span> <input
											type="text" name="employNum"
											value="<%StringHelper.filterQuoter(out,request.getParameter("employNum"));%>"
											 class="text border pl5 mr20" /></li>
										<li><span class="display-ib mr5">姓名： </span> <input
											type="text" name="name"
											value="<%StringHelper.filterQuoter(out, request.getParameter("name"));%>"
											id="textfield7" class="text border pl5 mr20" /></li>
										<li><span class="display-ib mr5">用户名： </span> <input
											type="text" name="accountName"
											value="<%StringHelper.filterQuoter(out,request.getParameter("accountName"));%>"
											class="text border pl5 mr20" /></li>
										<li>
											<span class="display-ib mr5">所属部门：</span> <input type="text" name="dept"
																							 value="<%StringHelper.filterQuoter(out, request.getParameter("dept"));%>"
																							 id="textfield8"
																							 class="text border pl5 mr20"/>
										</li>
										<li><span class="display-ib mr5">状态： </span> <select
											name="status" class="border mr20 h32 mw100">
												<option value="0">全部</option>
												<%
												    for(SysAccountStatus status:SysAccountStatus.values())
																			{
												%>
												<option value="<%=status.name()%>"
													<%if (status.name().equals(request.getParameter("status"))) {%>
													selected="selected" <%}%>><%=status.getName()%></option>
												<%
												    }
												%>
										</select></li>
										<li><a href="javascript:void(0);"
											onclick="$('#form1').submit();"
											class="btn btn-blue radius-6 mr5 pl1 pr15"><i
												class="icon-i w30 h30 va-middle search-icon "></i>搜索</a></li>
										<li>
											<%
											    if (dimengSession.isAccessableResource(ExportBusinessUser.class)) {
											%>
											<a
											href="javascript:void(0)" onclick="toExport('<%=controller.getURI(request, ExportBusinessUser.class)%>')"
											class="btn btn-blue radius-6 mr5  pl1 pr15"><i
												class="icon-i w30 h30 va-middle export-icon "></i>导出</a> <%
										     }else{
										 %>
											<span class="btn btn-gray radius-6 mr5 pl1 pr15"><i
												class="icon-i w30 h30 va-middle export-icon "></i>导出</span> <%
										     }
										 %>
										</li>
									</ul>
								</div>
							</div>
							<%} %>
							<div class="border mt20 table-container">
								<table class="table table-style gray6 tl">
									<thead>
										<tr class="title tc">

											<th>序号</th>
											<th>业务员工号</th>
											<th>姓名</th>
											<th>用户名</th>
											<th>手机号码</th>
											<th>职称</th>
											<th>所属部门</th>
											<th>用户状态</th>
											<th>创建时间</th>
											<th>操作</th>
										</tr>
									</thead>
									<tbody class="f12">
										<%
										    SysUser[] sysUsers = result.getItems();
											if (sysUsers != null && sysUsers.length > 0) {
												int i = 1;
												for (SysUser sysUser : sysUsers) {
													if (sysUser == null) {
														continue;
													}
										%>
										<tr class="tc">
											<td><%=i++%></td>
											<td>
												<%
												    StringHelper.filterHTML(out, sysUser.employNum);
												%>
											</td>
											<td>
												<%
												    StringHelper.filterHTML(out, sysUser.name);
												%>
											</td>
											<td>
												<%
												    StringHelper.filterHTML(out, sysUser.accountName);
												%>
											</td>


											<td>
												<%
												    StringHelper.filterHTML(out, sysUser.phone);
												%>
											</td>
											<td>
												<%
												    StringHelper.filterHTML(out, sysUser.pos);
												%>
											</td>
											<td>
												<%
													StringHelper.filterHTML(out, sysUser.dept);
												%>
											</td>
											<td>
												<%
												    StringHelper.filterHTML(out, sysUser.status.getName());
												%>
											</td>
											<td><%=DateTimeParser.format(sysUser.createTime)%></td>

											<td>
												<% if (dimengSession.isAccessableResource(CustomerDetail.class)) { %>
												<a href="<%=controller.getURI(request,CustomerDetail.class)%>?employNum=<%=sysUser.employNum%>" class="link-blue mr10">客户详情</a>
												<%}else{ %>
												<a href="javascript:void(0)" class="disabled mr10">客户详情</a>
												<%} %>
												<% if (dimengSession.isAccessableResource(PerformanceStatistics.class)) { %>
												<a href="<%=controller.getURI(request,PerformanceStatistics.class)%>?employNum=<%=sysUser.employNum%>" class="link-blue mr10">业绩详情</a>
												<%}else{ %>
												<a href="javascript:void(0)" class="disabled mr10">业绩详情</a>
												<%} %>
												<%	if(Constant.BUSINESS_ROLE_ID != sysU.roleId){
													if (dimengSession.isAccessableResource(ReplaceEmployNum.class) && sysUser.customNum > 0) {  %>
												<a href="javascript:void(0);" onclick="selectEmps('<%=sysUser.employNum%>')" class="link-blue mr10">客户转出</a>
												<%}else{ %>
												<a href="javascript:void(0)" class="disabled mr10">客户转出</a>
												<%}} %>
											</td>
										</tr>
										<%
										    }}else{
										%>
										<tr>
											<td colspan="10" class="tc">暂无数据</td>
										</tr>
										<%
										    }
										%>
									</tbody>
								</table>
							</div>
							<!--分页-->
							<%
							    AbstractConsoleServlet.rendPagingResult(out, result);
							%>
							<!--分页 end-->
						</div>
					</form>
				</div>
			</div>
		</div>
		<!--右边内容 结束-->
	<div id="replaceEmp" class="popup-box hide" style="min-height: 50px;">
		<div class="popup-title-container" >
			<h3 class="pl20 f18">客户转出</h3>
			<a class="icon-i popup-close2" onclick="closeInfo()"></a></div>
		<div class="popup-content-container-2">
			<form action="<%=controller.getURI(request, ReplaceEmployNum.class)%>" method="post" class="form" id="replaceNum">
				<input type="hidden" name="employNumOld" id="employNumOld"/>
				<div class="p30">
					<ul class="gray6">
						<li class="mb15"><span class="display-ib tr mr5 w120 fl">选择业务员：</span>
							<div class="pl120">
								<select name="employNumNew" class="border mr20 h32 mw100 required"  id="employNumNew" >

								</select>
								<span class="removeHtml red" errortip></span>
							</div>
						</li>
					</ul>
					<div class="tc f16 mt20">
						<input type="button" value="确定"  class="btn-blue2 btn white radius-6 pl20 pr20 sumbitForme" fromname="form" onclick="toSubmit();"/>
						<!-- <a class="btn btn-blue2 radius-6 pl20 pr20 ml40" href="javascript:closeInfo();">取消</a> -->
						<input type="button" value="取消"  class="btn btn-blue2 radius-6 pl20 pr20 ml40" onclick="closeInfo()"/>
					</div>
				</div>
			</form>
		</div>
	</div>
	<div id="info"></div>
	<div class="popup_bg" style="display: none;"></div>
	<%
		String message = controller.getPrompt(request, response, PromptLevel.WARRING);
		if (isOneLogin || !StringHelper.isEmpty(message)) {
	%>
	<div class="popup_bg"></div>
	<div class="popup-box" id="updatePass" style="margin: -120px 0 0 -220px;">
		<div class="popup-title-container">
			<span class="fl pl15">修改密码</span>
		</div>
		<input id="newPasswordRegexId" type="hidden"
			   value="<%configureProvider.format(out,SystemVariable.NEW_PASSWORD_REGEX);%>"/>
		<input id="passwordRegexContentId" type="hidden"
			   value="<%configureProvider.format(out,SystemVariable.PASSWORD_REGEX_CONTENT);%>"/>

		<form action="<%=controller.getURI(request, Update.class)%>?isLoad=true" method="post" class="form1"
			  onsubmit="return onSubmit();">
			<div class="border">
				<div class="content-container pt40 pr40 pb20">
					<ul class="gray6">
						<li class="mb10">
							<div class="tc f16">
								为保障账户安全，首次登录请修改您的密码！
							</div>
						</li>
						<li class="mb10">
							<span class="display-ib w200 tr mr5"><span class="red">*</span>原密码：</span>
							<input id="oldPassword" type="password" class="text border required" name="oldPassWord"/>
							<span class="display-b pl200 ml10" tip></span>
							<span errortip class="display-b pl200 ml10" style="display: none" autocomplete="off"></span>
						</li>
						<li class="mb10">
							<span class="display-ib w200 tr mr5"><span class="red">*</span>新密码：</span>
							<input id="passwordOne" type="password"
								   class="text border required cpassword-a min-length-6 max-length-20" name="newPassWord1"/>
							<span class="display-b pl200 ml10" tip></span>
							<span errortip class="display-b pl200 ml10" style="display: none" autocomplete="off"></span>
						</li>
						<li class="mb10">
							<span class="display-ib w200 tr mr5"><span class="red">*</span>重复新密码：</span>
							<input id="passwordTwo" type="password" class="text border required cpassword-b" name="newPassWord2"/>
							<span class="display-b pl200 ml10" tip></span>
							<span errortip class="display-b pl200 ml10" style="display: none" autocomplete="off"></span>
						</li>
						<li class="mb10" id="msginfo">
							<div class="display-b pl200 ml10" style="color: red">
								<%
									StringHelper.filterHTML(out, message);
								%>
							</div>
						</li>
						<li class="mb10">
							<div class="tc f16">
								<input type="submit" name="button2" id="button2" value="确认" fromname="form1"
									   class="btn btn-blue2 radius-6 pl20 pr20 sumbitForme"/>
							</div>
						</li>
					</ul>
				</div>
			</div>
		</form>
	</div>
	<%
		}
	%>
	<script type="text/javascript"
		src="<%=controller.getStaticPath(request)%>/js/dialog.js"></script>
	<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/security.js"></script>
		<%if(sysU.roleId == Constant.BUSINESS_ROLE_ID){ %>
		<script type="text/javascript">
		parent.frames["ContentFrame1"].cols = "0,*";
		$(parent.frames["topFrame"].document).find("a").removeClass("select-a");
		$(parent.frames["topFrame"].document).find("a[data-title='system']").addClass("select-a");
		</script>
		<%} %>
	<script type="text/javascript">
	$(function(){
		$("input[type='password']").focus(function(){
			$("#msginfo").hide();
		});
	});
	function toExport(url){
		$("#form1").attr("action",url);
		$("#form1").submit();
		$("#form1").attr("action",'<%=controller.getURI(request, BusinessUserList.class)%>');
	}

	function replaceBus(){
		$(".removeHtml").html("");
		$("div.popup_bg").show();
		$("#replaceEmp").show();
	}

	function toSubmit(){
		if($("#employNumNew").val() == "" || $("#employNumNew").val() == null){
			$(".removeHtml").html("请选择业务员");
			return false;
		}else{
			$(".removeHtml").html("");
			$("#replaceEmp").hide();
			$("#info").html(showConfirmDiv("确定将全部用户转至"+$("#employNumNew").find("option:selected").text()+"名下吗？",null,null));
			return true;
		}
	}

	function toConfirm(param,type){
		$("#replaceNum").submit();
	}

		function selectEmps(empNum){
			$("#employNumOld").val(empNum);
			$.ajax({
				type:"post",
				url:"<%=controller.getURI(request, SelectEmpNum.class)%>",
				dataType:"json",
				data:{"employNum":empNum},
				success:function(data){
					$("#employNumNew option").remove();
					$('<option value="">--请选择--</option>').appendTo("#employNumNew");
					if(data != null && data.length > 0){
						for(var i = 0; i < data.length ; i++ ){
							$("<option value='"+data[i].employNum+"'>"+data[i].employNum+"/"+data[i].name+"</option>").appendTo("#employNumNew");
						}
					}
					replaceBus();
				},
				error:function(){
					$("#info").html(showDialogInfo("系统异常，请稍后重试！", "error"));
				}
			});
		}

	function onSubmit() {
		var modulus = "<%=modulus%>", exponent = "<%=exponent%>";
		var key = RSAUtils.getKeyPair(exponent, '', modulus);
		$("#oldPassword").val(RSAUtils.encryptedString(key, $("#oldPassword").val()));
		$("#passwordOne").val(RSAUtils.encryptedString(key, $("#passwordOne").val()));
		$("#passwordTwo").val(RSAUtils.encryptedString(key, $("#passwordTwo").val()));
		return true;
	}
	window.onload = function(){
		var flag = <%=isOneLogin%>;
		if(!flag)
		{
			$(parent.frames["topFrame"].document).find("div.popup_bg").hide();
		}
	}
	</script>
	
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
    $("#info").html(showSuccInfo("<%=errorMessage%>", "wrong"));
    $("div.popup_bg").show();
</script>
<%
    }
%>
</body>
</html>