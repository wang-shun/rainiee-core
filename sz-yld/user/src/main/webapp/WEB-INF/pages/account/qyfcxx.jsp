<%@page import="com.dimeng.p2p.S61.entities.T6112" %>
<%@page import="com.dimeng.p2p.user.servlets.account.QyBases" %>
<%@page import="com.dimeng.p2p.user.servlets.account.Qyjszl" %>
<%@page import="com.dimeng.p2p.user.servlets.account.Qycwzk" %>
<%@page import="com.dimeng.p2p.user.servlets.account.Qylxxx" %>
<%@page import="com.dimeng.p2p.user.servlets.account.Qyccxx" %>
<%@page import="com.dimeng.p2p.user.servlets.account.Qyfcxx" %>
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
    CURRENT_SUB_CATEGORY = "QYJCXX";
    PagingResult<T6112> result = ObjectHelper.convert(request.getAttribute("result"), PagingResult.class);
%>
<div class="contain clearfix">
    <div class="user_top"></div>
    <div class="about">
        <%@include file="/WEB-INF/include/menu.jsp" %>
        <div class="w780 container fr">
            <div>
                <div class="newsbox">
                    <div class="til clearfix Men_bt">
                        <div class="Menubox">
                            <ul>
                                <li style="border-right: 1px #d7dfe3 solid;"><a
                                        href="<%=controller.getURI(request, QyBases.class) %>">基础信息</a></li>
                                <li style="border-right: 1px #d7dfe3 solid;"><a
                                        href="<%=controller.getURI(request, Qyjszl.class) %>">介绍资料</a></li>
                                <li style="border-right: 1px #d7dfe3 solid;"><a
                                        href="<%=controller.getURI(request, Qycwzk.class) %>">财务状况</a></li>
                                <li style="border-right: 1px #d7dfe3 solid;"><a
                                        href="<%=controller.getURI(request, Qylxxx.class) %>">联系信息</a></li>
                                <li style="border-right: 1px #d7dfe3 solid;"><a
                                        href="<%=controller.getURI(request, Qyccxx.class) %>">车产信息</a></li>
                                <li class="hover"><a style="color: #fff;"
                                                     href="<%=controller.getURI(request, Qyfcxx.class) %>">房产信息</a></li>
                            </ul>
                        </div>
                    </div>

                    <form action="<%=controller.getURI(request, Qyfcxx.class) %>" method="post">
                        <div class="bs_bg pt10">
                            <div class="no_table user_bolr bot" style="display:block;">
                                <table width="100%" border="0" cellspacing="0" cellpadding="0" class="user_table fl">
                                    <tbody>
                                    <tr class="user_lsbg">
                                        <td>小区名称</td>
                                        <td>建筑面积</td>
                                        <td>购买价格</td>
                                        <td>地址</td>
                                        <td>房产证编号</td>
                                    </tr>
                                    <%
                                        T6112[] qy = result.getItems();
                                        if (qy != null) {
                                            for (T6112 entity : qy) {
                                    %>
                                    <tr>
                                        <td><%StringHelper.filterHTML(out, entity.F03);%></td>
                                        <td><%=entity.F04 %>平方米</td>
                                        <td><%=entity.F06 %>万元</td>
                                        <td><%StringHelper.filterHTML(out, entity.F09);%></td>
                                        <td><%StringHelper.filterHTML(out, entity.F10);%></td>
                                    </tr>
                                    <%
                                            }
                                        }
                                    %>
                                    </tbody>
                                </table>
                            </div>

                            <div class="clear"></div>
                            <% Qyfcxx.rendPagingResult(out, result); %>
                            <div class="clear"></div>
                        </div>
                    </form>
                    <div class="clear"></div>
                </div>
            </div>
        </div>
        <div class="clear"></div>
    </div>
</div>
<%@include file="/WEB-INF/include/footer.jsp" %>
<%@include file="/WEB-INF/include/dialog.jsp" %>
</body>
</html>