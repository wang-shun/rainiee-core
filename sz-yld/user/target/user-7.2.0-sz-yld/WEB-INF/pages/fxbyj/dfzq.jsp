<%@page import="com.dimeng.p2p.user.servlets.fxbyj.Dfzq" %>
<%@include file="/WEB-INF/include/authenticatedSession.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>垫付债权-<%configureProvider.format(out, SystemVariable.SITE_NAME); %></title>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
</head>
<%
    CURRENT_CATEGORY = "JGDB";
    CURRENT_SUB_CATEGORY = "DFZQ";
    boolean isSaveContract = BooleanParser.parse(configureProvider.getProperty(SystemVariable.IS_SAVE_ADVANCE_CONTRACT));
%>
<body>
<%@include file="/WEB-INF/include/header.jsp" %>

<div class="main_bg clearfix">
    <div class="user_wrap w1002 clearfix">
        <%@include file="/WEB-INF/include/menu.jsp" %>
        <div class="r_main">
            <div class="user_mod newsbox">
                <form action="<%=controller.getURI(request, Dfzq.class)%>" method="post">
                    <div class="user_table">
                        <table id="dataTable" width="100%" border="0" cellspacing="0" cellpadding="0">
                            <tr class="til">
                                <td align="center">序号</td>
                                <td align="center">借款标题</td>
                                <td align="center">用户名</td>
                                <td align="center">垫付时间</td>
                                <td align="center">垫付金额</td>
                                <td align="center">垫付返回金额</td>
                                <td align="center">状态</td>
                                <td align="center">合同</td>
                            </tr>
                        </table>
                        <div class="page" id="pageContent"></div>
                        <input type="hidden" id="dfzqUrl" value="<%=controller.getURI(request, Dfzq.class)%>"/>
                    </div>
                </form>
                <div class="clear"></div>
            </div>
        </div> 
    </div>
    <div class="clear"></div>
</div>
<input type="hidden" id="htIndexUrl" value="<%configureProvider.format(out, URLVariable.XY_PTDZXY);%>"/>
<!-- <div class="popup_bg hide"></div> -->
<div id="info"></div>
<%@include file="/WEB-INF/include/footer.jsp" %>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/dialog.js"></script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/fxbyj/dfzq.js"></script>
<%
    String message = controller.getPrompt(request, response, PromptLevel.INFO);
    if (!StringHelper.isEmpty(message)) {
%>
<script type="text/javascript">
    $("#info").html(showDialogInfo("<%=message%>", "successful"));
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
<script type="text/javascript">
	var isSaveContract = <%=isSaveContract%>;
    $(document).ready(function () {
        //垫付债券ajax分页
        advanceBondPaging();
    });
    
    function toAjaxPage(){
    	advanceBondPaging();
    }
</script>
</body>
</html>