//require.config.js
require.config({
    // localCachePath变量定义在require.js文件中
    // 如果该值为真，意味当前工作在android设备上，并且已开启离线缓存
    // android设备上的离线缓存解决方案，要求baseUrl的值为空字符串localCachePath ? '' : '/SMTResourceNew/PAEsales/',
    baseUrl: localCachePath ? '' : '',
    shim: {
    	'Char':{
    		exports: "Chart"
    	},
    	'highCharts':{
    		exports: "highCharts"
    	},
    	'jQuery': {
            exports: "jQuery"
        },
        
        jquery_qrcode:{
        	deps: ['jQuery'],
            exports: 'jquery_qrcode'
        },
        'mobiscroll_core': {
            deps: ['jQuery'],
            exports: 'mobiscroll_core'
        },
        'mobiscroll_select': {
            deps: ['mobiscroll_core'],
            exports: 'mobiscroll_select'
        },
        'mobiscroll_android': {
            deps: ['mobiscroll_select'],
            exports: 'mobiscroll_android'
        },
        'mobiscroll_android_ics': {
            deps: ['mobiscroll_android'],
            exports: 'mobiscroll_android_ics'
        },
        'mobiscroll_datetime': {
            deps: ['mobiscroll_android_ics'],
            exports: 'mobiscroll_datetime'
        },
        'mobiscroll_list': {
            deps: ['mobiscroll_datetime'],
            exports: 'mobiscroll_list'
        },
        'mobiscrollAll': {
            deps: ['mobiscroll_list'],
            exports: 'mobiscrollAll'
        },
         'percentage': {
             deps: ['jQuery'],
             exports: "percentage"
         },
        'zepto': {
            exports: '$'
        },
        'xml2json': {
            deps: ['zepto'],
            exports: 'xml2json'
        },
        'underscore': {
            exports: '_'
        },
        'backbone': {
            deps: ['text', 'zepto', 'underscore'],
            exports: 'Backbone'
        },
        'DMJS': {
            deps: ['backbone'],
            exports: 'DMJS'
        },
        'DMJSModel': {
            deps: ['DMJS'],
            exports: 'DMJSModel'
        },
        'DMJSCollection': {
            deps: ['DMJS'],
            exports: 'DMJSCollection'
        },
        'DMJSView': {
            deps: ['DMJS'],
            exports: 'DMJSView'
        },
        'DMJSRouter': {
            deps: ['DMJS'],
            exports: 'DMJSRouter'
        },
        'DMJSHistory': {
            deps: ['DMJS'],
            exports: 'DMJSHistory'
        },
        'DMJSController': {
            deps: ['DMJS'],
            exports: 'DMJSController'
        },
        'DMJSAll': {
            deps: ['DMJS', 'DMJSModel', 'DMJSCollection', 'DMJSView', 'DMJSRouter', 'DMJSHistory', 'DMJSController'],
            exports: 'DMJSAll'
        },
        'utils': {
            deps: ['zepto'],
            exports: "tp1"
        },
        "commonClass/scroll/iscroll": {
            exports: "iScroll"
        },
    },
    paths: {
        text: 'assets/plugins/require/text',
        zepto: 'assets/lib/zepto/zepto',
        xml2json: 'assets/plugins/zepto/zepto.xml2json-min',
        underscore: 'assets/lib/underscore/underscore',
        backbone: 'assets/lib/backbone/backbone',
        Lib: 'assets/lib',
        Char: 'assets/lib/chart/Chart',
        highCharts:'assets/lib/chart/highcharts',
        jQuery: 'assets/lib/jquery/jquery-1.11.2.min',
        mobiscrollAll: 'assets/plugins/mobiscroll/mobiscrollAll',
        jQueryMobile: 'assets/lib/jquery/jquery.mobile.custom.min',
        mobiscroll_core: 'assets/plugins/mobiscroll/mobiscroll.core-2.6.2',
        mobiscroll_select: 'assets/plugins/mobiscroll/mobiscroll.select-2.6.2',
        mobiscroll_android: 'assets/plugins/mobiscroll/mobiscroll.android',
        mobiscroll_android_ics: 'assets/plugins/mobiscroll/mobiscroll.android-theme',
        mobiscroll_datetime: 'assets/plugins/mobiscroll/mobiscroll.datetime-2.6.2',
        mobiscroll_list: 'assets/plugins/mobiscroll/mobiscroll.list-2.6.2',
        jquery_qrcode: 'assets/plugins/mobiscroll/jquery.qrcode-0.12.0',
        common: 'app/modules/common',
        commonConfig: 'app/modules/common/config',
        commonController: 'app/modules/common/controller',
        commonModel: 'app/modules/common/model',
        commonView: 'app/modules/common/view',
        commonTemplate: 'app/modules/common/template',
        commonUI: 'app/modules/common/ui',
        commonClass: 'app/modules/common/class',
        commonTool: 'app/modules/common/tool',
        commonData: 'app/modules/common/data'
    },
    waitSeconds: 15
});
requirejs.onError = function(a, b, c) {
//	Native.throwException(a.toString(),a.requireType);
    if (confirm("由于网络不给力，部分资源加载失败！是否重新加载？")) {
        window.location.reload();
    }
}
//weixin.js
define("commonClass/weixin", ["Lib/native/weixin"], function(core) {
    var weixin = {
        "config": function() {
              DMJS.Request("weixin.keysInfo", {
                  type: "get",
                  dataType: "jsonp",
                  data: {url: location.href.split('#')[0]},
                  unBort: true,
                  cancelLightbox: true,
                  success: function(response) {
                      core.config({
                          debug: false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
                          appId: response.data.appId, // 必填，公众号的唯一标识
                          timestamp: response.data.timestamp, // 必填，生成签名的时间戳
                          nonceStr: response.data.noncestr, // 必填，生成签名的随机串
                          signature: response.data.signature, // 必填，签名，见附录1
                          jsApiList: ['onMenuShareAppMessage','onMenuShareTimeline',"hideMenuItems", "showMenuItems", "getLocation", "openLocation", "closeWindow"] // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
                      });
                  }
              });
        },
        "init": function() {
            this.config();
            core.ready(_.bind(this.success, this));
            core.error(_.bind(this.error, this));
        }, "success": function() {
            core.hideMenuItems({
            	menuList:[]
//              menuList: ["menuItem:originPage", "menuItem:copyUrl", "menuItem:openWithQQBrowser", "menuItem:openWithSafari"] // 要隐藏的菜单项，只能隐藏“传播类”和“保护类”按钮，所有menu项见附录3
            });
            core.showMenuItems({
            	menuList:[]
//              menuList: ["menuItem:share:weiboApp", "menuItem:share:facebook", "menuItem:share:QZone"] // 要隐藏的菜单项，只能隐藏“传播类”和“保护类”按钮，所有menu项见附录3
            });
			var shareImg=Config.base('envOption').PCWebsite+"app/weixin/assets/images/common/logo3.png";//注意必须是绝对路径
        	var shareTitle=document.title;//分享title
        	var descContent="我在"+document.title+"投资已获得很高的收益，您也快来试试吧！";//分享给朋友或朋友圈时的文字简介
        	var shateUrl = Config.base('envOption').PCWebsite + "app/register.jsp";//同样，必须是绝对路径
        	if(DMJS.userInfo){
        		shateUrl = shateUrl + "?yqm=" + DMJS.userInfo.tgm;;//同样，必须是绝对路径
        	}
			DMJS.Request("index.timing", {
                  type: "get",
                  dataType: "jsonp",
                  data: {url: location.href.split('#')[0]},
                  unBort: true,
                  cancelLightbox: true,
                  success: function(response) {
                      shareTitle = response.data.siteName;
                      descContent = "我在"+response.data.siteName+"投资已获得很高的收益，您也快来试试吧！";
                  }
              });
            //分享功能只能在认证过的公众号上测试
            core.onMenuShareTimeline({
            	title: shareTitle, // 分享标题
	            link: shateUrl, // 分享链接,将当前登录用户转为puid,以便于发展下线
	            imgUrl: shareImg, // 分享图标
	            success: function () { 
	                // 用户确认分享后执行的回调函数
	                alert("分享成功！");
	            },
	            cancel: function () { 
	                // 用户取消分享后执行的回调函数
	            }
            });
             core.onMenuShareAppMessage({
                title: shareTitle, // 分享标题
	            link: shateUrl , // 分享链接,将当前登录用户转为puid,以便于发展下线
	            desc: descContent, // 分享描述
	            imgUrl: shareImg, // 分享图标
                type: '', // 分享类型,music、video或link，不填默认为link
                dataUrl: '', // 如果type是music或video，则要提供数据链接，默认为空
                success: function () {
                    // 用户确认分享后执行的回调函数
					alert("分享成功！");
                },
                cancel: function () {
                    // 用户取消分享后执行的回调函数
                }
            });
//				this.seStp();
        }, "error": function() {
            console.info("weixin api  error");
            console.info("weixin error");
        }, "exit": function() {
            core.closeWindow();
        }, "step": function() {
            core.hideMenuItems({
            	menuList:[]
//              menuList: ["menuItem:originPage", "menuItem:copyUrl", "menuItem:openWithQQBrowser", "menuItem:openWithSafari"] // 要隐藏的菜单项，只能隐藏“传播类”和“保护类”按钮，所有menu项见附录3
            });
            core.showMenuItems({
            	menuList:[]
//              menuList: ["menuItem:share:weiboApp", "menuItem:share:facebook", "menuItem:share:QZone"] // 要隐藏的菜单项，只能隐藏“传播类”和“保护类”按钮，所有menu项见附录3
            });
        }

    };
    return weixin;
});
//Native.js
define("Native", function() {
    window.Native = {
        "openChildWebActivity": function(url, config) {
            console.log("打开第三方子页面:" + url);
            if (Config.logic("isWeixin")) {
//				DMJS.CommonTools.showBrowser({"url":url,"title":config&&config.title?config.title:"外 部 资 源"});
                return location.href = url;
            }
/*            cordova.exec(
                    function() {
                        console.log("加载第三方子页面")
                    },
                    function() {
                        alert("加载第三方子页面失败")
                    },
                    "views",
                    "openChildWebActivity",
                    [url]);*/
        },
        
        "exit": function() {
            DMJS.killRequest();
            if (Config.logic("isWeixin")) {
                require(["commonClass/weixin"], function(weixin) {
                    weixin.exit();
                });
                return;
            }
            cordova.exec(
                    function() {
                        console.log("关闭程序OK")
                    },
                    function() {
                        console.log("关闭程序fail")
                    },
                    "views",
                    "exitApp",
                    []);
            return;
        },
        alert: function(option, callback) {
            var butonLable = callback ? "确定" : undefined;
           // cordova.exec(callback, null, "dialog", "alert", [option.content, option.title, butonLable]);
        },
        confirm: function(option, FBntconfirm, FBntcancel) {
            var resultCallback = function(index) {
                if (index == 1) {
                    FBntcancel();
                } else if (index == 2) {
                    FBntconfirm();
                }
            }
            //cordova.exec(resultCallback, null, "dialog", "confirm", [option.content, option.title, [option.FBntcancel, option.FBntconfirm]]);
        },
        "getAppInfo": function(doAppInfo) {
            cordova.exec(
                    doAppInfo,
                    function() {
                        console.log("获取APP信息失败")
                    },
                    "device",
                    "getDeviceInfo",
                    []);
            return;
        },
        "dmjsReady": function() {
            if (DMJS.isReady) {
                return;
            }
            cordova.exec(
                    function() {
                        console.log("通知成功")
                    },
                    function() {
                        console.log("通知失败")
                    },
                    "views",
                    "dmjsReady",
                    []);
            return;
        },
        "openView": function(hash, config) {
           /* cordova.exec(
                    function() {
                        console.log("通知成功")
                    },
                    function() {
                        console.log("通知失败")
                    },
                    "views",
                    "openview",
                    [hash, config]);*/
            return;
        },
        "syncCrossData": function(id, val) {
            cordova.exec(
                    function(data) {
                        console.log("通知成功:" + data);
                        DMJS.userInfo = data;
                    },
                    function() {
                        console.log("通知失败")
                    },
                    "views",
                    "syncCrossData",
                    [id, val]);
            return;
        },
        "tip": function(message) {
            if (!this.ready) {
                DMJS.CommonTools.popTip(message, 1000);
                return;
            }
            cordova.exec(
                    function() {
                        console.log("通知成功")
                    },
                    function() {
                        console.log("通知失败")
                    },
                    "views",
                    "tip",
                    [message]);
            return;
        },
        "share": function(message) {
            cordova.exec(
                    function() {
                        console.log("通知成功")
                    },
                    function() {
                        console.log("通知失败")
                    },
                    "views",
                    "share",
                    [message]);
            return;
        }
    };

    var ua = navigator.userAgent.toLowerCase(), //
            android = ua.indexOf('android') != -1; //
    if (!window.cordova) {
        window.cordova = {'exec': function() {
                console.log("使用调用native功能，打包后使用!");
            }};
        Native.isReady = false;
    } else {
        window.open = function(url, target) {
            cordova.exec(function() {
            }, function() {
            }, "InAppBrowser", "open", [url, target, ""]);
        }
        Native.isReady = true;
    }
    return window.Native;
});


//commonConfig/base.js
define("commonConfig/base", ["DMJS", "Native"], function(PAWA) {
	var envOption = {

//	     'requestType':'GET',  //请求方式 转测填 POST， 开发填GET
		 'requestType':'POST',  //请求方式 转测填 POST， 开发填GET
    	 'appName':'百里金服',//项目名称
    	 'PCWebsite':'http://112.95.233.249:8694/', //PC端地址，转测填测试环境地址，上线填线上PC地址
//    	 'interFace':'/40085/', //上线填/app/ 转测填/4008?/
    	 'interFace':'/app/', //上线填/app/ 转测填/4008?/
//	     'interFace':'http://112.95.233.249:8694/app/', //测试地址
//		'interFace':'http://112.95.233.249:5139/app/',


	}
    window.Config = {
        /**
         * 当前请求数s
         */
        envOption:envOption,
    	isTg:false,
        ajaxCount: 0,
        isDMJSReady: false,
        pageSize: 10,
        logLever: 2,
        requestEncrypt: false,
        lazyRender: 350,
        interFace: envOption['interFace'],
        appName: envOption['appName'],
        trunKey: "P9f0CNEiozgJ3etIp",
        anim: {
            "in": "pt-page-moveFromRight",
//	        	"out":"pt-page-scaleDown"
            "out": "pt-page-moveToLeft123"
        },
        /**
         * 初始化全局配置
         */
        init: function() {
        	
            // ajax默认配置
            var ajaxSettings = $.ajaxSettings;
            ajaxSettings.lightboxHide = true;
            ajaxSettings.dataType = "json";
            ajaxSettings.beforeSendCallback = function() {
            };
            ajaxSettings.beforeSend = function(xhr) {
                ajaxSettings.beforeSendCallback(xhr);
                ajaxSettings.beforeSendCallback = function() {
                };
                if (Config.ajaxCount >= 0) {
                    // 第一个请求开始  有的请求是不需要遮罩层的..........
                    if (!$.ajaxSettings.cancelLightbox) {
                        if (wrapView.FlipPrompt && !wrapView.FlipPrompt.isBlock) {
                            wrapView.FlipPrompt.loading("正在加载...", false);
                        }
                    }
                }
                if (!$.ajaxSettings.cancelLightbox) {
                    Config.ajaxCount++;
                }
            };
            ajaxSettings.complete = function() {
                Config.ajaxCount--;
                if (Config.ajaxCount <= 0) {
                    Config.ajaxCount = 0;
                    if (ajaxSettings.lightboxHide == true) {
                        // 所有请求结束
                        if (wrapView && wrapView.FlipPrompt) {
                            wrapView.FlipPrompt.colse(true);
                        }
                    } else {
                        ajaxSettings.lightboxHide = true;
                    }
                }
            },
                    console.log("even backbutton");
            //document.addEventListener("resume",function(){
//	            	Native.toHandPasswordCheck(true);
            //},false);
            document.addEventListener("backbutton", function() {
                var __isBlock = $("#FlipPrompt").length > 0 && $("#FlipPrompt").css("display") != "none";

                if (__isBlock) {
                    return;
                }
                if (window.wrapView == undefined) {
                    return Native.exit();
                } else if (wrapView.headCalls && wrapView.headCalls.left) {
                    if ($("div.dw-persp").length > 0) {
                        return;
                    } else {
                        return wrapView.headCalls.left();
                    }
                }
                wrapView.lightBox.index = 0;
                wrapView.FlipPrompt.confirm({
                    title: "退出程序",
                    content: "<span>确定退出程序?</span>",
                    FBntconfirm: "取消",
                    FBntcancel: "退出",
                    FBntCancelColor: "pop_btn_orange",
                    notHideProp: true
                }, function() {

                }, function() {
                    Native.exit();
                });
            }, false);
//	            ajaxSettings.beforeSend = function(xhr) {
//	            	wrapView.lightBox.show();
//	            };
//	            ajaxSettings.complete = function() {
            //
//	    			wrapView.lightBox.hide();
//	            };
        }
    };


    Config.base = function(name) {
        return Config[name];
    };

    Config.base.configDone = function(callback) {
        Config.init();
        if (Native.isReady) {
            document.addEventListener('deviceready', function() {
                if (!Config.base("isDMJSReady")) {
                    Native.getAppInfo(function(response) {
                        Config.isDMJSReady = true;
                        console.log("获取设备信息:" + JSON.stringify(response));
                        _.extend(Config, response);
                        callback();
                    });
                }
            }, false);
        } else {
            callback();
        }
    };
    return Config.base;
});

