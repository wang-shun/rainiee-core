<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.util.StringHelper"%>
<%@page import="com.dimeng.p2p.variables.defines.URLVariable"%>
<%@page import="com.dimeng.p2p.front.servlets.financing.sbtz.CheckPsd"%>
<%@page import="org.bouncycastle.util.encoders.Hex"%>
<%@page import="com.dimeng.p2p.common.DimengRSAPulicKey"%>
<%@page import="com.dimeng.p2p.service.PtAccountManage"%>
<%@page import="com.dimeng.p2p.variables.defines.pays.PayVariavle"%>
<%@page import="com.dimeng.p2p.front.servlets.mall.ConfirmOrder"%>
<%@page import="com.dimeng.p2p.repeater.score.entity.UserAccount"%>
<%@page import="com.dimeng.p2p.front.servlets.mall.AddCar"%>
<%@page import="com.dimeng.p2p.front.servlets.mall.MallIndex"%>
<%@page import="com.dimeng.p2p.S63.enums.T6350_F07"%>
<%@page import="com.dimeng.p2p.front.servlets.mall.PtscXq"%>
<%@page import="com.dimeng.p2p.common.enums.YesOrNo"%>
<%@page import="com.dimeng.p2p.S63.enums.T6351_F11"%>
<%@page import="com.dimeng.p2p.repeater.score.entity.T6351Ext"%>
<%@page import="com.dimeng.p2p.repeater.score.ScoreCommodityManage"%>
<%@page import="java.math.BigDecimal" %>
<%@ page import="java.util.Map" %>
<%@ page import="com.dimeng.util.parser.BooleanParser" %>
<html>
<head>
<title><%=configureProvider.getProperty(SystemVariable.SITE_TITLE)%>
</title>
<%@include file="/WEB-INF/include/meta.jsp"%>
<%@include file="/WEB-INF/include/style.jsp"%>
</head>

