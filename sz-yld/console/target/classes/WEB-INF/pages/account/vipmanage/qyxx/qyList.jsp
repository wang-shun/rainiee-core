<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.qyxx.ShjlList"%>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.qyxx.ShList"%>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.qyxx.QyList"%>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.qyxx.ExportQy" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.qyxx.ViewQyxx" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.qyxx.UpdateQyxx" %>
<%@page import="com.dimeng.p2p.S61.enums.T6110_F07" %>
<%@page import="com.dimeng.p2p.S61.enums.T6110_F17" %>
<%@page import="com.dimeng.p2p.console.servlets.AbstractConsoleServlet" %>
<%@page import="com.dimeng.p2p.modules.account.console.service.entity.Enterprise" %>
<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <%@include file="/WEB-INF/include/script.jsp" %>
</head>
<body>
<%
    CURRENT_CATEGORY = "YHGL";
    CURRENT_SUB_CATEGORY = "QY";
    PagingResult<Enterprise> list = (PagingResult<Enterprise>) request.getAttribute("list");

    Enterprise[] enterpriseArray = null;
    if (list != null) {
        enterpriseArray = list.getItems();
    }
    String createTimeStart = request.getParameter("createTimeStart");
    String createTimeEnd = request.getParameter("createTimeEnd");
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <form id="form1" action="<%=controller.getURI(request, QyList.class)%>" method="post">
                    <div class="p20">
                        <div class="border">
                            <div class="title-container"><i class="icon-i w30 h30 va-middle title-left-icon"></i>企业信息
                            </div>
                            <div class="content-container pl40 pt30 pr40">
                                <ul class="gray6 input-list-container clearfix">
                                    <li><span class="display-ib mr5">企业账号</span>
                                        <input type="text" name="account"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("account"));%>"
                                               class="text border pl5 mr20"/>
                                    </li>
                                    <li><span class="display-ib mr5">企业名称</span>
                                        <input type="text" name="userName"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("userName"));%>"
                                               class="text border pl5 mr20"/>
                                    </li>
                                    <li><span class="display-ib mr5">联系人姓名</span>
                                        <input type="text" name="realName"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("realName"));%>"
                                               class="text border pl5 mr20"/>
                                    </li>
                                    <li><span class="display-ib mr5">联系人电话</span>
                                        <input type="text" name="msisdn"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("msisdn"));%>"
                                               class="text border pl5 mr20"/>
                                    </li>

                                    <li><span class="display-ib mr5">注册时间</span>
                                        <input type="text" name="createTimeStart" readonly="readonly" id="datepicker1"
                                               class="text border pl5 w120 date"/>
                                        <span class="pl5 pr5">至</span>
                                        <input readonly="readonly" type="text" name="createTimeEnd" id="datepicker2"
                                               class="text border pl5 w120 mr20 date"/>
                                    </li>
                                    <li><span class="display-ib mr5">状态</span>
                                        <select name="userState" class="border mr10 h32 mw60">
                                            <option value="">全部</option>
                                            <%
                                                for (T6110_F07 userState : T6110_F07.values()) {
                                            %>
                                            <option value="<%=userState.name()%>" <%if (userState.name().equals(request.getParameter("userState"))) {%>
                                                    selected="selected" <%}%>><%=userState.getChineseName()%>
                                            </option>
                                            <%
                                                }
                                            %>
                                        </select>
                                    </li>
                                    <li><span class="display-ib mr5">是否允许投资</span>
                                        <select name="investorType" class="border mr10 h32 mw60">
                                            <option value="">全部</option>
                                            <%
                                                for (T6110_F17 investorType : T6110_F17.values()) {
                                            %>
                                            <option value="<%=investorType.name()%>" <%if (investorType.name().equals(request.getParameter("investorType"))) {%>
                                                    selected="selected" <%}%>><%=investorType.getChineseName()%>
                                            </option>
                                            <%
                                                }
                                            %>
                                        </select>
                                    </li>
                                    <li><a href="javascript:void(0)" onclick="onSubmit()"
                                           class="btn btn-blue radius-6 mr5 pl1 pr15"><i
                                            class="icon-i w30 h30 va-middle search-icon "></i>搜索</a></li>
                                    <li>
                                        <%
                                            if (dimengSession.isAccessableResource(ExportQy.class)) {
                                        %>
                                        <a href="javascript:void(0)" onclick="showExport()"
                                           class="btn btn-blue radius-6 mr5  pl1 pr15"><i
                                                class="icon-i w30 h30 va-middle export-icon "></i>导出</a>
                                        <%
                                        } else {
                                        %>
                                        <span class="btn btn-gray radius-6 mr5  pl1 pr15"><i
                                                class="icon-i w30 h30 va-middle export-icon "></i>导出</span>
                                        <%
                                            }
                                        %>
                                    </li>
                                </ul>
                            </div>
                        </div>
                        <div class="border mt20 table-container">
                            <table class="table table-style gray6 tl">
                                <thead>
                                <tr class="title">
                                    <th class="tc">序号</th>
                                    <th class="tc">企业账号</th>
                                    <th class="tc">企业名称</th>
                                    <th class="tc">联系人姓名</th>
                                    <th class="tc">联系人电话</th>
                                    <th class="tc">营业执照/社会信用代码</th>
                                    <th class="tc">注册时间</th>
                                    <th class="tc">状态</th>
                                    <th class="tc">是否允许投资</th>
                                    <th class="w200" style="text-align: center;">操作</th>
                                </tr>
                                </thead>
                                <tbody class="f12">
                                <%
                                    if (enterpriseArray != null) {
                                        for (int i = 0; i < enterpriseArray.length; i++) {
                                            Enterprise enterprise = enterpriseArray[i];
                                            if (enterprise == null) {
                                                continue;
                                            }
                                %>
                                <tr class="tc">
                                    <td><%=i + 1%>
                                    </td>
                                    <td><%=enterprise.F02%>
                                    </td>
                                    <td><%StringHelper.filterHTML(out, enterprise.F10);%></td>
                                    <td><%StringHelper.filterHTML(out, enterprise.lxr);%></td>
                                    <td><%StringHelper.filterHTML(out, enterprise.lxrPhone);%></td>
                                    <td><%StringHelper.filterHTML(out, "Y".equals(enterprise.isShxydm)?enterprise.shxydm:enterprise.F09);%></td>
                                    <td><%=TimestampParser.format(enterprise.F07)%>
                                    </td>
                                    <td><%=enterprise.F06.getChineseName()%>
                                    </td>
                                    <td><%=enterprise.investType.getChineseName()%>
                                    </td>
                                    <td>
                                    	<%if (dimengSession.isAccessableResource(ViewQyxx.class)) {%>
                                        <a href="<%=controller.getURI(request,ViewQyxx.class)%>?id=<%=enterprise.F01%>" class="link-blue mr20 click-link">查看</a>
                                        <%}else{ %>
                                        <span class="disabled mr20">查看</span>
                                        <%} %>
                                        <%if (dimengSession.isAccessableResource(UpdateQyxx.class)) {%>
                                        <a href="<%=controller.getURI(request,UpdateQyxx.class)%>?id=<%=enterprise.F01%>" class="link-blue mr20">修改</a>
                                        <%} else { %>
                                        <span class="disabled mr20">修改</span>
                                        <%} %>
                                        <%if (dimengSession.isAccessableResource(ShList.class)) {%>
			                          		<%if("0".equals(enterprise.dshFlg)){ %>
			                          			<a href="<%=controller.getURI(request,ShList.class)%>?id=<%=enterprise.F01%>" class="libk-deepblue mr20">审核</a>
			                          		<%}else{ %>
			                          			<a href="<%=controller.getURI(request,ShList.class)%>?id=<%=enterprise.F01%>" class="libk-deepblue mr20">待审核</a>
			                          		<%} %>
			                          	<%}else{ %>
			                          		<%if("0".equals(enterprise.dshFlg)){ %>
			                          			<span class="disabled mr20">审核</span>
			                          		<%}else{ %>
			                          			<span class="disabled mr20">待审核</span>
			                          		<%} %>
			                          	<%} %>
			                          	<%if (dimengSession.isAccessableResource(ShjlList.class)) {%>
			                          		<a href="<%=controller.getURI(request, ShjlList.class)%>?yhId=<%=enterprise.F01%>" class="link-orangered mr20">审核记录</a>
			                        	<%}else{ %>
			                        		<span class="disabled mr20">审核记录</span>
			                        	<%} %>
                                    </td>
                                </tr>
                                <%
                                    }
                                } else {
                                %>
                                <tr class="dhsbg">
                                    <td colspan="10" class="tc">暂无数据</td>
                                </tr>
                                <%} %>
                                </tbody>
                            </table>
                        </div>

                        <!--分页-->
                        <%
                            AbstractConsoleServlet.rendPagingResult(out, list);
                        %>
                        <!--分页 end-->
                    </div>
                </form>

            </div>
        </div>
    </div>
    <!--右边内容 结束-->

<%@include file="/WEB-INF/include/datepicker.jsp" %>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/validation.js"></script>
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

    function showExport() {
        document.getElementById("form1").action = "<%=controller.getURI(request, ExportQy.class)%>";
        $("#form1").submit();
        document.getElementById("form1").action = "<%=controller.getURI(request, QyList.class)%>";
    }

    function onSubmit() {
        $("#form1").submit();
    }
</script>
</body>
</html>