<%@page import="com.dimeng.p2p.console.servlets.app.manage.AppStartFindSet"%>
<%@page import="com.dimeng.p2p.console.servlets.app.manage.AppVerSearch"%>
<%-- <%if("APPVER".equals(CURRENT_CATEGORY)){%> --%>
<div class="item-subnav-box" data-title="app">
    <dl>
      <dt class="f20 fb"><i class="icon-i w30 h30 va-middle nav-appgl-icon2"></i>APP管理</dt>
      
      <dd><a href="javascript:void(0);" class="click-link item-a" ><span class="a-text fl">APP管理</span></a>
	      <ul>
	      		<li>
					<%
						if(dimengSession.isAccessableResource(AppVerSearch.class))
						{
					%>
					<a class="click-link <%if ("APPBBGL".equals(CURRENT_SUB_CATEGORY)) {%> select-a <%}%>" href="<%=controller.getURI(request, AppVerSearch.class)%>" target="mainFrame">APP版本管理</a>
					<%}else{ %>
					<a href="javascript:void(0)" class="disabled">APP版本管理</a>
					<%} %>
				</li>
				
				<li>
					<%
						if(dimengSession.isAccessableResource(AppStartFindSet.class))
						{
					%>
					<a class="click-link <%if ("APPQDFXY".equals(CURRENT_SUB_CATEGORY)) {%> select-a <%}%>" id="APPQDFXY" href="<%=controller.getURI(request, AppStartFindSet.class)%>" target="mainFrame">启动发现页设置</a>
					<%}else{ %>
					<a href="javascript:void(0)" class="disabled">启动发现页设置</a>
					<%} %>
				</li>
	      </ul>
      </dd>
    </dl>
  </div>
<%-- <%}%> --%>