$(function(){
	$("a[id^='yearRate_'],a[id^='progress_'],a[id^='status_'],a[id^='bidType_'],a[id^='creditTerm_'],a[id^='sort_'],a[id^='product_']").click(function(){
		currentPage = 1;
		setPageStatus($(this));
	});

	$("a.page-link").click(function(){
		pageParam(this);
	});

	$("li.normalMenu").click(function(){
		setNormalMenu(this);
	});
});

function toAjaxPage(_obj){
	setParamToAjax(currentPage,_obj);
}

function setNormalMenu(obj){
	/*if($(obj).hasClass("hover")){
	 return;
	 }*/
	var a = $("<a href=\"javascript:void(0);\"></a>");
	var hoverLi = $("li.hover");
	hoverLi.attr("class","normalMenu");
	a.text(hoverLi.text());
	hoverLi.html(a);
	$(obj).attr("class","hover");
	$(obj).html($(obj).find("a").text());
	$(hoverLi).unbind("click");
	$(hoverLi).click(function(){
		setNormalMenu(this);
	});
	initPage();
	setParamToAjax(1);
	//if($("#product_1").attr("class")=="cur"){
		$("#bidTypeDl").remove();
	//}
}

function initPage(){
	currentPage = 1;
	preOrderItem = "";
	orderItemId = "";
	var bidType = $("#bidType_1").addClass("cur");
	$(bidType).siblings("a").removeClass("cur");
	var yearRate = $("#yearRate_1").addClass("cur");
	$(yearRate).siblings("a").removeClass("cur");
	var creditTerm = $("#creditTerm_1").addClass("cur");
	$(creditTerm).siblings("a").removeClass("cur");
	var progress = $("#progress_1").addClass("cur");
	$(progress).siblings("a").removeClass("cur");
	var status = $("#status_1").addClass("cur");
	$(status).siblings("a").removeClass("cur");
	var product = $("#product_1").addClass("cur");
	$(product).siblings("a").removeClass("cur");
	initTableTitle();
	var menuId = $("li.hover");
	if(menuId.val() == 1){
		$("#bidTypeDl").show();
		$("#creditTermDl").hide();
		$("#processDl").show();
		$("#bidStatusDl").show();
		$("#productDl").show();
		$("#tzlb_title").html("投资列表");
	}else if(menuId.val() == 2){
		$("#bidTypeDl").show();
		$("#creditTermDl").show();
		$("#processDl").hide();
		$("#bidStatusDl").show();
		$("#productDl").show();
	}else if(menuId.val() == 3){
		$("#bidTypeDl").show();
		$("#creditTermDl").show();
		$("#processDl").hide();
		$("#bidStatusDl").hide();
		$("#productDl").show();
		$("#tzlb_title").html("债权转让");
	}
}

function initTableTitle(){
	$("a[id^='sort_']").attr("class","sorting");
	$("#sort_1").attr("name","42");
	$("#sort_2").attr("name","12");
	$("#sort_3").attr("name","52");
	$("#sort_4").attr("name","62");
	$("#sort_5").attr("name","72");
	$("#sort_6").attr("name","42");
	$("#sort_7").attr("name","12");
	$("#sort_8").attr("name","52");
	$("#sort_9").attr("name","62");
	$("#sort_10").attr("name","72");
}

