define(['text!userTemplate/password/tranPwdUpdate.html','commonTool/validator',
], function(tranPwdUpdateTemplate,Validator) {
    var tranPwdUpdateView = DMJS.DMJSView.extend({
        id: 'tranPwdUpdateContent',
        name: 'tranPwdUpdateContent',
        tagName: 'div',
        className: "tranPwdUpdateContent",
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
        	var pwd = $('#onePwd');
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
					self.$el.html(_.template(tranPwdUpdateTemplate,response.data));
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
			self.controller.userModel.updateTranPwd({'data':parms,'cancelLightbox':false,"noCache":true,
			"success":function(response){
					  DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"),'修改交易密码成功!',
					  function(){DMJS.router.navigate("user/personal/userInfo",true);});
			},'error':function(response){DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"), response&&response.description);}});
        },
    });
    return tranPwdUpdateView;
});
