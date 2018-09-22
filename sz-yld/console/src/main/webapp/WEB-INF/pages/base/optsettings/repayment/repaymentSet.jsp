<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.S62.enums.T6290_F06" %>
<%@page import="com.dimeng.p2p.S62.enums.T6290_F02" %>
<%@page import="com.dimeng.p2p.S62.entities.T6290" %>
<%@page import="com.dimeng.p2p.console.servlets.base.optsettings.repayment.RepaymentSet" %>
<%@page import="com.dimeng.util.ObjectHelper" %>
<%@page import="java.util.List" %>
<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <%@include file="/WEB-INF/include/script.jsp"%>
</head>

<body>
<%
    CURRENT_CATEGORY = "JCXXGL";
    CURRENT_SUB_CATEGORY = "ZDTXSZ";
    List<T6290> hkTxList = ObjectHelper.convert(request.getAttribute("hkTxList"), List.class);
    List<T6290> yqTxList = ObjectHelper.convert(request.getAttribute("yqTxList"), List.class);
    /* T6290 model = null;
    if (request.getAttribute("model") != null) {
        model = ObjectHelper.convert(request.getAttribute("model"), T6290.class);
    }
    String modelId = "";
    String wayType = "";
    String advanceCount = "";
    String passCount = "";
    String passRateCount = "";
    String status = "";
    if (model != null) {
        modelId = String.valueOf(model.F01);
        wayType = model.F02;
        advanceCount = String.valueOf(model.F03);
        passCount = String.valueOf(model.F04);
        passRateCount = String.valueOf(model.F05);
        status = String.valueOf(model.F06.name());
    } */
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <!--加载内容-->
                <div class="p20">
                    <div class="border">
                        <div class="title-container"><i class="icon-i w30 h30 va-middle title-left-icon"></i>账单提醒设置</div>
                        <div class="content-container pl40 pt40 pr40 pb20">
                            <form action="<%=controller.getURI(request, RepaymentSet.class) %>" method="post" class="form1" onsubmit="return checkCondition();">
                                <div class="pat_title clearfix bold ml100">还款提醒</div>
                                <ul class="gray6 pt20 ml100" id="addHktxUl">
                                    <li class="mb10">
										<span class="display-ib w200 tr mr5"><em class="red pr5">*</em>离下个还款日期：</span>
                                        <input name="hkDay" type="text" maxlength="2" class="text border w150 yw_w5 required" <%if(null != hkTxList){%> value="<%=hkTxList.get(0).F03%>" <%}%> onKeyUp="value=value.replace(/\\D/g,'')" mtest="/^((([1-9][0-9]*)|0)|((([1-9][0-9]*)|0)))$/" mtestmsg="必须为整数"/>
                                        <span class="ml10">天前提醒</span><a id="addHkDay" href="javascript:void(0);" class="ml10 display-ib va-middle w30 h30 icon-i add-radiusbtn-icon"></a>
                                        <span tip></span>
                                        <span errortip class="" style="display: none"></span>
                                    </li>
                                </ul>
                                <ul class="gray6 ml100" id="subHktxUl">
                                <%if(null != hkTxList){
                                          for(int i=1;i<hkTxList.size();i++){
                                    %>
                                    <li class="mb10">
										<span class="display-ib w200 tr mr5"><em class="red pr5">*</em>离下个还款日期：</span>
                                        <input name="hkDay" type="text" maxlength="2" class="text border w150 yw_w5 required" value="<%=hkTxList.get(i).F03%>" onKeyUp="value=value.replace(/\\D/g,'')" mtest="/^((([1-9][0-9]*)|0)|((([1-9][0-9]*)|0)))$/" mtestmsg="必须为整数"/>
                                        <span class="ml10">天前提醒</span><a href="javascript:void(0);" onclick="delLi(this);" class="ml10 display-ib va-middle w30 h30 icon-i remove-radiusbtn-icon"></a>
                                        <span tip></span>
                                        <span errortip class="" style="display: none"></span>
                                    </li>
                                    <%}}%>
                                </ul>
                                
                                <ul class="gray6 ml100">
                                    <li class="mb10">
										<span class="display-ib w200 tr mr5"><em class="red pr5">*</em>提醒方式：</span>
										<%for (T6290_F02 m : T6290_F02.values()) {%>
                                        <input class="mr5" name="hktxType" onclick="clearInfo(this);" type="checkbox" value="<%=m.name()%>" <%if(null != hkTxList && hkTxList.get(0).F02.indexOf(m.name())>-1){%>checked='checked'<%} %> ><%=m.getChineseName() %>&nbsp;&nbsp;
                                        <%} %>
                                        <!-- <span tip></span> -->
                                        <span errortip class="red" id="hk_error_info"></span>
                                    </li>
                                    <li class="mb10">
										<span class="display-ib w200 tr mr5"><em class="red pr5">*</em>状态：</span>
										<select name="hkStatus" class="border mr20 h32 mw100 required">
                                            <%for (T6290_F06 status2 : T6290_F06.values()) {%>
                                            <option value="<%=status2.name()%>" <%if (null != hkTxList && status2.name().equals(hkTxList.get(0).F06.name())) {%> selected="selected" <%}%>><%=status2.getChineseName()%></option>
                                            <%}%>
                                        </select>
                                        <span tip></span>
                                        <span errortip class="" style="display: none"></span>
                                    </li>
                                </ul>
                                <div class="pat_title clearfix bold ml100 pt20">逾期提醒</div>
                                <ul class="gray6 pt20 ml100" id="addYqtxUl">
                                    <li class="mb10">
										<span class="display-ib w200 tr mr5"><em class="red pr5">*</em>逾期：</span>
                                        <input name="yqDay" type="text" maxlength="4" class="text border w150 yw_w5 required" <%if(null != yqTxList){%> value="<%=yqTxList.get(0).F03%>" <%}%> onKeyUp="value=value.replace(/\\D/g,'')" mtest="/^((([1-9][0-9]*)|0)|((([1-9][0-9]*)|0)))$/" mtestmsg="必须为整数"/>
                                        <span class="ml10">天前提醒</span><a id="addYqDay" href="javascript:void(0);" class="ml10 display-ib va-middle w30 h30 icon-i add-radiusbtn-icon"></a>
                                        <span tip></span>
                                        <span errortip class="" style="display: none"></span>
                                    </li>
                                </ul>
                                <ul class="gray6 ml100" id="subYqtxUl">
                                <%if(null != yqTxList){
                                          for(int i=1;i<yqTxList.size();i++){
                                    %>
                                    <li class="mb10">
										<span class="display-ib w200 tr mr5"><em class="red pr5">*</em>逾期：</span>
                                        <input name="yqDay" type="text" maxlength="4" class="text border w150 yw_w5 required" value="<%=yqTxList.get(i).F03%>" onKeyUp="value=value.replace(/\\D/g,'')" mtest="/^((([1-9][0-9]*)|0)|((([1-9][0-9]*)|0)))$/" mtestmsg="必须为整数"/>
                                        <span class="ml10">天前提醒</span><a href="javascript:void(0);" onclick="delLi(this);" class="ml10 display-ib va-middle w30 h30 icon-i remove-radiusbtn-icon"></a>
                                        <span tip></span>
                                        <span errortip class="" style="display: none"></span>
                                    </li>
                                    <%}}%>
                                </ul>
                                <ul class="gray6 ml100">
                                    <li class="mb10">
										<span class="display-ib w200 tr mr5"><em class="red pr5">*</em>提醒方式：</span>
										<%for (T6290_F02 m : T6290_F02.values()) {%>
                                        <input class="mr5" name="yqtxType" onclick="clearInfo(this);" type="checkbox" value="<%=m.name()%>" <%if(null != yqTxList && yqTxList.get(0).F02.indexOf(m.name())>-1){%>checked='checked'<%} %>><%=m.getChineseName() %>&nbsp;&nbsp;
                                        <%} %>
                                        <!-- <span tip></span> -->
                                        <span errortip class="red" id="yq_error_info"></span>
                                    </li>
                                    <li class="mb10">
										<span class="display-ib w200 tr mr5"><em class="red pr5">*</em>状态：</span>
										<select name="yqStatus" class="border mr20 h32 mw100 required">
                                            <%for (T6290_F06 status2 : T6290_F06.values()) {%>
                                            <option value="<%=status2.name()%>" <%if (null != yqTxList && status2.name().equals(yqTxList.get(0).F06.name())) {%> selected="selected" <%}%>><%=status2.getChineseName()%></option>
                                            <%}%>
                                        </select>
                                        <span tip></span>
                                        <span errortip class="" style="display: none"></span>
                                    </li>
                                </ul>
                                <ul class="gray6 pt10 ml100">
                                    <li>
                                        <div class="pl200 ml5">
                                            <input type="submit" class="btn btn-blue2 radius-6 pl20 pr20 sumbitForme"
                                                   fromname="form1" value="提交"/>
                                        </div>
                                    </li>
                                </ul>
                            </form>
                        </div>
                        
