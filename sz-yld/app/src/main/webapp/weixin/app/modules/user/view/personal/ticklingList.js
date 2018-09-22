define(['text!userTemplate/personal/ticklingList.html','text!userTemplate/personal/ticklingInfo.html','commonTool/tool','commonClass/scroll/PTRScroll',
], function(ticklingListTemplate,listTemplate, tool,PTRScroll) {
    var ticklingListView = DMJS.DMJSView.extend({
        id: 'ticklingListContent',
        name: 'ticklingListContent',
        tagName: 'div',
        className: "ticklingListContent",
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
            self.$el.html(_.template(ticklingListTemplate,{}));
            $("div[item='listItem']").css('minHeight',((document.body.clientHeight||document.documentElement.clientHeight)-$("#header").height())+"px");//内容块占满高度
            self.loadListScroller(self.page);
            self.loadDatas(self.page); 
            return this;
        },
        helpInfo:function(e){
        	 var $dom=$(e.target);
               // if(!$dom.is("#information_Article  > div > div > div > div")){
                    $dom=$dom.parents("div[data-id]");
              //  }
                var id = $dom.attr("data-id");
				if(id){
					 this.noDestroy=true;
					 DMJS.router.navigate('user/personal/helpInfo/'+id+"/XSZY",true);
				}
        },
        loadDatas:function(page,callBack){
        	
        	var self=this;
        	var __call=function(){
                    	var dom=self.$el.find("div[item='listItem']")[0];
                 		var len=dom.children.length;
                 		if(len<10){
                 			if(len==0){
                 			//dom.innerHTML=wrapView.noSearchDiv;
                 			$('#tickling_info').addClass('noData_fk');
                 			}
            				return self.scroller.disablePullUpToLoadMore();
            			}else if(page.isOver/*||$('#FlipPrompt')*/){
            				return self.scroller.disablePullUpToLoadMore();
            			}else{
            				return self.scroller.enablePullUpToLoadMore();
            			}
            			
                    }
        	self.controller.userModel.ticklingList({'data':{pageIndex:page.pageIndex,pageSize:page.pageSize},'cancelLightbox':true,
        	"noCache":true,
			"success":function(response){
				
				var $dom=self.$el.find("div[item='listItem']"),data =response.data;
				 if(!response.data||response.data.length<page.pageSize){
                    page.isOver=true;
                    DMJS.CommonTools.popTip("已经加载完所有数据！");
                    }
				//self.loadScroller();
                     if(page.pageIndex == 1){$dom.html(_.template(listTemplate,response));}
                     else{$dom.append(_.template(listTemplate,response));}
                     page.pageIndex++;

			},
			'error':function(response){
			//	self.$el.html(_.template(experienceTemplate,response));
				DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"), response.description);
			},
			"complete":function(){
                                  callBack&&callBack();
                                	__call();
                                }
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
                    	self.loadDatas(page,done);
                    },
                    loadMoreContent: function(done){
                    	self.loadDatas(page,done);
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
    return ticklingListView;
});
