$(function() {
	$("#yjtb_close").click(function() {
		$("div.popup_bg").hide();
		$("#yjtb_dialog").hide();
	});

	$("#yjtb").click(function() {
		$("div.popup_bg").show();
		$("#yjtb_dialog").show();
	});

	$("#zztb_close").click(function() {
		$("div.popup_bg").hide();
		$("#zztb_dialog").hide();
		$(".form1")[0].reset();
		var register = $("#sub-btn");
		register.addClass("btn_gray btn_disabled");
		$("#sub-btn").attr('disabled',true);
	});

	$("#zztb").click(function() {
		$("div.popup_bg").show();
		if(count>=3){
			//$("#info").html(showDialogInfo(,"prefect"));
			$("#info").html(showForwardInfo("您已达到新增自动投资规则数量最大值，<br/>请删除/关闭已有规则，再重新新增规则.","doubt","javascript:closeDiv();"));
		}else{
			$("#errorjk").hide();
			$("p[errortip]").hide();
			$("p[tip]").show();
			$(".mctbje").show();
			$("#zztb_dialog").show();
		}
	});
	
	$(".reset").click(function(){
		$(':input','#form1') 
		 .not(':button, :submit, :reset, :hidden, :radio, .select_style')
		 .val('')
		 .removeAttr('checked')
		 .removeAttr('selected');
		$("p[errortip]").hide();
		$("p[tip]").show();
	});

	$("input[name='mctbje']").bind("change", function() {
		if ($(this).val() == 0) {
			$(".mctbje").hide();
		} else {
			$(".mctbje").show();
		}
	});
});

function vailJkqx() {
	var start = $("input[name='jkqxStart']").val();
	var end = $("input[name='jkqxEnd']").val();
	if (parseInt(start) > parseInt(end)) {
		$("#errorjk").html("借款期限的起始月不能大于截止月");
		$("#errorjk").css("color", "red");
		$("#errorjk").show();
	} else {
		existRule(start,end);
	}

}

function existRule(start,end){
	$.ajax({
		type : "post",
		url : postUrl,
		data : {
			start : start,
			end:end
		},
		async : false,
		dataType : "html",
		success : function(data) {
			data = eval("(" + data + ")");
			if (data.result > 0) {
				$("#errorjk").html("已存在相同的借款期限，请重新选择或删除已有规则");
				$("#errorjk").css("color", "red");
				$("#errorjk").show();
			} else {
				$("#errorjk").hide();
			}
		}
	});
	
	
}

function updateZdtb(status,id){
	var msg = "";
	if(status=='QY'){
		msg = "您确定要开启此自动投资规则吗？";
	}else{
		msg = "您确定要关闭此自动投资规则吗？";
	}
	$(".popup_bg").show();
	$("#info").html(showForwardInfo(msg,"question","javascript:update('"+status+"',"+id+")"));
}

function update(status,id){
	$.ajax({
		type : "post",
		url : updateUrl,
		data : {
			status : status,
			id:id
		},
		async : false,
		dataType : "html",
		success : function(data) {
			data = eval("(" + data + ")");
			if (data.result == 'success') {
				location.href = location.href;
			} else if (data.result == 'exceed'){
				$(".popup_bg").show();
				$("#info").html(showDialogInfo("您已开启的自动投资规则数量最大值，请删除/关闭已有规则，再重新开启规则","error"));
			}else {
				$(".popup_bg").show();
				$("#info").html(showDialogInfo("操作失败","error"));
			}
		}
	});
}

function deleteZdtb(id){
	$(".popup_bg").show();
	$("#info").html(showForwardInfo("确定删除此自动投资规则吗？","question","javascript:deleteZdtbC("+id+")"));
}

function deleteZdtbC(id){
	$.ajax({
		type : "post",
		url : delUrl,
		data : {
			id:id
		},
		async : false,
		dataType : "html",
		success : function(data) {
			data = eval("(" + data + ")");
			if (data.result == 'success') {
				location.href = location.href;
			} else {
				showDialogInfo("操作失败","error");
			}
		}
	});
}

function onSubmit() {
	//判断年化利率范围
	var rateStart = $("input[name='rateStart']");
	var rateTest = /^\d*(\d|(\.[0-9]{1,2}))$/;
	if(!rateTest.test(rateStart.val())){
		rateStart.nextAll("p[errortip]").addClass("red").html("不能小于0，且只能有两位小数").show();
		rateStart.nextAll("p[tip]").hide();
		return false;
	}
	
	vailJkqx();
	if ($("#errorjk").css("display") != "none") {
		return false;
	}
}
