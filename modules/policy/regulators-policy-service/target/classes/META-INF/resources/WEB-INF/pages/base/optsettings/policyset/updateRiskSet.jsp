<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.util.ObjectHelper" %>
<%@page import="java.util.List" %>
<%@page import="com.dimeng.p2p.S61.entities.T6148" %>
<%@page import="com.dimeng.p2p.console.servlets.base.optsettings.policyset.UpdateRiskSet" %>

<html>
<link href="<%=controller.getStaticPath(request) %>/css/hhmmss.css"
      rel="stylesheet">
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <%@include file="/WEB-INF/include/script.jsp"%>
</head>
<body>
<%
    CURRENT_CATEGORY = "JCXXGL";
    CURRENT_SUB_CATEGORY = "FXPGSZ";
    List<T6148> t6148List = ObjectHelper.convert(request.getAttribute("t6148List"), List.class);
%>
<div class="right-container">
  <div class="viewFramework-body">
    <div class="viewFramework-content"> 
      
      <!--平台资金统计-->
      
      <div class="p20">
        <form action="<%=controller.getURI(request, UpdateRiskSet.class)%>" method="post" class="form1" onsubmit="return checkCondition();">
          <div class="border">
            <div class="title-container"><i class="icon-i w30 h30 va-middle title-left-icon"></i>风险评估设置</div>
            <div class="content-container pl40 pt40 pr40 pb20">
              <ul class="gray6">
                <li class="mb10 pr"><span class="display-ib w200 tr mr5 pa top5 left0"><em class="red pr5">*</em>保守型：</span>
                  <div class="pl200"><span class="pr10">分值区间</span>
                    <input type="hidden" class="minValue0" value="0"/>
                    <input name="maxBsx"  maxlength="10" type="text" class="maxValue0 text border w100 pl5 required  min-size-1" value="<%=t6148List.get(0).F04 %>" onKeyUp="value=value.replace(/\D/g,'')" mtest="/^((([1-9][0-9]*)|0)|((([1-9][0-9]*)|0)))$/" mtestmsg="必须为整数">
                    <span class="pl10">以下</span> <span tip=""></span> <span errortip="" class="" style="display: none;"></span></div>
                  <p class="red pl200 error_info0"></p>
                </li>
                <li class="mb10 pr"><span class="display-ib w200 tr mr5 pa top5 left0"><em class="red pr5">*</em>谨慎型：</span>
                  <div class="pl200"><span class="pr10">分值区间</span>
                    <input name="minJsx" maxlength="10" type="text" class="minValue1 text border w100 pl5 required" value="<%=t6148List.get(1).F03 %>" onKeyUp="value=value.replace(/\D/g,'')" mtest="/^((([1-9][0-9]*)|0)|((([1-9][0-9]*)|0)))$/" mtestmsg="必须为整数">
                    <span class="pl10 pr10">-</span>
                    <input name="maxJsx" maxlength="10" type="text" class="maxValue1 text border w100 pl5 required" value="<%=t6148List.get(1).F04 %>" onKeyUp="value=value.replace(/\D/g,'')" mtest="/^((([1-9][0-9]*)|0)|((([1-9][0-9]*)|0)))$/" mtestmsg="必须为整数">
                    <span tip=""></span> <span errortip="" class="" style="display: none;"></span></div>
                  <p class="red pl200 error_info1"></p>
                </li>
                <li class="mb10 pr"><span class="display-ib w200 tr mr5 pa top5 left0"><em class="red pr5">*</em>稳健型：</span>
                  <div class="pl200"><span class="pr10">分值区间</span>
                    <input name="minWjx" maxlength="10" type="text" class="minValue2 text border w100 pl5 required" value="<%=t6148List.get(2).F03 %>" onKeyUp="value=value.replace(/\D/g,'')" mtest="/^((([1-9][0-9]*)|0)|((([1-9][0-9]*)|0)))$/" mtestmsg="必须为整数">
                    <span class="pl10 pr10">-</span>
                    <input name="maxWjx" maxlength="10" type="text" class="maxValue2 text border w100 pl5 required" value="<%=t6148List.get(2).F04 %>" onKeyUp="value=value.replace(/\D/g,'')" mtest="/^((([1-9][0-9]*)|0)|((([1-9][0-9]*)|0)))$/" mtestmsg="必须为整数">
                    <span tip=""></span> <span errortip="" class="" style="display: none;"></span></div>
                  <p class="red pl200 error_info2"></p>
                </li>
                <li class="mb10 pr"><span class="display-ib w200 tr mr5 pa top5 left0"><em class="red pr5">*</em>进取型：</span>
                  <div class="pl200"><span class="pr10">分值区间</span>
                    <input name="minJqx" maxlength="10" type="text" class="minValue3 text border w100 pl5 required" value="<%=t6148List.get(3).F03 %>" onKeyUp="value=value.replace(/\D/g,'')" mtest="/^((([1-9][0-9]*)|0)|((([1-9][0-9]*)|0)))$/" mtestmsg="必须为整数">
                    <span class="pl10 pr10">-</span>
                    <input name="maxJqx" maxlength="10" type="text" class="maxValue3 text border w100 pl5 required" value="<%=t6148List.get(3).F04 %>" onKeyUp="value=value.replace(/\D/g,'')" mtest="/^((([1-9][0-9]*)|0)|((([1-9][0-9]*)|0)))$/" mtestmsg="必须为整数">
                    <span tip=""></span> <span errortip="" class="" style="display: none;"></span></div>
                  <p class="red pl200 error_info3"></p>
                </li>
                <li class="mb10 pr"><span class="display-ib w200 tr mr5 pa top5 left0"><em class="red pr5">*</em>激进型：</span>
                  <div class="pl200"><span class="pr10">分值区间</span>
                    <input name="minJjx" maxlength="10" type="text" class="minValue4 text border w100 pl5 required" value="<%=t6148List.get(4).F03 %>" onKeyUp="value=value.replace(/\D/g,'')" mtest="/^((([1-9][0-9]*)|0)|((([1-9][0-9]*)|0)))$/" mtestmsg="必须为整数">
                    <span class="pl10">以上</span> <span tip=""></span> <span errortip="" class="" style="display: none;"></span> </div>
                  <p class="red pl200 error_info4"></p>
                </li>
                
                <li class="mb10">
                  <div class="pl200 ml5">
                    <input type="submit" fromname="form1" class="btn btn-blue2 radius-6 pl20 pr20 sumbitForme" value="保存">
                    <!-- <input type="button" class="btn btn-blue2 radius-6 pl20 pr20 ml10" value="取消"> -->
                  </div>
                </li>
              </ul>
            </div>
          </div>
        </form>
      </div>
    </div>
  </div>
