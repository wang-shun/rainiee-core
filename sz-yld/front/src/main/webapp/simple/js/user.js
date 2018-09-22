$(function(){ 	 
	 /*认证提示*/
	 $(".certification i").mouseover(function() {
		 $(this).children().show();
	 });
	 $(".certification i").mouseout(function() {
		 $(this).children().hide();
	 });
	 $(".hover_tips_con .close").click(function() {
		$(this).parent().parent().hide();
	});
});

$(document).ready(function(){
	
	//左侧菜单展开
/*  $(".sidemenu .item").click(function(){//点击til
	  if($(this).next().is(':hidden')){
			  $(".item.up").next().slideToggle("slow");
			  $(".item.up").removeClass("up");	
		  }

	  $(this).next().slideToggle("slow");//当前的下一个显示或隐藏
	  if($(this).hasClass("up")){//如果有UP的样式
		  $(this).removeClass("up");//就移除
	  }else{
		  $(this).addClass("up");//如果没有就加上
	  }
	});*/
  
  //站内消息
	$(".message_con .con").click(function(){
		$(this).next().toggle();
	});
  
});


//安全信息点击展开
function show(obj){  
      var obj=document.getElementById(obj);  //获得id为b的容器
	  //如果下面的内容显示的话就隐藏
	  if(obj.style.display=="block"){
	     obj.style.display="none";
	  }else 
	  obj.style.display="block";
}
function openShutManager(oSourceObj,oTargetObj,shutAble,oOpenTip,oShutTip){
var sourceObj = typeof oSourceObj == "string" ? document.getElementById(oSourceObj) : oSourceObj;
var targetObj = typeof oTargetObj == "string" ? document.getElementById(oTargetObj) : oTargetObj;
var openTip = oOpenTip || "";
var shutTip = oShutTip || "";
if(targetObj.style.display!="none"){
   if(shutAble) return;
   targetObj.style.display="none";
   if(openTip  &&  shutTip){
    sourceObj.innerHTML = shutTip; 
   }
} else {
   targetObj.style.display="block";
   if(openTip  &&  shutTip){
    sourceObj.innerHTML = openTip; 
   }
}
}