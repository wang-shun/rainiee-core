<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.util.parser.TimestampParser"%>
<%@page import="com.dimeng.util.Formater"%>
<%@page import="com.dimeng.util.StringHelper"%>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.jgxx.blzqgmjl.BlzqGmjlView"%>
<%@page import="com.dimeng.p2p.console.servlets.AbstractBadClaimServlet"%>
<%@page import="com.dimeng.p2p.repeater.claim.entity.BuyBadClaimRecord"%>
<%@page import="com.dimeng.framework.service.query.PagingResult"%>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.jgxx.JgList"%>
<%@page import="com.dimeng.p2p.console.servlets.bid.loanmanage.jkgl.LoanList" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.jgxx.tzjl.JgTbjlView" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.jgxx.ViewJgxx" %>
<%@page import="com.dimeng.p2p.variables.defines.SystemVariable"%>
<%@page import="com.dimeng.p2p.S62.enums.T6230_F20" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.jgxx.lxxx.ViewLxxx" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.jgxx.fcxx.ViewListFcxx" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.jgxx.ccxx.ViewListCcxx" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.jgxx.cwzk.ViewCwzk" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.jgxx.jscl.ViewJscl" %>
<%@page import="com.dimeng.util.ObjectHelper" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.jgxx.dbjl.ViewListDbjl" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.jgxx.dfjl.ViewListDfjl" %>
<%@page import="com.dimeng.util.ObjectHelper" %>
<%@page import="com.dimeng.p2p.S62.enums.T6231_F21" %>
<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <%@include file="/WEB-INF/include/script.jsp" %>
</head>
<body>
<%
	CURRENT_CATEGORY = "YHGL";
	CURRENT_SUB_CATEGORY = "JG";
    PagingResult<BuyBadClaimRecord> badClaimRecords = ObjectHelper.convert(request.getAttribute("badClaimRecords"), PagingResult.class);
    String startTime = request.getParameter("startTime");
    String endTime = request.getParameter("endTime");
    //托管前缀
    String ESCROW_PREFIX = configureProvider.getProperty(SystemVariable.ESCROW_PREFIX);
    String ckUrl = controller.getURI(request, JgList.class);
    String operationJK = request.getParameter("operationJK");
    if("CK".equals(operationJK))
    {
        ckUrl = controller.getURI(request, LoanList.class);
    }
    else if("BLZQYZR".equals(operationJK))
    {
        ckUrl = "/console/finance/zjgl/blzq/blzqYzrList.htm";
    }
    else if("BLZQDZR".equals(operationJK))
    {
        ckUrl = "/console/finance/zjgl/blzq/blzqDzrList.htm";
    }
    else if("BLZQDSH".equals(operationJK))
    {
        ckUrl = "/console/finance/zjgl/blzq/blzqDshList.htm";
    }
    else if("BLZQZRZ".equals(operationJK))
    {
        ckUrl = "/console/finance/zjgl/blzq/blzqZrzList.htm";
    }
    else if("BLZQZRSB".equals(operationJK))
    {
        ckUrl = "/console/finance/zjgl/blzq/blzqZrsbList.htm";
    }
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <div class="p20">
                    <div class="border">
                        <div class="title-container"><i class="icon-i w30 h30 va-middle title-left-icon"></i>机构信息
                            <div class="fr mt5">
                                <input type="button" class="btn btn-blue2 radius-6 pl20 pr20 fr mr10" onclick="location.href='<%=ckUrl%>'" value="返回">
                            </div>
                        </div>

                    </div>

                    <!--切换栏目-->
                    <div class="border mt20">
                        <div class="tabnav-container">
                            <ul class="clearfix">
                                <li><a href="<%=controller.getURI(request, ViewJgxx.class)%>?id=<%StringHelper.filterHTML(out, request.getParameter("id"));%>&operationJK=<%=operationJK%>"
                                       class="tab-btn ">机构信息<i class="icon-i tab-arrowtop-icon"></i></a></li>
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
                                <li><a class="tab-btn"
                                       href="<%=controller.getURI(request, ViewListDbjl.class)%>?id=<%StringHelper.filterHTML(out, request.getParameter("id"));%>&operationJK=<%=operationJK%>">担保记录</a>
                                </li>
                                <li><a class="tab-btn"
                                       href="<%=controller.getURI(request, ViewListDfjl.class)%>?id=<%StringHelper.filterHTML(out, request.getParameter("id"));%>&operationJK=<%=operationJK%>">垫付记录</a>
                                </li>
                                <li><a href="javascript:void(0);" class="tab-btn select-a">不良债权购买记录</a></li>
                                <%if(!"huifu".equalsIgnoreCase(ESCROW_PREFIX)){ %>
                                <li>
                                    <a class="tab-btn" href="<%=controller.getURI(request, JgTbjlView.class)%>?id=<%=request.getParameter("id") %>&operationJK=<%=operationJK%>">投资记录</a>
                                </li>
                                <%} %>
                            </ul>
                        </div>
                        <div class="tab-content-container p20">
                            <form id="form1"
                                  action="<%=controller.getURI(request, BlzqGmjlView.class)%>?userId=<%=request.getParameter("id")%>"
                                  method="post">
		                        <input type="hidden" id="id" name="id" value="<%=request.getParameter("id")%>">
		                        <input type="hidden" id="operationJK" name="operationJK" value="<%=operationJK%>">
                                <div class="tab-item">
                                    <div>
                                        <ul class="gray6 input-list-container clearfix">
                                            <li><span class="display-ib mr5">债权编号</span>
                                                <input type="text" name="creditNo" class="text border pl5 mr20" value='<%StringHelper.filterHTML(out, request.getParameter("creditNo"));%>'/>
                                            </li>
                                            <li><span class="display-ib mr5">借款标题</span>
                                                <input type="text" name="loanTitle" class="text border pl5 mr20" value='<%StringHelper.filterHTML(out, request.getParameter("loanTitle"));%>'/>
                                            </li>
                                            <li><span class="display-ib mr5">认购时间</span>
                                                <input type="text" readonly="readonly" name="startTime"
                                                       id="datepicker1" class="text border pl5 w120 date"/>
                                                <span class="pl5 pr5">至</span>
                                                <input type="text" readonly="readonly" name="endTime"
                                                       id="datepicker2" class="text border pl5 w120 mr20 date"/>
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
                                                <th>债权编号</th>
                                                <th>借款标题</th>
                                                <th>借款金额（元）</th>
                                                <th>转让期数</th>
                                                <th>债权价值（元）</th>
                                                <th>购买价格（元）</th>
                                                <th>认购时间</th>
                                            </tr>
                                            </thead>
                                            <tbody class="f12">
                                            <%
                                            	if(badClaimRecords!=null){
                                            	   BuyBadClaimRecord[] badClaimRecordArray = badClaimRecords.getItems();
                                                if (badClaimRecordArray != null) {
                                                    int i = 1;
                                                    for (BuyBadClaimRecord badClaimRecord : badClaimRecordArray) {
                                                        if (badClaimRecord == null) {
                                                            continue;
                                                        }
                                            %>
                                            <tr class="tc">
                                                <td><%=i++%>
                                                </td>
                                                <td><%=badClaimRecord.creditNo%></td>
                                                <td title="<%=badClaimRecord.loanTitle%>"><%
                                                    StringHelper.filterHTML(out, StringHelper.truncation(badClaimRecord.loanTitle, 10));%></td>
                                                <td><%=Formater.formatAmount(badClaimRecord.loanAmount)%></td>
                                                <td><%=badClaimRecord.syPeriods%>/<%=badClaimRecord.zPeriods%>
                                                </td>
                                                <td><%=Formater.formatAmount(badClaimRecord.creditPrice)%></td>
                                                <td><%=Formater.formatAmount(badClaimRecord.subscribePrice)%></td>
                                                <td><%=TimestampParser.format(badClaimRecord.subscribeTime)%>
                                                </td>
                                            </tr>
                                            <% }
                                            } else {%>
                                            <tr>
                                                <td colspan="8" class="tc">暂无数据</td>
                                            </tr>
                                            <%} } else {%>
                                            <tr>
                                                <td colspan="8" class="tc">暂无数据</td>
                                            </tr>
                                            <%} %>

                                            </tbody>
                                        </table>

                                    </div>
                                </div>
                                <%
                                AbstractBadClaimServlet.rendPagingResult(out, badClaimRecords);
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
        <%if(!StringHelper.isEmpty(startTime)){%>
        $("#datepicker1").val("<%StringHelper.filterHTML(out, request.getParameter("startTime"));%>");
        <%}%>
        <%if(!StringHelper.isEmpty(endTime)){%>
        $("#datepicker2").val("<%StringHelper.filterHTML(out, request.getParameter("endTime"));%>");
        <%}%>
        $("#datepicker2").datepicker('option', 'minDate', $("#datepicker1").datepicker().val());
    });
</script>
</body>
</html>