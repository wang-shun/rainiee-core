<%@page import="com.dimeng.p2p.S61.enums.T6110_F17"%>
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
	Bdxq creditInfo = investManage.get(id);
	if(creditInfo==null){
		response.sendError(HttpServletResponse.SC_NOT_FOUND );
		return;
	}
	T6231 t6231 = investManage.getExtra(id);
	BigDecimal zxMoney = new BigDecimal(0);
	UserInfoManage userInfoManage = serviceSession.getService(UserInfoManage.class);
	T6110 t6110 = userInfoManage.getUserInfo(creditInfo.F02);
	BigDecimal enperienceAmount = BigDecimal.ZERO;
	boolean isCanTXSB = true;
	T6103 t6103 = new T6103();
	T6342 t6342 = new T6342();
	String jxqJson = "";
	String hbJson = "";
	List<MyRewardRecod> jxqList =new ArrayList<MyRewardRecod>();
	List<MyRewardRecod> hbList =new  ArrayList<MyRewardRecod>();
	boolean flag=false;
	//风险等级是否匹配(默认不匹配)
	boolean isRiskMatch=false;
	//标的产品是否增加投资限制
	boolean isInvestLimit=Boolean.parseBoolean(configureProvider.getProperty(RegulatoryPolicyVariavle.IS_INVEST_LIMIT));
	boolean isOpenRiskAccess=Boolean.parseBoolean(configureProvider.getProperty(RegulatoryPolicyVariavle.IS_OPEN_RISK_ASSESS));
	T6110 t6110_1 = null;
	boolean usedHb = false;
	boolean usedJxq = false;
	boolean usedTyj = false;
	if(dimengSession!=null && dimengSession.isAuthenticated()){
		t6110_1 = userInfoManage.getUserInfo(serviceSession.getSession().getAccountId());

		isCanTXSB = investManage.isCanTXSB(id);
		MyExperienceManage experienceManage=serviceSession.getService(MyExperienceManage.class);
		enperienceAmount=experienceManage.getExperienceAmount();
		//判断用户对该标是否已经使用体验金
		t6103 = experienceManage.getT6103(id);

		MyRewardManage myRewardManage = serviceSession.getService(MyRewardManage.class);
		//用户活动,此活动是否已投资过
		t6342 = myRewardManage.getT6342(id);
		//if(null == t6342){
			//加息券
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("type", T6340_F03.interest);
			params.put("status", T6342_F04.WSY);
			jxqList = myRewardManage.getMyRewardRecodList(params);
			ObjectMapper objectMapper = new ObjectMapper();
			jxqJson = objectMapper.writeValueAsString(jxqList);
			//红包
			params.put("type", T6340_F03.redpacket);
			hbList = myRewardManage.getMyRewardRecodList(params);
			hbJson = objectMapper.writeValueAsString(hbList);
		//}
		//该用户是否有加息券或者红包
		if( (t6103==null && t6342==null) && (jxqList.size()>0 || hbList.size()>0||enperienceAmount.compareTo(BigDecimal.ZERO)>0)){
			flag=true;
		}
		if(T6231_F36.ALL == t6231.F36)
		{
			usedHb = myRewardManage.IsUsedReward(id,T6340_F03.redpacket.name());
			usedJxq = myRewardManage.IsUsedReward(id,T6340_F03.interest.name());
			usedTyj = t6103 !=null;
		}
		T6147 t6147=userInfoManage.getT6147();
		/* if(t6147==null){
			t6147 = new T6147();
			t6147.F04=T6147_F04.BSX;
		} */
		//投资用户风险等级小于标产品风险等级; 自然人才有此限制
		if(RiskLevelCompareUtil.compareRiskLevel((t6147==null?null:t6147.F04),creditInfo.productRiskLevel.name())){
			isRiskMatch=true;
		}
		if(t6110_1.F06 == T6110_F06.FZRR){
			isRiskMatch=true;
		}
	}
	boolean isXgwj = investManage.isXgwj(id);
	boolean sfzjkt = Boolean.parseBoolean(configureProvider.getProperty(SystemVariable.BID_SFZJKT));
	PtAccountManage ptAccountManage = serviceSession.getService(PtAccountManage.class);
	DimengRSAPulicKey publicKey = ptAccountManage.getPublicKey();
	String modulus = new String(Hex.encode(publicKey.getModulus().toByteArray()));
	String exponent = new String(Hex.encode(publicKey.getPublicExponent().toByteArray()));
	TermManage termManage = serviceSession.getService(TermManage.class);
	T5017 fxtsh = termManage.get(TermType.FXTSH);
	String riskUrl=null;
	Timestamp timemp = investManage.getCurrentTimestamp();
%>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/security.js"></script>
<script type="text/javascript">
	var modulus = "<%=modulus%>", exponent = "<%=exponent%>";
	var key = RSAUtils.getKeyPair(exponent, '', modulus);
</script>

