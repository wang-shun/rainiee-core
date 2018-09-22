<%@page import="com.dimeng.p2p.console.servlets.spread.hdgl.hbtj.SearchHbCount"%>
<%@page import="com.dimeng.p2p.console.servlets.spread.hdgl.hdlist.SearchHdgl"%>
<%@page import="com.dimeng.p2p.console.servlets.spread.hdgl.jxqjstj.SearchJxqClearCount"%>
<%@ page import="com.dimeng.p2p.console.servlets.spread.hdgl.jxqtj.SearchJxqCount" %>
<%@ page import="com.dimeng.p2p.console.servlets.spread.hdgl.tyjjstj.ExperienceStaticsDH" %>
<%@ page import="com.dimeng.p2p.console.servlets.spread.hdgl.tyjtj.SearchExpCount" %>
<%@ page import="com.dimeng.p2p.console.servlets.spread.hdgl.tyjxq.SearchTyjgl" %>
<%@ page import="com.dimeng.p2p.console.servlets.spread.jljgl.jljxq.JljxqList" %>
<%@ page import="com.dimeng.p2p.console.servlets.spread.tgjl.tgcxjl.SearchTgjl" %>
<%@ page import="com.dimeng.p2p.console.servlets.spread.tgjl.tgjlgy.SearchTgtj" %>
<%@ page import="com.dimeng.p2p.console.servlets.spread.tgjl.tgxq.SearchTgxq" %>
<div class="item-subnav-box" data-title="extension">
	<dl>
		<dt class="f20 fb"><i class="icon-i w30 h30 va-middle nav-tggl-icon2"></i>推广管理</dt>
		<dd><a href="javascript:void(0);" class="click-link item-a"><span class="a-text fl">推广奖励</span><i
				class="icon-i w30 h30 arrow-down-icon mt5 fr"></i></a>
			<ul>
				<%
						if (dimengSession.isAccessableResource(SearchTgtj.class)) {
					%>
					<li><a class="click-link <%if("TGTJ".equals(CURRENT_SUB_CATEGORY)){%>select-a<%}%>" href="<%=controller.getURI(request, SearchTgtj.class)%>" target="mainFrame">推广奖励概要列表</a></li>
					<%
						} else {
					%>
					<li><a href="javascript:void(0)" class="disabled">推广奖励概要列表</a></li>
					<%
						}
					%>	
		<%
						if (dimengSession.isAccessableResource(SearchTgjl.class)) {
					%>
					<li><a class="click-link <%if("TGJL".equals(CURRENT_SUB_CATEGORY)){%>select-a<%}%>" href="<%=controller.getURI(request, SearchTgjl.class)%>" target="mainFrame">推广持续奖励详情列表</a></li>
					<%
						} else {
					%>
					<li><a href="javascript:void(0)" class="disabled">推广持续奖励详情列表</a></li>
					<%
						}
					%>
		<%
						if (dimengSession.isAccessableResource(SearchTgxq.class)) {
					%>
					<li><a class="click-link <%if("TGXQ".equals(CURRENT_SUB_CATEGORY)){%>select-a<%}%>" href="<%=controller.getURI(request, SearchTgxq.class)%>" target="mainFrame">推广详情列表</a></li>
					<%
						} else {
					%>
					<li><a href="javascript:void(0)" class="disabled">推广详情列表</a></li>
					<%
						}
					%>
         
        </ul>
      </dd>
      
      <dd><a href="javascript:void(0);" class="click-link item-a" ><span class="a-text fl">奖励金管理</span><i class="icon-i w30 h30 arrow-down-icon mt5 fr"></i></a>
        <ul>
        <%
						if (dimengSession.isAccessableResource(JljxqList.class)) {
					%>
					<li><a class="click-link <%if("JLJXQLB".equals(CURRENT_SUB_CATEGORY)){%>select-a<%}%>" href="<%=controller.getURI(request, JljxqList.class)%>" target="mainFrame">奖励金详情列表</a></li>
					<%
						} else {
					%>
					<li><a href="javascript:void(0)" class="disabled">奖励金详情列表</a></li>
					<%
						}
					%>	
		
        </ul>
      </dd>
      <dd><a href="javascript:void(0);" class="click-link item-a" ><span class="a-text fl">活动管理</span><i class="icon-i w30 h30 arrow-down-icon mt5 fr"></i></a>
        <ul>
        <%
						if (dimengSession.isAccessableResource(SearchHdgl.class)) {
					%>
					<li><a class="click-link <%if("HDGL".equals(CURRENT_SUB_CATEGORY)){%>select-a<%}%>" href="<%=controller.getURI(request, SearchHdgl.class)%>" target="mainFrame">活动列表</a></li>
					<%
						} else {
					%>
					<li><a href="javascript:void(0)" class="disabled">活动列表</a></li>
					<%
						}
					%>
				<%if(Boolean.parseBoolean(configureProvider.getProperty(SiteSwitchVariable.REDPACKET_INTEREST_SWITCH))){%>
				   <% if (dimengSession.isAccessableResource(SearchHbCount.class)) {
					%>
					<li><a class="click-link <%if("HBTJ".equals(CURRENT_SUB_CATEGORY)){%>select-a<%}%>" href="<%=controller.getURI(request, SearchHbCount.class)%>" target="mainFrame">红包统计</a></li>
					<%
						} else {
					%>
					<li><a href="javascript:void(0)" class="disabled">红包统计</a></li>
					<%
						}
					%>

					<% if (dimengSession.isAccessableResource(SearchJxqCount.class)) {
					%>
					<li><a class="click-link <%if("JXQTJ".equals(CURRENT_SUB_CATEGORY)){%>select-a<%}%>" href="<%=controller.getURI(request, SearchJxqCount.class)%>" target="mainFrame">加息券统计</a></li>
					<%
						} else {
					%>
					<li><a href="javascript:void(0)" class="disabled">加息券统计</a></li>
					<%
						}
					%>

				    <% if (dimengSession.isAccessableResource(SearchJxqClearCount.class)) {
					%>
					<li><a class="click-link <%if("JXQJSTJ".equals(CURRENT_SUB_CATEGORY)){%>select-a<%}%>" href="<%=controller.getURI(request, SearchJxqClearCount.class)%>" target="mainFrame">加息券结算统计</a></li>
					<%
						} else {
					%>
					<li><a href="javascript:void(0)" class="disabled">加息券结算统计</a></li>
					<%
						}
					%>
				<%}%>
			       <% if (dimengSession.isAccessableResource(SearchExpCount.class)) {
					%>
					<li><a class="click-link <%if("EXPTJ".equals(CURRENT_SUB_CATEGORY)){%>select-a<%}%>" href="<%=controller.getURI(request, SearchExpCount.class)%>" target="mainFrame">体验金统计</a></li>
					<%
						} else {
					%>
					<li><a href="javascript:void(0)" class="disabled">体验金统计</a></li>
					<%
						}
					%>
			        <% if (dimengSession.isAccessableResource(SearchTyjgl.class)) {
					%>
					<li><a class="click-link <%if("TYJXQLB".equals(CURRENT_SUB_CATEGORY)){%>select-a<%}%>" href="<%=controller.getURI(request, SearchTyjgl.class)%>" target="mainFrame">体验金详情列表</a></li>
					<%
						} else {
					%>
					<li><a href="javascript:void(0)" class="disabled">体验金详情列表</a></li>
					<%
						}
					%>
			        <% if (dimengSession.isAccessableResource(ExperienceStaticsDH.class)) {
					%>
					<li><a class="click-link <%if("TYJTJ".equals(CURRENT_SUB_CATEGORY)){%>select-a<%}%>" href="<%=controller.getURI(request, ExperienceStaticsDH.class)%>" target="mainFrame">体验金结算统计</a></li>
					<%
						} else {
					%>
					<li><a href="javascript:void(0)" class="disabled">体验金结算统计</a></li>
					<%
						}
					%>
        </ul>
      </dd>
    </dl>
  </div>
 