$(function(){
	$("#checkAll").click(function(){
		if($(this).attr("checked")){
			$("input:checkbox[name='id']").attr("checked",true);
		}else{
			$("input:checkbox[name='id']").attr("checked",false);
		}
	});
	$("input:checkbox[name='id']").click(function(){
		if(!$(this).attr("checked")){
			$("#checkAll").attr("checked",false);
		}else{
			var c1 = $("input:checkbox[name='id']:checked").length;
			var c2 = $("input:checkbox[name='id']").length;
			if(c1==c2){
				$("#checkAll").attr("checked",true);
			}
		}
	});
	$("#delAll").click(function(){
		if($("input:checkbox[name='id']:checked").length != 0){
			$(".popup_bg").show();
			$("#info").html(showConfirmDiv("是否确认删除？",0,"batchdel"));
		}else{
			$(".popup_bg").show();
			$("#info").html(showInfo("选择要删除的行","wrong"));
			return;
		}
	});
	
	$("#updateOrder").click(function(){
		if($("input:checkbox[name='id']:checked").length != 0){
			var ids = "";
			$("input:checkbox[name='id']:checked").each(function (idx){
				if(($("input:checkbox[name='id']:checked").length-1) == idx){
					ids += $(this).val();
				}else{	
					ids += $(this).val() + ",";
				}
			})
			var dialogstr ='<div class="popup-box" style="height:250px;min-height:250px;"> <div class="popup-title-container">'+
			'<h3 class="pl20 f18">修改排序值</h3>'+
		    '<a class="icon-i popup-close2" onclick="closeInfo()"></a></div>'+
		    '<div class="popup-content-container-2" >'+
		    '<input id="orderId" type="hidden" name="orderId" value="'+ids+'"/>'+
            '<div class="p30">'+
                '<ul class="gray6">'+
					'<li class="mb10">'+
					'<span class="display-ib w200 tr mr5 fl">'+
						'<span class="red">*</span>将选中项的排序值修改为：'+
					'</span>'+
					'<input id="order" type="text" onblur="checkOrder();" onKeyUp="value=value.replace(/\\D/g,\'\')" class="text border w150 pl5 required max-length-8" maxlength="8" name="order" value=""/>'+
                       '<span  tip></span>'+
                       '<span  errortip class="" style="display: none"></span>'+
                   '</li></li></ul>'+
                  '<div class="tc f16 pt20">'+
                      '<input type="button" onclick="updateOrder();" value="确定" class="btn-blue2 btn white radius-6 pl20 pr20 "/>'+
                      '<a class="btn btn-blue2 radius-6 pl20 pr20 ml40" href="javascript:closeInfo();">取消</a>'+
                  '</div>'+
              '</div>';
			$(".popup_bg").show();
			$("#info").html(dialogstr);
		}else{
			$(".popup_bg").show();
			$("#info").html(showInfo("请先选中需要修改排序值的行","wrong"));
			return;
		}
	});
});

function delThis(id){
	if(id == null || id == ""){
		$(".popup_bg").show();
		$("#info").html(showInfo("请选择要删除的记录","wrong"));
		return;
	}
	$(".popup_bg").show();
	$("#info").html(showConfirmDiv("是否确认删除？",id,"del"));
	
}

function toConfirm(i,type){
	if(type=="del"){
		var objs = $("input:checkbox[name='id']:checked");
		objs.each(function(){
			objs.attr("checked",false);
		});
		
		var form = document.forms[0];
		form.action = del_url+"?id="+i;
		form.submit();
	}else{
		var form = document.forms[0];
		form.action = del_url;
		form.submit();
	}
}

function openUpdateTitle(){
	var articleName = $("#articleNameHidden").val();
	var articleStatus = $("#articleStatusHidden").val();
	$("#articleName").val(articleName);
	$("#articleStatus").val(articleStatus);
	
	$("#szmc").show();
	$('.sz').show();
	$("span[errortip]").hide();
	$("span[tip]").show();
}

