define(['text!indexTemplate/mall/intergalMall.html',
		'text!indexTemplate/mall/intergalMallTemplate.html',
		'commonClass/scroll/PTRScroll',
		'commonTool/tool',
	],
	function(intergalMall, intergalMallTemplate,PTRScroll,tool) {
		var mallView = DMJS.DMJSView.extend({ //项目投资
			id: 'intalMallContent',
			name: 'intalMallContent',
			tagName: 'div',
			className: "intalMallContent",
			events: {
				'tap div[listStatus]':'choseCondition',
				'tap #borderb-gra': 'selectedCondition',
				'tap #goodsList':'goodsDetail'
			},
			init: function(options) {
				var self = this;
				_.extend(this, options);
				self.pageInfo = {
					"1": {
						"pageSize": 10,
						"pageIndex": 1,
						"loaded": false,
						"isOver": false,
						"id": 1
					}

				},
				self.currtSelected={"goodsType":0,"integral":0,"price":0,"sort":"sort_way_unlimited",};
			},
			render: function() {
				var self = this;
				this.noDestroy=false;
				this.$el.html(_.template(intergalMall, self.data)); // 将tpl中的内容写入到 this.el 元素中  
				//初始商品列表
				setTimeout(function() {
					self.loadDates(self.pageInfo[1]);
				}, Config.base("lazyRender") || 500);
			},
			"loadScroller2":function(scrollerId){
	            var self = this;
	            scrollerId = scrollerId ? scrollerId : self.id;
	            var height = wrapView.height -$("#header").height() - $("#footer").height()-$("#goodsCondition").height()-150;
	            if (!self.scroller2) {
	                var wraper = $("#"+scrollerId);
	                wraper.height(height);
	                     
	                this.scroller2 = new iScroll(wraper[0], { checkDOMChanges: true });
	            } else {
	            	if(height>=$('#messgeH').height()){
	            		$('#borderb-gra').height($('#messgeH').height());
	            	}else{
	            		$('#borderb-gra').height(height);
	            	}
//	                this.scroller2.refresh();
	            }
	        
			},
			"loadDates": function(pageInfo, callBack) {
				var self = this;
				if (pageInfo.isOver) {
					callBack && callBack();
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
				if (pageInfo.id == 1) {

					self.controller.indexModel.scoreMallList({ 
						data: {
							"goodsCategory":self.currtSelected.goodsType,
							"scoreRange":self.currtSelected.integral,
							"amountRange":self.currtSelected.price,
							"sortWay":self.currtSelected.sort.split("-")[0],
							"orderBy":self.currtSelected.sort.split("-")[1]?self.currtSelected.sort.split("-")[1]:"",
							"pageSize": pageInfo.pageSize,
							"pageIndex": pageInfo.pageIndex,
						},
						cancelLightbox: true,
						"success": function(_data) {
							var datalist = _data.data.scoreMallList;
							if (!datalist || datalist.length < Config.base("pageSize")) {
								$.ajaxSettings.lightboxHide = false;
								pageInfo.isOver = true;
								DMJS.CommonTools.popTip("已经加载完所有数据！");
							}
							self.list = datalist;
							pageInfo.loaded = true;
							var $dom = self.$el.find("div[items='" + pageInfo.id + "']").find("div.ListArea");
							if (pageInfo.pageIndex === 1) {
								$dom.html(_.template(intergalMallTemplate, self));
							} else {

								$dom.append(_.template(intergalMallTemplate, self));
							}
							pageInfo.pageIndex++;


						},
						"complete": function() {
							if (!!callBack) {
								callBack();
							}
							self.loadListScroller(pageInfo);
							__call();

						}
					});
				}

			},
			'goodsDetail':function(e){
				var self = this;
				var $dom=$(e.target);
			
                if(!$dom.is("#goodsList > div")){
                    $dom=$dom.parents("#goodsList > div");
                }
                var goodsId = $dom.attr("goodsId");
                var type = $dom.attr("goodsType");
            	self.noDestroy = true;
            	DMJS.router.navigate("index/index/goodsDetail/"+goodsId, true);
            	
			},
			"loadListScroller": function(pageInfo) {
				
				var self = this;
				var type = pageInfo.id;
				!self.scroller && (self.scroller = {});
				var wraper = $("div[items='" + type + "']");
				if (!self.scroller[type]) {
					wraper.height(wrapView.height - $("#header").height() - $("#goodsCondition").height() - $("#footer").height());
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
			'closeUP':function(e){
	        	 $("#borderb-gra").addClass("uhide");
	        	 $("#slide-mask").addClass("uhide");
	        	 this.curel.children().eq(1).removeClass("flipeImg");
             },
             'removeUP':function(e){
             	 $("#borderb-gra").removeClass("uhide");
             	 $("#slide-mask").removeClass("uhide");
             },
			//点击头部tap选择条件
			'choseCondition':function(e){
	        	var self=this;
	        	var $dom=$(e.target);
	        	if(!$dom.is("div[listStatus]")){
	        		$dom=$dom.parents("div[listStatus]");
	        	}
	        	
	        	
	        	if($('#borderb-gra').hasClass('uhide')){
	        		 $dom.children().eq(1).addClass("flipeImg");
	        	     self.removeUP();
	        	}else{
	        		if(self.curel.attr("listStatus")==$dom.attr("listStatus"))
	        		{
	        			self.closeUP();
	        		}else{
	        			self.curel.children().eq(1).removeClass("flipeImg");
	        			$dom.children().eq(1).addClass("flipeImg");
	        		}
	        	}
	        	//存放点击的条件
	        	self.curel=$dom;
	        	$("#borderb-gra").find("ul").each(function(index,element)
	        	{
	        		if(this.dataset.flag==$dom.attr("listStatus")){
	        			$(this).removeClass("uhide");
	        		}else{
	        			$(this).addClass("uhide");
	        		}
	        		
	        	});
	        	self.loadScroller2('borderb-gra');
	        	
	        	
	        },
	        //点击tap里的内容
			'selectedCondition':function(e){
				var self = this;
				var $dom=$(e.target);
				
                var type = $dom.parent().attr("data-flag");
                
                self.currtSelected[type] = $dom.attr("conditiontype");
                //跟换选中图标
            	$dom.addClass("borderb-gra02").siblings().removeClass("borderb-gra02");
            	//判断是否选中非第一行条件
            	if($dom.prev().is('li')){
            		self.curel.children().eq(0).addClass("t-oran");
            		self.curel.children().eq(0).html($(e.target).html());
            		self.curel.children().eq(1).removeClass("mall-down-ico").addClass("mall-downActive-ico");
            	}else{
            		self.curel.children().eq(0).removeClass("t-oran");
            		self.curel.children().eq(0).html($dom.attr("titleName"));
            		self.curel.children().eq(1).removeClass("mall-downActive-ico").addClass("mall-down-ico");
            	}
            	 //收起选择框
            	self.closeUP();
            	
	        	self.pageInfo = {
					"1": {
						"pageSize": 10,
						"pageIndex": 1,
						"loaded": false,
						"isOver": false,
						"id": 1
					}
				};
	             self.loadDates(self.pageInfo[1]);
			},

		});

		return mallView;
	});