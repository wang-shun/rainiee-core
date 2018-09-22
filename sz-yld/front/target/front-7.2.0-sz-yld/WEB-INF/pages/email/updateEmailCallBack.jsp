<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html dir="ltr" xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN">
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <title><%=configureProvider.getProperty(SystemVariable.SITE_NAME)%>-绑定邮箱</title>
    <%@include file="/WEB-INF/include/style.jsp" %>
</head>

<body>
<%@include file="/WEB-INF/include/header.jsp" %>
<%
    String message = ObjectHelper.convert(request.getAttribute("message"), String.class);
    String status = ObjectHelper.convert(request.getAttribute("status"), String.class);
%>
<div class="main_bg" style="padding-top:20px;">
    <div class="login_mod">
        <div class="login_hd"><i class="icon"></i><span class="gray3 f18">修改邮箱</span></div>
        <%if ("success".equals(status)) {%>
        <div class="tip_info mt50 pt50">
            <div class="successful"></div>
            <div class="tips">
                <span class="f18 gray3">修改邮箱成功！</span>
            </div>
        </div>
        <%if (dimengSession != null && dimengSession.isAuthenticated()) { %>
        <div class="tc mt50"><a href="<%=controller.getStaticPath(request)%>/user/index.html" class="btn06">返回会员中心</a>
        </div>
        <%} else { %>
        <div class="tc mt50"><a href="<%=controller.getStaticPath(request)%>/user/login.html" class="btn06">跳转登录页面</a>
        </div>
        <%} %>
        <%} else {%>
        <div class="tip_info mt50 pt50">
            <div class="error"></div>
            <div class="tips">
                <span class="f18 gray3">修改邮箱失败！</span><br/>
                <%=StringHelper.isEmpty(message) ? "" : message %>
            </div>
        </div>
        <%if (dimengSession != null && dimengSession.isAuthenticated()) { %>
        <div class="tc mt50"><a href="<%=controller.getStaticPath(request)%>/user/account/userBases.html?userBasesFlag=1" class="btn06">重新认证</a></div>
        <%}else{ %>
        <div class="tc mt50"><a href="<%=controller.getStaticPath(request)%>/user/login.html" class="btn06">重新认证</a></div>
        <%} %>
        <%}%>
    </div>
</div>
<%@include file="/WEB-INF/include/footer.jsp" %>
</body>
</html>