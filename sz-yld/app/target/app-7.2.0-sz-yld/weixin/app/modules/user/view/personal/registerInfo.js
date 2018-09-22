define(['text!userTemplate/personal/registerInfo.html', 'commonTool/validator', 'commonTool/tool', 'common/Context'], function(registerInfoTemplate, Validator, tool, appContext) {
	var registerInfoView = DMJS.DMJSView.extend({
		id: 'registerInfoViewContent',
		name: 'registerInfoViewContent',
		tagName: 'div',
		className: "registerInfoViewContent",
		events: {

		},
		init: function(options) {
			_.extend(this, options);
		},
		render: function() {
			var self = this;
			self.Reg();
			return this;
		},
		//密码查看
		"lookPwd": function(e) {
			var pwd = $('#password');
			if(e.hasClass("b-image-hide")) {
				pwd.attr("type", "text");
				e.removeClass("b-image-hide").addClass("b-image-show");
			} else {
				pwd.attr("type", "password");
				e.removeClass("b-image-show").addClass("b-image-hide");
			}
		},
		Reg: function() {
			var self = this;
			self.controller.userModel.REGEX({
				'data': {},
				'cancelLightbox': true,
				"noCache": true,
				"success": function(response) {
					self.$el.html(_.template(registerInfoTemplate, response.data));
					//self.loadScroller();
				},
				'error': function(response) {
					DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"), response && response.description);
				}
			});
		},
		submit: function() {
			var self = this;
			var parms = $("#" + self.id).getFormValue();
			
			if(parms['accountName'] == parms['password'] && parms['accountName'] && parms['password']) {
				DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"), '用户名与密码不能相同，请重新输入！');
				return false;
			}
			
			if (escape(parms['password']).indexOf("%u")>=0){
				DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"),"密码不能含有中文字符");
				return false;
			}
			
			if(self.data.nextPageData && /^[1][3|4|5|7|8][0-9]{9}$/.test(self.data.nextPageData.split("-")[0])) {
				parms['phone'] = self.data.nextPageData.split("-")[0];
				parms['verifyCode'] = self.data.nextPageData.split("-")[1];
			}
			parms['newPassword'] = parms['password'];
			parms['type'] = "LC"; //注册   理财   LC  借款 JK
			parms['weixinId'] = DMJS.weixinId; //用户openid，空值则不绑定注册
			if(Validator.check($("#" + self.id))) {
				self.controller.userModel.register({
					'data': parms,
					'cancelLightbox': false,
					"noCache": true,
					"success": function(response) {
						//类似于 注册 流程这些模块  服务器响应后 要清除 listHash 这类型数据不需要保存
						tool.hash_clear();
						DMJS.userInfo = response.data;
						DMJS.userInfo.accountName = response.data.userName
						var userInfo = JSON.stringify(DMJS.userInfo);
						userInfo = encodeURI(userInfo);
						document.cookie = "userInfo=" + userInfo;
						DMJS.router.navigate("user/personal/userInfo", true);
					},
					'error': function(response) {
						tool.hash_clear();
						DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"), response && response.description);
					}
				});
			}

		}
	});
	return registerInfoView;
});