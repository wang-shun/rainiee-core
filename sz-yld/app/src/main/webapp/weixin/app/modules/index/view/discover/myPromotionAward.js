define(['text!indexTemplate/discover/promotionAward.html', 'text!indexTemplate/discover/myPromotionAward.html','text!commonTemplate/bshare.html',
		'commonClass/commonTools'],
	function(promotionAwardTpl, myPromotionAwardTemplate,bshareTpl,comonTools) {
		var myPromotionAwardView = DMJS.DMJSView.extend({ //项目投资
			id: 'myPromotionAward',
			name: 'myPromotionAward',
			tagName: 'div',
			className: "myPromotionAward",
			events: {
				'touchstart  .JLongPress': 'longPress',
				'touchend  .JLongPress': 'longPressUp',
				'tap .spreadBtn':'toShareTip'
				//'tap .Jshare':'Jshare'
			},
			init: function(options) {
				_.extend(this, options);
			},
			render: function() {
				var self = this;
				$('.bdshare-slide-button').show();
				self.promotionAward();
				return this;
			},

			'promotionAward': function() { //推广奖励

				var self = this;
				self.controller.indexModel.mySpread({
					cancelLightbox: true,
					"success": function(_data) {
						var data = _data.data;
						if (self.typeS === '1') { //推广奖励
							self.$el.html(_.template(promotionAwardTpl, data)); // 将tpl中的内容写入到 this.el 元素中 
						} else { //我的推广
							if(!data.spreadEntitys){
								data.spreadEntitys=[];
							}
							self.$el.html(_.template(myPromotionAwardTemplate, data)); // 将tpl中的内容写入到 this.el 元素中 
							self.loadScroller();
						}
						//console.log(data.msg.substring((data.msg).indexOf("http://"),data.msg.length));
						//2维码
						
						self.loadScroller();
					},

					'error': function(response) {
						DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"), response.description);
					}
				});
			},
			
			toShareTip : function(){
				var self=this;
				var spreadBox=$('<div class="spread_box"><div class="spread_tipBox"></div></div>');
				$("#page_0").after(spreadBox);
				spreadBox.height(wrapView.height);
				setTimeout("$('.spread_box').remove()",1000);
			},
			
			'longPress': function(e) {
				var self = this;
				self.startTime = new Date();


			},
			share:function(data){
				window._bd_share_config = {
		common : {
			bdText : '',	
			bdDesc : data.msg,	
			bdUrl : data.msg.substring((data.msg).indexOf("http://"),data.msg.length), 	
			bdPic : '自定义分享图片'
		},
		share : [{
			"bdSize" : 16
		}],
		slide : [{	   
			bdImg : 0,
			bdPos : "right",
			bdTop : 100
		}],
		image : [{
			viewType : 'list',
			viewPos : 'top',
			viewColor : 'black',
			viewSize : '16',
			viewList : ['qzone','tsina','huaban','tqq','renren']
		}],
		selectShare : [{
			"bdselectMiniList" : ['qzone','tqq','kaixin001','bdxc','tqf']
		}]
	}

			},
		destroy:function(){
			var self=this;
			$('.bdshare-slide-button').hide()
			DMJS.removeThread("count_timer");
			self.destroyChilds();
            // 解除事件绑定
            self.undelegateEvents();
            if (self.scroller) {
                self.scroller.destroy();
                self.scroller = undefined;
            }
            // 移除DOM元素
            self.$el.remove();
            
		}

		});

		return myPromotionAwardView;
	});

