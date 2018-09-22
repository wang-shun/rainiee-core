define(['text!indexTemplate/donation/donationLog.html','text!indexTemplate/donation/donationLogList.html','commonClass/scroll/PTRScroll'], 
function(donationLogTemplate,listTemplate,PTRScroll){
    var donationLogView = DMJS.DMJSView.extend({//项目投资
        id: 'donationLogContent',
        name: 'donationLogContent',
        tagName: 'div',
        className: "donationLogContent", 
        events: {
            
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
            var self = this;
           	this.$el.html(_.template(donationLogTemplate,{}));
           	$("div[item='listItem']").css('minHeight',((document.body.clientHeight||document.documentElement.clientHeight)-$("#header").height())+"px");//内容块占满高度
           	self.loadListScroller(self.page);
            self.donationLogList(self.page); 
            return this;
        },
        donationLogList:function(page,callBack){
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
        	self.controller.indexModel.gyLoanRecordsList({'data':{bidId:self.data.donationId,pageIndex:page.pageIndex,pageSize:page.pageSize},'cancelLightbox':true,
        	"noCache":true,
			"success":function(response){
				var $dom=self.$el.find("div[item='listItem']");
				 if(!response.data||response.data.length<page.pageSize){
                    page.isOver=true;
                    DMJS.CommonTools.popTip("已经加载完所有数据！");
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
                    	self.donationLogList(page,done);
                    },
                    loadMoreContent: function(done){
                    	self.donationLogList(page,done);
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

    return donationLogView;
});



