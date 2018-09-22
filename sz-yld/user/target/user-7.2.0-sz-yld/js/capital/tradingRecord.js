var currentPage = 1;
var pageSize = 10;
var pageCount = 1;


/**
 * Ajax获取下拉列表数据
 */
function getSelectList(){
	var ajaxUrl=$("#tradingRecordUrl").val();
	var dataParam ={};
	$.ajax({
		type:"get",
		dataType:"json",
		url:ajaxUrl,
		data:dataParam,
		async: false,
		success:function(returnData){
			//清空交易类型下拉框的所有选项
			$("#tradingType option").empty();
			var optHTML="<option selected='selected' value=''>--全部--</option>";
			$("#tradingType").append(optHTML);
			optHTML="";
			if(returnData.tradingTypes.length>0){
				var rAssests = returnData.tradingTypes;
				$.each(rAssests, function (n, value) {
					optHTML+="<option value='"+value.F01+"'>"+value.F02+"</option>";
					$("#tradingType").append(optHTML);
					optHTML="";
				});
			}
		},
		error:function(XMLHttpRequest, textStatus, errorThrown){
			//alert(textStatus);
		}
	});
}

/**
 * 根据参数导出excel
 */
function tradingRecordExport(url){
	var tradingType=$("input[name='tradingType']").val();
	var accountType=$("input[name='accountType']").val();
	var startTime=$("#startDate").datepicker().val();
	var endTime=$("#endDate").datepicker().val();
	location.href=url+"?tradingType="+tradingType+"&accountType="+accountType+"&startTime="+startTime+"&endTime="+endTime;
}

/**
 * 交易记录查询AJAX分页
 * firstPage,1:表示当前页是1，0非第一页
 */
function tradingRecordPaging(isFirstPage){
	if(isFirstPage){
		currentPage = 1;
	}
	var ajaxUrl=$("#tradingRecordUrl").val();
	var tradingType=$("input[name='tradingType']").val();
	var accountType=$("input[name='accountType']").val();
	var startTime=$("#startDate").datepicker().val();
	var endTime=$("#endDate").datepicker().val();
	var dataParam = {"currentPage" : currentPage, "pageSize" : pageSize,"tradingType":tradingType,"accountType":accountType,"startTime":startTime,"endTime":endTime };
	$.ajax({
		type:"post",
		dataType:"json",
		url:ajaxUrl,
		data:dataParam,
		async: false,
		success:function(returnData){
			//判断用户是否已经
			if(returnData.msg != "undefined" && returnData.msg !=null && returnData.msg != ""){
				$(".popup_bg").show();
        		$("#info").html(showSuccInfo(returnData.msg,"error",loginUrl));
			}
			
			//查询成功后，将当前页码置为1
			//currentPage =1;
			//展示数据前先清空数据
			$("#dataTable tr").empty();
			var trHTML="<tr class='til'><td align='center'>序号</td><td align='center'>时间</td><td align='center'>类型明细</td><td align='center'>收入(元)</td><td align='center'>支出(元)</td><td align='center'>结余(元)</td><td align='center'>备注</td></tr>";
			$("#dataTable").append(trHTML);
			trHTML = "";
			//分页信息
			if(returnData.tradingRecords!=null){
				$("#pageContent").html(returnData.pageStr);
			}else{
				$("#pageContent").empty();
			}
			pageCount = returnData.pageCount;
			//分页点击事件
			$("a.page-link").click(function(){
				pageParam(this,1);
			});
			if(null == returnData.tradingRecords ){
				$("#dataTable").append("<tr align='center'><td colspan='8'>暂无数据</td></tr>");
			}else if(returnData.tradingRecords.length > 0){
				var rAssests = returnData.tradingRecords;
				$.each(rAssests, function (n, value) {
					var title="";
					if(value.F09.length>10){
						title=value.F09.substr(0,10)+"...";
					}else{
						title=value.F09;
					}
					trHTML +="<tr ><td align='center'>"+(n+1)+"</td><td align='center'>"+value.F18+"</td>" +
						"<td align='center'>"+value.tradeType+"</td>"+
						"<td align='center'>"+fmoney(value.F06,2)+"</td>"+
						"<td align='center'>"+fmoney(value.F07,2)+"</td>"+
						"<td align='center'>"+fmoney(value.F08,2)+"</td>"+
						"<td align='center' title='"+value.F09+"'>"+title+"</td></tr>";
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
	return year + "-" + month + "-" + date+" "+hour+":"+minute+":"+second; 
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
	tradingRecordPaging(false);
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