//tab切换
function setTab(name,cursel,n){
	var hover="hover";
	for(var i=1;i<=n;i++){
	var menu=$("#"+name+i);
	var con=document.getElementById("con_"+name+"_"+i);
	if(i==cursel){
			menu.addClass(hover);
		}
		else{
			menu.removeClass(hover);
		}
	
	if (con)con.style.display=i==cursel?"block":"none";
	}
}

$(function(){
	//头部wx,wb,app,user
    $('.top_bar .icon').mouseover(function(){
        $(this).children().show();
        $(this).addClass("cur");
    });
    $('.top_bar .icon').mouseout(function(){
        $(this).children().hide();
        $(this).removeClass("cur");
    });
    $('.top_bar .app').mouseover(function(){
        $(this).children().next().show();
        $(this).addClass("cur");
    });
    $('.top_bar .app').mouseout(function(){
        $(this).children().next().hide();
        $(this).removeClass("cur");
    });
    $('.top_bar .user').mouseover(function(){
        $(this).children().next().show();
    });
    $('.top_bar .user').mouseout(function(){
        $(this).children().next().hide();
    });

	 /*导航*/
	 /*  $('.head_menu .menu_item .nav').hover(function(){
        $(this).children().next().show();
        $(this).addClass("cur");
    },function(){
            $(this).children().next().hide();
            $(this).remove("cur");
    })*/
		 /*导航*/
	$('.main_nav li').mouseover(function(){
          $(this).children().next().show();
		  $(this).addClass("cur");
     }); 
	 $('.main_nav li').mouseout(function(){
          $(this).children().next().hide();
		  $(this).removeClass("cur");
     });
	
	 /*tips提示*/
	 $(".hover_tips").mouseover(function() {
		 $(this).children().show();
	 });
	 $(".hover_tips").mouseout(function() {
		 $(this).children().hide();
	 });
	 /*微信切换效果*/
	 $(".login_mod .wx_figure").click(function(e){
		 $('.weChat_port').show();
		 $('.landing_port').hide();
	 });
     $(".login_mod .other_port").click(function(){
    	 $('.landing_port').show();
		 $('.weChat_port').hide();
     });
	// 输入框提示文字
	$(".focus_input .focus_text").each(function(){
	   var thisVal=$(this).val();
	   //判断文本框的值是否为空，有值的情况就隐藏提示语，没有值就显示
	   if(thisVal!=""){
		 $(this).siblings(".focus_input label").hide();
		}else{
		 $(this).siblings(".focus_input label").show();
		}
	   //聚焦型输入框验证
	   $(this).focus(function(){
		 $(this).siblings(".focus_input label").hide();
		}).blur(function(){
		  var val=$(this).val();
		  if(val!=""){
		   $(this).siblings(".focus_input label").hide();
		  }else{
		   $(this).siblings(".focus_input label").show();
		  }
		});
	  });
	  
	/*返回顶部
	$("#re_top").click(function(){
		if($(document).scrollTop()>1){
			$("body,html").animate({scrollTop:0},600);
		}
	});
	*/
	/*下拉框*/
	//initSelectList();
	
	//选择输入框是改变边框颜色
	$("input,textarea").focus(function(){
		$(this).removeClass("border_focus");
	});

	$("input,textarea").focus(function(){
		var parentObj = $(this).parent();
		if(!parentObj.hasClass("input") && !parentObj.hasClass("icon_input") && !parentObj.hasClass("password_input"))
		{
			$(this).addClass("border_focus");
		}
		else
		{
			$(this).parent().addClass("border_focus");
		}
	});
	$("input,textarea").blur(function(){
		var parentObj = $(this).parent();
		if(!parentObj.hasClass("input") && !parentObj.hasClass("icon_input") && !parentObj.hasClass("password_input"))
		{
			$(this).removeClass("border_focus");
		}
		else
		{
			$(this).parent().removeClass("border_focus");
		}
	});
	  
});


/**
 * 初始化下拉框
 */
function initSelectList(){
	$('.select').selectlist({
		width: 180,
		height: 36
	});
	$('.select2').selectlist({
		zIndex: 10,
		width: 372,
		height: 36
	});
	$('.select2-3').selectlist({
		width: 372,
		height: 36
	});
	$('.select3').selectlist({
		width: 75,
		optionHeight: 22,
		height: 22
	});
	$('.select4').selectlist({
		width: 190,
		optionHeight: 22,
		height: 22
	});
	$('.select5').selectlist({
		width: 332,
		height: 36
	});
	$('.select6').selectlist({
		width: 105,
		optionHeight: 28,
		height: 28
	});
	$('.select7').selectlist({
		width: 95,
		optionHeight: 28,
		height: 28
	});
	$('.select8').selectlist({
		width: 232,
		optionHeight: 28,
		height: 28
	});
	$('.select9').selectlist({
		width: 95,
		optionHeight: 26,
		height: 26
	});
}

//返回顶部
$.fn.returnTop = $.fn.returntop = function(options){
	var _this = this;
	var setting = {
		hide	:	true, // 当srollTop为0,页面处于最顶端的时候，隐藏返回顶部按钮
		speed	:	200,	 //	返回顶部滚动所需要的时间
		callback	:	$.noop	//到达顶部后的回调函数
	};
	if(options){
		$.extend(setting,options);
	}
	this.click(function(){
		$('html, body').animate({scrollTop: 0},setting.speed,function(){
			setting.callback.call(this);
		});
	});
	if(setting.hide){
		$(document).scrollTop() > 0 ? _this.show() : _this.hide();
		$(window).bind("scroll", function() {
			$(document).scrollTop() > 0 ? _this.fadeIn() : _this.fadeOut();
		});
	}
};
/**
 * 生成Tonken值隐藏域
 * @returns {String}
 */
function getTokenStr(){
	var str = "";
	$.ajax({
		type:"post",
		dataType:"json",
		url:"/getTokenForAjax.htm",
		data:{},
		async:false,
		success:function(returnData){
			if(returnData != null){
				str = returnData.token;
			}
		},
		error: function(XMLHttpRequest, textStatus, errorThrown){
			alert(textStatus);
		}
	});
	
	return str;
}
$("#returnTop").returnTop();