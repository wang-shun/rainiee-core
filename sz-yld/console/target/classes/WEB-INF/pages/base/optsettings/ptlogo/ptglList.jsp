<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.util.ObjectHelper" %>
<%@page import="com.dimeng.p2p.console.servlets.base.optsettings.ptlogo.ViewPtgl" %>
<%@page import="com.dimeng.p2p.console.servlets.base.optsettings.ptlogo.PtglList" %>
<%@page import="com.dimeng.p2p.S71.entities.T7101" %>
<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <%@include file="/WEB-INF/include/script.jsp"%>
</head>
<body>
<%
    CURRENT_CATEGORY = "JCXXGL";
    CURRENT_SUB_CATEGORY = "PT";
    T7101 result = ObjectHelper.convert(request.getAttribute("result"), T7101.class);
%>

<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <div class="p20">
                    <div class="border">
                        <div class="title-container"><i class="icon-i w30 h30 va-middle title-left-icon"></i>平台图标管理
                        </div>
                        <div class="content-container p20">
                            <form id="form1" action="<%=controller.getURI(request, PtglList.class)%>" method="post">
                                <div class="border table-container">
                                    <table class="table table-style gray6 tl">
                                        <thead>
                                        <tr class="title tc">
                                            <th>序号</th>
                                            <th>名称</th>
                                            <th>图片</th>
                                            <th class="w200">操作</th>
                                        </tr>
                                        </thead>
                                        <tbody class="f12">
                                        <%
                                            int index = 1;
                                            if (result != null) {
                                        %>
                                        <tr class="tc">
                                            <td><%=index++ %>
                                            </td>
                                            <td>前台页头LOGO文件编码</td>
                                            <td>
                                                <div class="p5"><img
                                                        src="<%=fileStore.getURL(configureProvider.getProperty(SystemVariable.QTLOGO))%>"
                                                        class="ma150"></div>
                                            </td>
                                            <td>
                                                <%if (dimengSession.isAccessableResource(ViewPtgl.class)) {%>
                                                <a class="link-blue click-link"
                                                   href="<%=controller.getURI(request,ViewPtgl.class)%>?id=<%=result.F01%>&index=<%=index %>">修改</a>
                                                <%} else { %>
                                                <span class="disabled">修改</span>
                                                <%} %>
                                            </td>
                                        </tr>
                                        <tr class="tc">
                                            <td><%=index++ %>
                                            </td>
                                            <td>前台页尾LOGO文件编码</td>
                                            <td>
                                                <div class="p5"><img
                                                        src="<%=fileStore.getURL(configureProvider.getProperty(SystemVariable.QTYWLOGO))%>"
                                                        class="ma150"></div>
                                            </td>
                                            <td>
                                                <%if (dimengSession.isAccessableResource(ViewPtgl.class)) {%>
                                                <a class="link-blue click-link"
                                                   href="<%=controller.getURI(request,ViewPtgl.class)%>?id=<%=result.F01%>&index=<%=index %>">修改</a>
                                                <%} else { %>
                                                <span class="disabled">修改</span>
                                                <%} %>
                                            </td>
                                        </tr>
                                        <tr class="tr-even tc">
                                            <td><%=index++ %>
                                            </td>
                                            <td>后台登录LOGO文件编码</td>
                                            <td>
                                                <div class="p5"><img
                                                        src="<%=fileStore.getURL(configureProvider.getProperty(SystemVariable.HTDLLOGO))%>"
                                                        class="ma150"></div>
                                            </td>
                                            <td>
                                                <%if (dimengSession.isAccessableResource(ViewPtgl.class)) {%>
                                                <a class="link-blue click-link"
                                                   href="<%=controller.getURI(request,ViewPtgl.class)%>?id=<%=result.F01%>&index=<%=index %>">修改</a>
                                                <%} else { %>
                                                <span class="disabled">修改</span>
                                                <%} %>
                                            </td>
                                        </tr>
                                        <tr class="tc">
                                            <td><%=index++ %>
                                            </td>
                                            <td>后台首页LOGO文件编码</td>
                                            <td>
                                                <div class="p5"><img
                                                        src="<%=fileStore.getURL(configureProvider.getProperty(SystemVariable.HTSYLOGO))%>"
                                                        class="ma150"></div>
                                            </td>
                                            <td>
                                                <%if (dimengSession.isAccessableResource(ViewPtgl.class)) {%>
                                                <a class="link-blue click-link"
                                                   href="<%=controller.getURI(request,ViewPtgl.class)%>?id=<%=result.F01%>&index=<%=index %>">修改</a>
                                                <%} else { %>
                                                <span class="disabled">修改</span>
                                                <%} %>
                                            </td>
                                        </tr>
                                        <tr class="tr-even tc">
                                            <td><%=index++ %>
                                            </td>
                                            <td>微信二维码</td>
                                            <td>
                                                <div class="p5"><img
                                                        src="<%=fileStore.getURL(configureProvider.getProperty(SystemVariable.QTWXEWM))%>"
                                                        class="ma150"></div>
                                            </td>
                                            <td>
                                                <%if (dimengSession.isAccessableResource(ViewPtgl.class)) {%>
                                                <a class="link-blue click-link"
                                                   href="<%=controller.getURI(request,ViewPtgl.class)%>?id=<%=result.F01%>&index=<%=index %>">修改</a>
                                                <%} else { %>
                                                <span class="disabled">修改</span>
                                                <%} %>
                                            </td>
                                        </tr>
                                        <tr class="tc">
                                            <td><%=index++ %>
                                            </td>
                                            <td>微博二维码</td>
                                            <td>
                                                <div class="p5"><img
                                                        src="<%=fileStore.getURL(configureProvider.getProperty(SystemVariable.QTWBEWM))%>"
                                                        class="ma150"></div>
                                            </td>
                                            <td>
                                                <%if (dimengSession.isAccessableResource(ViewPtgl.class)) {%>
                                                <a class="link-blue click-link"
                                                   href="<%=controller.getURI(request,ViewPtgl.class)%>?id=<%=result.F01%>&index=<%=index %>">修改</a>
                                                <%} else { %>
                                                <span class="disabled">修改</span>
                                                <%} %>
                                            </td>
                                        </tr>
                                        <tr class="tr-even tc">
                                            <td><%=index++ %>
                                            </td>
                                            <td>手机客户端</td>
                                            <td>
                                                <div class="p5"><img
                                                        src="<%=fileStore.getURL(configureProvider.getProperty(SystemVariable.QTSJKHD))%>"
                                                        class="ma150"></div>
                                            </td>
                                            <td>
                                                <%if (dimengSession.isAccessableResource(ViewPtgl.class)) {%>
                                                <a class="link-blue click-link"
                                                   href="<%=controller.getURI(request,ViewPtgl.class)%>?id=<%=result.F01%>&index=<%=index %>">修改</a>
                                                <%} else { %>
                                                <span class="disabled">修改</span>
                                                <%} %>
                                            </td>
                                        </tr>
                                        <tr class="tc">
                                            <td><%=index++ %>
                                            </td>
                                            <td>标的默认图标</td>
                                            <td>
                                                <div class="p5"><img
                                                        src="<%=fileStore.getURL(configureProvider.getProperty(SystemVariable.BDMRTB))%>"
                                                        class="ma150"></div>
                                            </td>
                                            <td>
                                                <%if (dimengSession.isAccessableResource(ViewPtgl.class)) {%>
                                                <a class="link-blue click-link"
                                                   href="<%=controller.getURI(request,ViewPtgl.class)%>?id=<%=result.F01%>&index=<%=index %>">修改</a>
                                                <%} else { %>
                                                <span class="disabled">修改</span>
                                                <%} %>
                                            </td>
                                        </tr>
                                        <tr class="tc">
                                            <td><%=index++ %>
                                            </td>
                                            <td>平台水印图标</td>
                                            <td>
                                                <div class="p5"><img
                                                        src="<%=fileStore.getURL(configureProvider.getProperty(SystemVariable.WATERIMAGE))%>"
                                                        class="ma150"></div>
                                            </td>
                                            <td>
                                                <%if (dimengSession.isAccessableResource(ViewPtgl.class)) {%>
                                                <a class="link-blue click-link"
                                                   href="<%=controller.getURI(request,ViewPtgl.class)%>?id=<%=result.F01%>&index=<%=index %>">修改</a>
                                                <%} else { %>
                                                <span class="disabled">修改</span>
                                                <%} %>
                                            </td>
                                        </tr>
                                        <tr class="tc">
                                            <td><%=index++ %>
                                            </td>
                                            <td>用户中心头像图标（男）</td>
                                            <td>
                                                <div class="p5"><img
                                                        src="<%=fileStore.getURL(configureProvider.getProperty(SystemVariable.TXNANTB))%>"
                                                        class="ma150"></div>
                                            </td>
                                            <td>
                                                <%if (dimengSession.isAccessableResource(ViewPtgl.class)) {%>
                                                <a class="link-blue click-link"
                                                   href="<%=controller.getURI(request,ViewPtgl.class)%>?id=<%=result.F01%>&index=<%=index %>">修改</a>
                                                <%} else { %>
                                                <span class="disabled">修改</span>
                                                <%} %>
                                            </td>
                                        </tr>
                                        <tr class="tc">
                                            <td><%=index++ %>
                                            </td>
                                            <td>用户中心头像图标（女）</td>
                                            <td>
                                                <div class="p5"><img
                                                        src="<%=fileStore.getURL(configureProvider.getProperty(SystemVariable.TXNVTB))%>"
                                                        class="ma150"></div>
                                            </td>
                                            <td>
                                                <%if (dimengSession.isAccessableResource(ViewPtgl.class)) {%>
                                                <a class="link-blue click-link"
                                                   href="<%=controller.getURI(request,ViewPtgl.class)%>?id=<%=result.F01%>&index=<%=index %>">修改</a>
                                                <%} else { %>
                                                <span class="disabled">修改</span>
                                                <%} %>
                                            </td>
                                        </tr>
                                        <%
                                            }
                                        %>
                                        </tbody>
                                    </table>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</body>
</html>