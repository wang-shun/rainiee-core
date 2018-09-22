<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.console.servlets.base.dywlx.DywlxList"%>
<%@page import="com.dimeng.util.ObjectHelper" %>
<%@page import="com.dimeng.p2p.console.servlets.base.dywlx.TyDywlx" %>
<%@page import="com.dimeng.p2p.console.servlets.base.dywlx.QyDywlx" %>
<%@page import="com.dimeng.p2p.console.servlets.base.dywlx.ViewDywlx" %>
<%@page import="com.dimeng.p2p.console.servlets.base.dywlx.AddDywlx" %>
<%@page import="com.dimeng.p2p.S62.enums.T6213_F03" %>
<%@page import="com.dimeng.p2p.S62.entities.T6213" %>
<%@page import="com.dimeng.p2p.console.servlets.AbstractConsoleServlet" %>
<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <%@include file="/WEB-INF/include/script.jsp"%>
</head>
<body>
<%
    CURRENT_CATEGORY = "JCXXGL";
    CURRENT_SUB_CATEGORY = "DYWLX";
    PagingResult<T6213> result = ObjectHelper.convert(request.getAttribute("result"), PagingResult.class);
    T6213[] entityArray = (result == null ? null : result.getItems());
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <!--加载内容-->
                <form id="form1" action="<%=controller.getURI(request, DywlxList.class)%>" method="post">
                    <div class="p20">
                        <div class="border">
                            <div class="title-container">
                                <i class="icon-i w30 h30 va-middle title-left-icon"></i>抵押物类型管理
                            </div>
                            <div class="content-container pl40 pt30 pr40">
                                <ul class="gray6 input-list-container clearfix">
                                    <li><span class="display-ib mr5">类型名称：</span>
                                        <input type="text" name="f02" class="text border pl5 mr20"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("f02"));%>"/>
                                    </li>
                                    <li><span class="display-ib mr5">状态：</span>
                                        <select name="f03" class="border mr20 h32 mw100">
                                            <option value="">全部</option>
                                            <%
                                                for (T6213_F03 t6213_F03 : T6213_F03.values()) {
                                            %>
                                            <option value="<%=t6213_F03.name()%>" <%if (t6213_F03.name().equals(request.getParameter("f03"))) {%>
                                                    selected="selected" <%}%>><%=t6213_F03.getChineseName()%>
                                            </option>
                                            <%
                                                }
                                            %>
                                        </select>
                                    </li>
                                    <li><a href="javascript:void(0)" onclick="onSubmit()"
                                           class="btn btn-blue radius-6 mr5 pl1 pr15" name="input">
                                        <i class="icon-i w30 h30 va-middle search-icon "></i>搜索</a>
                                    </li>
                                    <li>
                                        <%if (dimengSession.isAccessableResource(AddDywlx.class)) {%>
                                        <a href="javascript:void(0)" name="input"
                                           onclick="window.location.href='<%=controller.getURI(request, AddDywlx.class)%>'"
                                           class="btn btn-blue radius-6 mr5 pl1 pr15">
                                            <i class="icon-i w30 h30 va-middle add-icon "></i>新增</a>
                                        <%} else {%>
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
                                    <th>类型名称</th>
                                    <th>状态</th>
                                    <th>操作</th>
                                </tr>
                                </thead>
                                <tbody class="f12">
                                    <%
						            if (entityArray != null && entityArray.length != 0) {for (int i =0;i<entityArray.length;i++){T6213 t6213=entityArray[i];if (t6213 == null) {continue;}
						            %>
                                <tr class="title tc">
                                    <td><%=i + 1%>
                                    </td>
                                    <td><%StringHelper.filterHTML(out, t6213.F02);%></td>
                                    <td><%=t6213.F03.getChineseName()%>
                                    </td>
                                    <td>
						              	<span>
							             	<%if (dimengSession.isAccessableResource(ViewDywlx.class)) {%>
							              	<a href="<%=controller.getURI(request,ViewDywlx.class)%>?id=<%=t6213.F01%>"
                                               class="link-blue mr20">修改</a>
							              	<%} else { %>
						                   	<span class="disabled">修改</span>
						                   	<%} %>
					                   	</span>
                                        <%if (t6213.F03 == T6213_F03.TY) { %>
                                        <%if (dimengSession.isAccessableResource(QyDywlx.class)) {%>
                                        <span class="blue"><a class="link-blue"
                                                              href="<%=controller.getURI(request,QyDywlx.class)%>?id=<%=t6213.F01%>">启用</a></span>
                                        <%} else { %>
                                        <span class="disabled">启用</span>
                                        <%} %>
                                        <%} else { %>
                                        <%if (dimengSession.isAccessableResource(TyDywlx.class)) {%>
                                        <span class="blue"><a class="link-blue"
                                                              href="<%=controller.getURI(request,TyDywlx.class)%>?id=<%=t6213.F01%>">停用</a></span>
                                        <%} else { %>
                                        <span class="disabled">停用</span>
                                        <%} %>
                                        <%} %>
                                    </td>
                                </tr>
                                    <%
						            	}}else{
						            %>
                                <tr class="title tc">
                                    <td colspan="4">暂无数据</td>
                                </tr>
                                    <%} %>
                            </table>
                        </div>
                        <%
                            AbstractConsoleServlet.rendPagingResult(out, result);
                        %>
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