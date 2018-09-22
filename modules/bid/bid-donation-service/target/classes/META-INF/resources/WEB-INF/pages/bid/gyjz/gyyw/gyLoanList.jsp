<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="java.util.Calendar"%>
<%@page import="com.dimeng.p2p.S62.entities.T6242"%>
<%@page import="com.dimeng.p2p.common.enums.FundAccountType" %>
<%@page import="com.dimeng.p2p.common.enums.UserType" %>
<%@page import="com.dimeng.p2p.console.servlets.AbstractConsoleServlet" %>
<%@page import="com.dimeng.p2p.console.servlets.bid.gyjz.gyyw.*" %>
<%@page import="com.dimeng.util.ObjectHelper" %>
<%@ page import="com.dimeng.p2p.S62.enums.T6242_F11" %>
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
    CURRENT_SUB_CATEGORY = "GYBDGL";
    PagingResult<GyLoan> result = ObjectHelper.convert(request.getAttribute("loanList"), PagingResult.class);
    GyLoan[] loans = null == result ? null : result.getItems();
    //T6211[] t6211s=ObjectHelper.convertArray(request.getAttribute("t6211s"), T6211.class);
    T6242 searchAmount = (T6242) request.getAttribute("searchAmount");
    String createTimeStart = request.getParameter("createTimeStart");
    String createTimeEnd = request.getParameter("createTimeEnd");
    T6242_F11[] allStatus = T6242_F11.values();
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <form id="form_loan" action="<%=controller.getURI(request, GyLoanList.class)%>" method="post">
                    <div class="p20">
                        <div class="border">
                            <div class="title-container"><i class="icon-i w30 h30 va-middle title-left-icon"></i>公益项目
                            </div>
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
                                    <li><span class="display-ib mr5">状态</span>
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
                                    </li>

                                    <li><span class="display-ib mr5">处理时间</span>
                                        <input type="text" readonly="readonly" name="createTimeStart" id="datepicker1"
                                               class="text border pl5 w120 date"/>
                                        <span class="pl5 pr5">至</span>
                                        <input type="text" readonly="readonly" name="createTimeEnd" id="datepicker2"
                                               class="text border pl5 w120 mr20 date"/>
                                    </li>


                                    <li><a href="javascript:search();" class="btn btn-blue radius-6 mr5 pl1 pr15"><i
                                            class="icon-i w30 h30 va-middle search-icon "></i>搜索</a></li>

                                    <li>
                                        <%
                                            if (dimengSession.isAccessableResource(ExportGyLoan.class)) {
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
                                    <li>
                                        <%if (dimengSession.isAccessableResource(AddGyLoan.class)) {%>
                                        <a href="<%=controller.getURI(request, AddGyLoan.class)%>"
                                           class="btn btn-blue radius-6 mr5 pl1 pr15"><i
                                                class="icon-i w30 h30 va-middle add-icon "></i>新增</a>
                                        <%} else { %>
                                        <span class="btn btn-gray radius-6 mr5 pl1 pr15"><i
                                                class="icon-i w30 h30 va-middle add-icon "></i>新增</span>
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
                                    <th class="tc">公益ID</th>
                                    <th class="tc">公益标题</th>
                                    <th class="tc">公益方</th>
                                    <th class="tc">筹款金额(元)</th>
                                    <th class="tc">已筹金额(元)</th>
                                    <th class="tc">起筹金额(元)</th>
                                    <th class="tc">筹款期限(天)</th>
                                    <th class="tc">处理时间</th>
                                    <th class="tc">状态</th>
                                    <th class="tc">操作</th>
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
                                <tr class="tc">
                                    <td><%=i++ %>
                                    </td>
                                    <td><%=loan.t6242.F21 %>
                                    </td>
                                    <td id="<%=hideLoanId %>"
                                        title="<%StringHelper.filterHTML(out, loan.t6242.F03); %>"><%
                                        StringHelper.filterHTML(out, StringHelper.truncation(loan.t6242.F03, 10)); %></td>
                                    <td title="<%StringHelper.filterHTML(out, loan.t6242.F22); %>"><%
                                        StringHelper.filterHTML(out, StringHelper.truncation(loan.t6242.F22, 10)); %></td>
                                    <td><%=Formater.formatAmount(loan.t6242.F05) %></td>
                                    <td><%=Formater.formatAmount(loan.t6242.F05.subtract(loan.t6242.F07)) %></td>
                                    <td><%=Formater.formatAmount(loan.t6242.F06) %></td>
                                    <td><%out.print(loan.t6242.F08); %></td>
                                    <td><%=DateTimeParser.format(loan.t6242.F16) %></td>
                                    <td>
                                        <%
                                            boolean isTimeEnd = false;
                                            Calendar _calendar = Calendar.getInstance();
                                            if (loan.t6242.F19 == null) {
                                                if (loan.t6242.F13 != null) {
                                                    _calendar.setTime(loan.t6242.F13);
                                                    _calendar.add(Calendar.DAY_OF_MONTH, loan.t6242.F08 - 1);
                                                    if (Calendar.getInstance().getTime().after(_calendar.getTime())) {
                                                        isTimeEnd = true;
                                                    }
                                                }
                                            } else {
                                                isTimeEnd = true;
                                            }
                                        %>
                                        <%if (isTimeEnd) { %>
                                        <%= T6242_F11.YJZ.getChineseName()%>
                                        <%} else {%>
                                        <%StringHelper.filterHTML(out, loan.t6242.F11.getChineseName()); %>
                                        <%}%>
                                    </td>
                                    <td>
                                        <%
                                            if (dimengSession.isAccessableResource(ViewGyLoan.class)) {
                                        %>
                                        <a href="<%=controller.getURI(request, ViewGyLoan.class) %>?loanId=<%=loan.t6242.F01 %>&userId=<%=loan.t6242.F23 %>&enterType=gyyw"
                                           class="link-blue">查看</a>
                                        <%} else { %>
                                        <a href="javascript:void(0)" class="disabled">查看</a>
                                        <%} %>
                                        <%
                                            if (loan.t6242.F11 == T6242_F11.SQZ && dimengSession.isAccessableResource(UpdateGyLoan.class)) {
                                        %>
                                        <a href="<%=controller.getURI(request, UpdateGyLoan.class) %>?loanId=<%=loan.t6242.F01 %>&userId=<%=loan.t6242.F23 %>&enterType=update"
                                           class="link-blue">修改</a>
                                        <%} else { %>
                                        <a href="javascript:void(0)" class="disabled">修改</a>
                                        <%} %>
                                        <%
                                            if (loan.t6242.F11 == T6242_F11.DSH && dimengSession.isAccessableResource(CheckGyBid.class)&& dimengSession.isAccessableResource(ViewGyLoan.class)) {
                                        %>
                                        <a href="<%=controller.getURI(request, ViewGyLoan.class) %>?loanId=<%=loan.t6242.F01 %>&userId=<%=loan.t6242.F23 %>&enterType=gyywsh"
                                           class="link-blue">审核</a>
                                        <%} else { %>
                                        <a href="javascript:void(0)" class="disabled">审核</a>
                                        <%} %>
                                        <%
                                            if (loan.t6242.F11 == T6242_F11.DFB && dimengSession.isAccessableResource(Release.class)) { %>

                                        <a href="javascript:void(0);"
                                           onclick="fbBid('<%=hideLoanId %>','<%=controller.getURI(request, Release.class) %>?loanId=<%=loan.t6242.F01%>')"
                                           class="link-blue">发布</a>
                                        <%--
                                        <a href="javascript:void(0);" onclick="showYfbDiv('<%=loan.t6242.F01%>')">预发布</a>
                                        --%>
                                        <%} else {%>
                                        <a href="javascript:void(0)" class="disabled">发布</a>
                                        <%} %>
                                        <%
                                            if (dimengSession.isAccessableResource(GyBidZf.class)
                                                    && (loan.t6242.F11 == T6242_F11.SQZ || loan.t6242.F11 == T6242_F11.DFB || loan.t6242.F11 == T6242_F11.DSH)) {
                                        %>
                                        <a href="javascript:void(0);" onclick="showZf('<%=loan.t6242.F01 %>');"
                                           class="libk-deepblue popup-link" data-wh="450*250">作废</a>
                                        <%} else {%>
                                        <a href="javascript:void(0)" class="disabled">作废</a>
                                        <%} %>
                                        <%-- 									<%if(loan.F20==T6230_F20.HKZ || loan.F20==T6230_F20.YJQ|| loan.F20==T6230_F20.YDF){ %>
                                                                                    <span>合同</span>
                                                                                <%} %> --%>
                                    </td>
                                </tr>
                                <%
                                    }
                                } else {
                                %>
                                <tr class="tc">
                                    <td colspan="11">暂无数据</td>
                                </tr>
                                <%} %>
                                </tbody>
                            </table>
                        </div>
						<div class="clear"></div>
                        <div class="mb10">
                            <span class="mr30">筹款总金额：<em
                                    class="red"><%=Formater.formatAmount(searchAmount.F05) %>
                            </em> 元</span>
                            <span class="mr30">已筹款总金额：<em
                                    class="red"><%=Formater.formatAmount(searchAmount.F05.subtract(searchAmount.F07)) %>
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
        <div class="popup_bg hide"></div>
    </div>

