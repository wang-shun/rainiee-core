define(['text!userTemplate/personal/riskInvestment.html','commonTool/tool','commonTool/validator'
], function(riskInvestmentTpl, tool,Validator) {
    var riskInvestmentView = DMJS.DMJSView.extend({
        id: 'riskInvestment',
        name: 'riskInvestment',
        tagName: 'div',
        className: "riskInvestment",
        events: {
        	'tap #Janswer':'Janswer'
        },
        init: function(options) {
            var self = this;
            _.extend(self, options);
        },
        render: function() {
            var self = this;
            self.controller.userModel.riskQuestions({//评估问题列表
            	'cancelLightbox':true,
        		"noCache":true,
				"success":function(response){
					self.data=response.data;
					self.$el.html(_.template(riskInvestmentTpl, self));
            		self.loadScroller();
				},
				'error':function(response){
				
					DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"), response.description);
				},
				"complete":function(){
	                                 
	            }
        	});
            
            return this;
        },
        'Janswer':function(e){
        	var $dom=$(e.target);
        	if($dom.parents('div.Joption').length>0){
        		$dom=$dom.parents('div.Joption');
        	}
        	if(!!$dom.is('.Joption')){
        		$dom=$dom;
        	}else{
        		return false;
        	}
        	$dom.siblings().removeClass('lfx-gou');
        	$dom.addClass('lfx-gou');
        	var answerId=$dom.siblings('.JanswerId').attr('questionId');
        	var option=$dom.attr('Joption');
        	var answerOptioin=answerId+'_'+option;
        	$dom.siblings('.JoptionAnswer').val(answerOptioin);
        },
        'submit':function(){
        	var self=this,data=$('.JoptionAnswer'),params=[];
        	if(!Validator.check($("#"+self.id))) {return false};
        	for(var i=0;i<data.length;i++){
        		params.push(data[i].value);
        	}
        	params=params.join(',');
        	self.controller.userModel.riskAssessment({//答题
            	'cancelLightbox':true,
        		"data":{'answer':params},
				"success":function(response){
					var data=response.data;
						DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"), response.description);
					DMJS.CommonTools.alertCommon('评估结果','评估完成，您的得分为'+data.score+'分，您的风险承受能力为：'+data.riskType,function(){
						DMJS.router.navigate("user/personal/userInfo", true);
					});
				},
				'error':function(response){
				
					DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"), response.description);
				},
				"complete":function(){
	                                 
	            }
        	});
        }
    });
    return riskInvestmentView;
});
