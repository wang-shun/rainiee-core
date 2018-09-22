var currentPage = 1;
var pageSize = 10;
var pageCount = 1;

/**
 * 查询ajax分页
 */
function donationPaging(){
	var ajaxUrl=$("#donationUrl").val();
	var grbBdxqUrl=$("#grbBdxqUrl").val();
	var jzxyUrl = $("#jzxyUrl").val();
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
			
			//清空表格中所有行
			$("#dataTable tr").empty();
			var trHTML='<tr class="til"><td align="center">序号</td><td align="center">公益标题</td><td align="center">捐赠金额(元)</td><td align="center">捐赠时间</td><td align="center">操作</td><td align="center"></td></tr>';
			$("#dataTable").append(trHTML);
			trHTML="";
			//分页信息
			if(returnData.donationList!=null){
				$("#pageContent").html(returnData.pageStr);
			}else{
				$("#pageContent").empty();
			}
			pageCount = returnData.pageCount;
			//分页点击事件
			$("a.page-link").click(function(){
				pageParam(this,1);
			});
			$("#donationsAmount").text(formatMoney(returnData.statis.donationsAmount));
			$("#donationsNum").text(returnData.statis.donationsNum+'笔');
			if(null == returnData.donationList || returnData.donationList.length<=0){
				$("#dataTable").append("<tr><td align='center' colspan='7'>暂无数据</td></tr>");
			}else if(returnData.donationList.length > 0){
				var rAssests = returnData.donationList;
				var i = 0;
				$.each(rAssests, function (n, value) {
					 i = n+1;
					trHTML+="<tr><td align='center'>"+i+"</td>"+
					"<td align='center' title='"+value.t6242.F03+"'>"+subStringLength(value.t6242.F03,10)+"</td>"+
					"<td align='center'>"+formatMoney(value.F04)+"</td>"+
					"<td align='center'>"+timeStamp2String(value.F06)+"</td>"+
					"<td align='center'><a href='"+grbBdxqUrl+value.F02+".html' class='highlight'>查看</a></td>";
					if(value.t6242.F11== 'JKZ' || value.t6242.F11 == 'YJZ')
					{
						trHTML += "<td align='center'><a target='_blank' href='"+jzxyUrl+"?id="+value.F02+"&g=g' class='highlight'>捐助协议</a></td></tr>";
					}
					else
					{
						trHTML += "<td align='center'></td></tr>";
					}
					$("#dataTable").append(trHTML);
					trHTML="";
				});
			}
		},
		error:function(XMLHttpRequest, textStatus, errorThrown){
			if(XMLHttpRequest.responseText.indexOf('<html')>-1){
        		$(".popup_bg").show();
        		$("#info").html(showSuccInfo("页面已过期，请重新登录!","error",loginUrl));
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
	donationPaging();
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

function formatMoney(s,flg) {
	if(flg == 1){
		if(s > 10000){
			s = s / 10000;
			if(s.toString().indexOf(".") < 0){
				return s.toString() + ".0";
			}
			return s;
		}
	}
	if (/[^0-9\.]/.test(s)){
		return "0.00";
	}
	if (s == null || s == ""){
		return "0.00";
	}
	s = s.toString().replace(/^(\d*)$/, "$1.");
	s = (s + "00").replace(/(\d*\.\d\d)\d*/, "$1");
	s = s.replace(".", ",");
	var re = /(\d)(\d{3},)/;
	while (re.test(s)){
		s = s.replace(re, "$1,$2");
	}
	s = s.replace(/,(\d\d)$/, ".$1");
	return s;
}

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