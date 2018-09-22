<%@page import="com.dimeng.p2p.console.servlets.finance.zjgl.fkcjjl.CjRecordList"%>
<%@page import="com.dimeng.p2p.console.servlets.finance.ddgl.orderexception.OrderExceptionList"%>
<%@page import="com.dimeng.p2p.console.servlets.finance.ddgl.order.OrderList"%>
<%@page import="com.dimeng.p2p.console.servlets.finance.czgl.xxczgl.XxczglList"%>
<%@page import="com.dimeng.p2p.console.servlets.finance.zjgl.fksh.FkshList"%>
<%@page import="com.dimeng.p2p.console.servlets.finance.txgl.wsh.TxglList"%>

<%@page import="com.dimeng.p2p.console.servlets.finance.zjgl.blzq.BlzqDshList"%>
<%@page import="com.dimeng.p2p.console.servlets.finance.zjgl.blzq.BlzqZrsbList"%>
<%@page import="com.dimeng.p2p.console.servlets.finance.zjgl.blzq.BlzqYzrList"%>
<%@page import="com.dimeng.p2p.console.servlets.finance.zjgl.blzq.BlzqZrzList"%>
<%@page import="com.dimeng.p2p.console.servlets.finance.zjgl.blzq.BlzqDzrList"%>

