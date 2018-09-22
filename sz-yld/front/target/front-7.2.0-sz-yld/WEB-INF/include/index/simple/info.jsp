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


<%{
    BidManage bidManage = serviceSession.getService(BidManage.class);
    ArticleManage articleManage = serviceSession.getService(ArticleManage.class);
%>
<!--行业资讯--金融学堂--投资排行榜-->
<div class="info_wrap">
    <div class="wrap clearfix">
        <!--行业资讯-->
        <div class="trade">
            <div class="trade_con box-shadow">
                <div class="hd clearfix"> <a href="<%configureProvider.format(out, URLVariable.ZXDT_WDHYZX);%>" class="more">更多&gt;</a>
                    <h2>行业资讯</h2>
                </div>
                <div class="bd clearfix" id="mtbdDataBody">
      <!--               <ul class="trade_ul">
                        <li>
                            <div class="text">
                                <a href="#">
                                    <span>企业级市场的黑马：用友iUAP企业互联网云运维平台</span>
                                </a>
                            </div>
                        </li>
                        <li>
                            <div class="text">
                                <a href="#">
                                    <span>一期授信5亿 北京银行、雨莅贷网络科技“合谋”小微业务</span>
                                </a>
                            </div>
                        </li>
                        <li>
                            <div class="text">
                                <a href="#">
                                    <span>雨莅贷网络科技青年节推“高颜值大片”</span>
                                </a>
                            </div>
                        </li>
                        <li>
                            <div class="text">
                                <a href="#">
                                    <span>【互联网金融新生态沙龙】雨莅贷网络科技：发展需完善生态圈建设</span>
                                </a>
                            </div>
                        </li>
                        <li>
                            <div class="text">
                                <a href="#">
                                    <span>雨莅贷网络科技与北京银行启动战略合作</span>
                                </a>
                            </div>
                        </li>
                    </ul> -->
                </div>
            </div>
        </div>
        <!--行业资讯-->

        <!--金融学堂-->
        <div class="trade">
            <div class="trade_con box-shadow">
                <div class="hd clearfix"> <a href="<%configureProvider.format(out, URLVariable.ZXDT_HLWJRYJ);%>" class="more">更多&gt;</a>
                    <h2>金融学堂</h2>
                </div>
                <div class="bd clearfix" id="jrxyDatayBody">
  <!--                   <ul class="trade_ul">
                        <li>
                            <div class="text">
                                <a href="#">
                                    <span>工信部发声力挺互联网金融 雨莅贷网络科技发展前景广阔</span>
                                </a>
                            </div>
                        </li>
                        <li>
                            <div class="text">
                                <a href="#">
                                    <span>互联网金融人气理性回归，网贷指数持续攀升</span>
                                </a>
                            </div>
                        </li>
                        <li>
                            <div class="text">
                                <a href="#">
                                    <span>网贷成交量超5000亿 是去年同期3倍量</span>
                                </a>
                            </div>
                        </li>
                        <li>
                            <div class="text">
                                <a href="#">
                                    <span>互联网理财成主流投资方式 组合理财受青睐</span>
                                </a>
                            </div>
                        </li>
                        <li>
                            <div class="text">
                                <a href="#">
                                    <span>理财市场利率整体下降 如何让财富持续增值？</span>
                                </a>
                            </div>
                        </li>
                    </ul> -->
                </div>
            </div>
        </div>
        <!--金融学堂-->
        <!--排行榜-->
        <div class="rank">
            <div class="ranking box-shadow clearfix">
                <div class="hd clearfix">
                    <div class="til">
                        <span>投资排行榜</span>
                    </div>
                    <ul class="tab">
                        <li id="ranking1" onclick="setTab('ranking',1,3)" class="hover con">周</li>
                        <li id="ranking2" onclick="setTab('ranking',2,3)">月</li>
                        <li id="ranking3" onclick="setTab('ranking',3,3)">年</li>
                    </ul>
                </div>
                <div class="bd" id="con_ranking_1">
                    <ul class="clearfix">
  <%--                    <% T7050[]  t7050s = bidManage.getUserBidRankForWeek(5);
                            if(t7050s != null){for(int i=0;i<t7050s.length;i++){T7050 t7050 = t7050s[i];%>
                        <tr>
                            <%if(i < 3){ %>
                            <td><i class="i-box public-icon ranking2-icon-<%=i+1%>"><%=i+1%></i></td>
                            <%}else{ %>
                            <td><i class="i-box i-box2"><%=i+1%></i></td>
                            <%} %>
                            <%if(t7050.F02 !=null && t7050.F02.length() > 4) { %>
                            <td><%StringHelper.filterHTML(out, t7050.F02.substring(0, 3)+"***"+t7050.F02.substring(t7050.F02.length()-1, t7050.F02.length())); %></td>
                            <%}else{ %>
                            <td><%StringHelper.filterHTML(out, t7050.F02); %></td>
                            <%} %>
                            <td><%=Formater.formatAmount(t7050.F03)%></td>
                        </tr>
                        <%}}%> --%>
                        
                        <li class="first clearfix">
                            <div class="rank_title rank_w">排名</div>
                            <div class="name_w">用户名</div>
                            <div class="money_w">投资金额（元）</div>
                        </li>
                        
                        
                         <% T7050[]  t7050s = bidManage.getUserBidRankForWeek(5);
                            if(t7050s != null){for(int i=0;i<t7050s.length;i++){T7050 t7050 = t7050s[i];%>
                        <li class="clearfix">                  
                            	<%if(i == 0){ %>
                             		<div class="rank_w rank_num"><span class="no_1"><%=i+1%></span></div>  
                                  <%}else if(i==1){ %> 
                                   	<div class="rank_w rank_num"><span class="no_icon no_2"><%=i+1%></span></div>  
                                    <%}else if(i==2){ %>
                                    	<div class="rank_w rank_num"><span class="no_icon no_3"><%=i+1%></span></div>  
                                    <%}else{ %>
                            <div class="rank_w rank_num"><span class="no_icon no_4"><%=i+1%></span></div>
                            <%} %>
                            <%if(t7050.F02 !=null && t7050.F02.length() > 4) { %>
                             <div class="name_w"><%StringHelper.filterHTML(out, t7050.F02.substring(0, 3)+"***"+t7050.F02.substring(t7050.F02.length()-1, t7050.F02.length())); %></div>
                            <%}else{ %>
                             <div class="name_w"><%StringHelper.filterHTML(out, t7050.F02); %></div>
                            <%} %>
                             <div class="money_w m_con"><%=Formater.formatAmount(t7050.F03)%></div>
                        </li>
                        <%}}%>
                        
                        
                        
                        
                  <!--       
                        
                        <li class="clearfix">
                            <div class="rank_w rank_num"><span class="no_1">1</span></div>
                            <div class="name_w">138 **** 83 </div>
                            <div class="money_w m_con">40,078,209.00</div>
                        </li>
                        <li class="clearfix">
                            <div class="rank_w rank_num"><span class="no_icon no_2">2</span></div>
                            <div class="name_w">138 **** 83 </div>
                            <div class="money_w m_con">40,078,209.00</div>
                        </li>
                        <li class="clearfix">
                            <div class="rank_w rank_num"><span class="no_icon no_3">3</span></div>
                            <div class="name_w">138 **** 83 </div>
                            <div class="money_w m_con">40,078,209.00</div>
                        </li>
                        <li class="clearfix">
                            <div class="rank_w rank_num"><span class="no_icon no_4">4</span></div>
                            <div class="name_w">138 **** 83 </div>
                            <div class="money_w m_con">40,078,209.00</div>
                        </li>
                        <li class="clearfix">
                            <div class="rank_w rank_num"><span class="no_icon no_4">5</span></div>
                            <div class="name_w">138 **** 83 </div>
                            <div class="money_w m_con">40,078,209.00</div>
                        </li> -->
                    </ul>
                </div>
                <div class="bd" id="con_ranking_2" style="display:none;">            
                    <ul class="clearfix">
                         <li class="first clearfix">
                            <div class="rank_title rank_w">排名</div>
                            <div class="name_w">用户名</div>
                            <div class="money_w">投资金额（元）</div>
                        </li>
                     <% T7051[]  t7051s  = bidManage.getUserBidRankForMonth(5);
                            if(t7051s != null){for(int i=0;i<t7051s.length;i++){T7051 t7051 = t7051s[i];%>
                        <li class="clearfix">
                            	<%if(i == 0){ %>
                             		<div class="rank_w rank_num"><span class="no_1"><%=i+1%></span></div>  
                                  <%}else if(i==1){ %> 
                                   	<div class="rank_w rank_num"><span class="no_icon no_2"><%=i+1%></span></div>  
                                    <%}else if(i==2){ %>
                                    	<div class="rank_w rank_num"><span class="no_icon no_3"><%=i+1%></span></div>  
                                    <%}else{ %>
                            <div class="rank_w rank_num"><span class="no_icon no_4"><%=i+1%></span></div>
                            <%} %>
                            <%if(t7051.F02 !=null && t7051.F02.length() > 4) { %>
                            <div class="name_w"><%StringHelper.filterHTML(out, t7051.F02.substring(0, 3)+"***"+t7051.F02.substring(t7051.F02.length()-1, t7051.F02.length())); %></div>
                            <%}else{ %>
                            <div class="name_w"><%StringHelper.filterHTML(out, t7051.F02); %></div>
                            <%} %>
                            <div class="money_w m_con"><%=Formater.formatAmount(t7051.F03)%></div>
                        </li>
                        <%}}%>
                                            
                       <!--  <li class="first clearfix">
                            <div class="rank_title rank_w">排名</div>
                            <div class="name_w">用户名</div>
                            <div class="money_w">投资金额（元）</div>
                        </li>
                        <li class="clearfix">
                            <div class="rank_w rank_num"><span class="no_1">1</span></div>
                            <div class="name_w">138 **** 82 </div>
                            <div class="money_w m_con">40,078,209.00</div>
                        </li>
                        <li class="clearfix">
                            <div class="rank_w rank_num"><span class="no_icon no_2">2</span></div>
                            <div class="name_w">138 **** 82 </div>
                            <div class="money_w m_con">40,078,209.00</div>
                        </li>
                        <li class="clearfix">
                            <div class="rank_w rank_num"><span class="no_icon no_3">3</span></div>
                            <div class="name_w">138 **** 83 </div>
                            <div class="money_w m_con">40,078,209.00</div>
                        </li>
                        <li class="clearfix">
                            <div class="rank_w rank_num"><span class="no_icon no_4">4</span></div>
                            <div class="name_w">138 **** 83 </div>
                            <div class="money_w m_con">40,078,209.00</div>
                        </li>
                        <li class="clearfix">
                            <div class="rank_w rank_num"><span class="no_icon no_4">5</span></div>
                            <div class="name_w">138 **** 83 </div>
                            <div class="money_w m_con">40,078,209.00</div>
                        </li> -->
                    </ul>
                </div>
                <div class="bd" id="con_ranking_3" style="display:none;">
                    <ul class="clearfix">
                         <li class="first clearfix">
                            <div class="rank_title rank_w">排名</div>
                            <div class="name_w">用户名</div>
                            <div class="money_w">投资金额（元）</div>
                        </li>
                     <%T7051[] records = bidManage.getUserBidRankForYear();
                            if(records != null){for(int i=0;i<records.length;i++){T7051 t7051 = records[i];%>
                        <li class="first clearfix">
                             	<%if(i == 0){ %>
                             		<div class="rank_w rank_num"><span class="no_1"><%=i+1%></span></div>  
                                  <%}else if(i==1){ %> 
                                   	<div class="rank_w rank_num"><span class="no_icon no_2"><%=i+1%></span></div>  
                                    <%}else if(i==2){ %>
                                    	<div class="rank_w rank_num"><span class="no_icon no_3"><%=i+1%></span></div>  
                                    <%}else{ %>
		                            <div class="rank_w rank_num"><span class="no_icon no_4"><%=i+1%></span></div>
		                            <%} %>
                            <%if(t7051.F02 !=null && t7051.F02.length() > 4) { %>
                            <div class="name_w"><%StringHelper.filterHTML(out, t7051.F02.substring(0, 3)+"***"+t7051.F02.substring(t7051.F02.length()-1, t7051.F02.length())); %></div>
                            <%}else{ %>
                            <td><%StringHelper.filterHTML(out, t7051.F02); %></td>
                            <%} %>
                            <div class="money_w m_con"><%=Formater.formatAmount(t7051.F03)%></div>
                        </li>
                        <%}}%>
                      <!--   <li class="first clearfix">
                            <div class="rank_title rank_w">排名</div>
                            <div class="name_w">用户名</div>
                            <div class="money_w">投资金额（元）</div>
                        </li>
                        <li class="clearfix">
                            <div class="rank_w rank_num"><span class="no_1">1</span></div>
                            <div class="name_w">138 **** 84 </div>
                            <div class="money_w m_con">40,078,209.00</div>
                        </li>
                        <li class="clearfix">
                            <div class="rank_w rank_num"><span class="no_icon no_2">2</span></div>
                            <div class="name_w">138 **** 83 </div>
                            <div class="money_w m_con">40,078,209.00</div>
                        </li>
                        <li class="clearfix">
                            <div class="rank_w rank_num"><span class="no_icon no_3">3</span></div>
                            <div class="name_w">138 **** 83 </div>
                            <div class="money_w m_con">40,078,209.00</div>
                        </li>
                        <li class="clearfix">
                            <div class="rank_w rank_num"><span class="no_icon no_4">4</span></div>
                            <div class="name_w">138 **** 84 </div>
                            <div class="money_w m_con">40,078,209.00</div>
                        </li>
                        <li class="clearfix">
                            <div class="rank_w rank_num"><span class="no_icon no_4">5</span></div>
                            <div class="name_w">138 **** 84 </div>
                            <div class="money_w m_con">40,078,209.00</div>
                        </li> -->
                    </ul>
                </div>
            </div>
        </div>
        <!--排行榜-->
    </div>
</div>
<!--行业资讯--金融学堂--投资排行榜-->
<!--媒体资讯--发布记录-->
<div class="pro_rel">
    <div class="wrap clearfix pt40">
        <!--媒体资讯-->
        <div class="projects">
            <div class="hd clearfix"> <a href="<%configureProvider.format(out, URLVariable.ZXDT_MTBD);%>" class="more">更多&gt;</a>
                <h2>媒体报道</h2>
            </div>
            <div class="bd clearfix" id="MTBDBody">
                <div class="pro_img">
                    <a href="#">
                        <img src="images/temp/mt_img.jpg"/>
                    </a>
                </div>           
               <!--  <ul class="media_ul" >
                    <li>
                        <div class="text" id="mtbdDataBody">
                            <a href="#">
                                <i></i>
                                <span>关于雨莅贷网络科技基金货币基金T+0快速赎回业务暂停通知</span>
                            </a>
                        </div>
                    </li>
                    <li>
                        <div class="text">
                            <a href="#">
                                <i></i>
                                <span>【互联网金融新生态沙龙】雨莅贷网络科技：发展需完善生态圈建设</span>
                            </a>
                        </div>
                    </li>
                    <li>
                        <div class="text">
                            <a href="#">
                                <i></i>
                                <span>雨莅贷网络科技认养200颗百年古树：用商业思维做公益</span>
                            </a>
                        </div>
                    </li>
                    <li>
                        <div class="text">
                            <a href="#">
                                <i></i>
                                <span>雨莅贷网络科技总裁向总：未来属于共享金融和物联网金融</span>
                            </a>
                        </div>
                    </li>
                    <li>
                        <div class="text">
                            <a href="#">
                                <i></i>
                                <span>深圳雨莅贷网络科技携手深圳晚报、维吉达尼发起新疆喀什小圆枣古树认养计划</span>
                            </a>
                        </div>
                    </li>
                    <li>
                        <div class="text">
                            <a href="#">
                                <i></i>
                                <span>结盟腾讯、华为、阿里，雨莅贷网络科技进入奔跑状态</span>
                            </a>
                        </div>
                    </li>
                </ul> -->
            </div>
        </div>
        <!--媒体资讯-->
        
        <!--发布记录-->
        <div class="relea">
            <div class="release">
                <div class="hd clearfix">
                    <ul>
                        <li id="release1" onMouseOver="setTab('release',1,2)" class="hover">TA投资了</li>
                        <li id="release2" onMouseOver="setTab('release',2,2)">TA发布了</li>
                    </ul>
                </div>
                <div id="con_release_1" class="release_bd">
                    <ul class="infoList">
                                <%
                            TaManage taManage = serviceSession.getService(TaManage.class);
                            List<InvestInfo> publishBids = taManage.getPublishBids();
                            List<InvestInfo> investments = taManage.getInvestments();
                        %>

                        <%if (investments != null) {for (InvestInfo investment : investments) {%>
                        <li>
                            <span class="gray6"><%=investment.intervalTime%>前：</span>
                            <span class="_highlight"><%=investment.loginName%></span><br />投资了金额 ¥<%=Formater.formatAmount(investment.amount)%>，<%StringHelper.filterHTML(out, StringHelper.truncation(investment.biddingTitle, 12));%></li>                       
                        <%}}%>
                    </ul>
                </div>
                <div id="con_release_2" class="release_bd" style="display:none;">
                    <ul class="infoList">
                    
                          <%if (publishBids != null) {for (InvestInfo publishBid : publishBids) {%>
                        <li><span class="gray6"><%=publishBid.intervalTime%>前：</span><span class="_highlight"><%=publishBid.loginName%></span><br />发布了借款金额 ¥<%=Formater.formatAmount(publishBid.amount)%>，<%StringHelper.filterHTML(out, StringHelper.truncation(publishBid.biddingTitle, 12));%></li>
                                    
                        <%}}%>               
                    </ul>
                </div>
            </div>
        </div>
        <!--发布记录-->
        
    </div>
</div>
<!--媒体资讯--发布记录-->

<%-- <div class="new-container clearfix">
    <!--left box-->
    <div class="left-box">
        <div class="pr40">
            <!--media-->
            <div class="media">
                <div class="new-hd"><a href="<%configureProvider.format(out, URLVariable.ZXDT_MTBD);%>" class="more">更多></a><span class="media_title"><%=articleManage.getCategoryNameByCode("MTBD")%></span></div>
                <div class="new-bd media-bd" id="MTBDBody">
                    <div class="media-pic"><a href="#"><img src=""></a></div>
                </div>
            </div>
            <!--media end-->

            <!--information-->
            <div class="information mt15">
                <div class="new-hd"><a href="<%configureProvider.format(out, URLVariable.ZXDT_WDHYZX);%>" class="more">更多></a><span class="media_title"><%=articleManage.getCategoryNameByCode("WDHYZX")%></span></div>
                <div class="new-bd" id="mtbdDataBody">

                </div>
            </div>
            <!--information end-->

            <!--school-->
            <div class="school mt15">
                <div class="new-hd"><a href="<%configureProvider.format(out, URLVariable.ZXDT_HLWJRYJ);%>" class="more">更多></a><span class="media_title"><%=articleManage.getCategoryNameByCode("HLWJRYJ")%></span></div>
                <div class="new-bd" id="jrxyDatayBody">

                </div>
            </div>
            <!--school end-->

        </div>
    </div>

    <!--right box-->
    <div class="right-box">

        <!--ranking-->
        <div class="ranking-container">
            <div class="title"><i class="icon ranking-icon"></i><span>投资排行榜</span></div>
            <div class="tab-nav tab-hd-container">
                <a id="ranking1" href="javascript:setTab('ranking',1,3)" class="tab-item-btn tab-a selectd">周</a>
                <a id="ranking2" href="javascript:setTab('ranking',2,3)" class="tab-item-btn tab-a">月</a>
                <a id="ranking3" href="javascript:setTab('ranking',3,3)" class="tab-item-btn tab-a">年</a>
            </div>
            <div class="content tab-bd-container">
                <!--box 1-->
                <div class="tab-item-con" id="con_ranking_1">
                    <table class="table">
                        <thead>
                        <tr>
                            <th>排名</th>
                            <th>用户名</th>
                            <th>投资金额(元)</th>
                        </tr>
                        </thead>
                        <tbody>
                        <% T7050[]  t7050s = bidManage.getUserBidRankForWeek(5);
                            if(t7050s != null){for(int i=0;i<t7050s.length;i++){T7050 t7050 = t7050s[i];%>
                        <tr>
                            <%if(i < 3){ %>
                            <td><i class="i-box public-icon ranking2-icon-<%=i+1%>"><%=i+1%></i></td>
                            <%}else{ %>
                            <td><i class="i-box i-box2"><%=i+1%></i></td>
                            <%} %>
                            <%if(t7050.F02 !=null && t7050.F02.length() > 4) { %>
                            <td><%StringHelper.filterHTML(out, t7050.F02.substring(0, 3)+"***"+t7050.F02.substring(t7050.F02.length()-1, t7050.F02.length())); %></td>
                            <%}else{ %>
                            <td><%StringHelper.filterHTML(out, t7050.F02); %></td>
                            <%} %>
                            <td><%=Formater.formatAmount(t7050.F03)%></td>
                        </tr>
                        <%}}%>
                        </tbody>
                    </table>
                </div>
                <!--box 1 end-->

                <!--box 2-->
                <div class="tab-item-con" style="display:none;"  id="con_ranking_2">
                    <table class="table">
                        <thead>
                        <tr>
                            <th>排名</th>
                            <th>用户名</th>
                            <th>投资金额(元)</th>
                        </tr>
                        </thead>
                        <tbody>
                        <% T7051[]  t7051s  = bidManage.getUserBidRankForMonth(5);
                            if(t7051s != null){for(int i=0;i<t7051s.length;i++){T7051 t7051 = t7051s[i];%>
                        <tr>
                            <%if(i < 3){ %>
                            <td><i class="i-box public-icon ranking2-icon-<%=i+1%>"><%=i+1%></i></td>
                            <%}else{ %>
                            <td><i class="i-box i-box2"><%=i+1%></i></td>
                            <%} %>
                            <%if(t7051.F02 !=null && t7051.F02.length() > 4) { %>
                            <td><%StringHelper.filterHTML(out, t7051.F02.substring(0, 3)+"***"+t7051.F02.substring(t7051.F02.length()-1, t7051.F02.length())); %></td>
                            <%}else{ %>
                            <td><%StringHelper.filterHTML(out, t7051.F02); %></td>
                            <%} %>
                            <td><%=Formater.formatAmount(t7051.F03)%></td>
                        </tr>
                        <%}}%>
                        </tbody>
                    </table>
                </div>
                <!--box 2 end-->

                <!--box 3-->
                <div class="tab-item-con hide" id="con_ranking_3">
                    <table class="table">
                        <thead>
                        <tr>
                            <th>排名</th>
                            <th>用户名</th>
                            <th>投资金额(元)</th>
                        </tr>
                        </thead>
                        <tbody>
                        <%T7051[] records = bidManage.getUserBidRankForYear();
                            if(records != null){for(int i=0;i<records.length;i++){T7051 t7051 = records[i];%>
                        <tr>
                            <%if(i < 3){ %>
                            <td><i class="i-box public-icon ranking2-icon-<%=i+1%>"><%=i+1%></i></td>
                            <%}else{ %>
                            <td><i class="i-box i-box2"><%=i+1%></i></td>
                            <%} %>
                            <%if(t7051.F02 !=null && t7051.F02.length() > 4) { %>
                            <td><%StringHelper.filterHTML(out, t7051.F02.substring(0, 3)+"***"+t7051.F02.substring(t7051.F02.length()-1, t7051.F02.length())); %></td>
                            <%}else{ %>
                            <td><%StringHelper.filterHTML(out, t7051.F02); %></td>
                            <%} %>
                            <td><%=Formater.formatAmount(t7051.F03)%></td>
                        </tr>
                        <%}}%>
                        </tbody>
                    </table>
                </div>
                <!--box 3 end-->


            </div>
        </div>
        <!--ranking end-->

        <!--ranking-->
        <div class="ranking-container mt20">
            <div class="already-container">
                <div class="title tab-hd-container">
                    <a id="release1" href="javascript:setTab('release',1,2)" class="tab-item-btn selectd">TA发布了</a><span class="pl10 pr10">|</span>
                    <a id="release2" href="javascript:setTab('release',2,2)" class="tab-item-btn">TA投资了</a>
                </div>
                <div class="content tab-bd-container release_bd1" id="con_release_1" >

                    <!--box1-->

                    <ul class="already-ul tab-item-con infoList1">
                        <%
                            TaManage taManage = serviceSession.getService(TaManage.class);
                            List<InvestInfo> publishBids = taManage.getPublishBids();
                            List<InvestInfo> investments = taManage.getInvestments();
                        %>

                        <%if (publishBids != null) {for (InvestInfo publishBid : publishBids) {%>
                        <li>
                            <div class="li-item">
                                <p class="p-title"><%=publishBid.intervalTime%>前：<span class="orange"><%=publishBid.loginName%></span></p>
                                <p>发布了借款金额 ¥<%=Formater.formatAmount(publishBid.amount)%>，<%StringHelper.filterHTML(out, StringHelper.truncation(publishBid.biddingTitle, 12));%></p>
                            </div>
                        </li>
                        <%}}%>

                    </ul>

                    <!--box1 end-->


                </div>

                <div class="content tab-bd-container release_bd2 hide"  id="con_release_2" >
                    <!--box2-->

                    <ul class="already-ul tab-item-con infoList2">

                        <%if (investments != null) {for (InvestInfo investment : investments) {%>
                        <li style="height: 78px;">
                            <div class="li-item">
                                <p class="p-title"><%=investment.intervalTime%>前：<span class="orange"><%=investment.loginName%></span></p>
                                <p>发布了借款金额 ¥<%=Formater.formatAmount(investment.amount)%>，<%StringHelper.filterHTML(out, StringHelper.truncation(investment.biddingTitle, 12));%></p>
                            </div>
                        </li>
                        <%}}%>
                    </ul>

                    <!--box2 end-->
                    </div>
            </div>

        </div>
        <!--ranking end-->

    </div>
</div> --%>
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

<!-- <script type="text/javascript">

    //tab切换
    function setTab(name,cursel,n){
        var hover="selectd";
        for(var i=1;i<=n;i++){
            var menu=$("#"+name+i);
            var con=document.getElementById("con_"+name+"_"+i);
            if(i==cursel){
                menu.addClass(hover);
            }
            else{
                menu.removeClass(hover);
            }

            if (con)con.style.display=i==cursel?"block":"none";
        }
    }
</script> -->