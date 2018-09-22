<%@page import="com.dimeng.p2p.S70.entities.T7054"%>
<%@page import="com.dimeng.p2p.front.servlets.xxpl.Qtxx"%>
<%@page import="com.dimeng.p2p.S50.entities.T5011" %>
<%@page import="com.dimeng.p2p.modules.base.front.service.ArticleManage" %>
<%@page import="com.dimeng.p2p.S50.enums.T5011_F02" %>
<%@page import="java.util.*" %>
<%@page import="com.dimeng.p2p.S61.entities.T6196"%>
<%@page import="com.dimeng.p2p.S61.entities.T6197"%>
<%@page import="com.dimeng.p2p.modules.bid.front.service.BidManage"%>
<%@page import="com.dimeng.p2p.modules.bid.front.service.entity.IndexStatic"%>
<%@page import="com.dimeng.p2p.repeater.policy.OperateDataManage"%>
<%@page import="com.dimeng.p2p.repeater.policy.entity.InvestmentLoanEntity" %>
<%@page import="com.dimeng.p2p.repeater.policy.entity.AgeDistributionEntity" %>
<%@page import="com.dimeng.p2p.repeater.policy.entity.VolumeTimeLimit" %>
<%@page import="com.dimeng.p2p.repeater.policy.entity.VolumeType" %>
<%@page import="com.dimeng.p2p.repeater.policy.entity.PlatformRiskControlEntity" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
	<% ArticleManage articleManage = serviceSession.getService(ArticleManage.class); %>
    <title>运营数据 信息披露</title>
    <%@include file="/WEB-INF/include/style.jsp" %>
</head>
<body>
<%@include file="/WEB-INF/include/header.jsp" %>
<%
	BidManage bidManage = serviceSession.getService(BidManage.class);
	IndexStatic indexStatic = bidManage.getIndexStatic();
	OperateDataManage manage = serviceSession.getService(OperateDataManage.class);
	T6196 t6196 = manage.getT6196();
	InvestmentLoanEntity investmentLoanEntity =manage.getInvestmentLoanData();
	AgeDistributionEntity[] ageDistributions=manage.getAgeRanageData();
	VolumeTimeLimit[] volumeTimeLimits =manage.getVolumeTimeLimits();
	VolumeType[] volumeTypes =manage.getVolumeTypes();
	List<T6197> t6197s= manage.getT6197List();
	PlatformRiskControlEntity prce=manage.getPlatformRCE();
	BigDecimal[] totals =manage.getTotalInvestAmount();
	T7054 t7054 = manage.getT7054();
	Date statisticalDate = manage.getStatisticalDate();
