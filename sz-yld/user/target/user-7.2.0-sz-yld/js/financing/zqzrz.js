$(function(){
	
	$("#ok").click(function(){
		var zcbId=$("#zcbId").val();
		var form = document.forms[0];
		form.action = url_del+"?zcbId="+zcbId;
		form.submit();
	});

});

function Cancel(zcbId){
	$("#zqCancelId").show();
	$("#zcbId").val(zcbId);
	$(".popup_bg").show();
}

var currentPage = 1;
var pageSize = 10;
var pageCount = 1;
//查询转让中的债权
function zqzrz(Id){
	//发送请求,生成数据,再将数据填充
	if (Id == 1) {//1表示点击页面查询,2表示从分页查询
		currentPage = 1;
	}
	$("#liId1").attr("class","hover");
	$("#liId2").removeClass();
	$("#liId3").removeClass();
	$("#liId4").removeClass();
	var ajaxUrl = zqzrz_url;
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
			var trHTML = "<tr class='til'><td align='center'>序号</td><td>债权ID</td><td>剩余期数</td><td>年化利率(%)</td><td>债权价值(元)</td><td>转让价格(元)</td><td>状态</td><td>&nbsp;</td></tr>";
			$("#dataTable").append(trHTML);//在table最后面添加一行
			trHTML = "";
			//分页信息
			$("#pageContent").html(returnData.pageStr);
			pageCount = returnData.pageCount;
			//分页点击事件
			$("a.page-link").click(function(){
				pageParam(this,1);
			});
			if (null == returnData || null == returnData.retList || returnData.retList.length == 0) {
				$("#pageContent").html("");
				$("#dataTable").append("<tr><td colspan='8' align=\"center\">暂无数据</td></tr>");
				return;
			}
			if(returnData.retList.length > 0){
				var rAssests = returnData.retList;
				$.each(rAssests, function (n, value) {
					var htmlHT = "<a onclick='Cancel("+value.F01+")' class='blue' style='cursor: pointer;' id='qxButton'>取消</a>";
					trHTML +="<tr>" +
					"<td align='center'>"+ (n+1)+"</td>" +
					"<td>"+ value.F09+"</td>" +
					"<td>"+ value.F21+"/"+value.F20+ "</td>"+
					"<td>"+ value.F23 + "</td>" +
					"<td>"+ fmoney(value.F14,2) + "元</td>" +
					"<td>"+ fmoney(value.F03,2) + "元</td>" +
					"<td>"+ value.F22 + "</td>" +
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

//查询可转出的债权
function kzczq(Id){
	//发送请求,生成数据,再将数据填充
	if (Id == 1) {//1表示点击页面查询,2表示从分页查询
		currentPage = 1;
	}
	$("#liId2").attr("class","hover");
	$("#liId1").removeClass();
	$("#liId3").removeClass();
	$("#liId4").removeClass();
	var ajaxUrl = zqkzc_url;
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
			//填充数据,li样式需要改变
			var trHTML = "<tr class='til'><td align='center'>序号</td><td>债权ID</td><td>剩余期数</td><td>下一还款日</td><td>年化利率(%)</td><td>待收本息(元)</td><td>债权价值(元)</td><td>&nbsp;</td></tr>";

			$("#dataTable").append(trHTML);//在table最后面添加一行
			trHTML = "";
			//分页信息
			$("#pageContent").html(returnData.pageStr);
			pageCount = returnData.pageCount;
			//分页点击事件
			$("a.page-link").click(function(){
				pageParam(this,2);
			});
			if (null == returnData || null == returnData.retList || returnData.retList.length == 0) {
				$("#pageContent").html("");
				$("#dataTable").append("<tr><td colspan='8' align=\"center\">暂无数据</td></tr>");
				return;
			}
			if(returnData.retList.length > 0){
				var rAssests = returnData.retList;
				$.each(rAssests, function (n, value) {
					var htmlHT = "<a class='blue' onclick='Transfer("+ value.F08 +","+ value.F03 +")' class='blue' style='cursor: pointer;' id='qxButton'>转让</a>";
					trHTML +="<tr>" +
					"<td align='center'>"+ (n+1)+"</td>" +
					"<td><a target='_blank' class='blue' href='"+sbtz_xq+value.F02+rewriter+"'>"+value.F01+"</a></td>" +
					"<td>"+ value.F05+"/"+value.F04+ "</td>"+
					"<td>"+ value.F11 + "</td>" +
					"<td>"+ value.F10 + "</td>" +
					"<td>"+ value.F09 + "元</td>" +
					"<td>"+ value.F12 + "元</td>" +
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

//查询已转出的债权
function yzczq(Id){
	if (Id == 1) {//1表示点击页面查询,2表示从分页查询
		currentPage = 1;
	}
	//发送请求,生成数据,再将数据填充
	$("#liId3").attr("class","hover");
	$("#liId1").removeClass();
	$("#liId2").removeClass();
	$("#liId4").removeClass();
	var ajaxUrl = zqyzc_url;
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

			var trHTML = "<tr class='til'><td align='center'>序号</td><td>债权ID</td><td>转让价格(元)</td><td>转出时债权价值(元)</td><td>转让费用(元)</td><td>盈亏(元)</td><td></td></tr>";

			$("#dataTable").append(trHTML);//在table最后面添加一行
			if(null == returnData || null == returnData.zqyzcList || null == returnData.zqyzcList.length){
				$("#pageContent").html("");
				$("#dataTable").append("<tr><td colspan='7' align=\"center\">暂无数据</td></tr>");
				return;
			}

			trHTML = "";
			//分页信息
			if (null != returnData.pageStr) {
				$("#pageContent").html(returnData.pageStr);
				pageCount = returnData.pageCount;
				//分页点击事件
				$("a.page-link").click(function(){
					pageParam(this,3);
				});
			}

			if(returnData.zqyzcList.length > 0){
				var rAssests = returnData.zqyzcList;
				$.each(rAssests, function (n, osf) {
					var htmlHT = "";
					htmlHT += "<tr><td align='center'>"+ (n+1)+"</td>";
					htmlHT += "<td>"+osf.zqNub+ "</td>";
					htmlHT += "<td>"+ fmoney(osf.F04,2)+ "元</td>";
					htmlHT += "<td>"+ fmoney(osf.F03,2)+ "元</td>";
					htmlHT += "<td>"+ fmoney(osf.F05,2)+ "元</td>";
					htmlHT += "<td>"+ fmoney(osf.F08,2)+ "元</td>";
					var htHtml = htUrl +"?id="+ osf.F01;
					htmlHT += " <td><a target='_blank' href='"+htHtml+"' class='blue'>合同</a></td></tr>";
					$("#dataTable").append(htmlHT);
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

//查询已转入的债权
function yzrzq(Id){
	if (Id == 1) {//1表示点击页面查询,2表示从分页查询
		currentPage = 1;
	}

	//发送请求,生成数据,再将数据填充
	$("#liId4").attr("class","hover");
	$("#liId1").removeClass();
	$("#liId2").removeClass();
	$("#liId3").removeClass();
	var ajaxUrl = zqyzr_url;
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

			var trHTML = "<tr class='til'><td align='center'>序号</td><td>剩余期数</td><td>年化利率(%)</td><td>转入时债权价值(元)</td><td>转让价格(元)</td><td>盈亏(元)</td><td>转入时间</td><td></td></tr>";
			$("#dataTable").append(trHTML);//在table最后面添加一行
			if(null == returnData || null == returnData.zqyzrList || null == returnData.zqyzrList.length){
				$("#pageContent").html("");
				$("#dataTable").append("<tr><td colspan='8' align=\"center\">暂无数据</td></tr>");
				return;
			}

			trHTML = "";
			//分页信息
			if (null != returnData.pageStr) {
				$("#pageContent").html(returnData.pageStr);
				pageCount = returnData.pageCount;
				//分页点击事件
				$("a.page-link").click(function(){
					pageParam(this,4);
				});
			}

			if(returnData.zqyzrList.length > 0){
				var rAssests = returnData.zqyzrList;
				$.each(rAssests, function (n, isfList) {
					trHTML = "";
					trHTML += "<tr><td align='center'>"+(n+1)+ "</td>";
					var syqs = "0/0";//剩余期数
					if(null != returnData.t6231List && returnData.t6231List.length > 0){
						var t6231List = returnData.t6231List;
						$.each(t6231List, function (k, valueT6231) {
							if (valueT6231.F01 == isfList.jkbId) {
								syqs = valueT6231.F03 + "/" + valueT6231.F02;
							}
						});
					}
					trHTML += "<td>"+syqs+ "</td>";

					var yearRate = 0.00;//年化利率
					if(null != returnData.bdxqList && returnData.bdxqList.length > 0){
						var bdxqList = returnData.bdxqList;
						$.each(bdxqList, function (m, valueBdxq) {
							if (valueBdxq.F01 == isfList.jkbId) {
								yearRate = fmoney(valueBdxq.F06 * 100, 2);
							}
						});
					}
					trHTML += "<td>"+yearRate+ "%</td>";
					trHTML += "<td>" + fmoney(isfList.F04, 2) + "元</td>";
					trHTML += "<td>" + fmoney(isfList.F05, 2) + "元</td>";
					trHTML += "<td>" + fmoney(isfList.F08, 2) + "元</td>";
					trHTML += "<td>" + getTime(isfList.F07, 2) + "</td>";

					var htZqzrUrl = htUrl + "?id=" + isfList.F02;
					trHTML += "<td>" + "<a target='_blank' href='" + htZqzrUrl + "' class='blue'>合同</a></td>";

					$("#dataTable").append(trHTML);
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
function pageParam(obj,liId){
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

	if (liId == 1) {
		zqzrz(2);//分页查询
	} else if (liId == 2) {
		kzczq(2);//分页查询
	} else if (liId == 3) {
		yzczq(2);//分页查询
	} else if (liId ==4) {
		yzrzq(2);//分页查询
	}
}

function Transfer(zqId,zqValue){
	$("#zrValue").removeClass();
	if(isException){
		showLogicExceptionInfo();
	}else{
		$("#zqId").val(zqId);
		$("#zqjz").html(zqValue);
		$("#zqValue").val(zqValue);
		var zqjz = $("#zqjz").text();//债权价值
		var zqzrsx = $("#zqzrsx").val();//转出价格上限
		var zqzrxx = $("#zqzrxx").val();//转出价格下限
		$("#zrValue").attr("placeholder",Math.ceil(zqjz*zqzrxx)+"~"+Math.ceil(zqjz*zqzrsx));
		var fwys = "zqzrbl-"+Math.ceil(zqjz*zqzrxx)+"-"+Math.ceil(zqjz*zqzrsx);//转出价格范围限制的样式
		$("#zrValue").addClass("text_style required isint "+fwys);
		$("#transferDivId").show();
		$(".popup_bg").show();
	}
}


//格式化年化利率两位小数点
function formatYearRate(yearRate){
	if(isEmpty(yearRate)){
		return;
	}
	return parseFloat(yearRate * 100).toFixed(2);
}

function isEmpty(str){
	if(str == null || str == ""){
		return true;
	}else{
		return false;
	}
}

function fmoney(s, n) {
    n = n > 0 && n <= 20 ? n : 2;
    var f = s < 0 ? "-" : ""; //判断是否为负数
    s = parseFloat((Math.abs(s) + "").replace(/[^\d\.-]/g, "")).toFixed(n) + "";//取绝对值处理, 更改这里n数也可确定要保留的小数位
    var l = s.split(".")[0].split("").reverse(),
        r = s.split(".")[1];
    var t = "";
    for(var i = 0; i < l.length; i++ ) {
        t += l[i] + ((i + 1) % 3 == 0 && (i + 1) != l.length ? "," : "");
    }
    return f + t.split("").reverse().join("") + "." + r.substring(0,2);//保留2位小数  如果要改动 把substring 最后一位数改动就可
}

function setDMKeyup(obj){
	$(obj).nextAll("p[errortip]").hide();
	$(obj).nextAll("p[tip]").html(chinaCost($(obj).val())).show();
}
