<%@page import="com.dimeng.p2p.console.servlets.finance.zjgl.txgl.Txcg"%>
<%@ page import="com.dimeng.p2p.console.servlets.finance.zjgl.pttzgl.PttzglList" %>
<%@page import="com.dimeng.p2p.console.servlets.finance.fuyou.dzgl.biddzgl.TbdzList"%>
<%@page import="com.dimeng.p2p.console.servlets.finance.fuyou.dzgl.paymentdzgl.HkdzList" %>
<%@page import="com.dimeng.p2p.console.servlets.finance.fuyou.dzgl.advancedzgl.DfdzList"%>
<%@page import="com.dimeng.p2p.console.servlets.finance.fuyou.dzgl.chargedzgl.CzdzList"%>
<%@page import="com.dimeng.p2p.console.servlets.finance.fuyou.dzgl.withdrawdzgl.TxdzList"%>
<%@page import="com.dimeng.p2p.console.servlets.finance.fuyou.dzgl.zqzrdzgl.ZqzrdzList"%>
<%@page import="com.dimeng.p2p.console.servlets.finance.fuyou.dzgl.userbalance.UserList"%>
<%@page import="com.dimeng.p2p.console.servlets.finance.fuyou.dzgl.gybdzgl.GybdzList"%>
<%@page import="com.dimeng.p2p.console.servlets.finance.fuyou.dzgl.mallbuydzgl.MallBuydzList"%>
<%@page import="com.dimeng.p2p.console.servlets.finance.fuyou.dzgl.mallrefunddzgl.MallRefunddzList"%>
<%@page import="com.dimeng.p2p.console.servlets.finance.fuyou.dzgl.badclaimdzgl.BadclaimList"%>
<%@page import="com.dimeng.p2p.console.servlets.finance.fuyou.zjdjgl.freeze.FreezeView"%>
<%@page import="com.dimeng.p2p.console.servlets.finance.zjgl.fkcjjl.CjRecordList"%>
<%@page import="com.dimeng.p2p.console.servlets.finance.ddgl.orderexception.OrderExceptionList"%>
<%@page import="com.dimeng.p2p.console.servlets.finance.ddgl.order.OrderList"%>
<%@page import="com.dimeng.p2p.console.servlets.finance.czgl.xxczgl.XxczglList"%>
<%@page import="com.dimeng.p2p.console.servlets.finance.ptfxbyjgl.DfsdzqList"%>
<%@page import="com.dimeng.p2p.console.servlets.finance.jgbyj.JgbyjList"%>
<%@page import="com.dimeng.p2p.console.servlets.finance.ptzjgl.PtzjglList"%>
<%@page import="com.dimeng.p2p.console.servlets.finance.ptfxbyjgl.PtfxbyjglList"%>
<%@page import="com.dimeng.p2p.console.servlets.finance.zjgl.fksh.FkshList"%>
<%@page import="com.dimeng.p2p.console.servlets.finance.yhzjgl.YhzjglList"%>
<%@page import="com.dimeng.p2p.console.servlets.finance.czgl.xsczgl.CzglList"%>
<%@page import="com.dimeng.p2p.console.servlets.finance.zjgl.ZjList"%>
<%@page import="com.dimeng.p2p.console.servlets.finance.zjgl.ByZjList"%>
<%@page import="com.dimeng.p2p.console.servlets.finance.zjgl.yhxygl.XyList"%>
<%@page import="com.dimeng.p2p.console.servlets.finance.zjmx.grzjmx.GrzjDetail"%>
<%@page import="com.dimeng.p2p.console.servlets.finance.zjmx.qyzjmx.QyzjDetail"%>
<%@page import="com.dimeng.p2p.console.servlets.finance.zjmx.jgzjmx.JgzjDetail"%>
<%@page import="com.dimeng.p2p.console.servlets.finance.zjmx.ptzjmx.PtzjDetail"%>
<%@ page import="com.dimeng.p2p.console.servlets.finance.zjgl.dfgl.YqddfList" %>
<%@ page import="com.dimeng.p2p.console.servlets.finance.zjgl.mall.MallRefundList" %>
<%
String FY_CURRENT_SUB_CATEGORY = request.getParameter("CURRENT_SUB_CATEGORY");
%>
<div class="item-subnav-box" data-title="finance">
    <dl>
      <dt class="f20 fb"><i class="icon-i w30 h30 va-middle nav-cwgl-icon2"></i>财务管理</dt>
       <dd><a href="javascript:void(0);" class="click-link item-a" ><span class="a-text fl">资金管理</span><i class="icon-i w30 h30 arrow-down-icon mt5 fr"></i></a>
        <ul>
        
		  <li>
			<%
			    if(dimengSession.isAccessableResource(CzglList.class))
				{
			%>
			<a class="click-link <%if ("CZGL".equals(FY_CURRENT_SUB_CATEGORY)) {%> select-a <%}%>" href="<%=controller.getURI(request, CzglList.class)%>" target="mainFrame">充值管理</a>
			<%
			    }else{
			%>
			<a href="javascript:void(0)" class="disabled">充值管理</a>
			<%
			    }
			%>
		</li>
          <li>
			<%
			    if(dimengSession.isAccessableResource(XxczglList.class))
				{
			%>
			<a class="click-link <%if ("XXCZGL".equals(FY_CURRENT_SUB_CATEGORY)) {%> select-a <%}%>" href="<%=controller.getURI(request, XxczglList.class)%>" target="mainFrame">线下充值管理</a>
			<%
			    }else{
			%>
			<a href="javascript:void(0)" class="disabled">线下充值管理</a>
			<%
			    }
			%>
		</li>
		 <li>
			<%
			    if(dimengSession.isAccessableResource(Txcg.class))
				{
			%>
			<a class="click-link <%if ("TXGL".equals(FY_CURRENT_SUB_CATEGORY)) {%> select-a <%}%>" href="<%=controller.getURI(request, Txcg.class)%>" target="mainFrame">提现管理</a>
			<%
			    }else{
			%>
			<a href="javascript:void(0)" class="disabled">提现管理</a>
			<%
			    }
			%>
		</li>
		<li>
			<%
			    if(dimengSession.isAccessableResource(FkshList.class))
				{
			%>
			<a class="click-link <%if ("FKGL".equals(FY_CURRENT_SUB_CATEGORY)) {%> select-a <%}%>" href="<%=controller.getURI(request, FkshList.class)%>" target="mainFrame">放款管理</a>
			<%
			    }else{
			%>
			<a href="javascript:void(0)" class="disabled">放款管理</a>
			<%
			    }
			%>
		</li>
		<li>
			<%
			    if(dimengSession.isAccessableResource(CjRecordList.class))
				{
			%>
			<a class="click-link <%if ("FKCJJL".equals(FY_CURRENT_SUB_CATEGORY)) {%> select-a <%}%>" href="<%=controller.getURI(request, CjRecordList.class)%>" target="mainFrame">放款成交记录</a>
			<%
			    }else{
			%>
			<a href="javascript:void(0)" class="disabled">放款成交记录</a>
			<%
			    }
			%>
		</li>
