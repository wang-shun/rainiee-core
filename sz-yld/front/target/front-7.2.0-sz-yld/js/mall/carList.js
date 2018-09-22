$(function(){
	$("input[name='checkAll']").click(function(){
		$("input[name='goods']").attr("checked",!$(this).attr("checked") ? false : $(this).attr("checked"));
		$("input[name='checkAll']").attr("checked",!$(this).attr("checked") ? false : $(this).attr("checked"));
		if($(this).attr("checked")){
			$("div.main_mod .bd").each(function(i,n){
				if($(this).find("input[name='goods']").val()){
					$("div.main_mod .bd").eq(i).addClass("on_bd");
				}
			});
			
		}else{
			$("div.main_mod .bd").removeClass("on_bd");
		}
		
		
		calcuteTotal();
	});
	
	$("input[name='goods']").click(function(){
		initGoodsCheck();
	});
	
	// 积分商城购物车产品名称
	 $('.order_info .title').mouseover(function(){
         $(this).children().next().show();
    }); 
	 $('.order_info .title').mouseout(function(){
         $(this).children().next().hide();
    }); 
	 
	 $("#delAll").click(function(){
		 batchDelCar();
	 });
	 
	 //初始化购物车选中
	 initGoodsCheck();
});

function initGoodsCheck(){
	var count = 0;
	$("input[name='goods']").each(function(){
		if($(this).attr("checked")!="checked"){
			$("input[name='checkAll']").attr("checked",false);
			$(this).parent().parent().parent().removeClass("on_bd");
		}else{
			$(this).parent().parent().parent().addClass("on_bd");
			count ++;
		}
	});
	if(count==$("input[name='goods']").length){
		$("input[name='checkAll']").attr("checked",true);
	}
	calcuteTotal();
}

function checkNum(obj,count,xgCount,ygCount,isCash,isScore){
	var myreg = /^[1-9]([0-9])*$/;
	if(!myreg.test($(obj).val())){
		$(obj).val(1);
	}
	if($(obj).val()*1 > count){
		$("#info").html(showDialogInfo("不能超过商品的库存数","doubt"));
		$(obj).val(1);
		return;
	}
	if(xgCount > 0 && ($(obj).val()*1+ygCount) > xgCount){
		$("#info").html(showDialogInfo("不能超过商品的限购数："+xgCount+"<br/>已购数量："+ygCount,"doubt"));
		$(obj).val(1);
		return;
	}
	if(isCash=="yes"){
		var singleAmount = parseFloat($(obj).parent().parent().next().next().attr("at"));
		$(obj).parent().parent().next().next().html(fmoney(singleAmount*($(obj).val()),2));
	}
	if(isScore=="yes"){
		var singleScore = $(obj).parent().parent().next().next().next().attr("at")*1;
		$(obj).parent().parent().next().next().next().html(singleScore*$(obj).val());
	}
	calcuteTotal();
}

function addNum(obj,count,limitCount,isCash,isScore,ygCount){
	if($(obj).prev().val()*1 >= count){
		$("#info").html(showDialogInfo("不能超过商品的库存数","doubt"));
		return;
	}
	if(limitCount > 0 && ($(obj).prev().val()*1+ygCount) >= limitCount){
		$("#info").html(showDialogInfo("不能超过商品的限购数："+limitCount+"<br/>已购数量："+ygCount,"doubt"));
		return;
	}
	var num = $(obj).prev().val()*1+1;
	$(obj).prev().val($(obj).prev().val()*1+1);
	if($("#allowsBalance").val()=="true" && isCash=="yes"){
		var singleAmount = parseFloat($(obj).parent().parent().parent().attr("amount"));
		$(obj).parent().parent().parent().find("li[name='lsAmount']").html(fmoney(singleAmount*num,2));
	}
	if(isScore=="yes"){
		var singleScore = $(obj).parent().parent().parent().attr("score")*1;
		$(obj).parent().parent().parent().find("li[name='lsScore']").html(singleScore*num);
	}
	calcuteTotal();
}

