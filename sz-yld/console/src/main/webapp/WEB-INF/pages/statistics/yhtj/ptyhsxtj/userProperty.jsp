<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.console.servlets.statistics.yhtj.ptyhsjtj.UserData" %>
<%@page import="com.dimeng.p2p.modules.statistics.console.service.entity.UserMonthData" %>
<%@page import="com.dimeng.p2p.modules.statistics.console.service.entity.UserQuarterData" %>
<%@ page import="com.dimeng.util.ObjectHelper" %>
<%@ page import="com.dimeng.p2p.modules.statistics.console.service.entity.InvestmentLoanEntity" %>
<%@ page import="com.dimeng.p2p.modules.statistics.console.service.entity.AgeDistributionEntity" %>
<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <%@include file="/WEB-INF/include/script.jsp"%>
</head>
<body>
	<%
	        CURRENT_CATEGORY = "TJGL";
	        CURRENT_SUB_CATEGORY = "PTYHSXTJ";
	        InvestmentLoanEntity investmentLoanEntity =
	            ObjectHelper.convert(request.getAttribute("investmentLoanEntity"), InvestmentLoanEntity.class);
	        AgeDistributionEntity[] userAgeEntitys = ObjectHelper.convertArray(request.getAttribute("userAgeEntity"), AgeDistributionEntity.class);
	        AgeDistributionEntity[] userSexEntitys = ObjectHelper.convertArray(request.getAttribute("userSexEntity"), AgeDistributionEntity.class);
	        AgeDistributionEntity[] userRegisterSourceEntitys = ObjectHelper.convertArray(request.getAttribute("userRegisterSourceEntity"), AgeDistributionEntity.class);
	        AgeDistributionEntity[] userInvestSourceEntitys = ObjectHelper.convertArray(request.getAttribute("userInvestSourceEntity"), AgeDistributionEntity.class);
	%>

	<div class="right-container">
		<div class="viewFramework-body">
			<div class="viewFramework-content">
				<!--在线用户统计-->
				<div class="p10">
					<table class="table">
						<tr>
							<td class="ww50"><div class="p10">
									<div class="border">
										<div class="title-container pr10">平台用户性别比例饼状图</div>
										<div class="tc">
											<div id="user_sex" style="min-width: 360px; height: 300px; margin: 0 auto"></div>
										</div>
									</div>
								</div></td>
							<td class="ww50"><div class="p10">
									<div class="border">
										<div class="title-container pr10">平台用户年龄分布饼状图</div>
										<div class="tc">
											<div id="user_age" style="min-width: 360px; height: 300px; margin: 0 auto"></div>
										</div>
									</div>
								</div></td>
						</tr>
						<tr>
							<td><div class="p10">
									<div class="border">
										<div class="title-container pr10">平台注册用户终端饼状图</div>
										<div class="tc">
											<div id="user_register_source" style="min-width: 360px; height: 300px; margin: 0 auto"></div>
										</div>
									</div>
								</div></td>
							<td><div class="p10">
									<div class="border">
										<div class="title-container pr10">平台投资终端饼状图</div>
										<div class="tc">
											<div id="user_invest_source" style="min-width: 360px; height: 300px; margin: 0 auto"></div>
										</div>
									</div>
								</div></td>
						</tr>
						<tr>
							<td><div class="p10">
									<div class="border">
										<div class="title-container pr10">投资/借款用户分布饼状图</div>
										<div class="tc">
											<div id="user_type" style="min-width: 360px; height: 300px; margin: 0 auto"></div>
										</div>
									</div>
								</div></td>
							<td><div class="p10">
									<!-- <div class="border">
										<div class="title-container pr10">平台注册用户终端饼状图</div>
										<div class="tc">
											<div id="user_register_source" style="min-width: 360px; height: 300px; margin: 0 auto"></div>
										</div>
									</div> -->
								</div></td>
						</tr>
					</table>

				</div>
			</div>
		</div>
	</div>
</body>
<script type="text/javascript">
var investLoanData = [['投资人', <%=investmentLoanEntity.totalInvestment %>],['借款人', <%=investmentLoanEntity.totalLoan %>],['其他', <%=investmentLoanEntity.totalOther %>]];
var userAgeData =[
              <%int a =0;
              	for(AgeDistributionEntity userAgeEntity : userAgeEntitys){
               if(0 == a){%>
              ['<%=userAgeEntity.ageRanage%>', <%=userAgeEntity.number%>]
              <%}else{%>
              , ['<%=userAgeEntity.ageRanage%>', <%=userAgeEntity.number%>]
          <%}a++;}%>
          ];
var userSexData =[
                  <%int s =0;
                  	for(AgeDistributionEntity userSexEntity : userSexEntitys){
                   if(0 == s){%>
                  ['<%=userSexEntity.ageRanage%>', <%=userSexEntity.number%>]
                  <%}else{%>
                  , ['<%=userSexEntity.ageRanage%>', <%=userSexEntity.number%>]
              <%}s++;}%>
              ];
var userRegisterSourceData =[
                  <%int r =0;
                  	for(AgeDistributionEntity userRegisterSourceEntity : userRegisterSourceEntitys){
                   if(0 == r){%>
                  ['<%=userRegisterSourceEntity.ageRanage%>', <%=userRegisterSourceEntity.number%>]
                  <%}else{%>
                  , ['<%=userRegisterSourceEntity.ageRanage%>', <%=userRegisterSourceEntity.number%>]
              <%}r++;}%>
              ];
var userInvestSourceData =[
                             <%int i =0;
                             	for(AgeDistributionEntity userInvestSourceEntity : userInvestSourceEntitys){
                              if(0 == i){%>
                             ['<%=userInvestSourceEntity.ageRanage%>', <%=userInvestSourceEntity.number%>]
                             <%}else{%>
                             , ['<%=userInvestSourceEntity.ageRanage%>', <%=userInvestSourceEntity.number%>]
                         <%}i++;}%>
                         ];
          
</script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/highcharts.js"></script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/tjgl/userProperty.js"></script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/exporting.js"></script>
</html>