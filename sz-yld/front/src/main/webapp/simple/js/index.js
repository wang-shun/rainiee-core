// 焦点图
(function(d,D,v){d.fn.responsiveSlides=function(h){var b=d.extend({auto:!0,speed:1E3,timeout:7E3,pager:!1,nav:!1,random:!1,pause:!1,pauseControls:!1,prevText:"Previous",nextText:"Next",maxwidth:"",controls:"",namespace:"rslides",before:function(){},after:function(){}},h);return this.each(function(){v++;var e=d(this),n,p,i,k,l,m=0,f=e.children(),w=f.size(),q=parseFloat(b.speed),x=parseFloat(b.timeout),r=parseFloat(b.maxwidth),c=b.namespace,g=c+v,y=c+"_nav "+g+"_nav",s=c+"_here",j=g+"_on",z=g+"_s",
o=d("<ul class='"+c+"_tabs "+g+"_tabs' />"),A={"float":"left",position:"relative"},E={"float":"none",position:"absolute"},t=function(a){b.before();f.stop().fadeOut(q,function(){d(this).removeClass(j).css(E)}).eq(a).fadeIn(q,function(){d(this).addClass(j).css(A);b.after();m=a})};b.random&&(f.sort(function(){return Math.round(Math.random())-0.5}),e.empty().append(f));f.each(function(a){this.id=z+a});e.addClass(c+" "+g);h&&h.maxwidth&&e.css("max-width",r);f.hide().eq(0).addClass(j).css(A).show();if(1<
f.size()){if(x<q+100)return;if(b.pager){var u=[];f.each(function(a){a=a+1;u=u+("<li><a href='#' class='"+z+a+"'>"+a+"</a></li>")});o.append(u);l=o.find("a");h.controls?d(b.controls).append(o):e.after(o);n=function(a){l.closest("li").removeClass(s).eq(a).addClass(s)}}b.auto&&(p=function(){k=setInterval(function(){var a=m+1<w?m+1:0;b.pager&&n(a);t(a)},x)},p());i=function(){if(b.auto){clearInterval(k);p()}};b.pause&&e.hover(function(){clearInterval(k)},function(){i()});b.pager&&(l.bind("click",function(a){a.preventDefault();
b.pauseControls||i();a=l.index(this);if(!(m===a||d("."+j+":animated").length)){n(a);t(a)}}).eq(0).closest("li").addClass(s),b.pauseControls&&l.hover(function(){clearInterval(k)},function(){i()}));if(b.nav){c="<a href='#' class='"+y+" prev'>"+b.prevText+"</a><a href='#' class='"+y+" next'>"+b.nextText+"</a>";h.controls?d(b.controls).append(c):e.after(c);var c=d("."+g+"_nav"),B=d("."+g+"_nav.prev");c.bind("click",function(a){a.preventDefault();if(!d("."+j+":animated").length){var c=f.index(d("."+j)),
a=c-1,c=c+1<w?m+1:0;t(d(this)[0]===B[0]?a:c);b.pager&&n(d(this)[0]===B[0]?a:c);b.pauseControls||i()}});b.pauseControls&&c.hover(function(){clearInterval(k)},function(){i()})}}if("undefined"===typeof document.body.style.maxWidth&&h.maxwidth){var C=function(){e.css("width","100%");e.width()>r&&e.css("width",r)};C();d(D).bind("resize",function(){C()})}})}})(jQuery,this,0);
$(function() {
    $(".rslides").responsiveSlides({
        auto: true,
        pager: true,
        nav: true,
        speed: 700,
        minwidth: 1680
    });
		
    initInvestData("sbtz");
    initInfoMTBD();
    initInfoData('WDHYZX','mtbdDataBody');
	initInfoData('HLWJRYJ','jrxyDatayBody');

});