function minNum(obj,isCash,isScore){
	if($(obj).next().val()*1 <= 1){
		$("#info").html(showDialogInfo("购买数量不能小于0","doubt"));
		return;
	}
	var num = $(obj).next().val()*1-1;
	$(obj).next().val(num);
	
	if(isCash=="yes"){
		var singleAmount = parseFloat($(obj).parent().parent().parent().attr("amount"));
		$(obj).parent().parent().parent().find("li[name='lsAmount']").html(fmoney(singleAmount*num,2));
	}
	if(isScore=="yes"){
		var singleScore = $(obj).parent().parent().parent().attr("score")*1;
		$(obj).parent().parent().parent().find("li[name='lsScore']").html(singleScore*num);
	}
	calcuteTotal();
}

function toPay(){
	if($("div.on_bd").length <= 0){
		$("#info").html(showDialogInfo("请选择商品！","doubt"));
		return;
	}
	
	var sNum = 0;
	var bNum = 0;
	var type = "";
	
	$("div.on_bd select[name='type'] option:selected").each(function(){
		if($(this).val()=="score"){
			sNum++;
		}
		if($(this).val()=="balance"){
			bNum++;
		}
		type = $(this).val();
		
	});
	
	if(type == "score" && parseInt($("#totalScore").html()) > parseInt($("#availScore").val())){
		$("#info").html(showDialogInfo("您的积分不足！","doubt"));
		return;
	}
	
	if(sNum > 0 && bNum > 0){
		$("#info").html(showDialogInfo("支付方式不一致，请重新选择","doubt"));
		return;
	}
	var jsonObj = "[";
	var errC = 0;
	var errMsg = "";
	$("div.on_bd ul").each(function(){
		if($(this).attr("count")*1<$(this).find("input[name='num']").val()*1){
			errC++;
			errMsg = "商品："+$(this).attr("goodName")+"。购买数量大于库存数，请重新选择！";
		}else{
			jsonObj +="{goodsId:"+$(this).find("input[name='goods']").attr("goodsId")+",goodsNum:"+$(this).find("input[name='num']").val()+",id:"+$(this).find("input[name='goods']").val()+"},";
		}
	});
	if(errC>0){
		$("#info").html(showDialogInfo(errMsg,"doubt"));
		return;
	}
	jsonObj = jsonObj.substring(0,jsonObj.length-1)+"]";
	$("input[name='payType']").val(type);
	$("input[name='goodsList']").val(jsonObj);
	$("form").submit();
}

function calcuteTotal(){
	$("#totalNum").html($("div.on_bd").length);
	var totalAmount = 0;
	var totalScore = 0;
	$("div.on_bd ul").each(function(){
		var isScore = $(this).attr("isScore");
		var isCash = $(this).attr("isCash");
		var num = $(this).find("input[name='num']").val();
		var singleScore = isScore == "yes" ? $(this).attr("score") : 0;
		var singleAmount = isCash == "yes" ? $(this).attr("amount") : 0;
		if(num){
			totalAmount += parseFloat(singleAmount)*num;
			totalScore += singleScore*1*num;
		}
	});
	$("#totalAmount").text(fmoney(totalAmount,2));
	$("#totalScore").text(totalScore);
}

function delCar(id){
	$("#info").html(showConfirmDiv("是否删除购物车中的该商品？",id,"question"));
	
}

function batchDelCar(){
	if($("div.on_bd").length <= 0){
		$("#info").html(showDialogInfo("请选择要删除的商品","doubt"));
		return;
	}
	var id = "";
	$("div.on_bd ul").each(function(){
		id +=$(this).find("input[name='goods']").val()+",";
	});
	id = id.substring(0,id.length-1);
	$("#info").html(showConfirmDiv("是否删除购物车中已选商品？",id,"question"));
}

function toConfirm(param,type){
	$("div.popup_bg").hide();
	$("div.dialog").hide();
	del(param);
}

function selectCar(id){
	var ids = id.split(",");
	for(var a = 0;a<ids.length;a++){
		$("div.bd").each(function(){
			if(ids[a]==$(this).attr("goodId")){
				$(this).remove();
			}
		});
	}
	if($("div.bd").length==0){
		location.href = location.href;
	}
	
	$("#carCount").text($("div.bd").length > 0 ? $("div.bd").length : "");
}

function del(param){
	$.ajax({
		type:"post",
		dataType:"text",
		url:$("#delCarUrl").val(),
		data:{"ids":param},
		async: false,
		success:function(returnData){
			selectCar(param);
			calcuteTotal();
		},
		error: function(XMLHttpRequest, textStatus, errorThrown){
			alert(textStatus);
		}
	});
	
}

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