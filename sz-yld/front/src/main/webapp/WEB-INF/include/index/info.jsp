<%@page import="com.dimeng.p2p.modules.base.front.service.entity.AdvertSpscRecord"%>
<%@page import="com.dimeng.p2p.modules.base.front.service.AdvertisementManage"%>
<%@page import="com.dimeng.p2p.S50.enums.T5021_F07"%>
<%@page import="com.dimeng.p2p.front.servlets.Info"%>
<%@page import="com.dimeng.p2p.front.servlets.zxdt.Wzgg"%>
<%@page import="com.dimeng.p2p.S50.entities.T5015"%>
<%@page import="com.dimeng.p2p.modules.base.front.service.NoticeManage"%>
<%@page import="com.dimeng.p2p.S50.entities.T5011"%>
<%@page import="com.dimeng.p2p.modules.base.front.service.ArticleManage"%>
<%@page import="com.dimeng.p2p.S50.enums.T5011_F02"%>
<%@page import="com.dimeng.p2p.front.servlets.AbstractFrontServlet"%>
<%@page import="com.dimeng.p2p.front.servlets.zxdt.Mtbd"%>
<%@page import="com.dimeng.p2p.front.servlets.zxdt.Hlwjryj"%>
<%@page import="com.dimeng.p2p.front.servlets.zxdt.Wdhyzx"%>
<%@page import="com.dimeng.p2p.common.enums.ArticleType"%>
<%@page import="com.dimeng.p2p.S70.entities.T7050"%>
<%@page import="com.dimeng.p2p.S70.entities.T7051"%>
<%@page import="com.dimeng.p2p.modules.bid.front.service.BidManage"%>
<%@page import="java.util.List" %>
<%@page import="com.dimeng.p2p.modules.base.front.service.entity.InvestInfo" %>
<%@page import="com.dimeng.p2p.modules.base.front.service.TaManage" %>
<div class="clear"></div>
<!--投资流程-->
<div class="technological_bg">
    <div class="technological w1002">
        <div class="title">投资流程<br/><i></i></div>    
        <ul class="clearfix">
            <li>
                <div class="icon ico01"><span>免费注册</span></div>
                <div class="text">30秒轻松免费注册成为会员</div>
            </li>
            <li class="line"></li>
            <li>
                <div class="icon ico02"><span>&nbsp;进行认证</span></div>
                <div class="text">填写个人资料，完成实名认证</div>
            </li>
            <li class="line"></li>
            <li>
                <div class="icon ico03"><span>充值账户</span></div>
                <div class="text">在线充值，免收手续费</div>
            </li>
            <li class="line"></li>
            <li>
                <div class="icon ico04"><span>&nbsp;&nbsp;成功投资</span></div>
                <div class="text">按需筛选借款标并进行投资</div>
            </li>
            <li class="line"></li>
            <li>
                <div class="icon ico05"><span>&nbsp;&nbsp;自动收款</span></div>
                <div class="text">投资人借款满期后自动回款</div>
            </li>
        </ul>
    </div>
</div>
<!--投资流程-->

