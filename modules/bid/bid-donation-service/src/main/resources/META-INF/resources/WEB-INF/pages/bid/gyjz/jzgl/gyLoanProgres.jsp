<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.console.servlets.bid.gyjz.jzgl.*" %>
<%@page import="com.dimeng.p2p.console.servlets.bid.gyjz.gyyw.*" %>
<%@page import="com.dimeng.util.ObjectHelper" %>
<%@ page import="com.dimeng.p2p.S62.enums.T6242_F11" %>
<%@ page import="com.dimeng.p2p.repeater.donation.entity.GyLoan" %>
<%@ page import="com.dimeng.p2p.repeater.donation.entity.BidProgres" %>
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
    PagingResult<BidProgres> loanList = ObjectHelper.convert(request.getAttribute("loanList"), PagingResult.class);
    BidProgres[] progresList = null == loanList ? null : loanList.getItems();
    int loanId = IntegerParser.parse(request.getParameter("loanId"));
    int userId = IntegerParser.parse(request.getParameter("userId"));
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
                                       class="tab-btn select-a">最新进展<i class="icon-i tab-arrowtop-icon"></i></a></li>
                                <li>
                                    <a href="<%=controller.getURI(request, GyLoanJzjl.class)%>?loanId=<%=loanId%>&userId=<%=userId%>&enterType=<%=enterType%>"
                                       class="tab-btn ">捐助记录</a></li>
                                <%} %>
                            </ul>
                        </div>
                        <%--
                        <div class="border p30 Men_bt no_table yw_dl" id="con_one_1">
                                <div class="mb20">
                                    <div class="yw_jcxx">
                                        <ul class="cell noborder">
                                            <li>
                                                <div class="til">
                                                    <span class="red">*</span>公益标题：
                                                </div>
                                                <div class="info"></div>
                                                <div class="info">
                                                    <%StringHelper.filterHTML(out, t6242.F03); %>
                                                </div>
                                                <div class="clear"></div>
                                            </li>


                                            <li>
                                                <div class="til">
                                                    <span class="red">*</span>公益金额： <br />
                                                </div>
                                                <div class="info">
                                                    <%=Formater.formatAmount(t6242.F05) %>元
                                                </div>
                                                <div class="clear"></div>
                                            </li>

                                        </ul>
                                    </div>
                                    <div class="clear"></div>
                                </div>
                                <div class="clear"></div>
                            </div>
                            --%>
                        <form action="<%=controller.getURI(request, GyLoanProgres.class)%>" method="post">
                        	<input type="hidden" name="loanId" value="<%=loanId%>">
                            <input type="hidden" name="userId" value="<%=userId%>">
	                        <div class="mt20 table-container p10">
	                            <table class="table table-style gray6 tl">
	                                <thead>
	                                <tr class="title tc">
	                                    <th>序号</th>
	                                    <th>标题时间</th>
	                                    <th>主题标题</th>
	                                    <th>状态</th>
	                                    <th>发布者</th>
	                                    <th>发布时间</th>
	                                </tr>
	                                </thead>
	                                <%
	                                    if (progresList != null && progresList.length != 0) {
	                                        int i = 1;
	                                        for (BidProgres progres : progresList) {
	                                            if (progres == null) {
	                                                continue;
	                                            }
	                                %>
	                                <tbody class="f12">
	                                <tr class="title tc">
	                                    <td><%=i++%>
	                                    </td>
	                                    <td><%=DateTimeParser.format(progres.F08, "yyyy-MM-dd") %>
	                                    </td>
	                                    <td>
	                                        <%StringHelper.filterHTML(out, progres.F04); %>
	                                    </td>
	                                    <td><%=progres.F05.getChineseName() %>
	                                    </td>
	                                    <td><%=progres.sysName %>
	                                    </td>
	                                    <td><%=DateTimeParser.format(progres.F07)%>
	                                    </td>
	                                </tr>
	                                </tbody>
	                                <%
	                                    }
	                                } else {
	                                %>
	                                <tr class="dhsbg tc">
	                                    <td colspan="6">暂无数据</td>
	                                </tr>
	                                <%} %>
	                            </table>
	                        </div>
	                        <%
	                        	GyLoanProgres.rendPagingResult(out, loanList);
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