<%@page import="com.dimeng.p2p.user.servlets.account.ccxx.Ccxx" %>
<%@page import="com.dimeng.p2p.user.servlets.account.fcxx.Fcxx" %>
<%@page import="com.dimeng.p2p.user.servlets.account.gzxx.Gzxx" %>
<%@page import="com.dimeng.p2p.user.servlets.account.UserBases" %>
<%@page import="com.dimeng.p2p.user.servlets.account.xlxx.Xlxx" %>
<%@page import="com.dimeng.p2p.S61.entities.T6142" %>
<%@page import="com.dimeng.util.ObjectHelper" %>
<%@include file="/WEB-INF/include/authenticatedSession.jsp" %>
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
<%

    CURRENT_CATEGORY = "ZHGL";
    CURRENT_SUB_CATEGORY = "GRJCXX";
    T6142 t = ObjectHelper.convert(request.getAttribute("t"), T6142.class);
    if (t == null) {
        response.sendError(HttpServletResponse.SC_NOT_FOUND);
    }
%>
<div class="main_bg clearfix">
    <div class="user_wrap w1002 clearfix">
        <%@include file="/WEB-INF/include/menu.jsp" %>
        <div class="r_main">

            <div class="user_mod">
                <div class="user_tab clearfix Menubox">
                    <ul>
                        <li onclick="location.href='<%=controller.getViewURI(request, UserBases.class) %>?userBasesFlag=1'">
                            个人基本信息<i></i></li>
                        <li onclick="location.href='<%=controller.getViewURI(request, UserBases.class) %>?userBasesFlag=6'">
                            认证信息<i></i></li>
                        <li class="hover">个人学历信息<i></i></li>
                        <li onclick="location.href='<%=controller.getViewURI(request, UserBases.class) %>?userBasesFlag=3'">
                            个人工作信息<i></i></li>
                        <li onclick="location.href='<%=controller.getViewURI(request, UserBases.class) %>?userBasesFlag=4'">
                            房产信息<i></i></li>
                        <li onclick="location.href='<%=controller.getViewURI(request, UserBases.class) %>?userBasesFlag=5'">
                            车产信息<i></i></li>
                    </ul>
                </div>
            </div>

            <div class="p30">
                <div class="form_info">
                    <ul class="cell">
                        <li>
                            <div class="til">
                                <span class="red">*</span>毕业院校：
                            </div>
                            <div class="info">
                                <%StringHelper.filterHTML(out, t.F03); %>
                            </div>
                            <div class="tir"></div>
                            <div class="clear"></div>
                        </li>
                        <li>
                            <div class="til">
                                <span class="red">*</span>入学年份：
                            </div>
                            <div class="info">
                                <%= t.F04 %>年
                            </div>
                            <div class="tir"></div>
                            <div class="clear"></div>
                        </li>
                        <li>
                            <div class="til">
                                <span class="red">*</span>毕业年份：
                            </div>
                            <div class="info">
                                <%= t.F07 %>年
                            </div>
                            <div class="tir"></div>
                            <div class="clear"></div>
                        </li>
                        <li>
                            <div class="til">
                                <span class="red">*</span>专业：
                            </div>
                            <div class="info">
                                <%StringHelper.filterHTML(out, t.F05); %>
                            </div>
                            <div class="tir"></div>
                            <div class="clear"></div>
                        </li>
                        <li>
                            <div class="til">
                                <span class="red">*</span>在校情况简介：
                            </div>
                            <div class="info">
                                <%StringHelper.filterHTML(out, t.F06); %>
                            </div>
                            <div class="tir"></div>
                            <div class="clear"></div>
                        </li>
                        <li>
                            <div class="tc">
                                <input type="button"
                                       onclick="location.href='<%=controller.getViewURI(request, UserBases.class) %>?userBasesFlag=2'"
                                       class="btn01" value="返回"/>
                            </div>

                            <div class="tir"></div>
                            <div class="clear"></div>
                        </li>
                    </ul>
                </div>

            </div>

        </div>
        <div class="clear"></div>
    </div>
</div>
<div class="clear"></div>

<script type="text/javascript">

</script>
<%@include file="/WEB-INF/include/footer.jsp" %>
<%@include file="/WEB-INF/include/dialog.jsp" %>
<script type="text/javascript"
        src="<%=controller.getStaticPath(request)%>/js/validation.js"></script>
</body>
</html>