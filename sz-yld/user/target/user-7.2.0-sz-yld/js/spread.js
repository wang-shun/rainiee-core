var currentPage = 1;
var pageSize = 10;
var pageCount = 1;

/**
 * 邀请好友统计
 */
function invateFriendsRegCount(){
	var ajaxUrl = $("#wdtgUrl").val();
	var dataParam = {};
	$.ajax({
		type:"post",
		dataType:"json",
		url:ajaxUrl,
		data:dataParam,
		success:function(returnData){
			
			if(returnData.msg != "undefined" && returnData.msg !=null && returnData.msg != ""){
				$(".popup_bg").show();
        		$("#info").html(showSuccInfo(returnData.msg,"error",loginUrl));
        		return;
			}
			
			//邀请好友统计数据显示
			$("#dataTable1 tr").empty();
			var countHTML = "<tr class='til'><td align='center'>推广客户数(个)</td><td align='center'>持续奖励总计(元)</td>" +
							"<td align='center'>有效推广奖励总计(元)</td><td align='center'>推广奖励总计(元)</td></tr>";
			$("#dataTable1").append(countHTML);
			countHTML="<tr><td align='center'>"+returnData.spreadTotle.yqCount+"</td><td align='center'>"+returnData.spreadTotle.rewardCxtg+"</td>" +
					  "<td align='center'>"+returnData.spreadTotle.rewardYxtg+"</td><td align='center'>"+returnData.spreadTotle.rewardTotle+"</td></tr>";
			$("#dataTable1").append(countHTML);
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
 * 邀请好友注册详情Ajax分页
 */
function invateFriendsRegPaging(){	
	var ajaxUrl = $("#wdtgUrl").val();	
	var dataParam = {"currentPage" : currentPage, "pageSize" : pageSize};
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
			
			//邀请好友统计数据显示
			$("#dataTable1 tr").empty();
			var countHTML = "<tr class='til'><td align='center'>推广客户数(个)</td><td align='center'>持续奖励总计(元)</td>" +
							"<td align='center'>有效推广奖励总计(元)</td><td align='center'>推广奖励总计(元)</td></tr>";
			$("#dataTable1").append(countHTML);
			countHTML="<tr><td align='center'>"+returnData.spreadTotle.yqCount+"</td><td align='center'>"+returnData.spreadTotle.rewardCxtg+"</td>" +
					  "<td align='center'>"+returnData.spreadTotle.rewardYxtg+"</td><td align='center'>"+returnData.spreadTotle.rewardTotle+"</td></tr>";
			$("#dataTable1").append(countHTML);
			//邀请好友注册详情
			//移除table中的tr
			$("#dataTable tr").empty();
			//填充数据,li样式需要改变
			var trHTML="<tr class='til'><td align='center'>序号</td><td align='center'>您推荐好友的用户名</td><td  align='center'>注册时间</td></tr>";
			$("#dataTable").append(trHTML);//在table最后面添加一行
			trHTML = "";
			
			//分页信息
			if(returnData.pgList!=null){
				$("#pageContent").html(returnData.pageStr);
			}
			pageCount = returnData.pageCount;
			//分页点击事件
			$("a.page-link").click(function(){
				pageParam(this,1);
			});
			if(null == returnData.pgList ){
				$("#dataTable").append("<tr><td colspan='3'  align='center'>暂无数据</td></tr>");
			}else if(returnData.pgList.length > 0){
				var rAssests = returnData.pgList;
				$.each(rAssests, function (n, value) {
					trHTML +="<tr><td  align='center'>"+(n+1)+"</td><td  align='center'>"+(value.userName).substr(0,2)+"***"+(value.userName).substr(4,value.userName.length)+"</td>" +
						"<td  align='center'>"+timeStamp2String(value.zcTime)+"</td></tr>";
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
	invateFriendsRegPaging();
}