<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.console.servlets.bid.gyjz.gyyw.GyLoanList"%>
<%@page import="com.dimeng.p2p.console.servlets.AbstractConsoleServlet" %>
<%@page import="com.dimeng.p2p.modules.spread.console.service.entity.SeriesDetailList" %>
<%@page import="com.dimeng.util.ObjectHelper" %>
<%@page import="com.dimeng.p2p.console.servlets.spread.tgjl.tgjlgy.SearchCxjl" %>
<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <%@include file="/WEB-INF/include/script.jsp"%>
</head>
<body>
<%
    CURRENT_CATEGORY = "TGGL";
    CURRENT_SUB_CATEGORY = "TGTJ";
    PagingResult<SeriesDetailList> result = ObjectHelper.convert(request.getAttribute("result"), PagingResult.class);
    String name = ObjectHelper.convert(request.getAttribute("name"), String.class);
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <form id="form1" action="<%=controller.getURI(request, SearchCxjl.class)%>" method="post">
                <input type="hidden" name="id" value="<%=request.getParameter("id") %>"/>
                    <div class="p20">
                        <div class="border">
                            <div class="title-container"><i class="icon-i w30 h30 va-middle title-left-icon"></i>推广持续奖励详情
                                - <%StringHelper.filterHTML(out, name); %>
                                <div class="fr">
                                    <input name="returnBtn" type="button" style="margin-top:5px;"
                                           class="btn btn-blue radius-6 pl20 pr20 ml40 mr10" value="返回"
                                           onclick="history.back(-1);"/>
                                </div>
                            </div>
                        </div>

                        <div class="border mt20 table-container">
                            <table class="table table-style gray6 tl">
                                <thead>
                                <tr class="title tc">
                                    <th class="tc">序号</th>
                                    <th>被推广人ID</th>
                                    <th>被推广人用户名</th>
                                    <th>被推广人姓名</th>
                                    <th>推广投资金额(元)</th>
                                    <th>持续奖励(元)</th>
                                    <th>投资时间</th>
                                </tr>
                                </thead>
                                <tbody class="f12">
                                <%
                                    SeriesDetailList[] lists = result.getItems();
                                    if (lists != null) {
                                        int index = 1;
                                        for (SeriesDetailList list : lists) {
                                %>
                                <tr class="tc">
                                    <td class="tc"><%=index++ %>
                                    </td>
                                    <td><%=list.id %>
                                    </td>
                                    <td><%StringHelper.filterHTML(out, list.userName); %></td>
                                    <td><%StringHelper.filterHTML(out, list.name); %></td>
                                    <td><%=list.money %>
                                    </td>
                                    <td><%=list.rewarMoney %>
                                    </td>
                                    <td><%=DateParser.format(list.investTime) %>
                                    </td>
                                </tr>
                                <%
                                    }
                                } else {
                                %>
                                <tr>
                                    <td colspan="7" class="tc">暂无数据</td>
                                </tr>
                                <%} %>
                                </tbody>
                            </table>
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
</body>
</html>