<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.console.servlets.bid.htgl.agreement.QyjkDzxy"%>
<%@page import="com.dimeng.p2p.console.servlets.AbstractConsoleServlet" %>
<%@page import="com.dimeng.p2p.console.servlets.bid.htgl.agreement.Index" %>
<%@page import="com.dimeng.p2p.S61.entities.T6161" %>
<%@page import="com.dimeng.p2p.modules.account.console.service.QyManage" %>
<%@page import="com.dimeng.p2p.console.servlets.bid.htgl.agreement.ZqzrDzxy" %>
<%@page import="com.dimeng.p2p.console.servlets.bid.htgl.agreement.GrjkDzxy" %>
<%@page import="com.dimeng.p2p.console.servlets.bid.htgl.agreement.DfDzxy" %>
<%@page import="com.dimeng.p2p.modules.bid.console.service.entity.DfxyRecord" %>
<%@page import="com.dimeng.p2p.modules.bid.console.service.entity.Qyjkxy" %>
<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <%@include file="/WEB-INF/include/script.jsp" %>
</head>
<body>
<%
    CURRENT_CATEGORY = "YWGL";
    CURRENT_SUB_CATEGORY = "DZXY";
    PagingResult<DfxyRecord> result = (PagingResult<DfxyRecord>) request.getAttribute("result");
    DfxyRecord[] dfxys = result.getItems();
    String advanceTimeStart = request.getParameter("advanceTimeStart");
    String advanceTimeEnd = request.getParameter("advanceTimeEnd");
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <form id="form2" action="<%=controller.getURI(request, DfDzxy.class)%>" method="post">
                    <div class="p20">
                        <div class="border">
                            <div class="title-container">
                                <i class="icon-i w30 h30 va-middle title-left-icon"></i>垫付协议管理
                            </div>
                            <div class="content-container pl40 pt30 pr40">
                                <ul class="gray6 input-list-container clearfix">
                                    <li><span class="display-ib mr5">借款账号</span>
                                        <input type="text" name="name" class="text border pl5 mr20"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("name"));%>"/>
                                    </li>
                                    <li><span class="display-ib mr5">垫付日期</span> <input
                                            type="text" name="advanceTimeStart" readonly="readonly"
                                            id="datepicker1" class="text border pl5 w120 date"/> <span
                                            class="pl5 pr5">至</span> <input type="text"
                                                                            readonly="readonly" name="advanceTimeEnd"
                                                                            id="datepicker2"
                                                                            class="text border pl5 w120 date mr20"/>
                                    </li>

                                    <li><a href="javascript:$('#form2').submit();"
                                           class="btn btn-blue radius-6 mr5 pl1 pr15"><i
                                            class="icon-i w30 h30 va-middle search-icon "></i>搜索</a></li>

                                </ul>
                            </div>
                        </div>
                        <div class="border mt20">
                            <div class="tabnav-container">
                                <ul class="clearfix">
                                    <li><a href="<%=controller.getURI(request, QyjkDzxy.class)%>" class="tab-btn ">企业借款协议</a>
                                    <li><a href="<%=controller.getURI(request, GrjkDzxy.class)%>" class="tab-btn ">个人借款协议</a>
                                    </li>
                                    <li><a href="<%=controller.getURI(request, ZqzrDzxy.class)%>" class="tab-btn ">债权转让协议</a>
                                    </li>
                                    <li><a href="javascript:void(0)" class="tab-btn  select-a">垫付协议<i class="icon-i tab-arrowtop-icon"></i></a></li>
                                    <%if(Boolean.parseBoolean(configureProvider.getProperty(BadClaimVariavle.IS_BADCLAIM_TRANSFER))){%>
	                                    <li><a href="/console/bid/htgl/agreement/blzqzrDzxy.htm" class="tab-btn ">不良债权转让协议</a>
	                                    </li>
                                    <%} %>
                                </ul>
                            </div>
                            <div class=" table-container p20">
                                <table class="table table-style gray6 tl">
                                    <thead>
                                    <tr class="title tc">
                                        <th>序号</th>
                                        <th>借款标题</th>
                                        <th>借款账号</th>
                                        <th>担保账号</th>
                                        <th>垫付金额(元)</th>
                                        <th>垫付日期</th>
                                        <th>操作</th>

                                    </tr>
                                    </thead>
                                    <tbody class="f12">
                                    <%
                                        if (dfxys != null && dfxys.length > 0) {
                                            int i = 1;
                                            for (DfxyRecord dfxy : dfxys) {
                                                if (dfxy == null) {
                                                    continue;
                                                }

                                    %>
                                    <tr class="tc">
                                        <td><%=i++%></td>
                                        <td align="center" title="<%StringHelper.filterHTML(out, dfxy.loanTitle); %>"><%
                                            StringHelper.filterHTML(out, StringHelper.truncation(dfxy.loanTitle, 15)); %></td>
                                        <td align="center"><%StringHelper.filterHTML(out, dfxy.loanAccnout);%></td>
                                        <td align="center"><%StringHelper.filterHTML(out, dfxy.advanceAccount);%></td>
                                        <td align="center"><%=Formater.formatAmount(dfxy.advanceSum)%></td>
                                        <td align="center"><%=TimestampParser.format(dfxy.advanceTime) %></td>
                                        <td align="center">
                                            <a target="_blank"
                                               href="<%configureProvider.format(out, URLVariable.XY_PTHTDZXY); %>?df=df&bid=<%=dfxy.loanId %>&dfuid=<%=dfxy.advanceAccountId %>"
                                               class="link-blue">合同</a>
                                        </td>
                                    </tr>
                                    <%
                                        }
                                    } else {
                                    %>
                                    <tr class="tc">
                                        <td colspan="7">暂无数据</td>
                                    </tr>
                                    <%} %>
                                    </tbody>
                                </table>
                            </div>
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

<div id="info"></div>

<%@include file="/WEB-INF/include/datepicker.jsp" %>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/dialog.js"></script>
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
        <%if(!StringHelper.isEmpty(advanceTimeStart)){%>
        $("#datepicker1").val("<%StringHelper.filterHTML(out, request.getParameter("advanceTimeStart"));%>");
        <%}%>
        <%if(!StringHelper.isEmpty(advanceTimeEnd)){%>
        $("#datepicker2").val("<%StringHelper.filterHTML(out, request.getParameter("advanceTimeEnd"));%>");
        <%}%>
        $("#datepicker2").datepicker('option', 'minDate', $("#datepicker1").datepicker().val());
    });

</script>

</body>
</html>