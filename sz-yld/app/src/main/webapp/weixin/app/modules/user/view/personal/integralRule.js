define(['text!userTemplate/personal/integralRule.html',
	'commonTool/tool',
	'commonConfig/url',
	'text!commonTemplate/foot/boTop.html'
], function(integralRule,tool,url,boTop) {
	var integralRuleView = DMJS.DMJSView.extend({ 
		id: 'integralRule',
		name: 'integralRule',
		tagName: 'div',
		className: "integralRule",
		events: {
		},
		init: function(options) {
			var self = this;
			self.orderFlag=0;//默认降序
			_.extend(self, options);
		},
		render: function() {
			var self = this;
			self.loadData();
		},
		loadData:function(){
			var self = this;
			self.controller.userModel.scoreRules({
                "noCache": true,
                cancelLightbox: true,
                "success": function(response) { 
              		if(response.code=="000000"){
              			self.data = response.data;
              			self.$el.html(_.template(integralRule, self));
              			self.loadScroller();
              		}
                },
                 'error':function(response){DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"), response&&response.description);}
            });
			
		},
		runScrollerTop:function(){
		 	var self=this;
            var type=self.pageInfo["1"].id;
        	this.scroller[type].scrollToPage(0,0,500);
        	$(".boTopTag").remove();
        }
	});

	return integralRuleView;
});