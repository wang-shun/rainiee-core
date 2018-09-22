<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.console.servlets.system.htzh.sys.SysUserList"%>
<%@page import="com.dimeng.p2p.repeater.business.entity.BusinessOptLog"%>
<%@page import="com.dimeng.p2p.common.Constant"%>
<%@page import="com.dimeng.framework.http.entity.RoleBean"%>
<%@page import="com.dimeng.p2p.common.enums.SysAccountStatus"%>
<%@page import="com.dimeng.p2p.console.servlets.system.htzh.sys.UpdateSysUser"%>
<%@page import="com.dimeng.p2p.modules.systematic.console.service.entity.SysUser"%>
<%@page import="com.dimeng.util.ObjectHelper"%>
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
		CURRENT_CATEGORY = "XTGL";
		CURRENT_SUB_CATEGORY = "GLYGL";
		SysUser sysUser = ObjectHelper.convert(request.getAttribute("sysUser"), SysUser.class);
		RoleBean[] roleBeans = ObjectHelper.convertArray(request.getAttribute("roles"), RoleBean.class);
		BusinessOptLog[] logs = ObjectHelper.convertArray(request.getAttribute("logs"), BusinessOptLog.class);
		if (sysUser == null) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
	%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
					<!--加载内容-->
					<div class="p20">
						<div class="border">
							<div class="title-container"><i class="icon-i w30 h30 va-middle title-left-icon"></i>修改管理员信息</div>
							<div class="content-container pl40 pt40 pr40 pb20">
								<form action="<%=controller.getURI(request, UpdateSysUser.class)%>" method="post" class="form1">
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
												<span class="red">*</span>姓名：
											</span>
                                        	<input name="name" maxlength="15" type="text" maxlength="15"
                                               	class="text border w300 yw_w5 required"
                                               	value="<%StringHelper.filterQuoter(out, sysUser.name);%>"/>
                                        	<span tip></span>
                                        	<span errortip class="" style="display: none"></span>
                                    	</li>
                                    	<li class="mb10">
											<span class="display-ib w200 tr mr5">
												<span class="red">*</span>手机号码：
											</span>
											<input name="phone" type="text" maxlength="11" value="<%StringHelper.filterQuoter(out, sysUser.phone);%>" class="text border w300 yw_w5 required mobile"/>
											<span tip></span>
											<span errortip class="" style="display: none"></span>
										</li>
										<li class="mb10">
											<span class="display-ib w200 tr mr5">
												职称：
											</span>
											<input name="pos" type="text" maxlength="30" value="<%StringHelper.filterQuoter(out, sysUser.pos);%>" class="text border w300 yw_w5"/>
											<span tip></span>
											<span errortip class="" style="display: none"></span>
										</li>
										<li class="mb10">
											<span class="display-ib w200 tr mr5">
												所属部门：
											</span>
											<input name="dept" type="text" maxlength="50" value="<%StringHelper.filterQuoter(out, sysUser.dept);%>" class="text border w300 yw_w5"/>
										</li>
										<%if(sysUser.roleId != Constant.BUSINESS_ROLE_ID){ %>
										<li class="mb10">
											<span class="display-ib w200 tr mr5">
												<span class="red">*</span>用户组：
											</span>
												<select name="roleId" class="border">
													<%
														if(roleBeans!=null)
														{
														for (RoleBean roleBean : roleBeans) {
																if (roleBean == null || roleBean.getRoleId()== Constant.BUSINESS_ROLE_ID) {
																	continue;
																}
													%>
													<option value="<%=roleBean.getRoleId()%>" <%if (roleBean.getRoleId() == sysUser.roleId) {%> selected="selected" <%}%>>
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
										<%} %>
										<li class="mb10">

											<span class="display-ib w200 tr mr5">
												<span class="red">*</span>用户状态：
											</span>
												<select name="status" class="border">
													<option value="QY" <%if (SysAccountStatus.QY.equals(sysUser.status)) {%> selected="selected" <%}%>>启用</option>
													<option value="TY" <%if (SysAccountStatus.TY.equals(sysUser.status)) {%> selected="selected" <%}%>>锁定</option>
												</select>
										</li>
										<li>
											<div class="pl200 ml5">
												<%
													if (dimengSession.isAccessableResource(UpdateSysUser.class)) {
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
						<%if(Constant.BUSINESS_ROLE_ID == sysUser.roleId){ %>
						<div class="tab-content-container p20">
							<div class="title-container"><i class="icon-i w30 h30 va-middle title-left-icon"></i>业务员锁定日志</div>
						    <table class="table table-style gray6 tl">
						      <thead>
						        <tr class="title tc">
						          <th>序号</th>
						          <th>开始时间</th>
						          <th>结束时间</th>
						        </tr>
						      </thead>
						      <tbody class="f12">
						        <%if(logs!=null && logs.length>0){
						              int i=1;
									  for(BusinessOptLog log:logs){	
									      if(log==null){
									          continue;
							    }%>
			                    <tr class="tc">
			                      <td><%=i++%></td>
								  <td><%=DateTimeParser.format(log.lockTime)%></td>
								  <td><%=DateTimeParser.format(log.unLockTime)%></td>
			                    </tr>
			                    <%}}else{%>
			                    <tr class="tc"><td colspan="3">暂无数据</td></tr>
							    <%} %>
						      </tbody>
						    </table>
				  		</div>
			  			<%} %>
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
	<%String warnMessage = controller.getPrompt(request, response , PromptLevel.WARRING);
	  if (!StringHelper.isEmpty(warnMessage)) {%>
	<script type="text/javascript">
		$("#info").html(showDialogInfo("<%=warnMessage%>","perfect"));
		$("div.popup_bg").show();
	</script>
	<%}%>
</body>
</html>