/*SuperSlide*/
(function(a){a.fn.slide=function(b){return a.fn.slide.defaults={type:"slide",effect:"fade",autoPlay:!1,delayTime:500,interTime:2500,triggerTime:150,defaultIndex:0,titCell:".hd li",mainCell:".bd",targetCell:null,trigger:"mouseover",scroll:1,vis:1,titOnClassName:"on",autoPage:!1,prevCell:".prev",nextCell:".next",pageStateCell:".pageState",opp:!1,pnLoop:!0,easing:"swing",startFun:null,endFun:null,switchLoad:null,playStateCell:".playState",mouseOverStop:!0,defaultPlay:!0,returnDefault:!1},this.each(function(){var c=a.extend({},a.fn.slide.defaults,b),d=a(this),e=c.effect,f=a(c.prevCell,d),g=a(c.nextCell,d),h=a(c.pageStateCell,d),i=a(c.playStateCell,d),j=a(c.titCell,d),k=j.size(),l=a(c.mainCell,d),m=l.children().size(),n=c.switchLoad,o=a(c.targetCell,d),p=parseInt(c.defaultIndex),q=parseInt(c.delayTime),r=parseInt(c.interTime);parseInt(c.triggerTime);var P,t=parseInt(c.scroll),u=parseInt(c.vis),v="false"==c.autoPlay||0==c.autoPlay?!1:!0,w="false"==c.opp||0==c.opp?!1:!0,x="false"==c.autoPage||0==c.autoPage?!1:!0,y="false"==c.pnLoop||0==c.pnLoop?!1:!0,z="false"==c.mouseOverStop||0==c.mouseOverStop?!1:!0,A="false"==c.defaultPlay||0==c.defaultPlay?!1:!0,B="false"==c.returnDefault||0==c.returnDefault?!1:!0,C=0,D=0,E=0,F=0,G=c.easing,H=null,I=null,J=null,K=c.titOnClassName,L=j.index(d.find("."+K)),M=p=defaultIndex=-1==L?p:L,N=p,O=m>=u?0!=m%t?m%t:t:0,Q="leftMarquee"==e||"topMarquee"==e?!0:!1,R=function(){a.isFunction(c.startFun)&&c.startFun(p,k,d,a(c.titCell,d),l,o,f,g)},S=function(){a.isFunction(c.endFun)&&c.endFun(p,k,d,a(c.titCell,d),l,o,f,g)},T=function(){j.removeClass(K),A&&j.eq(defaultIndex).addClass(K)};if("menu"==c.type)return A&&j.removeClass(K).eq(p).addClass(K),j.hover(function(){P=a(this).find(c.targetCell);var b=j.index(a(this));I=setTimeout(function(){switch(p=b,j.removeClass(K).eq(p).addClass(K),R(),e){case"fade":P.stop(!0,!0).animate({opacity:"show"},q,G,S);break;case"slideDown":P.stop(!0,!0).animate({height:"show"},q,G,S)}},c.triggerTime)},function(){switch(clearTimeout(I),e){case"fade":P.animate({opacity:"hide"},q,G);break;case"slideDown":P.animate({height:"hide"},q,G)}}),B&&d.hover(function(){clearTimeout(J)},function(){J=setTimeout(T,q)}),void 0;if(0==k&&(k=m),Q&&(k=2),x){if(m>=u)if("leftLoop"==e||"topLoop"==e)k=0!=m%t?(0^m/t)+1:m/t;else{var U=m-u;k=1+parseInt(0!=U%t?U/t+1:U/t),0>=k&&(k=1)}else k=1;j.html("");var V="";if(1==c.autoPage||"true"==c.autoPage)for(var W=0;k>W;W++)V+="<li>"+(W+1)+"</li>";else for(var W=0;k>W;W++)V+=c.autoPage.replace("$",W+1);j.html(V);var j=j.children()}if(m>=u){l.children().each(function(){a(this).width()>E&&(E=a(this).width(),D=a(this).outerWidth(!0)),a(this).height()>F&&(F=a(this).height(),C=a(this).outerHeight(!0))});var X=l.children(),Y=function(){for(var a=0;u>a;a++)X.eq(a).clone().addClass("clone").appendTo(l);for(var a=0;O>a;a++)X.eq(m-a-1).clone().addClass("clone").prependTo(l)};switch(e){case"fold":l.css({position:"relative",width:D,height:C}).children().css({position:"absolute",width:E,left:0,top:0,display:"none"});break;case"top":l.wrap('<div class="tempWrap" style="overflow:hidden; position:relative; height:'+u*C+'px"></div>').css({top:-(p*t)*C,position:"relative",padding:"0",margin:"0"}).children().css({height:F});break;case"left":l.wrap('<div class="tempWrap" style="overflow:hidden; position:relative; width:'+u*D+'px"></div>').css({width:m*D,left:-(p*t)*D,position:"relative",overflow:"hidden",padding:"0",margin:"0"}).children().css({"float":"left",width:E});break;case"leftLoop":case"leftMarquee":Y(),l.wrap('<div class="tempWrap" style="overflow:hidden; position:relative; width:'+u*D+'px"></div>').css({width:(m+u+O)*D,position:"relative",overflow:"hidden",padding:"0",margin:"0",left:-(O+p*t)*D}).children().css({"float":"left",width:E});break;case"topLoop":case"topMarquee":Y(),l.wrap('<div class="tempWrap" style="overflow:hidden; position:relative; height:'+u*C+'px"></div>').css({height:(m+u+O)*C,position:"relative",padding:"0",margin:"0",top:-(O+p*t)*C}).children().css({height:F})}}var Z=function(a){var b=a*t;return a==k?b=m:-1==a&&0!=m%t&&(b=-m%t),b},$=function(b){var c=function(c){for(var d=c;u+c>d;d++)b.eq(d).find("img["+n+"]").each(function(){var b=a(this);if(b.attr("src",b.attr(n)).removeAttr(n),l.find(".clone")[0])for(var c=l.children(),d=0;c.size()>d;d++)c.eq(d).find("img["+n+"]").each(function(){a(this).attr(n)==b.attr("src")&&a(this).attr("src",a(this).attr(n)).removeAttr(n)})})};switch(e){case"fade":case"fold":case"top":case"left":case"slideDown":c(p*t);break;case"leftLoop":case"topLoop":c(O+Z(N));break;case"leftMarquee":case"topMarquee":var d="leftMarquee"==e?l.css("left").replace("px",""):l.css("top").replace("px",""),f="leftMarquee"==e?D:C,g=O;if(0!=d%f){var h=Math.abs(0^d/f);g=1==p?O+h:O+h-1}c(g)}},_=function(a){if(!A||M!=p||a||Q){if(Q?p>=1?p=1:0>=p&&(p=0):(N=p,p>=k?p=0:0>p&&(p=k-1)),R(),null!=n&&$(l.children()),o[0]&&(P=o.eq(p),null!=n&&$(o),"slideDown"==e?(o.not(P).stop(!0,!0).slideUp(q),P.slideDown(q,G,function(){l[0]||S()})):(o.not(P).stop(!0,!0).hide(),P.animate({opacity:"show"},q,function(){l[0]||S()}))),m>=u)switch(e){case"fade":l.children().stop(!0,!0).eq(p).animate({opacity:"show"},q,G,function(){S()}).siblings().hide();break;case"fold":l.children().stop(!0,!0).eq(p).animate({opacity:"show"},q,G,function(){S()}).siblings().animate({opacity:"hide"},q,G);break;case"top":l.stop(!0,!1).animate({top:-p*t*C},q,G,function(){S()});break;case"left":l.stop(!0,!1).animate({left:-p*t*D},q,G,function(){S()});break;case"leftLoop":var b=N;l.stop(!0,!0).animate({left:-(Z(N)+O)*D},q,G,function(){-1>=b?l.css("left",-(O+(k-1)*t)*D):b>=k&&l.css("left",-O*D),S()});break;case"topLoop":var b=N;l.stop(!0,!0).animate({top:-(Z(N)+O)*C},q,G,function(){-1>=b?l.css("top",-(O+(k-1)*t)*C):b>=k&&l.css("top",-O*C),S()});break;case"leftMarquee":var c=l.css("left").replace("px","");0==p?l.animate({left:++c},0,function(){l.css("left").replace("px","")>=0&&l.css("left",-m*D)}):l.animate({left:--c},0,function(){-(m+O)*D>=l.css("left").replace("px","")&&l.css("left",-O*D)});break;case"topMarquee":var d=l.css("top").replace("px","");0==p?l.animate({top:++d},0,function(){l.css("top").replace("px","")>=0&&l.css("top",-m*C)}):l.animate({top:--d},0,function(){-(m+O)*C>=l.css("top").replace("px","")&&l.css("top",-O*C)})}j.removeClass(K).eq(p).addClass(K),M=p,y||(g.removeClass("nextStop"),f.removeClass("prevStop"),0==p&&f.addClass("prevStop"),p==k-1&&g.addClass("nextStop")),h.html("<span>"+(p+1)+"</span>/"+k)}};A&&_(!0),B&&d.hover(function(){clearTimeout(J)},function(){J=setTimeout(function(){p=defaultIndex,A?_():"slideDown"==e?P.slideUp(q,T):P.animate({opacity:"hide"},q,T),M=p},300)});var ab=function(a){H=setInterval(function(){w?p--:p++,_()},a?a:r)},bb=function(a){H=setInterval(_,a?a:r)},cb=function(){z||(clearInterval(H),ab())},db=function(){(y||p!=k-1)&&(p++,_(),Q||cb())},eb=function(){(y||0!=p)&&(p--,_(),Q||cb())},fb=function(){clearInterval(H),Q?bb():ab(),i.removeClass("pauseState")},gb=function(){clearInterval(H),i.addClass("pauseState")};if(v?Q?(w?p--:p++,bb(),z&&l.hover(gb,fb)):(ab(),z&&d.hover(gb,fb)):(Q&&(w?p--:p++),i.addClass("pauseState")),i.click(function(){i.hasClass("pauseState")?fb():gb()}),"mouseover"==c.trigger?j.hover(function(){var a=j.index(this);I=setTimeout(function(){p=a,_(),cb()},c.triggerTime)},function(){clearTimeout(I)}):j.click(function(){p=j.index(this),_(),cb()}),Q){if(g.mousedown(db),f.mousedown(eb),y){var hb,ib=function(){hb=setTimeout(function(){clearInterval(H),bb(0^r/10)},150)},jb=function(){clearTimeout(hb),clearInterval(H),bb()};g.mousedown(ib),g.mouseup(jb),f.mousedown(ib),f.mouseup(jb)}"mouseover"==c.trigger&&(g.hover(db,function(){}),f.hover(eb,function(){}))}else g.click(db),f.click(eb)})}})(jQuery),jQuery.easing.jswing=jQuery.easing.swing,jQuery.extend(jQuery.easing,{def:"easeOutQuad",swing:function(a,b,c,d,e){return jQuery.easing[jQuery.easing.def](a,b,c,d,e)},easeInQuad:function(a,b,c,d,e){return d*(b/=e)*b+c},easeOutQuad:function(a,b,c,d,e){return-d*(b/=e)*(b-2)+c},easeInOutQuad:function(a,b,c,d,e){return 1>(b/=e/2)?d/2*b*b+c:-d/2*(--b*(b-2)-1)+c},easeInCubic:function(a,b,c,d,e){return d*(b/=e)*b*b+c},easeOutCubic:function(a,b,c,d,e){return d*((b=b/e-1)*b*b+1)+c},easeInOutCubic:function(a,b,c,d,e){return 1>(b/=e/2)?d/2*b*b*b+c:d/2*((b-=2)*b*b+2)+c},easeInQuart:function(a,b,c,d,e){return d*(b/=e)*b*b*b+c},easeOutQuart:function(a,b,c,d,e){return-d*((b=b/e-1)*b*b*b-1)+c},easeInOutQuart:function(a,b,c,d,e){return 1>(b/=e/2)?d/2*b*b*b*b+c:-d/2*((b-=2)*b*b*b-2)+c},easeInQuint:function(a,b,c,d,e){return d*(b/=e)*b*b*b*b+c},easeOutQuint:function(a,b,c,d,e){return d*((b=b/e-1)*b*b*b*b+1)+c},easeInOutQuint:function(a,b,c,d,e){return 1>(b/=e/2)?d/2*b*b*b*b*b+c:d/2*((b-=2)*b*b*b*b+2)+c},easeInSine:function(a,b,c,d,e){return-d*Math.cos(b/e*(Math.PI/2))+d+c},easeOutSine:function(a,b,c,d,e){return d*Math.sin(b/e*(Math.PI/2))+c},easeInOutSine:function(a,b,c,d,e){return-d/2*(Math.cos(Math.PI*b/e)-1)+c},easeInExpo:function(a,b,c,d,e){return 0==b?c:d*Math.pow(2,10*(b/e-1))+c},easeOutExpo:function(a,b,c,d,e){return b==e?c+d:d*(-Math.pow(2,-10*b/e)+1)+c},easeInOutExpo:function(a,b,c,d,e){return 0==b?c:b==e?c+d:1>(b/=e/2)?d/2*Math.pow(2,10*(b-1))+c:d/2*(-Math.pow(2,-10*--b)+2)+c},easeInCirc:function(a,b,c,d,e){return-d*(Math.sqrt(1-(b/=e)*b)-1)+c},easeOutCirc:function(a,b,c,d,e){return d*Math.sqrt(1-(b=b/e-1)*b)+c},easeInOutCirc:function(a,b,c,d,e){return 1>(b/=e/2)?-d/2*(Math.sqrt(1-b*b)-1)+c:d/2*(Math.sqrt(1-(b-=2)*b)+1)+c},easeInElastic:function(a,b,c,d,e){var f=1.70158,g=0,h=d;if(0==b)return c;if(1==(b/=e))return c+d;if(g||(g=.3*e),Math.abs(d)>h){h=d;var f=g/4}else var f=g/(2*Math.PI)*Math.asin(d/h);return-(h*Math.pow(2,10*(b-=1))*Math.sin((b*e-f)*2*Math.PI/g))+c},easeOutElastic:function(a,b,c,d,e){var f=1.70158,g=0,h=d;if(0==b)return c;if(1==(b/=e))return c+d;if(g||(g=.3*e),Math.abs(d)>h){h=d;var f=g/4}else var f=g/(2*Math.PI)*Math.asin(d/h);return h*Math.pow(2,-10*b)*Math.sin((b*e-f)*2*Math.PI/g)+d+c},easeInOutElastic:function(a,b,c,d,e){var f=1.70158,g=0,h=d;if(0==b)return c;if(2==(b/=e/2))return c+d;if(g||(g=e*.3*1.5),Math.abs(d)>h){h=d;var f=g/4}else var f=g/(2*Math.PI)*Math.asin(d/h);return 1>b?-.5*h*Math.pow(2,10*(b-=1))*Math.sin((b*e-f)*2*Math.PI/g)+c:.5*h*Math.pow(2,-10*(b-=1))*Math.sin((b*e-f)*2*Math.PI/g)+d+c},easeInBack:function(a,b,c,d,e,f){return void 0==f&&(f=1.70158),d*(b/=e)*b*((f+1)*b-f)+c},easeOutBack:function(a,b,c,d,e,f){return void 0==f&&(f=1.70158),d*((b=b/e-1)*b*((f+1)*b+f)+1)+c},easeInOutBack:function(a,b,c,d,e,f){return void 0==f&&(f=1.70158),1>(b/=e/2)?d/2*b*b*(((f*=1.525)+1)*b-f)+c:d/2*((b-=2)*b*(((f*=1.525)+1)*b+f)+2)+c},easeInBounce:function(a,b,c,d,e){return d-jQuery.easing.easeOutBounce(a,e-b,0,d,e)+c},easeOutBounce:function(a,b,c,d,e){return 1/2.75>(b/=e)?d*7.5625*b*b+c:2/2.75>b?d*(7.5625*(b-=1.5/2.75)*b+.75)+c:2.5/2.75>b?d*(7.5625*(b-=2.25/2.75)*b+.9375)+c:d*(7.5625*(b-=2.625/2.75)*b+.984375)+c},easeInOutBounce:function(a,b,c,d,e){return e/2>b?.5*jQuery.easing.easeInBounce(a,2*b,0,d,e)+c:.5*jQuery.easing.easeOutBounce(a,2*b-e,0,d,e)+.5*d+c}});
/*公告*/
jQuery(".notice-list-container").slide({titCell:".hd ul",mainCell:".bd ul",autoPage:true,effect:"topLoop",autoPlay:true});
/*合作伙伴*/
jQuery(".cooperation_bd").slide({titCell:".hd ul",mainCell:".picList",autoPage:true,effect:"leftLoop",autoPlay:true,vis:5,trigger:"click"});
/*首页发布*/
jQuery(".release_bd1").slide({titCell:".hd ul",mainCell:".infoList1",autoPage:true,effect:"topLoop",autoPlay:true,vis:3});
jQuery(".release_bd2").slide({titCell:".hd ul",mainCell:".infoList2",autoPage:true,effect:"topLoop",autoPlay:true,vis:3});


