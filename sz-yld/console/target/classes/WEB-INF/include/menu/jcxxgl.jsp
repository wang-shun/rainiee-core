<%@page import="com.dimeng.p2p.console.servlets.base.bussettings.bfjlx.BfjlxList"%>
<%@page import="com.dimeng.p2p.console.servlets.base.bussettings.blx.BlxList"%>
<%@page import="com.dimeng.p2p.console.servlets.base.bussettings.product.SearchProduct" %>
<%@page import="com.dimeng.p2p.console.servlets.base.bussettings.ptdf.UpdatePtdfType" %>
<%@page import="com.dimeng.p2p.console.servlets.base.bussettings.xyrzx.XyrzxList" %>
<%@page import="com.dimeng.p2p.console.servlets.base.optsettings.bank.SearchBank" %>
<%@page import="com.dimeng.p2p.console.servlets.base.optsettings.constant.ConstantList" %>
<%@page import="com.dimeng.p2p.console.servlets.base.optsettings.czsm.ChargeExplain" %>
<%@page import="com.dimeng.p2p.console.servlets.base.optsettings.filterSettings.FilterSettings" %>
<%@page import="com.dimeng.p2p.console.servlets.base.optsettings.jylx.JylxList" %>
<%@page import="com.dimeng.p2p.console.servlets.base.optsettings.ptlogo.PtglList" %>
<%@page import="com.dimeng.p2p.console.servlets.base.optsettings.region.RegionList" %>
<%@ page import="com.dimeng.p2p.console.servlets.base.optsettings.repayment.RepaymentSet" %>
<%@ page import="com.dimeng.p2p.console.servlets.base.optsettings.task.TaskList" %>
<%@page import="com.dimeng.p2p.console.servlets.base.optsettings.thirdstat.ThirdStat"%>
<%@page import="com.dimeng.p2p.console.servlets.base.optsettings.txsm.WithDrawExplain"%>
<%@page import="com.dimeng.p2p.console.servlets.base.optsettings.xymb.XymbList"%>
<%@page import="com.dimeng.p2p.console.servlets.base.optsettings.dxz.DxzList"%>


<div class="item-subnav-box" data-title="basics">
	<dl>
		<dt class="f20 fb"><i class="icon-i w30 h30 va-middle nav-jbsz-icon2"></i>基本设置</dt>
		<dd><a href="javascript:void(0);" class="click-link item-a"><span class="a-text fl">业务设置</span><i
				class="icon-i w30 h30 arrow-down-icon mt5 fr"></i></a>
			<ul>
				<%
						if (dimengSession.isAccessableResource(XyrzxList.class)) {
					%>
					<li><a class="click-link <%if("XYRZX".equals(CURRENT_SUB_CATEGORY)){%>select-a<%}%>" href="<%=controller.getURI(request, XyrzxList.class)%>" target="mainFrame">信用认证项管理</a></li>
					<%
						} else {
					%>
					<li><a href="javascript:void(0)" class="disabled">信用认证项管理</a></li>
					<%
						}
					%>	
		 <%
						if (dimengSession.isAccessableResource(SearchProduct.class)) {
					%>
					<li><a class="click-link <%if("CPSZ".equals(CURRENT_SUB_CATEGORY)){%>select-a<%}%>" id="CPSZ" href="<%=controller.getURI(request, SearchProduct.class)%>" target="mainFrame">标产品管理</a></li>
					<%
						} else {
					%>
					<li><a href="javascript:void(0)" class="disabled">标产品管理</a></li>
					<%
						}
					%>	
					 <%
                        if (dimengSession.isAccessableResource(BlxList.class))
                        {
                    %>
                    <li><a class="click-link <%
                        if ("BLX".equals(CURRENT_SUB_CATEGORY))
                        {%>select-a<%}%>"  href="<%=controller.getURI(request, BlxList.class)%>" target="mainFrame">标类型管理</a></li>
                    <%
                    }
                    else
                    {
                    %>
                    <li><a href="javascript:void(0)" class="disabled">标类型管理</a></li>
                    <%
                        }
                    %>
		<%
						if (dimengSession.isAccessableResource(BfjlxList.class)) {
					%>
					<li><a class="click-link <%if("BFJLX".equals(CURRENT_SUB_CATEGORY)){%>select-a<%}%>" href="<%=controller.getURI(request, BfjlxList.class)%>" target="mainFrame">标附件类型管理</a></li>
					<%
						} else {
					%>
					<li><a href="javascript:void(0)" class="disabled">标附件类型管理</a></li>
					<%
						}
					%>
