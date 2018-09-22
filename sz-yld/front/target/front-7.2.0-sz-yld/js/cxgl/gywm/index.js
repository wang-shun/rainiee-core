//tab切换
function setTab(name,cursel,n){
	var hover="cur";
	for(var i=1;i<=n;i++){
		var menu=$("#"+name+i);
		if(i==cursel){
			menu.addClass(hover);
		}else{
			menu.removeClass(hover);
		}
	}
}

var ajax_type;   //文章类型
var module_type; //模块类型
//关于我们模块
function initGywmData(type){
	$("."+type).addClass("cur");
	$.ajax({
		type: "post",
		dataType: "html",
		url: _gywmUrl,
		data: {articType:type,pageSize:pageSize,currentPage:currentPage},
		success: function (data) {
			initContentDiv();
			if (data) {
				module_type = "gywm";
				ajax_type =type;
				if("GSJJ" == type || "ZXNS" == type || "LXWM" == type){
					$("#"+type+"_DIV").html(data)
				}

				if("GLTD" == type || "ZJGW" == type) {
					data = eval("("+data+")");
					var divStr="";
					var ulStr = $("<ul></ul>");
					for(var i=0;i<data.artics.length;i++){
						divStr = $("<li><div class='pic'><img src='"+data.artics[i].F09+"'/> </div> "+
						"<div class='name'> <strong> "+data.artics[i].F06 +"</strong> </div>"+
						"<div class='introduce' id='connent'> "+ data.artics[i].content +"  </div></li>");
						divStr.appendTo(ulStr);
					}
					ulStr.appendTo("#"+type+"_DIV");
					pageCount = data.pageCount;
					$("#pageContent").html(data.pageStr);
					$("a.page-link").click(function(){
						pageParam(this, type);
					});
				}

				if("HZHB" == type){
					data = eval("("+data+")");
					var divStr="";
					var ulStr = $("<ul></ul>");
					for(var i=0;i<data.artics.length;i++){

						divStr = $("<li><div class='til'> <span> "+data.artics[i].F04+"</span> <div class='fr'>"+data.artics[i].lastUPdateTime+"</div> </div> "+
							"<div class='con'> <div class='pic'> <a target='_blank' href='/gywm/hzhb/"+data.artics[i].F01+".html'>"+
							" <img src='"+data.artics[i].pic+"' alt='"+data.artics[i].F04+"'> </a> </div>"+
							" <div class='introduce' id='connent'><br>"+data.artics[i].F08+"</div> </div>"+
						    "<div class='link'>链接地址：<a target='_blank' href='"+data.artics[i].F05+"' class='mr100'> "+data.artics[i].F05+"</a>"+data.artics[i].F07+"</div></li>" );
						divStr.appendTo(ulStr);
					}
					ulStr.appendTo("#"+type+"_DIV");
					pageCount = data.pageCount;
					$("#pageContent").html(data.pageStr);
					$("a.page-link").click(function(){
						pageParam(this, type);
					});
				}

				if("YJFK" == type){
					data = eval("("+data+")");
					var divStr="";
					var ulStr = $("<ul></ul>");
					for(var i=0;i<data.artics.length;i++){
						divStr = $("<li class='item'><div class='hd f16 gray3'>"+data.artics[i].userName+"："+ data.artics[i].F03+
							"<span class='gray9 ml10 f14'>"+formatTime(data.artics[i].F04)+"</span></div>"+
							"<div class='bd'><i class='arrow'></i>"+_site_name+"："+data.artics[i].F05+"</div> </li>");
						divStr.appendTo(ulStr);
					}
					ulStr.appendTo("#"+type+"_DIV");
					pageCount = data.pageCount;
					$("#pageContent").html(data.pageStr);
					$("a.page-link").click(function(){
						pageParam(this, type);
					});
				}
			}
		}
	});
}

function initContentDiv(){
	$("#content_id").html("");
	$("#pageContent").html("");
	var gsjjDiv = "<div id='GSJJ_DIV' class='info_cont' style='word-break: break-all;'></div>";
	var gltdDiv = "<div id='GLTD_DIV' class='management_list'></div>";
	var zjgwDiv = "<div id='ZJGW_DIV' class='management_list'></div>";
	var zxnsDiv = "<div id='ZXNS_DIV' class='management_list'></div>";
	var lxwmDiv = "<div id='LXWM_DIV' class='management_list'></div>";
	var yjfkDiv = "<div id='YJFK_DIV' class='feedback_content'></div>";
	var hzhbDiv = "<div id='HZHB_DIV' class='partner_content'></div>";
	$("#content_id").html(gsjjDiv + gltdDiv + zjgwDiv + zxnsDiv + lxwmDiv + yjfkDiv + hzhbDiv);
}

//安全保障模块
function initAqbzData(type){
	$("."+type).addClass("cur");
	$.ajax({
		type: "post",
		dataType: "html",
		url: aqbzUrl,
		data: {articType:type},
		success: function (data) {
			$("#content_id").html("");
			if (data) {
				$("#content_id").html(data)
			}
		}
	});
}