function initInfoMTBD(){
	$.post(_infoUrl,{type:"MTBD"},function(returnData){
		returnData = eval("("+returnData+")");
		var infoList = returnData.infoList;
		$("#MTBDBody .ul").remove();
		
		var div = "<ul class=\"media_ul\">";
		if(infoList!=null){
			for(var i = 0; i < infoList.length ; i++){
				var item = infoList[i];
				var xqUrl = _mtbdXqUrl;
				xqUrl += item.F01+".html?type=MTBD";
				if(i == 0)
				{
					setImage(xqUrl,getFileUrl(item.F09));
				}
				div +=" <li><div class='text' id='mtbdDataBody'><a href=\""+xqUrl+"\"><i></i><span>"+item.F06+"</span></a></div></li>"
			}
		}else{
			$(".media-pic").hide();
			div += "<li class='p10' style=\"text-align: left;margin-left:110px;\">暂无数据</li>";
		}
		div += "</ul>";
		div += "</div>";
		$(div).appendTo("#MTBDBody");
	});
}

function setImage(url,img)
{
	$(".media-pic a").attr("href",url);
	$(".media-pic img").attr("src",img);
}
//媒体报道鼠标悬停
function mtbtMedia(){
	$(".media li").hover(
			function(){
				$(this).find(".txt").stop().animate({height:"262px"},400);
				$(this).find(".txt h3").stop().animate({paddingTop:"60px"},400);
			},function(){
				$(this).find(".txt").stop().animate({height:"0"},400);
				$(this).find(".txt h3").stop().animate({paddingTop:"0px"},400);
			}
	);
}


