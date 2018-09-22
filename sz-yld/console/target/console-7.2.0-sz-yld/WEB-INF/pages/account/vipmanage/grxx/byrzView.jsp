<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.grxx.GrList"%>
<%@page import="com.dimeng.p2p.console.servlets.AbstractConsoleServlet" %>
<%@page import="com.dimeng.util.ObjectHelper" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.grxx.Open" %>
<%@page import="com.dimeng.p2p.modules.account.console.service.entity.Attestation" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.grxx.TbjlView" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.grxx.JkjlView" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.grxx.KxrzView" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.grxx.JbxxView" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.grxx.GrxlxxView" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.grxx.GrgzxxView" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.grxx.FcxxView" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.grxx.CcxxView" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.grxx.ViewListDbjl" %>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.grxx.ViewListDfjl" %>
<%@page import="com.dimeng.p2p.modules.account.console.service.entity.BasicInfo" %>
<%@page import="com.dimeng.p2p.console.servlets.bid.loanmanage.jkgl.LoanList" %>
<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <%@include file="/WEB-INF/include/script.jsp" %>
    <%@include file="/WEB-INF/include/highslide.jsp" %>
</head>
<body>
<%
    CURRENT_CATEGORY = "YHGL";
    CURRENT_SUB_CATEGORY = "GRXX";
    BasicInfo basicInfo = ObjectHelper.convert(request.getAttribute("basicInfo"), BasicInfo.class);
    Attestation[] needAttestations = ObjectHelper.convertArray(request.getAttribute("needAttestation"), Attestation.class);
    boolean is_business = Boolean.parseBoolean(configureProvider.getProperty(BusinessVariavle.IS_BUSINESS));
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
                        <div class="title-container"><i class="icon-i w30 h30 va-middle title-left-icon"></i>用户详细信息
                            <div class="fr mt5">
                                <input type="button" class="btn btn-blue2 radius-6 pl20 pr20 fr mr10" onclick="location.href='<%=controller.getURI(request, GrList.class) %>'" value="返回">
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
                                <li>
                                    <a href="<%=controller.getURI(request, JbxxView.class)%>?userId=<%=basicInfo.userId%>&operationJK=<%=operationJK%>"
                                       class="tab-btn">基本信息</a></li>
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
                                <li><a href="javascript:void(0)" class="tab-btn select-a">必要认证（<%=byrztg%>
                                    /<%=basicInfo.needAttestation%>）<i class="icon-i tab-arrowtop-icon"></i></a></li>
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

                            <!--个人学历信息-->
                            <div class="tab-item">
                                <div class="border table-container">
                                    <form action="<%=controller.getURI(request, TbjlView.class)%>?userId=<%=basicInfo.userId%>"
                                          method="post">
                                        <table class="table table-style gray6 tl">
                                            <thead>
                                            <tr class="title">
                                                <th class="tc">项目</th>
                                                <th class="tc">状态</th>
                                                <th class="tc">认证时间</th>
                                                <%--<th class="tc">信用分数</th>--%>
                                                <th class="tc">操作</th>
                                            </tr>
                                            </thead>
                                            <tbody class="f12">
                                            <%
                                                if (needAttestations != null) {
                                                    int i = 1;
                                                    for (Attestation need : needAttestations) {
                                                        if (need == null) {
                                                            continue;
                                                        }
                                            %>

                                            <tr>
                                                <td class="tc"><%
                                                    StringHelper.filterHTML(out, need.attestationName);%></td>
                                                <td class="tc"><%=need.attestationState.getChineseName()%>
                                                </td>
                                                <td class="tc"><%=TimestampParser.format(need.attestationTime)%>
                                                </td>
                                               <%-- <td class="tc"><%=need.creditGrades%>--%>
                                                </td>
                                                <% String[] attachments = need.attachments;
                                                    if (attachments != null) {
                                                %>
                                                <td align="center" class="blue tc">
                                                	<a href="javascript:void(0);" onclick="showImg(this,<%=need.id %>);"
						                                       class="link-blue mr20 ">查看</a>
					                                 <div class='highslide-gallery gallery-examples' style='display:none;'>
					                                 
					                                 </div>
                                                    <%-- <%
                                                        int num = attachments.length;
                                                        for (int a = 0; a < num; a++) {
                                                            String id = attachments[a];
                                                            if (!StringHelper.isEmpty(id)) {
                                                    %>
                                                    <a href="<%=controller.getURI(request,Open.class)%>?id=<%=id%>"
                                                       target="_blank" class="link-blue ">查看证件&nbsp;<%=(num==1?"":a + 1)%>
                                                        &nbsp;</a>
                                                    <%
                                                            }
                                                        }
                                                    %> --%>
                                                </td>
                                                <%} else { %>
                                                <td align="center" class="disabled tc">查看证件</td>
                                                <%} %>
                                            </tr>
                                            <% }
                                            } else {%>
                                            <tr>
                                                <td colspan="4" class="tc">暂无数据</td>
                                            </tr>
                                            <%} %>

                                            </tbody>
                                        </table>
                                    </form>

                                </div>
                            </div>


                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <script type="text/javascript">
    function showImg(obj,id){
    	$(".highslide-gallery").html("");
    	var divObj = $(obj).next();
    	$.ajax({
            type:"post",
            url:"<%=controller.getURI(request, Open.class)%>",
            data:{"id":id},
            async: false ,
            dataType:"json",
            success: function(returnData){
            	if(returnData.fileCodes != null){
            		var a = "";
            		for(var i=0;i<returnData.fileCodes.length;i++){
            			var fileCode = returnData.fileCodes[i];
            			a += "<a onclick='return hs.expand(this)' href='"+fileCode+"' class='btn04 highslide fl' ><img src='"+fileCode+"'  alt='Highslide JS' /></a>";
            		}
            		divObj.html(a);
                	var aObj = divObj.children().eq(0);
                	aObj.click();
            	}
            },
            error: function(XMLHttpRequest, textStatus, errorThrown){
            	alert(textStatus);
            }
        });
    	
    }
    </script>
</body>
</html>