<body>
	<%@include file="/WEB-INF/include/header.jsp"%>
	<%
	ScoreCommodityManage scoreCommodityManage = serviceSession.getService(ScoreCommodityManage.class);
	final int id = IntegerParser.parse(request.getParameter("id"));
	T6351Ext commodityDetails = scoreCommodityManage.getCommodityObject(id);
	if(commodityDetails == null || commodityDetails.F11 == T6351_F11.unsold){
	    response.sendError(HttpServletResponse.SC_NOT_FOUND);
	    return;
	}
	int butTotal = scoreCommodityManage.getBuyTotal(id);
	PtAccountManage ptAccountManage = serviceSession.getService(PtAccountManage.class);
	DimengRSAPulicKey publicKey = ptAccountManage.getPublicKey();
	String modulus = new String(Hex.encode(publicKey.getModulus().toByteArray()));
	String exponent = new String(Hex.encode(publicKey.getPublicExponent().toByteArray()));
	MallChangeManage changeManage = serviceSession.getService(MallChangeManage.class);
	//已购买此商品数量
	Integer buyGoodsNum = 0;
	//商品成交次数
	//Integer buyGoodsTimes = changeManage.queryBuyGoodsTimes(id);
	String type = (String)request.getAttribute("type");
	//支持余额购买常量
	boolean allowsBalance = BooleanParser.parse(configureProvider.getProperty(MallVariavle.ALLOWS_THE_BALANCE_TO_BUY));
	%>
	<form  id="mainForm" action="<%=controller.getURI(request, ConfirmOrder.class) %>" method="post">
	<!--主体内容-->
	<div class="main_bg">
		<input name="singleScore" value="<%=commodityDetails.F05 %>" type="hidden" /> 
		<input name="commodityId" value="<%=commodityDetails.F01 %>" type="hidden" /> 
		<input name="singlePrice" value="<%=commodityDetails.F15 %>" type="hidden" /> 
		<input name="xgsl" value="<%=commodityDetails.F18 %>" type="hidden" /> 
		<input name="score" type="hidden" value="<%=commodityDetails.F05 %>" />
		<input name="price" type="hidden" value="<%=commodityDetails.F15 %>" />
		<input id="isScore" type="hidden" value="<%=commodityDetails.F16 %>" />
		<input id="isPrice" type="hidden" value="<%=commodityDetails.F17 %>" />
		<input name="payType" type="hidden" id="payType"/>
        <input name="goodsList" type="hidden"/>
		<input name="fromUrl" type="hidden" value="<%=controller.getViewURI(request, PtscXq.class)%>"/>
        <%if (dimengSession != null && dimengSession.isAuthenticated()) {
          UserAccount account = changeManage.queryAccount();
          buyGoodsNum = changeManage.queryBuyGoodsNum(id);
          %>
        <input name="totalScore" type="hidden" value="<%=account==null?0:account.totalScore%>"/>
        <input name="balance" type="hidden" value="<%=account==null?0:account.balance%>"/>
        <%} %>
        <input name="buyGoodsNum" type="hidden" value="<%=buyGoodsNum %>"/>
		<input name="ruleId" type="hidden" id="ruleId" value="<%=commodityDetails.F20%>"/>
		<div class="mall_details_top clearfix">
			<div class="pic">
				<img src="<%=StringHelper.isEmpty(commodityDetails.F08) ? controller.getStaticPath(request)+"/images/mall_pic_big.jpg" : fileStore.getURL(commodityDetails.F08) %>" />
			</div>
			<div class="info">
				<h1 title="<%=commodityDetails.F03 %>"><%=commodityDetails.F03 %></h1>
				<ul>
					<li>所属类别：<%=commodityDetails.commTypeName %></li>
					<%if(YesOrNo.yes == commodityDetails.F16){%>
					<li>积分：<span class="highlight f18" id="castScore"><%=commodityDetails.F05%></span></li>
					<%}%>
					<%if(YesOrNo.yes == commodityDetails.F17 && allowsBalance){%>
					<li>价格：<span class="highlight f18" id="castPrice">￥<%=commodityDetails.F15%></span></li>
					<%}%>
					<%if(commodityDetails.F20 > 0){%>
					<li>规则说明：<%StringHelper.filterHTML(out,commodityDetails.rule);%></li>
					<%}%>
					<%if(commodityDetails.F19.compareTo(BigDecimal.ZERO)!=0){%>
					<li><span style="text-decoration:line-through;">市场参考价：<%=commodityDetails.F19 %></span></li>
					<%}%>
					<li>成交笔数：<%=commodityDetails.F07 %>笔
					</li>
					<li>库存量：<span id="totalCount"><%=commodityDetails.F06 %></span>件
					</li>
					<li>限购数量：<%if(commodityDetails.F18==0){%>不限购<%}else{%><%=commodityDetails.F18 %>件<%}%>
					</li>
					<li><span style="vertical-align: middle;">购买数量：</span><span
						class="choose"><a href="javascript:void(0)" class="reduce"
							onclick="minusCount();">-</a> <input name="number" id="number_id"
							type="text" value="1" maxlength="7" validate="q" onKeyUp="value=value.replace(/\D/g,'');calc(this.value);"
							reg="/^\+?[1-9][0-9]*$/" warning="必须为非0正整数"/>
						<%--下面这个输入框用于防止回车键提交表单--%>
						<input type="text" style="display: none">
						<a
							href="javascript:void(0)" class="add" onclick="addCount();">+</a></span><span id="num_error_info" class="red ml10"></span></li>
				</ul>

				<%if(commodityDetails.commTypeEnum == T6350_F07.kind){ %>
					<%if (dimengSession != null && dimengSession.isAuthenticated()) { %>
					<%
						com.dimeng.p2p.service.UserInfoManage userCommManage = serviceSession.getService(com.dimeng.p2p.service.UserInfoManage.class);
						Map<String, String> retMap = userCommManage.checkAccountInfo();
						T6110 t6110 = userCommManage.getUserInfo(serviceSession.getSession().getAccountId());
						boolean checkFlag = BooleanParser.parse(retMap.get("checkFlag"));
						String checkMessage = retMap.get("checkMessage");
						String rzUrl = retMap.get("rzUrl");

					%>
					<script type="text/javascript">
						var auth = false;
						<%if(checkFlag){%>auth = true;<%}%>
						var authText = "<%=checkMessage %>";
						function checkAuth() {
							if (auth) {
								return true;
							}
							$("#con_error").html(authText);
							$("#dialog").show();
							$("#dialog #doalogClick").attr("href","<%=rzUrl %>");
							return false;
						}
						function closeDiv() {
						    if (!$("#errorico").hasClass("doubt")) {
						        $("#errorico").removeClass();
						        $("#errorico").addClass("doubt");
						    }
						    $("#dialog").hide();
						}
					   </script>
						<%if(t6110.F06 == T6110_F06.ZRR && T6110_F07.HMD != t6110.F07){%>
						<div class="pt10">
						    <%if(commodityDetails.F11 == T6351_F11.sold){ %>
								<%if(YesOrNo.yes == commodityDetails.F16){%>
								  <a class="btn07" href="javascript:;" id="ButtonByScore">立即兑换</a>
								<%}%>
								<%if(YesOrNo.yes == commodityDetails.F17 && allowsBalance){%>
								  <a class="btn07 <%if(YesOrNo.yes == commodityDetails.F16){%> ml20 <%}%>" id="ButtonLjgmSp" href="javascript:void(0);">立即购买</a>
								<%}%>
							  <a class="btn07 ml20 buy_btn" id="ButtonJrgwc" href="javascript:void(0);">加入购物车</a>
						    <%}else {%>
								<%if(YesOrNo.yes == commodityDetails.F16){%>
								  <a class="btn07 btn_gray btn_disabled" href="javascript:;">立即兑换</a>
								<%}%>
								<%if(YesOrNo.yes == commodityDetails.F17 && allowsBalance){%>
								  <a class="btn07 <%if(YesOrNo.yes == commodityDetails.F16){%> ml20 <%}%> btn_gray btn_disabled" href="javascript:;">立即购买</a>
								<%}%>
							  <a class="btn07 ml20 buy_btn buy_btn_gray btn_disabled" href="javascript:;">加入购物车</a>
						    <%}%>
						</div>
					<%}else{%>
						<div class="btn pt10">
							<%if(YesOrNo.yes == commodityDetails.F16){%>
							<a class="btn07 btn_gray btn_disabled" href="javascript:;">立即兑换</a>
							<%}%>
							<%if(YesOrNo.yes == commodityDetails.F17 && allowsBalance){%>
							<a class="btn07 <%if(YesOrNo.yes == commodityDetails.F16){%> ml20 <%}%>btn_gray btn_disabled" href="javascript:;">立即购买</a>
							<%}%>
							<a class="btn07 ml20 buy_btn buy_btn_gray btn_disabled"  href="javascript:void(0)">加入购物车</a>
						</div>
					<%}%>
					<%}else {%>
						<div class="btn pt10">
							<%if(YesOrNo.yes == commodityDetails.F16){%>
								<a class="btn07 btn_gray btn_disabled" href="javascript:;">立即兑换</a>
							<%}%>
							<%if(YesOrNo.yes == commodityDetails.F17 && allowsBalance){%>
								<a class="btn07 <%if(YesOrNo.yes == commodityDetails.F16){%> ml20 <%}%>btn_gray btn_disabled" href="javascript:;">立即购买</a>
							<%}%>
							<a class="btn07 ml20 buy_btn buy_btn_gray btn_disabled" style="cursor: pointer;" href="<%configureProvider.format(out, URLVariable.LOGIN);%>">加入购物车</a>
						</div>
					<%}%>
				<%}else {%>
					<%if (dimengSession != null && dimengSession.isAuthenticated() && commodityDetails.F11 == T6351_F11.sold) { %>
					<%
						com.dimeng.p2p.service.UserInfoManage userCommManage = serviceSession.getService(com.dimeng.p2p.service.UserInfoManage.class);
						Map<String, String> retMap = userCommManage.checkAccountInfo();
						T6110 t6110 = userCommManage.getUserInfo(serviceSession.getSession().getAccountId());
						boolean checkFlag = BooleanParser.parse(retMap.get("checkFlag"));
						String checkMessage = retMap.get("checkMessage");
						String rzUrl = retMap.get("rzUrl");
					%>
					<script type="text/javascript">
					var auth = false;
					<%if(checkFlag){%>auth = true;<%}%>
					var authText = "<%=checkMessage %>";
					function checkAuth() {
						if (auth) {
							return true;
						}
						$("#con_error").html(authText);
						$("#dialog").show();
						$("#dialog #doalogClick").attr("href","<%=rzUrl %>");
						return false;
					}
					function closeDiv() {
					    if (!$("#errorico").hasClass("doubt")) {
					        $("#errorico").removeClass();
					        $("#errorico").addClass("doubt");
					    }
					    $("#dialog").hide();
					}
					</script>
					<%if(t6110.F06 == T6110_F06.ZRR && T6110_F07.HMD != t6110.F07){%>
						<div class="pt20">
							<%if(YesOrNo.yes == commodityDetails.F16){%>
								<a class="btn07 " href="javascript:;" id="ButtonByScoreHf">立即兑换</a>
							<%}%>
							<%if(YesOrNo.yes == commodityDetails.F17 && allowsBalance){%>
								<a class="btn07  <%if(YesOrNo.yes == commodityDetails.F16){%> ml20 <%}%> " id="ButtonLjgmHf" href="javascript:void(0);">立即购买</a>
							<%}%>
						</div>
					<%}else{%>
						<%if(YesOrNo.yes == commodityDetails.F16){%>
						<a class="btn07 btn_gray btn_disabled" href="javascript:;">立即兑换</a>
						<%}%>
						<%if(YesOrNo.yes == commodityDetails.F17 && allowsBalance){%>
						<a class="btn07 <%if(YesOrNo.yes == commodityDetails.F16){%> ml20 <%}%> btn_gray btn_disabled" href="javascript:;">立即购买</a>
						<%}%>
					<%}%>
					<%}else {%>
						<div class="btn pt10">
							<%if(YesOrNo.yes == commodityDetails.F16){%>
								<a class="btn07 btn_gray btn_disabled" href="javascript:;">立即兑换</a>
							<%}%>
							<%if(YesOrNo.yes == commodityDetails.F17 && allowsBalance){%>
								<a class="btn07 <%if(YesOrNo.yes == commodityDetails.F16){%> ml20 <%}%> btn_gray btn_disabled" href="javascript:;">立即购买</a>
							<%}%>
						</div>
					<%}%>
				<%}%>
				</div>
				<%if (!(dimengSession != null && dimengSession.isAuthenticated())) { %>
				<div class="clear"></div>
				<div class="not_logged_in">
					请立即 <a href="<%configureProvider.format(out,URLVariable.LOGIN);%>?_z=/mall/ptscXq/<%=id %>.html" class="highlight">登录</a> 或 <a href="<%configureProvider.format(out,URLVariable.REGISTER);%>"
						class="highlight">注册</a>
				</div>
				<%}%>
				</div>
		<div class="mall_details">
			<div class="main_tab">
				<ul class="clearfix">
					<li id="spxq" class="hover">商品详情</li>
					<li id="gmjl">换购记录(<%=commodityDetails.recordNum%>)</li>
				</ul>
			</div>
			<div id="dataHtml" class="bd" ></div>
		</div>
	</div>
	</form>
	<!--主体内容-->
	<%@include file="/WEB-INF/include/footer.jsp"%>
	<div id="dialog" style="display: none;">
    <div class="popup_bg"></div>
    <div class="dialog">
        <div class="title"><a href="javascript:void(0);" class="out" onclick="closeDiv()"></a>提示</div>
        <div class="content">
            <div class="tip_information">
                <div id="errorico" class="doubt"></div>
                <div class="tips">
                    <span class="f20 gray3" id="con_error"></span>
                </div>
            </div>
            <div class="tc mt20">
				<a href="javascript:void(0);" class="btn01" id="doalogClick">确定</a>
			</div>
        </div>
    </div>
