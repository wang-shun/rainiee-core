var flgs = true;
var phoneFlg = true;
var jkmsFlgs = false;
var bdsx = false;
$(function(){
		$submit =  $(".sumbitForme");
		$intext =$('input[type="text"]');
		$inpassword =$('input[type="password"]');
		$textarea =$('textarea');
		$select = $("select");
		
		$submit.click(function(){
			/*//判断用户是否可以借款
			if(!isCommit){return false;}*/
			flgs = true;
			var cname = $(this).attr("fromname");
			$form_intext =$('.'+cname+' input[type="text"]');
			$form_inpassword =$('.'+cname+' input[type="password"]');
			$form_textarea =$('.'+cname+' textarea');
			$form_select =$('.'+cname+' select');
			
			$form_intext.each(function(){
				checkText($(this));
			});
			
			$form_inpassword.each(function(){
				return checkPassword($(this));
			});
			
			$form_textarea.each(function(){
				return checkTextarera($(this));
			});
			
			$form_select.each(function(){
				return checkSelect($(this));
			});
			
			
			var $isread = $("input[name='isread']");
			if($isread.size()>0){
				if($isread[0].checked!=true){
					alert("你还没有阅读并同意签署协议!");
					flgs = false;
				}
			}
			if($(this).attr("savefromname") != undefined){
				//提交判定
				var $save = $('.'+cname+' input[name="save"]');
				if($save.size()>0){
					$save.val(1);
				}
			}
			if(jkmsFlgs)
			{
				return false;
			}

			if(bdsx)
			{
				return false;
			}

			if(!flgs || !phoneFlg){
				return false;
			}else{
				$("#saveSpan").attr("style","display:block;");
				$("#saveBtn").attr("style","display:none;");
				if(navigator.userAgent.indexOf("Firefox")>0){
					window.history.go(-1);
				}
			}

		 });
		$intext.focus(function(){
			$(this).blur(function(){
				return checkText($(this));
			 });
		});
		
		$textarea.focus(function(){
			$(this).blur(function(){
				checkTextarera($(this));
			 });
		});
		$inpassword.focus(function(){
			$(this).blur(function(){
				checkPassword($(this));
			 });
		});
		$select.focus(function(){
			$(this).blur(function(){
				checkSelect($(this));
			 });
		});
});

function checkPhoneExists(url,phone,id){
	$.ajax({
		type:"post",
		dataType:"html",
		url:url,
		data:{phone: $("#" + phone).val(),id: id},
		success:function(data){
			if ($.trim(data) == 'true') {
				$error = $("#errortip").html("手机号码已存在！");
				phoneFlg = false;
			}else{
				$error = $("#errortip").html("");
				phoneFlg = true;
			}
		}
	});
}