//	commonConfig/lang.js
define("commonConfig/lang", ['commonConfig/base'], function() {
    var langConfig = {
        netWorkUnavailable: "您的网络现在不给力哦...",
        alertCommonTitle: "温馨提示",
        sessionUnDestory: "检测到您仍处于登录状态!",
        pickcashSuccessSuccess: "恭喜您！提现申请成功，请等待运营人员审核！"
    };

    Config.lang = function(name) {
        var str = langConfig[name] || '';
        return str;
    };
    return Config.lang;
});


//commonConfig/logic.js
define("commonConfig/logic", ['commonConfig/base'], function() {
    var logicConfig = {
        cardWithDM: true,
        idPasswordMd5: false,
        tranPasswordWithDM: true,
        isWeixin: true
    };

    Config.logic = function(name) {
        return logicConfig[name] || false;
    };
    return Config.logicConfig;
});

//commonConfig/url.js
define("commonConfig/url", ["commonConfig/base"], function() {
    var interFace = Config.base("interFace");
    /*
     * 说明：分离式部署特殊设计
     * 当分离部署到生产环境时，特殊接口访问生产环境
     */

    var domainLists = {
    	  //"/dev": "/40080/"
// 		"/dev": "http://115.159.150.99/40080/"

    };

    var urlConfig = {
    	//index
    	'index.timing':'timing.htm',//时间
    	'bindCallBackServlet':'platinfo/bindCallBackServlet.htm',//判断微信用户是否进行账号绑定
    	"indexStatic":"platinfo/getIndexStatic.htm",//首页统计数据
    	"index.addList":"platinfo/advServlet.htm",//首页banner
    	'index.noticeList':'platinfo/noticeList.htm',//首页公告
    	'index.tjBidList':'bid/publics/tjBidList.htm',//首页标
    	'index.bidList':'bid/publics/bidList.htm',//项目投资列表
    	'index.bidDetail':'bid/publics/bid.htm',//标详情
    	'index.bidItem':'bid/publics/bidItem.htm',//标的附加信息
    	'index.bidRecordsList':'bid/publics/bidRecordsList.htm',//标的投资记录
    	'index.repayList':'bid/publics/repayList.htm',//标的还款计划
    	'index.unUseAwardList':'bid/publics/unUseAwardList.htm',//标的奖励
    	'index.getPropertyName':'platinfo/getPropertyName.htm',//标的奖励
    	    	
    	'index.buyBid':'bid/buyBid.htm',//购买标
    	'index.creditorList':'creditor/publics/creditorList.htm',//债权投资列表
    	'index.creditorDetail':'creditor/publics/creditor.htm',//债权投资详情
    	'index.creditorBuy':'creditor/buyCreditor.htm',//债权购买
    	'index.bidExchange':'creditor/bidExchange.htm',//双乾债权购买
    	'index.commonConstansValue':'',//备案号
    	'index.tyjAmountLoan':'user/tyjAmountLoan.htm',//体验金收益
    	'index.gyLoanList':'bid/publics/gyLoanList.htm',//公益标列表
    	'index.gyLoanItem':'bid/publics/gyLoanItem.htm',//公益标详情
    	'index.gyLoanInfo':'bid/publics/gyLoanInfo.htm',//公益标信息(广告、统计信息)
    	'index.gyLoanBid':'bid/gyLoanBid.htm',//公益标捐助
    	'index.gyLoanRecordsList':'bid/publics/gyLoanRecordsList.htm',//公益标捐助
    	'index.articleList':'platinfo/articleList.htm',//资讯
    	'index.getConfig':'platinfo/getConfig.htm',//获取系统配置常量
    	'index.articleItem':'platinfo/articleItem.htm',//文章内容
    	'index.noticeItem':'platinfo/noticeItem.htm',//公告详情/
    	'index.getUserBidRank':'platinfo/getUserBidRank.htm',//土豪榜
    	'index.xyd':'loan/publics/xydLoanItem.htm',//信用贷常量信息
    	'index.xydSub':'loan/applyXydLoan.htm',//信用贷申请
    	'index.isAuthen':'loan/publics/isAuthentication.htm',//申请信用贷需要的认证
    	'index.grSub':'loan/applyGrLoan.htm',//个人借款申请
    	'index.qySub':'loan/applyQyLoan.htm',//企业借款申请
    	'index.operationData':'platinfo/operationData.htm',//运营数据
    	
    	//user
    	'user.getMobileCode':'platinfo/getMobileCode.htm',//获取手机验证码
    	'user.checkVerifyCode':'platinfo/resetCheckVerifyCode.htm',//校验验证码
    	'user.register':'platinfo/register.htm',//注册
    	'user.initType':'platinfo/initTermType.htm',//初始化协议类型
    	'user.agreement':'platinfo/agreement.htm',//协议
    	'user.login':'platinfo/login.htm',//登录
    	'user.logout':'platinfo/logout.htm',//退出登录
    	'user.resetLoginPwd':'platinfo/resetLoginPwd.htm',//找回登录密码
    	'user.updatePwd':'user/updatePwd.htm',//更新登录密码
    	'user.myAccount':'user/account.htm',//我的账户
    	'user.REGEX':'user/registerInfo.htm',//账号密码正则
    	'user.user':'user/user.htm',//个人信息
    	'user.setUserInfo':'user/setUserInfo.htm',//实名认证  	
    	'user.setUserInfo':'pay/fuyousignpay/fuyouPaySign.htm',//实名认证
    	'user.getCode':'pay/fuyousignpay/fuyouPaySignMsg.htm',//获取实名验证码
    	'user.toRelieve':'pay/fuyousignpay/fuyouTermination.htm',//签约成功后解约
    	'user.getEmailCode':'platinfo/getEmailCode.htm',//邮箱认证
    	'user.setPhone':'user/setUserPhone.htm',//手机认证
    	'user.setTranPwd':'user/setTranPwd.htm',//设置交易密码
    	'user.updateTranPwd':'user/updateTranPwd.htm',//修改交易密码
    	'user.findTranPwd':'user/forgotTranPwd.htm',//找回交易密码
    	'user.payUserRegister':'pay/payUserRegister.htm',//第三方注册
    	'user.myReward':'user/myAwardInfo.htm',//我的奖励
    	'user.experienceList':'user/myTyjList.htm',//我的体验金列表
    	'user.experienceInfo':'user/myExperienceItem.htm',//我的体验金详情
    	'user.myBankList':'user/myBankList.htm',//我的银行卡列表
    	'user.bankList':'user/bankList.htm',//银行卡列表
    	'user.addCard':'pay/bindCard.htm',//添加银行卡
    	'user.unbindCard':'pay/unbindCard.htm',//删除银行卡
    	'user.searchAddress':'pay/searchAddress.htm',//查询地址
    	'user.searchLoanAddress':'loan/searchAddress.htm',//查询个人借款地址
    	'user.fee':"user/fee.htm",//可绑定银行卡最大数量
    	'user.myTyjList':'user/myTyjList.htm',//体验金详情
    	'user.charge':'pay/fuyousignpay/fuyouPayCharge.htm',//充值
    	'user.withdraw':'pay/withdraw.htm',//提现
    	'user.tranLog':'user/tranRecordList.htm',//交易记录
		'user.myBidList':'user/myBidList.htm',// 我的借款列表
		'user.myLoanInfo':'user/repayInfo.htm',// 我的借款详情
		'user.replayEnd':'user/prepayment.htm',// 提前还款
		'user.replay':'user/payment.htm',// 还款
		'user.myRewardList':'user/myRewardList.htm',//我的红包列表
		'user.myCreditorInfo':'user/myCreditorInfo.htm',//我的投资
		'user.creditAssignmentList':'user/creditAssignmentList.htm',//债权转让
		'user.agreementView':'bid/publics/agreementView.htm',//查看标的合同
		'user.zqzrAgreementView':'bid/publics/zqzrAgreementView.htm',//查看债权合同
		'user.blzqagreementView':'bid/publics/blzqzrAgreementView.htm',//不良债权合同
		'user.transfer':'creditor/transfer.htm',//我的投资转让
		'user.cancelTransfer':'creditor/cancelTransfer.htm',//债权转让-取消转让
		'user.contractConstant':'bid/publics/getBackgroundValue.htm',//查询合同开关
		'user.saveBidContract':'bid/publics/ebqAgreementView.htm',//根据ID以及type查询合同
		
		'user.riskQuestions':'user/riskQuestions.htm',//风险评估问题
		'user.riskAssessment':'user/riskAssessment.htm',//风险评估
		
		
		'user.help':'platinfo/articleList.htm',//新手入门
		'user.helpInfo':'platinfo/articleItem.htm',//新手入门详情
		'user.mySpread':'user/mySpread.htm',//我的推广

		'user.addFeed':'user/addFeed.htm',//意见反馈
    	'user.myFeedBack':'user/myFeedBack.htm',//我的意见反馈
      	'user.changeCard':'pay/changeCard.htm',//富友更新银行卡
        'payment.fyouQueryBank':'pay/fyouQueryBank.htm', //富友银行卡状态
		
		'user.myScoreGetRecords':'user/mall/myScoreGetRecords.htm',//积分获取记录
		'user.myScoreExchangeRecords':'user/mall/myScoreExchangeRecords.htm',//积分获取记录
		'user.scoreRules':'user/mall/scoreRules.htm',//积分规则
		'user.myOrder':'user/mall/myOrder.htm',//我的订单
		'user.myOrderItem':'user/mall/myOrderItem.htm',//订单详情
		'user.userScore':'user/mall/userScore.htm',//签到
		'user.getDefaultAddress':'user/mall/getDefaultAddress.htm',//默认地址
		
		//mall 
		'mall.scoreMallList':'user/mall/scoreMallList.htm',//商品列表
		'mall.commodityDetails':'user/mall/commodityDetails.htm',//商品详情
		'mall.addShoppingCar':'user/mall/addShoppingCar.htm',//加入购物车
		'mall.showDetail':'user/mall/buyRecord.htm',//购买记录
		'mall.buyGoods':'user/mall/buyGoods.htm',//购买商品
		
		'mall.shoppingCar':'user/mall/shoppingCar.htm',//购物车
		'mall.delShoppingCar':'user/mall/delShoppingCar.htm',//删除购物车商品
		'mall.userScore':'user/mall/userScore.htm',//用户积分
		'mall.myHarvestAddress':'user/mall/myHarvestAddress.htm',//用户收货地址
		'mall.addAddress':'user/mall/addAddress.htm',//增加用户收货地址
		'mall.deleteAddress':'user/mall/deleteAddress.htm',//删除用户收货地址
		'mall.updateAddress':'user/mall/updateAddress.htm',//修改用户收货地址
		'mall.getAddressById':'user/mall/getAddressById.htm',//查询用户收货地址详情
    	//other
    	'other.letterList':'user/letterList.htm',  //站内信列表
    	'other.readLetter':'user/readLetter.htm',//读取站内信
    	"weixin.keysInfo":"weiXinServlet.htm?func=adf",//微信
    	"weixin.wXAccountLogin":"platinfo/wXAccountLogin.htm",//微信accountID登录

    };
//  var interfaceConfig = {
//      "server.path": domainLists[interFace].replace("/app/", "")
//  }
    var toGet = function(Obj) {
        var result = "";
        for (var key in Obj) {
            result += key + "=" + Obj[key] + "&";
        }
        result += new Date().getTime();
        return result;
    }
    Config.url = function(name, data) {
    	if(Config.base('envOption').requestType=='POST'){
    		return interFace+urlConfig[name];
    	}else if(Config.base('envOption').requestType=='GET'){
    		var params = toGet(data);
	        if (urlConfig[name]) {
	            return interFace+urlConfig[name]+ "?" + params;
	        }
    	}
        return "";
    };
    return Config.url;
});


//commonClass/stringTools.js
define('commonClass/stringTools', function() {
    var exports = {};


    function isNotEmpty(value) {
        if (value == undefined) {
            return false;
        }
        return !/^\s*$/.test(value);
    }


    exports.isNotEmpty = isNotEmpty;
    return exports;
});



