define(['text!userTemplate/personal/orderDetail.html',
        'commonTool/tool'],
		function(orderDetail,tool) {
	var orderDetailView = DMJS.DMJSView.extend({ //项目详情
		id: 'orderDetail',
		name: 'orderDetail',
		tagName: 'div',
		className: "orderDetail",
		events: {
			
		},
		init: function(options) {
			var self = this;
			_.extend(self, options);
			
		},
		render: function() {
			var self = this;
			
			self.controller.userModel.myOrderItem({
				cancelLightbox: true,
				data: {
					orderId:self.orderId,
				},
				cancelLightbox: true,
				"success": function(_data) {
					self.list = _data.data;
					self.orderList =  _data.data.myOrderInfos;
					self.$el.html(_.template(orderDetail, self)); // 将tpl中的内容写入到 this.el 元素中  
					self.loadScroller();
				},
			});
			setTimeout(function(){self.loadScroller();console.log("self.loadScroller()")},3000);
			return this;
		},
		
	});

	return orderDetailView;
});