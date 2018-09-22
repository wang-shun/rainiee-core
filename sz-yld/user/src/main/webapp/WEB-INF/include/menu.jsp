<%@page import="com.dimeng.p2p.repeater.claim.SubscribeBadClaimManage"%>
<%@page import="com.dimeng.p2p.user.servlets.agreementSign.AgreementSignDetail"%>
<%@page import="com.dimeng.p2p.modules.bid.user.service.entity.AgreementSign"%>
<%@page import="com.dimeng.p2p.user.servlets.credit.NewCreditStatistics"%>
<%@page import="com.dimeng.p2p.user.servlets.capital.OrderServlet"%>
<%@page import="com.dimeng.p2p.S61.entities.T6110"%>
<%@page import="com.dimeng.p2p.account.user.service.UserInfoManage"%>
<%@page import="com.dimeng.p2p.account.user.service.UserBaseManage"%>
<%@page import="com.dimeng.p2p.account.user.service.entity.UserBase"%>
<%@ page import="com.dimeng.p2p.repeater.guarantor.ApplyGuarantorManage" %>
<%@ page import="com.dimeng.p2p.S61.entities.T6125" %>
<%@ page import="com.dimeng.p2p.S61.enums.*" %>
<%
    UserInfoManage uManage = serviceSession.getService(UserInfoManage.class);
	T6110 t6110 =uManage.getUserInfo(serviceSession.getSession().getAccountId());
	T6110_F06 userType = t6110.F06;
	UserBaseManage userManage2 = serviceSession.getService(UserBaseManage.class);
	//账户信息
	UserBase userBase = userManage2.getUserBase();
    ApplyGuarantorManage applyGuarantorManage1 = serviceSession.getService(ApplyGuarantorManage.class);
    T6125 t61251 = applyGuarantorManage1.getGuanterInfo();
    SubscribeBadClaimManage badClaimManage = serviceSession.getService(SubscribeBadClaimManage.class);
    boolean isBuyBadClaim = badClaimManage.checkIsBuyBadClaim(serviceSession.getSession().getAccountId());
    Boolean is_has_guarant1 = Boolean.parseBoolean(configureProvider.getProperty(GuarantorVariavle.IS_HAS_GUARANTOR));
    Boolean is_badclaim_transfer = Boolean.parseBoolean(configureProvider.getProperty(BadClaimVariavle.IS_BADCLAIM_TRANSFER));
    String txnanTbUrl = fileStore.getURL(configureProvider.getProperty(SystemVariable.TXNANTB));
    String txnvTbUrl = fileStore.getURL(configureProvider.getProperty(SystemVariable.TXNVTB));
