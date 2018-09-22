<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.jgxx.JgList"%>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.jgxx.tzjl.JgTbjlView" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.jgxx.ViewJgxx" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.jgxx.cwzk.ViewCwzk" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.jgxx.fcxx.ViewListFcxx" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.jgxx.ccxx.ViewListCcxx" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.jgxx.lxxx.ViewLxxx" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.jgxx.jscl.ViewJscl" %>
<%@page import="com.dimeng.util.ObjectHelper" %>
<%@page import="com.dimeng.p2p.S61.entities.T6163" %>
<%@page import="com.dimeng.p2p.console.servlets.bid.loanmanage.jkgl.LoanList" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.jgxx.dbjl.ViewListDbjl" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.jgxx.dfjl.ViewListDfjl" %>
<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <%@include file="/WEB-INF/include/script.jsp" %>
</head>
<body>
<%
    CURRENT_CATEGORY = "YHGL";
    CURRENT_SUB_CATEGORY = "JG";
    T6163[] entitys = ObjectHelper.convertArray(request.getAttribute("info"), T6163.class);
    //托管前缀
    String ESCROW_PREFIX = configureProvider.getProperty(SystemVariable.ESCROW_PREFIX);
    String ckUrl = controller.getURI(request, JgList.class);
    String operationJK = request.getParameter("operationJK");
    if("CK".equals(operationJK))
    {
        ckUrl = controller.getURI(request, LoanList.class);
    }
    else if("BLZQYZR".equals(operationJK))
    {
        ckUrl = "/console/finance/zjgl/blzq/blzqYzrList.htm";
    }
    else if("BLZQDZR".equals(operationJK))
    {
        ckUrl = "/console/finance/zjgl/blzq/blzqDzrList.htm";
    }
    else if("BLZQDSH".equals(operationJK))
    {
        ckUrl = "/console/finance/zjgl/blzq/blzqDshList.htm";
    }
    else if("BLZQZRZ".equals(operationJK))
    {
        ckUrl = "/console/finance/zjgl/blzq/blzqZrzList.htm";
    }
    else if("BLZQZRSB".equals(operationJK))
    {
        ckUrl = "/console/finance/zjgl/blzq/blzqZrsbList.htm";
    }
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <div class="p20">
                    <div class="border">
                        <div class="title-container"><i class="icon-i w30 h30 va-middle title-left-icon"></i>机构信息
                            <div class="fr mt5">
                                <input type="button" class="btn btn-blue2 radius-6 pl20 pr20 fr mr10" onclick="location.href='<%=ckUrl %>'" value="返回">
                            </div>

                        </div>

                    </div>

                    <!--切换栏目-->
                    <div class="border mt20">
                        <div class="tabnav-container">
                            <ul class="clearfix">
                                <li>
                                    <a href="<%=controller.getURI(request, ViewJgxx.class)%>?id=<%StringHelper.filterHTML(out, request.getParameter("id"));%>&operationJK=<%=operationJK%>"
                                       class="tab-btn ">机构信息<i class="icon-i tab-arrowtop-icon"></i></a></li>
                                <li>
                                    <a href="<%=controller.getURI(request, ViewJscl.class)%>?id=<%StringHelper.filterHTML(out, request.getParameter("id"));%>&operationJK=<%=operationJK%>"
                                       class="tab-btn ">介绍资料</a></li>
                                <li><a href="javascript:void(0);" class="tab-btn select-a">财务状况<i
                                        class="icon-i tab-arrowtop-icon"></i></a></li>
                                <li>
                                    <a href="<%=controller.getURI(request, ViewLxxx.class)%>?id=<%StringHelper.filterHTML(out, request.getParameter("id"));%>&operationJK=<%=operationJK%>"
                                       class="tab-btn ">联系信息</a></li>
                                <li>
                                    <a href="<%=controller.getURI(request, ViewListCcxx.class)%>?id=<%StringHelper.filterHTML(out, request.getParameter("id"));%>&operationJK=<%=operationJK%>"
                                       class="tab-btn ">车产信息</a></li>
                                <li>
                                    <a href="<%=controller.getURI(request, ViewListFcxx.class)%>?id=<%StringHelper.filterHTML(out, request.getParameter("id"));%>&operationJK=<%=operationJK%>"
                                       class="tab-btn ">房产信息</a></li>
                                <li><a class="tab-btn"
                                       href="<%=controller.getURI(request, ViewListDbjl.class)%>?id=<%StringHelper.filterHTML(out, request.getParameter("id"));%>&operationJK=<%=operationJK%>">担保记录</a>
                                </li>
                                <li><a class="tab-btn"
                                       href="<%=controller.getURI(request, ViewListDfjl.class)%>?id=<%StringHelper.filterHTML(out, request.getParameter("id"));%>&operationJK=<%=operationJK%>">垫付记录</a>
                                </li>
                                <%
                                	boolean isHasBadClaim = BooleanParser.parse(configureProvider.getProperty(BadClaimVariavle.IS_BADCLAIM_TRANSFER));
	                            	if (isHasBadClaim){
                                   %>
                                <li>
                                    <a class="tab-btn" href="/console/account/vipmanage/jgxx/blzqgmjl/blzqGmjlView.htm?id=<%=request.getParameter("id") %>&operationJK=<%=operationJK%>">不良债权购买记录</a>
                                </li>
                                <%} %>
                                <%if(!"huifu".equalsIgnoreCase(ESCROW_PREFIX)){ %>
                                <li>
                                    <a class="tab-btn" href="<%=controller.getURI(request, JgTbjlView.class)%>?id=<%=request.getParameter("id") %>&operationJK=<%=operationJK%>">投资记录</a>
                                </li>
                                <%} %>
                            </ul>
                        </div>
                        <div class="tab-content-container p20">
                            <div class="tab-item">
                                <form action="<%=controller.getURI(request, ViewCwzk.class)%>" method="post"
                                      class="form1">
                                    <input type="hidden" id="id" name="id" value="<%=request.getParameter("id")%>">
                                    <div class="table-container">
                                    <table class="table table-style gray6 tl">
                                        <thead>
                                        <tr class="title tc">
                                            <th>年份</th>
                                            <th>主营收入（万元）</th>
                                            <th>净利润（万元）</th>
                                            <th>总资产（万元）</th>
                                            <th>净资产（万元）</th>
                                            <th>备注</th>
                                        </tr>
                                        </thead>
                                        <tbody class="f12">
                                        <%
                                            int i = 0;
                                            for (T6163 entity : entitys) {
                                        %>
                                        <tr class="tc">
                                            <td>
                                                <input type="hidden" value="<%=entity.F01 %>"
                                                       name="t6163s[<%=i%>].F01"/>
                                                <input type="hidden" value="<%=entity.F02 %>"
                                                       name="t6163s[<%=i%>].F02"/>
                                                <%=entity.F02 %>年
                                            </td>
                                            <td><%=entity.F03 %>
                                            </td>
                                            <td><%=entity.F05 %>
                                            </td>
                                            <td><%=entity.F06 %>
                                            </td>
                                            <td><%=entity.F07 %>
                                            </td>
                                            <td title="<%StringHelper.filterHTML(out, entity.F08); %>"><div class="t-ellipsis ma400"><%StringHelper.filterHTML(out, entity.F08); %></div>
                                            </td>
                                        </tr>
                                        <%
                                                i++;
                                            }
                                        %>

                                        </tbody>
                                    </table>
                                        </div>
                                </form>
                            </div>
                        </div>

                    </div>
                </div>
            </div>
        </div>
    </div>
</body>
</html>