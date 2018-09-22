var pageType = "bdxq";
$(function(){
	initData("bdxq");
	$(".main_tab ul li").click(function(){
		currentPage = 1;
		initData($(this).attr("id"));
	});
	
	// 输入框提示文字
	$(".focus_input .focus_text").each(function(){
   var thisVal=$(this).val();
   //判断文本框的值是否为空，有值的情况就隐藏提示语，没有值就显示
   if(thisVal!=""){
	 $(this).siblings(".focus_input label").hide();
	}else{
	 $(this).siblings(".focus_input label").show();
	}
   //聚焦型输入框验证
   $(this).focus(function(){
	 $(this).siblings(".focus_input label").hide();
	}).blur(function(){
	  var val=$(this).val();
	  if(val!=""){
	   $(this).siblings(".focus_input label").hide();
	  }else{
	   $(this).siblings(".focus_input label").show();
	  }
	});
  });
	
});

function initData(type){
	$(".main_tab ul li").removeClass("hover");
	$("#"+type).addClass("hover");
	$("#dataHtml").addClass("details");
	$.post(_dataUrl,{type:type,id:_bid,pageSize:pageSize,currentPage:currentPage},function(returnData){
		returnData = eval("("+returnData+")");
		if(type == "bdxq"){
			pageType = "bdxq";
			initBdxqData(returnData);
		}else if (type == "fkxx"){
			pageType = "fkxx";
			initDbxxData(returnData);
		}else if (type == "dywxx"){
			pageType = "dywxx";
			initDyxxData(returnData);
		}else if (type == "xgwj"){
			pageType = "xgwj";
			initXgwjData(returnData);
		}else if (type == "hkjh"){
			pageType = "hkjh";
			initHkjhData(returnData);
		}else if (type == "tzjl"){
			pageType = "tzjl";
			initTzjlData(returnData);
		}else if (type == "byjdfwb"){
			pageType = "byjdfwb";
			initByjdfwbData(returnData);
		}
		else if (type == "xmdt"){
			pageType = "xmdt";
			initXmdtData(returnData);
		}
	});
}
//标的详情
function initBdxqData(returnData){
	$("#dataHtml").html("");
	var div = "";
	if(returnData!=null){
		returnData = returnData.bdxqData;
		var existByjdf = returnData.existByjdf;
		if(existByjdf == 'false'){
			$("#byjdfwb").hide();
		}
		if(!isZrr){
			if(returnData.enterprise!=null){
				div += "<div  class=\"main_hd02\">企业信息</div>";
				div += "<ul class=\"mt30 ml20 clearfix info_list\">";
				div += "<li>注册年限："+returnData.enterprise.F07+" 年</li>";
				div += "<li>注册资金："+formatMoney(returnData.enterprise.F08)+" 万元</li>";
				div += "<li>资产净值："+formatMoney(returnData.enterprise.F14)+" 万元</li>";
				div += "<li>上年度经营现金流入："+formatMoney(returnData.enterprise.F15)+" 万元</li>";
				div += "<li>行业："+(returnData.enterprise.F09 == null ? "" : returnData.enterprise.F09)+"</li>";
				div += "<li>经营情况："+(returnData.enterprise.F17 == null ? "" : returnData.enterprise.F17)+"</li>";
				div += "<li>涉诉情况："+(returnData.enterprise.F18 == null ? "" : returnData.enterprise.F18)+"</li>";
				div += "<li>征信记录："+(returnData.enterprise.F19 == null ? "" : returnData.enterprise.F19)+"</li>";
				div += "</ul>";
				div += "</div>";
			}
			if(returnData.t6163s !=null  && returnData.t6163s.length>0){
				div += "<div  class=\"main_hd02 mt30\">财务状况</div>";
				div += "<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" class=\"table mt10\">";
				div += "<tr class=\"til\">";
				div += "<td align=\"center\">年份</td>";
				div += "<td align=\"center\">主营收入(万)</td>";
				div += "<td align=\"center\">净利润(万)</td>";
				div += "<td align=\"center\"> 总资产(万) </td>";
				div += "<td align=\"center\"> 净资产(万) </td>";
				div += "<td align=\"center\" style='width:30%'>备注</td>";
				div += "</tr>";
				for (var i = 0; i < returnData.t6163s.length ; i++ ){
					var t6163 = returnData.t6163s[i];
					div += "<tr>";
					div += "<td align=\"center\">"+t6163.F02+"年</td>";
					div += "<td align=\"center\">"+formatMoney(t6163.F03)+"</td>";
					div += "<td align=\"center\">"+formatMoney(t6163.F05)+"</td>";
					div += "<td align=\"center\">"+formatMoney(t6163.F06)+"</td>";
					div += "<td align=\"center\">"+formatMoney(t6163.F07)+"</td>";
					div += "<td align=\"center\">"+(t6163.F08 == null ? "" : t6163.F08)+"</td>";
					div += "</tr>";
				}
				div += "</table>";
				div += "</div>";
			}
		}

		div += "<div class=\"main_hd02 mt30\">项目信息</div>";
		div += "<ul class=\"mt30 ml20 clearfix info_list\">";
		div += "<li>融资方："+returnData.qyName+"</li>";
		div += "<li>信用等级："+returnData.creditLevel+"</li>";
		/*div += "<li>本期融资金额："+(returnData.bdxq.F05 >= 10000 ? formatMoney(returnData.bdxq.F05/10000)+"万元" : formatMoney(returnData.bdxq.F05)+"元")+"</li>";*/
		div += "<li>本期融资金额："+formatMoney(returnData.bdxq.F05)+"元"+"</li>";
		div += "<li>项目区域："+returnData.projectArea+"</li>";
		if(null != returnData.t6231.F06){
			div += "<li>还款日期："+returnData.t6231.F06+"</li>";
		}
		div += "<li>年化利率："+formatYearRate(returnData.bdxq.F06)+"%</li>";
		div += "<li>投资截止日期："+formatDate(returnData.bdxq.jsTime)+"</li>";
		/*div += "<li>借款用途："+returnData.t6231.F08+"</li>";
		div += "<li style=\"word-wrap:break-word\" >还款来源："+returnData.t6231.F16+"</li>";*/
		div += "</ul>";
		div += "</div>";

		div += "<div class=\"main_hd02 mt30\">借款描述</div>";
		//div += "<ul class=\"mt30 ml20 clearfix\" width=\"100%\">";
		//div += "<li>"+returnData.t6231.F09+"</li>";
		div += "<div style=\"padding: 10px 20px;\">"+returnData.t6231.F09+"</div>";
		//div += "</ul>";
		div += "</div></div>";

	}
	$("#dataHtml").html(div);
}
//风控信息
function initDbxxData(returnData){
	$("#dataHtml").html("");
	var div = "";
	if(returnData!=null){
		returnData = returnData.fkxxData;
		div += "<ul class=\"clearfix\">";
		if(returnData.dbxx!=null){
			div += "<li class='long'>担保方："+convertJsEmpty2Str(returnData.dbxx.F06)+"</li>";
			if("S" == returnData.dbxx.F08) {
				div += "<li class='long'>担保方介绍：" + convertJsEmpty2Str(returnData.dbxx.F05) + "</li>";
			}
			div += "<li>担保情况："+convertJsEmpty2Str(returnData.dbxx.F07)+"</li>";
			
		}
		/*if(returnData.t6237!=null){
			div += "<li class='long'>担保情况："+convertJsEmpty2Str(returnData.t6237.F02)+"</li>";
		}*/
		div += "</ul>";
		div +="</div>";
	}
	$("#dataHtml").html(div);
}

