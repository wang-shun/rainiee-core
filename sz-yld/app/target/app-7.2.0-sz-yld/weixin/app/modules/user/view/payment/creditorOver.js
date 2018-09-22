define(['text!userTemplate/payment/creditorOver.html','text!userTemplate/payment/creditorOverList.html',
		'commonTool/tool','commonTool/slide','commonClass/scroll/PTRScroll','text!commonTemplate/foot/boTop.html'
], function(creditorOverTemplate, creditorOverListTpl,tool,Slide,PTRScroll,boTop) {
    var creditorOverV = DMJS.DMJSView.extend({
        id: 'creditorOver',
        name: 'creditorOver',
        tagName: 'div',
        className: "creditorOver",
        events: {
        	'tap .JSAppointDropDown':'JSAppointDropDown',
        	'tap .JviewContract': 'viewContract',//查看合同
        	'tap .JcancelZR': 'JcancelZR',//取消转让
		
        	
        },
        init: function(options) {
            var self = this;
            _.extend(self, options);
              self.pageInfo={
                    "0":{"pageIndex":1,"loaded":false,"isOver":false,"id":0,'dataS':'zrz'},
                    "1":{"pageIndex":1,"loaded":false,"isOver":false,"id":1,'dataS':'yzc'},
                    "2":{"pageIndex":1,"loaded":false,"isOver":false,"id":2,'dataS':'yzr'}
               };
          
            
        },
        render: function() {
            var self = this;
            self.gaibAmount=tool.gaibAmount;
           	this.$el.html(_.template(creditorOverTemplate,{}));
           	$("#contentWrapper").height(wrapView.height-$("#header").height()-$("#indexTitleContent").height());
           	self.slider=new Slide(self.$el.find("#slider")[0], "H", function(){
                    var cur = this.currentPoint;
                    if(cur!=self.currentType){
						self.currentType=cur;
	                    $('.JSAppointDropDown li').removeClass('lbd-b-blue');
	                    $($('.JSAppointDropDown li')[cur]).addClass('lbd-b-blue');
	                 	self.loadDates(self.pageInfo[cur]);
                 	}
	            },false,function(e){});
	        
	      
                self.loadDates(self.pageInfo['0']);
           
            
            return this;
        },
        JSDetail:function(e){
			var self = this;
			var $dom = $(e.target);
			var bidId = $dom.parents('.Jdc').attr('creditorid');
			this.noDestroy=true;
			DMJS.router.navigate("index/index/projectInvDetail/"+bidId,true);
		},
		JSAppointDropDown:function(e){
			var self=this;
			var $dom=$(e.target);
        	
        	var chooiceType=$dom.attr("listType");
        	if(chooiceType!=self.currentType){
				self.currentType=chooiceType;
				
				$('.JSAppointDropDown li').removeClass('lbd-b-blue');
	            $($('.JSAppointDropDown li')[chooiceType]).addClass('lbd-b-blue');
	            
				self.loadDates(self.pageInfo[chooiceType]);
				self.slider.moveToPoint(parseInt(chooiceType));
			}
		},
        'viewContract':function(e){
        	var self=this;
			var $dom=$(e.target);
			var id=$dom.parents('.Jdc').attr('creditorid');
			this.noDestroy=true;
			if(self.isSaveTransfer=="true"){
				self.controller.userModel.saveBidContract({
	        		'data':{'zqId':id,'type':'zqzrht'},
	        		'cancelLightbox': false,
					"noCache": true,
					'success':function(response){
						Native.openChildWebActivity(response.data,{title:"合同"});
					},
					'error':function(response){
						DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"), response.description);
					}
	        	});
			}else{
				DMJS.router.navigate("user/personal/viewContract/"+id+"/"+'zq', true);
			}
        	
        },
        'JcancelZR':function(e){
        	var self=this;
			var $dom=$(e.target);
			var id=$dom.parents('.Jdc').attr('creditorid');
			wrapView.FlipPrompt.confirm({
				title: "债权转让",
				content: '是否执行"取消转让"债权操作？',
				FBntconfirm: "是",
				FBntcancel: "否",
				FBntConfirmColor: "pop_btn_orange",
				autoCloseBg:"false",
			}, function() {

				self.controller.userModel.cancelTransfer({ //取消转让

					data: {"creditorId":id},
					cancelLightbox: true,
					"success": function(_data) {
						
						if(_data.code==='000000'){
							wrapView.lightBox.hide();
							DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"),'取消转让成功!');
							setTimeout(function(){
								self.reflush();
								wrapView.lightBox.hide();
							},500);
						}
					},
					'error': function(response) {
													
							
							DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"),response.description);
							setTimeout(function(){
								DMJS.CommonTools.hideLightBoxAlert();
							},500);

					}
			});
				
			}, function() {});
			
        },
        "loadDates":function(pageInfo,callBack){
                    var self=this;
                    if(pageInfo.isOver){
                    	callBack&&callBack();
                    	return;
                    }
                   
                    var __call=function(){
                    	var type=pageInfo.id;
                    	var $dom=self.$el.find("div[items='"+type+"']").find("div.ListArea");
                 		var $dom_content=$dom.find("div");
                 		if($dom_content.find("div").length==0){
            				$dom.html("<div class='ub uinn-pa2 ulev-app4 ub-pc ub-ac t-ddd' id='noData'>暂无数据</div>");
                 			
                            self.scroller[type].disablePullUpToLoadMore();
            				
            				return ;
            			}else if(pageInfo.isOver||$dom_content.is("#noDataTip")){
            				self.scroller[type].disablePullUpToLoadMore();
            				if(pageInfo.pageIndex>2&&self.$el.find(".t-btn-a4").length==0){
                                self.$el.append(_.template(boTop,self));   //添加点击按钮回到顶部

                            }
            			
                			return;
            			}else{
            				if(pageInfo.pageIndex>2&&self.$el.find(".t-btn-a4").length==0){
                                self.$el.append(_.template(boTop,self));   //添加点击按钮回到顶部

                            }
            				return self.scroller[type].enablePullUpToLoadMore();
            			}
            			
                   }
               
                        
                        self.controller.userModel.creditAssignmentList({//债权转让
								cancelLightbox: true,
								data: {
									"type":pageInfo.dataS,
									"pageIndex": pageInfo.pageIndex
								},
                                cancelLightbox:true,
                                "success":function(_data){
                                       var datalist = _data.data;
                                       if(!datalist||datalist.length<Config.base("pageSize")){
                                            $.ajaxSettings.lightboxHide=false;
                                            pageInfo.isOver=true;
                                            DMJS.CommonTools.popTip("已经加载完所有数据！");
                                        }
                                       
                                        self.typeS=pageInfo.dataS;
                                        self.list=datalist; 
                                        pageInfo.loaded=true; 
                                        var $dom=self.$el.find("div[items='"+pageInfo.id+"']").find("div.ListArea");
                                        if(pageInfo.pageIndex === 1){
                                        	
                                            $dom.html(_.template(creditorOverListTpl,self));
                                        }else{
                                       	
                                        	$dom.append(_.template(creditorOverListTpl,self));
                                        }
                                        pageInfo.pageIndex++;
                                       
                               
                                },"complete":function(){
                                	if(!!callBack){
                                		callBack();
                                	}
                                	self.loadListScroller(pageInfo);
                                	__call();
                                	
                                }
                        });
                  
                    
        	},
       loadListScroller: function(pageInfo) {
			var self = this;
			var type = pageInfo.id;
			!self.scroller && (self.scroller = {});
			var wraper = $("div[items='" + type + "']");
			if (!self.scroller[type]) {
				wraper.height(wrapView.height - $("#header").height() - $("#indexTitleContent").height() - $("#footer").height());
				self.scroller[type] = new PTRScroll(wraper[0], {
					pullUpToLoadMore: !pageInfo.isOver,
					hideScrollbar: true,
					refreshContent: function(done) {
						pageInfo.pageIndex = 1;
						pageInfo.isOver = false;
						//pageInfo.loaded=false;
						
						self.loadDates(pageInfo, done);
						
					},
					loadMoreContent: function(done) {
						self.loadDates(pageInfo, done);
					}
				}, true);
			} else {
				if (pageInfo.isOver) {
					self.scroller[type].disablePullUpToLoadMore();
				} else {
					self.scroller[type].enablePullUpToLoadMore();
				}
			}
		},
		runScrollerTop:function(){
		 	var self=this;
            var type=self.currentType;
        	this.scroller[type].scrollToPage(0,0,500);
        	$(".boTopTag").remove();
          
        }
    });
    return creditorOverV;
});