<div class="item_details_top w1002 clearfix">
	<%if(creditInfo.F20 == T6230_F20.DFK){ %>
	<div class="seal seal_dfk"></div>
	<%}else if(creditInfo.F20 == T6230_F20.HKZ){ %>
	<div class="seal seal_hkz"></div>
	<%}else if(creditInfo.F20 == T6230_F20.YJQ){ %>
	<div class="seal seal_yjq"></div>
	<%}else if(creditInfo.F20 == T6230_F20.YDF){ %>
	<div class="seal seal_ydf"></div>
	<%}else if(creditInfo.F20 == T6230_F20.YZR){ %>
	<div class="seal seal_yzr"></div>
	<%}else if(creditInfo.F20 == T6230_F20.YLB){ %>
	<div class="seal seal_ylb"></div>
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
                    <span style="margin-left:20px; ">项目规模：</span>	<span class="gray3 f30"><%=Formater.formatAmount(creditInfo.F05)%></span>元
                 </div>
				<ul class="data clearfix">
					<li>年化利率<span class="f30 orange"><%=Formater.formatAmount(creditInfo.F06.multiply(new BigDecimal(100)))%></span><span class="f18 orange">%</span></li>
					<li class="line"></li>
					<li>投资期限
						<%if(T6231_F21.S == t6231.F21){%>
						<span class="f30 gray3"><%=t6231.F22 %></span><span class="f18">天</span>
						<%}else{%>
						<span class="f30 gray3"><%=creditInfo.F09 %></span><span class="f18">个月</span>
						<%} %>
					</li>
				</ul>
				<ul class="other_info clearfix mt20">
					<%if(creditInfo.F20 != T6230_F20.YFB){ %>
						<%if(creditInfo.F20==T6230_F20.HKZ ||creditInfo.F20==T6230_F20.YJQ ||creditInfo.F20==T6230_F20.YDF){ %>
							<li class="long"><div class="progress">进度<span class="progress_bg ml5"><span class="progress_bar" style="width:100%;"></span></span> <span class="cent">100%</span> </div></li>
						<%}else{ %>
							<li class="long"><div class="progress">进度<span class="progress_bg ml5"><span class="progress_bar" style="width:<%=Formater.formatProgress(creditInfo.proess)%>;"></span></span> <span class="cent"><%=Formater.formatProgress(creditInfo.proess) %></span> </div></li>
						<%} %>
					<%} %>
					<%if(t6231.F27 == T6231_F27.S){ %>
					<li>投资奖励：
						<span class="orange"><%=Formater.formatRate(t6231.F28) %></span>
                    	    <span class="hover_tips ml5">
							<div class="hover_tips_con">
								<div class="arrow"></div>
								<div class="border">放款后一次性奖励，奖励金额=投资本金*奖励利率</div>
							</div>
						  </span>
					</li>
					<%} %>
					<li>发布日期：<%=TimestampParser.format(creditInfo.F22,"yyyy-MM-dd") %></li>
					<%if(creditInfo.F11 == T6230_F11.S){ %>
					<li>担保方：<span title="<%=investManage.getDB(id) == null?"":investManage.getDB(id).F06%>"><%=investManage.getDB(id) == null?"":StringHelper.truncation(investManage.getDB(id).F06, 20)%></span></li>
					<li>保障方式：<%=creditInfo.F12.getChineseName() %></li>
					<%} %>
					<li>还款方式：<%=creditInfo.F10.getChineseName()%></li>
					<%
						long sytime = 0;
						if (creditInfo.F20 == T6230_F20.TBZ) {
					%>
					<li>
						剩余时间：

						<%
							if(creditInfo.jsTime != null){
								long hm=1000*3600*24;
								sytime = creditInfo.jsTime.getTime()-timemp.getTime();
								long day=sytime/hm;
								long hour=(sytime-day*hm)/(1000*3600);
								long min=(sytime-day*hm-hour*1000*3600)/(1000*60);
								long second = (sytime-day*hm-hour*1000*3600-min*60*1000)/1000;
						%>
						<font><em class="f18 gray33" id="ckDay"><%=day<0?0:day %></em>天</font>
						<em class="f18 gray33" id="ckHour"><%=hour<0?0:hour %></em>时
						<em class="f18 gray33" id="ckMin"><%=min<0?0:min %></em>分
						<font style="display: none;"><em class="f18 gray33" id="ckSecond"><%=second<0?0:second %></em>秒</font>
						<%} %>

					</li>
					<%
						}
					%>
					<li>
					<%
			      	  Calendar calend = Calendar.getInstance();
				      if(t6231.F12 != null){
				    	  calend.setTime(t6231.F12);
					      if(t6231.F21 == T6231_F21.S){
					    	  calend.add(Calendar.DAY_OF_MONTH, t6231.F22);
					      }else{
					    	  calend.add(Calendar.MONTH, creditInfo.F09);
					      }
					      calend.add(Calendar.DAY_OF_MONTH, creditInfo.F19);
			      	%>
					项目完结日期：<%=t6231.F13!=null ? DateParser.format(t6231.F13): (t6231.F34 != null ? DateParser.format(t6231.F34) : DateParser.format(calend.getTime(),"yyyy-MM-dd")) %>
					<%} %>
					</li>
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
						if(dimengSession!=null && dimengSession.isAuthenticated() && t6110_1.F06 == T6110_F06.FZRR){
					%>
					<li class="long">投资人条件：无</li>
					<%}else{%>
					<li class="long">投资人条件：风险承受等级“<%=creditInfo.productRiskLevel.getChineseName()%>”</li>
					<%}} %>
				</ul>
			</div>
		</div>
	</div>
	<form target="bidForm" method="post" class="form1" id="bidForm" action="<%configureProvider.format(out,URLVariable.PAY_BID_URL);%>" onkeydown="if(event.keyCode==13){return false;}">
		<%=FormToken.hidden(serviceSession.getSession()) %>
		<input type="hidden" id="sbSucc" name="sbSucc" value="<%configureProvider.format(out, URLVariable.USER_FINANCING);%>?isTbzzq=1">
		<div class="right">
			<%
				Calendar calendar = Calendar.getInstance();
				if (creditInfo.F20 == T6230_F20.TBZ && sytime>0) {
			%>
			<%if(dimengSession!=null && dimengSession.isAuthenticated()){
				T6101 userInfo= userInfoManage.search();
				String isYuqi =  userInfoManage.isYuqi();
				UserManage userManage = serviceSession.getService(UserManage.class);
				String usrCustId = userManage.getUsrCustId();
				boolean tg = BooleanParser.parse(configureProvider.getProperty(SystemVariable.SFZJTG));
				String escrow = configureProvider.getProperty(SystemVariable.ESCROW_PREFIX);
				BigDecimal ytje = investManage.selectUserMaxAmount(creditInfo.F01,serviceSession.getSession().getAccountId());
				BigDecimal maxkt = t6231.F26.subtract(ytje);
				BigDecimal[] array = {maxkt,userInfo.F06,creditInfo.F07};
				Arrays.sort(array);
				boolean isInvest = true;
				if(T6110_F06.FZRR == t6110_1.F06 && T6110_F17.F == t6110_1.F17){
				    isInvest = false;
				}
			%>
			<%if((tg) && StringHelper.isEmpty(usrCustId)){ %>
			<div class="f18 tc mt100">
				您需要在第三方托管平台上进行注册，才可申请！
				<a href="<%=configureProvider.format(URLVariable.OPEN_ESCROW_GUIDE) %>" class="red">立即注册</a>
			</div>
			<%}else{ %>
			<input type="hidden" id="isYuqi" name="isYuqi" value="<%=isYuqi%>">
			<input type="hidden" id="charge" name="charge" value="<%configureProvider.format(out,URLVariable.USER_CHARGE);%>">
			<input type="hidden" name="kyMoney" id="kyMoney" value="<%=userInfo.F06%>">
			<input type="hidden" name="loanId" id="loanId" value="<%=id%>">
			<input type="hidden" name="syje" id="syje" value="<%=creditInfo.F07%>">
			<input type="hidden" name="jkje" id="jkje" value="<%=creditInfo.F05%>">
			<input type="hidden"name="yhid" id="yhid"value="<%=t6110_1.F01%>">
			<input type="hidden" name="sfxsb" id="sfxsb" value="<%=creditInfo.xsb%>">
			<input type="hidden" name="minBid" id="minBid" value="<%=t6231.F25%>">
			<input type="hidden" name="isCanTXSB" id="isCanTXSB" value="<%=isCanTXSB%>">
			<input type="hidden" id="zdktje" value="<%=array[0]%>">
			<input type="hidden" name="useType" id="useType" value="<%=t6231.F36.name()%>">
			<input type="hidden" name="usedExp" id="usedExp" value="no"/>
			<div class="f18 border_d_b tc pb5">项目剩余可投金额<br /><span class="f30"><%=Formater.formatAmount(creditInfo.F07)%></span>元</div>
			<div class="mt10"><a href="<%configureProvider.format(out,URLVariable.USER_CHARGE);%>" class="blue fr">充值</a>
			可用金额：<span class="orange"><em class="bold"><%=Formater.formatAmount(userInfo.F06) %></em></span> 元</div>
			<p class="mt5">最大可投金额：
	            	<span class="orange bold"><%=Formater.formatAmount(array[0])%></span> 元
			</p>
			<%if(T6110_F07.HMD != t6110_1.F07 && t6231.F33 != T6231_F33.APP && isInvest) {%>
			<%
				if(t6231.F27.name().equals(T6231_F27.F.name()) && T6230_F28.F.name().equals(creditInfo.xsb.name()) && t6110_1.F06 == T6110_F06.ZRR){
					if(flag && T6231_F36.SINGLE == t6231.F36){
			%>
			<div class="mt10">
				<input id="userReward" name="userReward" type="checkbox" onclick="selectRewardType();" class="mr5" />使用奖励
			</div>
			<div class="select_reward clearfix mt5">
				<select class="fl mr5" style="width:75px;display:none;" id="myRewardType" name="myRewardType" onchange="showMyReward(this);">
					<option value="">请选择</option>
					<%
						if(enperienceAmount.compareTo(BigDecimal.ZERO)>0){
					%>
					<option value="experience">追加体验金</option>
					<%}if(hbList.size()>0){%>
					<option value="hb">红包</option>
					<%}if(jxqList.size()>0){%>
					<option value="jxq">加息券</option>
					<%}%>
				</select>
				<!-- 体验金 start -->
				<span id="experienceRule" class="experience_text reward_value" style="display:none;">体验金：<%=Formater.formatAmount(enperienceAmount) %>元</span>
				<!-- 体验金 end -->

				<!-- 红包 start -->
				<select class="pulldown_1 reward_value" style="display:none;" id="hbRule" name="hbRule">
				</select>
				<!-- 红包 end -->

				<!-- 加息券 start -->
				<select class="pulldown_1 reward_value" style="display:none;" id="jxqRule" name="jxqRule" onchange="yqCountAmount();">
				</select>
				<!-- 加息券 end -->
			</div>
			<%}else if(T6231_F36.ALL == t6231.F36){%>
				<%if(!usedHb && hbList != null && hbList.size() > 0){%>
				<div class="select_reward clearfix mt10">
				<!-- 红包 start -->
				<span class="ml15">使用红包：</span><select class="select2 hbRule" id="hbRule" name="hbRule">
				<option value="">请选择</option>
				<%
					for(MyRewardRecod rewardRecod : hbList){
				%>
				<option value="<%=rewardRecod.F01%>" userRule="<%=rewardRecod.useRule%>" investUseRule="<%=rewardRecod.investUseRule%>" hbAmount="<%=rewardRecod.value%>">
					<%
					if(rewardRecod.useRule == 0)
					{
						out.print(""+rewardRecod.value+"元(无限制)");
					}else if(rewardRecod.useRule == 1)
					{
						out.print(""+rewardRecod.value+"元(投资满"+rewardRecod.investUseRule+"元使用)");
					}else{
						out.print("");
					}
					%>
				</option>
				<%}%>
				</select></div>
				<%}%>
				<!-- 红包 end -->

				<!-- 加息券 start -->
				<%if(!usedJxq && jxqList != null && jxqList.size() > 0){%>
				<div class="select_reward clearfix mt5">
				使用加息券：<select class="select2 jxqRule" id="jxqRule" name="jxqRule" onchange="yqCountAmount();">
				<option value="">请选择</option>
					<%
					for(MyRewardRecod rewardRecod : jxqList){
					%>
					<option value="<%=rewardRecod.F01%>" userRule="<%=rewardRecod.useRule%>" investUseRule="<%=rewardRecod.investUseRule%>" jxlValue="<%=rewardRecod.value%>">
						<%
							if(rewardRecod.useRule == 0)
							{
								out.print("+"+Formater.formatAmount(rewardRecod.value)+"%(无限制)");
							}else if(rewardRecod.useRule == 1)
							{
								out.print("+"+Formater.formatAmount(rewardRecod.value)+"%(投资满"+rewardRecod.investUseRule+"元使用)");
							}else{
								out.print("");
							}
						%>
					</option>
					<%}%>
				</select>
				</div>
				<%}%>
				<!-- 加息券 end -->
				<!-- 体验金 start -->
				<%
					if(!usedTyj && enperienceAmount.compareTo(BigDecimal.ZERO)>0){
				%>
				<div class="select_reward clearfix mt5">
					使用体验金：<select class="select2 tyjRule" id="tyjRule" name="tyjRule">
					<option value="">请选择</option>
					<option value="<%=Formater.formatAmount(enperienceAmount) %>"><%=Formater.formatAmount(enperienceAmount) %>元</option>
				</select>
				</div>
				<%}%>
				<!-- 体验金 end -->
			<%} }%>
			<div class="focus_input pr mt15">
				<input id="amount" name="amount" maxlength="10" type="text" class="focus_text" onKeyUp="value=(parseInt((value=value.replace(/\D/g,''))==''?'0':value,10));yqCountAmount();"  value="" autocomplete="off"/>
				<label for="money" onclick="$(this).hide();$('#amount').focus();">起投金额：￥<%=Formater.formatAmount(t6231.F25) %>元</label>
			</div>
			<p id="dxje">&nbsp;</p>
			<p id="yqCount">预期收益金额：<span class="orange" id="yqCountEm">0</span> 元</p>
			<div class="mt15 f12">
				<%if(T6110_F07.HMD != t6110_1.F07) {
					if(t6110_1.F01 == creditInfo.F02 && !sfzjkt || (t6110_1.F01 == creditInfo.assureId)){%>
				<a href="javascript:void(0);" class="btn01 btn_gray btn_disabled">确认投资</a>
				<%}else{ %>
				<%
					TermType termType = TermType.TFJKXY;
					//如果是担保标，这跳转到四方协议，否则三方协议
					if(creditInfo.F11 == T6230_F11.S) {
						termType =TermType.FFJKXY;
					}else{
						termType =TermType.TFJKXY;
					}
					T5017 term = termManage.get(termType);
					if(term != null || fxtsh!=null){
				%>
				<input name="iAgree" type="checkbox" id="iAgree" class="m_cb"/>&nbsp;<label for="iAgree">我已阅读并同意</label>
				<%if(fxtsh!=null){ %>
				<a href="javascript:showFXTS()" class="highlight">《风险提示函》</a>
				<%} %>
				<%if(term!=null){ %>
				<a target="_blank"  href="<%=controller.getPagingItemURI(request, Term.class, termType.name())%>" class="highlight">《借款协议》</a>
				<%} %>
				<a id=""  href="javascript:void(0);" fromname="form1" class="btn01 sumbitForme btn_gray btn_disabled sub-btn" style="margin-top: 5px;">确认投资</a>
				<%}else{%>
				<a id="tbButton"  href="javascript:void(0);" fromname="form1" class="btn01 sumbitForme">确认投资</a>
				<%}%>
				<%}} %>
			</div>
			<%}else if(t6110_1.F06 == T6110_F06.ZRR && T6110_F07.HMD != t6110_1.F07 && t6231.F33 == T6231_F33.APP){ %>
			<div class="mt20">
				<p class="red">此项目仅供APP端投资</p>
			</div>
			<%} %>
			<%} %>
			<%}else{ %>
			<div class="f18 border_d_b tc pb5">项目剩余可投金额<br /><span class="f30"><%=Formater.formatAmount(creditInfo.F07)%>元</span></div>
			<div class="f18 tc mt100">请立即<a href="<%configureProvider.format(out,URLVariable.LOGIN);%>?_z=/financing/sbtz/bdxq/<%=id %>.html" class="orange">登录</a>，或
				<a href="<%configureProvider.format(out,URLVariable.REGISTER);%>" class="orange">注册</a></div>
			<%} %>

			<%}else if(creditInfo.F20 == T6230_F20.HKZ){ %>
			<% Mbxx mbxx=investManage.getMbxx(id); %>
			<div class="tc mt100">待还本息（元）</div>
			<div class="f22 tc orange mt5">￥<%=Formater.formatAmount(mbxx.dhje)%></div>
			<div class="tc mt50">剩余期数（期）<span class="f24 orange ml10"><%=t6231.F03%></span></div>
			<%
			} else if (creditInfo.F20 == T6230_F20.TBZ && sytime <=0) {

				long hm=1000*3600*24;
				long time =	calendar.getTimeInMillis()-creditInfo.F22.getTime();
				long day=time/hm;
				long hour=(time-day*hm)/(1000*3600);
				long min=(time-day*hm-hour*1000*3600)/(1000*60);
				long ss=(time-day*hm-hour*1000*3600-min*1000*60)/(1000);
				T6250[] t6250s= investManage.getRecord(id);
				if(!Formater.formatAmount(creditInfo.F07).equals(Formater.formatAmount(0.00))){
					ss=0;
				}
			%>
			<div class="tc mt50"><img src="<%=controller.getStaticPath(request)%>/images/item_details_icon.png"></div>
			<div class="tc mt20">筹款用时</div>
			<div class="f22 tc gray3 mt5"><%=day %>天<%=hour %>时<%=min %>分<%=ss %>秒</div>
			<div class="tc mt50">加入人次：<span class="orange"><%=t6250s == null ?0 :t6250s.length %>人</span></div>
			<%
			} else if (creditInfo.F20 == T6230_F20.DFK) {

				long hm=1000*3600*24;
				long time =	t6231.F11.getTime()-creditInfo.F22.getTime();
				long day=time/hm;
				long hour=(time-day*hm)/(1000*3600);
				long min=(time-day*hm-hour*1000*3600)/(1000*60);
				long ss=(time-day*hm-hour*1000*3600-min*1000*60)/(1000);
				T6250[] t6250s= investManage.getRecord(id);
				if(!Formater.formatAmount(creditInfo.F07).equals(Formater.formatAmount(0.00))){
					ss=0;
				}
			%>
			<div class="tc mt50"><img src="<%=controller.getStaticPath(request)%>/images/item_details_icon.png"></div>
			<div class="tc mt20">筹款用时</div>
			<div class="f22 tc gray3 mt5"><%=day %>天<%=hour %>时<%=min %>分<%=ss %>秒</div>
			<div class="tc mt50">加入人次：<span class="orange"><%=t6250s == null ?0 :t6250s.length %>人</span></div>
			<%
			} else if (creditInfo.F20 == T6230_F20.YJQ) {
			%>
			<div class="tc mt100">还清时间</div>
			<div class="f22 tc gray3 mt5"><%=creditInfo.F20 == T6230_F20.YDF?DateParser.format(t6231.F14):DateParser.format(t6231.F13)%></div>
			<%} else if (creditInfo.F20 == T6230_F20.YDF){ %>
			<div class="tc mt100">垫付时间</div>
			<div class="f22 tc gray3 mt5"><%=creditInfo.F20 == T6230_F20.YDF?DateParser.format(t6231.F14):DateParser.format(t6231.F13)%></div>
			<%} else if (creditInfo.F20 == T6230_F20.YZR){ %>
			<div class="tc mt100">转让时间</div>
			<div class="f22 tc gray3 mt5"><%=creditInfo.F20 == T6230_F20.YZR?DateParser.format(t6231.F34):DateParser.format(t6231.F13)%></div>
			<%
			} else if (creditInfo.F20 == T6230_F20.YFB) {
			%>
			<%if(!(dimengSession!=null && dimengSession.isAuthenticated())){%>
			<div class="f18 border_d_b tc pb5">请先<a href="<%configureProvider.format(out,URLVariable.LOGIN);%>?_z=/financing/sbtz/bdxq/<%=id %>.html" class="orange ml10">登录</a></div>
			<%} %>

			<div class="mt50">
				<p class="tc f18 ">可投金额：<span class="f20 orange"><%if(creditInfo.F05.doubleValue()>10000){%><%=Formater.formatAmount(creditInfo.F05.doubleValue()/10000)%>万<%}else{ %><%=Formater.formatAmount(creditInfo.F05)%>元<%} %></span></p>
				<p class="mt50">开放投资倒计时...<span id="tian"></span></p>
				<div class="time orange mt10"> </div>
				<div class="mt20"><a href="javaScript:void(0);" class="btn01 btn_disabled btn_gray">敬请期待</a></div>
			</div>
			<%} %>
		</div>
		<input type="hidden" name="tranPwd" id="tranPwd" />
	</form>
