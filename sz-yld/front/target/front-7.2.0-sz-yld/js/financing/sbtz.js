$(function(){
	$("input").keypress(function(evt){
		evt = (evt) ? evt : ((window.event) ? window.event : "");
		if(evt.keyCode==13) {return false;}
	  });
	
	$('#tips0').hover(function(){
        $("#tips0Msg").show();
       },function(){
    	$("#tips0Msg").hide();
    });
	
	$('#tips0Msg').hover(function(){
        $("#tips0Msg").show();
       },function(){
    	$("#tips0Msg").hide();
    });
	$('#tips1').hover(function(){
        $("#tips1Msg").show();
       },function(){
    	$("#tips1Msg").hide();
    });
	
	$('#tips1Msg').hover(function(){
        $("#tips1Msg").show();
       },function(){
    	$("#tips1Msg").hide();
    });
	
	$('#tips2').hover(function(){
        $("#tips2Msg").show();
       },function(){
    	$("#tips2Msg").hide();
    });
	
	$("#tbButton").click(function(){
		checkBid();
	});

	$("#tbButton2").click(function(){
		checkBid2();
	});
	
	$("#ok").click(function(){
		ok_click();
	});
	
	$("#cancel,.dialog_close,.cancel").click(function(){
		$("#tran_pwd").val("").next("p").hide();
		$("div.popup_bg").hide();
		$("div.dialog").hide();
	});

	function ok_click(){
		//网关需要验证交易密码
		var isOpenWithPds =  $("#isOpenWithPsd").val();
		if("true" == isOpenWithPds)
		{
			keleyidialog();
		}else{
//			getWebSoketConnection();
			var form = document.forms[0];
			form.submit();
			showWaitDiv();
		}
	}
	
	
	//一个汉字相当于2个字符  
	function get_length(s){  
	    var char_length = 0;  
	    for (var i = 0; i < s.length; i++){  
	        var son_char = s.charAt(i);  
	        encodeURI(son_char).length > 2 ? char_length += 2 : char_length += 1;  
	    }  
	    return char_length;  
	}

	// 将文字标题缩短，多出字符用省略号代替
	function subTitlestr(s, len) {
		var char_length = 0;
		if(s && len >= 1 && get_length(s) > len) {
			for (var i = 0; i < s.length; i++){
	            var son_char = s.charAt(i);  
	            encodeURI(son_char).length > 2 ? char_length += 2 : char_length += 1;
	            if(char_length == len) {
	            	return s.substring(0, i + 1) + '...';
	            }
	            else if(char_length > len){
	            	return s.substring(0, i) + '...';
	            }
	        }
		}
		return s;
	}
	// 检查表格标题长度，超过长度截取（一个中文字符占两个长度）
	var formTitle = $("[class*='formTitle-']");
	for(var i = 0; i < formTitle.length; i++) {
		var msg = formTitle[i].className.split(" ");
		for (var j = 0; j < msg.length; j++) {
			var temp = $.trim(msg[j]);
			if(temp.length>0){
				if(temp.indexOf('formTitle') != -1) {
					var formTitleClass = temp.split("-");
					if(formTitleClass.length>=2){
						formTitle[i].setAttribute('title', formTitle[i].innerText);
						formTitle[i].innerText = subTitlestr(formTitle[i].innerText, formTitleClass[1]);
					}
				}
			}
		}
	}
});

function keleyidialog() {
	var tran_pwd = $("#tran_pwd").val();
    if(!tran_pwd){
    	
    	$("#tran_pwd").next("p").html("交易密码不能为空！").css("fontSize","14px").show();
    	//return;
    }else{
    	var sPwd= RSAUtils.encryptedString(key,tran_pwd);
	    $("#tranPwd").val(sPwd);
    	var data={"password":sPwd,"id":accountId};
    	var flag = true;
		$.ajax({
			type:"post",
			dataType:"html",
			async:false,
			url:checkUrl,
			data:data,
			success:function(data){
				if(data != "01"){
				    $("#tran_pwd").next("p").html(data).css("fontSize","14px").show();
				    flag = false;
			    }
			}
		});
		if(flag){
	    	$("#tran_pwd").next("p").hide();
	    	$("div.dialog").hide();
			$("div.popup_bg").hide();
		    var sPwd= RSAUtils.encryptedString(key,tran_pwd);
		    $("#tranPwd").val(sPwd);
		    //提交
//			getWebSoketConnection();
			var form = document.forms[0];
			form.submit();
			showWaitDiv();
		}
    }
}