function convertJsEmpty2Str(val){
	if(val==null||val==undefined||val==''||val=='null'){
		return "";
	}else{
		return val;
	}
}

//风控信息
function initByjdfwbData(returnData){
	$("#dataHtml").html("");
	var div = "";
	if(returnData!=null){
		div += "<div class=\"pt10\" style=\"word-break:break-all;\">"+returnData.byjdfwb+"</div>";
	}
	$("#dataHtml").append(div);
}

//抵押物信息
function initDyxxData(returnData){
	$("#dataHtml").html("");
	var div = "";
	if(returnData!=null){
		returnData = returnData.dywxxData;
		if(returnData.dyxxs!=null){
			var dyxx = returnData.dyxxs.F04;
			div += dyxx;
		}
	}
	$("#dataHtml").append(div);
}

//相关文件
function initXgwjData(returnData){
	$("#dataHtml").html("");
	var div = "<div class=\"plan_tab_con01 clearfix\"> ";
	if(returnData!=null){
		returnData = returnData.xgwjData;
		if(returnData.t6212s!=null && returnData.t6212s.length > 0){
			for(var i = 0 ; i < returnData.t6212s.length ; i++ ){
				var t6212 = returnData.t6212s[i];
				div += "<div class=\"main_hd02\">"+t6212.F02+"</div>";
				div += "<ul class=\"attachment_card clearfix\">";
				if(returnData.t6233s != null && returnData.t6233s.length > 0){
					//非公开附件
					for(var j = 0 ; j < returnData.t6233s.length ; j++){
						var t6233 = returnData.t6233s[j];
						if(t6233 != null && t6233.F03 == t6212.F01){
							div += "<li><a href=\""+t6233.F06+"?id="+t6233.F01+"\" class=\"highslide\" onclick=\"return hs.expand(this)\">";
							div += "<img src=\""+t6233.F06+"?id="+t6233.F01+"\" alt=\"Highslide JS\" width=\"185\" height=\"116\"/>";
							div += "<p class=\"formTitle-21\">"+t6233.F04+"</p>";
							div += "</a></li>";
						}
					}
				}else if(returnData.t6232s != null && returnData.t6232s.length > 0){
					//公开附件
					for(var j = 0 ; j < returnData.t6232s.length ; j++){
						var t6232 = returnData.t6232s[j];
						if(t6232 != null && t6232.F03 == t6212.F01){
							div += "<li><a href=\""+t6232.F07+"\" class=\"highslide\" onclick=\"return hs.expand(this)\">";
							div += "<img src=\""+t6232.F07+"\" alt=\"Highslide JS\" width=\"185\" height=\"116\"/>";
							div += "<p class=\"formTitle-21\">"+t6232.F05+"</p>";
							div += "</a></li>";
						}
					}
				}
				div += "</ul>";
			}
		}
		
	}
	div += "</div>";
	$("#dataHtml").append(div);
}
//还款计划
function initHkjhData(returnData){
	$("#dataHtml").html("");
	pageCount = returnData.pageCount;
	var div = "<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" class=\"table mt10\">";
	div += "<tr class=\"til\">";
	div += "<td align=\"center\">合约还款日期</td>";
	div += "<td align=\"center\">状态</td>";
	div += "<td align=\"center\">科目</td>";
	div += "<td align=\"center\">金额</td>";
	div += "<td align=\"center\">实际还款日期</td>";
	div += "</tr>";
	if(returnData.hkjhList != null && returnData.hkjhList.length > 0){
		for(var i = 0; i < returnData.hkjhList.length ; i++){
			var alsoMoney = returnData.hkjhList[i];
			div += "<tr>";
			div += "<td align=\"center\">"+alsoMoney.F02+"</td>";
			div += "<td align=\"center\">"+getHkChineseName(alsoMoney.F03)+"</td>";
			div += "<td align=\"center\">"+alsoMoney.F05+"</td>";
			div += "<td align=\"center\">"+alsoMoney.F01+"元</td>";
			div += "<td align=\"center\">"+(alsoMoney.F04==null ? "--" : formatDate(alsoMoney.F04))+"</td>";
			div += "</tr>";
		}
	}else{
		div += "<tr><td colspan=\"5\" align=\"center\">暂无数据</td></tr>";
	}
	div += "</table>";
	if(pageCount > 1){
		div += "<div>"+returnData.pageStr+"</div>";
	}
	$("#dataHtml").append(div);
	$("a.page-link").click(function(){
		pageParam(this,"hkjh");
	});
}