function setPageStatus(obj){
	//判断当前标签 是否已经选中，如果选中了，直接返回
	if($(obj).hasClass("sorting2")){
		$(obj).attr("class","sorting sorting3 cur");
	}else{
	
		//将当前标签设为选中状态
		$(obj).attr("class","sorting sorting2 cur");
	}
	
	
	//将其它标签设为  非选中状态
	if($(obj).attr("id").indexOf("sort_") >= 0){
		$("a[id^='sort_']").each(function(){
			if($(obj).attr("id") != $(this).attr("id")){
				$(this).attr("class","sorting");
			}
		});
		if(orderItemId == ""){
			orderItemId = $(obj).attr("id");
		}else{
			if($(obj).attr("id") != orderItemId && $("#"+orderItemId).attr("name").substring(1,2) == "1"){
				$("#" + orderItemId).attr("name",getOrderKey($("#" + orderItemId).attr("name")));
				orderItemId = $(obj).attr("id");
			}
		}
	}else{
		if($(obj).attr("id").indexOf("product_")>-1 && $(obj).attr("id")!="product_1"){
			$("#product_1").removeClass("cur");
			var id= $(obj).attr("name");
			$.ajax({
				type:"post",
				dataType:"json",
				url:_bdlxUrl,
				data:{"id":id},
				async: false,
				success:function(data) {
					var list = eval(data);
					$("#bidTypeDl").remove();
					var dlHtml="<dl id='bidTypeDl'><dt>标的类型：</dt><dd><a id='bidType_1' href='javascript:void(0);' name='' class='cur'>不限</a>";
					$.each(list, function (n, value) {
						dlHtml +="<a id='bidType_"+(n+2)+"' href='javascript:void(0);' name='"+value.F01+"'>"+value.F02+"</a>";
					});
					dlHtml +="</dd></dl>";
					$("#productDl").after(dlHtml);
				}
			});
			$("a[id^='bidType_']").click(function(){
				setPageStatus($(this));
			});
		}
		else if($(obj).attr("id")=="product_1"){
			$("#bidTypeDl").remove();
			$("#product_1").removeClass("cur");
			$("a[id^='product_']").removeClass("cur");
			$(obj).addClass("cur");
		}
		$(obj).siblings("a").removeClass("cur");
		$("a[id^='sort_']").attr("class","sorting");

	}
	//获取查询条件参数，调用ajax查询
	setParamToAjax(1,obj);
}

//设置标信息查询条件，并调用ajax查询结果
function setParamToAjax(currentPageP,obj){
	var pageId = $("li.hover").val();
	var bidType = $("a[id^='bidType_'].cur").attr("name");
	var yearRate = $("a[id^='yearRate_'].cur").attr("name");
	var creditTerm = $("a[id^='creditTerm_'].cur").attr("name");
	var progress = $("a[id^='progress_'].cur").attr("name");
	var status = $("a[id^='status_'].cur").attr("name");
	var orderItem = $("a[id^='sort_'].cur").attr("name");
	var productId = $("a[id^='product_'].cur").attr("name");
	if($(obj).hasClass("sorting2") || $(obj).hasClass("sorting3") ){
		preOrderItem = orderItem;
	}
	if($(obj).hasClass("page-link")){
		orderItem = preOrderItem;
	}
	var ajaxUrl;
	var data;
	if(pageId == 1){
		data = {"currentPage" : currentPageP,
			"pageSize" : pageSize,
			"bidType"  : bidType,
			"yearRate" : yearRate,
			"progress" : progress,
			"status" : status,
			"orderItem" : orderItem,
			"productId":productId
		};
		ajaxUrl = $("#enterpriseUrl").val();
	}else if(pageId == 2){
		data = {"currentPage" : currentPageP,
			"pageSize" : pageSize,
			"bidType"  : bidType,
			"yearRate" : yearRate,
			"creditTerm" : creditTerm,
			"status" : status,
			"orderItem" : orderItem,
			"productId":productId
		};
		ajaxUrl = $("#personalUrl").val();
	}else if(pageId == 3){
		data = {"currentPage" : currentPageP,
			"pageSize" : pageSize,
			"bidType"  : bidType,
			"yearRate" : yearRate,
			"creditTerm" : creditTerm,
			"orderItem" : orderItem,
			"productId":productId
		};
		ajaxUrl = $("#zqzrUrl").val();
	}
	postService(ajaxUrl,data,obj);

	//将非本列的值设置为初始值
	var curCol = $("a[id^='sort_'].cur").attr("id");
	var curName = $("a[id^='sort_'].cur").attr("name");

	if(curCol !== null && curCol !== undefined && curCol !== ''){
		//得到当前列的id序号
		var curColIdNum = curCol.substring(5,curCol.length);//5＝ "sort_"的长度
//		initTableTitle();
		$("#sort_1").attr("name","42");
		$("#sort_2").attr("name","12");
		$("#sort_3").attr("name","52");
		$("#sort_4").attr("name","62");
		$("#sort_5").attr("name","72");
		$("#sort_6").attr("name","42");
		$("#sort_7").attr("name","12");
		$("#sort_8").attr("name","52");
		$("#sort_9").attr("name","62");
		$("#sort_10").attr("name","72");

		var notCurrId = "#sort_1";
		for(var i = 1;i<=10;i++){
			notCurrId = "#sort_" + i;//
			if(curColIdNum == i){//给选中的列设置name
				clazz = $(notCurrId).attr("class");
				$(notCurrId).attr("name",curName);
				break;//找到后退出
			}
		}
	}

};

