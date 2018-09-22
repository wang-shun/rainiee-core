<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.grxx.GrList"%>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.jgxx.JgList"%>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.qyxx.QyList"%>
<%@page import="com.dimeng.p2p.console.servlets.account.vipmanage.zhgl.ZhList"%>
<!--用户管理 子菜单-->
<div class="item-subnav-box" data-title="user">
	<dl>
		<dt class="f20 fb"><i class="icon-i w30 h30 va-middle nav-yhgl-icon2"></i>会员管理</dt>
		<dd>
			<a href="javascript:void(0);" class="click-link item-a"><span class="a-text fl">会员管理</span><i
				class="icon-i w30 h30 arrow-down-icon mt5 fr"></i></a>
			<ul>
				<li>
					<%
					    if (dimengSession.isAccessableResource(ZhList.class))
					            {
					%> <a href="<%=controller.getURI(request, ZhList.class)%>"
					class="click-link <%if ("ZHGL".equals(CURRENT_SUB_CATEGORY))
                        {%>select-a<%}%>"
					target="mainFrame" id="ZHGL">账号管理 </a>
					<%
					    }
					            else
					            {
					%> <a href="javascript:void(0)" class="disabled">账号管理</a> <%
     }
 %>
				</li>
				<li>
					<%
					    if (dimengSession.isAccessableResource(GrList.class))
					            {
					%> <a href="<%=controller.getURI(request, GrList.class)%>"
					class="click-link <%if ("GRXX".equals(CURRENT_SUB_CATEGORY))
                        {%>select-a<%}%>"
					target="mainFrame" id="GRXX">个人信息
				</a> <%
     }
             else
             {
 %> <a href="javascript:void(0)" class="disabled">个人信息</a> <%
     }
 %>
				</li>
				<li>
					<%
					    if (dimengSession.isAccessableResource(QyList.class))
					            {
					%> <a href="<%=controller.getURI(request, QyList.class)%>"
					class="click-link <%if ("QY".equals(CURRENT_SUB_CATEGORY))
                        {%>select-a<%}%>"
					target="mainFrame" id="QY">企业信息</a>

					<%
					    }
					            else
					            {
					%> <a href="javascript:void(0)" class="disabled">企业信息</a> <%
     }
 %>
				</li>
				<li>
					<%
					    if (dimengSession.isAccessableResource(JgList.class))
					            {
					%> <a href="<%=controller.getURI(request, JgList.class)%>"
					class="click-link <%if ("JG".equals(CURRENT_SUB_CATEGORY))
                        {%>select-a<%}%>"
					target="mainFrame" id="JG">机构信息</a>

					<%
					    }
					            else
					            {
					%> <a href="javascript:void(0)" class="disabled">机构信息</a> <%
     }
 %>
				</li>
			</ul>
		</dd>
		<%if(BooleanParser.parse(configureProvider.getProperty(RegulatoryPolicyVariavle.IS_OPEN_RISK_ASSESS))){ %>
		<dd>
			<a href="javascript:void(0);" class="click-link item-a"><span class="a-text fl">风险评估结果</span><i
				class="icon-i w30 h30 arrow-down-icon mt5 fr"></i></a>
			<ul>
			<%
				if (dimengSession.isAccessableResource("P2P_C_ACCOUNT_FXPGJG_POLICY_LIST")) {
			%>
			<li><a class="click-link <%if("FXPGJG".equals(CURRENT_SUB_CATEGORY)){%>select-a<%}%>" href="/console/account/riskresult/policy/riskResultList.htm" target="mainFrame">风险评估结果</a></li>
			<%
			} else {
			%>
			<li><a href="javascript:void(0)" class="disabled">风险评估结果</a></li>
			<%
				}
			%>
			</ul>
		</dd>
		<%} %>
	</dl>

</div>
