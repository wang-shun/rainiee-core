
function showBankCard(){
	var formDiv = "<form id='form1' action='' class='form1' method='post' autocomplete='off'>";
		formDiv += "<div id='title' class='title'><a class='out close' href='javascript:void(0);'></a>添加银行卡</div>";
		formDiv += "<div class='content'   style='max-height: 490px; overflow-y: auto;'>";
		formDiv += "<ul class='text_list'>";
		formDiv += "<li>";
		formDiv += "<div class='til'><span class='red'>*</span>开户名：</div>";
		formDiv += "<div class='con' id='userNameDiv'>";
		formDiv += " </div>";
		formDiv += "</li>";
		formDiv += "<li>";
		formDiv += "<div class='til'><span class='red'>*</span>选择银行：</div>";
		formDiv += "<div class='con'>";
		formDiv += "<select name='bankname' id='bankNameDiv' class='select8'>";
        formDiv += "</select>";
        formDiv += "</div>";
        formDiv += "</li>";
        formDiv += "<li>";
        formDiv += "<div class='til'><span class='red'>*</span>开户行所在地：</div>";
        formDiv += "<div class='con'>";
        formDiv += "<select name='sheng' id='sheng' class='required select6' >";
        formDiv += "</select>";
        /*formDiv += "<select name='shi' id='shi' class='select6' >";
        formDiv += "</select>";
        formDiv += "<select name='xian' id='xian' class='select6'>";
        formDiv += "</select>";*/
        formDiv += "<p tip></p>";
        formDiv += "<p errortip class='' style='display: none'></p>";
        formDiv += "</div>";
        formDiv += "</li>";
        formDiv += "<li>";
        formDiv += "<div class='til'><span class='red'>*</span>开户行：</div>";
        formDiv += "<div class='con'>";
        formDiv += "<input type='text' name='subbranch' id='textfield' class='text_style border  required max-length-60 fl'/>";
        formDiv += "<p tip></p>";
        formDiv += "<p errortip class='' style='display: none'></p>";
        formDiv += "</div>";
        formDiv += " </li>";
        formDiv += "<li>";
        formDiv += "<div class='til'><span class='red'>*</span>银行卡卡号：</div>";
        formDiv += "<div class='con'>";
        formDiv += "<input type='text' name='banknumber' id='textfield2' class='text_style border  required textv-a max-length-30 fl' ";
        formDiv += "onkeyup=\"this.value=this.value.replace(/\\D/g,'').replace(/....(?!$)/g,'$& ')\" />";
        formDiv += "<p tip></p>";
        formDiv += "<p errortip id='errorMsg' class='red' style='display: none'></p>";
        formDiv += "</div>";
        formDiv += "</li>";
        formDiv += "<li>";
        formDiv += "<div class='til'><span class='red'>*</span>确认卡号：</div>";
        formDiv += "<div class='con'>";
        formDiv += "<input type='text' name='cbanknumber' onpaste='return false' id='textfield4'";
        formDiv += "class='text_style border  required textv-b fl' onkeyup=\"this.value=this.value.replace(/\\D/g,'').replace(/....(?!$)/g,'$& ')\" />";
        formDiv += "<p tip></p>";
        formDiv += "<p errortip class='red' style='display: none'></p>";
        formDiv += "</div>";
        formDiv += "</li>";
        formDiv += "</ul>";
        formDiv += "<div class='tc mt20'>";
        formDiv += "<input type='button' fromname='form1' class='btn01 sumbitForme' onclick='addCardConfirm();' value='提交'/>";
        formDiv += "<input type='button' class='btn01 btn_gray ml20 close'  value='取消'/>";
        formDiv += "</div>";
        formDiv += "<div class='prompt_mod mt20 mb30'>";
        formDiv += "<span class='highlight'>温馨提示：</span><br>";
        formDiv += "1.如果您填写的开户支行不正确，可能将无法成功提现，由此产生的提现费用将不予返还。<br>";
        formDiv += "2.如果您不确定开户行支行名称，可打电话到所在地银行的营业网点询问或上网查询。<br>";
        formDiv += "3.不支持提现至信用卡账户。";
        formDiv += "</div>";
        formDiv += "</div>";
        formDiv += "<input type='reset' name='reset' style='display: none;' />";
        formDiv += "</form>";
	
    var bankUrl = $('#bankUrl').val();
    $("#bankDialog").html(formDiv);
	$.ajax({
        type: 'post',
        dataType: 'json',
        url: bankUrl,
        data: {},
        async:false,
        success: function (data) {
        	if(data != null){
        		if(data.num == '0000'){
        			var t6110 = data.t6110;
        			var safety = data.safety;
        			var bank = data.bank;
        			
        			//开户名
        			var userNameDiv = "<span class=\"red\"></span><input type=\"hidden\" id=\"userName\" name=\"userName\" value='"+safety.name+"' />";
        			if(t6110.F06 == "ZRR"){
        				userNameDiv += "<input type=\"hidden\" name=\"type\" value=\"1\"/>"+safety.name;
        			}else{
        				userNameDiv += "<select name=\"type\" id=\"type\" class=\"select8\">";
        				userNameDiv += "<option value=\"1\">"+safety.name+"</option>";
        				userNameDiv += "<option value=\"2\">"+safety.qyName +"</option>";
        				userNameDiv += "</select>";
        			}
        			$("#userNameDiv").html(userNameDiv);
        			
        			$('#type').selectlist({
        				width: 232,
        				optionHeight: 28,
        				height: 28,
        				onChange:function(){
        					if ($("#form1 input[name='type']").val() == 2) {
        		                $("#userName").val(safety.qyName);
        		            } else {
        		                $("#userName").val(safety.name);
        		            }
        				}
        			});
        			
        	        //银行
        			var option = "";
        			if(bank != null && bank.length > 0){
	        			$.each(bank, function(i, value) {
	        				option += "<option value='"+this.id+"'>"+ this.name+"</option>";
	        			});
        			}
        			$("#bankNameDiv").html(option);
        			//显示弹窗
        			$("#bankDialog").show();
        		    $(".popup_bg").show();
        		}else if(data.num == '0002') {
            		$('.popup_bg').show();
            		$('#info').html(showSuccInfo(data.msg,'error',loginUrl));
            	}else{
        			$('#info').html(showDialogInfo(data.msg, 'error'));
        			$('div.popup_bg').show();
        		}
        	}
        },
		error: function(XMLHttpRequest, textStatus, errorThrown){
			if(XMLHttpRequest.responseText.indexOf('<html')>-1){
        		$('.popup_bg').show();
        		$('#info').html(showSuccInfo('页面已过期，请重新登录!','error',loginUrl));
        	}else if(XMLHttpRequest.responseText != '') {
        		$('.popup_bg').show();
        		$('#info').html(showSuccInfo(XMLHttpRequest.responseText,'error',loginUrl));
        	}else{
        		$('.popup_bg').show();
        		$('#info').html(showSuccInfo('系统繁忙，请稍候重试','error',loginUrl));
        	}
		}
    });
	
	$('#bankNameDiv').selectlist({
		width: 232,
		optionHeight: 28,
		height: 28
	});
	
	
    $("p[errortip]").hide();
    $("p[tip]").show();
    //清空数据
    $("input[type=reset]").trigger("click");
    //初始化省市县
    $("#shengId").val("");
	$("#shiId").val("");
	$("#xianId").val("");
	initRegion();
	
    //初始化表单校验
    initVal();
	
	$(".close").click(function () {
        $("div.dialog").hide();
        $("div.popup_bg").hide();
    });
}


