<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.S62.entities.T6248"%>
<%@page import="com.dimeng.p2p.S62.enums.T6230_F20" %>
<%@page import="com.dimeng.p2p.modules.bid.console.service.entity.Hkjllb" %>
<%@page import="com.dimeng.p2p.S62.enums.T6230_F13" %>
<%@page import="com.dimeng.p2p.S62.enums.T6230_F11" %>
<%@page import="com.dimeng.p2p.S62.entities.T6230" %>
<%@page import="com.dimeng.p2p.S61.enums.T6110_F06" %>
<%@page import="com.dimeng.util.ObjectHelper" %>
<%@page import="com.dimeng.p2p.console.servlets.AbstractConsoleServlet" %>
<%@ page import="com.dimeng.p2p.console.servlets.finance.zjgl.fksh.*" %>
<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <%@include file="/WEB-INF/include/script.jsp" %>
</head>
<body>
<%
    CURRENT_CATEGORY = "CWGL";
    CURRENT_SUB_CATEGORY = "FKGL";
    PagingResult<T6248> result = ObjectHelper.convert(request.getAttribute("t6248List"), PagingResult.class);
    T6248[] t6248List = result.getItems();
    T6110_F06 userType = EnumParser.parse(T6110_F06.class, request.getAttribute("userType").toString());
    T6230 t6230 = ObjectHelper.convert(request.getAttribute("t6230"), T6230.class);
    int loanId = IntegerParser.parse(request.getParameter("loanId"));
    int userId = IntegerParser.parse(request.getParameter("userId"));
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <div class="p20">
                    <div class="border">
                        <div class="title-container"><i class="icon-i w30 h30 va-middle title-left-icon"></i>查看借款信息
                        </div>
                    </div>
                    <!--切换栏目-->
                    <div class="border mt20">
                        <div class="tabnav-container">
                            <ul class="clearfix pr pr60">
                                <li>
                                    <a href="<%=controller.getURI(request, ViewProject.class)%>?loanId=<%=loanId%>&userId=<%=userId%>"
                                       class="tab-btn-click">项目信息</a></li>
                                <%if (userType == T6110_F06.ZRR) {%>
                                <li>
                                    <a href="<%=controller.getURI(request, ViewUserInfo.class)%>?loanId=<%=loanId%>&userId=<%=userId%>"
                                       class="tab-btn-click">个人信息</a></li>
                                <li>
                                    <a href="<%=controller.getURI(request, DetailAuthentication.class)%>?loanId=<%=loanId%>&userId=<%=userId%>"
                                       class="tab-btn-click">个人认证信息</a></li>
                                <%}%>

                                <%if (userType == T6110_F06.FZRR) {%>
                                <li>
                                    <a href="<%=controller.getURI(request, ViewEnterprise.class)%>?loanId=<%=loanId%>&userId=<%=userId%>"
                                       class="tab-btn-click">企业信息</a></li>
                                <%}%>
                                <%if (T6230_F13.S == t6230.F13) {%>
                                <li>
                                    <a href="<%=controller.getURI(request, ViewDyw.class)%>?loanId=<%=loanId%>&userId=<%=userId%>"
                                       class="tab-btn-click">抵押物信息</a></li>
                                <%}%>
                                <%if (T6230_F11.S == t6230.F11) {%>
                                <li>
                                    <a href="<%=controller.getURI(request, ViewGuarantee.class)%>?loanId=<%=loanId%>&userId=<%=userId%>"
                                       class="tab-btn-click">担保信息</a></li>
                                <%}%>
                                <li>
                                    <a href="<%=controller.getURI(request, DetailAnnexMsk.class)%>?loanId=<%=loanId%>&userId=<%=userId%>"
                                       class="tab-btn-click">附件(马赛克)</a></li>
                                <li>
                                    <a href="<%=controller.getURI(request, DetailAnnexWz.class)%>?loanId=<%=loanId%>&userId=<%=userId%>"
                                       class="tab-btn-click">附件(完整版)</a></li>

                                <%if (t6230.F20 != T6230_F20.SQZ && t6230.F20 != T6230_F20.DSH && t6230.F20 != T6230_F20.DFB && t6230.F20 != T6230_F20.YFB) {%>
                                <li>
                                    <a href="<%=controller.getURI(request, ViewRecord.class)%>?loanId=<%=loanId%>&userId=<%=userId%>"
                                       class="tab-btn-click">投资记录<i class="icon-i tab-arrowtop-icon"></i></a></li>
                                <%}%>
                                <%if (t6230.F20 != T6230_F20.SQZ && t6230.F20 != T6230_F20.DSH && t6230.F20 != T6230_F20.DFB && t6230.F20 != T6230_F20.YFB && t6230.F20 != T6230_F20.YLB) {%>
	                                <li>
	                                    <a href="<%=controller.getURI(request, ViewHkRecord.class)%>?loanId=<%=loanId%>&userId=<%=userId%>"
	                                       class="tab-btn-click">还款计划</a></li>
                                <%}%>
                                <li><a href="javascript:void(0)" class="tab-btn select-a">项目动态<i class="icon-i tab-arrowtop-icon"></i></a></li>

                                <li class="pa right0 top0 mt5"><input type="button" value="返回"
                                                                       class="btn btn-blue radius-6 pl20 pr20 ml40 mr10"
                                                                       onclick="window.location.href='<%=configureProvider.format(URLVariable.FKSHLIST_URL)%>'">
                                </li>
                            </ul>
                        </div>
                        <form id="form_loan"
                              action="<%=controller.getURI(request, ViewProgresBidInfo.class)%>"
                              method="get">
                            <input name="userId" value="<%=request.getParameter("userId") %>" type="hidden"/>
                            <input name="loanId" value="<%=request.getParameter("loanId") %>" type="hidden"/>

                            <div class="tab-content-container p20">
                                <table class="table table-style gray6 tl">
                                    <thead>
                                    <tr class="title">
                                        <th>序号</th>
                                        <th>标题时间</th>
                                        <th>主题标题</th>
                                        <th>是否发布</th>
                                        <th>发布者</th>
                                        <th>发布时间</th>
                                    </tr>
                                    </thead>
                                    <tbody class="f12">
                                    <%
	                                    if (t6248List != null && t6248List.length > 0) {
	                                        int i = 1;
	                                        for (T6248 t6248 : t6248List) {
	                                            if (t6248 == null) {
	                                                continue;
	                                            }
                                    %>
                                    <tr class="tc">
                                        <td><%=i++ %></td>
	                                    <td><%=DateTimeParser.format(t6248.F08, "yyyy-MM-dd") %></td>
	                                    <td title="<%StringHelper.filterHTML(out, t6248.F04); %>">
	                                        <%StringHelper.filterHTML(out, StringHelper.truncation(t6248.F04, 10)); %>
	                                    </td>
	                                    <td><%StringHelper.filterHTML(out, t6248.F05.getChineseName()); %></td>
	                                    <td><%= t6248.sysName%></td>
	                                    <td><%=DateTimeParser.format(t6248.F07) %></td>
                                    </tr>
                                    <%
                                        }
                                    } else {
                                    %>
                                    <tr>
                                        <td colspan="6" class="tc">暂无数据</td>
                                    </tr>
                                    <%} %>
                                    </tbody>
                                </table>
                                <%AbstractConsoleServlet.rendPagingResult(out, result); %>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</body>
</html>