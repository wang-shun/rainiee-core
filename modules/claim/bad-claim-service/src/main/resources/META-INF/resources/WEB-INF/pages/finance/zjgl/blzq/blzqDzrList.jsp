<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="com.dimeng.p2p.console.servlets.AbstractConsoleServlet" %>
<%@page import="com.dimeng.p2p.repeater.claim.entity.BadClaimZr" %>
<%@page import="com.dimeng.p2p.console.servlets.finance.zjgl.blzq.BlzqDzrList" %>
<%@page import="com.dimeng.p2p.console.servlets.finance.zjgl.blzq.BlzqDzrTransfer" %>
<%@page import="com.dimeng.p2p.console.servlets.finance.zjgl.blzq.BlzqDshList" %>
<%@page import="com.dimeng.p2p.console.servlets.finance.zjgl.blzq.BlzqZrzList" %>
<%@page import="com.dimeng.p2p.console.servlets.finance.zjgl.blzq.BlzqZrsbList" %>
<%@page import="com.dimeng.p2p.console.servlets.finance.zjgl.blzq.BlzqYzrList" %>
<%@page import="com.dimeng.p2p.console.servlets.finance.zjgl.blzq.ExportBlzqDzr" %>
<html dir="ltr" xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN">
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <%@include file="/WEB-INF/include/script.jsp"%>
</head>

