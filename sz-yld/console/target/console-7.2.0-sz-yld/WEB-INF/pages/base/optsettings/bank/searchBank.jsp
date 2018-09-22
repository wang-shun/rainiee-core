<%@page import="com.dimeng.p2p.console.servlets.base.optsettings.bank.DisableBank" %>
<%@page import="com.dimeng.p2p.console.servlets.base.optsettings.bank.EnableBank" %>
<%@page import="com.dimeng.p2p.S50.enums.T5020_F03" %>
<%@page import="com.dimeng.p2p.S50.entities.T5020" %>
<%@page import="com.dimeng.p2p.console.servlets.base.optsettings.bank.UpdateBank" %>
<%@page import="com.dimeng.p2p.console.servlets.base.optsettings.bank.AddBank" %>
<%@page import="com.dimeng.p2p.console.servlets.base.optsettings.bank.SearchBank" %>
<%@page import="com.dimeng.util.ObjectHelper" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <%@include file="/WEB-INF/include/script.jsp"%>
</head>

<body>
<%
    CURRENT_CATEGORY = "JCXXGL";
    CURRENT_SUB_CATEGORY = "YHSZ";
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
            <%
            PagingResult<T5020> result = ObjectHelper.convert(request.getAttribute("result"), PagingResult.class);
        %>
                <!--加载内容-->
                <div class="p20">
                    <form action="<%=controller.getURI(request, SearchBank.class)%>" method="post" id="form1">
                        <div class="border">
                            <div class="title-container"><i class="icon-i w30 h30 va-middle title-left-icon"></i>银行设置
                            </div>
                            <div class="content-container pl40 pt30 pr40">
                                <ul class="gray6 input-list-container clearfix">

                                    <li>
                                        <span class="display-ib mr5">银行名称：</span><input type="text" name="name"
                                                                                        id="textfield4"
                                                                                        class="text border pl5 mr20"
                                                                                        value="<%StringHelper.filterHTML(out, request.getParameter("name"));%>"/>
                                    </li>
                                    <li><span class="display-ib mr5">状态：</span>
                                        <select name="status" class="border mr20 h32 mw100">
                                            <option value="">全部</option>
                                            <%
                                                for (T5020_F03 status : T5020_F03.values()) {
                                            %>
                                            <option value="<%=status.name()%>" <%if (status.name().equals(request.getParameter("status"))) {%>
                                                    selected="selected" <%}%>><%=status.getChineseName()%>
                                            </option>
                                            <%
                                                }
                                            %>
                                        </select>
                                    </li>
                                    <li>
                                        <a href="javascript:void(0)" onclick="onSubmit()"
                                           class="btn btn-blue radius-6 mr5 pl1 pr15">
                                            <i class="icon-i w30 h30 va-middle search-icon " name="input"></i>搜索</a>
                                        <%if (dimengSession.isAccessableResource(AddBank.class)) {%>
                                        <a href="<%=controller.getURI(request, AddBank.class)%>"
                                           class="btn btn-blue radius-6 mr5 pl1 pr15">
                                            <i class="icon-i w30 h30 va-middle add-icon "></i>新增</a>
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
                                    <th>银行名称</th>
                                    <th>银行代码</th>
                                    <th>状态</th>
                                    <th>操作</th>
                                </tr>
                                </thead>
                                <tbody class="f12">
                                <%
                                    T5020[] banks = (result == null ? null : result.getItems());
                                    if (banks != null && banks.length != 0) {
                                        int index = 1;
                                        for (T5020 bank : banks) {
                                %>
                                <tr class="title tc">
                                    <td><%=index++%>
                                    </td>
                                    <td><%
                                        StringHelper.filterHTML(out, bank.F02);
                                    %></td>
                                    <td><%
                                        StringHelper.filterHTML(out, bank.F04);
                                    %></td>
                                    <td><%=bank.F03.getChineseName() %>
                                    </td>
                                    <td>
                                        <%if (dimengSession.isAccessableResource(UpdateBank.class)) {%>
                                        <span><a
                                                href="<%=controller.getURI(request, UpdateBank.class)%>?id=<%=bank.F01%>"
                                                class="libk-deepblue mr20">修改</a></span>
                                        <%} else { %>
                                        <span class="disabled mr20">修改</span>
                                        <%} %>
                                        <%if (bank.F03 == T5020_F03.TY) { %>
                                        <%if (dimengSession.isAccessableResource(EnableBank.class)) {%>
                                        <span class="blue"><a class="link-blue"
                                                              href="<%=controller.getURI(request,EnableBank.class)%>?id=<%=bank.F01%>">启用</a></span>
                                        <%} else { %>
                                        <span class="disabled">启用</span>
                                        <%} %>
                                        <%} else { %>
                                        <%if (dimengSession.isAccessableResource(DisableBank.class)) {%>
                                        <span class="blue"><a class="link-blue"
                                                              href="<%=controller.getURI(request,DisableBank.class)%>?id=<%=bank.F01%>">停用</a></span>
                                        <%} else { %>
                                        <span class="disabled">停用</span>
                                        <%} %>
                                        <%} %>
                                    </td>
                                </tr>
                                <%
                                    }
                                } else {
                                %>
                                <tr class="title tc">
                                    <td colspan="5">暂无数据</td>
                                </tr>
                                <%} %>
                                </tbody>
                            </table>
                        </div>
                        <%
                            SearchBank.rendPagingResult(out, result);%>
                    </form>
                </div>
                <!--加载内容 结束-->
            </div>
        </div>
    </div>
    <!--右边内容 结束-->
<!--内容-->

<script type="text/javascript">
    function onSubmit() {
        $("#form1").submit();
    }
</script>
</body>
</html>