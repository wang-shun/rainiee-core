<%@page import="com.dimeng.p2p.user.servlets.account.gzxx.Gzxx" %>
<%@page import="com.dimeng.p2p.account.user.service.UserBaseManage" %>
<%@page import="com.dimeng.p2p.user.servlets.account.ccxx.Ccxx" %>
<%@page import="com.dimeng.p2p.user.servlets.account.fcxx.Fcxx" %>
<%@page import="com.dimeng.p2p.user.servlets.account.xlxx.Xlxx" %>
<%@page import="com.dimeng.p2p.user.servlets.account.UserBases" %>
<%@page import="com.dimeng.p2p.S61.entities.T6143" %>
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
    T6143 t = ObjectHelper.convert(request.getAttribute("t"), T6143.class);
    if (t == null) {
        response.sendError(HttpServletResponse.SC_NOT_FOUND);
    }
    UserBaseManage manage = serviceSession.getService(UserBaseManage.class);
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
                        <li onclick="location.href='<%=controller.getViewURI(request, UserBases.class) %>?userBasesFlag=2'">
                            个人学历信息<i></i></li>
                        <li class="hover">个人工作信息<i></i></li>
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
                                <span class="red">*</span>工作状态：
                            </div>
                            <div class="info">
                                <%=t.F03.getChineseName() %>
                            </div>
                            <div class="tir"></div>
                            <div class="clear"></div>
                        </li>
                        <li>
                            <div class="til">
                                <span class="red">*</span>单位名称：
                            </div>
                            <div class="info">
                                <%StringHelper.filterHTML(out, t.F04);%>
                            </div>
                            <div class="tir"></div>
                            <div class="clear"></div>
                        </li>
                        <li>
                            <div class="til">
                                <span class="red">*</span>职位：
                            </div>
                            <div class="info">
                                <%StringHelper.filterHTML(out, t.F05);%>
                            </div>
                            <div class="tir"></div>
                            <div class="clear"></div>
                        </li>
                        <li>
                            <div class="til">
                                <span class="red">*</span>工作邮箱：
                            </div>
                            <div class="info">
                                <%StringHelper.filterHTML(out, t.F06);%>
                            </div>
                            <div class="tir"></div>
                            <div class="clear"></div>
                        </li>
                        <li>
                            <div class="til">
                                <span class="red">*</span>工作城市：
                            </div>
                            <div class="info">
                                <%=manage.getRegion(t.F07) %>
                            </div>
                            <div class="tir"></div>
                            <div class="clear"></div>
                        </li>
                        <li>
                            <div class="til">
                                <span class="red">*</span>工作地址：
                            </div>
                            <div class="info">
                                <%StringHelper.filterHTML(out, t.F08);%>
                            </div>
                            <div class="tir"></div>
                            <div class="clear"></div>
                        </li>
                        <li>
                            <div class="til">
                                <span class="red">*</span>公司类别：
                            </div>
                            <div class="info">
                                <%StringHelper.filterHTML(out, t.F09);%>
                            </div>
                            <div class="tir"></div>
                            <div class="clear"></div>
                        </li>
                        <li>
                            <div class="til">
                                <span class="red">*</span>公司行业：
                            </div>
                            <div class="info">
                                <%StringHelper.filterHTML(out, t.F10);%>
                            </div>
                            <div class="tir"></div>
                            <div class="clear"></div>
                        </li>
                        <li>
                            <div class="til">
                                <span class="red">*</span>公司规模：
                            </div>
                            <div class="info">
                                <%StringHelper.filterHTML(out, t.F11);%>人
                            </div>
                            <div class="tir"></div>
                            <div class="clear"></div>
                        </li>
                        <li>
                            <div class="tc">
                                <input type="button"
                                       onclick="location.href='<%=controller.getViewURI(request, UserBases.class) %>?userBasesFlag=3'"
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
<%@include file="/WEB-INF/include/footer.jsp" %>
<%@include file="/WEB-INF/include/dialog.jsp" %>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/validation.js"></script>
</body>
</html>