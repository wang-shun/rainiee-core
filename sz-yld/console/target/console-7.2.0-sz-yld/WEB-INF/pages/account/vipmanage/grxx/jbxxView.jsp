<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.grxx.*"%>
<%@page import="com.dimeng.p2p.console.servlets.bid.loanmanage.jkgl.LoanList" %>
<%@page import="com.dimeng.p2p.modules.account.console.service.entity.BasicInfo" %>
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
    boolean isHasGuarantor = BooleanParser.parse(configureProvider.getProperty(GuarantorVariavle.IS_HAS_GUARANTOR));
    String escrow = configureProvider.getProperty(SystemVariable.ESCROW_PREFIX);
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
                        <div class="title-container">
                            <i class="icon-i w30 h30 va-middle title-left-icon"></i>用户详细信息
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
                        <div class="tabnav-container">
                            <ul class="clearfix border-b-s">
                                <li><a href="javascript:void(0);" class="tab-btn select-a">基本信息<i
                                        class="icon-i tab-arrowtop-icon"></i></a></li>
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
                                <%if(isHasGuarantor && !"huifu".equals(escrow)){ %>
                                <li><a class="tab-btn"
                                       href="<%=controller.getURI(request, ViewListDbjl.class)%>?userId=<%=basicInfo.userId%>&operationJK=<%=operationJK%>">担保记录</a>
                                </li>
                                <li><a class="tab-btn"
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
                        <div class="tab-content-container p20">

                            <!--基本信息-->
                            <div class="tab-item">
                                <ul class="gray6">
                                    <li class="mb10"><span class="display-ib w200 tr mr5">用户名：</span><%StringHelper.filterHTML(out, basicInfo.userName);%></li>
                                    <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>真实姓名：</span><%StringHelper.filterHTML(out, basicInfo.realName);%>
                                        <%
                                            if ("WYZ".equals(basicInfo.isSmrz)) {
                                        %>
                                        <span class="ico_2 ml5"></span>未认证
                                        <%
                                        } else if ("YYZ".equals(basicInfo.isSmrz)) {
                                        %>
                                        <span class="ico_1 ml5"></span>已认证
                                        <%
                                            }
                                        %>
                                    </li>
                                    <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>身份证号：</span><%StringHelper.filterHTML(out, basicInfo.identityCard);%></li>
                                    <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>手机号码：</span><%StringHelper.filterHTML(out, basicInfo.msisdn);%></li>
                                    <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>邮箱地址：</span><%StringHelper.filterHTML(out, basicInfo.mailbox);%></li>
                                    <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>性别：</span><%=basicInfo.sex != null ? basicInfo.sex.getName() : ""%></li>
                                    <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>出生日期：</span><%=DateParser.format(basicInfo.birthDate)%></li>
                                    <%--<li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>毕业学校：</span><%StringHelper.filterHTML(out, basicInfo.graduateSchool);%></li>
                                    <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>公司行业：</span><%StringHelper.filterHTML(out, basicInfo.companyBusiness);%></li>
                                    <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>公司规模：</span>
          	<%
                if (!StringHelper.isEmpty(basicInfo.companyScale)) {
                    StringHelper.filterHTML(out, basicInfo.companyScale);
            %>人
                        	<%} %></li>
                                    <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>职位：</span><%StringHelper.filterHTML(out, basicInfo.position); %></li>--%>
                                    <li class="mb10"><span class="display-ib w200 tr mr5">拉黑原因：</span><%StringHelper.filterHTML(out, basicInfo.blacklistDesc);%></li>
                                    <%if(is_open_risk_assess){ %>
                                    <li class="mb10"><span class="display-ib w200 tr mr5">风险评估：</span><%=basicInfo.riskAssess==null?"未评估":basicInfo.riskAssess.getChineseName()%></li>
                                    <li class="mb10"><span class="display-ib w200 tr mr5">评估剩余次数：</span><%=assessNumStr%></li>
                                    <%} %>
                                </ul>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</body>
</html>