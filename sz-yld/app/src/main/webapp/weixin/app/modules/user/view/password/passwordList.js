define(['text!userTemplate/password/passwordList.html','commonTool/tool'
], function(passwordListTemplate, tool) {
    var passwordView = DMJS.DMJSView.extend({
        id: 'passwordListViewContent',
        name: 'passwordListViewContent',
        tagName: 'div',
        className: "passwordListViewContent",
        events: {
        	'tap #setMm':'setMm'
        },
        init: function(options) {
            var self = this;
            _.extend(self, options);
        },
        render: function() {
            var self = this;
           	this.$el.html(_.template(passwordListTemplate,{}));
            return this;
        },
        unautherized:function(){
        	DMJS.router.navigate("user/personal/unautherized", true);
        },
        setMm:function(){
      
        	if(!!DMJS.userInfo.mobileVerified){
        		DMJS.router.navigate("user/personal/unautherized", true);
        	}else{
        		wrapView.FlipPrompt.confirm({
	   				title:Config.lang("alertCommonTitle"),
	   				content:'请先认证手机号码！',
	   				FBntconfirm:"设置",
	   				FBntcancel:"取消",
	   				FBntConfirmColor: "pop_btn_orange",
	       			},function(){DMJS.router.navigate("user/personal/unautherized",true);},function(){});
        	}
        	
        }
       
       
    });
    return passwordView;
});
