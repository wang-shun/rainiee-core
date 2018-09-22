define(['text!userTemplate/personal/myReward.html','commonTool/tool'
], function(myRewardTemplate, tool) {
    var myRewardView = DMJS.DMJSView.extend({
        id: 'myRewardViewContent',
        name: 'myRewardViewContent',
        tagName: 'div',
        className: "myRewardViewContent",
        events: {
        	
        },
        init: function(options) {
            var self = this;
            _.extend(self, options);
        },
        render: function() {
            var self = this;
            this.noDestroy=false;
            self.myReward();
            return this;
        },
        myReward:function(){
        	var self=this;
        	self.controller.userModel.myReward({'data':{},'cancelLightbox':false,
        	"noCache":true,
			"success":function(response){
				self.$el.html(_.template(myRewardTemplate,response.data));
				self.loadScroller();
			},
			'error':function(response){
				self.$el.html(_.template(myRewardTemplate,response));
				DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"), response.description);
			},
        	});
        	
        }
       
    });
    return myRewardView;
});
