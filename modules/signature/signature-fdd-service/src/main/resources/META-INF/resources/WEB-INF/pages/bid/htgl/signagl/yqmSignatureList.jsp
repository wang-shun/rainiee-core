<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.console.servlets.bid.htgl.signagl.YqmSignatureList"%>
<%@page import="com.dimeng.p2p.modules.bid.console.service.entity.Dzqm"%>
<%@page import="com.dimeng.p2p.S62.enums.T6273_F15"%>
<%@page import="com.dimeng.p2p.S62.enums.T6273_F08"%>
<%@page import="com.dimeng.p2p.S62.enums.T6273_F10"%>
<%@page import="com.dimeng.p2p.console.servlets.bid.htgl.signagl.SignatureList"%>
<%@page import="com.dimeng.p2p.console.servlets.bid.htgl.agreement.QyjkDzxy"%>
<%@page import="com.dimeng.p2p.console.servlets.AbstractConsoleServlet" %>
<%@page import="com.dimeng.p2p.console.servlets.bid.htgl.agreement.Index" %>
<%@page import="com.dimeng.p2p.S61.entities.T6161" %>
<%@page import="com.dimeng.p2p.modules.account.console.service.QyManage" %>
<%@page import="com.dimeng.p2p.console.servlets.bid.htgl.agreement.ZqzrDzxy" %>
<%@page import="com.dimeng.p2p.console.servlets.bid.htgl.agreement.GrjkDzxy" %>
<%@page import="com.dimeng.p2p.modules.bid.console.service.entity.Qyjkxy" %>
<%@page import="com.dimeng.p2p.signature.fdd.variables.FddVariable"%>
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
    PagingResult<Dzqm> result = (PagingResult<Dzqm>) request.getAttribute("result");
    Dzqm[] dzqmList = result.getItems();
    String createTimeStart = request.getParameter("createTimeStart");
    String createTimeEnd = request.getParameter("createTimeEnd");
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <form id="form2" action="<%=controller.getURI(request, YqmSignatureList.class)%>" method="post">
                    <div class="p20">
                        <div class="border">
                            <div class="title-container">
                                <i class="icon-i w30 h30 va-middle title-left-icon"></i>电子签章管理
                            </div>
                            <div class="content-container pl40 pt30 pr40">
                                <ul class="gray6 input-list-container clearfix">
                                    <li><span class="display-ib mr5">用户名</span>
                                        <input type="text" name="loginName" class="text border pl5 mr20"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("loginName"));%>"/>
                                    </li>
                                    
                                    <li><span class="display-ib mr5">姓名/企业名称</span>
                                        <input type="text" name="name" class="text border pl5 mr20"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("name"));%>"/>
                                    </li>
                                    <li><span class="display-ib mr5">客户编号</span>
                                        <input type="text" name="code" class="text border pl5 mr20"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("code"));%>"/>
                                    </li>
                                    <li><span class="display-ib mr5">标的编号</span>
                                        <input type="text" name="bidCode" class="text border pl5 mr20"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("bidCode"));%>"/>
                                    </li>
                                    <li><span class="display-ib mr5">交易号</span>
                                        <input type="text" name="tradeCode" class="text border pl5 mr20"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("tradeCode"));%>"/>
                                    </li>
                                    <li><span class="display-ib mr5">合同标题</span>
                                        <input type="text" name="htTitle" class="text border pl5 mr20"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("htTitle"));%>"/>
                                    </li>
                                    <li><span class="display-ib mr5">合同编号</span>
                                        <input type="text" name="htCode" class="text border pl5 mr20"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("htCode"));%>"/>
                                    </li>
                                    <li><span class="display-ib mr5">客户类型</span>
                                        <select name="userType" class="border mr20 h32 mw100">
                                            <option value="">全部</option>
                                            <%
                                                for (T6273_F10 t6273_F10 : T6273_F10.values()) {
                                            %>
                                            <option value="<%=t6273_F10.name()%>" <%if (t6273_F10.name().equals(request.getParameter("userType"))) {%>
                                                    selected="selected" <%}%>><%=t6273_F10.getChineseName()%>
                                            </option>
                                            <%
                                                }
                                            %>
                                        </select>
                                    </li>
                                    <li><span class="display-ib mr5">合同类型</span>
                                        <select name="htType" class="border mr20 h32 mw100">
                                            <option value="">全部</option>
                                            <%
                                                for (T6273_F08 t6273_F08 : T6273_F08.values()) {
                                            %>
                                            <option value="<%=t6273_F08.name()%>" <%if (t6273_F08.name().equals(request.getParameter("htType"))) {%>
                                                    selected="selected" <%}%>><%=t6273_F08.getChineseName()%>
                                            </option>
                                            <%
                                                }
                                            %>
                                        </select>
                                    </li>
                                    <li><span class="display-ib mr5">签名状态</span>
                                        <select name="qmType" class="border mr20 h32 mw100">
                                            <option value="">全部</option>
                                            <%
                                                for (T6273_F15 t6273_F15 : T6273_F15.values()) {
                                            %>
                                            <option value="<%=t6273_F15.name()%>" <%if (t6273_F15.name().equals(request.getParameter("qmType"))) {%>
                                                    selected="selected" <%}%>><%=t6273_F15.getChineseName()%>
                                            </option>
                                            <%
                                                }
                                            %>
                                        </select>
                                    </li>
                                    <li><span class="display-ib mr5">请求时间</span> <input
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
                                    <li><a href="<%=controller.getURI(request, SignatureList.class)%>" class="tab-btn ">未签名</a>
                                    </li>
                                    <li><a href="javascript:void(0)" class="tab-btn  select-a">已签名<i class="icon-i tab-arrowtop-icon"></i></a></li>
                                </ul>
                            </div>
                            <div class=" table-container p20">
                                <table class="table table-style gray6 tl">
                                    <thead>
                                    <tr class="title tc">
                                        <th>序号</th>
                                        <th>用户名</th>
                                        <th>姓名/企业名称</th>
                                        <th>客户类型</th>
                                        <th>客户编号</th>