function initInfoData(infoType,htmlBody){

	$.post(_infoUrl,{type:infoType},function(returnData){
		returnData = eval("("+returnData+")");
		var infoList = returnData.infoList;
		$("#"+infoType+" .ul").remove();
		
		var div = "<ul class=\"trade_ul\">";
		if(infoList!=null){
			for(var i = 0; i < infoList.length ; i++){
				var item = infoList[i];

				var xqUrl = "";
				/*if(infoType == "MTBD"){
				 xqUrl = _mtbdXqUrl;
				 }else if(infoType == "WDHYZX"){
				 xqUrl = _wdhyzxXqUrl;
				 }else if(infoType == "WZGG"){
				 xqUrl = _wzggXqUrl;
				 }else if(infoType == "HLWJRYJ"){
				 xqUrl = _hlwjryjXqUrl;
				 }*/

				if(infoType == "WZGG"){
					xqUrl = _wzggXqUrl;
				}else {
					xqUrl = _mtbdXqUrl;
				}
				xqUrl += item.F01+".html?type="+infoType;
				div +="<li><div class=\"text\"><a href='"+xqUrl+"'><span>"+item.F06+"</span></a></div></li>"
				/*div += "<li><a href='"+xqUrl+"'><span class=\"title\">"+item.F06+"</span><em class=\"time\">"+formatDate(item.F12)+"</em></a></li>";*/
			}
		}else{
			div += "<li style=\"border: 0px;text-align: center; background: none;\">暂无数据</li>";
		}
		div += "</ul>";
		$(div).appendTo("#"+htmlBody);
	});
}

function initInvestData(type){
	$.post(_investUrl,{type:type},function(returnData){
		returnData = eval("("+returnData+")");
		var dataList = returnData.investList;
		var typ;
		var dataHtml ="" ;	
		if(type == "sbtz"){
			typ="manager1";
			$(".investment-title .more-li a").attr("href",_sbtzMoreUrl);
			dataHtml += sbtzHtml(dataList);

		}
		else if(type == "zqzr")
		{	typ="manager2";
			$(".investment-title .more-li a").attr("href",_zqzrMoreUrl);
			dataHtml += zqzrHtml(dataList);
		}

		$(".investment-title li").removeClass("selectd");
		$("#"+typ).addClass("selectd");
		// $(".invest_item money_item .bd").remove();
		$("#investmentList").html('');
		$("#investmentList").html(dataHtml);
		cirprogress();		
		
	});
	if(type == "sbtz"){
		setTab('manager',1,2)
	}else{
		setTab('manager',2,2)	
	}
}

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


