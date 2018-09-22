var currentPage = 1;
var pageSize = 10;
var pageCount = 1;

function showDialogInfoMsg() {
    $("#dialog").hide();
    $("#dialogHmd").show();
	$(".popup_bg").show();
}

function closeDivHmd() {
    $("#dialogHmd").hide();
	$(".popup_bg").hide();
}

function closeDivPay() {
    $("#dialogPay").hide();
	$(".popup_bg").hide();
	if(isCheckAgree == "true"){
		resetForm();
	}
}
$(function () {
	
    //“我同意”按钮切回事件
    $("input:checkbox[name='iAgree']").attr("checked", false);
    $("input:checkbox[name='iAgree']").click(function() {
        var iAgree = $(this).attr("checked");
        var register = $(".sub-btn");
        if (iAgree) {
            register.removeClass("btn_gray btn_disabled");
            register.attr("id","ok");
            //选中“我同意”，绑定事件
            $("#ok").click(function(){
            	ok_click();
            });
        } else {
            register.addClass("btn_gray btn_disabled");
            $("#ok").unbind("click");
            register.attr("id","");
        }
    });
    $("#ok").click(function(){
    	ok_click();
	});

	$("#tran_pwd").focus(function () {
		$("#errorSpan").hide();
	});
});

//重置我同意按钮
function resetForm(){
    var register = $(".sub-btn");
    $("input:checkbox[name='iAgree']").attr("checked", false);
    register.addClass("btn_gray btn_disabled");
    $("#ok").unbind("click");
    register.attr("id","");
}

function ok_click(){
	var isOpenWithPds =  $("#isOpenWithPsd").val();
	if("true" == isOpenWithPds)
	{
		keleyidialog();
	}else{
		$("#form1").submit();
	}
}

