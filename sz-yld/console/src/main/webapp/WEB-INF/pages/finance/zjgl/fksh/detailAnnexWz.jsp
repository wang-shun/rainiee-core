<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.console.servlets.finance.zjgl.fksh.ViewProgresBidInfo"%>
<%@page import="com.dimeng.p2p.console.servlets.finance.zjgl.fksh.FkshList"%>
<%@page import="com.dimeng.p2p.console.servlets.finance.zjgl.fksh.DetailAuthentication" %>
<%@page import="com.dimeng.p2p.console.servlets.finance.zjgl.fksh.ViewHkRecord" %>
<%@page import="com.dimeng.p2p.console.servlets.finance.zjgl.fksh.ViewRecord" %>
<%@page import="java.math.BigDecimal" %>
<%@page import="com.dimeng.p2p.modules.bid.console.service.entity.WzAnnex" %>
<%@page import="com.dimeng.p2p.S62.enums.T6230_F20" %>
<%@page import="com.dimeng.p2p.S62.enums.T6230_F13" %>
<%@page import="com.dimeng.p2p.S62.enums.T6230_F11" %>
<%@page import="com.dimeng.p2p.S62.entities.T6230" %>
<%@page import="com.dimeng.p2p.S61.enums.T6110_F06" %>
<%@page import="com.dimeng.p2p.console.servlets.finance.zjgl.fksh.ViewAnnexWz" %>
<%@page import="com.dimeng.p2p.console.servlets.finance.zjgl.fksh.ViewEnterprise" %>
<%@page import="com.dimeng.p2p.console.servlets.finance.zjgl.fksh.ViewGuarantee" %>
<%@page import="com.dimeng.p2p.console.servlets.finance.zjgl.fksh.DetailAnnexMsk" %>
<%@page import="com.dimeng.p2p.console.servlets.finance.zjgl.fksh.ViewDyw" %>
<%@page import="com.dimeng.p2p.console.servlets.finance.zjgl.fksh.ViewUserInfo" %>
<%@page import="com.dimeng.p2p.console.servlets.finance.zjgl.fksh.ViewProject" %>
<%@page import="com.dimeng.util.ObjectHelper" %>
<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <%@include file="/WEB-INF/include/script.jsp"%>
</head>
<body>
<%
    CURRENT_CATEGORY = "CWGL";
    CURRENT_SUB_CATEGORY = "FKGL";
    WzAnnex[] t6233s = ObjectHelper.convertArray(request.getAttribute("t6233s"), WzAnnex.class);
    T6110_F06 userType = EnumParser.parse(T6110_F06.class, request.getAttribute("userType").toString());
    T6230 t6230 = ObjectHelper.convert(request.getAttribute("t6230"), T6230.class);
    int loanId = IntegerParser.parse(request.getParameter("loanId"));
    int userId = IntegerParser.parse(request.getParameter("userId"));
    BigDecimal creditAmount = ObjectHelper.convert(request.getAttribute("creditAmount"), BigDecimal.class);
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
                            <ul class="clearfix pr pr80">
                                <%if (loanId > 0) {%>
                                <li>
                                    <a href="<%=controller.getURI(request, ViewProject.class)%>?loanId=<%=loanId%>&userId=<%=userId%>"
                                       class="tab-btn-click">项目信息</a></li>
                                <%}%>
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
                                <li><a href="javascript:void(0);" class="tab-btn-click  select-a">附件(完整版)<i
                                        class="icon-i tab-arrowtop-icon"></i></a></li>
                                <%if (t6230.F20 != T6230_F20.SQZ && t6230.F20 != T6230_F20.DSH && t6230.F20 != T6230_F20.DFB && t6230.F20 != T6230_F20.YFB) {%>
                                <li>
                                    <a href="<%=controller.getURI(request, ViewRecord.class)%>?loanId=<%=loanId%>&userId=<%=userId%>"
                                       class="tab-btn-click">投资记录</a></li>
                                <%if(t6230.F20 != T6230_F20.YLB){%>
                                <li>
                                    <a href="<%=controller.getURI(request, ViewHkRecord.class)%>?loanId=<%=loanId%>&userId=<%=userId%>"
                                       class="tab-btn-click">还款计划</a></li>

                                <li><a href="<%=controller.getURI(request, ViewProgresBidInfo.class)%>?loanId=<%=loanId%>&userId=<%=userId%>"
                                       class="tab-btn ">项目动态</a></li>
                                <%}}%>
                                <li class="pa right0 top0 mt5"><input type="button" value="返回"
                                                                       class="btn btn-blue radius-6 pl20 pr20 ml40 mr10"
                                                                       onclick="window.location.href='<%=configureProvider.format(URLVariable.FKSHLIST_URL)%>'">
                                </li>
                            </ul>
                        </div>
                        <div class="tab-content-container p20">
                            <table class="table table-style gray6 tl">
                                <thead>
                                <tr class="title">
                                    <th class="tc">序号</th>
                                    <th class="tc">附件名称</th>
                                    <th class="tc">附件大小</th>
                                    <th class="tc">附件格式</th>
                                    <th class="tc">上传人</th>
                                    <th class="tc">附件类型</th>
                                    <th class="tc">上传时间</th>
                                    <th class="tc">操作</th>
                                </tr>
                                </thead>
                                <tbody class="f12">
                                <%
                                    if (t6233s != null && t6233s.length > 0) {
                                        int i = 1;
                                        for (WzAnnex t6233 : t6233s) {
                                            if (t6233 == null) {
                                                continue;
                                            }
                                %>
                                <tr class="tc">
                                    <td><%=i++%>
                                    </td>
                                    <td><%StringHelper.filterHTML(out, t6233.F04);%></td>
                                    <td><%=t6233.F05 / 1000%>kb</td>
                                    <td><%StringHelper.filterHTML(out, t6233.F07);%></td>
                                    <td><%StringHelper.filterHTML(out, t6233.name);%></td>
                                    <td><%StringHelper.filterHTML(out, t6233.annexName);%></td>
                                    <td><%=DateTimeParser.format(t6233.F08)%>
                                    </td>
                                    <td><a class="link-blue" href="<%=controller.getURI(request, ViewAnnexWz.class)%>?id=<%=t6233.F01%>"
                                           target="_blank">查看</a></td>
                                </tr>
                                <%
                                    }
                                } else {
                                %>
                                <tr>
                                    <td colspan="8" class="tc">暂无数据</td>
                                </tr>
                                <%} %>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
<%
    String warringMessage = controller.getPrompt(request, response, PromptLevel.WARRING);
    if (!StringHelper.isEmpty(warringMessage)) {
%>
<!--弹出框-->
<div class="popup-box">
    <div class="popup-title-container">
        <h3 class="pl20 f18">提示</h3>
        <a class="icon-i popup-close2" href="javascript:void(0);" onclick="closeInfo()"></a>
    </div>
    <div class="popup-content-container pt20 pb20 clearfix">
        <div class="tc mb20 mt40"><span class="icon-i w30 h30 va-middle radius-wrong-icon"></span><span
                class="f20 h30 va-middle ml10"><%StringHelper.filterHTML(out, warringMessage); %></span></div>
        <div class="tc f16"><a href="javascript:void(0);" onclick="closeInfo();"
                               class="btn-blue2 btn white radius-6 pl20 pr20">确定</a></div>
    </div>
</div>
<div class="popup_bg"></div>
<%}%>
</body>
</html>