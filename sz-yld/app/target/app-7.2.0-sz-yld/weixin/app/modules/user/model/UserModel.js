
define(['Lib/md5'], function(MD5) {
    var UserModel = DMJS.DMJSModel.extend({
        defaults: {
        },
        'commonData': {},
        //获取验证码统一一个接口， 根据 type类型来判断属于哪类验证码
        getMobileCode:function(param){ 
        	var urlKey="user.getMobileCode";
        	this.commonRequest(_.extend({'urlKey':urlKey},param));
        },
        //校验验证码
        checkVerifyCode:function(param){
        	var urlKey="user.checkVerifyCode";
        	this.commonRequest(_.extend({'urlKey':urlKey},param));
        },
        //注册，完善资料
        register:function(param){
        	var urlKey="user.register";
        	this.commonRequest(_.extend({'urlKey':urlKey},param));
        },
        //登录
        login:function(param){
        	var urlKey="user.login";
        //	var time=new Date().getTime();
     //  param.data['time']=1453196203120;
//      	param.data.password=MD5(param.data.password).toLowerCase();
        	param.data.flag=MD5(param.data.accountName+"|"+param.data.password+"|"+param.data.time).toLowerCase();
        	this.commonRequest(_.extend({'urlKey':urlKey},param)); 
        },//找回登录密码
        //用户绑定微信号获取openid
        bindCallBackServlet:function(param){
        	var urlKey="bindCallBackServlet";
        	this.commonRequest(_.extend({'urlKey':urlKey},param)); 
        },//找回登录密码
        
        resetLoginPwd:function(param){
        	var urlKey="user.resetLoginPwd";
//      	param.data.password=MD5(param.data.password).toLowerCase();
//      	param.data.rePassword=MD5(param.data.rePassword).toLowerCase();
        	this.commonRequest(_.extend({'urlKey':urlKey},param));
        },
        //修改登录密码
        loginPwdUpdate:function(param){
        	var urlKey="user.updatePwd";
//      	param.data.pwd=MD5(param.data.pwd).toLowerCase();
//      	param.data.onePwd=MD5(param.data.onePwd).toLowerCase();
//      	param.data.twoPwd=MD5(param.data.twoPwd).toLowerCase();
        	this.commonRequest(_.extend({'urlKey':urlKey},param));
        },
        //获取账号 密码  正则
        REGEX:function(param){
        	var urlKey="user.REGEX";
        	this.commonRequest(_.extend({'urlKey':urlKey},param));
        },
        //退出登录
        logout:function(param){
        	var urlKey="user.logout";
        	this.commonRequest(_.extend({'urlKey':urlKey},param));
        },
        //我的账户
        myAccount:function(param){
        	var urlKey="user.myAccount";
        	this.commonRequest(_.extend({'urlKey':urlKey},param));
        },
        //个人信息
        userInfo:function(param){
        	var urlKey="user.user";
        	this.commonRequest(_.extend({'urlKey':urlKey},param));
        },
        //实名认证
        setUserInfo:function(param){
        	var urlKey="user.setUserInfo";
        	this.commonRequest(_.extend({'urlKey':urlKey},param));
        },
        //绑定邮箱
        getEmailCode:function(param){
        	var urlKey="user.getEmailCode";
        	this.commonRequest(_.extend({'urlKey':urlKey},param));
        },
        //手机认证
        setPhone:function(param){
        var urlKey="user.setPhone";
        this.commonRequest(_.extend({'urlKey':urlKey},param));
        },
        //设置交易密码
        setTranPwd:function(param){
        	var urlKey="user.setTranPwd";
        	this.commonRequest(_.extend({'urlKey':urlKey},param));
        },
        //更新交易密码
        updateTranPwd:function(param){
        	var urlKey="user.updateTranPwd";
        	this.commonRequest(_.extend({'urlKey':urlKey},param));
        },
        //找回交易密码
        findTranPwd:function(param){
        	var urlKey="user.findTranPwd";
        	this.commonRequest(_.extend({'urlKey':urlKey},param));
        },
        //取得第三方注册信息
        payUserRegister:function(param){
        	var urlKey="user.payUserRegister";
        	this.commonRequest(_.extend({'urlKey':urlKey},param));
        },
        //我的奖励
        myReward:function(param){
        	var urlKey="user.myReward";
        	this.commonRequest(_.extend({'urlKey':urlKey},param));
        },
        //我的体验金列表
    	experienceList:function(param){
    		var urlKey="user.experienceList";
        	this.commonRequest(_.extend({'urlKey':urlKey},param));
    	},
    	//我的体验金详情
    	experienceInfo:function(param){
    		var urlKey="user.experienceInfo";
        	this.commonRequest(_.extend({'urlKey':urlKey},param));
    	},
    	//我的红包和加息劵列表
    	myRewardList:function(param){
    		var urlKey="user.myRewardList";
        	this.commonRequest(_.extend({'urlKey':urlKey},param));
    	},
    	//我的银行列表
    	myBankList:function(param){
    		var urlKey="user.myBankList";
        	this.commonRequest(_.extend({'urlKey':urlKey},param));
    	},
    	//银行卡列表
    	bankList:function(param){
    		var urlKey="user.bankList";
        	this.commonRequest(_.extend({'urlKey':urlKey},param));
    	},//常量查询
    	fee:function(param){
    		var urlKey="user.fee";
        	this.commonRequest(_.extend({'urlKey':urlKey},param));
    	},
    	//添加银行卡
    	addCard:function(param){
    		var urlKey="user.addCard";
        	this.commonRequest(_.extend({'urlKey':urlKey},param));
    	},
    	//删除银行卡
    	unbindCard:function(param){
    		var urlKey="user.unbindCard";
        	this.commonRequest(_.extend({'urlKey':urlKey},param));
    	},
		//富友更换银行卡
    	changeCard:function(param){
    		var urlKey="user.changeCard";
        	this.commonRequest(_.extend({'urlKey':urlKey},param));
    	},
    	//富友查询银行卡
    	fyouQueryBank:function(param){
        	var urlKey="payment.fyouQueryBank";
        	this.commonRequest(_.extend({'urlKey':urlKey},param));
       },

    	//体验金详情
    	myTyjList:function(param){
    		var urlKey="user.myTyjList";
        	this.commonRequest(_.extend({'urlKey':urlKey},param));
    	},

    	//充值
    	charge:function(param){
    		var urlKey="user.charge";
        	this.commonRequest(_.extend({'urlKey':urlKey},param));
    	},
    	//提现
    	withdraw:function(param){
    		var urlKey="user.withdraw";
        	this.commonRequest(_.extend({'urlKey':urlKey},param));
    	},
    	//交易记录
    	tranLog:function(param){
    		var urlKey="user.tranLog";
        	this.commonRequest(_.extend({'urlKey':urlKey},param));
    	},
    	//我的借款列表
    	myBidList:function(param){
    		var urlKey="user.myBidList";
    		this.commonRequest(_.extend({'urlKey':urlKey},param));
    	},
    	//我的借款详情
    	myLoanInfo:function(param){
    		var urlKey="user.myLoanInfo";
    		this.commonRequest(_.extend({'urlKey':urlKey},param));
    	},
    	getConfig:function(param){
    		var urlKey="index.getConfig";
    		this.commonRequest(_.extend({'urlKey':urlKey},param));
    	},
    	//提前还款
    	replayEnd:function(param){
    		var urlKey="user.replayEnd";
    		this.commonRequest(_.extend({'urlKey':urlKey},param));
    	},
    	//还款
    	replay:function(param){
    		var urlKey="user.replay";
    		this.commonRequest(_.extend({'urlKey':urlKey},param));
    	},
    	//我的投资
    	myCreditorInfo:function(param){
    		var urlKey="user.myCreditorInfo";
    		this.commonRequest(_.extend({'urlKey':urlKey},param));
    	},
    	
    	//查看借款合同
    	agreementView:function(param){
    		var urlKey="user.agreementView";
    		this.commonRequest(_.extend({'urlKey':urlKey},param));
    	},
    	//不良债权合同
    	blzqagreementView:function(param){
    		var urlKey="user.blzqagreementView";
    		this.commonRequest(_.extend({'urlKey':urlKey},param));
    	},
    	//债权转让
    	creditAssignmentList:function(param){
    		var urlKey="user.creditAssignmentList";
    		this.commonRequest(_.extend({'urlKey':urlKey},param));
    	},
    	
    	//查看债权合同
    	zqzrAgreementView:function(param){
    		var urlKey="user.zqzrAgreementView";
    		this.commonRequest(_.extend({'urlKey':urlKey},param));
    	},
    	
    	//我的投资转让
    	transfer:function(param){
    		var urlKey="user.transfer";
    		this.commonRequest(_.extend({'urlKey':urlKey},param));
    	},
    	//查询合同开关
//  	contractConstant:function(param){
//  		var urlKey="user.contractConstant";
//  		this.commonRequest(_.extend({'urlKey':urlKey},param));
//  	},
    	//根据ID以及类型查询合同
    	saveBidContract:function(param){
    		var urlKey="user.saveBidContract";
    		this.commonRequest(_.extend({'urlKey':urlKey},param));
    	},
    	//取消转让
    	cancelTransfer:function(param){
    		var urlKey="user.cancelTransfer";
    		this.commonRequest(_.extend({'urlKey':urlKey},param));
    	},
    	//意见反馈
    	tickling:function(param){
    		var urlKey="user.addFeed";
    		this.commonRequest(_.extend({'urlKey':urlKey},param));
    	},
    	//我的意见反馈
    	ticklingList:function(param){
    		var urlKey="user.myFeedBack";
    		this.commonRequest(_.extend({'urlKey':urlKey},param));
    	},
    	//风险评估问题
    	riskQuestions:function(param){
    		var urlKey="user.riskQuestions";
    		this.commonRequest(_.extend({'urlKey':urlKey},param));
    	},
    	
    	//风险评估
    	riskAssessment:function(param){
    		var urlKey="user.riskAssessment";
    		this.commonRequest(_.extend({'urlKey':urlKey},param));
    	},
    	//新手入门
    	help:function(param){
    		var urlKey="user.help";
    		this.commonRequest(_.extend({'urlKey':urlKey},param));
    	},
    	//获取记录
    	getIntegralRecords:function(param){
    		var urlKey="user.myScoreGetRecords";
    		this.commonRequest(_.extend({'urlKey':urlKey},param));
    	},
    	//兑换记录
    	getExchangeRecords:function(param){
    		var urlKey="user.myScoreExchangeRecords";
    		this.commonRequest(_.extend({'urlKey':urlKey},param));
    	},
    	//积分规则
    	scoreRules:function(param){
    		var urlKey="user.scoreRules";
    		this.commonRequest(_.extend({'urlKey':urlKey},param));
    	},
    	//我的订单
    	myOrder:function(param){
    		var urlKey="user.myOrder";
    		this.commonRequest(_.extend({'urlKey':urlKey},param));
    	},
    	//订单详情
    	myOrderItem:function(param){
    		var urlKey="user.myOrderItem";
    		this.commonRequest(_.extend({'urlKey':urlKey},param));
    	},
    	//新手入门详情
    	helpInfo:function(param){
    		var urlKey="user.helpInfo";
    		this.commonRequest(_.extend({'urlKey':urlKey},param));
    	},
    	//签到
    	userScore:function(param){
    		var urlKey="user.userScore";
    		this.commonRequest(_.extend({'urlKey':urlKey},param));
    	},
    	
    	
    });
    return UserModel;
});
