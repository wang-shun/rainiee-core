$(function(){
	
	// 积分商城购物车产品名称
	 $('.order_info .title').mouseover(function(){
         $(this).children().next().show();
    }); 
	 $('.order_info .title').mouseout(function(){
         $(this).children().next().hide();
    }); 
	 
	 $("input[name='setDefault']").click(function(){
		 if($(this).attr("checked")){
			 $("input[name='F08']").val("yes");
		 }else{
			 $("input[name='F08']").val("no");
		 }
	 });
	 $("#ok").click(function(){
			 //网关需要验证交易密码
			var isOpenWithPds =  $("#isOpenWithPsd").val();
			if("true" == isOpenWithPds){
				keleyidialog();
			}else{
				buy();
			}
		});
	 initAddressData();
});

function addAddress(){
	$("#address_title").html("新增收货地址");
	$(".text_list input[type='text']").val("");
	$(".text_list select").val("");
	$("input[name='F01']").val("");
	var region = "<select name=\"sheng\" id=\"sheng\" class=\"required select6\"></select>";
	region += "<p errortip class='prompt' style='display: none'></p>";
	$("#region").html(region);
	initRegion();
	$("#address").show();
	$(".dialog_shipping_address").show();
	$("div.popup_bg").show();
}
function closeAddress(){
	$("#address").hide();
	$(".dialog_shipping_address").hide();
	$("div.popup_bg").hide();
	$("div.dialog").hide();
	$("p[errortip]").hide();
}

function showPayDiv(){
	if($("#addressList li.cur").length==0){
		$("#info").html(showDialogInfo("请选择收货地址","doubt"));
		return;
	}
	var addressId = $("input[name='address']:checked").val();
	if(addressId==""){
		$("#info").html(showDialogInfo("请选择收货地址","doubt"));
		return;
	}
	$("#addressId").val(addressId);
	
	var isTG= $("#isTG").val();
	var isOpenWithPds =  $("#isOpenWithPsd").val();
	if("true" == isOpenWithPds)
	{
		$("#tran_pwd").val('');
	}
	$("#buyDiv").show();
	$("div.popup_bg").show();
	$("#commNum").text($("#totalNum").text());
	var payType = $("#payType").val();
	if(payType=="score"){
		$("#needStr").text("所需积分");
		$("#price").text($("#ts").text());
	}else{
		$("#needStr").text("总价");
		$("#price").text("￥"+$("#tp").text());
	}
}
function editAddress(obj){
	
	$("#address_title").html("编辑收货地址");
	$("#region").html(regionUpdate);
	
	var radio = $(obj).find("input[name='address']");
	$("input[name='F01']").val(radio.val());
	$("input[name='F03']").val(radio.attr("receive"));
	$("input[name='F05']").val(radio.attr("address"));
	$("input[name='F06']").val(radio.attr("telphone"));
	$("input[name='F07']").val((radio.attr("code")==null || radio.attr("code")=="" || radio.attr("code")=="null") ? "": radio.attr("code"));
	$("input[name='F08']").val(radio.attr("default"));
	$("select[name='xian']").val(radio.attr("region"));
	$("#shengId").val(radio.attr("shengId"));
	$("#shiId").val(radio.attr("shiId"));
	$("#xianId").val(radio.attr("xianId"));
	initRegion();
	/*$("select[name='sheng'] option[value='"+$("#shengId").val()+"']").attr("selected",true);
	var province = $("select[name='sheng']");
	var city = $("select[name='shi']");
	var area = $("select[name='xian']");
	var city_data = null;
	var area_data = null;
	//province.html(getSelectHtml(region, shengId));
	city_data = null;
	area_data = null;
	
	var pro_val = province.val();
	city.children().remove();
	area.children().remove();
	for ( var i = 0; i < region.length; i++) {
		if (region[i].id == pro_val) {
			city_data = region[i].children;
			if (city_data.length > 0) {
				city.html(getSelectHtml(city_data,0));
				city.show();
			}
			break;
		}
	}
	if(city_data){
		var city_val = radio.attr("shiId");
		area.children().remove();
		for ( var i = 0; i < city_data.length; i++) {
			if (city_data[i].id == city_val) {
				area_data = city_data[i].children;
				if (area_data.length > 0) {
					area.html(getSelectHtml(area_data,0));
					area.show();
				}
				break;
			}
		}
	}
	$("select[name='shi'] option[value='"+$("#shiId").val()+"']").attr("selected",true);
	$("select[name='xian'] option[value='"+$("#xianId").val()+"']").attr("selected",true);*/
	if(radio.attr("default")=="yes"){
		$("input[name='setDefault']").attr("checked","checked");
	}else{
		$("input[name='setDefault']").attr("checked",false);
	}
	$("#address").show();
	$(".dialog_shipping_address").show();
	$("div.popup_bg").show();
}