</div>
	<div id="info"></div>
	<div class="popup_bg" style="display: none;"></div>
	<!-- 加入购物车弹窗 -->
	<div class="dialog" id="gwc" style="display: none;">
    <div class="title"><a href="javascript:void(0);" class="out dialog_close"></a>提示</div>
	    <div class="content">
	    	<div class="tip_information"> 
	            <div class="successful"></div>
	            <div class="tips">
	                <span class="f20">商品已成功加入购物车！<br />您可以去购物车结算，或继续购物</span>
	            </div> 
	        </div>
			<div class="tc mt10"><a href="<%configureProvider.format(out, URLVariable.MALLSHOPPINGCAR_URL);%>" class="btn01">去购物车结算</a><a href="<%=controller.getURI(request, MallIndex.class)%>" class="btn01 ml20">继续购物</a></div>
	    </div>
	</div>
	<!-- 积分购买不足 -->
	<div class="dialog" id="jfbz" style="display: none;">
    <div class="title"><a href="javascript:void(0);" class="out dialog_close"></a>提示</div>
	    <div class="content">
	    	<div class="tip_information"> 
	            <div class="doubt"></div>
	            <div class="tips">
	                <span class="f20">您的积分不足！</span>
	            </div> 
	        </div>
			<div class="tc mt10"><a href="<%configureProvider.format(out, URLVariable.MYSCORE_URL);%>" class="btn01">查看我的积分</a><a href="javascript:void(0);" class="btn01 ml20 btn_gray cancelgm dialog_close">取消</a></div>
	    </div>
	</div>
	<!-- 余额购买不足 -->
	<div class="dialog" id="yebz" style="display: none;">
    <div class="title"><a href="javascript:void(0);" class="out dialog_close"></a>提示</div>
	    <div class="content">
	    	<div class="tip_information"> 
	            <div class="doubt"></div>
	            <div class="tips">
	                <span class="f20">您的可用余额不足！</span>
	            </div> 
	        </div>
			<div class="tc mt10"><a href="<%configureProvider.format(out, URLVariable.USER_CHARGE);%>" class="btn01">去充值</a><a href="javascript:void(0);" class="btn01 ml20 btn_gray cancelgm dialog_close">取消</a></div>
	    </div>
	</div>
	<!-- 商品详情-购买弹窗 -->
	<!-- <div class="dialog" id="spxqgm" style="display: none;">
	    <div class="title"><a href="javascript:void(0);" class="out dialog_close"></a>提示</div>
	    <div class="content">
	    	<div class="tc f16 gray3 mb15">共<span id="commNum"></span>件商品，所需积分：<span class="red" id="commScore"></span></div>
	        <ul class="text_list">
	            <li>
	            	<div class="til"><span class="red">*</span> 交易密码：</div>
	                <div class="con">
	                	<input type="text" class="text_style required" id="tran_pwd" autocomplete="off"/>
	                	<p class="red" style="display: none"></p>
	                </div>
	            </li>
	        </ul>
			<div class="tc mt20"><a href="javascript:void(0);" id="okgm" class="btn01">确定</a><a href="javascript:void(0);" class="btn01 ml20 btn_gray cancelgm dialog_close">取消</a></div> 
	    </div>
	</div> -->
	<!-- 话费详情弹窗 -->
	<div class="dialog" id="hfgm" style="display: none;">
	<form id="hfForm" action="<%=configureProvider.format(URLVariable.SCOREORDER_PAYMENT_URL)%>" method="post" onsubmit="return onSubmit();" onkeydown="if(event.keyCode==13)return false;">
	    <div class="title"><a href="javascript:void(0);" class="out dialog_close"></a>提示</div>
	    <div class="content">
	        <ul class="text_list">
	        	<li>
	            	<div class="til">商品名称：</div>
	                <div class="con f16 gray3"><%=commodityDetails.F03 %></div>
	            </li>
	            <li>
	            	<div class="til" id="needStr"></div>
	                <div class="con f16 red" id="scoreFree"></div>
	            </li>
				<%if(commodityDetails.F20 <= 0){%>
	            <li id="phone">
	            	<div class="til"><span class="red">*</span> 充值手机号码：</div>
	                <div class="con">
	                	<input type="hidden" id="type" name="payType" value="" />
	                	<input type="hidden" id="num" name="num" value="" />
	                	<input type="hidden" id="goodsList" name="goodsList" value="" />
		                <input name="mobile" id="mobile" type="text" class="text_style required" maxlength="11" value="" onblur="checkMobile()"/>
		                <p class="red" style="display: none"></p>
	                </div>
	            </li>
				<%}%>
	            <%
		        boolean tg = BooleanParser.parse(configureProvider.getProperty(SystemVariable.SFZJTG));
	            String  escrowPrefix = configureProvider.getProperty(SystemVariable.ESCROW_PREFIX);
		        boolean isOpenPwd = BooleanParser.parse(configureProvider.getProperty(PayVariavle.CHARGE_MUST_WITHDRAWPSD));
		        if (isOpenPwd)
		        { %>
	            <li>
	            	<div class="til"><span class="red">*</span> 交易密码：</div>
	                <div class="con">
		                <input type="password" class="text_style required" id="tran_pwd" autocomplete="off" onblur="checkPsd()"/>
		                <p class="red" style="display: none"></p>
		                <input type="hidden" name="tranPwd" id="tranPwd" />
	                </div>
	            </li>
	            <%} %>
	        </ul>
			<div id="escrow" class="tc mt20">
	        	<%if(!tg || "score".equals(type) || "FUYOU".equals(escrowPrefix)){ %>
					<input type="button" id="payok" value="确定" class="btn01 mb15 mt15 payBtn" />
	        	  <%}else if(tg && ("baofu".equals(escrowPrefix))){ %>
					<input type="button" id="payok" value="确定" class="btn01 mb15 mt15 payBtn" />
	        	 <%} else{ %>
	        		<input type="submit" value="确定" class="btn01 mb15 mt15 " />
	        	 <%} %>
				<input type="button" id="hfCancel" value="取消" class="btn01 ml20 btn_gray cancelgm dialog_close" />
	        </div>
	        
	        <div id="pt" class="tc mt20">
				<input type="button" id="payok" value="确定" class="btn01 mb15 mt15 payBtn" />	        	  
				<input type="button" id="hfCancel" value="取消" class="btn01 ml20 btn_gray cancelgm dialog_close" />
	        </div>
	    </div>
    </form>
	</div>
