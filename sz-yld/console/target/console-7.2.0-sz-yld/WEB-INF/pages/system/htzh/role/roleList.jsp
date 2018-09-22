<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.console.servlets.system.htzh.role.RoleList"%>
<%@page import="com.dimeng.p2p.common.Constant"%>
<%@page import="com.dimeng.p2p.console.servlets.system.htzh.role.DelRole"%>
<%@page import="com.dimeng.p2p.console.servlets.system.htzh.role.UpdateRole"%>
<%@page import="com.dimeng.framework.http.entity.RoleBean"%>
<%@page import="com.dimeng.p2p.console.servlets.system.htzh.role.SetMenu"%>
<%@page import="com.dimeng.p2p.console.servlets.system.htzh.role.SetRight"%>
<%@page import="com.dimeng.p2p.console.servlets.system.htzh.role.AddRole"%>
<%@page import="com.dimeng.p2p.console.servlets.AbstractConsoleServlet"%>
<html>
<head>
<%@include file="/WEB-INF/include/meta.jsp"%>
<%@include file="/WEB-INF/include/style.jsp"%>
<%@include file="/WEB-INF/include/script.jsp"%>
</head>
<body>
	<%
	    PagingResult<RoleBean> result = (PagingResult<RoleBean>) request.getAttribute("result");
		//如果是从设置权限页面跳转过来的，则需要刷新下左菜单
		String  fromUrl = (String)request.getAttribute("fromUrl");
	    CURRENT_CATEGORY = "XTGL";
	    CURRENT_SUB_CATEGORY = "YHZGL";
	%>
	<div class="right-container">
		<div class="viewFramework-body">
			<div class="viewFramework-content">
				<!--加载内容-->
				<form action="<%=controller.getURI(request, RoleList.class)%>"
					method="post">
					<input type="hidden" name="roleId" id="roleId" value="">

					<div class="p20">
						<div class="border">
							<div class="title-container">
								<i class="icon-i w30 h30 va-middle title-left-icon"></i>用户组管理
							</div>
							<div class="content-container pl40 pt30 pr40">
								<ul class="gray6 input-list-container clearfix">
									<li>
										<%
										    if (dimengSession.isAccessableResource(AddRole.class)) {
										%> <a
										href="<%=controller.getURI(request, AddRole.class)%>"
										class="btn btn-blue radius-6 mr5 pl1 pr15"> <i
											class="icon-i w30 h30 va-middle add-icon "></i>新增
									</a> <%
									     } else {
									 %> <a href="javascript:void(0)"
										class="btn btn-gray radius-6 mr5 pl1 pr15"><i
											class="icon-i w30 h30 va-middle add-icon "></i>新增</a> <%
									     }
									 %>
									</li>
								</ul>
							</div>
						</div>
						<div class="border mt20 table-container">
							<table width="100%" border="0" cellspacing="0" cellpadding="3"
								class="table table-style gray6 tl">
								<thead>
									<tr class="title tc">
										<th class="tc">序号</th>
										<th>组名称</th>
										<th>组描述</th>
										<th class="w200">操作</th>
									</tr>
								</thead>
								<%
								    RoleBean[] roleBeans = result.getItems();
								                            if (roleBeans != null && roleBeans.length != 0) {
								                                int i = 1;
								                                for (RoleBean roleBean : roleBeans) {
								                                    if (roleBean == null) {
								                                        continue;
								                                    }
								%>
								<tbody class="f12">
									<tr class="title tc">
										<td class="tc"><%=i++%></td>
										<td
											title="<%StringHelper.filterHTML(out, roleBean.getName());%>">
											<%
											    StringHelper.filterHTML(out, StringHelper.truncation(roleBean.getName(), 15));
											%>
										</td>
										<td
											title="<%StringHelper.filterHTML(out, roleBean.getDescription());%>">
											<%
											    StringHelper.filterHTML(out, StringHelper.truncation(roleBean.getDescription(), 15));
											%>
										</td>
										<td>
											<%
											    if(roleBean.getRoleId()!=Constant.BUSINESS_ROLE_ID){
											%> <%
											     if (dimengSession.isAccessableResource(SetMenu.class)) {
											 %> <a
											href="<%=controller.getURI(request, SetMenu.class)%>?roleId=<%=roleBean.getRoleId()%>"
											class="link-blue mr20 click-link">分配权限</a> <%
											     }else{
											 %> <a
											class="disabled mr20">分配权限</a> <%
											     }
											 %> <%
											     if (dimengSession.isAccessableResource(UpdateRole.class)) {
											 %> <a
											href="<%=controller.getURI(request, UpdateRole.class)%>?roleId=<%=roleBean.getRoleId()%>"
											class="libk-deepblue mr20">修改</a> <%
											     }else{
											 %> <a
											class="disabled mr20">修改</a> <%
											     }
											 %> <%
											     if(roleBean.getRoleId()!=Constant.ADMIN_USER_ID){
 											if (dimengSession.isAccessableResource(DelRole.class)) {
											 %> <a
											onclick="del('<%=roleBean.getName()%>','<%=roleBean.getRoleId()%>')"
											class="link-orangered ">删除</a> <%
											     }else{
											 %> <span
											class="disabled">删除</span> <%
											     }}}
											 %>
										
									</tr>
								</tbody>
								<%
								    }
								                        } else {
								%>
								<tr class="dhsbg">
									<td colspan="11" class="tc">暂无数据</td>
								</tr>
								<%
								    }
								%>
							</table>
						</div>
						<%
						    AbstractConsoleServlet.rendPagingResult(out, result);
						%>
					</div>
				</form>
				<!--加载内容 结束-->
			</div>
		</div>
	</div>
	<!--右边内容 结束-->
	<!--内容-->

	<div id="info"></div>
	<div class="popup_bg hide"></div>
	<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/dialog.js"></script>
	<script type="text/javascript">
		
	    function del(name, roleId) {
	    	var url="<%=controller.getURI(request, DelRole.class)%>?roleId=" + roleId;
	        $(".popup_bg").show();
	        $("#info").html(showForwardInfo("删除后无法恢复，确定要删除“" + name + "”这个用户组？","question",url));
	    }
		<%if(!StringHelper.isEmpty(fromUrl)){%>
			$(function(){
				//修改权限后，刷新左菜单
				var $obj = parent.document.getElementById("leftFrame");
				$obj.src=$obj.src+"?t=<%=System.currentTimeMillis()%>";
			});
		<%}%>

	</script>

	<%
	    String message = controller.getPrompt(request, response, PromptLevel.INFO);
	    if (!StringHelper.isEmpty(message)) {
	%>
	<script type="text/javascript">
	$(".popup_bg").show();
	$("#info").html(showDialogInfo("<%=message%>","yes"));
	</script>
	<%
	    }
	%>

	<%
	    String errorMessage = controller.getPrompt(request, response, PromptLevel.ERROR);
	    if (!StringHelper.isEmpty(errorMessage)) {
	%>
	<script type="text/javascript">
		$(".popup_bg").show();
		$("#info").html(showDialogInfo("<%=errorMessage%>","wrong"));
	</script>
	<%
	    }
	%>

	<%
	    String warnMessage = controller.getPrompt(request, response, PromptLevel.WARRING);
	    if (!StringHelper.isEmpty(warnMessage)) {
	%>
	<script type="text/javascript">
		$(".popup_bg").show();
		$("#info").html(showDialogInfo("<%=warnMessage%>","wrong"));
	</script>
	<%
	    }
	%>

</body>
</html>