<body>
<%
    CURRENT_CATEGORY = "CWGL";
    CURRENT_SUB_CATEGORY = "BLZQZRGLLIST";
    PagingResult<BadClaimZr> badClaimZrs = (PagingResult<BadClaimZr>) request.getAttribute("badClaimDzrPagingResult");
    BadClaimZr[] badClaimZrArray = null;
    if(badClaimZrs != null){
        badClaimZrArray = badClaimZrs.getItems();
    }
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <form id="form1" name="form1" action="<%=controller.getURI(request, BlzqDzrList.class)%>" method="post">
                    <div class="p20">
                        <div class="border">
                            <div class="title-container"><i class="icon-i w30 h30 va-middle title-left-icon"></i>待转让
                            </div>
                            <div class="content-container pl40 pt30 pr40">
                                <ul class="gray6 input-list-container clearfix">
                                    <li><span class="display-ib mr5">借款编号： </span>
                                        <input type="text" name="bidNo"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("bidNo"));%>"
                                               class="text border pl5 mr20"/>
                                    </li>
                                    <li><span class="display-ib mr5">借款标题： </span>
                                        <input type="text" name="loanTitle"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("loanTitle"));%>"
                                               class="text border pl5 mr20"/>
                                    </li>
                                    <li><span class="display-ib mr5">借款账户： </span>
                                        <input type="text" name="loanName"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("loanName"));%>"
                                               class="text border pl5 mr20"/>
                                    </li>
                                    <li><span class="display-ib mr5">逾期天数： </span>
                                        <input type="text" name="yuqiFromDays" onkeyup="value=value.replace(/\D/g,'')"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("yuqiFromDays"));%>"
                                               class="text border pl5 w120" onblur="checkStock();" id="yuqiMin"/>
                                        <span class="pl5 pr5">至</span>
                                        <input type="text" name="yuqiEndDays" onkeyup="value=value.replace(/\D/g,'')"
                                        value="<%StringHelper.filterHTML(out, request.getParameter("yuqiEndDays"));%>"
                                        class="text border pl5 w120 mr20" onblur="checkStock();" id="yuqiMax"/>
                                    </li>
                                    <li><span class="display-ib mr5">标的属性： </span>
                                        <select name="loanAttribute" class="border mr20 h32 mw100">
                                            <option value="">全部</option>
                                            <option value="dbb"
                                                    <%if("dbb".equals(request.getParameter("loanAttribute"))){ %>selected="selected"<%} %>>担保标</option>
                                            <option value="dyb"
                                                    <%if("dyb".equals(request.getParameter("loanAttribute"))){ %>selected="selected"<%} %>>抵押标</option>
                                            <option value="sdb"
                                                    <%if("sdb".equals(request.getParameter("loanAttribute"))){ %>selected="selected"<%} %>>实地标</option>
                                            <option value="xyb"
                                                    <%if("xyb".equals(request.getParameter("loanAttribute"))){ %>selected="selected"<%} %>>信用标</option>
                                        </select>
                                    </li>
                                    <li><a href="javascript:$('#form1').submit();" 
                                           class="btn btn-blue radius-6 mr5 pl1 pr15"><i
                                            class="icon-i w30 h30 va-middle search-icon "></i>搜索</a></li>
                                     <li>
                                        <%if (dimengSession.isAccessableResource(ExportBlzqDzr.class)) {%>
                                        <a href="javascript:void(0);" onclick="showExport();"
                                           class="btn btn-blue radius-6 mr5  pl1 pr15"><i
                                                class="icon-i w30 h30 va-middle export-icon "></i>导出</a>
                                        <%} else {%>
                                        <span class="btn btn-gray radius-6 mr5 pl1 pr15"><i
                                                class="icon-i w30 h30 va-middle export-icon "></i>导出</span>
                                        <%}%>
                                    </li>
                                </ul>
                            </div>
                        </div>
                        <div class="border mt20 table-container">
                                <table class="table table-style gray6 tl">
                                    <thead>
                                    <tr class="title tc">
                                        <th>序号</th>
                                        <th>借款编号</th>
                                        <th>标的属性</th>
                                        <th>借款标题</th>
                                        <th>借款账户</th>
                                        <th>借款金额（元）</th>
                                        <th>还款方式</th>
                                        <th>年化利率</th>
                                        <th>期数</th>
                                        <th>逾期时间</th>
                                        <th>逾期天数</th>
                                        <th>债权价值（元）</th>
                                        <th>应还金额（元）</th>
                                        <th>待还金额（元）</th>
                                        <th>操作</th>
                                    </tr>
                                    </thead>
                                    <tbody class="f12">
                                    <%
                                        if (badClaimZrArray != null && badClaimZrArray.length > 0) {
                                            int i = 1;
                                            for (BadClaimZr badClaimZr : badClaimZrArray) {
                                    %>
                                    <tr class="tc">
                                        <td><%=i++%></td>
                                        <td><%StringHelper.filterHTML(out, badClaimZr.bidNo);%></td>
                                        <td><%StringHelper.filterHTML(out, badClaimZr.loanAttribute);%></td>
                                        <td title="<%=badClaimZr.loanTitle%>"><%
                                            StringHelper.filterHTML(out, StringHelper.truncation(badClaimZr.loanTitle, 10)); %></td>
                                        <td><%StringHelper.filterHTML(out, badClaimZr.loanName); %></td>
                                        <td><%=Formater.formatAmount(badClaimZr.loanAmount)%></td>
                                        <td><%=badClaimZr.hkfs.getChineseName()%></td>
                                        <td><%=Formater.formatRate(badClaimZr.yearRate)%></td>
                                        <td><%=badClaimZr.syPeriod+"/"+badClaimZr.zPeriods%></td>
                                        <td><%=Formater.formatDate(badClaimZr.dueTime)%></td>
                                        <td><%=badClaimZr.lateDays%></td>
                                        <td><%=Formater.formatAmount(badClaimZr.claimAmount)%></td>
                                        <td><%=Formater.formatAmount(badClaimZr.principalAmount)%></td>
                                        <td><%=Formater.formatAmount(badClaimZr.dhAmount)%></td>
                                        <td>
                                        <%if (dimengSession.isAccessableResource("P2P_C_LOAN_VIEWPROJECT")) {%>
                                            <a href="/console/bid/loanmanage/jkgl/viewProject.htm?loanId=<%=badClaimZr.bidId%>&userId=<%=badClaimZr.userId%>&operationJK=BLZQDZR"
                                               class="link-blue">查看</a>
                                        <%} else {%>
                                            <a href="javascript:void(0)" class="disabled">查看</a>
                                        <%}%>           
                                        <%if (dimengSession.isAccessableResource(BlzqDzrTransfer.class)) {%>
                                            <a href="<%=controller.getURI(request, BlzqDzrTransfer.class)%>?loanId=<%=badClaimZr.bidId%>&periodId=<%=badClaimZr.periodId%>"  class="link-blue">转让</a>
                                        <%} else { %>
                                            <span class="disabled">转让</span>
                                        <%} %> 
                                        </td>
                                    </tr>
                                    <%
                                        }
                                    } else {
                                    %>
                                    <tr>
                                        <td colspan="15" class="tc">暂无数据</td>
                                    </tr>
                                    <%} %>
                                    </tbody>
                                </table>
                        </div>
                        <div class="clear"></div>
                        <!--分页-->
                        <%AbstractConsoleServlet.rendPagingResult(out, badClaimZrs); %>
                        <!--分页 end-->
                    </div>
                </form>
            </div>
        </div>
    </div>
    <!--右边内容 结束-->
<script type="text/javascript">
function showExport() {
    document.getElementById("form1").action = "<%=controller.getURI(request, ExportBlzqDzr.class)%>";
    $("#form1").submit();
    document.getElementById("form1").action = "<%=controller.getURI(request, BlzqDzrList.class)%>";
}

function checkStock(){
    var stockMin = parseInt($("#yuqiMin").val());
    var stockMax = parseInt($("#yuqiMax").val());
    //最小值不能大于最大值
    if(stockMin > 0 && stockMin > stockMax){
        $("#yuqiMax").val(stockMin);
    }
}
</script>    
</body>
</html>