function keleyidialog() {
	var tran_pwd = $("#tran_pwd").val();
    if(!tran_pwd){
    	
    	$("#errorSpan").html("交易密码不能为空").css("fontSize","14px").show();
    	//return;
    }else{
    	$("#errorSpan").hide();
    	var sPwd= RSAUtils.encryptedString(key,tran_pwd);
	    $("#tranPwd").val(sPwd);
    	var param = {'tranPwd':sPwd};
    	$.ajax({
			type:"post",
			dataType:"html",
			url: _checkTxPwdUrl,
			data: param,
			success:function(data){
				data = eval("("+data+")");
				if(data.code=="0001"){
					$("#tran_pwd").next("span").hide();
			    	$("div.dialog").hide();
					$("div.popup_bg").hide();
					//提交
					$("#form1").submit();
				}else if(data.code=="1001"){
					window.location.href=_loginUrl;
				}else{
					$("#errorSpan").html(data.msg).css("fontSize","14px").show();
				}
			},
			error : function(XMLHttpRequest, textStatus, errorThrown){
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
} 

/**
 * 可认购的债权Ajax分页
 */
function canBuyBadClaimPaging(){
    $("#yrgzqLiId").removeClass();
    $("#krgzqLiId").attr("class", "hover");
	var ajaxUrl = $("#ajaxUrl").val();
	var bdxqUrl = $("#bdxqUrl").val();
	$("#pageContent").empty();
	var dataParam = {"currentPage" : currentPage, "pageSize" : pageSize,"type":"krgzq"};
	
	$.ajax({
		type:"post",
		dataType:"json",
		url:ajaxUrl,
		data:dataParam,
		success:function(returnData){
			//垫付债权分页
			//移除table中的所有行
			$("#dataTable tr").empty();
			var trHTML = "<tr class='til f12'><td align='center'>序号</td>" +
					"<td align='center'>借款标题</td><td align='center'>借款金额(元)</td>" +
					"<td align='center'>剩余期数</td><td align='center'>年化利率</td>" +
					"<td align='center'>债权价值(元)</td><td align='center'>认购价格(元)</td><td align='center'>操作</td></tr>";
			$("#dataTable").append(trHTML);		
			trHTML="";
			if(returnData == null){
				$("#dataTable").append("<tr><td colspan='11' align='center'>暂无数据</td></tr>");
				return;
			}
			//分页信息
			if(returnData.blzqzrList!=null){
				$("#pageContent").html(returnData.pageStr);
			}else{
				$("#pageContent").empty();
			}
			pageCount = returnData.pageCount;
			//分页点击事件
			$("a.page-link").click(function(){
				pageParam(this,1);
			});
			if(null == returnData.blzqzrList ){
				$("#dataTable").append("<tr><td colspan='11' align='center'>暂无数据</td></tr>");
			}else if(returnData.blzqzrList.length > 0){
				var rAssests = returnData.blzqzrList;
				var i=1;
				$.each(rAssests, function (n, value) {
					var loanTitle="";
					if(value.loanTitle.length>10){
						loanTitle=value.loanTitle.substr(0,10)+"...";
					}else{
						loanTitle=value.loanTitle;
					}
					trHTML +="<tr><td align='center'>"+i+"</td>" +
					"<td align='center' title='"+value.loanTitle+"'><a href='"+bdxqUrl+value.bidId+".html' class='highlight' target='_blank'>"+loanTitle+"</a></td>"+
					"<td align='center'>"+fmoney(value.loanAmount,2)+"</td>"+
					"<td align='center'>"+value.syPeriods+"/"+value.zPeriods+"</td>"+
					"<td align='center'>"+fmoney(value.yearRate*100,2)+"%</td>"+
					"<td align='center'>"+fmoney(value.creditPrice,2)+"</td>"+
					"<td align='center'>"+fmoney(value.subscribePrice,2)+"</td>"+
					"<td align='center'><a style=\"color: red;cursor: pointer;\" onclick=\"buy("+value.id+","+value.creditPrice+","+value.subscribePrice+")\">购买</a></td></tr>";
					$("#dataTable").append(trHTML);//在table最后面添加一行
					trHTML = "";
					i++;
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
 * 已认购的债权Ajax分页
 */
function alreadyBuyBadClaim(){
    $("#krgzqLiId").removeClass();
    $("#yrgzqLiId").attr("class", "hover");
	var ajaxUrl = $("#ajaxUrl").val();	
	var bdxqUrl = $("#bdxqUrl").val();
	var htUrl = $("#htUrl").val();
	var dataParam = {"currentPage" : currentPage, "pageSize" : pageSize,"type":"yrgzq"};
	$("#pageContent").empty();
	$.ajax({
		type:"post",
		dataType:"json",
		url:ajaxUrl,
		data:dataParam,
		success:function(returnData){
			//垫付债权分页
			//移除table中的所有行
			$("#dataTable tr").empty();
			var trHTML = "<tr class='til f12'><td align='center'>序号</td>" +
					"<td align='center'>借款标题</td><td align='center'>借款金额(元)</td>" +
					"<td align='center'>剩余期数</td><td align='center'>年化利率</td>" +
					"<td align='center'>债权价值(元)</td><td align='center'>认购价格(元)</td>" +
					"<td align='center'>认购日期</td><td align='center'>操作</td></tr>";
			
			$("#dataTable").append(trHTML);		
			trHTML="";
			if(returnData == null){
				return;
			}
			//分页信息
			if(returnData.blzqzrList!=null){
				$("#pageContent").html(returnData.pageStr);
			}else{
				$("#pageContent").empty();
			}
			pageCount = returnData.pageCount;
			//分页点击事件
			$("a.page-link").click(function(){
				pageParam(this,2);
			});
			if(null == returnData.blzqzrList ){
				$("#dataTable").append("<tr><td colspan='12' align='center'>暂无数据</td></tr>");
			}else if(returnData.blzqzrList.length > 0){
				var rAssests = returnData.blzqzrList;
				var i=1;
				$.each(rAssests, function (n, value) {
					var loanTitle="";
					if(value.loanTitle.length>10){
						loanTitle=value.loanTitle.substr(0,8)+"...";
					}else{
						loanTitle=value.loanTitle;
					}
					trHTML +="<tr><td align='center'>"+i+"</td>" +
					"<td align='center' title='"+value.loanTitle+"'><a class='highlight' href='"+bdxqUrl+value.bidId+".html'>"+loanTitle+"</a></td>"+
					"<td align='center'>"+fmoney(value.loanAmount,2)+"</td>"+
					"<td align='center'>"+value.syPeriods+"/"+value.zPeriods+"</td>"+
					"<td align='center'>"+fmoney(value.yearRate*100,2)+"%</td>"+
					"<td align='center'>"+fmoney(value.creditPrice,2)+"</td>"+
					"<td align='center'>"+fmoney(value.subscribePrice,2)+"</td>"+
					"<td align='center'>"+timeStamp2String(value.subscribeTime)+"</td>"+
					"<td align='center'><a class='blue' target='_blank' href='"+htUrl+"?id="+value.id+"'>合同</a></td></tr>";
					$("#dataTable").append(trHTML);//在table最后面添加一行
					trHTML = "";
					i++;
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

function buy(id,creditorCost,money){
	$("#tran_pwd").val("");
	$("#errorSpan").hide();
	if(!checkUser(money)){
		return;
	}else{
		var tokenStr = getTokenStr();
		$("#content").html("您此次购买的债权价值为"+creditorCost+"元，需支付金额"+money+"元，确认购买？");
		$("#tokenStr").html(tokenStr);
		$("#blzqId").val(id);
		$("#dialog").hide();
	    $("#dialogPay").show();
		$(".popup_bg").show();
	}
}



/**
 * 校验购买资格
 * @param money
 */
function checkUser(money){
	var flag = false;
	var checkUrl = $("#checkUrl").val();
	$.ajax({
		type:"post",
		dataType:"json",
		url:checkUrl,
		data:{"money":money},
		async:false,
		success:function(returnData){
			flag = returnData.havaRZTG;
			if(!returnData.havaRZTG){
				$("#info").html(showDialog("doubt",returnData.authStr,returnData.rzUrl));	
				$("div.popup_bg").show();
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
	
	return flag;
}

//封装信息消息文本.
function showDialog(type,authText,_rzUrl){
	return '<div class="dialog">'+
		  '<div class="title"><a href="javascript:closeInfo();" class="out"></a>提示</div>'+
		  '<div class="content">'+
	    	'<div class="tip_information"> '+
	    		'<div class="'+type+'"></div>'+
	    		'<div class="tips f20">'+
	    			authText+
	    		'</div>'+
	    	'</div>'+
	    	'<div class="tc mt20"><a href="'+_rzUrl+'" class="btn01">确定</a></div> '+
	      '</div>'+
	      '</div>';
}

//封装信息消息文本.
function showPayDialog(type,authText,_rzUrl){
	return '<div class="dialog">'+
		  '<div class="title"><a href="javascript:closeInfo();" class="out"></a>认证提示</div>'+
		  '<div class="content">'+
	    	'<div class="tip_information"> '+
	    		'<div class="'+type+'"></div>'+
	    		'<div class="tips f20">'+
	    			authText+
	    		'</div>'+
	    	'</div>'+
	    	'<div class="tc mt20"><a href="'+_rzUrl+'" class="btn01">确定</a></div> '+
	      '</div>'+
	      '</div>';
}

/**
 *分页查询请求
 * @param obj
 * @param liId
 * @returns {boolean}
 */
function pageParam(obj,liId){
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
	if(liId==1){
		canBuyBadClaimPaging();
	}else if(liId==2){
		alreadyBuyBadClaim();
	}
}

//格式化时间
function timeStamp2String(time){     
	var datetime = new Date();     
	datetime.setTime(time);     
	var year = datetime.getFullYear();     
	var month = datetime.getMonth() + 1 < 10 ? "0" + (datetime.getMonth() + 1) : datetime.getMonth() + 1;     
	var date = datetime.getDate() < 10 ? "0" + datetime.getDate() : datetime.getDate();     
	var hour = datetime.getHours()< 10 ? "0" + datetime.getHours() : datetime.getHours();     
	var minute = datetime.getMinutes()< 10 ? "0" + datetime.getMinutes() : datetime.getMinutes();     
	var second = datetime.getSeconds()< 10 ? "0" + datetime.getSeconds() : datetime.getSeconds();     
	//return year + "-" + month + "-" + date+" "+hour+":"+minute+":"+second; 
	return year + "-" + month + "-" + date
} 

//格式化金额
//优化负数格式化问题
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