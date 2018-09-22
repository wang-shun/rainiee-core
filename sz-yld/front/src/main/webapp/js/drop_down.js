// JavaScript Document
$(function(){
		/*下拉框*/
	$('.select').selectlist({
		zIndex: 20,
		width: 180,
		height: 36
	});
	$('.select2').selectlist({
		zIndex: 10,
		width: 372,
		height: 36
	});
	$('.select2-3').selectlist({
		zIndex: 30,
		width: 372,
		height: 36
	});
	$('.select3').selectlist({
		zIndex: 20,
		width: 75,
		optionHeight: 22,
		height: 22
	});
	$('.select4').selectlist({
		zIndex: 20,
		width: 190,
		optionHeight: 22,
		height: 22
	});
	$('.select5').selectlist({
		zIndex: 20,
		width: 332,
		height: 36
	});
	$('.select6').selectlist({
		zIndex: 15,
		width: 105,
		optionHeight: 28,
		height: 28
	});
	$('.select7').selectlist({
		zIndex: 20,
		width: 95,
		optionHeight: 28,
		height: 28
	});
	$('.select8').selectlist({
		zIndex: 20,
		width: 232,
		optionHeight: 28,
		height: 28
	});
	$('.select9').selectlist({
		zIndex: 20,
		width: 95,
		optionHeight: 26,
		height: 26
	});
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