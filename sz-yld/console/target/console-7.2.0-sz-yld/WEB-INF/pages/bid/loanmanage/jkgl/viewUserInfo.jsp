<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.console.servlets.finance.zjgl.dfgl.YqddfList"%>
<%@page import="com.dimeng.p2p.console.servlets.bid.loanmanage.jkgl.ViewProgresBidInfo"%>
<%@page import="com.dimeng.p2p.console.servlets.bid.loanmanage.jkgl.DetailAuthentication" %>
<%@page import="com.dimeng.p2p.modules.account.console.service.entity.Grxx" %>
<%@page import="com.dimeng.p2p.console.servlets.bid.loanmanage.jkgl.ViewHkRecord" %>
<%@page import="com.dimeng.p2p.console.servlets.bid.loanmanage.jkgl.ViewRecord" %>
<%@page import="com.dimeng.p2p.S62.enums.T6230_F20" %>
<%@page import="com.dimeng.p2p.S62.enums.T6230_F11" %>
<%@page import="com.dimeng.p2p.S62.enums.T6230_F13" %>
<%@page import="com.dimeng.p2p.console.servlets.bid.loanmanage.jkgl.LoanList" %>
<%@page import="com.dimeng.p2p.S62.entities.T6230" %>
<%@page import="com.dimeng.p2p.console.servlets.bid.loanmanage.jkgl.DetailAnnexWz" %>
<%@page import="com.dimeng.p2p.console.servlets.bid.loanmanage.jkgl.DetailAnnexMsk" %>
<%@page import="com.dimeng.p2p.console.servlets.bid.loanmanage.jkgl.ViewGuarantee" %>
<%@page import="com.dimeng.p2p.console.servlets.bid.loanmanage.jkgl.ViewDyw" %>
<%@page import="com.dimeng.p2p.console.servlets.bid.loanmanage.jkgl.ViewProject" %>
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
    String ckUrl = controller.getURI(request, LoanList.class);
    String operationJK = request.getParameter("operationJK");
    if (StringHelper.isEmpty(operationJK)) {
        operationJK = "CK";
    }else{
        CURRENT_CATEGORY = "CWGL";
        CURRENT_SUB_CATEGORY = "BLZQZRGLLIST";
        if("BLZQDZR".equals(operationJK))
        {
            ckUrl = "/console/finance/zjgl/blzq/blzqDzrList.htm";
        }else if("BLZQDSH".equals(operationJK)){
            ckUrl = "/console/finance/zjgl/blzq/blzqDshList.htm";
        }else if("BLZQZRZ".equals(operationJK)){
            ckUrl = "/console/finance/zjgl/blzq/blzqZrzList.htm";
        }else if("BLZQYZR".equals(operationJK)){
            ckUrl = "/console/finance/zjgl/blzq/blzqYzrList.htm";
        }else if("BLZQZRSB".equals(operationJK)){
            ckUrl = "/console/finance/zjgl/blzq/blzqZrsbList.htm";
        }
    }
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
                        <div class="title-container pr"><i class="icon-i w30 h30 va-middle title-left-icon"></i>借款管理-个人信息
                            <div class="pa right0 top5">
                                <input type="button" class="btn btn-blue2 radius-6 pl20 pr20 fr mr10" onclick="location.href='<%=ckUrl %>'" value="返回">
                            </div>
                        </div>
                    </div>
                    <div class="border mt20">
                        <div class="tabnav-container">
                            <ul class="clearfix">
                                <li>
                                    <a href="<%=controller.getURI(request, ViewProject.class)%>?loanId=<%=loanId%>&userId=<%=userId%>&operationJK=<%=operationJK%>"
                                       class="tab-btn">项目信息</a></li>
                                <li><a href="javascript:void(0);" class="tab-btn select-a">个人信息<i
                                        class="icon-i tab-arrowtop-icon"></i></a></li>
                                <li>
                                    <a href="<%=controller.getURI(request, DetailAuthentication.class)%>?loanId=<%=loanId%>&userId=<%=userId%>&operationJK=<%=operationJK%>"
                                       class="tab-btn">个人认证信息</a></li>
                                <%
                                    if (T6230_F13.S == t6230.F13) {
                                %>
                                <li>
                                    <a href="<%=controller.getURI(request, ViewDyw.class)%>?loanId=<%=loanId%>&userId=<%=userId%>&operationJK=<%=operationJK%>"
                                       class="tab-btn">抵押物信息</a></li>
                                <%} %>
                                <%
                                    if (T6230_F11.S == t6230.F11) {
                                %>
                                <li>
                                    <a href="<%=controller.getURI(request, ViewGuarantee.class)%>?loanId=<%=loanId%>&userId=<%=userId%>&operationJK=<%=operationJK%>"
                                       class="tab-btn">担保信息</a></li>
                                <%} %>
                                <li>
                                    <a href="<%=controller.getURI(request, DetailAnnexMsk.class)%>?loanId=<%=loanId%>&userId=<%=userId%>&operationJK=<%=operationJK%>"
                                       class="tab-btn">附件(马赛克)</a></li>
                                <li>
                                    <a href="<%=controller.getURI(request, DetailAnnexWz.class)%>?loanId=<%=loanId%>&userId=<%=userId%>&operationJK=<%=operationJK%>"
                                       class="tab-btn">附件(完整版)</a></li>
                                <%
                                    if (t6230.F20 != T6230_F20.SQZ && t6230.F20 != T6230_F20.DSH && t6230.F20 != T6230_F20.DFB && t6230.F20 != T6230_F20.YFB && t6230.F20 != T6230_F20.YZF) {
                                %>
                                <li>
                                    <a href="<%=controller.getURI(request, ViewRecord.class)%>?loanId=<%=loanId%>&userId=<%=userId%>&operationJK=<%=operationJK%>"
                                       class="tab-btn">投资记录</a></li>
                                <%if(t6230.F20 != T6230_F20.YLB){%>
                                <li>
                                    <a href="<%=controller.getURI(request, ViewHkRecord.class)%>?loanId=<%=loanId%>&userId=<%=userId%>&operationJK=<%=operationJK%>"
                                       class="tab-btn">还款计划</a></li>
                                <li><a href="<%=controller.getURI(request, ViewProgresBidInfo.class)%>?loanId=<%=loanId%>&userId=<%=userId%>&operationJK=<%=operationJK%>"
                                       class="tab-btn ">项目动态</a></li>
                                <%}} %>
                            </ul>
                        </div>
                        <div class="tab-content-container p20">
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
                            </ul>
                        </div>
                    </div>


                </div>
            </div>
        </div>
    </div>

</body>
</html>