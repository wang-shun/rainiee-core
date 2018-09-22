var heightArr = new Array(),
    bottom_item;
$(function() {
	if($("#tab_container").length > 0) { 
	    var itemLength = $(".about_content .item-container").length;
	    for (var i = 1; i <= itemLength; i++) {
	        heightArr.push($("#mod0" + i).offset().top - 65);
	
	    }
	    bottom_item = heightArr[heightArr.length - 1] + $(".about_content .item-container").eq(itemLength - 1).height();
	    //获取要定位元素距离浏览器顶部的距离
	    
	    var navH = $("#tab_container").offset().top;
	    
	    //滚动条事件
	    $(window).scroll(function() {
	        //获取滚动条的滑动距离
	        var scroH = $(this).scrollTop();
	        //alert(scroH);
	        //滚动条的滑动距离大于等于定位元素距离浏览器顶部的距离，就固定，反之就不固定
	        tabNav(scroH, navH, bottom_item);
	        tabNavlist(itemLength, scroH);
	    });
	
	    /*$("#tab_container li a").click(function(){
	    	if($("body, html").is(":animated")){ return false;}
	    	var index=$(this).parent().index();
	    	$("body, html").stop().animate({"scrollTop":heightArr[index]+"px"},500);
		
	    });*/
	}

});

function tabNav(scroH, navH, bottom_item) {
    if (scroH >= navH && scroH < bottom_item) {
        $("#tab_container").css({
           "position": "fixed",
           "z-index": "999",
           "top": 0,
			"box-shadow": "0 1px 3px #ddd"
        });
    } else if (scroH < navH) {
        $("#tab_container").css({
            "position": "static",
            "box-shadow": "none"
        });
    } else if (scroH > bottom_item) {
        $("#tab_container").css({
            "position": "static",
            "box-shadow": "none"
        });
    }
}

function tabNavlist(itemLength, scollHeight) {
    $("#tab_container").find("li").removeClass("cur");

    for (var i = 0; i <= heightArr.length; i++) {
        var start = heightArr[i];
        var next = heightArr[i + 1];
        if (typeof(next) == "undefined") {
            next = heightArr[heightArr.length - 1] + $(".about_content .item-container").eq(itemLength - 1).height();
        }
        if (scollHeight < heightArr[0]) {
            $("#tab_container li").first().attr("class", "cur");
        } else if (scollHeight >= start && scollHeight < next) {
            $("#tab_container li").eq(i).attr("class", "cur");
        } else if (scollHeight >= heightArr[heightArr.length - 1] && scollHeight <= bottom_item) {
            $("#tab_container li").eq(itemLength - 1).attr("class", "cur");
        }
    }
}

//锚点滑动效果
$(function(){
	$('#tab_container li>a').click(function() {
		/* Act on the event */
    	var href = $(this).attr("data");
    	var pos = $(href).offset().top +"px";
    	$("html,body").animate({ scrollTop: pos }, 500);
	});
});