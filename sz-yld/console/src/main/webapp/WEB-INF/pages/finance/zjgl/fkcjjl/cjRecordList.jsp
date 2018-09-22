<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.S62.enums.T6231_F21" %>
<%@page import="com.dimeng.p2p.console.servlets.finance.zjgl.fkcjjl.ExportCjRecord" %>
<%@page import="com.dimeng.p2p.modules.bid.console.service.entity.CjRecord" %>
<%@page import="com.dimeng.util.Formater" %>
<%@page import="com.dimeng.util.ObjectHelper" %>
<%@page import="java.math.BigDecimal" %>
<%@page import="com.dimeng.p2p.console.servlets.AbstractConsoleServlet" %>
<%@page import="com.dimeng.p2p.console.servlets.finance.zjgl.fkcjjl.CjRecordList" %>
<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <%@include file="/WEB-INF/include/script.jsp" %>
</head>
<%
    PagingResult<CjRecord> result = (PagingResult<CjRecord>) request.getAttribute("result");
    BigDecimal totalAmount = ObjectHelper.convert(request.getAttribute("totalAmount"), BigDecimal.class);
    BigDecimal totalFkAmount = ObjectHelper.convert(request.getAttribute("totalFkAmount"), BigDecimal.class);
    CjRecord searchCjAmount = (CjRecord)request.getAttribute("searchCjAmount");
    CURRENT_CATEGORY = "CWGL";
    CURRENT_SUB_CATEGORY = "FKCJJL";
    String createTimeStart = request.getParameter("startTime");
    String createTimeEnd = request.getParameter("endTime");
%>
<body>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <form id="searchForm" name="form1" action="<%=controller.getURI(request, CjRecordList.class)%>"
                      method="post">
                    <div class="p20">
                        <div class="border">
                            <div class="title-container">
                                <i class="icon-i w30 h30 va-middle title-left-icon"></i>成交记录
                                <%-- <div class="fr">
                                  <input type="button" value="返回" style="margin-top:5px;" class="btn btn-blue radius-6 pl20 pr20 ml40" onclick="window.location.href='<%=controller.getURI(request, FkshList.class)%>'">
                                </div>  --%>
                            </div>
                            <div class="content-container pl40 pt30 pr40">
                                <ul class="gray6 input-list-container clearfix">
                                    <li><span class="display-ib mr5">借款总金额</span>
                                        <span class="link-blue"><%=Formater.formatAmount(totalAmount)%></span>元
                                    </li>
                                    <li class="ml50"><span class="display-ib mr5">放款总金额</span>
                                        <span class="link-blue"><%=Formater.formatAmount(totalFkAmount) %></span>元
                                    </li>
                                </ul>
                            </div>
                        </div>

                        <div class="border mt20">
                            <div class="content-container pl40 pt30 pr40">
                                <ul class="gray6 input-list-container clearfix">
                                    <li><span class="display-ib mr5">借款账号： </span>
                                        <input type="text" name="loanName"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("loanName"));%>"
                                               class="text border pl5 mr20"/>
                                    </li>
                                    <li><span class="display-ib mr5">借款ID： </span>
                                        <input type="text" name="loanNum"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("loanNum"));%>"
                                               class="text border pl5 mr20"/>
                                    </li>
                                    <li><span class="display-ib mr5">放款时间： </span>
                                        <input type="text" name="startTime" readonly="readonly" id="datepicker1"
                                               class="text border pl5 w120 date"/>
                                        <span class="pl5 pr5">至</span>
                                        <input readonly="readonly" type="text" name="endTime" id="datepicker2"
                                               class="text border pl5 w120 mr20 date"/>
                                    </li>
                                    <li><a href="javascript:void(0);" onclick="javascript:onSubmit();"
                                           class="btn btn-blue radius-6 mr5 pl1 pr15"><i
                                            class="icon-i w30 h30 va-middle search-icon "></i>搜索</a></li>
                                    <li>
                                        <%if (dimengSession.isAccessableResource(ExportCjRecord.class)) {%>
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
                                <tr class="title">
                                    <th class="tc">序号</th>
                                    <th class="tc">放款时间</th>
                                    <th class="tc">借款账号</th>
                                    <th class="tc">借款ID</th>
                                    <th class="tc">借款金额(元)</th>
                                    <th class="tc">投资金额(元)</th>
                                    <th class="tc">红包金额(元)</th>
                                    <th class="tc">体验金金额(元)</th>
                                    <th class="tc">借款期限</th>
                                    <th class="tc">放款人</th>
                                </tr>
                                </thead>
                                <tbody class="f12">
                                <%
                                    CjRecord[] records = result.getItems();
                                    if (records != null && records.length > 0) {
                                        int i = 1;
                                        for (CjRecord record : records) {
                                            if (record == null) {
                                                continue;
                                            }
                                %>
                                <tr class="tc">
                                    <td><%=i++%>
                                    </td>
                                    <td><%=DateTimeParser.format(record.fkTime, "yyyy-MM-dd HH:mm")%>
                                    </td>
                                    <td><%StringHelper.filterHTML(out, record.accountName);%></td>
                                    <td><%=record.F25%>
                                    </td>
                                    <td><%=Formater.formatAmount(record.F05)%>
                                    </td>
                                    <td><%=Formater.formatAmount(record.F05.subtract(record.F07))%>
                                    </td>
                                    <td><%=Formater.formatAmount(record.hbAmount)%>
                                    </td>
                                    <td><%=Formater.formatAmount(record.experAmount)%>
                                    </td>
                                    <td>
                                        <% if (record.F28.F21 == T6231_F21.F) { %>
                                        <%=record.days%>个月
                                        <%} else { %>
                                        <%=record.F28.F22 %>天
                                        <%} %>
                                    </td>
                                    <td><%StringHelper.filterHTML(out, record.fkName); %></td>
                                </tr>
                                <%
                                    }
                                } else {
                                %>
                                <tr>
                                    <td colspan="10" class="tc">暂无数据</td>
                                </tr>
                                <%} %>
                                </tbody>
                            </table>
                        </div>
                        <div class="clear"></div>
                        <div class="mb10">
                            <span class="mr30">借款总金额：<em
                                    class="red"><%=Formater.formatAmount(searchCjAmount.F05) %>
                            </em> 元</span>
                            <span class="mr30">投资总金额：<em
                                    class="red"><%=Formater.formatAmount(searchCjAmount.F05.subtract(searchCjAmount.F07)) %>
                            </em> 元</span>
                            <span class="mr30">红包总额：<em
                                    class="red"><%=Formater.formatAmount(searchCjAmount.hbAmount) %>
                            </em> 元</span>
                            <span class="mr30">体验金总额：<em
                                    class="red"><%=Formater.formatAmount(searchCjAmount.experAmount) %>
                            </em> 元</span>
                        </div>
                        <!--分页-->
                        <%AbstractConsoleServlet.rendPagingResult(out, result); %>
                        <!--分页 end-->
                    </div>
                </form>
            </div>
        </div>
    </div>
    <!--右边内容 结束-->
<%@include file="/WEB-INF/include/datepicker.jsp" %>
<script type="text/javascript">
    function showExport() {
        document.getElementById("searchForm").action = "<%=controller.getURI(request, ExportCjRecord.class)%>";
        $("#searchForm").submit();
        document.getElementById("searchForm").action = "<%=controller.getURI(request, CjRecordList.class)%>";
    }
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
    function onSubmit(){
    	$("input[name='<%=AbstractConsoleServlet.PAGING_CURRENT%>']").val('1');
    	$('#searchForm').submit();
    }
</script>
</body>
</html>