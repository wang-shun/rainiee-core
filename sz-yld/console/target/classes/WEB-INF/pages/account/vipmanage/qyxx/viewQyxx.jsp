<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.qyxx.KxrzView"%>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.qyxx.ByrzView"%>
<%@page import="com.dimeng.p2p.modules.account.console.service.entity.XyrzTotal"%>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.qyxx.QyList"%>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.qyxx.tzjl.QyTbjlView" %>
<%@page import="com.dimeng.p2p.S61.entities.T6161" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.qyxx.ccxx.ViewListCcxx" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.qyxx.fcxx.ViewListFcxx" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.qyxx.lxxx.ViewLxxx" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.qyxx.cwzk.ViewCwzk" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.qyxx.jscl.ViewJscl" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.qyxx.jkxx.ViewJkxx" %>
<%@page import="com.dimeng.util.ObjectHelper" %>
<%@page import="com.dimeng.p2p.S61.enums.T6110_F17" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.qyxx.dbjl.ViewListDbjl" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.qyxx.dfjl.ViewListDfjl" %>
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
    CURRENT_SUB_CATEGORY = "QY";
    T6161 entity = ObjectHelper.convert(
            request.getAttribute("info"), T6161.class);
    T6110_F17 t6110_f17 = request.getAttribute("t6110_f17")==null?T6110_F17.F: (T6110_F17)request.getAttribute("t6110_f17");
    XyrzTotal xyrzTotal = ObjectHelper.convert(request.getAttribute("xyrzTotal"), XyrzTotal.class);
    boolean isHasGuarantor = BooleanParser.parse(configureProvider.getProperty(GuarantorVariavle.IS_HAS_GUARANTOR));
    String escrow = configureProvider.getProperty(SystemVariable.ESCROW_PREFIX);
    String returnUrl = controller.getURI(request, QyList.class);
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
                        <div class="title-container"><i class="icon-i w30 h30 va-middle title-left-icon"></i>企业信息
                            <div class="fr mt5">
                                <input type="button" class="btn btn-blue2 radius-6 pl20 pr20 fr mr10" onclick="location.href='<%=returnUrl %>'" value="返回">
                            </div>
                        </div>
                    </div>

                    <!--切换栏目-->
                    <div class="border mt20">
                        <div class="tabnav-container">
                            <ul class="clearfix">
                                <li><a href="javascript:void(0);" class="tab-btn select-a">企业信息<i
                                        class="icon-i tab-arrowtop-icon"></i></a></li>
                                <li>
                                    <a href="<%=controller.getURI(request, ViewJscl.class)%>?id=<%StringHelper.filterHTML(out, request.getParameter("id"));%>&operationJK=<%=operationJK%>"
                                       class="tab-btn ">介绍资料</a></li>
                                <li>
                                    <a href="<%=controller.getURI(request, ViewCwzk.class)%>?id=<%StringHelper.filterHTML(out, request.getParameter("id"));%>&operationJK=<%=operationJK%>"
                                       class="tab-btn ">财务状况</a></li>
                                <li>
                                    <a href="<%=controller.getURI(request, ViewLxxx.class)%>?id=<%StringHelper.filterHTML(out, request.getParameter("id"));%>&operationJK=<%=operationJK%>"
                                       class="tab-btn ">联系信息</a></li>
                                <li>
                                    <a href="<%=controller.getURI(request, ViewListCcxx.class)%>?id=<%StringHelper.filterHTML(out, request.getParameter("id"));%>&operationJK=<%=operationJK%>"
                                       class="tab-btn ">车产信息</a></li>
                                <li>
                                    <a href="<%=controller.getURI(request, ViewListFcxx.class)%>?id=<%StringHelper.filterHTML(out, request.getParameter("id"));%>&operationJK=<%=operationJK%>"
                                       class="tab-btn ">房产信息</a></li>
                                <%if(isHasGuarantor && !"huifu".equals(escrow)){ %>
                                <li><a class="tab-btn"
                                       href="<%=controller.getURI(request, ViewListDbjl.class)%>?id=<%StringHelper.filterHTML(out, request.getParameter("id"));%>&operationJK=<%=operationJK%>">担保记录</a>
                                </li>
                                <li><a class="tab-btn"
                                       href="<%=controller.getURI(request, ViewListDfjl.class)%>?id=<%StringHelper.filterHTML(out, request.getParameter("id"));%>&operationJK=<%=operationJK%>">垫付记录</a>
                                </li>
                                <%} %>
                                <li>
                                    <a href="<%=controller.getURI(request, ViewJkxx.class)%>?id=<%StringHelper.filterHTML(out, request.getParameter("id"));%>&operationJK=<%=operationJK%>"
                                       class="tab-btn ">借款记录</a></li>
                                <li>
                                    <a class="tab-btn" href="<%=controller.getURI(request, QyTbjlView.class)%>?id=<%=request.getParameter("id") %>&operationJK=<%=operationJK%>">投资记录</a>
                                </li>
                                <li>
                                    <a href="<%=controller.getURI(request, ByrzView.class)%>?id=<%=request.getParameter("id") %>&operationJK=<%=operationJK%>"
                                       class="tab-btn">必要认证（<%=xyrzTotal.byrztg %>/<%=xyrzTotal.needAttestation %>）</a></li>
                                 <li>
	                               <a href="<%=controller.getURI(request, KxrzView.class)%>?id=<%=request.getParameter("id") %>&operationJK=<%=operationJK%>"
	                                  class="tab-btn">可选认证（<%=xyrzTotal.kxrztg %>/<%=xyrzTotal.notNeedAttestation %>）</a></li>
                            </ul>
                        </div>
                        <div class="tab-content-container p20">


                            <div class="tab-item">
                                <ul class="gray6">
                                    <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>企业名称：</span>
                                        <%StringHelper.filterHTML(out, entity.F04); %>
                                        <span class="ml5"></span></li>
                                    <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5"></em>企业简称：</span>
                                        <%StringHelper.filterHTML(out, entity.F18); %>
                                    </li>
                                    <%if ("N".equals(entity.F20)) { %>
                                    <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>营业执照登记注册号：</span>
                                        <%StringHelper.filterHTML(out, entity.F03); %>
                                    </li>
                                    <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>企业纳税号：</span>
                                        <%StringHelper.filterHTML(out, entity.F05);%>
                                        <span class="ml5"></span></li>
                                    <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>组织机构代码：</span>
                                        <%StringHelper.filterHTML(out, entity.F06);%>
                                        <span class="ml5"></span></li>
                                    <%} else { %>
                                    <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>社会信用代码：</span>
                                        <%StringHelper.filterHTML(out, entity.F19);%>
                                        <span class="ml5"></span></li>
                                    <%} %>
                                    <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>注册年份：</span>
                                        <%=entity.F07%>
                                        <span class="ml5">年</span></li>
                                    <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>注册资金：</span>
                                        <%=entity.F08%>
                                        <span class="ml5">万元</span></li>
                                    <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>行业：</span>
                                        <%StringHelper.filterHTML(out, entity.F09);%>
                                        <span class="ml5"></span></li>
                                    <li class="mb10">
				                        <span class="display-ib w200 tr mr5"><em class="red pr5">*</em>企业规模：</span>
				                        <%=entity.F10%>
				                        <span class="ml5">人</span>
				                    </li>
                                    <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>法人：</span>
                                        <%StringHelper.filterHTML(out, entity.F11);%>
                                        <span class="ml5"></span></li>
                                    <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>法人身份证号：</span>
                                        <%StringHelper.filterHTML(out, StringHelper.decode(entity.F13));%>
                                        <span class="ml5"></span></li>
                                    <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>资产净值：</span>
                                        <%=entity.F14%>
                                        <span class="ml5">万元</span></li>
                                    <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>上年度经营现金流入：</span>
                                        <%=entity.F15%>
                                        <span class="ml5">万元</span></li>
                                    <li class="mb10"><span class="display-ib w200 tr mr5">贷款卡证书编号：</span>
                                        <%StringHelper.filterHTML(out, entity.F16);%>
                                        <span class="ml5"></span></li>
                                    <li class="mb10"><span class="display-ib w200 tr mr5">企业信用证书编号：</span>
                                        <%StringHelper.filterHTML(out, entity.F17);%>
                                        <span class="ml5"></span></li>
                                   	<% 
                                   	String ESCROW_PREFIX = configureProvider.getProperty(SystemVariable.ESCROW_PREFIX);
                                   	if(null != ESCROW_PREFIX && !ESCROW_PREFIX.isEmpty() && "yeepay".equalsIgnoreCase(ESCROW_PREFIX)){ 
                                    %>
									<li class="mb10"><span class="display-ib w200 tr mr5">
										<em class="red pr5">*</em>开户银行许可证：</span> 
										<%StringHelper.filterHTML(out,  entity.F21);%>
                                        <span class="ml5"></span>
                                    </li>
                                    <%} %>
                                    <li class="mb10"><span
                                            class="display-ib w200 tr mr5">是否允许投资：</span>
                                        <input id="isInvestorHidden" type="hidden" name="isInvestorHidden" value="<%=request.getParameter("isInvestor")%>"> 
                                        <label class="display-ib mr50 pt5 pb5" for="ChoiceIs">
                                        	<input type="radio" name="isInvestor" disabled="disabled" class="mr10" value="S" <%if(T6110_F17.S.equals(t6110_f17)){ %>checked="checked"<%} %>/>是
                                        </label>
                                        <label class="display-ib mr50 pt5 pb5" for="ChoiceNo">
                                        	<input type="radio" name="isInvestor" disabled="disabled" class="mr10" value="F" <%if(T6110_F17.F.equals(t6110_f17)){ %>checked="checked"<%} %>/>否
                                        </label>

                                        <span tip></span>
                                        <span errortip class="" style="display: none"></span>
                                    </li>
                                    <li class="mb10">
                                    </li>
                                </ul>
                            </div>

                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
<script type="text/javascript"
        src="<%=controller.getStaticPath(request)%>/js/validation.js"></script>
</body>
</html>