//校验输入文本框
function checkText($eve){
	
	if($eve.is(":hidden")){
		return;
	} 
	
	flg = true;
	if($eve== undefined){
		return false;
	}
	if($eve.attr("class")== undefined){
		return false;
	}
	msg = $eve.attr("class").split(" ");
	var mtest = $eve.attr("mtest");
	value = $eve.val();
	$error = $eve.nextAll("span[errortip]");
	$tip = $eve.nextAll("span[tip]");

	for(var i=0;i<msg.length;i++){
		var temp = $.trim(msg[i]);
		if(temp.length>0){
			if(temp == "required"){
				if($.trim(value) == ""){
					$error.addClass("error_tip");
					$error.html("不能为空！");
					$tip.hide();
					$error.show();
					flg = false;
					flgs = false;
					return false;
				}else{
					$tip.show();
					$error.hide();
				}
			}

			if(temp == "more-than-zero"){
				if($.trim(value) <= 0){
					$error.addClass("error_tip");
					$error.html("不能小于等于0！");
					$tip.hide();
					$error.show();
					flg = false;
					flgs = false;
					return false;
				}
			}
			
			if(temp == "isint"){
				var myreg = /^[0-9]([0-9])*$/;
				if($.trim(value) == ""){
					return false;
				}
				if(!myreg.test(value))
				{
					$error.addClass("error_tip");
					$error.html("必须为整数！");
					$tip.hide();
					$error.show();
					flg = false;
					flgs = false;
					return false;
				}
			}
			
			if(temp.indexOf('is-number')!=-1){
				tsizearry = temp.split("-");
				if(tsizearry.length>=3){
					tsize = tsizearry[2];
					var myreg = /^[0-9]([0-9])*$/;
					if($.trim(value) == ""){
						return false;
					}
					if(!myreg.test(value))
					{
						$error.addClass("error_tip");
						$error.html("请输入"+tsize+"位数字！");
						$tip.hide();
						$error.show();
						flg = false;
						flgs = false;
						return false;
					}
				}
			}
			
			if(temp.indexOf('min-size')!=-1){
				tsizearry = temp.split("-");
				if(tsizearry.length>=3){
					tsize = tsizearry[2];
					if(parseInt(value) < parseInt(tsize)){
						$error.addClass("error_tip");
						$error.html("小于最小范围值:" + tsize);
						flg = false;
						flgs = false;
						$tip.hide();
						$error.show();
						return false;
						
					}
				}
			}
			if(temp.indexOf('max-size')!=-1){
				tsizearry = temp.split("-");
				if(tsizearry.length>=3){
					tsize = tsizearry[2];
					if(parseInt(value) > parseInt(tsize)){
						$error.addClass("error_tip");
						$error.html("超出最大范围值:" + tsize);
						flg = false;
						flgs = false;
						$tip.hide();
						$error.show();
						return false;
						
					}
				}
			}
			if(temp.indexOf('max-nesize')!=-1){
				tsizearry = temp.split("-");
				if(tsizearry.length>=3){
					tsize = tsizearry[2];
					if(parseInt(value) >= parseInt(tsize)){
						$error.addClass("error_tip");
						$error.html("不能超出或等于最大值" + tsize + "");
						flg = false;
						flgs = false;
						$tip.hide();
						$error.show();
						return false;
						
					}
				}
			}
			if(temp.indexOf('max-ht-size')!=-1){
				//输入值减合同剩余金额不得小于3000
				tsizearry = temp.split("-");
				if(tsizearry.length>=3){
					tsize = tsizearry[2];
					if((parseInt(tsize) - parseInt(value))<3000){
						$error.addClass("error_tip");
						$error.html("剩余合同金额不足3000");
						flg = false;
						flgs = false;
						$tip.hide();
						$error.show();
						return false;
						
					}
				}
			}
			
			if(temp.indexOf('minf-size')!=-1){
				tsizearry = temp.split("-");
				if(tsizearry.length>=3){
					tsize = tsizearry[2];
					if(parseFloat(value) < parseFloat(tsize)){
						$error.addClass("error_tip");
						$error.html("小于最小范围值");
						flg = false;
						flgs = false;
						$tip.hide();
						$error.show();
						return false;
						
					}
				}
			}
			if(temp.indexOf('maxf-size')!=-1){
				tsizearry = temp.split("-");
				if(tsizearry.length>=3){
					tsize = tsizearry[2];
					if(parseFloat(value) > parseFloat(tsize)){
						$error.addClass("error_tip");
						$error.html("超出最大范围值");
						flg = false;
						flgs = false;
						$tip.hide();
						$error.show();
						return false;
						
					}
				}
			}
			if(temp.indexOf('mulriple')!=-1){
				tsizearry = temp.split("-");
				if(tsizearry.length>=2){
					tsize = tsizearry[1];
					if(parseInt(value) % parseInt(tsize) != 0){
						$error.addClass("error_tip");
						$error.html("必须是"+tsize+"的倍数");
						flg = false;
						flgs = false;
						$tip.hide();
						$error.show();
						return false;
						
					}
				}
			}
			if(temp.indexOf('min-length')!=-1){
				tsizearry = temp.split("-");
				if(tsizearry.length>=3){
					tsize = tsizearry[2];
					if(value.length < parseInt(tsize)){
						$error.addClass("error_tip");
						$error.html("小于输入限制"+tsize+"，当前长度为"+value.length);
						flg = false;
						flgs = false;
						$tip.hide();
						$error.show();
						return false;
						
					}
				}
			}
			if(temp.indexOf('max-length')!=-1){
				tsizearry = temp.split("-");
				if(tsizearry.length>=3){
					tsize = tsizearry[2];
					if(value.length>tsize){
						$error.addClass("error_tip");
						$error.html("超过输入限制"+tsize+"，当前长度为"+value.length);
						flg = false;
						flgs = false;
						$tip.hide();
						$error.show();
						return false;
						
					}
				}
			}
			
			if(temp.indexOf('max-precision')!=-1){
				tsizearry = temp.split("-");
				if(tsizearry.length>=3){
					tsize = tsizearry[2];
					var _d = value + '';
					var precision = (_d.substring(_d.indexOf('.') + 1)).length;
					if(precision > tsize){
						$error.addClass("error_tip");
						$error.html("超出数字精度" + tsize + "，当前精度为" + precision);
						flg = false;
						flgs = false;
						$tip.hide();
						$error.show();
						return false;
						
					}
				}
			}
			if(temp == "e-mail"){
				var myreg = /^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/;
				if(!myreg.test(value))
				{
					$error.addClass("error_tip");
					$error.html("邮箱地址不正确!");
					flg = false;
					flgs = false;
					$tip.hide();
					$error.show();
					return false;
				}
			}
			if(temp == "phonenumber"){
				var myreg = /^(13|14|15|17|18)[0-9]{9}$/;
				if(!myreg.test(value))
				{
					$error.addClass("error_tip");
					$error.html("请输入正确手机号!");
					flg = false;
					flgs = false;
					$tip.hide();
					$error.show();
					return false;
				}
			}
			
			if(temp == "idcard"){
				if(!isIdCardNot(value,$error))
				{
					flg = false;
					flgs = false;
					$tip.hide();
					$error.show();
					return false;
				}
			}
			if(temp == "textv-b"){
				var ddindx = $eve.index(".textv-b");
				$passwords = $(".textv-a:eq("+ddindx+")");
				if(value != $passwords.val()){
					$error.addClass("error_tip");
					$error.html("两次输入不一致!");
					flg = false;
					flgs = false;
					$tip.hide();
					$error.show();
					return false;
				}
			}
			
			if(temp == "lcjesx-size"){
				if(value!=""){
					var ddindx = $eve.index(".lcjesx-size");
					$lcjesx = $(".lcjexx-size:eq("+ddindx+")");
					if($lcjesx.val()!=""){
						if(parseFloat(value) < parseFloat($lcjesx.val())){
							$error.addClass("error_tip");
							$error.html("用户投资上限大于等于用户投资下限!");
							flg = false;
							flgs = false;
							$tip.hide();
							$error.show();
							return false;
						}
					}
				}
			}
			
			if(temp == "jkje-size"){
				if(value!=""){
					var ddindx = $eve.index(".jkje-size");
					$lcjesx = $(".lcjesx-size:eq("+ddindx+")");
					if($lcjesx.val()!=""){
						if(parseFloat(value) < parseFloat($lcjesx.val())){
							$error.addClass("error_tip");
							$error.html("计划金额大于等于用户投资上限!");
							flg = false;
							flgs = false;
							$tip.hide();
							$error.show();
							return false;
						}
					}
				}
			}
			
			if(temp == "jzsj-size"){
				if(value!=""){
					var ddindx = $eve.index(".jzsj-size");
					$kssj = $(".kssj-size:eq("+ddindx+")");
					if($kssj.val()!=""){
						var startDateTemp = $kssj.val().split(" ");   
						var endDateTemp = value.split(" ");
						var arrStartDate = startDateTemp[0].split("-");   
						var arrEndDate = endDateTemp[0].split("-");   
						var arrStartTime = startDateTemp[1].split(":");   
						var allStartDate = new Date(arrStartDate[0],arrStartDate[1],arrStartDate[2],arrStartTime[0],arrStartTime[1],arrStartTime[2]);   
						var allEndDate = new Date(arrEndDate[0],arrEndDate[1],arrEndDate[2],"23","59","59");
						if(allStartDate.getTime()>allEndDate.getTime()){   
							$error.addClass("error_tip");
							$error.html("申请截止时间小于申请开始时间!");
							flg = false;
							flgs = false;
							$tip.hide();
							$error.show();
							return false;
						}
					}
				}
			}
			
			if(temp == "lxmax-size"){
				if(value!=""){
					var ddindx = $eve.index(".lxmax-size");
					$lcjesx = $(".lxmin-size:eq("+ddindx+")");
					if($lcjesx.val()!=""){
						if(parseFloat(value) < parseFloat($lcjesx.val())){
							$error.addClass("error_tip");
							$error.html("利息范围有误!");
							flg = false;
							flgs = false;
							$tip.hide();
							$error.show();
							return false;
						}
					}
				}
			}
			
			if(temp == "xxfs_ratio"){
				var sx = $("#SX_F04").val();
				var xx = $("#XX_F03").val();
				if(sx!="" && xx!=""){
					var $error_f03 = $("#XX_F03").nextAll("p[errortip]");
					if(parseFloat(xx) > parseFloat(sx)){
						$error_f03.addClass("error_tip");
						$error_f03.html("下限分数不能大于上限分数！");
						flg = false;
						flgs = false;
						$tip.hide();
						$error_f03.show();
						return false;
					}else{
						$error_f03.removeClass("error_tip");
						$error_f03.hide();
						$error_f03.html("");
					}
					
				}
			}
		}
	}

	if($eve.attr('id')!=undefined && $eve.attr('id')=='userName'){
		checkUserInfo();
		return;
	}
	if(mtest != undefined){
		if(mtest.length>0){
			var myreg = eval(mtest);  
			if(!myreg.test(value))
			{
				$error.addClass("error_tip");
				$error.html($eve.attr("mtestmsg"));
				$tip.hide();
				$error.show();
				flg = false;
				flgs = false;
				return false;
			}
		}
	}
	
	if(!flg){
		$tip.hide();
		$error.show();
		return false;
	}else{
		$error.removeClass("error_tip");
		$error.hide();
		$tip.show();
	}
}