function sbtzHtml(investList){
	var html = "<div id=\"con_manager_1\" class=\"manager_bd\">";
	html += "<div class=\"pro_til\">";
	html += "<ul class=\"clearfix\">";
	html += "<li class=\"pro_name\">项目名称</li>";
	html += "<li class=\"pro_rate\">预期年化收益率</li>";
	html += "<li class=\"pro_term\">期限</li>";
	html += "<li class=\"pro_money\">金额</li>";
	html += " <li class=\"pro_progr\">进度</li>";
	html += " <li class=\"pro_action\">操作</li>";
	html += " </ul>";
	html += " </div>";
	html += "<div class=\"cre_con\">";
	if(investList != null){
		for(var i = 0; i < investList.length ; i++){
			var item = investList[i];
			if(item.F02==tjbId){
				//如果是推荐标，则不用重复展示
				continue;
			}	
			if((item.F11 == "YFB" || item.F11 == "TBZ") && item.F31 == 'APP' ){
				//发布项目投资端选择在APP端显示，那么此项目处于  “预发布”、“投资中”状态时，只在APP、微信端显示此项目，
				continue;
			}
			html += " <ul class=\"clearfix\">";
			html += "<li class=\"cre_name\">";
			if(item.F28 == "S"){
				html += "<span class=\"item_icon novice_icon pl\">新</span>";
			}
			if(item.F29 == "S"){
				html += "<span class=\"item_icon reward_icon pl\">奖</span>";
			}
			if(item.F16 == "S"){
				html += "<span class=\"item_icon dan_icon\">保</span>";
			}else if(item.F17 == "S"){
				html += "<span class=\"item_icon di_icon\">抵</span>";
			}else if(item.F18 == "S"){
				html += "<span class=\"item_icon shi_icon\">实</span>";
			}else{
				html += "<span class=\"item_icon xin_icon pl\">信</span>";
			}
			html +="<a class=\"cre_name_til\" title=\""+item.F04+"\" href=\""+_sbtzXqUrl+item.F02+".html\">"+subStringLength(item.F04,10,"...")+"</a>";
			html +="</li>";
			html +="<li class=\"cre_rate\">"+formatYearRate(item.F07)+"%</li>";
			html +="<li class=\"cre_remain\">"+(item.F19 == "S" ? "<span>"+item.F20 +"天</span>" : "<span>"+item.F10+"个月</span>")+"</li>";
			
			if(item.F11 == "HKZ" || item.F11 == "YJQ" || item.F11 == "YDF" || item.F11 == "YZR"){
				item.F06 = item.F06 - item.F08;
				item.proess = 1;
				item.F08 = 0;
			}
			if(item.proess * 100 > 0 && item.proess * 100 < 1){
				item.proess = 0.01;
			}
			html += (item.F06 >= 10000 ? "<li class=\"cre_value\">"+formatMoney(item.F06,1)+"万元</li>" : "<li class=\"cre_value\">"+formatMoney(item.F06)+"元</li>");
			if(item.F11 == "YFB"){
				html +="<li class=\"cre_money\">";
				html +="<span class=\"re_time re_con\">"+formatDateTime(item.F13)+"</span>";
				html +="<span class=\"re_con\">即将开始</span>";
				html +="</li>";	
			}else{
				html +="<li class=\"cre_money\">";
				html +="<span class=\"cirprogress\">"+(item.proess == 0 ? 0 : parseInt(item.proess*100))+"%</span>";
				html +="</li>";		
			}	
			if(item.F11 == "TBZ"){
				if(isLogin){
					if(isHmd || !isInvest){
					/*	if(isHmd|| !isZrr){*/
					/*		html += "<li class=\"cre_action\">";
							html += " <a class=\"grab_btn btn-gray\" href=\""+_sbtzXqUrl+item.F02+".html\">";
							html += "<em></em>";
							html += "<i>立即投标</i>";
							html += "</a>";
							html += "</li>";*/
						html +="<li class=\"cre_action\">"; 
						html += " <a class=\"grab_btn btn-gray\" href=\""+_sbtzXqUrl+item.F02+".html\">立即投标</a>";			
					/*	html += "<em></em>";
						html += "<i>"+getBidChineseName(item.F11)+"</i>";
						html += "</a>";*/
						html += "</li>";
					}else{
						html += "<li class=\"cre_action\">";
						html += " <a class=\"grab_btn cre_btn\" href=\""+_sbtzXqUrl+item.F02+".html\">";
						html += "<em></em>";
						html += "<i>立即投标</i>";
						html += "</a>";
						html += "</li>";
					}
				}else{
					html += "<li class=\"cre_action\">";
					html += " <a class=\"grab_btn cre_btn\" href=\""+_sbtzXqUrl+item.F02+".html\">";
					html += "<em></em>";
					html += "<i>立即投标</i>";
					html += "</a>";
					html += "</li>";
				}
			}else{
				html +="<li class=\"cre_action\">"; 
				html += " <a class=\"grab_btn btn-gray\" href=\""+_sbtzXqUrl+item.F02+".html\">"+getBidChineseName(item.F11)+"</a>";			
			/*	html += "<em></em>";
				html += "<i>"+getBidChineseName(item.F11)+"</i>";
				html += "</a>";*/
				html += "</li>";
			}
			html += "</ul>";
		}	
	}else{
		html += "<p colspan=\"7\" align=\"center\" style=\"text-align:center; padding-top:20px;\">暂无数据</p>";	
	}
	html += "</div>";
	return html;
	
	/*
	var html = " <table class='table'><thead>";
	html += "<tr>";
	html += "<th width=\"7%\"></th>";
	html += "<th>项目名称</th>";
	html += "<th width=\"10%\">年化利率</th>";
	html += "<th width=\"10%\">借款期限</th>";
	html += "<th width=\"18%\">借款金额</th>";
	html += "<th width=\"18%\">进度</th>";
	html += "<th width=\"15%\">操作</th>";
	html += "</thead></tr>";
	if(investList != null){
		for(var i = 0; i < investList.length ; i++){
			var item = investList[i];
			if(item.F02==tjbId){
				//如果是推荐标，则不用重复展示
				continue;
			}
			if((item.F11 == "YFB" || item.F11 == "TBZ") && item.F31 == 'APP' ){
				//发布项目投资端选择在APP端显示，那么此项目处于  “预发布”、“投资中”状态时，只在APP、微信端显示此项目，
				continue;
			}
			html += "<tr>";
			html += "<td class='tr'><span class=\"pr10\">";
			if(item.F28 == "S"){
				html += "<span class=\"item_icon novice_icon\">新</span>";
			}
			if(item.F29 == "S"){
				html += "<span class=\"item_icon reward_icon\">奖</span>";
			}
			if(item.F16 == "S"){
				html += "<span class=\"item_icon dan_icon\">保</span>";
			}else if(item.F17 == "S"){
				html += "<span class=\"item_icon di_icon\">抵</span>";
			}else if(item.F18 == "S"){
				html += "<span class=\"item_icon shi_icon\">实</span>";
			}else{
				html += "<span class=\"item_icon xin_icon\">信</span>";
			}

			html += "</span>";
			html += "</td>";

			html += "<td class=\"td-title tl\">";
			html += "<div class=\"tititle-containertle\">";
			html += "<a title=\""+item.F04+"\" href=\""+_sbtzXqUrl+item.F02+".html\">"+subStringLength(item.F04,10,"...")+"</a>";
			html += "</div>";
			html += "</td>";
			html += "<td><div class=\"f20 gray3\">"+formatYearRate(item.F07)+"%</div></td>";
			html += "<td>"+(item.F19 == "S" ? "<div class=\"f20 gray3\">"+item.F20 +"天</div>" : "<div class=\"f20 gray3\">"+item.F10+"个月</div>")+"</td>";
			html += "<td>";
			if(item.F11 == "HKZ" || item.F11 == "YJQ" || item.F11 == "YDF" || item.F11 == "YZR"){
				item.F06 = item.F06 - item.F08;
				item.proess = 1;
				item.F08 = 0;
			}
			if(item.proess * 100 > 0 && item.proess * 100 < 1){
				item.proess = 0.01;
			}
			html += (item.F06 >= 10000 ? "<div class=\"f20 gray3\">"+formatMoney(item.F06,1)+"万元</div>" : "<div class=\"f20 gray3\">"+formatMoney(item.F06)+"元</span>");
			html += "</td>";
			
			html += "<td>";
			if(item.F11 == "YFB"){
				html += "<div class='highlight' id='sbtzTime"+item.F02+"'>"+formatDateTime(item.F13)+" 即将开始</div>";
				html += "<script type='text/javascript'>sbtzCountdown("+item.F13+","+item.F02+");</script>";
			}else{
				html += "<div class=\"progress-bar\">";
				html += "<p class=\"p-bg\"><span class=\"load-bg\" style=\"width:"+parseInt(item.proess*100)+"%;\"></span></p>";
				html += "<span class=\"num\">"+(item.proess == 0 ? 0 : parseInt(item.proess*100))+"%</span>";
                html += "</div>";
			}
			html += "</td>";
			html += "<td>";
			if(item.F11 == "TBZ"){
				if(isLogin){
					if(isHmd || !isInvest){
						if(isHmd|| !isZrr){
						html += "<span class=\"btn-border-gray\">立即投资</span>";
					}else{
						html += "<a href=\""+_sbtzXqUrl+item.F02+".html\" class=\"btn-border-blue\">立即投资</a>";
					}
				}else{
					html += "<a href=\""+_sbtzXqUrl+item.F02+".html\" class=\"btn-border-blue\">立即投资</a>";
				}
			}else{
				html += "<span class=\"btn-border-gray\">"+getBidChineseName(item.F11)+"</span>";
			}
			html += "</td>";
			
			html += "</tr>";
		}
	}else{
		html += "<tr><td colspan=\"7\" align=\"center\">暂无数据</td></tr>";
	}
	html += "</table>";
	return html;
*/
	}