<%-- 		<%if(Boolean.parseBoolean(configureProvider.getProperty(SiteSwitchVariable.PT_ADVANCE_SWITCH))){ %>
			<li>
				<%
					if(dimengSession.isAccessableResource(YqddfList.class))
					{
				%>
				<a class="click-link <%if ("DFGL".equals(FY_CURRENT_SUB_CATEGORY)) {%> select-a <%}%>" href="<%=controller.getURI(request, YqddfList.class)%>" target="mainFrame">不良资产管理</a>
				<%
					}else{
				%>
				<a href="javascript:void(0)" class="disabled">不良资产管理</a>
				<%
					}
				%>
			</li>
		<%}%> --%>
		<%-- <%if(Boolean.parseBoolean(configureProvider.getProperty(BadClaimVariavle.IS_BADCLAIM_TRANSFER))){ %>
		<li>
			<%
				if(dimengSession.isAccessableResource("P2P_C_FINANCE_BLZQZRGLLIST"))
				{
			%>
			<a class="click-link <%if ("BLZQZRGL".equals(CURRENT_SUB_CATEGORY)) {%> select-a <%}%>" href="/console/finance/zjgl/blzq/blzqDzrList.htm" target="mainFrame">不良债权转让管理</a>
			<%}else{ %>
			<a href="javascript:void(0)" class="disabled">不良债权转让管理</a>
			<%} %>
		</li>
		<%}%> --%>
		<li>
			<%
			    if(dimengSession.isAccessableResource(GrzjDetail.class))
				{
			%>
			<a class="click-link <%if ("GRZJMX".equals(FY_CURRENT_SUB_CATEGORY)) {%> select-a <%}%>" href="<%=controller.getURI(request, GrzjDetail.class)%>" target="mainFrame">个人资金明细</a>
			<%
			    }else{
			%>
			<a href="javascript:void(0)" class="disabled">个人资金明细</a>
			<%
			    }
			%>
		</li>
		<li>
			<%
			    if(dimengSession.isAccessableResource(QyzjDetail.class))
				{
			%>
			<a class="click-link <%if ("QYZJMX".equals(FY_CURRENT_SUB_CATEGORY)) {%> select-a <%}%>" href="<%=controller.getURI(request, QyzjDetail.class)%>" target="mainFrame">企业资金明细</a>
			<%
			    }else{
			%>
			<a href="javascript:void(0)" class="disabled">企业资金明细</a>
			<%
			    }
			%>
		</li>
		<li>
			<%
			    if(dimengSession.isAccessableResource(JgzjDetail.class))
				{
			%>
			<a class="click-link <%if ("JGZJMX".equals(FY_CURRENT_SUB_CATEGORY)) {%> select-a <%}%>" href="<%=controller.getURI(request, JgzjDetail.class)%>" target="mainFrame">机构资金明细</a>
			<%
			    }else{
			%>
			<a href="javascript:void(0)" class="disabled">机构资金明细</a>
			<%
			    }
			%>
		</li>
		<li>
			<%
			    if(dimengSession.isAccessableResource(PtzjDetail.class))
				{
			%>
			<a class="click-link <%if ("PTZJMX".equals(FY_CURRENT_SUB_CATEGORY)) {%> select-a <%}%>" href="<%=controller.getURI(request, PtzjDetail.class)%>" target="mainFrame">平台资金明细</a>
			<%
			    }else{
			%>
			<a href="javascript:void(0)" class="disabled">平台资金明细</a>
			<%
			    }
			%>
		</li>
		<li>
			<%
			    if(dimengSession.isAccessableResource(PttzglList.class))
				{
			%>
			<a class="click-link <%if ("PTTZGL".equals(FY_CURRENT_SUB_CATEGORY)) {%> select-a <%}%>" href="<%=controller.getURI(request, PttzglList.class)%>" target="mainFrame">平台调账管理</a>
			<%
			    }else{
			%>
			<a href="javascript:void(0)" class="disabled">平台调账管理</a>
			<%
			    }
			%>
		</li>
		<li>
			<%
				if(dimengSession.isAccessableResource(XyList.class))
				{
			%>
			<a class="click-link <%if ("XYGL".equals(FY_CURRENT_SUB_CATEGORY)) {%> select-a <%}%>" href="<%=controller.getURI(request, XyList.class)%>" target="mainFrame">用户信用管理</a>
			<%}else{ %>
			<a href="javascript:void(0)" class="disabled">用户信用管理</a>
			<%} %>
		</li>
			<%-- <li>
				<%
					if(dimengSession.isAccessableResource("P2P_C_FINANCE_DBLLIST"))
					{
				%>
				<a class="click-link <%if ("XYGL".equals(CURRENT_SUB_CATEGORY)) {%> select-a <%}%>" href="/console/finance/zjgl/yhdbgl/dbList.htm" target="mainFrame">用户担保管理</a>
				<%}else{ %>
				<a href="javascript:void(0)" class="disabled">用户担保管理</a>
				<%} %>
			</li>
		<%if(Boolean.parseBoolean(configureProvider.getProperty(MallVariavle.IS_MALL))){%>
			<li>
				<% if(dimengSession.isAccessableResource(MallRefundList.class)) { %>
				<a class="click-link <%if ("SCTKGL".equals(CURRENT_SUB_CATEGORY)) {%> select-a <%}%>" href="<%=controller.getURI(request, MallRefundList.class)%>" target="mainFrame">商城退款管理</a>
				<%}else{ %>
				<a href="javascript:void(0)" class="disabled">商城退款管理</a>
				<%} %>
			</li>
		<%}%> --%>
        </ul>
      </dd>
      <dd><a href="javascript:void(0);" class="click-link item-a" ><span class="a-text fl">对账管理</span><i class="icon-i w30 h30 arrow-down-icon mt5 fr"></i></a>
		<ul>
			<li>
				<%
					if(dimengSession.isAccessableResource(CzdzList.class))
					{
				%>
					<a class="click-link <%if ("CZDZ".equals(FY_CURRENT_SUB_CATEGORY)) {%> select-a <%}%>" href="<%=controller.getURI(request, CzdzList.class)%>" target="mainFrame">充值对账</a>
				<%}else{ %>
					<a href="javascript:void(0)" class="disabled">充值对账</a>
				<%} %>
			</li>
			<li>
				<%
					if(dimengSession.isAccessableResource(TxdzList.class))
					{
				%>
					<a class="click-link <%if ("TXDZ".equals(FY_CURRENT_SUB_CATEGORY)) {%> select-a <%}%>" href="<%=controller.getURI(request, TxdzList.class)%>" target="mainFrame">提现对账</a>
				<%}else{ %>
					<a href="javascript:void(0)" class="disabled">提现对账</a>
				<%} %>
			</li>
			<li>
				<%
					if(dimengSession.isAccessableResource(TbdzList.class))
					{
				%>
					<a class="click-link <%if ("TBDZ".equals(FY_CURRENT_SUB_CATEGORY)) {%> select-a <%}%>" href="<%=controller.getURI(request, TbdzList.class)%>" target="mainFrame">投资对账</a>
				<%}else{ %>
					<a href="javascript:void(0)" class="disabled">投资对账</a>
				<%} %>
			</li>
			 <li>
				<%
					if(dimengSession.isAccessableResource(HkdzList.class))
					{
				%>
					<a class="click-link <%if ("HKDZ".equals(FY_CURRENT_SUB_CATEGORY)) {%> select-a <%}%>" href="<%=controller.getURI(request, HkdzList.class)%>" target="mainFrame">还款对账</a>
				<%}else{ %>
					<a href="javascript:void(0)" class="disabled">还款对账</a>
				<%} %>
			</li>
			<li>
				<%
					if(dimengSession.isAccessableResource(DfdzList.class))
					{
				%>
					<a class="click-link <%if ("DFDZ".equals(FY_CURRENT_SUB_CATEGORY)) {%> select-a <%}%>" href="<%=controller.getURI(request, DfdzList.class)%>" target="mainFrame">垫付对账</a>
				<%}else{ %>
					<a href="javascript:void(0)" class="disabled">垫付对账</a>
				<%} %>
			</li>
			<li>
				<%
					if(dimengSession.isAccessableResource(ZqzrdzList.class))
					{
				%>
					<a class="click-link <%if ("ZQZRDZ".equals(FY_CURRENT_SUB_CATEGORY)) {%> select-a <%}%>" href="<%=controller.getURI(request, ZqzrdzList.class)%>" target="mainFrame">债权转让对账</a>
				<%}else{ %>
					<a href="javascript:void(0)" class="disabled">债权转让对账</a>
				<%} %>
			</li>
			<%-- <li>
				<%
					if(dimengSession.isAccessableResource(BadclaimList.class))
					{
				%>
					<a class="click-link <%if ("BLZQDZ".equals(FY_CURRENT_SUB_CATEGORY)) {%> select-a <%}%>" href="<%=controller.getURI(request, BadclaimList.class)%>" target="mainFrame">不良债权购买对账</a>
				<%}else{ %>
					<a href="javascript:void(0)" class="disabled">不良债权购买对账</a>
				<%} %>
			</li>
			<li>
				<%
					if(dimengSession.isAccessableResource(MallBuydzList.class))
					{
				%>
					<a class="click-link <%if ("SPGMDZ".equals(FY_CURRENT_SUB_CATEGORY)) {%> select-a <%}%>" href="<%=controller.getURI(request, MallBuydzList.class)%>" target="mainFrame">商品购买对账</a>
				<%}else{ %>
					<a href="javascript:void(0)" class="disabled">商品购买对账</a>
				<%} %>
			</li>
			<li>
				<%
					if(dimengSession.isAccessableResource(MallRefunddzList.class))
					{
				%>
					<a class="click-link <%if ("SPTKDZ".equals(FY_CURRENT_SUB_CATEGORY)) {%> select-a <%}%>" href="<%=controller.getURI(request, MallRefunddzList.class)%>" target="mainFrame">商品退款对账</a>
				<%}else{ %>
					<a href="javascript:void(0)" class="disabled">商品退款对账</a>
				<%} %>
			</li> --%>
			<li>
				<%
					if(dimengSession.isAccessableResource(UserList.class))
					{
				%>
					<a class="click-link <%if ("YHYECX".equals(FY_CURRENT_SUB_CATEGORY)) {%> select-a <%}%>" href="<%=controller.getURI(request, UserList.class)%>" target="mainFrame">用户余额查询</a>
				<%}else{ %>
					<a href="javascript:void(0)" class="disabled">用户余额查询</a>
				<%} %>
			</li>
			<li>
				<%//开关判断，为false，则不显示公益标
					if(BooleanParser.parse(configureProvider.getProperty(SiteSwitchVariable.DONATION_BID_SWITCH))){
					if(dimengSession.isAccessableResource(GybdzList.class))
					{
				%>
					<a class="click-link <%if ("GYBDZ".equals(FY_CURRENT_SUB_CATEGORY)) {%> select-a <%}%>" href="<%=controller.getURI(request, GybdzList.class)%>" target="mainFrame">公益捐款对账</a>
				<%}else{ %>
					<a href="javascript:void(0)" class="disabled">公益捐款对账</a>
				<%} }%>
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
			<a class="click-link <%if ("DDGL".equals(FY_CURRENT_SUB_CATEGORY)) {%> select-a <%}%>" href="<%=controller.getURI(request, OrderList.class)%>" target="mainFrame">订单管理</a>
			<%}else{ %>
			<a href="javascript:void(0)" class="disabled">订单管理</a>
			<%} %>
		</li>
		<li>
			<%
				if(dimengSession.isAccessableResource(OrderExceptionList.class))
				{
			%>
			<a class="click-link <%if ("DDYCRZ".equals(FY_CURRENT_SUB_CATEGORY)) {%> select-a <%}%>" href="<%=controller.getURI(request, OrderExceptionList.class)%>" target="mainFrame">订单异常日志</a>
			<%}else{ %>
			<a href="javascript:void(0)" class="disabled">订单异常日志</a>
			<%} %>
		</li>
        </ul>
      </dd>
      <dd><a href="javascript:void(0);" class="click-link item-a" ><span class="a-text fl">托管账户管理</span><i class="icon-i w30 h30 arrow-down-icon mt5 fr"></i></a>
        <ul>
          <li>
			<%
				if(dimengSession.isAccessableResource(FreezeView.class))
				{
			%>
			<a class="click-link <%if ("ZJDJGL".equals(FY_CURRENT_SUB_CATEGORY)) {%> select-a <%}%>" href="<%=controller.getURI(request, FreezeView.class)%>" target="mainFrame">资金冻结管理</a>
			<%}else{ %>
			<a href="javascript:void(0)" class="disabled">资金冻结管理</a>
			<%} %>
		</li>
        </ul>
      </dd>
    </dl>
  </div>
