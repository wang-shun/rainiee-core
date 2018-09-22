<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.console.servlets.base.bussettings.blx.BlxList"%>
<%@page import="com.dimeng.util.ObjectHelper" %>
<%@page import="com.dimeng.p2p.console.servlets.AbstractConsoleServlet" %>
<%@page import="com.dimeng.p2p.console.servlets.base.bussettings.blx.TyBlx" %>
<%@page import="com.dimeng.p2p.console.servlets.base.bussettings.blx.QyBlx" %>
<%@page import="com.dimeng.p2p.console.servlets.base.bussettings.blx.ViewBlx" %>
<%@page import="com.dimeng.p2p.console.servlets.base.bussettings.blx.AddBlx" %>
<%@page import="com.dimeng.p2p.S62.enums.T6211_F03" %>
<%@page import="com.dimeng.p2p.S62.entities.T6211" %>
<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <%@include file="/WEB-INF/include/script.jsp"%>
</head>
<body>
<%
    CURRENT_CATEGORY = "JCXXGL";
    CURRENT_SUB_CATEGORY = "BLX";
    PagingResult<T6211> result = ObjectHelper.convert(request.getAttribute("result"), PagingResult.class);
    T6211[] entityArray = (result == null ? null : result.getItems());
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <input type="hidden" name="token" value=""/>

                <form id="form1" name="form1" action="<%=controller.getURI(request, BlxList.class)%>" method="post">
                    <div class="p20">
                        <div class="border">
                            <div class="title-container"><i class="icon-i w30 h30 va-middle title-left-icon"></i>标类型管理
                            </div>
                            <div class="content-container pl40 pt30 pr40">
                                <ul class="gray6 input-list-container clearfix">
                                    <li><span class="display-ib mr5">类型名称： </span>
                                        <input type="text" name="f02"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("f02"));%>"
                                               class="text border pl5 mr20"/>
                                    </li>
                                    <li><span class="display-ib mr5">状态： </span>
                                        <select name="f03" class="border mr20 h32 mw100">
                                            <option value="">全部</option>
                                            <%for (T6211_F03 t6211_F03 : T6211_F03.values()) {%>
                                            <option value="<%=t6211_F03.name()%>" <%if (t6211_F03.name().equals(request.getParameter("f03"))) {%>
                                                    selected="selected" <%}%>><%=t6211_F03.getChineseName()%>
                                            </option>
                                            <%}%>
                                        </select>
                                    </li>
                                    <li><a href="javascript:$('#form1').submit();"
                                           class="btn btn-blue radius-6 mr5 pl1 pr15"><i
                                            class="icon-i w30 h30 va-middle search-icon "></i>搜索</a></li>
                                    <li>
                                        <%if (dimengSession.isAccessableResource(AddBlx.class)) {%>
                                        <a href="<%=controller.getURI(request, AddBlx.class)%>"
                                           class="btn btn-blue radius-6 mr5  pl1 pr15"><i
                                                class="icon-i w30 h30 va-middle add-icon "></i>新增</a>
                                        <%} else {%>
                                        <span class="btn btn-gray radius-6 mr5 pl1 pr15"><i
                                                class="icon-i w30 h30 va-middle add-icon "></i>新增</span>
                                        <%}%>
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
                                    <th>状态</th>
                                    <th>操作</th>
                                </tr>
                                </thead>
                                <tbody class="f12">
                                <%
                                    if (entityArray != null) {
                                        int i = 1;
                                        for (T6211 t6211 : entityArray) {
                                            if (t6211 == null) {
                                                continue;
                                            }
                                %>
                                <tr class="tc">
                                    <td><%=i++%>
                                    </td>
                                    <td><%StringHelper.filterHTML(out, t6211.F02);%></td>
                                    <td><%=t6211.F03.getChineseName()%>
                                    </td>
                                    <td>
                                        <%if (dimengSession.isAccessableResource(ViewBlx.class)) {%>
                                        <span class="blue"><a class="link-blue"
                                                              href="<%=controller.getURI(request,ViewBlx.class)%>?id=<%=t6211.F01%>">修改</a></span>
                                        <%} else { %>
                                        <span class="disabled">修改</span>
                                        <%} %>
                                        <%if (t6211.F03 == T6211_F03.TY) { %>
                                        <%if (dimengSession.isAccessableResource(QyBlx.class)) {%>
                                        <span class="blue"><a class="link-blue"
                                                              href="<%=controller.getURI(request,QyBlx.class)%>?id=<%=t6211.F01%>">启用</a></span>
                                        <%} else { %>
                                        <span class="disabled">启用</span>
                                        <%} %>
                                        <%} else { %>
                                        <%if (dimengSession.isAccessableResource(TyBlx.class)) {%>
                                        <span class="blue"><a class="link-blue"
                                                              href="<%=controller.getURI(request,TyBlx.class)%>?id=<%=t6211.F01%>">停用</a></span>
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
                                <tr class="tc">
                                    <td colspan="4">暂无数据</td>
                                </tr>
                                <%} %>
                                </tbody>
                            </table>
                        </div>
                        <!--分页-->
                        <%AbstractConsoleServlet.rendPagingResult(out, result); %>
                        <!--分页 end-->
                    </div>
                </form>
            </div>
        </div>
    </div>
    <!--右边内容 结束-->
</body>
</html>