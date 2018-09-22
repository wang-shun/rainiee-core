<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.jgxx.JgList"%>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.jgxx.tzjl.JgTbjlView" %>
<%@page import="com.dimeng.p2p.console.servlets.AbstractConsoleServlet" %>
<%@page import="com.dimeng.util.ObjectHelper" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.jgxx.ViewJgxx" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.jgxx.ccxx.ListCcxx" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.jgxx.fcxx.ViewListFcxx" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.jgxx.lxxx.ViewLxxx" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.jgxx.cwzk.ViewCwzk" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.jgxx.jscl.ViewJscl" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.jgxx.ccxx.ViewListCcxx" %>
<%@page import="com.dimeng.p2p.modules.account.console.service.entity.Dfxxmx" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.jgxx.dbjl.ViewListDbjl" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.jgxx.dfjl.ViewListDfjl" %>
<%@page import="com.dimeng.p2p.S62.enums.T6230_F20" %>
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
    CURRENT_SUB_CATEGORY = "DBJL";
    PagingResult<Dfxxmx> result = ObjectHelper.convert(request.getAttribute("result"), PagingResult.class);
    boolean isHasBadClaim = BooleanParser.parse(configureProvider.getProperty(BadClaimVariavle.IS_BADCLAIM_TRANSFER));
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
                      <form action="<%=controller.getURI(request, ViewListDfjl.class) %>" method="post" id="form1">
                        <input type="hidden" name="id" value="<%StringHelper.filterHTML(out, request.getParameter("id"));%>"/>
                        <input type="hidden" id="operationJK" name="operationJK" value="<%=operationJK%>">
                        <div class="tabnav-container">
                            <ul class="clearfix">
                                <li><a class="tab-btn"
                                       href="<%=controller.getURI(request, ViewJgxx.class)%>?id=<%StringHelper.filterHTML(out, request.getParameter("id"));%>&operationJK=<%=operationJK%>">机构信息</a>
                                </li>
                                <li><a class="tab-btn"
                                       href="<%=controller.getURI(request, ViewJscl.class)%>?id=<%StringHelper.filterHTML(out, request.getParameter("id"));%>&operationJK=<%=operationJK%>">介绍资料</a>
                                </li>
                                <li><a class="tab-btn"
                                       href="<%=controller.getURI(request, ViewCwzk.class)%>?id=<%StringHelper.filterHTML(out, request.getParameter("id"));%>&operationJK=<%=operationJK%>">财务状况</a>
                                </li>
                                <li><a class="tab-btn"
                                       href="<%=controller.getURI(request, ViewLxxx.class)%>?id=<%StringHelper.filterHTML(out, request.getParameter("id"));%>&operationJK=<%=operationJK%>">联系信息</a>
                                </li>
                                <li><a href="<%=controller.getURI(request, ViewListCcxx.class)%>?id=<%StringHelper.filterHTML(out, request.getParameter("id"));%>&operationJK=<%=operationJK%>" class="tab-btn">车产信息</a></li>
                                <li><a class="tab-btn"
                                       href="<%=controller.getURI(request, ViewListFcxx.class)%>?id=<%StringHelper.filterHTML(out, request.getParameter("id"));%>&operationJK=<%=operationJK%>">房产信息</a>
                                </li>
                                <li><a class="tab-btn"
                                       href="<%=controller.getURI(request, ViewListDbjl.class)%>?id=<%StringHelper.filterHTML(out, request.getParameter("id"));%>&operationJK=<%=operationJK%>">担保记录</a>
                                </li>
                                <li><a class="tab-btn select-a"
                                       href="javascript:void(0);">垫付记录<i
                                        class="icon-i tab-arrowtop-icon"></i></a>
                                </li>
                                <%
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
                        <div class="content-container pl40 pt30 pr40">
                                <ul class="gray6 input-list-container clearfix">
                                    <li><span class="display-ib mr5">借款编号：</span>
                                        <input type="text" name="jkbh"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("jkbh"));%>"
                                               class="text border pl5 mr20"/>
                                    </li>
                                    <li><span class="display-ib mr5">借款标题：</span>
                                        <input type="text" name="jkbt"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("jkbt"));%>"
                                               class="text border pl5 mr20"/>
                                    </li>
                                    <li><a href="javascript:$('#form1').submit();" class="btn btn-blue radius-6 mr5 pl1 pr15"><i
                                            class="icon-i w30 h30 va-middle search-icon "></i>搜索</a></li>
                                </ul>
                        </div>
                        <div class="tab-content-container p20">
                            <div class="tab-item">
                                <table class="table table-style gray6 tl">
                                    <thead>  	 	 	 	       	                     	
                                    <tr class="title tc">
                                        <th>序号</th>
                                        <th>借款编号</th>
                                        <th>用户名</th>
                                        <th>借款标题</th>
                                        <th>垫付金额(元)</th>
                                        <th>垫付返回金额(元)</th>
                                        <th>逾期期数（期）</th>
                                        <th>状态</th>
                                    </tr>
                                    </thead>
                                    <tbody class="f12">
                                    <%
                                    Dfxxmx[] t = result.getItems();
                                        if (t != null) {
                                            int index = 1;
                                            for (Dfxxmx entity : t) {
                                    %>

                                    <tr class="tc">
                                        <td><%=index++ %></td>
                                        <td><%StringHelper.filterHTML(out, entity.jkbh); %></td>
                                        <td><%StringHelper.filterHTML(out, entity.yhm); %></td>
                                        <td><%StringHelper.filterHTML(out, entity.jkbt);%></td>
                                        <td><%=Formater.formatAmount(entity.dfje)%></td>
                                        <td><%=Formater.formatAmount(entity.dffhje)%></td>
                                        <td><%=entity.yqqs %></td>
                                        <td><%StringHelper.filterHTML(out, entity.zt); %></td>
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
                                <%
                                    AbstractConsoleServlet.rendPagingResult(out, result);
                                %>
                            </div>
                        </div>
                      </form>
                    </div>
                </div>
            </div>
        </div>
</div>
</body>
</html>