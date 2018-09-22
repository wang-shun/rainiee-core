<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.qyxx.KxrzView"%>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.qyxx.ByrzView"%>
<%@page import="com.dimeng.p2p.modules.account.console.service.entity.XyrzTotal"%>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.qyxx.QyList"%>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.qyxx.tzjl.QyTbjlView" %>
<%@page import="com.dimeng.p2p.console.servlets.AbstractConsoleServlet" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.qyxx.ViewQyxx" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.qyxx.ccxx.ListCcxx" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.qyxx.fcxx.ViewListFcxx" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.qyxx.ccxx.ViewListCcxx" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.qyxx.lxxx.ViewLxxx" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.qyxx.cwzk.ViewCwzk" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.qyxx.jscl.ViewJscl" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.qyxx.jkxx.ViewJkxx" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.qyxx.dbjl.ViewListDbjl" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.qyxx.dfjl.ViewListDfjl" %>
<%@page import="com.dimeng.p2p.S61.entities.T6112" %>
<%@page import="com.dimeng.util.ObjectHelper" %>
<%@page import="com.dimeng.p2p.console.servlets.bid.loanmanage.jkgl.LoanList" %>
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
    PagingResult<T6112> result = ObjectHelper.convert(request.getAttribute("result"), PagingResult.class);
    XyrzTotal xyrzTotal = ObjectHelper.convert(request.getAttribute("xyrzTotal"), XyrzTotal.class);
    boolean isHasGuarantor = BooleanParser.parse(configureProvider.getProperty(GuarantorVariavle.IS_HAS_GUARANTOR));
    String escrow = configureProvider.getProperty(SystemVariable.ESCROW_PREFIX);
    String returnUrl = controller.getURI(request, QyList.class);
    String operationJK = request.getParameter("operationJK");
    if("CK".equals(operationJK))
    {
        returnUrl = controller.getURI(request, LoanList.class);
    }
    else if("BLZQYZR".equals(operationJK))
    {
        returnUrl = "/console/finance/zjgl/blzq/blzqYzrList.htm";
    }
    else if("BLZQDZR".equals(operationJK))
    {
        returnUrl = "/console/finance/zjgl/blzq/blzqDzrList.htm";
    }
    else if("BLZQDSH".equals(operationJK))
    {
        returnUrl = "/console/finance/zjgl/blzq/blzqDshList.htm";
    }
    else if("BLZQZRZ".equals(operationJK))
    {
        returnUrl = "/console/finance/zjgl/blzq/blzqZrzList.htm";
    }
    else if("BLZQZRSB".equals(operationJK))
    {
        returnUrl = "/console/finance/zjgl/blzq/blzqZrsbList.htm";
    }
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <div class="p20">
                    <div class="border">
                        <div class="title-container"><i class="icon-i w30 h30 va-middle title-left-icon"></i>企业信息
                            <div class="fr mt5">
                                <input type="button" class="btn btn-blue2 radius-6 pl20 pr20 fr mr10" onclick="location.href='<%=returnUrl %>'" value="返回">
                            </div>
                        </div>

                    </div>
                    <!--切换栏目-->
                    <div class="border mt20">
                        <div class="tabnav-container">
                            <ul class="clearfix">
                                <li>
                                    <a href="<%=controller.getURI(request, ViewQyxx.class)%>?id=<%StringHelper.filterHTML(out, request.getParameter("id"));%>&operationJK=<%=operationJK%>"
                                       class="tab-btn ">企业信息<i class="icon-i tab-arrowtop-icon"></i></a></li>
                                <li>
                                    <a href="<%=controller.getURI(request, ViewJscl.class)%>?id=<%StringHelper.filterHTML(out, request.getParameter("id"));%>&operationJK=<%=operationJK%>"
                                       class="tab-btn ">介绍资料</a></li>
                                <li>
                                    <a href="<%=controller.getURI(request, ViewCwzk.class)%>?id=<%StringHelper.filterHTML(out, request.getParameter("id"));%>&operationJK=<%=operationJK%>"
                                       class="tab-btn ">财务状况</a></li>
                                <li>
                                    <a href="<%=controller.getURI(request, ViewLxxx.class)%>?id=<%StringHelper.filterHTML(out, request.getParameter("id"));%>&operationJK=<%=operationJK%>"
                                       class="tab-btn ">联系信息</a></li>
                                <li>
                                    <a href="<%=controller.getURI(request, ViewListCcxx.class)%>?id=<%StringHelper.filterHTML(out, request.getParameter("id"));%>&operationJK=<%=operationJK%>"
                                       class="tab-btn ">车产信息</a></li>
                                <li><a href="javascript:void(0);" class="tab-btn select-a">房产信息<i
                                        class="icon-i tab-arrowtop-icon"></i></a></li>
                                <%if(isHasGuarantor && !"huifu".equals(escrow)){ %>
                                <li><a class="tab-btn"
                                       href="<%=controller.getURI(request, ViewListDbjl.class)%>?id=<%StringHelper.filterHTML(out, request.getParameter("id"));%>&operationJK=<%=operationJK%>">担保记录</a>
                                </li>
                                <li><a class="tab-btn"
                                       href="<%=controller.getURI(request, ViewListDfjl.class)%>?id=<%StringHelper.filterHTML(out, request.getParameter("id"));%>&operationJK=<%=operationJK%>">垫付记录</a>
                                </li>
                                <%} %>
                                <li>
                                    <a href="<%=controller.getURI(request, ViewJkxx.class)%>?id=<%StringHelper.filterHTML(out, request.getParameter("id"));%>&operationJK=<%=operationJK%>"
                                       class="tab-btn ">借款记录</a></li>
                                <li>
                                    <a class="tab-btn" href="<%=controller.getURI(request, QyTbjlView.class)%>?id=<%=request.getParameter("id") %>&operationJK=<%=operationJK%>">投资记录</a>
                                </li>
                                <li>
                                    <a href="<%=controller.getURI(request, ByrzView.class)%>?id=<%=request.getParameter("id") %>&operationJK=<%=operationJK%>"
                                       class="tab-btn">必要认证（<%=xyrzTotal.byrztg %>/<%=xyrzTotal.needAttestation %>）</a></li>
                                 <li>
	                               <a href="<%=controller.getURI(request, KxrzView.class)%>?id=<%=request.getParameter("id") %>&operationJK=<%=operationJK%>"
	                                  class="tab-btn">可选认证（<%=xyrzTotal.kxrztg %>/<%=xyrzTotal.notNeedAttestation %>）</a></li>
                            </ul>
                        </div>
                        <div class="tab-content-container p20">
                            <form id="form1" action="<%=controller.getURI(request, ViewListCcxx.class)%>" method="post">
                                <input type="hidden" id="id" name="id" value="<%=request.getParameter("id")%>">
                                <input type="hidden" id="operationJK" name="operationJK" value="<%=operationJK%>">

                            <div class="tab-item">

                                <table class="table table-style gray6 tl">
                                    <thead>
                                    <tr class="title tc">
                                        <th>序号</th>
                                        <th>小区名称</th>
                                        <th>建筑面积</th>
                                        <th>购买价格</th>
                                        <th>地址</th>
                                        <th>房产证编号</th>
                                    </tr>
                                    </thead>
                                    <tbody class="f12">
                                    <%
                                        T6112[] t = (result == null ? null : result.getItems());
                                        if (t != null) {
                                            int index = 1;
                                            for (T6112 entity : t) {
                                    %>

                                    <tr class="tc">
                                        <td align="center"><%=index++ %>
                                        </td>
                                        <td align="center"><%StringHelper.filterHTML(out, entity.F03); %></td>
                                        <td align="center"><%=Formater.formatAmount(entity.F04)%>平方米</td>
                                        <td align="center"><%=Formater.formatAmount(entity.F06)%>万元</td>
                                        <td align="center"><%StringHelper.filterHTML(out, entity.F09); %></td>
                                        <td align="center"><%StringHelper.filterHTML(out, entity.F10); %></td>
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
                                <%
                                    AbstractConsoleServlet.rendPagingResult(out, result);
                                %>
                            </div>
                            </form>
                        </div>

                    </div>
                </div>
            </div>
        </div>
    </div>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/validation.js"></script>
</body>
</html>