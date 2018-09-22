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
			  '<div class="title"><a href="javascript:closeInfo();" class="out"></a>提示</div>'+
			  '<div class="content">'+
		    	'<div class="tip_information"> '+
		    		'<div class="'+type+'"></div>'+
		    		'<div class="tips">'+
		    			'<span class="f20 gray3">'+msg+'</span>'+
		    		'</div>'+
		    	'</div>'+
		    	'<div class="tc mt20"><a href="javascript:closeInfo();" class="btn01">确定</a></div> '+
		      '</div>'+
		      '</div>';
	}

	function showForwardInfo(msg,type,url){
		return '<div class="dialog w510"  style="margin:-150px 0 0 -255px;">'+
		'<div class="dialog_close fr" onclick="closeInfo()"><a></a></div>'+
	    '<div class="con clearfix">'+
	     ' <div class="d_'+type+' fl"></div>'+
	      '<div class="info fr"><p class="f20 gray33">'+msg+'</p></div>'+
	     ' <div class="dialog_btn"><a href="'+url+'" class="btn btn01">确 定</a><a onclick="closeInfo()" class="btn btn05">取 消</a></div> '+
	   ' </div>'+
	'</div>';
	}
	
	function showSuccInfo(msg,type,url){
		return '<div class="dialog">'+
		'<div class="title"><a href="javascript:closeInfo();" class="out"></a>提示</div>'+
	    '<div class="content">'+
	    	'<div class="tip_information"> '+
	    		'<div class="'+type+'"></div>'+
	    		'<div class="tips">'+
	    			'<span class="f20 gray3">'+msg+'</span>'+
	    		'</div>'+
	    	'</div>'+
	    	'<div class="tc mt20"><a href="'+url+'" class="btn01">确定</a></div> '+
	    '</div>'+
	    '</div>';
	}
	
	function closeInfo(){
		$("#info1").html("");
		$("div.popup_bg").hide();
	}
	