<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="com.dimeng.p2p.console.servlets.system.htzh.role.RoleList"%>
<%@page import="com.dimeng.framework.http.entity.RightBean"%>
<%@page import="com.dimeng.framework.http.entity.RoleBean"%>
<%@page import="com.dimeng.framework.config.entity.ModuleBean"%>
<%@page import="com.dimeng.p2p.console.servlets.system.htzh.role.SetMenu"%>
<%@page import="com.dimeng.util.ObjectHelper"%>
<html>
<head>
<%@include file="/WEB-INF/include/meta.jsp"%>
<%@include file="/WEB-INF/include/style.jsp"%>
<%@include file="/WEB-INF/include/script.jsp"%>
</head>
<body>
	<%
	    ModuleBean[] moduleBeans = ObjectHelper.convertArray(request.getAttribute("moduleBeans"), ModuleBean.class);
		RightBean[] rightBeans = ObjectHelper.convertArray(request.getAttribute("rightBeans"), RightBean.class);
	    Map<String,RightBean> rights = new HashMap<String,RightBean>();
	    for(RightBean rightBean : rightBeans)
	    {
	        if(rightBean!=null)
	        {
	            rights.put(rightBean.getId(), rightBean);
	        }
	    }
	    int roleId = IntegerParser.parse(request.getAttribute("roleId"));
	    RoleBean roleBean = ObjectHelper.convert(request.getAttribute("roleBean"), RoleBean.class);
	    if (roleBean == null) {
	        response.sendError(HttpServletResponse.SC_NOT_FOUND);
	        return;
	    }
	%>
	<div class="right-container">
		<div class="viewFramework-body">
			<div class="viewFramework-content">
				<div class="p20">
					<form action="<%=controller.getURI(request, SetMenu.class)%>"  method="post" id="form1">
						<input type="hidden" value="<%=roleId%>" name="roleId"/>
						<table class="table table-border gray6">
							<tbody>
								<tr>
									<td class="left-td-title tc">
										<label for="all" class="mr30">
											<input name="oneBox" type="checkbox" id="all"> 所有权限
										</label>
									</td>
									<td>
										<div class="mt10 pr item-container">
											<span class="display-ib w120 tr mr5 pa left0 top0 h30 lh30 fb">
												<em class="red pr5">*</em>用户组名称：
											</span>
											<div class="pl120 ml5">
												<input class="text border w300 pl5" type="text" value="<%StringHelper.filterHTML(out, roleBean.getName()); %>" readonly="readonly">
											</div>
										</div>
										<div class="mt10 pr item-container">
											<span class="display-ib w120 tr mr5 pa left0 top0 h30 lh30 fb">用户组描述：</span>
											<div class="pl120 ml5">
												<textarea class="w300 p5 h50 border" readonly="readonly"><%StringHelper.filterHTML(out, roleBean.getDescription()); %></textarea>
											</div>
										</div>
										<div class="mt10 pr item-container">
											<span class="display-ib w120 tr mr5 pa left0 top0 h30 lh30 fb">权限管理：</span>
											<div class="pl120 ml5 lh30">勾选后将获得相应的操作权限</div>
										</div>
									</td>
								</tr>
								<%
								int index=0;
								if (moduleBeans.length>0) {
								    for (ModuleBean oneModuleBean : moduleBeans) {
                                        if (oneModuleBean == null || !oneModuleBean.isDisplay()) {
                                            continue;
                                        }
                                %>
                                <tr>
									<td class="left-td-title">
										<label for="module_<%=index %>"> 
											<input name="oneBox" class="" type="checkbox" id="module_<%=index++ %>" value="<%StringHelper.filterHTML(out, oneModuleBean.getId());%>" /> 
											<%StringHelper.filterHTML(out, oneModuleBean.getName());%>
										</label>
										<a href="javascript:void(0);" class="zoom-in-btn icon-i">-</a>
									</td>
									<td>
										<div class="td-item-container">
											<%
												if(oneModuleBean.getModuleBeans().length>0){
												    for(ModuleBean twoModuleBean : oneModuleBean.getModuleBeans()){
												        if (twoModuleBean == null || !twoModuleBean.isDisplay()) {
				                                            continue;
				                                        }
											%>
											<div class="td-title-container border-b-s h30 lh30">
												<label for="module_<%=index %>" class="main-color fb"> 
													<input name="twoBox" class="mr5" type="checkbox" id="module_<%=index++ %>" value="<%=twoModuleBean.getId()%>">
													<%StringHelper.filterHTML(out, twoModuleBean.getName());%>
												</label>
											</div>
											<%
												if(twoModuleBean.getModuleBeans().length>0){
												    for(ModuleBean threeModuleBean : twoModuleBean.getModuleBeans()){
												        if (threeModuleBean == null || !threeModuleBean.isDisplay()||("baofu".equals(configureProvider.getProperty(SystemVariable.ESCROW_PREFIX)) && "P2P_C_FINANCE_ZJGL_PTTZGL".equals(threeModuleBean.getId()))) {
				                                            continue;
				                                        }
											%>
											<div class="mt10 pr item-container" module="<%=twoModuleBean.getId()%>">
												<label class="display-ib w200 tl mr5 pa left0 top0 h30 lh30 fb" for="menu_<%=index %>">
													<input name="threeBox" class="mr5" type="checkbox" value="<%=threeModuleBean.getId()%>" id="menu_<%=index++ %>"/>
													<%StringHelper.filterHTML(out, threeModuleBean.getName());%>：
												</label>
												<div class="pl200 ml5">
													<ul class="gray6 flat-line-ul clearfix">
														<%
															if(threeModuleBean.getRightBeans().length>0){
															    for(RightBean rightBean : threeModuleBean.getRightBeans()){
															        if(rightBean ==null || !rightBean.isDisplay()){
															            continue;
															        }
														%>
															<li class="w250">
																<label class="cursor-p display-ib mr50 pt5 pb5" for="right_<%=index %>"> 
																	<input name="rightIds" class="mr10 rightId" type="checkbox"  value="<%=rightBean.getId() %>" id="right_<%=index++ %>"
																	<%if (rights.size()>0 && rights.containsKey(rightBean.getId())) { %> checked="checked" <%} %>/>
																	<%StringHelper.filterHTML(out, rightBean.getName());%>
																</label>
															</li>
														<%
															    }
															}
														%>
													</ul>
												</div>
											</div>
											<%
												    }
												}else if(twoModuleBean.getRightBeans().length>0){
											%>
											<div class="mt10 pr item-container" module="<%=twoModuleBean.getId()%>">
												<label class="display-ib w200 tl mr5 pa left0 top0 h30 lh30 fb" for="menu_<%=index %>">
													<input name="threeBox" class="mr5" type="checkbox" value="<%=twoModuleBean.getId()%>" id="menu_<%=index++ %>">
													<%StringHelper.filterHTML(out, twoModuleBean.getName());%>：
												</label>
												<div class="pl200 ml5">
													<ul class="gray6 flat-line-ul clearfix">
														<%
															for(RightBean rightBean : twoModuleBean.getRightBeans()){
														        if(rightBean ==null || !rightBean.isDisplay()){
														            continue;
														        }
														%>
															<li class="w250">
																<label class="cursor-p display-ib mr50 pt5 pb5" for="right_<%=index %>"> 
																	<input name="rightIds" class="mr10 rightId" type="checkbox" id="right_<%=index++ %>" value="<%=rightBean.getId() %>" 
																	<%if (rights.size()>0 && rights.containsKey(rightBean.getId())) { %> checked="checked" <%} %> />
																	<%StringHelper.filterHTML(out, rightBean.getName());%>
																</label>
															</li>
														<%
															}
														%>
													</ul>
												</div>
											</div>	
											<% 
												}
											%> 
											<%
												    }
												}
											%>
										</div>
									</td>
								</tr>	
                                <%
								    }
								}
								%>
							</tbody>
						</table>
						<div class="tc mt20 f16">
							<%
                             if (dimengSession.isAccessableResource(SetMenu.class)) {
                            %>
                            <input type="button" class="btn btn-blue2 radius-6 pl20 pr20" onclick="check()" value="确认"/>
                            <%} else { %>
                            <input type="button" class="disabled" value="确认"/>
                            <%} %>
                            <input type="button" onclick="location.href='<%=controller.getURI(request, RoleList.class) %>'"
                                   class="btn btn-blue2 radius-6 pl20 pr20 ml40" value="取消"/>
						</div>
					</form>
				</div>
				<!--加载内容 结束-->
			</div>
		</div>
	</div>
	<!--右边内容 结束-->
	<!--内容-->
	<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/system/setMenu.js"></script>
</body>
</html>