<%{
    BidManage bidManage = serviceSession.getService(BidManage.class);
    ArticleManage articleManage = serviceSession.getService(ArticleManage.class);
%>
<div class="w1002 clearfix">
  <!--媒体报道-->
  <div class="media" >
	<div class="main_hd clearfix mt40 mtbd"><h2><%=articleManage.getCategoryNameByCode("MTBD")%></h2><a href="<%configureProvider.format(out, URLVariable.ZXDT_MTBD);%>" class="fr">更多&gt;</a></div>
    <div class="bd clearfix" id="MTBDBody">
    </div>
  </div>
  <!--媒体报道-->
<!--发布-->
  <div class="release">
        <div class="hd clearfix">
        	<ul>
            	<li id="release1" onclick="setTab('release',1,2)" class="hover">TA发布了</li>
                <li>|</li>
                <li id="release2" onclick="setTab('release',2,2)">TA投资了</li>
            </ul>
        </div>
        <div id="con_release_1" class="release_bd">
        <%
                    TaManage taManage = serviceSession.getService(TaManage.class);
                    List<InvestInfo> publishBids = taManage.getPublishBids();
                    List<InvestInfo> investments = taManage.getInvestments();
        %>
            <ul class="infoList">
                <%if (publishBids != null) {for (InvestInfo publishBid : publishBids) {%>
                <li><%=publishBid.intervalTime%>前：<span class="highlight"><%=publishBid.loginName%></span><br/>发布了借款金额 ¥<%=Formater.formatAmount(publishBid.amount)%>，<%StringHelper.filterHTML(out, StringHelper.truncation(publishBid.biddingTitle, 12));%></li>
                <%}}%>
            </ul>
        </div>
        <div id="con_release_2" class="release_bd" style="display:none;">
            <ul class="infoList">
                <%if (investments != null) {for (InvestInfo investment : investments) {%>
                <li><%=investment.intervalTime%>前：<span class="highlight"><%=investment.loginName%></span><br/>借出了金额 ¥<%=Formater.formatAmount(investment.amount)%>，<%StringHelper.filterHTML(out, StringHelper.truncation(investment.biddingTitle, 12));%></li>
                <%}}%>
            </ul>
        </div>
    </div>
  <!--发布-->
  </div>

<div class="w1002 clearfix" style="margin-bottom: 20px;">
<!--行业资讯-->
	<div class="news" id="infoDataBody">
    	<div class="hd clearfix" id="infoHd">
        	<ul>
                <li id="WDHYZX" onclick="initInfoData('WDHYZX')" class="hover"><%=articleManage.getCategoryNameByCode("WDHYZX")%></li>
                <li>|</li>
                <li id="HLWJRYJ" onclick="initInfoData('HLWJRYJ')"><%=articleManage.getCategoryNameByCode("HLWJRYJ")%></li>
            </ul>
            <div class="fr moreInfo"><a href="#">更多&gt;</a></div>
        </div>
    </div>
    <!--行业资讯-->
	<!--投资排行榜-->
    <div class="w240 ranking">
      <h2 class="hd"><i class="icon"></i>投资排行榜</h2>
      <div class="til">
          <ul>
              <li id="ranking1" onclick="setTab('ranking',1,3)" class="hover">周</li>
              <li id="ranking2" onclick="setTab('ranking',2,3)">月</li>
              <li id="ranking3" onclick="setTab('ranking',3,3)">年</li>
          </ul>
      </div>
      <div class="bd" id="con_ranking_1">
          <table width="100%" border="0" cellspacing="0" cellpadding="0">
              <tr>
                <td width="23%" align="center">排名</td>
                <td width="35%" align="left">用户名</td>
                <td width="42%" align="left">投资金额（元）</td>
            </tr>
             <% T7050[]  t7050s = bidManage.getUserBidRankForWeek(5);
                if(t7050s != null){for(int i=0;i<t7050s.length;i++){T7050 t7050 = t7050s[i];%>
               <tr>
                 <%if(i < 3){ %>
                 <td align="center"><span class="top top_r<%=i+1%>"><%=i+1%></span></td>
                 <%}else{ %>
                 <td align="center"><span class="icon"><%=i+1%></span></td>
                 <%} %>
                 <%if(t7050.F02 !=null && t7050.F02.length() > 4) { %>
                 <td align="left"><%StringHelper.filterHTML(out, t7050.F02.substring(0, 3)+"***"+t7050.F02.substring(t7050.F02.length()-1, t7050.F02.length())); %></td>
                 <%}else{ %>
                  <td align="left"><%StringHelper.filterHTML(out, t7050.F02); %></td>
                  <%} %>
                 <td align="left">￥<%=Formater.formatAmount(t7050.F03)%></td>
             </tr>
             <%}}%>
          </table>
        </div>
        <div class="bd" id="con_ranking_2" style="display:none;">
          <table width="100%" border="0" cellspacing="0" cellpadding="0">
              <tr>
                <td width="23%" align="center">排名</td>
                <td width="35%" align="left">用户名</td>
                <td width="42%" align="left">投资金额（元）</td>
            </tr>
              <% 
                		T7051[]  t7051s  = bidManage.getUserBidRankForMonth(5);
                       if(t7051s != null){for(int i=0;i<t7051s.length;i++){T7051 t7051 = t7051s[i];%>
                      <tr>
                        <%if(i < 3){ %>
                        <td align="center"><span class="top top_r<%=i+1%>"><%=i+1%></span></td>
                        <%}else{ %>
                        <td align="center"><span class="icon"><%=i+1%></span></td>
                        <%} %>
                        <%if(t7051.F02 !=null && t7051.F02.length() > 4) { %>
                        <td align="left"><%StringHelper.filterHTML(out, t7051.F02.substring(0, 3)+"***"+t7051.F02.substring(t7051.F02.length()-1, t7051.F02.length())); %></td>
                        <%}else{ %>
                         <td align="left"><%StringHelper.filterHTML(out, t7051.F02); %></td>
                         <%} %>         
                        <td align="left">￥<%=Formater.formatAmount(t7051.F03)%></td>
                    </tr>
                     <%}}%>
          </table>
        </div>
        <div class="bd" id="con_ranking_3" style="display:none;">
          <table width="100%" border="0" cellspacing="0" cellpadding="0">
              <tr>
                <td width="23%" align="center">排名</td>
                <td width="35%" align="left">用户名</td>
                <td width="42%" align="left">投资金额（元）</td>
            </tr>
              <%T7051[] records = bidManage.getUserBidRankForYear();
                       if(records != null){for(int i=0;i<records.length;i++){T7051 t7051 = records[i];%>
                      <tr>
                      	<%if(i < 3){ %>
                        <td align="center"><span class="top top_r<%=i+1%>"><%=i+1%></span></td>
                        <%}else{ %>
                        <td align="center"><span class="icon"><%=i+1%></span></td>
                        <%} %>
                        <%if(t7051.F02 !=null && t7051.F02.length() > 4) { %>
                        <td align="left"><%StringHelper.filterHTML(out, t7051.F02.substring(0, 3)+"***"+t7051.F02.substring(t7051.F02.length()-1, t7051.F02.length())); %></td>
                        <%}else{ %>
                         <td align="left"><%StringHelper.filterHTML(out, t7051.F02); %></td>
                         <%} %>
                        <td align="left">￥<%=Formater.formatAmount(t7051.F03)%></td>
                    </tr>
                    <%}}%>
          </table>
        </div>
	</div>
    <!--投资排行榜-->
</div>
<%}%>
<script type="text/javascript">
var _infoUrl = "<%=controller.getURI(request, Info.class)%>";
var _mtbdMoreUrl = "<%configureProvider.format(out, URLVariable.ZXDT_MTBD);%>";
var _wdhyzxMoreUrl = "<%configureProvider.format(out, URLVariable.ZXDT_WDHYZX);%>";
var _wzggMoreUrl = "<%configureProvider.format(out, URLVariable.ZXDT_WZGG);%>";
var _hlwjryjMoreUrl = "<%configureProvider.format(out, URLVariable.ZXDT_HLWJRYJ);%>";
var _mtbdXqUrl = "/zxdt/mtbd/";
var _wdhyzxXqUrl = "/zxdt/wdhyzx/";
var _wzggXqUrl = "/zxdt/wzgg/";
var _hlwjryjXqUrl = "/zxdt/hlwjryj/";
</script>