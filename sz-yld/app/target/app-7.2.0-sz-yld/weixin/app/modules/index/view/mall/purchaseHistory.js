define(['text!indexTemplate/mall/purchaseHistory.html',
		'text!indexTemplate/mall/purchaseHistoryList.html',
		'commonClass/scroll/PTRScroll',
        'commonTool/tool'],
		function(purchaseHistory,purchaseHistoryList,PTRScroll,tool) {
	var purchaseHistoryView = DMJS.DMJSView.extend({ //项目详情
		id: 'purchaseHistory',
		name: 'purchaseHistory',
		tagName: 'div',
		className: "purchaseHistory",
		init: function(options) {
            var self = this;
            var obj={page:{
        	pageIndex:1,
        	pageSize:10,
        	isOver:false}};
            _.extend(self, options,obj);
        },
        render: function(){
            var self = this;
           	this.$el.html(_.template(purchaseHistory,{}));
           	$("div[item='listItem']").css('minHeight',((document.body.clientHeight||document.documentElement.clientHeight)-$("#header").height())+"px");//内容块占满高度
           	self.loadListScroller(self.page);
            self.purchaseHistoryList(self.page); 
            return this;
        },
        purchaseHistoryList:function(page,callBack){
        	var self=this;
        	var __call=function(){
//                  	var dom=self.$el.find("div[item='listItem']")[0];
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
        	self.controller.indexModel.showDetail(
        	{
				'data': {
					"id":self.goodsId,
					"type":'gmjl',
					pageIndex: page.pageIndex,
					pageSize: page.pageSize
				},
				'cancelLightbox': true,
				"noCache": true,
				"success":function(response){
//					var $dom=self.$el.find("div[item='listItem']");
					var $dom=self.$el.find("div[item='item1']").find(".ListArea");
					 if(!response.data.purchaseList||response.data.purchaseList.length<page.pageSize){
	                    page.isOver=true;
	                    DMJS.CommonTools.popTip("已经加载完所有数据！");
	                    }
	                     if(page.pageIndex == 1){
	                     	$dom.html(_.template(purchaseHistoryList,response.data));
	                     }
	                     else{
	                     	$dom.append(_.template(purchaseHistoryList,response.data));
	                     }
	                     page.pageIndex++;
	                     //self.loadScroller();
				},
				'error':function(response){
					DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"), response.description);
				},
				"complete":function(){
					callBack&&callBack();__call();
				}
        	});
        },
		loadListScroller: function(page){
            var self = this; 
            var wraper = self.$el.find("div[item='item1']"); 
            if(!self.scroller){
            	 wraper.height(wrapView.height-$("#titelMenu").height()-
                 		$("#header").height());
            	self.scroller = new PTRScroll(wraper[0], {
            		pullUpToLoadMore:!page.isOver,
                    hideScrollbar: true,
                    refreshContent: function(done){
                    	page.pageIndex=1;
                    	page.isOver=false;
                    	self.purchaseHistoryList(page,done);
                    },
                    loadMoreContent: function(done){
                    	self.purchaseHistoryList(page,done);
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
    });

	return purchaseHistoryView;
});