//项目动态
function initXmdtData(returnData){
	$("#dataHtml").html("");
	$("#dataHtml").removeClass("details");
	var div = "";
	if(returnData!=null){
		returnData = returnData.t6248List;
		div += "<div class='charity_details_news'><ul>";
		for(var i = 0; i < returnData.length; i++)
			{
			  div += "<li><div class='ico'></div><div class='time'>"+formatDate(returnData[i].F08)+"</div>";
			  div += "<p><span class='mr15'>【"+returnData[i].F04+"】</span><span>"+returnData[i].F06+"</span></p>";
			  if(!isEmpty(returnData[i].F09))
			  {
				  div += "<p><a href='"+returnData[i].F09 +"' target='_blank' class='blue'>查看更多</a></p> ";
			  }
			  div += "</li>";
			}
		div += "</ul></div>";
	}
	$("#dataHtml").html(div);
}

//投资记录
function initTzjlData(returnData){
	$("#dataHtml").html("");
	pageCount = returnData.pageCount;
	var div = "<p class=\"f16\">加入人次<span class=\"orange\">"+returnData.totalCount+"人</span>，投资总额<span class=\"orange\">"+formatMoney(returnData.totalMoney)+"元</span></p>";
	div += "<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" class=\"table mt10\">";
	div += "<tr class=\"til\">";
	div += "<td align=\"center\">序号</td>";
	div += "<td align=\"center\">投资人</td>";
	div += "<td align=\"center\">投资金额</td>";
	div += "<td align=\"center\">投资时间</td>";
	div += "<td align=\"center\">投资方式</td>";
	div += "</tr>";
	if(returnData.tzjlList != null && returnData.tzjlList.length > 0){
		for(var i = 0 ; i < returnData.tzjlList.length ; i++ ){
			var tenderRecord = returnData.tzjlList[i];
			div += "<tr>";
			div += "<td align=\"center\">"+(i+1)+"</td>";
			div += "<td align=\"center\">"+tenderRecord.F10+(tenderRecord.F11=='WEIXIN' || tenderRecord.F11=='APP' ?'<i class="phone_ico"></i>':'')+"</td>";
			div += "<td align=\"center\">"+tenderRecord.F04+"元</td>";
			div += "<td align=\"center\">"+formatTime(tenderRecord.F06)+"</td>";
			div += "<td align=\"center\">"+(tenderRecord.F09=='S'?'自动':'手动')+"</td>";
			div += "</tr>"
		}
	}else{
		div += "<tr><td colspan=\"5\" align=\"center\">暂无数据</td></tr>";
	}
	div += "</table>";
	if(returnData.pageCount > 1){
		div += "<div>"+returnData.pageStr+"</div>";
	}
	$("#dataHtml").append(div);
	$("a.page-link").click(function(){
		pageParam(this,"tzjl");
	});
}

function pageParam(obj,type){
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
	initData(type);
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
    return y + '-' + m + '-' + d ;  
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


function formatYearRate(yearRate){
	if(isEmpty(yearRate)){
		return;
	}
	return parseFloat(yearRate * 100).toFixed(2);
}


function getHkChineseName(status){
	var rtnChineseName;
	switch(status){
	   case "YH": 
		   	 rtnChineseName = "已还";
		     break;
	   case "WH":
		     rtnChineseName = "未还";
		     break;
	   case "TQH":
		     rtnChineseName = "提前还款";
		     break;
	   case "DF": 
		     rtnChineseName = "垫付";
		     break;
	   case "HKZ": 
		     rtnChineseName = "还款中";
		     break;
	   default:
		   	 rtnChineseName = "";
		     break;
	   }
	return rtnChineseName;
}

function setParamToAjax(newCurrentPage, obj){
	currentPage = newCurrentPage;
	initData(pageType);
}

function toAjaxPage(){
	initData(pageType);
}

