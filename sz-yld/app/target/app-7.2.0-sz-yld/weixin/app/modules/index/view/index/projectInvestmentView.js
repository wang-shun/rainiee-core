define(['text!indexTemplate/index/projectInvestment.html',
	'text!indexTemplate/index/projectInvestmentList.html',
//	'text!indexTemplate/index/projectInvestmentList1.html',
	'commonClass/scroll/PTRScroll', 
	'commonTool/tool',
	'text!commonTemplate/foot/boTop.html'
], function(projectInvestmenttTemplate, projectInvestmenttListTemplate,PTRScroll,tool,boTop) {
	var projectInvestmentView = DMJS.DMJSView.extend({ 
		id: 'projectInvestment',
		name: 'projectInvestment',
		tagName: 'div',
		className: "projectInvestment",
		events: {
			'tap #bidList':'bidList',
			'tap .Jorder':'orderFun'
		},
		init: function(options) {
			var self = this;
			self.orderFlag=0;//默认降序
			self.options=options;
			self.pageInfo={"pageIndex":1,"loaded":false,"isOver":false,"id":1,'orderType':undefined}
		},
		render: function() {
			var self = this;
			self.gaibAmount=tool.gaibAmount;
			self.changeFinancialType = tool.changeFinancialType;
			self.isStatusType = tool.isStatusType;
			this.$el.html(_.template(projectInvestmenttTemplate, self));
			self.loadDates(self.pageInfo);//初始化投资列表
	        
	       
		},
		'orderFun':function(e){//排序
			var self=this;
			var $dom=$(e.target);
			var orderType = $dom.attr("orderType");
			if(orderType===null){
				return;
			}
			$dom.siblings().removeClass('fn-c-org');
			$dom.siblings().find('.icon').removeClass('fn-c-org');
			$dom.addClass('fn-c-org');
			$dom.find('.icon').addClass('fn-c-org');
			$dom.siblings().find('.icon').text('↓');
			if($dom.find('.icon').text()==='↓'){
				$dom.find('.icon').text('↑');
				self.orderFlag=1;
			}else{
				$dom.find('.icon').text('↓');
				self.orderFlag=0;
			}
		    self.pageInfo.pageIndex = 1;
			self.pageInfo.isOver = false;
			if(orderType==='nly'){  //年利率
				
				if(self.orderFlag===1){//升序
					self.pageInfo.orderType = 12;	
				}else{
					self.pageInfo.orderType = 11;
				}
			}
			if(orderType==='qx'){ //期限
				
				if(self.orderFlag===1){
					self.pageInfo.orderType = 52;	
				}else{
					self.pageInfo.orderType = 51;
				}
			}
			if(orderType==='je'){//金额
				
				if(self.orderFlag===1){
					self.pageInfo.orderType = 42;	
				}else{
					self.pageInfo.orderType = 41;
				}
			}
			if(orderType==='mr'){//默认排序
				
				self.pageInfo.orderType = undefined;	
				
			}
			self.loadDates(self.pageInfo);
		},
		'bidList':function(e){
			var $dom=$(e.target);
			
                if(!$dom.is("#bidList > div")){
                    $dom=$dom.parents("#bidList > div");
                }
                var bidId = $dom.attr("bidid");
                var publicDate = $dom.attr("pulicdate");
                if(!!bidId){
                 DMJS.router.navigate("index/index/projectInvDetail/"+bidId+"/"+publicDate,true);
                }
			
		},
		"loadDates":function(pageInfo,callBack){//获取数据
                    var self=this;
                    if(pageInfo.isOver){
                    	callBack&&callBack();
                    	return;
                    }
                   
                    var __call=function(){
                    	var type=pageInfo.id;
                    	var $dom=self.$el.find("div[item='"+type+"']").find("div.ListArea");
                 		var $dom_content=$dom.find("div");
                 		if($dom_content.find("div").length==0){
            				$dom.html("<div class='ub uinn-pa2 ulev-app4 ub-pc ub-ac t-ddd' id='noData'>暂无数据</div>");
                 			
                            self.scroller[type].disablePullUpToLoadMore();
            				
            				return ;
            			}else if(pageInfo.isOver){
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
                   
                        var params={};
                        params["pageSize"]=10;
                        params["pageIndex"]=pageInfo.pageIndex;
                        if(!!pageInfo.orderType){
                        	params["orderId"]=pageInfo.orderType;
                        }
                        
                        self.controller.indexModel.bidList({//投资列表
								cancelLightbox: true,
								data: params,
								async:false,
                                cancelLightbox:true,
                                "success":function(_data){                                   
                                       var datalist = _data.data;
                                       if(!datalist||datalist.length<Config.base("pageSize")){
                                            $.ajaxSettings.lightboxHide=false;
                                            pageInfo.isOver=true;
                                            DMJS.CommonTools.popTip("已经加载完所有数据！");
                                        }
                                       
                                       $.each(datalist,function(i,item){
                                       		
	                                       		if(item.isDay==='S'){
	                                       			datalist[i].qxUnit='天';
	                                       		}else{
	                                       			datalist[i].qxUnit='个月';
	                                       		}
	                                       		
                                            	var amount = item.amount;
                                                var remainAmount = item.remainAmount;
                                                var progress = 0; 
                                                if( datalist[i]['status'] === 'YJQ'|| datalist[i]['status'] === 'HKZ'){
                                                    progress = 100;
                                                    datalist[i]['amount'] = amount - remainAmount;
                                                }else if(datalist[i]['status'] === 'YFB'){
                                                	progress = 0;
                                                    datalist[i]['amount'] = amount;
                                                  
                                                
                                                }else{
                                                	
                                                    progress = (amount - remainAmount)/amount*100;
                                                    if(progress<=0)
                                                    {
                                                        progress = 0;
                                                    }
                                                    else if(progress<1)
                                                    {
                                                        progress = Math.ceil(progress);//向上取整
                                                    }
                                                    else if(progress>99 && progress!=100){
                                                    	progress = Math.floor(progress);//向下取整
                                                    }
                                                    else
                                                    {
                                                        progress = Math.floor(progress);//向下取整
                                                    }       
                                                }
                                                datalist[i]['progress'] = progress; 
                                   });
                                        self.list=datalist; 
                                        pageInfo.loaded=true; 
                                        var $dom=self.$el.find("div[item='"+pageInfo.id+"']").find("div.ListArea");
                                        if(pageInfo.pageIndex === 1){ 
                                            $dom.html(_.template(projectInvestmenttListTemplate,self));
                                        }else{
                                       	
                                        	$dom.append(_.template(projectInvestmenttListTemplate,self));
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
			var wraper = $("div[item='" + type + "']");
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
            var type=self.pageInfo.id;
        	this.scroller[type].scrollToPage(0,0,500);
        	$(".boTopTag").remove();
          
        }
	});

	return projectInvestmentView;
});