%>
<div class="disclosure wrap">
	<div class="disclosure_data">
		<h2><span>·</span>平台数据总览<em>数据截止至：<%=statisticalDate %></em></h2>
		<ul>
			<li>
				<div class="item">
					<div class="til">
						<span class="hover_tips">
							<div class="hover_tips_con">
								<div class="arrow"></div>
								<div class="border">指自平台成立起，经平台撮合完成的借款项目的本金总合</div>
							</div>
						</span>
						累计交易金额（元）
					</div>
					<p data-from="0.00" data-to="<%=Formater.formatAmount(t7054.F01.add(t6196.F02).doubleValue()).replace(",", "") %>"><%=Formater.formatAmount(t7054.F01.add(t6196.F02).doubleValue()) %></p>
				</div>
			</li>
			<li>
				<div class="item">
					<div class="til">
						<span class="hover_tips">
							<div class="hover_tips_con">
								<div class="arrow"></div>
								<div class="border">指自平台立起，经平台撮合完成的借款交易笔数总合</div>
							</div>
						</span>
						累计交易笔数（笔）
					</div>
					<p><%=t7054.F02+t6196.F05 %></p>
				</div>
			</li>
			<li>
				<div class="item">
					<div class="til">
						<span class="hover_tips">
							<div class="hover_tips_con">
								<div class="arrow"></div>
								<div class="border">平台注册用户总数</div>
							</div>
						</span>
						注册用户数（人）
					</div>
					<p><%=t7054.F11+t6196.F03 %></p>
				</div>
			</li>
			<li>
				<div class="item">
					<div class="til">
						<span class="hover_tips">
							<div class="hover_tips_con">
								<div class="arrow"></div>
								<div class="border">指截至统计时点，通过平台已经上线运行的网络借贷信息中介平台完成的借款总余额</div>
							</div>
						</span>
						借贷余额（元）
					</div>
					<p data-from="0.00" data-to="<%=Formater.formatAmount(t7054.F03.add(t6196.F31).doubleValue()).replace(",", "") %>"><%=Formater.formatAmount(t7054.F03.add(t6196.F31).doubleValue()) %></p>
				</div>
			</li>
			<li>
				<div class="item">
					<div class="til">
						<span class="hover_tips">
							<div class="hover_tips_con">
								<div class="arrow"></div>
								<div class="border">指截至统计时点，通过平台已经上线运行的网络借贷信息中介平台完成的借款余额总笔数</div>
							</div>
						</span>
						借贷余额笔数（笔）
					</div>
					<p><%=t7054.F04+t6196.F32 %></p>
				</div>
			</li>
			<li>
				<div class="item">
					<div class="til">
						<span class="hover_tips">
							<div class="hover_tips_con">
								<div class="arrow"></div>
								<div class="border">指截至统计时点，待还款的利息</div>
							</div>
						</span>
						利息余额（元）
					</div>
					<p data-from="0.00" data-to="<%=Formater.formatAmount(t7054.F12.add(t6196.F33).doubleValue()).replace(",", "") %>"><%=Formater.formatAmount(t7054.F12.add(t6196.F33).doubleValue()) %></p>
				</div>
			</li>
			<li>
				<div class="item">
					<div class="til">
						<span class="hover_tips">
							<div class="hover_tips_con">
								<div class="arrow"></div>
								<div class="border">指出借人通过平台成功出借资金的出借人总数（投资人总数）。同一出借人多次出借的，按实际出借人计算。</div>
							</div>
						</span>
						累计出借人数量（人）
					</div>
					<p><%=t7054.F05+t6196.F34 %></p>
				</div>
			</li>
			<li>
				<div class="item">
					<div class="til">
						<span class="hover_tips">
							<div class="hover_tips_con">
								<div class="arrow"></div>
								<div class="border">指截至统计时点仍存在待还借款的借款人总数。同一借款人多次借款的，按实际借款人计算；</div>
							</div>
						</span>
						当前出借人数量（人）
					</div>
					<p><%=t7054.F06+t6196.F35 %></p>
				</div>
			</li>
			<li>
				<div class="item">
					<div class="til">
						<span class="hover_tips">
							<div class="hover_tips_con">
								<div class="arrow"></div>
								<div class="border">累计用户赚取收益</div>
							</div>
						</span>
						累计赚取收益（元）
					</div>
					<p class="f28 gray3" data-from="0.00" data-to="<%=Formater.formatAmount(t7054.F13.add(t6196.F04).doubleValue()).replace(",", "") %>"><%=Formater.formatAmount(t7054.F13.add(t6196.F04).doubleValue()) %></p>
				</div>
			</li>
			<li>
				<div class="item">
					<div class="til">
						<span class="hover_tips">
							<div class="hover_tips_con">
								<div class="arrow"></div>
								<div class="border">指借款人通过平台成功借款的借款人总数。同一借款人多次借款的，按实际借款人计算</div>
							</div>
						</span>
						累计借款人数量（人）
					</div>
					<p><%=t7054.F07+t6196.F36 %></p>
				</div>
			</li>
			<li>
				<div class="item">
					<div class="til">
						<span class="hover_tips">
							<div class="hover_tips_con">
								<div class="arrow"></div>
								<div class="border">指截至统计时点仍存在待还借款的借款人总数。同一借款人多次借款的，按实际借款人计算</div>
							</div>
						</span>
						当前借款人数量（人）
					</div>
					<p><%=t7054.F08+t6196.F37 %></p>
				</div>
			</li>
			<li>
				<div class="item">
					<div class="til">
						<span class="hover_tips">
							<div class="hover_tips_con">
								<div class="arrow"></div>
								<div class="border">指截至统计时点，与平台具有关联关系的借款人通过平台撮合完成的借款总余额</div>
							</div>
						</span>
						关联关系借款金额（元）
					</div>
					<p class="f28 gray3" data-from="0.00" data-to="<%=Formater.formatAmount(t6196.F40.doubleValue()).replace(",", "") %>"><%=Formater.formatAmount(t7054.F14.add(t6196.F40).doubleValue()) %></p>
				</div>
			</li>
			<li>
				<div class="item">
					<div class="til">
						<span class="hover_tips">
							<div class="hover_tips_con">
								<div class="arrow"></div>
								<div class="border">指在平台撮合的项目中，借款最多一户借款人的借款余额占总借款余额的比例</div>
							</div>
						</span>
						最大单一借款人待还金额占比（%）
					</div>
					<p class="f28 gray3" data-from="0.00" data-to="<%=Formater.formatAmount(t7054.F09.add(t6196.F38).doubleValue()).replace(",", "") %>"><%=Formater.formatAmount(t7054.F09.add(t6196.F38).doubleValue()) %></p>
				</div>
			</li>
			<li>
				<div class="item">
					<div class="til">
						<span class="hover_tips">
							<div class="hover_tips_con">
								<div class="arrow"></div>
								<div class="border">指在平台撮合的项目中，借款最多的前十户借款人的借款余额占总借款余额的比例</div>
							</div>
						</span>
						前十大借款人待还金额占比（%）
					</div>
					<p class="f28 gray3" data-from="0.00" data-to="<%=Formater.formatAmount(t7054.F10.add(t6196.F39).doubleValue()).replace(",", "") %>"><%=Formater.formatAmount(t7054.F10.add(t6196.F39).doubleValue()) %></p>
				</div>
			</li>
			<li>
				<div class="item">
					<div class="til">
						<span class="hover_tips">
							<div class="hover_tips_con">
								<div class="arrow"></div>
								<div class="border">指截至统计时点，与平台具有关联关系的借款人通过平台撮合完成的借款总笔数</div>
							</div>
						</span>
						关联关系借款笔数（笔）
					</div>
					<p><%=t6196.F41 %></p>
				</div>
			</li>
		</ul>
	</div>

	<div class="disclosure_data">
		<h2><span>·</span>累计交易总额<em>数据截止至：<%=statisticalDate %></em></h2>
		<div id="investTotal"></div>
		<!-- <div class="tc" id="investTotal" style="min-width:967px;height:400px"></div> -->
	</div>

	<div class="disclosure_data">
		<h2><span>·</span>平台用户分布<em>数据截止至：<%=statisticalDate %></em></h2>
		<div class="box">
			<div class="fl">
				<!-- <h3><span>|</span>投资/借款用户分布</h3> -->
				<div class="chart" id="investUserType"></div>
				<!-- <div class="tc fl" id="investUserType" style="width:480px;height:250px"></div> -->
			</div>
			<div class="fr">
				<!-- <h3><span>|</span>平台用户年龄分布</h3> -->
				<div class="chart" id="investUserAge"></div>
				<!-- <div class="tc fl" id="investUserAge" style="width:480px;height:250px"></div> -->
			</div>
		</div>
	</div>

	<div class="disclosure_data">
		<h2><span>·</span>借款项目分布<em>数据截止至：<%=statisticalDate %></em></h2>
		<div class="box">
			<div class="fl">
				<!-- <h3><span>|</span>项目期限分布</h3> -->
				<div class="chart" id="loanProjectTerm"></div>
				<!-- <div class="tc fl" id="loanProjectTerm" style="width:480px;height:250px"></div> -->
			</div>
			<div class="fr">
				<!-- <h3><span>|</span>项目类型分布</h3> -->
				<div class="chart" id="loanProjectType"><img src="images/data_chart5.jpg"></div>
				<!-- <div class="tc fl" id="loanProjectType" style="width:480px;height:250px"></div> -->
			</div>
		</div>
	</div>

	<div class="disclosure_data">
		<h2><span>·</span>平台风险管控<em>数据截止至：<%=statisticalDate %></em></h2>
		<ul>
			<li>
				<div class="item">
					<div class="til">
						<span class="hover_tips">
							<div class="hover_tips_con">
								<div class="arrow"></div>
								<div class="border">出借人到期未收到本金和利息的金额总合。</div>
							</div>
						</span>
						逾期金额（元）
					</div>
					<p><%=Formater.formatAmount(t7054.F16.add(t6196.F26)) %></p>
				</div>
			</li>
			<li>
				<div class="item">
					<div class="til">
						<span class="hover_tips">
							<div class="hover_tips_con">
								<div class="arrow"></div>
								<div class="border">金额逾期率=逾期金额/累计交易金额</div>
							</div>
						</span>
						金额逾期率（%）
					</div>
					<p><%=Formater.formatAmount(t7054.F17.add(t6196.F27)) %></p>
				</div>
			</li>
			<li>
				<div class="item">
					<div class="til">
						<span class="hover_tips">
							<div class="hover_tips_con">
								<div class="arrow"></div>
								<div class="border">指逾期90天（不含）以上的借款本金余额。</div>
							</div>
						</span>
						逾期90天（不含）以上金额（元）
					</div>
					<p><%=Formater.formatAmount(t7054.F22.add(t6196.F44))%></p>
				</div>
			</li>
			<li>
				<div class="item">
					<div class="til">
						<span class="hover_tips">
							<div class="hover_tips_con">
								<div class="arrow"></div>
								<div class="border">出借人到期未收到本金和利息的借款的笔数。</div>
							</div>
						</span>
						逾期笔数（笔）
					</div>
					<p><%=t7054.F18+t6196.F42 %></p>
				</div>
			</li>
			<li>
				<div class="item">
					<div class="til">
						<span class="hover_tips">
							<div class="hover_tips_con">
								<div class="arrow"></div>
								<div class="border">项目逾期率=逾期笔数/累计交易金额</div>
							</div>
						</span>
						项目逾期率（%）
					</div>
					<p><%=Formater.formatAmount(t7054.F19.add(t6196.F43)) %></p>
				</div>
			</li>
			<li>
				<div class="item">
					<div class="til">
						<span class="hover_tips">
							<div class="hover_tips_con">
								<div class="arrow"></div>
								<div class="border">指逾期90天（不含）以上的借款本金余额。</div>
							</div>
						</span>
						逾期90天（不含）以上笔数（笔）
					</div>
					<p><%=t7054.F23+t6196.F45 %></p>
				</div>
			</li>
			<li>
				<div class="item">
					<div class="til">
						<span class="hover_tips">
							<div class="hover_tips_con">
								<div class="arrow"></div>
								<div class="border">累计垫付的总金额。</div>
							</div>
						</span>
						累计代偿金额（元）
					</div>
					<p><%=Formater.formatAmount(t6196.F23.add(t7054.F20)) %></p>
				</div>
			</li>
			<li>
				<div class="item">
					<div class="til">
						<span class="hover_tips">
							<div class="hover_tips_con">
								<div class="arrow"></div>
								<div class="border">累计垫付的总笔数</div>
							</div>
						</span>
						累计代偿笔数（笔）
					</div>
					<p><%=t7054.F21+t6196.F46 %></p>
				</div>
			</li>
		</ul>
	</div>
