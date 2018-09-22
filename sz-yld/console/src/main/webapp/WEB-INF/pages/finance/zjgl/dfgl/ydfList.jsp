<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="java.util.Calendar"%>
<%@page import="java.util.Date"%>
<%@page import="com.dimeng.p2p.S51.enums.T5131_F02" %>
<%@page import="com.dimeng.p2p.S62.enums.T6230_F10" %>
<%@page import="com.dimeng.p2p.console.servlets.finance.zjgl.dfgl.YdfList" %>
<%@page import="com.dimeng.p2p.console.servlets.finance.zjgl.dfgl.DcYdf" %>
<%@page import="com.dimeng.p2p.console.servlets.AbstractConsoleServlet" %>
<%@page import="com.dimeng.p2p.modules.finance.console.service.entity.DfRecord" %>

<html dir="ltr" xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-CN" lang="zh-CN">
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <%@include file="/WEB-INF/include/script.jsp"%>
</head>

<body>
<%
    CURRENT_CATEGORY = "CWGL";
    CURRENT_SUB_CATEGORY = "DFGL";
    PagingResult<DfRecord> dfRecord = (PagingResult<DfRecord>) request.getAttribute("dfRecord");
    DfRecord ydfSearchAmount = (DfRecord) request.getAttribute("ydfSearchAmount");
    DfRecord[] dfRecordArray = dfRecord.getItems();
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <form id="form1" name="form1" action="<%=controller.getURI(request, YdfList.class)%>" method="post">
                    <div class="p20">
                        <div class="border">
                            <div class="title-container"><i class="icon-i w30 h30 va-middle title-left-icon"></i>不良资产管理
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
                                    <li><span class="display-ib mr5">借款用户名： </span>
                                        <input type="text" name="loanName"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("loanName"));%>"
                                               class="text border pl5 mr20"/>
                                    </li>
                                    <li><span class="display-ib mr5">逾期天数： </span>
                                        <input type="text" name="yuqiFromDays" onKeyUp="value=value.replace(/\D/g,'')"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("yuqiFromDays"));%>"
                                               class="text border pl5 w120 "/>
                                        <span class="pl5 pr5">至</span>
                                        <input type="text" name="yuqiEndDays" onKeyUp="value=value.replace(/\D/g,'')"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("yuqiEndDays"));%>"
                                               class="text border pl5 w120 mr20 "/>
                                    </li>
                                    <li><span class="display-ib mr5">还款方式： </span>
                                        <select name="hkfs" class="border mr20 h32 mw100">
                                            <option value="">全部</option>
                                            <option value="<%=T6230_F10.DEBX.name()%>"
                                                    <%if("DEBX".equals(request.getParameter("hkfs"))){ %>selected="selected" <%} %>><%=T6230_F10.DEBX.getChineseName() %>
                                            </option>
                                            <option value="<%=T6230_F10.MYFX.name()%>"
                                                    <%if("MYFX".equals(request.getParameter("hkfs"))){ %>selected="selected" <%} %>><%=T6230_F10.MYFX.getChineseName() %>
                                            </option>
                                            <option value="<%=T6230_F10.YCFQ.name()%>"
                                                    <%if("YCFQ".equals(request.getParameter("hkfs"))){ %>selected="selected" <%} %>><%=T6230_F10.YCFQ.getChineseName() %>
                                            </option>
                                            <option value="<%=T6230_F10.DEBJ.name()%>"
                                                    <%if("DEBJ".equals(request.getParameter("hkfs"))){ %>selected="selected" <%} %>><%=T6230_F10.DEBJ.getChineseName() %>
                                            </option>
                                        </select>
                                    </li>
                                    <li><span class="display-ib mr5">垫付方式： </span>
                                        <select name="dffs" class="border mr20 h32 mw100">
                                            <option value="">全部</option>
                                            <option value="<%=T5131_F02.BJ.name()%>"
                                                    <%if("BJ".equals(request.getParameter("dffs"))){ %>selected="selected" <%} %>><%=T5131_F02.BJ.getChineseName() %>
                                            </option>
                                            <option value="<%=T5131_F02.BX.name()%>"
                                                    <%if("BX".equals(request.getParameter("dffs"))){ %>selected="selected" <%} %>><%=T5131_F02.BX.getChineseName() %>
                                            </option>
                                        </select>
                                    </li>
                                    <li><a href="javascript:onSubmit();" 
                                           class="btn btn-blue radius-6 mr5 pl1 pr15"><i
                                            class="icon-i w30 h30 va-middle search-icon "></i>搜索</a></li>
                                    <li>
                                        <%if (dimengSession.isAccessableResource(DcYdf.class)) {%>
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
                            <div class="tabnav-container">
                                <ul class="clearfix">
                                    <li id="one1"><a
                                            href="<%=controller.getStaticPath(request)%>/finance/zjgl/dfgl/yqddfList.htm"
                                            class="tab-btn-click">逾期待垫付</a></li>
                                    <li id="one2"><a href="javascript:void(0);" class="tab-btn-click select-a">垫付待还款<i class="icon-i tab-arrowtop-icon"></i></a>
                                    </li>
                                    <li id="one3"><a
                                            href="<%=controller.getStaticPath(request)%>/finance/zjgl/dfgl/yjqList.htm"
                                            class="tab-btn-click">已结清</a></li>
                                </ul>
                            </div>
                            <div class=" table-container p20">
                                <table class="table table-style gray6 tl">
                                    <thead>
                                    <tr class="title">
                                        <th class="tc">序号</th>
                                        <th class="tc">借款编号</th>
                                        <th class="tc">借款标题</th>
                                        <th class="tc">借款用户名</th>
                                        <th class="tc">借款金额(元)</th>
                                        <th class="tc">还款方式</th>
                                        <th class="tc">剩余期数</th>
                                        <th class="tc">应还本金(元)</th>
                                        <th class="tc">应还利息(元)</th>
                                        <th class="tc">逾期罚息(元)</th>
                                        <th class="tc">逾期时间</th>
                                        <th class="tc">逾期天数</th>

                                        <th class="tc">垫付时间</th>
                                        <th class="tc">垫付方式</th>
                                        <th class="tc">垫付金额(元)</th>
                                        <th class="tc">垫付返还金额(元)</th>
                                        <th class="tc">垫付操作人</th>
                                    </tr>
                                    </thead>
                                    <tbody class="f12">
                                    <%
                                        if (dfRecordArray != null) {
                                            for (int i = 0; i < dfRecordArray.length; i++) {
                                                DfRecord yqddfRecord = dfRecordArray[i];
                                                if (yqddfRecord == null) {
                                                    continue;
                                                }
                                    %>
                                    <tr class="tc">
                                        <td><%=i + 1%>
                                        </td>
                                        <td><%StringHelper.filterHTML(out, yqddfRecord.bidNo);%></td>
                                        <td title="<%=yqddfRecord.loanRecordTitle%>"><%
                                            StringHelper.filterHTML(out, StringHelper.truncation(yqddfRecord.loanRecordTitle, 10)); %></td>
                                        <td><%StringHelper.filterHTML(out, yqddfRecord.loanName); %></td>
                                        <td><%=Formater.formatAmount(yqddfRecord.loanAmount)%>
                                        </td>
                                        <td><%=yqddfRecord.hkfs.getChineseName()%>
                                        </td>
                                        <td><%=yqddfRecord.loandeadline%>
                                        </td>
                                        <td><%=Formater.formatAmount(yqddfRecord.dhbj)%>
                                        </td>
                                        <%if(yqddfRecord.dfmethod != null && T5131_F02.BJ.name().equals(yqddfRecord.dfmethod.name()))
                                        {%>
                                            <td><%=Formater.formatAmount(0)%></td>
                                        <%}
                                        else
                                        {%>
                                            <td><%=Formater.formatAmount(yqddfRecord.dhlx)%></td>
                                        <%}%>
                                        <td><%=Formater.formatAmount(yqddfRecord.yuqiAmount)%>
                                        </td>
                                        <td>
                                            <%
                                                Date refunDay = yqddfRecord.refundDay;
                                                Calendar yqCal = Calendar.getInstance();
                                                if (refunDay != null) {
                                                    yqCal.setTime(refunDay);
                                                    yqCal.add(Calendar.DAY_OF_MONTH, 1);
                                                }
                                            %>
                                            <%=Formater.formatDate(refunDay == null ? null : yqCal.getTime())%>
                                        </td>
                                        <td><%=yqddfRecord.yuqi%>
                                        </td>
                                        <td><%=Formater.formatDateTime(yqddfRecord.dfTime)%>
                                        </td>
                                        <td><%=yqddfRecord.dfmethod == null ? "" : yqddfRecord.dfmethod.getChineseName()%>
                                        </td>
                                        <td><%=Formater.formatAmount(yqddfRecord.ydfAmount)%>
                                        </td>
                                        <td><%=Formater.formatAmount(yqddfRecord.dffhAmount)%>
                                        </td>
                                        <td><%=yqddfRecord.operate%>
                                        </td>
                                    </tr>
                                    <%
                                        }
                                    } else {
                                    %>
                                    <tr>
                                        <td colspan="17" class="tc">暂无数据</td>
                                    </tr>
                                    <%} %>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                        <div class="clear"></div>
                        <div class="mb10">
                            <span class="mr30">借款总金额：<em
                                    class="red"><%=Formater.formatAmount(ydfSearchAmount.loanAmount) %>
                            </em> 元</span>
                            <span class="mr30">应还本金总金额：<em
                                    class="red"><%=Formater.formatAmount(ydfSearchAmount.dhbj) %>
                            </em> 元</span>
                            <span class="mr30">应还利息总金额：<em
                                    class="red"><%=Formater.formatAmount(ydfSearchAmount.dhlx) %>
                            </em> 元</span>
                            <span class="mr30">逾期罚息总金额：<em
                                    class="red"><%=Formater.formatAmount(ydfSearchAmount.yuqiAmount) %>
                            </em> 元</span>
                            <span class="mr30">垫付总金额：<em
                                    class="red"><%=Formater.formatAmount(ydfSearchAmount.ydfAmount) %>
                            </em> 元</span>
                            <span class="mr30">垫付返还总金额：<em
                                    class="red"><%=Formater.formatAmount(ydfSearchAmount.dffhAmount) %>
                            </em> 元</span>
                        </div>
                        <!--分页-->
                        <%AbstractConsoleServlet.rendPagingResult(out, dfRecord); %>
                        <!--分页 end-->
                    </div>
                </form>
            </div>
        </div>
    </div>
    <!--右边内容 结束-->
<%@include file="/WEB-INF/include/datepicker.jsp" %>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/validation.js"></script>
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
        <%if (dfRecordArray != null) {for (int i =0;i<dfRecordArray.length;i++){DfRecord yqddfRecord=dfRecordArray[i];if (yqddfRecord == null) {continue;}%>
        $('#tempDatepicker<%=i%>').datepicker({
            inline: true,
            onSelect: function (selectedDate) {
                checkDate('<%=i%>');
            }
        });
        $('#tempDatepicker<%=i%>').datepicker('option', {dateFormat: 'yy-mm-dd'});
        <%}}%>
        $("#datepicker1").datepicker("setDate", "<%StringHelper.filterHTML(out, request.getParameter("createTimeStart"));%>");
        $("#datepicker2").datepicker("setDate", "<%StringHelper.filterHTML(out, request.getParameter("createTimeEnd"));%>");
        $("#datepicker2").datepicker('option', 'minDate', $("#datepicker1").datepicker().val());
    });

    function showExport() {
        document.getElementById("form1").action = "<%=controller.getURI(request, DcYdf.class)%>";
        $("#form1").submit();
        document.getElementById("form1").action = "<%=controller.getURI(request, YdfList.class)%>";
    }

    function showDf(i) {
        $("#df_" + i).show();
    }

    function showCk(i) {
        $("#ck_" + i).show();
    }

    function checkDate(i) {
        //window.setTimeout(function(){
        var dates = $("#tempDatepicker" + i);
        var value = dates.val();
        var $error = dates.nextAll("p[errortip]");
        var $tip = dates.nextAll("p[tip]");
        if ($.trim(value) != "") {
            $error.removeClass("error_tip");
            $error.hide();
            $tip.show();
        }
        //},2000);
    }
    function onSubmit(){
    	$("input[name='<%=AbstractConsoleServlet.PAGING_CURRENT%>']").val('1');
    	$('#form1').submit();
    }
</script>
</body>
</html>