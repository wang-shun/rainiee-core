<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.console.servlets.base.optsettings.filterSettings.FilterSettings"%>
<%@page import="com.dimeng.p2p.console.servlets.base.optsettings.filterSettings.DelFilterSettings"%>
<%@page import="com.dimeng.p2p.S62.entities.T6299"%>
<%@page import="com.dimeng.p2p.S62.enums.T6290_F06"%>
<%@page import="com.dimeng.p2p.S62.enums.T6290_F02"%>
<%@page import="com.dimeng.p2p.S62.entities.T6290"%>
<%@page
	import="com.dimeng.p2p.console.servlets.base.optsettings.repayment.RepaymentSet"%>
<%@page import="com.dimeng.util.ObjectHelper"%>
<html>
<head>
<%@include file="/WEB-INF/include/meta.jsp"%>
<%@include file="/WEB-INF/include/style.jsp"%>
<%@include file="/WEB-INF/include/script.jsp"%>
</head>

<body>
	<%
	    CURRENT_CATEGORY = "JCXXGL";
	    CURRENT_SUB_CATEGORY = "SXTJSZ";
	    T6299 NHSYFirst = ObjectHelper.convert(request.getAttribute("NHSYFirst"), T6299.class);
	    T6299 NHSYLast = ObjectHelper.convert(request.getAttribute("NHSYLast"), T6299.class);
	    T6299[] NHSYFilters = ObjectHelper.convertArray(request.getAttribute("NHSYFilters"), T6299.class);
	    T6299 RZJDFirst = ObjectHelper.convert(request.getAttribute("RZJDFirst"), T6299.class);
	    T6299 RZJDLast = ObjectHelper.convert(request.getAttribute("RZJDLast"), T6299.class);
	    T6299[] RZJDFilters = ObjectHelper.convertArray(request.getAttribute("RZJDFilters"), T6299.class);
	    T6299 JKQXFirst = ObjectHelper.convert(request.getAttribute("JKQXFirst"), T6299.class);
	    T6299 JKQXLast = ObjectHelper.convert(request.getAttribute("JKQXLast"), T6299.class);
	    T6299[] JKQXFilters = ObjectHelper.convertArray(request.getAttribute("JKQXFilters"), T6299.class);
	%>
	<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
					<!--加载内容-->
					<form action="<%=controller.getURI(request, FilterSettings.class)%>"
                      method="post" id="form1" class="form1" onsubmit="return checkCondition();">
					<!--筛选条件设置-->
					<div class="p20">
						<div class="border">
							<div class="title-container">
								<i class="icon-i w30 h30 va-middle title-left-icon"></i>筛选条件设置
							</div>
							<div class="content-container p40">

								<!--年化利率-->
								<div class="item-container pr mb40">
									<div class="pa w100 left0 top0 bottom0 tc"
										style="background: #84aaf1;">
										<i class="icon-i bule-arrow-icon"></i><span
											class="va-middle-text">年化利率</span>
									</div>
									<div class="ml100 border mh100">
										<ul class="gray6 input-list-container clearfix ml30 mt30 prompt-warp" id="nhsyUl">
											<li>
												<div class="mr30">
													<input type="hidden" name="NHSYids" value="<%=NHSYFirst.F01 %>" />
													<input type="hidden" name="NHSYF02" class="border w40 pl5 pr5 h34 lh34"
														value="<%=NHSYFirst.F02 %>" />
													<span class="pr">
														<input type="text" name="NHSYF03" id="minNHSY"
														class="border w40 pl5 pr5 h34 lh34 required minf-size-1 maxf-size-24 isint" value="<%=NHSYFirst.F03%>" /> %以下
													</span>
												</div>
											</li>
											<%
												if(NHSYFilters != null){
												    for(T6299 NHSYFilter : NHSYFilters){
												        if(NHSYFilter == null){
												            continue;
												        }
											%>
											<li>
												<div class="mr30">
													<input type="hidden" name="NHSYids" value="<%=NHSYFilter.F01 %>" />
													<span class="pr">
														<input type="text" name="NHSYF02" class="border w40 pl5 pr5 h34 lh34 required minf-size-1 maxf-size-24 isint" 
														value="<%=NHSYFilter.F02 %>" /> %</span>一 <span class="pr">
														<input type="text" name="NHSYF03"
														class="border w40 pl5 pr5 h34 lh34 required minf-size-1 maxf-size-24 isint" value="<%=NHSYFilter.F03 %>" /> %</span>
														<a id="delBtnYearRate" class="icon-i w30 h30 va-middle remove-radiusbtn-icon"></a>
												</div>
											</li>
											<% }}%>
											<li id="reYearRate">
												<div class="mr30">
													<input type="hidden" name="NHSYids" value="<%=NHSYLast.F01 %>" />
													<span class="pr"><input type="text" name="NHSYF02" id="maxNHSY" class="border w40 pl5 pr5 h34 lh34 required minf-size-1 maxf-size-24 isint"
														value="<%=NHSYLast.F02%>" /> %以上</span>
													<input type="hidden" name="NHSYF03" class="border w40 pl5 pr5 h34 lh34"
														value="<%=NHSYLast.F03 %>" />
													<a id="AddYearRate" class="icon-i w30 h30 va-middle add-radiusbtn-icon"></a>
												</div>
											</li>
										</ul>
									</div>
								</div>
								<!--年化利率 结束-->

								<!--融资进度-->
								<div class="item-container pr mb40">
									<div class="pa w100 left0 top0 bottom0 tc"
										style="background: #fd5f56;">
										<i class="icon-i red-arrow-icon"></i><span
											class="va-middle-text">融资进度</span>
									</div>
									<div class="ml100 border mh100">
										<ul class="gray6 input-list-container clearfix ml30 mt30" id="rzjdUl">
											<li>
												<div class="mr30">
													<input type="hidden" name="RZJDids" value="<%=RZJDFirst.F01 %>" />
													<input type="hidden" name="RZJDF02" class="border w40 pl5 pr5 h34 lh34"
														value="<%=RZJDFirst.F02 %>" />
													<span class="pr"><input type="text" name="RZJDF03" id="minRZJD" class="border w40 pl5 pr5 h34 lh34 required minf-size-1 maxf-size-100 isint"
														value="<%=RZJDFirst.F03 %>" /> %以下</span>
												</div>
											</li>
											<%
												if(RZJDFilters != null){
												    for(T6299 RZJDFilter : RZJDFilters){
												        if(RZJDFilter == null){
												            continue;
												        }
											%>
											<li>
												<div class="mr30">
													<input type="hidden" name="RZJDids" value="<%=RZJDFilter.F01 %>" />
													<span class="pr"><input type="text" name="RZJDF02" class="border w40 pl5 pr5 h34 lh34 required minf-size-1 maxf-size-100 isint"
														value="<%=RZJDFilter.F02 %>" /> %</span>一 <span class="pr"><input type="text" name="RZJDF03"
														class="border w40 pl5 pr5 h34 lh34 required minf-size-1 maxf-size-100 isint" value="<%=RZJDFilter.F03 %>" /> %</span>
														<a id="delBtnProgress" class="icon-i w30 h30 va-middle remove-radiusbtn-icon"></a>
												</div>
											</li>
											<% }}%>
											<li id="reProgress">
												<div class="mr30">
													<input type="hidden" name="RZJDids" value="<%=RZJDLast.F01 %>" />
													<span class="pr"><input type="text" name="RZJDF02" id="maxRZJD" class="border w40 pl5 pr5 h34 lh34 required minf-size-1 maxf-size-100 isint"
														value="<%=RZJDLast.F02 %>" /> %以上</span>
														<input type="hidden" name="RZJDF03" class="border w40 pl5 pr5 h34 lh34 required minf-size-1 maxf-size-100 isint"
														value="<%=RZJDLast.F03 %>" />
														<a id="AddProgress" class="icon-i w30 h30 va-middle add-radiusbtn-icon"></a>
												</div>
											</li>
										</ul>
									</div>
								</div>
								<!--融资进度 结束-->

								<!--借款期限-->
								<div class="item-container pr mb40">
									<div class="pa w100 left0 top0 bottom0 tc"
										style="background: #4cb888;">
										<i class="icon-i green-arrow-icon"></i><span
											class="va-middle-text">借款期限</span>
									</div>
									<div class="ml100 border mh100">
										<ul class="gray6 input-list-container clearfix ml30 mt30" id="jkqxUl">
											<li>
												<div class="mr30">
													<input type="hidden" name="JKQXids" value="<%=JKQXFirst.F01 %>" />
													<input type="hidden" name="JKQXF02" class="border w40 pl5 pr5 h34 lh34"
														value="<%=JKQXFirst.F02 %>" />
													<span class="pr"><input type="text" name="JKQXF03" id="minJKQX" class="border w40 pl5 pr5 h34 lh34 required minf-size-1 maxf-size-100 isint"
														value="<%=JKQXFirst.F03 %>" /> 个月以下</span>
												</div>
											</li>
											<%
												if(JKQXFilters != null){
												    for(T6299 JKQXFilter : JKQXFilters){
												        if(JKQXFilter == null){
												            continue;
												        }
											%>
											<li>
												<div class="mr30">
													<input type="hidden" name="JKQXids" value="<%=JKQXFilter.F01 %>" />
													<span class="pr"><input type="text" name="JKQXF02" class="border w40 pl5 pr5 h34 lh34 required minf-size-1 maxf-size-100 isint"
														value="<%=JKQXFilter.F02 %>" /> </span>一 <span class="pr"><input type="text" name="JKQXF03"
														class="border w40 pl5 pr5 h34 lh34 minf-size-1 required maxf-size-100 isint" value="<%=JKQXFilter.F03 %>" /> 个月</span>
														<a id="delBtnCreditTerm" class="icon-i w30 h30 va-middle remove-radiusbtn-icon"></a>
												</div>
											</li>
											<% }}%>
											<li id="reCreditTerm">
												<div class="mr30">
													<input type="hidden" name="JKQXids" value="<%=JKQXLast.F01 %>" />
													<span class="pr"><input type="text" name="JKQXF02" id="maxJKQX" class="border w40 pl5 pr5 h34 lh34 required minf-size-1 maxf-size-100 isint"
														value="<%=JKQXLast.F02 %>" /> 个月以上</span>
													<input type="hidden" name="JKQXF03" class="border w40 pl5 pr5 h34 lh34"
														value="<%=JKQXLast.F03 %>" />
														<a id="AddCreditTerm"
														class="icon-i w30 h30 va-middle add-radiusbtn-icon"></a>
												</div>
											</li>
										</ul>
									</div>
								</div>
								<!--借款期限 结束-->

								<div class="tc f16">
									<input value="提交"
										class="btn-blue2 btn2 white radius-6 pl20 pr20 h40 lh36 sumbitForme" fromname="form1"
										type="submit">
								</div>
							</div>
						</div>
					</div>
					</form>
					
					<div style="display: none;">
					<ul id="reCopyYearRate">
						<li>
							<div class="mr30">
								<span class="pr"><input type="text" name="addNHSYF02" class="border w40 pl5 pr5 h34 lh34 required minf-size-1 maxf-size-24 isint" onblur="checkText($(this))" value="" /> %</span>
									一
								<span class="pr"><input type="text" name="addNHSYF03" class="border w40 pl5 pr5 h34 lh34 required minf-size-1 maxf-size-24 isint" onblur="checkText($(this))" value="" /> %</span>
									<a id="delBtnYearRate" class="icon-i w30 h30 va-middle remove-radiusbtn-icon"></a>
							</div>
						</li>
					</ul>
					</div>
					
					<div style="display: none;">
					<ul id="reCopyProgress">
						<li>
							<div class="mr30">
								<span class="pr"><input type="text" name="addRZJDF02" class="border w40 pl5 pr5 h34 lh34 required minf-size-1 maxf-size-100 isint" onblur="checkText($(this))" value="" /> %</span>
									一 
								<span class="pr"><input type="text" name="addRZJDF03" class="border w40 pl5 pr5 h34 lh34 required minf-size-1 maxf-size-100 isint" onblur="checkText($(this))" value="" /> %</span>
									<a id="delBtnProgress" class="icon-i w30 h30 va-middle remove-radiusbtn-icon"></a>
							</div>
						</li>
					</ul>
					</div>
					
					<div style="display: none;">
						<ul id="reCopyCreditTerm">
							<li>
				              <div class="mr30">
					                <span class="pr"><input type="text" name="addJKQXF02" class="border w40 pl5 pr5 h34 lh34 required minf-size-1 maxf-size-100 isint" onblur="checkText($(this))" value="" /></span>
					                	一
					                <span class="pr"><input type="text" name="addJKQXF03" class="border w40 pl5 pr5 h34 lh34 required minf-size-1 maxf-size-100 isint" onblur="checkText($(this))" value="" />个月</span>
					                	<a id="delBtnCreditTerm" class="icon-i w30 h30 va-middle remove-radiusbtn-icon"></a> 
					          </div>
				            </li>
						</ul>
					</div>

					<!--加载内容 结束-->
				</div>
			</div>
			<div class="popup_bg hide"></div>
		</div>
		<!--右边内容 结束-->
	<!--内容-->
	<div id="info"></div>
	<script type="text/javascript"
		src="<%=controller.getStaticPath(request)%>/js/filterValidation.js"></script>
	<script type="text/javascript">
	var nhsyErrorMsg = "年化利率范围输入错误，后一个输入框的值必须大于前一个输入框的值！";
	var rzjdErrorMsg = "融资进度范围输入错误，后一个输入框的值必须大于前一个输入框的值！";
	var jkqxErrorMsg = "借款期限范围输入错误，后一个输入框的值必须大于前一个输入框的值！";
	function checkCondition(){
		return checkNHSYRange() && checkRZJDRange() && checkJKQXRange();
	}
	/**
	* 校验年化收益范围是否没有交叉
	* return: ture（没有交叉）;false（有交叉）
	*/
	function checkNHSYRange(){
		var minNHSY = parseInt($("#minNHSY").val());
		var maxScore = parseInt($("#maxNHSY").val());
		var nhsyUls = $("#nhsyUl li input[type='text']");
		var nhsyLen = nhsyUls.length;
		var flag = true;
		$("#nhsyUl li input[type='text']").each(function(index){
			var obj = $(this);
			var val = parseInt(obj.val());
			//第一个下限不用校验
			if(index == 0){
				return true;
			}
			if(index == nhsyLen-1)
			{
				return true;
			} 
			var prev = index - 1;
			var next = index + 1;
			var prevVal = parseInt(nhsyUls.eq(prev).val());
			var nextVal = parseInt(nhsyUls.eq(next).val());
			if((index == 1) && (index != nhsyLen-1))
			{
				if(val < prevVal || val >=nextVal){
					$("#info").html(showDialogInfo(nhsyErrorMsg,"wrong"));
					$("div.popup_bg").show();
					flag = false;
					return false;
				}
			}
			else if(index == nhsyLen-2)
			{
				if(val <= prevVal || val >nextVal){
					$("#info").html(showDialogInfo(nhsyErrorMsg,"wrong"));
					$("div.popup_bg").show();
					flag = false;
					return false;
				}
			} 
			else
			{
				if(val <= prevVal || val >=nextVal){
					$("#info").html(showDialogInfo(nhsyErrorMsg,"wrong"));
					$("div.popup_bg").show();
					flag = false;
					return false;
				}
			}
			
		});
		if(minNHSY > maxScore){
			$("#info").html(showDialogInfo(nhsyErrorMsg,"wrong"));
			$("div.popup_bg").show();
			flag = false;
		}
		return flag;
	}
	/**
	* 校验融资进度范围是否没有交叉
	* return: ture（没有交叉）;false（有交叉）
	*/
	function checkRZJDRange(){
		var minRZJD = parseInt($("#minRZJD").val());
		var maxRZJD = parseInt($("#maxRZJD").val());
		var rzjdUls = $("#rzjdUl li input[type='text']");
		var rzjdLen = rzjdUls.length;
		var flag = true;
		$("#rzjdUl li input[type='text']").each(function(index){
			var obj = $(this);
			var val = parseInt(obj.val());
			if(index == 0){
				return true;
			}
			if(index == rzjdLen-1){
				return true;
			}
			var prev = index - 1;
			var next = index + 1;
			var prevVal = parseInt(rzjdUls.eq(prev).val());
			var nextVal = parseInt(rzjdUls.eq(next).val());
			if((index == 1) && (index != rzjdLen-1))
			{
				if(val < prevVal || val >=nextVal){
					$("#info").html(showDialogInfo(rzjdErrorMsg,"wrong"));
					$("div.popup_bg").show();
					flag = false;
					return false;
				}
			}
			else if(index == rzjdLen-2)
			{
				if(val <= prevVal || val >nextVal){
					$("#info").html(showDialogInfo(rzjdErrorMsg,"wrong"));
					$("div.popup_bg").show();
					flag = false;
					return false;
				}
			} 
			else
			{
				if(val <= prevVal || val >=nextVal){
					$("#info").html(showDialogInfo(rzjdErrorMsg,"wrong"));
					$("div.popup_bg").show();
					flag = false;
					return false;
				}
			}
		});
		if(minRZJD > maxRZJD){
			$("#info").html(showDialogInfo(rzjdErrorMsg,"wrong"));
			$("div.popup_bg").show();
			flag = false;
		}
		return flag;
	}
	/**
	* 校验借款期限范围是否没有交叉
	* return: ture（没有交叉）;false（有交叉）
	*/
	function checkJKQXRange(){
		var minJKQX = parseInt($("#minJKQX").val());
		var maxJKQX = parseInt($("#maxJKQX").val());
		var jkqxUls = $("#jkqxUl li input[type='text']");
		var jkqxLen = jkqxUls.length;
		var flag = true;
		$("#jkqxUl li input[type='text']").each(function(index){
			var obj = $(this);
			var val = parseInt(obj.val());
			if(index == 0){
				return true;
			}
			if(index == jkqxLen-1){
				return true;
			}
			var prev = index - 1;
			var next = index + 1;
			var prevVal = parseInt(jkqxUls.eq(prev).val());
			var nextVal = parseInt(jkqxUls.eq(next).val());
			if((index == 1) && (index != jkqxLen-1))
			{
				if(val < prevVal || val >=nextVal){
					$("#info").html(showDialogInfo(jkqxErrorMsg,"wrong"));
					$("div.popup_bg").show();
					flag = false;
					return false;
				}
			}
			else if(index == jkqxLen-2)
			{
				if(val <= prevVal || val >nextVal){
					$("#info").html(showDialogInfo(jkqxErrorMsg,"wrong"));
					$("div.popup_bg").show();
					flag = false;
					return false;
				}
			} 
			else
			{
				if(val <= prevVal || val >=nextVal){
					$("#info").html(showDialogInfo(jkqxErrorMsg,"wrong"));
					$("div.popup_bg").show();
					flag = false;
					return false;
				}
			}
		});
		if(minJKQX > maxJKQX){
			$("#info").html(showDialogInfo(jkqxErrorMsg,"wrong"));
			$("div.popup_bg").show();
			flag = false;
		}
		return flag;
	}
	</script>
	<script type="text/javascript">
		$(function(){
			var yearRate = 0;
			<%if(NHSYFilters != null){%>
			yearRate = <%=NHSYFilters.length%>;
			<%}%>
			$("#delBtnYearRate").live("click",function(){
				var NHSYids = $(this).prev().prev().prev();
				$(this).closest("li").remove();
				if(NHSYids.val() > 0){
					var data = {"NHSYids":NHSYids.val()};
					//ajax异步提交删除已存在的筛选条件
					$.ajax({
						type:"post",
						dataType:"html",
						url:"<%=controller.getURI(request, DelFilterSettings.class)%>",
						data:data,
						success: function (returnData){
							if (returnData) {
								var ct = eval('(' + returnData + ')');
								if(ct.length > 0 && ct[0].num != 1){
									return false;
								}
							}
						}
					});
				}
				yearRate--;
			});
			
			$("#AddYearRate").click(function(){
				if(yearRate>=6){
					$(".popup_bg").show();
					$("#info").html(showDialogInfo("最多可新增6个输入区域", "wrong"));
					return null;
				}
				$("#reYearRate").before($("#reCopyYearRate").html());
				yearRate++;
			});
		});
		
		$(function(){
			var progress = 0;
			<%if(RZJDFilters != null){%>
			progress = <%=RZJDFilters.length%>;
			<%}%>
			$("#delBtnProgress").live("click",function(){
				var RZJDids = $(this).prev().prev().prev();
				$(this).closest("li").remove();
				if(RZJDids.val() > 0){
					var data = {"RZJDids":RZJDids.val()};
					$.ajax({
						type:"post",
						dataType:"html",
						url:"<%=controller.getURI(request, DelFilterSettings.class)%>",
						data:data,
						success: function (returnData){
							if (returnData) {
								var ct = eval('(' + returnData + ')');
								if(ct.length > 0 && ct[0].num != 1){
									return false;
								}
							}
						}
					});
				}
				progress--;
			});
			
			$("#AddProgress").click(function(){
				if(progress>=6){
					$(".popup_bg").show();
					$("#info").html(showDialogInfo("最多可新增6个输入区域", "wrong"));
					return null;
				}
				$("#reProgress").before($("#reCopyProgress").html());
				progress++;
			});
		});
		
		$(function(){
			var creditTerm = 0;
			<%if(JKQXFilters != null){%>
			creditTerm = <%=JKQXFilters.length%>;
			<%}%>
			$("#delBtnCreditTerm").live("click",function(){
				var JKQXids = $(this).prev().prev().prev();
				$(this).closest("li").remove();
				if(JKQXids.val() > 0){
					var data = {"JKQXids":JKQXids.val()};
					$.ajax({
						type:"post",
						dataType:"html",
						url:"<%=controller.getURI(request, DelFilterSettings.class)%>",
						data:data,
						success: function (returnData){
							if (returnData) {
								var ct = eval('(' + returnData + ')');
								if(ct.length > 0 && ct[0].num != 1){
									return false;
								}
							}
						}
					});
				}
				creditTerm--;
			});
			
			$("#AddCreditTerm").click(function(){
				if(creditTerm>=6){
					$(".popup_bg").show();
					$("#info").html(showDialogInfo("最多可新增6个输入区域", "wrong"));
					return null;
				}
				$("#reCreditTerm").before($("#reCopyCreditTerm").html());
				creditTerm++;
			});
		});
	</script>

	<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/dialog.js"></script>
	<%
	    String messageInfo = controller.getPrompt(request, response, PromptLevel.INFO); 
			if(!StringHelper.isEmpty(messageInfo)) {
	%>
	<script type="text/javascript">
		$(".popup_bg").show();
		$("#info").html(showDialogInfo('<%=messageInfo%>', "yes"));
	</script>
	<%
	    }
	%>
</body>
</html>