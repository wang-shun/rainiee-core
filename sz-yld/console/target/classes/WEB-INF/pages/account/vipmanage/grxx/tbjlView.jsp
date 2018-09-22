<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.grxx.GrList"%>
<%@page import="com.dimeng.p2p.console.servlets.AbstractConsoleServlet" %>
<%@page import="com.dimeng.p2p.S62.enums.T6230_F20" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.grxx.TbjlView" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.grxx.JkjlView" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.grxx.KxrzView" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.grxx.ByrzView" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.grxx.JbxxView" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.grxx.GrxlxxView" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.grxx.GrgzxxView" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.grxx.FcxxView" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.grxx.CcxxView" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.grxx.ViewListDbjl" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.grxx.ViewListDfjl" %>
<%@page import="com.dimeng.p2p.modules.account.console.service.entity.TenderRecord" %>
<%@page import="com.dimeng.p2p.modules.account.console.service.entity.BasicInfo" %>
<%@page import="com.dimeng.util.ObjectHelper" %>
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
    CURRENT_SUB_CATEGORY = "GRXX";
    BasicInfo basicInfo = ObjectHelper.convert(request.getAttribute("basicInfo"), BasicInfo.class);
    PagingResult<TenderRecord> tenderRecords = ObjectHelper.convert(request.getAttribute("tenderRecords"), PagingResult.class);
    String createTimeStart = request.getParameter("createTimeStart");
    String createTimeEnd = request.getParameter("createTimeEnd");
    boolean is_business = Boolean.parseBoolean(configureProvider.getProperty(BusinessVariavle.IS_BUSINESS));
    boolean isHasBadClaim = BooleanParser.parse(configureProvider.getProperty(BadClaimVariavle.IS_BADCLAIM_TRANSFER));
    boolean isHasGuarantor = BooleanParser.parse(configureProvider.getProperty(GuarantorVariavle.IS_HAS_GUARANTOR));
    String escrow = configureProvider.getProperty(SystemVariable.ESCROW_PREFIX);
    String returnUrl = controller.getURI(request, GrList.class);
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
                        <div class="title-container"><i class="icon-i w30 h30 va-middle title-left-icon"></i>用户详细信息
                            <div class="fr mt5">
                                <input type="button" class="btn btn-blue2 radius-6 pl20 pr20 fr mr10" onclick="location.href='<%=returnUrl %>'" value="返回">
                            </div>
                        </div>
                        <div class="content-container pl40 pt30 pr40">
                            <ul class="gray9 input-list-container clearfix">
                                <li><span class="display-ib mr5">用户名：</span><span
                                        class="display-ib mr40 gray3"><%StringHelper.filterHTML(out, basicInfo.userName);%></span>
                                </li>
                                <li><span class="display-ib mr5">账户余额：</span><span
                                        class="display-ib mr40 gray3"><%=Formater.formatAmount(basicInfo.accountBalance)%>元</span>
                                </li>
                                <li><span class="display-ib mr5">必要认证：</span>
        	<span class="display-ib mr40 gray3">
        		<%
                    int byrztg = basicInfo.byrztg;
                    if (byrztg <= 0) {
                %>
							     	未认证
							     <%
                                 } else {
                                 %>
							   	 通过<%=byrztg%>项
							   	 <%
                                     }
                                 %>
        	</span>
                                </li>
                                <li><span class="display-ib mr5">借款负债：</span><span
                                        class="display-ib mr40 gray3"> <%=Formater.formatAmount(basicInfo.borrowingLiability)%>元</span>
                                </li>
                                <li><span class="display-ib mr5">注册时间：</span><span
                                        class="display-ib mr40 gray3"><%=TimestampParser.format(basicInfo.registrationTime)%></span>
                                </li>
                                <li><span class="display-ib mr5">净资产：</span><span
                                        class="display-ib mr40 gray3"> <%=Formater.formatAmount(basicInfo.netAssets)%>元 </span>
                                </li>
                                <li><span class="display-ib mr5">理财资产：</span><span
                                        class="display-ib mr40 gray3"><%=Formater.formatAmount(basicInfo.lczc)%>元 </span>
                                </li>
                                <li><span class="display-ib mr5">可选认证：</span>
        	<span class="display-ib mr40 gray3">
        		<%
                    int kxrztg = basicInfo.kxrztg;
                    if (kxrztg <= 0) {
                %>
							     	未认证
							     <%
                                 } else {
                                 %>
							   	 通过<%=kxrztg%>项
							   	 <%
                                     }
                                 %>
        	</span>
                                </li>
                                <li><span class="display-ib mr5">逾期次数：</span><span
                                        class="display-ib mr40 gray3"><%=basicInfo.overdueCount%></span></li>
                                <li><span class="display-ib mr5">严重逾期次数：</span><span
                                        class="display-ib mr40 gray3"><%=basicInfo.seriousOverdue%></span></li>
                                <%if(is_business){ %>
                                <li><span class="display-ib mr5">业务员工号：</span><span class="display-ib mr40 gray3"><%=StringHelper.isEmpty(basicInfo.employNum)?"无":basicInfo.employNum %></span></li>
                                <%} %>
                            </ul>
                        </div>
                    </div>

                    <!--切换栏目-->
                    <div class="border mt20">
                        <div class="tabnav-container">
                            <ul class="clearfix border-b-s">
                                <li>
                                    <a href="<%=controller.getURI(request, JbxxView.class)%>?userId=<%=basicInfo.userId%>&operationJK=<%=operationJK%>"
                                       class="tab-btn">基本信息<i class="icon-i tab-arrowtop-icon"></i></a></li>
                                <li>
                                    <a href="<%=controller.getURI(request, GrxlxxView.class)%>?userId=<%=basicInfo.userId%>&operationJK=<%=operationJK%>"
                                       class="tab-btn">个人学历信息</a></li>
                                <li>
                                    <a href="<%=controller.getURI(request, GrgzxxView.class)%>?userId=<%=basicInfo.userId%>&operationJK=<%=operationJK%>"
                                       class="tab-btn">个人工作信息</a></li>
                                <li>
                                    <a href="<%=controller.getURI(request, FcxxView.class)%>?userId=<%=basicInfo.userId%>&operationJK=<%=operationJK%>"
                                       class="tab-btn">房产信息</a></li>
                                <li>
                                    <a href="<%=controller.getURI(request, CcxxView.class)%>?userId=<%=basicInfo.userId%>&operationJK=<%=operationJK%>"
                                       class="tab-btn">车产信息</a></li>
                                <li>
                                    <a href="<%=controller.getURI(request, ByrzView.class)%>?userId=<%=basicInfo.userId%>&operationJK=<%=operationJK%>"
                                       class="tab-btn">必要认证（<%=byrztg%>/<%=basicInfo.needAttestation%>）</a></li>
                                <li>
                                    <a href="<%=controller.getURI(request, KxrzView.class)%>?userId=<%=basicInfo.userId%>&operationJK=<%=operationJK%>"
                                       class="tab-btn">可选认证（<%=kxrztg%>/<%=basicInfo.notNeedAttestation%>）</a></li>
                                <%if(isHasGuarantor && !"huifu".equals(escrow)){ %>
                                <li><a class="tab-btn"
                                       href="<%=controller.getURI(request, ViewListDbjl.class)%>?userId=<%=basicInfo.userId%>&operationJK=<%=operationJK%>">担保记录</a>
                                </li>
                                <li><a class="tab-btn"
                                       href="<%=controller.getURI(request, ViewListDfjl.class)%>?userId=<%=basicInfo.userId%>&operationJK=<%=operationJK%>">垫付记录</a>
                                </li>
                                <%} %>
                                <li>
                                    <a href="<%=controller.getURI(request, JkjlView.class)%>?userId=<%=basicInfo.userId%>&operationJK=<%=operationJK%>"
                                       class="tab-btn">借款记录</a></li>
                                <li><a href="javascript:void(0)" class="tab-btn select-a">投资记录<i
                                        class="icon-i tab-arrowtop-icon"></i></a></li>
                            </ul>
                        </div>
                        <div class="tab-content-container p20">
                            <form id="form1"
                                  action="<%=controller.getURI(request, TbjlView.class)%>?userId=<%=basicInfo.userId%>&operationJK=<%=operationJK%>"
                                  method="post">
                                <div class="tab-item">
                                    <div>
                                        <ul class="gray6 input-list-container clearfix">
                                            <li><span class="display-ib mr5">借款编号</span>
                                                <input type="text" name="tenderRecordId" class="text border pl5 mr20" value='<%StringHelper.filterHTML(out, request.getParameter("tenderRecordId"));%>'/>
                                            </li>
                                            <li><span class="display-ib mr5">借款标题</span>
                                                <input type="text" name="tenderRecordTitle" class="text border pl5 mr20" value='<%StringHelper.filterHTML(out, request.getParameter("tenderRecordTitle"));%>'/>
                                            </li>
                                            <li><span class="display-ib mr5">投资时间</span>
                                                <input type="text" readonly="readonly" name="createTimeStart"
                                                       id="datepicker1" class="text border pl5 w120 date"/>
                                                <span class="pl5 pr5">至</span>
                                                <input type="text" readonly="readonly" name="createTimeEnd"
                                                       id="datepicker2" class="text border pl5 w120 mr20 date"/>
                                            </li>
                                            <li><span class="display-ib mr5">状态</span>
                                                <select name="loanRecordState" class="border display-ib mr5 h32">
                                                    <option value="">全部</option>
                                                    <option value="TBZ" <%if (T6230_F20.TBZ.name().equals(request.getParameter("loanRecordState"))) {%>
                                                            selected="selected" <%}%>>投资中
                                                    </option>
                                                    <option value="DFK" <%if (T6230_F20.DFK.name().equals(request.getParameter("loanRecordState"))) {%>
                                                            selected="selected" <%}%>>待放款
                                                    </option>
                                                    <option value="HKZ" <%if (T6230_F20.HKZ.name().equals(request.getParameter("loanRecordState"))) {%>
                                                            selected="selected" <%}%>>还款中
                                                    </option>
                                                    <option value="YJQ" <%if (T6230_F20.YJQ.name().equals(request.getParameter("loanRecordState"))) {%>
                                                            selected="selected" <%}%>>已结清
                                                    </option>
                                                    <option value="YLB" <%if (T6230_F20.YLB.name().equals(request.getParameter("loanRecordState"))) {%>
                                                            selected="selected" <%}%>>已流标
                                                    </option>
                                                    <option value="YDF" <%if (T6230_F20.YDF.name().equals(request.getParameter("loanRecordState"))) {%>
                                                            selected="selected" <%}%>>已垫付
                                                    </option>
                                                    <%if (isHasBadClaim){ %>
                                                    <option value="YZR" <%if (T6230_F20.YZR.name().equals(request.getParameter("loanRecordState"))) {%>
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
                                                <th>金额</th>
                                                <th>年化利率</th>
                                                <th>期限</th>
                                                <th>投资时间</th>
                                                <th>状态</th>
                                            </tr>
                                            </thead>
                                            <tbody class="f12">
                                            <%
                                                TenderRecord[] tenderRecordArray = tenderRecords.getItems();
                                                if (tenderRecordArray != null) {
                                                    int i = 1;
                                                    for (TenderRecord tenderRecord : tenderRecordArray) {
                                                        if (tenderRecord == null) {
                                                            continue;
                                                        }
                                            %>
                                            <tr class="tc">
                                                <td><%=i++%></td>
                                                <td><%=tenderRecord.tenderRecordId%></td>
                                                <td title="<%=tenderRecord.tenderRecordTitle%>"><%
                                                    StringHelper.filterHTML(out, StringHelper.truncation(tenderRecord.tenderRecordTitle, 10));%></td>
                                                <td><%=Formater.formatAmount(tenderRecord.tenderMoney)%>元</td>
                                                <td><%=Formater.formatRate(tenderRecord.yearRate)%></td>
                                                <%if (T6231_F21.S == tenderRecord.dayBorrowFlg) { %>
                                                <td><%=tenderRecord.deadline%>天</td>
                                                <%} else { %>
                                                <td><%=tenderRecord.deadline%>个月</td>
                                                <%} %>
                                                <td><%=TimestampParser.format(tenderRecord.tenderTime)%></td>
                                                <td><%=tenderRecord.tenderRecordState.getChineseName()%></td>
                                            </tr>
                                            <% }
                                            } else {%>
                                            <tr>
                                                <td colspan="8" class="tc">暂无数据</td>
                                            </tr>
                                            <%} %>

                                            </tbody>
                                        </table>

                                    </div>
                                </div>
                                <%
                                    AbstractConsoleServlet.rendPagingResult(out, tenderRecords);
                                %>
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