<%--                         <div class="content-container pl40 pt30 pr40">

                            <form action="<%=controller.getURI(request, RepaymentSet.class) %>" method="post"
                                  class="form1" onsubmit="return onSubmit();">
                                <input name="F01" type="hidden" value="<%=modelId%>">
                                <ul class="cell noborder yxjh ">
                                    <li class="mb10">
                                        <span class="display-ib w200 tr mr5"><span class="red">*</span>还款提醒方式：</span>

                                        <%for (T6290_F02 m : T6290_F02.values()) {%>

                                        <input class="mr5" name="F02" type="checkbox" value="<%=m.name()%>"
                                               <%if(wayType.indexOf(m.name())>-1){%>checked='checked'<%} %>><%=m.getChineseName() %>
                                        &nbsp;&nbsp;

                                        <%} %>
                                        <span tip></span>
                                        <span errortip class="red" id="warnTypeMsg"></span>
                                    </li>
                                    <li class="mb10">
                                        <span class="display-ib w200 tr mr5"><span class="red">*</span>离下个还款日期： </span>
                                        <input type="text" class="text border pl5 required" maxLength="3"
                                               mtest="/^([0]?\d|[1-9][0-9]|[1-2][0-9][0-9]|3[0-6][0-5])$/"
                                               mtestmsg="大于等于0,且不超过365" name="F03"
                                               onKeyUp="value=(parseInt((value=value.replace(/\D/g,''))==''?'0':value,10))"
                                               value="<%StringHelper.filterHTML(out,advanceCount); %>"/>
                                        天提醒 <span>还款日 默认为当天</span>
                                        <span tip></span>
                                        <span errortip class="" style="display: none"></span>
                                    </li>
                                    <li class="mb10">
                                        <span class="display-ib w200 tr mr5"><span class="red">*</span>逾期：</span>
                                        <input type="text" class="text border pl5 required"
                                               maxLength="3" mtest="/^([0]?\d|[1-9][0-9]|[1-2][0-9][0-9]|3[0-6][0-5])$/"
                                               mtestmsg="大于等于0,且不超过365"
                                               name="F04"
                                               onKeyUp="value=(parseInt((value=value.replace(/\D/g,''))==''?'0':value,10))"
                                               value="<%StringHelper.filterHTML(out,passCount); %>"/>
                                        天提醒 <span>如不提醒，可输入0</span>
                                        <span tip></span>
                                        <span errortip class="" style="display: none"></span>
                                    </li>
                                    <li style="display:none;" class="mb10">
                                        <span class="display-ib w200 tr mr5"><span class="red">*</span>逾期提现频率：</span>
                                        <input type="text" class="text border pl5"
                                               maxLength="3" mtest="/^([0]?\d|[1-9][0-9]|[1-2][0-9][0-9]|3[0-6][0-5])$/"
                                               mtestmsg="大于等于0,且不超过365" name="F05"
                                               value="<%StringHelper.filterHTML(out,passRateCount); %>"
                                               onKeyUp="value=(parseInt((value=value.replace(/\D/g,''))==''?'0':value,10))"/>
                                        天1次
                                        <span tip></span>
                                        <span errortip class="" style="display: none"></span>
                                    </li>
                                    <li class="mb10">
                                        <span class="display-ib w200 tr mr5"><span class="red">*</span>状态：</span>
                                        <select name="F06" class="border mr20 h32 mw100 required">

                                            <%
                                                for (T6290_F06 status2 : T6290_F06.values()) {
                                            %>
                                            <option value="<%=status2.name()%>" <%if (status2.name().equals(status)) {%>
                                                    selected="selected" <%}%>><%=status2.getChineseName()%>
                                            </option>
                                            <%
                                                }
                                            %>
                                        </select>
                                        <span tip></span>
                                        <span errortip class="" style="display: none"></span>
                                    </li>
                                    <li class="mb10">
                                        <div class="pl200 ml5"><input type="submit"
                                                                      class="btn btn-blue2 radius-6 pl20 pr20 sumbitForme"
                                                                      style="cursor: pointer;" fromname="form1"
                                                                      value="提交"/></div>
                                    </li>
                                </ul>

                            </form>
                        </div> --%>
                    </div>
                </div>
                <!--加载内容 结束-->
            </div>
        </div>
    </div>
    <!--右边内容 结束-->
