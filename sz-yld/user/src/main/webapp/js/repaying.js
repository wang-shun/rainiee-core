$(function(){
	$("div.dialog_close").click(function(){
		$(this).parent().hide();
	});
});
function open(id){
	$("div.dialog").hide();
	$("#"+id).show();
}

function onOver(id){
	$("#wdzqyz_"+id).show();
}
function onOut(id){
	$("#wdzqyz_"+id).hide();
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

	if (liId == 1 || $("#hkzdLiId").hasClass("hover")) {
		//先清除原有的数据
		for(var i=0;i<pageSize;i++){
			var divId = "#divId"+i;
			$(divId).remove();
		}
		repay();
	}else if (liId == 2 || $("#yhqdjkLiId").hasClass("hover")) {
		//先清除原有的数据
		for(var i=0;i<pageSize;i++){
			var divId = "#divId"+i;
			$(divId).remove();
		}
		yhqdjk();
	}else if (liId == 3 || $("#yzrdjkLiId").hasClass("hover")) {
		//先清除原有的数据
		for(var i=0;i<pageSize;i++){
			var divId = "#divId"+i;
			$(divId).remove();
		}
		yzrdjk();
	}
}

/**
 *状态,SQZ:申请中;DSH:待审核;DFB:待发布;YFB:预发布;TBZ:投资中;DFK:待放款;HKZ:还款中;YJQ:已结清;YLB:已流标;YDF:已垫付;YZF:已作废;
 * @param status
 * @returns {*}
 */
function getChineseName(status){
	var rtnChineseName;
	switch(status){
		case "SQZ":
			rtnChineseName = "申请中";
			break;
		case "DSH":
			rtnChineseName = "待审核";
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
		case "HKZ":
			rtnChineseName = "还款中";
			break;
		case "YJQ":
			rtnChineseName = "已结清";
			break;
		case "YLB":
			rtnChineseName = "已流标";
			break;
		case "YDF":
			rtnChineseName = "已垫付";
			break;
		case "YZF":
			rtnChineseName = "已作废";
			break;
		case "YZR":
			rtnChineseName = "已转让";
			break;
		default:
			rtnChineseName = "还款中";
			break;
	}
	return rtnChineseName;
}


/**
 * 截取字符串加省略号显示
 * @param str
 * @param maxLength
 * @param replace
 * @returns {String}
 */
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