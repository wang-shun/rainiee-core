<%@page import="com.dimeng.p2p.S61.enums.T6110_F17"%>
<%@page import="com.dimeng.p2p.modules.bid.front.service.entity.Zqzqlb"%>
<%@page import="com.dimeng.p2p.modules.bid.front.service.entity.Zqzrxx"%>
<%@page import="com.dimeng.p2p.S61.enums.T6147_F04"%>
<%@page import="com.dimeng.p2p.variables.defines.pays.PayVariavle"%>
<%@page import="com.dimeng.p2p.S61.entities.T6101"%>
<%@page import="com.dimeng.p2p.S61.entities.T6110"%>
<%@page import="com.dimeng.p2p.S61.enums.T6110_F06"%>
<%@page import="com.dimeng.p2p.S61.enums.T6110_F07"%>
<%@page import="com.dimeng.p2p.S62.entities.T6231"%>
<%@page import="com.dimeng.p2p.S62.entities.T6250"%>
<%@page import="com.dimeng.p2p.S61.entities.T6103"%>
<%@page import="com.dimeng.p2p.S63.entities.T6342"%>
<%@page import="com.dimeng.p2p.modules.bid.front.service.entity.FrontT6250"%>
<%@page import="com.dimeng.p2p.S62.enums.*"%>
<%@page import="com.dimeng.p2p.account.front.service.UserInfoManage"%>
<%@page import="com.dimeng.p2p.account.front.service.UserManage"%>
<%@page import="com.dimeng.p2p.account.user.service.MyExperienceManage"%>
<%@page import="com.dimeng.p2p.common.FormToken"%>
<%@page import="com.dimeng.p2p.S62.entities.T6216" %>
<%@page import="com.dimeng.p2p.front.servlets.TyjAmountLoan"%>
<%@page import="com.dimeng.p2p.modules.bid.front.service.BidManage"%>
<%@page import="com.dimeng.p2p.modules.bid.front.service.entity.Bdxq"%>
<%@page import="com.dimeng.p2p.modules.bid.front.service.entity.Mbxx"%>
<%@page import="java.util.Calendar"%>
<%@page import="com.dimeng.p2p.common.RSAUtils" %>
<%@page import="org.bouncycastle.util.encoders.Hex" %>
<%@page import="com.dimeng.p2p.service.PtAccountManage" %>
<%@page import="com.dimeng.p2p.common.DimengRSAPulicKey" %>
<%@page import="java.util.*"%>
<%@page import="com.dimeng.p2p.account.user.service.entity.MyRewardRecod"%>
<%@page import="com.dimeng.p2p.S63.enums.T6340_F03"%>
<%@page import="com.dimeng.p2p.S63.enums.T6342_F04"%>
<%@page import="com.dimeng.p2p.account.user.service.MyRewardManage"%>
<%@page import="org.codehaus.jackson.map.ObjectMapper"%>
<%@page import="com.dimeng.p2p.modules.base.front.service.TermManage" %>
<%@page import="com.dimeng.p2p.S50.entities.T5017" %>
<%@page import="com.dimeng.p2p.common.enums.TermType" %>
<%@page import="com.dimeng.p2p.front.servlets.Term" %>
<%@page import="com.dimeng.p2p.S61.entities.T6147"%>
<%@page import="com.dimeng.p2p.S61.entities.T6148"%>
<%@page import="com.dimeng.p2p.S61.enums.T6148_F02"%>
<%@page import="com.dimeng.p2p.S61.enums.T6147_F04"%>
<%@page import="com.dimeng.p2p.common.RiskLevelCompareUtil"%>
<%@ page import="java.sql.Timestamp" %>
<%
	BidManage investManage = serviceSession.getService(BidManage.class);
	int id = IntegerParser.parse(request.getParameter("id"));
	Zqzqlb zqInfo = investManage.getZqzrXq(id);
	if(null == zqInfo)
	{
		response.sendError(HttpServletResponse.SC_NOT_FOUND );
		return;
	}
	Bdxq creditInfo = investManage.get(zqInfo.bidId);
	if(creditInfo == null){
		response.sendError(HttpServletResponse.SC_NOT_FOUND );
		return;
	}
	T6231 t6231 = investManage.getExtra(zqInfo.bidId);
	BigDecimal zxMoney = new BigDecimal(0);
	BigDecimal zfMoney = new BigDecimal(0);
	UserInfoManage userInfoManage = serviceSession.getService(UserInfoManage.class);
	T6110 t6110 = userInfoManage.getUserInfo(creditInfo.F02);
	T6103 t6103 = new T6103();
	T6342 t6342 = new T6342();
	boolean flag=false;
	//风险等级是否匹配(默认不匹配)
	boolean isRiskMatch=false;
	//标的产品是否增加投资限制
	boolean isInvestLimit=Boolean.parseBoolean(configureProvider.getProperty(RegulatoryPolicyVariavle.IS_INVEST_LIMIT));
	boolean isOpenRiskAccess=Boolean.parseBoolean(configureProvider.getProperty(RegulatoryPolicyVariavle.IS_OPEN_RISK_ASSESS));
	boolean isXgwj = investManage.isXgwj(id);
	PtAccountManage ptAccountManage = serviceSession.getService(PtAccountManage.class);
	DimengRSAPulicKey publicKey = ptAccountManage.getPublicKey();
	String modulus = new String(Hex.encode(publicKey.getModulus().toByteArray()));
	String exponent = new String(Hex.encode(publicKey.getPublicExponent().toByteArray()));
	TermManage termManage = serviceSession.getService(TermManage.class);
	T5017 fxtsh = termManage.get(TermType.FXTSH);
	String riskUrl=null;
	Timestamp timemp = investManage.getCurrentTimestamp();
	String _rzUrl="";
	T6110 t6110_1 = null;
	if(dimengSession!=null && dimengSession.isAuthenticated()){
	    t6110_1 = userInfoManage.getUserInfo(serviceSession.getSession().getAccountId());
	}