/**
 * 提交添加银卡信息
 */
function addCardConfirm(){
	var reloadUrl = $("#reloadUrl").val();
	var addBankCardUrl = $("#addBankCardUrl").val();
	if(submitVal("form1")){
    	$.ajax({
            type: "post",
            dataType: "json",
            url: addBankCardUrl,
            data: $("#form1").serialize(),
            success: function (data) {
            	if(data != null){
            		if(data.num == "0000"){
            			$("#bankDialog").hide();
            			$("#info").html(showSuccInfo(data.msg, "successful",reloadUrl));
            		    $("div.popup_bg").show();
            		}else if(data.num == "0001"){
            			$("#errorMsg").html(data.msg);
            			$("#errorMsg").attr("class","red");
            			$("#errorMsg").show();
            		}else if(data.num == '0002') {
            			$("#bankDialog").hide();
                		$('div.popup_bg').show();
                		$('#info').html(showSuccInfo(data.msg,'error',loginUrl));
                	}else{
            			$("#bankDialog").hide();
            			$("#info").html(showDialogInfo(data.msg, "error"));
            			$("div.popup_bg").show();
            		}
            	}
            },
    		error: function(XMLHttpRequest, textStatus, errorThrown){
    			$("#bankDialog").hide();
    			if(XMLHttpRequest.responseText.indexOf('<html')>-1){
            		$(".popup_bg").show();
            		$("#info").html(showSuccInfo("页面已过期，请重新登录!","error",loginUrl));
            	}else if(XMLHttpRequest.responseText.indexOf('非法字符')>-1) {
            		$("#info").html(showDialogInfo("当前请求中存在非法字符，请重新输入！", "error"));
        			$("div.popup_bg").show();
				}else if(XMLHttpRequest.responseText != "") {
            		$(".popup_bg").show();
            		$("#info").html(showSuccInfo(XMLHttpRequest.responseText,"error",loginUrl));
            	}else{
            		$(".popup_bg").show();
            		$("#info").html(showSuccInfo("系统繁忙，请稍候重试","error",loginUrl));
            	}
    		}
        });
	}
}
