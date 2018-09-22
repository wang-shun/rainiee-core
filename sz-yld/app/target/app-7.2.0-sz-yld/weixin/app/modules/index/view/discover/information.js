define(['text!indexTemplate/discover/information.html',
		'text!indexTemplate/discover/informationList.html','commonClass/scroll/PTRScroll'], 
		function(informationTemplate,listTemplate,PTRScroll){
    var informationView = DMJS.DMJSView.extend({
        id: 'informationContent',
        name: 'informationContent',
        tagName: 'div',
        className: "informationContent", 
        events: {
            'tap #infomation_title' :'switchs',
            'tap #information_Article':'information_Article'
        },
        init: function(options){
        var obj={page:{
        	pageIndex:[1,1,1],
        	pageSize:10,
        	isOver:[false,false,false],
        	ids:[1,2,3],
        	loaded:[false,false,false],
        	type:1,
        	parms:['MTBD','WDHYZX','WZGG']
        }};
           	_.extend(this,options,obj); 
        },
         
        render: function(){
           var self = this;
            this.noDestroy=false;
            self.getPropertyName();
            return this;
        },
        getPropertyName:function(){
        	var self=this;
        	self.controller.indexModel.getPropertyName({
        		data:{},
                "noCache": true,
                cancelLightbox: true,
                "success": function(response) { 
                	self.data=response.data;
                    self.$el.html(_.template(informationTemplate,self.data));
                    self.loadListScroller(self.page);
                    self.loadDatas(self.page);
                },
                 'error':function(response){DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"), response&&response.description);}
            });
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
        information_Article:function(e){
        	 var $dom=$(e.target);
               // if(!$dom.is("#information_Article  > div > div > div > div")){
                    $dom=$dom.parents("div[data-id]");
                    $dom2=$dom.parents("div[data-date]");
              //  }
                var id = $dom.attr("data-id");
                var date = $dom.attr("data-date");
				if(id){
					 this.noDestroy=true;
					 DMJS.router.navigate('index/index/informationArticle/'+id+"/"+this.page.parms[this.page.type-1]+'/'+date,true);
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
            				return self.scroller[type].enablePullUpToLoadMore();
            			}
                   };
        	self.controller.indexModel.articleList({'data':{type:page.parms[page.type-1],pageIndex:page.pageIndex[page.type-1],pageSize:page.pageSize},'cancelLightbox':true,
        	"noCache":true,
			"success":function(response){
				var $dom=self.$el.find("div[item='item"+page.ids[type]+"']").find(".ListArea");
				 if(!response.data||response.data.length<page.pageSize){
                   page.isOver[type]=true;
                    DMJS.CommonTools.popTip("已经加载完所有数据！");
                    }
				  for(var i=0;i<response.data.length;i++){
				 	if(!response.data[i].desc)  continue;
				 	response.data[i].desc=(response.data[i].desc).replace(/<[^>]+>/g,"");
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
                 		$("#header").height()-$("#infomation_title").height());
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

    return informationView;
});



