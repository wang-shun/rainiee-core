<%@page import="org.bouncycastle.util.encoders.Hex"%>
<%@page import="com.dimeng.p2p.common.DimengRSAPulicKey"%>
<%@page import="com.dimeng.p2p.service.PtAccountManage"%>
<%@page import="com.dimeng.p2p.front.servlets.financing.sbtz.CheckPsd"%>
<%@page import="com.dimeng.p2p.variables.defines.pays.PayVariavle"%>
<%@page import="com.dimeng.p2p.front.servlets.mall.DelAddress"%>
<%@page import="com.dimeng.p2p.front.servlets.mall.AddressList"%>
<%@page import="com.dimeng.p2p.front.servlets.mall.SaveAddress"%>
<%@page import="com.dimeng.p2p.front.servlets.Region"%>
<%@page import="java.util.List"%>
<%@page import="com.dimeng.p2p.front.servlets.mall.MallIndex"%>
<%@page import="com.dimeng.p2p.front.servlets.mall.CarList"%>
<%@page import="com.dimeng.p2p.repeater.score.entity.ShoppingCarResult"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>订单确认-<%=configureProvider.getProperty(SystemVariable.SITE_TITLE)%></title>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
</head>
<%
List<ShoppingCarResult> list = ObjectHelper.convert(request.getAttribute("list"), List.class);
String type = (String)request.getAttribute("type");
String goodsList = (String)request.getAttribute("goodsList");
BigDecimal totalAmount = (BigDecimal)request.getAttribute("totalAmount");
int totalNum = (Integer)request.getAttribute("totalNum");
int totalScore = (Integer)request.getAttribute("totalScore");
PtAccountManage ptAccountManage = serviceSession.getService(PtAccountManage.class);
DimengRSAPulicKey publicKey = ptAccountManage.getPublicKey();
String modulus = new String(Hex.encode(publicKey.getModulus().toByteArray()));
String exponent = new String(Hex.encode(publicKey.getPublicExponent().toByteArray()));

%>
<body>
<%@include file="/WEB-INF/include/authenticatedSession.jsp" %>
<%@include file="/WEB-INF/include/header.jsp" %>
<!--主体内容-->
<div class="main_bg">
 <input id="addressListUrl" value="<%=controller.getURI(request, AddressList.class) %>" type="hidden"/>
 <input id="delUrl" value="<%=controller.getURI(request, DelAddress.class) %>" type="hidden"/>
 <input id="buyUrl" value="<%=configureProvider.format(URLVariable.SCOREORDER_PAYMENT_URL)%>" type="hidden"/>
 <input id="checkUrl" value="<%=controller.getURI(request, CheckPsd.class) %>" type="hidden"/>
 <input id="myOrderUrl" value="<%configureProvider.format(out, URLVariable.MYORDER_URL);%>" type="hidden"/> 
 <input id="accountId" name="accountId" value="<%=serviceSession.getSession().getAccountId() %>" type="hidden"/>
    <div class="main_mod main_mod_height">
        <div class="main_hd"><i class="icon"></i><span class="gray3 f18">确认收货地址</span></div>
        <div class="shipping_address">
            <ul id="addressList">
            </ul>
            <a href="#" class="btn01 ml40 mt10 mb10"  onclick="addAddress()">增加新地址</a>
        </div>
    </div>
    <div class="main_mod main_mod_height confirm_order">
    	<div class="main_hd"><i class="icon"></i><span class="gray3 f18">确认订单信息</span></div>
        <div class="order_info order_info_fivecol">
        	<div class="hd">
            	<ul class="clearfix">
                	<li class="td goods">商品信息</li>
                	<%if("score".equals(type)){ %>
                    <li class="td mod01">积分</li>
                    <%}else{ %>
                    <li class="td mod01">单价（元）</li>
                    <%} %>
                    <li class="td mod02">数量</li>
                    <%if("score".equals(type)){ %>
                    <li class="td mod04" style="width: 300px;">积分总计</li>
                    <%}else{ %>
                    <li class="td mod04" style="width: 300px;">小计（元）</li>
                    <%} %>
                    
                </ul>
            </div>
            <%
            for(ShoppingCarResult result : list){
            %>
            <div class="bd">
            	<ul class="clearfix">
                	<li class="td goods">
                        <img src="<%=StringHelper.isEmpty(result.goodsImg) ? controller.getStaticPath(request)+"/images/mall_list.jpg" : result.goodsImg %>">
                        <div class="title">
                        	<div class="name" title="<%=result.goodsName%>"><%StringHelper.filterHTML(out, result.goodsName); %></div>
                            <%-- <div class="arrow"></div>
                            <div class="full_name"><%StringHelper.filterHTML(out, result.desc); %></div> --%>
                        </div>
                    </li>
                    <%if("score".equals(type)){ %>
                    <li class="td mod01"><%=result.score %></li>
                    <%}else{ %>
                    <li class="td mod01"><%=Formater.formatAmount(result.amount) %></li>
                    <%} %>
                    <li class="td mod02" style="width: 100px;"><%=result.num %></li>
                    <%if("score".equals(type)){ %>
                    <li class="td mod04 highlight" style="width: 300px;"><%=result.score*result.num %></li>
                    <%}else{ %>
                    <li class="td mod04 highlight" style="width: 300px;"><%=Formater.formatAmount(result.amount.multiply(new BigDecimal(result.num))) %></li>
                    <%} %>
                </ul>
            </div>
            <%} %>
            
            <div class="tr border_d_b pt20 pb20">共<span id="totalNum"><%=totalNum %></span>件商品 
            	<%if("score".equals(type)){ %>
            	<span class="ml50">所需积分：<em class="highlight" id="ts"><%=totalScore %></em></span>
            	<%}else{ %>
            	<span class="ml50">总价：￥<em class="highlight" id="tp"><%=Formater.formatAmount(totalAmount) %></em></span>
            	<%} %>
            </div>
            <div class="border_d_b pt15 pb15 lh30">寄送至：   <span id="shdz" addressId=""></span><br />收货人：   <span id="shr"></span>    <span id="dh"></span></div>
            <div class="tr mt20"><a href="javascript:void(0)" onclick="showPayDiv()" class="btn07 ml20">确认<%="score".equals(type)?"兑换":"购买"%></a></div>
        </div>
    </div>
