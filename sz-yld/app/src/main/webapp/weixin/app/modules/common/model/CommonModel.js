
define(['DMJSAll'], function(DMJS){
    var UserModel = DMJS.DMJSModel.extend({
        defaults: {
        },
        'commonData':{},
        'getImageUrl':function(param){
            //获取图片服务器路径
	    	var urlKey="image.url";
	    	this.commonRequest(_.extend({'urlKey':urlKey},param));
        },
        timing:function(param){
	    	var urlKey="index.timing";
	    	this.commonRequest(_.extend({'urlKey':urlKey},param));
        },
        getConfig:function(param){
        	//获取系统常量配置
	    	var urlKey="index.getConfig";
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
        //取得第三方注册信息
        payUserRegister:function(param){
        	var urlKey="user.payUserRegister";
        	this.commonRequest(_.extend({'urlKey':urlKey},param));
        },
        //协议 type   ZC:注册|JK:借款|GYB:公益捐赠|ZQ:债权转让         JK 时需多传一个参数  isDB  是否为担保标 
        initType:function(param){
        	var urlKey="user.initType";
        	this.commonRequest(_.extend({'urlKey':urlKey},param));
        },
        //协议
        agreement:function(param){
        	var urlKey="user.agreement";
        	this.commonRequest(_.extend({'urlKey':urlKey},param));
        },
    	//地址
    	searchAddress:function(param){
    		var urlKey="user.searchAddress";
        	this.commonRequest(_.extend({'urlKey':urlKey},param));
    	},
    	//个人借款地址
    	searchLoanAddress:function(param){
    		var urlKey="user.searchLoanAddress";
        	this.commonRequest(_.extend({'urlKey':urlKey},param));
    	},
    	//备案号
    	getICP:function(param){
        	var urlKey="index.timing";
        	this.commonRequest(_.extend({'urlKey':urlKey},param));
       	},
       	//手续费，认证信息
       	fee:function(param){
       		var urlKey="user.fee";
        	this.commonRequest(_.extend({'urlKey':urlKey},param));
       	}
    });
    return UserModel;
});
