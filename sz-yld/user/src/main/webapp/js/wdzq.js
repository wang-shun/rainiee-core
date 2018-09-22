$(function(){
	$("#wdzq1").mouseover(function(){
		$("#wdzqyz1").show();
	});
	$("#wdzq1").mouseout(function(){
		$("#wdzqyz1").hide();
	});
	$("#wdzq2").mouseover(function(){
		$("#wdzqyz2").show();
	});
	$("#wdzq2").mouseout(function(){
		$("#wdzqyz2").hide();
	});
});

var currentPage = 1;
var pageSize = 10;
var pageCount = 1;

var tabNum = 1;

function initPage(type){
	currentPage = 1;
	if(type=='yjq'){
		yjqdzq();
	}else if(type=='tbz'){
		tbzdzq();
	}else{
		hszdzq();
	}
}

//1.回收中的债权
function hszdzq(){
	tabNum = 1;
	//发送请求,生成数据,再将数据填充
	$("#liId1").attr("class","hover");
	$("#liId1").html("回款中<i></i>");
	$("#liId2").removeClass();
	$("#liId3").removeClass();
	$("#hzcxId").show();
	var ajaxUrl = $("#hszdzqUrl").val();

	var dataParam = {"currentPage" : currentPage, "pageSize" : pageSize};
	$.ajax({
		type:"post",
		dataType:"json",
		url:ajaxUrl,
		data:dataParam,
		async: false,
		success:function(returnData){
			
			if(returnData.msg != "undefined" && returnData.msg !=null && returnData.msg != ""){
				$(".popup_bg").show();
        		$("#info").html(showSuccInfo(returnData.msg,"error",loginUrl));
        		return;
			}
			
			//移除table中的tr
			$("#dataTable tr").empty();
			//填充数据,li样式需要改变
			var trHTML = "<tr class='til'><td>序号</td><td>债权ID</td>	<td>原始投资金额(元)</td><td>年化利率(%)</td><td>待收本息(元)</td><td>期数</td><td>下个还款日</td><td>状态</td><td>操作</td></tr>";
			$("#dataTable").append(trHTML);//在table最后面添加一行

			if (null == returnData || null == returnData.hszdzqList || returnData.hszdzqList.length == 0 ) {
				$("#dataTable").append("<tr><td align='center' colspan='9'>暂无数据</td></tr>");
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

			if(returnData.hszdzqList.length > 0){
				var rAssests = returnData.hszdzqList;
				$.each(rAssests, function (n, value) {
					//1.投资地址
					var url1 = $("#financingUrl").val();
					var url2 = $("#viesSuffixUrl").val();
					var hrefUrl = url1 + value.F02 + url2;
					//2.投资状态
					var status = "回收中";
					if(value.F28 != "HKZ"){
						status = getChineseName(value.F28);
					}
					//合同地址拼接
					var htmlHT = "";
					if(value.zqzrOrderId <= 0){
						var htUrl = $("#htIndexUrl").val();
						htmlHT += "<a target='_blank' href='"+ htUrl +"?id="+value.F02+"' class='blue'>合同</a>";
					}						
					trHTML += "<tr><td>"+(n+1)+"</td><td><a class='blue' href='"+hrefUrl+"'>"+value.F01+"</a></td>" +
						"<td>"+ fmoney(value.F04,2) + "</td>";
					trHTML += "<td><div class='pr'>"+ fmoney(value.F14*100,2);
					if(value.jxl != 0 && value.zqzrOrderId <= 0){
						trHTML += "<span class='red'>+"+fmoney(value.jxl,2)+"%</span>" +
						"<a href='javascript:void(0)' onmousemove='showHint("+n+")' onmouseout='hideHint("+n+")' class='tx_an1' style='float:none; display:inline-block; background-position:0 -575px; margin-left:5px;'></a>" +
						"<div id='hszdzq_ico_"+n+"' class='pop-con' style='margin-left:170px; top:4px;display: none;'>" +
						"<div class='fl pop-pic'></div>" +
						"<div class='pop-info'>加息"+fmoney(value.jxl,2)+"%返利</div></div>";
					}
					trHTML += "</div></td>"+
						"<td>"+ fmoney(value.dsbx,2) + "</td>" +
						"<td>"+ value.syqs + "/" + value.hkqs +"</td>" +
						"<td>"+ value.xghkr + "</td>" +
						"<td>"+ status + "</td>" +
						"<td>"+ htmlHT + "</td>"+
						"</tr>";
					$("#dataTable").append(trHTML);//在table最后面添加一行
					trHTML = "";
				});
			}
		},
		error: function(XMLHttpRequest, textStatus, errorThrown){
			if(XMLHttpRequest.responseText.indexOf('<html')>-1){
        		$(".popup_bg").show();
        		$("#info").html(showSuccInfo("页面已过期，请重新登录!","error",loginUrl));
        	}else if(XMLHttpRequest.responseText != "") {
        		$(".popup_bg").show();
        		$("#info").html(showSuccInfo(XMLHttpRequest.responseText,"error",loginUrl));
        	}else{
        		$(".popup_bg").show();
        		$("#info").html(showSuccInfo("系统繁忙，请稍候重试","error",loginUrl));
        	}
        		
		}
	});
}

//2.已结清的债权
function yjqdzq(){
	tabNum = 2;
	$("#liId2").attr("class","hover");
	$("#liId2").html("已结清<i></i>");
	$("#liId1").removeClass();
	$("#liId3").removeClass();
	$("#hzcxId").hide();

	//发送请求,生成数据,再将数据填充
	var ajaxUrl = $("#yjqdzqUrl").val();
	var dataParam = {"currentPage" : currentPage, "pageSize" : pageSize};
	var isBadClaimTransfer = $("#isBadClaimTransfer").val();
	$.ajax({
		type:"post",
		dataType:"json",
		url:ajaxUrl,
		data:dataParam,
		success:function(returnData){
			
			if(returnData.msg != "undefined" && returnData.msg !=null && returnData.msg != ""){
				$(".popup_bg").show();
        		$("#info").html(showSuccInfo(returnData.msg,"error",loginUrl));
        		return;
			}
			
			//移除table中的tr
			$("#dataTable tr").empty();
			//填充数据
			var trHTML = "<tr class='til'><td>序号</td><td>债权ID</td><td>投资金额(元)</td><td>年化利率(%)</td><td>已赚金额(元)</td><td>结清日期</td><td>结清方式</td><td>操作</td></tr>";
			$("#dataTable").append(trHTML);//在table最后面添加一行
			if (null == returnData || null == returnData.yjqdzqList || returnData.yjqdzqList.length == 0 ) {
				$("#dataTable").append("<tr><td align='center' colspan='8'>暂无数据</td></tr>");
				$("#pageContent").html("");
				return;
			}
			trHTML = "";

			$("#pageContent").html(returnData.pageStr);
			pageCount = returnData.pageCount;
			//分页点击事件
			$("a.page-link").click(function(){
				pageParam(this);
			});
			if(returnData.yjqdzqList.length > 0){
				var rAssests = returnData.yjqdzqList;
				$.each(rAssests, function (n, value) {
					//1.投资地址
					var url1 = $("#financingUrl").val();
					var url2 = $("#viesSuffixUrl").val();
					var hrefUrl = url1 + value.F02 + url2;
					//合同地址拼接
					var htmlHT = "";
					if(value.zqzrOrderId <= 0){
						var htUrl = $("#htIndexUrl").val();
						htmlHT += "<a target='_blank' href='"+ htUrl +"?id="+value.F02+"' class='blue mr10'>合同</a>";
					}
					if(value.blzqzrId > 0 && isBadClaimTransfer){
						var userBlzqzrUrl = $("#userBlzqzrUrl").val();
						htmlHT += "<a target='_blank' href='"+userBlzqzrUrl+"?id="+value.blzqzrId+"&zqId="+value.zqid+"' class='blue'>转让合同</a>";
					}
					var status = getChineseName(value.F28);
					var myTime=getTime(value.jqsj);
					trHTML += "<tr><td>"+(n+1)+"</td><td><a class='blue' href='"+hrefUrl+"'>"+value.F01+"</a></td>" +
						"<td>"+ fmoney(value.F04,2) + "</td>"+
						"<td><div class='pr'>"+ fmoney(value.F14*100,2);
					if(value.jxl != 0){
						trHTML += "<span class='red'>+"+fmoney(value.jxl,2)+"%</span>" +
						"<a href='javascript:void(0)' onmousemove='showHint("+n+")' onmouseout='hideHint("+n+")' class='tx_an1' style='float:none; display:inline-block; background-position:0 -575px; margin-left:5px;'></a>" +
						"<div id='hszdzq_ico_"+n+"' class='pop-con' style='margin-left:170px; top:4px;display: none;'>" +
						"<div class='fl pop-pic'></div>" +
						"<div class='pop-info'>加息"+fmoney(value.jxl,2)+"%返利</div></div>";
					}
					trHTML += "</div></td>" +
						"<td>"+ fmoney(value.yzje,2) + "</td>" +
						"<td>"+ myTime +"</td>" +
						"<td>"+ status + "</td>" +
						"<td>"+ htmlHT + "</td>"+
						"</tr>";
					$("#dataTable").append(trHTML);//在table最后面添加一行
					trHTML = "";
				});
			}
		},
		error: function(XMLHttpRequest, textStatus, errorThrown){
			if(XMLHttpRequest.responseText.indexOf('<html')>-1){
        		$(".popup_bg").show();
        		$("#info").html(showSuccInfo("页面已过期，请重新登录!","error",loginUrl));
        	}else if(XMLHttpRequest.responseText != "") {
        		$(".popup_bg").show();
        		$("#info").html(showSuccInfo(XMLHttpRequest.responseText,"error",loginUrl));
        	}else{
        		$(".popup_bg").show();
        		$("#info").html(showSuccInfo("系统繁忙，请稍候重试","error",loginUrl));
        	}
		}
	});
}

//3.投资中的债权
function tbzdzq(){
	tabNum = 3;
	$("#liId3").attr("class","hover");
	$("#liId3").html("投资中<i></i>");
	$("#liId1").removeClass();
	$("#liId2").removeClass();
	$("#hzcxId").hide();

	//发送请求,生成数据,再将数据填充
	var ajaxUrl = $("#tbzdzqUrl").val();
	var dataParam = {"currentPage" : currentPage, "pageSize" : pageSize};
	$.ajax({
		type:"post",
		dataType:"json",
		url:ajaxUrl,
		data:dataParam,
		success:function(returnData){
			
			if(returnData.msg != "undefined" && returnData.msg !=null && returnData.msg != ""){
				$(".popup_bg").show();
        		$("#info").html(showSuccInfo(returnData.msg,"error",loginUrl));
        		return;
			}
			
			//移除table中的tr
			$("#dataTable tr").empty();
			//填充数据
			var trHTML = "<tr class='til'><td>序号</td><td>标编号</td><td>原始投资金额(元)</td><td>年化利率(%)</td><td>期限</td><td>剩余时间</td><td>投资进度</td></tr>";
			$("#dataTable").append(trHTML);//在table最后面添加一行
			if (null == returnData || null == returnData.tbzdzqList || returnData.tbzdzqList.length == 0 ) {
				$("#dataTable").append("<tr><td align='center' colspan='7'>暂无数据</td></tr>");
				$("#pageContent").html("");
				return;
			}
			trHTML = "";
			$("#pageContent").html(returnData.pageStr);
			pageCount = returnData.pageCount;
			//分页点击事件
			$("a.page-link").click(function(){
				pageParam(this);
			});
			
			if(returnData.tbzdzqList.length > 0){
				var rAssests = returnData.tbzdzqList;
				$.each(rAssests, function (n, value) {
					//1.投资地址
					var url1 = $("#financingUrl").val();
					var url2 = $("#viesSuffixUrl").val();
					var hrefUrl = url1 + value.F15 + url2;
					//2.投资期限
					var dayOrMonth = value.F08 + "个月";
					//T6231_F21.S
					if (value.F21 == "S") {
						dayOrMonth = value.F22 + "天";
					}

					//3.投资进度
					//var processHtml = parseInt(((value.F04 - value.F06) / value.F04 )*100);
					var processHtml = getFormatProgress(((value.F04 - value.F06) / value.F04 ));
					trHTML += "<tr>" +
						"<td>"+ (n+1) + "</td>"+
						"<td><a class='blue' href='"+hrefUrl+"'>"+value.F16+"</a></td>" +
						"<td>"+ fmoney(value.F13,2) + "</td>" +
						"<td><div class='pr'>"+ fmoney(value.F05*100,2);
					if(value.jxl != 0){
						trHTML += "<span class='red'>+"+fmoney(value.jxl,2)+"%</span>" +
						"<a href='javascript:void(0)' onmousemove='showHint("+n+")' onmouseout='hideHint("+n+")' class='tx_an1' style='float:none; display:inline-block; background-position:0 -575px; margin-left:5px;'></a>" +
						"<div id='hszdzq_ico_"+n+"' class='pop-con' style='margin-left:175px; top:4px;display: none;'>" +
						"<div class='fl pop-pic'></div>" +
						"<div class='pop-info'>加息"+fmoney(value.jxl,2)+"%返利</div></div>";
					}
						
					trHTML += "</div></td>" +
						"<td>"+ dayOrMonth+"</td>" +
						"<td>"+ value.surTime + "</td>" +
						"<td>"+ processHtml + "%</td>"+
						"</tr>";
					$("#dataTable").append(trHTML);//在table最后面添加一行
					trHTML = "";
				});
			}
		},
		error: function(XMLHttpRequest, textStatus, errorThrown){
			if(XMLHttpRequest.responseText.indexOf('<html')>-1){
        		$(".popup_bg").show();
        		$("#info").html(showSuccInfo("页面已过期，请重新登录!","error",loginUrl));
        	}else if(XMLHttpRequest.responseText != "") {
        		$(".popup_bg").show();
        		$("#info").html(showSuccInfo(XMLHttpRequest.responseText,"error",loginUrl));
        	}else{
        		$(".popup_bg").show();
        		$("#info").html(showSuccInfo("系统繁忙，请稍候重试","error",loginUrl));
        	}
		}
	});
}

/**
 *分页查询请求
 * @param obj
 * @param liId
 * @returns {boolean}
 */
function pageParam(obj){
	if($(obj).hasClass("on")){
		return false;
	}
	$(obj).addClass("on");
	$(obj).siblings("a").removeClass("on");
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
		hszdzq();
	} else if (tabNum == 2) {
		yjqdzq();
	} else if (tabNum == 3) {
		tbzdzq();
	}
}

function getChineseName(status){
	var rtnChineseName;
	switch(status){
		case "HKZ":
			rtnChineseName = "回收中";
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
			rtnChineseName = "已结清";
			break;
		default:
			rtnChineseName = "回收中";
			break;
	}
	return rtnChineseName;
}

function showHint(n){
	$("#hszdzq_ico_"+n).show();
}

function hideHint(n){
	$("#hszdzq_ico_"+n).hide();
}

function getFormatProgress(number){
	var v = number * 100;
    if (v <= 0)
    {
        return "0";
    }
    if (v <= 1)
    {
        return "1";
    }
    return Math.floor(v);
}