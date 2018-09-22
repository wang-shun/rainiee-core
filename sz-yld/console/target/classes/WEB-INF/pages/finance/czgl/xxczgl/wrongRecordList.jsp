<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.console.servlets.finance.czgl.xxczgl.XxczglList" %>
<%@page import="com.dimeng.p2p.modules.account.console.service.entity.OfflineChargeImportRecord" %>
<%@page import="java.util.List" %>
<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <%@include file="/WEB-INF/include/script.jsp" %>
</head>
<%
    List<OfflineChargeImportRecord> list = (List<OfflineChargeImportRecord>) request.getAttribute("list");
%>
<body>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <form action="<%=controller.getURI(request, XxczglList.class)%>" method="post" name="form1" id="form1">
                    <div class="p20">
                        <div class="border">
                            <div class="title-container">
                                <i class="icon-i w30 h30 va-middle title-left-icon"></i>批量导入线下充值错误信息
                            </div>

                        </div>
                        <div class="border mt20 table-container">
                            <table class="table table-style gray6 tl">
                                <thead>
                                <tr class="title tc">
                                    <th>序号</th>
                                    <th>用户名</th>
                                    <th>金额</th>
                                    <th>备注</th>
                                </tr>
                                </thead>
                                <tbody class="f12">
                                <%

                                    if (list != null && list.size() > 0 ) {
                                        int i = 1;
                                        for (OfflineChargeImportRecord item : list) {
                                            if (item == null) {
                                                continue;
                                            }
                                %>
                                <tr class="tc">
                                    <td><%=i++%>
                                    </td>
                                    <td><%StringHelper.filterHTML(out, item.getUserName());%></td>
                                    <td><%=item.getAmount()%></td>
                                    <td><%StringHelper.filterHTML(out, item.getRemark());%></td>

                                </tr>
                                <%
                                    }
                                } else {
                                %>
                                <tr>
                                    <td colspan="4" class="tc">暂无数据</td>
                                </tr>
                                <%} %>
                                </tbody>
                            </table>
                        </div>
                        <div class="box2 clearfix"></div>
                    </div>
                </form>
            </div>

        </div>
    </div>


</body>
</html>
