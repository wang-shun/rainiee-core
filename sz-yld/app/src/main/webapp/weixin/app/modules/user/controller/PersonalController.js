
define(['userModel/UserModel',
        'commonController/MainController',
        'commonTool/tool'
       ], function(UserModel, MainController,tool){
    var PersonalController = MainController.extend({
    	
        module: 'user',
        name: 'personal',
        actions: {
            'login(/:info)': 'login',//登录
            'binLogin(/:weixinId)': 'binLogin',//绑定登录
            'userInfo(/:accountId)':'userInfo',//我的账户
            'register(/:info)':'register',//注册
            'registerInfo/:id':'registerInfo',//注册信息
            'agreement/:id':'agreement',//注册协议
            'myReward':'myReward',//我的奖励
            'experience':'experience',//体验金
            'experienceInfo/:id/:status':'experienceInfo',//体验金详情
            'raise':'raise',//加息劵
            'redPacket':'redPacket',//红包
            'cashTransfer':'cashTransfer',//汇款计划
            'set' : 'set',//设置
            'unautherized':'unautherized',//安全信息认证（未认证）
            'cashTransfer':'cashTransfer',//汇款计划
            'myInvestment(/:type)':'myInvestment',//我的投资
            'tranLog':'tranLog',//交易记录
            'myLoan':'myLoan',//我的借款
            'viewContract/:id(/:typeS)':'viewContract',//查看合同
            'myLoanInfo/:id':'myLoanInfo',//查看合同
            'help':'help',//新手入门列表
            'helpInfo/:id/:type':'helpInfo',//新手入门详情
            'riskInvestment':'riskInvestment',//风险评估
          	'ticklingList':'ticklingList',//反馈列表
          	'tickling':'tickling',//反馈
            'myIntegral':'myIntegral',//我的积分
          	'integralRule':'integralRule',//积分规则
          	'myOrder':'myOrder',//我的订单
          	'orderDetail/:id':'orderDetail',//订单详情
          	'exchangeIntegral':'exchangeIntegral',//兑换记录
          	

        },
        init: function(){
        	this.userModel = new UserModel();
        	
        },
        'login':function(info){
    	   var self=this;
	       self.setHeader({
	       		"left":{
	       			"html":"返回",
	       			"func":function(){ 
	       			 tool._Navi_login('index/index/index'); 
	       			}
	       		},
	       		"title":"登录" 
	       }); 
	       self.setFoot({
	        	"key":"none"
	       });
	       require(['userView/personal/loginView'], function(loginView){
	    	   self.setContent(loginView,{
	       			"controller":self,
	       			"info":info,
	    		}).render();
	       });
       },
        
       'binLogin':function(weixinId){
    	   var self=this;
    	   var url = weixinId?weixinId:""; //获取url中"?"符后的字串
			var theRequest = new Object();
			if (url.indexOf("?") != -1&&!DMJS.weixinId) {//绑定微信号才进行的操作
				var str = url.substr(1);
				strs = str.split("&");
				for(var i = 0; i < strs.length; i ++) {
					theRequest[strs[i].split("=")[0]]=(strs[i].split("=")[1]);
				}
				DMJS.Request("bindCallBackServlet",{//获取用户的openId
					data: {
						"code": theRequest['code'],
						"state":theRequest['state'],
					},
					type: "get",
					dataType: "jsonp",
					cancelLightbox: true,
					"noCache":true,
					"success": function(_data) {//授权成功并绑定过账户
						DMJS.weixinId = _data.data;//用户Openid
						if(_data.code!="000000" && _data.code!="000059"){
							DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"), _data.description);
							DMJS.router.navigate("user/personal/login", true);
						}else{
							if(_data.code=="000059"){
								weixinId = DMJS.weixinId;
								
								if(weixinId){
									self.setHeader({
									       		"left":{
									       			"html":"返回",
									       			"func":function(){ 
									       			 tool._Navi_login('index/index/index'); 
									       			}
									       		},
									       		"title":"登录" 
									       }); 
									       self.setFoot({
									        	"key":"none"
									       });
									       require(['userView/personal/loginView'], function(loginView){
									    	   self.setContent(loginView,{
									       			"controller":self,
		       										"info":"",
									       			"data":{
									       				"weixinId":weixinId
									       			}
									    		}).render();
									       });
								}
							}else{
								DMJS.userInfo = _data.data;
								var userInfo = JSON.stringify(DMJS.userInfo);
								userInfo = encodeURI(userInfo); 
								document.cookie="userInfo="+userInfo; 
								//window.listHash.length>0 ? window.listHash.splice(0,window.listHash.length) : null;
								var list=window.listHash;
								if(list.length>1){
									DMJS.router.navigate(list[list.length-1], true);
								}else{
									DMJS.router.navigate("user/personal/userInfo", true);
								}
							}
						}
					},
					error:function(response){//授权成功未做平台账户绑定
						DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"), response.description);	
					}
				});
			}else{
		       self.setHeader({
		       		"left":{
		       			"html":"返回",
		       			"func":function(){ 
		       			 tool._Navi_login('index/index/index'); 
		       			}
		       		},
		       		"title":"登录" 
		       }); 
		       self.setFoot({
		        	"key":"none"
		       });
		       require(['userView/personal/loginView'], function(loginView){
		    	   self.setContent(loginView,{
		       			"controller":self,
	       				"info":"",
		    		}).render();
		       });
	       }
       },
       'userInfo':function(accountId){
       		var self=this;
       		if(accountId){
       			DMJS.userId = accountId;
       			DMJS.Request("weixin.wXAccountLogin",{//微信accountId自动登录
					data: {
						"accountId": accountId,
					},
					type: "get",
					dataType: "jsonp",
					cancelLightbox: true,
					"noCache":true,
					"success": function(_data) {
						if(_data.description.indexOf("被锁定")>-1||_data.description.indexOf("最大错误限制数")>-1){
							DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"), _data.description);
							setTimeout(function(){
								if(DMJS.CommonTools.isWeiXin()){//微信走这里
	                    			Native.exit();
	                    		}else{//方便微信测试
	                    			DMJS.userInfo = undefined;
			                        DMJS.removeCache("user.myAccount");
			                        DMJS.router.navigate("user/personal/login", true);
			                        DMJS.CommonTools.delCookies();
	                    		}
                    		},1000);
							return;
						}
						if(_data.code=="000000"){
							DMJS.userInfo = _data.data;
							var userInfo = JSON.stringify(DMJS.userInfo);
							userInfo = encodeURI(userInfo); 
							document.cookie="userInfo="+userInfo; 
						}
						
					       self.setHeader({
					       		
					       		"title":"我的账户" ,
					       		"right_right": {
									html: '消息',
									func: function() {
										DMJS.router.navigate("other/Message/messageList", true);
									}
								},
					       		"left":{
						       			"html":"返回",
						       			"func":function(){
						       			 tool._Navi_default('index/index/index'); 
						       			}
						       		},
					       }); 
					       self.setFoot({
					        	"key":"none"
					       });
					       require(['userView/personal/userInfo'], function(userInfo){
					    	   self.setContent(userInfo,{
					       			"controller":self,
					       			
					    		}).render();
					       });
					},
					"error":function(_data){
						DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"), _data.description);	
					}
				});
       		}else{
       	
    	   var self=this;
	       self.setHeader({
	       		
	       		"title":"我的账户" ,
	       		"right_right": {
					html: '消息',
					func: function() {
						DMJS.router.navigate("other/Message/messageList", true);
					}
				},
	       		"left":{
		       			"html":"返回",
		       			"func":function(){
		       			 tool._Navi_default('index/index/index'); 
		       			}
		       		},
	       }); 
	       self.setFoot({
	        	"key":"none"
	       });
	       require(['userView/personal/userInfo'], function(userInfo){
	    	   self.setContent(userInfo,{
	       			"controller":self,
	       			
	    		}).render();
	       });
	       }
       },
       register:function(info){
       	   var self=this;
	       self.setHeader({
	       		
	       		"title":"注册",
	       		"left":{
		       			"html":"返回",
		       			"func":function(){
		       				if(DMJS.weixinId){
		       					tool._Navi_default("user/personal/binLogin/"+DMJS.weixinId);
		       				}else{
				       			tool._Navi_default("user/personal/login");
		       				}
		       			}
		       		},
	       }); 
	       self.setFoot({
	        	"key":"none"
	       });
	       require(['userView/personal/register'], function(register){
	    	   self.setContent(register,{
	       			"controller":self,
	       			'info':info,
	    		});
	    		if(DMJS.currentView.fromCache!='Y'){
    			   DMJS.currentView.render();
    		  	 }
	       });
       },
       registerInfo:function(nextPageData){
       	   var self=this;
	       self.setHeader({
	       		
	       		"title":"完善信息",
	       		"left":{
		       			"html":"返回",
		       			"func":function(){
		       			DMJS.router.navigate("user/personal/login",true);
		       			DMJS.CommonTools.hash_clear();
		       			}
		       		},
	       		
	       }); 
	       self.setFoot({
	        	"key":"none"
	       });
	       require(['userView/personal/registerInfo'], function(registerInfo){
	    	   self.setContent(registerInfo,{
	       			"controller":self,
	       			"data":{
	       				nextPageData:nextPageData,
	       			}
	    		}).render();
	       });
       },
       agreement:function(type){
       	var self=this,title;
			switch(type){
				case 'FXTSH': title="风险提示函"; break;
				case 'ZC': title="注册协议"; break;
				case 'GYB': title="公益捐助协议"; break;
				case 'JK': title="借款协议"; break;
				case 'ZQ': title="债权转让协议"; break;
				case 'GRXX': title="个人信息采集授权条款"; break;
				case 'QYXX': title="企业信息采集授权条款"; break;
				default:title="协议";
			}
	       self.setHeader({
	       		"title":title,
	       		"left":{
		       			"html":"返回",
		       			"func":function(){
		       			tool._Navi_default("user/personal/login");
		       			}
		       		},
	       });
	       $('#header_left').css('min-width','4rem');
	        $('#header_left').css('min-width','4rem');
	       self.setFoot({
	        	"key":"none"
	       });
	       require(['userView/personal/agreement'], function(agreement){
	    	   self.setContent(agreement,{
	       			"controller":self,
	       			data:{type:type}
	    		}).render();
	    		
	       });
       },
       //我的奖励
       myReward:function(){
       	var self=this;
	       self.setHeader({
	       		
	       		"title":"我的奖励",
	       		"left":{
		       			"html":"返回",
		       			"func":function(){
		       			tool._Navi_default("user/personal/userInfo");
		       			}
		       		},
	       }); 
	       self.setFoot({
	        	"key":"none"
	       });
	       require(['userView/personal/myReward'], function(myReward){
	    	   self.setContent(myReward,{
	       			"controller":self,
	       			"data":{
	       			}
	    		}).render();
	       });
       },
       experience:function(){
       	
       	var self=this;
	       self.setHeader({
	       		"title":"体验金",
	       		"left":{
		       			"html":"返回",
		       			"func":function(){
		       			tool._Navi_default("user/personal/myReward");
		       			}
		       		},
	       }); 
	       self.setFoot({
	        	"key":"none"
	       });
	       require(['userView/personal/experience'], function(experience){
	    	   self.setContent(experience,{
	       			"controller":self,
	       			"data":{
	       			}
	    		}).render();
	       });
       
       },
       experienceInfo:function(id,status){
       	var self=this;
	       self.setHeader({
	       		"title":"体验金投资详情",
	       		"left":{
		       			"html":"返回",
		       			"func":function(){
		       			tool._Navi_default("user/personal/experience");
		       			}
		       		},
	       }); 
	       self.setFoot({
	        	"key":"none"
	       });
	       require(['userView/personal/experienceInfo'], function(experienceInfo){
	    	   self.setContent(experienceInfo,{
	       			"controller":self,
	       			"data":{id:id,status:status}
	    		}).render();
	       });
       
      
       },
       raise:function(){
       	var self=this;
	       self.setHeader({
	       		"title":"加息券",
	       		"left":{
		       			"html":"返回",
		       			"func":function(){
		       			tool._Navi_default("user/personal/myReward");
		       			}
		       		},
	       }); 
	       self.setFoot({
	        	"key":"none"
	       });
	       require(['userView/personal/raise'], function(raise){
	    	   self.setContent(raise,{
	       			"controller":self,
	       			"data":{
	       			}
	    		}).render();
	       });
       
       
       },
       redPacket:function(){
       	       	var self=this;
	       self.setHeader({
	       		"title":"红包",
	       		"left":{
		       			"html":"返回",
		       			"func":function(){
		       			tool._Navi_default("user/personal/myReward");
		       			}
		       		},
	       }); 
	       self.setFoot({
	        	"key":"none"
	       });
	       require(['userView/personal/redPacket'], function(redPacket){
	    	   self.setContent(redPacket,{
	       			"controller":self,
	       			"data":{
	       			}
	    		}).render();
	       });
       
       
       },
       cashTransfer:function(){
       	   var self=this;
	       self.setHeader({
	       		"title":"汇款计划",
	       		"left":{
		       			"html":"返回",
		       			"func":function(){
		       			tool._Navi_default("user/personal/userInfo");
		       			}
		       		},
	       }); 
	       self.setFoot({
	        	"key":"none"
	       });
	       require(['userView/personal/cashTransfer'], function(cashTransfer){
	    	   self.setContent(cashTransfer,{
	       			"controller":self,
	    		}).render();
	       });
       
       
       },
       //设置
       set:function(){
  	       	var self=this;
	        self.setHeader({
	      		"title":"设置",
	      		"left":{
		       			"html":"返回",
		       			"func":function(){
		       			tool._Navi_default("user/personal/myReward");
		       			}
		       		},
	      }); 
	      self.setFoot({
	       	"key":"none"
	      });
	      require(['userView/personal/set'], function(set){
	   	   self.setContent(set,{
	      			"controller":self,
	      			"data":{
	      			}
	   		}).render();
	      });
	  
       },
     //安全信息认证（未认证）
       unautherized:function(){
 	       	var self=this;
	        self.setHeader({
	      		"title":"安全信息认证",
	      		"left":{
		       			"html":"返回",
		       			"func":function(){
		       			tool._Navi_default("user/personal/userInfo");
		       			}
		       		},
	      }); 
	      self.setFoot({
	       	"key":"none"
	      });
	      require(['userView/personal/unautherized'], function(unautherized){
	   	   self.setContent(unautherized,{
	      			"controller":self,
	      			"data":{
	      			}
	   		}).render();
	      });
       },
       myInvestment:function(type){
	       	
	       	var self=this;
		       self.setHeader({
		       		"title":"我的投资",
		       		"left":{
			       			"html":"返回",
			       			"func":function(){
			       			tool._Navi_default("user/personal/userInfo");
			       			}
			       		},
		       }); 
		       self.setFoot({
		        	"key":"none"
		       });
		       require(['userView/personal/myInvestment'], function(myInvestment){
		    	   self.setContent(myInvestment,{
		       			"controller":self,
		       			'typeB':type
		    		});
		    		if(type=='tbz'||(DMJS.currentView&&DMJS.currentView.fromCache!='Y'))
		    		{
    			      DMJS.currentView.render();
    		  	    }
		       });
       			
       },
       tranLog:function(){
	       	var self=this;
		       self.setHeader({
		       		"title":"交易记录",
		       		"left":{
			       			"html":"返回",
			       			"func":function(){
			       			tool._Navi_default("user/personal/userInfo");
			       			}
			       		},
		       }); 
		       self.setFoot({
		        	"key":"none"
		       });
		       require(['userView/personal/tranLog'], function(tranLog){
		    	   self.setContent(tranLog,{
		       			"controller":self,
		    		}).render();
		       });
       },
       myLoan:function(){
	       	var self=this;
		       self.setHeader({
		       		"title":"我的借款",
		       		"left":{
			       			"html":"返回",
			       			"func":function(){
			       			tool._Navi_default("user/personal/userInfo");
			       			}
			       		},
		       }); 
		       self.setFoot({
		        	"key":"none"
		       });
		       require(['userView/personal/myLoan'], function(myLoan){
		    	   self.setContent(myLoan,{
		       			"controller":self,
		    		}).render();
		       });
       
       },
       myLoanInfo:function(id){
	       	var self=this;
		       self.setHeader({
		       		"title":"还款详情",
		       		"left":{
			       			"html":"返回",
			       			"func":function(){
			       			tool._Navi_default("user/personal/myLoan");
			       			}
			       		},
		       }); 
		       self.setFoot({
		        	"key":"none"
		       });
		       require(['userView/personal/myLoanInfo'], function(myLoanInfo){
		    	   self.setContent(myLoanInfo,{
		       			"controller":self,
		       			data:{loanId:id}
		    		}).render();
		       });
       
       },
       viewContract:function(id,typeS){
       		var self=this;
		       self.setHeader({
		       		"title":"合同",
		       		"left":{
			       			"html":"返回",
			       			"func":function(){
			       			tool._Navi_default("user/personal/userInfo");
			       			}
			       		},
		       }); 
		       self.setFoot({
		        	"key":"none"
		       });
		       require(['userView/personal/viewContract'], function(viewContract){
		    	   self.setContent(viewContract,{
		       			"controller":self,
		       			'bidId':id,
		       			'typeS':typeS
		    		}).render();
		       });
       },
       help:function(){
       		var self=this;
		       self.setHeader({
		       		"title":"新手入门",
		       		"left":{
			       			"html":"返回",
			       			"func":function(){
			       			tool._Navi_default("user/personal/userInfo");
			       			}
			       		},
		       }); 
		       self.setFoot({
		        	"key":"none"
		       });
		       require(['userView/personal/help'], function(help){
		    	   self.setContent(help,{
		       			"controller":self,
		    		});
		    		if(DMJS.currentView.fromCache!='Y'){
    			   DMJS.currentView.render();
    		  		 }
		       });
       
       },
        'riskInvestment':function(){
       		   var self=this;
		       self.setHeader({
		       		"title":"风险承受能力评估",
		       		"left":{
			       			"html":"返回",
			       			"func":function(){
			       			tool._Navi_default("user/personal/userInfo");
			       			}
			       		}
			        
		       }); 
		       self.setFoot({
		        	"key":"none"
		       });
		       require(['userView/personal/riskInvestment'], function(riskInvestment){
		    	   self.setContent(riskInvestment,{
		       			"controller":self,
		    		}).render();
		       });
       },
       'ticklingList':function(){
       		   var self=this;
		       self.setHeader({
		       		"title":"意见反馈",
		       		"left":{
			       			"html":"返回",
			       			"func":function(){
			       			tool._Navi_default("user/personal/userInfo");
			       			}
			       	},
			       	"right_right": {
					html: '反馈',
					func: function() {
						DMJS.router.navigate("user/personal/tickling", true);
					}
				},
					"right_left": {
						html: '反馈ICO',
						func: function() {
							DMJS.router.navigate("user/personal/tickling", true);
						}
					},
			        
		       }); 
		       self.setFoot({
		        	"key":"none"
		       });
		       require(['userView/personal/ticklingList'], function(ticklingList){
		    	   self.setContent(ticklingList,{
		       			"controller":self,
		    		}).render();
		       });
       
       },
       tickling:function(){
       		   var self=this;
		       self.setHeader({
		       		"title":"意见反馈",
		       		"left":{
			       			"html":"返回",
			       			"func":function(){
			       			tool._Navi_default("user/personal/ticklingList");
			       			}
			       	},
		       }); 
		       self.setFoot({
		        	"key":"none"
		       });
		       require(['userView/personal/tickling'], function(tickling){
		    	   self.setContent(tickling,{
		       			"controller":self,
		    		}).render();
		       });
       },
       helpInfo:function(id,type){
       		var self=this;
		       self.setHeader({
		       		"title":"新手入门",
		       		"left":{
			       			"html":"返回",
			       			"func":function(){
			       			tool._Navi_default("user/personal/userInfo");
			       			}
			       		},
		       }); 
		       self.setFoot({
		        	"key":"none"
		       });
		       require(['userView/personal/helpInfo'], function(helpInfo){
		    	   self.setContent(helpInfo,{
		       			 controller:self,
		       			 data: {id: id,type: type}
		    		}).render();
		       });
       
       },
       "myIntegral":function(){
       		   var self=this;
		       self.setHeader({
		       		
		       		"left":{
			       			"html":"返回",
			       			"func":function(){
//			       			    DMJS.route.navigate("user/personal/userInfo",true);
			       			    tool._Navi_default("user/personal/userInfo");
			       			}
			       		},
			        "title_left":{
			        	"active":true,
		       			"func":function(){
//		       				$("#header_title_right").removeClass("hover");
//		       				$("#header_title_left").addClass("hover");
//		       				DMJS.currentView.requestRecord("left");
		       			}
		       		},
		       		"title_right":{
		       			"func":function(){
//		       				$("#header_title_left").removeClass("hover");
//		       				$("#header_title_right").addClass("hover");
		       				DMJS.currentView.requestRecord("right");
		       			} 
		       		},
		       }); 
		       self.setFoot({
						'btnName': {
							text: "去兑换"
						}, //text 是按钮内容
						"key": "orderFoot",
						"func":  {
							"confirm": function(){
							    DMJS.router.navigate("index/index/intergalMall",true);
							   }
						},

					});
		       require(['userView/personal/myIntegral'], function(myIntegral){
		    	   self.setContent(myIntegral,{
		       			"controller":self,
		    		}).render();
		       });
       },
       integralRule:function(){
       		var self=this;
		       self.setHeader({
		       		"title":"积分规则",
		       		"left":{
			       			"html":"返回",
			       			"func":function(){
			       			tool._Navi_default("user/personal/myIntegral");
			       			}
			       		},
		       }); 
		       self.setFoot({
		        	"key":"none"
		       });
		       require(['userView/personal/integralRule'], function(integralRule){
		    	   self.setContent(integralRule,{
		       			"controller":self,
		    		}).render();
		       });
       
       },
       myOrder:function(){
       		   var self=this;
		       self.setHeader({
		       		"title":"我的订单",
		       		"left":{
			       			"html":"返回",
			       			"func":function(){
			       			tool._Navi_default("user/personal/userInfo");
			       			}
			       		},
		       }); 
		       self.setFoot({
		        	"key":"none"
		       });
		       require(['userView/personal/myOrder'], function(myOrder){
		    	   self.setContent(myOrder,{
		       			"controller":self,
		    		}).render();
		       });
       },
       orderDetail:function(id){
       		   var self=this;
		       self.setHeader({
		       		"title":"订单详情",
		       		"left":{
			       			"html":"返回",
			       			"func":function(){
			       			tool._Navi_default("user/personal/myOrder");
			       			}
			       		},
		       }); 
		       self.setFoot({
		        	"key":"none"
		       });
		       require(['userView/personal/orderDetail'], function(orderDetail){
		    	   self.setContent(orderDetail,{
		       			"controller":self,
		       			"orderId":id,
		    		}).render();
		       });
       },
       
       "exchangeIntegral":function(){
       		   var self=this;
		       self.setHeader({
		       		"left":{
			       			"html":"返回",
			       			"func":function(){
			       			tool._Navi_default("user/personal/userInfo");
			       			}
			       		},
			        "title_left":{
			        	"active":false,
		       			"func":function(){
//		       				$("#header_title_right").removeClass("hover");
//		       				$("#header_title_left").addClass("hover");
		       				DMJS.currentView.requestRecord("left");
		       			}
		       		},
		       		"title_right":{
		       			"func":function(){
//		       				$("#header_title_left").removeClass("hover");
//		       				$("#header_title_right").addClass("hover");
//		       				DMJS.currentView.requestRecord("right");
		       			} 
		       		},
		       }); 
		       self.setFoot({
						'btnName': {
							text: "去兑换"
						}, //text 是按钮内容
						"key": "orderFoot",
						"func":  {
							"confirm": function(){
							    DMJS.router.navigate("index/index/intergalMall",true);
							   }
						},

					});
		       require(['userView/personal/exchangeIntegral'], function(exchangeIntegral){
		    	   self.setContent(exchangeIntegral,{
		       			"controller":self,
		    		}).render();
		       });
       },
      
    });
    return PersonalController;
});
