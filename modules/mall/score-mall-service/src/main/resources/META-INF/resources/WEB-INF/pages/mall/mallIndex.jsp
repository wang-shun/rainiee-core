<%@page import="java.util.List" %>
<%@page import="com.dimeng.p2p.S63.entities.T6350" %>
<%@page import="com.dimeng.p2p.S63.entities.T6353" %>
<%@page import="com.dimeng.p2p.front.servlets.mall.MallIndex" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>积分商城<%=configureProvider.getProperty(SystemVariable.SITE_TITLE)%></title>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
</head>
<%
CURRENT_CATEGORY = "PTSC";
CURRENT_SUB_CATEGORY = "PTSCSY";
//启用的商品类别
List<T6350> t6350List = ObjectHelper.convert(request.getAttribute("t6350List"), List.class);
//积分范围
List<T6353> scoreRangeList = ObjectHelper.convert(request.getAttribute("scoreRangeList"), List.class);
//金额范围
List<T6353> amountRangeList = ObjectHelper.convert(request.getAttribute("amountRangeList"), List.class);
//最近兑换 10条记录
List<String> newestMallOrderList = ObjectHelper.convert(request.getAttribute("newestMallOrderList"), List.class);
//支持余额购买常量
boolean allowsBalance = BooleanParser.parse(configureProvider.getProperty(MallVariavle.ALLOWS_THE_BALANCE_TO_BUY));

%>
<body>

<%@include file="/WEB-INF/include/header.jsp" %>
<!--主体内容-->
<div class="main_bg">
    <div class="mall_filter_mod">
        <div class="main_hd"><i class="icon"></i><span class="gray3 f18">积分商城</span></div>
        <div class="mall_filter">
            <dl>
                <dt>商品类别：</dt>
                <dd id="goods_category">
                  <a href="javascript:void(0);" id="0" class="cur">不限</a> 
                  <%
                    if(null != t6350List) {
                        for(T6350 t6350 : t6350List){
                  %>
                  <a href="javascript:void(0);" id="<%=t6350.F01%>"><%=t6350.F03%></a> 
                  <%}}%>
                </dd>
            </dl>
            <dl>
                <dt>积分范围：</dt>
                <dd id="score_range">
                  <a href="javascript:void(0);" id="0" class="cur">不限</a> 
                  <%
                    if(null != scoreRangeList) {
                        for(T6353 t6353 : scoreRangeList){
                  %>
                  <%if("2147483647".equals(t6353.F03)) {%>
                  <a href="javascript:void(0);" id="<%=t6353.F01%>"><%=t6353.F02%>以上</a> 
                  <%}else if("0".equals(t6353.F02)){%>
                  <a href="javascript:void(0);" id="<%=t6353.F01%>"><%=t6353.F03%>以下</a> 
                  <%}else{%>
                  <a href="javascript:void(0);" id="<%=t6353.F01%>"><%=t6353.F02%>-<%=t6353.F03%></a>
                  <%}}}%>
                </dd>
            </dl>
            <%if(allowsBalance){%>
            <dl>
                <dt>价格范围：</dt>
                <dd id="amount_range">
                  <a href="javascript:void(0);" id="0" class="cur">不限</a> 
                  <%
                    if(null != amountRangeList) {
                        for(T6353 t6353 : amountRangeList){
                  %>
                  <%if("2147483647".equals(t6353.F03)) {%>
                  <a href="javascript:void(0);" id="<%=t6353.F01%>"><%=t6353.F02%>以上</a> 
                  <%}else if("0".equals(t6353.F02)){%>
                  <a href="javascript:void(0);" id="<%=t6353.F01%>"><%=t6353.F03%>以下</a> 
                  <%}else{%>
                  <a href="javascript:void(0);" id="<%=t6353.F01%>"><%=t6353.F02%>-<%=t6353.F03%></a>
                  <%}}}%>
                </dd>
            </dl>
            <%}%>
            <dl>
                <dt>排序方式：</dt>
                <dd id="sort_way">
                  <a href="javascript:void(0);" id="sort_way_unlimited" class="cur">默认</a> 
                  <a href="javascript:void(0);" id="sort_way_newest" class="sorting order_by">最新</a> 
                  <a href="javascript:void(0);" id="sort_way_hot" class="sorting order_by">热门</a> 
                  <a href="javascript:void(0);" id="sort_way_score" class="sorting order_by">积分</a>
                <%if(allowsBalance){%>
                    <a href="javascript:void(0);" id="sort_way_amount" class="sorting order_by">价格</a>
                <%}%>
                </dd>
            </dl>
        </div>
	</div>
    <div class="main_mod main mod height">
        <div class="mall_notice clearfix w1220">
            <div class="til"><i></i>最近成交</div>
            <div class="bd">
                <ul>
                <%
                    if(null != newestMallOrderList) {
                        int size = newestMallOrderList.size();
                        for(int i=0;i<size;i++){
                            String firstResult = newestMallOrderList.get(i);
                            String secondResult = "";
                            if(++i < size){
                                secondResult = newestMallOrderList.get(i);
                            }
                %>
                    <li>
                      <span title="<%=firstResult %>"><%=firstResult %></span>
                      <span title="<%=secondResult %>"><%=secondResult %></span>
                    </li>	
                <%}}%>
                </ul>
            </div>
        </div>
        <!--商品列表-->   
        <div class="mall_list" id="goods_content"></div>
        <!--商品列表-->   
        <!--分页-->    
        <div class="page p15 paging" id="pageContent"></div>
        <!--分页  --END--> 
	</div>