function saveAddress(){
	/*$('.form1 input[type="text"]').each(function(){
		checkText($(this));
	});
	$('.form1 select').each(function(){
		checkSelect($(this));
	});
	var flag = true;
	if($("p[errortip]:visible").length > 0){
		flag = false;
	}
	*/
	if(submitVal("form1")){
		var cs = {"F01":$("input[name='F01']").val(),"F03":$("input[name='F03']").val(),"F04":$("input[name='xian']").val(),"F05":$("input[name='F05']").val(),"F06":$("input[name='F06']").val(),"F07":$("input[name='F07']").val(),"F08":$("input[name='F08']").val()};
		$.ajax({
			type:"post",
			dataType:"html",
			url:$("#saveUrl").val(),
			data:cs,
			async: false,
			success:function(returnData){
				closeAddress();
				initAddressData();
				if(returnData != "sucess"){
					$("#info").html(showDialogInfo(returnData, "doubt"));
				}
			},
			error: function(XMLHttpRequest, textStatus, errorThrown){
				$("#info").html(showDialogInfo("系统异常，请稍后重试！", "error"));
			}
		});
	}
	
}

function delAddress(id){
	$.ajax({
		type:"post",
		dataType:"html",
		url:$("#delUrl").val(),
		data:{"id":id},
		async: false,
		success:function(returnData){
			closeAddress();
			initAddressData();
			if(id == $("#shdz").attr("addressId")) {
				$("#shdz").text("");
				$("#shr").text("");
				$("#dh").text("");
			}
			if(returnData=="failed"){
				$("#info").html(showDialogInfo("删除失败！", "doubt"));
			}
		},
		error: function(XMLHttpRequest, textStatus, errorThrown){
			$("#info").html(showDialogInfo("系统异常，请稍后重试！", "error"));
		}
	});
	
}

function toConfirm(id,type){
	delAddress(id);
}
function initAddressData(){
	$.ajax({
		type:"post",
		dataType:"json",
		url:$("#addressListUrl").val(),
		data:null,
		async: false,
		success:function(returnData){
			if(returnData!=null && returnData.length>0){
				var html = "";
				for(var i = 0;i<returnData.length;i++){
					var obj = returnData[i];
					html+="<li "+(obj.isDefault=="yes" ? "class=\"cur\"" :"")+">";
					if(obj.isDefault=="yes"){
						html+="<i class=\"ico\"></i>";
						$("#shdz").text(obj.regionStr+obj.address);
						$("#shr").text(obj.receiverName);
						$("#dh").text(obj.telephone);
					}
					html+="<input type=\"radio\" shengId=\""+obj.shengId+"\" shiId = \""+obj.shiId+"\" xianId = \""+obj.xianId+"\" value=\""+obj.id+"\" address=\""+obj.address+"\" receive=\""+obj.receiverName+"\" regionStr=\""+obj.regionStr+"\" region=\""+obj.region+"\" code=\""+obj.code+"\" default=\""+obj.isDefault+"\" telphone=\""+obj.telephone+"\" name=\"address\" "+(obj.isDefault=="yes" ? "checked=\"checked\"" :"")+" />";
					html+=obj.regionStr+"<span title=\""+obj.address+"\">"+subStringLength(obj.address,20,"...")+"</span>";
					html+="<span class=\"ml10 mr20\" title=\""+obj.receiverName+"\">（";
					html+=subStringLength(obj.receiverName,4,"...");
					html+="收）</span>";
					html+="<span class='mr20'>"+obj.telephone+"</span>";
					html+="<span class='mr20'>"+(obj.code?obj.code:"")+"</span>";
					html+="<span class=\"operation\"><a href=\"javascript:void(0);\" name=\"edit\" class=\"highlight ml20 mr20\">编辑</a><a href=\"javascript:void(0);\" name=\"del\" class=\"highlight\">删除</a></span></li>";
				}
				$("#addressList").html(html);
			}
			else
			{
				//为空则清空页面显示。
				$("#addressList").html("");
			}
			
		},
		error: function(XMLHttpRequest, textStatus, errorThrown){
			$("#info").html(showDialogInfo("系统异常，请稍后重试！", "error"));
		}
	});

	// 编辑收货地址
	 $('.shipping_address li').mouseover(function(){
        $(this).find('.operation').show();
   }); 
	 $('.shipping_address li').mouseout(function(){
		  $(this).find('.operation').hide();
   }); 
	 
	 $(".shipping_address li").click(function(event){
		 $("#addressList li").removeClass("cur");
		 $("#addressList li i").remove();
		 $("input[name='address']").attr("checked",false);
		 $(this).addClass("cur");
		 var obj = $(this).find("input[name='address']");
		 obj.attr("checked","checked");
		 $("<i class=\"ico\"></i>").appendTo(this);
		 $("#shdz").text(obj.attr("regionStr")+obj.attr("address"));
		 $("#shdz").attr("addressId",obj.val());
		 $("#shr").text(obj.attr("receive"));
		 $("#dh").text(obj.attr("telphone"));
	 });
	 
	 $("a[name='edit']").click(function(event){
		 event.stopPropagation();
		 editAddress($(this).parent().parent());
	 });
	 $("a[name='del']").click(function(event){
		 event.stopPropagation();
		 $("#info").html(showConfirmDiv("确定删除该收货地址吗？",$(this).parent().parent().find("input[name='address']").val(),"question"));
	 });
}

