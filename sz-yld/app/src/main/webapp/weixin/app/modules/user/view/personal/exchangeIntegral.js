define(['text!userTemplate/personal/exchangeIntegral.html',
	'text!userTemplate/personal/myExchangelList.html',
	'commonClass/scroll/PTRScroll', 
	'commonTool/tool',
	'text!commonTemplate/foot/boTop.html'
], function(exchangeIntegral,myExchangelList,PTRScroll,tool,boTop) {
	var exchangeIntegralView = DMJS.DMJSView.extend({ 
		id: 'exchangeIntegral',
		name: 'exchangeIntegral',
		tagName: 'div',
		className: "exchangeIntegral",
		events: {
		},
		init: function(options) {
			var self = this;
            var obj={page:{
        	pageIndex:1,
        	pageSize:10,
        	isOver:false}};
            _.extend(self, options,obj);
		},
		render: function() {
			 var self = this;
            this.noDestroy=false;
           	this.$el.html(_.template(exchangeIntegral,{}));
           	$("div[item='listItem']").css('minHeight',((document.body.clientHeight||document.documentElement.clientHeight)-$("#header").height())+"px");//内容块占满高度
           	self.loadListScroller(self.page);
            self.exchangelList(self.page); 
            return this; 
	       
		},
		exchangelList:function(page,callBack){
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
        	self.controller.userModel.getExchangeRecords({'data':{pageIndex:page.pageIndex,pageSize:page.pageSize},'cancelLightbox':true,
        	"noCache":true,
			"success":function(response){
				var $dom=self.$el.find("div[item='listItem']");
				 if(!response.data||response.data.length<page.pageSize){
                    page.isOver=true;
                    DMJS.CommonTools.popTip("已经加载完所有数据！");
                    }
                     if(page.pageIndex == 1){
                     	$dom.html(_.template(myExchangelList,response));
                     }
                     else{$dom.append(_.template(myExchangelList,response));}
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
//          var wraper = $("#integralList"); 
            if(!self.scroller){
            	 wraper.height(wrapView.height-
                 		$("#header").height());
            	self.scroller = new PTRScroll(wraper[0], {
            		pullUpToLoadMore:!page.isOver,
                    hideScrollbar: true,
                    refreshContent: function(done){
                    	page.pageIndex=1;
                    	page.isOver=false;
                    	self.exchangelList(page,done);
                    },
                    loadMoreContent: function(done){
                    	self.exchangelList(page,done);
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
		
		"requestRecord":function(type){
			var self = this;
			if(type=="left"){
				DMJS.router.navigate("user/personal/myIntegral", true);
			}
		},
		 runScrollerTop:function(){
		 	var self=this;
            var type=self.pageInfo["1"].id;
        	this.scroller[type].scrollToPage(0,0,500);
        	$(".boTopTag").remove();
          
        }
	});

	return exchangeIntegralView;
});