//commonClass/dateTools.js
define("commonClass/dateTools", ['DMJSAll'], function(DMJS) {
    function formatTime(date) {
        if (typeof date == "string") {
            if (date.length == 8) {
                var arr = [date.substr(0, 4), date.substr(4, 2), date.substr(6, 2)];
            } else if (date.length == 14) {
                var arr = [date.substr(0, 4), date.substr(4, 2), date.substr(6, 2), date.substr(8, 2), date.substr(10, 2), date.substr(12, 2)];
            } else {
                var arr = date.split(/[^0-9]+/);
            }
            date = new Date(arr[0], arr[1] - 1, arr[2], arr[3] || 0, arr[4] || 0, arr[5] || 0);
        }
        return date;
    }
    //重写 四舍五入
    Number.prototype.toFixed = function(d)
	  {
	      var s=this+"";if(!d)d=0;
	      if(s.indexOf(".")==-1)s+=".";s+=new Array(d+1).join("0");
	      if (new RegExp("^(-|\\+)?(\\d+(\\.\\d{0,"+ (d+1) +"})?)\\d*$").test(s))
	      {
	          var s="0"+ RegExp.$2, pm=RegExp.$1, a=RegExp.$3.length, b=true;
	          if (a==d+2){a=s.match(/\d/g); if (parseInt(a[a.length-1])>4)
	          {
	              for(var i=a.length-2; i>=0; i--) {a[i] = parseInt(a[i])+1;
	              if(a[i]==10){a[i]=0; b=i!=1;} else break;}
	          }
	          s=a.join("").replace(new RegExp("(\\d+)(\\d{"+d+"})\\d$"),"$1.$2");
	      }if(b)s=s.substr(1);return (pm+s).replace(/\.$/, "");} return this+"";
	};
    
    /**
     * 比较2个时间的大小,
     * parm:
     *  t1 时间参数   形式为  这些方式  "2012.3.4 23:22:33" "2012.3.4" new Date()
     *  t2 时间参数   形式为  这些方式  "2012.3.4 23:22:33" "2012.3.4" new Date()
     *  return: 返回-1 0 1
     *    t1>t2 返回1 
     *    t1=t2 返回0
     * 	  t1<t2  返回-1 
     */
    DMJS.compareDate = function(t1, t2) {
        if (!t1 || !t2) {
            return "";
        }
        t1 = formatTime(t1);
        t2 = formatTime(t2);
        var result = t1.getTime() - t2.getTime();
        return result == 0
                ? 0
                : result > 0 ? 1 : -1
    };
     //转换金额
	DMJS.changeAmount=function(amount){ 
				if(!amount) return 0.00;
                var r= /^[1-9]?[0-9]*\.[0-9]*$/;
                amount=(amount+"").indexOf(",")!=-1 ? amount.replace(/,/g,"")  :amount  ;
                if(r.test(amount) && parseInt(amount) < amount){  
                	if(amount<10000)return amount;
                 	amount=(amount+"").split(".").length>=2 ? (amount+"").split(".")[0] : amount;
                }
                	if(parseInt(amount) >= 100000000){
                		 return (amount/100000000).toFixed(2) + "<span class='fn-s-11 fn-c-gray'>亿</span>";
                	}
                    else if(parseInt(amount) >= 10000){
                        return (amount/10000).toFixed(2) + "<span class='fn-s-11 fn-c-gray'>万</span>";
                    }else{
                        return (amount+"").split(".").length>=2 ? amount : (amount*1).toFixed(2);
                    }
           };

    /**
     * 时间推移的功能,
     * parm:
     *  t1 当前时间   形式为  这些方式  "2012.3.4 23:22:33" "2012.3.4" new Date()
     *  num 推移的次数    
     *  unit  单位   只能为("y"或者"m"或者"d")
     *  return: 返回推移后的时间对象
     */
    DMJS.diffDateAdd = function(t1, num, unit) {
        if (!t1 || num == undefined || !unit) {
            return "";
        }
        t1 = formatTime(t1);
        var unit = {
            y: 1000 * 60 * 60 * 24 * 365,
            m: 1000 * 60 * 60 * 24 * 30,
            d: 1000 * 60 * 60 * 24
        }[unit];
        return new Date(t1.getTime() + num * unit);
    };

    /**
     * 返回2个时间间隔的天数,
     * parm:
     *  t1 时间参数   形式为  这些方式  "2012.3.4 23:22:33" "2012.3.4" new Date()
     *  t2 时间参数   形式为  这些方式  "2012.3.4 23:22:33" "2012.3.4" new Date() 
     *  返回2个时间之前的天数
     */
    DMJS.diffDate = function(t1, t2) {
        if (!t1 || !t2) {
            return false;
        }
        t1 = formatTime(t1);
        t2 = formatTime(t2);
        return parseInt(Math.abs(t2.getTime() - t1.getTime()) / (1000 * 60 * 60 * 24));
    };

    /**
     * 获取系统时间,
     * parm:
     *  successFun  请求成功的回调函数
     *  format      需要返回的时间格式
     *  这个函数没有返回   处理要写到回调函数中....
     */
    DMJS.systemTime = function(successFun, format) {
        if (typeof successFun != "function")
            return;
        DMJS.Request("qryEbankSysTime", {
            cancelLightbox: true,
            success: function(time) {
                var time = parseInt(time.responseBody.sysTime);
                format
                        && (time = DMJS.formatDate(new Date(time), format));
                successFun(time);
            },
            error: function() {
                var t = new Date();
                format
                        ? successFun(DMJS.formatDate(t, format))
                        : successFun(t.getTime());
            }
        });
    };

    DMJS.formatDate = function(date, format) {
        if (!date || date == "0") {
            return "";
        }
        if (!format) {
            format = "yyyy-MM-dd hh:mm:ss";
        }
        if (typeof date == "string") {
            if (date.length == 8) {
                var arr = [date.substr(0, 4), date.substr(4, 2), date.substr(6, 2)];
            } else if (date.length == 14) {
                var arr = [date.substr(0, 4), date.substr(4, 2), date.substr(6, 2), date.substr(8, 2), date.substr(10, 2), date.substr(12, 2)];
            } else {
                var arr = date.split(/[^0-9]+/);
            }

            format = format.replace(/([a-z])\1+/ig, function(all, $1) {
                var result = {
                    y: ~~arr[0],
                    M: ~~arr[1],
                    d: ~~arr[2],
                    h: ~~arr[3],
                    m: ~~arr[4],
                    s: ~~arr[5]
                }[$1];
                if (result && result < 10) {
                    result = "0" + result
                }
                return result || "";
            });
            return format;
        }
        format = format.replace(/([a-z])\1+/ig, function(all) {
            var first = all.charAt(0);
            if ("y M d h m s".indexOf(first) >= 0) {
                if (first == "y") {
                    return all.length > 2
                            ? date.getFullYear()
                            : (date.getFullYear() + "").substr(2);
                }
                var result = {
                    M: date.getMonth() + 1,
                    d: date.getDate(),
                    h: date.getHours(),
                    m: date.getMinutes(),
                    s: date.getSeconds()
                }[first];
                result < 10
                        && (result = "0" + result);
                return result;
            } else {
                return all;
            }
        });
        return format;
    };
     DMJS.agreementEnable = function(type, eleId,successCall) {
        DMJS.Request("user.initType",
        	{
            data:{type:type},
            type: "get",
            dataType: "jsonp",
            cancelLightbox: true,
			"noCache":true,
			"success":function(response){
				if(!response.data.agreementName&&!response.data.agreementType)
				    $("#"+eleId).addClass("uhide");
				   typeof successCall=='function'&&successCall();
			},
			'error':function(response){console.log(response);}
        	});
        
    };
});
//request.js
define("commonClass/request", ['Lib/base64', 'DMJSAll', 'xml2json', 'commonClass/commonTools', 'commonConfig/url'], function(Base64, DMJS, xml2json, x, url) {
    // 请求的唯一标识
    var uuid = 1;
    // 请求中的XHR队列
    var xhrs = {};
    var requestData = {};
    var limits = {};
    var base64 = new Base64(Config.base("trunKey"));
    var length;

    var ajaxSettings = $.ajaxSettings;

    var responseCache = {
        'user.myAccount': false
    }

    // 接口数据缓存对象
    DMJS.removeCache = function(key) {
        for (var _key in responseCache) {
            if (!key || key == _key) {
                responseCache[key] = false;
            }
        }
    }
    DMJS.killRequest = function(urlList) {
        // urlList 需要取消掉的url的地址     如果urlList为空 表示取消掉所有的请求
        if (urlList) {
            if (!$.isArray(urlList)) {
                urlList = [urlList];
            }
            $.each(urlList, function(i, url) {
                if (url in xhrs) {
                    xhrs[url].isAbort = "1";
                    xhrs[url].abort();
                    delete xhrs[url];
                }
            });
        } else {
            for (key in xhrs) {
                if (!xhrs[key].unBort) {
                    xhrs[key].isAbort = "1";
                    xhrs[key].abort();
                }
            }
            if (window.wrapView) {
                wrapView.lightBox.hide();
                wrapView.FlipPrompt.closeFlayer();
            }
        }
        ajaxSettings.lightboxHide = true;
    };
    DMJS.getRequestLength = function() {
        return xhrs.length;
    }
    //去掉挡板的Element
    function removeElement(obj, o) {
        var my = arguments.callee;
        for (var key in obj) {
            var val = obj[key],
                    isObj = Object.prototype.toString.call(val) == "[object Object]";
            if (isObj) {
                if (val.Element) {
                    $.isArray(val.Element)
                            ? o[key] = val.Element
                            : o[key] = [val.Element]
                } else {
                    o[key] = {};
                    my(val, o[key]);
                }
            } else {
                o[key] = val;
            }
        }
    }

    //格式化 options
    function formatOptions(options) {
        var isJson = !Config.base("isPADock"),
                channelType = Config.base("channelType");
        options.urlKey = options.url;
        options.url = Config.url(options.url);

        //60秒后断开请求....
        options.timeout = options.timeout || 60000;
        // 响应后处理
        var complete = options.complete || function() {
        };
        // 成功回调函数
        var success = options.success || function() {
        };
        var error = options.error || function() {
        };
        var doRetry = options.doRetry || false;
        // 为当前请求生成唯一标识
        //options.xhrID = uuid++;
        !options.type
                && (options.type = "post");

        options.lightboxHide == false
                ? ajaxSettings.lightboxHide = false
                : ajaxSettings.lightboxHide = true;

        ajaxSettings.cancelLightbox = options.cancelLightbox;

        if (!options.dataType) {
            if (isJson) {
                options.dataType = 'json';
                options.data.responseDataType = "JSON";
            } else {
                options.dataType = 'text/xml';
            }
        }

        options.error = function(response, type) {
            //alert("请求error"+"type"+":"+response);
            $.ajaxSettings.complete();
            if (!response) {
                return;
            }
            if (response.isAbort) {
                return;
            }
            //如果ajax的请求是要求json格式而接口返回非json数据 也会进入error 类型是parsererror
            if (doRetry) {
                if (typeof doRetry == "object" && doRetry.before) {
                    doRetry.before();
                }
                var request = requestData[response.url];
                wrapView.FlipPrompt.confirm({
                    title: Config.lang("alertCommonTitle"),
                    content: "<span>网络连接超时,请检查您的网络设置或重试？</span>",
                    FBntconfirm: "稍后再试",
                    FBntcancel: "重试",
                    FBntCancelColor: "pop_btn_orange"
                }, function() {
                }, function() {
                    setTimeout(function() {
                        if (typeof doRetry == "function") {
                            doRetry(function() {
                                DMJS.Request(response.url, request);
                            });
                        } else if (typeof doRetry == "object" && doRetry.after) {
                            doRetry.after(function() {
                                DMJS.Request(response.url, request);
                            });
                        } else {
                            DMJS.Request(response.url, request);
                        }
                    }, 100);
                });
            } else {
                error(response);
                return;
            }
//			if(type == "parsererror"){
//				error(response);
//				return;
//			}
//			if(type == "error" && !response.result ){
//				error(response);
//				return;
//			}
            //如果是手动取消掉的   不需要弹框			
//			if(!response.isAbort){
//				if(!response.noerror){
//					if(!option.doRetry){
//						error(response);
//						return;
//					}
//					
//				}
//			}
        }

        options.complete = function(response) {
            // 移除队列
            delete requestData[options.urlKey];
            delete xhrs[options.urlKey];
            try {
                if (complete) {
                    complete(response);
                }
            } catch (e) {
                $.ajaxSettings.complete();
                if (wrapView && wrapView.FlipPrompt) {
                    wrapView.FlipPrompt.colse(true);
                }
            }
            $.ajaxSettings.complete();
        }
        var data = options.data;
        if (data && Config.base("requestEncrypt") && !data.sign) {
            var strData = JSON.stringify(data);
            console.log(strData);
            options.data = {
                "sign": base64.encode(strData)
            }
        }
    }

    /*
     * 串行发送请求
     */
    function processResquestSyn(lists, responseLists, errorList, callback, error) {
        //alert("调用process request")
        var my = arguments.callee,
                isJson = !Config.base("isPADock"),
                options = lists.shift();

        formatOptions(options);
        //alert("请求URL:"+options.url);
        options.error = function(response) {
            responseLists.push("error");
            //alert("请求error:"+error);
            errorList.push(true);
            var n = length - lists.length - 1;
            if (wrapView.headerView) {
                wrapView.headerView.enableEvent();
            }
            //如果还有其他请求  并且没有限制条件        	
            if (lists.length > 0 && !limits[n]) {
                my(lists, responseLists, errorList, callback, error);
            } else {
                length > 1
                        && callback.apply(null, responseLists);
                error.apply(null, errorList);
            }
        }

        var success = function(response) {
            //alert("请求成功");
            //如果是挡板 并且是 没有缓存数据		                        
            if (!isJson && !responseCache[options.urlKey]) {
                result = $.xml2json(response);
                response = {};
                removeElement(result, response);
            }
            responseLists.push(response);
            errorList.push(undefined);


            if (lists.length > 0) {
                var n = length - lists.length - 1;
                if (!limits[n] || (limits[n] && limits[n](response))) {
                    my(lists, responseLists, errorList, callback, error);
                } else {
                    $.each(new Array(length - responseLists.length), function() {
                        responseLists.push("error");
                    });

                    try {
                        //当请求全部正确的时候
                        callback.apply(null, responseLists);
                    } catch (e) {
                        console.log("exception,error:" + e.message);
                        wrapView.FlipPrompt.alert({
                            title: Config.lang("alertCommonTitle"),
                            content: "系统繁忙，请稍后重试！"
                        }, function() {

                        });
                    }
                }
            } else {
                try {
                    callback.apply(this, responseLists);
                    errorList.join("") != ""
                            && error.apply(null, errorList);
                } catch (e) {
                    console.log("exception,error:" + e.message);
                    wrapView.FlipPrompt.alert({
                        title: Config.lang("alertCommonTitle"),
                        content: "系统繁忙，请稍后重试！"
                    }, function() {

                    });
                }
            }
        }

        //如果设置了缓存  并且存在缓存的数据  不从服务器拉数据.......
        if (!options.noCache && responseCache[options.urlKey]) {
            success(responseCache[options.urlKey]);
        } else {
            options.success = success;
            if (typeof options.beforeSendCallback == "function") {
                ajaxSettings.beforeSendCallback = function(xhr) {
                    options.beforeSendCallback(xhr)
                }
            }
            var xhr = $.ajax(options);
            xhr.url = options.urlKey;
            xhrs[options.urlKey] = xhr;

        }
    }

    /*
     同步发送请求
     */
    function processResquest(lists, responseLists, callback, error) {
        //alert("请求process request")
        var isJson = !Config.base("isPADock"),
                len = lists.length,
                errArr = new Array(len),
                arr = new Array(len);

        $.each(lists, function(i, options) {
            options.error = function(response) {
                //alert("请求error:");
                len--;
                arr[i] = "error";
                errArr[i] = true;
                if (wrapView.headerView) {
                    wrapView.headerView.enableEvent();
                }
                if (len == 0) {
                    error(response);
                }
            };
            formatOptions(options);
            //alert("请求url"+options.url);
            var success = function(response) {
                try {
                    if (responseCache[options.urlKey] != undefined) {
                        responseCache[options.urlKey] = response;
                    }
                    //当请求全部正确的时候
                    callback.apply(null, [response]);
                    //errArr.join("")!=""
                    //	&&error.apply(null,errArr); 						
                } catch (e) {
                    console.log("exception,error:" + e.message + "\r\n stack:" + e.stack);
                    wrapView.FlipPrompt.alert({
                        title: Config.lang("alertCommonTitle"),
                        content: "系统繁忙，请稍后重试！"
                    }, function() {
                    });
                }
            }
            //如果设置了缓存  并且存在缓存的数据  不从服务器拉数据.......
            if (!options.noCache && responseCache[options.urlKey]) {
                success(responseCache[options.urlKey]);
            } else {
                options.success = success;
                if (typeof options.beforeSendCallback == "function") {
                    ajaxSettings.beforeSendCallback = function(xhr) {
                        options.beforeSendCallback(xhr)
                    }
                }
                var xhr = $.ajax(options);
                xhr.url = options.urlKey;
                xhr.unBort = options.unBort;
                xhrs[options.urlKey] = xhr;
            }

        });
    }


    DMJS.Request = function(name, options) {
        try {
            limits = {};
            var success = $.isFunction(options.success) ? options.success : function() {
            },
                    error = $.isFunction(options.error) ? options.error : function() {
            };
            if ($.isArray(name)) {
                length = name.length;
                if (options.syn) {
                    $.each(name, function(i, d) {
                        requestData[d.url] = options;
                        if (typeof d.limit == "function")
                            limits[i] = d.limit;
                    });
                    //alert("流程1");
                    processResquestSyn(name, [], [], success, error);
                } else {
                    //alert("流程2");
                    processResquest(name, [], success, error);
                }
            } else {
                //alert("流程3")
                options.url = name || '';
                requestData[name] = options;
                processResquest([options], [], success, error);
                length = 1;
            }
        } catch (e) {
            //alert("请求error:"+e.message)
        }
    }
    return DMJS.Request;
});
//commonTools.js
define("commonClass/commonTools", ['commonConfig/base', 'zepto', 'underscore', 'commonClass/scroll/iscroll', 'commonClass/dateTools', 'commonTool/localData'],
        function(config, $, _, iscroll) {

            DMJS.CommonTools = {
            	hash_clear:function(){
				           	window.listHash.length>0 ? window.listHash.splice(0,window.listHash.length) : null;
				            console.log("hash length: "+window.listHash.length);
				},
            	//获取备案号
            	getICP:function(){
                	   DMJS.currentView.controller.commonModel.getICP({
                		   data:{key:'SITE_BEIAN'},
                           "success": function(_data) {
                           	    DMJS.ICP=_data.data.filingNum;
                           	    DMJS.titleName=_data.data.siteName;
                           	    DMJS.tel=_data.data.tel;
                           	   // DMJS.siteDomain=_data.data.siteDomain;
                        	   $("#ICP").html(_data.data.filingNum);
                        	   $("#titleName").html(_data.data.siteName);
                        	   $("#tel")[0].href="tel:"+_data.data.tel.replace(/\s*-\s/g,"");
                        	   $("#tel").html(_data.data.tel);
                        	  // $("#pcUrl")[0].href=_data.data.siteDomain;
                           }
                       });
                },
                backConst:function(parms,func){
                	DMJS.currentView.controller.commonModel.getConfig({
		            	"data":{"configName":parms},
		            	"success":function(_data){
		            		this.Const=_data.data;
		            		if(func){
		            			func();
		            		}else{
		            			return _data.data;
		            		}
		            		
		            		
		        		},
		        		'error': function(response) {
								DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"), response.description);
						}
		            });
                },
                 tongLian:function(){
                var str="<div id='layer' style='height:100%;width: 100%; position: fixed;  top :0;background:rgba(0,0,0,.8)'>"
						+"<div class='hei-inherit  ub  ub-pc'><div class='img-rechargeTL' >"
						+"</div></div></div>";
				 var dom=$(str);
				 $(dom).click(function(){
				 	dom.toggle();
				 });
				 $('body').append(dom);
                },
               isWeiXin:function (){
								var ua = window.navigator.userAgent.toLowerCase();
								if(ua.match(/MicroMessenger/i) == 'micromessenger'){
								        return true;
								    }else{
								        return false;
								    }
								},
                 fmoney:function(s , n){
               if(s > 0){
                    n = n > 0 && n <= 20 ? n : 2;  
                    s = parseFloat((s + "").replace(/[^\d\.-]/g, "")).toFixed(n) + "";  
                    var l = s.split(".")[0].split("").reverse(),  
                    r = s.split(".")[1];  
                    t = "";  
                    for(i = 0; i < l.length; i ++ )  
                    {  
                       t += l[i] + ((i + 1) % 3 == 0 && (i + 1) != l.length ? "," : "");  
                    }  
                    return t.split("").reverse().join("") + "." + r;   
                }else if(s == 0){
                    return "0.00";
                }else{
                    return s;
                }
           },
                // js 遍历所有Cookie
                delCookies: function()
                {
                    var strCookie = document.cookie;
                    var arrCookie = strCookie.split("; "); // 将多cookie切割为多个名/值对
                    for (var i = 0; i < arrCookie.length; i++)
                    { // 遍历cookie数组，处理每个cookie对
                        var arr = arrCookie[i].split("=");
                        if (arr.length > 0)
                            DMJS.CommonTools.delCookie(arr[0]);
                    }
                },
                getCookieVal: function(offset)
                {
                    var endstr = document.cookie.indexOf(";", offset);
                    if (endstr == -1)
                        endstr = document.cookie.length;
                    return decodeURIComponent(document.cookie.substring(offset, endstr));
                },
                delCookie: function(name)
                {
                    var exp = new Date();
                    exp.setTime(exp.getTime() - 1);
                    var cval = DMJS.CommonTools.getCookie(name);
                    if(name=='userInfo'){
                    	cval=encodeURI(cval);
                    }
                    document.cookie = name + "=" + cval + "; expires=" + exp.toGMTString();
                },
                imgDown:function($img){
                	var imgL=$img.length,self=this;
            	    
	          		  var timer = setInterval(function() {
	          			  var num=0;
	          			  for(var i=0;i<imgL;i++){
	          				  if ($($img[i]).height()!=0) {
	                           	 num++;
	                             }
	          				  
	          			  }
	          			  if(num==imgL){
	          				self.loadScroller();
	                           	
	                            clearInterval(timer);
	          			  }
	          			
	                    }, 100);
                },
                getCookie: function(name)
                {
                    var arg = name + "=";
                    var alen = arg.length;
                    var clen = document.cookie.length;
                    var i = 0;
                    while (i < clen)
                    {
                        var j = i + alen;
                        if (document.cookie.substring(i, j) == arg)
                            return DMJS.CommonTools.getCookieVal(j);
                        i = document.cookie.indexOf(" ", i) + 1;
                        if (i == 0)
                            break;
                    }
                    return null;
                },
                /**
                 * 登入区外加签时间［@"yyyyMMddhhmmss"］
                 */
                getSignTime: function() {
                    var curTime = DMJS.CommonTools.getSystemTime("yyyyMMddhhmmss");
                    return curTime;
                },
                removeMoneyNumberComma: function(moneyNum) {
                    moneyNum = (moneyNum + '').replace(/,/g, "");
                    moneyNum = isNaN((1 * moneyNum).toFixed(2)) ? (new Number(0).toFixed(2)) : (1 * moneyNum).toFixed(2);
                    return (moneyNum + '').replace(/\.00/g, '');

                },
                /**
                 * 保持session临时解决方案
                 */
                keepSessionAlive: function() {
                    var timerId = setInterval(function() {
//                        DMJS.Request("sys.keepSession", {
//                            type: "jsonp",
//                            cancelLightbox: true,
//                            success: function(response) {
//                                alert("response")
//                            },
//                            error: function(response) {
//                            }
//                        });
                    }, 600 * 1000);
                },
                /**
                 * 登入区外加签［@"channelType#yyyyMMddhhmmss"］
                 */
                getSignData: function() {
                    var channelType = Config.base("channelType");
                    var curTime = DMJS.CommonTools.getSignTime();
                    var sign = channelType + "#" + curTime;
                    var result = DMJS.CommonTools.desEncode(sign);
                    return result;
                },
                /**
                 *
                 * [根据手机网银规范格式化金额数字]
                 * moneyNum:格式化前的金额数字或金额数字字符串
                 * ruturn:格式化后的金额数字
                 *
                 * 规则：
                 * 1.金额数字小数后小于两位，补零到小数位两位
                 * 2.金额数字小数后两位，不处理
                 * 3.金额数字小数大于两位，四舍五入至小数位两位
                 *
                 * 注意：
                 * 1.这里利用了JS的类型转换特性，永远不会报错
                 * 2.isNaN==true默认给值0.00
                 */
                formatMoneyNumber: function(moneyNum) {
                    var result = isNaN((1 * moneyNum).toFixed(2)) ? (new Number(0).toFixed(2)) : (1 * moneyNum).toFixed(2);
                    return /\./.test(result) ? result.replace(/(\d{1,3})(?=(\d{3})+\.)/g, "$1,") : result.replace(/(\d{1,3})(?=(\d{3})+\b)/g, "$1,");
                },
                getTextForPwdKeyboard: function(inputField) {

                    var resultText = "";
                    if (inputField.hasClass == null) {
                        return inputField.val();
                    }
                    //android对进行掩码了的数据，得采用另外的形式进行获取
                    if (inputField.hasClass("keyboard_bank_pwd") ||
                            inputField.hasClass("keyboard_login_pwd") ||
                            inputField.hasClass("keyboard_acc_num") ||
                            inputField.hasClass("keyboard_otp")) {
                        //取款密码得采用这种方式获取  否则获取的是********
                        resultText = inputField.attr("paText");
                    } else {
                        resultText = inputField.val();
                    }
                    return resultText;
                },
                setTextForPwdKeyboard: function(inputField, changeText) {
                    if (inputField.hasClass == null) {
                        return inputField.val(text);
                    }
                    //android对进行掩码了的数据，得采用另外的形式进行获取
                    if (inputField.hasClass("keyboard_bank_pwd") ||
                            inputField.hasClass("keyboard_login_pwd") ||
                            inputField.hasClass("keyboard_acc_num") ||
                            inputField.hasClass("keyboard_otp")) {
                        //取款密码得采用这种方式获取  否则获取的是********
                        inputField.attr("paText", changeText);
                        var isNeedHide = false;
                        if (inputField.hasClass("keyboard_otp")) {
                            isNeedHide = false;
                        }
                        else if (inputField.hasClass("keyboard_login_pwd")) {
                            isNeedHide = true;
                        }
                        else if (inputField.hasClass("keyboard_bank_pwd")) {
                            isNeedHide = true;
                        }
                        else if (inputField.hasClass("keyboard_acc_num")) {
                            isNeedHide = false;
                        }

                        var showValue = "";
                        //如果需要掩码
                        if (isNeedHide) {
                            for (var i = 0; i < changeText.length; i++) {
                                showValue = showValue + "*";
                            }
                        } else {
                            showValue = changeText;
                        }
                        //如果内容为空
                        if (showValue.length <= 0) {
                            var placeholder = inputField.attr("placeholder");
                            //如果不需要处理placeholder
                            if (placeholder == null || placeholder.length <= 0) {
                                //删除文字置灰色  placeholder属性
                                inputField.removeClass("f_c_grey");
                                inputField.val("");
                            }
                            //如果需要处理placeholder
                            else {
                                //删除文字置灰色  placeholder属性
                                inputField.removeClass("f_c_grey");
                                //添加文字置灰色 placeholder属性
                                inputField.addClass("f_c_grey");
                                inputField.val(placeholder);
                            }
                        } else {
                            //删除文字置灰色  placeholder属性
                            inputField.removeClass("f_c_grey");
                            inputField.val(showValue);
                        }
                    } else {
                        inputField.val(changeText);
                    }
                },
                /**
                 *  @author EX-LUOCHUN001  2013.03.28
                 *  一个简单的枚举类,主要和 getDataByKey方法配合使用
                 */
                DateType: {
                    //枚举对象
                    OBJECT: "object",
                    //枚举字符
                    STRING: "String",
                    //枚举数组
                    ARRAY: "Array",
                    /**
                     *  @author EX-LUOCHUN001  2013.03.28
                     *  如果找不到相应数据,则根据dataType给出一个默认值
                     *  parm:
                     *      @param {String} dataType 想要获取的数据类型,与DMJS.CommonTools.DateType枚举相对应起来
                     *  return: 默认值
                     */
                    getDefaultValue: function(dataType) {
                        if (dataType === DMJS.CommonTools.DateType.OBJECT) {
                            return null;
                        } else if (dataType === DMJS.CommonTools.DateType.STRING) {
                            return "";
                        } else if (dataType === DMJS.CommonTools.DateType.ARRAY) {
                            var emptyArray = [];
                            return emptyArray;
                        } else {
                            return null;
                        }
                    },
                    /**
                     *  @author EX-LUOCHUN001  2013.03.28
                     *  想要获取的数据类型是否是能处理的数据类型
                     *  parm:
                     *      @param {String} dataType 数据类型,与DMJS.CommonTools.DateType枚举相对应起来
                     *  return: 对应数据类型默认值
                     */
                    isContainType: function(dataType) {
                        if (dataType === DMJS.CommonTools.DateType.OBJECT || dataType === DMJS.CommonTools.DateType.STRING || dataType === DMJS.CommonTools.DateType.ARRAY) {
                            return true;
                        }
                        return false;
                    },
                },
              
                /**
                 *获取剪贴板数据方法
                 */
					getClipboardText:function (event){
						var clipboardData = event.clipboardData || window.clipboardData;
						return clipboardData.getData("text");
					},
					
					
					
					/**
	                 *设置剪贴板数据
	                 */
					setClipboardText:function (event, value){
						if(event.clipboardData){
							return event.clipboardData.setData("text/plain", value);
						}else if(window.clipboardData){
							return window.clipboardData.setData("text", value);
						}
					},
                
                /**
                 *获取光标的位置 
                 */
				getPositionForInput: function(ctrl) {
                    var CaretPos = 0;
                    if (document.selection) {// IE Support
                        ctrl.focus();
                        var Sel = document.selection.createRange();
                        Sel.moveStart('character', -ctrl.value.length);
                        CaretPos = Sel.text.length;
                    } else if (ctrl.selectionStart || ctrl.selectionStart == '0') {// Firefox support
                        CaretPos = ctrl.selectionStart;
                    }
                    return (CaretPos);
                },
                showLightBoxAlert: function() {
                    wrapView.lightBox.show(true);
                },
                showLightBoxAlert1: function() {
                    wrapView.lightBox.show(false);
                },
                hideLightBoxAlert: function() {
                    setTimeout(function() {
                        wrapView.lightBox.hide();
                        wrapView.FlipPrompt.closeFlayer();
                    }, 100);
                },
                alertCommon: function($title, $content, func) {
                    $("input,select").each(function() {
                        $(this)[0].blur();
                    });
                    DMJS.CommonTools.showLightBoxAlert1();
                    //弹出3S自动关闭
                    if(!func){
                    	setTimeout(function(){
                    		 DMJS.CommonTools.hideLightBoxAlert();
                    	},4000);
                    }
                    wrapView.FlipPrompt.alert(
                            {title: $title, content: $content, displaySure: func ? "yes" : 'no'},
                    function() {

                      DMJS.CommonTools.hideLightBoxAlert();
                        if (func) {
                            func();
                        }
                    }
                    );
                },
                popTip: function(tip, count) {
                    wrapView.FlipPrompt.tip(tip, false);
                    setTimeout(function() {
                        wrapView.FlipPrompt.colse(true);
                    }, count || 800);
                },
                showBrowser: function(option) {
                    require(['commonView/BrowserView'], function(BrowserView) {
                        var propView = new BrowserView(option);
                        return propView.render();
                    });
                },
                showOptionMenu: function(option) {
                    require(['commonView/OptionMenuView'], function(optionMenuView) {
                        var optionMenuView = new optionMenuView();
                        return optionMenuView.render();
                    });
                },
                autoClearLoadMoreDom: function(pageBean, loadMoreObject) {
                    if (!pageBean) {
                        return;
                    }
                    if (pageBean.totalResults == 0
                            || pageBean.currentPage == pageBean.totalPageSize) {
                        loadMoreObject.hide();
                    }
                },
                dealWithNoData: function(pageBean, lastItemDom) {
                    if (pageBean.totalResults == 0) {
                        lastItemDom.before("<div id='noDataTips'>暂无此数据！</div>");
                    }
                },
                clearFooter: function() {
                    if ($("footer")[0]) {
                        $("footer").remove();
                        wrapView.footerView = null;
                    }
                },
                clearHeader: function() {
                    if ($("header")[0]) {
                        $("header").remove();
                        wrapView.headerView = null;
                    }
                },
                /**
                 *函数说明：日期控件通用函数
                 *参数说明：
                 *       id:     控件ID
                 *       params: 传给日期控件的参数(其中的type: 控件格式(month-显示年月, date-显示年月日, datetime-显示年月日时分, time-显示时分))
                 *       pattern:样式
                 *               (theme: 外观,有android,ios,wp等外观,可传参数--android, android-ics light, android-ics, ios,
                 *                       jqm, sense-ui, wp light, wp, 默认是sense-ui；
                 *                mode:  滚动样式: scroller-滚动; clickpick-单击加减; mixed-滚动单击加减混合;
                 *                display:显示位置： modal-模态弹出, inline-内嵌到页面, bubble-箭头注释的形式, top-顶部弹出, bottom-底部弹出;
                 *                lang:  语言，默认是英语en-US，zh为中文)
                 */
                commonDateSelect: function(DateParam, callback) {

                    var id = DateParam.id;
                    var params = DateParam.type;
                    var maxYear = DateParam.maxDate;
                    var minYear = DateParam.minDate;
                    //设置日期控件显示顺序 年-月-日
                    if ('undefined' == typeof params.dateOrder) {
                        params.dateOrder = 'yymmdd';
                    }
                    //设置返回的日期格式
                    if ('undefined' == typeof params.dateFormat) {
                        if ('month' == params.preset) {
                            params.dateFormat = 'yy-mm';
                        }
                        else if ('date' == params.preset) {
                            params.dateFormat = 'yy-mm-dd';
                        }
                        else if ('datetime' == params.preset) {
                            params.dateFormat = 'yy-mm-dd';
                        } else if ('year' == params.preset) {
                            params.dateFormat = 'yy';
                        }
                    }
                    //设置控件显示样式
                    var pattern = {
                        theme: 'android-ics light',
                        mode: 'scroller',
                        display: 'modal',
                        lang: 'zh',
                        setText: '确定',
                        cancelText: '取消',
                        maxDate: maxYear,
                        minDate: minYear
                    };
                    if (callback != undefined && typeof callback == 'function') {
                        params.callback = callback;
                    }
                    //弹出控件
                    jQuery('#' + id).val('')
                            .scroller('destroy')
                            .scroller(
                                    jQuery.extend(
                                            params,
                                            pattern
                                            )
                                    );
                },
                /**
                 * textareaId  显示出来的textarea文本输入域的id 
                 * divID 需要模拟的元素标签的高度
                 * maxHeight 允许改变高度的最大高度
                 * minHeight 最小高度（就是textarea本身的高度）
                 */
                autoHeight: function(textareaId, divID, maxHeight, minHeight) {
                    if (!typeof textareaId == 'string' && !typeof divID == 'string' && !typeof maxHeight == 'number' && !typeof minHeight == 'number') {
                        console.log('textarea自适应高度传入参数不对');
                        return;
                    }
                    if (maxHeight < minHeight) {
                        var min_height = maxHeight;
                        maxHeight = minHeight;
                        minHeight = min_height;
                    }
                    var textarea = document.getElementById(textareaId);
                    var cloneTextarea = document.getElementById(divID);
                    cloneTextarea.innerHTML = textarea.value;
                    cloneTextarea.style.height = 'initial';
                    var height = cloneTextarea.offsetHeight;
                    if (height > minHeight && height < maxHeight) {
                        textarea.style.height = cloneTextarea.offsetHeight + 'px';
                    } else if (height <= minHeight) {
                        textarea.style.height = minHeight + 'px';
                    }
                    if (height >= maxHeight) {
                        cloneTextarea.style.overflow = 'hidden';
                        textarea.style.height = maxHeight + 'px';
                        cloneTextarea.style.height = maxHeight + 'px';
                        return;
                    }
                },
                /**
                 * 显示级联菜单
                 */
                showCascadeList: function(listParam) {

                    var id = listParam.id;
                    var parentText = listParam.parentText;
                    var childrenText = listParam.childrenText;

                    var listHtml = "";

                    for (var i = 0; i < childrenText.length; i++) {
                        listHtml += "<li>" + parentText[i] + "<ul>";
                        for (var j = 0; j < childrenText[i].length; j++) {
                            listHtml += "<li>" + childrenText[i][j] + "</li>";
                        }
                        listHtml += "</ul></li>";
                    }
                    jQuery("#" + id).html(listHtml);

                    var opt = {
                        tree_list: {preset: 'list'}
                    }
                    jQuery('#' + id).val('').scroller('destroy').scroller(jQuery.extend(opt['tree_list'], {
                        theme: 'android-ics light',
                        mode: 'scroller',
                        display: 'modal',
                        lang: 'zh',
                        setText: '确定',
                        cancelText: '取消'
                    }));
                },
                /**
                 * 显示多选框控件 
                 */
                commonCheckBox: function(self, $flag) {
                    var thisView = $("#" + self.id);
                    var flag = $flag;
                    var checkBoxHtml = "";
                    checkBoxHtml += "<div id='" + flag + "_showInfo' class='checkbox_accreditNotice' style='display:none' >";
                    checkBoxHtml += "<div class='checkbox_innerContent' style='position: relative;'>";
                    checkBoxHtml += "<div class='checkbox_dwv'>请选择</div>";
                    checkBoxHtml += "<div id='" + flag + "_selectWarpper'>";
                    checkBoxHtml += "<div id='" + flag + "_showIns' class='checkbox_showIns' >";
                    checkBoxHtml += "</div></div><div class='checkbox_dwbc'>";
                    checkBoxHtml += "<div class='checkbox_btn'  id='" + flag + "_sureBtn'>确定</div>";
                    checkBoxHtml += "<div class='checkbox_btn checkbox_right'  id='" + flag + "_cancelBtn'>取消</div>";
                    checkBoxHtml += "</div></div></div>";
                    thisView.after(checkBoxHtml);
                    var itemValue = self.itemValue;
                    var displayValue = self.displayValue;
                    var liHTML = "";
                    for (var i = 0; i < itemValue.length; i++) {
                        liHTML += "<li><input type='checkbox' value=" + itemValue[i] + " displayValue=" + displayValue[i] + " name='" + flag + "_checkInput' /><span>" + displayValue[i] + "</span></li>"
                    }
                    jQuery("#" + flag + "_showIns").append(liHTML);

                    jQuery(':checkbox').iCheck({
                        checkboxClass: 'icheckbox_square-orange',
                        increaseArea: '20%'
                    });
                    jQuery("#" + flag + "_showIns li").bind("tap", function() {
                        jQuery(this).find(".iCheck-helper").click();
                    });
                },
                /*弹出多选框*/
                showCheckBox: function($flag, checkBoxID) {
                    var flag = $flag;
                    var showInfo = jQuery("#" + flag + "_showInfo");
                    showInfo.show();
                    showInfo.height(screen.height);
                    //绑定点击确定的事件
                    jQuery("#" + flag + "_sureBtn").bind("tap", function() {
                        var s = '';
                        jQuery("input[name='" + flag + "_checkInput']:checked").each(function() {
                            s += jQuery(this).attr('displayValue') + ',';
                        });
                        jQuery("#" + flag + "_showInfo").hide();
                        s = s.substr(0, s.length - 1);
                        jQuery("#" + checkBoxID).val(s);
                    });
                    //绑定点击取消的事件
                    jQuery("#" + flag + "_cancelBtn").bind("tap", function() {
                        jQuery("#" + flag + "_showInfo").hide();
                    });
                    this.loadScroll2(flag);
                },
                /*多选框滚动条*/
                loadScroll2: function(flag) {
                    var wraper = $("#" + flag + "_selectWarpper");
                    wraper.height(155);
                    if (!this.checkBoxScroller) {
                        this.checkBoxScroller = new iscroll(wraper[0], {
                            hideScrollbar: true
                        });
                    }
                    $("#scrollBar").css("top", "37px");
                    $("#scrollBar").css("bottom", "40px");
                    this.checkBoxScroller.refresh();
                },
                /**
                 * 日期格式的处理:位数补齐
                 * @param strDate
                 */
                dealDate: function(strDate) {
                    var strDateSplit = strDate.split("-");
                    var strDateYear = strDateSplit[0], strDateMonth = strDateSplit[1], strDateyDay = strDateSplit[2];
                    if ((strDateMonth.length == 1) || (strDateyDay.length == 1)) {
                        if (strDateMonth.length == 1) {
                            strDateMonth = "0" + strDateMonth;
                        }
                        if (strDateyDay.length == 1) {
                            strDateyDay = "0" + strDateyDay;
                        }
                        var strDate2 = strDateYear + '-' + strDateMonth + '-' + strDateyDay;
                        return strDate2;
                    } else {
                        return strDate;
                    }
                },
                /**
                 * 计算两个日期的差
                 */
                 DateDiff:function (sDate1,sDate2,fg){    //sDate1和sDate2是日期;fg是分隔符
				       var  aDate,  oDate1,  oDate2,  iDays  
				       aDate  =  sDate1.split(fg);
				       oDate1  =  new  Date(aDate[1]  +  fg  +  aDate[2]  +  fg  +  aDate[0]) //转换为12-18-2006格式  
				       aDate  =  sDate2.split("-")  
				       oDate2  =  new  Date(aDate[1]  +  fg  +  aDate[2]  +  fg  +  aDate[0])  
				       iDays  =  parseInt(Math.abs(oDate1  -  oDate2)  /  1000  /  60  /  60  /24) //把相差的毫秒数转换为天数  
				       return  iDays  
				   } ,
                /**
                 * 根据生日计算年龄
                 */
                operAge: function(birth, nowdate) {
                    var age;
                    var nowDateFormat = nowdate.replace(/-/g, "");
                    var birthFormat = birth.replace(/-/g, "");
                    var years = parseInt(nowDateFormat.substr(0, 4), 10);
                    var month = parseInt(nowDateFormat.substr(4, 2), 10);
                    var days = parseInt(nowDateFormat.substr(6, 2), 10);
                    var birthYear = parseInt(birthFormat.substr(0, 4), 10);
                    var birthMoth = parseInt(birthFormat.substr(4, 2), 10);
                    var birthDate = parseInt(birthFormat.substr(6, 2), 10);
                    if (month - birthMoth < 0) {
                        age = years - birthYear - 1;
                    } else if (month - birthMoth > 0) {
                        age = years - birthYear;
                    } else {
                        if (days - birthDate - 1 >= 0) {
                            age = years - birthYear;
                        } else {
                            age = years - birthYear - 1;
                        }
                    }
                    if (age < 0) {
                        age = 0;
                    }
                    return age;
                },
                formatAddZero: function(i) {
                    if (i < 10)
                        return "0" + i;
                    else
                        return i;
                },
                renderStringLen: function(str, length) {
                    length = length ? length : 10;
                    if (str && str.length > length) {
                        return str.substring(0, length) + "...";
                    }
                    return str;
                },
                /**
                 * 显示下拉列表
                 */
                showList: function(selectParam, callback) {
                    var inputId = selectParam.inputID;
                    var id = selectParam.id;
                    var optionVals = selectParam.optionVals;
                    var optionText = selectParam.optionText;
                    var params = {};
                    var selectHtml = "";
                    for (var i = 0; i < optionVals.length; i++) {
                        selectHtml += "<option value=" + "'" + optionVals[i] + "'" + ">" + optionText[i] + "</option>";
                    }
                    jQuery("#" + id).html(selectHtml);

                    params = {preset: 'select'};
                    if (callback != undefined && typeof callback == 'function') {
                        params.callback = callback;
                    }
                    if (inputId != undefined) {
                        params.inputID = inputId;
                    }
                    var pattern = {
                        theme: 'android-ics light',
                        mode: 'scroller',
                        display: 'modal',
                        lang: 'zh',
                        setText: '确定',
                        cancelText: '取消',
                    };
                    jQuery('#' + id).val('').scroller('destroy').scroller(jQuery.extend(params, pattern));
                },
                toggleLoadingInPage: function(view, isShow) {
                    require(["text!commonTemplate/inPageLoading.html"], function(inPageLoading) {
                        if (view.$el.find("div.inPageLoading").length > 0) {
                            !isShow && view.$el.find("div.inPageLoading").remove();
                        } else {
                            isShow && view.$el.append(inPageLoading);
                        }
                    });
                },
              	
                getMarginTop:function(view,content,pageInfo,menuHeight){
                	var windowHeight= (window.innerHeight > 0) ? window.innerHeight : screen.height; 
        			var contentHeight=view.$el.find("#"+content).height();
        			
        			var bottomH=view.$el.find("#bottom").height();  
        			var headerHeight=$("#header").height();
        			var top=0;
        			if(pageInfo!=undefined){
        				var $dom=view.$el.find("div[items='"+pageInfo.id+"']");
        				var pullHeight=$dom.find("#pullUpLoadMore").height();
        			    var listH=$dom.find(".ListArea").height();
        			        bottomH=$dom.find("#bottom").height();
        			      if(menuHeight!=undefined){  //有menu的上拉下拉刷新滚动页面
        				
                           if($dom.find("#noData").length!=0){
                        	  var noDataH=$dom.find("#noData").height();
                        	 
                        	  top=contentHeight-noDataH-bottomH-menuHeight;
                        	  
                           }else{                     

                        	   top=contentHeight-listH-menuHeight+7.5;
                           }
                          
        				}else{                        //无menu的上拉下拉刷新滚动页面
        					if($dom.find("#noData").length!=0){
                          	    var noDataH=$dom.find("#noData").height();
                          	 
                          	    top=contentHeight-noDataH-bottomH;
                          	  
                             }else{                     

                            	 top=contentHeight-listH+13.5;
                             }
        					
        					
        				}
        				if(top>0){
                           return top;
                        }else{
                       	   return 0;
                        }
        			}
        			if(pullHeight==undefined){         //无刷新的滚动页面
	        			if(headerHeight+contentHeight<windowHeight){  
	        				top=windowHeight-headerHeight-contentHeight;
	        				return top;
	        			}else{
	        				return 0;
	        			}
        			}
               },
            hash_clear:function(){
           			window.listHash.length>0 ? window.listHash.splice(0,window.listHash.length) : null;
            		console.log("hash length: "+window.listHash.length);
           },
            }
            return DMJS.CommonTools;
        });
