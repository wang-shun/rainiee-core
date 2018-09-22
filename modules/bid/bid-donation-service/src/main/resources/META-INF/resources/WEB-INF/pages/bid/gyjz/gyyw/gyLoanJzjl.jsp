<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.console.servlets.bid.gyjz.gyyw.*" %>
<%@page import="com.dimeng.p2p.console.servlets.bid.gyjz.jzgl.*" %>
<%@page import="com.dimeng.util.ObjectHelper" %>
<%@ page import="com.dimeng.p2p.S62.enums.T6242_F11" %>
<%@ page import="com.dimeng.p2p.repeater.donation.entity.GyLoan" %>
<%@ page import="com.dimeng.p2p.repeater.donation.entity.Donation" %>
<%@ page import="com.dimeng.p2p.repeater.donation.entity.GyLoanStatis" %>
<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <%@include file="/WEB-INF/include/script.jsp" %>
</head>
<body>
<%
    CURRENT_CATEGORY = "YWGL";
    CURRENT_SUB_CATEGORY = "GYBDGL";
    GyLoan loan = ObjectHelper.convert(request.getAttribute("loan"), GyLoan.class);
    PagingResult<Donation> donations = ObjectHelper.convert(request.getAttribute("loanList"), PagingResult.class);
    Donation[] donationList = null == donations ? null : donations.getItems();
    int loanId = IntegerParser.parse(request.getParameter("loanId"));
    int userId = IntegerParser.parse(request.getParameter("userId"));
    GyLoanStatis statis = ObjectHelper.convert(request.getAttribute("statis"), GyLoanStatis.class);
    if (null == statis) {
        statis = new GyLoanStatis();
    }
    String enterType = request.getParameter("enterType");
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <!--加载内容-->
                <div class="p20">
                    <div class="border">
                        <div class="title-container"><i class="icon-i w30 h30 va-middle title-left-icon"></i>查看公益标信息
                        </div>
                    </div>
                    <div class="border mt20">
                        <a href="<%="jzgl".equals(enterType)?controller.getURI(request, GyLoanProgresList.class) : controller.getURI(request, GyLoanList.class)%>"
                           class="btn btn-blue2 radius-6 pl20 pr20 mr10" style="float: right;margin-top: 7px;">返回</a>

                        <div class="tabnav-container">
                            <ul class="clearfix">
                                <li>
                                    <a href="<%=controller.getURI(request, ViewGyLoan.class)%>?loanId=<%=loanId%>&userId=<%=userId%>&enterType=<%=enterType%>"
                                       class="tab-btn ">项目信息</a></li>
                                <li>
                                    <a href="<%=controller.getURI(request, ViewGyLoanCys.class)%>?loanId=<%=loanId%>&userId=<%=userId%>&enterType=<%=enterType%>"
                                       class="tab-btn ">倡议书</a></li>
                                <%if (null != loan && (loan.t6242.F11 == T6242_F11.DFB || loan.t6242.F11 == T6242_F11.JKZ || loan.t6242.F11 == T6242_F11.YJZ)) { %>

                                <li>
                                    <a href="<%=controller.getURI(request, GyLoanProgres.class)%>?loanId=<%=loanId%>&userId=<%=userId%>&enterType=<%=enterType%>"
                                       class="tab-btn ">最新进展</a></li>
                                <li>
                                    <a href="<%=controller.getURI(request, GyLoanJzjl.class)%>?loanId=<%=loanId%>&userId=<%=userId%>&enterType=<%=enterType%>"
                                       class="tab-btn select-a">捐助记录<i class="icon-i tab-arrowtop-icon"></i></a></li>
                                <%} %>
                            </ul>
                        </div>
                        <div class="content-container pl40 pt30 pr40">
                            <ul class="gray6 input-list-container clearfix">
                                <li>
                                    <div class="info">
                                        捐款加入人数<span style="color:#fc8936;"> <%=statis.totalNum %> </span>人,已捐助金额 <span
                                            style="color:#fc8936;"><%=statis.donationsAmount %></span>元
                                    </div>

                                </li>
                            </ul>
                        </div>
                        <form action="<%=controller.getURI(request, GyLoanJzjl.class)%>" method="post">
                            <input type="hidden" name="loanId" value="<%=loanId%>">
                            <input type="hidden" name="userId" value="<%=userId%>">

                            <div class="table-container p10">
                                <table width="100%" border="0" cellspacing="0" cellpadding="3"
                                       class="table table-style gray6 tl">
                                    <thead>
                                    <tr class="title tc">
                                        <th>序号</th>
                                        <th>爱心用户</th>
                                        <th>捐助金额(元)</th>
                                        <th>捐助时间</th>
                                    </tr>
                                    </thead>

                                    <%
                                        if (donationList != null && donationList.length != 0) {
                                            int i = 1;
                                            for (Donation donation : donationList) {
                                                if (donation == null) {
                                                    continue;
                                                }
                                    %>
                                    <tbody class="f12">
                                    <tr class="title tc">
                                        <td><%=i++%>
                                        </td>
                                        <td><%=donation.userName%>
                                        </td>
                                        <td><%=Formater.formatAmount(donation.F04)%>
                                        </td>
                                        <td>
                                            <%=DateTimeParser.format(donation.F06)
                                            %>
                                        </td>
                                    </tr>
                                    </tbody>
                                    <%
                                        }
                                    } else {
                                    %>
                                    <tr class="dhsbg tc">
                                        <td colspan="4">暂无数据</td>
                                    </tr>
                                    <%} %>
                                </table>
                            </div>
                            <%
                                GyLoanJzjl.rendPagingResult(out, donations);
                            %>
                        </form>
                    </div>
                </div>
                <!--加载内容 结束-->
            </div>
        </div>
    </div>
    <!--右边内容 结束-->
<!--内容-->
</body>
</html>