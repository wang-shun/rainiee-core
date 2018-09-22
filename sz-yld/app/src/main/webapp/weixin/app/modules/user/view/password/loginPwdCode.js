define(['text!userTemplate/password/loginPwdCode.html','commonTool/validator',
], function(loginPwdCodeTemplate, Validator) {
    var loginPwdCodeView = DMJS.DMJSView.extend({
        id: 'loginPwdCodeContent',
        name: 'loginPwdCodeContent',
        tagName: 'div',
        className: "loginPwdCodeContent",
        events: {
        	'input #phoneValue':'formatInput',
        	'input #verifyCode':'toTapNext'
        },
        init: function(options) {
            var self = this;
            self.isGetVerCode = "false";
            _.extend(self, options);
        },
        render: function() {
            var self = this;
            this.noDestroy=false;
           	this.$el.html(_.template(loginPwdCodeTemplate,{}));
           	if(DMJS.userInfo){
           		this.$el.find("#yzm").removeClass("c-gray6").attr({action:"action:getMobileCode"});
           	}
            return this;
        },
        //格式化手机号码输入
        "formatInput":function(e){
        	var value = $("#phoneValue").val().trim();
        	if(value.length==4||value.length==9){
        		var length = value.length;
        		value = value.substr(0,length-1)+" "+value.charAt(length-1);
        	}
        	$("#phoneValue").val(value);
        	$("#phone").val(value.replace(/\s/g,""));
        	var $dom=$(e.target).val();
        	if($dom.length>0){
        		$("#yzm").removeClass("c-gray6").attr({action:"action:getMobileCode"});
        	}else{
        		$("#yzm").addClass("c-gray6").removeAttr("action");
        	}
        },
        toTapNext : function(e){
			var $dom=$(e.target).val();
			if($dom.length>0){
				$("#next").removeClass("c-gray6").attr({action:"action:next"});
			}else{
				$("#next").addClass("c-gray6").removeAttr("action");
			}
		},
        getMobileCode:function(){
        	var self=this;
        	if(!Validator.check($("#phone"))) {return false;}
        	var timee=document.getElementById('yzm_count');
			var yzm=document.getElementById('yzm');
			var yzm_info=document.getElementById('yzm_info');
			var data={type:"ZHZF"};
			if(DMJS.userInfo){
				data.phone=DMJS.userInfo.phoneNumber;
			}else{
				data.phone=$("#phone").val();
			}
			self.controller.userModel.getMobileCode({'data':data,'cancelLightbox':false,
			"noCache":true,"success":function(response){
				yzm.style.display="none";
				yzm_info.style.display="block";
				self.reckTime(timee,yzm,yzm_info);
				self.isGetVerCode = "true";
				DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"), "验证码已发送,请查收！");
			},'error':function(response){
				if(response.description.indexOf("号码未认证")>=0){
					var phone=$('#phone').val();
					wrapView.FlipPrompt.confirm({    //提示去注册
               				title: "",
               				content: '手机号码未注册,您可以用它来注册一个新账号!',
               				FBntconfirm: "注册",
               				FBntcancel: "取消",
               				FBntConfirmColor: "pop_btn_orange",
               				autoCloseBg:"false",
               			}, function() {
               				DMJS.router.navigate("user/personal/register/"+phone, true);         				
               			}, function() {
							/*$('#phone').val('');
							$("#phoneValue").val('');
							$("#yzm").addClass("c-gray6").removeAttr("action");*/
               			});
					
				}else{
					
					DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"), response.description);
				}
				
			}
        	});
        
        },
        next:function(){
        	var self=this;
        	
        	var isGetCode = self.isGetVerCode;
			if("false"==isGetCode){
				DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"), "请先获取手机验证码");
				return;
			}
			
        	var parms=$("#"+self.id).getFormValue();
        	if(DMJS.userInfo){
        		parms['phone']=DMJS.userInfo.phoneNumber;
        	}
        	parms['type']="ZHZF";
        	if(Validator.check($("#"+self.id))){
			self.controller.userModel.checkVerifyCode({'data':parms,'cancelLightbox':false,
			"noCache":true,"success":function(response){
				if(DMJS.userInfo){
					DMJS.router.navigate('user/password/loginPwd/'+DMJS.userInfo.phoneNumber,true);
				}else{
					DMJS.router.navigate('user/password/loginPwd/'+$('#phone').val(),true);
				}
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
    return loginPwdCodeView;
});
