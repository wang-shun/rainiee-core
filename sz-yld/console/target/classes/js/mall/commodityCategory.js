function updateCommodityCategory(url,F01,F02,F03,F07){
	$("#addCategory_popup").show();
	var html = "<div class='popup-box' style='min-height: 270px;min-width:630px;'>";
	html += "<form action='"+url+"' method='post' onsubmit='return updateCategorySubmit("+F01+");'>";
	html += "<input type='hidden' name='F01' value='"+F01+"'>";
	html += "<div class='popup-title-container'>";
	html += "<h3 class='pl20 f18'>修改类别</h3>";
	html += "<a class='icon-i popup-close2' href='javascript:void(0);' onclick='closeUpdatePopup();'></a>";
	html += "</div>";
	html += "<div class='popup-content-container pt20 ob20 clearfix' >";
	/*html += "<div class='mb20 gray6'>";
	html += "<span class='display-ib tr mr5'>&nbsp;</span>";
	html += "<input class='ml80' type='radio' value='kind' name='F07'";
	if(F07=="kind"){
		html += " checked='checked' ";
	}
	html += "/>实物";
	html += "<input class='ml50' type='radio' value='fee' name='F07'";
	if(F07=="fee"){
		html += " checked='checked' ";
	}
	html += "/>话费";
	html += "</div>";*/
	html += "<div class='mb20 gray6'>";
	html += "<span class='display-ib tr mr5'>";
	html += "<span class='red'>*</span>类别编码：</span>";
	html += "<input id='update_f02' name='F02' maxlength='10' onblur='checkF02(this,"+F01+");' onfocus='clearErroInfo(this);' type='text' value='"+F02+"' class='text border w250 yw_w5'/>";
	html += "<span tip>请输入2~10位字母+数字的组合</span>";
	html += "<span errortip class='' style='display: none'></span>";
	html += "</div>";
	html += "<div class='mb20 gray6'>";
	html += "<span class='display-ib tr mr5'>";
	html += "<span class='red'>*</span>商品类别：</span>";
	html += "<input id='update_f03' name='F03' maxlength='30' onblur='checkF03(this,"+F01+");' onfocus='clearErroInfo(this);' type='text' value='"+F03+"' class='text border w250 yw_w5'/>";
	html += "<span tip>请输入长度2~30位</span>";
	html += "<span errortip class='' style='display: none'></span>";
	html += "</div>";
	html += "<div class='tc f16'>";
	html += "<input type='submit' class='btn-blue2 btn white radius-6 pl20 pr20' value='提交' />";
	html += "<input type='button' value='取消' class='btn-blue2 btn white radius-6 pl20 pr20 ml40' onclick='closeUpdatePopup();'/>";
	html += "</div>";
	html += "</div>";
	html += "</form>";   
	html += "</div>";
	$("#updateCategory").html(html);
}

function closeUpdatePopup(){
	$("#updateCategory").html('');
	$("#addCategory_popup").hide();
}

function addCategorySubmit(){
	var $F02 = $("#add_f02");
	var $F03 = $("#add_f03");
	if(checkF02($F02,0) && checkF03($F03,0)){
		return true;
	}
	return false;
}

function updateCategorySubmit(F01){
	var $F02 = $("#update_f02");
	var $F03 = $("#update_f03");
	if(checkF02($F02,F01) && checkF03($F03,F01)){
		return true;
	}
	return false;
}

function checkF02(obj,F01){
	var $F02 = $(obj);
	$error = $F02.nextAll("span[errortip]");
    $tip = $F02.nextAll("span[tip]");
    var F02 = $F02.val();
    var reg = /^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{2,10}$/;
    if(!reg.test(F02)){
    	showErrorInfo($tip,$error,"请输入2~10位字母+数字的组合");
    	return false;
    }
    var isTrue = false;
    $.ajax({
		type:"post",
		dataType:"json",
		url:_nURL,
		data:{name : F02},
		async: false,
		success:function(returnData){
			if(returnData.num == 1){
				isTrue = true;
			}else{
				if(returnData.num == 0 
						&& parseInt(F01) != 0 
						&& parseInt(returnData.F01) == parseInt(F01)){
					isTrue = true;
				}else if(returnData.num == 2){
					showErrorInfo($tip,$error,returnData.msg);
				}else{
					showErrorInfo($tip,$error,"该类别编码已存在!");
				}
			}
		}
	});
    return isTrue;
}

function checkF03(obj,F01){
	var $F03 = $(obj);
	$error = $F03.nextAll("span[errortip]");
    $tip = $F03.nextAll("span[tip]");
    var F03 = $F03.val();
    if(!F03 || !$.trim(F03)){
        showErrorInfo($tip,$error,"请输入长度2~30位字符");
        return false;
	}
    if(F03.length<2 || F03.length>30){
    	showErrorInfo($tip,$error,"请输入长度2~30位字符");
    	return false;
	}

    var isTrue = false;
    $.ajax({
		type:"post",
		dataType:"json",
		url:_nURL,
		data:{name : F03},
		async: false,
		success:function(returnData){
			if(returnData.num == 1){
				isTrue = true;
			}else{
				if(returnData.num == 0 
						&& parseInt(F01) != 0 
						&& parseInt(returnData.F01) == parseInt(F01)){
					isTrue = true;
				}else if(returnData.num == 2){
					showErrorInfo($tip,$error,returnData.msg);
				}else{
					showErrorInfo($tip,$error,"该类别名称已存在!");
				}
			}
		}
	});
    return isTrue;
}

function clearErroInfo(obj){
	var $obj = $(obj);
	$error = $obj.nextAll("span[errortip]");
    $tip = $obj.nextAll("span[tip]");
    $error.removeClass("error_tip");
    $error.html('');
    $tip.show();
    $error.hide();
}

function showErrorInfo($tip,$error,errorInfo){
	$error.addClass("error_tip");
    $error.html(errorInfo);
    $tip.hide();
    $error.show();
}