//commonClass/ActionFilter.js
//单例过滤链，注意逻辑BUG
define("commonClass/ActionFilter", [], function() {
    var link = {};
    var filters = {};
    var doFilter = function(viewId, mothodName, func, params) {
        if (this != window) {
            _.extend(this, viewId);
            this.extend();
            return this;
        }
        if (link[viewId + "." + mothodName] || link[viewId + ".*"]) {
            var triggerObj = new Array().concat(link[viewId + "." + mothodName] || [], link[viewId + ".*"] || []);
            for (var i = 0; i < triggerObj.length; i++) {
                var filterName = triggerObj[i];
                var nextFilterName = triggerObj[i + 1];
                if (nextFilterName) {
                    filters[filterName]._doNext = _.bind(filters[nextFilterName]._func, filters[nextFilterName]);
                } else {
                    filters[filterName]._doNext = func;
                }
            }
            filters[triggerObj[0]]._func(viewId, mothodName, params);
        } else {
            func();
        }
    }
    doFilter.prototype.extend = function() {
        console.log("load filter");
        if (!filters[this.id]) {
            for (var i = 0; i < this.triggers.length; i++) {
                !link[this.triggers[i]] && (link[this.triggers[i]] = []);
                link[this.triggers[i]].push(this.id);
            }
            filters[this.id] = this;
            console.log("load filter:" + this.id + " success");
        } else {
            console.log("filter:" + this.id + " loaded");
        }
    }
    doFilter.prototype.doNext = function(viewId, methodName) {

        this._doNext(this.lastViewId, this.lastMethodName, this.args);
    }
    doFilter.prototype._func = function(viewId, methodName, params) {
        this.lastViewId = viewId;
        this.lastMethodName = methodName;
        if (params && params.length > 0) {
            this.args = params;
        }
        this.func(viewId, methodName);
    }
    require.config({
        paths: {
            filters: 'app/modules/filter'
        }
    });
    return doFilter;
});


