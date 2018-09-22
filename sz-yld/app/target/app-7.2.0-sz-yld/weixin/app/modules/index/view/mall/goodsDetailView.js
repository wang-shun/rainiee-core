define(['text!indexTemplate/mall/goodsDetail.html',
        'text!indexTemplate/mall/phoneBill.html',
        'commonTool/tool'],
		function(goodsDetail,phoneBill,tool) {
	var goodsDetailView = DMJS.DMJSView.extend({ //项目详情
		id: 'goodsDetail',
		name: 'goodsDetail',
		tagName: 'div',
		className: "goodsDetail",
		events: {
			'tap #purchaseHistory': 'purchaseHistory'
		},
		init: function(options) {
			var self = this;
			_.extend(self, options);
			
		},
		render: function() {
			var self = this;
			
			self.controller.indexModel.commodityDetails({
				cancelLightbox: true,
				data: {
					id:self.goodsId
				},
				cancelLightbox: true,
				"success": function(_data) {
					self.data = _data.data;
					self.$el.html(_.template(goodsDetail, self.data)); // 将tpl中的内容写入到 this.el 元素中
					if("virtual"==self.data.scoreMall.commTypeEnum){
						$('#shoppingCart').addClass("uhide");
						$('#imgShoppingCart').addClass("uhide");
					}
					if(!self.data.allowsBalance||"no"==self.data.scoreMall.isCanMoney){
						$('#buyImmediately').addClass("uhide");
					}else if("no"==self.data.scoreMall.isCanScore){
						$('#exchange').addClass("uhide");
					}
					self.loadScroller();
				},
			});
			
			setTimeout(function(){
				self.showDetail();
			},500);
			return this;
		},
		/*跳转到购买记录*/
		"purchaseHistory":function(){
			var self = this;
			DMJS.router.navigate("index/index/purchaseHistory/"+self.goodsId, true);
		},
		"showDetail":function(){
			var self = this;
			self.controller.indexModel.showDetail({
				cancelLightbox: true,
				data: {
					"id":self.goodsId,
					"type":"spxq",
				},
				cancelLightbox: true,
				"success": function(_data) {
					$("#showDetail").html(_data.data.commodityItem);
					self.loadScroller();
				},
			});
		},
		"goodsExchange":function(){
			              var data = DMJS.currentView.data;
	    			      if(!DMJS.userInfo){
	    			   	  	  DMJS.router.navigate("user/personal/login", true);
	    			   	  }
	    			      if(data.scoreMall.stock<1){
	    			      	 DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"), "商品库存不足");
	    			      	 return false;
	    			      }
	    			      //校验限购
	    			      if(data.scoreMall.purchase>0&&data.scoreMall.num>=data.scoreMall.purchase){
	    			      	 DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"), "本商品每人限购"+data.scoreMall.purchase+"件");
	    			      	 return false;
	    			      }
	    			      //校验积分
	    			      if(data.scoreMall.score>data.userScore){
	    			      	 DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"), "您的积分不足!");
	    			      	 return false;
	    			      }
	    			      data.buyImmediately = false;
	    			      if(data.scoreMall.commTypeEnum=="virtual"){
	    			      	         wrapView.FlipPrompt.confirm({    //提示去注册
			               				title: "",
			               				content: _.template(phoneBill, data),
			               				FBntconfirm: "确定",
			               				FBntcancel: "取消",
			               				FBntConfirmColor: "pop_btn_orange",
			               				autoCloseBg:"false",
			               			}, function() {
			               				var mobile = $('#mobile').val();
			               				var tranPwd = $('#tranPwd').val();
			               				var reg = /^[1][3|4|5|7|8][0-9]{9}$/;
			               				if(!mobile){
			               					$('#errorMsg').text('手机号码不能为空');
			               					return false;
			               				}
			               				if(!reg.test(mobile)){
			               					$('#errorMsg').text('输入手机号码不符合规范');
			               					return false;
			               				}
			               				if(!tranPwd){
			               					$('#errorMsg').text('交易密码不能为空');
			               					return false;
			               				}
			               				
			               				var parms = {};
										parms.payType='score';
										parms.goodsList="[{goodsId:"+data.scoreMall.id+",goodsNum:1,mobile:"+mobile+"}]";
										parms.tranPwd=tranPwd;
			               				
			               				DMJS.currentView.controller.indexModel.buyGoods({
											cancelLightbox: true,
											data: parms,
											cancelLightbox: true,
											"success": function(_data) {
												if(_data.code==='000000'){
													$('#FlipPrompt').hide();
													wrapView.lightBox.hide();
													DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"),_data.description);
													setTimeout(function(){
														DMJS.router.navigate("user/personal/myOrder", true);
													},1000);
												}
												
											},
											'error': function(response) {
												DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"),response.description);
											}
										});
			               			}, function() {
			               			},'noneTitle');
	    			      	         
	    			      }else{
	    			      	  var orederData = {};
		    			      orederData.scoreMall=[];
		    			      orederData.goodsSum = 1;
		    			      orederData.payType = "score";
		    			      orederData.totalValue =data.scoreMall.score;
		    			      data.scoreMall.selectedNum = 1;
		    			      orederData.scoreMall.push(data.scoreMall);
		    			      DMJS.currentView.controller.orderData = orederData;
		    			      DMJS.router.navigate("index/index/fillInOrder", true);
	    			      }
	    			      
	    			      
		 },
		 "goodsBuyImmediately":function(){
						  var data = DMJS.currentView.data;
	    			      if(!DMJS.userInfo){
	    			   	  	  DMJS.router.navigate("user/personal/login", true);
	    			   	  }
	    			      if(data.scoreMall.stock<1){
	    			      	 DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"), "商品库存不足");
	    			      	 return false;
	    			      }
	    			      //校验限购
	    			      if(data.scoreMall.purchase>0&&data.scoreMall.num>=data.scoreMall.purchase){
	    			      	 DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"), "本商品每人限购"+data.scoreMall.purchase+"件");
	    			      	 return false;
	    			      }
	    			      //校验金额
	    			      if(data.scoreMall.amount>data.userBalance){
	    			      	 wrapView.FlipPrompt.confirm({
	               				title: "",
	               				content: '您的余额不足!',
	               				FBntconfirm: "去充值",
	               				FBntcancel: "取消",
	               				FBntConfirmColor: "pop_btn_orange",
	               				autoCloseBg:"false",
	               			}, function() {
	               				DMJS.router.navigate("user/payment/recharge", true);       				
	               			}, function() {
	               			});
	    			      	 return false;
	    			      }
	    			      data.buyImmediately = true;
	    			      if(data.scoreMall.commTypeEnum=="virtual"){
	    			      	         wrapView.FlipPrompt.confirm({    //提示去注册
			               				title: "",
			               				content: _.template(phoneBill, data),
			               				FBntconfirm: "确定",
			               				FBntcancel: "取消",
			               				FBntConfirmColor: "pop_btn_orange",
			               				autoCloseBg:"false",
			               			}, function() {
			               				var mobile = $('#mobile').val();
			               				var tranPwd = $('#tranPwd').val();
			               				var reg = /^[1][3|4|5|7|8][0-9]{9}$/;
			               				if(!mobile){
			               					$('#errorMsg').text('手机号码不能为空');
			               					return false;
			               				}
			               				if(!reg.test(mobile)){
			               					$('#errorMsg').text('输入手机号码不符合规范');
			               					return false;
			               				}
			               				if(!tranPwd){
			               					$('#errorMsg').text('交易密码不能为空');
			               					return false;
			               				}
			               				
			               				var parms = {};
										parms.payType='balance';
										parms.goodsList="[{goodsId:"+data.scoreMall.id+",goodsNum:1,mobile:"+mobile+"}]";
										parms.tranPwd=tranPwd;
			               				
			               				DMJS.currentView.controller.indexModel.buyGoods({
											cancelLightbox: true,
											data: parms,
											cancelLightbox: true,
											"success": function(_data) {
												if(_data.code==='000000'){
													$('#FlipPrompt').hide();
													wrapView.lightBox.hide();
													DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"),_data.description);
													setTimeout(function(){
														DMJS.router.navigate("user/personal/myOrder", true);
													},1000);
												}
												
											},
											'error': function(response) {
												DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"),response.description);
											}
										});
			               			}, function() {
			               			},'noneTitle');
	    			      	         
	    			      }else{
		    			      var orederData = {};
		    			      orederData.scoreMall=[];
		    			      orederData.goodsSum = 1;
		    			      orederData.payType = "money";
		    			      data.scoreMall.selectedNum = 1;
		    			      orederData.scoreMall.push(data.scoreMall);
		    			      orederData.totalValue =data.scoreMall.amount;
		    			      DMJS.currentView.controller.orderData = orederData;
		    			      
		    			      DMJS.router.navigate("index/index/fillInOrder", true);
	    			      }
					},
		 "goodsAddShoppingCart":function(){
                       	  var id = DMJS.currentView.data.scoreMall.id;
                       	  DMJS.currentView.controller.indexModel.addShoppingCart({
								cancelLightbox: true,
								data: {
									"id":id,
									"num":1
								},
								cancelLightbox: true,
								"success": function(respone) {
									 $('#imgShoppingCart').removeClass('b_image-shopcart').addClass('b_image-checkedshopcart');
									 DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"), respone && respone.description);
									 setTimeout(function(){wrapView.FlipPrompt.colse();},2000);
								},
						  });
                        },
	});

	return goodsDetailView;
});