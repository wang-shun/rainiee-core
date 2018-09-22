/*ajax查询订单数据*/
function postService(url,data,obj){
	$.ajax({
		type:"post",
		url:url,
		data:data,
		async: false ,
		dataType:"json",
		success: function(returnData){

			var data = returnData.detail;
			var logData = returnData.logList;
			$("#shxxDiv").hide();
			$("#wlxxDiv").hide();
			$("#czxxDiv").hide();
			if(!isEmpty(obj) && obj == "refund")
			{
				showOrderData(data);
			}
			else if(!isEmpty(obj) && obj == "return")
			{
				showOrderData(data);
				showReceivingData(data);
			}
			else if(!isEmpty(obj) && obj == "logistics")
			{
				$("input[name='logistics']").val(data.express);
				$("input[name='oddNumbers']").val(data.expressNum);
				$("#remarks_id").val(data.updateReason);
			}
			else if(!isEmpty(obj) && obj == "send")
			{
				showOrderData(data);
				showReceivingData(data);
				if(data.productType != "virtual"){
					$("#wlxxDiv").show();
				}
				if(!isEmpty(data.express))
				{
					$("#express").val(data.express);
					$("#expressNum").val(data.expressNum);
				}

				if(logData.length > 0)
				{
					showLogData(logData);
				}
			}
			else if(!isEmpty(obj) && obj == "examine")
			{
				showOrderData(data);
				if(!isEmpty(data.productType) && data.productType=='kind')
				{
					showReceivingData(data);
				}
			}
			else
			{
				showOrderData(data);
				showReceivingData(data);
				showLogisticsData(data);

				if(logData != null && logData.length > 0)
				{
					showLogData(logData);
				}
			}
		},
		error: function(XMLHttpRequest, textStatus, errorThrown){
			alert(textStatus);
		}
	});
}

function postServiceForUpdate(url,data){
	$.ajax({
		type:"post",
		url:url,
		data:data,
		async: false ,
		dataType:"json",
		success: function(returnData){
			var data = returnData.detail;
			$("#shxxDiv").hide();
			$("#wlxxDiv").hide();
			$("#czxxDiv").hide();
			showUpdateData(data);
		},
		error: function(XMLHttpRequest, textStatus, errorThrown){
			alert(textStatus);
		}
	});
}

/*显示修改信息页面*/
function showUpdateData(data)
{
	$("#shxxDiv").hide();
	$("#wlxxDiv").hide();
	$("#czxxDiv").hide();
	var kinds = $(".kind");
	var fees = $(".fee");
	if(!isEmpty(data.productType) && data.productType=='kind')
	{
		$(".kind").attr("style","display:block;");
		$(".fee").attr("style","display:none;");
		$("#updateReceiver").val(data.receiver);
		if(!isEmpty(data.areaID)){
			setAreaFromAreaId(data.areaID);
		}
		if(!isEmpty(data.addressMain)){
			$("#updateAddressMain").val(data.addressMain);
		}
		if(!isEmpty(data.addressDetail)){
			$("#updateAddressDetail").val(data.addressDetail);
		}
		if(!isEmpty(data.receiverPhoneNum)){
			$("#updateReceiverPhoneNum").val(data.receiverPhoneNum);
		}
		$("#updateZipCode").val(data.zipCode);
		$("#updateReason").html(data.updateReason);
	}else{
		$(".fee").attr("style","display:block;");
		$(".kind").attr("style","display:none;");
		if(!isEmpty(data.chargePhoneNum)){
			$("#updateChargePhoneNum").val(data.chargePhoneNum);
		}
	}
	$("#updateReason").val(data.updateReason);
}

/* 初始化区域信息 */
function setAreaFromAreaId(areaId){
	var shengId = 0;
    var shiId = 0;
    var xianId = 0;
    if (areaId % 10000 < 100) {
        shengId = areaId;
    } else if (areaId % 100 < 1) {
        shengId = parseInt(areaId / 10000) * 10000;
        shiId = areaId;
    } else {
        shengId = parseInt(areaId / 10000) * 10000;
        shiId = parseInt(areaId / 100) * 100;
        xianId = areaId;
    }
    $("#shengId").val(shengId);
	$("#shiId").val(shiId);
	$("#xianId").val(xianId);
	
	var province = $("select[name='sheng']");
	var city = $("select[name='shi']");
	var area = $("select[name='xian']");
	var city_data = null;
	var area_data = null;
	province.html(getSelectHtml(region, shengId));
	city_data = null;
	area_data = null;
	if (shengId > 0 || shiId > 0 || xianId > 0) {
		city.show();
		area.show();
		for ( var i = 0; i < region.length; i++) {
			if (region[i].id == shengId) {
				city_data = region[i].children;
				if (city_data.length > 0) {
					city.html(getSelectHtml(city_data, shiId));
				}
				break;
			}
		}
		for ( var i = 0; i < city_data.length; i++) {
			if (city_data[i].id == shiId) {
				area_data = city_data[i].children;
				if (area_data.length > 0) {
					area.html(getSelectHtml(area_data, xianId));
				}
				break;
			}
		}
	}
}

