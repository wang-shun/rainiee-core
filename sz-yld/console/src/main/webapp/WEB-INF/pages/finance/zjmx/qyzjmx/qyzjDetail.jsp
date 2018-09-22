<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.console.servlets.finance.zjmx.qyzjmx.QyzjDetail"%>
<%@page import="com.dimeng.p2p.S51.entities.T5122" %>
<%@page import="com.dimeng.p2p.S61.enums.T6101_F03" %>
<%@page import="com.dimeng.p2p.console.servlets.AbstractConsoleServlet" %>
<%@page import="com.dimeng.p2p.console.servlets.finance.zjmx.qyzjmx.ExportQyzjDetail" %>
<%@page import="com.dimeng.p2p.modules.account.console.service.entity.ZjDetailView" %>
<%@page import="com.dimeng.p2p.modules.base.console.service.TradeTypeManage" %>
<%@ page import="com.dimeng.p2p.S61.enums.T6110_F06" %>
<%@ page import="com.dimeng.p2p.S61.enums.T6110_F10" %>
<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <%@include file="/WEB-INF/include/script.jsp"%>
</head>
<%
    int tradingType = IntegerParser.parse(request.getParameter("tradingType"));
    String startTime = request.getParameter("startTime");
    String endTime = request.getParameter("endTime");
    PagingResult<ZjDetailView> qyzjDetailViewList = (PagingResult<ZjDetailView>) request.getAttribute("qyzjDetailViewList");
    ZjDetailView qyzjDetailCount = (ZjDetailView) request.getAttribute("qyzjDetailCount");
%>
<%
    CURRENT_CATEGORY = "CWGL";
    CURRENT_SUB_CATEGORY = "QYZJMX";
