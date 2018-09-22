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
<!--推荐标的-->
 <div class="invest_rec pt40 pb40">
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
	<div class="recommend_til"><img src="<%=controller.getStaticPath(request)+"/images/recommend.jpg"%>"></div>
    <div class="recommend_con">
    	<div class="top clearfix">
        	<div class="fl">
					<%if(credit.F28 == T6230_F28.S){ %>
					<span class="item_icon novice_xin_icon">新</span>
					<% } if(credit.F29 == T6231_F27.S){%>
					<span class="item_icon reward_jiang_icon">奖</span>
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
                <a href="<%=controller.getPagingItemURI(request, Index.class,credit.F02)%>"><%StringHelper.filterHTML(out, credit.F04);%></a>
            </div>
            
        </div>
        <div class="list">
        	<table width="100%" border="0" cellspacing="0" cellpadding="0">
              <tr class="til">
                <td width="15%">年化利率</td>
                <td width="15%">期限</td>
                <td width="24%">金额</td>
                <td width="30%">进度</td>
                <td width="16%">操作</td>
              </tr>
              <tr>
                <td class="noborder">
                	<%
                		String yearRate = Formater.formatRate(credit.F07,false);
                	%>
                	<span class="f18 gray3"><%=yearRate%></span><span class="f16">%</span>
                </td>
                <td class="noborder">
                     <%if(credit.F19 == T6231_F21.F){%>
                     <em class="f18 gray3"><%=credit.F10%></em> <em class="f16">个月</em>
                     <%}else{%> 
                     <em class="f18 gray3"><%=credit.F20%></em> <em class="f16">天</em> 
                     <%}%>
                </td>
                <td class="noborder">
                	<%if(credit.F06.doubleValue()>=10000){
                		String amount = Formater.formatAmount(credit.F06.doubleValue()/10000);
                	%>
						<span class="f18 gray3"><%=amount%></span><span class="f16">万元</span>
					<%}else{
					    String amount = Formater.formatAmount(credit.F06.doubleValue());
					%>
						<span class="f18 gray3"><%=amount%></span><span class="f16">元</span>
					<%}%>
                </td>
                <td class="noborder">
                <%if(credit.F11 == T6230_F20.YFB){%>
	              <p class="orange mt10" id="time"><%=DateTimeParser.format(credit.F13,"yyyy-MM-dd HH:mm")%> 即将开始</p>
                <%}else{%>
                  <div class="progress">
			        <span class="progress_bg"><span class="progress_bar" style="width:<%=(int)(credit.proess*100)%>%;"></span></span>
			        <span class="cent"><%=Formater.formatProgress(credit.proess)%></span>
			      </div>  
                <%}%>
                </td>
                <td class="noborder">
                	<%if(credit.F11 == T6230_F20.YFB){%>
	                	<a href="<%=controller.getPagingItemURI(request, com.dimeng.p2p.front.servlets.financing.sbtz.Index.class,credit.F02)%>" class="btn06 btn_gray btn_disabled">预发布</a>
                    <%}else{
				        if(credit.F11 == T6230_F20.TBZ){
		        			if(dimengSession!=null && dimengSession.isAuthenticated()){
    							UserInfoManage uManage = serviceSession.getService(UserInfoManage.class);
    							T6110 t6110 =uManage.getUserInfo(serviceSession.getSession().getAccountId());
    							if(T6110_F07.HMD == t6110.F07){%>
									<a href="javascript:void(0)" class="btn06 btn_gray btn_disabled"><%=credit.F11.getChineseName()%></a>
								<%}else {%>
									<a href="<%=controller.getPagingItemURI(request, com.dimeng.p2p.front.servlets.financing.sbtz.Index.class,credit.F02)%>" class="btn06">去投资</a>
								<%}%>
    						<%}else{%>
    							<a href="<%=controller.getPagingItemURI(request, com.dimeng.p2p.front.servlets.financing.sbtz.Index.class,credit.F02)%>" class="btn06">去投资</a>
							<%}%>
		        		<%}else if(credit.F11 == T6230_F20.DFK){%>
							<a href="javascript:void(0)" class="btn06 btn_gray btn_disabled"><%=credit.F11.getChineseName()%></a>
						<%}else{%>
							<a href="javascript:void(0)" class="btn06 btn_gray btn_disabled"><%=credit.F11.getChineseName()%></a>
						<%}%>
					<%}%>
                </td>
              </tr>
            </table>
        </div>
    </div>
</div>

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