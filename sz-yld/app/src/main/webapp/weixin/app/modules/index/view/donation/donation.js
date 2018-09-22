define(['text!indexTemplate/donation/donation.html','commonTool/validator',], function(donationTemplate,Validator){
    var donationView = DMJS.DMJSView.extend({//项目投资
        id: 'donationContent',
        name: 'donationContent',
        tagName: 'div',
        className: "donationContent", 
        events: {
           "input #amount":"calculateFees" ,
        },
        init: function(options){
           	_.extend(this,options); 
        },
        render: function(){
           var self=this; 
           self.myAccount();
           return this;
        },
        myAccount:function(){
        	var self=this;
        	self.controller.commonModel.myAccount({
                "noCache": true,
                cancelLightbox: true,
                "success": function(response) { 
                    _.extend(self.data, response.data);
                    self.$el.html(_.template(donationTemplate, self.data)); // 将tpl中的内容写入到 this.el 元素中  
                    DMJS.agreementEnable('GYB',"agreementFlag");
                    self.el.children[0].style.height=wrapView.height*1+100+"px";//让高度多出100 避免虚拟键盘遮住输入框
                   // self.loadScroller();
                },
                 'error':function(response){DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"), response&&response.description);}
            });
        },
        buy:function(){
        	var self=this;
        	if(!Validator.check(self.$el)) {return false}
        	if($("#amount").val()*1>self.data.overAmount*1){
        		wrapView.FlipPrompt.confirm({    //提示chongzhi
               				title: "",
               				content: '账户余额不足！',
               				FBntconfirm: "充值",
               				FBntcancel: "取消",
               				FBntConfirmColor: "pop_btn_orange",
               				autoCloseBg:"false",
               			}, function() {
               				DMJS.router.navigate("user/payment/recharge", true);         				
               			}, function() {

               			});
        		return ;
        		//DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"),"账户余额不足");return false;
        	}
        	if($("#amount").val()*1<self.data.donationsMinAmount*1){
        		var min = self.data.donationsMinAmount.toFixed(2);
          		DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"),"您的捐助金额小于起捐金额"+min+"元");
          		$("#amount").val(min);
          		return false;
        	}
        	var parms=self.$el.getFormValue();
        		parms['loanId']=self.data.donationId;
        	self.controller.indexModel.gyLoanBid({
                data:parms,"noCache": true,cancelLightbox: true,
                "success": function(response) { 
                	if(response.data.url){
                		Native.openChildWebActivity(response.data.url,{title:"第三方"});
                	}else{
                		DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"), '捐助成功!',function(){
                			DMJS.router.navigate("index/index/donationBenefit", true);
                			
                		});
                	}
                },
                 'error':function(response){
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
                	 DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"), response&&response.description);
                	 
                 }
            });
        },
        calculateFees:function(e){
			var self=this;
        	var __val=$(e.target).val();
  	     	__val=__val.replace(/[^\d\.]/g,"").replace(/\.{2,}/,".");//.replace(/^[0|\.]/g,'')
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
            // $(e.target).val(__val);
           	 if(self.data.donationsAmount*1<__val*1){
           	 	$("#amount").val(self.data.donationsAmount*1);
           	 }
           	/* else if(self.data.overAmount*1<__val*1){
           	 	$("#amount").val(self.data.overAmount*1);
           	 }*/
           	 else{
           	 	$(e.target).val(__val);
           	 }
        },
        "turnRememberRido": function(e) {
			if ($("input[type='checkbox']").is(":checked")) {
				$("input[type='checkbox']").removeAttr("checked");
			} else {
				$("input[type='checkbox']").attr("checked", true);
			}
		},
        agreement: function(type) {
			this.noDestroy = true;
			DMJS.router.navigate("user/personal/agreement/"+type, true);
		},
    });

    return donationView;
});



