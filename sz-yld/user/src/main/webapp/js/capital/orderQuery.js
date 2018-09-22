var currentPage = 1;
var pageSize = 10;
var pageCount = 1;


/**
 * 订单查询ajax分页
 */
function orderQueryPaging(isFirstPage){
	if(isFirstPage){
		currentPage = 1;
	}
	var ajaxUrl=$("#orderServletUrl").val();
	var orderType=$("input[name='orderType']").val();
	var startTime=$("#startDate").datepicker().val();
	var endTime=$("#endDate").datepicker().val();
	var dataParam = {"currentPage" : currentPage, "pageSize" : pageSize,"orderType":orderType,"startTime":startTime,"endTime":endTime};
	
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
			
			//查询成功后，将当前页码置为1
			//currentPage = 1;
			//清空表格中所有行
			$("#dataTable tr").empty();
			var trHTML="<tr class='til'><td align='center'>序号</td><td align='center'>订单号</td><td align='center'>金额(元)</td><td align='center'>类型</td><td align='center'>创建时间</td><td align='center'>状态</td></tr>";
			$("#dataTable").append(trHTML);
			trHTML="";
			//分页信息
			if(returnData.orderList!=null){
				$("#pageContent").html(returnData.pageStr);
			}else{
				$("#pageContent").empty();
			}
			pageCount = returnData.pageCount;
			//分页点击事件
			$("a.page-link").click(function(){
				pageParam(this,1);
			});
			if(null == returnData.orderList ){
				$("#dataTable").append("<tr><td colspan='7' align='center'>暂无数据</td></tr>");
			}else if(returnData.orderList.length > 0){
				var rAssests = returnData.orderList;
				$.each(rAssests, function (n, value) {
					trHTML+="<tr><td align='center'>"+(n+1)+"</td>"+
					"<td align='center'>"+value.F01+"</td>"+
					"<td align='center'>"+fmoney(value.F13, 2)+"</td>"+
					"<td align='center'>"+value.orderTypeName+"</td>"+
					"<td align='center'>"+timeStamp2String(value.F04)+"</td>"+
					//"<td align='center'>"+timeStamp2String(value.F05)+"</td>"+
					"<td align='center'>"+value.status+"</td></tr>";
					$("#dataTable").append(trHTML);
					trHTML="";
				});
			}
		},
		error:function(XMLHttpRequest, textStatus, errorThrown){
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
	orderQueryPaging(false);
}


//格式化时间
function timeStamp2String(time){     
	var timeStamp2String = "";
	if(time){
		var datetime = new Date();     
		datetime.setTime(time);     
		var year = datetime.getFullYear();     
		var month = datetime.getMonth() + 1 < 10 ? "0" + (datetime.getMonth() + 1) : datetime.getMonth() + 1;     
		var date = datetime.getDate() < 10 ? "0" + datetime.getDate() : datetime.getDate();     
		var hour = datetime.getHours()< 10 ? "0" + datetime.getHours() : datetime.getHours();     
		var minute = datetime.getMinutes()< 10 ? "0" + datetime.getMinutes() : datetime.getMinutes();     
		var second = datetime.getSeconds()< 10 ? "0" + datetime.getSeconds() : datetime.getSeconds();     
		timeStamp2String = year + "-" + month + "-" + date+" "+hour+":"+minute+":"+second; 
	}
	return timeStamp2String; 
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
