define(['indexModel/IndexModel',
	'commonController/MainController',
	'commonTool/tool'
], function(IndexModel, MainController, tool) {
	var IndexController = MainController.extend({
		module: 'index',
		name: 'index',
		tabFlag: 0, //第几个tab页
		actions: {
			'index': 'index',
			'projectInvestment': 'projectInvestment',
			'projectInvDetail/:bidid(/:date)': 'projectInvDetail',
			'bidbuy(/:bidid)(/:st)': 'bidbuy',
			'creditorTransfer': 'creditorTransfer', //债权投资列表
			'creditorDetail/:bidId/:id(/:creId)': 'creditorDetail', //债权投资详情
			'creditorBidbuy/:bidId/:creId(/:st)': 'creditorBidbuy', //债权投资
			'donationBenefit': 'donationBenefit', //公益捐赠
			'donationDetail/:id/:status(/:isTimeEnd)': 'donationDetail', //捐助详情
			'donation/:id/:id/:minAmount': 'donation', //捐助
			'donationLog/:id': 'donationLog', //捐助记录
			'operationData':'operationData',//运营数据
			'information': 'information', //资讯
			'informationArticle/:id/:type(/:date)': 'informationArticle', //资讯详情
			'fiduciary':'fiduciary',//信用贷
			'cashList': 'cashList', //土豪榜
			'loanPerson':'loanPerson',//个人借款意向
			'loanCompany':'loanCompany',//企业借款意向
			'loanApplication':'loanApplication',//借款申请
			'myPromotionAward/:type':'myPromotionAward',//我的推广
			'intergalMall':'intergalMall',//积分商城
			'shopCart':'shopCart',//购物车
			'goodsDetail/:goodsId':'goodsDetail',//商品详情
			'purchaseHistory/:goodsId':'purchaseHistory',//购买记录
			'fillInOrder':'fillInOrder',//填写订单
			'addressManager(/:isUser)':'addressManager',//地址管理
			'addAddress':'addAddress',//增加地址+修改地址
			'addAddress/:addressId':'addAddress',//增加地址+修改地址
			'financialCalculator':'financialCalculator',//理财计算器
			'calculation':'calculation',//计算结果
			'appDownload':'appDownload',//计算结果
		},
		init: function() {
			this.indexModel = new IndexModel();
			//this.userModel = new UserModel();
		},
		'biddata': null, //标和债权数据
		'dataFlag':null,//债权flag
		/**
		 * 进入首页
		 */

		'index': function() {
			var self = this;
			self.setHeader({
				//"title":Config.base("appName"),
				'title': " ",
				"right_right": {
					html: '我的',
					func: function() {
						DMJS.router.navigate("user/personal/userInfo", true);
					}
				},
				"right_left": {
					html: DMJS.userInfo ? "" : '登录 / 注册',
					func: function() {
						DMJS.router.navigate("user/personal/login", true);
					}
				},
				"left": {
					"html": "首页logo",
					"func": function() {
						DMJS.router.navigate("index/index/index", true);
					}
				}

			});
			self.setFoot({
				"key": "none"
			});
			require(['indexView/index/index'], function(indexView) {
				self.setContent(indexView, {
					"controller": self
				});
				if (DMJS.currentView.fromCache != 'Y') {
					DMJS.currentView.render();
				}
			});
		},
		'projectInvestment': function() { //投资列表
			var self = this;
			self.setHeader({

				'title': "投资列表",
				"left": {
					"html": "返回",
					"func": function() {
						tool._Navi_default("index/index/index");
					}
				},
				"right_right": {
					html: '计算器',
					func: function() {
						DMJS.router.navigate("index/index/financialCalculator", true);
					}
				}
			

			});
			self.setFoot({
				"key": "none"
			});
			require(['indexView/index/projectInvestmentView'], function(projectInvestmentView) {
				self.setContent(projectInvestmentView, {
					"controller": self
				}).render();
			});

		},
		cashList: function() {
			var self = this;
			self.setHeader({
				'title': "土豪榜",
				"left": {
					"html": "返回",
					"func": function() {
						tool._Navi_default("index/index/index");
					}
				},
			});
			self.setFoot({
				"key": "none",
			});
			require(['indexView/discover/cashList'], function(cashList) {
				self.setContent(cashList, {
					"controller": self,
				}).render();
			});
		},
		'projectInvDetail': function(bidid,date) { //投资详情
			var self = this;
			self.indexModel.bidDetail({
				cancelLightbox: true,
				data: {
					"bidId": bidid,
				},
				cancelLightbox: true,
				"success": function(_data) {

					var data = _data.data;
					self.biddata = data;
					self.setHeader({
						'title': "项目详情",
						"left": {
							"html": "返回",
							"func": function() {
								tool._Navi_default("index/index/projectInvestment");
							}
						},

					});
					var bottom_text = undefined,
						bottom_func = undefined;
					if(_data.data.allowTerminal=="PC"){
						bottom_text = "此项目仅供PC端投资";
					}
					else if (data.status == "TBZ") {
						bottom_text = "马上投资";
						bottom_func = function() {
							if(data.isOpenRisk&&data.isinvestLimit){
								if(!data.isRiskMatch){
									DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"), "项目风险级别已超出您的风险承受等级，建议您重新进行风险承受能力评估，再决定是否继续投资！");
								}else if(DMJS.userInfo&&DMJS.userInfo.isXS==false&&data.isXsb=="true"){
									DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"), "感谢您的支持！此标为新手标，只有未成功投资过并且没有购买过债权的用户才可以投标。");
						        }else{
									DMJS.router.navigate('index/index/bidbuy/' + bidid+"/"+_data.data.isShowFXTS, true);
								}
							}else if(DMJS.userInfo&&DMJS.userInfo.isXS==false&&data.isXsb=="true"){
								DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"), "感谢您的支持！此标为新手标，只有未成功投资过并且没有购买过债权的用户才可以投标。");
					        }else{
								DMJS.router.navigate('index/index/bidbuy/' + bidid+"/"+_data.data.isShowFXTS, true);
							}
							
						}
						
					} else if (data.status == "YFB") {
						bottom_text = "即将发布，敬请期待";
					} 
					
					else {
						bottom_text = null;
						bottom_func = undefined;
					}
					self.setFoot({
						"key": bottom_text ? "oneButtonFoot" : "none",
						"func": bottom_func ? {
							"button": bottom_func,

						} : undefined,
						
						"btnName": bottom_text ? {
							"text": bottom_text
						} : undefined
					});
					data.publicDate=_data.data.publicDate.replace(/-/g,'/').replace(/\.0/g,'');
					require(['indexView/index/projectInvDetailView'], function(projectInvDetailView) {
						self.setContent(projectInvDetailView, {
							"controller": self,
							'bidid': bidid,
							'dataL': data,
							'isShowFXTS':_data.data.isShowFXTS,
						}).render();
					});
				},
				error:function(response){
					DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"), response.description,function(){
						DMJS.CommonTools.hash_clear();
					});
				}
			});
		},
		'bidbuy': function(bidid,stauts) { //购买标
			var self = this;

			self.setHeader({

				'title': "投资",
				"left": {
					"html": "返回",
					"func": function() {
						tool._Navi_default("index/index/projectInvDetail" + bidid);
					}
				},

			});
			self.setFoot({
				"key": "none"
			});
			require(['indexView/index/bidbuy'], function(bidbuy) {
				self.setContent(bidbuy, {
					"controller": self,
					'bidDetailData': self.biddata,
					'bidid': bidid,
					'isShowFXTS':stauts,
				});
				if (DMJS.currentView.fromCache != 'Y') {
					DMJS.currentView.render();
				}
			});
		},
		//债权转让列表
		'creditorTransfer': function() {
			var self = this;
			self.setHeader({
				"left": {
					"html": "返回",
					"func": function() {
						tool._Navi_default('index/index/index');
					}
				},
				"title": "债权转让"
			});
			self.setFoot({
				"key": "none"
			});
			require(['indexView/index/creditorTransfer'], function(creditorTransfer) {
				self.setContent(creditorTransfer, {
					"controller": self,

				}).render();
			});
		},
		//债权转让详情
		'creditorDetail': function(bidId,id, creId) {
			var self = this,bottom_text,bottom_func;
			if(DMJS.currentView&&DMJS.currentView.id=='creditorTransferView'){
				self.dataFlag=DMJS.currentView.dataFlag;
			}
			self.indexModel.creditorDetail({
				cancelLightbox: true,
				data: {
					"bidId": bidId,
					"creditorId": id,
					'creId':creId
				},
				cancelLightbox: true,
				"success": function(_data) {
					
					var data=_data.data;
					self.biddata = data;
					if(parseInt(data.creditorVal)===0){
						bottom_text='已结束';
						bottom_func=undefined;
					}else if(data.status!='ZRZ'){
						bottom_text='已转让';
						bottom_func=undefined;
					}
					else{
						bottom_text='马上购买'
						bottom_func=function(){
							if(data.isOpenRisk&&data.isinvestLimit){
								if(!data.isRiskMatch){
									DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"), "项目风险级别已超出您的风险承受等级，建议您重新进行风险承受能力评估，再决定是否继续投资！");	
								}
								else{
									DMJS.router.navigate('index/index/creditorBidbuy/' + bidId + '/' + id+"/"+_data.data.isShowFXTS, true);
								}
							}
							else{
								DMJS.router.navigate('index/index/creditorBidbuy/' + bidId + '/' + id+"/"+_data.data.isShowFXTS, true);
							}
							
							
							
						}
					}
					self.setHeader({
						"left": {
							"html": "返回",
							"func": function() {
								tool._Navi_default('index/index/creditorTransfer');
							}
						},
						"title": "债权详情"
					});
					
					self.setFoot({
						'btnName': {
							text: bottom_text
						}, //text 是按钮内容
						"key": "oneButtonFoot",
						"func":  bottom_func ? {
							"button": bottom_func,

						} : undefined,

					});
					require(['indexView/index/creditorDetail'], function(creditorDetail) {
						self.setContent(creditorDetail, {
							"controller": self,
							'crediId': id,
							'creId':creId,
							'bidId': bidId,
							'dataFlag': self.dataFlag,
							'dataL':data,
							'isShowFXTS':_data.data.isShowFXTS,
							
						}).render();
					});


				}

			})

		},
		//债权投资
		'creditorBidbuy': function(bidId, crediId,stauts) {
			var self = this;
			self.setHeader({
				"left": {
					"html": "返回",
					"func": function() {
						tool._Navi_default('index/index/creditorDetail/' + bidId + '/' + crediId);
					}
				},
				"title": "债权购买"
			});
			self.setFoot({
				"key": "none"
			});
			require(['indexView/index/creditorBidbuy'], function(creditorBidbuy) {
				self.setContent(creditorBidbuy, {
					"controller": self,
					'creditorDetailData': self.biddata,
					'crediId': crediId,
					'bidId': bidId,
					'isShowFXTS':stauts,
				}).render();
			});
		},

		donationBenefit: function() {

			var self = this;
			self.setHeader({
				'title': "公益捐赠",
				"left": {
					"html": "返回",
					"func": function() {
						tool._Navi_default("index/index/index");
					}
				},
			});
			self.setFoot({
				"key": "none"
			});
			require(['indexView/donation/donationBenefit'], function(donationBenefit) {
				self.setContent(donationBenefit, {
					"controller": self
				}).render();
			});

		},
		donationDetail: function(donationId,status,isTimeEnd) {
			var self = this;
			self.setHeader({
				'title': "捐助详情",
				"left": {
					"html": "返回",
					"func": function() {
						tool._Navi_default("index/index/donationBenefit");
					}
				},
			});
			var text= (status=="JKZ"&&isTimeEnd!="true") ? '捐助' : '捐助完成';//'oneButtonFoot' :"none";
			var func = undefined;
			if(status=="JKZ"&&isTimeEnd!="true"){
				 func = function() { //点击触发的事件
						DMJS.router.navigate('index/index/donation/' + donationId + "/" + DMJS.currentView.data.remaindAmount+"/"+DMJS.currentView.data.minAmount, true);
				  }
			}
			self.setFoot({
				'btnName': {
					text: text
				}, //text 是按钮内容
				"key": 'oneButtonFoot',
				"func": func ? {
					button: func
				}:undefined,
			});
			require(['indexView/donation/donationDetail'], function(donationDetail) {
				self.setContent(donationDetail, {
					"controller": self,
					data: {
						donationId: donationId
					}
				}).render();
			});

		},
		donation: function(id, donationsAmount,minAmount) {
			var self = this;
			self.setHeader({
				'title': "捐助",
				"left": {
					"html": "返回",
					"func": function() {
						tool._Navi_default("index/index/donationDetail/" + id);
					}
				},
			});
			self.setFoot({
				"key": "none",
			});
			require(['indexView/donation/donation'], function(donation) {
				self.setContent(donation, {
					"controller": self,
					data: {
						donationId: id,
						donationsAmount: donationsAmount*1,
						donationsMinAmount:minAmount*1
					}
				});
				if (DMJS.currentView.fromCache != 'Y') {DMJS.currentView.render();}
			});
		},
		donationLog: function(donationId) {
			var self = this;
			self.setHeader({
				'title': "捐助记录",
				"left": {
					"html": "返回",
					"func": function() {
						tool._Navi_default("index/index/donationDetail/" + donationId);
					}
				},
			});
			self.setFoot({
				"key": "none",
			});
			require(['indexView/donation/donationLog'], function(donationLog) {
				self.setContent(donationLog, {
					"controller": self,
					data: {
						donationId: donationId
					}
				}).render();
			});

		},
		operationData: function() {
			var self = this;
			self.setHeader({
				'title': "运营数据",
				"left": {
					"html": "返回",
					"func": function() {
						tool._Navi_default("index/index/index");
					}
				},
			});
			self.setFoot({
				"key": "none",
			});
			require(['indexView/discover/operationData'], function(operationData) {
				self.setContent(operationData, {
					"controller": self,
				}).render();
			});

		},
		information: function() {
			var self = this;
			self.setHeader({
				'title': "资讯",
				"left": {
					"html": "返回",
					"func": function() {
						tool._Navi_default("index/discover/discover");
					}
				},
			});
			self.setFoot({
				"key": "none",
			});
			require(['indexView/discover/information'], function(information) {
				self.setContent(information, {
					"controller": self,
				});
				if (DMJS.currentView.fromCache != 'Y') {
					DMJS.currentView.render();
				}
			});

		},
		informationArticle: function(id, type,date) {
			var self = this;
			var title="";
			switch(type){
				case 'MTBD': title="媒体报道"; break;
				case 'WDHYZX': title="行业资讯"; break;
				case 'WZGG': title="公告详情"; break;
				default:title="公告详情";
			}
			
			self.setHeader({
				'title': title,
				"left": {
					"html": "返回",
					"func": function() {
						tool._Navi_default("index/index/information");
					}
				},
			});
			self.setFoot({
				"key": "none",
			});
			require(['indexView/discover/informationArticle'], function(informationArticle) {
				self.setContent(informationArticle, {
					"controller": self,
					data: {
						articleId: id,
						type: type,
						'dateS':date
					}
				}).render();
			});

		},
		fiduciary:function(){
			var self = this;
			self.setHeader({
				'title': "信用贷",
				"left": {
					"html": "返回",
					"func": function() {
						tool._Navi_default("index/index/index");
					}
				},
			});
			self.setFoot({
				"key": "none",
			});
			require(['indexView/loan/fiduciary'], function(fiduciary) {
				self.setContent(fiduciary, {
					"controller": self,
				}).render();
			});
		
		},
		loanPerson:function(){
			var self = this;
			self.setHeader({
				'title': "个人借款意向",
				"left": {
					"html": "返回",
					"func": function() {
						tool._Navi_default("index/index/index");
					}
				},
			});
			self.setFoot({
				"key": "none",
			});
			require(['indexView/loan/loanPerson'], function(loanPerson) {
				self.setContent(loanPerson, {
					"controller": self,
				});
				if (DMJS.currentView.fromCache != 'Y') {
					DMJS.currentView.render();
				}
			});
		},
		loanCompany:function(){
			var self = this;
			self.setHeader({
				'title': "企业借款意向",
				"left": {
					"html": "返回",
					"func": function() {
						tool._Navi_default("index/index/index");
					}
				},
			});
			self.setFoot({
				"key": "none",
			});
			require(['indexView/loan/loanCompany'], function(loanCompany) {
				self.setContent(loanCompany, {
					"controller": self,
				});
				if (DMJS.currentView.fromCache != 'Y') {
					DMJS.currentView.render();
				}
			});
		},
		loanApplication:function(){
			var self = this;
			self.setHeader({
				'title': "借款申请",
				"left": {
					"html": "返回",
					"func": function() {
						tool._Navi_default("index/index/fiduciary");
					}
				},
			});
			self.setFoot({
				"key": "none",
			});
			require(['indexView/loan/loanApplication'], function(loanApplication) {
				self.setContent(loanApplication, {
					"controller": self,
				});
				if (DMJS.currentView.fromCache != 'Y') {
					DMJS.currentView.render();
				}
			});
		},
		/*积分商城*/
		intergalMall:function(){
			var self = this;
			self.indexModel.scoreMallList({
				cancelLightbox: true,
						data: {
//							"pageSize": 10,
//							"pageIndex": pageInfo.pageIndex
						},
						cancelLightbox: true,
						"success": function(_data) {
							console.log("suc")
							self.setHeader({
							'title': "积分商城",
							"left": {
								"html": "返回",
								"func": function() {
//										DMJS.router.navigate("index/index/discover",true);
										tool._Navi_default("index/index/discover",true);
									}
								},
							});
							self.setFoot({
								"key": "none",
							});
							require(['indexView/mall/intergalMall'], function(mall) {
								self.setContent(mall, {
									"controller": self,
									"data":_data,
								});
								if (DMJS.currentView.fromCache != 'Y') {
									DMJS.currentView.render();
								}
							});
						}
			});
		},
		/*商品详情*/
		goodsDetail:function(goodsId){
			var self = this;
			self.setHeader({
				'title': "商品详情",
				"left": {
					"html": "返回",
					"func": function() {
//						DMJS.router.navigate("index/index/intergalMall",true);
						tool._Navi_default("index/index/intergalMall",true);
					}
				},
				"right_right":{//购物车订单入口
					"html": "购物车",
					"func": function(){
						DMJS.router.navigate("index/index/shopCart", true);
					}
				}
			});
			self.setFoot({
				"key": "mallFoot",
				"func":{
					"exchange": function(){
						 DMJS.currentView.goodsExchange();
					},
					"buyImmediately":function(){
						DMJS.currentView.goodsBuyImmediately();
					},
					"addShoppingCart":function(){
						DMJS.currentView.goodsAddShoppingCart();
					},
				} ,

			});
			require(['indexView/mall/goodsDetailView'], function(goodsDetail) {
				self.setContent(goodsDetail, {
					"controller": self,
					"goodsId":goodsId,
				}).render();
			});
		},
		/*购物车*/
		shopCart: function(){
			var self = this;
			self.indexModel.shoppingCart({ //购物车列表
						data: {
							"pageSize": 10,
							"pageIndex": 1
						},
						cancelLightbox: true,
						"success": function(_data) {
						var footKey="shopCart";
						var allowsBalance = _data.data.allowsBalance;
						var datalist = _data.data.scoreMallList;
						if(datalist.length < 1){
							footKey="none";
						}
			
			self.setHeader({
				'title': "购物车",
				"left": {
					"html": "返回",
					"func": function() {
						tool._Navi_default("index/index/goodsDetail");//返回我的购物车
					}
				}
			});
			self.setFoot({
				"key": footKey,
				"func":{
					"allSelect":function(){
						var $allSelect;
						var data = DMJS.currentView.data;
						if($("#allSelect").hasClass('not_selected')){
				   			$allSelect = $(".not_selected");
				   			$allSelect.removeClass('not_selected');
				   		    $allSelect.addClass('selected');
				   		    if(allowsBalance){
				   		    	$("#totalDiv").removeClass("b_image-dragY");
	    			   		    $("#totalDiv").addClass("b_image-dragN");
				   		    	$("#amountMoney").html("¥"+data.allAmountMoney1);
				   		    }else{
				   		    	$("#amountMoney").html(data.allScore1+"积分");
				   		    }
			   		    	DMJS.currentView.data.goodsSum = DMJS.currentView.data.allNumber;
			   		    	DMJS.currentView.data.allAmountMoney = DMJS.currentView.data.allAmountMoney1;
			   		    	 DMJS.currentView.data.allScore = DMJS.currentView.data.allScore1;
				   		    $("#buyAmount").html(DMJS.currentView.data.allNumber);//购买总件数
    			   		}else{
    			   			$allSelect = $(".selected");
    			   			$allSelect.removeClass('selected');
    			   		    $allSelect.addClass('not_selected');
    			   		    DMJS.currentView.data.goodsSum = 0;
    			   		    DMJS.currentView.data.allAmountMoney = 0;
    			   		    DMJS.currentView.data.allScore = 0;
    			   		    $("#buyAmount").html(0);
    			   		    if(allowsBalance){
	    			   		    $("#totalDiv").removeClass("b_image-dragY");
	    			   		    $("#totalDiv").addClass("b_image-dragN");
	    			   		    $("#amountMoney").html("¥0.00");
    			   		    }else{
    			   		    	$("#amountMoney").html("0积分");
    			   		    }
    			   		}
					},
					"delSelect":function(){
						DMJS.currentView.delShoppingCar();
					},
					"settlAccounts":function(){
						DMJS.currentView.toCompute();
					},
				}
			});
			require(['indexView/mall/shopCart'], function(shopCart) {
				self.setContent(shopCart, {
					"controller": self,
				}).render();
			});
			},
			});
		},
		/*购买记录*/
		purchaseHistory:function(goodsId){
			var self = this;
			self.setHeader({
				'title': "购买记录",
				"left": {
					"html": "返回",
					"func": function() {
						tool._Navi_default("index/index/goodsDetail/"+DMJS.currentView.goodsId);
					}
				},
			});
			self.setFoot({
				"key": "none",
			});
			require(['indexView/mall/purchaseHistory'], function(purchaseHistory) {
				self.setContent(purchaseHistory, {
					"controller": self,
					"goodsId":goodsId,
				}).render();
			});
		},
		/*填写订单*/
		fillInOrder:function(){
			var self = this;
			if(!self.orderData){
				tool._Navi_default("index/index/intergalMall");
				return false;
			}
			self.setHeader({
				'title': "填写订单",
				"left": {
					"html": "返回",
					"func": function() {
						tool._Navi_default("index/index/intergalMall");
					}
				},
			});
			var bottom_text = self.orderData.payType=="score"?"确认兑换":"立刻购买";
			
			self.setFoot({
				'btnName': {
							text: bottom_text
						}, //text 是按钮内容
				"key": "orderFoot",
				"func":{
					"confirm": function(){
						DMJS.currentView.submitData();
					},
				} ,
			});
			require(['indexView/mall/fillInOrderView'], function(fillInOrderView) {
				self.setContent(fillInOrderView, {
					"controller": self,
					"data":self.orderData,
				}).render();
			});
		},
		myPromotionAward:function(typeS){
			var self = this,title,right_right;
			if(typeS==='1'){
				title='推广有奖';
				right_right={
      	       			"html":"推广",
      	       			"func":function(){
      	       				DMJS.router.navigate("index/index/myPromotionAward/2", true);
      	       				
      	       			}
      	       	};
			}else{
				title='我的推广';
				right_right=undefined;
			}
			self.setHeader({
				'title':title,
				"left": {
					"html": "返回",
					"func": function() {
						tool._Navi_default("index/index/discover");
					}
				},
				"right_right":right_right
			});
			self.setFoot({
				"key": "none",
			});
			require(['indexView/discover/myPromotionAward'], function(promotionAward) {
				self.setContent(promotionAward, { 
					"controller": self,
					'typeS':typeS
				}).render();
			});
		},
		/*选择地址*/
		addressManager:function(isUser){
			var self = this;
			title='选择地址';
			if(isUser){
				title='收货地址管理';
			}
			self.setHeader({
				'title':title,
				"left": {
					"html": "返回",
					"func": function() {
						tool._Navi_default("user/personal/userInfo");
					}
				},
			});
			self.setFoot({
				'btnName': {
							text: "新增地址",
						}, //text 是按钮内容
				"key": "oneButtonFoot",
				"func":{
					"button": function(){
						DMJS.router.navigate("index/index/addAddress", true);
					},
				} ,
			});
			require(['indexView/mall/addressManager'], function(addressManager) {
				self.setContent(addressManager, { 
					"controller": self,
				}).render();
			});
		},
		/*增加修改地址*/
		addAddress:function(id){
			var self = this;
			var title = '新增地址';
			if(id&&id!=""){
				title = '编辑地址';
			}
			self.setHeader({
				'title':title,
				"left": {
					"html": "返回",
					"func": function() {
						tool._Navi_default("index/index/addressManager");
					}
				},
			});
			self.setFoot({
				"key": "none",
			});
			require(['indexView/mall/addAddress'], function(addAddress) {
				self.setContent(addAddress, { 
					"controller": self,
					'addressId':id
				}).render();
			});
		},
		/*理财计算器*/
		financialCalculator:function(){
			var self = this;
			self.setHeader({
				'title':'理财计算器',
				"left": {
					"html": "返回",
					"func": function() {
						tool._Navi_default("index/index/discover");
					}
				},
			});
			self.setFoot({
				"key": "none",
			});
			require(['indexView/discover/financialCalculator'], function(financialCalculator) {
				self.setContent(financialCalculator, { 
					"controller": self,
					"data":self.data,
				}).render();
			});
		},
		/*理财计算器*/
		calculation:function(){
			var self = this;
			self.setHeader({
				'title':'计算结果',
				"left": {
					"html": "返回",
					"func": function() {
						tool._Navi_default("index/index/financialCalculator");
					}
				},
			});
			
			self.setFoot({
				'btnName': {
					text: '重新计算'
				}, //text 是按钮内容
				"key": "oneButtonFoot",
				"func": {
					"button": function(){
						window.listHash.splice(window.listHash.length-1,window.listHash.length);
						DMJS.router.navigate("index/index/financialCalculator", true);
					},
	
				},
	
			});
			if(!DMJS.currentView){
				DMJS.router.navigate("index/index/financialCalculator", true);
				return false;
			}
			require(['indexView/discover/calculation'], function(calculation) {
				self.setContent(calculation, { 
					"controller": self,
					'data':DMJS.currentView.data,
				}).render();
			});
		},
		//APP下载页面
		appDownload:function(){
			var self = this;
			self.setHeader({
				'title':'APP下载',
				"left": {
					"html": "返回",
					"func": function() {
						tool._Navi_default("index/index/index");
					}
				},
			});
			self.setFoot({
				"key": "none",
			});
			require(['indexView/index/appDownload'], function(appDownload) {
				self.setContent(appDownload, { 
					"controller": self,
				}).render();
			});
		},
		
	});
	return IndexController;
});