function zqzrHtml(investList){
 var html = "<div id=\"con_manager_2\" class=\"manager_bd\">";
	 html += "<div class=\"pro_til cre_til\">";
	 html += "<ul class=\"clearfix\">";
	 html += "<li class=\"cre_name\">项目名称</li>";
	 html += "<li class=\"cre_rate\">预期年化收益率</li>";
	 html += "<li class=\"cre_remain\">剩余期数</li>";
	 html += "<li class=\"cre_value\">债权价值</li>";
	 html += "<li class=\"cre_money\">转让价格</li>";
	 html += "<li class=\"cre_action\">操作</li>";
	 html += "</ul>";
	 html += "</div>";
	 html += "<div class=\"cre_con\">";
	if(investList != null){
		for(var i = 0; i < investList.length ; i++){
			var item = investList[i];
			html += "<ul class=\"clearfix\">";
			html += "<li class=\"cre_name\">";			
			if(item.F28 == "S"){
				html += "<span class=\"item_icon novice_icon pl\">新</span>";
			}
			if(item.F29 == "S"){
				html += "<span class=\"item_icon reward_icon pl\">奖</span>";
			}
			if(item.F16 == "S"){
				html += "<span class=\"item_icon dan_icon\">保</span>";
			}else if(item.F17 == "S"){
				html += "<span class=\"item_icon di_icon\">抵</span>";
			}else if(item.F18 == "S"){
				html += "<span class=\"item_icon shi_icon\">实</span>";
			}else{
				html += "<span class=\"item_icon xin_icon pl\">信</span>";
			}
			html += "<a href=\""+_zqxqUrl+item.F01+".html\" class=\"cre_name_til\">"+item.F12+"</a>";
			html += "</li>";
			html += "<li class=\"cre_rate\">"+formatYearRate(item.F14)+"%</li>";
			html += "<li class=\"cre_remain\">"+item.F23+"/"+item.F22+"</li>";
			html += "<li class=\"cre_value\">"+formatMoney(item.F03)+"元</li>";
			html += "<li class=\"cre_money\">"+formatMoney(item.F02)+"元</li>";	
			if(isLogin){
				if (item.F06=="YJS") {
					html += " <li class=\"cre_action\"> <a class=\"grab_btn btn-gray\" href=\"javascript:;\">已转让</a></li>";
				}else{
					if(isHmd || !isInvest){
						if(isHmd || !isZrr){
							html += " <li class=\"cre_action\"> <a class=\"grab_btn btn-gray\" href=\"javascript:;\">购买</a></li>";	
						}else{
							var isRiskMatchStr=isRiskMatch(item.F30,userRiskLevel);							  
							html += " <li class=\"cre_action\"><a class=\"grab_btn cre_btn\" href=\""+_zqxqUrl+item.F01+".html\"><em></em><i>购买</i></a></li>";
						}
					}
				}
			}else{
				if (item.F06=="YJS") {
					html += " <li class=\"cre_action\"> <a class=\"grab_btn btn-gray\" href=\"javascript:;\">已转让</a></li>";
				}else{
					html += " <li class=\"cre_action\"><a class=\"grab_btn cre_btn\" href=\""+_zqxqUrl+item.F01+".html\"><em></em><i>购买</i></a></li>";
				}
			}
			html += "</ul>";
		}	
		
	}else{
		html += "<p colspan=\"7\" align=\"center\" style=\"text-align:center; padding-top:20px;\">暂无数据</p>";
	}
	html +="</div>"
		html +="</div>"
	return html;
	
	/*
	var html = "<table class=\"table\"><thead>";
	html += "<tr>";
	html += "<th width=\"6%\"></th>";
	html += "<th>项目名称</th>";
	html += "<th width=\"10%\">年化利率</th>";
	html += "<th width=\"10%\">剩余期数</th>";
	html += "<th width=\"15%\">债权价值</th>";
	html += "<th width=\"15%\">待收本息</th>";
	html += "<th width=\"15%\">转让价格</th>";
	html += "<th width=\"15%\">操作</th>";
	html += "</thead></tr>";
	if(investList != null){
		for(var i = 0; i < investList.length ; i++){
			var item = investList[i];
			html += "<tr>";
			html += "<td class=\"tr\"><span class=\"pr10\">";
			if(item.F28 == "S"){
				html += "<span class=\"item_icon novice_icon\">新</span>";
			}
			if(item.F29 == "S"){
				html += "<span class=\"item_icon reward_icon\">奖</span>";
			}
			if(item.F16 == "S"){
				html += "<span class=\"item_icon dan_icon\">保</span>";
			}else if(item.F17 == "S"){
				html += "<span class=\"item_icon di_icon\">抵</span>";
			}else if(item.F18 == "S"){
				html += "<span class=\"item_icon shi_icon\">实</span>";
			}else{
				html += "<span class=\"item_icon xin_icon\">信</span>";
			}

			html += "</span>";
			html += "</td>";
			html += "<td class=\"td-title tl\">";
			html += "<div class=\"title-container\">";
			html += "<a title=\""+item.F12+"\" href=\""+_zqxqUrl+item.F01+".html\">"+subStringLength(item.F12,10,"...")+"</a>";
			html += "</div>";
			html += "</td>";
			html += "<td><div class=\"f20 gray3\">"+formatYearRate(item.F14)+"%</div></td>";
			html += "<td>";
			html += "<div class=\"f20\">"+item.F23+"/"+item.F22+"</div>";
			html += "</td>";
			html += "<td><div class=\"f18 gray3\">"+formatMoney(item.F03)+"元</div></td>";
			html += "<td><div class=\"f18 gray3\">"+formatMoney(item.dsbx)+"元</div></td>";
			html += "<td><div class=\"f18 gray3\">"+formatMoney(item.F02)+"元</div></td>";
			html += "<td>";
			if(isLogin){
				if (item.F06=="YJS") {
					html += "<span class=\"btn-border-gray\">已转让</span>";
				}else{
					if(isHmd || !isInvest){
						if(isHmd || !isZrr){
						html += "<span class=\"btn-border-gray\">购买</span>";
					}else{
						var isRiskMatchStr=isRiskMatch(item.F30,userRiskLevel);
						html += "<a href=\"javascript:void(0)\" onclick=\"buy("+item.F02+","+item.F03+","+item.F25+",'"+isInvestLimit+"','"+isRiskMatchStr+"','"+isOpenRiskAccess+"')\" style=\"cursor: pointer;\" class=\"btn06\">购买</a>";
						html += "<a href=\""+_zqxqUrl+item.F01+".html\" class=\"btn-border-blue\">购买</a>";
					}
				}
			}else{
				if (item.F06=="YJS") {
					html += "<span class=\"btn-border-gray\">已转让</span>";
				}else{
					html += "<a href=\""+_zqxqUrl+item.F01+".html\" class=\"btn-border-blue\">购买</a>";
				}
			}
			html += "</td>";
			html += "</tr>";
		}
	}else{
		html += "<tr><td colspan=\"8\" align=\"center\">暂无数据</td></tr>";
	}
	html += "</table>";
	return html;
*/
	}

