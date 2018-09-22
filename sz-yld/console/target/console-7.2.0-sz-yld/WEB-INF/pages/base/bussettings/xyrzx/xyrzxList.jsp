<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.S51.enums.T5123_F06"%>
<%@page import="com.dimeng.p2p.console.servlets.base.bussettings.xyrzx.XyrzxList"%>
<%@page import="com.dimeng.util.ObjectHelper" %>
<%@page import="com.dimeng.p2p.console.servlets.AbstractConsoleServlet" %>
<%@page import="com.dimeng.p2p.console.servlets.base.bussettings.xyrzx.TyXyrzx" %>
<%@page import="com.dimeng.p2p.console.servlets.base.bussettings.xyrzx.QyXyrzx" %>
<%@page import="com.dimeng.p2p.console.servlets.base.bussettings.xyrzx.ViewXyrzx" %>
<%@page import="com.dimeng.p2p.S51.entities.T5123" %>
<%@page import="com.dimeng.p2p.console.servlets.base.bussettings.xyrzx.AddXyrzx" %>
<%@page import="com.dimeng.p2p.S51.enums.T5123_F04" %>
<%@page import="com.dimeng.p2p.S51.enums.T5123_F03" %>
<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <%@include file="/WEB-INF/include/script.jsp"%>
</head>
<body>
<%
    CURRENT_CATEGORY = "JCXXGL";
    CURRENT_SUB_CATEGORY = "XYRZX";
    PagingResult<T5123> result = ObjectHelper.convert(request.getAttribute("result"), PagingResult.class);
    T5123[] entityArray = (result == null ? null : result.getItems());
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <!--加载内容-->
                <form id="form1"
                      action="<%=controller.getURI(request, XyrzxList.class)%>"
                      method="post">
                    <div class="p20">
                        <div class="border">
                            <div class="title-container">
                                <i class="icon-i w30 h30 va-middle title-left-icon"></i>信用认证项管理
                            </div>
                            <div class="content-container pl40 pt30 pr40">
                                <ul class="gray6 input-list-container clearfix">
                                    <li><span class="display-ib mr5">类型名称：</span><input
                                            type="text" name="name" class="text border pl5 mr20"
                                            value="<%StringHelper.filterHTML(out, request.getParameter("name"));%>"/>
                                    </li>
                                    <li><span class="display-ib mr5">必要认证：</span>
                                        <select name="type" class="border mr20 h32 mw100">
                                            <option value="">全部</option>
                                            <%
                                                for (T5123_F03 t5123_F03 : T5123_F03.values()) {
                                            %>
                                            <option value="<%=t5123_F03.name()%>"
                                                    <%if (t5123_F03.name().equals(request.getParameter("type"))) {%>
                                                    selected="selected" <%}%>><%=t5123_F03.getChineseName()%>
                                            </option>
                                            <%
                                                }
                                            %>
                                        </select>
                                    </li>
                                    <li><span class="display-ib mr5">用户类型：</span>
                                        <select name="userType" class="border mr20 h32 mw100">
                                            <option value="">全部</option>
                                            <%
                                                for (T5123_F06 t5123_F06 : T5123_F06.values()) {
                                            %>
                                            <option value="<%=t5123_F06.name()%>"
                                                    <%if (t5123_F06.name().equals(request.getParameter("userType"))) {%>
                                                    selected="selected" <%}%>><%=t5123_F06.getChineseName()%>
                                            </option>
                                            <%
                                                }
                                            %>
                                        </select>
                                    </li>
                                    <li><span class="display-ib mr5">状态：</span>
                                        <select name="status" class="border mr20 h32 mw100">
                                            <option value="">全部</option>
                                            <%
                                                for (T5123_F04 t5123_F04 : T5123_F04.values()) {
                                            %>
                                            <option value="<%=t5123_F04.name()%>"
                                                    <%if (t5123_F04.name().equals(request.getParameter("status"))) {%>
                                                    selected="selected" <%}%>><%=t5123_F04.getChineseName()%>
                                            </option>
                                            <%
                                                }
                                            %>
                                        </select>
                                    </li>
                                    <li>
                                        <a href="javascript:void(0)" onclick="onSubmit()"
                                           class="btn btn-blue radius-6 mr5 pl1 pr15" name="input">
                                            <i class="icon-i w30 h30 va-middle search-icon "></i>搜索</a>
                                    </li>
                                    <li>
                                        <%if (dimengSession.isAccessableResource(AddXyrzx.class)) {%>
                                        <a href="javascript:void(0)" name="input"
                                           onclick="window.location.href='<%=controller.getURI(request, AddXyrzx.class)%>'"
                                           class="btn btn-blue radius-6 mr5 pl1 pr15">
                                            <i class="icon-i w30 h30 va-middle add-icon "></i>新增</a> <%} else {%>
                                        <span class="btn btn-gray radius-6 mr5 pl1 pr15"><i
                                                class="icon-i w30 h30 va-middle add-icon "></i>新增</span> <%} %>
                                    </li>
                                </ul>
                            </div>
                        </div>

                        <div class="border mt20 table-container">
                            <table class="table table-style gray6 tl">
                                <thead>
                                <tr class="title tc">
                                    <th>序号</th>
                                    <th>类型名称</th>
                                    <th>必要认证</th>
                                    <th>用户类型</th>
                                    <th>状态</th>
                                    <!-- <th>最高分数</th> -->
                                    <th>操作</th>
                                </tr>
                                </thead>
                                <tbody class="f12">
                                <%
                                    if (entityArray != null && entityArray.length != 0) {
                                        for (int i = 0; i < entityArray.length; i++) {
                                            T5123 t5123 = entityArray[i];
                                            if (t5123 == null) {
                                                continue;
                                            }
                                %>
                                <tr class="title tc">
                                    <td><%=i + 1%>
                                    </td>
                                    <td>
                                        <%StringHelper.filterHTML(out, t5123.F02);%>
                                    </td>
                                    <td><%=t5123.F03.getChineseName()%>
                                    </td>
                                    <td><%=t5123.F06.getChineseName()%>
                                    </td>
                                    <td><%=t5123.F04.getChineseName()%>
                                    </td>
<%--                                     <td><%=t5123.F05%> --%>
<!--                                     </td> -->
                                    <td>
                                        <%if (dimengSession.isAccessableResource(ViewXyrzx.class)) {%>
												<span><a
                                                        href="<%=controller.getURI(request,ViewXyrzx.class)%>?id=<%=t5123.F01%>"
                                                        class="link-blue mr20">修改</a></span>
                                        <%} else { %> <span
                                            class="disabled">修改</span> <%} %> <%if (t5123.F04 == T5123_F04.TY) { %>
                                        <%if (dimengSession.isAccessableResource(QyXyrzx.class)) {%> <span><a
                                            class="link-blue"
                                            href="<%=controller.getURI(request,QyXyrzx.class)%>?id=<%=t5123.F01%>">启用</a></span>
                                        <%} else { %> <span
                                            class="disabled">启用</span> <%} %> <%} else { %> <%if (dimengSession.isAccessableResource(TyXyrzx.class)) {%>
												<span><a class="link-blue"
                                                         href="<%=controller.getURI(request,TyXyrzx.class)%>?id=<%=t5123.F01%>">停用</a></span>
                                        <%} else { %> <span class="disabled">停用</span> <%} %> <%} %>
                                    </td>
                                </tr>
                                <%
                                    }
                                } else {
                                %>
                                <tr class="title tc">
                                    <td colspan="6">暂无数据</td>
                                </tr>
                                <%} %>
                                </tbody>
                            </table>
                        </div>
                        <%AbstractConsoleServlet.rendPagingResult(out, result);%>
                    </div>
                </form>
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