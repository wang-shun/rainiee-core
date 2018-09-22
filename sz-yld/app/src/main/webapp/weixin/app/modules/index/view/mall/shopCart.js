define(['text!indexTemplate/mall/shopCart.html',
		'text!indexTemplate/mall/shopCartGoods.html',
		'commonClass/scroll/PTRScroll',
		'commonTool/tool',
	],
	function(shopCart,shopCartGoods,PTRScroll) {
		var shopCartView = DMJS.DMJSView.extend({ //购物车
			id: 'shopCart',
			name: 'shopCart',
			tagName: 'div',
			className: "shopCart",
			events: {
				'tap #totalDiv': 'chargeBtn',
				'tap #goodsList':'goodsDetail'
			},
//			data:{
//				goodsSum:0,//选中商品总数
//				totalValue:0,//商品总价值
//				selectedGoodsList:[],//商品信息集合
//				allNumber:0,//购买商品总数量
//				allGoods:{},//所以商品
//				allAmountMoney:0,//购买商品总金额
//				allScore:0,//购买商品总积分
//				allAmountMoney1:0,//全选使用购买商品总金额
//				allScore1:0,//全选使用购买商品总积分
//				allUseMoney:true,//购物车全部商品都能使用余额支付
//				allUseSocre:true,//购物车全部商品都能使用积分支付
//			},
			init: function(options) {
				var self = this;
				_.extend(this, options);
				self.data = {
				goodsSum:0,//选中商品总数
				totalValue:0,//商品总价值
				selectedGoodsList:[],//商品信息集合
				allNumber:0,//购买商品种类数量
				allGoods:{},//所以商品
				allAmountMoney:0,//购买商品总金额
				allScore:0,//购买商品总积分
				allAmountMoney1:0,//全选使用购买商品总金额
				allScore1:0,//全选使用购买商品总积分
				allUseMoney:true,//购物车全部商品都能使用余额支付
				allUseSocre:true,//购物车全部商品都能使用积分支付
			};
				self.pageInfo = {
					"1": {
						"pageIndex": 1,
						"loaded": false,
						"isOver": false,
						"id": 1
					}

				}
			},
			render: function() {
				var self = this;
				this.$el.html(_.template(shopCart, self.data)); // 将tpl中的内容写入到 this.el 元素中
				$("#userScore").html(DMJS.userInfo.usableScore);
				//初始商品列表
				setTimeout(function() {
					self.loadDates(self.pageInfo[1]);
				}, Config.base("lazyRender") || 500);
			},
			"loadDates": function(pageInfo, callBack) {
				var self = this;
				if (pageInfo.isOver) {
					callBack && callBack();
					return;
				}

				var __call = function() {
					var type = pageInfo.id;
					var $dom = self.$el.find("div[items='" + type + "']");
					var $dom_content = $dom.find("div");
					if ($dom_content.find("div").length == 0) {
							$dom.html("<div class='ub uinn-pa2 ulev-app4 ub-pc ub-ac t-wh bc-gray' id='noData'>暂无数据</div>");
							self.scroller[type].disablePullUpToLoadMore();
							return;
					} else if (pageInfo.isOver || $dom_content.is("#noDataTip")) {
								$("#scoreBuy").removeClass("uhide");
								self.scroller[type].disablePullUpToLoadMore();
								if (pageInfo.reqPageNum > 2 && self.$el.find(".t-btn-a4").length == 0) {
									self.$el.append(_.template(boTop, self)); //添加点击按钮回到顶部

								}
								$dom.append(_.template(footTemplate, self.dataUser));
								var top = DMJS.CommonTools.getMarginTop(self, "transactionContentList", pageInfo);
								if (top != 0) {
									$dom.find("#bottom").css({
										marginTop: top
									});
								}
								self.scroller[type].refresh();
								return;
					} else {
								$("#scoreBuy").removeClass("uhide");
								if (pageInfo.pageIndex > 2 && self.$el.find(".t-btn-a4").length == 0) {
									self.$el.append(_.template(boTop, self)); //添加点击按钮回到顶部
		
								}
								return self.scroller[type].enablePullUpToLoadMore();
					}

				}
				if (pageInfo.id == 1) {

					self.controller.indexModel.shoppingCart({ //购物车列表
						cancelLightbox: true,
						data: {
							"pageSize": 10,
							"pageIndex": pageInfo.pageIndex
						},
						cancelLightbox: true,
						"success": function(_data) {
							var datalist = _data.data.scoreMallList;
							if (!datalist || datalist.length < Config.base("pageSize")) {
								$.ajaxSettings.lightboxHide = false;
								pageInfo.isOver = true;
								DMJS.CommonTools.popTip("已经加载完所有数据！");
							}
							self.allowsBalance = _data.data.allowsBalance;//是否允许余额支付
							if(!self.allowsBalance){
								$("#totalDiv").addClass('uhide');
								$("#totalDiv").removeClass('b_image-dragN');
								$("#totalDiv").addClass('b_image-dragY');
								$("#amountMoney").html(0 + "积分");//切换成积分总额
							}
							self.list = datalist;
							pageInfo.loaded = true;
							var $dom = $("#goodsList").find("div.ListArea");
							$("#allSelect").removeClass('selected');//去掉全选选中
							$("#allSelect").addClass('not_selected');
							for(var i in datalist){
								var goods = datalist[i];
								var num = goods.num>goods.stock?goods.stock:goods.num;
								if(goods.isCanMoney == "yes"){
									self.data.allAmountMoney1 += parseFloat(goods.amount)*num;//全选时使用金额和积分
								}else{
									self.data.allUseMoney=false;//购物车不是全部商品都能使用余额支付
								}
								if(goods.isCanScore == "yes"){
									self.data.allScore1 += parseFloat(goods.score)*num;
								}else{
									self.data.allUseSocre=false;//购物车不是全部商品都能使用积分支付
								}
								self.data.allNumber = self.data.allNumber + num;
							}
							if (pageInfo.pageIndex === 1) {
								self.data.allGoods = datalist;
								if(datalist.length < 1){
									$("#footer").addClass("uhide");
									$("#scoreBuy").addClass("uhide");
								}
								$dom.html(_.template(shopCartGoods, self));
							} else {
								self.data.allGoods = datalist;
								$dom.append(_.template(shopCartGoods, self));
							}
							pageInfo.pageIndex++;


						},
						"complete": function() {
							if (!!callBack) {
								callBack();
							}
							self.loadListScroller(pageInfo); 
							__call();

						}
					});
				}

			},
			'goodsDetail':function(e){
				var self = this;
				var $oldDom = $(e.target);
				var $dom = $oldDom;
				var divType = $dom.attr("divType");
                if(!$dom.is(".ListArea > div")){
                    $dom=$dom.parents(".ListArea > div");
                }
                var goodsId = $dom.attr("goodsId");
                switch(divType){
                	case "select" : this.selectGoods(goodsId,$oldDom,$dom);break;
                	case "reduce" : this.reduce(goodsId,$oldDom,$dom);break;
                	case "add" : this.add(goodsId,$oldDom,$dom);break;
                }
                /*if(goodsId&&!divType){
                	DMJS.router.navigate("index/index/goodsDetail/"+goodId, true);
                }*/
			},
			"loadListScroller": function(pageInfo) {
				
				var self = this;
				var type = pageInfo.id;
				!self.scroller && (self.scroller = {});
				var wraper = $("#goodsList").parents();
				if (!self.scroller[type]) {
					wraper.height(wrapView.height - $("#header").height() - $("#footer").height());
					self.scroller[type] = new PTRScroll(wraper[0], {
						pullUpToLoadMore: !pageInfo.isOver,
						hideScrollbar: true,
						refreshContent: function(done) {
							pageInfo.pageIndex = 1;
							pageInfo.isOver = false;
							//pageInfo.loaded=false;
							
							self.loadDates(pageInfo, done);
							
						},
						loadMoreContent: function(done) {
							self.loadDates(pageInfo, done);
						}
					}, true);
				} else {
					if (pageInfo.isOver) {
						self.scroller[type].disablePullUpToLoadMore();
					} else {
						self.scroller[type].enablePullUpToLoadMore();
					}
				}
		    },
			'chargeBtn' : function(e){//切换积分和金额支付按钮
				var self = this;
				var $dom=$(e.target);
				var hasMoney = false;//存在不能余额支付
				var hasScore =false;//存在不能积分兑换
				var allGoodsDom = $(".selected");
				for(var i in allGoodsDom){
					var goodsDom = allGoodsDom[i];
					var goodsId = $(goodsDom.parentNode).attr("goodsId");;
					var goods = this.returnSelObject(goodsId);
					if(goods && goods.isCanScore == "no"){
						hasScore = true;
					}
					if(goods && goods.isCanMoney == "no"){
						hasMoney = true;
					}
				}
				if($dom.hasClass('b_image-dragN')){//使用积分的按钮样式是灰色
					if(hasScore){
						if(allGoodsDom.length==1){
							DMJS.CommonTools.popTip("该商品不支持积分支付！");
						}else{
							DMJS.CommonTools.popTip("部分商品无法使用积分支付！");
						}
					}else{
						if(DMJS.userInfo.usableScore < this.data.allScore){
							DMJS.CommonTools.popTip("积分不足！");
						}else{
							$dom.removeClass('b_image-dragN');
							$dom.addClass('b_image-dragY');
							$("#amountMoney").html(this.data.allScore + "积分");//切换成积分总额
						}
						
					}
				}else{
					$dom.removeClass('b_image-dragY');
					$dom.addClass('b_image-dragN');
					$("#amountMoney").html("¥" + this.data.allAmountMoney);//切换成金额总额
				}
				
			},
			'reduce':function(id,e,dom){//减少数量事件
				var self = this;
				var $dom=$(e[0]);
				if(!$dom.hasClass("b_rectangle_right")){
					$dom = $dom.parent(".b_rectangle_right");
				}
				var number= $dom.next().html();
				number = parseFloat(number) - 1;
				
				if(number < 1){
//					DMJS.CommonTools.popTip("购买数量不能小于1！");
					number = 1;
					return;
				}
				if(number==1){
					$dom.removeClass("bc-withe");
					$dom.addClass("bc-gray1");
				}
				this.data.allNumber -= 1;//全选商品减少
				$dom.next().html(number);
				var goods = this.returnSelObject(id);//得到点击的商品详情
				var isRemainder= $("#totalDiv").hasClass("b_image-dragN");//是否是余额支付 true 余额支付  false 积分兑换
				if(dom.find(".selected").length > 0){
					self.data.allAmountMoney -= parseFloat(goods.amount);//获得总支付金额
					self.data.allScore -= parseFloat(goods.score);//获得总兑换积分
					if(isRemainder){//积分按钮为关闭
						$("#amountMoney").html("¥" + this.data.allAmountMoney);
					}else{
						$("#amountMoney").html(this.data.allScore + "积分");
					}
					this.data.goodsSum -= 1;
					$("#buyAmount").html(this.data.goodsSum);
				}
				if(goods.isCanMoney == "yes"){
					self.data.allAmountMoney1 -= parseFloat(goods.amount);//全选时使用金额和积分
				}
				if(goods.isCanScore == "yes"){
					self.data.allScore1 -= parseFloat(goods.score);
				}
			},
			'add':function(id,e,dom){//增加数量事件
				var self = this;
				var $dom=$(e[0]);
				if(!$dom.hasClass("b_rectangle_left")){
					$dom = $dom.parent(".b_rectangle_left");
				}
				var number= $dom.prev().html();
				number = parseFloat(number) + 1;
				var isOverstep= this.compare(id,number);
				if(isOverstep){
					number = $dom.prev().html();
					return;
				}
				if(number>1){
					$dom.parent().find(".b_rectangle_right").removeClass("bc-gray1");
					$dom.parent().find(".b_rectangle_right").addClass("bc-withe");
				}
				this.data.allNumber += 1;//全选商品增加
				$dom.prev().html(number);
				var goods = this.returnSelObject(id);//得到点击的商品详情
				var isRemainder= $("#totalDiv").hasClass("b_image-dragN");//是否是余额支付 true 余额支付  false 积分兑换
				if(dom.find(".selected").length > 0){
					self.data.allAmountMoney += parseFloat(goods.amount);//获得总支付金额
					self.data.allScore += parseFloat(goods.score);//获得总兑换积分
					if(isRemainder){//积分按钮为关闭
						$("#amountMoney").html("¥" + this.data.allAmountMoney);
					}else{
						if(DMJS.userInfo.usableScore < this.data.allScore){
							DMJS.CommonTools.popTip("积分不足！");
						}else{
							$("#amountMoney").html(this.data.allScore + "积分");
						}
					}
					this.data.goodsSum += 1;
					$("#buyAmount").html(this.data.goodsSum);
				}
				if(goods.isCanMoney == "yes"){
					self.data.allAmountMoney1 += parseFloat(goods.amount);//全选时使用金额和积分
				}
				if(goods.isCanScore == "yes"){
					self.data.allScore1 += parseFloat(goods.score);
				}
				
			},
			'compare':function(id,num){//比较是否超过库存，购买限量
				for(var i in this.data.allGoods){
					var goods = this.data.allGoods[i];
					if(goods.carId ==id){
						if(num > (goods.purchase -goods.ygCount)&& goods.purchase != 0){
							DMJS.CommonTools.popTip("不能超过商品的限购数："+ goods.purchase  +";已购数量：" + goods.ygCount);
							return true;
						}
						if(num > goods.stock){
							DMJS.CommonTools.popTip("不能超过商品的库存数");
							return true;
						}
						
					}
				}
			},
			'returnSelObject':function(id){//返回选中商品对象
				for(var i in this.data.allGoods){
					var goods = this.data.allGoods[i];
					if(goods.carId ==id){
						return goods;
					}
				}
			},
			'selectGoods':function(id,e,$dom){//选择商品
				var self = this;
				var num = parseInt($dom.find(".b_rectangle_center").html());
				var goods = this.returnSelObject(id);//得到点击的商品详情
				var isRemainder= $("#totalDiv").hasClass("b_image-dragN");//是否是余额支付 true 余额支付  false 积分兑换
				if(e.hasClass('not_selected')){//选中商品
					if(isRemainder){//余额支付
						e.removeClass('not_selected');
						e.addClass('selected');
						this.data.allAmountMoney += parseFloat(goods.amount)*num;//获得总支付金额
						this.data.allScore += parseFloat(goods.score)*num;//获得总兑换积分
					}else{//积分兑换
						if(goods.isCanScore=="no"){
							DMJS.CommonTools.popTip("该商品不能使用积分支付");
							return;
						}else{
							e.removeClass('not_selected');
							e.addClass('selected');
							this.data.allAmountMoney += parseFloat(goods.amount)*num;
							this.data.allScore += parseFloat(goods.score)*num;
						}
					}
					this.data.goodsSum += num;
				}else{
					e.removeClass('selected');
					e.addClass('not_selected');
					$("#allSelect").removeClass('selected');
					$("#allSelect").addClass('not_selected');
					this.data.goodsSum -= num;
					this.data.allAmountMoney -= parseFloat(goods.amount)*num;
					this.data.allScore -= parseFloat(goods.score)*num;
				}
				if(isRemainder){//积分按钮为关闭
					$("#amountMoney").html("¥" + this.data.allAmountMoney);
				}else{
					$("#amountMoney").html(this.data.allScore + "积分");
				}
				$("#buyAmount").html(this.data.goodsSum);
			},
			'delShoppingCar':function(e){
				var self = this;
				var allSelect = $(".selected");
				var carId = [];
				var pageInfo = {
					id: 1,
					isOver: false,
					loaded: false,
					pageIndex: 1};
				for(var i = 0;i< allSelect.length;i++){
					carId.push($(allSelect[i]).parent().attr("goodsid"));
				}
				console.log(carId);
				if(carId.length>0){
					self.controller.indexModel.delShoppingCar({ //删除购物车商品
						cancelLightbox: true,
						data: {
							'ids':carId.toString(),
						},
						"success": function(_data) {
							if(_data.code == "000000"){
								self.reflush();
							}else{
								DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"),_data.description);
							}
						},
					});
				}
			},
			'toCompute':function(){
				var self = this;
				var hasMoney = false;//存在不能余额支付
				var hasScore =false;//存在不能积分兑换
				var isRemainder= $("#totalDiv").hasClass("b_image-dragN");//是否是余额支付 true 余额支付  false 积分兑换
				var allSelect = $(".selected");
				if(allSelect.length <1){//没有商品直接结束
					console.log("无选中商品");
					return;
				}
				for(var i = 0;i< allSelect.length;i++){
					var id = $(allSelect[i]).parent().attr("goodsid");
					if(!id){
						continue;
					}
					var goods = this.returnSelObject(id);//得到点击的商品详情
					if(goods.stock == 0){
						DMJS.CommonTools.popTip("已选择的商品中存在库存不足");
						self.data.selectedGoodsList=new Array();
						return;
					}
					if(goods){
						var selectedNum = $(allSelect[i]).parent().find('.b_rectangle_center').html();
						goods.selectedNum = parseInt(selectedNum);//结算时商品个数
					    self.data.selectedGoodsList.push(goods);
					}
					if(goods && goods.isCanScore == "no"){
						hasScore = true;
					}
					if(goods && goods.isCanMoney == "no"){
						hasMoney = true;
					}
				}
				var isTrue = false;
				if(isRemainder){
					if(hasMoney){
						self.data.selectedGoodsList=new Array();
						DMJS.CommonTools.popTip("部分商品不能使用余额支付");
					}else{
						//去处理
						console.log("余额支付")
						isTrue = true;
					}
				}else{
					if(hasScore){
						self.data.selectedGoodsList=new Array();
						DMJS.CommonTools.popTip("部分商品不能使用积分支付");
					}else{
						//去处理
						console.log("积分支付");
						isTrue = true;
					}
				}
				if(isTrue){//查询用户余额
					self.controller.indexModel.myAccount({
						"success": function(_data) {
							if(_data.code == "000000"){
								if(isRemainder){
									if(_data.data.overAmount < self.data.allAmountMoney){
										DMJS.CommonTools.popTip("余额不足");
										self.data.selectedGoodsList=new Array();
										return false;
									}else{
										//处理
										  var data = {};
										  data.goodsSum = self.data.goodsSum;
					    			      data.payType = "money";
					    			      data.totalValue = self.data.allAmountMoney;
					    			      data.scoreMall = self.data.selectedGoodsList;
					    			      DMJS.currentView.controller.orderData = data;
					    			      DMJS.router.navigate("index/index/fillInOrder", true);
									}
								}else{
									if(_data.data.usableScore < self.data.allScore){
										DMJS.CommonTools.popTip("积分不足");
										return false;
									}else{
										//处理
									  var data = {};
									  data.goodsSum = self.data.goodsSum;
				    			      data.payType = "score";
				    			      data.totalValue = self.data.allScore;
				    			      data.scoreMall = self.data.selectedGoodsList;
				    			      DMJS.currentView.controller.orderData = data;
				    			      DMJS.router.navigate("index/index/fillInOrder", true);
									}
								}
							}else{
								DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"),_data.description);
							}
						},
					});
				}
			},
			'nullFunc':function(e){//空事件
				return;
			}
				
		});

		return shopCartView;
	});