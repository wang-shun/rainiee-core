define(['text!indexTemplate/mall/fillInOrder.html'],
		function(fillInOrderTemplate) {
	var fillInOrderView = DMJS.DMJSView.extend({
		id: 'fillInOrder',
		name: 'fillInOrder',
		tagName: 'div',
		className: "fillInOrder",
		events: {
		},
		init: function(options) {
            var self = this;
            _.extend(self, options);
        },
        render: function(){
        	var self = this;
        	self.loadData();
        },
        loadData:function(){
        	var self = this;
        	if(!DMJS.currentView.controller.orderAddress){
        		self.controller.indexModel.getDefaultAddress({
        		cancelLightbox: true,
						"success": function(_data) {
							self.data.defaultAddress = _data.data;
							if(_data.data.region){
								var reg = _data.data.region.split(",");
							    self.data.defaultAddress.address = reg[0]+reg[1]+reg[2]+self.data.defaultAddress.address;
							}
				           	self.$el.html(_.template(fillInOrderTemplate,self.data));
				           	self.loadScroller();
						},
						'error': function(response) {
										DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"),response.description);
						}
        		});
        	}else{
        		self.data.defaultAddress = DMJS.currentView.controller.orderAddress;
        		self.$el.html(_.template(fillInOrderTemplate,self.data));
        	}
        	
        },
        "gotoAddress":function(){
        	var self  = this;
        	if(self.data.defaultAddress.address){
        		DMJS.router.navigate("index/index/addressManager", true);
        	}else{
        		DMJS.router.navigate("index/index/addAddress", true);
        	}
        	
        },
		"onDestroy":function(){
			    var self = this;
//		        DMJS.currentView.controller.orderData = null;
				DMJS.currentView.controller.orderAddress = null;
		        self.data = null;
		},
		"submitData":function(){
			var self = this;
			if(self.data.defaultAddress.id==0){
				DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"),"您还未建立收货地址!");
				return false;
			}
			var content = "<div class='fn-s-15 ub ub-ac'><div>交易密码：</div><div><input placeholder='请输入交易密码' name='tranPwd'  id='tranPwd'  type='password' maxlength='8' class='uc-a02 ulev-14 hei-30 bd-b'></div></div>"+
			"<div id='tips' class='tx-al-l-zy ulev-app2 fn-c-org '></div>";
			self.controller.indexModel.myAccount({
						"noCache": true,
		                cancelLightbox: true,
						"success": function(_data) {
							if(_data.code == "000000"){
								if("money"==self.data.payType&&_data.data.overAmount < self.data.totalValue){
									DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"),"余额不足");
									return false;
								}else if("score"==self.data.payType&&_data.data.usableScore < self.data.totalValue){
										DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"),"积分不足");
										return false;
								}
								wrapView.FlipPrompt.confirm({
								title: "提示",
								content: content,
								FBntconfirm: "确定",
								FBntcancel: "取消",
								FBntConfirmColor: "pop_btn_orange",
								autoCloseBg:"false",
								}, function() {
								var tranPwd = $('#tranPwd').val();
								if(!tranPwd){
									$('#tips').text('请交易密码价格');
									return false;
								}else{
									$('#tips').text('');
								}
								var parms = {};
								parms.payType=self.data.payType;
								var ary = [];
								for(var item in self.data.scoreMall){
									ary.push({goodsId:self.data.scoreMall[item].id,goodsNum:self.data.scoreMall[item].selectedNum,id:self.data.scoreMall[item].carId});
								}
								JSON.stringify(ary);
//								parms.goodsList="[{goodsId:"+id+",goodsNum:1}]";
								parms.goodsList=JSON.stringify(ary);
								parms.addressId=self.data.defaultAddress.id;
								parms.tranPwd=tranPwd;
								
								self.controller.indexModel.buyGoods({
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
											DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"),response.description);
											self.$el.find("input[name='tranPwd']").val("");
										}
									}
								});
									
								 }, function() {});
							}else{
								DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"),_data.description);
							}
						},
			});
			
		}
       
    });
	return fillInOrderView;
});