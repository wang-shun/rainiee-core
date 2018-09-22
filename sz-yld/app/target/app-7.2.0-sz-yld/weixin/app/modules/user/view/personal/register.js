define(['text!userTemplate/personal/register.html', 'commonTool/validator','commonTool/tool' ], function(registerTemplate, Validator,tool) {
	var registerView = DMJS.DMJSView.extend({
		id: 'registerViewContent',
		name: 'registerViewContent',
		tagName: 'div',
		className: "bg-c-gray",
		events: {
			'input #phone':'toTapVerify',
			'input #verifyCode':'toTapNext'
		},
		init: function(options) {
			var self = this;
			self.isGetVerCode = "false";
			_.extend(self, options);
		},
		render: function() {
			var self = this;
			this.noDestroy = false;
//			self.Reg();
			this.$el.html(_.template(registerTemplate, self));
			DMJS.agreementEnable('ZC',"agreementFlag",function(){
				if($('#agreementFlag').hasClass('uhide')){
					$('#agreementFlag').remove();
				}
			});
			self.loadScroller();
			if(self.info && self.info!=""){
				this.$el.find("#yzm").removeClass("c-gray6").attr({action:"action:getMobileCode"});
			}
//			$("div[item='listItem']").css('height',((document.body.clientHeight||document.documentElement.clientHeight)-$("#header").height())+"px");//内容块占满高度
			return this;
		},
		toTapVerify : function(e){
			var $dom=$(e.target).val();
        	if($dom.length>0){
        		$("#yzm").removeClass("c-gray6").attr({action:"action:getMobileCode"});
        	}else{
        		$("#yzm").addClass("c-gray6").removeAttr("action");
        	}
		},
		toTapNext : function(e){
			var num=$("#phone").val();
			var $dom=$(e.target).val();
			if($dom.length>0 && num.length>0){
				$("#next").removeClass("c-gray6").attr({action:"action:next"});
			}else{
				$("#next").addClass("c-gray6").removeAttr("action");
			}
		},
		/*Reg:function(){
        	var self=this;
        	self.controller.userModel.REGEX({'data':{},'cancelLightbox':true,
			"noCache":true,"success":function(response){
					self.$el.html(_.template(registerTemplate,_.extend(self,response)));
					self.loadScroller();
			},'error':function(response){
				DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"), response&&response.description);
			}
        	});
        },*/
		getMobileCode: function(e) {
			var self = this;
			var timee=document.getElementById('yzm_count');
			var yzm=document.getElementById('yzm');
			var yzm_info=document.getElementById('yzm_info');
			if (!Validator.check($("#phone"))) {return false}
				self.controller.userModel.getMobileCode({
					'data': {phone: $("#phone").val(),type: "RZ"},'cancelLightbox': false,"noCache": true,
					"success": function(response) {
						yzm.style.display="none";
						yzm_info.style.display="block";
						self.reckTime(timee,yzm,yzm_info);
						self.isGetVerCode = "true";
						DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"), "验证码已发送,请查收！");
					},
					'error': function(response) {
						if(response.description.indexOf("已注册")>=0){
					var phone=$('#phone').val();
					wrapView.FlipPrompt.confirm({    //提示去注册
               				title: "",
               				content: '手机号码已注册，您可以用它直接登录!',
               				FBntconfirm: "登录",
               				FBntcancel: "确定",
               				FBntConfirmColor: "pop_btn_orange",
               				FBntCancelColor:"pop_btn_orange",
               			}, function() {
               				DMJS.router.navigate("user/personal/login/"+phone, true);  
               				DMJS.CommonTools.hash_clear();
               			}, function() {
               			});
					
				}else{
					
					DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"), response.description);
				}
					}});
		},
//		submit: function() {
//			var self = this;
//			var parms = $("#" + self.id).getFormValue();
//			parms['type'] = "RZ";
//			if(self.weixinId){
//				parms['weixinId']=self.weixinId;//用户openid，空值则不绑定注册
//			}
//			if (Validator.check($("#" + self.id))) {
//				self.controller.userModel.checkVerifyCode({
//					'data': parms,
//					'cancelLightbox': false,
//					"noCache": true,
//					"success": function(response) {
//						//类似于 注册 流程这些模块  服务器响应后 要清除 listHash 这类型数据不需要保存
//						tool.hash_clear();
//						DMJS.userInfo = response.data;
//						DMJS.userInfo.accountName=response.data.userName
//						var userInfo = JSON.stringify(DMJS.userInfo);
//						userInfo = encodeURI(userInfo); 
//						document.cookie="userInfo="+userInfo; 
////						DMJS.router.navigate('user/personal/registerInfo/' + $("#phone").val(), true);
//						DMJS.router.navigate('user/personal/userInfo', true);
//					},
//					'error': function(response) {if(response.description.indexOf("已注册")>=0){
//					var phone=$('#phone').val();
//					wrapView.FlipPrompt.confirm({    //提示去注册
//             				title: "",
//             				content: '手机号码已注册，您可以用它直接登录!',
//             				FBntconfirm: "登录",
//             				FBntcancel: "确定",
//             				FBntConfirmColor: "pop_btn_orange",
//             				FBntCancelColor:"pop_btn_orange",
//             			}, function() {
//             				DMJS.router.navigate("user/personal/login/"+phone, true);  
//             			}, function() {});
//					
//				}else{
//					
//					DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"), response.description);
//				}}
//				});
//			}
//		},
		"turnRememberRido": function(e) {
			if ($("input[type='checkbox']").is(":checked")) {
				$("input[type='checkbox']").removeAttr("checked");
			} else {
				$("input[type='checkbox']").attr("checked", true);
			}
		},
		agreement: function(type) {
			this.noDestroy = true;
			DMJS.router.navigate("user/personal/agreement/"+type, true);
		},
		next: function() {
			var self = this;
			
			var isGetCode = self.isGetVerCode;
			if("false"==isGetCode){
				DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"), "请先获取手机验证码");
				return;
			}
			
			var parms = $("#" + self.id).getFormValue();
			parms['type'] = "RZ";
			var phone=$('#phone').val();
			var verifyCode=$('#verifyCode').val();
			var nextPageData = phone+"-"+verifyCode
			if (Validator.check($("#" + self.id))) {
				self.controller.userModel.checkVerifyCode({
					'data': parms,
					'cancelLightbox': false,
					"noCache": true,
					"success": function(response) {
						DMJS.router.navigate('user/personal/registerInfo/' + nextPageData, true);
					},
					'error': function(response) {
						DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"), response.description);
					}
				});
			}
		},
		login:function(){
				tool.hash_clear();
				DMJS.router.navigate("user/personal/login", true);
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
			
			self.destroyChilds();
            // 解除事件绑定
            self.undelegateEvents();
//          if (self.scroller) {
//              self.scroller.destroy();
//              self.scroller = undefined;
//          }
            // 移除DOM元素
            self.$el.remove();
		}
	});
	return registerView;
});