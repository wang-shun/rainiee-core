<div class="left-subnav-containe"> 
<%@include file="/WEB-INF/include/menu/sy.jsp" %>
<%
	Boolean leftsfzjtg = BooleanParser.parseObject(configureProvider.getProperty(SystemVariable.SFZJTG));
	String escrow = configureProvider.getProperty(SystemVariable.ESCROW_PREFIX);
	if(leftsfzjtg || "FUYOU".equals(escrow)|| "ALLINPAY".equals(escrow))
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
<%@include file="/WEB-INF/include/menu/ptsc.jsp" %>
<%@include file="/WEB-INF/include/menu/xcgl.jsp" %>
<%@include file="/WEB-INF/include/menu/xtgl.jsp" %>
<%@include file="/WEB-INF/include/menu/yhgl.jsp" %>
<%@include file="/WEB-INF/include/menu/ywgl.jsp" %>
<%@include file="/WEB-INF/include/menu/jcxxgl.jsp" %>
<%@include file="/WEB-INF/include/menu/app.jsp" %>
</div>