$(function(){
	/*$(".page a.page-link").click(function(){
		pageParam(this);
	});*/
	initData();
});
function initData(){
	$.post(url,{currentPage:currentPage,pageSize:pageSize},function(returnData){
		$("#dataBody").html("");
		$("#pageContent").html("");
		returnData = eval("("+returnData+")");
		pageCount = returnData.pageCount;
		var applyList = eval(returnData.applyList);
		if(applyList != null && applyList.length > 0){
			$("#pageContent").html(returnData.pageStr);
	    	for(var i = 0; i < applyList.length; i++){
	    		var item = applyList[i];
	    		var tr = "<tr><td align='center'>"+(i+1)+"</td><td align='center' title=\""+item.F03+"\">";
	    		if(item.F20 == "TBZ" || item.F20 == "YFB" || item.F20 == "DFK" || item.F20 == "HKZ" || item.F20 == "YJQ" || item.F20 == "YDF"){
	    			tr += "<a class=\"highlight\" href=\""+bdxqUrl+item.F01+viewSuffix+"\">";
	    			tr += subStringLength(item.F03,10,"...");
	    			tr += "</a></td>";
	    		}else{
	    			tr += subStringLength(item.F03,10,"...")+"</td>";
	    		}
	    		tr += "<td align='center'>"+formatMoney(item.F05)+"</td>";
	    		tr += "<td align='center'>"+formatYearRate(item.F06)+"</td>";
	    		tr += "<td align='center'>"+(item.F28.F21 == "F" ? (item.F09+"个月"):(item.F28.F22+"天"))+"</td>";
	    		tr += "<td align='center'>"+(item.F20 == "YZF" ? "已删除" : getBidChineseName(item.F20))+"</td>";
	    		tr += "<td align='center'>";
	    		if(item.F20 == "TBZ" || item.F20 == "YFB" || item.F20 == "DFK" || item.F20 == "HKZ" || item.F20 == "YJQ" || item.F20 == "YDF"){
	    			tr += "<a href=\"javascript:void(0)\" onclick=\"view('"+item.F01+"','"+bdxqUrl+item.F01+viewSuffix+"')\"  class=\"highlight\">";
	    			tr += "查看";
	    			tr += "</a></td>"
	    		}else if(item.F20 == "DSH" || item.F20 == "SQZ"){
	    			tr += "<a class=\"highlight\" href=\"javascript:void(0);\" onclick=\"confirmMSg('"+item.F01+"','"+item.F03+"')\">";
	    			tr += "删除";
	    			tr += "</a></td>";
	    		}
	    		$("#dataBody").append(tr);
	    		
	    	}
		}else{
			var tr = "<tr><td align='center' colspan=\"7\">暂无数据</td></tr>";
			$("#dataBody").append(tr);
		}
    	
    	$("a.page-link").click(function(){
    		pageParam(this);
    	});
		
	});
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
	initData();
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
	   case "TBZ":
		   	 rtnChineseName = "投资中";
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
	   case "DFB":
			 rtnChineseName = "待发布";
		     break;
	   case "DSH":
		   	 rtnChineseName = "待审核";
		     break;
	   case "SQZ":
		   	 rtnChineseName = "申请中";
		     break;
	   case "YLB":
		   	 rtnChineseName = "已流标";
		     break;
	   case "YZF":
		     rtnChineseName = "已作废";
			 break;
	   default:
		   	 rtnChineseName = "";
		     break;
	   }
	return rtnChineseName;
}

var bid_tmp;
function confirmMSg(bid,title){

	$(".confirm_info").html("<p class=\"f20 gray33\" id=\"con_error\">您确定要删除“"+title+"”吗？</p>");
	$("#confirm").show();
	bid_tmp = bid;
}

function delBid(){
	$("#confirm").hide();
	$.post(bidzfUrl,{loanId:bid_tmp},function(data){
		if(data=="success"){
			$(".info").html("<p class=\"f20 gray33\" id=\"con_error\">删除成功！</p>");
			$("#tip_div").removeClass().addClass("successful");
			$("#dialog").show();
		}else{
			$(".info").html("<p class=\"f20 gray33\" id=\"con_error\">删除失败！</p>");
			$("#tip_div").removeClass().addClass("doubt");
			$("#dialog").show();
		}
	});
}