</div>
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

	var jxqJson = '<%=jxqJson%>';
	var hbJson = '<%=hbJson%>';
	var endTime =<%=creditInfo.F22.getTime()- timemp.getTime()%>;
	var isRiskMatch='<%=isRiskMatch%>';
	var isInvestLimit ='<%=isInvestLimit%>';
	var isOpenRiskAccess ='<%=isOpenRiskAccess%>';
	var clearTime = null;
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
	function time() {
		var leftsecond = parseInt(endTime / 1000);
		var day = Math.floor(leftsecond / (60 * 60 * 24)) < 0 ? 0 : Math
				.floor(leftsecond / (60 * 60 * 24));
		var hour = Math.floor((leftsecond - day * 24 * 60 * 60) / 3600) < 0 ? 0
				: Math.floor((leftsecond - day * 24 * 60 * 60) / 3600);
		var minute = Math
				.floor((leftsecond - day * 24 * 60 * 60 - hour * 3600) / 60) < 0 ? 0
				: Math
						.floor((leftsecond - day * 24 * 60 * 60 - hour * 3600) / 60);
		var second = Math.floor(leftsecond - day * 24 * 60 * 60 - hour * 3600
				- minute * 60) < 0 ? 0 : Math.floor(leftsecond - day * 24 * 60
				* 60 - hour * 3600 - minute * 60);
		if (hour < 10) {
			hour = "0" + hour;
		}
		if (minute < 10) {
			minute = "0" + minute;
		}
		if (second < 10) {
			second = "0" + second;
		}
		$("#tian").html("    (" + day + '天)');
		$(".time")
				.html(
						'<span class="details_bg">'
								+ (hour == "00" || hour == "0" ? "00" : hour)
								+ '</span><span class="details_ico"></span><span class="details_bg">'
								+ (minute == "00" || minute == "0" ? "00"
										: minute)
								+ '</span><span class="details_ico"></span><span class="details_bg">'
								+ second
								+ '</span><div class="clear"></div>');
		if (leftsecond <= 0) {
			clearInterval(clearTime);
			location.reload();
		}
	}
