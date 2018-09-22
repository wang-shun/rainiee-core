define(['text!userTemplate/personal/myIntegral.html',
	'text!userTemplate/personal/myIntegralList.html',
	'commonClass/scroll/PTRScroll', 
	'commonTool/tool',
	'text!commonTemplate/foot/boTop.html'
], function(myIntegral, myIntegralList,PTRScroll,tool,boTop) {
	var myIntegralView = DMJS.DMJSView.extend({ 
		id: 'myIntegral',
		name: 'myIntegral',
		tagName: 'div',
		className: "myIntegral",
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
           	this.$el.html(_.template(myIntegral,{}));
           	$("div[item='listItem']").css('minHeight',((document.body.clientHeight||document.documentElement.clientHeight)-$("#header").height())+"px");//内容块占满高度
           	self.loadListScroller(self.page);
            self.myIntegralList(self.page); 
            return this; 
	       
		},
		myIntegralList:function(page,callBack){
        	var self=this;
        	var __call=function(){
                    	var dom=self.$el.find("div[item='item1']").find(".ListArea")[0];
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
        	self.controller.userModel.getIntegralRecords({'data':{pageIndex:page.pageIndex,pageSize:page.pageSize},'cancelLightbox':true,
        	"noCache":true,
			"success":function(response){
				var $dom=self.$el.find("div[item='item1']").find(".ListArea");
				 if(!response.data||response.data.length<page.pageSize){
                    page.isOver=true;
                    DMJS.CommonTools.popTip("已经加载完所有数据！");
                    }
                     if(page.pageIndex == 1){
                     	$dom.html(_.template(myIntegralList,response));
                     }
                     else{$dom.append(_.template(myIntegralList,response));}
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
//          var wraper = $("#"+self.id); 
            var wraper = self.$el.find("div[item='item1']"); 
            if(!self.scroller){
            	 wraper.height(wrapView.height-
                 		$("#header").height()-$("#header-content").height() -$("#footer").height());
            	self.scroller = new PTRScroll(wraper[0], {
            		pullUpToLoadMore:!page.isOver,
                    hideScrollbar: true,
                    refreshContent: function(done){
                    	page.pageIndex=1;
                    	page.isOver=false;
                    	self.myIntegralList(page,done);
                    },
                    loadMoreContent: function(done){
                    	self.myIntegralList(page,done);
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
			if(type=="right"){
				DMJS.router.navigate("user/personal/exchangeIntegral", true);
			}
			
		},
		 runScrollerTop:function(){
		 	var self=this;
            var type=self.pageInfo["1"].id;
        	this.scroller[type].scrollToPage(0,0,500);
        	$(".boTopTag").remove();
          
        }
	});

	return myIntegralView;
});