%>
  <div class="w200">
    <div class="side_info">
      <div class="portrait">
      <a href="<%=controller.getViewURI(request, com.dimeng.p2p.user.servlets.Index.class)%>">
      <%
          if(StringHelper.isEmpty(userBase.icon)){
            		if(!StringHelper.isEmpty(userBase.sex) && userBase.sex.equals("女")){
      %>
      		    <img src='<%=(StringHelper.isEmpty(txnvTbUrl)?controller.getStaticPath(request)+"/images/woman_portrait.jpg":txnvTbUrl)%>'/>
      		<%
      		    }else{
      		%>
          		<img src='<%=(StringHelper.isEmpty(txnanTbUrl)?controller.getStaticPath(request)+"/images/portrait.jpg":txnanTbUrl)%>'/>
      		  <%
      		      }
      		  %>
      <%
          }else {
      %>
          <img width="10" height="100" src="<%=fileStore.getURL(userBase.icon)%>"/>
      <%
          }
      %>
        </a>
      </div>
      <div class="title" title="<%=t6110.F02%>"><%=t6110.F02%></div>
    </div>
    <div class="sidemenu">                	
      <div class="my_account"><a href="<%=controller.getViewURI(request, com.dimeng.p2p.user.servlets.Index.class)%>"><i class="icon"></i>我的账户<%-- <%=configureProvider.getProperty(SystemVariable.SITE_NAME)%> --%></a></div>
        <ul class="menu">
        <%
            if(T6110_F07.HMD == t6110.F07){
        %>
            <li class="group">
              <span class="item <%="ZJGL".equals(CURRENT_CATEGORY)?"up":""%>"><i class="icon01"></i>资金管理</span>
              <ul class="child" <%="ZJGL".equals(CURRENT_CATEGORY)?"style='display:block;'":""%>>
                <li><a <%="CZ".equals(CURRENT_SUB_CATEGORY)?"class=\"cur\"":""%> href="<%configureProvider.format(out,URLVariable.USER_CHARGE);%>">充值</a></li>
              </ul>
            </li>
             <%
                 if(userType == T6110_F06.ZRR || t6110.F18 == T6110_F18.S){
             %> 
            <li class="group">
                <span class="item <%="LCGL".equals(CURRENT_CATEGORY)?"up":""%>"><i class="icon02"></i>理财管理</span>
                <ul class="child" <%="LCGL".equals(CURRENT_CATEGORY)?"style='display:block;'":""%>>
                	<li><a <%="ZQZR".equals(CURRENT_SUB_CATEGORY)?"class=\"cur\"":""%> href="<%=controller.getViewURI(request, com.dimeng.p2p.user.servlets.financing.zqzr.Zqzrz.class)%>">债权转让</a></li>
                </ul>
            </li>
             <%
                 }
             %> 
            <%
                 if(t6110.F10 == T6110_F10.F){
             %>
                <li class="group">
                  <span class="item <%="JKGL".equals(CURRENT_CATEGORY)?"up":""%>"><i class="icon03"></i>借款管理</span>
                  <ul class="child" <%="JKGL".equals(CURRENT_CATEGORY)?"style='display:block;'":""%>>
                    <li><a <%="WDJK".equals(CURRENT_SUB_CATEGORY)?"class=\"cur\"":""%> href="<%=controller.getViewURI(request, com.dimeng.p2p.user.servlets.credit.Repaying.class)%>">我的借款</a></li>
                  </ul>
                </li>
            <%
                }
            %>
            <%
                //开关判断，为false，则不显示公益标
                if(BooleanParser.parse(configureProvider.getProperty(SiteSwitchVariable.DONATION_BID_SWITCH))){
            %>
            <li class="group">
                <span class="item <%="GYGL".equals(CURRENT_CATEGORY)?"up":""%>"><i class="icon10"></i>公益管理</span>
                <ul class="child" <%="GYGL".equals(CURRENT_CATEGORY)?"style='display:block;'":""%>>
                    <li><a <%="DONATION".equals(CURRENT_SUB_CATEGORY)?"class=\"cur\"":""%> href="<%=controller.getViewURI(request, com.dimeng.p2p.user.servlets.financing.donation.DonationList.class)%>">我的公益标</a></li>
                </ul>
            </li>
            <%
                }
            %>
            <%
                 if(t6110.F10 == T6110_F10.S || (is_has_guarant1 && t61251 != null && (t61251.F05 == T6125_F05.SQCG || t61251.F05 == T6125_F05.QXCG))){
             %>
                <li class="group">
                  <span class="item <%="JGDB".equals(CURRENT_CATEGORY)?"up":""%>"><i class="icon11"></i>风险备用金管理</span>
                  <ul class="child" <%="JGDB".equals(CURRENT_CATEGORY)?"style='display:block;'":""%>>
                    <li><a <%="DBYWMX".equals(CURRENT_SUB_CATEGORY)?"class=\"cur\"":""%> href="<%=controller.getViewURI(request, com.dimeng.p2p.user.servlets.fxbyj.Dbywmx.class)%>">担保业务明细</a></li>
                    <li><a <%="DFZQ".equals(CURRENT_SUB_CATEGORY)?"class=\"cur\"":""%> href="<%=controller.getViewURI(request, com.dimeng.p2p.user.servlets.fxbyj.Dfzq.class)%>">垫付债权</a></li>
                  </ul>
                </li>
            <%
                }
            %> 
            <li class="group">
              <span class="item <%="XXGL".equals(CURRENT_CATEGORY)?"up":""%>"><i class="icon05"></i>消息管理</span>
              <ul class="child" <%="XXGL".equals(CURRENT_CATEGORY)?"style='display:block;'":""%>>
                <li><a <%="ZNXX".equals(CURRENT_SUB_CATEGORY)?"class=\"cur\"":""%> href="<%=controller.getViewURI(request, com.dimeng.p2p.user.servlets.letter.Index.class)%>">站内消息</a></li>
              </ul>
            </li>
        <%
            }else{
        %>
            <li class="group">
              <span class="item <%="ZJGL".equals(CURRENT_CATEGORY)?"up":""%>"><i class="icon01"></i>资金管理</span>
              <ul class="child" <%="ZJGL".equals(CURRENT_CATEGORY)?"style='display:block;'":""%>>
                <li><a <%="JYJL".equals(CURRENT_SUB_CATEGORY)?"class=\"cur\"":""%> href="<%=controller.getViewURI(request, com.dimeng.p2p.user.servlets.capital.TradingRecord.class)%>">交易记录</a></li>
                <li><a <%="CZ".equals(CURRENT_SUB_CATEGORY)?"class=\"cur\"":""%> href="<%configureProvider.format(out,URLVariable.USER_CHARGE);%>">充值</a></li>
                <li><a <%="TXGL".equals(CURRENT_SUB_CATEGORY)?"class=\"cur\"":""%> href="<%configureProvider.format(out,URLVariable.USER_WITHDRAW);%>">提现</a></li>
                <li><a <%="DDCX".equals(CURRENT_SUB_CATEGORY)?"class=\"cur\"":""%> href="<%=controller.getViewURI(request, OrderServlet.class)%>">订单查询</a></li>
              </ul>
            </li>
            <%
                if(userType == T6110_F06.ZRR || t6110.F18 == T6110_F18.S){
            %> 
                <li class="group">
                  <span class="item <%="LCGL".equals(CURRENT_CATEGORY)?"up":""%>"><i class="icon02"></i>理财管理</span>
                  <ul class="child" <%="LCGL".equals(CURRENT_CATEGORY)?"style='display:block;'":""%>>
                    <li><a <%="WDZQ".equals(CURRENT_SUB_CATEGORY)?"class=\"cur\"":""%> href="<%=controller.getViewURI(request, com.dimeng.p2p.user.servlets.financing.wdzq.Hszdzq.class)%>">我的投资</a></li>
                    <li><a <%="ZQZR".equals(CURRENT_SUB_CATEGORY)?"class=\"cur\"":""%> href="<%=controller.getViewURI(request, com.dimeng.p2p.user.servlets.financing.zqzr.Zqzrz.class)%>">债权转让</a></li>
                    <li><a <%="LCTJ".equals(CURRENT_SUB_CATEGORY)?"class=\"cur\"":""%> href="<%=controller.getViewURI(request, com.dimeng.p2p.user.servlets.financing.FinancingStatistics.class)%>">理财统计</a></li>
                      <%
                          if(userType == T6110_F06.ZRR && BooleanParser.parse(configureProvider.getProperty(SystemVariable.IS_AUTOBID))){
                      %>
                    <li><a <%="ZDTBGJ".equals(CURRENT_SUB_CATEGORY)?"class=\"cur\"":""%> href="<%configureProvider.format(out,URLVariable.USER_AUTO_BID);%>">自动投资工具</a></li>
                      <%
                          }
                      %>
                  </ul>
                </li>
            <%
                }
            %>
            <%
                if(t6110.F10 == T6110_F10.F){
            %>
                <li class="group">
                  <span class="item <%="JKGL".equals(CURRENT_CATEGORY)?"up":""%>"><i class="icon03"></i>借款管理</span>
                  <ul class="child" <%="JKGL".equals(CURRENT_CATEGORY)?"style='display:block;'":""%>>
                    <li><a <%="WDJK".equals(CURRENT_SUB_CATEGORY)?"class=\"cur\"":""%> href="<%=controller.getViewURI(request, com.dimeng.p2p.user.servlets.credit.Repaying.class)%>">我的借款</a></li>
                    <li><a <%="JKSQCX".equals(CURRENT_SUB_CATEGORY)?"class=\"cur\"":""%> href="<%=controller.getViewURI(request, com.dimeng.p2p.user.servlets.credit.Apply.class)%>">借款申请查询</a></li>
                    <li><a <%="JKTJ_NEW".equals(CURRENT_SUB_CATEGORY)?"class=\"cur\"":""%> href="<%=controller.getViewURI(request, NewCreditStatistics.class)%>">借款统计</a></li>
                  </ul>
                </li>    
            <%}%>
            <%
                //开关判断，为false，则不显示公益标
                if(BooleanParser.parse(configureProvider.getProperty(SiteSwitchVariable.DONATION_BID_SWITCH))){
            %>
            <li class="group">
                <span class="item <%="GYGL".equals(CURRENT_CATEGORY)?"up":""%>"><i class="icon10"></i>公益管理</span>
                <ul class="child" <%="GYGL".equals(CURRENT_CATEGORY)?"style='display:block;'":""%>>
                    <li><a <%="DONATION".equals(CURRENT_SUB_CATEGORY)?"class=\"cur\"":""%> href="<%=controller.getViewURI(request, com.dimeng.p2p.user.servlets.financing.donation.DonationList.class)%>">我的公益标</a></li>
                </ul>
            </li>
            <%
                }
            %>
            <%if(t6110.F10 == T6110_F10.S || (is_has_guarant1 && t61251 != null && (t61251.F05 == T6125_F05.SQCG || t61251.F05 == T6125_F05.QXCG))){%>
                <li class="group">
                  <span class="item <%="JGDB".equals(CURRENT_CATEGORY)?"up":""%>"><i class="icon11"></i>风险备用金管理</span>
                  <ul class="child" <%="JGDB".equals(CURRENT_CATEGORY)?"style='display:block;'":""%>>
                    <li><a <%="DBYWMX".equals(CURRENT_SUB_CATEGORY)?"class=\"cur\"":""%> href="<%=controller.getViewURI(request, com.dimeng.p2p.user.servlets.fxbyj.Dbywmx.class) %>">担保业务明细</a></li>
                    <li><a <%="DFZQ".equals(CURRENT_SUB_CATEGORY)?"class=\"cur\"":""%> href="<%=controller.getViewURI(request, com.dimeng.p2p.user.servlets.fxbyj.Dfzq.class) %>">垫付债权</a></li>
                    <%if(isBuyBadClaim || (is_badclaim_transfer && t6110.F10 == T6110_F10.S && t6110.F19 == T6110_F19.S)){ %>
                    <li><a <%="BLZQZR".equals(CURRENT_SUB_CATEGORY)?"class=\"cur\"":""%> href="/user/fxbyj/blzqzr.html">不良债权转让</a></li>
                  	<%} %>
                  </ul>
                </li>   
            <%}%>

            <li class="group">
              <span class="item <%="ZHGL".equals(CURRENT_CATEGORY)?"up":""%>"><i class="icon04"></i>账户管理</span>
              <ul class="child" <%="ZHGL".equals(CURRENT_CATEGORY)?"style='display:block;'":""%>>
                <%if(t6110.F06 == T6110_F06.ZRR){ %>
                    <li><a <%="GRJCXX".equals(CURRENT_SUB_CATEGORY)?"class=\"cur\"":""%> href="<%=configureProvider.format(URLVariable.USER_BASES)%>?userBasesFlag=1">个人基础信息</a></li>    
                <%}else{%>
                    <li><a <%="QYJCXX".equals(CURRENT_SUB_CATEGORY)?"class=\"cur\"":""%> href="<%=controller.getViewURI(request, com.dimeng.p2p.user.servlets.account.QyBases.class) %>">企业基础信息</a></li>
                    <li><a <%="AQXX".equals(CURRENT_SUB_CATEGORY)?"class=\"cur\"":""%> href="<%=configureProvider.format(URLVariable.COM_FZRR) %>">安全信息</a></li>
                <%}%> 
                <li><a <%="YHKXX".equals(CURRENT_SUB_CATEGORY)?"class=\"cur\"":""%> href="<%configureProvider.format(out,URLVariable.CARD_MANAGE);%>">银行卡信息</a></li>
                <%if(BooleanParser.parse(configureProvider.getProperty(SystemVariable.IS_HAS_NETSIGN))){ %>
                <li><a <%="WQXY".equals(CURRENT_SUB_CATEGORY)?"class=\"cur\"":""%> href="<%=controller.getViewURI(request, AgreementSignDetail.class)%>">网签协议</a></li>
              	<%} %>
              </ul>
            </li>
            <% if(userType == T6110_F06.ZRR && Boolean.parseBoolean(configureProvider.getProperty(MallVariavle.IS_MALL))){%>
        	<li class="group">
               <span class="item <%="WDSC".equals(CURRENT_CATEGORY)?"up":""%>"><i class="icon08"></i>我的商城</span>
               <ul class="child" <%="WDSC".equals(CURRENT_CATEGORY)?"style='display:block;'":""%>>
                   <li><a <%="WDJF".equals(CURRENT_SUB_CATEGORY)?"class=\"cur\"":""%> href="/user/mall/myScore.html">我的积分</a></li>
                   <li><a <%="WDDD".equals(CURRENT_SUB_CATEGORY)?"class=\"cur\"":""%> href="/user/mall/myOrder.htm">我的订单</a></li>
               	   <li><a <%="SHDZGL".equals(CURRENT_SUB_CATEGORY)?"class=\"cur\"":""%> href="/user/mall/harvestAddress.htm"><em class="line"></em>收货地址管理</a></li>
               </ul>
           </li>
           <%} %>
            <%if(t6110.F06 == T6110_F06.ZRR){ %>
            	<li class="group">
	              <span class="item <%="WDJL".equals(CURRENT_CATEGORY)?"up":""%>"><i class="icon07"></i>我的奖励</span>
	              <ul class="child" <%="WDJL".equals(CURRENT_CATEGORY)?"style='display:block;'":""%>>
	              <% if(Boolean.parseBoolean(configureProvider.getProperty(SiteSwitchVariable.REDPACKET_INTEREST_SWITCH))){%>
	                <li><a <%="WDHB".equals(CURRENT_SUB_CATEGORY)?"class=\"cur\"":""%> href="<%=controller.getViewURI(request, com.dimeng.p2p.user.servlets.reward.Wdhb.class) %>">我的红包</a></li>
	                <li><a <%="WDJXQ".equals(CURRENT_SUB_CATEGORY)?"class=\"cur\"":""%> href="<%=controller.getViewURI(request, com.dimeng.p2p.user.servlets.reward.Wdjxq.class) %>">我的加息券</a></li>
	              <%}%>
	                <li><a <%="WDTYJ".equals(CURRENT_SUB_CATEGORY)?"class=\"cur\"":""%> href="<%=controller.getViewURI(request, com.dimeng.p2p.user.servlets.spread.Wdtyj.class) %>">我的体验金</a></li>
	              </ul>
	            </li>
                <li class="group">
                  <span class="item <%="TGGL".equals(CURRENT_CATEGORY)?"up":""%>"><i class="icon06"></i>邀请好友</span>
                  <ul class="child" <%="TGGL".equals(CURRENT_CATEGORY)?"style='display:block;'":""%>>
                    <li><a <%="WYTG".equals(CURRENT_SUB_CATEGORY)?"class=\"cur\"":""%> href="<%=controller.getViewURI(request, com.dimeng.p2p.user.servlets.spread.Wytg.class) %>">推荐好友</a></li>
                  </ul>
                </li>  
        <%}%> 
           <li class="group">
              <span class="item <%="XXGL".equals(CURRENT_CATEGORY)?"up":""%>"><i class="icon05"></i>消息管理</span>
              <ul class="child" <%="XXGL".equals(CURRENT_CATEGORY)?"style='display:block;'":""%>>
                <li><a <%="ZNXX".equals(CURRENT_SUB_CATEGORY)?"class=\"cur\"":""%> href="<%=controller.getViewURI(request, com.dimeng.p2p.user.servlets.letter.Index.class) %>">站内消息</a></li>
              </ul>
            </li>
            <%
            //开关判断，为false，则不显示我要吐槽
            if(BooleanParser.parse(configureProvider.getProperty(SiteSwitchVariable.ADVICE_COMPLAIN_SWITCH))){
            %>
            <li class="group">
                <span class="item <%="TSJY".equals(CURRENT_CATEGORY)?"up":""%>"><i class="icon09"></i>我要吐槽</span>
                <ul class="child" <%="TSJY".equals(CURRENT_CATEGORY)?"style='display:block;'":""%>>
                    <li><a <%="WDTSJY".equals(CURRENT_SUB_CATEGORY)?"class=\"cur\"":""%> href="<%=controller.getViewURI(request, com.dimeng.p2p.user.servlets.tsjy.Wdtsjy.class) %>">我要吐槽</a></li>
                </ul>
            </li>
            <%}%>
        <%}%> 
        </ul>        	
      </div>
    </div>
    <script type="text/javascript">
    $(function(){
        $(".portrait img").bind("mouseover",function(){
            $(".bdimgshare-content").hide();
            $(".bdimgshare-bg").hide();
        });
    });
    </script>