<div class="popup-box hide" id="cancelDivId" style="min-height: 200px;">
    <form action="<%=controller.getURI(request, GyBidZf.class) %>" id="shForm" method="post" class="form1">
        <input type="hidden" name="loanId" id="cancelLoanId"/>

        <div class="popup-title-container">
            <h3 class="pl20 f18">作废</h3>
            <a class="icon-i popup-close2" href="javascript:void(0);" onclick="closeInfo()"></a>
        </div>
        <div class="popup-content-container pt20 pb20 clearfix">
            <div>
                <ul class="gray6">
                    <li class="mb10"><span class="display-ib tr mr5"><em class="red pr5">*</em><em class="gray3">标的作废描述（50字以内）：</em></span>
                        <textarea name="reason" id="textarea2" rows="3"
                                  class="required ww100 border max-length-50"></textarea>
                        <span tip class="red"></span>
                        <span errortip class="red" style="display: none"></span></li>
                </ul>
            </div>
            <div class="tc f16">
                <input name="button" type="submit" class="btn-blue2 btn white radius-6 pl20 pr20 sumbitForme"
                       id="button3" fromname="form1" value="确认"/>
                <input type="button" class="btn btn-blue2 radius-6 pl20 pr20" onclick="closeInfo()" value="取消"/>
            </div>
        </div>
    </form>