</div>
<div class="popup_bg" style="display: none;"></div>
<!--主体内容-->
<div id="info"></div>
<div id="address" style="display: none;">
<div class="dialog dialog_shipping_address">
    <div class="title"><a href="javascript:void(0)" class="out" onclick="closeAddress()"></a><span id="address_title">新增收货地址</span></div>
    <div class="content form1">
    <%-- <form action="<%=controller.getURI(request, SaveAddress.class) %>" method="post" class="form1" id="form1" onclick="return false;toSubmit()"> --%>
        <input id="shengId" value="<%=request.getParameter("sheng")%>" type="hidden"/>
        <input id="shiId" value="<%=request.getParameter("shi")%>" type="hidden"/>
        <input id="xianId" value="<%=request.getParameter("xian")%>" type="hidden"/>
        <input id="saveUrl" value="<%=controller.getURI(request, SaveAddress.class) %>" type="hidden"/>
        <input name="F01" value="" type="hidden"/>
        <input name="F08" value="no" type="hidden"/>
        <ul class="text_list">
            <li>
            	<div class="til"><span class="red">*</span> 收货人：</div>
                <div class="con">
                	<input name="F03" type="text" class="text_style required max-length-30" maxlength="30" value="" mtest="/^[\u4e00-\u9fa5]{2,}$/" mtestmsg="请输入合法姓名"/>
                    <p errortip class="red prompt" style="display: none"></p>
                </div>
            </li>
            <li>
            	<div class="til"><span class="red">*</span> 所在地区：</div>
                <div class="con" id="region">
                	<select name="sheng" class="select_style required">
                	  <option>请选择</option>
                	</select>
                    <select name="shi" class="select_style required">
                	  <option>请选择</option>
                	</select>
                    <select name="xian" class="select_style required" mtestmsg="请填写完整的地区信息">
                	  <option>请选择</option>
                	</select>
                    <p errortip class="red prompt" style="display: none"></p>
              </div>
            </li>
            <li>
            	<div class="til"><span class="red">*</span> 详细地址：</div>
                <div class="con">
                	<input name="F05" type="text" class="text_style required max-length-100" maxlength="100"  value="" mtestmsg="请填写收货人详细地址"/>
                    <p errortip class="red prompt" style="display: none"></p>
                </div>
            </li>
            <li>
            	<div class="til"><span class="red">*</span> 手机号码：</div>
                <div class="con">
                	<input name="F06" type="text" class="text_style required telephone max-length-11" maxlength="11" value="" mtestmsg="请填写收货人手机号码"/>
                    <p errortip class="red prompt" style="display: none"></p>
                </div>
            </li>
            <li>
            	<div class="til">邮编：</div>
                <div class="con">
                	<input name="F07" type="text" class="text_style postcode" maxlength="6" onkeyup="value=value.replace(/\D/g,'')" value=""/>
                	<p errortip class="red prompt" style="display: none"></p>
                </div>
            </li>
            <li>
            	<div class="til">&nbsp;</div>
                <div class="con"><input name="setDefault" type="checkbox" value="" /> 设为默认地址</div>
            </li>
        </ul>
		<div class="tc mt20">
			<input type="button" class="btn01 sumbitForme" onclick="saveAddress()" fromname="form1" value="提交"/>
            <input type="button" class="btn01 btn_gray" style="cursor: pointer;" onclick="closeAddress()" value="取消">
		</div>
		<!-- </form> -->
    </div>
