<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.qyxx.KxrzView"%>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.qyxx.ByrzView"%>
<%@page import="com.dimeng.p2p.modules.account.console.service.entity.XyrzTotal"%>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.qyxx.QyList"%>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.qyxx.tzjl.QyTbjlView" %>
<%@page import="com.dimeng.util.ObjectHelper" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.qyxx.ViewQyxx" %>
<%@page import="com.dimeng.p2p.console.servlets.AbstractConsoleServlet" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.qyxx.ccxx.ViewListCcxx" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.qyxx.fcxx.ViewListFcxx" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.qyxx.lxxx.ViewLxxx" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.qyxx.cwzk.ViewCwzk" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.qyxx.jscl.ViewJscl" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.qyxx.jkxx.ViewJkxx" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.qyxx.dbjl.ViewListDbjl" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.qyxx.dfjl.ViewListDfjl" %>
<%@page import="com.dimeng.p2p.S62.enums.T6230_F20" %>
<%@page import="com.dimeng.p2p.modules.account.console.service.entity.LoanRecord" %>
<%@page import="com.dimeng.p2p.S62.enums.T6231_F21" %>
<%@page import="com.dimeng.p2p.console.servlets.bid.loanmanage.jkgl.LoanList" %>
<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <%@include file="/WEB-INF/include/script.jsp" %>
</head>
<body>
<%
    CURRENT_CATEGORY = "YHGL";
    CURRENT_SUB_CATEGORY = "QY";
    PagingResult<LoanRecord> loanRecords = ObjectHelper.convert(request.getAttribute("info"), PagingResult.class);
    XyrzTotal xyrzTotal = ObjectHelper.convert(request.getAttribute("xyrzTotal"), XyrzTotal.class);
    String createTimeStart = request.getParameter("createTimeStart");
    String createTimeEnd = request.getParameter("createTimeEnd");
    boolean isHasBadClaim = BooleanParser.parse(configureProvider.getProperty(BadClaimVariavle.IS_BADCLAIM_TRANSFER));
    boolean isHasGuarantor = BooleanParser.parse(configureProvider.getProperty(GuarantorVariavle.IS_HAS_GUARANTOR));
    String escrow = configureProvider.getProperty(SystemVariable.ESCROW_PREFIX);
    String returnUrl = controller.getURI(request, QyList.class);
    String operationJK = request.getParameter("operationJK");
    if("CK".equals(operationJK))
    {
        returnUrl = controller.getURI(request, LoanList.class);
    }
    else if("BLZQYZR".equals(operationJK))
    {
        returnUrl = "/console/finance/zjgl/blzq/blzqYzrList.htm";
    }
    else if("BLZQDZR".equals(operationJK))
    {
        returnUrl = "/console/finance/zjgl/blzq/blzqDzrList.htm";
    }
    else if("BLZQDSH".equals(operationJK))
    {
        returnUrl = "/console/finance/zjgl/blzq/blzqDshList.htm";
    }
    else if("BLZQZRZ".equals(operationJK))
    {
        returnUrl = "/console/finance/zjgl/blzq/blzqZrzList.htm";
    }
    else if("BLZQZRSB".equals(operationJK))
    {
        returnUrl = "/console/finance/zjgl/blzq/blzqZrsbList.htm";
    }
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <div class="p20">
                    <div class="border">
                        <div class="title-container"><i class="icon-i w30 h30 va-middle title-left-icon"></i>企业信息
                            <div class="fr mt5">
                                <input type="button" class="btn btn-blue2 radius-6 pl20 pr20 fr mr10" onclick="location.href='<%=returnUrl %>'" value="返回">
                            </div>
                        </div>

                    </div>
                    <!--切换栏目-->
                    <div class="border mt20">
                        <div class="tabnav-container">
                            <ul class="clearfix">
                                <li>
                                    <a href="<%=controller.getURI(request, ViewQyxx.class)%>?id=<%StringHelper.filterHTML(out, request.getParameter("id"));%>&operationJK=<%=operationJK%>"
                                       class="tab-btn ">企业信息<i class="icon-i tab-arrowtop-icon"></i></a></li>
                                <li>
                                    <a href="<%=controller.getURI(request, ViewJscl.class)%>?id=<%StringHelper.filterHTML(out, request.getParameter("id"));%>&operationJK=<%=operationJK%>"
                                       class="tab-btn ">介绍资料</a></li>
                                <li>
                                    <a href="<%=controller.getURI(request, ViewCwzk.class)%>?id=<%StringHelper.filterHTML(out, request.getParameter("id"));%>&operationJK=<%=operationJK%>"
                                       class="tab-btn ">财务状况</a></li>
                                <li>
                                    <a href="<%=controller.getURI(request, ViewLxxx.class)%>?id=<%StringHelper.filterHTML(out, request.getParameter("id"));%>&operationJK=<%=operationJK%>"
                                       class="tab-btn ">联系信息</a></li>
                                <li>
                                    <a href="<%=controller.getURI(request, ViewListCcxx.class)%>?id=<%StringHelper.filterHTML(out, request.getParameter("id"));%>&operationJK=<%=operationJK%>"
                                       class="tab-btn ">车产信息</a></li>
                                <li>
                                    <a href="<%=controller.getURI(request, ViewListFcxx.class)%>?id=<%StringHelper.filterHTML(out, request.getParameter("id"));%>&operationJK=<%=operationJK%>"
                                       class="tab-btn ">房产信息</a></li>
                                <%if(isHasGuarantor && !"huifu".equals(escrow)){ %>
                                <li><a class="tab-btn"
                                       href="<%=controller.getURI(request, ViewListDbjl.class)%>?id=<%StringHelper.filterHTML(out, request.getParameter("id"));%>&operationJK=<%=operationJK%>">担保记录</a>
                                </li>
                                <li><a class="tab-btn"
                                       href="<%=controller.getURI(request, ViewListDfjl.class)%>?id=<%StringHelper.filterHTML(out, request.getParameter("id"));%>&operationJK=<%=operationJK%>">垫付记录</a>
                                </li>
                                <%} %>
                                <li><a href="javascript:void(0);" class="tab-btn select-a">借款记录<i
                                        class="icon-i tab-arrowtop-icon"></i></a></li>
                                <li>
                                    <a class="tab-btn" href="<%=controller.getURI(request, QyTbjlView.class)%>?id=<%=request.getParameter("id") %>&operationJK=<%=operationJK%>">投资记录</a>
                                </li>
                                <li>
                                    <a href="<%=controller.getURI(request, ByrzView.class)%>?id=<%=request.getParameter("id") %>&operationJK=<%=operationJK%>"
                                       class="tab-btn">必要认证（<%=xyrzTotal.byrztg %>/<%=xyrzTotal.needAttestation %>）</a></li>
                                 <li>
	                               <a href="<%=controller.getURI(request, KxrzView.class)%>?id=<%=request.getParameter("id") %>&operationJK=<%=operationJK%>"
	                                  class="tab-btn">可选认证（<%=xyrzTotal.kxrztg %>/<%=xyrzTotal.notNeedAttestation %>）</a></li>
                            </ul>
                        </div>
                        <div class="tab-content-container p20">
                            <form id="form1" action="<%=controller.getURI(request, ViewJkxx.class)%>" method="post">
                                <input type="hidden" id="id" name="id" value="<%=request.getParameter("id")%>">
                                <input type="hidden" id="operationJK" name="operationJK" value="<%=operationJK%>">
                                <div class="tab-item">
                                    <div>
                                        <ul class="gray6 input-list-container clearfix">
                                            <li><span class="display-ib mr5">借款编号</span>
                                                <input type="text" name="loanNum" class="text border pl5 mr20" value='<%StringHelper.filterHTML(out, request.getParameter("loanNum"));%>'/>
                                            </li>
                                            <li><span class="display-ib mr5">借款标题</span>
                                                <input type="text" name="loanRecordTitle" class="text border pl5 mr20"
                                                       value="<%StringHelper.filterHTML(out, request.getParameter("loanRecordTitle"));%>"/>
                                            </li>
                                            <li><span class="display-ib mr5">借款时间</span>
                                                <input type="text" readonly="readonly" name="createTimeStart"
                                                       id="datepicker1" class="text border pl5 w120 date"/>
                                                <span class="pl5 pr5">至</span>
                                                <input type="text" readonly="readonly" name="createTimeEnd"
                                                       id="datepicker2" class="text border pl5 w120 mr20 date"/>
                                            </li>
                                            <li><span class="display-ib mr5">状态</span>
                                                <select name="state" class="border display-ib mr5 h32">
                                                    <option value="">全部</option>
                                                    <option value="TBZ" <%if (T6230_F20.TBZ.name().equals(request.getParameter("state"))) {%>
                                                            selected="selected" <%}%>>投资中
                                                    </option>
                                                    <option value="DFK" <%if (T6230_F20.DFK.name().equals(request.getParameter("state"))) {%>
                                                            selected="selected" <%}%>>待放款
                                                    </option>
                                                    <option value="HKZ" <%if (T6230_F20.HKZ.name().equals(request.getParameter("state"))) {%>
                                                            selected="selected" <%}%>>还款中
                                                    </option>
                                                    <option value="YJQ" <%if (T6230_F20.YJQ.name().equals(request.getParameter("state"))) {%>
                                                            selected="selected" <%}%>>已结清
                                                    </option>
                                                    <option value="YLB" <%if (T6230_F20.YLB.name().equals(request.getParameter("state"))) {%>
                                                            selected="selected" <%}%>>已流标
                                                    </option>
                                                    <option value="YDF" <%if (T6230_F20.YDF.name().equals(request.getParameter("state"))) {%>
                                                            selected="selected" <%}%>>已垫付
                                                    </option>
                                                    <%if (isHasBadClaim){ %>
                                                    <option value="YZR" <%if (T6230_F20.YZR.name().equals(request.getParameter("state"))) {%>
                                                            selected="selected" <%}%>>已转让
                                                    </option>
                                                    <%} %>
                                                </select>
                                            </li>
                                            <li><a class="btn btn-blue radius-6 mr5 pl1 pr15"
                                                   href="javascript:$('#form1').submit();"><i
                                                    class="icon-i w30 h30 va-middle search-icon "></i>搜索</a></li>
                                        </ul>
                                    </div>
                                    <div class="border table-container">
                                        <table class="table table-style gray6 tl">
                                            <thead>
                                            <tr class="title tc">
                                                <th>序号</th>
                                                <th>借款编号</th>
                                                <th>借款标题</th>
                                                <th>年化利率</th>
                                                <th>借款金额</th>
                                                <th>投资金额</th>
                                                <th>期限</th>
                                                <th>借款时间</th>
                                                <th>状态</th>
                                            </tr>
                                            </thead>
                                            <tbody class="f12">
                                            <%
                                                LoanRecord[] loanRecordArray = loanRecords.getItems();
                                                if (loanRecordArray != null) {
                                                    int i = 1;
                                                    for (LoanRecord loanRecord : loanRecordArray) {
                                                        if (loanRecord == null) {
                                                            continue;
                                                        }
                                            %>
                                            <tr class="tc">
                                                <td><%=i++%></td>
                                                <td align="center"><%=loanRecord.loanNum%>
                                                </td>
                                                <td title="<%=loanRecord.loanRecordTitle%>"><%
                                                    StringHelper.filterHTML(out, StringHelper.truncation(loanRecord.loanRecordTitle, 10));%></td>
                                                <td align="center"><%=Formater.formatRate(loanRecord.yearRate)%>
                                                </td>
                                                <td align="center"><%=Formater.formatAmount(loanRecord.loanAmount)%>元
                                                </td>
                                                <td align="center"><%=Formater.formatAmount(loanRecord.amount)%>元</td>
                                                <%if (T6231_F21.S == loanRecord.dayBorrowFlg) { %>
                                                <td align="center"><%=loanRecord.deadline%>天</td>
                                                <%} else { %>
                                                <td align="center"><%=loanRecord.deadline%>个月</td>
                                                <%} %>
                                                <td align="center"><%=TimestampParser.format(loanRecord.loanRecordTime)%>
                                                </td>
                                                <%-- <td align="center"><%=loanRecord.overdueCount%></td>
                                                <td align="center"><%=loanRecord.seriousOverdue%></td> --%>
                                                <td align="center"><%
                                                    if (loanRecord.loanRecordState == T6230_F20.HKZ) {
                                                        out.print("还款中");
                                                    } else {
                                                        out.print(loanRecord.loanRecordState.getChineseName());
                                                    }
                                                %></td>
                                            </tr>
                                            <% }
                                            } else {%>
                                            <tr>
                                                <td colspan="9" class="tc" style="border-bottom: 0px;">暂无数据</td>
                                            </tr>
                                            <%} %>

                                            </tbody>
                                        </table>

                                    </div>
                                    <%
                                        AbstractConsoleServlet.rendPagingResult(out, loanRecords);
                                    %>
                                </div>

                            </form>
                        </div>
                    </div>
                </div>
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
</script>
</body>
</html>