</div>
    <!--右边内容 结束-->
<!--内容-->
<div id="info"></div>
<div class="popup_bg" style="display: none;"></div>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/dialog.js"></script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/validation.js"></script>
<%
    String message = controller.getPrompt(request, response, PromptLevel.INFO);
    if (!StringHelper.isEmpty(message)) {
%>

<script type="text/javascript">
    $("#info").html(showDialogInfo("<%=message%>", "yes"));
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

<%
    String warnMessage = controller.getPrompt(request, response, PromptLevel.WARRING);
    if (!StringHelper.isEmpty(warnMessage)) {
%>
<script type="text/javascript">
    $("#info").html(showDialogInfo("<%=warnMessage%>", "wrong"));
    $("div.popup_bg").show();
</script>
<%
    }
%>
<script type="text/javascript">
    function checkCondition(){
    	for(var i = 0; i < 4; i++){
    		var nextI = i+1;
    		$(".error_info"+nextI).html("");
    		var minValue = parseInt($(".minValue"+i).val());
    		var maxValue = parseInt($(".maxValue"+i).val());
    		if(minValue >= maxValue){
    			$(".error_info"+i).html("下限值不能大于等于上限值");
    			return false;
    		}
    		var minValue1 = parseInt($(".minValue"+nextI).val());
    		if(minValue1 != (maxValue+1)){
    			var tishi = "";
    			switch (nextI) {
				case 1:
					tishi = "谨慎型评分下限值=保守型评分上限值+1";
					break;
                case 2:
                	tishi = "稳健型评分下限值=谨慎型评分上限值+1";
					break;
                case 3:
                	tishi = "进取型评分下限值=稳健型评分上限值+1";
					break;
                case 4:
                	tishi = "激进型评分下限值=进取型评分上限值+1";
					break;
				}
    			$(".error_info"+nextI).html(tishi);
    			return false;
    		}
    	}
    	return true;
    }
</script>

</body>
</html>