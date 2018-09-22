$(function(){
	$("#checkAll").click(function(){
		if($(this).attr("checked")){
			$("input:checkbox[name='letterId']").attr("checked",true);
		}else{
			$("input:checkbox[name='letterId']").attr("checked",false);
		}
	});
	$("input:checkbox[name='letterId']").click(function(){
		if("checked"!=$(this).attr("checked")){
			$("#checkAll").attr("checked",false);
		}else{
			var c1 = $("input:checkbox[name='letterId']:checked").length;
			var c2 = $("input:checkbox[name='letterId']").length;
			if(c1==c2){
				$("#checkAll").attr("checked",true);
			}
		}
	});
	/*$("select[name='status']").change(function(){
		//window.location.href = url_self+"?status="+$(this).find("option:selected").val();
		currentPage = 1;
		initData();
	});*/
	
	/*$("#status").selectlist({
		zIndex: 15,
		width: 105,
		optionHeight: 28,
		height: 28,
		onChange: function(){
			currentPage = 1;
			initData();
		}
	});*/
	
	$("a.out").click(function(){
		$("div.dialog").hide();
		$("div.popup_bg").hide();
		$("#icon_type").attr("class","doubt");
		initData();
	});
	$("#cancel").click(function(){
		$("div.dialog").hide();
		$("div.popup_bg").hide();
		$("#icon_type").attr("class","doubt");
		initData();
	});
	/*$("#ok").click(function(){
		var form = document.forms[0];
		form.action = url_self;
		form.submit();
	});*/
	initData();
});

function searchForStatus(obj,status){
	$("input[name='status']").val(status);
	$("a.searchForStatus").attr("class","searchForStatus");
	$(obj).addClass("cur");
	currentPage = 1;
	initData();
}


function bindEvent(){
	$("#close").click(function(){
		$("div.dialog").hide();
		$("div.popup_bg").hide();
		$("#icon_type").attr("class","doubt");
		initData();
	});
	$("#cancel").click(function(){
		$("div.dialog").hide();
		$("div.popup_bg").hide();
		$("#icon_type").attr("class","doubt");
		initData();
	});
}
function openLetter(obj, status, id){
	show('_'+id);
	if("WD"==status){
		$.post(url_update,{id:id},function(result){
			//判断用户会话是否有效
			if(result.msg != "undefined" && result.msg !=null && result.msg != ""){
				$(".popup_bg").show();
        		$("#info").html(showSuccInfo(result.msg,"error",loginUrl));
        		return;
			}
			
			result = parseInt(result);
			if(1==result){
				$(obj).attr("onclick","openLetter(this,'YD', '"+id+"')");
				$(obj).find("li.ico span").attr("class","read");
				$(obj).find("li.til").removeClass("bold gray3");
				$(obj).prev().find("input[name='letterId']").attr("stu","YD");
				var unread = parseInt($("#unread").text());
				unread = unread-1;
				$("#unread").text(unread);
				if(unread>99){
					unread="99+";
				}
				$("span.letter").text(unread);
			}
		},'json');
	}
}


var icount = 0;
var jcount = 0;
function delAll(){
	if($("input:checkbox[name='letterId']:checked").length<=0){
		$("#inf0Div").html("<p class='f20 gray33'>请选择要删除的数据！</p>");
		$("#btnHtml").html('<a href="javascript:void(0)" class="btn01 ml20" id="close">确定</a>');
		$("div.dialog").show();
		$("div.popup_bg").show();
		bindEvent();
		return ;
	}
	$("#inf0Div").html("<p class='f20 gray33'>确定要删除选中的站内信吗？</p>");	
	$("div.dialog").show();
	$("div.popup_bg").show();
	$("#btnHtml").html('<a href="javascript:void(0)" id="ok" class="btn01" onclick="delMsg();return false">确定</a><a href="javascript:void(0)" class="btn01 btn_gray ml20" id="close">取消</a>');
	bindEvent();
}

