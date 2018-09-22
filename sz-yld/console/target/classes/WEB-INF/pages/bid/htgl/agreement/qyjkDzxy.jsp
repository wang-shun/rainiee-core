<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.console.servlets.bid.htgl.agreement.QyjkDzxy"%>
<%@page import="com.dimeng.p2p.console.servlets.AbstractConsoleServlet" %>
<%@page import="com.dimeng.p2p.console.servlets.bid.htgl.agreement.Index" %>
<%@page import="com.dimeng.p2p.S61.entities.T6161" %>
<%@page import="com.dimeng.p2p.modules.account.console.service.QyManage" %>
<%@page import="com.dimeng.p2p.console.servlets.bid.htgl.agreement.ZqzrDzxy" %>
<%@page import="com.dimeng.p2p.console.servlets.bid.htgl.agreement.GrjkDzxy" %>
<%@page import="com.dimeng.p2p.console.servlets.bid.htgl.agreement.DfDzxy" %>
<%@page import="com.dimeng.p2p.modules.bid.console.service.entity.Qyjkxy" %>
<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <%@include file="/WEB-INF/include/script.jsp" %>
</head>
<body>
<%
    CURRENT_CATEGORY = "YWGL";
    CURRENT_SUB_CATEGORY = "DZXY";
    PagingResult<Qyjkxy> result = (PagingResult<Qyjkxy>) request.getAttribute("result");
    Qyjkxy[] qyjkxys = result.getItems();
    String createTimeStart = request.getParameter("createTimeStart");
    String createTimeEnd = request.getParameter("createTimeEnd");
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <form id="form2" action="<%=controller.getURI(request, QyjkDzxy.class)%>" method="post">
                    <div class="p20">
                        <div class="border">
                            <div class="title-container">
                                <i class="icon-i w30 h30 va-middle title-left-icon"></i>企业借款协议管理
                            </div>
                            <div class="content-container pl40 pt30 pr40">
                                <ul class="gray6 input-list-container clearfix">
                                    <li><span class="display-ib mr5">借款账号</span>
                                        <input type="text" name="name" class="text border pl5 mr20"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("name"));%>"/>
                                    </li>
                                    <li><span class="display-ib mr5">签订日期</span> <input
                                            type="text" name="createTimeStart" readonly="readonly"
                                            id="datepicker1" class="text border pl5 w120 date"/> <span
                                            class="pl5 pr5">至</span> <input type="text"
                                                                            readonly="readonly" name="createTimeEnd"
                                                                            id="datepicker2"
                                                                            class="text border pl5 w120 date mr20"/>
                                    </li>

                                    <li><a href="javascript:$('#form2').submit();"
                                           class="btn btn-blue radius-6 mr5 pl1 pr15"><i
                                            class="icon-i w30 h30 va-middle search-icon "></i>搜索</a></li>

                                </ul>
                            </div>
                        </div>
                        <div class="border mt20">
                            <div class="tabnav-container">
                                <ul class="clearfix">
                                    <li><a href="javascript:void(0)" class="tab-btn  select-a">企业借款协议<i class="icon-i tab-arrowtop-icon"></i></a></li>
                                    <li><a href="<%=controller.getURI(request, GrjkDzxy.class)%>" class="tab-btn ">个人借款协议</a>
                                    </li>
                                    <li><a href="<%=controller.getURI(request, ZqzrDzxy.class)%>" class="tab-btn ">债权转让协议</a>
                                    </li>
                                    <li><a href="<%=controller.getURI(request, DfDzxy.class)%>" class="tab-btn ">垫付协议</a></li>
                                    <%if(Boolean.parseBoolean(configureProvider.getProperty(BadClaimVariavle.IS_BADCLAIM_TRANSFER))){%>
	                                    <li><a href="/console/bid/htgl/agreement/blzqzrDzxy.htm" class="tab-btn ">不良债权转让协议</a>
	                                    </li>
                                    <%} %>
                                </ul>
                            </div>
                            <div class=" table-container p20">
                                <table class="table table-style gray6 tl">
                                    <thead>
                                    <tr class="title tc">
                                        <th>序号</th>
                                        <th>借款标题</th>
                                        <th>借款账号</th>
                                        <th>企业名称</th>
                                        <th>签订日期</th>
                                        <th>操作</th>

                                    </tr>
                                    </thead>
                                    <tbody class="f12">
                                    <%
                                        QyManage qyManage = serviceSession.getService(QyManage.class);
                                        if (qyjkxys != null && qyjkxys.length > 0) {
                                            int i = 1;
                                            for (Qyjkxy qyjkxy : qyjkxys) {
                                                if (qyjkxy == null) {
                                                    continue;
                                                }

                                                T6161 t6161 = qyManage.getQyxx(qyjkxy.F02);
                                    %>
                                    <tr class="tc">
                                        <td><%=i++%>
                                        </td>
                                        <td align="center" title="<%StringHelper.filterHTML(out, qyjkxy.F03); %>"><%
                                            StringHelper.filterHTML(out, StringHelper.truncation(qyjkxy.F03, 15)); %></td>
                                        <td align="center"><%StringHelper.filterHTML(out, qyjkxy.jkLoginName);%></td>
                                        <td align="center"><%StringHelper.filterHTML(out, t6161.F04);%></td>
                                        <td align="center"><%=TimestampParser.format(qyjkxy.F22) %>
                                        </td>
                                        <td align="center">
                                            <a target="_blank"
                                               href="<%configureProvider.format(out, URLVariable.XY_PTHTDZXY); %>?id=<%=qyjkxy.F01 %>"
                                               class="link-blue">合同</a>
                                        </td>
                                    </tr>
                                    <%
                                        }
                                    } else {
                                    %>
                                    <tr class="tc">
                                        <td colspan="6">暂无数据</td>
                                    </tr>
                                    <%} %>
                                    </tbody>
                                </table>
                            </div>
                        </div>
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

<div id="info"></div>

<%@include file="/WEB-INF/include/datepicker.jsp" %>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/dialog.js"></script>
<script type="text/javascript">
    $(function () {
        $("#datepicker1").datepicker({
            inline: true,
            onSelect: function (selectedDate) {
                $("#datepicker2").datepicker("option", "minDate", selectedDate);
            }
        });
        $('#datepicker1').datepicker('option', {dateFormat: 'yy-mm-dd'});
        $("#datepicker2").datepicker({inline: true});
        $('#datepicker2').datepicker('option', {dateFormat: 'yy-mm-dd'});
        <%if(!StringHelper.isEmpty(createTimeStart)){%>
        $("#datepicker1").val("<%StringHelper.filterHTML(out, request.getParameter("createTimeStart"));%>");
        <%}%>
        <%if(!StringHelper.isEmpty(createTimeEnd)){%>
        $("#datepicker2").val("<%StringHelper.filterHTML(out, request.getParameter("createTimeEnd"));%>");
        <%}%>
        $("#datepicker2").datepicker('option', 'minDate', $("#datepicker1").datepicker().val());
    });

</script>

</body>
</html>