function checkTitleName(){
	var articleName = $("#articleName").val();
	var $error = $("#errortip");
	var $tip = $("#tip");
	var pattern = new RegExp("[<>&]"); 
	if($.trim(articleName) == "" || articleName == null || articleName ==undefined){
		$error.addClass("error_tip");
		$error.html("不能为空！");
		$tip.hide();
		$error.show();
		return false;
	}else if(pattern.test(articleName)){  
		$error.addClass("error_tip");
		$error.html("存在非法字符！");
		$tip.hide();
		$error.show();
		return false;
    }else if(articleName.length > 8){
		$error.addClass("error_tip");
		$error.html("超过输入限制"+8+"，当前长度为"+articleName.length+"");
		$tip.hide();
		$error.show();
		return false;
	}else{
    	$error.removeClass("error_tip");
        $error.hide();
        $tip.show();
        return true;
    }
}


function updateName(type){
	var articleName = $("#articleName").val();
	var articleStatus = $("#articleStatus").val();
	
	if(!checkTitleName()){
		return false;
	}
	
	$.ajax({type:"post",
			dataType:"html",
			url:upateUrl,
			data:{"articleName":articleName,"articleStatus":articleStatus,"type":type},
			async: false,
			success:function(returnDate){
				  var rd = eval('(' + returnDate + ')');
				  if (rd.length > 0 && rd[0].num == 1) {
					    $("#szmc").hide();
					  	$(".popup_bg").show();
						$("#info").html(showDialogInfo(rd[0].msg,"wrong"));
						return false;
				  }
				  if(rd.length > 0 && rd[0].num == 0){
					  window.location.href= reloadUrl;
				  }
			},
			error: function(XMLHttpRequest, textStatus, errorThrown){
				$(".popup_bg").show();
				$("#info").html(showDialogInfo("系统繁忙，请稍后重试！","wrong"));
			}});
}


function checkOrder(){
	var orderId = $("#orderId").val();
	var order = $("#order").val();
	var $error = $("#order").nextAll("span[errortip]");
	var $tip = $("#order").nextAll("span[tip]");
	var myreg = /^[0-9]([0-9])*$/;
   
	if($.trim(order) == "" || order == null || order ==undefined){
		$error.addClass("error_tip");
		$error.html("不能为空！");
		$tip.hide();
		$error.show();
		return false;
	}else if(!myreg.test(order)) {
        $error.addClass("error_tip");
        $error.html("必须为整数！");
        $tip.hide();
        $error.show();
        return false;
    }else if ($.trim(order) < 0) {
        $error.addClass("error_tip");
        $error.html("不能小于0！");
        $tip.hide();
        $error.show();
        return false;
    }else if (order.length > 8) {
        $error.addClass("error_tip");
        $error.html("超过最大输入长度8！");
        $tip.hide();
        $error.show();
        return false;
    }else{
    	$error.removeClass("error_tip");
        $error.hide();
        $tip.show();
        return true;
    }
}


function updateOrder(){
	var orderId = $("#orderId").val();
	var order = $("#order").val();
	var orderTable = $("#orderTable").val();
	if(!checkOrder()){
		return false;
	}
	
	$.ajax({type:"post",
		dataType:"html",
		url:upateOrderUrl,
		data:{"orderId":orderId,"order":order,"orderTable":orderTable},
		async: false,
		success:function(returnDate){
			  var rd = eval('(' + returnDate + ')');
			  if (rd.length > 0 && rd[0].num == 1) {
				  	$(".popup_bg").show();
					$("#info").html(showDialogInfo(rd[0].msg,"wrong"));
					return false;
			  }
			  if(rd.length > 0 && rd[0].num == 0){
				  window.location.href= reloadUrl;
			  }
		},
		error: function(XMLHttpRequest, textStatus, errorThrown){
			$(".popup_bg").show();
			$("#info").html(showDialogInfo("系统繁忙，请稍后重试！","wrong"));
		}});
	
}