function keleyidialog() {
	var tran_pwd = $("#tran_pwd").val();
    if(!tran_pwd){
    	$("#tran_pwd").next("p").html("交易密码不能为空").css("fontSize","14px").show();
    }else{
    	var sPwd= RSAUtils.encryptedString(key,tran_pwd);
	    $("#tranPwd").val(sPwd);
    	var data={"password":sPwd,"id":$("#accountId").val()};
    	var flag = true;
		$.ajax({
			type:"post",
			dataType:"html",
			async:false,
			url:$("#checkUrl").val(),
			data:data,
			success:function(data){
				if(data != "01"){
					$("#tran_pwd").next("p").html(data).css("fontSize","14px").show();
					flag = false;
				}
			}
		});
		if(flag){
	    	$("#tran_pwd").next("p").hide();
	    	$("div.dialog").hide();
			$("div.popup_bg").hide();
		    var sPwd= RSAUtils.encryptedString(key,tran_pwd);
		    $("#tranPwd").val(sPwd);
		    //提交
		    buy();
		}
    }
}

function buy(){
	var addressId = $("input[name='address']:checked").val();
	if(addressId==""){
		$("#info").html(showDialogInfo("请选择收货地址","doubt"));
		return;
	}
	var payType = $("#payType").val();
	var goodsList = $("#goodsList").val();
	$.ajax({
		type:"post",
		dataType:"text",
		url:$("#buyUrl").val(),
		data:{"addressId":addressId,"payType":payType,"goodsList":goodsList,"password":$("#tranPwd").val()},
		async: false,
		success:function(returnData){
			returnData = eval(""+returnData+"");
			$("#buyDiv").hide();
			$("div.popup_bg").hide();
			
			if(returnData[0].result ==1){
				$("#info").html(showSuccInfo(returnData[0].msg,"successful",$("#myOrderUrl").val()));
			}else if(returnData[0].result ==0){
				$("#info").html(showDialogInfo(returnData[0].msg,"doubt"));
				$("#tranPwd").val("");
				$("#tran_pwd").val("");
			}else{
				$("#info").html(showDialogInfo(returnData[0].msg,"error"));
				$("#tranPwd").val("");
				$("#tran_pwd").val("");
			}
			
		},
		error: function(XMLHttpRequest, textStatus, errorThrown){
			$("#info").html(showDialogInfo("系统异常，请稍后重试！", "error"));
			$("#tranPwd").val("");
			$("#tran_pwd").val("");
		}
	});
	
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