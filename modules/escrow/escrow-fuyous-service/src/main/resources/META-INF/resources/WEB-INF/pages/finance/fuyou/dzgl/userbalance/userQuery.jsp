<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.S61.enums.T6110_F10"%>
<%@page
	import="com.dimeng.p2p.console.servlets.finance.fuyou.dzgl.userbalance.UserList"%>
<%@page
	import="com.dimeng.p2p.escrow.fuyou.entity.UserQueryResponseEntity"%>
<%@page import="com.dimeng.p2p.S61.entities.T6110"%>
<%@page import="com.dimeng.p2p.S61.enums.T6110_F06"%>

<html>
<head>
<%@include file="/WEB-INF/include/meta.jsp"%>
<%@include file="/WEB-INF/include/style.jsp"%>
<%@include file="/WEB-INF/include/script.jsp"%>
</head>
<body>

	<%
	    CURRENT_CATEGORY = "CWGL";
	    CURRENT_SUB_CATEGORY = "DSFXXCX";
	    String userType = (String)request.getAttribute("userType");
	    String thirdTag = (String)request.getAttribute("thirdTag");
	    String userTag = (String)request.getAttribute("userTag");
	    UserQueryResponseEntity entity = (UserQueryResponseEntity)request.getAttribute("userQueryEntity");
	%>
	<div class="right-container">
		<div class="viewFramework-body">
			<div class="viewFramework-content">
				<form action="<%=controller.getURI(request, UserList.class)%>"
					method="post" name="form1" id="form1">
					<div class="p20">
						<div class="border">
							<div class="title-container">
								<i class="icon-i w30 h30 va-middle title-left-icon"></i>用户第三方信息查询
								<div class="fr mt5 mr10">
									<input type="button" class="btn btn-blue2 radius-6 pl20 pr20"
										onclick="location.href='<%=controller.getURI(request, UserList.class)%>'"
										value="返回">
								</div>
							</div>
						</div>
						<div class="border mt20 table-container">
							<table class="table table-style gray6 tl">
								<thead>
									<tr class="title">
										<th class="tc">账户类型</th>
										<th class="tc">资金账号</th>
										<th class="tc">账户名称</th>
										<th class="tc">手机号码</th>
										<th class="tc">邮箱</th>
										<th class="tc">账户状态</th>
									</tr>
								</thead>
								<tbody class="f12">
									<tr class="dhsbg">
										<td class="tc">
											<%
	                                            if (T6110_F06.parse(userType) == T6110_F06.ZRR) {
	                                                out.print("个人");
	                                            } else {
	                                                if(T6110_F10.parse(userTag) == T6110_F10.S){
	                                                	out.print("机构");
	                                                }else{
	                                                    out.print("企业");
	                                                }
	                                            }
											%>
										</td>
										<td class="tc"><%=thirdTag %></td>
										<td class="tc"><%=entity.getCust_nm() %></td>
										<td class="tc"><%=entity.getMobile_no() %></td>
										<td class="tc"><%=entity.getEmail() %></td>
										<td class="tc">
											<%
												if("1".equals(entity.getUser_st()))
												{
												    out.print("正常");
												} 
												else if("2".equals(entity.getUser_st()))
												{
												    out.print("已注销");
												} 
												else 
												{
												    out.print("申请注销");
												}
											%>
										</td>
									</tr>
								</tbody>
							</table>
						</div>
						<div class="clear"></div>
						<div class="box2 clearfix"></div>
					</div>
				</form>
			</div>
		</div>
	</div>
	<%
	    String msg = controller.getPrompt(request, response, PromptLevel.INFO);
	    if (!StringHelper.isEmpty(msg))
	    {
	%>
	<div class="w440 thickbox thickpos" style="margin: -80px 0 0 -220px;"
		id="showDiv">
		<div class="info clearfix">
			<div class="clearfix">
				<span class="fl tips">
					<%
					    StringHelper.filterHTML(out, msg);
					%>
				</span>
			</div>
			<div class="dialog_btn">
				<input type="button" name="button2" onclick="$('#showDiv').hide()"
					value="确认" class="btn4 ml50" />
			</div>
		</div>
	</div>
	<%} %>
</body>
</html>