//校验输入文本框
function checkSelect($eve){
	if($eve.is(":hidden")){
		return;
	} 
	flg = true;
	if($eve== undefined){
		return false;
	}
	if($eve.attr("class")== undefined){
		return false;
	}
	msg = $eve.attr("class").split(" ");
	value = $eve.val();
	$error = $eve.nextAll("span[errortip]");
	$tip = $eve.nextAll("span[tip]");

	for(var i=0;i<msg.length;i++){
		var temp = $.trim(msg[i]);
		if(temp.length>0){
			if(temp == "required"){
				if($.trim(value) == ""){
					$error.addClass("error_tip");
					$error.html("请选择！");
					$tip.hide();
					$error.show();
					flg = false;
					flgs = false;
					return false;
				}
			}
		}
	}
}

function checkPassword($eve){
	if($eve.is(":hidden")){
		return;
	}
	flg = true;
	if($eve== undefined){
		return false;
	}
	if($eve.attr("class")== undefined){
		return false;
	}
	msg = $eve.attr("class").split(" ");
	var mtest = $eve.attr("mtest");
	value = $eve.val();
	$error = $eve.nextAll("span[errortip]");
	$tip = $eve.nextAll("span[tip]");

	for(var i=0;i<msg.length;i++){
		var temp = $.trim(msg[i]);
		if(temp.length>0){
			if(temp == "required"){
				if($.trim(value) == ""){
					$error.addClass("error_tip");
					$error.html("不能为空！");
					$tip.hide();
					$error.show();
					flg = false;
					flgs = false;
					return false;
				}
			}
			if(temp == "password-a"){
				var myreg = /^(?![a-zA-Z0-9]+$)(?![^a-zA-Z/D]+$)(?![^0-9/D]+$).{8,12}$/;
				if(!myreg.test(value))
				{
					$error.addClass("error_tip");
					$error.html("数字+字母+特殊字符，8-12位长度");
					flg = false;
					flgs = false;
					$tip.hide();
					$error.show();
					return false;
				}
			}
			if(temp == "cpassword-a"){
				if($.trim(value)!=""){
					var myreg = /^(?![a-zA-Z0-9]+$)(?![^a-zA-Z/D]+$)(?![^0-9/D]+$).{8,12}$/;
					if(!myreg.test(value))
					{
						$error.addClass("error_tip");
						$error.html("数字+字母+特殊字符，8-12位长度");
						flg = false;
						flgs = false;
						$tip.hide();
						$error.show();
						return false;
					}
				}
			}
			if(temp == "password-b"){
				var myreg =  /^(?![a-zA-Z0-9]+$)(?![^a-zA-Z/D]+$)(?![^0-9/D]+$).{8,12}$/;
				if(!myreg.test(value))
				{
					$error.addClass("error_tip");
					$error.html("数字+字母+特殊字符，8-12位长度");
					flg = false;
					flgs = false;
					$tip.hide();
					$error.show();
					return false;
				}
				var ddindx = $eve.index(".password-b");
				$passwords = $(".password-a:eq("+ddindx+")");
				if(value != $passwords.val()){
					$error.addClass("error_tip");
					$error.html("两次输入密码不一致!");
					flg = false;
					flgs = false;
					$tip.hide();
					$error.show();
					return false;
				}
			}
			if(temp == "password-e"){
				var myreg =  /^(?![a-zA-Z0-9]+$)(?![^a-zA-Z/D]+$)(?![^0-9/D]+$).{8,12}$/;
				if(!myreg.test(value))
				{
					$error.addClass("error_tip");
					$error.html("数字+字母+特殊字符，8-12位长度");
					flg = false;
					flgs = false;
					$tip.hide();
					$error.show();
					return false;
				}
				var ddindx = $eve.index(".password-e");
				$passwords = $(".password-a:eq("+ddindx+")");
				if(value == $passwords.val()){
					$error.addClass("error_tip");
					$error.html("密码和交易密码输入不能一致!");
					flg = false;
					flgs = false;
					$tip.hide();
					$error.show();
					return false;
				}
			}
			if(temp == "password-f"){
				var myreg =  /^(?![a-zA-Z0-9]+$)(?![^a-zA-Z/D]+$)(?![^0-9/D]+$).{8,12}$/;
				if(!myreg.test(value))
				{
					$error.addClass("error_tip");
					$error.html("数字+字母+特殊字符，8-12位长度");
					flg = false;
					flgs = false;
					$tip.hide();
					$error.show();
					return false;
				}
				
				var ddindx = $eve.index(".password-f");
				$passwords = $(".password-e:eq("+ddindx+")");
				if(value != $passwords.val()){
					$error.addClass("error_tip");
					$error.html("两次输入交易密码不一致!");
					flg = false;
					flgs = false;
					$tip.hide();
					$error.show();
					return false;
				}
			}
			if(temp == "cpassword-b"){
				if($.trim(value)!=""){
					var myreg =  /^(?![a-zA-Z0-9]+$)(?![^a-zA-Z/D]+$)(?![^0-9/D]+$).{8,12}$/;
					if(!myreg.test(value))
					{
						$error.addClass("error_tip");
						$error.html("数字+字母+特殊字符，8-12位长度");
						flg = false;
						flgs = false;
						$tip.hide();
						$error.show();
						return false;
					}
					var ddindx = $eve.index(".cpassword-b");
					$passwords = $(".cpassword-a:eq("+ddindx+")");
					if(value != $passwords.val()){
						$error.addClass("error_tip");
						$error.html("两次输入密码不一致!");
						flg = false;
						flgs = false;
						$tip.hide();
						$error.show();
						return false;
					}
				}
			}
			if(temp == "spassword-b"){
				if(value!=""){
					var myreg =  /^(?![a-zA-Z0-9]+$)(?![^a-zA-Z/D]+$)(?![^0-9/D]+$).{8,12}$/;
					if(!myreg.test(value))
					{
						$error.addClass("error_tip");
						$error.html("数字+字母+特殊字符，8-12位长度");
						flg = false;
						flgs = false;
						$tip.hide();
						$error.show();
						return false;
					}
					var ddindx = $eve.index(".spassword-b");
					$passwords = $(".spassword-a:eq("+ddindx+")");
					if(value != $passwords.val()){
						$error.addClass("error_tip");
						$error.html("两次输入密码不一致!");
						flg = false;
						flgs = false;
						$tip.hide();
						$error.show();
						return false;
					}
				}
			}
			if(temp.indexOf('max-length')!=-1){
				tsizearry = temp.split("-");
				if(tsizearry.length>=3){
					tsize = tsizearry[2];
					if(value.length>tsize){
						$error.addClass("error_tip");
						$error.html("超过输入限制"+tsize+"，当前长度为"+value.length);
						flg = false;
						flgs = false;
						$tip.hide();
						$error.show();
						return false;
						
					}
				}
			}
			
		}
	}
	if(mtest != undefined){
		if(mtest.length>0){
			var myreg = eval(mtest);  
			if(!myreg.test(value))
			{
				$error.addClass("error_tip");
				$error.html($eve.attr("mtestmsg"));
				$tip.hide();
				$error.show();
				flg = false;
				flgs = false;
				return false;
			}
		}
	}
	
	if(!flg){
		$tip.hide();
		$error.show();
		return false;
	}else{
		$error.removeClass("error_tip");
		$error.hide();
		$tip.show();
	}
}

