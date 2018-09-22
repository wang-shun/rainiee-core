define(['text!indexTemplate/index/bidbuy.html', 'userModel/UserModel', 'commonTool/tool','commonTool/validator'], function(bidbuyTemplate, UserModel, tool,Validator) {
	var bidbuyView = DMJS.DMJSView.extend({ //项目投资
		id: 'bidbuy',
		name: 'bidbuy',
		tagName: 'div',
		className: "bidbuy",
		events: {
			'input #amount':'yqCountAmount',
			'tap #buyBid':'buyBid',
		},
		init: function(options) {
			var self = this;
			_.extend(self, options);
			self.userModel = new UserModel();
			self.fmoney = tool.fmoney;
		},
		render: function() {
			var self = this;
			if (!self.bidDetailData) {
				tool.hash_clear();
				DMJS.router.navigate('index/index/projectInvestment', true);
				return false;
			}else{
				self.remainAmount = self.bidDetailData.remainAmount;
				
			}
			self.userModel.myAccount({
				"noCache": true,
				cancelLightbox: true,
				"success": function(response) {
					self.tg = response.data.tg;
					self.overAmount = response.data.overAmount;
					self.$el.html(_.template(bidbuyTemplate, self)); // 将tpl中的内容写入到 this.el 元素中  
					self.el.children[0].style.height=wrapView.height*1+100+"px";//让高度多出100 避免虚拟键盘遮住输入框
					self.loadScroller();
					if(!(self.bidDetailData.isXsb=='true'||self.bidDetailData.isJlb=='true')){
					   self.unUseAwardList(self.bidid);
					}
					DMJS.agreementEnable('JK',"jkxy",function(){
						
						if($("#jkxy").hasClass("uhide")&&self.isShowFXTS=='false'){
							$("#agreementFlag").remove();
						}
						if($("#jkxy").hasClass("uhide")&&self.isShowFXTS=='true'){
							 $("#autid").attr('title','请阅读风险提示函');
						}else if(!$("#jkxy").hasClass("uhide")&&self.isShowFXTS=='false'){
							$("#autid").attr('title','请阅读借款协议');
						}
						
					});
				},
				'error': function(response) {
					DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"), response && response.description);
				}
			});
			return this;
		},
		"turnRememberRido": function(e) {
			document.activeElement.blur();
			if ($("input[type='checkbox']").is(":checked")) {
				$("input[type='checkbox']").removeAttr("checked");
			} else {
				$("input[type='checkbox']").attr("checked", true);
			}
		},
		agreement: function(type) {
			this.noDestroy = true;
			if(!!type){
				DMJS.router.navigate("user/personal/agreement/"+type, true);
			}
			
		},
		
		
		'unUseAwardList': function(id) { //奖励列表
			var self = this;
			self.controller.indexModel.unUseAwardList({
				cancelLightbox: true,
				data:{id:id},
				"success": function(_data) {
					var data = _data.data;
					self.selectAwardList(data);
				}
			});
		},
		'selectAwardList': function(list) { //选择奖励,optionUseRule:满多少金额,optionCutAmount:减多少金额
			var self = this;
			var optionVals = [],optionText = [],optionUseRule=[],optionType=[],optionCutAmount=[],i=0,jxqList=list.jxqList,redPkgList=list.redPkgList;
				
				optionVals[i]='0';
				optionText[i]='不使用';
				
					
					if(list.experAmonut&&(!!list.experAmonut)&&parseInt(list.experAmonut)>0){//体验金
						i++;
						optionVals[i]='1';
						optionText[i]=list.experAmonut+'元体验金';
						optionType[i]=1;
						optionCutAmount[i]=list.experAmonut;
					}
			
				if(jxqList&&(jxqList.length>0)){//加息券
					for(var j=0;j<jxqList.length;j++){
						i++;
						optionVals[i]=jxqList[j].id;
						optionText[i]=jxqList[j].investUseRule*1==0 ? jxqList[j].amount+'%加息券(无限制)' :jxqList[j].amount+'%加息券(投资满'+jxqList[j].investUseRule+'元可用)';
						optionUseRule[i]=jxqList[j].investUseRule;
						optionCutAmount[i]=jxqList[j].amount;
						optionType[i]=2;
						
					}
				}
				if(redPkgList&&(redPkgList.length)>0){//红包
					for(var j=0;j<redPkgList.length;j++){
						i++;
						optionVals[i]=redPkgList[j].id;
						optionText[i]=redPkgList[j].investUseRule*1==0 ? redPkgList[j].amount+"元红包(无限制)" : redPkgList[j].amount+'元红包(投资满'+redPkgList[j].investUseRule+'元可用)';
						optionUseRule[i]=redPkgList[j].investUseRule;
						optionCutAmount[i]=redPkgList[j].amount;
						optionType[i]=3;
					}
				}
				if(optionVals.length<=1){
					$('#JlList').addClass('uhide');
					return false;
				}
				DMJS.CommonTools.showList({//parseInt(optionUseRule[index])
                              'inputID':"selectReward",
                              'id':"select_selectReward",
                              'optionVals':optionVals,
                              'optionText':optionText
                          },function(e){
                          	
                        	  var select=e[0];
                        	  var index=select.selectedIndex;
                        	  $('#selectReward').text(optionText[index]);
                        	  if(optionVals[index]==='0'){//不使用
                        	  	$('#userReward').val('');
                        	  	$('#myRewardType').val('');
                        	  	$('#jxqRule').val('');
                        	  	$('#hbRule').val('');
                        	  }else{
                        	  	$('#userReward').val('1');
                        	  	if(optionType[index]==1){
                        	  		
                        	  		$('#myRewardType').val('experience');
                        	  		$('#userRewardAmount').val(optionCutAmount[index]);
                        	  		$('#userRewardRule').val('');
                        	  		$('#hbRule').val('');
                        	  		$('#jxqRule').val('');
                        	  	}else if(optionType[index]==2){
                        	  		$('#myRewardType').val('jxq');
                        	  		$('#jxqRule').val(optionVals[index]);
                        	  		$('#userRewardAmount').val(optionCutAmount[index]);
                        	  		$('#userRewardRule').val(parseInt(optionUseRule[index]));
                        	  		$('#hbRule').val('');
                        	  	}else{
                        	  		$('#myRewardType').val('hb');
                        	  		$('#hbRule').val(optionVals[index]);
                        	  		$('#userRewardAmount').val(optionCutAmount[index]);
                        	  		$('#userRewardRule').val(parseInt(optionUseRule[index]));
                        	  		$('#jxqRule').val('');
                        	  	}
                        	  	
                        	  	
                        	  }
                        	self.yqCountAmount();//重新计算预期收益
                      });
			
		},
		'buyBid':function(){//购买标
			document.activeElement.blur();
			var self=this;
			if(!Validator.check($("#"+self.id))) {
				return false;
			}
			var params = $("#" + self.id).getFormValue(),
				amount=parseInt(params['amount']),userReward=params['userReward'],
				userRewardRule=$('#userRewardRule').val(),content,dataTips,
				userRewardAmount=(($('#userRewardAmount').val()).replace(/,/g, '')*1),jxqRule=params['jxqRule'],
				hbRule=params['hbRule'];
			
			if(!self.validateAmount(amount,userReward,userRewardRule,self))//验证
				{
					return false;
				}
			
			if(userReward==='1'){//使用奖励
				if(!!jxqRule){
					
					content='您此次购买金额为'+amount+'元<br/><p style="font-size:1.2rem">使用加息券：年化<span class="fn-c-org">'+userRewardAmount*1+'</span>%返利<p>';
				}else if(!!hbRule){
					
					content='您此次购买金额为'+amount+'元<br/><p style="font-size:1.2rem">使用红包<span class="fn-c-org">'+userRewardAmount+'</span>元,实际支付金额 <span class="fn-c-org">'+(amount-userRewardAmount)+'</span>元</p>';
				}else{
					content='您此次购买金额为'+amount+'元<br/><p style="font-size:1.2rem">追加体验金投资<span class="fn-c-org">'+userRewardAmount+'</span>元</p>';
				}
				
			}else{
				
				content='您此次购买金额为'+amount+'元';
			}
			
			wrapView.FlipPrompt.confirm({
                title: '投资购买确认',
                content:_.template(content,self),
                FBntconfirm: "确定",
                FBntcancel: "取消",
                FBntConfirmColor: "pop_btn_orange",
                autoCloseBg:'false'
            }, function() {
            	  wrapView.lightBox.hide();
		          wrapView.FlipPrompt.closeFlayer();
            	  self.controller.indexModel.buyBid({
                        "data":params,
                        "success":function(_data){
                        	if(_data.data&&_data.data.url){
                        		Native.openChildWebActivity(_data.data.url);
                        	}else{
                        	    DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"),'恭喜你，投资成功。');
                        	 	var time= setTimeout(function() {
		                        var length = window.listHash.length;
		                        if(window.listHash[length-3]=='index/index/projectInvestment'){
		                        	DMJS.router.navigate('index/index/projectInvestment', true);
		                        }else{
		                        	DMJS.router.navigate('user/personal/myInvestment/tbz', true);
		                        }
		                        DMJS.CommonTools.hash_clear();
		                        clearTimeout(time);
		                        	
		                    }, 1100);
                        	
                        	}
                        },
                        "error":function(response){
                        	if(response.description.indexOf("交易密码")>=0){
							wrapView.FlipPrompt.confirm({    //提示去修改交易密码
               				title: "",
               				content: response.description,
               				FBntconfirm: "找回交易密码",
               				FBntcancel: "取消",
               				FBntConfirmColor: "pop_btn_orange",
               				autoCloseBg:"false",
               			}, function() {
               				DMJS.router.navigate("user/password/tranPwdCode", true);         				
               			}, function() {

               			});
						}else{
							DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"),response.description);
							self.$el.find("input[name='tranPwd']").val("");
						}
						

                        }
                    });
            }, function() {
               
            });
			
		},
		validateAmount:function(amount,userReward,userRewardRule,self){//验证金额
			
			if(amount<parseFloat((self.bidDetailData.minBidingAmount.replace(/,/g,'')))){
				DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"),'您的投资金额小于起投金额'+self.bidDetailData.minBidingAmount+'元',function(){
					$('#amount').val(parseFloat(self.bidDetailData.minBidingAmount.replace(/,/g,'')));
					self.yqCountAmount();//重新计算预期收益
				});
				return false;
			}else if(amount>parseFloat(self.overAmount)){
			
				wrapView.FlipPrompt.confirm({    //提示去充值
               				title: "",
               				content: '账户余额不足',
               				FBntconfirm: "充值",
							FBntcancel: "取消",
							FBntConfirmColor: "pop_btn_orange",
				}, function() {DMJS.router.navigate("user/payment/recharge", true);}, function() {});
				
				return false;
			}else if(amount>parseFloat((self.bidDetailData.maxBidingAmount).replace(/,/g,''))){
				
				DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"),'您的投资金额大于最大投标金额'+self.bidDetailData.maxBidingAmount+'元');
				return false;
			}else if(!!userReward){//使用奖励
				if(!!userRewardRule&&amount<parseFloat(userRewardRule.replace(/,/g,''))){
					DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"),'投资满'+parseFloat(userRewardRule.replace(/,/,''))+'元使用');
					return false;
				}
				if($("#myRewardType").val()=='hb'&&($("#amount").val()*1<$("#userRewardAmount").val()*1)){
					DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"),'红包金额小于投标金额才可使用');
					return false;
				}
				return true;
			}else{
				return true;
			}
		},
		/**
	 * 计算预期收益
	 * 
	 * 根据用户输入的购买金额和使用的奖励，即时显示预期收益值
	1、等额本息
	 设贷款额为a，月利率为i，年利率为I，还款月数为n，每月还款额为b，还款利息总和为Y   Y＝n×a×i×（1＋i）^n÷〔（1＋i）^n－1〕－a      i=I/12
	2、一次还本付息（自然月）
	 设贷款额为a，月利率为i，年利率为I
	 */
	/**
	 *计算预期收益
	 */
	yqCountAmount:function (e) {
		var self=this,jxq='',staus={DEBX:'等额本息',YCFQ:'本息到期一次付清',MYFX:'每月付息,到期还本','DEBJ':'等额本金',YCFX:'一次付息,到期还本'};
		var info=self.bidDetailData;
		var amount = $("#amount").val();
		var myreg = /^[0-9]([0-9])*$/;
		if(!!!amount){
			//$("#info").html(showDialogInfo("请输入投资金额！","doubt"));
			//$("div.popup_bg").show();
			$("#amount").val('');
			self.showYqsy(0.00);
			return;	
		}
		
		if(amount.length>10){
			$(e.target).val(amount.substr(0,10));
		}
		if(parseInt(amount)>parseInt(self.remainAmount)){
				$('#amount').val(parseInt(self.remainAmount));
				amount=(self.remainAmount*1);

		}
		
		if(!myreg.test(amount)){
			$(e.target).val(parseInt((amount=amount.replace(/\D/g,''))==''?'0':amount,10));
			//$("#info").html(showDialogInfo("投资金额必须为整数！","doubt"));
			//$("div.popup_bg").show();
			return;
		}
	//	var myRewardType = $("#myRewardType").val();
		var yearratio = 0;
		//我的加息券 需要加息券可以把下面代码打开
		if($("#myRewardType").val()=='jxq' ){
			var jxl = $("#userRewardAmount").val();//加息率
			yearratio = parseFloat(info.rate) + parseFloat(jxl/100) ;
		}else{
			yearratio =info.rate;//年利率
		}

		var month = info.cycle;//期数
		month = parseInt(month);
		amount = parseFloat(amount);
		yearratio = parseFloat(yearratio);
		//月利率
		var active = yearratio / 12;
		//累计支付利息
		var totalInterest = 0;
    if(info.paymentType == staus.DEBX){//等额本息
		var t1 = Math.pow(1 + active, month);
		var t2 = t1 - 1;
		var tmp = t1 / t2;
		//等额本息利率
		var monthratio = active * tmp;
		//累计支付利息
		totalInterest = (amount*monthratio).toFixed(2)* month - amount;
        }else if(info.paymentType == staus.MYFX || info.paymentType == staus.YCFX){//每月付息,到期还本
		//到期还本：按月付息、到期还本
		totalInterest = amount * month * active;
        }else if(info.paymentType == staus.YCFQ){//本息到期一次付清
		//借款到期后一次性归还本金和利息。
        if(info.isDay == "S"){
       //如果是按天计算
		active = yearratio / 360;
        }
		totalInterest = month * amount * active;
        }else if(info.paymentType == staus.DEBJ){//等额本金
		totalInterest = ((amount / month + amount * active) + amount / month * (1 + active)) / 2 * month - amount;
        }
		//我的体验金
		if($("#myRewardType").val()=='experience'){
			var tyj=$("#userRewardAmount").val().replace(/,/g,"")*1;
			self.tyjYqCountAmount(amount,totalInterest,tyj,self.bidid);
		}else{
			self.showYqsy(totalInterest);
		}
	},
	showYqsy:function(totalInterest){
		var yqData = (Math.round(totalInterest*100)) / 100;//存款利息：取两位小数
		if (!isNaN(yqData)) {
			$("#expectedRate").text(yqData);
		}
	},
	/**
	 *  体验金计算预期收益
	 */
	tyjYqCountAmount:function(amount,totalInterest,tyjAmount,id) {
		var self=this;
	//	var tyjAmount = <%=enperienceAmount %>;
		//判断是否追加体验金,大于0表示已追加.计算体验金利息
		var dataParam = {"tyjAmount":tyjAmount,"biaoId":id};
		if(amount > 0 && tyjAmount > 0){//发送ajax请求
			self.controller.indexModel.tyjAmountLoan(
				{
				"noCache": true,
				cancelLightbox: true,
				data:dataParam,
				"success": function(response) {
					totalInterest = parseFloat(totalInterest) + parseFloat(response.data) ;
					self.showYqsy(totalInterest);
				},
				'error': function(response) {
					self.showYqsy(totalInterest);
				}
			});
		}else{
			self.showYqsy(totalInterest);
		}
	}
	
	
	});

	return bidbuyView;
});