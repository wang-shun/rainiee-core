define(['text!userTemplate/personal/helpInfo.html'], function(helpInfoTemplate){
    var helpInfoView = DMJS.DMJSView.extend({//
        id: 'helpInfoContent',
        name: 'helpInfoContent',
        tagName: 'div',
        className: "helpInfoContent", 
        events: {
            
        },
        init: function(options){
           	_.extend(this,options); 
        },
        render: function(){
            var self=this; 
           	self.helpInfo();
           	return this;
        },
        helpInfo:function(){
        	var self=this;
        	self.controller.userModel.helpInfo({
        		data:{id:self.data.id,type:self.data.type},
                "noCache": true,
                cancelLightbox: true,
                "success": function(response) { 
                    _.extend(self.data, response.data);
                    if(!response.data.from&&response.data.from!="") self.data.from="";
                    self.$el.html(_.template(helpInfoTemplate, self.data)); // 将tpl中的内容写入到 this.el 元素中  
                     DMJS.CommonTools.imgDown.apply(self,[$('img')]);
                },
                 'error':function(response){DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"), response&&response.description);}
            });
        },

    });

    return helpInfoView;
});



