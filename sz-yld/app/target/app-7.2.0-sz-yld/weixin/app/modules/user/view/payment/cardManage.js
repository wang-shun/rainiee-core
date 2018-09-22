define([ 'text!userTemplate/payment/cardManage.html','commonTool/tool' ], function(cardManageTemplate,tool) {
	var cardManageView = DMJS.DMJSView.extend({
		id : 'cardManageViewContent',
		name : 'cardManageViewContent',
		tagName : 'div',
		className : "cardManageViewContent",
		events : {
			
			"tap #cardInfo":"cardInfo",
			"tap #charge":"charge",
			'tap #select_card':'select_card'
		},
        data : {
        	X:0,
        	width:0
		},
		
		startX:0,
		endX:0,
		dataUser:{
		},
		init : function(options) {
			_.extend(this, options);
		},
		render : function() {
			var self = this;
			self.controller.userModel.myBankList({
					'data': {},'cancelLightbox': false,"noCache": true,
					"success": function(response)
					{
						self.cardList=response.data;
						self.$el.html(_.template(cardManageTemplate, self.cardList)); //flag:false(申请更换银行卡),flag:true(查看更换银行卡)
					},
					'error': function(response) {DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"), response.description);}
					});
			
		
			return this;
		},
		cardInfo:function(e){
				var $dom=$(e.target);
				if($dom.is('#charge')){
					return;
				}
                $dom=$dom.parents("div[data-id]");
                var id = $dom.attr("data-id");
                var bankName = $dom.attr("data-bankname");
                var bankNum=$dom.attr("data-number");
                var del=$(e.target).attr("data-delete");
				if(id&&!del){
					DMJS.router.navigate('user/payment/cardInfo/'+bankNum+"/"+bankName,true);
				}
		},
		addCard:function(){
			DMJS.router.navigate('user/payment/addCard',true);
		},
		select_card:function(){//查询更换银行卡信息
			var self=this;
			
				self.controller.userModel.fyouQueryBank({
					'cancelLightbox': false,"noCache": true,
					"success": function(response) {	
						
						DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"), response.description,function(){
							self.reflush();
						});
						
					},
					'error': function(response) {
						DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"), response.description);
						}
					});
			
			
			
		},
		charge:function(e){
			
			var self=this;
			wrapView.FlipPrompt.confirm({    //提示去充值
   				title: "温馨提示",
   				content: '是否确认更换',
   				FBntconfirm: "取消",
				FBntcancel: "确认",
				FBntCancelColor: "pop_btn_orange"
				}, function() {}, function() {
					self.controller.userModel.changeCard({
						'cancelLightbox': false,"noCache": true,
						"success": function(response) {	
							Native.openChildWebActivity(response.data.url,{title:"更换银行卡"});
									
						},
						'error': function(response) {DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"), response.description);}
						});
				});
			
		},
		start:function(e){
			this.data.X=e.touches[0].clientX;
			e.preventDefault();
			e.returnValue=false;
		},
		move:function(e){
			var width=e.touches[0].clientX-this.data.X;
			console.log(width);
			this.data.width+=width;
			e.target.parentNode.style.webkitTransitionDuration="200ms";
			if(width>10){
					e.target.parentNode.style.webkitTransform='translate(0px, 0px) scale(1) translateZ(0px)';	
			}
				else if(this.data.width<-100){
					e.target.parentNode.style.webkitTransform='translate(-150px, 0px) scale(1) translateZ(0px)';	
			}else if(width-this.data.width>3){
				e.target.parentNode.style.webkitTransform='translate('+width+'px, 0px) scale(1) translateZ(0px)';
				//alert(e.target.parentNode.style.transform);
			}
		},
		 end:function (e) {
		 	if(this.data.X-e.changedTouches[0].clientX>100){
		 		e.target.parentNode.style.webkitTransform='translate(-150px, 0px) scale(1) translateZ(0px)';
		 	}
		 		this.data.X=0;
        		this.data.width=0;
           },
          
          
	});

	return  cardManageView;
});
