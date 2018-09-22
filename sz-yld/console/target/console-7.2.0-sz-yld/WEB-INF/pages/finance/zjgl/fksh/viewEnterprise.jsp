<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.console.servlets.finance.zjgl.fksh.ViewProgresBidInfo"%>
<%@page import="com.dimeng.p2p.console.servlets.finance.zjgl.fksh.FkshList"%>
<%@page import="com.dimeng.p2p.console.servlets.finance.zjgl.fksh.ViewHkRecord" %>
<%@page import="com.dimeng.p2p.console.servlets.finance.zjgl.fksh.ViewRecord" %>
<%@page import="com.dimeng.p2p.S62.enums.T6230_F20" %>
<%@page import="com.dimeng.p2p.S62.enums.T6230_F13" %>
<%@page import="com.dimeng.p2p.S62.enums.T6230_F11" %>
<%@page import="com.dimeng.p2p.S62.entities.T6230" %>
<%@page import="com.dimeng.p2p.S61.entities.T6161" %>
<%@page import="com.dimeng.p2p.console.servlets.finance.zjgl.fksh.ViewProject" %>
<%@page import="com.dimeng.p2p.console.servlets.finance.zjgl.fksh.ViewDyw" %>
<%@page import="com.dimeng.p2p.console.servlets.finance.zjgl.fksh.ViewGuarantee" %>
<%@page import="com.dimeng.p2p.console.servlets.finance.zjgl.fksh.DetailAnnexMsk" %>
<%@page import="com.dimeng.p2p.console.servlets.finance.zjgl.fksh.DetailAnnexWz" %>
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
    int loanId = IntegerParser.parse(request.getParameter("loanId"));
    int userId = IntegerParser.parse(request.getParameter("userId"));
    T6161 t6161 = ObjectHelper.convert(request.getAttribute("t6161"), T6161.class);
    T6230 t6230 = ObjectHelper.convert(request.getAttribute("t6230"), T6230.class);
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
                                <li>
                                    <a href="<%=controller.getURI(request, ViewProject.class)%>?loanId=<%=loanId%>&userId=<%=userId%>"
                                       class="tab-btn-click">项目信息</a></li>
                                <li><a href="javascript:void(0);" class="tab-btn-click select-a">企业信息<i
                                        class="icon-i tab-arrowtop-icon"></i></a></li>
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
                            <div class="f16">企业基本信息</div>
                            <div class="hsxt"></div>
                            <div class="tab-item">
                                <ul class="gray6">
                                    <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>企业名称：</span>
                                        <%StringHelper.filterHTML(out, t6161.F04); %>
                                    </li>
                                    <%if("N".equals(t6161.F20)){ %>
                                    <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>营业执照登记注册号：</span>
                                        <%StringHelper.filterHTML(out, t6161.F03); %>
                                    </li>
                                    <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>企业纳税号：</span>
                                        <%StringHelper.filterHTML(out, t6161.F05); %>
                                    </li>
                                    <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>组织机构代码：</span>
                                        <%StringHelper.filterHTML(out, t6161.F06); %>
                                    </li>
                                    <%}else{ %>
                                    <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>社会信用代码：</span>
                                        <%StringHelper.filterHTML(out, t6161.F19); %>
                                    </li>
                                    <%} %>
                                    <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>注册年份：</span>
                                        <%=t6161.F07%>年
                                    </li>
                                    <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>注册资金：</span>
                                        <%=t6161.F08%>万元
                                    </li>
                                    <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>资产净值：</span>
                                        <%=t6161.F14%>万元
                                    </li>
                                    <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>上年度经营现金流入：</span>
                                        <%=t6161.F15%>万元
                                    </li>
                                    <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>行业：</span>
                                        <%StringHelper.filterHTML(out, t6161.F09);%>
                                    </li>
                                </ul>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/region.js"></script>
</body>
</html>