%>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/security.js"></script>
<script type="text/javascript">
	var modulus = "<%=modulus%>", exponent = "<%=exponent%>";
	var key = RSAUtils.getKeyPair(exponent, '', modulus);
</script>

<div class="item_details_top w1002 clearfix">
	<%if(zqInfo.F06 == T6260_F07.YJS){%>
	<div class="seal seal_yzr"></div>
	<%} %>
	<div class="left">
		<div class="til" title="<%StringHelper.filterHTML(out, creditInfo.F03);%>">
			<%if(creditInfo.xsb == T6230_F28.S){ %>
			<span class="item_icon novice_icon">新</span>
			<%} %>
			<%if(t6231.F27 == T6231_F27.S){ %>
			<span class="item_icon reward_icon">奖</span>
			<%} %>
			<%
				if(creditInfo.F11 == T6230_F11.S){
			%>
			<span class="item_icon dan_icon">保</span>
			<%}else if(creditInfo.F13 == T6230_F13.S){ %>
			<span class="item_icon di_icon">抵</span>
			<%}else if(creditInfo.F14 == T6230_F14.S){ %>
			<span class="item_icon shi_icon">实</span>
			<%}else if(creditInfo.F33 == T6230_F33.S){ %>
			<span class="item_icon xin_icon">信</span>
			<%} %>
			<%StringHelper.filterHTML(out, StringHelper.truncation(creditInfo.F03, 20));%></div>

		<div class="clearfix mt30">
			<div class="pic"><img src="<%if(StringHelper.isEmpty(creditInfo.F21)){%><%=fileStore.getURL(configureProvider.getProperty(SystemVariable.BDMRTB))%><%}else{%><%=fileStore.getURL(creditInfo.F21) %><%}%>"  /></div>
			<div class="info">
				<div class="border_d_b pb10">
					<%if(creditInfo.F20 == T6230_F20.YJQ || creditInfo.F20 == T6230_F20.HKZ || creditInfo.F20 == T6230_F20.YDF){
						creditInfo.F05 = creditInfo.F05.subtract(creditInfo.F07);
					}%>
                    	转让价格：<span class="gray3 f30 pl10" id = "zqzrjg"><%=Formater.formatAmount(zqInfo.F02)%></span>元
                 </div>
                 <ul class="data clearfix">
					<li>债权价值<span class="f18 orange pl5"><%=Formater.formatAmount(zqInfo.F03)%></span><span class="f18 orange">元</span></li>
					<li class="line"></li>
					<li>待收本息<span class="f18 orange"><%=Formater.formatAmount(zqInfo.dsbx)%></span><span class="f18 orange">元</span></li>
				</ul>
				<ul class="other_info clearfix mt10">
					<li>年化利率：<span class="orange"><%=Formater.formatAmount(creditInfo.F06.multiply(new BigDecimal(100)))%>%</span></li>
					<li>剩余期数：
						<%=zqInfo.F23 %>/<%=zqInfo.F22 %>
					</li>
					<li>还款方式：<%=creditInfo.F10.getChineseName()%></li>
					<li>
					下个还款日：<%=DateParser.format(zqInfo.nextRepaymentDay)%>
					</li>
					<%if(creditInfo.F11 == T6230_F11.S){ %>
					<%-- <li>担保机构：<span title="<%=investManage.getDB(id) == null?"":investManage.getDB(id).F06%>"><%=investManage.getDB(id) == null?"":StringHelper.truncation(investManage.getDB(id).F06, 10)%></span></li> --%>
					<li>担保方案：<%=creditInfo.F12.getChineseName() %></li>
					<%} %>
					<%
						if(fxtsh!=null){
					%>
					<li class="long">
						风险提示：详见<a href="javascript:showFXTS()" class="highlight">《风险提示函》</a>
					</li>
					<%
						}
					%>
					<%if(isInvestLimit){
						if(t6110_1 != null && t6110_1.F06 == T6110_F06.FZRR){
					%>
					<li class="long">投资人条件：无</li>
					<%}else{%>
					<li class="long">投资人条件：风险承受等级“<%=creditInfo.productRiskLevel.getChineseName()%>”</li>
					<%}} %>
				</ul>
			</div>
		</div>
	</div>
	
	
	<%
    UserManage userManage = serviceSession.getService(UserManage.class);
    String usrCustId = null;
    boolean tg = BooleanParser.parse(configureProvider.getProperty(SystemVariable.SFZJTG));
    boolean isOpenPwd = BooleanParser.parse(configureProvider.getProperty(PayVariavle.CHARGE_MUST_WITHDRAWPSD));
    String action = "";
    if (tg  && !"FUYOU".equals(configureProvider.format(SystemVariable.ESCROW_PREFIX))) {
        action = configureProvider.format(URLVariable.ESCROW_URL_EXCHANGE);
    } else {
        action = configureProvider.format(URLVariable.TB_ZQZR);
    }
	%>
	<div class="right">
		<%if(zqInfo.F06 == T6260_F07.YJS){%>
			<div class="tc mt50"><img src="<%=controller.getStaticPath(request)%>/images/item_details_icon.png"></div>
			<div class="pl50 mt30">债权人：<%StringHelper.filterHTML(out, zqInfo.zqzrz.substring(0, 2) + "******" + zqInfo.zqzrz.substring(zqInfo.zqzrz.length() - 2, zqInfo.zqzrz.length())); %></div>
			<div class="pl50 mt10">受让人：<%StringHelper.filterHTML(out, zqInfo.srr.substring(0, 2) + "******" + zqInfo.srr.substring(zqInfo.srr.length() - 2, zqInfo.srr.length())); %></div>
			<div class="pl50 mt10">转让价格：<%=Formater.formatAmount(zqInfo.F02)%>元</div>
			<div class="pl50 mt10">转让时间：<%=DateTimeParser.format(zqInfo.F05)%></div>
		<%}else{ if(dimengSession!=null && dimengSession.isAuthenticated()){
		    T6101 userInfo= userInfoManage.search();
		    usrCustId = userManage.getUsrCustId();
		    
		    T6147 t6147=userInfoManage.getT6147();
		    /* if(t6147==null){
		    	t6147=new T6147();
		    	t6147.F04=T6147_F04.BSX;
		    } */
		    if(RiskLevelCompareUtil.compareRiskLevel((t6147==null?null:t6147.F04), zqInfo.F30.name())){
		    	isRiskMatch=true;
		    }
		    boolean isInvest = true;
			if(T6110_F06.FZRR == t6110_1.F06 && T6110_F17.F == t6110_1.F17){
			    isInvest = false;
			}
		%>
			<%if(tg && StringHelper.isEmpty(usrCustId)){ %>
			<div class="f18 tc mt100">
				您需要在第三方托管平台上进行注册，才可申请！
				<a href="<%=configureProvider.format(URLVariable.OPEN_ESCROW_GUIDE) %>" class="red">立即注册</a>
			</div>
			<%}else{ %>
			<div class="f18 tc pt10">债权转让需<span class="red">全额</span>购买</div>
			<div class="f18 tc pt10">转让价格</div>
			<div class="f24 tc border_d_b pb10 pt15"><%=Formater.formatAmount(zqInfo.F02)%>元</div>
			<p class="mt20"><a href="<%configureProvider.format(out,URLVariable.USER_CHARGE);%>" class="blue fr">充值</a>
			可用金额：<span class="orange"><em class="bold"><%=Formater.formatAmount(userInfo.F06) %></em></span> 元</p>
			<p class="mt20">预期收益金额：<span class="orange"><em class="bold"><%=Formater.formatAmount(zqInfo.yqsy) %></em>元</span></p>
			<%if(T6110_F07.HMD != t6110_1.F07 && isInvest) {
				if(t6110_1.F01 == creditInfo.assureId){%>
				<div class="mt40 mb10">
					<a href="javascript:void(0);" class="btn01 btn_gray btn_disabled sub-btn">购买</a>
				</div>
				<%}else{ %>
				<p class="f12 mt20">
				<%
					T5017 term = termManage.get(TermType.ZQZRXY);
					if(term != null || fxtsh!=null){
				%>
				<input name="iAgree" type="checkbox" id="iAgree" class="mr5"/>&nbsp;<label for="iAgree">我已阅读并同意</label>
				<%if(fxtsh!=null){ %>
				<a href="javascript:showFXTS();"
	                   class="yellow_c1">《<%=fxtsh.F01.getName()%>》</a>
				<%} %>
					<%
					if(term != null){
					%>
				<a target="_blank" href="<%=controller.getPagingItemURI(request, Term.class, TermType.ZQZRXY.name())%>"
	                   class="yellow_c1">《<%=term.F01.getName()%>》</a>
					<%} %>
	            </p>
	            <div class="mt40 mb10">
					<a id="" class="btn01 sumbitForme btn_gray btn_disabled sub-btn" style="margin-top: 5px;">购买</a>
				</div>
				<%}else{%>
				<div class="mt40 mb10">
					<a id="tbButton"  href="javascript:void(0);" class="btn01 sumbitForme">购买</a>
				</div>
				<%}}%>
		<%}}%>
		<%}else{ %>
		<div class="f18 tc pt10">债权转让需<span class="red">全额</span>购买</div>
		<div class="f18 tc pt10">转让价格</div>
		<div class="f24 tc border_d_b pb10 pt15"><%=Formater.formatAmount(zqInfo.F02)%>元</div>
		<div class="f18 tc mt100">请立即<a href="<%configureProvider.format(out,URLVariable.LOGIN);%>?_z=/financing/sbtz/zqxq/<%=id %>.html" class="orange">登录</a>，或
			<a href="<%configureProvider.format(out,URLVariable.REGISTER);%>" class="orange">注册</a></div>
		<%}}%>
	</div>
