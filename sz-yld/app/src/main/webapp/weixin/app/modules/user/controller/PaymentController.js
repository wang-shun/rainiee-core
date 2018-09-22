define(['userModel/UserModel',
	'commonController/MainController',
	'commonTool/tool',
	'commonClass/ActionFilter'
], function(UserModel, MainController, tool, ActionFilter) {
	var PaymentController = MainController.extend({
		module: 'user',
		name: 'payment',
		actions: {
			'recharge': 'recharge', //充值
			'withdraw': 'withdraw', //提现
			'cardManage(/:refresh)': 'cardManage', //银行卡管理
			'creditorOver': 'creditorOver', //债权转让列表
			'addCard(/:l)(/:m)': 'addCard', //添加银行卡
			'cardInfo/:id/:id': 'cardInfo', //银行卡信息
			'openTgAccount(/:phoneNum)':'openTgAccount'//开通托管账户

		},
		init: function() {
			this.userModel = new UserModel();

		},

		//充值
		'recharge': function() {
			var self = this;
			self.setHeader({
				"left": {
					"html": "返回",
					"func": function() {
						tool._Navi_default('user/personal/userInfo');
					}
				},
				"title": "充值"
			});
			self.setFoot({
				"key": "none"
			});
			require(['userView/payment/recharge'], function(recharge) {
				self.setContent(recharge, {
					"controller": self,
				}).render();
			});
		},
		//提现
		'withdraw': function() {
			var self = this;
			self.userModel.myBankList({
					'data': {},'cancelLightbox': false,"noCache": true,
					"success": function(response)
					{
						 var ismax=response.data.myBankList.length>=(response.data.maxbanks)*1;
						self.setHeader({
							"left": {
								"html": "返回",
								"func": function() {
									tool._Navi_default('user/personal/userInfo');
								}
							},
							"title": "提现",
							"right_right": ismax ? undefined : {
								"html": "添加银行卡",
								"func": function() {
									ActionFilter(DMJS.currentView.id, 'addCard', function() {
										DMJS.currentView['addCard'].apply(DMJS.currentView);
									});
								}
							}
						});
						
						self.setFoot({
							"key": "none"
						});
						require(['userView/payment/withdraw'], function(withdraw) {
							self.setContent(withdraw, {
								"controller": self,
								 "data": {}
							}).render();
						});
						
					},
					'error': function(response) {DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"), response.description);}
					});
					
			
		},
		//债权转让列表
		'creditorOver': function() {
			var self = this;
			self.setHeader({
				"left": {
					"html": "返回",
					"func": function() {
						tool._Navi_default('user/personal/userInfo');
					}
				},
				"title": "债权转让"
			});
			self.setFoot({
				"key": "none"
			});
			require(['userView/payment/creditorOver'], function(creditorOver) {
				self.setContent(creditorOver, {
					"controller": self,
					"data": {

					}
				});
				if (DMJS.currentView && DMJS.currentView.fromCache != 'Y') {
					DMJS.currentView.render();
				}
			});
		},
		cardManage: function() {
			var self = this;
           	 self.userModel.myBankList({
					'data': {},'cancelLightbox': false,"noCache": true,
					"success": function(response)
					{
						 var ismax=response.data.myBankList.length>=(response.data.maxbanks)*1;
						self.setHeader({
							"left": {
								"html": "返回",
								"func": function() {
									tool._Navi_default('user/personal/userInfo');
								}
							},
							"title": "银行卡管理",
							"right_right": ismax ? undefined : {
								"html": "添加银行卡",
								"func": function() {
									ActionFilter(DMJS.currentView.id, 'addCard', function() {
										DMJS.currentView['addCard'].apply(DMJS.currentView);
									});
								}
							}
						});
						self.setFoot({
							"key": "none"
						});
						require(['userView/payment/cardManage'], function(cardManage) {
							self.setContent(cardManage, {
								"controller": self,
								 cardList:response.data
							}).render();
						});
					},
					'error': function(response) {DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"), response.description);}
					});


		},
		'addCard': function(l, m) {
			var self = this;
	/*		if (l >= m && m != 0) {
				DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"), '您绑定的银行卡数量已达最大值,不能再绑定其他银行卡!');
				return false;
			}
*/
			self.setHeader({
				"left": {
					"html": "返回",
					"func": function() {
						tool._Navi_default('user/personal/userInfo');
					}
				},
				"title": "添加银行卡"
			});
			self.setFoot({
				"key": "none"
			});
			require(['userView/payment/addCard'], function(addCard) {
				self.setContent(addCard, {
					"controller": self,
				}).render();
			});

		},
		cardInfo: function(bankNum, bankName) {
			var self = this;
			self.setHeader({
				"left": {
					"html": "返回",
					"func": function() {
						tool._Navi_default('user/personal/cardManage');
					}
				},
				"title": "银行卡信息"
			});
			self.setFoot({
				"key": "none"
			});
			require(['userView/payment/cardInfo'], function(cardInfo) {
				self.setContent(cardInfo, {
					"controller": self,
					"data": {
						bankNum: bankNum,
						bankName: bankName
					}
				}).render();
			});
		},
		openTgAccount:function(phoneNum) {
			var self = this;
			self.setHeader({
				"left": {
					"html": "返回",
					"func": function() {
						tool._Navi_default('user/personal/userInfo');
					}
				},
				"title": "开通托管账户"
			});
			self.setFoot({
				"key": "none"
			});
			require(['userView/payment/openTgAccount'], function(openTgAccount) {
				self.setContent(openTgAccount, {
					"controller": self,
					"data": {
						'phoneNum':phoneNum
					}
				}).render();
			});
		},

	});
	return PaymentController;
});
