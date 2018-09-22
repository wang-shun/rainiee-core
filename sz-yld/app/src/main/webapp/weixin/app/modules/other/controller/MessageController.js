
define(['otherModel/OtherModel',
        'commonController/MainController',
        'commonTool/tool'
       ], function(OtherModel, MainController,tool){
    var otherController = MainController.extend({

        module: 'other',
        name: 'Message',
        actions: {
            'messageList': 'messageList',//站内信
        },
        init: function(){
        	this.otherModel = new OtherModel();
        },
      //站内信
       'messageList':function(){
    	   var self=this;
	       self.setHeader({
	       		"left":{
	       			"html":"返回",
	       			"func":function(){
	       				tool._Navi_default('user/personal/userInfo');
	       				}
	       		},
	       		"title":"站内信",
	       		
	       });
	       self.setFoot({
	        	"key":"none"
	        });
	       require(['otherView/message'], function(message){
	    		   self.setContent(message,{
		       			"controller":self,
		       			"data":{
		 	       			
	 	       			}
		    		}).render();
	       });
       },
       
       
       
       
       
    });
    return otherController;
});