function isRiskMatch(productRiskLevel,userRiskLevel){
	if(productRiskLevel=='BSX'){
		return "true";
	}
	if(productRiskLevel=='JSX'&&userRiskLevel!='BSX'){
		return "true";
	}
	if(productRiskLevel=='WJX'&&(userRiskLevel!='BSX' && userRiskLevel!='JSX')){
		return "true";
	}
	if(productRiskLevel=='JQX'&&(userRiskLevel=='JQX'||userRiskLevel=='JJX')){
		return "true";
	}
	if(productRiskLevel=='JJX'&&userRiskLevel=='JJX'){
		return "true";
	}
	return "false";
}

var formatDate = function (time) {  
	if(time==null){
		return "--";
	}
	var date = new Date();
	date.setTime(time);
    var y = date.getFullYear();  
    var m = date.getMonth() + 1;  
    m = m < 10 ? '0' + m : m;  
    var d = date.getDate();  
    d = d < 10 ? ('0' + d) : d;  
    return y + '-' + m + '-' + d ;  
}

var formatDateTime = function (time) {  
	if(time==null){
		return "--:";
	}
	var date = new Date();
	date.setTime(time);
    var y = date.getFullYear();  
    var m = date.getMonth() + 1;  
    m = m < 10 ? '0' + m : m;  
    var d = date.getDate();  
    d = d < 10 ? ('0' + d) : d;
    var h = date.getHours();
    h = h < 10 ? ('0' + h) : h;
    var mi = date.getMinutes();
    mi = mi < 10 ? ('0' + mi) : mi;
    return y + '-' + m + '-' + d + " "+h+":"+mi;  
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

function formatMoney(s,flg) {
	if(flg == 1){
		if(s >= 10000){
			s = s / 10000;
			if(s.toString().indexOf(".") < 0){
				return s.toString() + ".00";
			}
			if(s.toString().substring(s.toString().indexOf(".")+1,s.toString().length).length==1){
				return s.toString() + "0";
			}
			return s.toFixed(2);
		}
	}
    if (/[^0-9\.]/.test(s)){
    	return "0.00";
    }
    if (s == null || s == ""){
    	return "0.00";
    }
    s = s.toString().replace(/^(\d*)$/, "$1.");
    s = (s + "00").replace(/(\d*\.\d\d)\d*/, "$1");
    s = s.replace(".", ",");
    var re = /(\d)(\d{3},)/;  
    while (re.test(s)){
    	s = s.replace(re, "$1,$2");
    }
    s = s.replace(/,(\d\d)$/, ".$1");
    return s;  
}
//格式化数字，只显示整数部分，如：10,000
function formatMoneyNotPoint(s) {
	s = s.toString().replace(/^(\d*)$/, "$1.");
    s = (s + "00").replace(/(\d*\.\d\d)\d*/, "$1");
    s = s.replace(".", ",");
    var re = /(\d)(\d{3},)/;  
    while (re.test(s)){
    	s = s.replace(re, "$1,$2");
    }
    s = s.replace(/,(\d\d)$/, ".$1");
    return s.substring(0,s.indexOf("."));
}
//格式化年化利率两位小数点
function formatYearRate(yearRate){
	if(isEmpty(yearRate)){
		return;
	}
	return parseFloat(yearRate * 100).toFixed(2);
}

function getInteger(num,flg){
	if(isEmpty(num)){
		return;
	}
	if(typeof(num)=="string"){
		num = num.replace(",","");
	}
	if(isNaN(Number(num))){
		return;
	}
	var tem = num.toString();
	if(flg==1){
		//获取数字的整数部分
		return tem.substring(0,tem.toString().indexOf("."));
	}else if(flg==2){
		//获取数字的小数部分
		return tem.substring(tem.toString().indexOf(".")+1,tem.toString().length);
	}else{
		return;
	}
}

function getBidChineseName(status){
	var rtnChineseName;
	switch(status){
	   case "HKZ": 
		   	 rtnChineseName = "还款中";
		     break;
	   case "YFB":
		     rtnChineseName = "预发布";
		     break;
	   case "DFK":
		     rtnChineseName = "待放款";
		     break;
	   case "YJQ": 
		     rtnChineseName = "已结清";
		     break;
	   case "YDF": 
		     rtnChineseName = "已垫付";
		     break;
	   case "DSH":
		   	 rtnChineseName = "待审核";
		     break;
	   case "SQZ":
		   	 rtnChineseName = "申请中";
		     break;
	   case "YLB":
			 rtnChineseName = "已流标";
			 break;
	   case "YZR":
			 rtnChineseName = "已转让";
			 break;
	   default:
		   	 rtnChineseName = "";
		     break;
	   }
	return rtnChineseName;
}

function getGyChineseName(status){
	var rtnChineseName;
	switch(status){
		case "SQZ":
			rtnChineseName = "申请中";
			break;
		case "DSH":
			rtnChineseName = "待审核";
			break;
		case "DFB":
			rtnChineseName = "待发布";
			break;
		case "JKZ":
			rtnChineseName = "捐款中";
			break;
		case "YJZ":
			rtnChineseName = "已捐助";
			break;
		case "YZF":
			rtnChineseName = "已作废";
			break;
		default:
			rtnChineseName = "";
			break;
	}
	return rtnChineseName;
}

function getFileUrl(fileName){
	var foo = fileName.split("-");
	if(foo.length != 5){
		return "";
	}
	var filePath = "/fileStore/";
	var separator = "/";
	var year = parseInt(foo[0], 16);
	var month = parseInt(foo[1], 16);
	var day = parseInt(foo[2], 16);
	var type = parseInt(foo[3], 16);
	var id = parseInt(foo[4].substring(0,foo[4].indexOf(".")), 16);
	filePath += type+separator+year+separator+month+separator+day+separator+id+"."+foo[4].substring(foo[4].indexOf(".")+1,foo[4].length);
	return filePath;
}

/**
 * Created by faith on 2016/5/16
 */

$(document).ready(function(){

    //banner
    $(".rslides").responsiveSlides({
        auto: true,
        pager: true,
        nav: true,
        speed: 700,
        minwidth: 1680
    });

    //活动公告
    notice();
	
	
    /*首页发布*/
    jQuery(".release_bd").slide(
        {
            titCell:".hd ul",
            mainCell:".infoList",
            autoPage:true,
            effect:"topLoop",
            autoPlay:true,
            vis:2
        }
    );

    /*合作伙伴*/
    jQuery(".cooperation_bd").slide(
        {
            titCell:".hd ul",
            mainCell:".picList",
            autoPage:true,
            effect:"leftLoop",
            autoPlay:true,
            vis:6,
            trigger:"click"
        }
    );
 
    $(".cirprogress").each(function(){
       var num=$(this).text();
        num=parseInt(num);
        alert(num);
        $(this).css('background-position',-(num*54.11)+'px'+" "+0);
    });

});


//活动公告
function notice(){
    var n_h = $(".notice .bd").height();
	var li = $(".notice .bd li");
    var len = $(".notice .bd li").length;
    var i = 0;
    var parent = $(".notice .bd > ul");
    /*if (len > 1) {
        var timer = setInterval(function (i) {
            if (i == len - 1) {
                parent.animate({ "margin-top": 0 }, 300)
                i = 0;
            } else {
                parent.animate({ "margin-top": "-=" + n_h }, 300)
                i++;
            }
        }, 4000);
    }*/
	
	if (len > 1) {
        var timer = setInterval(function(){
				scoll(i);
			}, 4000);
	}
		
	
		function scoll(){
			if (i >= len - 1) {
				parent.animate({ "margin-top": 0 }, 300)
				i = 0;
			} else {
				parent.animate({ "margin-top": "-=" + n_h }, 300)
				i++;
			}
		}
	
	
	
	
	li.hover(function(){
		clearInterval(timer);
	},
	function(){
		  timer = setInterval(function(){
			 scoll()
			 }, 4000);
		
	});
	
	
}

 function cirprogress(){
	    $(".cirprogress").each(function(){
	        var num=$(this).text();
	         num=parseInt(num);
	         $(this).css('background-position',-(num*54.11)+'px'+" "+0);
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

 $("#returnTop").returnTop();