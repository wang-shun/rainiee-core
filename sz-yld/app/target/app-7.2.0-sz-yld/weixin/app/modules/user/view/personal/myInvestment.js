define(['text!userTemplate/personal/myInvestment.html', 'text!userTemplate/personal/myInvestmentList.html',
	'commonTool/tool', 'commonTool/slide', 'commonClass/scroll/PTRScroll', 'text!commonTemplate/foot/boTop.html',
	'text!userTemplate/payment/creditorOverBuy.html'
], function(myInvestmentTemplate, myInvestmentListTpl, tool, Slide, PTRScroll, boTop,creditorOverBuyTpl) {
	var myInvestmentV = DMJS.DMJSView.extend({
		id: 'myInvestment',
		name: 'myInvestment',
		tagName: 'div',
		className: "myInvestment",
		events: {
			'tap .JSAppointDropDown': 'JSAppointDropDown',
			'tap .JviewContract': 'viewContract',//查看合同
			'tap .Jzr': 'isGreaterThree' ,//转让
			'tap .JSDetail':'JSDetail',//跳转到详情
		},
		init: function(options) {
			var self = this;
			_.extend(self, options);
			self.accountInfo();
			self.pageInfo = {
				"0": {
					"pageIndex": 1,
					"loaded": false,
					"isOver": false,
					"id": 0,
					'dataS': 'hkz'
				},
				"1": {
					"pageIndex": 1,
					"loaded": false,
					"isOver": false,
					"id": 1,
					'dataS': 'tbz'
				},
				"2": {
					"pageIndex": 1,
					"loaded": false,
					"isOver": false,
					"id": 2,
					'dataS': 'yjq'
				}
			};


		},
		render: function() {
			var self = this,typeB;
			self.gaibAmount = tool.gaibAmount;
			this.$el.html(_.template(myInvestmentTemplate, {}));
			$("#contentWrapper").height(wrapView.height-$("#header").height()-$("#indexTitleContent").height());
			self.slider = new Slide(self.$el.find("#slider")[0], "H", function() {
				
				var cur = this.currentPoint;
				if(cur!=self.currentType){
					self.currentType=cur;
					$('.JSAppointDropDown li').removeClass('lbd-b-blue');
					$($('.JSAppointDropDown li')[cur]).addClass('lbd-b-blue');
					self.loadDates(self.pageInfo[cur]);
				}
			}, false, function(e) {});
			if(self.typeB=='tbz'){
				typeB=self.pageInfo['1'];
				//typeB.pageIndex = 1;
				typeB.pageIndex = 1;
				$('.JSAppointDropDown li').removeClass('lbd-b-blue');
				$($('.JSAppointDropDown li')[1]).addClass('lbd-b-blue');
				self.slider.moveToPoint(1);
				typeB.isOver = false;
				self.loadDates(typeB);
			}else{
				typeB=self.pageInfo['0'];
				self.loadDates(typeB);
				self.currentType=typeB.id;
			}
			
				
			

			return this;
		},
		accountInfo:function(){//账号信息
			var self=this;
			self.controller.userModel.myAccount({
				"noCache": true,
				cancelLightbox: true,
				"success": function(response) {
					
					self.tg=response.data.tg;//是否托管
					self.isWithdrawPsd = response.data.isWithdrawPsd;
				},
				'error': function(response) {
					DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"), response && response.description);
				}
			});
		},
		JSDetail:function(e){
			var self = this;
			var $dom = $(e.target);
			var bidId = $dom.parents('.Jdc').attr('bidid');
			self.scroller = {};
			this.noDestroy=true;
			DMJS.router.navigate("index/index/projectInvDetail/"+bidId,true);
		},
		JSAppointDropDown: function(e) {
			var self = this;
			var $dom = $(e.target);
			var chooiceType = $dom.attr("listType");
			if(chooiceType!=self.currentType){
				self.currentType=chooiceType;
				
				$('.JSAppointDropDown li').removeClass('lbd-b-blue');
				$($('.JSAppointDropDown li')[chooiceType]).addClass('lbd-b-blue');
				
				self.loadDates(self.pageInfo[chooiceType]);
				self.slider.moveToPoint(parseInt(chooiceType));
			}
		},
		'viewContract': function(e) {//查看借款合同
			var self = this;
			var $dom = $(e.target);
			var id = $dom.parents('.Jdc').attr('bidid');
			var zqId=$dom.parents('.Jdc').attr('zqid');
			var blzqzrid=$dom.parents('.Jdc').attr('blzqzrid');
			this.noDestroy=true;
			if($dom.hasClass('ZR')){
				if(self.isAllowBadclaim=="true"){
					self.controller.userModel.saveBidContract({
	        		'data':{'zqId':zqId,'blzqzrId':blzqzrid,type:'blzrht'},
	        		'cancelLightbox': false,
					"noCache": true,
					'success':function(response){
						Native.openChildWebActivity(response.data,{title:"合同"});
					},
					'error':function(response){
						DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"), response.description);
					}
	        	});
				}else{
					DMJS.router.navigate("user/personal/viewContract/" +blzqzrid+"/"+zqId, true);
				}
				
				
			}else if($dom.hasClass('JK')){
			   	if(self.isSaveBidContract=="true"){
			   		self.controller.userModel.saveBidContract({
	        		'data':{'creditId':id,'type':'jkht'},
	        		'cancelLightbox': false,
					"noCache": true,
					'success':function(response){
						if(response.data){
							Native.openChildWebActivity(response.data,{title:"合同"});
						}else{
							DMJS.router.navigate("user/personal/viewContract/" + id, true);
						}
						
					},
					'error':function(response){
						DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"), response.description);
					}
	        	});
			   	}else{
					DMJS.router.navigate("user/personal/viewContract/" + id, true);
			   }
			}
			
		},
		zr: function($dom) {//转让
			var self = this,zrList={};
			var sourceZqPrice = $dom.parents('.Jdc').attr('sourceZqPrice');
			var zqzrRateMin = $dom.parents('.Jdc').attr('zqzrRateMin');
			var zqzrRateMax = $dom.parents('.Jdc').attr('zqzrRateMax');
			var nhl = $dom.parents('.Jdc').attr('nhl');
			zrList.ycfxsyqs=$dom.parents('.Jdc').attr('ycfxsyqs');
			zrList.hkfs=$dom.parents('.Jdc').attr('hkfs');
			zrList.tg=self.tg;
			zrList.isWithdrawPsd=self.isWithdrawPsd;
			zrList.creditorId = $dom.parents('.Jdc').attr('creditorId');
			zrList.sourceZqPrice = $dom.parents('.Jdc').attr('sourceZqPrice');
			zrList.transferRate = $dom.parents('.Jdc').attr('transferRate');
			//新增转让价格的最大最小值
			zrList.zqzrMin = parseFloat(sourceZqPrice*zqzrRateMin).toFixed(2);
			zrList.zqzrMax = parseFloat(sourceZqPrice*zqzrRateMax).toFixed(2);
			self.zrContent=_.template(creditorOverBuyTpl, zrList);
			wrapView.FlipPrompt.confirm({
				title: "转让",
				content: self.zrContent,
				FBntconfirm: "转让",
				FBntcancel: "取消",
				IsBntConverse: "true",
				FBntConfirmColor: "pop_btn_orange",
				autoCloseBg:"false",
			}, function() {
				if((zrList.zqzrMin!="0.00" && zrList.zqzrMax!="0.00") && (!$('#salePrice').val() || parseFloat($('#salePrice').val())<zrList.zqzrMin || parseFloat($('#salePrice').val())>zrList.zqzrMax)){
					
					$('#tips').text('请输入一个'+zrList.zqzrMin+'~'+zrList.zqzrMax+'之间的金额！');
					return false;
				}else if(!$('#tranPwd').val()){					
					$('#tips').text('请输入交易密码！');
					return false;
				}else{
					$('#tips').text('');
				}
				
				var parms = $("#creditorBuy").getFormValue();
					parms['creditorId']=zrList.creditorId;
					parms['bidAmount']=zrList.sourceZqPrice;
					parms['sjzrjg']=$("#lastPrice").text();
					parms['hkfs']=zrList.hkfs;
				self.controller.userModel.transfer({ //债权转让
					cancelLightbox: true,
					data: parms,
					cancelLightbox: true,
					"success": function(_data) {
						if(_data.code==='000000'){
							$('#FlipPrompt').hide();
							wrapView.lightBox.hide();
							DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"),'转让申请提交成功，请耐心等待审核！');
							setTimeout(function(){
								DMJS.router.navigate("user/payment/creditorOver", true);
							},1000);
						}
						
					},
					'error': function(response) {
						if((response.description).indexOf('交易密码')!=-1){
							$('#tips').text(response.description);
							
							
						}
						else{
							
							$('#FlipPrompt').hide();
							wrapView.lightBox.hide();
							DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"),response.description);
							setTimeout(function(){
								DMJS.CommonTools.hideLightBoxAlert();
							},1000);
							 
						}
						
					}
				});
			}, function() {

			});
			
			$("#salePrice").bind("input",function(){
				var salePrice=$(this).val();
				if(salePrice == 0){
					$("#lastPrice").text('');
				}else{
					var lastPrice = parseFloat(salePrice) - parseFloat(sourceZqPrice*zrList.ycfxsyqs*nhl/12/100);
					$("#lastPrice").text(parseInt(lastPrice));
				}
			
			});
			
		},
	
		'isGreaterThree':function(e){//距离下个还款日小于三天就禁止转让(苹果不支持 2000-10-10 这样的日期格式)
			var self=this;
			var $dom = $(e.target),xghkr=($dom.attr('xghkr')).replace(/-/g,'/');
        	self.controller.commonModel.timing({
					"data" : {},'cancelLightbox':true, async:true, 
					"success" : function(_data) {
						var sysTime=tool.changeTime(_data.data.time,'day');//系统时间
						var day=DMJS.CommonTools.DateDiff(xghkr,sysTime,'/');
						if(day>3){
							self.zr($dom);//转让
							
						}else{
							DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"),'距离下一个还款日小于3天，不能转让!');
							return false;
						}
						
					},
					"error" : function(response) {
							DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"),response.description);
					}
				});
		},
		"loadDates": function(pageInfo, callBack) {
			var self = this;
			self.accountInfo();
			if (pageInfo.isOver) {
				callBack && callBack();
				return;
			}

			var __call = function() {
				var type = pageInfo.id;
				var $dom = self.$el.find("div[items='" + type + "']").find("div.ListArea");
				var $dom_content = $dom.find("div");
				if ($dom_content.find("div").length == 0) {
					$dom.html("<div class='ub uinn-pa2 ulev-app4 ub-pc ub-ac t-ddd' id='noData'>暂无数据</div>");

					self.scroller[type].disablePullUpToLoadMore();

					return;
				} else if (pageInfo.isOver || $dom_content.is("#noDataTip")) {
					self.scroller[type].disablePullUpToLoadMore();
					if (pageInfo.pageIndex > 2 && self.$el.find(".t-btn-a4").length == 0) {
						self.$el.append(_.template(boTop, self)); //添加点击按钮回到顶部

					}
					
					return;
				} else {
					if (pageInfo.pageIndex > 2 && self.$el.find(".t-btn-a4").length == 0) {
						self.$el.append(_.template(boTop, self)); //添加点击按钮回到顶部

					}
					return self.scroller[type].enablePullUpToLoadMore();
				}

			}


			self.controller.userModel.myCreditorInfo({ //我的投资
				cancelLightbox: true,
				data: {
					"type": pageInfo.dataS,
					"pageIndex": pageInfo.pageIndex,
				},
				cancelLightbox: true,
				"success": function(_data) {
					var datalist = _data.data;
					if (!datalist || datalist.length < Config.base("pageSize")) {
						$.ajaxSettings.lightboxHide = false;
						pageInfo.isOver = true;
						DMJS.CommonTools.popTip("已经加载完所有数据！");
					}

					self.typeS = pageInfo.dataS;
					self.list = datalist;
					pageInfo.loaded = true;
					var $dom = self.$el.find("div[items='" + pageInfo.id + "']").find("div.ListArea");
					if (pageInfo.pageIndex === 1) {

						$dom.html(_.template(myInvestmentListTpl, self));
					} else {

						$dom.append(_.template(myInvestmentListTpl, self));
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


		},
		loadListScroller: function(pageInfo) {
			var self = this;
			var type = pageInfo.id;
			!self.scroller && (self.scroller = {});
			var wraper = $("div[items='" + type + "']");
			if (!self.scroller[type]) {
				wraper.height(wrapView.height - $("#header").height() - $("#indexTitleContent").height() - $("#footer").height());
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
		runScrollerTop:function(){
		 	var self=this;
            var type=self.currentType;
        	this.scroller[type].scrollToPage(0,0,500);
        	$(".boTopTag").remove();
          
        }
	});
	return myInvestmentV;
});