//filters/userFilter.js
define("filters/userFilter", ['commonClass/ActionFilter'], function(ActionFilter) {
    return new ActionFilter({
        "id": "userStatusFilter",
        "triggers": ['fiduciaryContent.loanApplication','projectInvDetail.button','creditorDetailView.button','userinfoViewContent.recharge','userinfoViewContent.withdraw','userinfoViewContent.cardManage',
        'cardManageViewContent.addCard','donationDetailViewContent.button','goodsDetail.addShoppingCart','goodsDetail.exchange','goodsDetail.buyImmediately'],
        'func': function(_viewId, _methodName) {
            var self = this;
            if (!DMJS.userInfo) {
                DMJS.router.navigate("user/personal/login", true);
                return;
            }
            if(DMJS.userInfo.isHMD){
            	if(_methodName!="recharge"){
            		DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"), "账号异常,请联系客服！");
            		return;
            	}
            }
            var eventAction = _viewId+"."+_methodName;
            DMJS.currentView.controller.commonModel.userInfo({"noCache": true, cancelLightbox: true,
                "success": function(response) { 
                	 if (response.data.tg) {
                	 	self.getFlag(self.tgFunc,response.data,eventAction);
                	 }
                	 else if (!response.data.tg) { 
                	 	self.getFlag(self.unTgFunc,response.data,eventAction);
                	 }
                	// else {self.doNext(); } 
                },
                 'error':function(response){DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"), response&&response.description);}});
        },
        tgFunc: function(accountInfo,flag) {
        	var self=this;
        	if(self.safeInfo(accountInfo,flag)){
        		//判断是否第三方注册
        		if(!!accountInfo.usrCustId){
        			self.doNext();
        		}
        		else{
        			wrapView.FlipPrompt.confirm({
			   	   				title:"温馨提示",
			   	   				content:"您尚未注册第三方，请先注册！",
			   	   				FBntconfirm:"注册",
			   	   				FBntcancel:"取消",
			   	   				FBntConfirmColor: "pop_btn_orange",
			   	       		},function(){
			   	       				DMJS.router.navigate("user/payment/openTgAccount",true);
			   	       				/*DMJS.currentView.controller.commonModel.payUserRegister({
						   	     			"success":function(response){
						   	     				Native.openChildWebActivity(response.data,{title:"账 号 开 户"});
						   	     	       	}
						   	     	});*/
			   	       			},function(){
			   	       		   	});
        		}
        	}
        },
        
        unTgFunc: function(accountInfo,flag) {
        	var self=this;
        	if(self.safeInfo(accountInfo,flag)){
        		self.doNext();
        	}
        },
       
        safeInfo:function(accountInfo,flag){
			var tipContent="您必须先进行";
			var returnFlag=true;
			if(flag['isNeedPhone']&&!accountInfo.mobileVerified){returnFlag=false;  tipContent+="手机认证、"};
			if(flag['isNeedNciic']&&!accountInfo.idcardVerified&&!!!accountInfo.realName){returnFlag=false;  tipContent+="实名认证、"};
			if(flag.method=="userinfoViewContent.recharge"){
				if(flag['isNeedEmail']&&!accountInfo.emailVerified){returnFlag=false;   tipContent+="邮箱认证、"};
			}else{
				if(flag['isNeedEmail']&&!accountInfo.emailVerified){returnFlag=false;   tipContent+="邮箱认证、"};
			}
			if(flag['isNeedPsd']&&!accountInfo.tg&&!accountInfo.withdrawPsw){returnFlag = false;tipContent += "交易密码设置、"; }
			if(!returnFlag){
				tipContent=tipContent.substring(0,tipContent.length-1);
    			tipContent+="!";
				wrapView.FlipPrompt.confirm({
	   				title:Config.lang("alertCommonTitle"),
	   				content:tipContent,
	   				FBntconfirm:"设置",
	   				FBntcancel:"取消",
	   				FBntConfirmColor: "pop_btn_orange",
	       			},function(){if(!returnFlag){DMJS.router.navigate("user/personal/unautherized",true);}},function(){});
			}
            return returnFlag;
       },
       //获取需要认证的信息
       getFlag:function(callback,accountInfo,eventMethod){
       	var self=this;
       	var obj={isNeedEmail:true,isNeedNciic:true,isNeedPhone:true,isNeedPsd:true,method:eventMethod};
       	DMJS.currentView.controller.commonModel.fee({
					'data': {},'cancelLightbox': false,"noCache": true,
					"success": function(response) {	
						var isNeedEmailRZ=response.data.baseInfo.isNeedEmailRZ=="false" ? false : true;
						var isNeedEmail=response.data.chargep.isNeedEmail=="false" ? false : true;
						obj.isNeedNciic=response.data.chargep.isNeedNciic=="false" ? false : true;
						obj.isNeedPhone=response.data.chargep.isNeedPhone=="false" ? false : true;
						obj.isNeedPsd=response.data.chargep.isNeedPsd=="false" ? false : true;
						obj.isNeedEmailRZ=isNeedEmailRZ;//平台邮箱认证
						obj.isNeedEmail=isNeedEmail;//充值邮箱认证
						callback.apply(self,[accountInfo,obj]);
					},
					'error': function(response) {DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"), response.description);}});
       	}
      
    });

});


//filters/sessionFilter.js
//部分view不允许非登录访问       	
define("filters/sessionFilter", ['commonClass/ActionFilter'], function(ActionFilter) {
    return new ActionFilter({
        "id": "sessionFilter",
        "triggers": ['router.navigate'],
        'func': function(_viewId, _methodName) {
            var goHash = this.args[0];
            console.info("navigate:info:" + goHash);
            var cuserInfo = DMJS.CommonTools.getCookie("userInfo");
            if(cuserInfo){
            	 DMJS.userInfo = JSON.parse(cuserInfo);
            }else{
            	DMJS.userInfo=null;
            	//DMJS.CommonTools.hash_clear();
            }
           if (goHash && !DMJS.userInfo) {
                if (goHash.indexOf("index/index/index")!=0
                  &&goHash.indexOf("user/password/passwordList")!=0
                  &&goHash.indexOf("user/password/loginPwdCode")!=0
                  &&goHash.indexOf("user/password/loginPwd")!=0
                  &&goHash.indexOf("user/personal/register")!=0
                  &&goHash.indexOf("user/personal/binRegister")!=0
                  &&goHash.indexOf("user/personal/registerInfo")!=0
                  &&goHash.indexOf("user/personal/agreement")!=0
                  &&goHash.indexOf("user/personal/login")!=0
                  &&goHash.indexOf("user/personal/binLogin")!=0
                  &&goHash.indexOf("user/personal/help")!=0
                  &&goHash.indexOf("index/index/discover")!=0
                  &&goHash.indexOf("index/index/projectInvestment")!=0
                  &&goHash.indexOf("index/index/projectInvDetail")==-1
                  &&goHash.indexOf("index/index/creditorTransfer")!=0
                  &&goHash.indexOf("index/index/creditorDetail")!=0
                  &&goHash.indexOf("index/index/donationBenefit")!=0
                  &&goHash.indexOf("index/index/donationDetail")!=0
                  &&goHash.indexOf("index/index/information")!=0
                  &&goHash.indexOf("index/index/cashList")!=0
                  &&goHash.indexOf("index/index/loanCompany")!=0
                  &&goHash.indexOf("index/index/loanPerson")!=0
                  &&goHash.indexOf("index/index/fiduciary")!=0
                  &&goHash.indexOf("index/index/operationData")!=0
                  //&&goHash.indexOf("index/index/loanApplication")!=0
                   
                  &&goHash.indexOf("index/index/intergalMall")!=0
                  &&goHash.indexOf("index/index/goodsDetail")!=0
                  &&goHash.indexOf("index/index/financialCalculator")!=0
                  &&goHash.indexOf("index/index/calculation")!=0
                  &&goHash.indexOf("index/index/appDownload")!=0
                  ) {
                    	DMJS.beforeLoginHash = goHash;
                    return DMJS.router.navigate("user/personal/login", true);
                }
            }
            
            this.doNext();
            
        }
    });
});

define("filters/weixinFilter", ['commonClass/ActionFilter'], function(ActionFilter) {
    return new ActionFilter({
        "id": "weixinFilter",
        "triggers": ['router.navigate'],
        'func': function(_viewId, _methodName) {
            var goHash = this.args[0];
            if (goHash == "index/index/more" ||
                    goHash == "user/password/pwdManage"
                    ) {
                return DMJS.navigate("user/personal/userInfo");
            }
            require(["commonClass/weixin"], function(weixin) {
                weixin.step();
            });
            DMJS.beforeLoginHash = goHash;
            this.doNext();
        }
    });
});


//filters/authorizeFilter
//部分view不允许非登录访问       	
define("filters/authorizeFilter", ['commonClass/ActionFilter'], function(ActionFilter) {
    return new ActionFilter({
        "id": "authorizeFilter",
        "triggers": ['request.success'],
        'func': function(_viewId, _methodName) {
            var response = this.args[1];
            if (response == undefined) {
                this.doNext();
            } else if (response.code == "000048" || response.code == "000049") {
                delete DMJS.userInfo;
                DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"), response.description, function() {
                    DMJS.navigate("user/personal/login", true);
                });
            } else if (response.code == "000047") {
                DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"), response.description, function() {
                    DMJS.currentView.success = function() {
                        DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"), "授权成功", function() {
                            DMJS.navigate("user/personal/userInfo", true);
                        });
                    }
                    Native.openChildWebActivity(response.data.url, {title: "账 号 授 权"});
                });
            } else {
                this.doNext();
            }

        }
    });
});


