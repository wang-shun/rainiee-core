// JavaScript Document
var videoyes=checkVideo();
var myVideo=document.getElementById("homeVideo");
var swfVideo=document.getElementById("vplayer");
var jdtLeft=0;
var setVideo=null;
var play_boolean=false;
var video_time;

	//鼠标画上显示操作栏
	$(".video-container").hover(function(){
		$(this).find(".video-btn").stop(false,false).animate({"bottom":0},400);
	},function(){
		$(this).find(".video-btn").stop(false,false).animate({"bottom":-50},400);
	});
	
	

	//点击播放
	$(".center-play-btn").click(function(){
		if(play_boolean)
		{
			play_boolean=false;
		}
		else{
			play_boolean=true;
		}
		videoPlay();
	});
	
	//点击播放
	$(".play-video").click(function(){ 
		if(play_boolean)
		{
			play_boolean=false;
		}
		else{
			play_boolean=true;
		}
		videoPlay();
	});
	
	
	//静音控制
	$(".sound-video").click(function(){
		if(videoyes)
		{
			if(myVideo.muted)
			{
				myVideo.muted=false;
				$(this).removeClass("no-sound-video");
			}
			else
			{
				myVideo.muted=true;
				$(this).addClass("no-sound-video");
			}
		}
	});
	
	//全屏
	$(".full-video-btn").click(function(){
		if($(this).hasClass("small-video-btn"))
		{
			$(this).removeClass("small-video-btn");
			$(".video-container").css({"position":"absolute","z-index":0});
		}else{
			$(this).addClass("small-video-btn");
			$(".video-container").css({"position":"fixed","z-index":99999});
			
		}
		
	});
	
	$(".jdt-box").click(function(e){
		if(play_boolean)
		{
			e=e||window.event;
			jdtLeft=$(".jdt-box").offset().left;
			var x=e.pageX||e.clientX+document.body.scroolLeft;
			var nowx=(x-jdtLeft)/$(this).width();
			if(videoyes)
			{
				myVideo.currentTime=video_time*nowx;
			}
			else
			{
				var zhen=Math.round(nowx*video_time);
				swfVideo.TGotoFrame(zhen);
			}
		}
		
	});
	function videoJdt(){
		setVideo=setInterval(function(){
		var nowtime=Math.round(myVideo.currentTime);
		$(".time-container").html(videoTimeShow(nowtime));
		if(nowtime>=video_time)
		{
			myVideo.currentTime=0;
		}
		var w=nowtime/Math.round(video_time)*100;
		$(".jdt-box .jdt-bg").css({"width":w+"%"});
		$(".jdt-box .jdt-now-icon").css({"left":w+"%"});
		},20);
		
	}
	
	function swfJdt(){
		setVideo=setInterval(function(){
		$(".time-container").html(swfVideo.CurrentFrame());
		var nowtime=swfVideo.CurrentFrame();
		var w=nowtime/video_time*100;
		$(".jdt-box .jdt-bg").css({"width":w+"%"});
		$(".jdt-box .jdt-now-icon").css({"left":w+"%"});
		},20);	
	}
//时间
function videoTimeShow(num){
	var hh=0,mm=0,h1=0,h2=0,m1=0,m2=0;
	 hh = Math.round(num/60);
	 mm=Math.round(num%60);
	 h1=(hh/10+"").split('.')[0];
	 h2=Math.round(hh%10);
	 m1=(mm/10+"").split('.')[0];
	 m2=Math.round(mm%10);
	 if(m1<1)
	 {
		 m1=0;
	 }
	if(h1<1)
	 {
		 h1=0;
	 }
	 return h1+""+h2+":"+m1+m2; 
}
	
//判断浏览器是否支持Video
function checkVideo() {
    if (!!document.createElement('video').canPlayType) {
        var vidTest = document.createElement("video");
        oggTest = vidTest.canPlayType('video/ogg; codecs="theora, vorbis"');
        if (!oggTest) {
            h264Test = vidTest.canPlayType('video/mp4; codecs="avc1.42E01E, mp4a.40.2"');
            if (!h264Test) {
                return false;
            }
            else {
                if (h264Test == "probably") {
                    return true;
                }
                else {
                    return false;
                }
            }
        }
        else {
            if (oggTest == "probably") {
                return true;
            }
            else {
               return false;
            }
        }
    }
    else {
        return false;
    }
}

//播放
	function videoPlay(){
		
		if(play_boolean)
		{
			$(".play-video").addClass("stop-video");
			$(".center-play-btn").stop(false,false).animate({"opacity":0},400);
				if(videoyes)
				{
					video_time=myVideo.duration;
					myVideo.play();
					videoJdt();
				}
				else
				{
					video_time=swfVideo.TotalFrames;
					swfJdt();
					swfVideo.play();
				}
		}
		else 
  		{
			clearInterval(setVideo);
			$(".play-video").removeClass("stop-video");
			$(".center-play-btn").stop(false,false).animate({"opacity":1},400);
			if(videoyes)
			{
				myVideo.pause();
			}
			else
			{
				swfVideo.StopPlay();
			}
		}
		
	}