<!--内容-->
<div id="info"></div>
<div class="popup_bg hide"></div>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/validation.js"></script>
<script type="text/javascript">

$(function(){
	$("#addHkDay").click(function(){
		if($("#subHktxUl li").length>1){
			$("#info").html(showDialogInfo("最多可添加2个条件","wrong"));
			$(".popup_bg").show();
			return;
		}
		var liHtml = '<li class="mb10">';
		liHtml += '<span class="display-ib w200 tr mr5"><em class="red pr5">*</em>离下个还款日期：</span>';
		liHtml += '\n<input name="hkDay" type="text" maxlength="2" class="text border w150 yw_w5 required"  onKeyUp="value=value.replace(/\\D/g,\'\')" mtest="/^((([1-9][0-9]*)|0)|((([1-9][0-9]*)|0)))$/" mtestmsg="必须为整数"/>';
		liHtml += '\n<span class="ml10">天前提醒</span><a href="javascript:void(0);" onclick="delLi(this);" class="ml10 display-ib va-middle w30 h30 icon-i remove-radiusbtn-icon"></a>';
		liHtml += '<span tip></span>';
		liHtml += '<span errortip class="" style="display: none"></span>';
		liHtml += '</li>';
		$("#subHktxUl").append(liHtml);
	});
	
	$("#addYqDay").click(function(){
		if($("#subYqtxUl li").length>1){
			$("#info").html(showDialogInfo("最多可添加2个条件","wrong"));
			$(".popup_bg").show();
			return;
		}
		var liHtml = '<li class="mb10">';
		liHtml += '<span class="display-ib w200 tr mr5"><em class="red pr5">*</em>逾期：</span>';
		liHtml += '\n<input name="yqDay" type="text" maxlength="4" class="text border w150 yw_w5 required"  onKeyUp="value=value.replace(/\\D/g,\'\')" mtest="/^((([1-9][0-9]*)|0)|((([1-9][0-9]*)|0)))$/" mtestmsg="必须为整数"/>';
		liHtml += '\n<span class="ml10">天前提醒</span><a href="javascript:void(0);" onclick="delLi(this);" class="ml10 display-ib va-middle w30 h30 icon-i remove-radiusbtn-icon"></a>';
		liHtml += '<span tip></span>';
		liHtml += '<span errortip class="" style="display: none"></span>';
		liHtml += '</li>';
		$("#subYqtxUl").append(liHtml);
	});
});	

    function onSubmit() {
        $("#hk_error_info").html("");
        var hktxType = $("input[name=hktxType]:checked").length;
        if (hktxType == 0) {
            $("#hk_error_info").html("至少选择一种提醒方式");
            return false;
        }
        
        $("#yq_error_info").html("");
        var yqtxType = $("input[name=yqtxType]:checked").length;
        if (yqtxType == 0) {
            $("#yq_error_info").html("至少选择一种提醒方式");
            return false;
        }
        
        return true;

    }
    
    function clearInfo(obj){
    	if($(obj).is(':checked')) {
    		$(obj).nextAll("p[errortip]").html("");
    	}
    }
    
    function delLi(obj){
    	$(obj).parent().remove();
    }
    
    
    
</script>

<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/dialog.js"></script>
<%	
		String message = controller.getPrompt(request, response, PromptLevel.WARRING); 
		if(!StringHelper.isEmpty(message)) {
	%>
		<script type="text/javascript">
		$(".popup_bg").show();
		$("#info").html(showDialogInfo('<%=message%>',"wrong"));
		</script>
	<%} %>
<%	
		String messageInfo = controller.getPrompt(request, response, PromptLevel.INFO); 
		if(!StringHelper.isEmpty(messageInfo)) {
	%>
		<script type="text/javascript">
		$(".popup_bg").show();
		$("#info").html(showDialogInfo('<%=messageInfo%>',"yes"));
		</script>
	<%} %>
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
</body>
</html>