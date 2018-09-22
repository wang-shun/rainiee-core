define(['text!userTemplate/personal/experience.html',
		'text!userTemplate/personal/experienceList.html','commonTool/tool','commonClass/scroll/PTRScroll', 
], function(experienceTemplate,listTemplate,tool,PTRScroll) {
    var experienceView = DMJS.DMJSView.extend({
        id: 'experienceViewContent',
        name: 'experienceViewContent',
        tagName: 'div',
        className: "experienceViewContent",
        events: {
        	'tap #experience_info':'experienceInfo'
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
            self.$el.html(_.template(experienceTemplate,{}));
           $("div[item='listItem']").css('minHeight',((document.body.clientHeight||document.documentElement.clientHeight)-$("#header").height())+"px");//内容块占满高度
            self.loadListScroller(self.page);
            self.experienceList(self.page); 
            return this;
        },
        experienceList:function(page,callBack){
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
        	self.controller.userModel.experienceList({'data':{pageIndex:page.pageIndex,pageSize:page.pageSize},'cancelLightbox':true,
        	"noCache":true,
			"success":function(response){
				var $dom=self.$el.find("div[item='listItem']");
				 if(!response.data||response.data.length<page.pageSize){
                    page.isOver=true;
                    DMJS.CommonTools.popTip("已经加载完所有数据！");
                    }
				for(var i=0;i<response.data.length;i++){
					//统一钱的格式 1,000.00 ==> 1000
					response.data[i].expAmount=(response.data[i].expAmount.replace(/,/g,"")*1).toFixed(0);
					response.data[i].endDate=response.data[i].endDate?response.data[i].endDate.substring(0,10):response.data[i].endDate;
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
        experienceInfo:function(e){
        	var target=e.target;
        	var id=target.dataset.id;
        	var status=target.dataset.status;
        	if(id&&status)
        	DMJS.router.navigate("user/personal/experienceInfo/"+id+"/"+status, true);
        },
        getDestroyTime:function(time){
        	if(!time) return time;
        	//苹果不支持 2000-10-10 这样的日期格式
        	var time=time.replace(/-/g,'/');
        	var date=new Date().getTime();
        	var destroyTime=new Date(time).getTime();
        	var differ=destroyTime-date;
        	if(differ<=0){
        		return time;
        	}else{
        		//开始拼接 成下面格式 XX天XX时XX分XX秒失效
        		var dd = parseInt(differ/1000/60/60/24);//计算剩余的天数
                var hh = parseInt(differ/1000/60/60%24);//计算剩余的小时数   
                var mm = parseInt(differ/1000/60%60);//计算剩余的分钟数   
                var ss = parseInt(differ/1000%60);//计算剩余的秒数  
                return dd+"天"+hh+"时"+mm+"分"+ss+"秒失效";
        	}
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
                    	self.experienceList(page,done);
                    },
                    loadMoreContent: function(done){
                    	self.experienceList(page,done);
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
    return experienceView;
});
