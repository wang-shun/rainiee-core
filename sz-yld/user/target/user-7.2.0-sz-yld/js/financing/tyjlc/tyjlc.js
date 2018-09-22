var currentPage = 1;
var pageSize = 10;
var pageCount = 1;
$(function(){
	$(".Menubox li").click(function(){
		type = $(this).attr("id");
		initData(type,1);
	});
	initData(type,1);
});

function initHead(type){
	$("#dataHead").html("");
	var tr = "<tr class=\"til\">";
	if(type == "SQZ"){
		tr += "<td align='center'>序号</td><td align='center'>借款标题</td><td align='center'>体验金加入金额(元)</td>" +
			  "<td align='center'>年化利率(%)</td><td align='center'>体验金收益期</td>";
	}else if(type == "CYZ"){
		tr += "<td align='center'>序号</td><td align='center'>借款标题</td><td align='center'>体验金加入金额(元)</td>" +
				"<td align='center'>年化利率(%)</td><td align='center'>待赚金额(元)</td><td align='center'>已赚金额(元)</td>" +
				"<td align='center'>下个还款日</td>";
	}else if(type == "YJZ"){
		tr += "<td align='center'>序号</td><td align='center'>借款标题</td><td align='center'>体验金加入金额(元)</td>" +
				"<td align='center'>年化利率(%)</td><td align='center'>已赚金额(元)</td><td align='center'>结清时间</td>";
	}
	tr += "</tr>";
	$("#dataHead").append(tr);
}

function initBody(type,experenceList){
	if( experenceList != null && experenceList.length > 0 ){
		for(var i = 0; i < experenceList.length ; i ++){
			var item = experenceList[i];
			var tr = "<tr><td align='center'>"+(i+1)+"</td><td align='center' title=\""+item.F02+"\">"+subStringLength(item.F02,10,"...")+"</td>" +
					"<td align='center'>"+formatMoney(item.F03,1)+"</td><td align='center'>"+formatYearRate(item.F04)+"</td>";
			if(type == "SQZ"){
				var returnPeriod;
				if(item.F11 == "F"){
					if(item.F15 == "false"){
						returnPeriod = item.F10 > item.F13*30 ? item.F13*30+"天" :item.F10+"天";
					}else{
						returnPeriod = item.F10 > item.F13 ? item.F13+"个月" : item.F10+"个月";
					}
				}else{
					if(item.F15 == "false"){
						returnPeriod = item.F10 > item.F12 ? item.F12 +"天" : item.F10+"天";
					}else{
						returnPeriod = item.F10*30 > item.F12 ? item.F12+"天" :item.F10*30+"天";
					}
				}
				tr += "<td align='center'>"+returnPeriod+"</td>";
			}else if(type == "CYZ"){
				tr += "<td align='center'>"+formatMoney(item.F08)+"</td><td align='center'>"+formatMoney(item.F05)+"</td><td align='center'>"+formatDate(item.F09)+"</td>";
			}else if(type == "YJZ"){
				tr += "<td align='center'>"+formatMoney(item.F05)+"</td><td align='center'>"+formatDate(item.F06)+"</td>";
			}
			tr += "</tr>";
			$("#dataBody").append(tr);
		}
	}else{
		var tr = "";
		if(type == "SQZ"){
			tr = "<tr><td colspan=\"5\" align='center'>暂无数据</td><tr>";
		}else if(type == "CYZ"){
			tr = "<tr><td colspan=\"7\" align='center'>暂无数据</td><tr>";
		}else if(type == "YJZ"){
			tr = "<tr><td colspan=\"6\" align='center'>暂无数据</td><tr>";
		}
		$("#dataBody").append(tr);
	}
	
}

function initData(type,index){
	if(index == 1){
		currentPage = 1;
	}
	$(".Menubox").find("li").removeClass("hover");
	$("#"+type).addClass("hover");
	//type = type;
	$.post(url,{currentPage:currentPage,pageSize:pageSize,type:type},function(returnData){
		returnData = eval("("+returnData+")");
		pageCount = returnData.pageCount;
		$("#pageContent").html(returnData.pageStr);
		$("#dataBody").html("");
		initHead(type);
		var experenceList = eval(returnData.experienceList);
		initBody(type,experenceList);
		$("a.page-link").click(function(){
    		pageParam(this);
    	});
	});
}

function pageParam(obj,status){
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
	var wdtyjPage = $(obj).parent().parent().hasClass("wdtyjPage");
	if(wdtyjPage){
		ajaxSubmit(status);
	}else{
		initData(type,2);
	}
	
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
function formatMoney(s,flg) {
	if(flg == 1){
		if(s >= 10000){
			s = s / 10000;
			if(s.toString().indexOf(".") < 0){
				return s.toString() + ".0";
			}
			return s;
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

function isEmpty(str){
	if(str == null || str == ""){
		return true;
	}else{
		return false;
	}
}
function formatYearRate(yearRate){
	if(isEmpty(yearRate)){
		return;
	}
	return parseFloat(yearRate * 100).toFixed(2);
}

var formatDate = function (time) { 
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
    return y + '-' + m + '-' + d;  
}