<%@page import="com.dimeng.p2p.console.servlets.mall.jfgl.exchange.ScoreExchangeList"%>
<%@page import="com.dimeng.p2p.console.servlets.mall.jfgl.gain.ScoreGetList"%>
<%@page import="com.dimeng.p2p.console.servlets.mall.jfgl.statistics.ScoreStatisticsList"%>
<%@page import="com.dimeng.p2p.console.servlets.mall.ddgl.dfh.DfhScoreOrderList"%>
<%@page import="com.dimeng.p2p.console.servlets.mall.ddgl.dsh.DshScoreOrderList"%>
<%@page import="com.dimeng.p2p.console.servlets.mall.ddgl.orderlist.ScoreOrderList"%>
<%@page import="com.dimeng.p2p.console.servlets.mall.ddgl.wshtg.WshtgScoreOrderList"%>
<%@page import="com.dimeng.p2p.console.servlets.mall.ddgl.yfh.YfhScoreOrderList"%>
<%@page import="com.dimeng.p2p.console.servlets.mall.ddgl.yth.YthScoreOrderList"%>
<%@ page import="com.dimeng.p2p.console.servlets.mall.spgl.goodslist.CommodityList" %>
<div class="item-subnav-box" data-title="mall">
    <dl>
      <dt class="f20 fb"><i class="icon-i w30 h30 va-middle nav-ptsc-icon2"></i>积分商城</dt>
      <dd><a href="javascript:void(0);" class="click-link item-a" ><span class="a-text fl">商品管理</span><i class="icon-i w30 h30 arrow-down-icon mt5 fr"></i></a>
        <ul>
			<%
			    if (dimengSession.isAccessableResource(CommodityList.class)) {
			%>
			<li><a class="click-link <%if("SPLB".equals(CURRENT_SUB_CATEGORY)){%>select-a<%}%>" href="<%=controller.getURI(request, CommodityList.class)%>" target="mainFrame">商品列表</a></li>
			<%
			    } else {
			%>
			<li><a href="javascript:void(0)" class="disabled">商品列表</a></li>
			<%
			    }
			%>
		</ul>
      </dd>
      <dd><a href="javascript:void(0);" class="click-link item-a" ><span class="a-text fl">订单管理</span><i class="icon-i w30 h30 arrow-down-icon mt5 fr"></i></a>
        <ul>
			<%
			    if (dimengSession.isAccessableResource(ScoreOrderList.class)) {
			%>
			<li><a class="click-link <%if("DDLB".equals(CURRENT_SUB_CATEGORY)){%>select-a<%}%>" href="<%=controller.getURI(request, ScoreOrderList.class)%>" target="mainFrame">订单列表</a></li>
			<%
			    } else {
			%>
			<li><a href="javascript:void(0)" class="disabled">订单列表</a></li>
			<%
			    }
			%>

			<%
			    if (dimengSession.isAccessableResource(DshScoreOrderList.class)) {
			%>
			<li><a class="click-link <%if("DSHDD".equals(CURRENT_SUB_CATEGORY)){%>select-a<%}%>" href="<%=controller.getURI(request, DshScoreOrderList.class)%>" target="mainFrame">待审核</a></li>
			<%
			} else {
			%>
			<li><a href="javascript:void(0)" class="disabled">待审核</a></li>
			<%
				}
			%>

			<%
				if (dimengSession.isAccessableResource(DfhScoreOrderList.class)) {
			%>
			<li><a class="click-link <%if("DFHDD".equals(CURRENT_SUB_CATEGORY)){%>select-a<%}%>" href="<%=controller.getURI(request, DfhScoreOrderList.class)%>" target="mainFrame">待发货</a></li>
			<%
			} else {
			%>
			<li><a href="javascript:void(0)" class="disabled">待发货</a></li>
			<%
				}
			%>

			<%
				if (dimengSession.isAccessableResource(YfhScoreOrderList.class)) {
			%>
			<li><a class="click-link <%if("YFHDD".equals(CURRENT_SUB_CATEGORY)){%>select-a<%}%>" href="<%=controller.getURI(request, YfhScoreOrderList.class)%>" target="mainFrame">已发货</a></li>
			<%
			} else {
			%>
			<li><a href="javascript:void(0)" class="disabled">已发货</a></li>
			<%
				}
			%>

			<%
				if (dimengSession.isAccessableResource(YthScoreOrderList.class)) {
			%>
			<li><a class="click-link <%if("YTHDD".equals(CURRENT_SUB_CATEGORY)){%>select-a<%}%>" href="<%=controller.getURI(request, YthScoreOrderList.class)%>" target="mainFrame">已退货</a></li>
			<%
			} else {
			%>
			<li><a href="javascript:void(0)" class="disabled">已退货</a></li>
			<%
				}
			%>

			<%
				if (dimengSession.isAccessableResource(WshtgScoreOrderList.class)) {
			%>
			<li><a class="click-link <%if("WSHTGDD".equals(CURRENT_SUB_CATEGORY)){%>select-a<%}%>" href="<%=controller.getURI(request, WshtgScoreOrderList.class)%>" target="mainFrame">未审核通过</a></li>
			<%
			} else {
			%>
			<li><a href="javascript:void(0)" class="disabled">未审核通过</a></li>
			<%
				}
			%>
        
        </ul>
      </dd>
      <dd><a href="javascript:void(0);" class="click-link item-a" ><span class="a-text fl">积分管理</span><i class="icon-i w30 h30 arrow-down-icon mt5 fr"></i></a>
        <ul>
			<%
				if (dimengSession.isAccessableResource(ScoreStatisticsList.class)) {
			%>
			<li><a class="click-link <%if("JFTJLB".equals(CURRENT_SUB_CATEGORY)){%>select-a<%}%>" href="<%=controller.getURI(request, ScoreStatisticsList.class)%>" target="mainFrame">积分统计列表</a></li>
			<%
			} else {
			%>
			<li><a href="javascript:void(0)" class="disabled">积分统计列表</a></li>
			<%
				}
			%>

			<%
				if (dimengSession.isAccessableResource(ScoreGetList.class)) {
			%>
			<li><a class="click-link <%if("JFHQJL".equals(CURRENT_SUB_CATEGORY)){%>select-a<%}%>" href="<%=controller.getURI(request, ScoreGetList.class)%>" target="mainFrame">积分获取记录</a></li>
			<%
			} else {
			%>
			<li><a href="javascript:void(0)" class="disabled">积分获取记录</a></li>
			<%
				}
			%>

			<%
				if (dimengSession.isAccessableResource(ScoreExchangeList.class)) {
			%>
			<li><a class="click-link <%if("JFDHJL".equals(CURRENT_SUB_CATEGORY)){%>select-a<%}%>" href="<%=controller.getURI(request, ScoreExchangeList.class)%>" target="mainFrame">积分兑换记录</a></li>
			<%
			} else {
			%>
			<li><a href="javascript:void(0)" class="disabled">积分兑换记录</a></li>
			<%
				}
			%>
        
        </ul>
      </dd>
    </dl>
  </div>
 