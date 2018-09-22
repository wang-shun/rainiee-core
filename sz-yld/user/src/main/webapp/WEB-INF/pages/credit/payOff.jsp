<%@page import="com.dimeng.p2p.S62.entities.T6231" %>
<%@page import="com.dimeng.p2p.S62.enums.T6231_F21" %>
<%@page import="com.dimeng.p2p.modules.bid.user.service.BidManage" %>
<%@page import="com.dimeng.p2p.modules.bid.user.service.WdjkManage" %>
<%@page import="com.dimeng.p2p.modules.bid.user.service.entity.HkEntity" %>
<%@page import="com.dimeng.p2p.modules.bid.user.service.entity.LoanCount" %>
<%@page import="com.dimeng.p2p.user.servlets.credit.PayOff" %>
<%@page import="com.dimeng.p2p.user.servlets.credit.Repaying" %>
<%@include file="/WEB-INF/include/authenticatedSession.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title><%configureProvider.format(out, SystemVariable.SITE_NAME); %>_借款管理_我的借款</title>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
</head>
<body>
<%@include file="/WEB-INF/include/header.jsp" %>
<%
    WdjkManage manage = serviceSession.getService(WdjkManage.class);
    LoanCount loanCount = manage.getMyLoanCount();
    HkEntity[] settleLoans = manage.getYhqJk(null);
    CURRENT_CATEGORY = "JKGL";
    CURRENT_SUB_CATEGORY = "WDJK";
%>
<div class="clear"></div>
<div class="contain clearfix">
    <div class="user_top"></div>
    <div class="about">
        <%@include file="/WEB-INF/include/menu.jsp" %>
        <div class="container fr mb15">
            <div class="user_bgls fl f20">
                <p class="mt40 ml40">
                    借款总金额<br/> <span class="red"><%=Formater.formatAmount(loanCount.countMoney) %>元</span>
                </p>
            </div>
            <div class="hzcx_st fl"></div>
            <div class="fl">
                <ul class="hzcx_li pl40">
                    <li>逾期金额<br><%=Formater.formatAmount(loanCount.overdueMoney) %>元
                    </li>
                    <li>待还金额<br><%=Formater.formatAmount(loanCount.repayMoney) %>元
                    </li>
                    <li>近30天应还金额<br><%=Formater.formatAmount(loanCount.newRepayMoney) %>元
                    </li>
                </ul>
            </div>
        </div>
        <div class="w780 fr">
            <div>
                <div class="newsbox">
                    <div class="til clearfix Men_bt">
                        <div class="Menubox">
                            <ul>
                                <li>
                                    <a href="<%=controller.getViewURI(request, Repaying.class) %>">还款中的借款</a>
                                </li>
                                <li class="hover" style="border-right: 1px #d7dfe3 solid;">
                                    <a style="color: #fff;" href="<%=controller.getViewURI(request, PayOff.class) %>">已还清的借款</a>
                                </li>
                            </ul>
                        </div>
                    </div>
                    <div class="bs_bg pt10">
                        <div class="user_bolr bot" style="display: block;">
                            <table width="100%" border="0" cellspacing="0" cellpadding="0"
                                   class="user_table tc">
                                <tr class="user_lsbg">
                                    <td width="18%">借款标题</td>
                                    <td width="18%">借款金额</td>
                                    <td width="16%">年化利率</td>
                                    <td width="16%">期限</td>
                                    <td width="15%">还款总额</td>
                                    <td width="17%">还清日期</td>
                                </tr>
                                <%
                                    BidManage bidManage = serviceSession.getService(BidManage.class);
                                    if (settleLoans != null) {
                                        for (HkEntity settleLoan : settleLoans) {
                                            if (settleLoan == null) {
                                                continue;
                                            }
                                            T6231 t6231 = bidManage.getExtra(settleLoan.F01);
                                %>
                                <tr>
                                    <td>
                                        <a href="<%=configureProvider.format(URLVariable.FINANCING_SBTZ_XQ)%><%=settleLoan.F01 %><%=rewriter.getViewSuffix() %>">
                                            <%StringHelper.filterHTML(out, settleLoan.F03); %>
                                        </a>
                                    </td>
                                    <td><%=Formater.formatAmount(settleLoan.F26) %>
                                    </td>
                                    <td><%=Formater.formatRate(settleLoan.F06) %>
                                    </td>
                                    <td>
                                        <%if (settleLoan.F28.F21.equals(T6231_F21.F)) { %>
                                        <%=settleLoan.F09 %>个月
                                        <%} else { %>
                                        <%=settleLoan.F28.F22 %>天
                                        <%} %>
                                    </td>
                                    <td><%=Formater.formatAmount(settleLoan.hkTotle) %>元</td>
                                    <td><%=DateParser.format(t6231.F13) %>
                                    </td>
                                </tr>
                                <%
                                    }
                                } else {
                                %>
                                <tr>
                                    <td colspan="7">没有记录</td>
                                </tr>
                                <%} %>
                            </table>
                        </div>
                    </div>
                    <div class="clear"></div>
                </div>
            </div>
        </div>
        <div class="clear"></div>
    </div>
</div>
<div class="clear"></div>
<%@include file="/WEB-INF/include/footer.jsp" %>
</body>
</html>