//调用ajax查询标信息
function postService(url,data,obj){
	$.ajax({
		type:"post",
		url:url,
		data:data,
		async: false ,
		dataType:"json",
		success: function(returnData){
			setCountInfo(returnData.total);
			pageCount = returnData.pageCount;
			var table = createTableHeader();
			if(returnData.bidList == null){
				setOrderItem(table,obj);
				$("#pageContent").html(returnData.pageStr);
				return;
			}
			var bidList = eval(returnData.bidList);
			for(var i = 0; i < bidList.length; i++){
				if((bidList[i].F11 == "YFB" || bidList[i].F11 == "TBZ") && bidList[i].F31 == 'APP' ){
					//发布项目投资端选择在APP端显示，那么此项目处于  “预发布”、“投资中”状态时，只在APP、微信端显示此项目，
					continue;
				}
				createTableBody(table,bidList[i]);
			}
			setOrderItem(table,obj);
			$("#pageContent").html(returnData.pageStr);
			$("a.page-link").click(function(){
				pageParam(this);
			});
		},
		error: function(XMLHttpRequest, textStatus, errorThrown){
			alert(textStatus);
		}
	});
}

function setOrderItem(table,obj){
	var orderItemId = $(obj).attr("id");
	var orderItemName = $(obj).attr("name");
	$("#bidInfo").html(table);
	if(typeof(orderItemId) != "undefined" && orderItemId.indexOf("sort_") >= 0){
		var orderItem = $("#" + orderItemId);
		orderItem.attr("name",getOrderKey(orderItemName));
		/*if(orderItem.hasClass("sorting3")){
			orderItem.attr("class","sorting sorting2 cur");
		}else{
			orderItem.attr("class","sorting sorting3 cur");
		}*/
	}
	$("a[id^='sort_']").click(function(){
		setPageStatus($(this));
	});
}

function setCountInfo(countInfo){
	var pageId = $("li.hover").val();
	if(pageId == 3){
		$("#transCountAmountSpan").html("累计成交总金额<br/>" + formatCountMoney(countInfo.totleMoney));
		$("#zqzrbsEm").text(countInfo.totleCount);
		$("#zqzrbsLi").show();
		$("#ljcjbsLi").hide();
		$("#yhzqLi").hide();
		$("#line").hide();
	}else{
		$("#transCountAmountSpan").html("累计成交总金额<br/>" + formatCountMoney(countInfo.totleMoney));
		$("#transCountEm").text(countInfo.totleCount);
		$("#earnCountSpan").html("为用户累计赚取<br/>" + formatCountMoney(countInfo.userEarnMoney));
		$("#zqzrbsLi").hide();
		$("#ljcjbsLi").show();
		$("#yhzqLi").show();
		$("#line").show();
	}
}

function formatCountMoney(amount){
	var s = "<span class='f36 gray3'>";
	if(amount == 0){
		return s + amount + ".00</span>元";
	}
	if(amount > 100000000){
		amount = amount * 10000/ 1000000000000;
		if(amount.toString().indexOf(".") < 0){
			s = s + formatMoney(amount,2) + "</span>亿元";
		}else{
			s = s + formatMoney(amount,2) + "</span>亿元";
		}
	}else if(amount >= 10000 && amount < 100000000){
		amount = amount * 10000 / 100000000;
		if(amount.toString().indexOf(".") < 0){
			s = s + formatMoney(amount,2) + "</span>万元";
		}else{
			s = s + formatMoney(amount,2) + "</span>万元";
		}
	}else{
		s = s + formatMoney(amount,2) + "</span>元";
	}
	return s;
}

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
	setParamToAjax(currentPage,obj);
}

function createTableHeader(){
	var table;
	var pageId = $("li.hover").val();
	var num = 0;
	var tr;
	var tableId;
	if(pageId == 3){
		table = $("<table width=\"100%\" cellspacing=\"0\" id=\"zqzrTableShow\"></table>");
		if(typeof($("#zqzrTableShow").attr("id")) == "undefined"){
			tableId = "#zqzrTable tr";
		}else{
			tableId = "#zqzrTableShow tr";
		}
	}else{
		table = $("<table width=\"100%\" cellspacing=\"0\" id=\"bidTable\"></table>");
		if(typeof($("#bidTable").attr("id")) == "undefined"){
			tableId = "#bidTableHdn tr";
		}else{
			tableId = "#bidTable tr";
		}
	}
	$(tableId).each(function (){
		if(num != 0){
			return;
		}
		tr = $(this).clone();
		num++;
	});
	table.append(tr);
	return table;
}

