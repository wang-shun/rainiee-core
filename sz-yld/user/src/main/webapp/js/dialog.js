$(function(){	
	$("div.dialog_close").click(function(){
		$("#info").html("");
		$("div.popup_bg").hide();
		$("div.dialog").hide();
	});
	$("#cancel").click(function(){
		$("div.dialog").hide();
		$("div.popup_bg").hide();
	});
});
// 封装信息消息文本.
	function showDialogInfo(msg,type){
		return '<div class="dialog">'+
			  '<div class="title" ><a href="javascript:void(0);" class="out" onclick="closeInfo()"></a>提示</div>'+
			  '<div class="content">'+
			    '<div class="tip_information">'+
			    '<div class="'+type+'"></div>'+
			      '<div class="tips">' +
			      	'<span class="f20 gray3">'+msg+'</span>'+
			      '</div>'+
			    '</div>'+
			    '<div class="tc mt20"><a href="javascript:void(-1);" onclick="closeInfo()"  class="btn01">确定</a></div>'+
			  '</div>'+
			'</div>';
	}

	function showForwardInfo(msg,type,url){
		return '<div class="dialog"">'+
		'<div class="title" ><a href="javascript:void(0);" class="out" onclick="closeInfo()"></a>提示</div>'+
		'<div class="content">'+
		'<div class="tip_information">'+
	    '<div class="'+type+'"></div>'+
	      '<div class="tips">' +
	      	'<span class="f20 gray3">'+msg+'</span>'+
	      '</div>'+
	     '</div>'+
	     ' <div class="tc mt20"><a href="'+url+'" class="btn01">确 定</a><a onclick="closeInfo()" class="btn01 btn_gray ml20">取 消</a></div> '+
	   ' </div>'+
	'</div>';
	}
	
	function showSuccInfo(msg,type,url){
		return '<div class="dialog">'+
		  '<div class="title" ><a href="javascript:void(0);" class="out" onclick="closeInfo()"></a>提示</div>'+
		  '<div class="content">'+
		  '<div class="tip_information">'+
		    '<div class="'+type+'"></div>'+
		      '<div class="tips">' +
		      	'<span class="f20 gray3">'+msg+'</span>'+
		      '</div>'+
		    '</div>'+
	     ' <div class="tc mt20"><a href="'+url+'" class="btn01">确 定</a></div> '+
	   ' </div>'+
	'</div>';
	}
	
	function closeInfo(){
		$("#info").html("");
		$("div.popup_bg").hide();
	}

	function showInfo(msg,type){
		return '<div class="dialog">'+
		  '<div class="title" ><a href="javascript:void(0);" class="out" onclick="closeInfo()"></a>提示</div>'+
		  '<div class="content">'+
		  '<div class="tip_information">'+
		    '<div class="'+type+'"></div>'+
		      '<div class="tips">' +
		      	'<span class="f20 gray3">'+msg+'</span>'+
		      '</div>'+
		    '</div>'+
			'<div class="tc mt20"><a href="javascript:void(-1);" onclick="closeDiv()"  class="btn01">确定</a></div>'+
			'</div>'+
			'</div>';
	}

	function closeDiv(){
		$("#info").html("");
		$("div.popup_bg").hide();
		var list=parent.art.dialog.list;for(var i in list)list[i].close();
	}
	
	function displayNoneDiv(id){
		$("#"+id).css('display','none');
		$("div.popup_bg").hide();
	}
	
	
	//富友绑卡页面使用 lingyuanjie 20160514
	// 封装信息消息文本
	function showDialogInfoReload(msg,type){
		return '<div class="dialog">'+
			  '<div class="title" ><a href="javascript:void(0);" class="out" onclick="closeInfoReload()"></a>提示</div>'+
			  '<div class="content">'+
			    '<div class="tip_information">'+
			    '<div class="'+type+'"></div>'+
			      '<div class="tips">' +
			      	'<span class="f20 gray3">'+msg+'</span>'+
			      '</div>'+
			    '</div>'+
			    '<div class="tc mt20"><a href="javascript:void(-1);" onclick="closeInfoReload()"  class="btn01">确定</a></div>'+
			  '</div>'+
			'</div>';
	}
	//关闭弹层，刷新当前页
	function closeInfoReload(){
		$("#info").html("");
		$("div.popup_bg").hide();
		window.location.reload(); //不同处 -> 关闭弹窗后刷新当前页
	}
	
	