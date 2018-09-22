define([ 'text!indexTemplate/index/creditorDetail.html','text!indexTemplate/index/creditorDetailExtendInfo.html','commonTool/tool','commonTool/slide'], function(creditorDetailTemplate,creditorDetailExtendInfoTpl,tool,Slide) {
	var creditorDetailView = DMJS.DMJSView.extend({
		id : 'creditorDetailView',
		name : 'creditorDetailView',
		tagName : 'div',
		className : "creditorDetailView",
		events : {
			'tap .JSAppointDropDown': 'JSAppointDropDown',
			'tap #Jlogin':'Jlogin'
		},
        
		init : function(options) {
			var self = this;
			_.extend(self, options);
		},
		render : function() {
			var self = this;
			
			if (!self.dataFlag) {
				
				window.listHash.length=window.listHash.length-1;
				DMJS.router.navigate('index/index/creditorTransfer', true);
				return false;
			}
			self.isStatusType = tool.isStatusType;
			self.fmoney = tool.fmoney;
			self.changePaymentType = tool.changePaymentType;
			self.gaibAmount = tool.gaibAmount;
			self.bidrecordslist=undefined;
			self.repayList=undefined;
			self.biditem=undefined;
			self.creditorDetail();

			
			return this;
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
			}
		},
		Jlogin:function(){//跳转到登录
			window.listHash.push("user/personal/login");
			DMJS.router.navigate("user/personal/login", true);
		},
		'creditorDetail':function(){//债权投资详情
			var self=this;
			
					var data = self.dataL;
					
					var amount = data.amount;
					var remainAmount = data.remainAmount;
					var progress = 0;
					if (data.status === 'YJQ' || data.status === 'HKZ') {
						progress = 100;
						
					} else if (data.status === 'YFB') {
						progress = 0;
						
					} else {
						progress = (amount - remainAmount) / amount * 100;
						if (progress <= 0) {
							progress = 0;
						} else if (progress < 1) {
							progress = Math.ceil(progress); //向上取整
						} else {
							progress = Math.floor(progress); //向下取整
						}
					}
					data.progress=progress;
					self.list = data;
					self.list.qxUnit = data.isDay == 'S' ? '天' : '个月';
					self.$el.html(_.template(creditorDetailTemplate, self)); // 将tpl中的内容写入到
					self.loadScroller(); 
					self.isLogin();
				
		
		},
		isLogin:function(){//登录后才可以看标的附加信息
			var self=this;
					//暂时不需要
		        	//if(DMJS.userInfo){
		        		$('#login_more').hide();
		        		self.creditorInfo();
		        	//}
		},
		creditorInfo:function(){//债权附加信息
			var self = this ;
        	self.controller.indexModel.bidItem({
				cancelLightbox: true,
				data: {
					"bidId": self.bidId
				},
				"success": function(_data) {
					self.biditem=_data.data;
					self.creditorRecordsList();
				}
        	});
		},
		creditorRepayList:function(){//还款计划
			var self=this;
			self.controller.indexModel.repayList({
				cancelLightbox: true,
				data: {
					"bidId": self.bidId
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
						self.loadScroller();
					
				}
        	});
			
		},
		
		creditorRecordsList:function(){//投资记录
			var self=this;
			self.controller.indexModel.bidRecordsList({
				cancelLightbox: true,
				data: {
					"bidId": self.bidId

				},
				"success": function(_data) {
					
						self.bidrecordslist=_data.data;
						self.creditorRepayList();
					
				}
        	});
			
		},
		
	});

	return  creditorDetailView;
});
