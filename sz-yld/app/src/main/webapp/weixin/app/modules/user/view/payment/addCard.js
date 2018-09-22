define(['text!userTemplate/payment/addCard.html', 'commonTool/validator'], function(addCardTemplate, Validator) {
	var addCardView = DMJS.DMJSView.extend({
		id: 'addCard',
		name: 'addCard',
		tagName: 'div',
		className: "addCard",
		events: {

		},
		data: {},
		init: function(options) {
			_.extend(this, options);
		},
		render: function() {
			var self = this;
			self.selectProvince('SHENG'); //得到所有的省
			this.$el.html(_.template(addCardTemplate, self)); // 将tpl中的内容写入到
			self.loadScroller();
			self.bankList();
			return this;
		},
		addCard: function() {
			var self = this;
			if(self.data.XIAN == undefined) {
				DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"), "请选择完整的地址");
				return false;
			}
			if(!Validator.check($("#" + self.id))) {
				return false
			}
			var params = self.$el.getFormValue();
			params['xian'] = self.data.XIAN;
			self.controller.userModel.addCard({
				'data': params,
				'cancelLightbox': true,
				"noCache": true,
				"success": function(response) {
					if(response.data.url){
						Native.openChildWebActivity(response.data.url,{title:"第三方"});
					}else{
						DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"), '添加银行卡成功',
							function() {
								var list = window.listHash;
								if(list && list.length >= 2) {
									DMJS.router.navigate(list[list.length - 2], true);
									list.length = list.length - 1;
								} else {
									DMJS.router.navigate("user/payment/cardManage", true);
								}
	
							});
					}
				},
				'error': function(response) {
					DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"), response.description);
				}
			});

		},
		bankList: function() {
			var self = this;
			self.controller.userModel.bankList({
				'data': {},
				'cancelLightbox': true,
				"noCache": true,
				"success": function(response) {
					self.selectTip('selectBank', 'bank', response.data);
				},
				'error': function(response) {
					DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"), response.description);
				}
			});
		},
		selectTip: function(id, selectId, list) {
			var self = this;
			var optionVals = [],
				optionText = [];
			for(var i = 0; i < list.length; i++) {
				optionVals.push(list[i].id);
				optionText.push(list[i].bankName);
			}
			DMJS.CommonTools.showList({
				'inputID': id,
				'id': selectId,
				'optionVals': optionVals,
				'optionText': optionText
			}, function() {});
			$("#selectBank").val("");

		},
		selectProvince: function(type) { //得到省
			var self = this;
			self.controller.commonModel.searchAddress({
				"data": {
					"type": type
				},
				async: true,
				'cancelLightbox': true,
				"success": function(response) {
					var list = response.data;
					var optionVals = [];
					var optionText = [];
					for(var i = 0; i < list.length; i++) {
						optionVals[i] = list[i].id;
						optionText[i] = list[i].sheng;
					}
					//1.inputId  2.selectId 3.select-Vlaue数组  4.select-Text数组  5.type 6.回调函数 7.是否执行 8是否重写func方法
					self.select('selectProvince', 'province', optionVals, optionText, 'SHI', self.selectCity, true);
				},
				'error': function(response) {
					DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"), response.description);
				}
			});

		},
		select: function(inputSelect, selectList, optionVals, optionText, type, next, execute, func) { //构建可滑动选择的下拉框
			var self = this;
			var fun = func || function(e) {
				var option = e[0].options[e[0].selectedIndex];
				$("#" + inputSelect).html(option.text);
				self.data.XIAN = option.value;
				if(execute) next.apply(self, [option.value, type]);
				if(type == "SHI") {
					$('#selectCity').html("请选择");
					$('#selectCounty').html("请选择");
					$('#city').html("");
					$('#county').html("");
					self.data.XIAN = undefined
				}
				//如果是双乾 就注释下面
				if(type == "XIAN") {
					$('#selectCounty').html("请选择");
					$('#county').html("");
					self.data.XIAN = undefined
				}
			};
			DMJS.CommonTools.showList({
				'inputID': inputSelect,
				'id': selectList,
				'optionVals': optionVals,
				'optionText': optionText
			}, fun);

		},
		selectCity: function(id, type) {
			var self = this;
			self.controller.commonModel.searchAddress({
				"data": {
					provinceId: id,
					"type": type
				},
				async: true,
				'cancelLightbox': true,
				"success": function(response) {
					var list = response.data;
					var optionVals = [];
					var optionText = [];
					for(var i = 0; i < list.length; i++) {
						optionVals[i] = list[i].id;
						optionText[i] = list[i].shi;
					}
					self.select('selectCity', 'city', optionVals, optionText, 'XIAN', self.selectCounty, true);
				},
				'error': function(response) {
					DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"), response.description);
				}
			});

		},
		selectCounty: function(id, type) {
			var self = this;
			self.controller.commonModel.searchAddress({
				"data": {
					cityId: id,
					"type": 'XIAN'
				},
				async: true,
				'cancelLightbox': true,
				"success": function(response) {
					var list = response.data;
					var optionVals = [];
					var optionText = [];
					for(var i = 0; i < list.length; i++) {
						optionVals[i] = list[i].id;
						optionText[i] = list[i].xian;
					}
					self.select('selectCounty', 'county', optionVals, optionText);
					$('#county').val("");
				},
				'error': function(response) {
					DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"), response.description);
				}
			});

		},

	});

	return addCardView;
});