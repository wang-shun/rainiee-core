<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.grxx.GrList"%>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.grxx.TbjlView" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.grxx.JkjlView" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.grxx.KxrzView" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.grxx.ByrzView" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.grxx.GrxlxxView" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.grxx.GrgzxxView" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.grxx.JbxxView" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.grxx.FcxxView" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.grxx.CcxxView" %>
<%@page import="com.dimeng.p2p.console.servlets.AbstractConsoleServlet" %>
<%@page import="com.dimeng.p2p.modules.account.console.service.entity.BasicInfo" %>
<%@page import="com.dimeng.util.ObjectHelper" %>
<%@page import="com.dimeng.p2p.modules.account.console.service.entity.Dfxxmx" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.grxx.ViewListDbjl" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.grxx.ViewListDfjl" %>
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
    CURRENT_SUB_CATEGORY = "GRXX";
    BasicInfo basicInfo = ObjectHelper.convert(request.getAttribute("basicInfo"), BasicInfo.class);
    boolean is_business = Boolean.parseBoolean(configureProvider.getProperty(BusinessVariavle.IS_BUSINESS));
    boolean is_open_risk_assess = Boolean.parseBoolean(configureProvider.getProperty(RegulatoryPolicyVariavle.IS_OPEN_RISK_ASSESS));
    String one_year_risk_assess_num = configureProvider.getProperty(RegulatoryPolicyVariavle.ONE_YEAR_RISK_ASSESS_NUM);
    String assessNumStr = "";
    if(!StringHelper.isEmpty(one_year_risk_assess_num)){
        int assessNumInt = IntegerParser.parse(one_year_risk_assess_num);
        assessNumStr = (assessNumInt-basicInfo.assessedNum)+"/"+assessNumInt;
    }
    PagingResult<Dfxxmx> result = ObjectHelper.convert(request.getAttribute("result"), PagingResult.class);
    boolean isHasBadClaim = BooleanParser.parse(configureProvider.getProperty(BadClaimVariavle.IS_BADCLAIM_TRANSFER));
    boolean isHasGuarantor = BooleanParser.parse(configureProvider.getProperty(GuarantorVariavle.IS_HAS_GUARANTOR));
    String returnUrl = controller.getURI(request, GrList.class);
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
                        <div class="title-container"><i class="icon-i w30 h30 va-middle title-left-icon"></i>用户详细信息
                            <div class="fr mt5">
                                <input type="button" class="btn btn-blue2 radius-6 pl20 pr20 fr mr10" onclick="location.href='<%=returnUrl %>'" value="返回">
                            </div>
                        </div>
						<div class="content-container pl40 pt30 pr40">
                            <ul class="gray9 input-list-container clearfix">
                                <li><span class="display-ib mr5">用户名：</span><span
                                        class="display-ib mr40 gray3"><%StringHelper.filterHTML(out, basicInfo.userName);%></span>
                                </li>
                                <li><span class="display-ib mr5">账户余额：</span><span
                                        class="display-ib mr40 gray3"><%=Formater.formatAmount(basicInfo.accountBalance)%>元</span>
                                </li>
                                <li><span class="display-ib mr5">必要认证：</span>
        	<span class="display-ib mr40 gray3">
        		<%
                    int byrztg = basicInfo.byrztg;
                    if (byrztg <= 0) {
                %>
							     	未认证
							     <%
                                 } else {
                                 %>
							   	 通过<%=byrztg%>项
							   	 <%
                                     }
                                 %>
        	</span>
                                </li>
                                <li><span class="display-ib mr5">借款负债：</span><span
                                        class="display-ib mr40 gray3"> <%=Formater.formatAmount(basicInfo.borrowingLiability)%>元</span>
                                </li>
                                <li><span class="display-ib mr5">注册时间：</span><span
                                        class="display-ib mr40 gray3"><%=TimestampParser.format(basicInfo.registrationTime)%></span>
                                </li>
                                <li><span class="display-ib mr5">净资产：</span><span
                                        class="display-ib mr40 gray3"> <%=Formater.formatAmount(basicInfo.netAssets)%>元 </span>
                                </li>
                                <li><span class="display-ib mr5">理财资产：</span><span
                                        class="display-ib mr40 gray3"><%=Formater.formatAmount(basicInfo.lczc)%>元 </span>
                                </li>
                                <li><span class="display-ib mr5">可选认证：</span>
        	<span class="display-ib mr40 gray3">
        		<%
                    int kxrztg = basicInfo.kxrztg;
                    if (kxrztg <= 0) {
                %>
							     	未认证
							     <%
                                 } else {
                                 %>
							   	 通过<%=kxrztg%>项
							   	 <%
                                     }
                                 %>
        	</span>
                                </li>
                                <li><span class="display-ib mr5">逾期次数：</span><span
                                        class="display-ib mr40 gray3"><%=basicInfo.overdueCount%></span></li>
                                <li><span class="display-ib mr5">严重逾期次数：</span><span
                                        class="display-ib mr40 gray3"><%=basicInfo.seriousOverdue%></span></li>
                                 <%if(is_business){ %>
                                <li><span class="display-ib mr5">业务员工号：</span><span class="display-ib mr40 gray3"><%=StringHelper.isEmpty(basicInfo.employNum)?"无":basicInfo.employNum %></span></li>
                                <%} %>
                            </ul>
                        </div>
                    </div>
                    <!--切换栏目-->
                    <div class="border mt20">
                      <form action="<%=controller.getURI(request, ViewListDfjl.class) %>?userId=<%=basicInfo.userId%>&operationJK=<%=operationJK%>" method="post" id="form1">
                        <input type="hidden" name="userId" value="<%StringHelper.filterHTML(out, request.getParameter("userId"));%>"/>
                        <div class="tabnav-container">
                            <ul class="clearfix border-b-s">
                                <li>
                                    <a href="<%=controller.getURI(request, JbxxView.class)%>?userId=<%=basicInfo.userId%>&operationJK=<%=operationJK%>"
                                       class="tab-btn mr20">基本信息</a></li>
                                <li>
                                    <a href="<%=controller.getURI(request, GrxlxxView.class)%>?userId=<%=basicInfo.userId%>&operationJK=<%=operationJK%>"
                                       class="tab-btn">个人学历信息</a></li>
                                <li>
                                    <a href="<%=controller.getURI(request, GrgzxxView.class)%>?userId=<%=basicInfo.userId%>&operationJK=<%=operationJK%>"
                                       class="tab-btn">个人工作信息</a></li>
                                <li>
                                    <a href="<%=controller.getURI(request, FcxxView.class)%>?userId=<%=basicInfo.userId%>&operationJK=<%=operationJK%>"
                                       class="tab-btn">房产信息</a></li>
                                <li>
                                    <a href="<%=controller.getURI(request, CcxxView.class)%>?userId=<%=basicInfo.userId%>&operationJK=<%=operationJK%>"
                                       class="tab-btn">车产信息</a></li>
                                <li>
                                    <a href="<%=controller.getURI(request, ByrzView.class)%>?userId=<%=basicInfo.userId%>&operationJK=<%=operationJK%>"
                                       class="tab-btn">必要认证（<%=byrztg%>/<%=basicInfo.needAttestation%>）</a></li>
                                <li>
                                    <a href="<%=controller.getURI(request, KxrzView.class)%>?userId=<%=basicInfo.userId%>&operationJK=<%=operationJK%>"
                                       class="tab-btn">可选认证（<%=kxrztg%>/<%=basicInfo.notNeedAttestation%>）</a></li>
                                <%if(isHasGuarantor){ %>
                                <li><a class="tab-btn"
                                       href="<%=controller.getURI(request, ViewListDbjl.class)%>?userId=<%=basicInfo.userId%>&operationJK=<%=operationJK%>">担保记录</a>
                                </li>
                                <li><a class="tab-btn select-a"
                                       href="<%=controller.getURI(request, ViewListDfjl.class)%>?userId=<%=basicInfo.userId%>&operationJK=<%=operationJK%>">垫付记录</a>
                                </li>
                                <%} %> 
                                <li>
                                    <a href="<%=controller.getURI(request, JkjlView.class)%>?userId=<%=basicInfo.userId%>&operationJK=<%=operationJK%>"
                                       class="tab-btn">借款记录</a></li>
                                <li>
                                    <a href="<%=controller.getURI(request, TbjlView.class)%>?userId=<%=basicInfo.userId%>&operationJK=<%=operationJK%>"
                                       class="tab-btn">投资记录</a></li>
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