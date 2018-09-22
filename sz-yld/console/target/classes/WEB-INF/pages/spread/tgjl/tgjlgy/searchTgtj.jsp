<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.console.servlets.AbstractConsoleServlet" %>
<%@page import="com.dimeng.p2p.console.servlets.spread.tgjl.tgjlgy.ExportTgtj" %>
<%@page import="com.dimeng.p2p.console.servlets.spread.tgjl.tgjlgy.SearchCxjl" %>
<%@page import="com.dimeng.p2p.console.servlets.spread.tgjl.tgjlgy.SearchTgtj" %>
<%@page import="com.dimeng.p2p.modules.spread.console.service.entity.SpreadTotalInfo" %>
<%@page import="com.dimeng.p2p.modules.spread.console.service.entity.SpreadTotalList" %>
<%@page import="com.dimeng.util.ObjectHelper" %>
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
    PagingResult<SpreadTotalList> result = ObjectHelper.convert(request.getAttribute("result"), PagingResult.class);
    SpreadTotalInfo totalInfo = ObjectHelper.convert(request.getAttribute("totalInfo"), SpreadTotalInfo.class);
    SpreadTotalList searchTotalListAmount = (SpreadTotalList)request.getAttribute("searchTotalListAmount");
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <form id="form1" action="<%=controller.getURI(request, SearchTgtj.class)%>" method="post">
                    <div class="p20">
                        <div class="border">
                            <div class="title-container"><i class="icon-i w30 h30 va-middle title-left-icon"></i>推广奖励概要列表
                            </div>
                            <div class="content-container pl40 pt30 pr40">
                                <ul class="gray6 input-list-container clearfix">
                                    <li><span class="display-ib mr5">推广人总数</span>
                                        <span class="link-blue"><%=totalInfo.personNum%></span>&nbsp;人
                                    </li>
                                    <li class="ml50"><span class="display-ib mr5">被推广人总数</span>
                                        <span class="link-blue"><%=totalInfo.spreadPersonNum%></span>&nbsp;人
                                    </li>
                                    <li class="ml50"><span class="display-ib mr5">推广返利总额</span>
                                        <span class="link-blue"><%=totalInfo.returnMoney%></span>&nbsp;元
                                    </li>
                                </ul>
                            </div>
                        </div>
                        <div class="border mt20">
                            <div class="content-container pl40 pt30 pr40">
                                <ul class="gray6 input-list-container clearfix">
                                    <li><span class="display-ib mr5">推广人ID</span>
                                        <input type="text" name="id" id="textfield" class="text border pl5 mr20" maxlength="9" onkeyup="value=value.replace(/\D/g,'')"
                                            value="<%StringHelper.filterHTML(out, request.getParameter("id"));%>"/>
                                    </li>
                                    <li><span class="display-ib mr5">推广人用户名</span>
                                        <input type="text" name="userName" id="textfield4" class="text border pl5 mr20"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("userName"));%>"/>
                                    </li>
                                    <li><span class="display-ib mr5">推广人姓名</span>
                                        <input type="text" name="name" id="textfield4" class="text border pl5 mr20"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("name"));%>"/>
                                    </li>

                                    <li><a href="javascript:$('#form1').submit();"
                                           class="btn btn-blue radius-6 mr5 pl1 pr15"><i
                                            class="icon-i w30 h30 va-middle search-icon "></i>搜索</a></li>

                                    <li>
                                        <%
                                            if (dimengSession.isAccessableResource(ExportTgtj.class)) {
                                        %>
                                        <a href="javascript:void(0)" onclick="exportList()"
                                           class="btn btn-blue radius-6 mr5 pl1 pr15"><i
                                                class="icon-i w30 h30 va-middle export-icon "></i>导出</a>
                                        <%
                                        } else {
                                        %>
                                        <span class="btn btn-gray radius-6 mr5 pl1 pr15"><i
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
                                    <th>推广人ID</th>
                                    <th>推广人用户名</th>
                                    <th>推广人姓名</th>
                                    <th>推广客户数</th>
                                    <th>持续奖励总计(元)</th>
                                    <th>有效推广奖励总计(元)</th>
                                    <th>推广奖励总计(元)</th>
                                    <th class="w200">操作</th>
                                </tr>
                                </thead>
                                <tbody class="f12">
                                <%
                                    if (result != null && result.getItemCount() > 0) {
                                        SpreadTotalList[] lists = result.getItems();
                                        if (lists != null) {
                                            int index = 1;
                                            for (SpreadTotalList list : lists) {
                                %>
                                <tr class="tc">
                                    <td><%=index++%>
                                    </td>
                                    <td><%=list.personID%>
                                    </td>
                                    <td>
                                        <%
                                            StringHelper.filterHTML(out, list.userName);
                                        %>
                                    </td>
                                    <td>
                                        <%
                                            StringHelper.filterHTML(out, list.name);
                                        %>
                                    </td>
                                    <td><%=list.customNum%>
                                    </td>
                                    <td><%=list.seriesRewarMoney%>
                                    </td>
                                    <td><%=list.validRewardMoney%>
                                    </td>
                                    <td><%=list.rewardTotalMoney%>
                                    </td>
                                    <td>
                                        <%if (dimengSession.isAccessableResource(SearchCxjl.class)) {%>
                                        <a href="<%=controller.getURI(request, SearchCxjl.class)%>?id=<%=list.personID%>"
                                           class="link-blue">查看持续奖励详情</a>
                                        <%} else { %>
                                        <a href="javascript:void(0)" class="disabled">查看持续奖励详情</a>
                                        <%} %>
                                    </td>
                                </tr>
                                <%
                                        }
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
						<p class="mt5">
                            <span class="mr30">持续奖励总计：<em class="red"><%=Formater.formatAmount(searchTotalListAmount.seriesRewarMoney)%>
                            </em> 元</span>
                            <span class="mr30">有效推广奖励总计：<em class="red"><%=Formater.formatAmount(searchTotalListAmount.validRewardMoney)%>
                            </em> 元</span>
                            <span class="mr30">推广奖励总计：<em class="red"><%=Formater.formatAmount(searchTotalListAmount.rewardTotalMoney)%>
                            </em> 元</span>
                        </p>
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
<script type="text/javascript">
	function exportList() {
		var del_url = '<%=controller.getURI(request, ExportTgtj.class)%>';
        var form = document.forms[0];
        form.action = del_url;
        form.submit();
        form.action = '<%=controller.getURI(request, SearchTgtj.class)%>';
    }
</script>
</body>
</html>