$(document).ready(function (){
    /*$("#statusSltId").change(function (){
	    var status = $("#statusSltId").val();
		ajaxSubmit(status);
	});*/

	$("#statusSltId").selectlist({
		zIndex: 15,
		width: 105,
		optionHeight: 28,
		height: 28,
		onChange: function(){
			var status = $("input[name='status']").val();
			ajaxSubmit(status);
		}
	});
    $("#statusSltId option").eq(0).attr("selected",true);//解决按F5刷新问题
	ajaxSubmit();
});

/**
 *分页查询请求
 * @param obj
 * @param liId
 * @returns {boolean}
 */
function pageParam(obj,status) {
	if ($(obj).hasClass("on")) {
		return false;
	}
	$(obj).addClass("on");
	$(obj).siblings("a").removeClass("on");
	if ($(obj).hasClass("startPage")) {
		currentPage = 1;
	} else if ($(obj).hasClass("prev")) {
		currentPage = parseInt(currentPage) - 1;
	} else if ($(obj).hasClass("next")) {
		currentPage = parseInt(currentPage) + 1;
	} else if ($(obj).hasClass("endPage")) {
		currentPage = pageCount;
	} else {
		currentPage = parseInt($(obj).html());
	}
	ajaxSubmit(status);
}

function getStatusDesc(status){
	var rtnChineseName;
	switch(status){
		case "WSY":
			rtnChineseName = "未使用";
			break;
		case "YSY":
			rtnChineseName = "已使用";
			break;
		case "YGQ":
			rtnChineseName = "已过期";
			break;
		default:
			rtnChineseName = "";
			break;
	}
	return rtnChineseName;
}

function getSourceDesc(type,quota){
	var rtnChineseName;
	switch(type){
		case "register":
			rtnChineseName = "注册赠送";
			break;
		case "recharge":
			rtnChineseName = "单笔充值满"+quota+"元赠送";
			break;
		case "firstrecharge":
			rtnChineseName = "首次充值满"+quota+"赠送";
			break;
		case "birthday":
			rtnChineseName = "生日赠送";
			break;
		case "investlimit":
			rtnChineseName = "投资额度满"+quota+"元赠送";
			break;
		case "foruser":
			rtnChineseName = "指定用户赠送";
			break;
		case "tjsccz":
			rtnChineseName = "被推荐人首次充值满"+quota+"元赠送";
			break;
		case "tjsctz":
			rtnChineseName = "被推荐人首次投资满"+quota+"元赠送";
			break;
		case "tjtzze":
			rtnChineseName = "被推荐人投资总额达到"+quota+"元赠送";
			break;
		case "exchange":
			rtnChineseName = "积分兑换";
			break;
		case "integraldraw":
			rtnChineseName = "积分抽奖";
			break;
		default:
			rtnChineseName = "";
			break;
	}
	return rtnChineseName;
}

function getUseRuleDesc(ruleStatus,ruleAmount){
	var rtnChineseName;
	switch(ruleStatus){
		case 0:
			rtnChineseName = "无限制";
			break;
		case 1:
			rtnChineseName = "投资满"+ruleAmount+"元使用";
			break;
		default:
			rtnChineseName = "";
			break;
	}
	return rtnChineseName;
}

//格式化时间
function timeStamp2String(time){     
	if(time==''||time==undefined||time==null){
		return "";
	}
	var datetime = new Date();     
	datetime.setTime(time);     
	var year = datetime.getFullYear();     
	var month = datetime.getMonth() + 1 < 10 ? "0" + (datetime.getMonth() + 1) : datetime.getMonth() + 1;     
	var date = datetime.getDate() < 10 ? "0" + datetime.getDate() : datetime.getDate();     
	return year + "-" + month + "-" + date; 
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