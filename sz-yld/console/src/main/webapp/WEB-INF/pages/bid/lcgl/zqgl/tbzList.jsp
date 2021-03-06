<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.console.servlets.bid.lcgl.zqgl.TbzList"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="com.dimeng.p2p.S62.enums.T6231_F21" %>
<%@page import="com.dimeng.p2p.console.servlets.bid.lcgl.zqgl.TbzExport" %>
<%@page import="com.dimeng.p2p.console.servlets.bid.lcgl.zqgl.YzcList" %>
<%@page import="com.dimeng.p2p.console.servlets.bid.lcgl.zqgl.YjqList" %>
<%@page import="com.dimeng.p2p.console.servlets.bid.lcgl.zqgl.YfkList" %>
<%@page
        import="com.dimeng.p2p.modules.financial.console.service.entity.Creditor" %>
<%@page import="com.dimeng.p2p.console.servlets.AbstractConsoleServlet" %>
<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <%@include file="/WEB-INF/include/script.jsp" %>
</head>
<body>
<%
    CURRENT_CATEGORY = "YWGL";
    CURRENT_SUB_CATEGORY = "ZQGL";
    PagingResult<Creditor> creditors = (PagingResult<Creditor>) request.getAttribute("creditors");
    BigDecimal tbzAmountCount = (BigDecimal) request.getAttribute("tbzAmountCount");
    Creditor[] creditorArray = creditors.getItems();
    String createTimeStart = request.getParameter("createTimeStart");
    String createTimeEnd = request.getParameter("createTimeEnd");
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <form id="form1" action="<%=controller.getURI(request, TbzList.class)%>" method="post">
                    <div class="p20">
                        <div class="border">
                            <div class="title-container">
                                <i class="icon-i w30 h30 va-middle title-left-icon"></i>债权管理
                            </div>
                            <div class="content-container pl40 pt30 pr40">
                                <ul class="gray6 input-list-container clearfix">
                                    <li><span class="display-ib mr5">投资人</span> <input
                                            type="text" name="userName" class="text border pl5 mr20"
                                            value="<%StringHelper.filterHTML(out, request.getParameter("userName"));%>"/>
                                    </li>
                                    <li><span class="display-ib mr5">借款标题</span> <input
                                            type="text" name="loanRecordTitle"
                                            class="text border pl5 mr20"
                                            value="<%StringHelper.filterHTML(out, request.getParameter("loanRecordTitle"));%>"/>
                                    </li>

                                    <li><span class="display-ib mr5">投资时间</span> <input
                                            type="text" name="createTimeStart" readonly="readonly"
                                            id="datepicker1" class="text border pl5 w120 date"/> <span
                                            class="pl5 pr5">至</span> <input type="text"
                                                                            readonly="readonly" name="createTimeEnd"
                                                                            id="datepicker2"
                                                                            class="text border pl5 w120 mr20 date"/></li>
									<li><span class="display-ib mr5">投资人类型</span>
                                        <select name="investUserType" class="border mr20 h32 mw100">
                                            <option value="">全部</option>
                                            <option value="ZRR" <%if ("ZRR".equals(request.getParameter("investUserType"))) {%>
                                                    selected="selected" <%}%>>个人</option>
                                            <option value="FZRR" <%if ("FZRR".equals(request.getParameter("investUserType"))) {%>
                                                    selected="selected" <%}%>>企业</option>
                                            <option value="FZRRJG" <%if ("FZRRJG".equals(request.getParameter("investUserType"))) {%>
                                                    selected="selected" <%}%>>机构</option>
                                        </select>
                                    </li>
                                    <li><a href="javascript:$('#form1').submit();"
                                           class="btn btn-blue radius-6 mr5 pl1 pr15 ml10"><i
                                            class="icon-i w30 h30 va-middle search-icon "></i>搜索</a></li>
                                    <li>
                                        <%
                                            if (dimengSession.isAccessableResource(TbzExport.class)) {
                                        %> <a href="javascript:void(0)"
                                              onclick="showExport()"
                                              class="btn btn-blue radius-6 mr5  pl1 pr15"><i
                                            class="icon-i w30 h30 va-middle export-icon "></i>导出</a> <%
                                    } else {
                                    %> <a href="javascript:void(0)" class="btn btn-gray radius-6 mr5  pl1 pr15"><i
                                            class="icon-i w30 h30 va-middle export-icon "></i>导出</a> <%
                                        }
                                    %>
                                    </li>

                                </ul>
                            </div>
                        </div>
                        <div class="border mt20">
                            <div class="tabnav-container">
                                <ul class="clearfix">
                                    <li><a href="javascript:void(0);" class="tab-btn select-a">投资中<i
                                        class="icon-i tab-arrowtop-icon"></i></a></li>
                                    <li><a
                                            href="<%=controller.getURI(request, YfkList.class)%>"
                                            class="tab-btn ">回款中</a></li>
                                    <li><a
                                            href="<%=controller.getURI(request, YjqList.class)%>"
                                            class="tab-btn ">已结清</a></li>
                                    <li><a
                                            href="<%=controller.getURI(request, YzcList.class)%>"
                                            class="tab-btn ">已转出</a></li>
                                </ul>
                            </div>
                            <div class=" table-container p20">
                                <table class="table table-style gray6 tl">
                                    <thead>
                                    <tr class="title tc">
                                        <th>序号</th>
                                        <th>标的ID</th>
                                        <th>借款标题</th>
                                        <th>投资人</th>
                                        <th>投资人类型</th>
                                        <th>年化利率</th>
                                        <th>期限</th>
                                        <th>持有债权金额(元)</th>
                                        <th>投资时间</th>
                                    </tr>
                                    </thead>
                                    <tbody class="f12">
                                    <%
                                        if (creditorArray != null && creditorArray.length > 0) {
                                            for (int i = 0; i < creditorArray.length; i++) {
                                                Creditor creditor = creditorArray[i];
                                                if (creditor == null) {
                                                    continue;
                                                }
                                    %>
                                    <tr class="tc">
                                        <td><%=i + 1%>
                                        </td>
                                        <td><%=creditor.creditorId%>
                                    	</td>
                                        <td title="<%StringHelper.filterHTML(out, creditor.jkbt);%>">
                                            <%
                                                StringHelper.filterHTML(out, StringHelper.truncation(creditor.jkbt, 10));
                                            %>
                                        </td>
                                        <td>
                                            <%
                                                StringHelper.filterHTML(out, creditor.userName);
                                            %>
                                        </td>
                                        <td><%StringHelper.filterHTML(out, creditor.investUserType); %></td>
                                        <td><%=Formater.formatRate(creditor.yearRate)%>
                                        </td>
                                        <td>
                                            <%
                                                if (creditor.f21 == T6231_F21.S) {
                                            %><%=creditor.day%>天<%
                                        } else {
                                        %><%=creditor.deadline%>个月<%
                                            }
                                        %>
                                        </td>
                                        <td><%=Formater.formatAmount(creditor.investmentAmount)%>
                                        </td>
                                        <td><%=DateTimeParser.format(creditor.tenderTime)%>
                                        </td>
                                    </tr>
                                    <%
                                        }
                                    } else {
                                    %>
                                    <tr class="tc">
                                        <td colspan="9">暂无数据</td>
                                    </tr>
                                    <%} %>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                        <div class="mb10">持有债权总金额：<em class="red"><%=Formater.formatAmount(tbzAmountCount) %>
                        </em>元
                        </div>
                        <!--分页-->
                        <%
                            AbstractConsoleServlet.rendPagingResult(out, creditors);
                        %>
                        <!--分页 end-->

                    </div>
                </form>
            </div>
        </div>
    </div>
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
        $("#datepicker1").val("<%StringHelper.filterHTML(out, request.getParameter("createTimeStart"));%>");
        <%}%>
        <%if(!StringHelper.isEmpty(createTimeEnd)){%>
        $("#datepicker2").val("<%StringHelper.filterHTML(out, request.getParameter("createTimeEnd"));%>");
        <%}%>
        $("#datepicker2").datepicker('option', 'minDate', $("#datepicker1").datepicker().val());
    });

    function showExport() {
        document.getElementById("form1").action = "<%=controller.getURI(request, TbzExport.class)%>";
        $("#form1").submit();
        document.getElementById("form1").action = "<%=controller.getURI(request, TbzList.class)%>";
    }
</script>
</body>
</html>