<%-- 		<%
						if (dimengSession.isAccessableResource(DywlxList.class)) {
					%>
					<li><a class="click-link <%if("DYWLX".equals(CURRENT_SUB_CATEGORY)){%>select-a<%}%>" href="<%=controller.getURI(request, DywlxList.class)%>">抵押物属性管理</a></li>
					<%
						} else {
					%>
					<li><a href="javascript:void(0)" class="disabled">抵押物属性管理</a></li>
					<%
						}
					%>	 --%>
				    <%--Bug #55510【后台-基本设置-业务设置】平台垫付都去掉了，这个“不良资产处理方案设置”应该也去掉吧，跟原平台垫付相关的权限也要去掉
				    <%if(Boolean.parseBoolean(configureProvider.getProperty(SiteSwitchVariable.PT_ADVANCE_SWITCH))){
						if (dimengSession.isAccessableResource(UpdatePtdfType.class)) {
					%>
					<li><a class="click-link <%if("PTDFGL".equals(CURRENT_SUB_CATEGORY)){%>select-a<%}%>" href="<%=controller.getURI(request, UpdatePtdfType.class)%>" target="mainFrame">不良资产处理方案设置</a></li>
					<%
						} else {
					%>
					<li><a href="javascript:void(0)" class="disabled">不良资产处理方案设置</a></li>
					<%
						}}
					%>	--%>
        
        </ul>
      </dd>
      <dd><a href="javascript:void(0);" class="click-link item-a" ><span class="a-text fl">运营设置</span><i class="icon-i w30 h30 arrow-down-icon mt5 fr"></i></a>
        <ul>
        <%
						if (dimengSession.isAccessableResource(JylxList.class)) {
					%>
					<li><a class="click-link <%if("JYLX".equals(CURRENT_SUB_CATEGORY)){%>select-a<%}%>" href="<%=controller.getURI(request, JylxList.class)%>" target="mainFrame">交易类型管理</a></li>
					<%
						} else {
					%>
					<li><a href="javascript:void(0)" class="disabled">交易类型管理</a></li>
					<%
						}
					%>
		<%
						if (dimengSession.isAccessableResource(RegionList.class)) {
					%>
					<li><a class="click-link <%if("QYGL".equals(CURRENT_SUB_CATEGORY)){%>select-a<%}%>" href="<%=controller.getURI(request, RegionList.class)%>" target="mainFrame">区域管理</a></li>
					<%
						} else {
					%>
					<li><a href="javascript:void(0)" class="disabled">区域管理</a></li>
					<%
						}
					%>
		<%
						if (dimengSession.isAccessableResource(SearchBank.class)) {
					%>
					<li><a class="click-link <%if("YHSZ".equals(CURRENT_SUB_CATEGORY)){%>select-a<%}%>" href="<%=controller.getURI(request, SearchBank.class)%>" target="mainFrame">银行设置</a></li>
					<%
						} else {
					%>
					<li><a href="javascript:void(0)" class="disabled">银行设置</a></li>
					<%
						}
					%>
		<%
						if (dimengSession.isAccessableResource(XymbList.class)) {
					%>
					<li><a class="click-link <%if("XYMBSZ".equals(CURRENT_SUB_CATEGORY)){%>select-a<%}%>" href="<%=controller.getURI(request, XymbList.class)%>" target="mainFrame">协议模板设置</a></li>
					<%
						} else {
					%>
					<li><a href="javascript:void(0)" class="disabled">协议模板设置</a></li>
					<%
						}
					%>
		<%
						if (dimengSession.isAccessableResource(PtglList.class)) {
					%>
					<li><a class="click-link <%if("PT".equals(CURRENT_SUB_CATEGORY)){%>select-a<%}%>" href="<%=controller.getURI(request, PtglList.class)%>" target="mainFrame">平台图标管理</a></li>
					<%
						} else {
					%>
					<li><a href="javascript:void(0)" class="disabled">平台图标管理</a></li>
					<%
						}
					%>
		<%
						if (dimengSession.isAccessableResource(ConstantList.class)) {
					%>
					<li><a class="click-link <%if("PTCLSZ".equals(CURRENT_SUB_CATEGORY)){%>select-a<%}%>" href="<%=controller.getURI(request, ConstantList.class)%>" target="mainFrame">平台常量管理</a></li>
					<%
						} else {
					%>
					<li><a href="javascript:void(0)" class="disabled">平台常量管理</a></li>
					<%
						}
					%>
		<%
						if (dimengSession.isAccessableResource(RepaymentSet.class)) {
					%>
					<li><a class="click-link <%if("ZDTXSZ".equals(CURRENT_SUB_CATEGORY)){%>select-a<%}%>" href="<%=controller.getURI(request, RepaymentSet.class)%>" target="mainFrame">账单提醒设置</a></li>
					<%
						} else {
					%>
					<li><a href="javascript:void(0)" class="disabled">账单提醒设置</a></li>
					<%
						}
					%>
		<%
						if (dimengSession.isAccessableResource(TaskList.class)) {
					%>
					<li><a class="click-link <%if("DSRWGL".equals(CURRENT_SUB_CATEGORY)){%>select-a<%}%>" href="<%=controller.getURI(request, TaskList.class)%>" target="mainFrame">定时任务管理</a></li>
					<%
						} else {
					%>
					<li><a href="javascript:void(0)" class="disabled">定时任务管理</a></li>
					<%
						}
					%>
		<%
						if (dimengSession.isAccessableResource(ThirdStat.class)) {
					%>
					<li><a class="click-link <%if("DSFTJSZ".equals(CURRENT_SUB_CATEGORY)){%>select-a<%}%>" href="<%=controller.getURI(request, ThirdStat.class)%>" target="mainFrame">第三方统计设置</a></li>
					<%
						} else {
					%>
					<li><a href="javascript:void(0)" class="disabled">第三方统计设置</a></li>
					<%
						}
					%>
		<%
						if (dimengSession.isAccessableResource(ChargeExplain.class)) {
					%>
					<li><a class="click-link <%if("CZSMGL".equals(CURRENT_SUB_CATEGORY)){%>select-a<%}%>" href="<%=controller.getURI(request, ChargeExplain.class)%>" target="mainFrame">充值说明管理</a></li>
					<%
						} else {
					%>
					<li><a href="javascript:void(0)" class="disabled">充值说明管理</a></li>
					<%
						}
					%>
		<%
						if (dimengSession.isAccessableResource(WithDrawExplain.class)) {
					%>
					<li><a class="click-link <%if("TXSMGL".equals(CURRENT_SUB_CATEGORY)){%>select-a<%}%>" href="<%=controller.getURI(request, WithDrawExplain.class)%>" target="mainFrame">提现说明管理</a></li>
					<%
						} else {
					%>
					<li><a href="javascript:void(0)" class="disabled">提现说明管理</a></li>
					<%
						}
					%>
					<%
						if (dimengSession.isAccessableResource(FilterSettings.class)) {
					%>
					<li><a class="click-link <%if("SXTJSZ".equals(CURRENT_SUB_CATEGORY)){%>select-a<%}%>" href="<%=controller.getURI(request, FilterSettings.class)%>" target="mainFrame">筛选条件设置</a></li>
					<%
						} else {
					%>
					<li><a href="javascript:void(0)" class="disabled">筛选条件设置</a></li>
					<%
						}
					%>
					
					<%-- <%
						if (dimengSession.isAccessableResource(DxzList.class)) {
					%>
					<li><a class="click-link <%if("DXZ".equals(CURRENT_SUB_CATEGORY)){%>select-a<%}%>" href="<%=controller.getURI(request, DxzList.class)%>" target="mainFrame">定向组管理</a></li>
					<%
						} else {
					%>
					<li><a href="javascript:void(0)" class="disabled">定向组管理</a></li>
					<%
						}
					%> --%>

			<%
				if(Boolean.parseBoolean(configureProvider.getProperty(RegulatoryPolicyVariavle.IS_OPEN_RISK_ASSESS))){
				if (dimengSession.isAccessableResource("P2P_C_BASE_UPDATERISKSET")) {
			%>
			<li><a class="click-link <%if("FXPGSZ".equals(CURRENT_SUB_CATEGORY)){%>select-a<%}%>" href="/console/base/optsettings/policyset/updateRiskSet.htm" target="mainFrame">风险评估设置</a></li>
			<%
			} else {
			%>
			<li><a href="javascript:void(0)" class="disabled">风险评估设置</a></li>
			<%
				}
			%>
			<%
				if (dimengSession.isAccessableResource("P2P_C_BASE_RISK_LIST")) {
			%>
			<li><a class="click-link <%if("FXPGWTSZ".equals(CURRENT_SUB_CATEGORY)){%>select-a<%}%>" href="/console/base/optsettings/policy/riskQuesList.htm" target="mainFrame">风险评估问题设置</a></li>
			<%
			} else {
			%>
			<li><a href="javascript:void(0)" class="disabled">风险评估问题设置</a></li>
			<%
				}}
			%>
        </ul>
      </dd>
      <%
     boolean is_mall_ =  Boolean.parseBoolean(configureProvider.getProperty(MallVariavle.IS_MALL));
          if(is_mall_){
	  %>
      <dd><a href="javascript:void(0);" class="click-link item-a" ><span class="a-text fl">商城运营设置</span><i class="icon-i w30 h30 arrow-down-icon mt5 fr"></i></a>
        <ul>
        <%
						if (dimengSession.isAccessableResource("P2P_C_BASE_VIEWSETSCORE")) {
					%>
					<li><a class="click-link <%if("JFSZ".equals(CURRENT_SUB_CATEGORY)){%>select-a<%}%>" href="/console/base/malloptsettings/scoreset/viewSetScore.htm" target="mainFrame">积分设置</a></li>
					<%
						} else {
					%>
					<li><a href="javascript:void(0)" class="disabled">积分设置</a></li>
					<%
						}
					%>	
		 <%
						if (dimengSession.isAccessableResource("P2P_C_BASE_SCORECLEANZEROSET")) {
					%>
					<li><a class="click-link <%if("JFQLSZ".equals(CURRENT_SUB_CATEGORY)){%>select-a<%}%>" href="/console/base/malloptsettings/scorecleanset/scoreCleanZeroSet.htm" target="mainFrame">积分清零设置</a></li>
					<%
						} else {
					%>
					<li><a href="javascript:void(0)" class="disabled">积分清零设置</a></li>
					<%
						}
					%>	
					 <%
                        if (dimengSession.isAccessableResource("P2P_C_BASE_EXPLAINSCORERULE"))
                        {
                    %>
                    <li><a class="click-link <%
                        if ("JFGZ".equals(CURRENT_SUB_CATEGORY))
                        {%>select-a<%}%>"  href="/console/base/malloptsettings/scorerule/explainScoreRule.htm" target="mainFrame">积分规则说明</a></li>
                    <%
                    }
                    else
                    {
                    %>
                    <li><a href="javascript:void(0)" class="disabled">积分规则说明</a></li>
                    <%
                        }
                    %>
		<%
						if (dimengSession.isAccessableResource("P2P_C_BASE_COMMODITYCATEGORY")) {
					%>
					<li><a class="click-link <%if("SPLBSZ".equals(CURRENT_SUB_CATEGORY)){%>select-a<%}%>" href="/console/base/malloptsettings/category/commodityCategory.htm" target="mainFrame">商品类别设置</a></li>
					<%
						} else {
					%>
					<li><a href="javascript:void(0)" class="disabled">商品类别设置</a></li>
					<%
						}
					%>
		<%
						if (dimengSession.isAccessableResource("P2P_C_BASE_VIEWSETCONDITION")) {
					%>
					<li><a class="click-link <%if("SCSXTJSZ".equals(CURRENT_SUB_CATEGORY)){%>select-a<%}%>" href="/console/base/malloptsettings/condition/viewSetCondition.htm" target="mainFrame">商城筛选条件设置</a></li>
					<%
						} else {
					%>
					<li><a href="javascript:void(0)" class="disabled">商城筛选条件设置</a></li>
					<%
						}
					%>	
        </ul>
      </dd>
      <%
	      }
	  %>
    </dl>
  </div>
