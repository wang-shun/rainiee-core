<%@ page import="com.dimeng.p2p.S61.entities.T6147" %>
<%@ page import="com.dimeng.util.ObjectHelper" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title><%=configureProvider.getProperty(SystemVariable.SITE_NAME)%>
    </title>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
</head>

<body>
<%@include file="/WEB-INF/include/header.jsp" %>


<div class="main_bg">
    <div class="main_mod">
        <%
            T6147 t6147 = ObjectHelper.convert(request.getAttribute("t6147"), T6147.class);
            if(t6147 != null){
        %>
            <div class="complete clearfix tc">
                <i class="correct_ico tc"></i>
                <div class="complete_til tc">
                    评估完成，您的得分为<%=t6147.F03%>分！</br>
                    您的风险承受能力为：<span class="orange"><%=t6147.F04.getChineseName()%></span>
                </div>
            </div>

            <div class="clearfix tc pt40"><a class="btn06" href="<%=configureProvider.format(URLVariable.USER_BASES)%>?userBasesFlag=1">确定</a></div>
        <%}%>
    </div>

</div>
<div id="info"></div>
<div class="popup_bg" style="display: none;"></div>
<script type="text/javascript"
        src="<%=controller.getStaticPath(request)%>/js/dialog.js"></script>
<%
    String errorMessage = controller.getPrompt(request, response, PromptLevel.ERROR);
    if (!StringHelper.isEmpty(errorMessage)) {
%>
<script type="text/javascript">
    $("#info").html(showDialogInfo('<%=errorMessage%>', "error"));
</script>
<%} %>
<%@include file="/WEB-INF/include/footer.jsp" %>
</body>
</html>