</div>
<div id="info"></div>
<%@include file="/WEB-INF/include/footer.jsp" %>
<%@include file="/WEB-INF/include/jquery-ui.jsp" %>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/highcharts.js"></script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/jquery.circliful.min.js"></script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/yysj/operationData.js"></script>
<script type="text/javascript">
	var monthCategary = [
         <%	for(int i=0;i<t6197s.size();i++){%>
         	<%="'"+DateParser.format(t6197s.get(t6197s.size()-1-i).F03,"MM/yy")+"'"%>
         	<%if(i!=t6197s.size()-1){ out.print(",");}%>
         <%}%>
	];
	var investLoan = [
        ['投资人', <%=investmentLoanEntity.totalInvestment+t6196.F06 %>],
        ['借款人', <%=investmentLoanEntity.totalLoan+t6196.F07 %>]
    ];
	<%-- <%
		Map<String,Integer> ageMap=new HashMap<String,Integer>();
		ageMap.put("90后", t6196.F08+ageDistributions[0].number);
		ageMap.put("80后", t6196.F09+ageDistributions[1].number);
		ageMap.put("70后", t6196.F10+ageDistributions[2].number);
		ageMap.put("60后", t6196.F11+ageDistributions[3].number);
		ageMap.put("其他", t6196.F12+ageDistributions[4].number);
		List<Map.Entry<String, Integer>> ageList = new ArrayList<Map.Entry<String, Integer>>(ageMap.entrySet());
		Collections.sort(ageList, new Comparator<Map.Entry<String, Integer>>() {  
		    public int compare(Map.Entry<String, Integer> o1,Map.Entry<String, Integer> o2) {  
		        return ( o1.getValue()-o2.getValue());  
		    }  
		});  
	%>
	var ageData = [
	   <%
	   for (int i = 0; i < ageList.size(); i++) {  
		    Map.Entry<String,Integer> ent=ageList.get(i);
	   %>
	   ['<%=ent.getKey()%>',<%=ent.getValue()%>]<%if(i!=ageList.size()-1){ out.print(",");}%>
	   <%}%>
	]; --%>
	var ageData =[
   	   ["90后",<%=t6196.F08+ageDistributions[0].number%>],
   	   ["80后",<%=t6196.F09+ageDistributions[1].number%>],
   	   ["70后",<%=t6196.F10+ageDistributions[2].number%>],
   	   ["60后",<%=t6196.F11+ageDistributions[3].number%>],
   	   ["其他",<%=t6196.F12+ageDistributions[4].number%>]
   ];
   <%
   		Map<String,BigDecimal> timeLimitMap=new HashMap<String,BigDecimal>();
   		timeLimitMap.put("0-3个月",t6196.F13.add(volumeTimeLimits[0].amount));
   		timeLimitMap.put("3-6个月",t6196.F14.add(volumeTimeLimits[1].amount));
   		timeLimitMap.put("6-9个月",t6196.F15.add(volumeTimeLimits[2].amount));
   		timeLimitMap.put("9-12个月",t6196.F16.add(volumeTimeLimits[3].amount));
   		timeLimitMap.put("12-24个月",t6196.F17.add(volumeTimeLimits[4].amount));
   		timeLimitMap.put("24个月以上",t6196.F18.add(volumeTimeLimits[5].amount));
   		List<Map.Entry<String, BigDecimal>> timeLimitList = new ArrayList<Map.Entry<String, BigDecimal>>(timeLimitMap.entrySet());
   %>   
   var timeLimtsData = [['1-3个月', <%=t6196.F13.add(volumeTimeLimits[0].amount) %>],
	            ['4-6个月', <%=t6196.F14.add(volumeTimeLimits[1].amount)%>],
	            ['7-9个月', <%=t6196.F15.add(volumeTimeLimits[2].amount) %>],
	            ['10-12个月', <%=t6196.F16.add(volumeTimeLimits[3].amount)%>],
	            ['12-24个月', <%=t6196.F17.add(volumeTimeLimits[4].amount)%>],
	            ['24个月以上', <%=t6196.F18.add(volumeTimeLimits[5].amount) %>],
	            ['其他(天标)', <%=t6196.F30.add(volumeTimeLimits[6].amount) %>]
   ];	
   var projectTypeData = [
         ['担保标',<%=t6196.F19.add(volumeTypes[0].amount)%>],
         ['抵押认证标',<%=t6196.F20.add(volumeTypes[1].amount)%>],
         ['实地认证标',<%=t6196.F21.add(volumeTypes[2].amount)%>],
         ['信用认证标',<%=t6196.F22.add(volumeTypes[3].amount)%>]
   ];
   var monthlyData=[
         <%for(int i=0;i<t6197s.size();i++){%>
         	<%=t6197s.get(t6197s.size()-1-i).F02.add(totals[t6197s.size()-i-1]) %>
         	<%if(i!=t6197s.size()-1){ out.print(",");}%>
         <%}%>           
   ];
</script>

</body>
</html>