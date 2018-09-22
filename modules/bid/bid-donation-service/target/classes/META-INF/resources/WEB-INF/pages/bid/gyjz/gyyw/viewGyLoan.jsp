<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.S61.enums.T6110_F06" %>
<%@page import="com.dimeng.p2p.console.servlets.bid.gyjz.gyyw.*" %>
<%@page import="com.dimeng.p2p.console.servlets.bid.gyjz.jzgl.*" %>
<%@page import="com.dimeng.util.ObjectHelper" %>
<%@ page import="com.dimeng.p2p.S62.entities.T6242" %>
<%@ page import="com.dimeng.p2p.S62.enums.T6242_F11" %>
<%@ page import="com.dimeng.p2p.repeater.donation.entity.GyLoan" %>
<%@ page import="com.dimeng.p2p.repeater.donation.entity.GyBidCheck" %>
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
    T6242 t6242 = ObjectHelper.convert(request.getAttribute("loan"), T6242.class);
    T6110_F06 userType = EnumParser.parse(T6110_F06.class, request.getAttribute("userType").toString());

    //审核记录
    GyBidCheck[] bidChecks = ObjectHelper.convertArray(request.getAttribute("bidChecks"), GyBidCheck.class);
    String userName = ObjectHelper.convert(request.getAttribute("userName"), String.class);
    if (t6242 == null) {
        response.sendError(HttpServletResponse.SC_NOT_FOUND);
    }
    int loanId = IntegerParser.parse(request.getParameter("loanId"));
    int userId = t6242.F23;
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
                           class="btn btn-blue2 radius-6 pl20 pr20 mr10" style="float: right;margin-top: 5px;">返回</a>

                        <div class="tabnav-container">
                            <ul class="clearfix">
                                <li><a href="javascript:void(0);" class="tab-btn select-a">项目信息<i
                                        class="icon-i tab-arrowtop-icon"></i></a></li>

                                <li>
                                    <a href="<%=controller.getURI(request, ViewGyLoanCys.class)%>?loanId=<%=loanId%>&userId=<%=userId%>&enterType=<%=enterType%>"
                                       class="tab-btn ">倡议书</a></li>
                                <%if (null != t6242 && (t6242.F11 == T6242_F11.DFB || t6242.F11 == T6242_F11.JKZ || t6242.F11 == T6242_F11.YJZ)) { %>

                                <li>
                                    <a href="<%=controller.getURI(request, GyLoanProgres.class)%>?loanId=<%=loanId%>&userId=<%=userId%>&enterType=<%=enterType%>"
                                       class="tab-btn ">最新进展</a></li>
                                <li>
                                    <a href="<%=controller.getURI(request, GyLoanJzjl.class)%>?loanId=<%=loanId%>&userId=<%=userId%>&enterType=<%=enterType%>"
                                       class="tab-btn ">捐助记录</a></li>
                                <%} %>
                            </ul>
                        </div>
                        <div class="content-container pl40 pt40 pr40 pb20" id="con_one_1">
                            <ul class="gray6">
                                <li class="mb10"><span class="display-ib w200 tr mr5">
												<span class="red">*</span>公益标题：</span>
                                    <%StringHelper.filterHTML(out, t6242.F03); %>
                                </li>
                                <li class="mb10">
											<span class="display-ib w200 tr mr5">
												<span class="red">*</span>公益金额：
											</span>
                                    <%=Formater.formatAmount(t6242.F05) %>元
                                </li>
                                <li class="mb10">
											<span class="display-ib w200 tr mr5">
												<span class="red">*</span>公益机构：
											</span>
                                    <%StringHelper.filterHTML(out, t6242.F22); %>
                                </li>

                                <li class="mb10">
											<span class="display-ib w200 tr mr5">
												<span class="red">*</span>最低起捐： <br/>
											</span>
                                    <%=Formater.formatAmount(t6242.F06) %>元
                                </li>
                                <li class="mb10">
											<span class="display-ib w200 tr mr5">
												<span class="red">*</span>筹款期限：
											</span>
                                    <%out.print(t6242.F08); %>天
                                </li>
                                <li class="mb10">
											<span class="display-ib w200 tr mr5">
												<span class="red">*</span>项目简介：
											</span>
                                    <%StringHelper.filterHTML(out, t6242.F24); %>
                                </li>
                            </ul>
                        </div>
                        <div class="mt20 table-container p10">
                            <table class="table table-style gray6 tl">
                                <thead>
                                <tr class="title tc">
                                    <th>序号</th>
                                    <th>审核人</th>
                                    <th>反馈时间</th>
                                    <th>状态</th>
                                    <th>审核意见</th>
                                </tr>
                                </thead>
                                <tbody class="f12">
                                <%
                                    if (bidChecks != null) {
                                        int i = 1;
                                        for (GyBidCheck bidCheck : bidChecks) {
                                            if (bidCheck == null) {
                                                continue;
                                            }
                                %>
                                <tr class="title tc">
                                    <td><%=i++%>
                                    </td>
                                    <td><%=bidCheck.name%>
                                    </td>
                                    <td>
                                        <%=DateTimeParser.format(bidCheck.F04)
                                        %>
                                    </td>
                                    <td><%=bidCheck.F05.getChineseName() %>
                                    </td>
                                    <td><%StringHelper.filterHTML(out, bidCheck.F06); %>
                                    </td>
                                </tr>
                                <%
                                    }
                                } else {
                                %>
                                <tr class="tc">
                                    <td colspan="5">暂无数据</td>
                                </tr>
                                <%} %>
                                </tbody>
                            </table>
                        </div>
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