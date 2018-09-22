define([ 'text!userTemplate/personal/login.html',
		'commonTool/validator', 'common/Context','commonTool/tool',"commonClass/weixin" ], function(loginTemplate,Validator, appContext,tool,weixin) {
	var loginView = DMJS.DMJSView.extend({
		id : 'loginviewContent',
		name : 'loginviewContent',
		tagName : 'div',
		className : "loginviewContent",
		events : {
			
		},
		init : function(options) {
			_.extend(this, options);
			DMJS.userInfo=undefined;//重置前端缓存用户信息
			weixin.init();//初始化微信设置
		},
		render : function() {
			var self = this;
			DMJS.userInfo=undefined;
			DMJS.CommonTools.delCookies();
        	//tool.hash_clear();
			this.$el.html(_.template(loginTemplate, self)); // 将tpl中的内容写入到
			return this;
		},
		//密码查看
        "lookPwd":function(e){
        	var pwd = $('#password');
        	if(e.hasClass("b-image-hide")){
        		pwd.attr("type","text");
        		e.removeClass("b-image-hide").addClass("b-image-show");
        	}else{
        		pwd.attr("type","password");
        		e.removeClass("b-image-show").addClass("b-image-hide");
        	}
        },
		doLogin : function(sucCaller,time) {
			var self = this;
			if(!!!time) {
				DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"),"获取时间戳出错!");
				return false;
			}
			
			var params = _.extend(self.$el.getFormValue(), self.data);
			if (escape(params['password']).indexOf("%u")>=0){
				DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"),"密码不能含有中文字符");
				return false;
			}
			params['time']=time;
			if(DMJS.weixinId){
				params['weixinId']=DMJS.weixinId;//用户openid，空值则不绑定注册
			}
			var _caller_ = function() {
				if(!$("input[name=password]").val()||$("input[name=password]").val()==""){return;}
				self.controller.userModel.login({
					"data" : params,'cancelLightbox':true,
					"success" : function(response) {
						sucCaller(response);
					},
					"logicError" : function(response) {
						if (response && response.description) {
							DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"),
									response.description);
							$("input[name='password']").val("");
						}
					}
				});
			};
			if (DMJS.weixinId) {
				setTimeout(_caller_, 300);
			} else {
				delete params.weixinId;
				_caller_();
			}
		},
		login : function() {
			var self=this;
			var nameUse=$("#userName").val();
		   if(nameUse.indexOf(' ')!=-1){
			  DMJS.CommonTools.alertCommon("温馨提示","用户名错误"); 
        	  return false;
		    }
			if(!Validator.check($("#"+self.id))) {return false}
				self.timing()
		
	},
	timing:function(){
		var self = this;
		self.controller.commonModel.timing({
					"data" : {},'cancelLightbox':true,
					"success" : function(response) {
					self.doLogin(function(_data) {
					DMJS.userInfo = _data.data;
					weixin.init();
					var userInfo = JSON.stringify(DMJS.userInfo);
					userInfo = encodeURI(userInfo); 
					document.cookie="userInfo="+userInfo; 
					//window.listHash.length>0 ? window.listHash.splice(0,window.listHash.length) : null;
					var list=window.listHash;
					//替换掉登录的地址，防止ios设备微信自带回退按钮回退登录界面
					window.history.replaceState(null,null,window.location.origin+window.location.pathname+"#index/index/index");
					if(list.length>1){
						DMJS.router.navigate(list[list.length-1], true);
					}else{
						DMJS.router.navigate("user/personal/userInfo", true);
					}
					
					
			},response.data.time);
					},
					"error" : function(response) {
							DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"),response.description);
					}
				});
	},
	forgetPassword:function(e){
        	//DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"),"请访问WEB版功能找回您的密码！");
        	DMJS.router.navigate("user/personal/getLoginPWD",true);
        }
		});

	return loginView;
});
