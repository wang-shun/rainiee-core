<%@include file="/WEB-INF/include/meta.jsp" %>
<%@include file="/WEB-INF/include/style.jsp" %>
<script type="text/javascript">
	var menuFinishLoad = false;
</script>
<div class="left-container"><a href="javascript:void(0);" class="left-hide-arrow icon-i"></a>
<div class="left-subnav-containe"> 
<%@include file="/WEB-INF/include/menu/sy.jsp" %>
<%
	SysUserManage index = serviceSession.getService(SysUserManage.class);
	boolean isOneLogin = index.isOneLogin();
	Boolean leftsfzjtg = BooleanParser.parseObject(configureProvider.getProperty(SystemVariable.SFZJTG));
	String escrow = configureProvider.getProperty(SystemVariable.ESCROW_PREFIX);
	if(leftsfzjtg || "FUYOU".equals(escrow) || "ALLINPAY".equals(escrow))
	{
%>
	<jsp:include page="<%=configureProvider.getProperty(URLVariable.CWGL_MENU_PAGE) %>" flush="true">
		<jsp:param value="<%=CURRENT_SUB_CATEGORY %>" name="CURRENT_SUB_CATEGORY"/>
	</jsp:include>
<%}else{ %>
	<%@include file="/WEB-INF/include/menu/cwgl.jsp" %>
<%} %>
<%@include file="/WEB-INF/include/menu/tggl.jsp" %>
<%@include file="/WEB-INF/include/menu/tjgl.jsp" %>
	<%if(BooleanParser.parse(configureProvider.getProperty(MallVariavle.IS_MALL))){%>
	<%@include file="/WEB-INF/include/menu/ptsc.jsp" %>
	<%}%>
<%@include file="/WEB-INF/include/menu/xcgl.jsp" %>
<%@include file="/WEB-INF/include/menu/xtgl.jsp" %>
<%@include file="/WEB-INF/include/menu/yhgl.jsp" %>
<%@include file="/WEB-INF/include/menu/ywgl.jsp" %>
<%@include file="/WEB-INF/include/menu/jcxxgl.jsp" %>
<%@include file="/WEB-INF/include/menu/app.jsp" %>
</div>
</div>
<%@include file="/WEB-INF/include/script.jsp" %>
<script type="text/javascript">
	window.onload = function(){
		menuFinishLoad = true;
		$(parent.frames["mainFrame"].document).find("div.load-animate-container").hide();
		$(parent.frames["topFrame"].document).find("div.loadfirst").hide();
	}
</script>