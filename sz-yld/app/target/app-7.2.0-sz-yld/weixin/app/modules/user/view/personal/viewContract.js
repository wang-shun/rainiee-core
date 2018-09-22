define(['text!userTemplate/personal/viewContract.html','commonTool/tool'
], function(viewContractTemplate, tool) {
    var viewContractView = DMJS.DMJSView.extend({
        id: 'viewContract',
        name: 'viewContract',
        tagName: 'div',
        className: "viewContract",
        events: {
        
        },
        init: function(options) {
            var self = this;
            _.extend(self, options);
        },
        render: function() {
            var self = this;
             if(self.typeS=='zq'){
            	self.viewContractZQ();
            }else{
            	self.viewContractJK();
            }
            
            return this;
        },
        
        viewContractJK:function(){//查看借款合同
        	var self=this;
        	self.controller.userModel.agreementView({
					'data': {'id':self.bidId,'isGyb':false},
					'cancelLightbox': false,
					"noCache": true,
					"success": function(response) {
						
						self.$el.html(_.template(viewContractTemplate,response.data));
						self.loadScroller();
					},
					'error': function(response) {
						DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"), response.description);
					}
				});
        },
        viewContractZQ:function(){//查看债权合同
        	var self=this;
        	self.controller.userModel.zqzrAgreementView({
					'data': {'id':self.bidId},
					'cancelLightbox': false,
					"noCache": true,
					"success": function(response) {
						
						self.$el.html(_.template(viewContractTemplate,response.data));
						self.loadScroller();
					},
					'error': function(response) {
						DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"), response.description);
					}
				});
        },
    });
    return viewContractView;
});