//校验文本域
function checkTextarera($eve){
	if($eve.is(":hidden")){
		return;
	} 
	flg = true;
	if($eve== undefined){
		return false;
	}
	if($eve.attr("class")== undefined){
		return false;
	}
	msg = $eve.attr("class").split(" ");
	value = $eve.val();
	$error = $eve.nextAll("span[errortip]");
	$tip = $eve.nextAll("span[tip]");
	flg = true;
	for(var i=0;i<msg.length;i++){
		var temp = $.trim(msg[i]);
		if(temp.length>0){
			if(temp == "required"){
				if($.trim(value) == ""){
					$error.addClass("error_tip");
					$error.html("不能为空！");
					$tip.hide();
					$error.show();
					flg = false;
					flgs = false;
					return false;
				}
			}
			if(temp == "forbidden-s"){
				if(value != null && $.trim(value) != ""){
					var myreg = /<script[^>]*>[\s\S]*<\/script>/;
					if(value.search(myreg) != -1)
					{
						$error.addClass("error_tip");
						$error.html("内容含非法脚本！");
						$tip.hide();
						$error.show();
						flg = false;
						flgs = false;
						return false;
					}
				}
			}
			if(temp.indexOf('max-length')!=-1){
				tsizearry = temp.split("-");
				if(tsizearry.length>=3){
					tsize = tsizearry[2];
					if(value.length>tsize){
						$error.addClass("error_tip");
						$error.html("超过输入限制"+tsize+"，当前长度为"+value.length);
						flg = false;
						flgs = false;
						$tip.hide();
						$error.show();
						return false;
					}
				}
			}
			if(temp.indexOf('min-length')!=-1){
				tsizearry = temp.split("-");
				if(tsizearry.length>=3){
					tsize = tsizearry[2];
					if(value.length < parseInt(tsize)){
						$error.addClass("error_tip");
						$error.html("小于输入限制"+tsize+"，当前长度为"+value.length);
						flg = false;
						flgs = false;
						$tip.hide();
						$error.show();
						return false;
						
					}
				}
			}
		}
	}
	if(!flg){
		$tip.hide();
		$error.show();
		return false;
	}else{
		$error.removeClass("error_tip");
		$error.hide();
		$tip.show();
	}
}

