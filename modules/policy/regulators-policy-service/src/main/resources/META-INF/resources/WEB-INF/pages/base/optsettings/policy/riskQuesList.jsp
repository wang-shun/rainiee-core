<%@page import="com.dimeng.p2p.console.servlets.base.optsettings.policy.AddRiskQues" %>
<%@page import="com.dimeng.p2p.console.servlets.base.optsettings.policy.RiskQuesList" %>
<%@page import="com.dimeng.p2p.console.servlets.base.optsettings.policy.UpdateRiskQues" %>
<%@page import="com.dimeng.p2p.repeater.policy.query.QuesQuery" %>
<%@page import="com.dimeng.util.ObjectHelper" %>
<%@ page import="com.dimeng.p2p.console.servlets.base.optsettings.policy.UpdateQuesStatus" %>
<%@ page import="com.dimeng.p2p.S61.enums.T6149_F07" %>
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
    CURRENT_SUB_CATEGORY = "FXPGWTSZ";
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
            <%
            PagingResult<QuesQuery> result = ObjectHelper.convert(request.getAttribute("result"), PagingResult.class);
            String errorInfo = ObjectHelper.convert(request.getAttribute("errorInfo"), String.class);
        %>
                <!--加载内容-->
                <div class="p20">
                    <form action="<%=controller.getURI(request, RiskQuesList.class)%>" method="post" id="form1">
                        <div class="border">
                            <div class="title-container"><i class="icon-i w30 h30 va-middle title-left-icon"></i>风险评估问题设置
                            </div>
                            <div class="content-container pl40 pt30 pr40">
                                <ul class="gray6 input-list-container clearfix">

                                    <li>
                                        <span class="display-ib mr5">问题描述：</span><input type="text" name="title"
                                                                                        id="textfield4"
                                                                                        class="text border pl5 mr20"
                                                                                        value="<%StringHelper.filterHTML(out, request.getParameter("title"));%>"/>
                                    </li>
                                    <li><span class="display-ib mr5">状态</span>
                                        <%
                                            String status = request.getParameter("status1");
                                        %>
                                        <select id="" class="border mr20 h32 mw100" name="status1">
                                            <option value="">全部</option>
                                            <%  for(T6149_F07 c : T6149_F07.values()) {  %>
                                            <option value="<%=c.name()%>" <%if(c.name().equals(status)){%>selected="selected"<%}%>>
                                                <%StringHelper.filterHTML(out, c.getChineseName());%>
                                            </option>
                                                <%}%>
                                        </select>
                                    </li>
                                    <li>
                                        <a href="javascript:void(0)" onclick="onSubmit()"
                                           class="btn btn-blue radius-6 mr5 pl1 pr15">
                                            <i class="icon-i w30 h30 va-middle search-icon " name="input"></i>搜索</a>
                                        <%if (dimengSession.isAccessableResource(AddRiskQues.class)) {%>
                                        <a href="<%=controller.getURI(request, AddRiskQues.class)%>"
                                           class="btn btn-blue radius-6 mr5 pl1 pr15">
                                            <i class="icon-i w30 h30 va-middle add-icon "></i>新增问题</a>
                                        <%} else { %>
                                        <span class="btn btn-gray radius-6 mr5 pl1 pr15"><i
                                                class="icon-i w30 h30 va-middle add-icon "></i>新增问题</span>
                                        <%} %>
                                    </li>
                                </ul>
                            </div>
                        </div>
                        <div class="mt20 table-container">
                            <table class="table table-style gray6 tl">
                                <thead>
                                <tr class="title tc">
                                    <th>序号</th>
                                    <th>问题描述</th>
                                    <th>选项A</th>
                                    <th>选项B</th>
                                    <th>选项C</th>
                                    <th>选项D</th>
                                    <th>状态</th>
                                    <th>排序值</th>
                                    <th>修改人</th>
                                    <th>修改时间</th>
                                    <th>操作</th>
                                </tr>
                                </thead>
                                <tbody class="f12">
                                <%
                                    QuesQuery[] quesList = (result == null ? null : result.getItems());
                                    if (quesList != null && quesList.length != 0) {
                                        int index = 1;
                                        for (QuesQuery quesQuery : quesList) {
                                %>
                                <tr class="title tc">
                                    <td class="tc"><%=index++%>
                                    </td>
                                    <td title="<%=quesQuery.F02%>"><%
                                        StringHelper.filterHTML(out, StringHelper.truncation(quesQuery.F02,15));
                                    %></td>
                                    <td title="<%=quesQuery.F03%>"><%
                                        StringHelper.filterHTML(out, StringHelper.truncation(quesQuery.F03,15));
                                    %></td>
                                    <td title="<%=quesQuery.F04%>"><%StringHelper.filterHTML(out, StringHelper.truncation(quesQuery.F04,15)); %>
                                    </td>
                                    <td title="<%=quesQuery.F05%>"><%StringHelper.filterHTML(out, StringHelper.truncation(quesQuery.F05,15)); %>
                                    </td>
                                    <td title="<%=quesQuery.F06%>"><% StringHelper.filterHTML(out, StringHelper.truncation(quesQuery.F06,15));%>
                                    </td>
                                    <td><%StringHelper.filterHTML(out, quesQuery.F07.getChineseName()); %>
                                    </td>
                                    <td><%=quesQuery.F08 %>
                                    </td>
                                    <td><%=quesQuery.opertor %>
                                    </td>
                                    <td><%=DateTimeParser.format(quesQuery.F09,"yyyy-MM-dd HH:mm:ss") %>
                                    </td>
                                    <td>
                                        <%if (dimengSession.isAccessableResource(UpdateRiskQues.class)) {%>
                                        <span><a
                                                href="<%=controller.getURI(request, UpdateRiskQues.class)%>?F01=<%=quesQuery.F01%>"
                                                class="libk-deepblue mr20">修改</a></span>
                                        <%} else { %>
                                        <span class="diabled mr10">修改</span>
                                        <%} %>
                                        <%
                                            String desc = T6149_F07.QY == quesQuery.F07 ? "停用" : "启用";
                                            if (dimengSession.isAccessableResource(UpdateQuesStatus.class)) {
                                        %>
                                        <a href="<%=controller.getURI(request,UpdateQuesStatus.class)%>?F01=<%=quesQuery.F01%>&status=<%=T6149_F07.QY == quesQuery.F07 ? T6149_F07.TY.name() : T6149_F07.QY.name()%>" class="link-blue"><%=desc%></a>
                                        <%}else{%>
                                        <span class="diabled mr10"><%=desc%></span>
                                        <%}%>
                                    </td>
                                </tr>
                                <%
                                    }
                                } else {
                                %>
                                <tr class="title tc">
                                    <td colspan="11">暂无数据</td>
                                </tr>
                                <%} %>
                                </tbody>
                            </table>
                        </div>
                        <%
                            RiskQuesList.rendPagingResult(out, result);%>
                    </form>
                </div>
                <!--加载内容 结束-->
            </div>
        </div>
    </div>
    <!--右边内容 结束-->
<!--内容-->
<div id="info"></div>
<div class="popup_bg" style="display: none;"></div>
<script type="text/javascript"
        src="<%=controller.getStaticPath(request)%>/js/dialog.js"></script>
<%
    String errorMessage = controller.getPrompt(request, response, PromptLevel.ERROR);

    if (!StringHelper.isEmpty(errorInfo)) {
        errorMessage = errorInfo;
    }
    if (!StringHelper.isEmpty(errorMessage)) {
%>
<script type="text/javascript">
    $("#info").html(showDialogInfo('<%=errorMessage%>', "wrong"));
</script>
<%} %>
<script type="text/javascript">
//查询
    function onSubmit() {
        $("#form1").submit();
    }
</script>
</body>
</html>