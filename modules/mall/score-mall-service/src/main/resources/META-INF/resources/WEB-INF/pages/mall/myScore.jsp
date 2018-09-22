<%@page import="com.dimeng.p2p.user.servlets.mall.MyExchange"%>
<%@page import="com.dimeng.p2p.repeater.score.SetScoreManage" %>
<%@page import="com.dimeng.p2p.S63.entities.T6354" %>
<%@ page import="com.dimeng.p2p.user.servlets.mall.MyScore" %>
<%@include file="/WEB-INF/include/authenticatedSession.jsp" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>我的积分-<%
        configureProvider.format(out, SystemVariable.SITE_NAME);
    %></title>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
</head>
<%
    CURRENT_CATEGORY = "WDSC";
    CURRENT_SUB_CATEGORY = "WDJF";
%>
<body>
<%@include file="/WEB-INF/include/header.jsp" %>
    <div class="main_bg clearfix">
        <div class="user_wrap w1002 clearfix">
            <%@include file="/WEB-INF/include/menu.jsp" %>
            <!--右边内容-->
        <div class="r_main">
        	<div class="user_mod">
            	<div class="user_til clearfix"><i class="icon"></i><span class="gray3 f18">我的积分</span></div>
				<div class="amount_list clearfix mt30 mb10">
                	<ul id="scoreRecordId">
                    	<li>
                            总积分
                            <p class="f22 orange">0</p>
                        </li>
                        <li>
                            可用积分
                            <p class="f22 orange">0</p>
                        </li>
                        <li>
                            已用积分
                            <p class="f22 orange">0</p>
                        </li>
                    </ul>
                </div>                
            </div>
            <div class="user_mod border_t15">
            	<div id="divAppendId" class="user_tab clearfix">
                    <ul>  
                        <li id="getId" onclick="scoreGetRecord(1);"  class="hover">积分获取记录<i></i></li>
                        <li id="exchangeId" onclick="scoreExchangeRecord(1);" >积分兑换记录<i></i></li>
                    </ul>        
                </div>
            	<div id="scoreGetListId" class="user_table pt5">
                    <table id="scoreGetTableId" width="100%" border="0" cellspacing="0" cellpadding="0">
                      <tr class="til">
                        <td align="center">序号</td>
                        <td align="center">获取时间</td>
                        <td align="center">获取积分</td>
                        <td align="center">类型</td>
                      </tr>
                    </table>
                </div>
                <div class="page" id="pageContent"></div>
            </div>
            
            <div class="user_mod border_t15">
            	<div class="user_tab clearfix">
                    <ul>                      
                        <li id="two1" onclick="setTab('two',1,2)"  class="hover">积分说明<i></i></li>
                        <li id="two2" onclick="setTab('two',2,2)" >积分规则<i></i></li>
                    </ul>        
                </div>
               <%
                     SetScoreManage manage = serviceSession.getService(SetScoreManage.class);
                     T6354 t6354 = manage.getT6354();
                     if(null != t6354){
                %>
                <div id="con_two_1" class="lh26" style="overflow-x: auto;">
                    <%StringHelper.format(out, t6354.F02, fileStore);%>
                </div>
       	    	<div id="con_two_2" class="lh26" style="display:none;overflow-x: auto;">
                    <%StringHelper.format(out, t6354.F03, fileStore);%>
                </div>
            	<% }else{%>
            	<div class="lh26 tc">暂无数据</div>
            	<% }%>
            </div>
            
        </div>        
        <!--右边内容-->
        </div>
    </div>