function isIdCardNot(num,$error) {            
    num = num.toUpperCase();           //身份证号码为15位或者18位，15位时全为数字，18位前17位为数字，最后一位是校验位，可能为数字或字符X。        
    if (!(/(^\d{15}$)|(^\d{17}([0-9]|X)$)/.test(num))) {     
        $error.addClass("error_tip");
		$error.html('输入的身份证号不符合规定！15位号码应全为数\n字，18位号码末位可以为数字或X。');
        return false;         
    } //校验位按照ISO 7064:1983.MOD 11-2的规定生成，X可以认为是数字10。 
    //下面分别分析出生日期和校验位 
    var len, re; len = num.length; if (len == 15) { 
        re = new RegExp(/^(\d{6})(\d{2})(\d{2})(\d{2})(\d{3})$/); 
        var arrSplit = num.match(re);  //检查生日日期是否正确
        var dtmBirth = new Date('19' + arrSplit[2] + '/' + arrSplit[3] + '/' + arrSplit[4]); 
        var bGoodDay; bGoodDay = (dtmBirth.getYear() == Number(arrSplit[2])) && ((dtmBirth.getMonth() + 1) == Number(arrSplit[3])) && (dtmBirth.getDate() == Number(arrSplit[4]));
        if (!bGoodDay) {         
            $error.addClass("error_tip");
    		$error.html('输入的身份证号里出生日期不对！');
            return false;
        } else { //将15位身份证转成18位 //校验位按照ISO 7064:1983.MOD 11-2的规定生成，X可以认为是数字10。        
            var arrInt = new Array(7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2);         
            var arrCh = new Array('1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2');      
            var nTemp = 0, i;            
            num = num.substr(0, 6) + '19' + num.substr(6, num.length - 6);           
            for(i = 0; i < 17; i ++) {                 
                nTemp += num.substr(i, 1) * arrInt[i];        
            }
            num += arrCh[nTemp % 11]; 
            return true;
        }
    }
    if (len == 18) {
        re = new RegExp(/^(\d{6})(\d{4})(\d{2})(\d{2})(\d{3})([0-9]|X)$/); 
        var arrSplit = num.match(re);  //检查生日日期是否正确 
        var dtmBirth = new Date(arrSplit[2] + "/" + arrSplit[3] + "/" + arrSplit[4]); 
        var bGoodDay; bGoodDay = (dtmBirth.getFullYear() == Number(arrSplit[2])) && ((dtmBirth.getMonth() + 1) == Number(arrSplit[3])) && (dtmBirth.getDate() == Number(arrSplit[4])); 
        if (!bGoodDay) { 
            $error.addClass("error_tip");
    		$error.html('输入的身份证号里出生日期不对！');
            return false; 
        }
        else { //检验18位身份证的校验码是否正确。 //校验位按照ISO 7064:1983.MOD 11-2的规定生成，X可以认为是数字10。 
            var valnum; 
            var arrInt = new Array(7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2); 
            var arrCh = new Array('1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2'); 
            var nTemp = 0, i; 
            for(i = 0; i < 17; i ++) { 
                nTemp += num.substr(i, 1) * arrInt[i];
            } 
            valnum = arrCh[nTemp % 11]; 
            if (valnum != num.substr(17, 1)) { 
                $error.addClass("error_tip");
        		$error.html('18位身份证的校验码不正确！');
                return false; 
            } 
            return true; 
        } 
    } return false;
}

