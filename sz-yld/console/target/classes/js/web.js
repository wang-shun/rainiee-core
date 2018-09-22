// JavaScript Document
var  myfn=new Object({
		AjaxFn:function(url,div){
		$.ajax({
		url:url,
		success: function(data){
		div.html(data);}
		});
	}
});	

//窗口关闭
function closeInfo(){
	$(".popup-box").hide();
	$(".popup_bg").hide();
	}



$(function(){
	
	//页面加载
	myfn.AjaxFn("part/top.html",$(".top-container"));
	myfn.AjaxFn("part/subnavbar.html",$(".left-container"));
	
	
	
	//头部点击样式
	$(".top-container").on("click",".main-nav-a",function(){
		$(this).parents("ul").find(".main-nav-a").removeClass("select-a");
		$(this).addClass("select-a");
		$(".left-container .item-subnav-box[data-title="+$(this).attr("data-title")+"]").show().siblings(".item-subnav-box").hide();
	});
	
	
	//右边页面加载
	$("body").on("click", ".click-link", function(){
			var urlstr=$(this).attr("data-url");
			if (typeof(urlstr)!= "undefined"){
				if(urlstr=="null"){
					alert("此链接暂无页面，请查看其它!");
					}
				else{
					myfn.AjaxFn(urlstr,$(".viewFramework-content"));
				}
			}
	});
	 
	
	
	
	//左边菜单点击样式
	
	/* $(".left-container").on("click", ".click-link", function(){
			$(this).parents("dl").find(".click-link").removeClass("select-a");
		  	$(this).addClass("select-a");
		 	$(this).find(".arrow-down-icon").addClass("arrow-up-icon");
			$(this).parents("dd").siblings("dd").find(".arrow-down-icon").removeClass("arrow-up-icon");
			$(this).parents("dd").find("ul").slideDown(400);
			$(this).parents("dd").siblings("dd").find("ul").slideUp(400);
		 });*/
	 
	 
	 //左边隐藏，展开
	 $(".left-hide-arrow").click(function(){
		 var  btn=$(this);
		 var leftcontainer=$(".left-container");
		 var rightcontainer=$(".right-container");
		 if(btn.hasClass("left-container-hide"))
		 {
			 btn.removeClass("left-container-hide");
			 leftcontainer.stop().animate({"left":0+"px"},400);
			 rightcontainer.stop().animate({"left":170+"px"},400);
			 }
		 else{
				btn.addClass("left-container-hide");
			 leftcontainer.stop().animate({"left":-170+"px"},400);
			 rightcontainer.stop().animate({"left":0+"px"},400);
		 	}
		 });
		 
	//Tab切换菜单
	$("body").on("mouseenter", ".tab-btn", function(){
			var tabindex=$(this).parent("li").index();
			$(this).addClass("select-a").append("<i class='icon-i tab-arrowtop-icon'></i>");
			$(this).parents("li").siblings("li").find("a").removeClass("select-a").find("i").remove();
			$(this).parents(".tabnav-container").siblings(".tab-content-container").find(".tab-item").hide().eq(tabindex).show();
		});
		
	//筛选条件设置
	$("body").on("click",".remove-radiusbtn-icon",function(){
			$(this).parents("li").remove();
		});
		
	$("body").on("click",".add-radiusbtn-icon",function(){
		if($(this).parents("li").index()<7){
			var strli="<li><div class='mr30'><input type='text' class='border w40 pl5 pr5 h34 lh34' value='10' />%一<input type='text' class='border w40 pl5 pr5 h34 lh34' value='' />"+
                        "%<a href='javascript:void(0);' class='icon-i w30 h30 va-middle remove-radiusbtn-icon'></a></div></li>";
		
			$(this).parents("li").before(strli);
			}
			else{
				alert("最多只能新增8个区域！");}
		});
	
	//弹出框按钮
	$("body").on("click",".popup-link",function(){
		var urlstr=$(this).attr("data-url");
			if (typeof(urlstr)!= "undefined"){
				if(urlstr!="null"){
					myfn.AjaxFn(urlstr,$(".popup-box"));
					$(".popup-box").show();
					$(".popup_bg").show();
				}
			}
				
			
			
	});
	
	
});