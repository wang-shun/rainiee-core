define(['text!userTemplate/personal/experienceInfo.html','commonTool/tool'
], function(experienceInfoTemplate, tool) {
    var experienceInfoView = DMJS.DMJSView.extend({
        id: 'experienceInfoContent',
        name: 'experienceInfoContent',
        tagName: 'div',
        className: "experienceInfoContent",
        events: {
        },
        init: function(options) {
            var self = this;
            _.extend(self, options);
        },
        render: function() {
            var self = this;
            this.noDestroy=false;
            self.experienceInfo();
            return this;
        },
        experienceInfo:function(){
        	var self=this;
        	self.controller.userModel.experienceInfo({'data':{expId:self.data.id,status:self.data.status},'cancelLightbox':true,
        	"noCache":true,
			"success":function(response){
				response.data[0].status = self.data.status;
				self.$el.html(_.template(experienceInfoTemplate,response.data[0]));
			},
			'error':function(response){
			    self.$el.html(_.template(experienceInfoTemplate,response));
				DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"), response.description);
        	}
        });},
    });
    return experienceInfoView;
});
