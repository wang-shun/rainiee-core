define([ 'text!userTemplate/payment/cardInfo.html' ], function(cardInfoTemplate) {
	var cardInfoView = DMJS.DMJSView.extend({
		id : 'cardInfo',
		name : 'cardInfo',
		tagName : 'div',
		className : "cardInfo",
		events : {
		},
		data:{},
		init : function(options) {
			_.extend(this, options);
		},
		render : function() {
			var self = this;
			this.$el.html(_.template(cardInfoTemplate, self.data)); // 将tpl中的内容写入到
			//self.loadScroller(); 
			return this;
		},
	});

	return  cardInfoView;
});
