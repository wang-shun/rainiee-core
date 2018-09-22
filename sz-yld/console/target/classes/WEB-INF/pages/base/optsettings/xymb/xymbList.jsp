<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.console.servlets.base.xydj.XydjList" %>
<%@page import="com.dimeng.util.ObjectHelper" %>
<%@page import="com.dimeng.p2p.console.servlets.AbstractConsoleServlet" %>
<%@page import="com.dimeng.p2p.console.servlets.base.optsettings.xymb.UpdateXymb" %>
<%@page import="com.dimeng.p2p.S51.entities.T5125" %>
<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <%@include file="/WEB-INF/include/script.jsp"%>
</head>
<body>
<%
    CURRENT_CATEGORY = "JCXXGL";
    CURRENT_SUB_CATEGORY = "XYMBSZ";
    PagingResult<T5125> result = ObjectHelper.convert(request.getAttribute("result"), PagingResult.class);
    T5125[] entityArray = (result == null ? null : result.getItems());
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <!--加载内容-->
                <div class="p20">
                    <div class="border">
                        <div class="title-container">
                            <i class="icon-i w30 h30 va-middle title-left-icon"></i>协议模板管理
                        </div>
                    </div>
                    <form id="form1" action="<%=controller.getURI(request, XydjList.class)%>" method="post">
                        <div class="border mt20 table-container">
                            <table class="table table-style gray6 tl">
                                <thead>
                                <tr class="title tc">
                                    <th>序号</th>
                                    <th>协议类型编号</th>
                                    <th>协议版本号</th>
                                    <th>协议类型名称</th>
                                    <th>最后更新时间</th>
                                    <th>操作</th>
                                </tr>
                                </thead>
                                <tbody class="f12">
                                    <%
	            if (entityArray != null && entityArray.length != 0) {for (int i =0;i<entityArray.length;i++){T5125 t5125=entityArray[i];if (t5125 == null) {continue;}
	            %>
                                <tr class="title tc">
                                    <td><%=i + 1%>
                                    </td>
                                    <td><%=t5125.F01%>
                                    </td>
                                    <td><%=t5125.F02%>
                                    </td>
                                    <td><%StringHelper.filterHTML(out, t5125.F03);%></td>
                                    <td><%=DateTimeParser.format(t5125.F04)%>
                                    </td>
                                    <td>
                                        <%if (dimengSession.isAccessableResource(UpdateXymb.class)) {%>
                                        <span><a
                                                href="<%=controller.getURI(request,UpdateXymb.class)%>?id=<%=t5125.F01%>&version=<%=t5125.F02%>"
                                                class="libk-deepblue mr20">修改</a></span>
                                        <%} else { %>
                                        <span class="disabled">修改</span>
                                        <%} %>
                                    </td>
                                </tr>
                                    <%
	            	}}else{
	            %>
                                <tr class="title tc">
                                    <td colspan="6">暂无数据</td>
                                </tr>
                                    <%} %>
                            </table>
                        </div>
                        <%
                            AbstractConsoleServlet.rendPagingResult(out, result);
                        %>
                    </form>
                </div>
                <!--加载内容 结束-->
            </div>
        </div>
    </div>
    <!--右边内容 结束-->
<!--内容-->

</body>
</html>