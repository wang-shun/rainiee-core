define(['text!userTemplate/password/tranPwd.html','commonTool/tool','commonTool/validator',
], function(tranPwdTemplate, tool,Validator) {
    var tranPwdView = DMJS.DMJSView.extend({
        id: 'tranPwdCotnent',
        name: 'tranPwdCotnent',
        tagName: 'div',
        className: "tranPwdCotnent",
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
        		parms['twoPwd'] = parms['onePwd'];
			self.controller.userModel.findTranPwd({'data':parms,'cancelLightbox':false,
			"noCache":true,"success":function(response){
					tool.hash_clear();
					DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"),'设置成功！',function(){
						DMJS.router.navigate("user/personal/userInfo",true);
					})
			},'error':function(response){
				tool.hash_clear();
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
					      self.$el.html(_.template(tranPwdTemplate,response.data));
				     }, 200);	
			},'error':function(response){
				DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"), response&&response.description);
			}
        	});
        },
       
       
    });
    return tranPwdView;
});
