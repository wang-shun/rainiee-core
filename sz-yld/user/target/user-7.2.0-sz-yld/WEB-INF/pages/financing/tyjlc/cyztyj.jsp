<%@page import="com.dimeng.p2p.user.servlets.financing.AbstractFinancingServlet" %>
<%@page import="com.dimeng.p2p.account.user.service.entity.MyExperience" %>
<%@include file="/WEB-INF/include/authenticatedSession.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>体验金理财-<%configureProvider.format(out, SystemVariable.SITE_NAME); %></title>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
</head>
<%
    CURRENT_CATEGORY = "LCGL";
    CURRENT_SUB_CATEGORY = "TYJLC";
%>
<body>
<%@include file="/WEB-INF/include/header.jsp" %>
<div class="contain clearfix">
    <div class="user_top"></div>
    <div class="about">
        <%@include file="/WEB-INF/include/menu.jsp" %>

        <%@include file="/WEB-INF/include/tyjlc/header.jsp" %>
        <div class="w780 fr">
            <div>
                <div class="newsbox">
                    <div class="til clearfix Men_bt">
                        <div class="Menubox">
                            <ul>
                                <li>
                                    <a href="<%=controller.getViewURI(request, com.dimeng.p2p.user.servlets.financing.tyjlc.Index.class)%>">申请中的体验金投资</a>
                                </li>

                                <li class="hover">持有中的体验金投资</li>
                                <li style="border-right:1px #d7dfe3 solid;"><a
                                        href="<%=controller.getViewURI(request, com.dimeng.p2p.user.servlets.financing.tyjlc.Yjztyj.class)%>">已截止的体验金投资</a>
                                </li>


                            </ul>
                        </div>
                    </div>
                    <form action="<%=controller.getViewURI(request, com.dimeng.p2p.user.servlets.financing.tyjlc.Cyztyj.class)%>"
                          method="post">
                        <div class="bs_bg pt10">
                            <div class="no_table user_bolr bot">
                                <table width="100%" border="0" cellspacing="0" cellpadding="0" class="user_table tc">
                                    <tr class="user_lsbg">
                                        <td>序号</td>
                                        <td>借款标题</td>
                                        <td>体验金加入金额</td>
                                        <td>年化利率</td>
                                        <td>待赚金额</td>
                                        <td>已赚金额</td>
                                        <td>下个还款日</td>
                                    </tr>
                                    <%
                                        PagingResult<MyExperience> raList = service.searMyExperience("CYZ", paging);
                                        int index = 1;
                                        if (raList != null && raList.getItemCount() > 0) {
                                            for (MyExperience myExperience : raList.getItems()) {

                                                if (myExperience == null) {
                                                    continue;
                                                }
                                    %>
                                    <tr>
                                        <td><%=index++ %>
                                        </td>
                                        <td><%=myExperience.F02%>
                                        </td>
                                        <td><%=Formater.formatAmount(myExperience.F03) %>
                                        </td>
                                        <td><%=Formater.formatRate(myExperience.F04) %>
                                        </td>
                                        <td><%=Formater.formatAmount(myExperience.F08) %>
                                        </td>
                                        <td><%=Formater.formatAmount(myExperience.F05) %>
                                        </td>
                                        <td><%=Formater.formatDate(myExperience.F09)%>
                                        </td>
                                    </tr>
                                    <%
                                            }
                                        }
                                    %>
                                </table>
                            </div>
                            <div class="page">
                                <%AbstractFinancingServlet.rendPagingResult(out, raList); %>
                            </div>
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
</body>
</html>