<%
    long syTime = creditInfo.F22.getTime()- timemp.getTime();
    if(creditInfo.F20 == T6230_F20.YFB && syTime>0){%>
	clearTime = setInterval(function() {
		endTime = endTime - 1000;
		time();
	}, 1000);
	<%}else{%>
	clearInterval(clearTime);
	<%}%>

	<%
    long leftTime = creditInfo.jsTime.getTime()-timemp.getTime();
    long day = leftTime/(1000*3600*24);
    if(creditInfo.F20 == T6230_F20.TBZ && day <= 0 && leftTime>0){%>
	var leftTime = '<%=leftTime%>';
	var clearLeftTime = null;
	$(function(){
		ckTime();
	});
	function ckTime(){
		var leftsecond = parseInt(leftTime / 1000);
		var day = Math.floor(leftsecond/ (60 * 60 * 24)) < 0 ? 0 : Math.floor(leftsecond / (60 * 60 * 24));
		var hour = Math.floor((leftsecond - day * 24 * 60 * 60) / 3600) < 0 ? 0: Math.floor((leftsecond - day * 24 * 60 * 60) / 3600);
		var minute = Math.floor((leftsecond - day* 24 * 60 * 60 - hour * 3600) / 60) < 0 ? 0: Math.floor((leftsecond - day* 24 * 60 * 60 - hour * 3600) / 60);
		var second = Math.floor(leftsecond - day* 24 * 60 * 60 - hour * 3600- minute * 60) < 0 ? 0 : Math.floor(leftsecond - day * 24 * 60* 60 - hour * 3600 - minute* 60);
		if(hour<10)
		{
			hour="0"+hour;
		}
		if(minute<10)
		{
			minute="0"+minute;
		}
		if(second<10)
		{
			second="0"+second;
		}
		$("#ckDay").parent().hide();
		$("#ckHour").html(hour);
		$("#ckMin").html(minute);
		$("#ckSecond").parent().show();
		$("#ckSecond").html(second);
		if (leftTime <= 0) {
			clearInterval(clearLeftTime);
			location.reload();
		}
	}
	clearLeftTime = setInterval(function() {
		leftTime = leftTime - 1000;
		ckTime();
	}, 1000);

	<%}%>
	/**
	 *计算预期收益
	 */
	function yqCountAmount() {
		$("#dxje").html("&nbsp;");
		var amount = $("#amount").val();
		var myreg = /^[0-9]([0-9])*$/;
		if(amount == "" || amount == undefined || amount == null || amount=="undefined"){
			/*$("#info").html(showDialogInfo("请输入投资金额！","doubt"));
			$("div.popup_bg").show();*/
			return;	
		}
		if(!myreg.test(amount)){
			$("#info").html(showDialogInfo("投资金额必须为整数！","doubt"));
			$("div.popup_bg").show();
			return;
		}
		var myRewardType = $("input[name='myRewardType']").val();
		var yearratio = 0;
		//我的加息券
		if($("#jxqRule li.selected").attr("jxlValue") != null && $("#jxqRule li.selected").attr("jxlValue") != "undefined"){
			var jxl = $("#jxqRule li.selected").attr("jxlValue");//加息率
			yearratio = parseFloat(<%=creditInfo.F06%>) + parseFloat(jxl/100) ;
		}else{
			yearratio = <%=creditInfo.F06%>;//年化利率
		}

		var month = <%=creditInfo.F09%>;//期数
		month = parseInt(month);
		amount = parseFloat(amount);
		yearratio = parseFloat(yearratio);
		//月利率
		var active = yearratio / 12;
		//累计支付利息
		var totalInterest = 0;
		//等额本息、每月付息,到期还本、本息到期一次付清、等额本金
		<%
        if(creditInfo.F10 == T6230_F10.DEBX){//等额本息
        %>
		var t1 = Math.pow(1 + active, month);
		var t2 = t1 - 1;
		var tmp = t1 / t2;
		//等额本息利率
		var monthratio = active * tmp;
		//累计支付利息
		totalInterest = (amount*monthratio).toFixed(2)* month - amount;
		<%
        }else if(creditInfo.F10 == T6230_F10.MYFX){//每月付息,到期还本
        %>
		//到期还本：按月付息、到期还本
		totalInterest = amount * month * active;
		<%
        }else if(creditInfo.F10 == T6230_F10.YCFQ){//本息到期一次付清
        %>
		//借款到期后一次性归还本金和利息。
		<%
        if(T6231_F21.S == t6231.F21){
        %>//如果是按天计算
		active = yearratio / 360;
		month = <%=t6231.F22%>;
		month = parseInt(month);
		<%
        }
        %>
		totalInterest = month * amount * active;
		<%
        }else if(creditInfo.F10 == T6230_F10.DEBJ){//等额本金
        %>
		totalInterest = ((amount / month + amount * active) + amount / month * (1 + active)) / 2 * month - amount;
		<%
        }
        %>
		//我的体验金
		if(($("#userReward").is(':checked') && myRewardType=="experience") || ($("input[name='tyjRule']") != "undefined" && $("input[name='tyjRule']").val() != null && $("input[name='tyjRule']").val() != "")){
			tyjYqCountAmount(amount,totalInterest);
		}else{
			showYqsy(totalInterest);
		}
		
		$("#dxje").html(chinaCost(amount));
	}

	/**
	 *计算预期收益
	 */
	function yqCountAmountAll() {
		$("#dxje").html("&nbsp;");
		var amount = $("#amount").val();
		var myreg = /^[0-9]([0-9])*$/;
		if(amount == "" || amount == undefined || amount == null || amount=="undefined"){
			/*$("#info").html(showDialogInfo("请输入投资金额！","doubt"));
			 $("div.popup_bg").show();*/
			return;
		}
		if(!myreg.test(amount)){
			$("#info").html(showDialogInfo("投资金额必须为整数！","doubt"));
			$("div.popup_bg").show();
			return;
		}
		var yearratio = 0;
		//我的加息券
		if($("#jxqRule li.selected").val() != null && $("#jxqRule li.selected").val() != "undefined"){
			var jxl = $("#jxqRule li.selected").attr("jxlValue");//加息率
			yearratio = parseFloat(<%=creditInfo.F06%>) + parseFloat(jxl/100) ;
		}else{
			yearratio = <%=creditInfo.F06%>;//年化利率
		}

		var month = <%=creditInfo.F09%>;//期数
		month = parseInt(month);
		amount = parseFloat(amount);
		yearratio = parseFloat(yearratio);
		//月利率
		var active = yearratio / 12;
		//累计支付利息
		var totalInterest = 0;
		//等额本息、每月付息,到期还本、本息到期一次付清、等额本金
		<%
        if(creditInfo.F10 == T6230_F10.DEBX){//等额本息
        %>
		var t1 = Math.pow(1 + active, month);
		var t2 = t1 - 1;
		var tmp = t1 / t2;
		//等额本息利率
		var monthratio = active * tmp;
		//累计支付利息
		totalInterest = (amount*monthratio).toFixed(2)* month - amount;
		<%
        }else if(creditInfo.F10 == T6230_F10.MYFX){//每月付息,到期还本
        %>
		//到期还本：按月付息、到期还本
		totalInterest = amount * month * active;
		<%
        }else if(creditInfo.F10 == T6230_F10.YCFQ){//本息到期一次付清
        %>
		//借款到期后一次性归还本金和利息。
		<%
        if(T6231_F21.S == t6231.F21){
        %>//如果是按天计算
		active = yearratio / 360;
		month = <%=t6231.F22%>;
		month = parseInt(month);
		<%
        }
        %>
		totalInterest = month * amount * active;
		<%
        }else if(creditInfo.F10 == T6230_F10.DEBJ){//等额本金
        %>
		totalInterest = ((amount / month + amount * active) + amount / month * (1 + active)) / 2 * month - amount;
		<%
        }
        %>

		//我的体验金
		if($("#tyjRule li.selected").val() != null && $("#tyjRule li.selected").val() != "undefined"){
			tyjYqCountAmount(amount,totalInterest);
		}else{
			showYqsy(totalInterest);
		}

		$("#dxje").html(chinaCost(amount));
	}
	//预期收益计算end

	/**
	 *  体验金计算预期收益
	 */
	function tyjYqCountAmount(amount,totalInterest) {

		var tyjAmount = <%=enperienceAmount %>;
		//判断是否追加体验金,大于0表示已追加.计算体验金利息
		var dataParam = {"tyjAmount":tyjAmount,"biaoId":<%=creditInfo.F01%>};
		if(amount > 0 && tyjAmount > 0){//发送ajax请求
			$.ajax({
				type:"post",
				dataType:"html",
				url:"<%=controller.getURI(request, TyjAmountLoan.class)%>",
				data:dataParam,
				success:function(data){//将计算成功得到的利息与原来的相加
					totalInterest = parseFloat(totalInterest) + parseFloat(data) ;
					showYqsy(totalInterest);
				},
				error:function(){
					showYqsy(totalInterest);
				}
			});
		}else{
			showYqsy(totalInterest);
		}
	}
	//预期收益计算end

	function showYqsy(totalInterest){
		var yqData = (Math.round(totalInterest*100)) / 100;//存款利息：取两位小数
		if (!isNaN(yqData)) {
			$("#yqCountEm").text(yqData);
			//$("#yqCount").show();
		}
	}

	function selectRewardType(){
		//清除已选中的选项
		$("#myRewardType li").removeAttr("class");
		$("#myRewardType li").eq(0).attr("class","selected");
		$("#myRewardType input[name='myRewardType']").val("");
		$("#myRewardType input[type='button']").val("请选择");
		yqCountAmount();
		var $myReward = $("#myRewardType");
		if($("#userReward").is(':checked')){
			$("#myRewardType li").removeAttr("class");
			$("#myRewardType li").eq(0).attr("class","selected");
			$("#myRewardType input[name='myRewardType']").val("");
			$("#myRewardType input[type='button']").val("请选择");
			$myReward.show();
		}else{
			$myReward.hide();
			$(".reward_value").hide();
		}
	}

	$(function () {
		$('#myRewardType').selectlist({
			width: 80,
			optionHeight: 22,
			height: 22,
			onChange:function(){
				showMyReward(this);
			}
		});

		<%if(T6231_F36.ALL == t6231.F36){%>
		$('#hbRule').selectlist({
			width: 178,
			optionHeight: 22,
			height: 22,
			onChange:function(){
				yqCountAmount();
			}
		});
		$('#tyjRule').selectlist({
			width: 178,
			optionHeight: 22,
			height: 22,
			onChange:function(){
				yqCountAmount();
			}
		});
		$('#jxqRule').selectlist({
			width: 178,
			optionHeight: 22,
			height: 22,
			onChange:function(){
				yqCountAmount();
			}
		});
		<%}%>
		<%if(T6231_F36.ALL != t6231.F36){%>
		$("#myRewardType").hide();
		<%}%>
		$("input:checkbox[name='userReward']").attr("checked", false);
		//“我同意”按钮切回事件
		$("input:checkbox[name='iAgree']").attr("checked", false);
		$("input:checkbox[name='iAgree']").click(function() {
			var iAgree = $(this).attr("checked");
			var register = $(".sub-btn");
			if (iAgree) {
				register.removeClass("btn_gray btn_disabled");
				register.attr("id","tbButton");
				//选中“我同意”，绑定事件
				$("#tbButton").click(function(){
					checkBid();
				});
			} else {
				register.addClass("btn_gray btn_disabled");
				$("#tbButton").unbind("click");
				register.attr("id","");
			}
		});
	});


