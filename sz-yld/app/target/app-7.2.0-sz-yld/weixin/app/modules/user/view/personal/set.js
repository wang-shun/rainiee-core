define(['text!userTemplate/personal/set.html','commonTool/tool','common/Context'
], function(setTemplate, tool,appContext) {
    var setView = DMJS.DMJSView.extend({
        id: 'setViewContent',
        name: 'setViewContent',
        tagName: 'div',
        className: "setViewContent",
        events: {
        	
        },
        init: function(options) {_.extend(this, options);},
        render: function() {
            var self = this;
            this.noDestroy=false;
           	this.$el.html(_.template(setTemplate,{}));
           	self.loadScroller();
            return this;
        },
       logout:function(){
    	   var self=this;
			if(DMJS.userInfo){
			self.controller.userModel.logout({
				"success":function(){
				DMJS.userInfo = undefined;
				DMJS.CommonTools.delCookies();
				tool.hash_clear();
				DMJS.router.navigate("index/index/index",true);
			},
				"error":function(){
				tool.hash_clear();
				console.log("退出失败！");
				},
				});
			}
			else{
			tool.hash_clear();
			DMJS.router.navigate("user/personal/login",true);
			}
       }
    });
    return setView;
});
