
define(['userModel/UserModel',
        'commonController/MainController',
        'commonTool/tool'
       ], function(UserModel, MainController,tool){
    var PasswordController = MainController.extend({
    	

        module: 'user',
        name: 'password',
        actions: {
           'passwordList':'passwordList',//密码管理
           'loginPwdUpdate':'loginPwdUpdate',//更新密码
           'loginPwdCode':'loginPwdCode',//发送找回密码的验证码
           'loginPwd/:id':'loginPwd',//找回密码
           'tranPwdCode':'tranPwdCode',//发送找回交易密码的验证码
           'tranPwd/:id':'tranPwd',//找回交易密码
           'tranPwdUpdate':'tranPwdUpdate',//更新交易密码
        },
        init: function(){
        	this.userModel = new UserModel();
        	
        },
        passwordList:function(){
        	
    	   var self=this;
	       self.setHeader({
	       		"left":{
	       			"html":"返回",
	       			"func":function(){ 
	       			 tool._Navi_default('user/personal/login');
	       			}
	       		},
	       		"title":"密码管理" 
	       }); 
	       self.setFoot({
	        	"key":"none"
	       });
	       require(['userView/password/passwordList'], function(passwordList){
	    	   self.setContent(passwordList,{
	       			"controller":self,
	       			"data":{
	       			}
	    		}).render();
	       });
       
        },
        loginPwdUpdate:function(){
    	   var self=this;
	       self.setHeader({
	       		"left":{
	       			"html":"返回",
	       			"func":function(){ 
	       			 tool._Navi_default('user/personal/login');
	       			}
	       		},
	       		"title":"修改登录密码" 
	       }); 
	       self.setFoot({"key":"none"});
	       require(['userView/password/loginPwdUpdate'], function(loginPwdUpdate){
	    	   self.setContent(loginPwdUpdate,{
	       			"controller":self,
	       			"data":{
	       			}
	    		}).render();
	       });
        },
       loginPwdCode:function(){
        	var self=this;
	       self.setHeader({
	       		"left":{
	       			"html":"返回",
	       			"func":function(){ 
	       			 tool._Navi_default('user/personal/login');
	       			}
	       		},
	       		"title":"找回登录密码" 
	       }); 
	       self.setFoot({"key":"none"});
	       require(['userView/password/loginPwdCode'], function(loginPwdCode){
	    	   self.setContent(loginPwdCode,{
	       			"controller":self
	    		}).render();
	       });
        
       },
        loginPwd:function(phone){
        	var self=this;
	       self.setHeader({
	       		"left":{
	       			"html":"返回",
	       			"func":function(){ 
	       			 tool._Navi_default('user/personal/login');
	       			}
	       		},
	       		"title":"设置登录密码" 
	       }); 
	       self.setFoot({"key":"none"});
	       require(['userView/password/loginPwd'], function(loginPwd){
	    	   self.setContent(loginPwd,{
	       			"controller":self,
	       			data:{phone:phone}
	    		}).render();
	       });
        },
         tranPwdCode:function(){
        	var self=this;
	       self.setHeader({
	       		"left":{
	       			"html":"返回",
	       			"func":function(){ 
	       			 tool._Navi_default('user/personal/userInfo');
	       			}
	       		},
	       		"title":"找回交易密码" 
	       }); 
	       self.setFoot({"key":"none"});
	       require(['userView/password/tranPwdCode'], function(tranPwdCode){
	    	   self.setContent(tranPwdCode,{
	       			"controller":self
	    		}).render();
	       });
        
       },
        tranPwd:function(phone){
        	var self=this;
	       self.setHeader({
	       		"left":{
	       			"html":"返回",
	       			"func":function(){ 
	       			 tool._Navi_default('user/personal/userInfo');
	       			}
	       		},
	       		"title":"设置交易密码" 
	       }); 
	       self.setFoot({"key":"none"});
	       require(['userView/password/tranPwd'], function(tranPwd){
	    	   self.setContent(tranPwd,{
	       			"controller":self,
	       			data:{phone:phone}
	    		}).render();
	       });
        },
        tranPwdUpdate:function(){
        	var self=this;
	       self.setHeader({
	       		"left":{
	       			"html":"返回",
	       			"func":function(){ 
	       			 tool._Navi_default('user/password/passwordList');
	       			}
	       		},
	       		"title":"修改交易密码" 
	       }); 
	       self.setFoot({"key":"none"});
	       require(['userView/password/tranPwdUpdate'], function(tranPwdUpdate){
	    	   self.setContent(tranPwdUpdate,{
	       			"controller":self,
	    		}).render();
	       });
        },

    });
    return PasswordController;
});
