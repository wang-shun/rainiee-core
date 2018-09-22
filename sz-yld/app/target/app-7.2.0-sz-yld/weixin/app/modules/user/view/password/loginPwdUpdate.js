define(['text!userTemplate/password/loginPwdUpdate.html','commonTool/tool','commonTool/validator',
], function(loginPwdUpdateTemplate, tool,Validator) {
    var loginPwdUpdateView = DMJS.DMJSView.extend({
        id: 'loginPwdUpdateContent',
        name: 'loginPwdUpdateContent',
        tagName: 'div',
        className: "",
        events: {
        	
        },
        init: function(options) {
            var self = this;
            _.extend(self, options);
        },
        render: function() {
            var self = this;
         	self.Reg();
            return this;
        },
        //密码查看
        "lookPwd":function(e){
        	var pwd = $('#password');
        	if(e.hasClass("b-image-hide")){
        		pwd.attr("type","text");
        		e.removeClass("b-image-hide").addClass("b-image-show");
        	}else{
        		pwd.attr("type","password");
        		e.removeClass("b-image-show").addClass("b-image-hide");
        	}
        },
         Reg:function(){
        	var self=this;
        	self.controller.userModel.REGEX({'data':{},'cancelLightbox':true,
			"noCache":true,"success":function(response){
			   setTimeout(function() {
					self.$el.html(_.template(loginPwdUpdateTemplate,response.data));
				}, 200);
					
			},'error':function(response){
				DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"), response&&response.description);
			}
        	});
        },
        update:function(){
        	var self=this;
        	var parms=$("#"+self.id).getFormValue();
        	if(!Validator.check($("#"+self.id))){return false;}
        	parms['twoPwd'] = parms['onePwd'];
			self.controller.userModel.loginPwdUpdate({'data':parms,'cancelLightbox':false,
			"noCache":true,"success":function(response){
				//类似于 注册 流程这些模块  服务器响应后 要清除 listHash 这类型数据不需要保存
					tool.hash_clear();
					DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"),'修改密码成功,请重新登录!',
					function(){DMJS.router.navigate("user/personal/login",true);});
			},'error':function(response){
				DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"), response&&response.description);}});
        },
       loginPwdCode:function(){
       		if(DMJS.userInfo && DMJS.userInfo.phone==""){
        		wrapView.FlipPrompt.confirm({    //提示去注册
               				title: "",
               				content: '未认证手机号，请去个人中心认证手机号！',
               				FBntconfirm: "设置",
               				FBntcancel: "取消",
               				FBntConfirmColor: "pop_btn_orange",
               				autoCloseBg:"true",
               			}, function() {
               				DMJS.router.navigate("user/personal/unautherized", true); 
               			}, function() {
               				
               			});
               	return false;		
        	}else{
        		DMJS.router.navigate("user/password/loginPwdCode", true); 
        	}
       },
       
    });
    return loginPwdUpdateView;
});
