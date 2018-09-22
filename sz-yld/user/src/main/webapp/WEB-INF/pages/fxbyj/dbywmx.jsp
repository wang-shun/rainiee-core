<%@page import="com.dimeng.p2p.user.servlets.fxbyj.Dbywmx" %>
<%@include file="/WEB-INF/include/authenticatedSession.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>担保业务明细-<% configureProvider.format(out, SystemVariable.SITE_NAME);%></title>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
</head>
<%
    CURRENT_CATEGORY = "JGDB";
    CURRENT_SUB_CATEGORY = "DBYWMX";
%>
<body>
<%@include file="/WEB-INF/include/header.jsp" %>
<div class="main_bg clearfix">
    <div class="user_wrap w1002 clearfix">
        <%@include file="/WEB-INF/include/menu.jsp" %>
        <div class="r_main">
            <div class="user_mod clearfix">
                <%
                /* boolean isHasGuarant = BooleanParser.parse(configureProvider.getProperty(GuarantorVariavle.IS_HAS_GUARANTOR));
                    if(isHasGuarant){ */
                        ApplyGuarantorManage manage = serviceSession.getService(ApplyGuarantorManage.class);
                        T6125 t6125 = manage.getGuanterInfo();
                       /*  if(t6125 != null){ */
                %>
                <div class="guarantee_mod clearfix">
                    <div class="icon"></div>
                    <div class="fl"><span class="f16">担保额度(元):&nbsp;<%=t6125 == null ? "0.00" : Formater.formatAmount(t6125.F04)%></span></div>
                </div>
               <%--  <%}}%> --%>
            <div class="user_mod newsbox">
                <form action="<%=controller.getURI(request, Dbywmx.class)%>"
                      method="post">
                    <input type="hidden" name="jkbId" id="jkbId" value=""> <input
                        type="hidden" name="dfksqs" id="dfksqs" value="">

                    <div class="user_table">
                        <table id="dataTable" width="100%" border="0" cellspacing="0" cellpadding="0">
                            <tr class="til">
                                <td align="center">序号</td>
                                <td align="center">借款标题</td>
                                <td align="center">用户名</td>
                                <td align="center">借款金额</td>
                                <td align="center">剩余期限</td>
                                <td align="center">担保方式</td>
                                <td align="center">待还金额</td>
                                <td align="center">是否逾期</td>
                                <td align="center">状态</td>
                                <td align="center">操作</td>
                            </tr>
                        </table>
                        <div class="page" id="pageContent"></div>
                    </div>
                    <input type="hidden" id="dbyemxUrl" value="<%=controller.getURI(request, Dbywmx.class)%>">
                </form>
                <div class="clear"></div>
            </div>
        </div>
        <div class="clear"></div>
    </div>
</div>
<div class="dialog" style=" display: none;">
    <div class="title"><a href="javascript:void(0);" class="out"></a>垫付债权</div>
    <div class="content">
        <div class="tip_information">
            <div class="doubt"></div>
            <div class="tips">
                <span class="f20 gray33">是否执行垫付债权操作？</span>
            </div>
            <div class="clear"></div>
        </div>
        <div class="tc mt20">
            <a href="javascript:void(0)" id="ok" class="btn01">是</a><a
                href="javascript:void(0)" id="cancel" class="btn01 btn_gray ml20">否</a>
        </div>
    </div>
</div>
<div class="w510 thickpos" id="info"></div>
<div class="popup_bg" style="display:none;"></div>
<div id="info"></div>
<%@include file="/WEB-INF/include/footer.jsp" %>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/dialog.js"></script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/fxbyj/dfzq.js"></script>
<script type="text/javascript">
    $(function () {
        $("#ok").click(function () {
            var jkbId = $("#jkbId").val();
            var dfksqs = $("#dfksqs").val();
            var form = document.forms[0];
            form.action = "<%=configureProvider.format(URLVariable.PAY_SBDF_URL)%>?loanId=" + jkbId + "&advanceNum=" + dfksqs;
            form.submit();
        });
        $("a.out").click(function(){
			$("div.dialog").hide();
			$("div.popup_bg").hide();
		});
        //担保业务明细分页
        businessDetailPaging();
    });
    function Dfzq(jkbId, dfksqs) {
        $("div.popup_bg").show();
        $("div.dialog").show();
        $("#jkbId").val(jkbId);
        $("#dfksqs").val(dfksqs);
    }
    
    function toAjaxPage(){
    	businessDetailPaging();
    }
</script>
<%
    String message = controller.getPrompt(request, response, PromptLevel.INFO);
    if (!StringHelper.isEmpty(message)) {
%>
<script type="text/javascript">
    $("#info").html(showDialogInfoReload("<%=message%>", "successful"));
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
    $("#info").html(showDialogInfo("<%=errorMessage%>", "error"));
    $("div.popup_bg").show();
</script>
<%
    }
%>
<%
    String warnMessage = controller.getPrompt(request, response, PromptLevel.WARRING);
    if (!StringHelper.isEmpty(warnMessage)) 
    {
        if(warnMessage.indexOf("网签认证")>0)
        {
%>
<script type="text/javascript">
	$("#info").html(showForwardInfo("<%=warnMessage%>", "doubt","<%=configureProvider.format(URLVariable.USER_NETSIGN_URL)%>"));
    $("div.popup_bg").show();
</script>
<%
        }
        else
        {
%>
<script type="text/javascript">
    $("#info").html(showDialogInfo("<%=warnMessage%>", "doubt"));
    $("div.popup_bg").show();
</script>
<%
    	}
    }
%>
</body>
</html>