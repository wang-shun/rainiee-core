<%@page import="com.dimeng.p2p.S61.entities.T6101" %>
<%@page import="com.dimeng.p2p.user.servlets.capital.ByjChargeOut" %>
<%@page import="com.dimeng.util.ObjectHelper" %>
<%@include file="/WEB-INF/include/authenticatedSession.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>我的<%configureProvider.format(out, SystemVariable.SITE_NAME);%>_备用金充值管理
    </title>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
</head>
<body>
<%@include file="/WEB-INF/include/header.jsp" %>
<%

    UserInfoManage manage = serviceSession.getService(UserInfoManage.class);
    T6101 balance = manage.searchFxbyj();
    String type = ObjectHelper.convert(request.getAttribute("type"), String.class);
    String amount = ObjectHelper.convert(request.getAttribute("amount"), String.class);
    T6110 userInfo = manage.getUserInfo(serviceSession.getSession().getAccountId());
    UserManage userManage = serviceSession.getService(UserManage.class);
    String usrCustId = userManage.getUsrCustId();
    CURRENT_CATEGORY = "ZJGL";
    CURRENT_SUB_CATEGORY = "CZ";
%>
<div class="main_bg clearfix">
    <div class="user_wrap w1002 clearfix">
        <%@include file="/WEB-INF/include/menu.jsp" %>
        <div class="r_main">
            <div class="user_mod">
                <div id="con_one_1" class="p25">
                    <form action="<%=controller.getURI(request, ByjChargeOut.class)%>" method="post"
                          onsubmit="return onSubmit();">
                        <div class="gray3 f16">填写转出保证金金额</div>
                        <div class="user_mod_gray mt10 pt30 pb30">
                            <div class="form_info topup_form">
                                <ul class="cell">
                                    <li>
                                        <div class="til">风险备用金金额：</div>
                                        <div class="info">
                                            <span class="gray3 f18"><%=Formater.formatAmount(balance.F06) %></span>元
                                        </div>
                                        <div class="clear"></div>
                                    </li>
                                    <li>
                                        <div class="til">
                                            <span class="red">*</span>转出金额：
                                        </div>
                                        <div class="info">
                                            <input onkeyup="return onlyNumber(this);" id="amount" name="amount" type="text"
                                                   class="text fl mr5"
                                                   value="<%StringHelper.filterQuoter(out, amount); %>"/>
                                            <span class="fl">元&nbsp;&nbsp;</span>
                                            <span id="con_error" class="red"></span>
                                            <br/>
                                            <p tip >&nbsp;</p>
                                        </div>
                                        <div class="clear"></div>
                                    </li>
                                </ul>
                                <div class="tc"><input type="submit" style="cursor: pointer;" value="提交"
                                                       class="btn01 mb15 mt15"/></div>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
        <div class="clear"></div>
    </div>
</div>
<div class="popup_bg" style="display: none;"></div>
<div id="info"></div>
<%@include file="/WEB-INF/include/footer.jsp" %>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/dialog.js"></script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/amountUtil.js"></script>
<script type="text/javascript">
    function checkAmount(obj) {
    	var amount = obj.val();
        if (!amount) {
        	obj.nextAll("p[tip]").html("&nbsp;");
            $("#con_error").html("不能为空！");
            return false;
        }
        if (amount == 0) {
        	obj.nextAll("p[tip]").html("&nbsp;");
            $("#con_error").html("不能为0！");
            return false;
        }

        var myreg = /^\d+(\.\d{1,2})?$/;
        if(!myreg.test(amount))
        {
        	obj.nextAll("p[tip]").html("&nbsp;");
            $("#con_error").html("格式错误，最多两位小数！");
            return false;
        }
        $("#con_error").html("");
        return true;
    }
    function onSubmit() {
        if (checkAmount($("input[name='amount']"))) {
            return true;
        }
        return false;
    }

    function onlyNumber(obj) {
        $("#con_error").html("");
        var isNotNumber = isNaN($(obj).val());
        if (isNotNumber) {//如果不是数字
            $(obj).val("");
        }
        $(obj).nextAll("p[tip]").html(chinaCost($(obj).val()));
    }
    
    $(function(){
    	$("#amount").blur(function() {
    		if(!checkAmount($(this))){
    			return;
    		}
    	});
    });
</script>

<%
    String infoMsg = controller.getPrompt(request, response, PromptLevel.ERROR);
    if (!StringHelper.isEmpty(infoMsg)) {
%>
<script type="text/javascript">
    $("#info").html(showDialogInfo("<%StringHelper.filterHTML(out, infoMsg); %>", "doubt"));
    $("div.popup_bg").show();
</script>
<%} %>
</body>
</html>