function showWaitDiv(){
//	$("#info").html(showDialogInfo("投标进行中，请稍后。", "successful"));
	$("#info").html(showSuccInfo("恭喜您，投资成功!", "successful",$("#sbSucc").val()));
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

//封装信息消息文本.
function showRiskDialog(type){
	return '<div class="dialog">'+
		  '<div class="title"><a href="javascript:closeInfo();" class="out"></a>提示</div>'+
		  '<div class="content">'+
	    	'<div class="tip_information"> '+
	    		'<div class="'+type+'"></div>'+
	    		'<div class="tips f20">您的风险承受等级不可投资该项目。</div>'+
	    	'</div>'+
	    	'<div class="tc mt20"><a href="'+_rzUrl+'" class="btn01">去评估</a>&nbsp;&nbsp;&nbsp;<a href="javascript:closeInfo()" class="btn01">取消</a></div>'+
	      '</div>'+
	      '</div>';
}

function checkBid2(){
	var amount1=$("#amount1").val();
	var isTG= $("#isTG").val();

	if(havaRZTG=="false"){
		$("#info1").html(showRZDialog("doubt"));	
		$("div.popup_bg").show();
		return;
	}
	
	if(!amount1){
		$("#info1").html(showDialogInfo("请输入捐助金额！","doubt"));
		$("div.popup_bg").show();
		return;
	}
	
	if(!isDouble()){
		return;
	}
	
	/*var syje= $("#syje").val();
	if(parseInt(syje) < parseInt(amount1)){
		$("#info1").html(showDialogInfo("您的捐助金额大于标的剩余金额。","doubt"));
		$("div.popup_bg").show();
		return;

	}*/
	
	/*var minBid = $("#minBid").val();
	if(parseInt(amount1) < parseInt(minBid)){
		$("#info1").html(showDialogInfo("您的捐助金额小于起捐金额"+fmoney(minBid,2)+"元。","doubt"));
		$("div.popup_bg").show();
		return;
	}*/

	/*if(parseFloat(syje) - parseFloat(amount1) <  parseFloat(minBid) &&  parseFloat(syje) - parseFloat(amount1) >  0){
		$("#info1").html(showDialogInfo("捐助后剩余可捐金额不能在0.00元到"+fmoney(minBid,2)+"元之间。","doubt"));
		$("div.popup_bg").show();
		return;
	}*/

	var kyMoney=$("#kyMoney").val();
	if(parseFloat(kyMoney) < parseFloat(amount1)){
		/*var url=$("#charge").val();
		 $("#info").html(showForwardInfo('您的账户余额不足进行本次投资 ，请充值，点击"确定"，跳到充值页面，点击"取消"返回当前页面',"perfect",url));
		 $("div.popup_bg").show();
		 return;*/
		var $cli = $("#tbButton").parent().parent();
		var err = $cli.children("p").eq(1);
		$error = err.nextAll("p[errortip]");
		$tip = err.nextAll("p[tip]");
//		$error.addClass("error_tip");
//		$error.html("可用金额不足！");
		$tip.hide();
//		$error.show();
		$("#info1").html(showDialogInfo("可用金额不足。", 'doubt'));
		return false;
	}

	var isYuqi= $("#isYuqi").val();

	if(isYuqi == "Y"){
		$("#info1").html(showDialogInfo("您有逾期未还的借款，请先还完再操作。","doubt"));
		$("div.popup_bg").show();
		return;
	}

	$("#zxMoney").text(amount1);
	$("div.popup_bg").show();
	$("div.dialog").show();
}

function checkBid(){
	
	var amount=$("#amount").val();
	var isTG= $("#isTG").val();

	if(havaRZTG=="false"){
		$("#info").html(showRZDialog("doubt"));	
		$("div.popup_bg").show();
		return;
	}
	if(isOpenRiskAccess=="true" && isInvestLimit=="true" && isRiskMatch=="false"){
		$("#info").html(showRiskDialog("doubt"));	
		$("div.popup_bg").show();
		return;
	}
	if(amount == 0 || amount.length==0){
		$("#info").html(showDialogInfo("请输入投资金额！","doubt"));
		$("div.popup_bg").show();
		return;
	}
	
	var kyMoney=$("#kyMoney").val();
	if(parseInt(kyMoney) < parseInt(amount)){
		/*var url=$("#charge").val();
		$("#info").html(showForwardInfo('您的账户余额不足进行本次投资 ，请充值，点击"确定"，跳到充值页面，点击"取消"返回当前页面',"perfect",url));
		$("div.popup_bg").show();
		return;*/
		var $cli = $("#tbButton").parent().parent();
		var err = $cli.children("p").eq(1);
		$error = err.nextAll("p[errortip]");
		$tip = err.nextAll("p[tip]");
//		$error.addClass("error_tip");
//		$error.html("可用金额不足！");
		$tip.hide();
//		$error.show();
		$("#info").html(showDialogInfo("可用金额不足。", 'doubt'));
		return false;
	}
	
	var syje= $("#syje").val();
	if(parseInt(syje) < parseInt(amount)){
		$("#info").html(showDialogInfo("您的投资金额大于标的剩余金额。","doubt"));
		$("div.popup_bg").show();
		return;
		
	}
	
	var zdktje= $("#zdktje").val();
	if(parseFloat(zdktje) < parseFloat(amount)){
		$("#info").html(showDialogInfo("您的投资金额大于标的最大可投金额。","doubt"));
		$("div.popup_bg").show();
		return;
	}
	
	var minBid = $("#minBid").val();
	if(parseInt(amount) < parseInt(minBid)){
		$("#info").html(showDialogInfo("您的投资金额小于起投金额"+fmoney(minBid,2)+"元。","doubt"));
		$("div.popup_bg").show();
		return;
	}
	
	if(parseInt(syje) - parseInt(amount) <  parseInt(minBid) &&  parseInt(syje) - parseInt(amount) >  0){
		$("#info").html(showDialogInfo("投资后剩余可投金额不能在0.00元到"+fmoney(minBid,2)+"元之间。","doub"));	
		$("div.popup_bg").show();
		return;
	}
	
	var isYuqi= $("#isYuqi").val();
	
	if(isYuqi == "Y"){
		$("#info").html(showDialogInfo("您有逾期未还的借款，请先还完再操作。","doubt"));
		$("div.popup_bg").show();
		return;
	}
	
	var isCanTXSB = $("#isCanTXSB").val();
	if(isCanTXSB == 'false'){
		$("#info").html(showDialogInfo("感谢您的支持！<br/>此标为新手标，只有未成功投资过并且没有购买过债权的新用户才可以投资。","doubt"));
		$("div.popup_bg").show();
		return;
	}
	
	//我的奖励
	$(".myReward_tip").hide();
	if($("#userReward").is(':checked') || $("#useType").val() == "ALL"){
		/*if($("input[name='myRewardType']").val()=="experience"){
			$("#tyjSpan").show();
		}*/
		var $hbRule = $("#hbRule li.selected");
		if($hbRule && $hbRule.attr("hbAmount") && $hbRule.attr("hbAmount") != ""){

			var investUseRule = $hbRule.attr("investUseRule");
			if($hbRule.attr("userRule") == 1 && parseFloat(amount)<parseFloat(investUseRule)){
				$("#info").html(showDialogInfo("投资满"+investUseRule+"元使用。","doubt"));	
				$("div.popup_bg").show();
				return;
			}
			//红包金额小于投资金额才能使用
			if(parseFloat($hbRule.attr("hbAmount"))>=parseFloat(amount))
			{
				$("#info").html(showDialogInfo("红包金额小于投资金额才可使用。","doubt"));
				$("div.popup_bg").show();
				return;
			}
			$("#hbAmount").text(fmoney(parseFloat($hbRule.attr("hbAmount")),2));
			$("#realInvestAmount").text(fmoney((parseFloat(amount)-parseFloat($hbRule.attr("hbAmount"))),2));
			$("#bhDiv").show();
		}
		var $jxqRule = $("#jxqRule li.selected");
		if($jxqRule && $jxqRule.attr("jxlValue") && $jxqRule.attr("jxlValue") != ""){
			var investUseRule = $jxqRule.attr("investUseRule");
			if($jxqRule.attr("userRule") == 1 && parseFloat(amount)<parseFloat(investUseRule)){
				$("#info").html(showDialogInfo("投资满"+investUseRule+"元使用。","doubt"));	
				$("div.popup_bg").show();
				return;
			}
			$("#jxlValue").text(fmoney((parseFloat($jxqRule.attr("jxlValue"))),2));
			$("#jxqSpan").show();
		}
		if($("input[name='myRewardType']").val()=="experience" || ($("input[name='tyjRule']").val()!=null && $("input[name='tyjRule']").val() != "")){
			$("#tyjSpan").show();
			$("#usedExp").val("yes");
		}
		if($("input[name='myRewardType']").val()==""){
			$("#info").html(showDialogInfo("请选择奖励类型！","doubt"));	
			$("div.popup_bg").show();
			return;
		}
	}
	$("#zxMoney").text(fmoney(amount,2));
	$("div.popup_bg").show();
	$("#tzConfim.dialog").show();
	
}

function openImageDiv(id) {

	//显示灰色 jQuery 遮罩层 
	var bh = $("body").height();
	var bw = $("body").width();
	$("#fullbg"+id).css({
		height : bh,
		width : bw,
		display : "block"
	});
	$("#dialog"+id).show();
}
//关闭灰色 jQuery 遮罩 
function closeBg(id) {
	$("#fullbg"+id).hide();
	$("#dialog"+id).hide();
}

function AutoResizeImage(maxWidth,maxHeight,objImg,imgId){
	nivWidth = window.screen.width;
	nivHeight = window.screen.height;
	if(nivWidth == 1366 && nivHeight == 768){
		maxWidth = 750;
		maxHeight = 750;
	}else if(nivWidth == 1360 && nivHeight == 768){
		maxWidth = 745;
		maxHeight = 745;
	}else if(nivWidth == 1280 && nivHeight == 1024){
		$("#dialog"+imgId).css("left","30%");
		if($.browser.msie){
		maxWidth = 945;
		maxHeight = 945;
		$("#dialog"+imgId).css("top","25%");	
		}else{
			maxWidth = 950;
			maxHeight = 950;
		}
	}else if(nivWidth == 1280 && nivHeight == 960){
		$("#dialog"+imgId).css("left","30%");
		if($.browser.msie){
		maxWidth = 935;
		maxHeight = 935;
		$("#dialog"+imgId).css("top","20%");	
		}else{
			maxWidth = 950;
			maxHeight = 950;
			$("#dialog"+imgId).css("top","25%");
		}
	}else if(nivWidth == 1280 && nivHeight == 800){
		$("#dialog"+imgId).css("left","30%");
		if($.browser.msie){
		maxWidth = 935;
		maxHeight = 935;
		$("#dialog"+imgId).css("top","20%");	
		}else{
			maxWidth = 950;
			maxHeight = 950;
			$("#dialog"+imgId).css("top","25%");
		}
	}else if(nivWidth == 1024 && nivHeight == 768){
		$("#dialog"+imgId).css("left","33%");
		if($.browser.msie){
		maxWidth = 680;
		maxHeight = 680;
		$("#dialog"+imgId).css("top","27%");	
		}else{
			maxWidth = 750;
			maxHeight = 750;
			$("#dialog"+imgId).css("top","25%");
		}
	}
	
	var img = new Image();
	img.src = objImg.src;
	var hRatio;
	var wRatio;
	var Ratio = 1;
	var w = img.width;
	var h = img.height;
	wRatio = maxWidth / w;
	hRatio = maxHeight / h;
	if (maxWidth ==0 && maxHeight==0){
	Ratio = 1;
	}else if (maxWidth==0){//
	if (hRatio<1) Ratio = hRatio;
	}else if (maxHeight==0){
	if (wRatio<1) Ratio = wRatio;
	}else if (wRatio<1 || hRatio<1){
	Ratio = (wRatio<=hRatio?wRatio:hRatio);
	}
	if (Ratio<1){
	w = w * Ratio;
	h = h * Ratio;
	}
	objImg.height = h;
	objImg.width = w;
	}

    function showMyReward(obj){
    	$(".reward_value").hide();
    	var myRewardType = $("#myRewardType input[name='myRewardType']").val();
    	switch (myRewardType) {
		case "experience":
			yqCountAmount();
			$("#experienceRule").show();
			break;
        case "hb":
        	$("#hbRule").remove();
        	$("#jxqRule").remove();
        	$("#experienceRule").after('<select class="pulldown_1 reward_value" style="display:none;" id="hbRule" name="hbRule"></select>')
        	$("#hbRule").append(getHbHtml());
        	$('#hbRule').selectlist({
        		width: 180,
        		optionHeight: 22,
        		height: 22,
        		onChange:function(){
        		}
        	});
        	yqCountAmount();
        	$("#hbRule").show();
			break;
        case "jxq":
        	$("#jxqRule").remove();
        	$("#hbRule").remove();
        	$("#experienceRule").after('<select class="pulldown_1 reward_value" style="display:none;" id="jxqRule" name="jxqRule" ></select>')
        	$("#jxqRule").append(getJxqHtml());
        	$('#jxqRule').selectlist({
        		width: 180,
        		optionHeight: 22,
        		height: 22,
        		onChange:function(){
        			yqCountAmount();
        		}
        	});
        	yqCountAmount();
        	$("#jxqRule").show();
			break;
		default:
			yqCountAmount();
			break;
		}
    }
    
    /**
     * 加息券
     */
    function getJxqHtml(){
    	var jxqList = eval(jxqJson);
    	var html = "";
    	$.each(jxqList, function (n, value) {
    		html += "<option userRule='"+value.useRule+"' investUseRule='"+value.investUseRule+"' jxlValue='"+value.value+"' value='"+value.F01+"'>+"+getJxqUseRuleDesc(value.value,value.useRule,value.investUseRule)+"</option>";
		});
    	return html;
    }
    
    function getJxqUseRuleDesc(value,ruleStatus,ruleAmount){
	    var rtnChineseName;
	    switch(ruleStatus){
		    case 0:
			    rtnChineseName = fmoney(value,2)+"%(无限制)";
			    break;
		    case 1:
			    rtnChineseName = fmoney(value,2)+"%(投资满"+ruleAmount+"元使用)";
			    break;
		    default:
			    rtnChineseName = "";
			    break;
	    }
	    return rtnChineseName;
    }
    
    /**
     * 红包
     */
    function getHbHtml(){
    	var hbList = eval(hbJson);
    	var html = "";
    	$.each(hbList, function (n, value) {
    		html += "<option userRule='"+value.useRule+"' investUseRule='"+value.investUseRule+"' hbAmount='"+value.value+"' value='"+value.F01+"'>"+getHbUseRuleDesc(value.value,value.useRule,value.investUseRule)+"</option>";
		});
    	return html;
    }
    
    function getHbUseRuleDesc(value,ruleStatus,ruleAmount){
	    var rtnChineseName;
	    switch(ruleStatus){
		    case 0:
			    rtnChineseName = value+"元(无限制)";
			    break;
		    case 1:
			    rtnChineseName = value+"元(投资满"+ruleAmount+"元使用)";
			    break;
		    default:
			    rtnChineseName = "";
			    break;
	    }
	    return rtnChineseName;
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