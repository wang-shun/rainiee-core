define(['text!indexTemplate/donation/donationBenefit.html',
		'text!indexTemplate/donation/donationBenefitList.html','commonClass/scroll/PTRScroll','commonTool/slide'], function(donationTemplate,listTemplate,PTRScroll,Slide){
    var donationView = DMJS.DMJSView.extend({//项目投资
        id: 'donationViewContent',
        name: 'donationViewContent',
        tagName: 'div',
        className: "donationViewContent", 
        events: {
           "tap #donationDetail" : 'donationDetail'
        },
        init: function(options){
           var self = this;
            var obj={page:{
        	pageIndex:1,
        	pageSize:10,
        	isOver:false}};
            _.extend(self, options,obj);
        },
        render: function(){
            var self=this; 
            self.donationInfo(self.donationList); 
            return this;
        },
        donationDetail:function(e){
			 var $dom=$(e.target);
                if(!$dom.is("#donationDetail >div>div")){
                    $dom=$dom.parents("#donationDetail >div>div");
                }
                var id = $dom.attr("data-id");
                var status = $dom.attr("data-status");
				var isTimeEnd = $dom.attr("data-isTimeEnd");
				if(!(id&&status&&isTimeEnd)){console.log('参数不对'); return false;}
				DMJS.router.navigate('index/index/donationDetail/'+id+"/"+status+"/"+isTimeEnd,true);
		},
		donationInfo:function(callback){
			var self=this;
				self.controller.indexModel.gyLoanInfo({
					"data" : {},'cancelLightbox':true,
					"success" : function(response) {
						  self.gyLoanInfo=self.gyLoanInfo||{};
						  _.extend(self.gyLoanInfo,response.data);
						  self.$el.html(_.template(donationTemplate, self.gyLoanInfo)); // 将tpl中的内容写入到 this.el 元素中  
						  $("div[item='listItem']").css('minHeight',((document.body.clientHeight||document.documentElement.clientHeight)-$("#header").height())+"px");//内容块占满高度
						  if(response.data.advs.length<1){
						  	$("#sliderAddWrappers").addClass("uhide");
						  };
						  self.buildAddList(response.data.advs);
						  self.loadListScroller(self.page);
						   
						  callback.apply(self,[self.page]);
					},
					"error" : function(response) {
							DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"),response.description);
					},
					"complete":function(){
//						$("#sliderAddWrapper").addClass("background_noData");
					},
				});
		},
        donationList:function(page,callBack){
        	var self=this;
        	var __call=function(){
                    	var dom=self.$el.find("div[item='listItem']")[0];
                 		var len=dom.children.length;
                 		if(len<10){
                 			if(len==0){
                 			dom.innerHTML=wrapView.noSearchDiv;
                 			}
            				return self.scroller.disablePullUpToLoadMore();
            			}else if(page.isOver/*||$('#FlipPrompt')*/){
            				return self.scroller.disablePullUpToLoadMore();

            			}else{
            				return self.scroller.enablePullUpToLoadMore();
            			}
            			
                    }
        	self.controller.indexModel.gyLoanList({'data':{pageIndex:page.pageIndex,pageSize:page.pageSize},'cancelLightbox':true,
        	"noCache":true,
			"success":function(response){
				var $dom=self.$el.find("div[item='listItem']");
				 if(!response.data||response.data.length<page.pageSize){
                    page.isOver=true;
                    DMJS.CommonTools.popTip("已经加载完所有数据！");
                    }
				    var progress=0;
			    	for(var item in response.data){
			    		progress = response.data[item].progress*100;
						if(progress<=1&&progress!=0){
							response.data[item].progress = 1/100;
						}else if(progress<100&&progress>=90){
							response.data[item].progress = 99/100;
						}
			    	}
				     
                     if(page.pageIndex == 1){$dom.html(_.template(listTemplate,response));}
                     else{$dom.append(_.template(listTemplate,response));}
                     page.pageIndex++;
                     //self.loadScroller();
			},
			'error':function(response){
				DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"), response.description);
			},
			"complete":function(){callBack&&callBack();__call();}
        	});
        },
		loadListScroller: function(page){
            var self = this; 
            var wraper = $("#"+self.id); 
            if(!self.scroller){
            	 wraper.height(wrapView.height-
                 		$("#header").height());
            	self.scroller = new PTRScroll(wraper[0], {
            		pullUpToLoadMore:!page.isOver,
                    hideScrollbar: true,
                    refreshContent: function(done){
                    	page.pageIndex=1;
                    	page.isOver=false;
                    	self.donationList(page,done);
                    },
                    loadMoreContent: function(done){
                    	self.donationList(page,done);
                    }
                },true);
            }
            else{
            	if(page.isOver){
                	self.scroller.disablePullUpToLoadMore();
            	}else{
            		self.scroller.enablePullUpToLoadMore();
            	}
        }
        },
        'buildAddList':function(_data){//首页广告
        	var self=this,$html_tmp='',$umal_tmp='';
        			var addSilder=self.$el.find("#sliderAdd1"),prointArea=self.$el.find("#proint");
        			for(var i=0;i<_data.length;i++){
        				if(i>5){break;}
//      				$html_tmp+="<li><a href='"+_data[i].advUrl+"'><img src='"+_data[i].advImg+"' title='"+_data[i].advTitle+"'/></a></li>";
//      				$umal_tmp+="<div class=\"uc-a-for1 uwh-for1 c-gra-for2 umar-r-for\"></div>";
        				if(_data[i].advUrl){
            				$html_tmp+="<li><a  target='_blank' class='lu-height085' href='"+_data[i].advUrl+"'><img src='"+_data[i].advImg+"' title='"+_data[i].advTitle+"'/></a></li>";
            				$umal_tmp+="<div class=\"uc-a-for1 uwh-for1 c-gra-for2 umar-r-for\"></div>";
        				}else{
        					$html_tmp+="<li><img src='"+_data[i].advImg+"' title='"+_data[i].advTitle+"'/></li>";
            				$umal_tmp+="<div class=\"uc-a-for1 uwh-for1 c-gra-for2 umar-r-for\"></div>";
        				}
        			}
        		
        			addSilder.append($html_tmp);
        			prointArea.append($umal_tmp);
        			var showProint=function(index){
        				prointArea.children("div").removeClass("c-wh").addClass("c-gra-for2");
        				$(prointArea.children("div")[index]).removeClass("c-gra-for2").addClass("c-wh");
        			}
        			self.sliderAdd=new Slide("sliderAdd1", "H", function(e){
        				showProint(this.currentPoint);
        				},false,function(e){
        			},undefined,function(index){
        				if(_data.data&&_data.data[index]&&_data.data[index].advUrl){
        					window.open(_data.data[index].advUrl,"_system");
        				}
        			});
        			self.startAddAutoTurn();
        			showProint(0);
       },
        "startAddAutoTurn":function(){

        	var self=this;
        	if(self.sliderAdd.length<1){
        		return;
        	}
			DMJS.startThread("donationAdv",5000,-1,function(){
				if(this.isBack&&!self.sliderAdd.hasPrev()){
					this.isBack=false;
				}
				if(!this.isBack&&!self.sliderAdd.hasNext()){
					this.isBack=true;
				}
				if(this.isBack){
					self.sliderAdd.toPrev();
				}else{
					self.sliderAdd.toNext();
				}
				var addSilder=self.$el.find("#sliderAdd1"),prointArea=self.$el.find("#proint");
    			prointArea.children("div").removeClass("c-wh").addClass("c-gra-for2");
    			$(prointArea.children("div")[self.sliderAdd.currentPoint]).removeClass("c-gra-for2").addClass("c-wh");
			});
       },
        "onDestroy":function(){
        	var self=this;
        	DMJS.stopThread("donationAdv");
        	if(self.noDestory){
        		self.sliderAdd&&self.sliderAdd.suspend();
            	self.slider&&self.slider.suspend();
        	}else{
            	self.sliderAdd&&self.sliderAdd.destroy();
            	self.slider&&self.slider.destroy();
        	}
        },
    });

    return donationView;
});



