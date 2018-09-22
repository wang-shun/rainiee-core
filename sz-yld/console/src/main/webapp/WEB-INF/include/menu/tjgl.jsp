<%@page import="com.dimeng.p2p.console.servlets.statistics.tjbb.dftj.DFStatistics"%>
<%@page import="com.dimeng.p2p.console.servlets.statistics.zjtj.jgzctj.JgMoney"%>
<%@page import="com.dimeng.p2p.console.servlets.statistics.zjtj.qyzctj.QyMoney"%>
<%@ page import="com.dimeng.p2p.console.servlets.statistics.tjbb.hktj.RepaymentStatistics" %>
<%@ page import="com.dimeng.p2p.console.servlets.statistics.tjbb.smrztj.RealNameStatistics" %>
<%@ page import="com.dimeng.p2p.console.servlets.statistics.tjbb.tztj.InvestmentList" %>
<%@ page import="com.dimeng.p2p.console.servlets.statistics.tjbb.zqzrtj.TransferCreditorList" %>
<%@ page import="com.dimeng.p2p.console.servlets.statistics.yhtj.ptyhsjtj.UserData" %>
<%@ page import="com.dimeng.p2p.console.servlets.statistics.yhtj.ptzxyhtj.UserOnline" %>
<%@ page import="com.dimeng.p2p.console.servlets.statistics.yhtj.ptyhsxtj.UserProperty" %>
<%@ page import="com.dimeng.p2p.console.servlets.statistics.ywtj.cjtj.Volume" %>
<%@ page import="com.dimeng.p2p.console.servlets.statistics.ywtj.yqtj.Overdue" %>
<%@ page import="com.dimeng.p2p.console.servlets.statistics.zjtj.cztxtj.RechargeWithdraw" %>
<%@ page import="com.dimeng.p2p.console.servlets.statistics.zjtj.fxbzjtj.RiskFunds" %>
<%@ page import="com.dimeng.p2p.console.servlets.statistics.zjtj.grzctj.GrMoney" %>
<%@ page import="com.dimeng.p2p.console.servlets.statistics.zjtj.ptzjtj.Funds" %>
<%@ page import="com.dimeng.p2p.console.servlets.statistics.zjtj.total.ShowFunds" %>
<div class="item-subnav-box" data-title="statistics">
	<dl>
		<dt class="f20 fb"><i class="icon-i w30 h30 va-middle nav-tjgl-icon2"></i>统计管理</dt>
		<dd><a href="javascript:void(0);" class="click-link item-a"><span class="a-text fl">用户统计</span><i
				class="icon-i w30 h30 arrow-down-icon mt5 fr"></i></a>
			<ul>
				<%
						if (dimengSession.isAccessableResource(UserData.class)) {
					%>
					<li><a class="click-link <%if("PTYHSJTJ".equals(CURRENT_SUB_CATEGORY)){%>select-a<%}%>" href="<%=controller.getURI(request, UserData.class)%>" target="mainFrame">平台用户数据统计</a></li>
					<%
						} else {
					%>
					<li><a href="javascript:void(0)" class="disabled">平台用户数据统计</a></li>
					<%
						}
					%>
		            <%
						if (dimengSession.isAccessableResource(UserOnline.class)) {
					%>
					<li><a class="click-link <%if("PTZXYHTJ".equals(CURRENT_SUB_CATEGORY)){%>select-a<%}%>" id="PTZXYHTJ" href="<%=controller.getURI(request, UserOnline.class)%>" target="mainFrame">平台在线用户统计</a></li>
					<%
						} else {
					%>
					<li><a href="javascript:void(0)" class="disabled">平台在线用户统计</a></li>
					<%
						}
					%>
					<%
						if (dimengSession.isAccessableResource(UserProperty.class)) {
					%>
					<li><a class="click-link <%if("PTYHSXTJ".equals(CURRENT_SUB_CATEGORY)){%>select-a<%}%>" href="<%=controller.getURI(request, UserProperty.class)%>" target="mainFrame">平台用户属性统计</a></li>
					<%
						} else {
					%>
					<li><a href="javascript:void(0)" class="disabled">平台用户属性统计</a></li>
					<%
						}
					%>
        </ul>
      </dd>
      <dd><a href="javascript:void(0);" class="click-link item-a" ><span class="a-text fl">资金统计</span><i class="icon-i w30 h30 arrow-down-icon mt5 fr"></i></a>
        <ul>
        <%
						if (dimengSession.isAccessableResource(ShowFunds.class)) {
					%>
					<li><a class="click-link <%if("ZJTJZL".equals(CURRENT_SUB_CATEGORY)){%>select-a<%}%>" href="<%=controller.getURI(request, ShowFunds.class)%>" target="mainFrame">资金统计总览</a></li>
					<%
						} else {
					%>
					<li><a href="javascript:void(0)" class="disabled">资金统计总览</a></li>
					<%
						}
					%>	
					 <%if(dimengSession.isAccessableResource(GrMoney.class)){ %>
	      			<li><a class=" click-link <%if("GRZCTJ".equals(CURRENT_SUB_CATEGORY)){%>select-a<%}%>"  href="<%=controller.getURI(request, GrMoney.class) %>" target="mainFrame">个人资产统计</a></li>
	      			<%}else{ %>
          			<li><a href="javascript:void(0)" class="disabled">个人资产统计</a></li>
          			<%} %>
          			
          			<%if(dimengSession.isAccessableResource(QyMoney.class)){ %>
	      			<li><a class=" click-link <%if("QYZCTJ".equals(CURRENT_SUB_CATEGORY)){%>select-a<%}%>"  href="<%=controller.getURI(request, QyMoney.class) %>" target="mainFrame">企业资产统计</a></li>
	      			<%}else{ %>
          			<li><a href="javascript:void(0)" class="disabled">企业资产统计</a></li>
          			<%} %>
          			
          			<%if(dimengSession.isAccessableResource(JgMoney.class)){ %>
	      			<li><a class=" click-link <%if("JGZCTJ".equals(CURRENT_SUB_CATEGORY)){%>select-a<%}%>"  href="<%=controller.getURI(request, JgMoney.class) %>" target="mainFrame">机构资产统计</a></li>
	      			<%}else{ %>
          			<li><a href="javascript:void(0)" class="disabled">机构资产统计</a></li>
          			<%} %>
          			
					<%
						if (dimengSession.isAccessableResource(Funds.class)) {
					%>
					<li><a class="click-link <%if("PTZJTJ".equals(CURRENT_SUB_CATEGORY)){%>select-a<%}%>" href="<%=controller.getURI(request, Funds.class)%>" target="mainFrame">平台资金统计</a></li>
					<%
						} else {
					%>
					<li><a href="javascript:void(0)" class="disabled">平台资金统计</a></li>
					<%
						}
					%>	
		<%
						if (dimengSession.isAccessableResource(RiskFunds.class)) {
					%>
					<li><a class="click-link <%if("FXBZJTJ".equals(CURRENT_SUB_CATEGORY)){%>select-a<%}%>" href="<%=controller.getURI(request, RiskFunds.class)%>" target="mainFrame">风险保证金统计</a></li>
					<%
						} else {
					%>
					<li><a href="javascript:void(0)" class="disabled">风险保证金统计</a></li>
					<%
						}
					%>	
		<%
						if (dimengSession.isAccessableResource(RechargeWithdraw.class)) {
					%>
					<li><a class="click-link <%if("CZTXSJTJ".equals(CURRENT_SUB_CATEGORY)){%>select-a<%}%>" href="<%=controller.getURI(request, RechargeWithdraw.class)%>" target="mainFrame">充值提现数据统计</a></li>
					<%
						} else {
					%>
					<li><a href="javascript:void(0)" class="disabled">充值提现数据统计</a></li>
					<%
						}
					%>	
        </ul>
      </dd>
      <dd><a href="javascript:void(0);" class="click-link item-a" ><span class="a-text fl">业务统计</span><i class="icon-i w30 h30 arrow-down-icon mt5 fr"></i></a>
        <ul>
       			<%
					if (dimengSession.isAccessableResource(Volume.class)) {
				%>
				<li><a class="click-link <%if("CJSJTJ".equals(CURRENT_SUB_CATEGORY)){%>select-a<%}%>" href="<%=controller.getURI(request, Volume.class)%>" target="mainFrame">成交数据统计</a></li>
				<%
					} else {
				%>
				<li><a href="javascript:void(0)" class="disabled">成交数据统计</a></li>
				<%
					}
				%>	
				<%
					if (dimengSession.isAccessableResource(Overdue.class)) {
				%>
				<li><a class="click-link <%if("YQSJTJ".equals(CURRENT_SUB_CATEGORY)){%>select-a<%}%>" href="<%=controller.getURI(request, Overdue.class)%>" target="mainFrame">逾期数据统计</a></li>
				<%
					} else {
				%>
				<li><a href="javascript:void(0)" class="disabled">逾期数据统计</a></li>
				<%
					}
				%>	
				<%
				boolean has_business = Boolean.parseBoolean(configureProvider.getProperty(BusinessVariavle.IS_BUSINESS));
				if(has_business){
					if (dimengSession.isAccessableResource("P2P_C_STATISTICS_BUSINESS")) {
				%>
				<li><a class="click-link <%if("YWYYJTJ".equals(CURRENT_SUB_CATEGORY)){%>select-a<%}%>" href="/console/statistics/ywtj/ywytj/businessStatistics.htm" target="mainFrame">业务员业绩统计</a></li>
				<%
					} else {
				%>
				<li><a href="javascript:void(0)" class="disabled">业务员业绩统计</a></li>
				<%
					}
				%>
				<%
					if (dimengSession.isAccessableResource("P2P_C_STATISTICS_BUSINESSDETAILS")) {
				%>
				<li><a class="click-link <%if("YWYYJMX".equals(CURRENT_SUB_CATEGORY)){%>select-a<%}%>" href="/console/statistics/ywtj/ywymxtj/businessDetailsStatistics.htm" target="mainFrame">业务员业绩明细</a></li>
				<%
					} else {
				%>
				<li><a href="javascript:void(0)" class="disabled">业务员业绩明细</a></li>
				<%
					}}
				%>		
        </ul>
      </dd>
       <dd><a href="javascript:void(0);" class="click-link item-a" ><span class="a-text fl">统计报表</span><i class="icon-i w30 h30 arrow-down-icon mt5 fr"></i></a>
        <ul>
                    <%
						if (dimengSession.isAccessableResource(InvestmentList.class)) {
					%>
					<li><a class="click-link <%if("TZTJB".equals(CURRENT_SUB_CATEGORY)){%>select-a<%}%>" href="<%=controller.getURI(request, InvestmentList.class)%>" target="mainFrame">投资统计表</a></li>
					<%
						} else {
					%>
					<li><a href="javascript:void(0)" class="disabled">投资统计表</a></li>
					<%
						}
					%>	
		            <%
						if (dimengSession.isAccessableResource(RepaymentStatistics.class)) {
					%>
					<li><a class="click-link <%if("HKTJB".equals(CURRENT_SUB_CATEGORY)){%>select-a<%}%>" href="<%=controller.getURI(request, RepaymentStatistics.class)%>" target="mainFrame">还款统计表</a></li>
					<%
						} else {
					%>
					<li><a href="javascript:void(0)" class="disabled">还款统计表</a></li>
					<%
						}
					%>
		            <%
						if (dimengSession.isAccessableResource(TransferCreditorList.class)) {
					%>
					<li><a class="click-link <%if("ZQZRTJB".equals(CURRENT_SUB_CATEGORY)){%>select-a<%}%>" href="<%=controller.getURI(request, TransferCreditorList.class)%>" target="mainFrame">债权转让统计表</a></li>
					<%
						} else {
					%>
					<li><a href="javascript:void(0)" class="disabled">债权转让统计表</a></li>
					<%
						}
					%>
				   <%
						if (dimengSession.isAccessableResource(DFStatistics.class)) {
					%>
					<li><a class="click-link <%if("JGDFTJB".equals(CURRENT_SUB_CATEGORY)){%>select-a<%}%>" href="<%=controller.getURI(request, DFStatistics.class)%>" target="mainFrame">垫付统计表</a></li>
					<%
						} else {
					%>
					<li><a href="javascript:void(0)" class="disabled">垫付统计表</a></li>
					<% } %>
					
					<%if(Boolean.parseBoolean(configureProvider.getProperty(BadClaimVariavle.IS_BADCLAIM_TRANSFER))){
					    if(dimengSession.isAccessableResource("P2P_C_FINANCE_BLZCCLTJBLIST")) {
					%>
					<li><a class="click-link <%if("BLZCCLTJB".equals(CURRENT_SUB_CATEGORY)){%>select-a<%}%>" href="/console/statistics/tjbb/blzccltjb/blzcclList.htm" target="mainFrame">不良资产处理统计表</a></li>
					<%
						} else {
					%>
					<li><a href="javascript:void(0)" class="disabled">不良资产处理统计表</a></li>
					<% }	} %>
					
					<%if(Boolean.parseBoolean(configureProvider.getProperty(RealNameAuthVarivale.IS_REALNAMEAUTH))){
						if (dimengSession.isAccessableResource(RealNameStatistics.class)) {
					%>
					<li><a class="click-link <%if("SMRZTJB".equals(CURRENT_SUB_CATEGORY)){%>select-a<%}%>" href="<%=controller.getURI(request, RealNameStatistics.class)%>" target="mainFrame">实名认证统计表</a></li>
					<%} else {%>
					<li><a href="javascript:void(0)" class="disabled">实名认证统计表</a></li>
					<%}}%>	
        </ul>
      </dd>
    </dl>
  </div>
 