</div>

	<form method="post" class="form1" id="bidForm" action="<%=action %>">
	<div class="popup_bg" style="display: none;"></div>
	<div id="showBuy" class="dialog" style="display: none;">
			<%=FormToken.hidden(serviceSession.getSession()) %>
		<input type="hidden" id="zqSucc" name="zqSucc" value="<%configureProvider.format(out, URLVariable.USER_ZQYZR);%>">
	    <input type="hidden" name="zqzrId" id="zqzrId">
		<input type="hidden" name="zqId" value="<%=zqInfo.F01%>">
			<div id="idRisk" style="display:none"></div>
			<div style="display: none;" id="id_content">
			<div class="title"><a href="javascript:void(0);" class="out cancel"></a>债权购买确认</div>
			<div class="content">
				<div class="tip_information">
					<div class="doubt"></div>
					<div class="tips">
						<span class="f20 gray3">您此次购买债权金额为<span id="zqjz" class="red" ><%=zxMoney%></span>元</span>
					</div>
					<div class="tips">
			            <span class="f20 gray3">需支付金额
			            <span class="red" id="zrjg"><%=zfMoney%></span>元,确认购买？</span>
					</div>
					<%
						if (isOpenPwd)
						{
					%>
					<div class="mt20">
						<span class="red">*</span>交易密码:&nbsp;&nbsp;
		                	<input type="password" name="tran_pwd"  class="required text_style" id="tran_pwd" autocomplete="off"/>
		                <br/>
		                <span id="errorSpan" class="red" style="display: none"></span>
					</div>
					<%
						}
					%>
				</div>
				<div class="tc mt10">
					<a href="javascript:void(0);" id="ok" class="btn01">确定</a>
					<a href="javascript:void(0);" class="cancel btn01 btn_gray ml20" onclick="resetForm();">取消</a>
				</div>
			</div>
		</div>
	<input type="hidden" name="tranPwd" id="tranPwd"/>
	</div>
	</form>

