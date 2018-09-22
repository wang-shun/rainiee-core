<%@page import="com.dimeng.p2p.repeater.score.entity.UserAccount"%>
<%@page import="com.dimeng.p2p.front.servlets.mall.DelCar"%>
<%@page import="com.dimeng.p2p.front.servlets.mall.ConfirmOrder"%>
<%@page import="com.dimeng.p2p.front.servlets.mall.MallIndex"%>
<%@page import="com.dimeng.p2p.front.servlets.mall.CarList"%>
<%@page import="com.dimeng.p2p.repeater.score.entity.ShoppingCarResult"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>购物车<%=configureProvider.getProperty(SystemVariable.SITE_TITLE)%></title>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
</head>
<%
	ShoppingCarResult[] carList = ObjectHelper.convertArray(request.getAttribute("result"), ShoppingCarResult.class);
	MallChangeManage changeManage = serviceSession.getService(MallChangeManage.class);
	UserAccount account = changeManage.queryAccount();
    //支持余额购买常量
    boolean allowsBalance = BooleanParser.parse(configureProvider.getProperty(MallVariavle.ALLOWS_THE_BALANCE_TO_BUY));
    UserInfoManage userInfoManage_1 = serviceSession.getService(UserInfoManage.class);
    T6110 t6110_1 = userInfoManage_1.getUserInfo(serviceSession.getSession().getAccountId());
