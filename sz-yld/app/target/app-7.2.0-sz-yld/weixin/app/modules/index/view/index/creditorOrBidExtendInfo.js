define([ 'text!indexTemplate/index/creditorOrBidExtendInfo.html','commonTool/tool'], function(creditorOrBidExtendInfoTemplate,tool) {
	var creditorDetailView = DMJS.DMJSView.extend({
		id : 'creditorOrBidExtendInfo',
		name : 'creditorOrBidExtendInfo',
		tagName : 'div',
		className : "creditorOrBidExtendInfo",
		events : {
			'tap .JSDropDown': 'JSDropDown'
		},
        
		init : function(options) {
			var self = this;
			_.extend(self, options);
		},
		render : function() {
			var self = this;
			
			if (!self.dataFlag) {

				DMJS.router.navigate('index/index/creditorTransfer', true);
				return false;
			}
			self.isStatusType = tool.isStatusType;
			self.fmoney = tool.fmoney;
			self.changePaymentType = tool.changePaymentType;
			self.gaibAmount = tool.gaibAmount;
			self.bidrecordslist=[];
			self.repayList=[];
			self.biditem={};
			self.creditorDetail();

			
			return this;
		},
		JSDropDown: function(e) {//点击显示隐藏
			var $dom = $(e.target),self=this;
			if (!$dom.is(".JSDropDown")) {
				$dom = $(e.target).parents(".JSDropDown");
			}
			tool.JSDropDown($dom);
			self.loadScroller(); 
		},
		
		isLogin:function(){//登录后才可以看标的附加信息
			var self=this;
			var result = DMJS.localData.get('loginStatus');
		        	if(result&&result=="Y"){
		        		$('#login_more').hide();
		        		self.creditorInfo();
		        		self.creditorRecordsList();
		        		self.creditorRepayList();
		        	}
			
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
					self.$el.find('#bidxx').html(_.template(creditorDetailExtendInfoTpl, self));
					self.loadScroller();
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
					self.$el.find('#bidxx').html(_.template(creditorDetailExtendInfoTpl, self));
					self.loadScroller();
				}
        	});
			
		},
		
	});

	return  creditorDetailView;
});