function createTableBody(table,bidInfo){
	var pageId = $("li.hover").val();
	if(pageId == 3){
		createZqzrTable(table,bidInfo);
	}else{
		createBidTable(table,bidInfo);
	}
}

function createBidTable(table,bidInfo){
	var tr = $("<tr></tr>");
	var branch = "";
	var xin = "";
	if(bidInfo.F16 == "S"){
		branch = "<span class='item_icon dan_icon'>保</span>";
	}else if(bidInfo.F17 == "S"){
		branch = "<span class='item_icon di_icon'>抵</span>";
	}else if(bidInfo.F18 == "S"){
		branch = "<span class='item_icon shi_icon'>实</span>";
	}else{
		branch = "<span class='item_icon xin_icon'>信</span>";
	}
	if(bidInfo.F28 == "S"){
		xin = "<span class='item_icon novice_icon'>新</span>";
	}
	if(bidInfo.F29 == "S"){
		xin = "<span class='item_icon reward_icon'>奖</span>";
	}
	var td = $("<td></td>");
	var div = $("<div class=\"title\"></div>");
	var span = $(branch);
	if(xin!=""){
		var xsb = $(xin);
		xsb.appendTo(div);
	}
	var a = $("<a></a>");
	// 借款标题
	
	span.appendTo(div);
	/*if(bidInfo.F28 == "S" || bidInfo.F29 == "S"){
	 xsb.appendTo(div);
	 }*/
	a.attr("href",$("#bdxqUrl").val() + bidInfo.F02 + ".html");
	a.attr("title",bidInfo.F04);
	a.html(subStringLength(bidInfo.F04,12));
	a.appendTo(div);
	div.appendTo(td);
	td.appendTo(tr);
	// 借款金额
	td = "<td>";
	if(bidInfo.F11 == "HKZ" || bidInfo.F11 == "YJQ" || bidInfo.F11 == "YDF" || bidInfo.F11 == "YZR"){
		bidInfo.F06 = bidInfo.F06 - bidInfo.F08;
		bidInfo.proess = 1;
		bidInfo.F08 = 0;
	}
	var amount = formatMoney(bidInfo.F06,1);
	if(bidInfo.F06 >= 10000){
		td += "<span class=\"f18 gray3\">" + amount + "</span>万元";
	}else{
		td += "<span class=\"f18 gray3\">" + amount + "</span>元";
	}
	td += "</td>";
	$(td).appendTo(tr);

	// 年化利率
	td = "<td>";
	td += "<span class=\"f18 gray3\">" + formatYearRate(bidInfo.F07) + "</span>%";
	td += "</td>";
	$(td).appendTo(tr);

	// 贷款期限
	td = "<td>";
	if(bidInfo.F19 == "S"){
		td += "<span class=\"f18 gray3\">" + bidInfo.F20 + "</span>天";
	}else{
		td += "<span class=\"f18 gray3\">" + bidInfo.F10 + "</span>个月";
	}
	td += "</td>";
	
	$(td).appendTo(tr);

	// 还需金额
	td ="<td>";
	var remainAmount = formatMoney(bidInfo.F08,1);
	if(bidInfo.F08 >= 10000){
		td += "<span class=\"f18 gray3\">" + remainAmount + "</span>万元";
	}else{
		td += "<span class=\"f18 gray3\">" + remainAmount + "</span>元";
	}
	td += "</td>";
	$(td).appendTo(tr);

	
	td = $("<td></td>");
	
	
	
	// 进度
	if(bidInfo.F11 != "YFB"){
		div = $("<div class=\"progress mt10\"><div>");
		span = $("<span class=\"progress_bg\"></span>");
		if(bidInfo.proess * 100 > 0 && bidInfo.proess * 100 < 1){
			bidInfo.proess = 0.01;
		}
		var strong = $("<span class='progress_bar' style='width:" + parseInt(bidInfo.proess * 100) + "%;'></span>");
		var em = $("<span class='cent'>" + parseInt(bidInfo.proess * 100) + "%</span>");
		strong.appendTo(span);
		span.appendTo(div);
		em.appendTo(div);
		div.appendTo(td);
	}else{
		var dateTime = new Date();
		dateTime.setTime(bidInfo.F13);
		span = $("<p class='orange mt10'>" + dateTime.Format("yyyy-MM-dd hh:mm") + "<br />即将开始</p>");
		span.appendTo(td);
	}
	td.appendTo(tr);
	
	td = $("<td></td>");
	// 借款状态
	a = $("<a></a>");
	if(bidInfo.F11 == "TBZ"){
		if(sessionFlg){
			if(hmdFlg || !isInvest){
				/*if(hmdFlg || !zrrFlg){*/
				a.attr("class","btn06 btn_gray btn_disabled");
				a.attr("href","javascript:void(0);");
				a.text("立即投资");
			}else{
				a.attr("class","btn06");
				a.attr("href",$("#bdxqUrl").val() + bidInfo.F02 + ".html");
				a.text("立即投资");
				
			}
		}else{
			a.attr("class","btn06");
			a.attr("href",$("#bdxqUrl").val() + bidInfo.F02 + ".html");
			a.text("立即投资");
		}
	}else{
		a.attr("class","btn06 btn_gray btn_disabled");
		a.attr("href","javascript:void(0);");
		a.text(getBidChineseName(bidInfo.F11));
	}
	a.appendTo(td);
	td.appendTo(tr);
	
	tr.appendTo(table);
}