function showOrderData(data)
{
	if(!isEmpty(data.orderNo)){
		$("#orderNo").text(data.orderNo);
	}
	if(!isEmpty(data.loginName)){
		$("#loginName").text(data.loginName);
	}
	if(!isEmpty(data.realName)){
		$("#realName").text(data.realName);
	}
	if(!isEmpty(data.phoneNum)){
		$("#phoneNum").text(data.phoneNum);
	}
	var pt = data.productType;
	if(pt == 'virtual'){
		$("#chargePhoneNum").show().prev().show();
		if(!isEmpty(data.chargePhoneNum)){
			$("#chargePhoneNum").text(data.chargePhoneNum);
		}
	}else{
		$("#chargePhoneNum").text('');
		$("#chargePhoneNum").hide().prev().hide();
	}
	if(!isEmpty(data.productNum)){
		$("#productNum").text(data.productNum);
	}
	if(!isEmpty(data.productName)){
		$("#productName").text(data.productName);
	}
	if(!isEmpty(data.worth)){
		$("#worth").text(data.worth);
	}
	if(!isEmpty(data.payType)){
		$("#payType").text(getPayType(data.payType));
	}
	if(!isEmpty(data.quantity)){
		$("#quantity").text(data.quantity);
	}
}

function showReceivingData(data)
{
	if(!isEmpty(data.receiver))
	{
		$("#shxxDiv").show();
		if(!isEmpty(data.receiver)){
			$("#receiver").text(data.receiver);
		}
		if(!isEmpty(data.addressMain)){
			$("#addressMain").text(data.addressMain);
		}
		if(!isEmpty(data.addressDetail)){
			$("#addressDetail").text(data.addressDetail);
		}
		if(!isEmpty(data.receiverPhoneNum)){
		$("#receiverPhoneNum").text(data.receiverPhoneNum);
		}
		if(!isEmpty(data.zipCode)){
			$("#zipCode").text(data.zipCode);
			$("input[name='zipCode']").val(data.zipCode);
		}
	}
}

function showLogisticsData(data)
{
	if(!isEmpty(data.express))
	{
		$("#wlxxDiv").show();
			if(!isEmpty(data.express)){
				$("#express").text(subStringLength(data.express,30,"..."));
				$("#express").attr("title",data.express);


		}
		if(!isEmpty(data.expressNum)){
			$("#expressNum").text(subStringLength(data.expressNum,30,"..."));
			$("#expressNum").attr("title",data.expressNum);
		}
	}
}

/*显示操作日志信息*/
function showLogData(logData)
{
	$("#czxxDiv").show();
	$("#logList").html("");
	var table = $("#logList");
	for(var i = 0; i < logData.length; i++)
	{
		var tr = $("<tr class='tc'></tr>");
		var td = $("<td></td>");
		td.html(i+1);
		td.appendTo(tr);

		var td = $("<td></td>");
		td.html(logData[i].operatorName);
		td.appendTo(tr);

		var td = $("<td></td>");
		td.html(getOperationType(logData[i].F03));
		td.appendTo(tr);

		var td = $("<td></td>");
		td.html(subStringLength(logData[i].F04,10));
		td.attr("title",logData[i].F04);
		td.appendTo(tr);

		var td = $("<td></td>");
		td.html(formatDateTime(logData[i].F06));
		td.appendTo(tr);

		tr.appendTo(table);
	}
}

/*支付方式*/
function getPayType(payType)
{
	var payTypeName;
	switch(payType)
	{
		case "score":
			payTypeName = "积分";
			break;
		case "balance":
			payTypeName = "余额";
			break;
		default:
			payTypeName = "";
			break;
	}
	return payTypeName;
}

/*操作状态*/
function getOperationType(operationType)
{
	var operationTypeName;
	switch(operationType)
	{
		case "SH":
			operationTypeName = "审核";
			break;
		case "XG":
			operationTypeName = "修改";
			break;
		case "FH":
			operationTypeName = "发货";
			break;
		case "TH":
			operationTypeName = "退货";
			break;
		case "SQTK":
			operationTypeName = "申请退款";
			break;
		case "TK":
			operationTypeName = "退款";
			break;
		case "JJTK":
			operationTypeName = "拒绝退款";
			break;
		default:
			operationTypeName = "";
			break;
	}
	return operationTypeName;
}

/*字符串截取*/
function subStringLength(str,maxLength,replace){
	var rtnStr = "";
	if(isEmpty(str)){
		return rtnStr;
	}
	if(typeof(replace) == "undefined" || isEmpty(replace)){
		replace = "...";
	}
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

/*判断是否为空*/
function isEmpty(str){
	if(str == null || str == ""){
		return true;
	}
	return false;
}

function closeShowData()
{
	$("#wlxxDiv").find("input").val("");
	$("#showDataId").hide();
	$(".div_popup_bg").hide();
	$("span[tip]").show();
	$("span[errortip]").hide();
}

function closeUpdateData()
{
	$("#updateDataId").hide();
	$(".div_popup_bg").hide();
	$("span[tip]").show();
	$("span[errortip]").hide();
}

function closeBatchSippingView()
{
	$("#batchSippingView").hide();
}
var formatDateTime = function (time) {  
	if(time==null){
		return "--:";
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
    var mi = date.getMinutes();
    mi = mi < 10 ? ('0' + mi) : mi;
    return y + '-' + m + '-' + d + " "+h+":"+mi;  
}