define(['text!userTemplate/personal/unautherized.html','commonTool/validator',
], function(unautherizedTemplate,Validator) {
    var unautherizedView = DMJS.DMJSView.extend({
        id: 'setViewContent',
        name: 'setViewContent',
        tagName: 'div',
        className: "setViewContent",
        events: {
        	"tap #setUserInfo":'setUserInfo',
        	"tap #setEmail":'setEmail',
        	"tap #setTranPwd":'setTranPwd',
        	'tap #phoneSave':'setPhone',
        	'tap #basicInfo':'basicInfo_in'
        },
        init: function(options) {
        	var self = this;
            _.extend(self, options);
        },
        render: function() {
            var self = this;
            this.noDestroy=false;
        	self.userInfo(self.Reg);
            return this;
        },
        //密码正则
        Reg:function(){
        	var self=this;
        	self.controller.userModel.REGEX({'data':{},'cancelLightbox':true,
			"noCache":true,"success":function(response){
					 _.extend(self.data, response.data);
					 self.safetyLevel();
                    self.$el.html(_.template(unautherizedTemplate, self.data));
                     self.loadScroller();
			},'error':function(response){
				DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"), response&&response.description);
			}
        	});
        },
     /*  basicInfo_in:function(e){
       
       	$('#basicInfo').find("input,select").each(function(){
        			$(this).blur();
            	});
       },*/
        //计算等级
      safetyLevel:function(){
      	 var self=this;
      	 var arr = new Array();
      	 arr.push(self.data.mobileVerified);arr.push(self.data.idcardVerified);arr.push(self.data.emailVerified);arr.push(self.data.withdrawPsw);
		 var newArr = arr.filter(function(item){
		 		 return item==true;
		  });
		  self.data.safeLevelTxt="高";
		  switch(newArr.length){
			  case 0:
			  	   self.data.safeLevel = 0;
				   self.data.safeLevelTxt="低";
			       break;
			  case 1: 
			      self.data.safeLevel = 33;
				  self.data.safeLevelTxt="低";
			       break;
			  case 2:
			  	   self.data.safeLevel = 67;
			 	   self.data.safeLevelTxt="中";
			       break;
			  case 3:
			       if(self.data.tg){
			       	  self.data.safeLevel = 100;
			 	      self.data.safeLevelTxt="高";
			       }else{
			       	 self.data.safeLevel = 67;
			 	     self.data.safeLevelTxt="中";
			       }
			       break;
			  case 4:
			       self.data.safeLevel = 100;
			 	   self.data.safeLevelTxt="高";
			       break;
			   default:break;
			 }
     },
      //显示隐藏
      switchDiv:function(id , h,$dom){
          if($("#"+id).css("display") == "none"){ 
              $("#"+id).show();
              $dom.text("取消");  
           
          }else{
               $("#"+id).hide();
              $dom.text("设置");
              $("#"+id+" input[type='text']").val("");
          }
          this.loadScroller();
      },
      userInfo:function(callback){
            var self = this;
            self.controller.userModel.userInfo({
                "noCache": true,
                cancelLightbox: true,
                "success": function(_data) { 
                    _.extend(self.data, _data.data);
                  //  self.$el.html(_.template(unautherizedTemplate, self.data)); // 将tpl中的内容写入到 this.el 元素中  
                   // self.loadScroller();
                    callback.apply(self,[]);
                },
                'error':function(response){DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"), response&&response.description);}
            });
        
      },
      setUserInfo:function(e){
      	var self=this;
      	var id="userinfoForm";
      	if(!Validator.check($("#"+id))) {return false}
       	var params = $("#"+id).getFormValue();
            self.controller.userModel.setUserInfo({data:params,noCache: true,cancelLightbox: true,
                "success": function(_data) { 
                   DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"), "实名认证成功！",function(){
                		self.reflush();
                	});
                },
                'error':function(response){DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"), response&&response.description);}
            });
      },
      setEmail:function(){
      	var self=this;
      	var id="emailForm";
      	if(!Validator.check($("#"+id))) {return false}
       	var params = $("#"+id).getFormValue();
       		params['type']="RZ"
            self.controller.userModel.getEmailCode({data:params,noCache: true,cancelLightbox: true,
                "success": function(_data) { 
                	DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"), "邮件已发送到您的邮箱,请查收！",function(){
                		self.reflush();
                	});
                },
                'error':function(response){DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"), response&&response.description);}
            });
      },
       //密码查看
        "lookPwd":function(e){
        	var pwd = $('#onePwd');
        	if(e.hasClass("b-image-hide")){
        		pwd.attr("type","text");
        		e.removeClass("b-image-hide").addClass("b-image-show");
        	}else{
        		pwd.attr("type","password");
        		e.removeClass("b-image-show").addClass("b-image-hide");
        	}
        },
      setTranPwd:function(){
      	var self=this;
      	var id="passwdForm";
      	if(!Validator.check($("#"+id))) {return false}
       	var params = $("#"+id).getFormValue();
       	params['twoPwd'] = params['onePwd'];
            self.controller.userModel.setTranPwd({data:params,noCache: true,cancelLightbox: true,
                "success": function(_data) { 
                	DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"), "设置交易密码成功！",function(){
                		self.reflush();
                	});
                },
                'error':function(response){DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"), response&&response.description);}
            });
     
      },
       setPhone:function(){
      	var self=this;
      	var id="phoneForm";
      	if(!Validator.check($("#"+id))) {return false}
      	self.checkCode(function(){
       	var params = $("#"+id).getFormValue();
      	 	params['type']="FXRZ";
      	 	params['phoneCode']=$("#verifyCode").val();
            self.controller.userModel.setPhone({data:params,noCache: true,cancelLightbox: true,
                "success": function(_data) { 
                	DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"), "设置成功！",function(){
                		self.reflush();
                	});
                },
                'error':function(response){DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"), response&&response.description);}
            });
      	})
      },
      checkCode:function(callback){
      	var self = this;
			var parms = $("#phoneForm").getFormValue();
			parms['type'] = "FXRZ";
			if (Validator.check($("#verifyCode"))) {
				self.controller.userModel.checkVerifyCode({
					'data': parms,
					'cancelLightbox': false,
					"noCache": true,
					"success": function(response) {
						callback.apply(self,[]);
					},
					'error': function(response) {
						DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"), response.description);
					}
				});
			}
      },
	getMobileCode: function(e) {
			var self = this;
			var timee=document.getElementById('yzm_count');
			var yzm=document.getElementById('yzm');
			var yzm_info=document.getElementById('yzm_info');
			if (!Validator.check($("#phone"))) {return false}
				self.controller.userModel.getMobileCode({
					'data': {phone: $("#phone").val(),type: "FXRZ"},'cancelLightbox': false,"noCache": true,
					"success": function(response) {
						yzm.style.display="none";
						yzm_info.style.display="block";
						self.reckTime(timee,yzm,yzm_info);
						DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"), "验证码已发送,请查收！");
					},
					'error': function(response) {DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"), response.description);}});
		},    
		reckTime :function(obj,yzm,yzm_info){
			var times=60;
			var sid=setInterval(function(){
					times=times-1;
					obj.innerHTML=times;
					if(times==0){
						clearInterval(sid);
						yzm.style.display="block";
						yzm_info.style.display="none";
						obj.innerHTML=60;
					}
					console.log(times);
				},1000);
			window.threadId=sid;
			},
			destroy:function(){
			var self=this;
			//clearInterval(window.threadId);
			//delete window.threadId;
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
    return unautherizedView;
});
