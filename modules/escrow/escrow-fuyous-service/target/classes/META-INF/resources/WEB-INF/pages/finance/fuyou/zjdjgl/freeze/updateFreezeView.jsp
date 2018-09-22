<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.console.servlets.finance.fuyou.zjdjgl.freeze.FreezeView"%>
<%@page import="com.dimeng.p2p.console.servlets.finance.fuyou.zjdjgl.freeze.FreezeServlet"%>
<%@page import="com.dimeng.p2p.S61.entities.T6101"%>
<%@page import="com.dimeng.framework.http.entity.RoleBean"%>
<%@page import="com.dimeng.util.ObjectHelper"%>
<%@page import="com.dimeng.p2p.common.FormToken"%>

<html>
<head>
<%@include file="/WEB-INF/include/meta.jsp"%>
<%@include file="/WEB-INF/include/style.jsp"%>
<%@include file="/WEB-INF/include/script.jsp"%>
<%@include file="/WEB-INF/include/jquery.jsp" %>
<%@include file="/WEB-INF/include/datepicker.jsp"%>
</head>
<body>	
<%
	CURRENT_CATEGORY = "CWGL";
	CURRENT_SUB_CATEGORY = "UPDATEFREEZE";
	T6101 t6101 = ObjectHelper.convert(request.getAttribute("t6101"), T6101.class);
	RoleBean[] roleBeans = ObjectHelper.convertArray(request.getAttribute("roles"), RoleBean.class);
	 if (t6101 == null) {
		response.sendError(HttpServletResponse.SC_NOT_FOUND);				
		return;
	} 
%>
<div class="right-container">
    <div class="viewFramework-body">
      	<div class="viewFramework-content">
      		<form action="<%=controller.getURI(request, FreezeServlet.class)%>" method="post" class="form1" onsubmit="return validations(this)">
					<%=FormToken.hidden(serviceSession.getSession())%>
				<input type="hidden" value="<%StringHelper.filterQuoter(out, t6101.F05); %>" id="platformUserNo" name="platformUserNo">
				<input type="hidden" value="<%StringHelper.filterQuoter(out, t6101.F06.toString()); %>" id="money" name="money">
				<div class="p20">
					<div class="border">
		               	<div class="title-container"><i class="icon-i w30 h30 va-middle title-left-icon"></i>资金冻结</div>
		               	<div class="content-container pl40 pt40 pr40 pb20">
		               		<ul class="gray6">
		               			<li class="mb10">
		                			<span class="display-ib w200 tr mr5">登录账号：</span>
		                  			<%StringHelper.filterQuoter(out, t6101.F05);%>
		               			</li>
		               			<li class="mb10">
		                			<span class="display-ib w200 tr mr5">余额：</span>
		                  			<%StringHelper.filterQuoter(out, t6101.F06.toString());%>
		               			</li>
		               			<li class="mb10">
		               				<span class="display-ib w200 tr mr5"><em class="red pr5">*</em>冻结资金：</span>
									<input name="amount" id="amount" onkeyup="check();" type="text" class="text border pl5 mr20" maxlength="15"/>元
									<span id="errortip" class="error_tip"></span> 
		               			</li>
		               			<li class="mb10">
				                	<span class="display-ib w200 tr mr5"><em class="red pr5">*</em>解冻时间：</span>
									<input type="text" onchange="validations()" name="expired"  id="datepicker1" readonly="readonly" class="text border pl5 date" />
									<span id="datepickererrortip" class="error_tip"></span> 
		               			</li> 
		               			<li class="mb10">
		               				<div class="pl200 ml5">
			              				<%if (dimengSession.isAccessableResource(FreezeServlet.class)) {%>
											<input type="submit" class="btn btn-blue2 radius-6 pl20 pr20 sumbitForme" value="提交" />
										<%} else {%>
											<input type="button" class="disabled" value="确认" />
										<%}%>
			              				<input type="button" onclick="location.href='<%=controller.getURI(request, FreezeView.class) %>'" class="btn btn-blue radius-6 mr5 pr20 pl20 sumbitForme" value="返回" />
									</div>
								</li>                
		             			</ul>
		           		</div>
	         		</div>
	        	</div>
			</form>
		</div>
	</div>
</div>
<div id="info"></div>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/dialog.js"></script>
<%
    String message = controller.getPrompt(request, response, PromptLevel.INFO);
    if (!StringHelper.isEmpty(message)) {
%>
	<script type="text/javascript">
	var str = '<%=message%>';
	$("#info").html(showDialogInfo(str, "yes"));
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
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/jquery-ui-1.10.4/js/time.js"></script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/validation.js"></script>
<script type="text/javascript">
	$(function() {
		
		var d =new Date();
		var str = d.getFullYear()+"-"+(d.getMonth()+1)+"-"+d.getDate();
	   
	    $("#datepicker1").datetimepicker({
	    	showSecond: true, //显示秒
			timeFormat:'HH:mm:ss',
			dateFormat:'yy-mm-dd'
		});
	    $("#datepicker1").datepicker('option', 'minDate',str);
	    $("div.w440 div.tit span.close").click(function(){
			$("#dialog").hide();
		});
	});

	//验证余额不能超过限制
	function check(){
		$("#errortip").html("");
		var platformUserNo = $("#money").val();
		var amount = $("#amount").val();
		if(eval(platformUserNo)<eval(amount)){
			$("#errortip").html("超过余额限制" + platformUserNo + ",重新输入");
			$("#amount").focus();
			return ;
		}
	}

	//验证日期不能为空
	function validations(){
		var amount = $("#amount").val();
		if (amount == null || amount == "" || amount == 0) {
			$("#errortip").html("冻结金额不能为空或是0！");
			return false;
		}else{
			$("#errortip").html("");
		}
		
	    if(!/^\d+\.?\d{0,2}$/.test(amount)){ 
	    	$("#errortip").html("冻结金额必须为正数，且只能包含两位小数！");
			return false;
	    }
		
		var platformUserNo = $("#money").val();
		if(eval(platformUserNo) < eval(amount)){
			$("#errortip").html("超过余额限制" + platformUserNo + ",重新输入");
			$("#amount").focus();
			return false;
		}
		
		var datepicker = $("#datepicker1").val();	
		if(datepicker == null || datepicker == ""){
			$("#datepickererrortip").html("日期不能为空");
			return false;
		}else{
			//得到当前系统时间毫秒数 
			var dateNow = Date.parse(new Date());
		    //得到选中时间，转换毫秒数
		    var dateValue = datepicker.replace(new RegExp("-","gm"),"/"); 
		    dateValue = (new Date(dateValue)).getTime(); //得到毫秒数 
		    
			if (dateValue != null || datepicker != "") {    
				if (dateValue < dateNow) {
					$("#datepickererrortip").html("解冻时间不能小于当前时间！");
					return false;
				} else {
					$("#datepickererrortip").html("");
				}
			}
		}
	}

	function a(){
		window.location.href="<%=controller.getURI(request, FreezeView.class) %>";
	}
</script>
</body>
</html>