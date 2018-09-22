define([ 'text!userTemplate/payment/recharge.html','commonTool/validator'], function(rechargeTemplate,Validator) {
	var rechargeView = DMJS.DMJSView.extend({
		id : 'rechargeViewContent',
		name : 'rechargeViewContent',
		tagName : 'div',
		className : "rechargeViewContent",
		events : {
			"tap #turnRadio":"turnRadio",
			'input input[name="amount"]':'calculateFees',
			'tap #sc_amount':'shortCut',
		},
        data : {
		},
		init : function(options) {
			_.extend(this, options);
		},
		render : function() {
			var self = this;
			this.getFeet();
			this.cardList();
			return this;
		},
		turnRadio:function(e){var aa= e.target.parentNode.querySelectorAll('input[type=radio]')[0].checked=true;},
		charge:function(){
			var self=this;
			var paraData=self.$el.getFormValue();
			//只有通联需要放开。其他方式请注释
			if(DMJS.CommonTools.isWeiXin()&&paraData['paymentInstitution']=='ALLINWAPPAY'){
				DMJS.CommonTools.tongLian();
				return false;
			}
			
        	if(!Validator.check($("#"+self.id))){ 
                    $("#amount").val("");
                    $("#paySum").html("0.00");
                    $("#txsxf").html("0.00");
        	   		return false;
        	}
        		   //连连支付需要卡id
        		   if(paraData['paymentInstitution']=='LIANLIANGATE'){
        		   	if(self.bankList.myBankList.length>0){
        		   	paraData['cardId']=self.bankList.myBankList[0].id;
        		   }
        		   	else{
        		   		DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"),'请先绑卡',function(){
						DMJS.router.navigate("user/payment/addCard",true);});
						return false;
        		   	}
        		   }
        		   
        		    //paraData['paymentInstitution']="SHUANGQIAN";
        		    // paraData['paymentInstitution']="ALLINWAPPAY";
        		   // paraData['paymentInstitution']="LIANLIANGATE";
                    self.controller.userModel.charge({"data":paraData,
                     "success":function(response){
								if(response.data.url){
									Native.openChildWebActivity(response.data.url,{title:"第三方"});
								}else{
									DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"),'充值成功',function(){
									DMJS.router.navigate("user/personal/user",true);});
								}
							},
					 	'error':function(response){
					 	if(response.data.url){
									Native.openChildWebActivity(response.data.url,{title:""});
								}else{
									DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"),response.description);
								}
					 }
                    });
		},
		calculateFees:function(e){
			var self=this,amount;
        	var __val=$(e.target).val();
        	$("#sc_amount").children().removeClass('no');
        	amount=__val.replace(/[^\d\.]/g,"").replace(/\.{2,}/,".").replace(/^[0|\.]/g,'').replace(/\./,"");
  	   		/*if(__val.split(".").length>=2){
      		var __vals=__val.split(".");
      		var __vals_last=__vals[0];
      		var xhd=__vals[1];
      		__val=__vals_last+"."+xhd.substring(0,2)
  	  		 }*/
        	 amount=amount*1;
  	     	if(self.data.max<amount){
     			DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"),"充值金额不能高于最大值"+self.data.max+"元");
       			amount=self.data.max;
           	 }
            
           	 $(e.target).val(amount);
         	 self.feet(amount);
           	   //双乾手续费
//        		self.sqfeet(amount);
        },
        shortCut:function(e){
        	var self=this;
        	if(!e.target.dataset.amount) return ;
        	$(e.target).parent().children().removeClass('no');
        	$(e.target).addClass("no");
        	var $amount=$("#amount");
        	var amount=e.target.dataset.amount;
        	
        	if(!!amount){
        		$amount.val(amount);
      		self.feet(amount);
        		  //双乾手续费
//        		self.sqfeet(amount);
        	}
        },
        feet:function(amount){
        	var self=this,fee=self.data,p=fee.p,pMax=fee.pMax,min=fee.min,max=fee.Max,txsxf=amount*p;
        	if(txsxf>=pMax){
        		txsxf=pMax;
        	}
        	// if(txkfType=="true")
        	  $("#paySum").html((amount-txsxf).toFixed(2));//实际金额
        	  $("#txsxf").html((txsxf*1).toFixed(2));
        },
        //双乾手续费
        sqfeet:function(amount){
        	var self=this,txsxf;
        	var kjfeetype = self.data.sqFee.kjFeeType;
        	var kuaiJieRate = self.data.sqFee.kuaiJieRate;
        	
        	if(amount=='0'){
        		$("#paySum").html('0.00');
        		 $("#txsxf").html('0.00');
        	}else{
        		if(parseInt(kjfeetype)==2||parseInt(kjfeetype)==4){
        		txsxf = 0;
	        	}else if(parseInt(kjfeetype)==1||parseInt(kjfeetype)==3){
	        		if(amount*kuaiJieRate<2)
	        		{
	        			txsxf = 2;
	        		}else{
	        			txsxf = amount*kuaiJieRate;
	        		}
	        	}
	        	  $("#paySum").html((amount-txsxf).toFixed(2));//实际金额
	        	  $("#txsxf").html((txsxf*1).toFixed(2));
        	}
        	
        },
        getFeet:function(){
        	var self=this;
        	  self.controller.userModel.fee({
					 async: true,'cancelLightbox':true,
                     "success":function(response){
                     _.extend(self.data,response.data.chargep);
//                      双乾托管打开以下注释
//                   self.data.sqFee = response.data.sqFee;
                   self.data.withdrawp = response.data.withdrawp;
                  			 	self.$el.html(_.template(rechargeTemplate, self.data)); // 将tpl中的内容写入到
								DMJS.CommonTools.imgDown.apply(self,[$('img')]);
							},
					 	'error':function(response){
								DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"),response.description);
					 }
                    });
        },
        cardList:function(){
           	var self=this;
           	 self.controller.userModel.myBankList({
					'data': {},'cancelLightbox': false,"noCache": true,
					"success": function(response) {	
						self.bankList=response.data;
					},
					'error': function(response) {DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"), response.description);}
					});
           },
           destroy:function(){
			var self=this;
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

	return  rechargeView;
});
