<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.console.servlets.AbstractConsoleServlet" %>
<%@page import="com.dimeng.p2p.console.servlets.bid.gyjz.gyyw.*" %>
<%@page import="com.dimeng.p2p.console.servlets.bid.gyjz.jzgl.*" %>
<%@page import="com.dimeng.util.ObjectHelper" %>
<%@ page import="com.dimeng.p2p.S62.enums.T6242_F11" %>
<%@ page import="com.dimeng.p2p.repeater.donation.entity.BidProgres" %>
<%@ page import="com.dimeng.p2p.repeater.donation.entity.GyLoan" %>
<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <%@include file="/WEB-INF/include/script.jsp" %>
</head>
<body>
<%
    CURRENT_CATEGORY = "YWGL";
    CURRENT_SUB_CATEGORY = "GYBD_PROGRES_GL";
    PagingResult<BidProgres> result = ObjectHelper.convert(request.getAttribute("loanProgreList"), PagingResult.class);
    BidProgres[] loans = null == result ? null : result.getItems();
    GyLoan gyLoan = ObjectHelper.convert(request.getAttribute("gyLoan"), GyLoan.class);
    String createTimeStart = request.getParameter("createTimeStart");
    String createTimeEnd = request.getParameter("createTimeEnd");
    T6242_F11[] allStatus = T6242_F11.values();
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <!--加载内容-->
                <form id="form_loan"
                      action="<%=controller.getURI(request, ViewProgres.class)%>?loanId=<%=gyLoan.t6242.F01 %>"
                      method="post">
                    <div class="p20">
                        <div class="border">
                            <div class="title-container">
                              <i class="icon-i w30 h30 va-middle title-left-icon"></i>进展管理
                              <div class="fr mt5">
                                    <input type="button" class="btn btn-blue2 radius-6 pl20 pr20 fr" onclick="location.href='<%=controller.getURI(request, GyLoanProgresList.class) %>'" value="返回">
                                </div>
                            </div>
                            <div class="content-container pl40 pt40 pr40 pb20">
                                <ul class="gray6">
                                    <li class="mb10"><span class="display-ib w200 tr mr5">公益ID：</span><span
                                            class="blue"><%=gyLoan.t6242.F21 %></span>
                                    </li>
                                    <li class="mb10"><span class="display-ib w200 tr mr5">公益标题 ：</span><span
                                            class="blue"><%=gyLoan.t6242.F03 %></span>
                                    </li>
                                    <li class="mb10"><span class="display-ib w200 tr mr5">公益方：</span><span
                                            class="blue"><%=gyLoan.t6242.F22 %></span>
                                    </li>
                                    <li class="mb10"><span class="display-ib w200 tr mr5">捐款金额：</span><span
                                            class="blue"><%=Formater.formatAmount(gyLoan.t6242.F05) %></span>元
                                    </li>
                                    <li class="mb10"><span class="display-ib w200 tr mr5">捐款完成时间：</span><span
                                            class="blue"><%=DateTimeParser.format(gyLoan.t6242.F19) %></span>
                                    </li>
                                </ul>
                            </div>
                        </div>
                        <div class="border mt20">
                            <div class="content-container pl40 pt10 pr40">
                                <ul class="gray6">
                                    <li class="mb10">
                                        <%
                                            if (dimengSession.isAccessableResource(AddProgres.class)) {
                                        %>
                                        <a href="<%=controller.getURI(request, AddProgres.class) %>?loanId=<%=gyLoan.t6242.F01 %>&userId=<%=gyLoan.t6242.F23 %>"
                                           class="btn btn-blue radius-6 mr5 pl1 pr15"><i
                                                class="icon-i w30 h30 va-middle add-icon "></i>新增进展</a>
                                        <%} %>
                                    </li>
                                </ul>
                            </div>
                        </div>
                        <div class="border mt20 table-container">
                            <table class="table table-style gray6 tl">
                                <thead>
                                <tr class="title tc">
                                    <th>序号</th>
                                    <th>标题时间</th>
                                    <th>主题标题</th>
                                    <th>是否发布</th>
                                    <th>发布者</th>
                                    <th>发布时间</th>
                                    <th>操作</th>
                                </tr>
                                </thead>
                                <%
                                    if (loans != null && loans.length != 0) {
                                        int i = 1;
                                        String hideLoanId;
                                        for (BidProgres loan : loans) {

                                %>
                                <tbody class="f12">
                                <tr class="title tc">
                                    <td><%=i++ %>
                                    </td>
                                    <td><%=DateTimeParser.format(loan.F08, "yyyy-MM-dd") %>
                                    </td>
                                    <td title="<%StringHelper.filterHTML(out, loan.F04); %>"><%
                                        StringHelper.filterHTML(out, StringHelper.truncation(loan.F04, 10)); %></td>
                                    <td><%StringHelper.filterHTML(out, loan.F05.getChineseName()); %></td>
                                    <td><%=loan.sysName %>
                                    </td>
                                    <td><%=DateTimeParser.format(loan.F07) %>
                                    </td>

                                    <td class="blue">
                                        <%
                                            if (dimengSession.isAccessableResource(UpdateProgres.class)) {
                                        %>
                                        <span><a
                                                href="<%=controller.getURI(request, UpdateProgres.class) %>?loanId=<%=gyLoan.t6242.F01 %>&userId=<%=gyLoan.t6242.F23 %>&pId=<%=loan.F01 %>"
                                                class="link-blue mr20">修改</a></span>
                                        <%} else { %>
                                        <span class="disabled">修改</span>
                                        <%} %>
                                        <%
                                            if (dimengSession.isAccessableResource(DelProgres.class)) {
                                        %>
                                        <a href="javascript:void(0);"
                                           onclick="delProgres('<%=controller.getURI(request, DelProgres.class) %>?loanId=<%=gyLoan.t6242.F01 %>&userId=<%=gyLoan.t6242.F23 %>&pId=<%=loan.F01 %>');"
                                           class="link-orangered">删除</a>
                                        <%} else { %>
                                        <span class="disabled">删除</span>
                                        <%} %>

                                    </td>
                                </tr>
                                </tbody>
                                <%
                                    }
                                } else {
                                %>
                                <tr class="dhsbg tc">
                                    <td colspan="7">暂无数据</td>
                                </tr>
                                <%} %>
                            </table>
                        </div>
                        <%
                            AbstractConsoleServlet.rendPagingResult(out, result);
                        %>
                    </div>
                </form>
                <!--加载内容 结束-->
            </div>
        </div>
    </div>
    <!--右边内容 结束-->

<%@include file="/WEB-INF/include/jquery.jsp" %>
<%
    String warringMessage = controller.getPrompt(request, response, PromptLevel.WARRING);
    if (!StringHelper.isEmpty(warringMessage)) {
%>
<div class="w440 thickbox thickpos" style="margin:-80px 0 0 -220px;" id="showDiv">
    <div class="info clearfix">
        <div class="clearfix">
            <span class="fl tips"><%StringHelper.filterHTML(out, warringMessage); %></span>
        </div>
        <div class="dialog_btn"><input type="button" name="button2" onclick="$('#showDiv').hide()" value="确认"
                                       class="btn4 ml50"/></div>
    </div>
</div>
<%} %>
<div class="popup_bg hide"></div>
<div id="info"></div>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/validation.js"></script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/dialog.js"></script>
<script type="text/javascript">
    function delProgres(d_url) {
    	$(".popup_bg").show();
		$("#info").html(showConfirmDiv("确定要删除选择的公益标进展信息吗?",d_url,""));
        return false;
    }
    
    function toConfirm(param,type){
		$("#info").html("");
		location.href = param;
	}
</script>
</body>
</html>