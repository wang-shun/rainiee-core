define(['text!userTemplate/personal/userInfo.html', 'commonTool/tool', "commonClass/weixin"], function(userInfoTemplate, tool, weixin) {
	var userInfoView = DMJS.DMJSView.extend({
		id: 'userinfoViewContent',
		name: 'userinfoViewContent',
		tagName: 'div',
		className: "userinfoViewContent",
		events: {
			'tap #more': 'more'
		},
		data: {
			x: 0,
			y: 100,
			id: ''
		},
		init: function(options) {
			var self = this;
			_.extend(self, options);
		},
		render: function() {
			var self = this;
			this.noDestroy = false;
			self.data.y = 70;
			DMJS.startThread("UdrawSingleConvas", 100, -1, function() {
				self.drawSingleConvas(self.data.x, self.data.y, 15);
				self.data.x -= 20;
				if(self.data.x == -240) {
					self.data.x = 0;
				}
			});
			self.myAccount();
			return this;
		},
		myAccount: function() {
			var self = this;
			self.controller.userModel.myAccount({
				"noCache": true,
				cancelLightbox: true,
				"success": function(response) {
					_.extend(self.data, response.data);

					self.userInfo();

				},
				'error': function(response) {
					DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"), response && response.description);
				}
			});
		},
		userInfo: function() {
			var self = this;
			self.controller.userModel.userInfo({
				"noCache": true,
				cancelLightbox: true,
				"success": function(response) {
					var _data = response.data,
						dataL = self.data;
					dataL.riskType = _data.riskType;
					dataL.riskTimes = _data.riskTimes;
					dataL.isOpenRisk = _data.isOpenRisk;
					//              	dataL.isGuarantee=_data.isGuarantee;//判断是否曾经是担保人
					//              	dataL.fxbzj=_data.fxbzj;//担保人的保证金
					self.riskTimes = _data.riskTimes;
					self.$el.html(_.template(userInfoTemplate, dataL)); // 将tpl中的内容写入到 this.el 元素中
					DMJS.CommonTools.backConst("Mall.IS_MALL", function() {
						if(this.Const == "true") {
							$("#myMall").removeClass("uhide");
							self.loadScroller();
						}
					});
					self.loadScroller();
					//未读信息
					if(response.data.letterCount > 0)
						$("#msgImg").removeClass('b_icon_msg').addClass('b_icon_msgr');
					else {
						$("#msgImg").removeClass('b_icon_msgr').addClass('b_icon_msg');
					}
					//更新用户信息
					DMJS.userInfo = response.data;
					weixin.init();
					var userInfo = JSON.stringify(DMJS.userInfo);
					userInfo = encodeURI(userInfo);
					document.cookie = "userInfo=" + userInfo;
				},
				'error': function(response) {
					DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"), response && response.description);
				}
			});
		},
		more: function(e) {
			var self = this;
			$("#more-info").toggle();
			if($(e.target).hasClass('img-up')) {
				window.clearInterval(self.data.id);
				self.data.id = setInterval(function() {
					if(self.data.y < 70) {
						self.data.y += 2;
					} else {
						window.clearInterval(self.data.id);
					}
				}, 100);
				$(e.target).removeClass('img-up').addClass('img-dw');
				this.loadScroller();
			} else {
				window.clearInterval(self.data.id);
				self.data.id = setInterval(function() {
					if(self.data.y > 10) {
						self.data.y -= 2;
					} else {
						window.clearInterval(self.data.id);
					}
				}, 100);
				$(e.target).removeClass('img-dw').addClass('img-up');
				this.loadScroller();
			}
		},
		recharge: function() {
			DMJS.router.navigate("user/payment/recharge",true);	
		},
		withdraw: function() {
			var self = this;
			self.controller.userModel.myBankList({
				'data': {},
				'cancelLightbox': false,
				"noCache": true,
				"success": function(response) {
					if(response.data.myBankList.length <= 0) {

						wrapView.FlipPrompt.confirm({ //提示去绑卡
							title: "温馨提示",
							content: '请先绑卡',
							FBntconfirm: "确定",
							FBntcancel: "取消",
							FBntConfirmColor: "pop_btn_orange",
						}, function() {
							DMJS.router.navigate("user/payment/cardManage", true);
							DMJS.CommonTools.hash_clear();
						}, function() {});
						return false;
					}
					DMJS.router.navigate("user/payment/withdraw", true);
				},
				'error': function(response) {
					DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"), response.description);
				}
			});
		},
		cardManage: function() {
			DMJS.router.navigate("user/payment/cardManage", true);
		},
		riskInvestment: function() {
			var self = this;
			if(self.riskTimes === 0) {
				DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"), '您今年的评估次数已用完');
				return false;
			}
			DMJS.router.navigate("user/personal/riskInvestment", true);
		},

		gotoSignIn: function() {
			var self = this;
			self.controller.userModel.userScore({
				"noCache": true,
				cancelLightbox: true,
				"success": function(response) {

					if(response.code == "000000") {
						response.data.giveScore;
						DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"), "您已获得" + response.data.giveScore + "积分");
						$("#signId").removeClass("unSign-in").addClass("sign-in").removeAttr("action");
						//更新用户信息
						self.reflush();
					}

				},
				'error': function(response) {
					DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"), response && response.description);
				}
			});
		},
		"onDestroy": function() {
			DMJS.stopThread("UdrawSingleConvas");
		},
		drawSingleConvas: function(x, y, h) { //起始x坐标y坐标和函数定点高
			var canvas = document.getElementById('ucanvas');
			if(!canvas) {
				return false;
			}
			context = canvas.getContext('2d');
			context.clearRect(0, 0, 800, 200);
			context.beginPath();
			context.fillStyle = "rgba(250,200,200,0.4)";
			context.moveTo(x, y);
			var bodyWidth = $("body").width()
			var j = 0;
			for(var i = 0;
				(x + j * 60 - 240) < bodyWidth; i++) {
				context.bezierCurveTo(x + j * 60, y, x + (j + 1) * 60, y - h, x + (j + 2) * 60, y);
				j += 2;
				context.quadraticCurveTo(x + (j + 1) * 60, y + h, x + (j + 2) * 60, y);
				j += 2;
			}
			/*context.bezierCurveTo(x, y,x+60, y-h, x+120, y);
			context.quadraticCurveTo(x+180, y+h, x+240, y);
			context.bezierCurveTo(x+240, y,x+300, y-h, x+360, y);
			context.quadraticCurveTo(x+420, y+h, x+480, y);
			*/

			context.lineTo(bodyWidth, y + 200);
			context.lineTo(-40, y + 200);
			context.closePath();
			context.fill();

			context.beginPath();
			context.fillStyle = "rgba(250,150,100,0.4)";

			var newX = x - 60;
			context.moveTo(newX, y);
			var j = 0;
			for(var i = 0;
				(newX + j * 60 - 240) < bodyWidth; i++) {
				context.bezierCurveTo(newX + j * 60, y, newX + (j + 1) * 60, y - h, newX + (j + 2) * 60, y);
				j += 2;
				context.quadraticCurveTo(newX + (j + 1) * 60, y + h, newX + (j + 2) * 60, y);
				j += 2;
			}
			context.lineTo(bodyWidth + 240, y + 200);
			context.lineTo(-40, y + 200);
			context.closePath();
			context.fill();
		}
	});
	return userInfoView;
});