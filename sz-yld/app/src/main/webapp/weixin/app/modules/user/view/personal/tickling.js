define(['text!userTemplate/personal/tickling.html'
], function(ticklingTemplate) {
    var ticklingView = DMJS.DMJSView.extend({
        id: 'ticklingContent',
        name: 'ticklingContent',
        tagName: 'div',
        className: "ticklingContent",
//      repeatedlyClick:0,
        events: {
        	'input #feedContent':'feedContent'
        },
        init: function(options) {_.extend(this, options);},
        render: function() {
            var self = this;
            this.noDestroy=false;
           	this.$el.html(_.template(ticklingTemplate,{}));
           	self.loadScroller();
            return this;
        },
        feedContent:function(e){
        	var $dom=$(e.target).val();
        	if($dom.length>150){
        		$('#feedContent').val($dom.substring(0,150))
        	}
        },
        tickling:function(){
          	var self=this;
//  		if(self.repeatedlyClick!=0&&(new Date).getTime()-self.repeatedlyClick<500){
//			   return false;
//		    }
//      	self.repeatedlyClick = (new Date).getTime();
        	var content=$('#feedContent').val();
        	if(content.length<=0){
        		DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"),'内容不能为空！');
        		return false;
        	}
        	
        	$('#submit').removeAttr("action");
        	$('#feedContent').blur();
        	
        	self.controller.userModel.tickling({
        		data:{content:content},
                "noCache": true,
                cancelLightbox: true,
                "success": function(response) { 
                	console.log("success");
                	DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"),'提交成功，感谢您的反馈！',function(){
                		DMJS.CommonTools.hash_clear();
                		DMJS.router.navigate("user/personal/ticklingList", true);
                	});
                },
                 'error':function(response){$('#submit').attr("action","action:tickling");DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"), response&&response.description);}
            });
        
        },
        
    });
    return ticklingView;
});
