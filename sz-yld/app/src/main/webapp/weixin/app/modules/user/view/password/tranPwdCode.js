define(['text!userTemplate/password/tranPwdCode.html','commonTool/validator',
], function(tranPwdCodeTemplate, Validator) {
    var tranPwdCodeView = DMJS.DMJSView.extend({
        id: 'tranPwdCodeContent',
        name: 'tranPwdCodeContent',
        tagName: 'div',
        className: "tranPwdCodeContent",
        events: {
        	
        },
        init: function(options) {
            var self = this;
            _.extend(self, options);
        },
        render: function() {
            var self = this;
            this.noDestroy=false;
           	this.$el.html(_.template(tranPwdCodeTemplate,{}));
            return this;
        },
        getMobileCode:function(){
        	var self=this;
        	/*if(!Validator.check($("#phone"))) {return false;}*/
        	var timee=document.getElementById('yzm_count');
			var yzm=document.getElementById('yzm');
			var yzm_info=document.getElementById('yzm_info');
			self.controller.userModel.getMobileCode({'data':{phone:DMJS.userInfo.phoneNumber,type:"ZHZF"},'cancelLightbox':false,
			"noCache":true,"success":function(response){
				yzm.style.display="none";
				yzm_info.style.display="block";
				self.reckTime(timee,yzm,yzm_info);
				DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"), "验证码已发送,请查收！");
			},'error':function(response){
				DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"), response.description);
			}
        	});
        
        },
        next:function(){
        	var self=this;
        	var parms=$("#"+self.id).getFormValue();
        	parms['phone']=DMJS.userInfo.phoneNumber;
        	parms['type']="ZHZF";
        	if(Validator.check($("#"+self.id))){
			self.controller.userModel.checkVerifyCode({'data':parms,'cancelLightbox':false,
			"noCache":true,"success":function(response){
				DMJS.router.navigate('user/password/tranPwd/'+DMJS.userInfo.phoneNumber,true);
			},'error':function(response){
				DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"), response.description);
			}
        	});
        }
        },
        reckTime :function(obj,yzm,yzm_info){
			var times=60;
			var sid=setInterval(function(){
					times=times-1;
					obj.innerHTML=times;
					if(times==0){
						clearInterval(sid);
						yzm.style.display="block";
						yzm_info.style.display="none";
						obj.innerHTML=60;
					}
					console.log(times);
				},1000);
			window.threadId=sid;
			},
			destroy:function(){
			var self=this;
			//clearInterval(window.threadId);
			//delete window.threadId;
			self.destroyChilds();
            // 解除事件绑定
            self.undelegateEvents();
            if (self.scroller) {
                self.scroller.destroy();
                self.scroller = undefined;
            }
            // 移除DOM元素
            self.$el.remove();
		}
       
       
    });
    return tranPwdCodeView;
});
