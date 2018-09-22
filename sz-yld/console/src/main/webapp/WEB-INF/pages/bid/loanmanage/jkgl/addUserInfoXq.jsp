<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.console.servlets.bid.loanmanage.jkgl.LoanList"%>
<%@page import="com.dimeng.p2p.S62.entities.T6230" %>
<%@page import="com.dimeng.p2p.S62.enums.T6230_F11" %>
<%@page import="com.dimeng.p2p.S62.enums.T6230_F13" %>
<%@page import="com.dimeng.p2p.console.servlets.bid.loanmanage.jkgl.UpdateProject" %>
<%@page import="com.dimeng.p2p.console.servlets.bid.loanmanage.jkgl.ViewAuthentication" %>
<%@page import="com.dimeng.p2p.console.servlets.bid.loanmanage.jkgl.AddDyw" %>
<%@page import="com.dimeng.p2p.console.servlets.bid.loanmanage.jkgl.AddGuaranteeXq" %>
<%@page import="com.dimeng.p2p.console.servlets.bid.loanmanage.jkgl.AddAnnexMsk" %>
<%@page import="com.dimeng.p2p.console.servlets.bid.loanmanage.jkgl.AddAnnexWz" %>
<%@page import="com.dimeng.p2p.modules.account.console.service.entity.Grxx" %>
<%@page import="com.dimeng.util.ObjectHelper" %>
<%@ page import="com.dimeng.p2p.variables.defines.pays.PayVariavle" %>
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
    int loanId = IntegerParser.parse(request.getParameter("loanId"));
    int userId = IntegerParser.parse(request.getParameter("userId"));
    T6230 t6230 = ObjectHelper.convert(request.getAttribute("t6230"), T6230.class);
    Grxx user = ObjectHelper.convert(request.getAttribute("user"), Grxx.class);
    if (t6230 == null) {
        response.sendError(HttpServletResponse.SC_NOT_FOUND);
    }
    String mYxrz = configureProvider.getProperty(PayVariavle.CHARGE_MUST_EMAIL);
%>
    <div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <div class="p20">
                    <div class="border">
                        <div class="title-container"><i class="icon-i w30 h30 va-middle title-left-icon"></i>新增借款信息
                        </div>
                    </div>
                    <div class="border mt20">
                        <div class="tabnav-container">
                            <ul class="clearfix">
                                <%
                                    if (loanId > 0) {
                                %>
                                <li>
                                    <a href="<%=controller.getURI(request, UpdateProject.class)%>?loanId=<%=loanId%>&userId=<%=userId%>"
                                       class="tab-btn">项目信息<i class="icon-i tab-arrowtop-icon"></i></a></li>
                                <%} %>

                                <li><a href="javascript:void(0);" class="tab-btn select-a">个人信息<i
                                        class="icon-i tab-arrowtop-icon"></i></a></li>
                                <li>
                                    <a href="<%=controller.getURI(request, ViewAuthentication.class)%>?loanId=<%=loanId%>&userId=<%=userId%>"
                                       class="tab-btn ">个人认证信息</a></li>

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
                        <div class="content-container pl40 pt40 pr40 pb20">
                            <ul class="gray6">
                                <li class="mb10"><span class="display-ib w200 tr mr5"><em
                                        class="red pr5">*</em>真实姓名：</span>
                                    <%StringHelper.filterHTML(out, user.name);%>
                                </li>
                                <li class="mb10"><span class="display-ib w200 tr mr5"><em
                                        class="red pr5">*</em>身份证号：</span>
                                    <%StringHelper.filterHTML(out, StringHelper.decode(user.sfzh));%>
                                </li>
                                <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>手        机：</span>
                                    <%StringHelper.filterHTML(out, user.phone);%>
                                </li>
                                <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5"><%if(BooleanParser.parse(mYxrz)){%>*<%}%></em>邮        箱：</span>
                                    <%StringHelper.filterHTML(out, user.email);%>
                                </li>
                                <li>
                                    <div class="pl200 ml5">
                                    	<input type="button" class="btn btn-blue2 radius-6 pl20 pr20 ml10"
                                                   onclick="window.location.href='<%=controller.getURI(request, UpdateProject.class) %>?loanId=<%=loanId%>&userId=<%=userId%>'"
                                                   value="上一步">
                                        <input type="button" class="btn btn-blue2 radius-6 pl20 pr20 ml10"
                                                   onclick="window.location.href='<%=controller.getURI(request, ViewAuthentication.class) %>?loanId=<%=loanId%>&userId=<%=userId%>'"
                                                   value="下一步">
                                        <input type="button" class="btn btn-blue2 radius-6 pl20 pr20 ml10"
                                               onclick="location.href='<%=controller.getURI(request, LoanList.class) %>'"
                                               value="取消"></div>
                                </li>
                            </ul>
                        </div>
                    </div>


                </div>
            </div>
        </div>
    </div>

</body>
</html>