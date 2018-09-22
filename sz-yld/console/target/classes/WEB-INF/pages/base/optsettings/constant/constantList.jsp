<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.util.ObjectHelper" %>
<%@page import="com.dimeng.p2p.console.servlets.base.optsettings.constant.UpdateConstant" %>
<%@page import="java.net.URLDecoder" %>
<%@page import="java.net.URLEncoder" %>
<%@page import="com.dimeng.util.filter.HTMLFilter" %>
<%@page import="com.dimeng.framework.config.entity.VariableType" %>
<%@page import="com.dimeng.p2p.console.servlets.AbstractConsoleServlet" %>
<%@page import="com.dimeng.p2p.console.servlets.base.optsettings.constant.ConstantList" %>
<%@page import="com.dimeng.framework.config.entity.VariableBean" %>
<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <%@include file="/WEB-INF/include/script.jsp"%>
</head>
<body>
<%
    PagingResult<VariableBean> result = ObjectHelper.convert(request.getAttribute("result"), PagingResult.class);
    String type = request.getParameter("type");
    type = ((StringHelper.isEmpty(type) || "null".equalsIgnoreCase(type)) ? "" : type);
    String key = request.getParameter("key");
    key = ((StringHelper.isEmpty(key) || "null".equalsIgnoreCase(key)) ? "" : key);
    String cur = request.getParameter(AbstractConsoleServlet.PAGING_CURRENT);
    String des = request.getParameter("des");
    des = ((StringHelper.isEmpty(des) || "null".equalsIgnoreCase(des)) ? "" : des);
    int decode = IntegerParser.parse(request.getParameter("decode"), 0);
    if (decode > 0) {
        des = URLDecoder.decode(des.replace("^", "%"), resourceProvider.getCharset());
    }
    String desEncode = StringHelper.isEmpty(des) ? "" : URLEncoder.encode(des, resourceProvider.getCharset()).replace("%", "^");

    CURRENT_CATEGORY = "JCXXGL";
    CURRENT_SUB_CATEGORY = "PTCLSZ";
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <!--加载内容-->
                <form action="<%=controller.getURI(request, ConstantList.class)%>" method="post" id="form1">
                    <div class="p20">
                        <div class="border">
                            <div class="title-container"><i class="icon-i w30 h30 va-middle title-left-icon"></i>平台常量管理
                            </div>
                            <div class="content-container pl40 pt30 pr40">
                                <ul class="gray6 input-list-container clearfix">
                                    <li><span class="display-ib mr5">常量名称：</span><input
                                            onKeyUp="if(value.length == value.replace(/[%|^|#]/g,'').length){return true;}else{value=value.replace(/[%|^|#]/g,'')}" type="text" name="des"
                                            value="<%StringHelper.filterQuoter(out, des);%>" id="textfield"
                                            class="text border pl5 mr20"/></li>
                                    <li><span class="display-ib mr5">KEY值：</span><input type="text" name="key"
                                                                                        value="<%StringHelper.filterQuoter(out, key);%>"
                                                                                        id="textfield"
                                                                                        class="text border pl5 mr20"/>
                                    </li>
                                    <li><span class="display-ib mr5">常量类型：</span>
                                        <select name="type" class="border mr20 h32 mw100">
                                            <option value="">全部</option>
                                            <%VariableType[] variableTypes = resourceProvider.getVariableTypes(); %>
                                            <%if (variableTypes != null) for (VariableType variableType : variableTypes) {%>
                                            <option value="<%=variableType.getId() %>" <%=variableType.getId().equals(type) ? "selected=\"selected\"" : "" %>><%=variableType.getName() %>
                                            </option>
                                            <%}%>
                                        </select>
                                    </li>
                                    <li>
                                        <a href="javascript:void(0)" onclick="onSubmit()"
                                           class="btn btn-blue radius-6 mr5 pl1 pr15">
                                            <i class="icon-i w30 h30 va-middle search-icon " name="input"></i>搜索</a>
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
                                    <th>常量名称</th>
                                    <th>KEY值</th>
                                    <th>常量值</th>
                                    <th>操作</th>
                                </tr>
                                </thead>
                                <tbody class="f12">
                                <%
                                    VariableBean[] variables = (result == null ? null : result.getItems());
                                    if (variables != null && variables.length != 0) {
                                        int i = 1;
                                        HTMLFilter htmlFilter = new HTMLFilter(out);
                                        for (VariableBean variable : variables) {
                                            if (variable == null) {
                                                continue;
                                            }
                                %>
                                <tr class="title tc">
                                    <td><%=i++%>
                                    </td>
                                    <td title="<%StringHelper.filterHTML(out, variable.getDescription());%>"
                                        align="left">
                                        <%StringHelper.truncation(htmlFilter, variable.getDescription(), 50);%>
                                    </td>
                                    <td title="<%StringHelper.filterHTML(out, variable.getKey());%>" align="left">
                                        <%StringHelper.truncation(htmlFilter, variable.getKey(), 50);%>
                                    </td>
                                    <td title="<%StringHelper.filterHTML(out, variable.getValue());%>" align="left">
                                        <%StringHelper.truncation(htmlFilter, variable.getValue(), 50);%>
                                    </td>
                                    <td><span>
										<%
                                            if (dimengSession.isAccessableResource(UpdateConstant.class)) {
                                        %>
										<a href="<%=controller.getURI(request,UpdateConstant.class)%>?key=<%=variable.getKey() %>&searchDes=<%=desEncode %>&searchKey=<%=key %>&searchType=<%=type %>&cur=<%=cur %>"
                                           class="libk-deepblue mr20">修改</a>
										<%} else { %>
										<span class="disabled">修改</span>
										<%} %>
										</span></td>
                                        <%
											}}else{
										%>
                                <tr class="title tc">
                                    <td colspan="5">暂无数据</td>
                                </tr>
                                <%} %>
                                </tbody>
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
    	$("input[name='paging.current']").val(1);
        $("#form1").submit();
    }
</script>
</body>
</html>