function createZqzrTable(table,bidInfo){
	var tr = $("<tr></tr>");
	var branch = "";
	if(bidInfo.F19 == "S"){
		branch = "<span class='item_icon dan_icon'>保</span>";
	}else if(bidInfo.F20 == "S"){
		branch = "<span class='item_icon di_icon'>抵</span>";
	}else if(bidInfo.F21 == "S"){
		branch = "<span class='item_icon shi_icon'>实</span>";
	}else{
		branch = "<span class='item_icon xin_icon'>信</span>";
	}
	var td = $("<td></td>");
	var div = $("<div class=\"title\"></div>");
	var span = $(branch);
	var a = $("<a></a>");
	// 债权转让标题
	span.appendTo(div);
	a.attr("href",$("#zqxqUrl").val() + bidInfo.F01 + ".html");
	a.attr("title",bidInfo.F12);
	a.html(subStringLength(bidInfo.F12,12));
	a.appendTo(div);
	div.appendTo(td);
	td.appendTo(tr);

	// 年化利率
	td = "<td>";
	td += "<span class=\"f18 gray3\">" + formatYearRate(bidInfo.F14) + "</span>%";
	td += "</td>";
	
	$(td).appendTo(tr);

	// 剩余期限
	td = "<td>";
	td += "<span class=\"f18 gray3\">" + bidInfo.F23 + "</span>/" + bidInfo.F22 + "";
	td += "</td>";
	$(td).appendTo(tr);

	// 债权价值
	td = "<td>";
	td += "<span class=\"f18 gray3\">" + formatMoney(bidInfo.F03,2) + "</span>元";
	td += "</td>";
	$(td).appendTo(tr);

	// 待收本息
	td = "<td>";
	td += "<span class=\"f18 gray3\">" + formatMoney(bidInfo.dsbx,2) + "</span>元";
	td += "</td>";
	$(td).appendTo(tr);

	// 转让价格
	td = "<td>";
	td += "<span class=\"f18 gray3\">" + formatMoney(bidInfo.F02,2) + "</span>元";
	td += "</td>";
	$(td).appendTo(tr);

	// 购买
	td = $("<td></td>");
	if(bidInfo.F06 == "ZRZ"){
		//if(sessionFlg){
			if(hmdFlg || !isInvest){
				/*if(hmdFlg || !zrrFlg){*/
				span = $("<span></span>");
				span.attr("class","btn06 btn_gray btn_disabled");
				span.text("购买");
				span.appendTo(td);
			}else{
				a = $("<a></a>");
				a.attr("class","btn06");
				a.attr("href",$("#zqxqUrl").val() + bidInfo.F01 + ".html");
				/*$(a).click(function(){
					buy(bidInfo.F02,bidInfo.F03,bidInfo.F25,isInvestLimit,isRiskMatch(bidInfo.F30,userRiskLevel),isOpenRiskAccess);
				});*/
				a.text("购买");
				a.appendTo(td);
			}
		//}else{
		//	a = $("<a></a>");
		//	a.attr("class","btn06");
		//	a.attr("href",$("#loginUrl").val());
		//	a.text("购买");
		//	a.appendTo(td);
		//}
	}else{
		span = $("<span></span>");
		span.attr("class","btn06 btn_gray btn_disabled");
		span.text("已转让");
		span.appendTo(td);
	}
	
	td.appendTo(tr);
	tr.appendTo(table);
}

