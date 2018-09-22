$(function(){
	initData();
});
function initData(){
	$.post(url,function(returnData){
		$("#dataBody").html("");
		$("#lookAgreement").html("");
		$("#saveButton").html("");
		returnData = eval("("+returnData+")");
		var tr = " ";
		var agreementSaveList = eval(returnData.agreementSaveList);
		var isNetSign = eval(returnData.isNetSign);
		var isSaveAgreement = eval(returnData.isSaveAgreement);
		var dateTime = new Date();
		if(agreementSaveList != null && agreementSaveList.length > 0){
	    	for(var i = 0; i < agreementSaveList.length; i++){
	    		var item = agreementSaveList[i];
	    		if(isSaveAgreement && isXybq){
	    			if(item.agreementSaveState == "YBQ" && item.agreementVersionNum == versionNum){
	    				var look = "<a href=\""+viewURL+"?id="+ item.agreementId +"\" class=\"highlight fr\" target=\"_blank\">";
	    				look +="查看&gt;";
	    				look +="</a>";
	    				$("#lookAgreement").append(look);
	    			}
	    		}
	    		tr += "<tr><td align='center'>"+item.agreementVersionNum+"</td>";
	    		dateTime.setTime(item.agreementUpdateTime);
	    		tr += "<td align='center'>"+dateTime.Format("yyyy-MM-dd hh:mm")+"</td>";
	    		dateTime.setTime(item.agreementSignTime);
	    		tr += "<td align='center'>"+dateTime.Format("yyyy-MM-dd hh:mm")+"</td></tr>";
	    	}
	    	$("#dataBody").append(tr);
	    	if(isNetSign){
		    	var ok = "<a href=\"javascript:void(0)\" class=\"btn06 btn_gray btn_disabled\">";
		    	ok +="已确定";
		    	ok +="</a>";
		    	$("#saveButton").append(ok);
	    	}else{
				var ok = "<a href=\"javascript:void(0)\" class=\"btn06\" onclick=\"checkUserInfo();\">";
		    	ok +="确定";
		    	ok +="</a>";
		    	$("#saveButton").append(ok);
	    	}
		}else{
			var tr = "<tr><td align='center' colspan=\"3\">暂无数据</td></tr>";
			$("#dataBody").append(tr);
			var ok = "<a href=\"javascript:void(0)\" class=\"btn06\" onclick=\"checkUserInfo();\">";
	    	ok +="确定";
	    	ok +="</a>";
	    	$("#saveButton").append(ok);
		}
    	
	});
}

function checkUserInfo(){
	if (tg && "" == usrCustId)
    {
        //你还没在第三方注册账号
        //开户引导页
        var info = "网签合同必须先注册第三方托管账户，" +"<a id='to_validate' href=\""+openEscrowGuide+"\" class=\"blue\">立即注册</a>";
        $("#errorMess").html(info);
        $("#error_div").show();
        $(".popup_bg").show();
        return false;
    }else if (!isSmrz || !yhrzxx)
    {
        var errorMessage = "网签合同必须先绑定手机号码，实名认证。";
        if (isOpenWithPsd)
        {
            errorMessage = "网签合同必须先绑定手机号码，实名认证，设置交易密码。";
        }
        //跳转到实名认证页面
        var info = errorMessage + "<a id='to_validate' href=\"" + safetyAddr
                + "\" class=\"blue\">去设置 >></a>";
        $("#errorMess").html(info);
        $("#error_div").show();
        $(".popup_bg").show();
        return false;
    } else {
    	$("#affirm").show();
    	$(".popup_bg").show();
    }
}

function netSign(){
	$("#netSigon").removeAttr("onclick");
	$.ajax({
		type:"post",
		dataType:"html",
		url:netSignUrl,
		async:false,
		success:function(data){
			if (data) {
                var ct = eval('(' + data + ')');
                if (ct.length > 0 && ct[0].num == 01) {
                	$("#info").html(showDialogInfo(ct[0].msg, "doubt"));
                    $("div.popup_bg").show();
                    return false;
                }
                if (ct.length > 0 && ct[0].num == 02) {
                	$("#info").html(showDialogInfo(ct[0].msg, "doubt"));
                    $("div.popup_bg").show();
                    return false;
                }
                if(ct.length > 0 && ct[0].num == 00){
                    initData();
                    $("#affirm").hide();
                	$(".popup_bg").hide();
                    return false;
                }
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
	$("#netSigon").attr("onclick","netSign()");
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