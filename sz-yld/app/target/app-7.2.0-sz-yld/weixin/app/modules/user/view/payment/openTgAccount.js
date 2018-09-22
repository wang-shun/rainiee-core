define(['text!userTemplate/payment/openTgAccount.html', 'commonTool/tool','commonTool/validator'], function(openTgAccountTemplate, tool,Validator) {
	var openTgAccountView = DMJS.DMJSView.extend({
		id: 'openTgAccountView',
		name: 'openTgAccountView',
		tagName: 'div',
		className: "openTgAccountView",
		events: {

		},
		data: {},

		init: function(options) {
			_.extend(this, options);
		},
		render: function() {
			var self = this;
			self.$el.html(_.template(openTgAccountTemplate, self.data));
			self.loadScroller();
			return this;
		},
		
		toOpenTg:function(){
			var self = this;
			var paraData=self.$el.getFormValue();
			if (!Validator.check($("#"+self.id))) {return false}
			self.controller.userModel.payUserRegister({
				'data': paraData,
				'cancelLightbox': false,
				"noCache": true,
				"success": function(response) {
					 Native.openChildWebActivity(response.data,{title:"账 号 开 户"});
				},
				'error': function(response) {
					DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"), response.description);
				}
			});
		},
		getMobileCode: function(e) {
			var self = this;
			var timee=document.getElementById('yzm_count');
			var yzm=document.getElementById('yzm');
			var yzm_info=document.getElementById('yzm_info');
			if (!Validator.check($("#phone"))) {return false}
				self.controller.userModel.getMobileCode({
					'data': {phone: $("#phone").val(),type:"RZKH"},'cancelLightbox': false,"noCache": true,
					"success": function(response) {
						yzm.style.display="none";
						yzm_info.style.display="block";
						self.reckTime(timee,yzm,yzm_info);
						DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"), "验证码已发送,请查收！");
					},
					'error': function(response) {
						DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"), response.description);
					}});
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

	});

	return openTgAccountView;
});