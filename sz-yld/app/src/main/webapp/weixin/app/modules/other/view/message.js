define(['text!otherTemplate/messageContent.html',
        'text!otherTemplate/messageList.html',
       'commonClass/scroll/PTRScroll',
], function(messaContent,messageList,PTRScroll){
    var messageView = DMJS.DMJSView.extend({
        id: 'messageContent',
        name: 'messageContent',
        tagName: 'div',
        className: "message", 
        events: {
            
        }, 
        data: { }, 
        init: function(options) {
        	 var self = this;
             var obj={page:{
         	      pageIndex:1,
         	      pageSize:10,
         	      isOver:false
         	      }};
             _.extend(self, options,obj);
        },
       
       
        render: function(){
            var self=this; 
            this.$el.html(_.template(messaContent, {})); // 将tpl中的内容写入到 this.el 元素中  
            $("div[item='listItem']").css('minHeight',((document.body.clientHeight||document.documentElement.clientHeight)-$("#header").height())+"px");//内容块占满高度
            self.loadListScroller(self.page);
            self.messageList(self.page); 
            
            return this;
        },
        messageList:function(page,callBack){
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
        	self.controller.otherModel.getLetter({'data':{pageIndex:page.pageIndex,pageSize:page.pageSize},'cancelLightbox':true,
        	"noCache":true,
			"success":function(response){
				var $dom=self.$el.find("div[item='listItem']");
				 if(!response.data||response.data.length<page.pageSize){
                    page.isOver=true;
                    DMJS.CommonTools.popTip("已经加载完所有数据！");
                    }
				 //删除点击这里链接
				 for(var i=0;i<response.data.length;i++){
				 	response.data[i].content=response.data[i].content.replace(/这里/g,'').replace(/点击/g,'');
				 }
				//self.loadScroller();
                     if(page.pageIndex == 1){$dom.html(_.template(messageList,response));}
                     else{$dom.append(_.template(messageList,response));}
                     page.pageIndex++;
				 $dom.find("div .img-open").parent().find('#msgTitle').addClass('t-dgra');
			},
			'error':function(response){
			//	self.$el.html(_.template(messaContent,response));
				DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"), response.description);
			},
			"complete":function(){
                                  callBack&&callBack();
                                	__call();
                                }
        	});
        
        },
        //拉拽刷新数据
        loadListScroller: function(pageInfo){
            var self = this; 
            var wraper = $("#"+self.id); 
            if(!self.scroller){
            	 wraper.height(wrapView.height-
                 		$("#header").height());
            	self.scroller = new PTRScroll(wraper[0], {
            		pullUpToLoadMore:!pageInfo.isOver,
                    hideScrollbar: true,
                    refreshContent: function(done){
                    	pageInfo.pageIndex=1;
                    	pageInfo.isOver=false;
                    	self.messageList(pageInfo,done);
                    },
                    loadMoreContent: function(done){
                    	self.messageList(pageInfo,done);
                    }
                },true);
            }
            else{
            	if(pageInfo.isOver){
                	self.scroller.disablePullUpToLoadMore();
            	}else{
            		self.scroller.enablePullUpToLoadMore();
            	}
        }
        },
        //查看全部信息
        readLetter:function(id,$dom){
            var self = this;
            if($dom.find("div .ut-s").length>0){
            	$dom.find("div .ut-s").removeClass("ut-s");
            if($dom.find("div .img-open").length<1)
            self.controller.otherModel.readLetter({data:{id:id},
            "noCache":true,
			"success":function(response){
			 $dom.find("div .img-close").removeClass("img-close").addClass('img-open');
			 $dom.find("#msgTitle").addClass('t-dgra');
			 self.loadListScroller(self.page);
			},
			'error':function(response){
			//	self.$el.html(_.template(messaContent,response));
				DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"), response.description);
			}});
            	
            }
            else{
            	 $dom.find("div").removeClass("ut-u").addClass('ut-s');
            }
       		//self.scroller.refresh();
        },
    });

    return messageView;
});
