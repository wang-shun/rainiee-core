define(['text!indexTemplate/mall/addressManager.html',
		'text!indexTemplate/mall/addressList.html',
		'commonClass/scroll/PTRScroll',
		'commonTool/tool',
	],function(addressManager,listTemplate,PTRScroll,tool) {
    var addressManagerView = DMJS.DMJSView.extend({
        id: 'addressManagerContent',
        name: 'addressManagerContent',
        tagName: 'div',
        className: "addressManagerContent",
        events: {
        	'touchstart .JSMoveHOrs':'start',
			'touchmove .JSMoveHOrs':'move',
			'touchend .JSMoveHOrs':'end',
			'tap .update':'toModifyAddress',
			'tap .delete': 'deleteAddress',
        },
        data : {
        	X:0,
        	width:0,
        	$selDom:""
		},
		startX:0,
		endX:0,
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
            self.$el.html(_.template(addressManager,{}));
            self.loadListScroller(self.page);
            self.loadDatas(self.page); 
            return this;
        },
        start:function(e){
			this.data.X=e.touches[0].clientX;
			e.preventDefault();
			e.returnValue=false;
		},
		move:function(e){
			var width=e.touches[0].clientX-this.data.X;
			console.log(width);
			this.data.width+=width;
			if(this.data.$selDom){
				this.data.$selDom.style.webkitTransform='translate(0px, 0px) scale(1) translateZ(0px)';
			}
			this.data.$selDom = e.target.parentNode;
			if($(".ListArea > div").has(this.data.$selDom).length<1){
				return;
			}
			if(!$(this.data.$selDom).is(".ListArea > div")){
                    this.data.$selDom=$(".ListArea > div").has(this.data.$selDom)[0];
            }
			this.data.$selDom.style.webkitTransitionDuration="200ms";
			if(width>10){
					this.data.$selDom.style.webkitTransform='translate(0px, 0px) scale(1) translateZ(0px)';	
			}
			else if(this.data.width<-100){
					this.data.$selDom.style.webkitTransform='translate(-60px, 0px) scale(1) translateZ(0px)';	
			}else if(width-this.data.width>3){
				this.data.$selDom.style.webkitTransform='translate('+width+'px, 0px) scale(1) translateZ(0px)';
				//alert(e.target.parentNode.style.transform);
			}
		},
		end:function (e) {
			this.data.$selDom = e.target.parentNode;
			if($(".ListArea > div").has(this.data.$selDom).length<1){
				return;
			}
			if(!$(this.data.$selDom).is(".ListArea > div")){
                    this.data.$selDom=$(".ListArea > div").has(this.data.$selDom)[0];
            }
		 	if(this.data.X-e.changedTouches[0].clientX>100){
		 		this.data.$selDom.style.webkitTransform='translate(-60px, 0px) scale(1) translateZ(0px)';
		 	}
		 		this.data.X=0;
        		this.data.width=0;
        },
        loadDatas:function(page,callBack){
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
        	self.controller.indexModel.myHarvestAddress({'data':{pageIndex:page.pageIndex,pageSize:page.pageSize},'cancelLightbox':true,
        	"noCache":true,
			"success":function(response){
				var datalist = response.data.getAddresses;
				var $dom=self.$el.find("div[item='listItem']");
				self.countAddress = response.data.countAddress;
				self.list = datalist;
				 if(!datalist||datalist.length<page.pageSize){
                    page.isOver=true;
                    DMJS.CommonTools.popTip("已经加载完所有数据！");
                    }
                     if(page.pageIndex == 1){$dom.html(_.template(listTemplate,self));}
                     else{$dom.append(_.template(listTemplate,self));}
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
                 		$("#header").height()-$("#footer").height());
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
       	toModifyAddress:function(e){
        	var $dom=$(e.target);
		
            if(!$dom.is(".ListArea > div")){
                $dom=$dom.parents(".ListArea > div");
            }
            var addressId = $dom.attr("addressId");
            if(addressId){
            	DMJS.router.navigate("index/index/addAddress/"+addressId, true);
            }
        },
        deleteAddress:function(e){
        	var self = this;
			var $dom=$(e.target);
		
            if(!$dom.is(".ListArea > div")){
                $dom=$dom.parents(".ListArea > div");
            }
            var addressId = $dom.attr("addressId");
            self.controller.indexModel.deleteAddress({
     			'data': {
     				'id' : addressId,
     			},
				"success": function(_data) {
					if(_data.code == "000000"){
						self.page.pageIndex = 1;
						self.loadDatas(self.page);
					}else{
						DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"),_data.description);
					}
				},
			});
		},
		selectedAddress:function(id){
			var self  = this;
			if(window.listHash[window.listHash.length-2]=="index/index/fillInOrder"){
				for(var item in self.list){
				if(self.list[item].id==parseInt(id)){
					tool._Navi_default("index/index/addressManager");
					DMJS.currentView.controller.orderAddress = self.list[item];
					break;
				}
			}
			}
		},
    });
    return addressManagerView;
});
