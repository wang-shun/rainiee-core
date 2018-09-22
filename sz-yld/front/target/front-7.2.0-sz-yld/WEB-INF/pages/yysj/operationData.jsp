<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
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
<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <title>运营数据-<%=configureProvider.getProperty(SystemVariable.SITE_TITLE)%></title>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <link rel="stylesheet" type="text/css" href="<%=controller.getStaticPath(request)%>/js/highslide.css"/>
</head>
<body>
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
%>
<%@include file="/WEB-INF/include/header.jsp" %>
<div class="main_bg">
	<div class="platform_mod">
        <div class="platform_title clearfix"><h3 class="highlight"><i class="ico mr10"></i><span class="gray3">平台数据总览</span><i class="ico ml10"></i></h3></div>
        <div class="total">
            <ul>
                <li>累计投资金额<br />
                    <span class="f28 gray3" data-from="0.00" data-to="<%=Formater.formatAmount(indexStatic.rzzje.add(t6196.F02).doubleValue()).replace(",", "") %>"><%=Formater.formatAmount(indexStatic.rzzje.add(t6196.F02).doubleValue()) %></span>元
		        </li>
                <li class="line"></li>
                <li>注册用户数<br /><span class="f28 gray3"><%=manage.getRegisterUser()+t6196.F03 %></span>人</li>
                <li class="line"></li>
                <li>累计赚取收益<br />
					<span class="f28 gray3" data-from="0.00" data-to="<%=Formater.formatAmount(indexStatic.yhzsy.add(t6196.F04).doubleValue()).replace(",", "")%>"><%=Formater.formatAmount(indexStatic.yhzsy.add(t6196.F04).doubleValue()) %></span>元
				</li>
                <li class="line"></li>
                <li>累计成交笔数<br /><span class="f28 gray3"><%=manage.getTradeCount()+t6196.F05 %></span>笔</li>
            </ul>
		</div>
        <div class="platform_title clearfix pt30"><h3 class="highlight"><i class="ico mr10"></i><span class="gray3">累计投资金额</span><i class="ico ml10"></i></h3></div>
        <div class="tc" id="investTotal" style="min-width:967px;height:400px"></div>
        <div class="platform_title clearfix mt40"><h3 class="highlight"><i class="ico mr10"></i><span class="gray3">平台用户分布</span><i class="ico ml10"></i></h3></div>
        <div class="clearfix">
	        <div class="tc fl" id="investUserType" style="width:480px;height:250px"></div>
	        <div class="tc fl" id="investUserAge" style="width:480px;height:250px"></div>
        </div>
        <div class="platform_title clearfix mt40"><h3 class="highlight"><i class="ico mr10"></i><span class="gray3">借款项目分布</span><i class="ico ml10"></i></h3></div>       
        <div class="clearfix">
        	<div class="tc fl" id="loanProjectTerm" style="width:480px;height:250px"></div>
        	<div class="tc fl" id="loanProjectType" style="width:480px;height:250px"></div>
        </div>
        <div class="platform_title clearfix mt40"><h3 class="highlight"><i class="ico mr10"></i><span class="gray3">平台风险管控</span><i class="ico ml10"></i></h3></div>       
        <div class="mb20">
        	<div class="clearfix mt10">
            	<div class="platform_gray fl mr10">
                	<div class="f12 pt50">
                        <span class="hover_tips">
                            <div class="hover_tips_con">
                                <div class="arrow"></div>
                                <div class="border">
                                    	累计代偿金额为垫付金额
                                </div>
                            </div>
                        </span>
                       	 累计代偿金额
                    </div>
                    <span class="f28 gray3"><%=Formater.formatAmount(t6196.F23.add(prce.totalAdvancedAmount)) %></span>元
                </div>
                <div class="platform_gray fl">
                	<div class="f12 pt50">
                        <span class="hover_tips">
                            <div class="hover_tips_con">
                                <div class="arrow"></div>
                                <div class="border">
                                    	最大单户借款余额占比=最大单户待收本金/借款未还款总额
                                </div>
                            </div>
                        </span>
                        	最大单户借款余额占比
                    </div>
                    <span class="f28 gray3"><%=Formater.formatAmount(prce.maxUserLoanBalanceProportion.add(t6196.F24)) %></span>%
                </div>
                <div class="platform_gray fr">
                	<div class="f12 pt50">
                        <span class="hover_tips">
                            <div class="hover_tips_con">
                                <div class="arrow"></div>
                                <div class="border">
									最大10户借款余额占比=∑最大10户待收本金/借款未还款总额
                                </div>
                            </div>
                        </span>
                        	最大10户借款余额占比
                    </div>
                    <span class="f28 gray3"><%=Formater.formatAmount(prce.maxTenUsersLoanBalancePropertion.add(t6196.F25))%></span>%
                </div>
            </div>
            <div class="clearfix mt10">
            	<div class="platform_gray fl mr10">
                	<div class="f12 pt50">
                        <span class="hover_tips">
                            <div class="hover_tips_con">
                                <div class="arrow"></div>
                                <div class="border">
									借款逾期金额为累计逾期总额
                                </div>
                            </div>
                        </span>
                        	借款逾期金额
                    </div>
                    <span class="f28 gray3"><%=Formater.formatAmount(t6196.F26.add(prce.loanOverdueBalanceAmount)) %></span>元
                </div>
                <div class="platform_gray fl">
                	<div class="f12 pt50">
                        <span class="hover_tips">
                            <div class="hover_tips_con">
                                <div class="arrow"></div>
                                <div class="border">
									借款逾期率=借款逾期金额/累计成交总额
                                </div>
                            </div>
                        </span>
                        	借款逾期率
                    </div>
                    <span class="f28 gray3"><%=Formater.formatAmount(prce.loanOverdueBalanceRate.add(t6196.F27)) %></span>%
                </div>
                <div class="platform_gray fr">
                	<div class="f12 pt50">
                        <span class="hover_tips">
                            <div class="hover_tips_con">
                                <div class="arrow"></div>
                                <div class="border">
									借款坏账率=借款坏账金额/累计成交总额
                                </div>
                            </div>
                        </span>
                        	借款坏账率
                    </div>
                    <span class="f28 gray3"><%=Formater.formatAmount(prce.loanBadDebtRate.add(t6196.F28))%></span>%
                </div>
            </div>
        </div>
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