function updateYD(){
	if($("input:checkbox[name='letterId']:checked").length<=0){
		$("#inf0Div").html("<p class='f20 gray33'>请选择要标记的数据！</p>");	
		$("#btnHtml").html('<a href="javascript:void(0)" class="btn01 ml20" id="close">确定</a>');
		$("div.dialog").show();
		$("div.popup_bg").show();
		bindEvent();
		return ;
	}
	updateYdMsg();
}

function closeMsg(){
	$("input:checkbox[name='letterId']:checked").each(function(i){
		$(this).attr("checked",false);
		icount = 0;
		jcount = 0;
	});
	$("div.dialog").hide();
}
function delMsg(){
	var ids = "";
	icount = 0;
	jcount = 0;
	$("input:checkbox[name='letterId']:checked").each(function(i){
		ids += $(this).val()+";";
		if($(this).attr("stu")=="WD"){
			jcount++;
		}else{
			icount++;
		}
	});
	if(ids.length<1){
		$("#inf0Div").html("<p class='f20 gray33'>请选择要删除的数据！</p>");
		$("#btnHtml").html('<a href="javascript:void(0)" class="btn01 ml20" id="close">确定</a>');
		$("div.dialog").show();
		$("div.popup_bg").show();
		bindEvent();
		return;
	}
	ids = ids.substring(0,ids.length-1);
	$.post(url_del,{letterId:ids},function(data){
		
		//判断用户会话信息是否
		if(data== "toLogin"){
    		window.location.href=loginUrl;
		}else if(data=="success"){
			var unread = parseInt($("#unread").text());
			var read = parseInt($("#read").text());
			var wdCount = unread-jcount;
			if(wdCount<0){
				wdCount=0;
			}
			$("#unread").text(wdCount);
			$("span.letter").text((wdCount>99?"99+":wdCount));
			var readArray = ids.split(";");
			$("#read").text(read-readArray.length);
			$("#icon_type").attr("class","successful");
			$("#inf0Div").html("<p class='f20 gray33'>删除成功！</p>");
			$("#btnHtml").html('<a href="javascript:void(0)" class="btn01 ml20" id="cancel">确定</a>');
			$("div.dialog").show();
			$("div.popup_bg").show();
			bindEvent();
		}else{
			$("#inf0Div").html("<p class='f20 gray33'>删除失败！</p>");
			$("#btnHtml").html('<a href="javascript:void(0)" class="btn01 ml20" id="cancel">确定</a>');
			$("div.dialog").show();
			$("div.popup_bg").show();
			bindEvent();
		}
	});

}


function updateYdMsg(){
	var ids = "";
	icount = 0;
	jcount = 0;
	$("input:checkbox[name='letterId']:checked").each(function(i){
		ids += $(this).val()+";";
		if($(this).attr("stu")=="WD"){
			jcount++;
		}else{
			icount++;
		}
	});
	if(jcount == 0){
		$("#inf0Div").html("<p class='f20 gray33'>请选择未读的数据！</p>");
		$("#btnHtml").html('<a href="javascript:void(0)" class="btn01 ml20" id="close">确定</a>');
		$("div.dialog").show();
		$("div.popup_bg").show();
		bindEvent();
		return;
	}
	if(ids.length<1){
		$("#inf0Div").html("<p class='f20 gray33'>未选择数据,请重新选择！</p>");
		$("div.dialog").show();
		$("div.popup_bg").show();
		return;
	}
	ids = ids.substring(0,ids.length-1);
	$.post(url_updateYd,{letterId:ids},function(data){
		
		//判断用户会话信息是否
		if(data.msg != "undefined" && data.msg !=null && data.msg != ""){
			$(".popup_bg").show();
    		$("#info").html(showSuccInfo(data.msg,"error",loginUrl));
    		return;
		}
		
		if(data=="success"){
			var unread = parseInt($("#unread").text());
			var wdCount = unread-jcount;
			if(wdCount<0){
				wdCount = 0;
			}
			$("#unread").text(wdCount);
			$("span.letter").text((wdCount>99?"99+":wdCount));
			$("#icon_type").attr("class","successful");
			$("#inf0Div").html("<p class='f20 gray33'>标记成功！</p>");
			$("#btnHtml").html('<a href="javascript:void(0)" class="btn01 ml20" id="cancel">确定</a>');
			$("div.dialog").show();
			$("div.popup_bg").show();
			bindEvent();
		}else{
			$("#inf0Div").html("<p class='f20 gray33'>标记失败！</p>");
			$("#btnHtml").html('<a href="javascript:void(0)" class="btn01 ml20" id="cancel">确定</a>');
			$("div.dialog").show();
			$("div.popup_bg").show();
			bindEvent();
		}
	});

}

