<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.console.servlets.base.malloptsettings.scoreset.ViewSetScore" %>
<%@page import="com.dimeng.p2p.repeater.score.entity.SetScore" %>
<%@page import="com.dimeng.p2p.variables.defines.SystemVariable" %>
<%@page import="com.dimeng.util.ObjectHelper" %>
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
    CURRENT_SUB_CATEGORY = "JFSZ";
    SetScore setScore = ObjectHelper.convert(request.getAttribute("setScore"), SetScore.class);
    Boolean tg = BooleanParser.parseObject(configureProvider.getProperty(SystemVariable.SFZJTG));
    String escrow = configureProvider.getProperty(SystemVariable.ESCROW_PREFIX);
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <!--加载内容-->
                <div class="p20">
                    <div class="border">
                        <div class="title-container">
                            <i class="icon-i w30 h30 va-middle title-left-icon"></i>积分设置
                        </div>
                        <div class="content-container pl40 pt40 pr40 pb20">
                            <form action="<%=controller.getURI(request, ViewSetScore.class)%>"
                                  method="post" class="form1">
                                <div class="pl200 ml5 info orange" id="info_msg">
                                <%StringHelper.filterHTML(out, controller.getPrompt(request, response, PromptLevel.ERROR));%>
                                <%StringHelper.filterHTML(out, controller.getPrompt(request, response, PromptLevel.INFO));%>
                                </div>
                                <div class="pat_title clearfix bold"><input type="checkbox" <%if("on".equals(setScore.baseSetCheckbox)){ %>checked="checked"<%}%> value="baseSet" name="baseSetCheckbox" class="mr10">基本设置</div>
                                <ul class="gray6">
                                    <li class="mb10">
											<span class="display-ib w200 tr mr5">
												<input type="checkbox" <%if("on".equals(setScore.registerCheckbox)){ %>checked="checked"<%}%> value="register" name="registerCheckbox" class="mr10 baseSetCheckbox">注册赠送积分：
											</span>
                                        <input name="register" type="text" maxlength="10"
                                               class="text border w300 yw_w5 required"
                                               value="<%=setScore.register%>" onKeyUp="value=(parseInt((value=value.replace(/\D/g,''))==''?'0':value,10))" mtest="/^((([1-9][0-9]*)|0)|((([1-9][0-9]*)|0)))$/" mtestmsg="必须为整数"/>
                                        <span tip></span>
                                        <span errortip class="" style="display: none"></span>
                                    </li>
                                    <li class="mb10">
											<span class="display-ib w200 tr mr5">
												<input type="checkbox" <%if("on".equals(setScore.cellPhoneCheckbox)){ %>checked="checked"<%}%> value="cellphone" name="cellPhoneCheckbox" class="mr10 baseSetCheckbox">手机认证赠送积分：
											</span>
											<input name="cellPhone" type="text" maxlength="10"
                                               class="text border w300 yw_w5 required"
                                               value="<%=setScore.cellPhone%>" onKeyUp="value=(parseInt((value=value.replace(/\D/g,''))==''?'0':value,10))" mtest="/^((([1-9][0-9]*)|0)|((([1-9][0-9]*)|0)))$/" mtestmsg="必须为整数"/>
                                            <span tip></span>
                                            <span errortip class="" style="display: none"></span>
                                    </li>
                                   <li class="mb10">
											<span class="display-ib w200 tr mr5">
												<input type="checkbox" <%if("on".equals(setScore.mailBoxCheckbox)){ %>checked="checked"<%}%> value="mailbox" name="mailBoxCheckbox" class="mr10 baseSetCheckbox">邮箱认证赠送积分：
											</span>
                                        <input name="mailBox" type="text" maxlength="10"
                                               class="text border w300 yw_w5 required"
                                               value="<%=setScore.mailBox%>" onKeyUp="value=(parseInt((value=value.replace(/\D/g,''))==''?'0':value,10))" mtest="/^((([1-9][0-9]*)|0)|((([1-9][0-9]*)|0)))$/" mtestmsg="必须为整数"/>
                                        <span tip></span>
                                        <span errortip class="" style="display: none"></span>
                                    </li>
                                    <li class="mb10">
											<span class="display-ib w200 tr mr5">
												<input type="checkbox" <%if("on".equals(setScore.realNameCheckbox)){ %>checked="checked"<%}%> value="realname" name="realNameCheckbox" class="mr10 baseSetCheckbox">实名认证赠送积分：
											</span>
                                        <input name="realName" type="text" maxlength="10"
                                               class="text border w300 yw_w5 required"
                                               value="<%=setScore.realName%>" onKeyUp="value=(parseInt((value=value.replace(/\D/g,''))==''?'0':value,10))" mtest="/^((([1-9][0-9]*)|0)|((([1-9][0-9]*)|0)))$/" mtestmsg="必须为整数"/>
                                        <span tip></span>
                                        <span errortip class="" style="display: none"></span>
                                    </li>
                                    <%if(tg){%>
                                    <li class="mb10">
											<span class="display-ib w200 tr mr5">
												<input type="checkbox" <%if("on".equals(setScore.trusteeshipCheckbox)){ %>checked="checked"<%}%> value="trusteeship" name="trusteeshipCheckbox" class="mr10 baseSetCheckbox">开通第三方托管账户：
											</span>
                                        <input name="trusteeship" type="text" maxlength="10"
                                               class="text border w300 yw_w5 required"
                                               value="<%=setScore.trusteeship%>" onKeyUp="value=(parseInt((value=value.replace(/\D/g,''))==''?'0':value,10))" mtest="/^((([1-9][0-9]*)|0)|((([1-9][0-9]*)|0)))$/" mtestmsg="必须为整数"/>
                                        <span tip></span>
                                        <span errortip class="" style="display: none"></span>
                                    </li>
                                    <% }%>
                                    <li class="mb10">
											<span class="display-ib w200 tr mr5">
												<input type="checkbox" <%if("on".equals(setScore.inviteCheckbox)){ %>checked="checked"<%}%> value="invite" name="inviteCheckbox" class="mr10 baseSetCheckbox">邀请用户投资获取积分：
											</span>
                                        <input name="invite" type="text" maxlength="10"
                                               class="text border w300 yw_w5 required"
                                               value="<%=setScore.invite%>" onKeyUp="value=(parseInt((value=value.replace(/\D/g,''))==''?'0':value,10))" mtest="/^((([1-9][0-9]*)|0)|((([1-9][0-9]*)|0)))$/" mtestmsg="必须为整数"/>
                                        <span class="gray9" tip>邀请的用户只要投资，就可以获得积分(可多次获取)</span>
                                        <span errortip class="" style="display: none"></span>
                                    </li>
                                    <li class="mb10">
											<span class="display-ib w200 tr mr5">
												<input type="checkbox" <%if("on".equals(setScore.chargeCheckbox)){ %>checked="checked"<%}%> value="charge" name="chargeCheckbox" class="baseSetCheckbox" style="margin-right:52px;">充值：每单笔充值
											</span>
                                        <input name="chargeAmount" type="text" maxlength="10"
                                               class="text border w300 yw_w5 required"
                                               value="<%=setScore.chargeAmount%>" onKeyUp="value=(parseInt((value=value.replace(/\D/g,''))==''?'0':value,10))" mtest="/^((([1-9][0-9]*)|0)|((([1-9][0-9]*)|0)))$/" mtestmsg="必须为整数"/>元
                                        <span class="ml20 mr5">赠送积分</span>
                                        <input name="chargeScore" type="text" maxlength="10"
                                               class="text border w300 yw_w5 required"
                                               value="<%=setScore.chargeScore%>" onKeyUp="value=(parseInt((value=value.replace(/\D/g,''))==''?'0':value,10))" mtest="/^((([1-9][0-9]*)|0)|((([1-9][0-9]*)|0)))$/" mtestmsg="必须为整数"/>
                                        <span tip></span>
                                        <span errortip class="" style="display: none"></span>
                                    </li>
                                    <li class="mb10">
											<span class="display-ib w200 tr mr5">
												<input type="checkbox" <%if("on".equals(setScore.investCheckbox)){ %>checked="checked"<%}%> value="invest" name="investCheckbox" class="baseSetCheckbox" style="margin-right:52px;">投资：每单笔投资
											</span>
                                        <input name="investAmount" type="text" maxlength="10"
                                               class="text border w300 yw_w5 required"
                                               value="<%=setScore.investAmount%>" onKeyUp="value=(parseInt((value=value.replace(/\D/g,''))==''?'0':value,10))" mtest="/^((([1-9][0-9]*)|0)|((([1-9][0-9]*)|0)))$/" mtestmsg="必须为整数"/>元
                                        <span class="ml20 mr5">赠送积分</span>
                                        <input name="investScore" type="text" maxlength="10"
                                               class="text border w300 yw_w5 required"
                                               value="<%=setScore.investScore%>" onKeyUp="value=(parseInt((value=value.replace(/\D/g,''))==''?'0':value,10))" mtest="/^((([1-9][0-9]*)|0)|((([1-9][0-9]*)|0)))$/" mtestmsg="必须为整数"/>
                                        <span tip></span>
                                        <span errortip class="" style="display: none"></span>
                                    </li>
                                    <li class="mb10">
											<span class="display-ib w200 tr mr5">
												<input type="checkbox" <%if("on".equals(setScore.buygoodsCheckbox)){ %>checked="checked"<%}%> value="buygoods" name="buygoodsCheckbox" class="mr10 baseSetCheckbox">现金购买商品：每单笔购买
											</span>
                                        <input name="buygoodsAmount" type="text" maxlength="10"
                                               class="text border w300 yw_w5 required"
                                               value="<%=setScore.buygoodsAmount%>" onKeyUp="value=(parseInt((value=value.replace(/\D/g,''))==''?'0':value,10))" mtest="/^((([1-9][0-9]*)|0)|((([1-9][0-9]*)|0)))$/" mtestmsg="必须为整数"/>元
                                        <span class="ml20 mr5">赠送积分</span>
                                        <input name="buygoodsScore" type="text" maxlength="10"
                                               class="text border w300 yw_w5 required"
                                               value="<%=setScore.buygoodsScore%>" onKeyUp="value=(parseInt((value=value.replace(/\D/g,''))==''?'0':value,10))" mtest="/^((([1-9][0-9]*)|0)|((([1-9][0-9]*)|0)))$/" mtestmsg="必须为整数"/>
                                        <span tip></span>
                                        <span errortip class="" style="display: none"></span>
                                    </li>
                                </ul>
                                <div class="pat_title clearfix bold"><input type="checkbox" <%if("on".equals(setScore.signCheckbox)){ %>checked="checked"<%}%> value="sign" name="signCheckbox" class="mr10">签到积分设置</div>
                                <ul class="gray6">
                                    <li class="mb10">
											<span class="display-ib w200 tr mr5">
												初始积分：
											</span>
                                        <input name="initialScore" type="text" maxlength="10"
                                               class="text border w300 yw_w5 required"
                                               value="<%=setScore.initialScore%>" onKeyUp="value=(parseInt((value=value.replace(/\D/g,''))==''?'0':value,10))" mtest="/^((([1-9][0-9]*)|0)|((([1-9][0-9]*)|0)))$/" mtestmsg="必须为整数"/>
                                        <span class="gray9" tip>第一天的签到积分</span>
                                        <span errortip class="" style="display: none"></span>
                                    </li>
                                    <li class="mb10">
											<span class="display-ib w200 tr mr5">
												递增积分：
											</span>
                                        <input name="increaseScore" type="text" maxlength="10"
                                               class="text border w300 yw_w5 required"
                                               value="<%=setScore.increaseScore%>" onKeyUp="value=(parseInt((value=value.replace(/\D/g,''))==''?'0':value,10))" mtest="/^((([1-9][0-9]*)|0)|((([1-9][0-9]*)|0)))$/" mtestmsg="必须为整数"/>
                                        <span class="gray9" tip>从连续签到第二天开始，每天增加递增积分</span>
                                        <span errortip class="" style="display: none"></span>
                                    </li>
                                    <li class="mb10">
											<span class="display-ib w200 tr mr5">
												连续签到上限：
											</span>
                                        <input name="limitScore" type="text" maxlength="10"
                                               class="text border w300 yw_w5 required"
                                               value="<%=setScore.limitScore%>" onKeyUp="value=(parseInt((value=value.replace(/\D/g,''))==''?'0':value,10))" mtest="/^((([1-9][0-9]*)|0)|((([1-9][0-9]*)|0)))$/" mtestmsg="必须为整数"/>
                                        <span class="gray9" tip>达到连续签到上限天数后，继续连续签到获得的积分等于达到上限天数获得的积分</span>
                                        <span errortip class="" style="display: none"></span>
                                    </li>
                                    <li>
                                        <div class="pl200 ml5">
                                            <input type="submit" class="btn btn-blue2 radius-6 pl20 pr20 sumbitForme"
                                                   fromname="form1" value="保存"/>
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
<div id="info"></div>
<div class="popup_bg" style="display: none;"></div>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/dialog.js"></script>
<script type="text/javascript"
        src="<%=controller.getStaticPath(request)%>/js/validation.js"></script>
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
$(function(){
	
	$('input[name="baseSetCheckbox"]').click(function(){
		if($(this).is(':checked')) {
			 $(".baseSetCheckbox").attr("checked", true);
		}else{
			$(".baseSetCheckbox").attr("checked", false);
		}
	});
	
});
</script>
</body>
</html>