</script>

<div class="popup_bg" style="display: none;"></div>
<div class="dialog" style="display: none;" id="tzConfim">
	<div class="title"><a href="javascript:void(0);" class="out cancel"></a>投资购买确认</div>
	<div class="content">
		<div class="tip_information">
			<div class="successful"></div>
			<div class="tips">
				<span class="f20 gray3">您此次购买金额为<span id="zxMoney"><%=zxMoney%></span>元</span>
				<span class="myReward_tip" id="tyjSpan" style="display: none;"><br/>追加体验金投资：<i class="red"><span id="zxMoney1"><%=enperienceAmount%></span></i>元</span>
				<span class="myReward_tip" id="jxqSpan" style="display: none;"><br/>使用加息券：年化<i class="red"><span id="jxlValue">0.00</span></i>%返利</span>
			</div>
			<div class="f16 border_t border_b mt20 mb20 pt15 pb15 myReward_tip" id="bhDiv" style="display: none;">使用红包
				<span class="red" id="hbAmount">0</span>元
            <span class="ml30" >实际支付金额
            <span class="red" id="realInvestAmount">0</span>元</span>
			</div>
			<%
				boolean tg = BooleanParser.parse(configureProvider.getProperty(SystemVariable.SFZJTG));
				boolean isOpenPwd = BooleanParser.parse(configureProvider.getProperty(PayVariavle.CHARGE_MUST_WITHDRAWPSD));
				if (isOpenPwd)
				{
			%>
			<div class="mt20"><span class="red">*</span>交易密码：
				<input type="password" class="text_style required" maxlength="20" id="tran_pwd" autocomplete="off"/>
				<p class="red" style="display: none"></p>
			</div>
			<%
				}
			%>
		</div>
		<div class="tc mt10"><a href="javascript:void(0);" id="ok" class="btn01">确定</a><a href="javascript:void(0);" class="cancel btn01 btn_gray ml20">取消</a></div>
	</div>
</div>

<script type="text/javascript">
	function getWebSoketConnection() {
		var accountId = '<%=dimengSession ==null || !dimengSession.isAuthenticated()?"":serviceSession.getSession().getAccountId()%>';
		var webSocketUrt = '<%StringHelper.filterHTML(out, configureProvider.getProperty(SystemVariable.SITE_DOMAIN)); %>';
		var webSocket=new WebSocket("ws://"+webSocketUrt+"/pay/websocket/"+accountId);
		webSocket.onerror = function(event) {
			alert(event.data);
		};

		webSocket.onopen = function(event) {
			//alert("连接建立成功");
			//document.getElementById("list").innerHTML="连接建立成功！";
		};

		webSocket.onmessage = function(event) {
			//alert(event.data);
			//document.getElementById("list").innerHTML+="<br/>" + event.data;
			//$("#info").html(showDialogInfoReload(event.data, "successful"));
			$("#info").html(showSuccInfo(event.data, "successful", $("#sbSucc").val()));
		};
	}



	function sendMessage() {
		var msg=document.getElementById("nickname").value+"："+document.getElementById("textarea").value;
		document.getElementById("textarea").value="";
		webSocket.send(msg);
	}
</script>
