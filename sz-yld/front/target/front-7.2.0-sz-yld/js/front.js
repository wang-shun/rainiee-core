$(function(){
	//帮助中心
	$(document).ready(function(){
		$(".problem_type_con dt").click(function(){//点击til
			$(this).next().toggle();;//当前的下一个显示或隐藏
			if($(this).hasClass("up")){//如果有UP的样式
				$(this).removeClass("up");//就移除
			}else{
				$(this).addClass("up");//如果没有就加上
			}
		  });
	});
	// 积分商城购物车产品名称
	 $('.order_info .title').mouseover(function(){
          $(this).children().next().show();
     }); 
	 $('.order_info .title').mouseout(function(){
          $(this).children().next().hide();
     }); 
	 // 编辑收货地址
	 $('.shipping_address li').mouseover(function(){
          $(this).find('.operation').show();
     }); 
	 $('.shipping_address li').mouseout(function(){
		  $(this).find('.operation').hide();
     }); 
	  
});

