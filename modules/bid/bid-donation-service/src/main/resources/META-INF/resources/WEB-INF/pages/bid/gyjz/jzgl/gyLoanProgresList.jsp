<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="java.math.BigDecimal"%>
<%@page import="com.dimeng.p2p.console.servlets.AbstractConsoleServlet" %>
<%@page import="com.dimeng.p2p.console.servlets.bid.gyjz.gyyw.*" %>
<%@page import="com.dimeng.p2p.console.servlets.bid.gyjz.jzgl.*" %>
<%@page import="com.dimeng.util.ObjectHelper" %>
<%@ page import="com.dimeng.p2p.S62.enums.T6242_F11" %>
<%@ page import="com.dimeng.p2p.repeater.donation.entity.GyLoan" %>
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
    CURRENT_SUB_CATEGORY = "GYBD_PROGRES_GL";
    PagingResult<GyLoan> result = ObjectHelper.convert(request.getAttribute("loanIntentions"), PagingResult.class);

    GyLoanStatis statis = ObjectHelper.convert(request.getAttribute("statis"), GyLoanStatis.class);

    GyLoan[] loans = null == result ? null : result.getItems();
    //T6211[] t6211s=ObjectHelper.convertArray(request.getAttribute("t6211s"), T6211.class);
    BigDecimal searchListAmount = (BigDecimal) request.getAttribute("searchListAmount");
    String createTimeStart = request.getParameter("createTimeStart");
    String createTimeEnd = request.getParameter("createTimeEnd");
    String comTimeStart = request.getParameter("comTimeStart");
    String comTimeEnd = request.getParameter("comTimeEnd");
    
    T6242_F11[] allStatus = T6242_F11.values();
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <form id="form_loan" action="<%=controller.getURI(request, GyLoanProgresList.class)%>" method="post">
                    <div class="p20">
                        <div class="border">
                            <div class="title-container"><i class="icon-i w30 h30 va-middle title-left-icon"></i>进展管理
                            </div>
                            <div class="content-container pl40 pt30 pr40">
                                <ul class="gray6 input-list-container clearfix">
                                    <li><span class="display-ib mr5">捐款总额</span>
                                        <span title="捐款总额=状态为捐款中或已捐助的公益标总额"
                                              class="link-blue"><%=Formater.formatAmount(statis.donationsAmount) %></span>元
                                    </li>
                                    <li class="ml50"><span class="display-ib mr5">捐款笔数</span>
                                        <span class="link-blue"><%=statis.donationsNum %></span>笔
                                    </li>
                                </ul>
                            </div>
                        </div>
                        <div class="border mt20">
                            <div class="content-container pl40 pt30 pr40">
                                <ul class="gray6 input-list-container clearfix">
                                    <li><span class="display-ib mr5">公益标题</span>
                                        <input type="text" name="loanTitle" class="text border pl5 mr20"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("loanTitle"));%>"/>
                                    </li>
                                    <li><span class="display-ib mr5">公益ID</span>
                                        <input type="text" name="loanNo" class="text border pl5 mr20"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("loanNo"));%>"/>
                                    </li>
                                    <li><span class="display-ib mr5">公益方</span>
                                        <input type="text" name="gyName" class="text border pl5 mr20"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("gyName"));%>"/>
                                    </li>
                                    <%-- <li><span class="display-ib mr5">状态</span>
                                        <select id="select" class="border mr20 h32 mw100" name="status">
                                            <option value="0">全部</option>
                                            <%
                                                String status = request.getParameter("status");
                                                if (StringHelper.isEmpty(status)) {
                                                    status = "";
                                                }
                                            %>
                                            <%
                                                if (null != allStatus) {
                                                    for (T6242_F11 t6242_F11 : allStatus) {
                                                        if (null == t6242_F11) {
                                                            continue;
                                                        }
                                            %>
                                            <option value="<%=t6242_F11.name() %>"
                                                    <%if(status.equals(t6242_F11.name())){ %>selected="selected"<%} %>><%
                                                StringHelper.filterHTML(out, t6242_F11.getChineseName()); %></option>
                                            <%
                                                    }
                                                }
                                            %>
                                        </select>
                                    </li> --%>

                                    <li><span class="display-ib mr5">捐款完成时间</span>
                                        <input type="text" readonly="readonly" name="comTimeStart" id="datepicker1"
                                               class="text border pl5 w120 date"/>
                                        <span class="pl5 pr5">至</span>
                                        <input type="text" readonly="readonly" name="comTimeEnd" id="datepicker2"
                                               class="text border pl5 w120 mr20 date"/>
                                    </li>
                                    <li><span class="display-ib mr5">创建时间</span>
                                        <input type="text" readonly="readonly" name="createTimeStart" id="datepicker3"
                                               class="text border pl5 w120 date"/>
                                        <span class="pl5 pr5">至</span>
                                        <input type="text" readonly="readonly" name="createTimeEnd" id="datepicker4"
                                               class="text border pl5 w120 mr20 date"/>
                                    </li>

                                    <li><a href="javascript:search();" class="btn btn-blue radius-6 mr5 pl1 pr15"><i
                                            class="icon-i w30 h30 va-middle search-icon "></i>搜索</a></li>

                                    <li>
                                        <%
                                            if (dimengSession.isAccessableResource(ExportGyLoanProgres.class)) {
                                        %>
                                        <a href="javascript:void(0)" onclick="showExport()"
                                           class="btn btn-blue radius-6 mr5  pl1 pr15"><i
                                                class="icon-i w30 h30 va-middle export-icon "></i>导出</a>
                                        <%
                                        } else {
                                        %>
                                        <span class="btn btn-gray radius-6 mr5  pl1 pr15"><i
                                                class="icon-i w30 h30 va-middle export-icon "></i>导出</span>
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
                                    <th>公益ID</th>
                                    <th>公益标题</th>
                                    <th>公益方</th>
                                    <th>筹款金额(元)</th>
                                    <th>捐款完成时间</th>
                                    <th>进展数</th>
                                    <th>创建人</th>
                                    <th>创建时间</th>
                                    <th class="w200">操作</th>
                                </tr>
                                </thead>
                                <tbody class="f12">
                                <%
                                    if (loans != null && loans.length != 0) {
                                        int i = 1;
                                        String hideLoanId;
                                        for (GyLoan loan : loans) {
                                            hideLoanId = "loan_" + loan.t6242.F01;
                                %>
                                <tr class="title tc">
                                    <td><%=i++ %>
                                    </td>
                                    <td><%=loan.t6242.F21 %>
                                    </td>
                                    <td id="<%=hideLoanId %>"
                                        title="<%StringHelper.filterHTML(out, loan.t6242.F03); %>"><%
                                        StringHelper.filterHTML(out, StringHelper.truncation(loan.t6242.F03, 10)); %></td>
                                    <td><%StringHelper.filterHTML(out, loan.t6242.F22); %></td>
                                    <td><%=Formater.formatAmount(loan.t6242.F05) %>
                                    </td>
                                    <td><%=DateTimeParser.format(loan.t6242.F19) %>
                                    </td>
                                    <td><%=loan.progres %>
                                    </td>
                                    <td>
                                        <%out.print(loan.sysName); %>
                                    </td>
                                    <td><%=DateTimeParser.format(loan.t6242.F15) %>
                                    </td>
                                    <td>
                                        <%
                                            if (dimengSession.isAccessableResource(ViewGyLoan.class)) {
                                        %>
                                        <a href="<%=controller.getURI(request, ViewGyLoan.class) %>?loanId=<%=loan.t6242.F01 %>&userId=<%=loan.t6242.F23 %>&enterType=jzgl"
                                           class="link-blue mr20">查看</a>
                                        <%} else { %>
                                        <a href="javascript:void(0)" class="disabled">查看</a>
                                        <%} %>
                                        <%
                                            if ((loan.t6242.F11 == T6242_F11.JKZ || loan.t6242.F11 == T6242_F11.YJZ) && dimengSession.isAccessableResource(ViewProgres.class)) {
                                        %>
                                        <a href="<%=controller.getURI(request, ViewProgres.class) %>?loanId=<%=loan.t6242.F01 %>&userId=<%=loan.t6242.F23 %>"
                                           class="link-blue">进展管理</a>
                                        <%} else { %>
                                        <a href="javascript:void(0)" class="disabled">进展管理</a>
                                        <%} %>

                                    </td>
                                </tr>
                                <%
                                    }
                                } else {
                                %>
                                <tr class="title tc">
                                    <td colspan="10">暂无数据</td>
                                </tr>
                                <%} %>
                                </tbody>
                            </table>
                        </div>
						<div class="clear"></div>
                        <div class="mb10">
                            <span class="mr30">筹款总金额：<em
                                    class="red"><%=Formater.formatAmount(searchListAmount) %>
                            </em> 元</span>
                        </div>
                        <!--分页-->
                        <%
                            AbstractConsoleServlet.rendPagingResult(out, result);
                        %>
                        <!--分页 end-->

                    </div>
                </form>
            </div>
        </div>
    </div>

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
<%@include file="/WEB-INF/include/datepicker.jsp" %>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/validation.js"></script>
<script type="text/javascript">
    $(function () {
        $("[name='userType']").val('<%=request.getParameter("userType")%>');
        $("#datepicker1").datepicker({
            inline: true,
            onSelect: function (selectedDate) {
                $("#datepicker2").datepicker("option", "minDate", selectedDate);
            }
        });
        $('#datepicker1').datepicker('option', {dateFormat: 'yy-mm-dd'});
        $("#datepicker2").datepicker({inline: true});
        $('#datepicker2').datepicker('option', {dateFormat: 'yy-mm-dd'});
        <%if(!StringHelper.isEmpty(comTimeStart)){%>
        $("#datepicker1").val("<%StringHelper.filterHTML(out, request.getParameter("comTimeStart"));%>");
        <%}%>
        <%if(!StringHelper.isEmpty(comTimeEnd)){%>
        $("#datepicker2").val("<%StringHelper.filterHTML(out, request.getParameter("comTimeEnd"));%>");
        <%}%>
        $("#datepicker2").datepicker('option', 'minDate', $("#datepicker1").datepicker().val());
        
        $("#datepicker3").datepicker({
            inline: true,
            onSelect: function (selectedDate) {
                $("#datepicker4").datepicker("option", "minDate", selectedDate);
            }
        });
        $('#datepicker3').datepicker('option', {dateFormat: 'yy-mm-dd'});
        $("#datepicker4").datepicker({inline: true});
        $('#datepicker4').datepicker('option', {dateFormat: 'yy-mm-dd'});
        <%if(!StringHelper.isEmpty(createTimeStart)){%>
        $("#datepicker3").val("<%StringHelper.filterHTML(out, request.getParameter("createTimeStart"));%>");
        <%}%>
        <%if(!StringHelper.isEmpty(createTimeEnd)){%>
        $("#datepicker4").val("<%StringHelper.filterHTML(out, request.getParameter("createTimeEnd"));%>");
        <%}%>
        $("#datepicker4").datepicker('option', 'minDate', $("#datepicker3").datepicker().val());
    });
    function search() {
        document.getElementsByName("paging.current")[0].value = 1;
        $("#form_loan").submit();
    }

    function showExport() {
        document.getElementById("form_loan").action = "<%=controller.getURI(request, ExportGyLoanProgres.class)%>";
        $("#form_loan").submit();
        document.getElementById("form_loan").action = "<%=controller.getURI(request, GyLoanProgresList.class)%>";
    }
</script>
</body>
</html>