var currentPage = 1;
var pageSize = 10;
var pageCount = 1;

/**
 * 垫付债券Ajax分页
 */
function advanceBondPaging(){	
	var ajaxUrl = $("#dfzqUrl").val();	
	var dataParam = {"currentPage" : currentPage, "pageSize" : pageSize};
	
	$.ajax({
		type:"post",
		dataType:"json",
		url:ajaxUrl,
		data:dataParam,
		success:function(returnData){
			//垫付债权分页
			//移除table中的所有行
			$("#dataTable tr").empty();
			var trHTML = "<tr class='til'><td align='center'>序号</td><td align='center'>借款标题</td>" +
					"<td align='center'>用户名</td><td align='center'>垫付时间</td><td align='center'>垫付金额(元)</td>" +
					"<td align='center'>垫付返回金额(元)</td><td align='center'>状态</td><td align='center'>合同</td></tr>";
			$("#dataTable").append(trHTML);		
			trHTML="";
			//分页信息
			if(returnData.dfzqList!=null){
				$("#pageContent").html(returnData.pageStr);
			}else{
				$("#pageContent").empty();
			}
			pageCount = returnData.pageCount;
			//分页点击事件
			$("a.page-link").click(function(){
				pageParam(this,1);
			});
			if(null == returnData.dfzqList ){
				$("#dataTable").append("<tr><td colspan='8' align='center'>暂无数据</td></tr>");
			}else if(returnData.dfzqList.length > 0){
				var rAssests = returnData.dfzqList;
				var i=1;
				$.each(rAssests, function (n, value) {
					var title="";
					if(value.title.length>10){
						title=value.title.substr(0,10)+"...";
					}else{
						title=value.title;
					}
					var userName="";
					if(value.userName.length>10){
						userName=value.userName.substr(0,10)+"...";
					}else{
						userName=value.userName;
					}
					//合同地址拼接
					var htmlHT = "";
					if(isSaveContract)
					{
						var htUrl = $("#htIndexUrl").val();
						htmlHT += "<a target='_blank' href='"+ htUrl +"?df=df&id="+value.F02+"' class='blue'>合同</a>";
					}
					else
					{
						var htUrl = $("#htIndexUrl").val();
						htmlHT += "<a target='_blank' href='"+ htUrl +"?df=df&id="+value.F02+"' class='blue'>合同</a>";
					}
					trHTML +="<tr><td align='center'>"+i+"</td>" +
					"<td align='center' title='"+value.title+"'>"+title+"</td>"+
					"<td align='center' title='"+value.userName+"'>"+userName+"</td>"+
					"<td align='center'>"+timeStamp2String(value.F07)+"</td>"+
					"<td align='center'>"+fmoney(value.F05,2)+"</td>"+
					"<td align='center'>"+fmoney(value.F06,2)+"</td>"+
					"<td align='center'>"+value.state+"</td>"+
					"<td align='center'>"+htmlHT+"</td></tr>";
					$("#dataTable").append(trHTML);//在table最后面添加一行
					trHTML = "";
					i++;
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

/**
 * 担保业务明细Ajax分页
 */
function businessDetailPaging(){	
	var ajaxUrl = $("#dbyemxUrl").val();	
	var dataParam = {"currentPage" : currentPage, "pageSize" : pageSize};
	$.ajax({
		type:"post",
		dataType:"json",
		url:ajaxUrl,
		data:dataParam,
		success:function(returnData){
			//垫付债权分页
			//移除table中的所有行
			$("#dataTable tr").empty();
			var trHTML = "<tr class='til f12'><td align='center'>序号</td><td align='center'>借款标题</td>" +
					"<td align='center'>用户名</td><td align='center'>借款金额(元)</td><td align='center'>剩余期限</td>" +
					"<td align='center'>担保方式</td><td align='center'>待还金额(元)</td><td align='center'>是否逾期</td>" +
					"<td align='center'>状态</td><td align='center'>操作</td></tr>";
			$("#dataTable").append(trHTML);	
			trHTML="";
			//分页信息
			if(returnData.result!=null){
				$("#pageContent").html(returnData.pageStr);
			}else{
				$("#pageContent").empty();
			}
			pageCount = returnData.pageCount;
			//分页点击事件
			$("a.page-link").click(function(){
				pageParam(this,2);
			});
			if(null == returnData.result ){
				$("#dataTable").append("<tr><td colspan='10' align='center'>暂无数据</td></tr>");
			}else if(returnData.result.length > 0){
				var rAssests = returnData.result;
				var i=1;
				$.each(rAssests, function (n, value) {
					var loanMoney;
					if(value.F02.length>4){
						title=value.F02.substr(0,4)+"...";
					}else{
						title=value.F02;
					}
					if(value.F06=="YJQ"||value.F06=="HKZ"||value.F06=="YDF"||value.F06=="YZR"){
						loanMoney=value.F03-value.F14;
					}else{
						loanMoney=value.F03;
					}
					var userName = "";
					if(value.userName.length>4){
						userName=value.userName.substr(0,4)+"...";
					}else{
						userName=value.userName;
					}
					var aHTML="";
					if(value.F19!='F'&&value.F06=='HKZ'){
						aHTML="<a style=\"color: red;cursor: pointer;\" onclick=\"Dfzq("+value.jkbId+","+(value.F15-value.F16+1)+")\">垫付</a>";
					}
					trHTML +="<tr><td align='center'>"+i+"</td>" +
					"<td align='center' title='"+value.F02+"'><div style='width:60px;' class='text_hide'>"+title+"</div></td>"+
					"<td align='center' title='"+value.userName+"'><div style='width:50px;' class='text_hide'>"+userName+"</div></td>"+
					"<td align='center'>"+fmoney(loanMoney,2)+"</td>"+
					"<td align='center'>"+value.F16+"/"+value.F15+"</td>"+
					"<td align='center'>"+value.dbfs+"</td>"+
					"<td align='center'>"+fmoney(value.dhbj,2)+"</td>"+
					"<td align='center'>"+value.isYQ+"</td>"+
					"<td align='center'>"+value.state+"</td>"+
					"<td align='center'>"+aHTML+"</td></tr>";
					$("#dataTable").append(trHTML);//在table最后面添加一行
					trHTML = "";
					i++;
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
	if(liId==1){
		advanceBondPaging();
	}else if(liId==2){
		businessDetailPaging();
	}
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