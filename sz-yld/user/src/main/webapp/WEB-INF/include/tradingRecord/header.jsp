<%@page import="com.dimeng.p2p.account.user.service.ZjlsManage"%>
<%@page import="com.dimeng.p2p.S61.enums.T6101_F03"%>
<%@page import="com.dimeng.p2p.account.user.service.UserInfoManage" %>
<%@page import="com.dimeng.p2p.S61.entities.T6110" %>
<%@page import="com.dimeng.p2p.S61.enums.T6110_F07" %>
<%
	ZjlsManage manage = serviceSession.getService(ZjlsManage.class);
	boolean tg = BooleanParser.parse(configureProvider.getProperty(SystemVariable.SFZJTG));
	String  escrowPrefix = configureProvider.getProperty(SystemVariable.ESCROW_PREFIX);
	UserInfoManage userInfoManage = serviceSession.getService(UserInfoManage.class);
    T6110 userInfo = userInfoManage.getUserInfo(serviceSession.getSession().getAccountId());
%>
<div class="trading_total clearfix">
	<div class="total fl mt15">
			可用余额<br /> <span class="orange f22"><%=Formater.formatAmount(manage.getZhje(T6101_F03.WLZH))%>元</span>
	</div>
	<div class="line mt20"></div>
	<div class="fl mr50 pr20 mt15">已充值总额<br><%=Formater.formatAmount(manage.getCz())%>元</div>
	<%if(tg && "yeepay".equals(escrowPrefix)){ %>
	<div class="fl mt15">已提现总额<br><%=Formater.formatAmount(manage.getTGTx())%>元</div>
	<%}else{ %>
	<div class="fl mt15">已提现总额<br><%=Formater.formatAmount(manage.getTx())%>元</div>
	<%} %>
	<div class="fr mr50">
		<p><a href="<%configureProvider.format(out,URLVariable.USER_CHARGE);%>" class="btn01">充值</a></p>
		<%if (T6110_F07.HMD != userInfo.F07) { %>
		<p class="mt20"><a href="<%configureProvider.format(out,URLVariable.USER_WITHDRAW);%>" class="btn02">提现</a></p>
		<%} %>
	</div>
</div>