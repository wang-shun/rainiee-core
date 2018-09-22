<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.util.ObjectHelper" %>
<%@page import="com.dimeng.p2p.console.servlets.AbstractConsoleServlet" %>
<%@page import="com.dimeng.p2p.console.servlets.base.optsettings.jylx.JylxList" %>
<%@page import="com.dimeng.p2p.S51.enums.T5122_F03" %>
<%@page import="com.dimeng.p2p.console.servlets.base.optsettings.jylx.ViewJylx" %>
<%@page import="com.dimeng.p2p.console.servlets.base.optsettings.jylx.QyJylx" %>
<%@page import="com.dimeng.p2p.console.servlets.base.optsettings.jylx.TyJylx" %>
<%@page import="com.dimeng.p2p.S51.entities.T5122" %>
<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <%@include file="/WEB-INF/include/script.jsp"%>
</head>
<body>
<%
    CURRENT_CATEGORY = "JCXXGL";
    CURRENT_SUB_CATEGORY = "JYLX";
    PagingResult<T5122> result = ObjectHelper.convert(request.getAttribute("result"), PagingResult.class);
    T5122[] entityArray = (result == null ? null : result.getItems());
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <!--加载内容-->
                <div class="p20">
                    <form id="form1"
                          action="<%=controller.getURI(request, JylxList.class)%>"
                          method="post">
                        <div class="border">
                            <div class="title-container">
                                <i class="icon-i w30 h30 va-middle title-left-icon"></i>交易类型管理
                            </div>
                            <div class="content-container pl40 pt30 pr40">
                                <ul class="gray6 input-list-container clearfix">
                                    <li><span class="display-ib mr5">类型名称：</span><input
                                            type="text" name="name" class="text border pl5 mr20"
                                            value="<%StringHelper.filterHTML(out, request.getParameter("name"));%>"/>
                                    </li>
                                    <li><span class="display-ib mr5">状态：</span> <select
                                            name="status" class="border mr20 h32 mw100">
                                        <option value="">全部</option>
                                        <%
                                            for (T5122_F03 t5122_F03 : T5122_F03.values()) {
                                        %>
                                        <option value="<%=t5122_F03.name()%>"
                                                <%if (t5122_F03.name().equals(request.getParameter("status"))) {%>
                                                selected="selected" <%}%>><%=t5122_F03.getChineseName()%>
                                        </option>
                                        <%
                                            }
                                        %>
                                    </select></li>
                                    <li><a href="javascript:void(0)" onclick="onSubmit()"
                                           class="btn btn-blue radius-6 mr5 pl1 pr15" name="input">
                                        <i class="icon-i w30 h30 va-middle search-icon "></i>搜索
                                    </a>
                                        <%--回车提交表单--%>
                                        <input type="submit" style="display:block;overflow:hidden;width:0px;height:0px; position:absolute"/>
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
                                    if (entityArray != null && entityArray.length != 0) {
                                        for (int i = 0; i < entityArray.length; i++) {
                                            T5122 t5122 = entityArray[i];
                                            if (t5122 == null) {
                                                continue;
                                            }
                                %>
                                <tr class="title tc">
                                    <td><%=i + 1%>
                                    </td>
                                    <td>
                                        <%StringHelper.filterHTML(out, t5122.F02);%>
                                    </td>
                                    <td><%=t5122.F03.getChineseName()%>
                                    </td>
                                    <td>
                                        <%if (dimengSession.isAccessableResource(ViewJylx.class)) {%>
											<span>
												<a href="<%=controller.getURI(request,ViewJylx.class)%>?id=<%=t5122.F01%>" class="link-blue mr20">修改</a>
											</span> 
										<%} else { %> 
											<span class="disabled">修改</span> 
										<%} %> 
										<%if (t5122.F03 == T5122_F03.TY) { %>
	                                        <%if (dimengSession.isAccessableResource(QyJylx.class)) {%> 
	                                        	<span class="blue">
	                                            	<a class="link-blue"  href="<%=controller.getURI(request,QyJylx.class)%>?id=<%=t5122.F01%>">启用</a>
	                                           	</span>
	                                        <%} else { %> 
	                                        	<span class="disabled mr20">启用</span> 
	                                        <%} %> 
	                                    <%} else { %> 
                                            <%if (dimengSession.isAccessableResource(TyJylx.class)) {%>
												<span class="blue">
													<a class="link-blue" href="<%=controller.getURI(request,TyJylx.class)%>?id=<%=t5122.F01%>">停用</a>
												</span>
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
                                    <td colspan="4">暂无数据</td>
                                </tr>
                                <%} %>
                                </tbody>
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
<script type="text/javascript">
    function onSubmit() {
        $("#form1").submit();
    }
</script>
</body>
</html>