%>
<body>
<%@include file="/WEB-INF/include/authenticatedSession.jsp" %>
<%@include file="/WEB-INF/include/header.jsp" %>
<!--主体内容-->
<div class="main_bg">
	<div class="main_mod">
	    <input id="allowsBalance" type="hidden" value="<%=allowsBalance%>"/>
		<div class="main_hd"><i class="icon"></i><span class="gray3 f18">全部商品</span></div>
		<%if(carList!=null && carList.length >0 ){ %>
        <div class="order_info order_info_tencol">
        	<div class="hd">
            	<ul class="clearfix">
                	<li class="td all"><input name="checkAll" type="checkbox" value="" /> 全选</li>
                	<li class="td goods">商品信息</li>
                    <%if(allowsBalance){%>
                     <li class="td mod01">单价(元)</li>
                    <%}%>
                    <li class="td mod02">积分</li>
                    <li class="td mod03">数量</li>
                    <li class="td mod04">库存</li>
                    <%if(allowsBalance){%>
                    <li class="td mod05">小计(元)</li>
                    <%}%>
                    <li class="td mod06">积分总计</li>
                    <li class="td mod07">支付方式</li>
                    <li class="td mod08">操作</li>
                </ul>
            </div>
            <form action="<%=controller.getURI(request, ConfirmOrder.class)%>" method="post">
            <input name="availScore" id="availScore" type="hidden" value="<%=account==null?0:account.totalScore%>"/>
            <input name="payType" type="hidden"/>
            <input name="goodsList" type="hidden"/>
                <input name="source" type="hidden" value="car"/>
            <input name="fromUrl" type="hidden" value="<%=controller.getViewURI(request, CarList.class)%>"/>
            <input id="delCarUrl" type="hidden" value="<%=controller.getURI(request, DelCar.class)%>"/>
            <%for(ShoppingCarResult car : carList){ %>
            <div class="bd" goodId="<%=car.id%>">
            	<ul class="clearfix" isScore="<%=car.isBuyScore %>" isCash="<%=car.isBuyCash %>" count="<%=car.goodsCount%>" goodName="<%=car.goodsName%>" score="<%=car.score%>" amount="<%=car.amount%>">
                	<li class="td all">
                	<%if(car.goodsCount>0 && (("yes".equals(car.isBuyCash) && allowsBalance) || "yes".equals(car.isBuyScore))){ %>
                	<input name="goods"  type="checkbox" value="<%=car.id %>" goodsId="<%=car.goodsId%>"/>
                	<%} %>
                	</li>
                	
                	<li class="td goods"><a href="/mall/ptscXq/<%=car.goodsId %>.html">
                        <img src="<%=StringHelper.isEmpty(car.goodsImg) ? controller.getStaticPath(request)+"/images/mall_list.jpg" : car.goodsImg %>">
                        <div class="title">
                        	<div class="name" style="cursor: pointer;" title="<%=car.goodsName%>"><%StringHelper.filterHTML(out, car.goodsName); %></div>
                            <%-- <div class="arrow"></div>
                            <div class="full_name"><%StringHelper.filterHTML(out, car.desc); %></div> --%>
                        </div></a>
                    </li>
                    <%if(allowsBalance){%>
                    <li class="td mod01"><%="yes".equals(car.isBuyCash) ? Formater.formatAmount(car.amount) :"--" %></li>
                    <%}%>
                    <li class="td mod02"><%="yes".equals(car.isBuyScore) ? car.score :"--" %></li>
                    <li class="td mod03">
                    	<%if(car.goodsCount>0){ %>
	                    	<span class="choose">
	                    	<a href="javascript:void(0)" onclick="minNum(this,'<%=car.isBuyCash %>','<%=car.isBuyScore %>')" class="reduce">-</a>
	                    	<input name="num" type="text" value="<%=car.num %>" onblur="checkNum(this,'<%=car.goodsCount %>',<%=car.singleCount %>,<%=car.ygCount%>,'<%=car.isBuyCash %>','<%=car.isBuyScore %>')"/>
	                    	<a href="javascript:void(0);" onclick="addNum(this,<%=car.goodsCount %>,<%=car.singleCount %>,'<%=car.isBuyCash %>','<%=car.isBuyScore %>',<%=car.ygCount%>)" class="add">+</a>
	                    	</span>
                    	<%}else{ %>
                    		<span class="red f12">商品库存不足</span>
                    	<%} %>
                    </li>
                    <li class="td mod04"><%=car.goodsCount %></li>
                    <%if(allowsBalance){%>
                    <li class="td mod05 highlight" at="<%=car.amount%>" name="lsAmount"><%="yes".equals(car.isBuyCash) ? Formater.formatAmount(car.amount.multiply(new BigDecimal(car.num))) :"--" %></li>
                    <%}%>
                    <li class="td mod06 highlight" at="<%=car.score%>" name="lsScore"><%="yes".equals(car.isBuyScore) ? car.score*car.num :"--" %></li>
                  	<li class="td mod07">
                        <select name="type" class="select_style">
                        <%if("yes".equals(car.isBuyScore)){ %>
                        <option value="score">积分支付</option>
                        <%} %>
                        <%if("yes".equals(car.isBuyCash) && allowsBalance){ %>
                        <option value="balance">余额支付</option>
                        <%} %>
                        </select>
                    </li>
                    <%if(t6110_1.F06 == T6110_F06.ZRR && T6110_F07.HMD != t6110_1.F07){%>
                    <li class="td mod08"><a href="javascript:void(0)" onclick="delCar(<%=car.id %>)" class="highlight">删除</a></li>
                    <%}else{%>
                    <li class="td mod08">删除</li>
                    <%}%>
                </ul>
            </div>
           <%} %>
           </form>

            <div class="border p10 pt15 pb15 mt20 lh30">
                <%if(t6110_1.F06 == T6110_F06.ZRR && T6110_F07.HMD != t6110_1.F07){%>
            <a href="<%=controller.getURI(request, MallIndex.class) %>" class="btn01 ml10 fr">继续购物</a>
            <a href="javascript:void(0)" onclick="toPay()" class="btn01 fr">去结算</a>
                <%}%>
            <input name="checkAll" type="checkbox" value="" class="ml10" /> 全选
                <%if(t6110_1.F06 == T6110_F06.ZRR && T6110_F07.HMD != t6110_1.F07){%>
            <a href="javascript:void(0)" id="delAll" class="ml20 highlight">删除选中的商品</a>
                <%}else{%>
                删除选中的商品
                <%}%>
            <span class="ml50 mr20 pl10">已选择<span id="totalNum">0</span>件商品</span>
                <%if(allowsBalance){%>
		            总价：<span class="red f16">￥</span><span class="highlight" id="totalAmount">0.00</span> <span class="ml20">
                <%}%>
		            所需积分：</span><span class="highlight" id="totalScore">0</span></div>
        </div>

         <%}else{ %>
            <div class="mall_empty">
        	<p class="f20 gray3">购物车内还是空的，快去积分商城兑换商品吧！</p>
            <a href="<%=controller.getURI(request, MallIndex.class) %>" class="btn06 mt10">返回积分商城</a>
        </div>
         <%} %>
    </div>
</div>
<!--主体内容-->
<div id="info"></div>
<div class="popup_bg" style="display: none;"></div>
<%@include file="/WEB-INF/include/footer.jsp" %>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/dialog.js"></script>
<%
    String warringMessage = controller.getPrompt(request, response, PromptLevel.WARRING);
if (!StringHelper.isEmpty(warringMessage)) {
%>
<%-- <script type="text/javascript">
$("#info").html(showDialogInfo('<%=warringMessage%>', "doubt"));
$("div.popup_bg").show();
</script> --%>

<div class="popup_bg"></div>
<div class="dialog">
<div class="title"><a href="javascript:closeInfo();" class="out"></a>提示</div>
   <div class="content">
   	<div class="tip_information">
   		<div class="doubt"></div>
   		<div class="tips">
   			<span class="f20 gray3"><%=warringMessage%></span>
   		</div>
   	</div>
   	<div class="tc mt20"><a href="<%=controller.getURI(request, CarList.class) %>" class="btn01">确定</a></div> 
   </div>
   </div>
<%} %>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/mall/carList.js"></script>
</body>
</html>