<%@page import="java.util.Map"%>
<%@page import="com.dimeng.p2p.user.servlets.AbstractUserServlet"%>
<%@page import="com.dimeng.p2p.S63.enums.T6352_F06"%>
<%@page import="java.util.Set"%>
<%@page import="java.util.List"%>
<%@page import="com.dimeng.p2p.repeater.score.entity.MyOrderInfoExt"%>
<%@page import="com.dimeng.p2p.user.servlets.reward.Wdhb" %>
<%@include file="/WEB-INF/include/authenticatedSession.jsp" %>
<%@page import="com.dimeng.p2p.S63.enums.T6350_F07"%>
<%@page import="com.dimeng.p2p.S63.enums.T6359_F08"%>
<%@page import="com.dimeng.p2p.user.servlets.mall.MyOrder"%>
<%@ page import="com.dimeng.util.StringHelper" %>


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>我的订单-<%
        configureProvider.format(out, SystemVariable.SITE_NAME);
    %></title>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
</head>
<%
    CURRENT_CATEGORY = "WDSC";
    CURRENT_SUB_CATEGORY = "WDDD";
    String _url = configureProvider.format(SystemVariable.SITE_DOMAIN);
    PagingResult<MyOrderInfoExt> result = (PagingResult<MyOrderInfoExt>)request.getAttribute("myOrderInfo");
    Map<String, List<MyOrderInfoExt>> map = (Map<String, List<MyOrderInfoExt>>)request.getAttribute("orderMap");
    Set<?> keySet = null;
    if(map!=null){
        keySet = map.keySet();
    }
%>
<body>
<%@include file="/WEB-INF/include/header.jsp" %>
    <div class="main_bg clearfix">
        <div class="user_wrap w1002 clearfix">
            <%@include file="/WEB-INF/include/menu.jsp" %>
            <!--右边内容-->
            <form id="form1" action="<%=controller.getURI(request, MyOrder.class)%>" method="post">
        <div class="r_main">
        	<div class="user_mod">
            	<div class="user_til clearfix"><i class="icon"></i><span class="gray3 f18">我的订单</span></div>
            	
                <div class="mall_indent">
                	
                	<%
                	if(keySet!=null){
                		for(Object key : keySet){
                	%>
                	<dl>
                	<dt>订单号：<%=key %></dt>
                	<%
                	List<MyOrderInfoExt> value = map.get(key);
                	for(MyOrderInfoExt myOrderInfo : value){
                	%>
                		<dd>
                            <div class="pic"><img src="<%=StringHelper.isEmpty(myOrderInfo.comdPicture) ? controller.getStaticPath(request)+"/images/mall_list.jpg" : fileStore.getURL(myOrderInfo.comdPicture) %>" ></div>
                            <h3><a href=<%="http://"+_url+"/mall/ptscXq/"+myOrderInfo.F03 +".html"%>>商品名称：<%=myOrderInfo.comdName %></a></h3>
                            <ul class="list01 clearfix">
                            	<li>价值：
                            		<%if(myOrderInfo.payment.name().equals(T6352_F06.balance.name())){%><%=Formater.formatAmount(myOrderInfo.F05) %>(元)
                            		<%}else if(myOrderInfo.payment.name().equals(T6352_F06.score.name())){ %><%=myOrderInfo.F04 %>
                            		<%} %>
                            	</li>
                                <li>支付方式：<%=myOrderInfo.payment.getChineseName() %></li>
                                <li>数量：<%=myOrderInfo.F06 %></li>
                                <li class="last">状态：<%=myOrderInfo.F08==T6359_F08.pendding?"待确认":(myOrderInfo.F08==T6359_F08.nopass?"已取消":myOrderInfo.F08.getChineseName())%></li>
                            </ul>
                            <ul class="list02 clearfix">
                            	<li class="l">成交时间：<%=DateTimeParser.format(myOrderInfo.orderTime)%></li>
                                <li>发货时间：<%=myOrderInfo.F10==null?"无": DateTimeParser.format(myOrderInfo.F10)%></li>
                                <%if(myOrderInfo.category == T6350_F07.kind){%>   
                                <li class="l">物流信息：<span title="<%=myOrderInfo.F11%>"><%=StringHelper.isEmpty(myOrderInfo.F11)?"无":StringHelper.truncation(myOrderInfo.F11,30)%></span></li>
                                <li>物流单号：<span title="<%=myOrderInfo.F12%>"><%=StringHelper.isEmpty(myOrderInfo.F12)?"无":StringHelper.truncation(myOrderInfo.F12, 30)%></span></li>
                                <%} else{%>
                                <li class="l">充值手机号：<%StringHelper.filterHTML(out, myOrderInfo.phoneNum); %></li>
                                <%} %>
                            </ul>
                        </dd>
                        <%} %>
                    </dl>
                	<%} }else{%>
                	<div class='f16 tc pt100 pb100'>暂无数据</div>
                	<%} %>
                	<%if(keySet!=null && keySet.size()>0){ %>
                    <!--分页-->  
                    <% 
                    AbstractUserServlet.rendPagingResult(out, result);  
                    %>
                    <!--分页  --END--> 
                    <%} %>
                </div>
                
            </div>
        </div>   
        </form>     
        <!--右边内容-->
        </div>
    </div>
<%@include file="/WEB-INF/include/footer.jsp" %>
<script type="text/javascript">
</script>
</body>
</html>