//投资攻略模块
function initTzglData(type){
	$("."+type).addClass("cur");
	$.ajax({
		type: "post",
		dataType: "html",
		url: _tzglUrl,
		data: {articType:type,pageSize:pageSize,currentPage:currentPage},
		success: function (data) {
			$("#content_id").html("");
			$("#pageContent").html("");
			if (data) {
				ajax_type =type;
				module_type = "tzgl";
				if("XSZY" != type ){
					$("#content_id").html("<div class='info_cont ny_newslist'>"+data+"</div>")
				}

				if("XSZY" == type ) {
					data = eval("("+data+")");
					var divStr="";
					var ulStr = $("<div class='online_info'> <div class='list'> <ul></ul></div></div>");
					for(var i=0;i<data.artics.length;i++){
						divStr = $("<li><a target='_blank' href='/bzzx/xszy/"+data.artics[i].F01+".html'><span class='lbt'>"+data.artics[i].F06 +
							"</span><span class='gray9 ml10 f14'>"+formatTime(data.artics[i].F13)+"</span></a></li>");
						divStr.appendTo(ulStr);
					}
					$("#content_id").html(ulStr)
					pageCount = data.pageCount;
					$("#pageContent").html(data.pageStr);
					$("a.page-link").click(function(){
						pageParam(this, type);
					});
				}
			}
		}
	});
}

//帮助中心模块
function initHelpData(type){
	$("."+type).addClass("cur");
	$.ajax({
		type: "post",
		dataType: "html",
		url: _helpUrl,
		data: {articType:type},
		success: function (data) {
			$("#content_id").html("");
			if (data) {
				data = eval("("+data+")");
				if(data.questionObj != null){
					$("#qtype_tmpl").tmpl(data).appendTo('#content_id');
					questionTypeClick(data.questionObj.length); //绑定问题类型点击事件
					questionClick(); //绑定问题的点击事件
				}
			}
		}
	});
}

//绑定问题的点击事件
function questionClick(){
	$(".problem_type_con dt").click(function(){//点击til
		$(this).next().toggle();;//当前的下一个显示或隐藏
		if($(this).hasClass("up")){//如果有UP的样式
			$(this).removeClass("up");//就移除
		}else{
			$(this).addClass("up");//如果没有就加上
		}
	});
}

//绑定问题类型点击事件
function questionTypeClick(qtypeLength){
	$(".qtype_li").click(function(){
		setQtypeTab('type',$(this).attr("index"), qtypeLength);
	});
}

function setQtypeTab(name,cursel,n){
	var hover="hover";
	for(var i=1;i<=n;i++){
		var menu=$("#"+name+i);
		var con=document.getElementById("con_"+name+"_"+i);
		if(i==cursel){
			menu.addClass(hover);
		}
		else{
			menu.removeClass(hover);
		}

		if (con)con.style.display=i==cursel?"block":"none";
	}
}

//最新动态模块
function initZxdtData(type){
	$("."+type).addClass("cur");
	$.ajax({
		type: "post",
		dataType: "html",
		url: _zxdtUrl,
		data: {articType:type,pageSize:pageSize,currentPage:currentPage},
		success: function (data) {
			if (data) {
				ajax_type =type;
				module_type = "zxdt";
				$("#content_id").html("");
				$("#pageContent").html("");

				data = eval("("+data+")");
				if('MTBD' == type || 'SHZR' == type){
					$("#tmpl_one").tmpl(data).appendTo('#content_id');
				}else if('WZGG' == type){
					$("#tmpl_two").tmpl(data).appendTo('#content_id');
				}else{
					$("#tmpl_thr").tmpl(data).appendTo('#content_id');
				}

				pageCount = data.pageCount;
				$("#pageContent").html(data.pageStr);
				$("a.page-link").click(function(){
					pageParam(this, type);
				});
			}else{
				initZxdtData("WZGG")
			}
		}
	});
}

function pageParam(obj, type){
	if($(obj).hasClass("cur")){
		return false;
	}
	currentPage = parseInt(currentPage);
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
	if("tzgl" == module_type){
		initTzglData(type);
	}
	if("gywm" == module_type){
		initGywmData(type);
	}
	if("zxdt" == module_type){
		initZxdtData(type);
	}
}

function toAjaxPage(){
	if("tzgl" == module_type){
		initTzglData(ajax_type);
	}
	if("gywm" == module_type){
		initGywmData(ajax_type);
	}
	if("zxdt" == module_type){
		initZxdtData(ajax_type);
	}
}

var formatTime = function (time) {
	if(time==null){
		return "--";
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
	var mm = date.getMinutes();
	mm = mm < 10 ? ('0' + mm) : mm;
	return y + '-' + m + '-' + d +" " + h + ":" + mm;
}