//如果是奖励标，则显示奖励利率
function changeJlb(value) {
	if ("jlb" == value){
		$("#jlll").show();
		/*$("#czjlll").val("0.00");*/
	}else {
		$("#jlll").hide();
	}

	if("jlb" == value || "xsb" == value)
	{
		$("input[name='rewardUsedType']").attr("disabled",true);
		$("input[name='rewardUsedType'][value='NONE']").attr("checked",true);
	}else{
		$("input[name='rewardUsedType']").attr("disabled",false);
		$("input[name='rewardUsedType'][value='ALL']").attr("checked",true);
	}
}

//输入借款金额，则修改最大的投资金额
function changeJkje(value) {
	if (value !== null || value !== undefined || value !== ''){
		$("#zdtze").val(value);
	}
}


function checkUserInfo() {
	var userName = $("#userName").val();
	var p = $("#userMsg");
	p.removeClass();
	p.addClass("error_tip");
	var data={"userName":userName};
	$.ajax({
		type:"post",
		dataType:"html",
		url:_cURL,
		data:data,
		async: false,
		success:function(data){
			if(data){
				var ct = eval('('+data+')');
				if(ct.length>0 && ct[0].num != 1){
					var msg = ct[0].msg;
					p.show();
					p.text(msg);
					isCommit = false;
				}else{
					p.hide();
					isCommit = true;
				}
			}
		}
	});
}