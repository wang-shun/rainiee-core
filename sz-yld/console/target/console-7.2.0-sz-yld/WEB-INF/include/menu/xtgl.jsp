<%@page import="com.dimeng.p2p.common.Constant"%>
<%@page import="com.dimeng.p2p.console.servlets.system.htzh.role.RoleList"%>
<%@page import="com.dimeng.p2p.console.servlets.system.htzh.sys.SysUserList"%>
<%@page import="com.dimeng.p2p.console.servlets.system.syslog.constant.ConstantLogList"%>
<%@page import="com.dimeng.p2p.console.servlets.system.syslog.operlog.OperLogList"%>
<%@page import="com.dimeng.p2p.console.servlets.system.syslog.userlog.UserLogList"%>
<%@page import="com.dimeng.p2p.console.servlets.system.ywtg.email.EmailList"%>
<%@page import="com.dimeng.p2p.console.servlets.system.ywtg.letter.LetterList"%>
<%@page import="com.dimeng.p2p.console.servlets.system.ywtg.sms.SmsList"%>
<%@page import="com.dimeng.p2p.modules.systematic.console.service.SysUserManage"%>
<%
SysUserManage sysUserMl = serviceSession.getService(SysUserManage.class);
	com.dimeng.p2p.modules.systematic.console.service.entity.SysUser sysUl = sysUserMl.get(serviceSession.getSession().getAccountId());
%>
<div class="item-subnav-box" data-title="system">
    <dl>
      <dt class="f20 fb"><i class="icon-i w30 h30 va-middle nav-xtgl-icon2"></i>系统管理</dt>
       <dd><a href="javascript:void(0);" class="click-link item-a" ><span class="a-text fl">后台账号</span><i class="icon-i w30 h30 arrow-down-icon mt5 fr"></i></a>
        <ul id="htzh_ul">
        <%
        if(Constant.BUSINESS_ROLE_ID != sysUl.roleId){
						if (dimengSession.isAccessableResource(SysUserList.class)) {
					%>
					<li><a class="click-link <%if("GLYGL".equals(CURRENT_SUB_CATEGORY)){%>select-a<%}%>" href="<%=controller.getURI(request, SysUserList.class)%>" target="mainFrame">管理员管理</a></li>
					<%
						} else {
					%>
					<li><a href="javascript:void(0)" class="disabled">管理员管理</a></li>
					<%
						}}
					%>
				<%-- 	 <%
					 boolean has_business1 = Boolean.parseBoolean(configureProvider.getProperty(BusinessVariavle.IS_BUSINESS));
						if(has_business1){
						if (dimengSession.isAccessableResource("P2P_C_SYS_YWYUSERLIST")) {
					%>
					<li><a href="/console/system/htzh/business/businessUserList.htm" id="YWYGL" class="click-link <%="YWYGL".equals(CURRENT_SUB_CATEGORY) ? "select-a"
								: ""%>" target="mainFrame">业务员管理</a></li>
					<%
						} else {
					%>
					<li><a href="javascript:void(0)" class="disabled">业务员管理</a></li>
					<%
						}}
					%>
 --%>					<%
					if(Constant.BUSINESS_ROLE_ID != sysUl.roleId){
						if (dimengSession.isAccessableResource(RoleList.class)) {
					%>
					<li><a id="yhzgl_li_a" class="click-link <%if("YHZGL".equals(CURRENT_SUB_CATEGORY)){%>select-a<%}%>" href="<%=controller.getURI(request, RoleList.class)%>" target="mainFrame">用户组管理</a></li>
					<%
						} else {
					%>
					<li><a href="javascript:void(0)" class="disabled">用户组管理</a></li>
					<%
						}}
					%>
        </ul>
      </dd>
      <%if(Constant.BUSINESS_ROLE_ID != sysUl.roleId){ %>
      <dd><a href="javascript:void(0);" class="click-link item-a" ><span class="a-text fl">系统日志</span><i class="icon-i w30 h30 arrow-down-icon mt5 fr"></i></a>
        <ul>
        <%
						if (dimengSession.isAccessableResource(UserLogList.class)) {
					%>
					<li><a class="click-link <%if("QTDLRZ".equals(CURRENT_SUB_CATEGORY)){%>select-a<%}%>" href="<%=controller.getURI(request, UserLogList.class)%>" target="mainFrame">前台日志</a></li>
					<%
						} else {
					%>
					<li><a href="javascript:void(0)" class="disabled">前台日志</a></li>
					<%
						}
					%>
					
		<%
						if (dimengSession.isAccessableResource(OperLogList.class)) {
					%>
					<li><a class="click-link <%if("HTCZRZ".equals(CURRENT_SUB_CATEGORY)){%>select-a<%}%>" href="<%=controller.getURI(request, OperLogList.class)%>" target="mainFrame">后台日志</a></li>
					<%
						} else {
					%>
					<li><a href="javascript:void(0)" class="disabled">后台日志</a></li>
					<%
						}
					%>
		<%
						if (dimengSession.isAccessableResource(ConstantLogList.class)) {
					%>
					<li><a class="click-link <%if("PTCLRZ".equals(CURRENT_SUB_CATEGORY)){%>select-a<%}%>" href="<%=controller.getURI(request, ConstantLogList.class)%>" target="mainFrame">常量日志</a></li>
					<%
						} else {
					%>
					<li><a href="javascript:void(0)" class="disabled">常量日志</a></li>
					<%
						}
					%>		
          
        </ul>
      </dd>
      <dd><a href="javascript:void(0);" class="click-link item-a" ><span class="a-text fl">业务推广</span><i class="icon-i w30 h30 arrow-down-icon mt5 fr"></i></a>
        <ul>
        <%
						if (dimengSession.isAccessableResource(LetterList.class)) {
					%>
					<li><a class="click-link <%if("ZNXTG".equals(CURRENT_SUB_CATEGORY)){%>select-a<%}%>" href="<%=controller.getURI(request, LetterList.class)%>" target="mainFrame">站内信推广</a></li>
					<%
						} else {
					%>
					<li><a href="javascript:void(0)" class="disabled">站内信推广</a></li>
					<%
						}
					%>	
		<%
						if (dimengSession.isAccessableResource(SmsList.class)) {
					%>
					<li><a class="click-link <%if("DXTG".equals(CURRENT_SUB_CATEGORY)){%>select-a<%}%>" href="<%=controller.getURI(request, SmsList.class)%>" target="mainFrame">短信推广</a></li>
					<%
						} else {
					%>
					<li><a href="javascript:void(0)" class="disabled">短信推广</a></li>
					<%
						}
					%>
		<%
						if (dimengSession.isAccessableResource(EmailList.class)) {
					%>
					<li><a class="click-link <%if("YJTG".equals(CURRENT_SUB_CATEGORY)){%>select-a<%}%>" href="<%=controller.getURI(request, EmailList.class)%>" target="mainFrame">邮件推广</a></li>
					<%
						} else {
					%>
					<li><a href="javascript:void(0)" class="disabled">邮件推广</a></li>
					<%
						}
					%>
        </ul>
      </dd>
      <%} %>
    </dl>
  </div>
<script type="text/javascript">
	$(function(){
		//解决修改权限后，菜单不刷新问题
		var $system = $(".left-subnav-containe .item-subnav-box[data-title='system']");
		$system.show();
		$("#htzh_ul").show();
		$("#yhzgl_li_a").addClass("select-a");
	});
</script>
 