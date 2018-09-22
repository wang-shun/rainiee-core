$(function(){
	
	$("input").keypress(function(event){
		event = event || window.event;
		if(event.keyCode==13) {return false;}
	  });
	
	$("#amount").keyup(function(){
		var fs=$("#amount").val();
		var zrjg=$("#zrjg").val();
		var zxMoney=zrjg*fs;
		$("#tbMoney").text(zxMoney);
	});
	
	/*$("#tbButton").click(function(){
		checkBid();
	});*/
	
	$("#ok").click(function(){
		ok_click();
	});
	
	$("#cancel,.out,.cancel").click(function(){
		$("#tran_pwd").val("").next("p").hide();
		$("div.popup_bg").hide();
		$("div.dialog").hide();
		$("#errorSpan").hide();
	});
	
	
});

function ok_click(){
	var isOpenWithPds =  $("#isOpenWithPsd").val();
	if("true" == isOpenWithPds)
	{
		keleyidialog();
	}else{
		var form = document.forms[0];
		form.submit();
	}
}

function checkBid(){

	var fs=$("#amount").val();
	if(fs == 0 || fs.length==0){
		$("#info").html(showDialogInfo("请输入购买份数","perfect"));	
		$("div.popup_bg").show();
		return;
	}
	var syfs= $("#syfs").val();
	if(parseInt(syfs) < parseInt(fs)){
		$("#info").html(showDialogInfo("你的购买的份数大余剩余债权份数","perfect"));	
		$("div.popup_bg").show();
		return;
	}
	var kyMoney=$("#kyMoney").val();
	var zrjg=$("#zrjg").val();
	var zxMoney=zrjg*fs;
	if(kyMoney < zxMoney){
		var url=$("#charge").val();
		$("#info").html(showForwardInfo('你的账户余额不足进行本次投资 ，请充值，点击"确定"，跳到充值页面，点击"取消"返回当前页面',"perfect",url));
		$("div.popup_bg").show();
		return;
	}
	var isYuqi= $("#isYuqi").val();
	
	if(isYuqi == "Y"){
		$("#info").html(showDialogInfo("您有逾期未还的借款，请先还完再操作。","perfect"));
		$("div.popup_bg").show();
		return;
	}
	
	$("#fs").text(fs);
	$("#zxMoney").text(zxMoney);
	$("div.popup_bg").show();
	$("#showBuy").show();
}

function buy(zrjg,zqjz,zqsqId,isInvestLimit,isRiskMatch,isOpenRiskAccess){	
	if(havaRZTG=="false"){
		$("#info").html(showRZDialog("doubt"));	
		$("div.popup_bg").show();
		return;
	}
	$("#zqzrId").val(zqsqId);
	$("#zqjz").html(fmoney(zqjz,2));
	$("#zrjg").html(fmoney(zrjg,2));
	$("div.popup_bg").show();
	$("#showBuy").show();
	if(isOpenRiskAccess=="true"&&isInvestLimit=="true" && isRiskMatch == "false"){
		$("#id_content").hide();
		$("#idRisk").show();
		$("#idRisk").html('<div class="dialog">'+
				  '<div class="title"><a href="javascript:closeInfo();" class="out"></a>提示</div>'+
				  '<div class="content">'+
			    	'<div class="tip_information"> '+
			    		'<div class="doubt"></div>'+
			    		'<div class="tips f20">您的风险承受等级不可购买该债权。</div>'+
			    	'</div>'+
			    	'<div class="tc mt20"><a href="'+_rzUrl+'" class="btn01">去评估</a>&nbsp;&nbsp;&nbsp;<a href="javascript:closeInfo()" class="btn01 btn_gray ml20">取消</a></div>'+
			      '</div>'+
			      '</div>');
	}else{
		$("#idRisk").hide();
		$("#id_content").show();
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
					var form = document.forms[0];
					form.submit();
				}else if(data.code=="1001"){
					window.location.href=_loginUrl;
				}else{
					$("#errorSpan").html(data.msg).css("fontSize","14px").show();
				}
			},
			error : function(){
			 	alert("系统繁忙，请稍后重试！");
			}
	    });
    }
} 
//封装信息消息文本.
function showRZDialog(type){
	return '<div class="dialog">'+
		  '<div class="title"><a href="javascript:closeInfo();" class="out"></a>认证提示</div>'+
		  '<div class="content">'+
	    	'<div class="tip_information"> '+
	    		'<div class="'+type+'"></div>'+
	    		'<div class="tips f20">'+
	    			authText+
	    		'</div>'+
	    	'</div>'+
	    	'<div class="tc mt20"><a href="'+_rzUrl+'" class="btn01">去认证</a></div> '+
	      '</div>'+
	      '</div>';
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