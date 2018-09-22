<%@page import="com.dimeng.p2p.S62.enums.T6231_F21" %>
<%@page import="com.dimeng.p2p.modules.bid.user.service.entity.Tbzdzq" %>
<%@page import="com.dimeng.p2p.user.servlets.financing.AbstractFinancingServlet" %>
<%@include file="/WEB-INF/include/authenticatedSession.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>我的债权-<%configureProvider.format(out, SystemVariable.SITE_NAME); %></title>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
</head>
<%
    CURRENT_CATEGORY = "LCGL";
    CURRENT_SUB_CATEGORY = "WDZQ";
%>
<body>
<%@include file="/WEB-INF/include/header.jsp" %>
<div class="contain clearfix">
    <div class="user_top"></div>
    <div class="about">
        <%@include file="/WEB-INF/include/menu.jsp" %>
        <%@include file="/WEB-INF/include/wdzq/header.jsp" %>
        <div class="w780 fr">
            <div>
                <div class="newsbox">
                    <div class="til clearfix Men_bt">
                        <div class="Menubox">
                            <ul>
                                <li>
                                    <a href="<%=controller.getViewURI(request, com.dimeng.p2p.user.servlets.financing.wdzq.Hszdzq.class)%>">回款中的债权</a>
                                </li>
                                <li>
                                    <a href="<%=controller.getViewURI(request, com.dimeng.p2p.user.servlets.financing.wdzq.Yjqdzq.class)%>">已结清的债权</a>
                                </li>
                                <li style="border-right:1px #d7dfe3 solid;" class="hover">投资中的债权</li>
                            </ul>
                        </div>
                    </div>
                    <form action="<%=controller.getViewURI(request, com.dimeng.p2p.user.servlets.financing.wdzq.Tbzdzq.class)%>"
                          method="post">
                        <div class="bs_bg pt10">
                            <div class="no_table user_bolr bot">
                                <table width="100%" border="0" cellspacing="0" cellpadding="0" class="user_table tc">
                                    <tr class="user_lsbg">
                                        <td>序号</td>
                                        <td>标编号</td>
                                        <td>原始投资金额</td>
                                        <td>年化利率</td>
                                        <td>期限</td>
                                        <td>剩余时间</td>
                                        <td>投资进度</td>
                                        <!-- <td>操作</td> -->
                                    </tr>
                                    <%
                                        PagingResult<Tbzdzq> laList = service.getLoanAssests(paging);
                                        if (laList != null && laList.getItemCount() > 0) {
                                            int i = 1;
                                            for (Tbzdzq lAssests : laList.getItems()) {
                                                if (lAssests == null) {
                                                    continue;
                                                }
                                    %>
                                    <tr>
                                        <td><%=i++%>
                                        </td>
                                        <td><a class="blue"
                                               href="<%configureProvider.format(out, URLVariable.FINANCING_SBTZ_XQ); %><%=lAssests.F15+rewriter.getViewSuffix()%>"><%=lAssests.F16  %>
                                        </a></td>
                                        <td><%=Formater.formatAmount(lAssests.F13)%>元</td>
                                        <td><%=Formater.formatRate(lAssests.F05)%>
                                        </td>
                                        <td><%
                                            if (T6231_F21.S == lAssests.F21) {
                                                out.print(lAssests.F22 + "天");
                                            } else {
                                                out.print(lAssests.F08 + "个月");
                                            }
                                        %></td>
                                        <td><%StringHelper.filterHTML(out, lAssests.surTime);%></td>
                                        <td><%=Formater.formatProgress((lAssests.F04.doubleValue() - lAssests.F06.doubleValue()) / lAssests.F04.doubleValue()) %>
                                        </td>
                                        <!-- <td>取消</td> -->
                                    </tr>
                                    <%
                                            }
                                        }
                                    %>
                                </table>
                            </div>
                            <div class="page"><%AbstractFinancingServlet.rendPagingResult(out, laList); %></div>
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
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/wdzq.js"></script>
</body>
</html>