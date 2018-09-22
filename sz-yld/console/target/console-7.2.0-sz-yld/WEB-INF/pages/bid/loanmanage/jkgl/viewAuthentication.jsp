<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.console.servlets.bid.loanmanage.jkgl.ViewProgresBidInfo"%>
<%@page import="com.dimeng.p2p.console.servlets.bid.loanmanage.jkgl.AddEnterpriseXm"%>
<%@page import="com.dimeng.p2p.S61.enums.T6120_F05" %>
<%@page import="com.dimeng.p2p.modules.account.console.service.entity.Rzxx" %>
<%@page import="com.dimeng.p2p.console.servlets.bid.loanmanage.jkgl.LoanList" %>
<%@page import="com.dimeng.p2p.console.servlets.bid.loanmanage.jkgl.DetailAuthent" %>
<%@page import="com.dimeng.p2p.console.servlets.bid.loanmanage.jkgl.AddAnnexWz" %>
<%@page import="com.dimeng.p2p.console.servlets.bid.loanmanage.jkgl.AddAnnexMsk" %>
<%@page import="com.dimeng.p2p.console.servlets.bid.loanmanage.jkgl.AddGuaranteeXq" %>
<%@page import="com.dimeng.p2p.S62.enums.T6230_F11" %>
<%@page import="com.dimeng.p2p.S62.enums.T6230_F13" %>
<%@page import="com.dimeng.p2p.console.servlets.bid.loanmanage.jkgl.AddDyw" %>
<%@page import="com.dimeng.p2p.console.servlets.bid.loanmanage.jkgl.AddUserInfoXq" %>
<%@page import="com.dimeng.p2p.console.servlets.bid.loanmanage.jkgl.UpdateProject" %>
<%@page import="com.dimeng.p2p.S62.entities.T6230" %>
<%@page import="com.dimeng.p2p.S61.enums.T6110_F06" %>
<%@page import="com.dimeng.util.ObjectHelper" %>
<%@page import="com.dimeng.p2p.console.servlets.AbstractConsoleServlet" %>
<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <%@include file="/WEB-INF/include/script.jsp" %>
</head>
<body>
<%
    CURRENT_CATEGORY = "YWGL";
    CURRENT_SUB_CATEGORY = "BDGL";
    PagingResult<Rzxx> list = ObjectHelper.convert(request.getAttribute("authentications"), PagingResult.class);
    Rzxx[] authentications = (list == null ? null : list.getItems());
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
                        <div class="title-container"><i class="icon-i w30 h30 va-middle title-left-icon"></i>查看认证信息
                        </div>
                    </div>
                    <div class="border mt20">
                        <div class="tabnav-container">
                            <ul class="clearfix">

                                <li>
                                    <a href="<%=controller.getURI(request, UpdateProject.class)%>?loanId=<%=loanId%>&userId=<%=userId%>"
                                       class="tab-btn">项目信息<i class="icon-i tab-arrowtop-icon"></i></a></li>
                                <%
                                    if (userType == T6110_F06.ZRR) {
                                %>
                                <li>
                                    <a href="<%=controller.getURI(request, AddUserInfoXq.class)%>?loanId=<%=loanId%>&userId=<%=userId%>"
                                       class="tab-btn">个人信息</a></li>
                                <%} %>
                                <li><a href="javascript:void(0)" class="tab-btn select-a">个人认证信息</a></li>
                                <%
                                    if (userType == T6110_F06.FZRR) {
                                %>
                                <li>
                                    <a href="<%=controller.getURI(request, AddEnterpriseXm.class)%>?loanId=<%=loanId%>&userId=<%=userId%>"
                                       class="tab-btn">企业信息</a></li>
                                <%
                                    }
                                %>
                                <%
                                    if (T6230_F13.S == t6230.F13) {
                                %>
                                <li>
                                    <a href="<%=controller.getURI(request, AddDyw.class)%>?loanId=<%=loanId%>&userId=<%=userId%>"
                                       class="tab-btn ">抵押物信息</a></li>
                                <%} %>
                                <%
                                    if (T6230_F11.S == t6230.F11) {
                                %>
                                <li>
                                    <a href="<%=controller.getURI(request, AddGuaranteeXq.class)%>?loanId=<%=loanId%>&userId=<%=userId%>"
                                       class="tab-btn ">担保信息</a></li>
                                <%} %>
                                <li>
                                    <a href="<%=controller.getURI(request, AddAnnexMsk.class)%>?loanId=<%=loanId%>&userId=<%=userId%>"
                                       class="tab-btn ">附件(马赛克)</a></li>
                                <li>
                                    <a href="<%=controller.getURI(request, AddAnnexWz.class)%>?loanId=<%=loanId%>&userId=<%=userId%>"
                                       class="tab-btn ">附件(完整版)</a></li>
                            </ul>
                        </div>
                        <form id="form1"
                              action="<%=controller.getURI(request, com.dimeng.p2p.console.servlets.bid.loanmanage.jkgl.ViewAuthentication.class)%>?loanId=<%=loanId%>&userId=<%=userId%>"
                              method="post">
                            <div class="content-container pl40 pt20 pr40 pb20">

                                <div class="tab-item">

                                    <table class="table table-style gray6 tl">
                                        <thead>
                                        <tr class="title tc">
                                            <th>序号</th>
                                            <th>认证项目</th>
                                            <th>状态</th>
                                            <th>认证时间</th>
                                            <th>操作</th>
                                        </tr>
                                        </thead>
                                        <tbody class="f12">
                                        <%
                                            if (authentications != null) {
                                                int i = 1;
                                                for (Rzxx entity : authentications) {
                                                    if (entity == null) {
                                                        continue;
                                                    }
                                        %>
                                        <tr class="tc">
                                            <td><%=i++%>
                                            </td>
                                            <td>
                                                <%
                                                    StringHelper.filterHTML(out, entity.lxmc);
                                                %>
                                            </td>
                                            <td><%=entity.status.getChineseName()%>
                                            </td>
                                            <td><%=DateTimeParser.format(entity.time)%>
                                            </td>
                                            <%if (entity.status != T6120_F05.WYZ) { %>
                                            <td>
                                                <a href="<%=controller.getURI(request, DetailAuthent.class)%>?id=<%=entity.yxjlID%>"
                                                   target="_blank" class="link-blue">查看</a></td>
                                            <%} else { %>
                                            <td></td>
                                            <%} %>
                                        </tr>
                                        <%
                                            }
                                        } else {
                                        %>
                                        <tr class="tc">
                                            <td colspan="5">暂无数据</td>
                                        </tr>
                                        <%} %>

                                        </tbody>
                                    </table>

                                </div>
                                <%
                                    AbstractConsoleServlet.rendPagingResult(out, list);
                                %>
                            </div>
                            <div class="tc w220 mb10">
                            	<%
                                                   String prevUrl = "";
                                                   if (userType == T6110_F06.FZRR) {
                                                       prevUrl = controller.getURI(request, AddEnterpriseXm.class);
                                                   }else{
                                                       prevUrl = controller.getURI(request, AddUserInfoXq.class);
                                                   }
                                                   
                                                   %>
                                <input type="button" class="btn btn-blue2 radius-6 pl20 pr20 ml10"
                                                   onclick="window.location.href='<%=prevUrl %>?loanId=<%=loanId%>&userId=<%=userId%>'"
                                                   value="上一步">
                                                   <%
                                                   String nextUrl = "";
                                                   if (T6230_F13.S == t6230.F13) {
                                                       nextUrl = controller.getURI(request, AddDyw.class);
                                                   }else if(T6230_F11.S == t6230.F11){
                                                       nextUrl = controller.getURI(request, AddGuaranteeXq.class);
                                                   }else{
                                                       nextUrl = controller.getURI(request, AddAnnexMsk.class);
                                                   }
                                                   
                                                   %>
                                        <input type="button" class="btn btn-blue2 radius-6 pl20 pr20 ml10"
                                                   onclick="window.location.href='<%=nextUrl %>?loanId=<%=loanId%>&userId=<%=userId%>'"
                                                   value="下一步">
                                        <input type="button" class="btn btn-blue2 radius-6 pl20 pr20 ml10"
                                               onclick="location.href='<%=controller.getURI(request, LoanList.class) %>'"
                                               value="取消"></div>
                            </div>
                        </form>
                    </div>


                </div>
            </div>
        </div>

</body>
</html>