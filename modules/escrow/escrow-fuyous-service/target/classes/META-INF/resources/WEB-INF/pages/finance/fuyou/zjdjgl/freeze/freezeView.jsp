<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.console.servlets.finance.fuyou.zjdjgl.freeze.UpdateFreezeView"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="com.dimeng.p2p.escrow.fuyou.entity.freeze.FYT6101"%>
<%@page import="com.dimeng.p2p.escrow.fuyou.entity.freeze.Freeze"%>
<%@page import="com.dimeng.framework.http.entity.RoleBean"%>
<%@page import="com.dimeng.util.ObjectHelper"%>
<%@page import="com.dimeng.p2p.common.enums.SysAccountStatus"%>
<%@page import="com.dimeng.p2p.console.servlets.finance.AbstractFinanceServlet" %>
<%@page import="com.dimeng.p2p.console.servlets.finance.fuyou.zjdjgl.freeze.FreezeView"%>
<%@page import="com.dimeng.p2p.console.servlets.finance.fuyou.zjdjgl.freeze.FreezeRecordView"%>
<html>
<head>
<%@include file="/WEB-INF/include/meta.jsp"%>
<%@include file="/WEB-INF/include/style.jsp"%>
<%@include file="/WEB-INF/include/script.jsp"%>
</head>
<body>
<%
	CURRENT_CATEGORY = "CWGL";
	CURRENT_SUB_CATEGORY = "ZJDJGL";	
	PagingResult<FYT6101> result = (PagingResult<FYT6101>) request.getAttribute("list");
	RoleBean[] roleBeans = ObjectHelper.convertArray(request.getAttribute("roles"), RoleBean.class);
%>
<div class="right-container">
   <div class="viewFramework-body">
     	<div class="viewFramework-content">
     		<form action="<%=controller.getURI(request, FreezeView.class)%>" method="post" name="form1" id="form1">
			<div class="p20">
         			<div class="border">
           			<div class="title-container"><i class="icon-i w30 h30 va-middle title-left-icon"></i>资金冻结管理</div>
           			<div class="content-container pl40 pt30 pr40">
             				<ul class="gray6 input-list-container clearfix">
               				<li>
               					<span class="display-ib mr5">登录账号：</span> 
               					<input type="text" name="name"	value="<%StringHelper.filterQuoter(out, request.getParameter("name"));%>" id="textfield7" class="text border pl5 mr20" />
							</li>
							<li>
								<!-- <input type="submit" class="btn btn-blue radius-6 mr5 pl1 pr15" value= "查找"/> -->
								<a href="javascript:search();" class="btn btn-blue radius-6 mr5 pl1 pr15"><i class="icon-i w30 h30 va-middle search-icon "></i>搜索</a>
			 				</li>
             				</ul>
					</div>
         			</div>
         			<div class="border mt20 table-container">
           			<table class="table table-style gray6 tl">
             				<thead>
               			<tr class="title tc">
							<th>序号</th>
							<th>登录账号</th>
							<th>姓名</th>
							<th>手机号</th>
							<th>余额(元)</th>
							<th>操作</th>
						</tr>
						</thead>
             				<tbody class="f12">
						<%
							FYT6101[] t6101s = result.getItems();
								if (t6101s != null) {
									int i = 1;
									for (FYT6101 t6101 : t6101s) {
										if (t6101 == null) {
											continue;
										}
						%>
						<tr class="dhsbg tc">
							<td><%=i++%></td>
							<td><%StringHelper.filterHTML(out, t6101.F05);%></td>
							<td><%StringHelper.filterHTML(out, t6101.F09);%></td>
							<td><%StringHelper.filterHTML(out, t6101.F08);%></td>
							<td><%StringHelper.filterHTML(out, t6101.F06.toString());%></td>							
							<td>
								<span>
									<%if (dimengSession.isAccessableResource(UpdateFreezeView.class)) {%>
										<a href="<%=controller.getURI(request, UpdateFreezeView.class)%>?name=<%=t6101.F05%>" class="mr10 blue">冻结</a>
					              	<%}else{ %>
					              		<span class="disabled">冻结</span>
					               	<%} %>
				                </span> 
				             	<span>	
					               	<%if (dimengSession.isAccessableResource(FreezeRecordView.class)) {%>
										<a href="<%=controller.getURI(request, FreezeRecordView.class)%>?name=<%=t6101.F05%>&userId=<%=t6101.F02%>" class="mr10 blue">查看冻结记录</a>
					              	<%}else{ %>
					              		<span class="disabled">查看冻结记录</span>
					               	<%} %>
					             </span> 
					         </td>
						<%
							}
							}else {
                            %>
                            <tr>
                                <td colspan="6" class="tc">暂无数据</td>
                            </tr>
                           <%} %>
						</tr>
				 		</tbody>
          		 		</table>
				</div>
				<%AbstractFinanceServlet.rendPagingResult(out, result);	%>
			</div>
		</form>
     	</div>
	</div>
</div>

<div id="info"></div>
<div class="popup_bg hide"></div>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/dialog.js"></script>
<%@include file="/WEB-INF/include/datepicker.jsp"%>

<%
    String warring = controller.getPrompt(request, response, PromptLevel.WARRING);
    if (!StringHelper.isEmpty(warring)) {
%>
	<script type="text/javascript">
	var str = '<%=warring%>';
	$("#info").html(showDialogInfo(str, "wrong"));
	</script>
<%
    }
%>	

<%
    String infoMsg = controller.getPrompt(request, response, PromptLevel.INFO);
	if (!StringHelper.isEmpty(infoMsg)) {
%>
	<script type="text/javascript">
    $("#info").html(showDialogInfo("<%=infoMsg%>", 'yes'));
    $("div.popup_bg").show();
	</script>
<%
    }
%>

<script type="text/javascript">
	function search(){
		document.getElementsByName("paging.current")[0].value=1;
		$("#form1").submit();
	}
</script>
</body>
</html>