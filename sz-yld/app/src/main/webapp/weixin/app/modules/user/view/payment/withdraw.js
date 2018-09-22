define([ 'text!userTemplate/payment/withdraw.html','commonTool/tool','commonTool/validator'], function(withdrawTemplate,tool,Validator) {
	var withdrawView = DMJS.DMJSView.extend({
		id : 'withdrawContent',
		name : 'withdrawviewContent',
		tagName : 'div',
		className : "withdrawContent",
		events : {
			"tap #turnRadio":"turnRadio",
			'input input[name="amount"]':'calculateFees',
		},
        data : {

		},
		dataUser:{
			userName:undefined
		},
		init : function(options) {
			_.extend(this, options);
		},
		render : function() {
			var self = this;
		//	this.$el.html(_.template(withdrawTemplate, self)); // 将tpl中的内容写入到
			self.cardList(self.getFeet);
			return this;
		},
		//个人账户信息
		myAccount: function() {
            var self = this;
            self.controller.userModel.myAccount({
                "noCache": true,
                cancelLightbox: true,
                "success": function(response) { 
                	self.overAmount=response.data.overAmount;
                    $("#overAmount").html(response.data.overAmount)//获取可投金额
                },
                 'error':function(response){DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"), response&&response.description);}
            });
        },
		
		turnRadio:function(e){var aa= e.target.parentNode.querySelectorAll('input[type=radio]')[0].checked=true;},
		withdraw:function(){
			var self=this;
			if(!Validator.check($("#"+self.id))){ return false;}
			//if($("#amount").val()*1>self.overAmount*1){DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"),"账户余额不足");return false;}
			var params = self.$el.getFormValue();
			 if(self.checkedParams(params.amount))return false;
           	 self.controller.userModel.withdraw({
					'data': params,'cancelLightbox': false,"noCache": true,
					"success": function(response) {	
						if(response.code=="000000"&&DMJS.userInfo.tg){
							Native.openChildWebActivity(response.data,{title:"第三方"});
						}else{
									DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"),response.description,function(){
									DMJS.router.navigate("user/personal/user",true);});
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
						}else
						DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"), response.description);
						}
					});
		},
		checkedParams:function(amount){
			var self=this;
			if(self.data.txkfType=="true" && amount<=parseInt($("#txsxf").html())){
				DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"), "提现金额应大于提现手续费金额");
				return true;
			}
			if(amount<self.data.min*1||amount>self.data.max*1){
				
				DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"), "提现金额不能低于"+self.data.min+"元，且不能高于"+tool.gaibAmount(self.data.max,"元",1));
			   return true;
			}
			return false;
		},
		cardList:function(callback){
           	var self=this;
           	 self.controller.userModel.myBankList({
					'data': {},'cancelLightbox': false,"noCache": true,
					"success": function(response) {	
						self.canCashAmt=response.data.canCashAmt;
						_.extend(self.data,response.data);
						if(response.data.myBankList.length<=0){
							DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"), "请先绑卡",function(){
								DMJS.router.navigate("user/payment/cardManage",true);
								DMJS.CommonTools.hash_clear();
							});
							return false;
						}
						
						 callback.apply(self,[]);//
					
					},
					'error': function(response) {DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"), response.description);}
					});
           	   
          },
          addCard:function(){
        	  var self=this;
             	self.controller.userModel.addCard({'data':null,'cancelLightbox':true,
             	"noCache":true,
     			"success":function(response){
     				if(response.data.url){
     					Native.openChildWebActivity(response.data.url,{title:"第三方"});
     				}
     			},
     			'error':function(response){DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"), response.description);}});
          },
            calculateFees:function(e){
			var self=this;
        	var __val=$(e.target).val();
  	     	__val=__val.replace(/[^\d\.]/g,"").replace(/\.{2,}/,".").replace(/^[0|\.]/g,'');
  	   		if(__val.split(".").length>=2){
      		var __vals=__val.split(".");
      		var __vals_last=__vals[0];
      		var xhd=__vals[1];
      		__val=__vals_last+"."+xhd.substring(0,2)
  	  		 }
             amount=__val*1;
           	 $(e.target).val(__val);
           	 if(amount<=0){
           		$("#paySum").html('0.00');//实际金额
           	    $("#txsxf").html('0.00');
           	    return ;
           	    return false;
           	 }
           	 if(self.canCashAmt<amount){
           	 	amount=self.canCashAmt;
           	 	 $(e.target).val(amount);
           	 }
           	 self.feet(amount);
           	 //双乾
//           	 self.sqfeet(amount);
       	  var dateD=new Date();//提现到账日期加1
      	  if(amount>0){
                dateD.setDate(dateD.getDate()+1);
      	  }
      	  var deductD=dateD.toLocaleDateString();
          $('#tiem').html(deductD.replace(/\//g,"-"));
            
        },
        
        selectTip:function(id,selectId,list){
			var self=this;
			var optionVals=[],optionTexts=[];
			optionVals.push("GENERAL");optionTexts.push("普通提现 (下一个工作日到账)");
			optionVals.push("FAST");optionTexts.push("快速提现 (最迟下一个工作日到账)");
			optionVals.push("IMMEDIATE");optionTexts.push("即时提现 (实时到账)");
        	DMJS.CommonTools.showList(
            		{'inputID':"withdrawWay",'id':"selectWithdrawType",
            			"optionText":optionTexts,"optionVals":optionVals}
            ,function(){
            	var __value=self.$el.find("#selectWithdrawType").val();
            	for(var i=0;i<optionVals.length;i++){
            		if(optionVals[i]==__value){
            			self.$el.find("#withdrawWay").html(optionTexts[i]);
            		}
            	}
            	self.calculateFees();
            });
        	$("#selectWithdrawType").val("GENERAL");
        	$("#withdrawWay").val("普通提现 (下一个工作日到账)");
	             
		},
        feet:function(amount){
        	var self=this,fee=self.data,bl=fee.proportion,p=fee.poundage1,pMax=fee.poundage2,way=fee.way,txkfType=fee.txkfType,txsxf;
        	  if(way=="ED"){
        		if(amount >= 1 && amount < 201){
        			if(p*1 === 0){
        				txsxf=2;
        			}else{
        				txsxf=(0.008 * amount)+2;
        			}
        		}else{
        			amount>=50000?txsxf=(pMax*1+2):txsxf=(p*1+2);
        		}
        	  }else{
        	  	txsxf=self.acculatePoundage(bl*amount, 2)+2;
        	  }
        	  // + 2 是第三方手续费，因不能计算得出，所以写死为2元一笔 
        	  txsxf = 2;
        	 // if(amount-txsxf<=0) return false;
        	 //内扣 提现金额中扣  外扣   提现金额+上手续费=支付总额
        	 if(txkfType=="true")$("#paySum").html((amount*1).toFixed(2));//实际金额
        	 if(txkfType=="false")$("#paySum").html((amount*1+txsxf*1).toFixed(2));//实际金额
        	 $("#txsxf").html((txsxf*1).toFixed(2));
        	  
        },
        acculatePoundage:function(amount, len) {
        	var add = 0;
        	var s1 = amount + "";
        	var start = s1.indexOf(".");
        	if(start == -1) {
        		return amount;
        	}
        	if(s1.substr(start + len + 1, 1) >= 5) {
        		add = 1;
        	}
        	var t = Math.pow(10, len);
        	var s = Math.floor(amount * t) + add;
        	return s/t;
        },
        //双乾托管放开以下注释
         sqfeet:function(amount){
        	var self=this,txsxf;
        	if(self.data.way=="ED"){
        		amount>=50000?txsxf=self.data.poundage2:txsxf=self.data.poundage1;
        	}else if(self.data.way=="BL"){
        		var withdrawRate = self.data.sqFee.withdrawRate;
	        	txsxf = amount*withdrawRate;
	        	txsxf<self.data.withdrawMinFee?(txsxf=self.data.withdrawMinFee):txsxf;
        	}
        	$("#paySum").html((amount*1).toFixed(2));//实际金额
        	$("#txsxf").html((txsxf*1).toFixed(2));
        },
           getFeet:function(){
        	var self=this;
        	  self.controller.userModel.fee({
					 async: true,'cancelLightbox':true,
                     "success":function(response){
                     _.extend(self.data,response.data.withdrawp);
                     self.$el.html(_.template(withdrawTemplate, self.data)); 
    	 			 if(response.data.withdrawp.txkfType=="true"){$("#txts").html(" (实际手续费以第三方为准)");}
	    	 		if(response.data.withdrawp.txkfType=="false"){$("#txts").html(" (实际手续费以第三方为准)");}//实际金额
	    	 		self.selectTip();
                    self.loadScroller();
                     self.myAccount();
				 	 DMJS.CommonTools.imgDown.apply(self,[$('img')]);
                      //双乾托管打开以下注释
//                     self.data.sqFee = response.data.sqFee;
                    $("#amount").attr('validator',"val["+self.data.min+","+self.data.max+"]");
							},
					 	'error':function(response){
								DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"),response.description);
					 }
                    });
        }
		
	});

	return withdrawView;
});
