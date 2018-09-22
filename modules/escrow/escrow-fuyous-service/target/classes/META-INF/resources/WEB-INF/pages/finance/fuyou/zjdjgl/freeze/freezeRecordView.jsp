<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.console.servlets.finance.fuyou.zjdjgl.freeze.UnFreezeServlet"%>
<%@page import="com.dimeng.p2p.escrow.fuyou.entity.freeze.T6170"%>
<%@page import="com.dimeng.p2p.escrow.fuyou.entity.enums.T6170_F06"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="com.dimeng.framework.http.entity.RoleBean"%>
<%@page import="com.dimeng.util.ObjectHelper"%>
<%@page import="com.dimeng.p2p.common.enums.SysAccountStatus"%>
<%@page import="com.dimeng.p2p.console.servlets.finance.AbstractFinanceServlet" %>
<%@page import="com.dimeng.p2p.console.servlets.finance.fuyou.zjdjgl.freeze.FreezeRecordView"%>
<%@page import="com.dimeng.p2p.console.servlets.finance.fuyou.zjdjgl.freeze.FreezeView"%>
<%@page import="com.dimeng.p2p.common.FormToken"%>
<html>
<head>
<%@include file="/WEB-INF/include/meta.jsp"%>
<%@include file="/WEB-INF/include/style.jsp"%>
<%@include file="/WEB-INF/include/script.jsp"%>
</head>
<body>
<%
	CURRENT_CATEGORY = "CWGL";
	CURRENT_SUB_CATEGORY = "UNFREEZE";
	PagingResult<T6170> result = (PagingResult<T6170>) request.getAttribute("list");
	String name = (String)request.getAttribute("name");
	RoleBean[] roleBeans = ObjectHelper.convertArray(request.getAttribute("roles"), RoleBean.class);
%>
	<div class="right-container">
    <div class="viewFramework-body">
      	<div class="viewFramework-content">
      		<form action="<%=controller.getURI(request, FreezeRecordView.class)%>" method="post" name="form1" id="form1">
      			<%=FormToken.hidden(serviceSession.getSession())%>
      			<input type="hidden" name="token" value=""/>
				<div class="p20">
          			<div class="border">
            			<div class="title-container"><i class="icon-i w30 h30 va-middle title-left-icon"></i>冻结记录列表
            				<div class="fr mt5 mr10">
									<input type="button" class="btn btn-blue2 radius-6 pl20 pr20"
										onclick="location.href='<%=controller.getURI(request, FreezeView.class)%>'"
										value="返回">
							</div>
            			</div>
            			<input type="hidden" name="name" value="<%=name %>" id="name" />
            			<input type="hidden" name="userId" value="${param.userId }" id="userId" />
            			<div class="content-container pl40 pt30 pr40">
              				<ul class="gray6 input-list-container clearfix">
								<li><span class="display-ib mr5">冻结流水号：</span> 
								<input type="text" name="serialNumber" value="${param.serialNumber }"
									id="serialNumber" class="text border pl5 mr20" /></li>
								<li>
                                    <span class="display-ib mr5">状态：</span>
                                    <select name="status" class="border mr20 h32 mw100">
                                        <option value="" <% if ("".equals(request.getAttribute("status"))) {%>
                                                selected="selected"<%} %>>全部
                                        </option>
                                        <option value="DJZ" <%if ("DJZ".equals(request.getAttribute("status"))) {%>
                                                selected="selected"<%} %>>冻结中
                                        </option>
                                        <option value="YJD" <%if ("YJD".equals(request.getAttribute("status"))) {%>
                                                selected="selected"<%} %>>已解冻
                                        </option>
                                    </select>
                                </li>
								<li>
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
							<th>冻结流水号</th>
							<th>用户名</th>
							<th>冻结金额</th>
							<th>解冻时间</th>
							<th>状态</th>
							<th>操作</th>
						</tr>
						</thead>
              			<tbody class="f12">
						<%
						
							T6170[] t6170s = result.getItems();
								if (t6170s != null) {
									int i = 1;
									for (T6170 t6170 : t6170s) {
										if (t6170 == null) {
											continue;
										}
						%>
						<tr class="dhsbg tc">
							<td><%=i++%></td>							
							<td><%StringHelper.filterHTML(out, t6170.F02);%></td>
							<td><%StringHelper.filterHTML(out, t6170.F03);%></td>
							<td><%StringHelper.filterHTML(out, t6170.F04.toString());%></td>
							<td><%=DateTimeParser.format(t6170.F05)%></td>
							<td>
							<%
								if(t6170.F06 == T6170_F06.DJZ){
								    out.print("冻结中");
								} else {
								    out.print("已解冻");
								}
							%></td>
							<td>
								<span>
								<%if (dimengSession.isAccessableResource(UnFreezeServlet.class)) {	
									if(t6170.F06 == T6170_F06.DJZ){
								%>
									<a href="javascript:void(0);" onclick="js_method('<%=controller.getURI(request, UnFreezeServlet.class)%>?serialNo=<%=t6170.F02%>&name=<%=name %>');"
												class="mr10 blue">解冻</a> 
								<%	
									}
				            	}else{ 
				            	    if(t6170.F06 == T6170_F06.DJZ){
					            	%>
					              	<span class="disabled">解冻</span>
					            <%
				            	    }
			            	    }
			            	    %>
					            </span>
					        </td>
					    </tr>
						<%
							}
							}else {
                            %>
                             <tr>
                                 <td colspan="7" class="tc">暂无数据</td>
                             </tr>
                            <%} %>
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
<script type="text/javascript">
 
	$(function() {
		$("div.w440 div.tit span.close").click(function(){
			$("#dialog").hide();
		});
		  var tokenValue = $("input[name='<%=FormToken.parameterName()%>']").val();
		    $("input[name='token']").val(tokenValue);
	});
	
	function seachBack()
	{
		window.location.href="<%=controller.getURI(request, FreezeRecordView.class) %>"
	};
	
    function js_method(url) 
    {
    	var token = $("input[name=token]").val();
    	_url=url+"&token="+token;
    	$(".popup_bg").show();
        $("#info").html(showConfirmDiv("确定解冻该笔资金吗？", _url, "jd"));
    }
    
    function toConfirm(_url,type)
    {
    	if(type=="jd")
    	{
    		window.location.href = _url;
    	}
    }

	function search()
	{
		document.getElementsByName("paging.current")[0].value=1;
		$("#form1").submit();
	};
</script>
</body>
</html>