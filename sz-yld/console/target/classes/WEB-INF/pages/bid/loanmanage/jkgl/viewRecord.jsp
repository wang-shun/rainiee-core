<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.console.servlets.finance.zjgl.dfgl.YqddfList"%>
<%@page import="com.dimeng.p2p.console.servlets.bid.loanmanage.jkgl.ViewProgresBidInfo"%>
<%@page import="com.dimeng.p2p.console.servlets.bid.loanmanage.jkgl.LoanList"%>
<%@page import="com.dimeng.p2p.S61.enums.T6110_F06" %>
<%@page import="com.dimeng.p2p.S62.entities.T6230" %>
<%@page import="com.dimeng.p2p.S62.enums.T6230_F11" %>
<%@page import="com.dimeng.p2p.S62.enums.T6230_F13" %>
<%@page import="com.dimeng.p2p.S62.enums.T6230_F20" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.grxx.JbxxView" %>
<%@page import="com.dimeng.p2p.modules.bid.console.service.entity.TbRecord" %>
<%@page import="com.dimeng.util.ObjectHelper" %>
<%@page import="com.dimeng.p2p.console.servlets.AbstractConsoleServlet" %>
<%@page import="com.dimeng.p2p.console.servlets.bid.loanmanage.jkgl.ViewProject" %>
<%@page import="com.dimeng.p2p.console.servlets.bid.loanmanage.jkgl.ViewUserInfo" %>
<%@page import="com.dimeng.p2p.console.servlets.bid.loanmanage.jkgl.DetailAuthentication" %>
<%@page import="com.dimeng.p2p.console.servlets.bid.loanmanage.jkgl.ViewEnterprise" %>
<%@page import="com.dimeng.p2p.console.servlets.bid.loanmanage.jkgl.ViewDyw" %>
<%@page import="com.dimeng.p2p.console.servlets.bid.loanmanage.jkgl.ViewGuarantee" %>
<%@page import="com.dimeng.p2p.console.servlets.bid.loanmanage.jkgl.DetailAnnexWz" %>
<%@page import="com.dimeng.p2p.console.servlets.bid.loanmanage.jkgl.DetailAnnexMsk" %>
<%@page import="com.dimeng.p2p.console.servlets.bid.loanmanage.jkgl.ViewHkRecord" %>
<%@ page import="com.dimeng.p2p.S61.enums.T6110_F10" %>
<%@ page import="com.dimeng.p2p.console.servlets.account.vipmanage.qyxx.ViewQyxx" %>
<%@ page import="com.dimeng.p2p.console.servlets.account.vipmanage.jgxx.ViewJgxx" %>
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
    PagingResult<TbRecord> result = ObjectHelper.convert(request.getAttribute("t6250s"), PagingResult.class);
    TbRecord[] t6250s = result.getItems();
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
                        <div class="title-container pr"><i class="icon-i w30 h30 va-middle title-left-icon"></i>查看投资记录
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
                                       class="tab-btn ">项目信息</a></li>
                                <%
                                    if (userType == T6110_F06.ZRR) {
                                %>
                                <li>
                                    <a href="<%=controller.getURI(request, ViewUserInfo.class)%>?loanId=<%=loanId%>&userId=<%=userId%>&operationJK=<%=operationJK%>"
                                       class="tab-btn ">个人信息</a></li>
                                <li>
                                    <a href="<%=controller.getURI(request, DetailAuthentication.class)%>?loanId=<%=loanId%>&userId=<%=userId%>&operationJK=<%=operationJK%>"
                                       class="tab-btn ">个人认证信息</a></li>
                                <%
                                    }
                                %>
                                <%
                                    if (userType == T6110_F06.FZRR) {
                                %>
                                <li>
                                    <a href="<%=controller.getURI(request, ViewEnterprise.class)%>?loanId=<%=loanId%>&userId=<%=userId%>&operationJK=<%=operationJK%>"
                                       class="tab-btn ">企业信息</a></li>
                                <%
                                    }
                                %>
                                <%
                                    if (T6230_F13.S == t6230.F13) {
                                %>
                                <li>
                                    <a href="<%=controller.getURI(request, ViewDyw.class)%>?loanId=<%=loanId%>&userId=<%=userId%>&operationJK=<%=operationJK%>"
                                       class="tab-btn ">抵押物信息</a></li>
                                <%} %>
                                <%
                                    if (T6230_F11.S == t6230.F11) {
                                %>
                                <li>
                                    <a href="<%=controller.getURI(request, ViewGuarantee.class)%>?loanId=<%=loanId%>&userId=<%=userId%>&operationJK=<%=operationJK%>"
                                       class="tab-btn ">担保信息</a></li>
                                <%} %>
                                <li>
                                    <a href="<%=controller.getURI(request, DetailAnnexMsk.class)%>?loanId=<%=loanId%>&userId=<%=userId%>&operationJK=<%=operationJK%>"
                                       class="tab-btn ">附件(马赛克)</a></li>
                                <li>
                                    <a href="<%=controller.getURI(request, DetailAnnexWz.class)%>?loanId=<%=loanId%>&userId=<%=userId%>&operationJK=<%=operationJK%>"
                                       class="tab-btn ">附件(完整版)</a></li>
                                <li><a href="javascript:void(0)" class="tab-btn select-a">投资记录<i class="icon-i tab-arrowtop-icon"></i></a></li>
                                <%
                                    if (t6230.F20 != T6230_F20.SQZ && t6230.F20 != T6230_F20.DSH && t6230.F20 != T6230_F20.DFB && t6230.F20 != T6230_F20.YFB && t6230.F20 != T6230_F20.YLB) {
                                %>
                                <li>
                                    <a href="<%=controller.getURI(request, ViewHkRecord.class)%>?loanId=<%=loanId%>&userId=<%=userId%>&operationJK=<%=operationJK%>"
                                       class="tab-btn ">还款计划</a></li>
                                <li><a href="<%=controller.getURI(request, ViewProgresBidInfo.class)%>?loanId=<%=loanId%>&userId=<%=userId%>&operationJK=<%=operationJK%>"
                                       class="tab-btn ">项目动态</a></li>
                                <%} %>
                            </ul>
                        </div>
                        <form id="form_loan"
                              action="<%=controller.getURI(request, com.dimeng.p2p.console.servlets.bid.loanmanage.jkgl.ViewRecord.class)%>"
                              method="get">
                            <input name="userId" value="<%=request.getParameter("userId") %>" type="hidden"/>
                            <input name="loanId" value="<%=request.getParameter("loanId") %>" type="hidden"/>
                            <input name="operationJK" value="<%=request.getParameter("operationJK") %>" type="hidden"/>
                            <div class="tab-content-container p20">

                                <div class="tab-item">

                                    <table class="table table-style gray6 tl">
                                        <thead>
                                        <tr class="title tc">
                                            <th>序号</th>
                                            <th>投资人</th>
                                            <th>投资金额(元)</th>
                                            <th>红包金额(元)</th>
                                            <th>体验金金额(元)</th>
                                            <th>加息利率(%)</th>
                                            <th>投资时间</th>
                                            <th>来源</th>
                                            <th>投资方式</th>
                                        </tr>
                                        </thead>
                                        <tbody class="f12">
                                        <%
                                            if (t6250s != null && t6250s.length > 0) {
                                                int i = 1;
                                                for (TbRecord t6250 : t6250s) {
                                                    if (t6250 == null) {
                                                        continue;
                                                    }
                                        %>
                                        <tr class="tc">
                                            <td><%=i++%></td>
                                            <td>
                                              <span class="blue">
                                                  <%
                                                  String url = "javascript:void(0)";
                                                      if(T6110_F06.ZRR == t6250.userType && dimengSession.isAccessableResource(JbxxView.class))
                                                      {
                                                          url = controller.getURI(request,JbxxView.class)+"?userId="+t6250.F03+"&operationJK="+operationJK;
                                                      }
                                                      else if(T6110_F06.FZRR == t6250.userType && T6110_F10.S == t6250.sfdb && dimengSession.isAccessableResource(ViewJgxx.class))
                                                      {
                                                          url = controller.getURI(request,ViewJgxx.class)+"?id="+t6250.F03+"&operationJK="+operationJK;
                                                      }
                                                      else if(T6110_F06.FZRR == t6250.userType && T6110_F10.F == t6250.sfdb && dimengSession.isAccessableResource(ViewQyxx.class))
                                                      {
                                                          url = controller.getURI(request,ViewQyxx.class)+"?id="+t6250.F03+"&operationJK="+operationJK;
                                                      }
                                                  %>
                                                <a href="<%=url%>" class="mr10"><%StringHelper.filterHTML(out, t6250.userName);%></a>
                                              </span>
                                            </td>
                                            <td><%=Formater.formatAmount(t6250.F04)%></td>
                                            <td><%=Formater.formatAmount(t6250.hbAmount)%></td>
                                            <td><%=Formater.formatAmount(t6250.exAmount)%></td>
                                            <td><%=Formater.formatAmount(t6250.jxll)%></td>
                                            <td><%=DateTimeParser.format(t6250.F06)%></td>
                                            <td><%=t6250.F11.getChineseName()%></td>
                                            <td><%=t6250.bidWay%></td>
                                        </tr>
                                        <%
                                            }
                                        } else {
                                        %>
                                        <tr class="tc">
                                            <td colspan="9">暂无数据</td>
                                        </tr>
                                        <%} %>
                                        </tbody>
                                    </table>
                                    <%AbstractConsoleServlet.rendPagingResult(out, result);%>
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