<!--                                         <th>身份证号</th> -->
<!--                                         <th>手机号</th> -->
                                        <th>标的编号</th>
                                        <th>合同标题</th>
                                        <th>合同编号</th>
                                        <th>合同类型</th>
                                        <th>交易号</th>
                                        <th>请求时间</th>
                                        <th>状态</th>
                                        <th>操作</th>
                                    </tr>
                                    </thead>
                                    <tbody class="f12">
                                    <%
                                        if (dzqmList != null && dzqmList.length > 0) {
                                            int i = 1;
                                            for (Dzqm dzqm : dzqmList) {
                                                if (dzqm == null) {
                                                    continue;
                                                }
                                    %>
                                    <tr class="tc">
                                        <td><%=i++%>
                                        </td>
                                        <td align="center" title="<%StringHelper.filterHTML(out, dzqm.loginName); %>"><%
                                            StringHelper.filterHTML(out, StringHelper.truncation(dzqm.loginName, 15)); %></td>
                                        <td align="center"><%StringHelper.filterHTML(out, dzqm.name);%></td>
                                        <td align="center"><%=dzqm.F10.getChineseName()%></td>
                                        <td align="center"><%StringHelper.filterHTML(out, dzqm.userCode);%></td>
<%--                                         <td align="center"><%StringHelper.filterHTML(out, dzqm.sfzh);%></td> --%>
<%--                                         <td align="center"><%StringHelper.filterHTML(out, dzqm.phone);%></td> --%>
                                        <td align="center"><%StringHelper.filterHTML(out, dzqm.bidCode);%></td>
                                        <td align="center"><%StringHelper.filterHTML(out, dzqm.htTitle);%></td>
                                        <td align="center"><%StringHelper.filterHTML(out, dzqm.F04);%></td>
                                        <td align="center"><%=dzqm.F08.getChineseName()%></td>
                                        <td align="center"><%StringHelper.filterHTML(out, dzqm.F19);%></td>
                                        <td align="center"><%=TimestampParser.format(dzqm.F06) %></td>
                                        <td align="center"><%=dzqm.F15.getChineseName()%></td>
                                        </td>
                                        <td align="center">
                                            <a href="<%if(StringHelper.isEmpty(dzqm.docUrl)){ %>javascript:void(0);<%}else{ %><%=dzqm.docUrl %>" target="_blank"<%} %>"
                                               class="link-blue">查看</a>
                                            <%if(T6273_F15.DGD == dzqm.F15) {%>
                                            	<a   href="<%=configureProvider.format(FddVariable.FDD_EXTSIGN_AUTO_URL) %>?qmId=<%=dzqm.qmId %>&t=yq"
                                               class="link-blue">归档</a>
                                            <%} %>
                                        </td>
                                    </tr>
                                    <%
                                        }
                                    } else {
                                    %>
                                    <tr class="tc">
                                        <td colspan="15">暂无数据</td>
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