%>
<body>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <form id="searchForm" name="form1" action="<%=controller.getURI(request, QyzjDetail.class)%>"
                      method="post">
                    <div class="p20">
                        <div class="border">
                            <div class="title-container"><i class="icon-i w30 h30 va-middle title-left-icon"></i>企业资金明细
                            </div>
                            <div class="content-container pl40 pt30 pr40">
                                <ul class="gray6 input-list-container clearfix">
                                    <li><span class="display-ib mr5">用户名： </span>
                                        <input type="text" name="loginName" id="serialNumber"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("loginName"));%>"
                                               class="text border pl5 mr20"/>
                                    </li>
                                    <li><span class="display-ib mr5">类型明细： </span>
                                        <select name="tradingType" class="border mr20 h32 mw100">
                                            <option value="">全部</option>
                                            <%
                                                TradeTypeManage tradeTypeManage = serviceSession.getService(TradeTypeManage.class);
                                                T5122[] t5122s = tradeTypeManage.search(T6110_F06.FZRR, T6110_F10.F, null);
                                                if (t5122s != null && t5122s.length > 0) {
                                                    for (T5122 type : t5122s) {
                                                        if(type.F01 == 4004){
                                                            continue;
                                                        }
                                            %>
                                            <option value="<%=type.F01%>"
                                                    <%if(type.F01 == tradingType){%>selected="selected"<%}%>><%=type.F02%>
                                            </option>
                                            <%
                                                        }
                                                }
                                            %>
                                        </select>
                                    </li>
                                    <li><span class="display-ib mr5">账户类型： </span>
                                        <select name="zhlx" class="border mr20 h32 mw100">
                                            <option value="">全部</option>
                                            <option value="<%=T6101_F03.WLZH.name()%>"
                                                    <%if("WLZH".equals(request.getParameter("zhlx"))){ %>selected="selected" <%} %>><%=T6101_F03.WLZH.getChineseName() %>
                                            </option>
                                            <option value="<%=T6101_F03.SDZH.name()%>"
                                                    <%if("SDZH".equals(request.getParameter("zhlx"))){ %>selected="selected" <%} %>><%=T6101_F03.SDZH.getChineseName() %>
                                            </option>
                                            <%
                                                boolean isHasGuarant = BooleanParser.parse(configureProvider.getProperty(GuarantorVariavle.IS_HAS_GUARANTOR));
                                                if(isHasGuarant){
                                            %>
                                            <option value="<%=T6101_F03.FXBZJZH.name()%>"
                                                    <%if("FXBZJZH".equals(request.getParameter("zhlx"))){ %>selected="selected" <%} %>><%=T6101_F03.FXBZJZH.getChineseName() %>
                                            </option>
                                            <%}%>
                                        </select>
                                    </li>
                                    <li><span class="display-ib mr5">时间： </span>
                                        <input type="text" name="startTime" readonly="readonly" id="startDate"
                                               class="text border pl5 w120 date"/>
                                        <span class="pl5 pr5">至</span>
                                        <input readonly="readonly" type="text" name="endTime" id="endDate"
                                               class="text border pl5 w120 mr20 date"/>
                                    </li>
                                    <li><a href="javascript:onSubmit();" 
                                           class="btn btn-blue radius-6 mr5 pl1 pr15"><i
                                            class="icon-i w30 h30 va-middle search-icon "></i>搜索</a></li>
                                    <li>
                                        <%if (dimengSession.isAccessableResource(ExportQyzjDetail.class)) {%>
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
                                    <th class="tc">用户名</th>
                                    <th class="tc">时间</th>
                                    <th class="tc">类型明细</th>
                                    <th class="tc">账户类型</th>
                                    <th class="tc">收入(元)</th>
                                    <th class="tc">支出(元)</th>
                                    <th class="tc">结余(元)</th>
                                    <th class="tc">备注</th>
                                </tr>
                                </thead>
                                <tbody class="f12">
                                <%
                                    ZjDetailView[] qyzjDetailViews = null;
                                    if (qyzjDetailViewList != null) {
                                        qyzjDetailViews = qyzjDetailViewList.getItems();
                                    }
                                    if (qyzjDetailViews != null && qyzjDetailViews.length > 0) {
                                        int i = 1;
                                        for (ZjDetailView record : qyzjDetailViews) {
                                            if (record == null) {
                                                continue;
                                            }
                                %>
                                <tr class="tc">
                                    <td><%=i++%>
                                    </td>
                                    <td><%=record.userName%>
                                    </td>
                                    <td><%=DateTimeParser.format(record.F05) %>
                                    </td>
                                    <td><%=record.tradingName%>
                                    </td>
                                    <td><%=record.zhlx.getChineseName() %>
                                    </td>
                                    <td><%=Formater.formatAmount(record.F06)%>
                                    </td>
                                    <td><%=Formater.formatAmount(record.F07)%>
                                    </td>
                                    <td><%=Formater.formatAmount(record.F08)%>
                                    </td>
                                    <td><%=record.F09%>
                                    </td>
                                </tr>
                                <%
                                    }
                                } else {
                                %>
                                <tr>
                                    <td colspan="9" class="tc">暂无数据</td>
                                </tr>
                                <%} %>
                                </tbody>
                            </table>
                        </div>
                        <p class="mt5">
                            <span class="mr30">收入总计：<em class="red"><%=Formater.formatAmount(qyzjDetailCount.F06)%>
                            </em> 元</span>
                            <span class="mr30">支出总计：<em class="red"><%=Formater.formatAmount(qyzjDetailCount.F07)%>
                            </em> 元</span>
                        </p>
                        <!--分页-->
                        <%AbstractConsoleServlet.rendPagingResult(out, qyzjDetailViewList); %>
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
        $("#startDate").datepicker({
            inline: true,
            onSelect: function (selectedDate) {
                $("#endDate").datepicker("option", "minDate", selectedDate);
            }
        });
        $("#endDate").datepicker({inline: true});
        <%if(!StringHelper.isEmpty(startTime)){%>
        $('#startDate').datepicker('setDate', "<%StringHelper.filterQuoter(out, startTime); %>");
        <%}%>
        <%if(!StringHelper.isEmpty(endTime)){%>
        $('#endDate').datepicker('setDate', "<%StringHelper.filterQuoter(out, endTime); %>");
        <%}%>
        $("#endDate").datepicker('option', 'minDate', $("#startDate").datepicker().val());
    });
    function showExport() {
        document.getElementById("searchForm").action = "<%=controller.getURI(request, ExportQyzjDetail.class)%>";
        $("#searchForm").submit();
        document.getElementById("searchForm").action = "<%=controller.getURI(request, QyzjDetail.class)%>";
    }
    function onSubmit(){
    	$("input[name='<%=AbstractConsoleServlet.PAGING_CURRENT%>']").val('1');
    	$('#searchForm').submit();
    }
</script>
</body>
</html>