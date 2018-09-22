<%@page import="java.util.Date"%>
<%@page import="com.dimeng.p2p.S62.enums.T6230_F20"%>
<%@page import="com.dimeng.p2p.S62.enums.T6231_F21"%>
<%@page import="com.dimeng.p2p.front.servlets.financing.sbtz.Index"%>
<%@page import="com.dimeng.p2p.S62.enums.T6230_F14"%>
<%@page import="com.dimeng.p2p.S62.enums.T6230_F13"%>
<%@page import="com.dimeng.p2p.S62.enums.T6230_F11"%>
<%@page import="com.dimeng.p2p.S62.enums.T6230_F28"%>
<%@page import="com.dimeng.p2p.S61.enums.T6110_F07"%>
<%@page import="com.dimeng.p2p.S61.enums.T6110_F06"%>
<%@page import="com.dimeng.p2p.S62.enums.T6231_F29"%>
<%@page import="com.dimeng.p2p.S62.enums.T6231_F27"%>
<%@page import="com.dimeng.p2p.S61.entities.T6110"%>
<%@page import="com.dimeng.p2p.modules.bid.front.service.entity.Bdlb"%>
<%@page import="com.dimeng.p2p.modules.bid.front.service.BidManage"%>
<%@page import="com.dimeng.p2p.front.servlets.AbstractFrontServlet"%>
<%@page import="com.dimeng.p2p.account.front.service.UserInfoManage"%>
<%@ page import="java.sql.Timestamp" %>
<div class="clear"></div>
<%
    {
    	BidManage bidManage = serviceSession.getService(BidManage.class);
    	PagingResult<Bdlb> crePagingResult = bidManage.searchTJB(null, AbstractFrontServlet.INDEX_PAGING);
    	Bdlb[] credits = crePagingResult.getItems();
    	if (credits == null){
    	    dimengSession.setAttribute("tjbID", null);
    	}
		Timestamp timemp = bidManage.getCurrentTimestamp();
    	if (credits != null) {
    	    for (Bdlb credit : credits) {
			    if(credit.F33 == T6231_F29.S){
			        dimengSession.setAttribute("tjbId", credit.F02+"");
%>

<!--项目推荐-->
        <div class="invest_rec pt40 ">
            <div class="invest_item">
                <div class="left_wrap">
                    <div class="l_con">
                        <div class="l_til">项目推荐</div>
                        <span class="in_bg in_cloud_bg"></span>
                        <span class="in_bg in_cloud_bg2"></span>
                        <span class="in_bg in_cloud_bg3"></span>
                        <span class="in_bg in_rocket"></span>
                    </div>
                </div>
                <div class="right_wrap">
                    <div class="in_til">		                     
		                       <%if(credit.F28 == T6230_F28.S){ %>
									<span class="item_icon novice_icon pl">新</span>
									<% } if(credit.F29 == T6231_F27.S){%>
									<span class="item_icon reward_icon pl">奖</span>
									<% }%>
									<%if(credit.F16 == T6230_F11.S){
									%><span class="item_icon xin_icon pl">保</span><%
									}else if(credit.F17 == T6230_F13.S){
									%><span class="item_icon xin_icon pl">抵</span><%
									}else if(credit.F18 == T6230_F14.S ){
									%><span class="item_icon xin_icon pl">实</span><%
									}else{
									%><span class="item_icon xin_icon ">信</span><%
									}
								%>
                        <a class="item_til" href="<%=controller.getPagingItemURI(request, Index.class,credit.F02)%>">
                            <span class="til_con"><%StringHelper.filterHTML(out, credit.F04);%></span>
                        </a>
                    </div>
                    <div class="in_con clearfix">
                        <div class="info_item data_rate">
                            <p>年化利率</p>
                            <a class="profit" href="javascript:;"><%=Formater.formatRate(credit.F07)%><span class="profit_unit"></span></a>
                        </div>
                        <div class="info_item data_det">
                            <div class="det_item clearfix">
                            	<ul>
                                	<li class="det-w1">借款金额：</li>
					                  <%if(credit.F06.doubleValue()>=10000){
											String amount = Formater.formatAmount(credit.F06.doubleValue()/10000);
										%>
										
									<li class="det-w2"><b class="item_value"><%=amount%></b></li>                              
                                    <li class="det-w3">万元</li>
										<%}else{
											String amount = Formater.formatAmount(credit.F06.doubleValue());
										%>
									<li class="det-w2"><b class="item_value"><%=amount%></b></li>                              
                                    <li class="det-w3">元</li>
										<%}%>
                                </ul>
                            </div>
                            <div class="det_item">
                            	<ul>
                                	<li class="det-w1">借款期限：</li>
                                		<%if(credit.F19 == T6231_F21.F){%>
										<li class="det-w2"><span class="item_value"><%=credit.F10%></span></li>
										<li class="det-w3">个月</li>
										<%}else{%>							
										<li class="det-w2"><span class="item_value"><%=credit.F20%> </span></li>
										 <li class="det-w3">天</li>
										<%}%>
                                </ul>
                            </div>
                        </div>
                        <div class="info_item data_pro data-progress">
                            <div class="rate_progress">
                                <div class="progress_text item_progress">进度：<b class="graph_text"><%=Formater.formatProgress(credit.proess)%></b></div>
                                <div class="item_graph item_progress">
                                    <b class="graph_bar processBar" style="width:<%=(int)(credit.proess*100)%>%"></b>
                                </div>
                            </div>
                            <!--立即投标-->
                            <!--<a class="grab_btn btn_active f16" href="#">
                                <em></em>
                                <i>立即投标</i>
                            </a>-->
                            <!--预发布-->                   
                            <%if(credit.F11 == T6230_F20.YFB){%>
				<a href="<%=controller.getPagingItemURI(request, com.dimeng.p2p.front.servlets.financing.sbtz.Index.class,credit.F02)%>" class="grab_btn btn_recommend f16">预发布</a>
				<%}else{
					if(credit.F11 == T6230_F20.TBZ){
						if(dimengSession!=null && dimengSession.isAuthenticated()){
							UserInfoManage uManage = serviceSession.getService(UserInfoManage.class);
							T6110 t6110 =uManage.getUserInfo(serviceSession.getSession().getAccountId());
							if(T6110_F07.HMD == t6110.F07){%>
				<a href="javascript:void(0)" class="btn-border-gray"><%=credit.F11.getChineseName()%></a>
				<%}else {%>
				<a href="<%=controller.getPagingItemURI(request, com.dimeng.p2p.front.servlets.financing.sbtz.Index.class,credit.F02)%>" class="grab_btn btn_recommend f16">立即投资</a>
				<%}%>
				<%}else{%>
				<a href="<%=controller.getPagingItemURI(request, com.dimeng.p2p.front.servlets.financing.sbtz.Index.class,credit.F02)%>" class="grab_btn btn_recommend f16">立即投资</a>
				<%}%>
				<%}else if(credit.F11 == T6230_F20.DFK){%>
				<a href="javascript:void(0)" class="btn-border-gray"><%=credit.F11.getChineseName()%></a>
				<%}else{%>
				<a href="javascript:void(0)" class="btn-border-gray"><%=credit.F11.getChineseName()%></a>
				<%}%>
				<%}%>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <!--项目推荐-->
<!--recommend-->
<%-- <div class="recommend-container">
	<div class="recommend-title public-icon">推荐标</div>
	<table class="datatable">
		<tr>
			<td width="9%">
				<%if(credit.F28 == T6230_F28.S){ %>
				<span class="item_icon novice_icon">新</span>
				<% } if(credit.F29 == T6231_F27.S){%>
				<span class="item_icon reward_icon">奖</span>
				<% }%>
				<%if(credit.F16 == T6230_F11.S){
				%><span class="item_icon dan_icon">保</span><%
				}else if(credit.F17 == T6230_F13.S){
				%><span class="item_icon di_icon">抵</span><%
				}else if(credit.F18 == T6230_F14.S ){
				%><span class="item_icon shi_icon">实</span><%
				}else{
				%><span class="item_icon xin_icon">信</span><%
				}
			%>

			</td>
			<td class="td-title tl">
				<div class="title-container"><a href="<%=controller.getPagingItemURI(request, Index.class,credit.F02)%>"><%StringHelper.filterHTML(out, credit.F04);%></a></div>
			</td>
			<td width="10%">
				<div class="f20 gray3"><%=Formater.formatRate(credit.F07)%></div>
				<div class="gray9">年化利率</div></td>
			<td width="10%">
				<div class="f20 gray3">
				<%if(credit.F19 == T6231_F21.F){%>
				<%=credit.F10%>个月
				<%}else{%>
				<%=credit.F20%>天
				<%}%>
				</div>
				<div class="gray9">借款期限</div>
			</td>
			<td width="18%">
				<div class="f20 gray3">
					<%if(credit.F06.doubleValue()>=10000){
						String amount = Formater.formatAmount(credit.F06.doubleValue()/10000);
					%>
					<%=amount%>万元
					<%}else{
						String amount = Formater.formatAmount(credit.F06.doubleValue());
					%>
					<%=amount%>元
					<%}%>
				</div>
				<div class="gray9">借款金额</div>
			</td>
			<td width="18%">

				<%if(credit.F11 == T6230_F20.YFB){%>
				<p class="orange mt10" id="time"><%=DateTimeParser.format(credit.F13,"yyyy-MM-dd HH:mm")%> 即将开始</p>
				<%}else{%>
				<div class="progress-bar">
					<p class="p-bg"><span class="load-bg" style="width:<%=(int)(credit.proess*100)%>%"></span></p>
					<span class="num"><%=Formater.formatProgress(credit.proess)%></span>
				</div>
				<%}%>
			</td>
			<td width="15%">
				<%if(credit.F11 == T6230_F20.YFB){%>
				<a href="<%=controller.getPagingItemURI(request, com.dimeng.p2p.front.servlets.financing.sbtz.Index.class,credit.F02)%>" class="btn-border-gray">预发布</a>
				<%}else{
					if(credit.F11 == T6230_F20.TBZ){
						if(dimengSession!=null && dimengSession.isAuthenticated()){
							UserInfoManage uManage = serviceSession.getService(UserInfoManage.class);
							T6110 t6110 =uManage.getUserInfo(serviceSession.getSession().getAccountId());
							if(T6110_F07.HMD == t6110.F07){%>
				<a href="javascript:void(0)" class="btn-border-gray"><%=credit.F11.getChineseName()%></a>
				<%}else {%>
				<a href="<%=controller.getPagingItemURI(request, com.dimeng.p2p.front.servlets.financing.sbtz.Index.class,credit.F02)%>" class="btn-border-blue">立即投资</a>
				<%}%>
				<%}else{%>
				<a href="<%=controller.getPagingItemURI(request, com.dimeng.p2p.front.servlets.financing.sbtz.Index.class,credit.F02)%>" class="btn-border-blue">立即投资</a>
				<%}%>
				<%}else if(credit.F11 == T6230_F20.DFK){%>
				<a href="javascript:void(0)" class="btn-border-gray"><%=credit.F11.getChineseName()%></a>
				<%}else{%>
				<a href="javascript:void(0)" class="btn-border-gray"><%=credit.F11.getChineseName()%></a>
				<%}%>
				<%}%>
			</td>
			<!--预发标
             <td width="18%">2016-08-01 12:00 <br>即将开始</td>
            <td width="15%"><a class="btn-border-gray">预发标</a></td>
            -->
		</tr>
	</table>
</div> --%>
<!--recommend end-->

<script type="text/javascript">
	var endTime =<%=credit.F13.getTime()-timemp.getTime()%>;
	var clearTime = null;
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
		//$("#time").html(hour+"时"+minute+"分"+second+"秒 开始发售");
		if (leftsecond <= 0) {
			clearInterval(clearTime);
			location.reload();
		}
	}
<%
 	long leftTime = credit.F13.getTime()-timemp.getTime();
 	long day = leftTime/(1000*3600*24);//天数，少于一天的时候才倒计时
	if((credit.F11==T6230_F20.YFB && day <= 0 && leftTime>0)){%>
	    //去掉此倒计时操作，只在详情页倒计时
		/*clearTime = setInterval(function() {
			endTime = endTime - 1000;
			time();
		}, 1000);*/
<%}else{%>
	clearInterval(clearTime);
<%}%>

</script>
<%
    break;
                    }
                }
            }
        }
%>