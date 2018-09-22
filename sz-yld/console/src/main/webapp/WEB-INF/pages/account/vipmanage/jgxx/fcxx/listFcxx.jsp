<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.jgxx.JgList"%>
<%@page import="com.dimeng.p2p.console.servlets.AbstractConsoleServlet" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.jgxx.dbzz.UpdateDbzz" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.jgxx.UpdateJgxx" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.jgxx.cwzk.UpdateCwzk" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.jgxx.fcxx.UpdateFcxx" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.jgxx.fcxx.ViewFcxx" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.jgxx.fcxx.AddFcxx" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.jgxx.ccxx.ListCcxx" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.jgxx.lxxx.UpdateLxxx" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.jgxx.jscl.UpdateJscl" %>
<%@page import="com.dimeng.p2p.S61.entities.T6112" %>
<%@page import="com.dimeng.util.ObjectHelper" %>
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
    PagingResult<T6112> result = ObjectHelper.convert(request.getAttribute("result"), PagingResult.class);
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <div class="p20">
                    <div class="border">
                        <div class="title-container"><i class="icon-i w30 h30 va-middle title-left-icon"></i>修改机构信息
                        </div>

                    </div>
                    <!--切换栏目-->
                    <div class="border mt20">
                        <div class="tabnav-container">
                            <ul class="clearfix">
                                <li>
                                    <a href="<%=controller.getURI(request, UpdateJgxx.class)%>?id=<%StringHelper.filterHTML(out, request.getParameter("id"));%>"
                                       class="tab-btn ">机构信息<i class="icon-i tab-arrowtop-icon"></i></a></li>
                                <li>
                                    <a href="<%=controller.getURI(request, UpdateDbzz.class)%>?id=<%StringHelper.filterHTML(out, request.getParameter("id"));%>"
                                       class="tab-btn ">担保情况</a></li>
                                <li>
                                    <a href="<%=controller.getURI(request, UpdateJscl.class)%>?id=<%StringHelper.filterHTML(out, request.getParameter("id"));%>"
                                       class="tab-btn ">介绍资料</a></li>
                                <li>
                                    <a href="<%=controller.getURI(request, UpdateCwzk.class)%>?id=<%StringHelper.filterHTML(out, request.getParameter("id"));%>"
                                       class="tab-btn ">财务状况</a></li>
                                <li>
                                    <a href="<%=controller.getURI(request, UpdateLxxx.class)%>?id=<%StringHelper.filterHTML(out, request.getParameter("id"));%>"
                                       class="tab-btn ">联系信息</a></li>
                                <li>
                                    <a href="<%=controller.getURI(request, ListCcxx.class)%>?id=<%StringHelper.filterHTML(out, request.getParameter("id"));%>"
                                       class="tab-btn ">车产信息</a></li>
                                <li><a href="javascript:void(0);" class="tab-btn select-a">房产信息<i
                                        class="icon-i tab-arrowtop-icon"></i></a></li>
                            </ul>
                        </div>
                        <div class="tab-content-container p20">
                            <form id="form1" action="<%=controller.getURI(request, UpdateLxxx.class)%>" method="post">
                                <input type="hidden" id="id" name="id" value="<%=request.getParameter("id")%>">
                                <input type="hidden" id="entryType" name="entryType" value="">
                                <div class="tab-item">
                                    <a href="<%=controller.getURI(request, AddFcxx.class)%>?id=<%=request.getParameter("id")%>"
                                       class="btn btn-blue radius-6 mr5 pl1 pr15 mb10"><i
                                            class="icon-i w30 h30 va-middle add-icon "></i>新增</a>
                                    <table class="table table-style gray6 tl">
                                        <thead>
                                        <tr class="title tc">
                                            <th class="tc">序号</th>
                                            <th>小区名称</th>
                                            <th>建筑面积</th>
                                            <th>购买价格</th>
                                            <th>地址</th>
                                            <th>房产证编号</th>
                                            <th class="tc">操作</th>
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
                                            <td class="tc"><%=index++ %>
                                            </td>
                                            <td align="center"><%StringHelper.filterHTML(out, entity.F03); %></td>
                                            <td align="center"><%=Formater.formatAmount(entity.F04) %>平方米</td>
                                            <td align="center"><%=Formater.formatAmount(entity.F06) %>万元</td>
                                            <td align="center"><%StringHelper.filterHTML(out, entity.F09); %></td>
                                            <td align="center"><%StringHelper.filterHTML(out, entity.F10); %></td>
                                            <td align="center" class="blue tc">
                                                <a href="<%=controller.getURI(request,ViewFcxx.class)%>?fid=<%=entity.F01%>&id=<%StringHelper.filterHTML(out, request.getParameter("id"));%>"
                                                   class="link-blue mr20">查看</a>
                                                <a href="<%=controller.getURI(request, UpdateFcxx.class)%>?fid=<%=entity.F01%>&id=<%StringHelper.filterHTML(out, request.getParameter("id"));%>"
                                                   class="link-blue mr20">修改</a>
                                            </td>
                                        </tr>
                                        <%
                                            }
                                        } else {
                                        %>
                                        <tr>
                                            <td colspan="7" class="tc">暂无数据</td>
                                        </tr>
                                        <%} %>

                                        </tbody>
                                    </table>
                                    <%
                                        AbstractConsoleServlet.rendPagingResult(out, result);
                                    %>
                                </div>
                                <div class="tc  pt20">
                                	<input type="button"
                                                   onclick="window.location.href='<%=controller.getURI(request,ListCcxx.class)%>?id=<%=request.getParameter("id")%>'"
                                                   class="btn btn-blue2 radius-6 pl20 pr20 " value="上一步"/>
                                    <input type="button" onclick="tj();" class="btn btn-blue2 radius-6 pl20 pr20 ml10" value="提交"/>
                                    <input type="button"
                                           onclick="window.location.href='<%=controller.getURI(request, JgList.class)%>'"
                                           class="btn btn-blue2 radius-6 pl20 pr20 ml10" value="取消"/>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/validation.js"></script>
<script type="text/javascript">
    function tj() {
        $('#entryType').val('submit');
    	$('#form1').submit();
    }
</script>
</body>
</html>