define(['text!userTemplate/password/loginPwd.html','commonTool/tool','commonTool/validator',
], function(loginPwdTemplate, tool,Validator) {
    var loginPwdView = DMJS.DMJSView.extend({
        id: 'loginPwdContent',
        name: 'loginPwdContent',
        tagName: 'div',
        className: "loginPwdContent",
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
        submit:function(){
        	var self=this;
        	var parms=$("#"+self.id).getFormValue();
        	if(self.data.phone&&/^[1][3|4|5|7|8][0-9]{9}$/.test(self.data.phone)){parms['phone']=self.data.phone;}
        	else{DMJS.CommonTools.alertCommon("温馨提示","获取不到手机号码！"); }
        	
        	if(Validator.check($("#"+self.id))){
        	parms['rePassword'] = parms['password'];
			self.controller.userModel.resetLoginPwd({'data':parms,'cancelLightbox':false,
			"noCache":true,"success":function(response){
				//类似于 注册 流程这些模块  服务器响应后 要清除 listHash 这类型数据不需要保存
					tool.hash_clear();
					DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"), response&&response.description,function(){
						DMJS.router.navigate("user/personal/login",true);
					})
			},'error':function(response){
				DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"), response&&response.description);
			}
        	});
        }
        },
        Reg:function(){
        	var self=this;
        	self.controller.userModel.REGEX({'data':{},'cancelLightbox':true,
			"noCache":true,"success":function(response){
						  	
						  	setTimeout(function() {
					self.$el.html(_.template(loginPwdTemplate,response.data));
				}, 200);
				
			},'error':function(response){
				DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"), response&&response.description);
			}
        	});
        },
       
       
    });
    return loginPwdView;
});
