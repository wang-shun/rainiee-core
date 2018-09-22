define(['text!indexTemplate/index/creditorBidbuy.html', 'userModel/UserModel',
		'commonTool/tool', 'commonTool/validator'
		], function(creditorBuyTemplate, UserModel, tool, Validator) {
	var creditorBuyView = DMJS.DMJSView.extend({ //债权投资
		id: 'creditorBuyContent',
		name: 'creditorBuyContent',
		tagName: 'div',
		className: "creditorBuyContent",
		events: {
			'tap #creditorBuy': 'creditorBuy'
		},
		init: function(options) {
			var self = this;
			_.extend(self, options);
			self.userModel = new UserModel();
			self.fmoney = tool.fmoney;
		},
		render: function() {
			var self = this;
			if (!self.creditorDetailData) {
				window.listHash.length = window.listHash.length - 1;
				DMJS.router.navigate('index/index/creditorTransfer', true);
				return false;
			} else {
				self.salePrice = self.creditorDetailData.salePrice;
				
			}
			self.userModel.myAccount({
				"noCache": true,
				cancelLightbox: true,
				"success": function(response) {
					self.tg = response.data.tg;
					self.overAmount = response.data.overAmount;
					self.$el.html(_.template(creditorBuyTemplate, self)); // 将tpl中的内容写入到 this.el 元素中
					$("#yqsy").html((self.creditorDetailData.dsjeAmount*1-self.salePrice*1).toFixed(2));
					DMJS.agreementEnable('ZQ',"zqxy",function(){
						if($("#zqxy").hasClass("uhide")&&self.isShowFXTS=='false'){
							$("#agreementFlag").remove();
						}
						if($("#zqxy").hasClass("uhide")&&self.isShowFXTS=='true'){
							$("#autid").attr('title','请阅读风险提示函');
						}else if(!$("#zqxy").hasClass("uhide")&&self.isShowFXTS=='false'){
							$("#autid").attr('title','请阅读债权转让协议');
						}
						
					});
					self.loadScroller();

				},
				'error': function(response) {
					DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"), response && response.description);
				}
			});

			return this;
		},

		'creditorBuy': function() {//购买债权
			document.activeElement.blur();
			var self = this;
			var parms = $("#" + self.id).getFormValue();
				parms['creditorId']=self.crediId;
			   //双乾，打开以下语句注释
//				parms['type'] = "shuangqian";
			if (Validator.check($("#" + self.id))) {
				if (parseFloat(self.salePrice) > parseFloat(self.overAmount)) {
					wrapView.FlipPrompt.confirm({    //提示去充值
			               				title: "",
			               				content: '账户余额不足',
			               				FBntconfirm: "充值",
										FBntcancel: "取消",
										FBntConfirmColor: "pop_btn_orange",
							}, function() {DMJS.router.navigate("user/payment/recharge", true);}, function() {});
					return false;
				}
				self.controller.indexModel.creditorBuy({
						cancelLightbox: true,
						"data":parms,
						"success": function(_data) {
							if(_data.code=='000000')
							{
								if(_data.data.url){
									Native.openChildWebActivity(_data.data.url);
								}else{
									DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"), '购买成功!');
									DMJS.CommonTools.hash_clear();
									setTimeout(function(){
										DMJS.router.navigate("index/index/creditorTransfer", true);
									},800);
								}
							}
	
						},
						'error': function(response) {
							if(response.description.indexOf("交易密码")>=0){
							wrapView.FlipPrompt.confirm({    //提示去修改交易密码
               				title: "",
               				content: response.description,
               				FBntconfirm: "找回交易密码",
               				FBntcancel: "取消",
               				FBntConfirmColor: "pop_btn_orange",
               				autoCloseBg:"false",
               			}, function() {
               				DMJS.router.navigate("user/password/tranPwdCode", true);         				
               			}, function() {

               			});
						}else{
							DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"), response && response.description);
							self.$el.find("input[name='tranPwd']").val("");
						}
							
						}
					});
			}
			
			
			
		},
		"turnRememberRido": function(e) {
			document.activeElement.blur();
			if ($("#autid").is(":checked")) {
				$("#autid").removeAttr("checked");
			} else {
				$("#autid").attr("checked", true);
			}
		},
       
		agreement: function(type) {
			this.noDestroy = true;
			if(!!type){
				DMJS.router.navigate("user/personal/agreement/"+type, true);
			}
			
		},
		
	});

	return creditorBuyView;
});