//filters/main.js
define("filters/main", ['filters/userFilter', 'filters/sessionFilter', 'filters/authorizeFilter', 'filters/weixinFilter'],
        function() {
            console.log("DMJS APP LOGIC FILTER STARTED ... ");
//	require(['filters/cardManageFilter.sq'], function(){});
        });






//commonTool/Console.js

define("commonTool/Console", [], function() {
    var core = window.console;
    window.console = {
        "log": function(message) {
            this.debug(message);
        },
        "debug": function(message) {
            if (Config.base("logLever") >= 2) {
                core.log("DEBUG:" + message);
            }
        },
        "info": function(message) {
            if (Config.base("logLever") >= 1) {
                core.log("INFO:" + message);
            }
        }
    }
    return window.Console
});



//commonTool/date.js
define("commonTool/date", [], function() {
    var _Date = Date;
    window.Date = function() {
        var _result = new _Date(arguments);
        if (DMJS && DMJS.systemTimeSync) {
            _result.setTime(_result.getTime() + DMJS.systemTimeSync);
            return _result;
        }
    }
    return window;
});
//commonTool/validator.js
//校验  公共
define("commonTool/validator",['commonClass/stringTools', "commonTool/tool"], function(stringTools,tool) {
    var ruler = {
    	
    	'notEmpty': {
            'logic': stringTools.isNotEmpty,
            'text': '请输入<%=$dom.attr("title")%>'
       },
        'length': {
            'logic': function(val, min, max) {
                var length = val ? val.length : 0;
                if ((min && min != -1 && min > length)||(max &&max < length)) {
                    return false;
                }
                return true;
            },
            'text': '<%if($dom.attr("tips")){return $dom.attr("tips")}else{return $dom.attr("title")+"输入格式有误！"}%>'
        },
        'reg': {
            'logic': function(val, reg) {
                return reg.test(val);
            },
            'text': '输入的<%=$dom.attr("title")%>不符合规范'
        },
        'Int':{
        	'logic':function(val, min, max) {
        		if(isNaN(val)||val==""||val.indexOf('.')!=-1||val*1<1)  return false; 
        		return true;
        	},
        	'text': '<%=$dom.attr("title")%>必须是正整数'
        },
        'val': {
            'logic': function(val, min, max) {
                if (min != undefined && min != -1 && min > val) {
                    return false;
                }
                if (max != undefined && max != -1 && max < val) {
                    return false;
                }
                return true;
            },
            'text': '<%=$dom.attr("title")%>输入范围在<%=params[1]%>-<%=params[2]%>之间'
        },
        //整数倍
		'multiple':{
			'logic':function(val,value){
			val=val*1,value=value*1;
				if(val%value!=0){
					return false;
				}
				return true;
			},
			'text':'<%=$dom.attr("title")%>必须是<%=params[1]%>的倍数'
		},
        'amountMin': {
            'logic': function(val, min) {
                if (min && min != -1 && min > val) {
                    return false;
                }
                return true;
            },
            'text': '<%=$dom.attr("title")%>的值不能小于最低投标金额'
        },
        'isNumber': {
            'logic': function(val) {
                if (!/^[1-9]+[0-9]*$/.test(val)) {
                    return false;
                }
                return true;
            },
            'text': '<%=$dom.attr("title")%>的值必须为数字'
        },
        'equal': {
            'logic': function(val, compVal) {
                return val == compVal;
            },
            'text': '两次密码不一致'
        },
        'noequal': {
            'logic': function(val, compVal) {
                return val != compVal;
            },
            'text': '<%=$dom.attr("title")%>不能和原密码相同'
        },
        'checked': {
            'logic': function() {
                return this.is(":checked");
            },
            'text': '<%=$dom.attr("title")%>'
        },
		'checkidcard':{
			'logic':function(val){
				 if (val.length == 18) {   
				        var a_idCard = val.split("");  // 得到身份证数组  
				        if(tool.isValidityBrithBy18IdCard(val) && tool.isTrueValidateCodeBy18IdCard(a_idCard)) { //进行18位身份证的基本验证和第18位的验证
				        	return true;
				        }else{
				        	return false;
				        }
				        
				 }else {
				        return false;   
				    }   
			},
			'text':'请输入正确的身份证号码'
		},
        "radioChecked": {
            "logic": function() {
                var name = this.attr("name");
                return $("input[name='" + name + "']:checked").length > 0
            },
            "text": '请选择<%=$dom.attr("title")%>'
        }
        
    };
    var validator = {
        'check': function($range) {
        	var tag='input,select,textarea';
            var return_flg = true;
            var self = this;
            if($range.is(tag)){
            	if (!self.triggerCheckItem($range)) {
                    return_flg = false;
                    return false;
                }
            }else{
            	  $range.find(tag).each(function() {
                if (!self.triggerCheckItem($(this))) {
                    return_flg = false;
                    return false;
                }
            });
            }
          
            return return_flg;
        },
        'checkItems': function($range) {
            var return_flg = true;
            var self = this;
            $range.each(function() {
                if (!self.triggerCheckItem($(this))) {
                    return_flg = false;
                    return false;
                }
            });
            return return_flg;
        },
        'removeCheckItem': function($dom, removeItem) {
            if ($dom.attr("validator")) {
                var checkItems = $dom.attr("validator").split(";"), newCheckItems = [];
                for (var i = 0; i < checkItems.length; i++) {
                    if (checkItems[i].indexOf(removeItem) != 0) {
                        newCheckItems.push(checkItems[i]);
                    }
                }
                $dom.attr("validator", newCheckItems.join(";"));
            }
        },
        'addCheckItem': function($dom, addItem) {
            var _itemsStr = $dom.attr("validator") ? $dom.attr("validator") : "";
            var checkItems = _itemsStr.split(";"),
                    newCheckItems = [],
                    checkStr = addItem.indexOf("[") == -1 ? addItem : addItem.substring(0, addItem.indexOf("["));
            for (var i = 0; i < checkItems.length; i++) {
                if (checkItems[i].indexOf(checkStr) != 0) {
                    newCheckItems.push(checkItems[i]);
                }
            }
            newCheckItems.push(addItem);
            $dom.attr("validator", newCheckItems.join(";"));
        },
        'triggerCheckItem': function($dom, triggerItems) {
            if (!triggerItems) {
                triggerItems = $dom.attr("validator");
            }
            if (triggerItems) {
                var checkItems = triggerItems.split(";");
                var definemsg = 0;//自定义提示语位置
                for (var i = 0; i < checkItems.length; i++) {
                	var isZDY = checkItems[i].indexOf("{");//自定义提示 {}包住校验项
                    var _index = checkItems[i].indexOf("["), 
                    	//如果存在自定义就需要去掉页面的   {}
                    	rule = isZDY == 0?ruler[checkItems[i].substring(1,checkItems[i].length-1)]:ruler[checkItems[i]], 
                    	params = [];
                    if (_index != -1) {
                    	rule = ruler[checkItems[i].substring(0, _index)];
                    	if(isZDY == 0){
                    		rule = ruler[checkItems[i].substring(1,_index)];
                    		params = eval(checkItems[i].substring(_index,checkItems[i].length-1));
                    	}else{
	                        params = eval(checkItems[i].substring(_index));
                    	}
                    }
                    params.unshift($dom.val());
                    if (rule && !rule.logic.apply($dom, params)) {
                		var text = rule.text;
                    	if(isZDY == 0){//如果是自定义就需要取自定义消息进行提示
                    		var msg = $dom.attr("definemsg");
                    		if(msg){
	                    		text = msg.split(";")[definemsg]?msg.split(";")[definemsg]:text;
	                    		definemsg++;
                    		}else{
                    			text = rule.text;
                    		}
                    	}
                        if (text) {
                            var resultText = _.template(text, {'$dom': $dom, 'params': params});
                            DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"), resultText);
                        }
                        return false;
                    }
                }
            }
            return true;
        }
    }
    return validator;
});

