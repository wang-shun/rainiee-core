define(['text!indexTemplate/index/index.html',
        'text!commonTemplate/bottom.html',
        'commonTool/slide',
        'commonTool/tool', 'userModel/UserModel'
], function(indexContentTemplate,bottom,Slide,tool, UserModel){
    var indexView = DMJS.DMJSView.extend({
        id: 'indexContent',
        name: 'indexContent',
        tagName: 'div',
        className: "index", 
        events: {
           'tap .JGoBuy':'JGoBuy'
        },
        init: function(options){
           	 _.extend(this, options); 
           	 var self = this;
           	 this.noDestroy=false;
           	 this.userModel = new UserModel();
           	 this.$el.html(_.template(indexContentTemplate, self.data));// 将tpl中的内容写入到 this.el 元素中  
        },
        render: function(){
            var self=this; 
            $('#loanContentList').append(_.template(bottom,self));
    		self.loadTemplate();
            self.loadScroller();
            self.statistics();
            self.noticeUpDown()
            self.noticeList();
            self.tjBidList();
            self.buildAddList();
            DMJS.CommonTools.backConst("BUSINESS.DONATION_BID_SWITCH",function(){
           	 	if(this.Const=="true"){
           	 		$("#dona").removeClass("uhide");
           	 	}
           	});
           	DMJS.CommonTools.backConst("Mall.IS_MALL",function(){
           	 	if(this.Const=="true"){
           	 		$("#mall").removeClass("uhide");
           	 	}
           	});
            return this;
        },
        goTop:function(){
        	var self=this;
        	self.scroller.scrollToPage(0,0,500);
        },
        'noticeList':function(){//公告
        	var self=this;
        	self.controller.indexModel.noticeList({
        		
        		"success":function(_data){
        			var data=_data.data,noticeStr='';
        			for(var i=0;i<data.length;i++){
        				noticeStr+="<li><div action='index/index/informationArticle/"+data[i].id+"/WZGG/"+data[i].releaseTime+"'><span class='notice_icon'></span><span>"+DMJS.CommonTools.renderStringLen(data[i].title,12)+"</span><span class='ufr'>"+data[i].releaseTime+"</span></div></li>"
        			}
        			if(!!noticeStr){
        				$('.Jnotice').removeClass('uhide');
        				$('#noticeL').html(noticeStr);
        				self.noticeUpDown();
        			}else{
        				$('.Jnotice').addClass('uhide');
        			}
        		},
        		
        		'error': function(response) {
						DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"), response.description);
				}
        	});
        },
        loadTemplate:function(){
        		var self=this;
           		self.noticeSlider=new Slide("lisssss", "H", function(e){
        				},false,function(e){
        			},undefined,function(index){
        			});
        		self.loadScroller();
        },
        'noticeUpDown':function(){
        	var self=this;
        	self.noticeSlider=new Slide("noticeL", "V", function(e){
        				showProint(this.currentPoint);
        				},false,function(e){
        			},undefined,function(index){
        			});
        			self.startAddAutoTurn(2,2000);
        },
        'statistics':function(){//首页统计
        	var self=this;
        	self.controller.indexModel.indexStatic({
        		
        		"success":function(_data){
        		
        			var data=_data.data;	
        			$('#tzze').html(tool.changeAmount(data.rzzje));
        			$('#tzzsy').html(tool.changeAmount(data.yhzsy));
        			if(data.yhjys<1000){
        				$('#tzzrs').html(data.yhjys);
        			}
        			else{
        				$('#tzzrs').html(tool.changeAmount(data.yhjys));
        			}
        		
        		},
        		
        		'error': function(response) {
						DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"), response.description);
				}
        	});
        },
        'tjBidList':function(num){//首页标
        	var self=this;
        	self.controller.indexModel.tjBidList({
        		
        		"success":function(_data){
        			var data=_data.data[0];
        			if(!!data){
        				self.firstBid(data);
        			}else{
						 var params={};
                         params["pageSize"]=1;
                         params["pageIndex"]=1;
						 self.controller.indexModel.bidList({//投资列表
								cancelLightbox: true,
								data: params,
								async:false,
                                cancelLightbox:true,
                                "success":function(_data){
                                	var data=_data.data[0];
                                	if(data){
                                		self.firstBid(data);
                                	}else{
                                		$('.Jbid').hide();
                                	}
                                }
                        });
        				self.loadScroller();
        			}
        			
        		},
        		'error': function(response) {
						DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"), response.description);
				}
        	});
        },
        'firstBid':function(data){
        	var self = this;
        	self.data = data;
        	if(data.jlRate<=0 && data.isJlb !=="true"){
        		data.jlRate = '';
        	}
        	else{
        		data.jlRate = (self.data.jlRate*100).toFixed(2);
        	}
            self.bidId=data.id;
			self.publicDate=data.publicDate;
			//重新写判断
			if(data.isXsb=="true"){
				$("#bidFlagText").html("新手推荐");
			}else if(data.isJlb=="true"){
				$("#bidFlagText").html("奖励推荐");
			}else if(data.isYfb=="true"){
				$("#bidFlagText").html("预 发 布");
			}else if(data.status=="TBZ"){
				$("#bidFlagText").html("投 标 中");
			}else{
				$("#bidFlag").addClass("uhide");
			}
			$('#bid_flag').text(data.flag);
			$('#bid_bidTitle').text(DMJS.CommonTools.renderStringLen(data.bidTitle,13));
			if(data.rate >= 0){
				$('#bid_jlrate').parent().removeClass("uhide");
				$('#bid_jlrate').text((data.rate*100).toFixed(2));
			}
			if(data.jlRate > 0){	
				$('#bid_addrate').text('+'+(data.jlRate)+'%');
			}
			$('#bid_yq').text(data.cycle);
			$('#bid_je').html(tool.gaibAmount(data.amount,'元',1));
			if(data.isDay=="F"){
				$('#isDay').html("个月");
			}else if(data.isDay=="S"){
				$('#isDay').html("天");
			}
			if(data.status == "YFB"){
				$("#bid_buy").html("即将发布");
				$("#bid_buy").removeClass("b-c-blue");
				$("#bid_buy").removeClass("lc-gra6");
				$("#bid_buy").addClass("yfb-c");
			}else if(data.status == "DFK"){
				$("#bid_buy").html("抢购结束");
				$("#bid_buy").removeClass("b-c-blue");
				$("#bid_buy").removeClass("yfb-c");
				$("#bid_buy").addClass("lc-gra6");
			}
			else{
				$("#bid_buy").html("马上去抢购");
				$("#bid_buy").removeClass("yfb-c");
				$("#bid_buy").removeClass("lc-gra6");
				$("#bid_buy").addClass("b-c-blue");
			}
			$('#bid_zffs').text(data.minBidingAmount);
			var bili = (data.amount*1-data.remainAmount*1)/data.amount*1*100;
			if(bili < 1 && bili >0){
				bili = 1;
			}else if(bili < 100 && bili>99){
				bili = 99
			}else{
				bili = Math.round(bili);	
			}
			var x = 0,y = 160-Math.round(160*bili/100),h=15;
			if(y<=10){
				y-=7;
			}else if(y>=150){
				y+=7;
			}
			DMJS.startThread("drawSingleConvas",100,-1,function(){
				self.drawSingleConvas(x,y,15,data.rate);
				x-=20;
				if(x==-240){x=0;}
			});
       },
        'JGoBuy':function(){
        	var self=this;
        	if(!!self.bidId)
        	DMJS.router.navigate("index/index/projectInvDetail/"+self.bidId+"/"+self.publicDate,true);
        },
        'buildAddList':function(){//首页广告
        	var self=this,$html_tmp='',$umal_tmp='';
        	self.controller.indexModel.addList({
        	
        		"success":function(_data){
        			var addSilder=self.$el.find("#sliderAdd"),prointArea=self.$el.find("#proint"),advData=_data.data;
        			for(var i=0;i<advData.length;i++){
        					if(!advData[i].advUrl){
        					
		        					$html_tmp+="<li><img style='height:100%' src='"+advData[i].advImg+"' title='"+advData[i].advTitle+"'/></li>";
		            				$umal_tmp+="<div class=\"uc-a-for1 uwh-for1 c-gra-for2 umar-r-for\"></div>";
		        				}else{
		        					$html_tmp+="<li><a  target='_blank' class='lu-height085' href='"+advData[i].advUrl+"'><img src='"+advData[i].advImg+"' title='"+advData[i].advTitle+"'/></a></li>";
		            				$umal_tmp+="<div class=\"uc-a-for1 uwh-for1 c-gra-for2 umar-r-for\"></div>";
		        				}
        			}
        			addSilder.append($html_tmp);
        			prointArea.append($umal_tmp);
        			var showProint=function(index){
        				prointArea.children("div").removeClass("c-wh").addClass("c-gra-for2");
        				$(prointArea.children("div")[index]).removeClass("c-gra-for2").addClass("c-wh");
        			}
        			self.sliderAdd=new Slide("sliderAdd", "H", function(e){
        				showProint(this.currentPoint);
        				},false,function(e){
        			},undefined,function(index){
        				if(_data.data&&_data.data[index]&&_data.data[index].advUrl){
        					window.open(_data.data[index].advUrl,"_system");
        				}
        			});
        			self.startAddAutoTurn(1);
        			showProint(0);
        		},
        		"complete":function(){
        			self.$el.find("#sliderAddWrapper").removeClass("background_loading").addClass("background_noData");
        			
        		}
        	});
       },
        "startAddAutoTurn":function(num,timeS){
        	var Slider,self=this,domStr;
        	var timeS=timeS||5000;
			if(num===1){
				Slider=self.sliderAdd;
				domStr="addPicAutoTurn";
				
			}else{
				Slider=self.noticeSlider;
				domStr="noticeAutoTurn";
			}
        		
        	if(Slider.length<1){
        		return;
        	}
			DMJS.startThread(domStr,timeS,-1,function(){
				console.log(this);
				if(Slider.isBack&&!Slider.hasPrev()){
					Slider.isBack=false;
				}
				if(!Slider.isBack&&!Slider.hasNext()){
					Slider.isBack=true;
				}
				if(Slider.isBack){
					Slider.toPrev();
				}else{
					Slider.toNext();
				}
				if(num===1){
					var addSilder=self.$el.find("#sliderAdd"),prointArea=self.$el.find("#proint");
	    			prointArea.children("div").removeClass("c-wh").addClass("c-gra-for2");
	    			$(prointArea.children("div")[self.sliderAdd.currentPoint]).removeClass("c-gra-for2").addClass("c-wh");
				}
				
			});
       },
       "onDestroy":function(){
        	var self=this;
        	DMJS.getThread("addPicAutoTurn")?DMJS.stopThread("addPicAutoTurn"):DMJS.stopThread("noticeAutoTurn");
        	DMJS.stopThread("drawSingleConvas");
        	if(self.noDestory){
        		self.sliderAdd&&self.sliderAdd.suspend();
            	self.slider&&self.slider.suspend();
        	}else{
            	self.sliderAdd&&self.sliderAdd.destroy();
            	self.slider&&self.slider.destroy();
        	}
        },
       drawSingleConvas:function(x,y,h,rate){//起始x坐标y坐标和函数定点高
        	var canvas = document.getElementById('canvas');
        	if(!canvas){
        		return false;
        	}
            context = canvas.getContext('2d');
           	context.clearRect(0,0,800,200);
			context.beginPath();
			context.fillStyle="rgba(153,224,255,0.8)";
			context.moveTo(x, y);
            context.bezierCurveTo(x, y,x+60, y-h, x+120, y);
            context.quadraticCurveTo(x+180, y+h, x+240, y);
            context.bezierCurveTo(x+240, y,x+300, y-h, x+360, y);
            context.quadraticCurveTo(x+420, y+h, x+480, y);
            
            
            context.lineTo(x+480,y+200);
            context.lineTo(x-40,y+200);
            context.fill();
			context.closePath();
			
			context.beginPath();
            context.fillStyle="rgba(51,193,255,0.8)";
            
            context.moveTo(x, y);
            context.bezierCurveTo(x-30, y,x+30, y+h, x+90, y);
            context.quadraticCurveTo(x+150, y-h, x+210, y);
            context.bezierCurveTo(x+210, y,x+270, y+h, x+330, y);
            context.quadraticCurveTo(x+390, y-h, x+450, y);
            context.bezierCurveTo(x+450, y,x+510, y+h, x+570, y);
            context.lineTo(x+570,y+200);
            context.lineTo(x-40,y+200);
            context.fill();
			context.closePath();
			context.fillStyle = '#FFA200';
			context.font="normal 1.5rem arial";
       		context.fillText('年化利率',80 ,60);
       		context.font="normal 2rem arial";
       		context.fillText((rate*100).toFixed(2)+"%",80 ,100);
       		context.textAlign="center";
       		context.textBaseline="middle";
    }
    });

    return indexView;
});
