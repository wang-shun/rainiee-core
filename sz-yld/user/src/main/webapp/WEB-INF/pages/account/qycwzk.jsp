<%@page import="com.dimeng.p2p.S61.entities.T6163" %>
<%@page import="com.dimeng.p2p.account.user.service.QyBaseManage" %>
<%@page import="com.dimeng.p2p.user.servlets.account.QyBases" %>
<%@page import="com.dimeng.p2p.user.servlets.account.Qyjszl" %>
<%@page import="com.dimeng.p2p.user.servlets.account.Qycwzk" %>
<%@page import="com.dimeng.p2p.user.servlets.account.Qylxxx" %>
<%@page import="com.dimeng.p2p.user.servlets.account.Qyccxx" %>
<%@page import="com.dimeng.p2p.user.servlets.account.Qyfcxx" %>
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
    QyBaseManage manage = serviceSession.getService(QyBaseManage.class);
    T6163[] qy = manage.getQycwzk();
    CURRENT_CATEGORY = "ZHGL";
    CURRENT_SUB_CATEGORY = "QYJCXX";
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
                                <li class="hover"><a style="color: #fff;"
                                                     href="<%=controller.getURI(request, Qycwzk.class) %>">财务状况</a></li>
                                <li style="border-right: 1px #d7dfe3 solid;"><a
                                        href="<%=controller.getURI(request, Qylxxx.class) %>">联系信息</a></li>
                                <li style="border-right: 1px #d7dfe3 solid;"><a
                                        href="<%=controller.getURI(request, Qyccxx.class) %>">车产信息</a></li>
                                <li style="border-right: 1px #d7dfe3 solid;"><a
                                        href="<%=controller.getURI(request, Qyfcxx.class) %>">房产信息</a></li>
                            </ul>
                        </div>
                    </div>

                    <div class="bs_bg pt20">
                        <div class="no_table user_bolr bot" style=" display: block;">
                            <table width="100%" border="0" cellspacing="0" cellpadding="0" class="user_table fl">
                                <tbody>
                                <tr class="user_lsbg">
                                    <td width="60">年份</td>
                                    <td width="120">主营收入(万元)</td>
                                    <td>净利润(万元)</td>
                                    <td>总资产(万元)</td>
                                    <td>净资产(万元)</td>
                                    <td>备注</td>
                                    <td>&nbsp;</td>
                                </tr>
                                <%
                                    if (qy != null) {
                                        for (T6163 entity : qy) {
                                %>
                                <tr>
                                    <td><%=entity.F02 %>年</td>
                                    <td class="sr_t01"><%=entity.F03 %>
                                    </td>
                                    <td class="sr_t01"><%=entity.F05 %>
                                    </td>
                                    <td class="sr_t01"><%=entity.F06 %>
                                    </td>
                                    <td class="sr_t01"><%=entity.F07 %>
                                    </td>
                                    <td class="sr_t02"><p title="<%=entity.F08%>"><%
                                        StringHelper.filterHTML(out, entity.F08);%></p></td>
                                </tr>
                                <%
                                        }
                                    }
                                %>
                                </tbody>
                            </table>
                        </div>
                        <div class="clear"></div>
                    </div>
                </div>
            </div>
            <div class="clear"></div>
        </div>
    </div>
</div>
<%@include file="/WEB-INF/include/footer.jsp" %>
<%@include file="/WEB-INF/include/dialog.jsp" %>
</body>
</html>