</div>
<div id="info"></div>
<%@include file="/WEB-INF/include/datepicker.jsp" %>
<%
    String warringMessage = controller.getPrompt(request, response, PromptLevel.WARRING);
    if (!StringHelper.isEmpty(warringMessage)) {
%>
<!--弹出框-->
<div class="popup-box">
    <div class="popup-title-container">
        <h3 class="pl20 f18">提示</h3>
        <a class="icon-i popup-close2" href="javascript:void(0);" onclick="closeInfo()"></a>
    </div>
    <div class="popup-content-container pt20 pb20 clearfix">
        <div class="tc mb20 mt40"><span class="icon-i w30 h30 va-middle radius-wrong-icon"></span><span
                class="f20 h30 va-middle ml10"><%StringHelper.filterHTML(out, warringMessage); %></span></div>
        <div class="tc f16"><a href="javascript:void(0);" onclick="closeInfo();"
                               class="btn-blue2 btn white radius-6 pl20 pr20">确定</a></div>
    </div>
</div>
<div class="popup_bg"></div>
<%} %>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/validation.js"></script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/dialog.js"></script>
<script type="text/javascript">
    $(function () {
        $("[name='userType']").val('<%=request.getParameter("userType")%>');
        $('#datepicker1').datepicker('option', {dateFormat: 'yy-mm-dd'});
        $('#datepicker1').datepicker({
            inline: true,
            onSelect: function (selectedDate) {
                $("#datepicker2").datepicker("option", "minDate", selectedDate);
            }
        });
        $('#datepicker2').datepicker('option', {dateFormat: 'yy-mm-dd'});
        $('#datepicker2').datepicker({inline: true});
        <%if(!StringHelper.isEmpty(createTimeStart)){%>
        $("#datepicker1").val("<%StringHelper.filterHTML(out, request.getParameter("createTimeStart"));%>");
        <%}%>
        <%if(!StringHelper.isEmpty(createTimeEnd)){%>
        $("#datepicker2").val("<%StringHelper.filterHTML(out, request.getParameter("createTimeEnd"));%>");
        <%}%>
        $('#datepicker3').datetimepicker({
            timeFormat: 'HH:mm:ss',
            dateFormat: 'yy-mm-dd'
        });
        $("#datepicker2").datepicker('option', 'minDate', $("#datepicker1").datepicker().val());
        //$("#datepicker3").datepicker({inline: true});
        // $('#datepicker3').datepicker('option', {dateFormat:'yy-mm-dd'});
    });
    function showYfbDiv(loanId) {
        $("#loanId").attr("value", loanId);
        $("#yfbDiv").show();
    }

    var _url = "";
    function fbBid(hideLoanId, url) {
        _url = url;
        $(".popup_bg").show();
        $("#info").html(showConfirmDiv("您需要立即发布“" + $("#" + hideLoanId).attr("title") + "”标？", 0, "fb"));
    }

    function toConfirm(msg, i, type) {
        location.href = _url;
    }

    function showZf(loanId) {
        $("#textarea2").val("");
        $("span[errortip]").hide();
        $(".popup_bg").show();
        $("#cancelLoanId").attr("value", loanId);
        $("#cancelDivId").show();
    }

    function search() {
        document.getElementsByName("paging.current")[0].value = 1;
        $("#form_loan").submit();
    }

    function showExport() {
        document.getElementById("form_loan").action = "<%=controller.getURI(request, ExportGyLoan.class)%>";
        $("#form_loan").submit();
        document.getElementById("form_loan").action = "<%=controller.getURI(request, GyLoanList.class)%>";
    }
</script>
</body>
</html>