function subStringLength(str,maxLength,replace){
	if(isEmpty(str)){
		return;
	}
	if(typeof(replace) == "undefined" || isEmpty(replace)){
		replace = "...";
	}
	var rtnStr = "";
	var index = 0;
	var end = Math.min(str.length,maxLength);
	for(; index < end; ++index){
		rtnStr = rtnStr + str.charAt(index);
	}
	if(str.length > maxLength){
		rtnStr = rtnStr + replace;
	}
	return rtnStr;
}


function isEmpty(str){
	if(str == null || str == ""){
		return true;
	}else{
		return false;
	}
}

function isRiskMatch(productRiskLevel,userRiskLevel){
	if(productRiskLevel=='BSX'){
		return "true";
	}
	if(productRiskLevel=='JSX'&&userRiskLevel!='BSX'){
		return "true";
	}
	if(productRiskLevel=='WJX'&&(userRiskLevel!='BSX' && userRiskLevel!='JSX')){
		return "true";
	}
	if(productRiskLevel=='JQX'&&(userRiskLevel=='JQX'||userRiskLevel=='JJX')){
		return "true";
	}
	if(productRiskLevel=='JJX'&&userRiskLevel=='JJX'){
		return "true";
	}
	return "false";
}

function formatMoney(s,flg) {
	if(flg == 1){
		if(s >= 10000){
			s = s / 10000;
			if(s.toString().indexOf(".") < 0){
				return s.toString() + ".00";
			}
			if(s.toString().substring(s.toString().indexOf(".")+1,s.toString().length).length==1){
				return s.toString() + "0";
			}
			
			return s.toFixed(2);
		}
	}
    if (/[^0-9\.]/.test(s)){
    	return "0.00";
    }
    if (s == null || s == ""){
    	return "0.00";
    }
    s = s.toString().replace(/^(\d*)$/, "$1.");
    s = (s + "00").replace(/(\d*\.\d\d)\d*/, "$1");
    s = s.replace(".", ",");
    var re = /(\d)(\d{3},)/;  
    while (re.test(s)){
    	s = s.replace(re, "$1,$2");
    }
    s = s.replace(/,(\d\d)$/, ".$1");
    return s;  
}

function formatYearRate(yearRate){
	if(isEmpty(yearRate)){
		return;
	}
	return parseFloat(yearRate * 100).toFixed(2);
}

function getBidChineseName(status){
	var rtnChineseName;
	switch(status){
		case "HKZ":
			rtnChineseName = "还款中";
			break;
		case "YFB":
			rtnChineseName = "预发布";
			break;
		case "DFK":
			rtnChineseName = "待放款";
			break;
		case "YJQ":
			rtnChineseName = "已结清";
			break;
		case "YDF":
			rtnChineseName = "已垫付";
			break;
		case "YZR":
			rtnChineseName = "已转让";
			break;
		default:
			rtnChineseName = "";
			break;
	}
	return rtnChineseName;
}

function getOrderKey(key){
	var orderKey;
	switch(key){
		case "42":
			orderKey = "41";
			break;
		case "41":
			orderKey = "42";
			break;
		case "12":
			orderKey = "11";
			break;
		case "11":
			orderKey = "12";
			break;
		case "51":
			orderKey = "52";
			break;
		case "52":
			orderKey = "51";
			break;
		case "62":
			orderKey = "61";
			break;
		case "61":
			orderKey = "62";
			break;
		case "72":
			orderKey = "71";
			break;
		case "71":
			orderKey = "72";
			break;
		default:
			orderKey = "";
			break;
	}
	return orderKey;
}

Date.prototype.Format = function(fmt)
{
	var o = {
		"M+" : this.getMonth()+1,                 //月份
		"d+" : this.getDate(),                    //日
		"h+" : this.getHours(),                   //小时
		"m+" : this.getMinutes(),                 //分
		"s+" : this.getSeconds(),                 //秒
		"q+" : Math.floor((this.getMonth()+3)/3), //季度
		"S"  : this.getMilliseconds()             //毫秒
	};
	if(/(y+)/.test(fmt))
		fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));
	for(var k in o)
		if(new RegExp("("+ k +")").test(fmt))
			fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));
	return fmt;
}