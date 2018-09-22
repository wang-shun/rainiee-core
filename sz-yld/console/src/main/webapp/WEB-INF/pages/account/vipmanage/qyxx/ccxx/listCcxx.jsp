<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.qyxx.QyList"%>
<%@page import="com.dimeng.p2p.console.servlets.AbstractConsoleServlet" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.qyxx.UpdateQyxx" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.qyxx.cwzk.UpdateCwzk" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.qyxx.fcxx.ListFcxx" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.qyxx.ccxx.UpdateCcxx" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.qyxx.ccxx.AddCcxx" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.qyxx.lxxx.UpdateLxxx" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.qyxx.ccxx.ListCcxx" %>
<%@page import="com.dimeng.p2p.S61.entities.T6113" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.qyxx.jscl.UpdateJscl" %>
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
    CURRENT_SUB_CATEGORY = "QY";
    PagingResult<T6113> result = ObjectHelper.convert(request.getAttribute("result"), PagingResult.class);
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <div class="p20">
                    <div class="border">
                        <div class="title-container"><i class="icon-i w30 h30 va-middle title-left-icon"></i>修改企业信息
                        </div>

                    </div>
                    <!--切换栏目-->
                    <div class="border mt20">
                        <div class="tabnav-container">
                            <ul class="clearfix">
                                <li>
                                    <a href="<%=controller.getURI(request, UpdateQyxx.class)%>?id=<%StringHelper.filterHTML(out, request.getParameter("id"));%>"
                                       class="tab-btn ">企业信息<i class="icon-i tab-arrowtop-icon"></i></a></li>
                                <li>
                                    <a href="<%=controller.getURI(request, UpdateJscl.class)%>?id=<%StringHelper.filterHTML(out, request.getParameter("id"));%>"
                                       class="tab-btn ">介绍资料</a></li>
                                <li>
                                    <a href="<%=controller.getURI(request, UpdateCwzk.class)%>?id=<%StringHelper.filterHTML(out, request.getParameter("id"));%>"
                                       class="tab-btn ">财务状况</a></li>
                                <li>
                                    <a href="<%=controller.getURI(request, UpdateLxxx.class)%>?id=<%StringHelper.filterHTML(out, request.getParameter("id"));%>"
                                       class="tab-btn ">联系信息</a></li>
                                <li><a href="javascript:void(0);" class="tab-btn select-a">车产信息<i
                                        class="icon-i tab-arrowtop-icon"></i></a></li>
                                <li>
                                    <a href="<%=controller.getURI(request, ListFcxx.class)%>?id=<%StringHelper.filterHTML(out, request.getParameter("id"));%>"
                                       class="tab-btn ">房产信息</a></li>
                            </ul>
                        </div>
                        <div class="tab-content-container p20">
                            <form id="form1" action="<%=controller.getURI(request, UpdateLxxx.class)%>" method="post">
                                <input type="hidden" id="id" name="id" value="<%=request.getParameter("id")%>">
                                <input type="hidden" id="entryType" name="entryType" value="">
                                <div class="tab-item">
                                    <a href="<%=controller.getURI(request, AddCcxx.class)%>?id=<%=request.getParameter("id")%>"
                                       class="btn btn-blue radius-6 mr5 pl1 pr15 mb10"><i
                                            class="icon-i w30 h30 va-middle add-icon "></i>新增</a>
                                    <table class="table table-style gray6 tl">
                                        <thead>
                                        <tr class="title tc">
                                            <th class="tc">序号</th>
                                            <th>汽车品牌</th>
                                            <th>车牌号码</th>
                                            <th>购车年份</th>
                                            <th>购买价格</th>
                                            <th>评估价格</th>
                                            <th class="tc">操作</th>
                                        </tr>
                                        </thead>
                                        <tbody class="f12">
                                        <%
                                            T6113[] t = (result == null ? null : result.getItems());
                                            if (t != null) {
                                                int index = 1;
                                                for (T6113 entity : t) {
                                        %>
                                        <tr class="tc">
                                            <td class="tc"><%=index++ %>
                                            </td>
                                            <td align="center"><%StringHelper.filterHTML(out, entity.F03); %></td>
                                            <td align="center"><%StringHelper.filterHTML(out, entity.F04); %></td>
                                            <td align="center"><%=entity.F05 %>年</td>
                                            <td align="center"><%=Formater.formatAmount(entity.F06) %>万元</td>
                                            <td align="center"><%=Formater.formatAmount(entity.F07) %>万元</td>
                                            <td align="center" class="blue tc"><a
                                                    href="<%=controller.getURI(request, UpdateCcxx.class)%>?fid=<%=entity.F01%>&id=<%StringHelper.filterHTML(out, request.getParameter("id"));%>"
                                                    class="link-blue">修改</a></td>
                                        </tr>
                                        <%
                                                }
                                            }
                                        %>

                                        </tbody>
                                    </table>
                                </div>
                                <%
                                    AbstractConsoleServlet.rendPagingResult(out, result);
                                %>
                                
                                <div class="tc  pt20">
                                	<input type="button"
                                                   onclick="window.location.href='<%=controller.getURI(request,UpdateLxxx.class)%>?id=<%=request.getParameter("id")%>'"
                                                   class="btn btn-blue2 radius-6 pl20 pr20 " value="上一步"/>
                                    <input type="button"
                                                   onclick="window.location.href='<%=controller.getURI(request,ListFcxx.class)%>?id=<%=request.getParameter("id")%>'"
                                                   class="btn btn-blue2 radius-6 pl20 pr20 ml10" value="下一步"/>
                                    <input type="button" onclick="tj();" class="btn btn-blue2 radius-6 pl20 pr20 ml10" value="提交"/>
                                    <input type="button"
                                           onclick="window.location.href='<%=controller.getURI(request, QyList.class)%>'"
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