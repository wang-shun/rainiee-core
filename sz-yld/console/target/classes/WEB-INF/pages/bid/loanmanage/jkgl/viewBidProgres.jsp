<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.console.servlets.bid.loanmanage.jkgl.DelBidProgres"%>
<%@page import="com.dimeng.p2p.console.servlets.bid.loanmanage.jkgl.UpdateBidProgres"%>
<%@page import="com.dimeng.p2p.console.servlets.AbstractConsoleServlet" %>
<%@page import="com.dimeng.p2p.console.servlets.bid.gyjz.*" %>
<%@page import="com.dimeng.util.ObjectHelper" %>
<%@ page import="com.dimeng.p2p.console.servlets.bid.loanmanage.jkgl.ViewBidProgres" %>
<%@ page import="com.dimeng.p2p.S62.entities.T6248" %>
<%@ page import="java.util.Map" %>
<%@ page import="com.dimeng.p2p.console.servlets.bid.loanmanage.jkgl.LoanList" %>
<%@ page import="com.dimeng.p2p.console.servlets.bid.loanmanage.jkgl.AddBidProgres" %>
<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <%@include file="/WEB-INF/include/script.jsp" %>
</head>
<body>
<%
    CURRENT_CATEGORY = "YWGL";
    CURRENT_SUB_CATEGORY = "BDGL";
    PagingResult<T6248> result = ObjectHelper.convert(request.getAttribute("t6248List"), PagingResult.class);
    T6248[] loans = null == result ? null : result.getItems();
    Map<String,String> infoMap = (Map<String,String>)request.getAttribute("infoMap");
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <!--加载内容-->
                <form id="form_loan"
                      action="<%=controller.getURI(request, ViewBidProgres.class)%>?loanId=<%=infoMap.get("loanId") %>"
                      method="post">
                    <div class="p20">
                        <div class="border">
                            <div class="title-container">
                              <i class="icon-i w30 h30 va-middle title-left-icon"></i>借款管理-动态管理
                              <div class="fr mt5">
                                    <input type="button" class="btn btn-blue2 radius-6 pl20 pr20 fr" onclick="location.href='<%=controller.getURI(request, LoanList.class) %>'" value="返回">
                                </div>
                            </div>
                            <div class="content-container pl40 pt40 pr40 pb20">
                                <ul class="gray6">
                                    <li class="mb10"><span class="display-ib w200 tr mr5">标的ID：</span><span
                                            class="blue"><%=infoMap.get("loanNumber") %></span>
                                    </li>
                                    <li class="mb10"><span class="display-ib w200 tr mr5">借款标题 ：</span><span
                                            class="blue"><%=infoMap.get("loanTitle") %></span>
                                    </li>
                                    <li class="mb10"><span class="display-ib w200 tr mr5">借款账号：</span><span
                                            class="blue"><%=infoMap.get("loanUser") %></span>
                                    </li>
                                    <li class="mb10"><span class="display-ib w200 tr mr5">借款金额：</span><span
                                            class="blue"><%=infoMap.get("loanAmount") %></span>元
                                    </li>
                                </ul>
                            </div>
                        </div>
                        <div class="border mt20">
                            <div class="content-container pl40 pt10 pr40">
                                <ul class="gray6">
                                    <li class="mb10">
                                        <%
                                            if (dimengSession.isAccessableResource(AddBidProgres.class)) {
                                        %>
                                        <a href="<%=controller.getURI(request, AddBidProgres.class) %>?loanId=<%=infoMap.get("loanId") %>"
                                           class="btn btn-blue radius-6 mr5 pl1 pr15"><i
                                                class="icon-i w30 h30 va-middle add-icon "></i>新增动态</a>
                                        <%} else {
                                        %>
                                            <input type="button" class="disabled" value="确认"/>
                                        <%
                                            }
                                        %>
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
                                        for (T6248 loan : loans) {

                                %>
                                <tbody class="f12">
                                <tr class="title tc">
                                    <td><%=i++ %></td>
                                    <td><%=DateTimeParser.format(loan.F08, "yyyy-MM-dd") %></td>
                                    <td title="<%StringHelper.filterHTML(out, loan.F04); %>">
                                        <%StringHelper.filterHTML(out, StringHelper.truncation(loan.F04, 10)); %>
                                    </td>
                                    <td><%StringHelper.filterHTML(out, loan.F05.getChineseName()); %></td>
                                    <td><%= loan.sysName%></td>
                                    <td><%=DateTimeParser.format(loan.F07) %></td>
                                    <td class="blue">
                                        <%
                                            if (dimengSession.isAccessableResource(UpdateBidProgres.class)) {
                                        %>
                                            <span><a href="<%=controller.getURI(request, UpdateBidProgres.class) %>?loanId=<%=infoMap.get("loanId")%>&pId=<%=loan.F01%>"
                                                    class="link-blue mr20">修改</a>
                                            </span>
                                        <%} else { %>
                                            <span class="disabled">修改</span>
                                        <%} %>
                                        <%
                                            if (dimengSession.isAccessableResource(DelBidProgres.class)) {
                                        %>
                                            <a href="javascript:void(0);"
                                               onclick="delProgres('<%=controller.getURI(request, DelBidProgres.class) %>?loanId=<%=infoMap.get("loanId")%>&pId=<%=loan.F01%>');"
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
		$("#info").html(showConfirmDiv("确定要删除此动态信息吗?",d_url,""));
        return false;
    }
    
    function toConfirm(param,type){
		$("#info").html("");
		location.href = param;
	}
</script>
</body>
</html>