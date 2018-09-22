define(['text!indexTemplate/discover/cash.html',
		'text!indexTemplate/discover/cashList.html','commonClass/scroll/PTRScroll'], function(cashTemplate,listTemplate,PTRScroll){
    var cashListView = DMJS.DMJSView.extend({//项目投资
        id: 'cashListContent',
        name: 'cashListContent',
        tagName: 'div',
        className: "cashListContent", 
        events: {
             'tap #infomation_title' :'switchs',
        },
        init: function(options){
        var obj={page:{
        	pageIndex:[1,1,1],
        	pageSize:10,
        	isOver:[false,false,false],
        	ids:[1,2,3],
        	loaded:[false,false,false],
        	type:1,
        	parms:['1','2','3']
        }};
           	_.extend(this,options,obj); 
        },
        render: function(){
           var self = this;
            this.noDestroy=false;
           	this.$el.html(_.template(cashTemplate,{}));
          // 	$("div[listItem").find('.ListArea').css('minHeight',(wrapView.height-$("#header").height()-$("#infomation_title").height()-$("#infomation_title2").height())+"px");//内容块占满高度
            self.loadListScroller(self.page);
            self.loadDatas(self.page); 
            return this;
        },
		switchs:function(e){
        	var self=this;
        	var target=e.target;
        	var item=target.dataset.item;
        	var type=item.substring(4)*1;
        	$(target).parent().find('div .active-blue').removeClass('active-blue');
        	$(target).addClass('active-blue');
        	$('div[item]').addClass('hide');
        	$('div[item='+item+']').removeClass('hide');
        	
        	if(type==self.page.type) return false;
        	self.page.type=type
        	if(!self.page.loaded[type-1]){
        		self.loadListScroller.apply(self,[self.page]);
        		self.loadDatas.apply(self,[self.page]);
        	}
        },
        loadDatas:function(page,callBack){
        	var self=this;
        	var type=page.type-1;
        	var __call=function(){
                    	var dom=self.$el.find("div[item='item"+page.ids[type]+"']").find(".ListArea")[0];
                 		var len=dom.children.length;
                 		if(len<10){
                 			if(len==0){
                 				dom.innerHTML=wrapView.noSearchDiv;
                 			}
            				return self.scroller[type].disablePullUpToLoadMore();
            			}else if(page.isOver[type]/*||$('#FlipPrompt')*/){
            				return self.scroller[type].disablePullUpToLoadMore();

            			}else{
            				//只加载10条
            				return self.scroller[type].disablePullUpToLoadMore();
            				//return self.scroller[type].enablePullUpToLoadMore();
            			}
                   };
        	self.controller.indexModel.cashList({'data':{type:page.parms[page.type-1],pageIndex:page.pageIndex[page.type-1],pageSize:page.pageSize},'cancelLightbox':true,
        	"noCache":true,
			"success":function(response){
				var $dom=self.$el.find("div[item='item"+page.ids[type]+"']").find(".ListArea");
				 if(!response.data||response.data.length<page.pageSize){
                   page.isOver[type]=true;
                    DMJS.CommonTools.popTip("已经加载完所有数据！");
                    }
                     if(page.pageIndex[type] == 1){$dom.html(_.template(listTemplate,response));}
                     else{$dom.append(_.template(listTemplate,response));}
                     page.pageIndex[type]++;
                     page.loaded[type]=true;
                     //self.loadScroller();
			},
			'error':function(response){
				DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"), response.description);
			},
			"complete":function(){callBack&&callBack();__call();}
        	});
        },
         loadListScroller: function(page){
         	var type=page.type-1;
            var self = this; 
            var wraper = self.$el.find("div[item='item"+page.ids[type]+"']"); 
            !self.scroller&&(self.scroller={});
            if(!self.scroller[type]){
            	 wraper.height(wrapView.height-
                 		$("#header").height()-$("#infomation_title").height()-$("#infomation_title2").height());
                 		//console.log()
            	self.scroller[type] = new PTRScroll(wraper[0], {
            		pullUpToLoadMore:!page.isOver[type],
                    hideScrollbar: true,
                    refreshContent: function(done){
                    	page.pageIndex[type]=1;
                    	page.isOver[type]=false;
                    	self.loadDatas(page,done);
                    },
                    loadMoreContent: function(done){
                    	self.loadDatas(page,done);
                    }
                },true);
            }
            else{
            	if(page.isOver[type]){
                	self.scroller[type].disablePullUpToLoadMore();
            	}else{
            		self.scroller[type].enablePullUpToLoadMore();
            	}
        }
        },
    });

    return cashListView;
});