<%@page import="com.dimeng.p2p.console.servlets.finance.txgl.shtg.Shtg"%>
<%@page import="com.dimeng.p2p.console.servlets.finance.txgl.txcg.Txcg"%>
<%@page import="com.dimeng.p2p.console.servlets.finance.txgl.txsb.Txsb"%>
<%@page import="com.dimeng.p2p.console.servlets.finance.czgl.xsczgl.CzglList"%>
<%@page import="com.dimeng.p2p.console.servlets.finance.zjgl.yhxygl.XyList"%>
<%@page import="com.dimeng.p2p.console.servlets.finance.zjmx.grzjmx.GrzjDetail"%>
<%@page import="com.dimeng.p2p.console.servlets.finance.zjmx.qyzjmx.QyzjDetail"%>
<%@page import="com.dimeng.p2p.console.servlets.finance.zjmx.jgzjmx.JgzjDetail"%>
<%@page import="com.dimeng.p2p.console.servlets.finance.zjmx.ptzjmx.PtzjDetail"%>
<%@ page import="com.dimeng.p2p.console.servlets.finance.zjgl.dfgl.YqddfList" %>
<%@ page import="com.dimeng.p2p.console.servlets.finance.zjgl.pttzgl.PttzglList" %>
<%@ page import="com.dimeng.p2p.console.servlets.finance.ptyhkgl.PtyhkglList" %>
<div class="item-subnav-box" data-title="finance">
    <dl>
      <dt class="f20 fb"><i class="icon-i w30 h30 va-middle nav-cwgl-icon2"></i>财务管理</dt>
       <dd><a href="javascript:void(0);" class="click-link item-a" ><span class="a-text fl">充值管理</span><i class="icon-i w30 h30 arrow-down-icon mt5 fr"></i></a>
       	<ul>
       	<li>
			<%
				if(dimengSession.isAccessableResource(CzglList.class))
				{
			%>
			<a class="click-link <%if ("CZGL".equals(CURRENT_SUB_CATEGORY)) {%> select-a <%}%>" href="<%=controller.getURI(request, CzglList.class)%>" target="mainFrame">线上充值管理</a>
			<%}else{ %>
			<a href="javascript:void(0)" class="disabled">线上充值管理</a>
			<%} %>
		</li>
          <li>
			<%
				if(dimengSession.isAccessableResource(XxczglList.class))
				{
			%>
			<a class="click-link <%if ("XXCZGL".equals(CURRENT_SUB_CATEGORY)) {%> select-a <%}%>" id="XXCZGL" href="<%=controller.getURI(request, XxczglList.class)%>" target="mainFrame">线下充值管理</a>
			<%}else{ %>
			<a href="javascript:void(0)" class="disabled">线下充值管理</a>
			<%} %>
		</li>
       	</ul>
       </dd>
       <dd><a href="javascript:void(0);" class="click-link item-a" ><span class="a-text fl">提现管理</span><i class="icon-i w30 h30 arrow-down-icon mt5 fr"></i></a>
       	<ul>
		<li>
			<%
				if(dimengSession.isAccessableResource(TxglList.class))
				{
			%>
			<a class="click-link <%if ("TXGL".equals(CURRENT_SUB_CATEGORY)) {%> select-a <%}%>" id="TXGL" href="<%=controller.getURI(request, TxglList.class)%>" target="mainFrame">未审核</a>
			<%}else{ %>
			<a href="javascript:void(0)" class="disabled">未审核</a>
			<%} %>
		</li>
		<li>
			<%
				if(dimengSession.isAccessableResource(Shtg.class))
				{
			%>
			<a class="click-link <%if ("SHTG".equals(CURRENT_SUB_CATEGORY)) {%> select-a <%}%>" id="SHTG" href="<%=controller.getURI(request, Shtg.class)%>" target="mainFrame">审核通过</a>
			<%}else{ %>
			<a href="javascript:void(0)" class="disabled">审核通过</a>
			<%} %>
		</li>
		<li>
			<%
				if(dimengSession.isAccessableResource(Txcg.class))
				{
			%>
			<a class="click-link <%if ("TXCG".equals(CURRENT_SUB_CATEGORY)) {%> select-a <%}%>" id="TXCG" href="<%=controller.getURI(request, Txcg.class)%>" target="mainFrame">提现成功</a>
			<%}else{ %>
			<a href="javascript:void(0)" class="disabled">提现成功</a>
			<%} %>
		</li>
		<li>
			<%
				if(dimengSession.isAccessableResource(Txsb.class))
				{
			%>
			<a class="click-link <%if ("TXSB".equals(CURRENT_SUB_CATEGORY)) {%> select-a <%}%>" id="TXSB" href="<%=controller.getURI(request, Txsb.class)%>" target="mainFrame">提现失败</a>
			<%}else{ %>
			<a href="javascript:void(0)" class="disabled">提现失败</a>
			<%} %>
		</li>
       	</ul>
       </dd>
       <dd><a href="javascript:void(0);" class="click-link item-a" ><span class="a-text fl">放款管理</span><i class="icon-i w30 h30 arrow-down-icon mt5 fr"></i></a>
       	<ul>
		<li>
			<%
				if(dimengSession.isAccessableResource(FkshList.class))
				{
			%>
			<a class="click-link <%if ("FKGL".equals(CURRENT_SUB_CATEGORY)) {%> select-a <%}%>" id="FKGL" href="<%=controller.getURI(request, FkshList.class)%>" target="mainFrame">放款管理</a>
			<%}else{ %>
			<a href="javascript:void(0)" class="disabled">放款管理</a>
			<%} %>
		</li>
		<li>
			<%
				if(dimengSession.isAccessableResource(CjRecordList.class))
				{
			%>
			<a class="click-link <%if ("FKCJJL".equals(CURRENT_SUB_CATEGORY)) {%> select-a <%}%>" href="<%=controller.getURI(request, CjRecordList.class)%>" target="mainFrame">成交记录</a>
			<%}else{ %>
			<a href="javascript:void(0)" class="disabled">成交记录</a>
			<%} %>
		</li>
       	</ul>
       </dd>
       <dd><a href="javascript:void(0);" class="click-link item-a" ><span class="a-text fl">不良资产管理</span><i class="icon-i w30 h30 arrow-down-icon mt5 fr"></i></a>
       	<ul>
			<%if(Boolean.parseBoolean(configureProvider.getProperty(BadClaimVariavle.IS_BADCLAIM_TRANSFER))){%>
			<li>
				<%
					if(dimengSession.isAccessableResource(BlzqDzrList.class))
					{
				%>
				<a class="click-link <%if ("BLZQZRGL".equals(CURRENT_SUB_CATEGORY)) {%> select-a <%}%>" href="<%=controller.getURI(request, BlzqDzrList.class)%>" target="mainFrame">待转让</a>
				<%}else{ %>
				<a href="javascript:void(0)" class="disabled">待转让</a>
				<%} %>
			</li>
			<li>
				<%
					if(dimengSession.isAccessableResource(BlzqDshList.class))
					{
				%>
				<a class="click-link <%if ("BLZQZRGL".equals(CURRENT_SUB_CATEGORY)) {%> select-a <%}%>" href="<%=controller.getURI(request, BlzqDshList.class)%>" target="mainFrame">待审核</a>
				<%}else{ %>
				<a href="javascript:void(0)" class="disabled">待审核</a>
				<%} %>
			</li>
			<li>
				<%
					if(dimengSession.isAccessableResource(BlzqZrzList.class))
					{
				%>
				<a class="click-link <%if ("BLZQZRGL".equals(CURRENT_SUB_CATEGORY)) {%> select-a <%}%>" href="<%=controller.getURI(request, BlzqZrzList.class)%>" target="mainFrame">转让中</a>
				<%}else{ %>
				<a href="javascript:void(0)" class="disabled">转让中</a>
				<%} %>
			</li>
			<li>
				<%
					if(dimengSession.isAccessableResource(BlzqYzrList.class))
					{
				%>
				<a class="click-link <%if ("BLZQZRGL".equals(CURRENT_SUB_CATEGORY)) {%> select-a <%}%>" href="<%=controller.getURI(request, BlzqYzrList.class)%>" target="mainFrame">已转让</a>
				<%}else{ %>
				<a href="javascript:void(0)" class="disabled">已转让</a>
				<%} %>
			</li>
			<li>
				<%
					if(dimengSession.isAccessableResource(BlzqZrsbList.class))
					{
				%>
				<a class="click-link <%if ("BLZQZRGL".equals(CURRENT_SUB_CATEGORY)) {%> select-a <%}%>" href="<%=controller.getURI(request, BlzqZrsbList.class)%>" target="mainFrame">转让失败</a>
				<%}else{ %>
				<a href="javascript:void(0)" class="disabled">转让失败</a>
				<%} %>
			</li>
			<%}%>
			 <%if(Boolean.parseBoolean(configureProvider.getProperty(SiteSwitchVariable.PT_ADVANCE_SWITCH))){%>
			<li>
				<%
					if(dimengSession.isAccessableResource(YqddfList.class))
					{
				%>
				<a class="click-link <%if ("DFGL".equals(CURRENT_SUB_CATEGORY)) {%> select-a <%}%>" href="<%=controller.getURI(request, YqddfList.class)%>" target="mainFrame">不良资产管理</a>
				<%}else{ %>
				<a href="javascript:void(0)" class="disabled">不良资产管理</a>
				<%} %>
			</li>
			<%}%>
       	</ul>
       </dd>
       <dd><a href="javascript:void(0);" class="click-link item-a" ><span class="a-text fl">风控管理</span><i class="icon-i w30 h30 arrow-down-icon mt5 fr"></i></a>
       	<ul>
		<li>
			<%
				if(dimengSession.isAccessableResource(XyList.class))
				{
			%>
			<a class="click-link <%if ("XYGL".equals(CURRENT_SUB_CATEGORY)) {%> select-a <%}%>" href="<%=controller.getURI(request, XyList.class)%>" target="mainFrame">用户信用管理</a>
			<%}else{ %>
			<a href="javascript:void(0)" class="disabled">用户信用管理</a>
			<%} %>
		</li>
			<%-- <%if(BooleanParser.parse(configureProvider.getProperty(GuarantorVariavle.IS_HAS_GUARANTOR))){%>
			<li>
				<%
					if(dimengSession.isAccessableResource("P2P_C_FINANCE_DBLLIST"))
					{
				%>
				<a class="click-link <%if ("XYGL".equals(CURRENT_SUB_CATEGORY)) {%> select-a <%}%>" href="/console/finance/zjgl/yhdbgl/dbList.htm" target="mainFrame">用户担保管理</a>
				<%}else{ %>
				<a href="javascript:void(0)" class="disabled">用户担保管理</a>
				<%} %>
			</li>
			<%}%> --%>
       	</ul>
       </dd>
       <dd><a href="javascript:void(0);" class="click-link item-a" ><span class="a-text fl">平台资金管理</span><i class="icon-i w30 h30 arrow-down-icon mt5 fr"></i></a>
        <ul>
			<%if(Boolean.parseBoolean(configureProvider.getProperty(MallVariavle.IS_MALL))){%>
			<li>
				<% if(dimengSession.isAccessableResource("P2P_C_FINANCE_MALLREFUNDLIST") ) { %>
				<a class="click-link <%if ("SCTKGL".equals(CURRENT_SUB_CATEGORY)) {%> select-a <%}%>" href="/console/finance/zjgl/mall/mallRefundList.htm" target="mainFrame">商城退款管理</a>
				<%}else{ %>
				<a href="javascript:void(0)" class="disabled">商城退款管理</a>
				<%} %>
			</li>
			<%}%>
		<li>
			<%
				if(dimengSession.isAccessableResource(PttzglList.class))
					{
			%>
			<a class="click-link <%if ("PTTZGL".equals(CURRENT_SUB_CATEGORY)) {%> select-a <%}%>" href="<%=controller.getURI(request, PttzglList.class)%>" target="mainFrame">平台调账管理</a>
			<%}else{ %>
			<a href="javascript:void(0)" class="disabled">平台调账管理</a>
			<%} %>
		</li>
		<%	Boolean cwzjtg= BooleanParser.parseObject(configureProvider.getProperty(SystemVariable.SFZJTG));
			if(cwzjtg){ %>
		<li>
			<%
				if(dimengSession.isAccessableResource(PtyhkglList.class))
					{
			%>
			<a class="click-link <%if ("PTYHKGL".equals(CURRENT_SUB_CATEGORY)) {%> select-a <%}%>" href="<%=controller.getURI(request, PtyhkglList.class)%>" target="mainFrame">平台银行卡管理</a>
			<%}else{ %>
			<a href="javascript:void(0)" class="disabled">平台银行卡管理</a>
			<%} %>
		</li>	
		<%} %>
        </ul>
      </dd>
      <dd><a href="javascript:void(0);" class="click-link item-a" ><span class="a-text fl">资金明细</span><i class="icon-i w30 h30 arrow-down-icon mt5 fr"></i></a>
      	<ul>
      		<li>
			<%
				if(dimengSession.isAccessableResource(GrzjDetail.class))
				{
			%>
			<a class="click-link <%if ("GRZJMX".equals(CURRENT_SUB_CATEGORY)) {%> select-a <%}%>" href="<%=controller.getURI(request, GrzjDetail.class)%>" target="mainFrame">个人资金明细</a>
			<%}else{ %>
			<a href="javascript:void(0)" class="disabled">个人资金明细</a>
			<%} %>
		</li>
		<li>
			<%
				if(dimengSession.isAccessableResource(QyzjDetail.class))
				{
			%>
			<a class="click-link <%if ("QYZJMX".equals(CURRENT_SUB_CATEGORY)) {%> select-a <%}%>" href="<%=controller.getURI(request, QyzjDetail.class)%>" target="mainFrame">企业资金明细</a>
			<%}else{ %>
			<a href="javascript:void(0)" class="disabled">企业资金明细</a>
			<%} %>
		</li>
		<li>
			<%
				if(dimengSession.isAccessableResource(JgzjDetail.class))
				{
			%>
			<a class="click-link <%if ("JGZJMX".equals(CURRENT_SUB_CATEGORY)) {%> select-a <%}%>" href="<%=controller.getURI(request, JgzjDetail.class)%>" target="mainFrame">机构资金明细</a>
			<%}else{ %>
			<a href="javascript:void(0)" class="disabled">机构资金明细</a>
			<%} %>
		</li>
		<li>
			<%
				if(dimengSession.isAccessableResource(PtzjDetail.class))
				{
			%>
			<a class="click-link <%if ("PTZJMX".equals(CURRENT_SUB_CATEGORY)) {%> select-a <%}%>" href="<%=controller.getURI(request, PtzjDetail.class)%>" target="mainFrame">平台资金明细</a>
			<%}else{ %>
			<a href="javascript:void(0)" class="disabled">平台资金明细</a>
			<%} %>
		</li>
      	</ul>
      </dd>
      <dd><a href="javascript:void(0);" class="click-link item-a" ><span class="a-text fl">订单管理</span><i class="icon-i w30 h30 arrow-down-icon mt5 fr"></i></a>
        <ul>
          <li>
			<%
				if(dimengSession.isAccessableResource(OrderList.class))
				{
			%>
			<a class="click-link <%if ("DDGL".equals(CURRENT_SUB_CATEGORY)) {%> select-a <%}%>" href="<%=controller.getURI(request, OrderList.class)%>" target="mainFrame">订单管理</a>
			<%}else{ %>
			<a href="javascript:void(0)" class="disabled">订单管理</a>
			<%} %>
		</li>
		<li>
			<%
				if(dimengSession.isAccessableResource(OrderExceptionList.class))
				{
			%>
			<a class="click-link <%if ("DDYCRZ".equals(CURRENT_SUB_CATEGORY)) {%> select-a <%}%>" href="<%=controller.getURI(request, OrderExceptionList.class)%>" target="mainFrame">订单异常日志</a>
			<%}else{ %>
			<a href="javascript:void(0)" class="disabled">订单异常日志</a>
			<%} %>
		</li>
        </ul>
      </dd>
    </dl>
  </div>