function initData(){
	$("#checkAll").attr("checked",false);
	$.post(url_self,{pageSize:pageSize,currentPage:currentPage,status:$("input[name='status']").val()},function(returnData){
		
		//判断用户会话信息是否
		if(returnData.msg != "undefined" && returnData.msg !=null && returnData.msg != ""){
			$(".popup_bg").show();
    		$("#info").html(showSuccInfo(returnData.msg,"error",loginUrl));
    		return;
		}
		
		$(".dataLi").remove();
		$("#pageContent").html("");
		returnData = eval("("+returnData+")");
		pageCount = returnData.pageCount;
		var letterList = eval(returnData.letterList);
		if(letterList != null && letterList.length > 0){
			$("#pageContent").html(returnData.pageStr);
			$("#dataBody").append("</div>");
			var li = "";
			for(var i = 0; i < letterList.length; i++){
				var item = letterList[i];
				li += "<li class=\"dataLi item\">";
				li += "<ul class=\"con\">";
				li += "<li class=\"mod_l\"><input name=\"letterId\" class=\"mr10\"  type=\"checkbox\" value=\""+item.id+"\" stu=\""+item.status+"\"/></li>";
				li += "<div style=\"cursor: pointer;\" onclick=\"openLetter(this,'"+item.status+"','"+item.id+"')\">";
				li += "<li class=\"mod01 ico\"><span class='"+(item.status=="YD" ? "read" : "unread")+"'></span></li>";
				li += "<li class=\"mod02 \">"+siteName+"</li>";
				li += "<li class=\"mod03 til "+(item.status=="YD" ? "" : " bold gray3")+"\" title='"+item.title+"'>"+item.title+"</li>";
				li += "<li class=\"mod_r\"><span class=\"mr20\">"+formatDate(item.sendTime)+"</span>"+formatTime(item.sendTime)+"</li>";
				li += "</div></ul>";
				li += "<div id=\"_"+item.id+"\" class=\"info b1  bot\"><div class=\"arrow\"></div>"+(item.content==null?"":item.content)+"</div>";
				li += "</li>";
			}
			$(li).appendTo($(".message_con"));

		}else{
			var li = "<li class=\"dataLi item mt20\"><div style=\"text-align:center;\">暂无数据</div></li>";
			$(li).appendTo("#dataBody");
		}
		$("a.page-link").click(function(){
    		pageParam(this);
    	});
	});
}

function pageParam(obj){
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
	initData();
}
var formatDate = function (time) {  
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
    var s = date.getSeconds();
    s = s < 10 ? ('0' + s) : s;  
    return y + '-' + m + '-' + d ;  
};

function formatTime(time){
	var date = new Date();
	date.setTime(time);
	var h = date.getHours();
    h = h < 10 ? ('0' + h) : h;  
    var mm = date.getMinutes();
    mm = mm < 10 ? ('0' + mm) : mm;
    var s = date.getSeconds();
    s = s < 10 ? ('0' + s) : s;  
    return  h +":"+mm+":"+s;  
}