<div id="info"></div>
<div class="popup_bg" style="display:none;"></div>
<input type="hidden" id="ajaxScoreUrl" value="<%=controller.getURI(request, MyScore.class)%>"/>
<input type="hidden" id="ajaxExchangeUrl" value="<%=controller.getURI(request, MyExchange.class)%>"/>
<%@include file="/WEB-INF/include/footer.jsp" %>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/dialog.js"></script>
<script type="text/javascript">
	var currentPage = 1;
	var pageSize = 5;
	var pageCount = 1;
	var tabNum = 1;
	var loginUrl="<%=controller.getURI(request,Login.class)%>";
	
	$(function () {
		scoreGetRecord();
	});
	/* 积分获取记录 */
	function scoreGetRecord(index){
		
		if(index==1){
			currentPage = 1;
		}
		tabNum = 1;
		$("#getId").attr("class","hover");
		$("#exchangeId").removeClass();
        
        var ajaxScoreUrl = $("#ajaxScoreUrl").val();
        var dataParam = {"currentPage": currentPage, "pageSize": pageSize};

        $.ajax({
            type: "post",
            dataType: "json",
            url: ajaxScoreUrl,
            data: dataParam,
            async: false,
            success: function (returnData) {
            	//移除table中的tr
        		$("#scoreGetTableId tr").empty();
                //借款总金额
                var ulLiTxt = "<li>总积分<p class='orange f22'>" + returnData.scoreCount.SumScore + "</p></li>" +
                        "<li>可用积分<p class='orange f22'>" + returnData.scoreCount.SumUnUsedScore + "</p></li>" +
                        "<li>已用积分<p class='orange f22'>" + returnData.scoreCount.SumUsedScore + "</p></li>";
                $("#scoreRecordId").html("");
                $("#scoreRecordId").html(ulLiTxt);
                var trHTML = "<tr class='til'><td align='center'>序号</td><td align='center'>获取时间</td><td align='center'>获取积分</td><td align='center'>类型</td></tr>";
    			$("#scoreGetTableId").append(trHTML);//在table最后面添加一行
                if (null == returnData || null == returnData.t6106List || returnData.t6106List.length == 0) {
                	$("#scoreGetTableId").append("<tr><td align='center' colspan='8'>暂无数据</td></tr>");
    				$("#pageContent").html("");
    				return;
                }
                
                trHTML = "";
    			//分页信息
    			$("#pageContent").html(returnData.pageStr);
    			pageCount = returnData.pageCount;
    			//分页点击事件
    			$("a.page-link").click(function(){
    				pageParam(this);
    			});

                //填充表格数据
    			if(returnData.t6106List.length > 0){
    				var rAssests = returnData.t6106List;
    				$.each(rAssests, function (n, value) {
    					var status = getChineseName(value.F05);
    					var myTime=formatTime(value.F04);
    					
    					trHTML += "<tr>"+
    						"<td align='center'>"+ (n+1) + "</td>" +
    						"<td align='center'>"+ myTime + "</td>"+
    						"<td align='center'>"+ value.F03 +"</td>" +
    						"<td align='center'>"+ status + "</td>" +
    						"</tr>";
    					$("#scoreGetTableId").append(trHTML);//在table最后面添加一行
    					trHTML = "";
    				});
    			}
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
            	if(XMLHttpRequest.responseText.indexOf('<html')>-1){
            		$(".popup_bg").show();
            		$("#info").html(showSuccInfo("页面已过期，请重新登录!","error",loginUrl));
            	}else{
            		alert(textStatus);
            	}
            }
        });
	}
	
	/* 积分兑换记录 */
	function scoreExchangeRecord(index){
		
		if(index==1){
			currentPage = 1;
		}
		tabNum = 2;
		$("#exchangeId").attr("class","hover");
		$("#getId").removeClass();
        
        var ajaxExchangeUrl = $("#ajaxExchangeUrl").val();
        var dataParam = {"currentPage": currentPage, "pageSize": pageSize};

        $.ajax({
            type: "post",
            dataType: "json",
            url: ajaxExchangeUrl,
            data: dataParam,
            async: false,
            success: function (returnData) {
            	//移除table中的tr
        		$("#scoreGetTableId tr").empty();
                //借款总金额
                var ulLiTxt = "<li>总积分<p class='orange f22'>" + returnData.scoreCount.SumScore + "</p></li>" +
                        "<li>可用积分<p class='orange f22'>" + returnData.scoreCount.SumUnUsedScore + "</p></li>" +
                        "<li>已用积分<p class='orange f22'>" + returnData.scoreCount.SumUsedScore + "</p></li>";
                $("#scoreRecordId").html("");
                $("#scoreRecordId").html(ulLiTxt);
                var trHTML = "<tr class='til'><td align='center'>序号</td><td align='center'>商品名称</td><td align='center'>数量</td><td align='center'>所需积分</td><td align='center'>成交时间</td></tr>";
    			$("#scoreGetTableId").append(trHTML);//在table最后面添加一行
                if (null == returnData || null == returnData.scoreOrderInfoExtList || returnData.scoreOrderInfoExtList.length == 0) {
                	$("#scoreGetTableId").append("<tr><td align='center' colspan='8'>暂无数据</td></tr>");
    				$("#pageContent").html("");
    				return;
                }
                
                trHTML = "";
    			//分页信息
    			$("#pageContent").html(returnData.pageStr);
    			pageCount = returnData.pageCount;
    			//分页点击事件
    			$("a.page-link").click(function(){
    				pageParam(this);
    			});

                //填充表格数据
    			if(returnData.scoreOrderInfoExtList.length > 0){
    				var rAssests = returnData.scoreOrderInfoExtList;
    				$.each(rAssests, function (n, value) {
    					var myTime=formatTime(value.F04);
    					
    					trHTML += "<tr>"+
    						"<td align='center'>"+ (n+1) + "</td>" +
    						"<td align='center'>"+ value.comdName + "</td>"+
    						"<td align='center'>"+ value.comdNum + "</td>"+
    						"<td align='center'>"+ value.comdScore +"</td>" +
    						"<td align='center'>"+ myTime + "</td>" +
    						"</tr>";
    					$("#scoreGetTableId").append(trHTML);//在table最后面添加一行
    					trHTML = "";
    				});
    			}
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
            	if(XMLHttpRequest.responseText.indexOf('<html')>-1){
            		$(".popup_bg").show();
            		$("#info").html(showSuccInfo("页面已过期，请重新登录!","error",loginUrl));
            	}else{
            		alert(textStatus);
            	}
            }
        });
	}
	
	function getChineseName(status){
		var rtnChineseName = null;
		switch(status){
			case "register":
				rtnChineseName = "注册";
				break;
			case "sign":
				rtnChineseName = "签到";
				break;
			case "invite":
				rtnChineseName = "邀请";
				break;
			case "invest":
				rtnChineseName = "投资";
				break;
			case "cellphone":
				rtnChineseName = "手机认证";
				break;
			case "mailbox":
				rtnChineseName = "邮箱认证";
				break;
			case "realname":
				rtnChineseName = "实名认证";
				break;
			case "trusteeship":
				rtnChineseName = "开通第三方托管账户";
				break;
			case "charge":
				rtnChineseName = "充值";
				break;
			case "buygoods":
				rtnChineseName = "现金购买商品积分";
				break;
			case "nopassreturn":
				rtnChineseName = "审核不通过返还积分";
				break;
			default:
				break;
		}
		return rtnChineseName;
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
	}
	
	 /* 分页查询请求 */
	function pageParam(obj){
		if($(obj).hasClass("cur")){
			return false;
		}
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

		if (tabNum == 1) {
			scoreGetRecord();
		} else if (tabNum == 2) {
			scoreExchangeRecord();
		} 
	}
	
	function toAjaxPage(){
    	if($("#getId").hasClass("hover")){
    		scoreGetRecord();
    	}else if($("#exchangeId").hasClass("hover")){
    		scoreExchangeRecord();
    	}
    }
</script>
</body>
</html>