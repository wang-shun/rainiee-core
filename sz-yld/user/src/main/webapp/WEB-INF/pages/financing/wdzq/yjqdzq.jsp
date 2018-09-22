<%@page import="com.dimeng.p2p.modules.bid.user.service.entity.ZqxxEntity" %>
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
    Boolean isBadClaimTransfer = Boolean.parseBoolean(configureProvider.getProperty(BadClaimVariavle.IS_BADCLAIM_TRANSFER));
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
                                <li class="hover">已结清的债权</li>
                                <li style="border-right:1px #d7dfe3 solid;"><a
                                        href="<%=controller.getViewURI(request, com.dimeng.p2p.user.servlets.financing.wdzq.Tbzdzq.class)%>">投资中的债权</a>
                                </li>

                            </ul>
                        </div>
                    </div>
                    <form action="<%=controller.getViewURI(request, com.dimeng.p2p.user.servlets.financing.wdzq.Yjqdzq.class)%>"
                          method="post">
                        <div class="bs_bg pt10">
                            <div class="no_table user_bolr bot">
                                <table width="100%" border="0" cellspacing="0" cellpadding="0" class="user_table tc">
                                    <tr class="user_lsbg">
                                        <td>债权ID</td>
                                        <td>投资金额</td>
                                        <td>年化利率</td>
                                        <!--  <td>回收金额</td> -->
                                        <td>已赚金额</td>
                                        <td>结清日期</td>
                                        <td>结清方式</td>
                                        <td>&nbsp;</td>
                                    </tr>
                                    <%
                                        PagingResult<ZqxxEntity> saList = service.getSettleAssests(paging);
                                        if (saList != null && saList.getItemCount() > 0) {
                                            for (ZqxxEntity sAssests : saList.getItems()) {
                                                if (sAssests == null) {
                                                    continue;
                                                }
                                    %>
                                    <tr>
                                        <td><a class="blue"
                                               href="<%configureProvider.format(out, URLVariable.FINANCING_SBTZ_XQ); %><%=sAssests.F02+rewriter.getViewSuffix()%>"><%=sAssests.F01 %>
                                        </a></td>
                                        <td><%=Formater.formatAmount(sAssests.F04) %>元</td>
                                        <td><%=Formater.formatRate(sAssests.F14)%>
                                        </td>
                                        <%-- <td><%=Formater.formatAmount(sAssests.hsje) %>元</td> --%>
                                        <td><%=Formater.formatAmount(sAssests.yzje) %>元</td>
                                        <td><%=TimestampParser.format(sAssests.jqsj, "yyyy-MM-dd") %>
                                        </td>
                                        <td><%StringHelper.filterHTML(out, sAssests.F28.getChineseName());%></td>
                                        <td>
                                            <%if (sAssests.zqzrOrderId <= 0) { %>
                                            <a target="_blank"
                                               href="<%configureProvider.format(out, URLVariable.XY_PTDZXY); %>?id=<%=sAssests.F02 %>"
                                               class="blue">合同</a>
                                            <%} %>
                                            <%if (sAssests.blzqzrId > 0 && isBadClaimTransfer) { %>
                                            <a target="_blank"
                                               href="<%configureProvider.format(out, URLVariable.USER_BLZQZR_URL); %>?id=<%=sAssests.blzqzrId %>&zqId=<%=sAssests.zqid %>"
                                               class="blue">转让合同</a>
                                            <%} %>
                                        </td>
                                    </tr>
                                    <%
                                            }
                                        }
                                    %>
                                </table>
                            </div>
                            <div class="page">
                                <%AbstractFinancingServlet.rendPagingResult(out, saList); %>
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
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/wdzq.js"></script>
</body>
</html>