//commonTool/thread.js
define("commonTool/thread", ['DMJSAll'], function() {
    var threads = {};
    var setTimeout = window.setTimeout;//window.setTimeout=undefined;
    var setInterval = window.setInterval;//window.setInterval=undefined;
    var clearInterval = window.clearInterval;//window.clearInterval=undefined;
    var Thread = function(alternation, times, func) {
        var core = {
            "timer": undefined,
            "times": 0,
            "startTime": undefined,
            "start": function() {
                if (!this.timer) {
                    this.startTime = new Date();
                    this.timer = setInterval(function() {
                        console.log("运行线程[" + new Date() + "]：" + func);
                        func(core.startTime);
                        core.times++;
                        if (core.times > times && times != -1) {
                            core.stop();
                        }
                    }, alternation);
                } else {
                    console.log("线程已经处于运行状态：" + func);
                }
            },
            "stop": function() {
                clearInterval(this.timer);
                this.timer = undefined;
            }
        }
        return core;
    }
    DMJS.setTimeout = setTimeout;
    DMJS.startThread = function(key, alternation, times, func) {
        console.log("创建thread[" + new Date() + "]：key:" + key);
        if (threads[key]) {
            DMJS.removeThread(key);
        }
        var thread = new Thread(alternation, times, func);
        thread.start();
        threads[key] = thread;
        return thread;
    }
    DMJS.getThread = function(key) {
        return key ? (threads[key]) : (threads);
    }
    DMJS.stopThread = function(key) {
        if (key && threads[key]) {
            threads[key].stop();
        } else {
            for (var key in threads) {
                threads[key].stop();
            }
        }
    }
    DMJS.removeThread = function(key) {
        DMJS.stopThread(key);
        if (key && threads[key]) {
            console.info("销毁线程:" + key);
            delete threads[key];
        } else {
            for (var key in threads) {
                delete threads[key];
            }
        }
    }
    return null;
});

//commonTool/localData.js
define("commonTool/localData", ["Lib/base64"], function(Base64) {
    var localDB = {core: localStorage || DimengFileLocalStrong};
    var base64 = new Base64(Config.base("trunKey"));
    var localData = {
        "loginUserName": '',
        'loginUserPassword': '',
        'loginStatus': 'N'
    };
    var localUserData = {
        'handPasswordSwitch': 'on',
        'handPassword': '',
        'nextHPWDChance': 5
    }
    var nowUserData = {};

    function jons(callback) {
        (localDB.__getItem = localDB.getItem) && (localDB.getItem = function(key) {
            return base64.decode(localDB.__getItem(key));
        });
        (localDB.__setItem = localDB.setItem) && (localDB.setItem = function(key, val) {
            return localDB.__setItem(key, base64.encode(val));
        });
        callback();
    }
    //对外方法禁止变动
    var funcs = {
        "get": function(key) {
            return localDB.getItem(key);
        },
        "set": function(key, val) {
            localData.key = val;
            localDB.setItem(key, val);
        },
        "getByUser": function(key) {
            return 	nowUserData[key];
        },
        "setByUser": function(key, value) {
            if (!DMJS.userInfo) {
                return;
            }
            nowUserData[key] = value;
            localDB.setItem("user:" + DMJS.userInfo.accountName, JSON.stringify(nowUserData));
        },
        "initUser": function(userName) {
            if (!DMJS.userInfo && !userName) {
                return;
            }
            userName = userName ? userName : DMJS.userInfo.accountName;
            var val_local = localDB.getItem("user:" + userName);
            if (val_local && val_local != "") {
                nowUserData = JSON.parse(val_local);
            } else {
                nowUserData = _.extend({}, localUserData);
                localDB.setItem("user:" + userName, JSON.stringify(nowUserData));
            }
        },
        "delUser": function() {
            if (!DMJS.userInfo) {
                return;
            }
            var userId = DMJS.userInfo.id;
            localDB.removeItem("user:" + userId);
        }
    };
    (function(callback) {
        localDB.getItem = function(key) {
            var result = base64.decode(this.core.getItem(key));
            if (!result || result == "") {
                this.core.removeItem(key);
                return undefined;
            }
            return result;
        };
        localDB.setItem = function(key, val) {
            return this.core.setItem(key, base64.encode(val));
        };
        (function() {
            var args = arguments;
            for (var i = 0; i < this.core.length; i++) {
                this.getItem(this.core.key(i));
            }
            _.each(args, function(ins) {
                ins();
            });
        }).apply(localDB, [callback]);
    })(function() {
        for (var key in localData) {
            var val_local = localDB.getItem(key);
            if (val_local) {
                localData[key] = val_local;
            } else if (localData.key) {
                localDB.setItem(key, localData.key);
            }
        }
        for (var i = 0; i < localDB.length; i++) {
            var key_local = localDB.key(i);
            if (!localData[key_local]) {
                if (key_local.indexOf("user:") == 0) {
                    //var userName=key_local.substring(5);
                } else {
                    localDB.removeItem(key_local);
                }
            }
        }
        funcs.initUser(funcs.get("loginUserName"));
    });
    DMJS.localData = funcs;
    return funcs;
});


/**
 * 定义DMJS对象
 */
//DMJS.js
define("DMJS", ['backbone', 'Lib/md5', 'commonClass/ActionFilter'], function(_backbone, MD5, ActionFilter) {
    // 将DMJS原型引用为Backbone, 便于调用和扩展
    var DMJSConstructor = new Function();
    DMJSConstructor.prototype = Backbone;
    DMJSConstructor.prototype.constructor = DMJSConstructor;

    // 创建并扩展DMJS对象
    _.extend(window.DMJS = new DMJSConstructor(), {
        // 框架版本号
        VERSION: '0.0.1',
        lastHash: '',
        htmlView: {},
        hashView: {},
        
        // 初始化创建Router对象
        init: function() {
        	//从cookie中取出user
            var cuserInfo = DMJS.CommonTools.getCookie("userInfo");
            if(cuserInfo){
            	 DMJS.userInfo = JSON.parse(cuserInfo);
            } 
              //初始化存放hash的数组
		    if(!window.listHash){
				window.listHash=[];
			}
            this.router = new DMJS.DMJSRouter();
            var _callFunc = this.router.navigate;
            this.router.navigate = function() {
                var _params = arguments;
                ActionFilter("router", "navigate", function() {
                    _callFunc.apply(DMJS.router, _params);
                }, _params);
            }
            this.history.start({
                pushState: false
            });
			window.erroHash=[];
            this.naviVal=[/.+/];
            this.pushNavi(this.naviVal);
            var self = this;
        },
        // 导航到当前的action
        navigate: function(fragment) {
        	var router = this.router, fragment = fragment || location.hash.substring(1);
        
            router.navigate('', {
                trigger: false,
                replace: true
            });

            router.navigate(fragment, {
                trigger: true,
                replace: true
            });
            },
        pushNavi:function(arr){
        	for(var i=0;i<arr.length;i++){
        		DMJS.history.handlers.push({
                    callback: _.bind(this.autoload, this),
                    route: arr[i]
                });
        	}
        },
        // 自动解析并加载模块入口
        autoload: function(hash) {
            this.lastHash = hash;
            var hashArr = hash.split('/');
            var module = hashArr.splice(0, 1)[0], controller = hashArr.join('/');
            if (module && module != 'common') {
                var mainPath = 'app/modules/' + module + '/main';
                require([mainPath], _.bind(function(moduleMain) {
                if(!this.stopRouteLock(hash)){return ;}
                    moduleMain();
                    this.navigate(hash);
                    

                }, this));
            }
        },
    
        removeViewCache: function(key) {
            if (key && this.hashView[key]) {
                delete this.hashView[key];
            } else {
                for (var key in this.hashView) {
                    delete this.hashView[key];
                }
            }
        },
       stopRouteLock:function(hash){
        		window.erroHash&&window.erroHash.push(hash);
                	if(window.erroHash.length>=2&&(window.erroHash[window.erroHash.length-2]==window.erroHash[window.erroHash.length-1])){
                		if(location.hash.indexOf('#index')!=-1){
                			this.navigate('index/index/index');
                		}
                		else if(location.hash.indexOf('#user')!=-1){
                			this.navigate('user/personal/userInfo');
                		}
                		else{
                			this.navigate('index/index/index');
                		}
                		if(window.erroHash.length>10){
                    		window.erroHash.length>0 ? window.erroHash.splice(0,window.erroHash.length) : null;
                    	}
                		console.log("错误地址:"+hash);
                		return false;
                	}
                	
                	return true;
        }
    });

    return DMJS;
});

/**
 * 重载Backbone.Collection
 */
define("DMJSCollection", ['backbone'], function() {
    var DMJSCollection = DMJS.DMJSCollection = Backbone.Collection.extend({
        initialize: function(options) {
            this.init.apply(this, arguments);
        },
        init: function(options) {
        }
    });

    return DMJSCollection;
});

/**
 * 创建Controller控制器, 控制器作为执行业务逻辑的容器, 并负责创建调度模型和视图
 */
define("DMJSController", function() {
    var DMJSController = DMJS.DMJSController = function(options) {
        this.initialize.apply(this, arguments);
    };
    // Controller继承Backbone.Events, 因此允许使用Events中的事件方法
    _.extend(DMJSController.prototype, Backbone.Events, {
        initialize: function(options) {
            var actions = this.actions;
            if (actions) {
                var router = DMJS.router, list = [], route = null, prefix = this.module + '/' + this.name + '/';

                for (var key in actions) {
                    list.unshift([key, this[actions[key]]]);
                }

                for (var i = 0, len = list.length; i < len; i++) {
                    route = list[i];
                    router.route(prefix + route[0], '', _.bind(route[1], this));
                }
            }

            this.init.apply(this, arguments);
        },
        // 模块名
        module: '',
        // 控制器名, 用于模块路由时定义规则
        name: '',
        // 模块规则
        actions: null,
        // 初始化方法
        init: function(options) {
        },
        // 在销毁控制器时同时移除URL中的Action监听
        destroy: function() {
            var actions = this.actions;
            if (actions) {
                var router = DMJS.router, handlers = DMJS.history.handlers, handler = null, prefix = this.module + '/' + this.name + '/';
                for (var key in actions) {
                    var exp = router._routeToRegExp(prefix + key).toString();

                    for (var i = 0, len = handlers.length; i < len; i++) {
                        handler = handlers[i];
                        if (handler.route.toString() == exp) {
                            handlers.splice(i, 1);
                            break;
                        }
                    }
                }
            }
        }

    });

    // 添加控制器的继承方法, 在继承子控制器时自动添加getInstance获取单例的方法
    DMJSController.extend = function(protoProps, classProps) {
        var Controller = DMJS.DMJSModel.extend.call(this, protoProps, classProps);
        //var Controller=_.extend(this,protoProps,classProps);
        Controller.getInstance = function(options) {
            if (!this._instance) {
                this._instance = new this(options);
            }

            return this._instance;
        }
        return Controller;
    }
    return DMJSController;
});



/**
 * 重载Backbone.History
 */
define("DMJSHistory", ['backbone'], function() {
    var DMJSHistory = DMJS.DMJSHistory = Backbone.History.extend({
        initialize: function(options) {
            this.init.apply(this, arguments);
        },
        init: function(options) {
        }
    });

    return DMJSHistory;
});


/**
 * 重载Backbone.Model DMJSModel.js
 */