</div>
<div id="info"></div>
<div class="popup_bg" style="display:none;"></div>
<!--主体内容-->
<%@include file="/WEB-INF/include/footer.jsp" %>
<%@include file="/WEB-INF/include/script.jsp" %>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/dialog.js"></script>
<%
    String message = controller.getPrompt(request, response, PromptLevel.WARRING);
    if (!StringHelper.isEmpty(message)) {
%>
		<script type="text/javascript">
		$(".popup_bg").show();
		$("#info").html(showDialogInfo('<%=message%>',"doubt"));
		</script>
<%
    }
%>
<script type="text/javascript">
// 最近成交
var n_h = $(".mall_notice .bd li").height();
var len = $(".mall_notice .bd li").length;
var i = 0;
var parent = $(".mall_notice .bd > ul");
if (len > 1) {
	function func() {
		if (i == len - 1) {
			parent.animate({ "margin-top": 0 }, 300);
			i = 0;
		} else {
			parent.animate({ "margin-top": "-=" + n_h }, 300);
			i++;
		}
	}
    var timer = setInterval(func, 3000);
    $(".mall_notice .bd").hover(function(){
        clearInterval(timer);
    },function(){
        timer = setInterval(func, 3000);
    });
}

</script>
<script type="text/javascript">

    var currentPage = 1;
    var pageSize = 12;
    var pageCount = 1;
    var prm = {"goodsCategory":0,"scoreRange":0,"amountRange":0,"sortWay":"sort_way_unlimited","orderBy":""};
    $(function(){
    	
    	getMallInfo(1);
        
        //平台商城列表的查询条件
        $("#goods_category").find("a").click(function(){
        	prm.goodsCategory = getValue("goods_category",this); //商品类别
        	getMallInfo(1);
        });
        $("#score_range").find("a").click(function(){
        	prm.scoreRange = getValue("score_range",this); //积分范围
        	getMallInfo(1);
        });
        $("#amount_range").find("a").click(function(){
        	prm.amountRange = getValue("amount_range",this); //价格范围
        	getMallInfo(1);
        });
        $("#sort_way").find("a").click(function(){
        	prm.sortWay = getValueSortWay(this); //排序方式
        	getMallInfo(1);
        });
    });
    function getValueSortWay(fun){
    	var id = $(fun).attr("id");
    	if('sort_way_unlimited'==id){
    		$(fun).attr("class","cur");
    		$(fun).siblings().attr("class","sorting order_by");
    	}else{
    		$("#sort_way_unlimited").removeClass("cur");
    		$(fun).siblings(".order_by").attr("class","sorting order_by");
    		if($(fun).hasClass('sorting3')){
    			$(fun).attr("class","sorting sorting2 order_by");
    			prm.orderBy = "ASC";
    		}else{
    			$(fun).attr("class","sorting sorting3 order_by");
    			prm.orderBy = "DESC";
    		}
    	}
    	return id;
    }
    function getValue(idName,fun){
    	$("#"+idName).children(".cur").removeClass("cur");
    	$(fun).addClass("cur");
    	return $(fun).attr("id");
    }
    
    function getMallInfo(index){
    	
    	prm.currentPage = index;
    	prm.pageSize = pageSize;
    	$.ajax({
    		type:"post",
    		dataType:"json",
    		url:"<%=controller.getURI(request, MallIndex.class)%>",
    		data:prm,
    		success:function(returnData){
    			var trHTML = "<ul class='clearfix'>";
    			//展示数据前先清空数据
    			$("#goods_content").html(trHTML);
    			//分页信息
    			if(returnData.t6351List!=null){
    				$("#pageContent").html(returnData.pageStr);
    			}else{
    				$("#pageContent").empty();
    			}
    			pageCount = returnData.pageCount;
    			//分页点击事件
    			$("a.page-link").click(function(){
    				pageParam(this,1);
    			});
    			if (null == returnData.t6351List) {
    				trHTML = "<div class='f16 tc pt100 pb100'>暂无数据</div>";
    				$("#goods_content").html(trHTML);
    				return;
                } else if(returnData.t6351List.length > 0){
                	var picUrl = "<%=controller.getStaticPath(request)%>/images/mall_list.jpg";
    				var rAssests = returnData.t6351List;
    				$.each(rAssests, function (n, obj) {
                        var paymentMethodStr = "";
                        if(('yes'==obj.F16)) {
                            paymentMethodStr = "<span class='mr20 ml15'>积分：<span class='f16 highlight'>" + obj.F05 + "</span></span>";
                        }
                        if(('yes'==obj.F17) && '<%=allowsBalance%>' == 'true') {
                            if(('yes'==obj.F16)) {
                                paymentMethodStr = paymentMethodStr+ "价格：<span class='f16 highlight'>￥" + obj.F15 + "</span>" ;
                            }else{
                                paymentMethodStr = "<span class='mr20 ml15'>价格：<span class='f16 highlight'>￥" + obj.F15 + "</span></span>" ;
                            }
                        }
    					trHTML += "<li>"+
    						      "<div class='pic'><a target=\"_blank\" href='/mall/ptscXq/"+obj.F01+".html'><img src='"+((obj.F08)?obj.F08:picUrl)+"'></a></div>"+
    						      "<h3 class='til'><a target=\"_blank\" href='/mall/ptscXq/"+obj.F01+".html' title='"+obj.F03+"'>"+obj.F03+"</a></h3>"+
    						      "<p class='gray9'>"+
                                   paymentMethodStr +
    						      "</p>"+
    						      "<p class='gray9 ml15 mt10'>"+(obj.F19 == 0?'':"<span class='scj'>市场参考价：￥"+obj.F19+"</span>")+"成交笔数: "+obj.F07+" 笔</p>"+
    						      "</li>";
    				});
    			}
    			trHTML += "</ul>";
    			$("#goods_content").html(trHTML);
    		},
    		error: function(XMLHttpRequest, textStatus, errorThrown){
    			$("#info").html(showDialogInfo("系统异常，请稍后重试！", "error"));
    		}
    	});
    }
    
    /**
     *分页查询请求
     * @param obj
     * @param liId
     * @returns {boolean}
     */
    function pageParam(obj, liId) {
        if ($(obj).hasClass("selected")) {
            return false;
        }
        $(obj).addClass("selected");
        $(obj).siblings("a").removeClass("selected");
        if ($(obj).hasClass("startPage")) {
            currentPage = 1;
        } else if ($(obj).hasClass("prev")) {
            currentPage = parseInt(currentPage) - 1;
        } else if ($(obj).hasClass("next")) {
            currentPage = parseInt(currentPage) + 1;
        } else if ($(obj).hasClass("endPage")) {
            currentPage = pageCount;
        } else {
            currentPage = parseInt($(obj).html());
        }
        getMallInfo(currentPage);
    }

    function pageSubmit(_obj) {
        currentPage = $(_obj).prev().val();
        var re = /^[1-9]+[0-9]*]*$/;
        if (!re.test(currentPage)) {
            return;
        }
        if (currentPage > $(_obj).prev().attr("maxSize") * 1) {
            currentPage = $(_obj).prev().attr("maxSize") * 1;
        }
        getMallInfo(currentPage);

    }
</script>
</body>
</html>