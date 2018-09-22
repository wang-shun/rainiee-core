define(['text!userTemplate/personal/agreement.html','commonTool/tool'
], function(agreementTemplate, tool) {
    var agreementView = DMJS.DMJSView.extend({
        id: 'agreementContent',
        name: 'agreementContent',
        tagName: 'div',
        className: "agreementContent",
        events: {
        
        },
        init: function(options) {
            var self = this;
            _.extend(self, options);
        },
        render: function() {
            var self = this;
            this.noDestroy=false;
            if(self.data.type==='FXTSH'){
            	self.agreement();	
            }else{
            	self.initType(self.agreement);
            }
          	
            return this;
        },
        initType:function(callback){
        	var self=this;
        	self.controller.commonModel.initType({'data':{type:self.data.type},'cancelLightbox':false,
			"noCache":true,
			"success":function(response){
				callback.apply(self,[response.data]);
			},
			'error':function(response){console.log(response);}
        	});
        	
        },
        agreement:function(data){
        	var self=this;
        	var agreementType=data?data.agreementType:'FXTSH';
        	self.controller.commonModel.agreement({'data':{type:agreementType},'cancelLightbox':false,
			"noCache":true,
			"success":function(response){
				console.log(response);	
				self.$el.html(_.template(agreementTemplate,response.data));
				self.loadScroller();
			},
			'error':function(response){console.log(response);}
        	});
        }
       
    });
    return agreementView;
});
