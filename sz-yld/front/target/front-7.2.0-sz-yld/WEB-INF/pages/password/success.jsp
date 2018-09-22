<%@page import="com.dimeng.p2p.variables.defines.SystemVariable" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <title><%configureProvider.format(out, SystemVariable.SITE_TITLE);%>-找回密码</title>
    <%@include file="/WEB-INF/include/style.jsp" %>
</head>
<body>
<%
    String accountId = serviceSession.getSession().getAttribute("PASSWORD_ACCOUNT_ID");
    if (StringHelper.isEmpty(accountId)) {
        controller.sendRedirect(request, response, controller.getViewURI(request, com.dimeng.p2p.front.servlets.password.Index.class));
    }
%>
<%@include file="/WEB-INF/include/header.jsp" %>
<div class="main_bg" style="padding-top:20px;">
    <div class="login_mod">
        <div class="login_hd"><i class="icon"></i><span class="gray3 f18">找回密码</span></div>
        <div class="secret clearfix">
            <div class="f18 tc">尊敬的客户，恭喜您已经成功重置新的密码，请妥善保管。<br/><span id="msg" class="red">3</span>秒后会自动跳转到登录页面，请使用新密码登录。
            </div>
            <div class="f18 tc">如果3秒后没有反应，<a href="<%configureProvider.format(out,URLVariable.INDEX);%>" class="red">点击这里</a>返回首页。
            </div>
        </div>
    </div>
</div>
<%@include file="/WEB-INF/include/footer.jsp" %>
<script type="text/javascript">
    var i = 2;
    //计时器
    function timers() {
        $(function () {
            if (i >= 0) {
                $("#msg").html(i);
                i--;
            } else {
                location.href = "<%configureProvider.format(out,URLVariable.LOGIN);%>";
            }
        });
    }
    var timer = setInterval("timers()", 1000);
</script>
</body>
</html>