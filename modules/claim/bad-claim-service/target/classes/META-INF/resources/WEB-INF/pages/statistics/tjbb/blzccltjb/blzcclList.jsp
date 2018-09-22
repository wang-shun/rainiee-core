<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="com.dimeng.p2p.console.servlets.AbstractConsoleServlet" %>
<%@page import="com.dimeng.p2p.repeater.claim.entity.BadClaimYzr" %>
<%@page import="com.dimeng.p2p.console.servlets.statistics.tjbb.blzccltjb.BlzcclList" %>
<%@page import="com.dimeng.p2p.console.servlets.statistics.tjbb.blzccltjb.ExportBlzccl" %>
<%@page import="com.dimeng.p2p.repeater.claim.entity.BadAssets" %>
<%@page import="com.dimeng.util.ObjectHelper" %>
<%@page import="com.dimeng.p2p.S61.entities.T6161" %>
<%@page import="java.util.List" %>
<%@page import="java.math.BigDecimal" %>

<html dir="ltr" xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN">
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <%@include file="/WEB-INF/include/script.jsp"%>
</head>

<body>
<%
    CURRENT_CATEGORY = "TJGL";
    CURRENT_SUB_CATEGORY = "BLZCCLTJBLIST";
    PagingResult<BadClaimYzr> badClaimYzrs = (PagingResult<BadClaimYzr>) request.getAttribute("badClaimYzrPagingResult");
    List<T6161> t6161List = (List<T6161>) request.getAttribute("t6161List");
    BadClaimYzr[] badClaimYzrArray = null;
    if(badClaimYzrs != null){
        badClaimYzrArray = badClaimYzrs.getItems();
    }
    String createTimeStart = request.getParameter("startTime");
    String createTimeEnd = request.getParameter("endTime");
    
    BadAssets badAssets = ObjectHelper.convert(request.getAttribute("badAssetsTotal"), BadAssets.class);
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <form id="form1" name="form1" action="<%=controller.getURI(request, BlzcclList.class)%>" method="post">
                    <div class="p20">
                        <div class="border">
                            <div class="title-container"><i class="icon-i w30 h30 va-middle title-left-icon"></i>不良资产处理统计表
                            </div>
                            <div class="content-container pl40 pt30 pr40">
                                <ul class="gray6 input-list-container clearfix">
                                   <li><span class="display-ib mr5">债权编号： </span>
                                        <input type="text" name="claimNo"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("claimNo"));%>"
                                               class="text border pl5 mr20"/>
                                    </li>
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
                                               class="text border pl5 w120"/>
                                        <span class="pl5 pr5">至</span>
                                        <input type="text" name="yuqiEndDays" onkeyup="value=value.replace(/\D/g,'')"
                                        value="<%StringHelper.filterHTML(out, request.getParameter("yuqiEndDays"));%>"
                                        class="text border pl5 w120 mr20" />
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
                                    <li><span class="display-ib mr5">债权接收方： </span>
                                        <select name="claimReceiver" class="border mr20 h32 mw100">
                                            <option value="">全部</option>
                                            <%if(t6161List != null && t6161List.size()>0){ 
                                                for(T6161 t6161:t6161List){
                                            %>
                                            <option value="<%=t6161.F01%>"
                                                    <%if(String.valueOf(t6161.F01).equals(request.getParameter("claimReceiver"))){ %>selected="selected"<%} %>><%=t6161.F04%></option>
                                        <%}}%>
                                        </select>
                                    </li>
                                    <li>
                                        <span class="display-ib mr5">购买时间：</span>
                                        <input type="text" name="startTime" id="datepicker1"
                                               readonly="readonly" class="text border pl5 w120 date"/>
                                        <span class="pl5 pr5">至</span>
                                        <input type="text" name="endTime" id="datepicker2"
                                               readonly="readonly" class="text border pl5 w120 date mr20"/>
                                    </li>
                                    <li><a href="javascript:$('#form1').submit();" 
                                           class="btn btn-blue radius-6 mr5 pl1 pr15"><i
                                            class="icon-i w30 h30 va-middle search-icon "></i>搜索</a></li>
                                    <li>
                                        <%if (dimengSession.isAccessableResource(ExportBlzccl.class)) {%>
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
                        <div class="border mt20">
                            <div class=" table-container p20">
                                <table class="table table-style gray6 tl">
                                    <thead>
                                    <tr class="title tc">
                                        <th>序号</th>
                                        <th>债权编号</th>
                                        <th>借款编号</th>
                                        <th>标的属性</th>
                                        <th>借款标题</th>
                                        <th>借款账户</th>
                                        <th>借款金额（元）</th>
                                        <th>还款方式</th>
                                        <th>期数</th>
                                        <th>逾期时间</th>
                                        <th>逾期天数</th>
                                        <th>债权价值（元）</th>
                                        <th>购买价格（元）</th>
                                        <th>债权接收方</th>
                                        <th>购买时间</th>
                                    </tr>
                                    </thead>
                                    <tbody class="f12">
                                    <%
                                        if (badClaimYzrArray != null && badClaimYzrArray.length > 0) {
                                            int i = 1;
                                            for (BadClaimYzr badClaimYzr:badClaimYzrArray) {
                                    %>
                                    <tr class="tc">
                                        <td><%=i++%></td>
                                        <td><%StringHelper.filterHTML(out, badClaimYzr.claimNo);%></td>
                                        <td><%StringHelper.filterHTML(out, badClaimYzr.bidNo);%></td>
                                        <td><%StringHelper.filterHTML(out, badClaimYzr.loanAttribute);%></td>
                                        <td title="<%=badClaimYzr.loanTitle%>"><%
                                            StringHelper.filterHTML(out, StringHelper.truncation(badClaimYzr.loanTitle, 10)); %></td>
                                        <td><%StringHelper.filterHTML(out, badClaimYzr.loanName); %></td>
                                        <td><%=Formater.formatAmount(badClaimYzr.loanAmount)%></td>
                                        <td><%=badClaimYzr.hkfs.getChineseName()%></td>
                                        <td><%=badClaimYzr.syPeriod+"/"+badClaimYzr.zPeriods%></td>
                                        <td><%=Formater.formatDate(badClaimYzr.dueTime)%></td>
                                        <td><%=badClaimYzr.lateDays%></td>
                                        <td><%=Formater.formatAmount(badClaimYzr.claimAmount)%></td>
                                        <td><%=Formater.formatAmount(badClaimYzr.transferAmount)%></td>
                                        <td><%StringHelper.filterHTML(out, badClaimYzr.claimReceiver);%></td>
                                        <td><%=TimestampParser.format(badClaimYzr.buyTime)%></td>
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
                        </div>
                        <p class="mt5">
                            <span class="mr30">债权总价值：<em class="red"><%=Formater.formatAmount(badAssets.claimAmountTatal)%>
                            </em> 元</span>
                            <span class="mr30">购买价格总金额：<em class="red"><%=Formater.formatAmount(badAssets.transferAmountTatal)%>
                            </em> 元</span>
                        </p>
                        <div class="clear"></div>
                        <!--分页-->
                        <%AbstractConsoleServlet.rendPagingResult(out, badClaimYzrs); %>
                        <!--分页 end-->
                    </div>
                </form>
            </div>
        </div>
    </div>
    <!--右边内容 结束-->
<%@include file="/WEB-INF/include/datepicker.jsp" %>
<script type="text/javascript">
    $(function () {
        $("#datepicker1").datepicker({
            inline: true,
            onSelect: function (selectedDate) {
                $("#datepicker2").datepicker("option", "minDate", selectedDate);
            }
        });
        $('#datepicker1').datepicker('option', {dateFormat: 'yy-mm-dd'});
        $("#datepicker2").datepicker({inline: true});
        $('#datepicker2').datepicker('option', {dateFormat: 'yy-mm-dd'});
        <%if(!StringHelper.isEmpty(createTimeStart)){%>
        $("#datepicker1").val("<%StringHelper.filterHTML(out, request.getParameter("startTime"));%>");
        <%}%>
        <%if(!StringHelper.isEmpty(createTimeEnd)){%>
        $("#datepicker2").val("<%StringHelper.filterHTML(out, request.getParameter("endTime"));%>");
        <%}%>
        $("#datepicker2").datepicker('option', 'minDate', $("#datepicker1").datepicker().val());
    });
    
    function showExport() {
        document.getElementById("form1").action = "<%=controller.getURI(request, ExportBlzccl.class)%>";
        $("#form1").submit();
        document.getElementById("form1").action = "<%=controller.getURI(request, BlzcclList.class)%>";
    }
</script> 
</body>
</html>