/**
 * Created by luoyiming on 2016/8/19.
 */

(function () {

    function LuckDraw(box) {
        this.list = [0, 1, 2, 5, 8, 7, 6, 3];
		this.winning=null;
        this.box = box;
        this._index = 0;
        this.minnum = null;
		this.maxnum=null;
        this.random_num=null;//Math.floor(Math.random()*600+100);
		this.allTime = 40;
        this.v1=0.97;
        this.v2=1.03;
		this.frag=true;

    }

    LuckDraw.prototype = {
        init: function (num) {
           if(this.frag)
		   {
			   	this.winning=num;
			   	this.initialValue();
            	this.animate();
				$(".lucky-prize-button").find(".text").text("抽奖中");
				this.frag=false;
		   }
        },
		
		
		//初始值
		initialValue:function(){
				this.minnum = 0;
				this.maxnum=400+(this.winning-1)*10-(this._index*10);
				this.random_num=this.maxnum;
		},
		
		
		//旋转动画
		animate:function(){
			var that = this;
			if(this.minnum<this.random_num-50)
            {
                if(this.minnum%4==0&&this.allTime>=10)
                {
                    this.allTime-=5;
                }
            }
            else
            {
                if(this.minnum%4==0)
                {
                    this.allTime+=5;
                }
            }

            that.LiStyle();
            that.AddIndex();
            this.minnum++;

            if (this.minnum <=this.maxnum) {
                setTimeout(function () {
                    that.animate();
                }, that.allTime);
            }
            else
            {
                
				$("#AialogLuckyDraw").show();
				var text=this.box.find("li").eq(that.list[that._index]).find(".lucky-prize-text").text();
				$("#AialogLuckyDraw .text-data").text(text);
				$(".lucky-prize-button").find(".text").text("再次抽奖");
				that.frag=true;
				
                //alert("恭喜你，你中奖了！ 你的奖品是第"+this.list[this._index]+"格的礼物！");
                return false;
            }

		},


        //索引
        AddIndex: function () {
            if (this.minnum % 10 == 1) {
                this._index++;
                if (this._index >= 8) {
                    this._index = 0;
                }
            }
        },

        //转动效果
        LiStyle: function () {
            this.box.find("li").removeClass("cur").find(".protective-pic").remove();
            this.box.find("li").eq(this.list[this._index]).addClass("cur").append("<div class='protective-pic'></div>");
        },

        //停止
        IsStop: function () {

        }


    }
	
	
	//调用方法
    var luckDraw = new LuckDraw($("#LuckyDraw"));
    $("#LuckyDraw  .lucky-prize-btn").click(function () {
	
        luckDraw.init(3);
    });
	
	$(".obtain-btn").click(function(){
		$("#AialogLuckyDraw").hide();
		});

})();
