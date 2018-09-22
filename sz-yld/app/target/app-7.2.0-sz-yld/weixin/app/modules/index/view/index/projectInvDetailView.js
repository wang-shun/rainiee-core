define(['text!indexTemplate/index/projectInvDetail.html',
	'text!indexTemplate/index/creditorDetailExtendInfo.html',
	'commonTool/tool','commonClass/scroll/iscroll', 'commonTool/slide'],
		function(projectInvDetailTemplate, creditorDetailExtendInfoTpl,tool,iscroll,Slide) {
	var projectInvDetailV = DMJS.DMJSView.extend({ //项目详情
		id: 'projectInvDetail',
		name: 'projectInvDetail',
		tagName: 'div',
		className: "projectInvDetail",
		events: {
			'tap .JSAppointDropDown': 'JSAppointDropDown',
			'tap #Jlogin':'Jlogin'
		},
		init: function(options) {
			var self = this;
			_.extend(self, options);
			
		},
		render: function() {
			var self = this;
			self.isStatusType = tool.isStatusType;
			self.fmoney = tool.fmoney;
			self.changePaymentType = tool.changePaymentType;
			self.gaibAmount = tool.gaibAmount;
			self.bidrecordslist=null;
			self.repayList=null;
			self.biditem=null;
			self.bidDetail();
			
			return this;
		},
		Jlogin:function(){//跳转到登录
			DMJS.router.navigate("user/personal/login", true);
		},
		bidDetail: function() {//项目详情
			var self = this;
			
					var data = self.dataL;
					var amount = data.amount;
					var remainAmount = data.remainAmount;
					var progress = 0;
					if (data.status === 'YJQ' || data.status === 'HKZ') {
						progress = 100;
						
					} else if (data.status === 'YFB') {
						progress = 0;
					
					} else {
						
						progress = (amount - remainAmount) / amount ;
						if (progress <= 0) {
							progress = 0;
						} else if (progress < 1) {
							progress = Math.ceil(progress* 100); //向上取整
						} else {
							progress = Math.floor(progress* 100); //向下取整
						}
					}
					
					data.progress=progress;
					self.list = data;
					self.list.qxUnit = data.isDay == 'S' ? '天' : '个月';
					self.$el.html(_.template(projectInvDetailTemplate, self)); // 将tpl中的内容写入到 this.el 元素中  
					if(data.status === 'YFB'||data.status === 'TBZ'){
						self.remainTDetail(data);
					 DMJS.startThread("count_timer",1000,-1,function(){
			          	self.remainTDetail.apply(self,[data]);
			           });
					}
					
					self.isLogin();
					
					
				
	
		},
		agreement: function(type) {
			if(!!type){
				DMJS.router.navigate("user/personal/agreement/"+type, true);
			}
		},
		JSAppointDropDown: function(e) {
			var self = this;
			var $dom = $(e.target);
			var chooiceType = $dom.attr("listType");
			if(chooiceType!=self.currentType){
				self.currentType=chooiceType;
				
				$('.JSAppointDropDown li').removeClass('lbd-b-blue');
				$($('.JSAppointDropDown li')[chooiceType]).addClass('lbd-b-blue');
				
				self.slider.moveToPoint(parseInt(chooiceType));
				var viewHeight = $("#bidtxx").height() + $("div[items='" + chooiceType + "'] > div").height() + 100;
				self.loadListScroller(viewHeight,chooiceType);
			}
		},
		loadListScroller :function(viewHeight,chooiceType){
				var self = this;
				var minHeight = wrapView.height - $("#header").height() - $("#footer").height();
				if(viewHeight < minHeight){
					viewHeight = minHeight;
				}
				$("#projectInvDetail > div").height(viewHeight);
				self.loadScroller();
		},
	
remainTDetail:function(dataList){
        	
        	var self=this;
        	if(self.remainTime){
               self.remainTime -= 1000;
            }
            else{ 
            	if(dataList.status != 'YFB'){
	                var endTime = tool.changeMillisecond(dataList.endTime);
	        
	                 self.remainTime = endTime -dataList.timemp;//结束时间-数据库时间
            	}else{ 
	            
	                 self.remainTime = tool.changeMillisecond(dataList.publicDate)-dataList.timemp;//预发布时间-数据库时间
            	}
            }
            if(self.remainTime<=0){
                if(dataList.status != 'YFB'){
                	self.remainText = '已过期';
                }else{
                	setTimeout(function(){
                		DMJS.currentFoot=undefined;
                		var url = window.location.hash.replace("/"+window.location.hash.split("/")[4],"");
                    	DMJS.navigate(url,true);
                	},1000);
                	
                }
                DMJS.removeThread("count_timer");
                
            }
            else{
            	self.remainText=tool.ckTime(self.remainTime);
              
            }
            $("#remainTime").html(self.remainText);
            
            
        },
		isLogin:function(){//登录后才可以看标的附加信息
			var self=this;
			//暂时不需要
		        	//if(DMJS.userInfo){
		        		$('#login_more').hide();
		        		self.creditorInfo();//项目附加信息
		        	//}
			
		},
		creditorInfo:function(){//附加信息
			var self = this ;
        	self.controller.indexModel.bidItem({
				cancelLightbox: true,
				data: {
					"bidId": self.bidid
				},
				"success": function(_data) {
					self.biditem=_data.data;
					self.creditorRecordsList();//请求投资记录
				}
        	});
		},
		creditorRepayList:function(){//还款计划
			var self=this;
			self.controller.indexModel.repayList({
				cancelLightbox: true,
				data: {
					"bidId": self.bidid

				},
				"success": function(_data) {
						self.repayList=_data.data;
						self.$el.find('#bidxx').html(_.template(creditorDetailExtendInfoTpl, self));
						self.slider = new Slide(self.$el.find("#slider")[0], "H", function() {
							var cur = this.currentPoint;
							if(cur!=self.currentType){
								self.currentType=cur;
								$('.JSAppointDropDown li').removeClass('lbd-b-blue');
								$($('.JSAppointDropDown li')[cur]).addClass('lbd-b-blue');
							}
						}, false, function(e) {});
						var viewHeight = $("#bidtxx").height() + $("div[items='0'] > div").height() + 100;
						self.loadListScroller(viewHeight,0);
				}
          	});
			
		},
		
		creditorRecordsList:function(){//投资记录
			var self=this;
			self.controller.indexModel.bidRecordsList({
				cancelLightbox: true,
				data: {
					"bidId": self.bidid

				},
				"success": function(_data) {
						self.bidrecordslist=_data.data;
						self.creditorRepayList();
				}
        	});
			
		},
		
		destroy:function(){
			var self=this;
			DMJS.removeThread("count_timer");
			self.destroyChilds();
            // 解除事件绑定
            self.undelegateEvents();
            if (self.scroller) {
                self.scroller.destroy();
                self.scroller = undefined;
            }
            // 移除DOM元素
            self.$el.remove();
            
		}
	});

	return projectInvDetailV;
});