<input type="hidden" name="isTG" id="isTG" value="<%=tg%>"/>
<input type="hidden" name="isOpenWithPsd" id="isOpenWithPsd" value="<%=isOpenPwd%>"/>
<input type="hidden" name="escrowPrefix" id="escrowPrefix" value="<%=escrowPrefix%>"/>
<input id="accountId" value="<%=dimengSession == null || !dimengSession.isAuthenticated()? "":serviceSession.getSession().getAccountId() %>" type="hidden"/>

	<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/commodity/commodityDetail.js"></script>
	<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/dialog.js"></script>
	<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/security.js"></script>

 <input id="myOrderUrl" value="<%configureProvider.format(out, URLVariable.MYORDER_URL);%>" type="hidden"/> 
<%
	String message = controller.getPrompt(request, response , PromptLevel.INFO);
	if (!StringHelper.isEmpty(message)) {%>
		<script type="text/javascript">
			$("#info").html(showSuccInfo("<%=message%>","successful",$("#myOrderUrl").val()));	
			$("div.popup_bg").show();
		</script>
<%}%>
<%
	String errorMessage = controller.getPrompt(request, response , PromptLevel.ERROR);
	if (!StringHelper.isEmpty(errorMessage)) {%>
		<script type="text/javascript">
			$("#info").html(showDialogInfo("<%=errorMessage%>","error"));	
			$("div.popup_bg").show();
		</script>
<%}%>
<%
	String warnMessage = controller.getPrompt(request, response , PromptLevel.WARRING);
	if (!StringHelper.isEmpty(warnMessage)) {%>
		<script type="text/javascript">
			$("#info").html(showDialogInfo("<%=warnMessage%>","doubt"));
			$("div.popup_bg").show();
		</script>
<%}%>
<script type="text/javascript">
		var modulus = "<%=modulus%>", exponent = "<%=exponent%>";
		var key = RSAUtils.getKeyPair(exponent, '', modulus);
		var _dataUrl = "<%=controller.getURI(request, PtscXq.class)%>";
		var _addCarUrl = "<%=controller.getURI(request, AddCar.class)%>";
		var _buyUrl = "<%configureProvider.format(out, URLVariable.SCOREORDER_PAYMENT_URL);%>";
		var _myOrderUrl = "<%configureProvider.format(out, URLVariable.MYORDER_URL);%>";
		var _checkUrl = "<%=controller.getURI(request, CheckPsd.class)%>";
		var _commId = "<%=id%>";
		var currentPage = 1;
		var pageSize = 10;
		var pageCount = 0;
		$(function(){
			initData("spxq");
			$(".main_tab ul li").click(function(){
				currentPage = 1;
				initData($(this).attr("id"));
			});
		});
		
		function initData(type){
			$(".main_tab ul li").removeClass("hover");
			$("#"+type).addClass("hover");
			$.post(_dataUrl,{type:type,id:_commId,pageSize:pageSize,currentPage:currentPage},function(returnData){
				returnData = eval("("+returnData+")");
				if(type == "spxq"){
					initSpxqData(returnData);
				}else if (type == "gmjl"){
					initGmjlData(returnData);
				}
			});
		}
		/**
		 * 商品详情
		 */
		function initSpxqData(returnData){
			$("#dataHtml").html("");
			$("#pageContent").html("");
			var div = "";
			if(returnData!=null){
				div += "<div style=\"overflow-x: auto;\">"+returnData.spxqData.F10+"</div>";
			}
			$("#dataHtml").html(div);
		}
		
		//购买记录
		function initGmjlData(returnData){
			$("#dataHtml").html("");
			
			var div = "<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" class=\"table mt10\">";
			div += "<tr class=\"til\">";
			div += "<td width=\"23%\" align=\"center\">用户</td>";
			div += "<td width=\"18%\" align=\"center\">数量</td>";
			div += "<td width=\"29%\" align=\"center\">价值</td>";
			div += "<td width=\"30%\" align=\"center\">换购时间</td>";
			div += "</tr>";
			if(returnData.ScoreOrderInfoList != null && returnData.ScoreOrderInfoList.length > 0){
				for(var i = 0 ; i < returnData.ScoreOrderInfoList.length ; i++ ){
					var buyRecord = returnData.ScoreOrderInfoList[i];
					var loginName = buyRecord.loginName;
					div += "<tr>";
					div += "<td align=\"center\">"+loginName.substring(0, 2) + "******" + loginName.substring(loginName.length - 2, loginName.length)+"</td>";
					div += "<td align=\"center\">"+buyRecord.comdNum+"</td>";
					div += "<td align=\"left\">"+(buyRecord.payment=='score' ?('<i class=\"integral_ico\"></i>'+buyRecord.comdScore) : ('<i class=\"price_ico\"></i>'+buyRecord.comdPrices))+"</td>";
					div += "<td align=\"center\">"+formatTime(buyRecord.orderTime)+"</td>";
					div += "</tr>";
				}
			}else{
				div += "<tr><td colspan=\"5\" align=\"center\">暂无数据</td></tr>";
			}
			div += "</table>";
			/* 分页信息 */
			pageCount = returnData.pageCount;
			if(returnData.ScoreOrderInfoList!=null){
				div += "<div>"+returnData.pageStr+"</div>";
			}else{
				div += "<div></div>";
			} 
			$("#dataHtml").append(div);
			/* 分页点击事件 */
			$("a.page-link").click(function(){
				pageParam(this);
			});
		}
		
		function pageParam(obj){
			if($(obj).hasClass("cur")){
				return false;
			}
			currentPage = parseInt(currentPage);
			$(obj).addClass("cur");
			$(obj).siblings("a").removeClass("cur");
			if($(obj).hasClass("startPage")){
				currentPage = 1;
			}else if($(obj).hasClass("prev")){
				currentPage = parseInt(currentPage) - 1;
			}else if($(obj).hasClass("next")){
				currentPage = parseInt(currentPage) + 1;
			}else if($(obj).hasClass("endPage")){
				currentPage = pageCount;
			}else{
				currentPage = parseInt($(obj).html());
			}
			initData("gmjl");
		}
		
		var formatTime = function (time) {  
			if(time==null){
				return "--";
			}
			var date = new Date();
			date.setTime(time);
		    var y = date.getFullYear();  
		    var m = date.getMonth() + 1;  
		    m = m < 10 ? '0' + m : m;  
		    var d = date.getDate();  
		    d = d < 10 ? ('0' + d) : d;  
		    var h = date.getHours();
		    h = h < 10 ? ('0' + h) : h;  
		    var mm = date.getMinutes();
		    mm = mm < 10 ? ('0' + mm) : mm;
		    return y + '-' + m + '-' + d +" " + h + ":" + mm;  
		};
		
		function toAjaxPage(obj){
			initData("gmjl");
		}
</script>

</body>
</html>
