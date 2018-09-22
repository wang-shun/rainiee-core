<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.util.ObjectHelper" %>
<%@page import="com.dimeng.p2p.console.servlets.base.optsettings.dxz.AddDxz" %>
<%@page import="com.dimeng.p2p.console.servlets.base.optsettings.dxz.DxzList" %>
<%@page import="com.dimeng.p2p.console.servlets.base.optsettings.dxz.CheckDxzInfo" %>
<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <%@include file="/WEB-INF/include/script.jsp"%>
</head>
<body>
<%
	CURRENT_CATEGORY = "JCXXGL";
	CURRENT_SUB_CATEGORY = "DXZ";
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <!--加载内容-->
                <div class="p20">
                    <div class="border">
                        <div class="title-container"><i class="icon-i w30 h30 va-middle title-left-icon"></i>新增定向组</div>
                        <div class="content-container pl40 pt40 pr40 pb20">
                            <input id="newPasswordRegexId" type="hidden"
                                   value="<%configureProvider.format(out,SystemVariable.NEW_PASSWORD_REGEX);%>"/>
                            <input id="passwordRegexContentId" type="hidden"
                                   value="<%configureProvider.format(out,SystemVariable.PASSWORD_REGEX_CONTENT);%>"/>

                            <form action="<%=controller.getURI(request, AddDxz.class)%>" method="post" class="form1" onsubmit="return onSubmit();">
                                <ul class="gray6">
                                    <li class="mb10">
										<span class="display-ib w200 tr mr5">
												<span class="red">*</span>定向组ID：
										</span>
                                        <input id="F02" name="F02" type="text" maxlength="8" onblur="checkDxzId(this);"
                                               value="<%StringHelper.filterQuoter(out, request.getParameter("F02"));%>" class="text border w250 yw_w5"/>
                                       <!--  <span tip id="dxzIdTisMsg">4-8位数字或字母</span>
                                        <span id="dxzIdMsg" errortip class="" style="display: none"></span> -->
                                        <span id="dxzIdTisMsg">4-8位数字或字母</span>
                                    </li>
                                     <li class="mb10">
										<span class="display-ib w200 tr mr5">
												<span class="red">*</span>定向组名称：
										</span>
                                        <input id="F03" name="F03" type="text" maxlength="15" onblur="checkDxzName(this)"
                                               value="<%StringHelper.filterQuoter(out, request.getParameter("F02"));%>" class="text border w250 yw_w5"/>
                                        <!-- <span tip>15个字符以内</span>
                                        <span id="dxzNameMsg" errortip class="" style="display: none"></span> -->
                                        <span id="dxzNameTisMsg">15个字符以内</span>
                                    </li>
                                    
                                    <li class="mb10">
										<span class="display-ib w200 tr mr5">备注：</span>
                                        <textarea name="F08" class="w250 h120 border p5 max-length-50"></textarea>
                                        <span tip="">50个字符以内</span>
                                        <span errortip="" class="" style="display: none"></span>
                                    </li>
                                    
                                    <li>
                                        <div class="pl200 ml5">
                                            <%
                                                if (dimengSession.isAccessableResource(AddDxz.class)) {
                                            %>
                                            <input type="submit" class="btn btn-blue2 radius-6 pl20 pr20 sumbitForme"
                                                   fromname="form1" value="确认"/>
                                            <%
                                            } else {
                                            %>
                                            <input type="button" class="disabled" value="确认"/>
                                            <%
                                                }
                                            %>
                                            <input type="button"
                                                   onclick="location.href='<%=controller.getURI(request, DxzList.class) %>'"
                                                   class="btn btn-blue2 radius-6 pl20 pr20 ml40" value="取消"/>
                                        </div>
                                    </li>
                                </ul>
                            </form>
                        </div>
                    </div>
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
<script type="text/javascript">
var checkURL = '<%=controller.getURI(request, CheckDxzInfo.class)%>';

/**
 * 验证输入定向组ID
 */
var dxzIdIsFlg = false;
function checkDxzId(thisValue){
	var dxzId = thisValue.value;
	var p_msg = $("#dxzIdTisMsg");
	p_msg.removeClass();
	p_msg.text("4-8位数字或字母");
	var myreg = /^[a-zA-Z0-9]{4,8}$/;
	if(isEmpty(dxzId) || !myreg.test(dxzId))
	{
		dxzIdIsFlg = false;
		p_msg.addClass("error_tip");
	}
	else{
		dxzIdIsFlg = checkUserInfo("dxzId",dxzId,p_msg);
	}
	return dxzIdIsFlg;
}

/**
 * 验证输入定向组名称 
 */
var dxzNameIsFlg = false;
function checkDxzName(thisValue){
	var dxzName = thisValue.value;
	var p_msg = $("#dxzNameTisMsg");
	p_msg.removeClass();
	p_msg.text("15个字符以内");
	if(isEmpty(dxzName))
	{
		dxzNameIsFlg = false;
		p_msg.addClass("error_tip");
	}
	else{
		dxzNameIsFlg = checkUserInfo("dxzName",dxzName,p_msg);
	}
	return dxzNameIsFlg;
}

function checkUserInfo(checkType,dxzIdOrName,p)
{
	var isOK = false;
	var data={"checkType":checkType,"dxzIdOrName":dxzIdOrName};
	$.ajax({
		type:"post",
		dataType:"html",
		url:checkURL,
		data:data,
		async: false,
		success:function(data){
			if(data){
				var ct = eval('('+data+')');
				if(ct.length>0 && ct[0].num != 1){
					var msg = ct[0].msg;
					p.show();
					p.text(msg);
					p.addClass("error_tip");
					isOK = false;
					
				}else{
					isOK = true;
				}
			}
		}
	});
	return isOK;
}

function onSubmit() {
	if(isEmpty($("#F02").val()))
	{
		$("#dxzIdTisMsg").show();
		$("#dxzIdTisMsg").addClass("error_tip");
		return false;
	}
	if(isEmpty($("#F03").val())){
		$("#dxzNameTisMsg").show();
		$("#dxzNameTisMsg").addClass("error_tip");
		return false;
	}
	if(!dxzIdIsFlg || !dxzNameIsFlg)
	{
		return false;
	}
	return true;
}

/**
 * 验证是否为空 true:为空 
 */
function isEmpty(str){
	if(str == null || str == ""){
		return true;
	}else{
		return false;
	}
}

</script>

<%
    String warnMessage = controller.getPrompt(request, response, PromptLevel.WARRING);
    if (!StringHelper.isEmpty(warnMessage)) {
%>
<script type="text/javascript">
    $("#info").html(showDialogInfo("<%=warnMessage%>", "perfect"));
    $("div.popup_bg").show();
</script>
<%}%>
</body>
</html>