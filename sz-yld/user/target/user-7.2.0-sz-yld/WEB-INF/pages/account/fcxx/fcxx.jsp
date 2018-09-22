<%@page import="com.dimeng.p2p.user.servlets.account.ccxx.Ccxx" %>
<%@page import="com.dimeng.p2p.user.servlets.account.gzxx.Gzxx" %>
<%@page import="com.dimeng.p2p.user.servlets.account.xlxx.Xlxx" %>
<%@page import="com.dimeng.util.filter.HTMLFilter" %>
<%@page import="com.dimeng.p2p.user.servlets.account.UserBases" %>
<%@page import="com.dimeng.p2p.user.servlets.account.fcxx.UpdateFcxx" %>
<%@page import="com.dimeng.p2p.user.servlets.account.fcxx.ViewFcxx" %>
<%@page import="com.dimeng.p2p.user.servlets.account.fcxx.Fcxx" %>
<%@page import="com.dimeng.p2p.user.servlets.account.fcxx.AddFcxx" %>
<%@page import="com.dimeng.p2p.S61.entities.T6112" %>
<%@page import="com.dimeng.util.ObjectHelper" %>
<%@page import="java.text.DecimalFormat" %>
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

    PagingResult<T6112> result = ObjectHelper.convert(request.getAttribute("result"), PagingResult.class);
%>
<div class="contain clearfix">
    <div class="user_top"></div>
    <div class="about">
        <%@include file="/WEB-INF/include/menu.jsp" %>
        <form action="<%=controller.getURI(request, Fcxx.class) %>" method="post">
            <div class="container fr">
                <div class="til clearfix Men_bt">
                    <div class="Menubox">
                        <ul>
                            <li>
                                <a href="<%=controller.getViewURI(request, UserBases.class) %>?userBasesFlag=1">个人基本信息</a>
                            </li>
                            <li style="border-right: 1px #d7dfe3 solid;"><a
                                    href="<%=controller.getViewURI(request, UserBases.class) %>?userBasesFlag=6">认证信息</a>
                            </li>
                            <li>
                                <a href="<%=controller.getViewURI(request, UserBases.class) %>?userBasesFlag=2">个人学历信息</a>
                            </li>
                            <li>
                                <a href="<%=controller.getViewURI(request, UserBases.class) %>?userBasesFlag=3">个人工作信息</a>
                            </li>
                            <li class="hover">房产信息</li>
                            <li><a href="<%=controller.getViewURI(request, UserBases.class) %>?userBasesFlag=5">车产信息</a>
                            </li>
                        </ul>
                    </div>
                </div>
                <br/>
                <a href="<%=controller.getURI(request, AddFcxx.class) %>" class="btn01" style="margin-left:20px;">新增</a>
                <br/>
                <table width="100%" border="0" cellspacing="0" cellpadding="0"
                       class="user_table tc">
                    <tr class="user_lsbg">
                        <td>序号</td>
                        <td>小区名称</td>
                        <td>建筑面积(平方米)</td>
                        <td>购买价格(万元)</td>
                        <td>地址</td>
                        <td>操作</td>
                    </tr>
                    <%
                        T6112[] t6112 = result.getItems();
                        if (t6112 != null) {
                            HTMLFilter htmlFilter = new HTMLFilter(out);
                            int index = 1;
                            for (T6112 t : t6112) {
                    %>
                    <tr>
                        <td><%=index++ %>
                        </td>
                        <td title="<%StringHelper.filterHTML(out, t.F03);%>">
                            <%StringHelper.truncation(htmlFilter, t.F03, 10);%>
                        </td>
                        <td><%=new DecimalFormat("##0.00").format(t.F04) %>
                        </td>
                        <td><%=Formater.formatAmount(t.F06) %>
                        </td>
                        <td title="<%StringHelper.filterHTML(out, t.F09);%>">
                            <%StringHelper.truncation(htmlFilter, t.F09, 15);%>
                        </td>
                        <td>
                            <a href="<%=controller.getURI(request, ViewFcxx.class) %>?id=<%=t.F01 %>"
                               class="blue">查看</a>
                            <a href="<%=controller.getURI(request, UpdateFcxx.class) %>?id=<%=t.F01 %>"
                               class="blue ml5">修改</a>
                        <td>
                    </tr>
                    <%
                            }
                        }
                    %>
                </table>
                <%Fcxx.rendPagingResult(out, result); %>
                <div class="clear"></div>
            </div>
        </form>
        <div class="clear"></div>

    </div>
</div>
<div class="clear"></div>

<script type="text/javascript">

</script>
<%@include file="/WEB-INF/include/footer.jsp" %>
</body>
</html>