</div>
</div>

<!-- 购买弹窗 -->
	<div class="dialog" id="buyDiv" style="display: none;">

	    <div class="title"><a href="javascript:closeInfo();" class="out dialog_close"></a>提示</div>
	    <div class="content">
	    	<div class="tc f16 gray3 mb15">共<span id="commNum"></span>件商品，<span id="needStr">所需积分</span>：<span class="red" id="price"></span></div>
            <form action="<%=configureProvider.format(URLVariable.SCOREORDER_PAYMENT_URL)%>" method="post" id="tg_form">
                <input id="payType" name="payType" value="<%=type %>" type="hidden"/>
                <input id="goodsList" name="goodsList" value="<%=goodsList %>" type="hidden"/>
                <input id="addressId" name="addressId" value="" type="hidden" />
                <input name="source" value="<%StringHelper.filterHTML(out,request.getParameter("source"));%>" type="hidden" />
             </form>
	    	<%
		    boolean tg = BooleanParser.parse(configureProvider.getProperty(SystemVariable.SFZJTG));
	    	String  escrowPrefix = configureProvider.getProperty(SystemVariable.ESCROW_PREFIX);
		        boolean isOpenPwd = BooleanParser.parse(configureProvider.getProperty(PayVariavle.CHARGE_MUST_WITHDRAWPSD));
		        if (isOpenPwd)
		        {
		%>
	        <ul class="text_list">
	            <li>
	            	<div class="til"><span class="red">*</span> 交易密码：</div>
	                <div class="con">
	                	<input type="password" class="text_style required" id="tran_pwd" autocomplete="off" maxlength="20"/>
	                	<p class="red" style="display: none"></p>
	                	<input type="hidden" name="tranPwd" id="tranPwd" />
	                </div>
	            </li>
	        </ul>
	        <%} %>
			<div class="tc mt20">
			<%if("score".equals(type) || !tg || "FUYOU".equals(escrowPrefix)){ %>
                <input type="button" id="ok" value="确定" class="btn01 mb15 mt15" />
        	  <%}else if(tg && "baofu".equals(escrowPrefix)){ %>
                <input type="button" id="ok" value="确定" class="btn01 mb15 mt15" />
                <%} else{ %>
                <input type="button" value="确定" onclick="tgSubmit();" class="btn01 mb15 mt15" />
                <%} %>
                <input type="button" onclick="closeInfo()"  value="取消" class="btn01 ml15 btn_gray cancelgm dialog_close" />
	        </div>
	    </div>

	</div>
	<input type="hidden" name="isTG" id="isTG"
       value="<%=tg%>"/>
<input type="hidden" name="isOpenWithPsd" id="isOpenWithPsd"
       value="<%=isOpenPwd%>"/>
       <input type="hidden" name="escrowPrefix" id="escrowPrefix"
       value="<%=escrowPrefix%>"/>
<%@include file="/WEB-INF/include/footer.jsp" %>

<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/dialog.js"></script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/validation.js"></script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/jquery-jtemplates.js"></script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/mall/confirmOrder.js"></script>
<script type="text/javascript"
        src="<%=controller.getStaticPath(request)%>/js/region.js"></script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/security.js"></script>
<script type="text/javascript">
	var modulus = "<%=modulus%>", exponent = "<%=exponent%>";
	var key = RSAUtils.getKeyPair(exponent, '', modulus);
	var regionUpdate = '<input id="shengId" value="" type="hidden"/>';
	regionUpdate +='<input id="shiId" value="" type="hidden"/>';
	regionUpdate +='<input id="xianId" value="" type="hidden"/>';
	regionUpdate += '<select name="sheng" id="sheng" class="select6 required"></select>';
	regionUpdate += '<select name="shi" id="shi" class="select6"></select>';
	regionUpdate += '<select name="xian" id="xian" class="select6"></select>';
	regionUpdate += '<p errortip class="prompt" style="display: none"></p>';
    function tgSubmit(){
        $("#tg_form").submit();
    }
</script>
</body>
</html>