define("DMJSModel", ['backbone', 'commonClass/ActionFilter'], function(_backbone, ActionFilter) {
    var DMJSModel = DMJS.DMJSModel = Backbone.Model.extend({
        initialize: function(options) {
            this.init.apply(this, arguments);
        },
        init: function(options) {
        },
        'commonRequest': function(param) {
            var self = this;
            
             var dataType ="jsonp";
             var type = "get";
            if(Config.base('envOption').requestType=='POST'){
                 dataType ="json";
                 type = "POST";
            }
            param = _.extend({
                "error": function(response) {
                    DMJS.CommonTools.alertCommon("温馨提示", response.description);
                    if (param.commonError) {
                        param.commonError(response);
                    }
                },
               "dataType":dataType,
               "type":type, 
                "beforeSendCallback": function(xhr) {
                    xhr.withCredentials = true;
                }
            }, param);
            var successInvoke = param.success;
            param.success = function(response) {
                console.log("请求:" + param.urlKey + ', success Response: ' + response);
//        		 ActionFilter();
                ActionFilter("request", "success", function() {
                    if (response && "000000" == response.code) {
                        successInvoke(response);
                        return;
                    } else if (response && response.code == "000002") {
                    	DMJS.userInfo = undefined;
                        DMJS.removeCache("user.myAccount");
                        DMJS.router.navigate("user/personal/login", true);
                        DMJS.CommonTools.delCookies();
                    	/*if(param.urlKey == "user.myAccount" && DMJS.userId){//微信辅助登录，后台传回的用户id。后台无法实现第一次请求就登录成功。只能多进行一次请求
                    		setTimeout(function(){
                    			DMJS.navigate("user/personal/userInfo/"+DMJS.userId, true);
                    		},500);
                    	}else{
                    		if(DMJS.CommonTools.isWeiXin()){//微信走这里
                    			Native.exit();
                    		}else{//方便微信测试
                    			DMJS.userInfo = undefined;
		                        DMJS.removeCache("user.myAccount");
		                        DMJS.router.navigate("user/personal/login", true);
		                        DMJS.CommonTools.delCookies();
                    		}
							
                        }*/
                        /*DMJS.CommonTools.alertCommon("温馨提示", "未登录或由于您长时间未做任何操作已经注销了您的登录信息，请重新登录！", function() {
                            DMJS.removeCache("user.myAccount");
                            DMJS.router.navigate("user/personal/login", true);
                        });*/
                    }/*//锁住或拉黑
                    else if(response && response.code == '000044'){
                    	DMJS.userInfo = undefined;
                        DMJS.removeCache("user.myAccount");
                        DMJS.CommonTools.delCookies();
                    	 DMJS.CommonTools.alertCommon("温馨提示", response.description, function() {
                           DMJS.router.navigate("user/personal/login", true);
                        });
                    }*/
                 else if (response && response.code == "000061") {
                    	//多个设备登录同一个账号时跳转到登录页面
                        DMJS.userInfo = undefined;
                        DMJS.removeCache("user.myAccount");
                        DMJS.CommonTools.alertCommon("温馨提示", response.description, function() {
                           DMJS.router.navigate("user/personal/login", true);
                        });
                        DMJS.CommonTools.delCookies();
                   }
                    else if (response && response.code == "200001") {
                    	//200001 未登录状态 移除cookie 和 缓存  重新登录
                        DMJS.userInfo = undefined;
                        DMJS.removeCache("user.myAccount");
                        DMJS.router.navigate("user/personal/login", true);
                        DMJS.CommonTools.delCookies();
                        DMJS.CommonTools.hash_clear();
                }else if (response && response.description) {
                        if (param.logicError) {
                            param.logicError(response);
                        } else if(param.error){
                        	param.error(response);
                        }
                        else {
                            DMJS.CommonTools.alertCommon("温馨提示", response.description);
                        }
                        if (param.commonError) {
                            param.commonError(response);
                        }
                    } else {
                        if (param.logicError) {
                            param.logicError(response);
                        }else if(param.error){
							param.error(response);
                        } 
                        else {
                            DMJS.CommonTools.alertCommon("温馨提示", "数据请求失败，请刷新页面！");
                        }
                        if (param.commonError) {
                            param.commonError(response);
                        }
                    }
                }, [param, response]);

            }
            DMJS.Request(param.urlKey, param);
        }
    });

    // 用于存放应用中共享的模型对象
    DMJSModel.shareModels = {
        // @private
        _shareList: {},
        /**
         * 添加共享的模型
         */
        add: function(name, model) {
            DMJSModel.shareModels._shareList[name] = model;
        },
        /**
         * 获取共享的模型
         */
        get: function(name) {
            return DMJSModel.shareModels._shareList[name];
        },
        /**
         * 移除模型
         */
        remove: function(name) {
            var shareList = DMJSModel.shareModels._shareList;
            shareList[name] = null;
            delete shareList[name];
        }
    }

    return DMJSModel;
});


/**
 * 重载Backbone.Router, 将规则解析到DMJSController中的方法
 */
define("DMJSRouter", ['backbone'], function() {
    var DMJSRouter = DMJS.DMJSRouter = Backbone.Router.extend({
        initialize: function(options) {
            this.init.apply(this, arguments);
        },
        init: function(options) {

        }
    });

    return DMJSRouter;
});





/**
 * 重载Backbone.View DMJSView.js
 */
define("DMJSView", ['commonClass/scroll/iscroll', 'commonClass/ActionFilter',], function(iscroll, ActionFilter) {
    var DMJSView = DMJS.DMJSView = Backbone.View.extend({
        // 用于视图关联时的标识
        name: '',
        // 存放所有子视图对象
        children: [],
        // 根据名称存放所有子视图对象
        childrenAsName: {},
        // 根据表达式存放所有子视图列表
        childrenAsEl: {},
        // 等待加入到视图中的子视图对象列表
        awaitChildren: [],
        // 视图所引用的Controller
        controller: null,
        resizable: false,
        initialize: function(options) {
            if (options) {
                this.controller = options.controller;
            }
            this.init.apply(this, arguments);
            var cancelScroll = this.type != "control" && "wrapView" in  window && wrapView.myScroll,
                    render = this.render,
                    self = this;
            if (this.render) {
                this.render = function() {
                    var ret = render.apply(self, arguments);
                    return ret;
                }
            }
            self.events = _.extend({//'focus input': '_inputFocus',
                'keydown input': 'turnNextInput',
                'tap div[action]': 'goAction',
		        'tap dl[action]': 'goAction',
                'input .Jnum':'inpN',
                'blur input': '_inputBlur',
                'tap .t-btn-a4':'runScrollerTop'
            }, self.events)
        },
        init: function(options) {
            //
        },
        // 显示视图对象
        show: function() {
            this.$el.show();
        },
        // 隐藏视图对象
        hide: function() {
            this.$el.hide();
        },
        /**
         * 添加一个子视图对象到当前视图中
         * @param {String} el 添加到视图的节点表达式
         * @param {String} name 子视图名称
         * @param {View Object} view 子视图对象
         * @param {Boolean} await 当节点不存在时是否将子视图加入到待渲染列表
         */
        addChild: function(el, name, view, await) {
            // 子视图添加的节点
            var $node = this.$el;
            if (el) {
                $node = $node.find(el);
            }

            if ($node.length) {
                // 节点存在, 立即添加到视图
                $node.append(view.el);
                view.parent = this;

                this.children.push({
                    el: el,
                    name: name,
                    view: view
                });
                this.childrenAsName[name] = view;

                if (!this.childrenAsEl[el]) {
                    this.childrenAsEl[el] = [view];
                } else {
                    this.childrenAsEl[el].push(view);
                }
            } else if (await || await === undefined) {
                // 节点不存在, 存储在待添加列表
                this.awaitChildren.push({
                    el: el,
                    name: name,
                    view: view
                });
            }

            return this;
        },
        /**
         * 渲染所有待添加的子视图列表
         */
        renderChildren: function() {
            var awaitChildren = this.awaitChildren, child = null;

            for (var i = 0, len = awaitChildren.length; i < len; i++) {
                child = awaitChildren[i];
                this.addChild(child.el, child.name, child.view, false);
            }
        },
        /**
         * 清除所有的待添加子视图列表
         */
        clearAwaitChildren: function() {
            this.awaitChildren.length = 0;
        },
        /**
         * 根据视图名称获取视图对象
         */
        getChildByName: function(name) {
            return this.childrenAsName[name];
        },
        /**
         * 根据表达式获取视图对象
         */
        getChildsByEl: function(el) {
            return this.childrenAsEl[el];
        },
        /**
         * 获取所有子视图列表
         */
        getChildren: function() {
            return this.children;
        },
        // @private
        _destroyChild: function(type, value) {
            var children = this.children, child = null, childName = '', childEl = '', childValue = '';

            for (var i = 0, len = children.length; i < len; i++) {
                child = children[i];
                childName = child.name;
                childEl = child.el;
                childValue = child[type];

                if (childValue == value) {
                    child.view.destroy();

                    this.childrenAsName[childName] = null;
                    this.childrenAsEl[childEl] = null;
                    delete this.childrenAsName[childName];
                    delete this.childrenAsEl[childEl];
                    break;
                }
            }
        },
        /**
         * 根据名称销毁子视图
         * @param {String} name
         */
        destroyChildByName: function(name) {
            this._destroyChild('name', name);
        },
        /**
         * 根据表达式销毁子视图
         * @param {String} el
         */
        destroyChildByEl: function(el) {
            this._destroyChild('el', el);
        },
        /**
         * 销毁所有子视图对象
         */
        destroyChilds: function() {
            var children = this.children;
            for (var i = 0, len = children.length; i < len; i++) {
                child = children[i];
                child.remove();
            }

            this.clearAwaitChildren();
            this.childrenAsName = {};
            this.childrenAsEl = {};
            this.children.length = 0;
        },
        /**
         * 销毁当前视图对象, 同时将销毁所有子视图
         */
        destroy: function() {
            this.onDestroy && this.onDestroy();
            // 销毁子视图对象
            this.destroyChilds();
            // 解除事件绑定
            this.undelegateEvents();
            if (self.scroller) {
                self.scroller.destroy();
                self.scroller = undefined;
            }
            // 移除DOM元素
            this.$el.remove();
            delete this;
        },
        "turnNextInput": function(e) {
            var self = this;
            if (e.keyCode == 13) {
                $(e.target)[0].blur();
                //$(e.target).nexts("input")[0].focus();
                var nextFocus = $(e.target).attr("nextFocus");
                var nextAction = $(e.target).attr("nextAction");
                if (nextFocus && nextFocus != "" && $("input[name='" + nextFocus + "']").length > 0) {
                    $("input[name='" + nextFocus + "']")[0].focus();
                } else if (nextAction && nextAction != "") {
                    self[nextAction] && self[nextAction]();
                }
            }
            var kkkkk = $(e.target).val();
            if (/[\ud83d\ud83c][\u0000-\ueeee]/g.test(kkkkk)) {
                kkkkk = kkkkk.replace(/[\ud83d\ud83c][\u0000-\ueeee]/g, "");
                $(e.target).val(kkkkk);
            }
        },
        'goAction': function(e) {
            var self = this;
            var $dom = $(e.target);
            if (!$dom.is("[action]")) {
                $dom = $(e.target).parents("[action]");
            }
            var hash = $dom.attr("action");
            if (hash && hash != "") {
//        		$dom.removeClass("btn_activity").addClass("btn_activity");
                var arrs = hash.split(":");
                if (arrs.length > 1) {
                    var params = [], funcName = arrs[1];
                    var _index = arrs[1].indexOf("[");
                    if (_index != -1) {
                        funcName = arrs[1].substring(0, _index);
                        params = eval(arrs[1].substring(_index));
                    }
                    params.push($dom);
                    ActionFilter(self.id, funcName, function() {
                        self[funcName].apply(self, params);
                    }, params);
                } else {
                    DMJS.router.navigate(hash, true);
                }
            }
        },
        'inpN':function(e){
        	var $dom=$(e.target),$domS=($dom.val()).replace(/\./g,"");
        	if(!($dom.val())){
        		$dom.val('');
        	}
        	$dom.val($domS);
        },
        loadScroller: function(scrollerId) {
            var self = this;
            scrollerId = scrollerId ? scrollerId : self.id;
            if (!self.scroller) {
                var wraper = $("#"+scrollerId);
                var wraper = self.$el;
                wraper.height(wrapView.height -
                        $("#header").height() - $("#footer").height());
                     
                this.scroller = new iScroll(wraper[0], {
                  
                });
            } else {
                this.scroller.refresh();
            }
        },
        
        reflush: function() {
            if (this.scroller) {
                this.scroller.destroy  ? this.scroller.destroy() : null;
                delete this.scroller;
            }
            this.init();
            this.render();
        },
        lazyRender: function() {
            var self = this;
            DMJS.CommonTools.toggleLoadingInPage(self, true);
            self._lazyRender = setTimeout(function() {
                self.render();
            }, Config.base("lazyRender") || 500);
        },
        _inputFocus: function(e) {
            $(e.target).parents("div.uinput").addClass("input-focus");
        },
        _inputBlur: function(e) {
            $(e.target).parents("div.uinput").removeClass("input-focus");
        }
    });

    // 用于存放应用中共享的视图对象
    DMJSView.shareViews = {
        // @private
        _shareList: {},
        /**
         * 添加共享的视图
         */
        add: function(name, view) {
            this._shareList[name] = view;
        },
        /**
         * 获取共享的视图
         */
        get: function(name) {
            return this._shareList[name];
        },
        /**
         * 移除视图
         */
        remove: function(name) {
            var shareList = this._shareList;
            shareList[name] = null;
            delete shareList[name];
        }
    }

    return DMJSView;
});


/**
 * DMJSAll用于一次性导入所有DMJS模块
 */
define("DMJSAll", ['DMJS', //
    'DMJSModel', //
    'DMJSCollection', //
    'DMJSView', //
    'DMJSRouter', //
    'DMJSHistory', //
    'DMJSController'], function(DMJS) {
    return DMJS;
});


require(["commonConfig/base", 'commonTool/Console'], function(baseConfig) {
    baseConfig.configDone(function() {
        require(['DMJSAll',
            'commonController/MainController',
            "filters/main",
            'Native', 'Lib/base64',
            'commonConfig/logic',
            'commonConfig/url',
            'commonConfig/lang',
            'commonClass/request',
            'commonClass/scroll/iscroll',
            'commonTool/thread'
        ],
                function(DMJS, MainController) {
                    require(['jQuery', 'common/Context','mobiscrollAll'], function(jQuery, appContext) {
                        jQuery.noConflict();//防止跟其他库冲突 (名字)
                        console.log("进入主要流程");
                        var ua = window.navigator.userAgent.toLowerCase();
						if(ua.match(/MicroMessenger/i) != 'micromessenger' || ua.match(/Mobile/i) != 'mobile'){
						     document.head.innerHTML = '<title>抱歉，出错了</title><meta charset="utf-8"><meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=0"><link rel="stylesheet" type="text/css" href="https://res.wx.qq.com/open/libs/weui/0.4.1/weui.css">';
               				 document.body.innerHTML = '<div class="weui_msg"><div class="weui_icon_area"><i class="weui_icon_info weui_icon_msg"></i></div><div class="weui_text_area"><h4 class="weui_msg_title">请在微信客户端打开链接</h4></div></div>';
							return false;
						}
                        DMJS.init();
                        MainController.createWrapView();
                        appContext.onAppStart(function() {
							
                            $("#_initLoading").remove();
                            if(window.location.search && window.location.search.indexOf("?code")!=-1){//微信返回回来url自带search
                            	DMJS.navigate("user/personal/binLogin/"+window.location.search);
                            }else if (window.location.hash && window.location.hash != "") {
                            	var ind=window.location.hash.indexOf("?code");//微信返回url不带search
                            	if(ind!=-1){
                            		DMJS.navigate("user/personal/binLogin/"+window.location.hash.substring(ind));
                            	}else{
                            		var goWhereTem = (window.location.hash).substring(1);
	                                DMJS.navigate(goWhereTem);
                            	}
                               
                            } else {
                                DMJS.navigate("index/index/index");
                            }
                        });
                    });
                 });
    });
});




      