<%
	T5017 term = termManage.get(TermType.FXTSH);
%>
<div id="showFXTSH" class="dialog" style="display:none">
	<div class="title tc">风险提示函 <a href="javascript:closeInfo();" class="out"></a></div>
	<%if(term!=null){%>
	<div class="content" style="max-height: 490px; overflow-y: auto;"><%StringHelper.format(out, term.F02, fileStore);%></div>
	<%}else{%>
	<div class="content" style="max-height: 490px; overflow-y: auto;">暂无风险提示函内容!</div>
	<%} %>
	<div class="tc pb20 pt20"><a href="javascript:closeFXTS()" class="btn01">我已知悉</a></div>
</div>
<script type="text/javascript">
	function showFXTS(){
		var h=$(window).height()-200;
		if(h<600){	$("#showFXTSH .content").height(h);
			$("#showFXTSH").css({"top":"50%","margin-top":(h+160)/2*(-1)});
		}
		$("#showFXTSH").show();
	}
	function closeFXTS(){
		$("#showFXTSH").hide();
	}
	$(function () {
		$("input:checkbox[name='userReward']").attr("checked", false);
		var $iAgree = $("input:checkbox[name='iAgree']");
		if($iAgree.length==0){
			var register = $(".sub-btn");
			register.removeClass("btn_gray btn_disabled");
			register.attr("id","tbButton");
			//选中“我同意”，绑定事件
			$("#tbButton").click(function(){							
		 	buy(<%=zqInfo.F02%>,<%=zqInfo.F03%>,<%=zqInfo.F25%>,'<%=isInvestLimit %>','<%=isRiskMatch %>','<%=isOpenRiskAccess%>>');
			});
		}
		//“我同意”按钮切回事件
		$iAgree.attr("checked", false);
		$iAgree.click(function() {
			var iAgree = $(this).attr("checked");
			var register = $(".sub-btn");
			if (iAgree) {
				register.removeClass("btn_gray btn_disabled");
				register.attr("id","tbButton");
				//选中“我同意”，绑定事件
				$("#tbButton").click(function(){							
			 	buy(<%=zqInfo.F02%>,<%=zqInfo.F03%>,<%=zqInfo.F25%>,'<%=isInvestLimit %>','<%=isRiskMatch %>','<%=isOpenRiskAccess%>>');
				});
			} else {
				register.addClass("btn_gray btn_disabled");
				$("#tbButton").unbind("click");
				register.attr("id","");
			}
		});
	});

	//重置我同意按钮
	function resetForm(){
		$("#tran_pwd").val("");
		//var register = $(".sub-btn");
		//$("input:checkbox[name='iAgree']").attr("checked", false);
		//register.addClass("btn_gray btn_disabled");
		//$("#tbButton").unbind("click");
		//$("#ok").unbind("click");
		//register.attr("id","");
	}

</script>
	
