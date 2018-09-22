define(['text!userTemplate/personal/myLoan.html','text!userTemplate/personal/myLoanList.html',
'commonClass/scroll/PTRScroll','text!commonTemplate/tips/loanET.html','text!commonTemplate/tips/loanT.html','commonTool/slide'
], function(myLoanTemplate,listTemplate,PTRScroll,loanET,loanT,Slide) {
    var myInvestmentV = DMJS.DMJSView.extend({
        id: 'myLoan',
        name: 'myLoan',
        tagName: 'div',
        className: "myLoan",
        events: {
        	'tap #myLoan_title' :'clickItem',
        	'tap #listContent':'myLoan_info',
        },
        init: function(options) {
            var self = this;
            var obj={page:{
        	pageIndex:[1,1,1],
        	pageSize:10,
        	isOver:[false,false,false],
        	ids:[1,2,3],
        	loaded:[false,false,false],
        	type:1,
        	parms:['1','2']}};
            _.extend(self, options,obj);
        },
        render: function() {
        	
            var self = this;
            this.noDestroy=false;
            this.$el.html(_.template(myLoanTemplate,{}));
           if(!self.BadClaim){
            	$('.YZR').remove();
            }
            self.slider = new Slide($("#listContent")[0], "H", function() {
				var cur = this.currentPoint+1;
					self.switchs(cur,'myLoan_title')
			}, false, function(e) {});
            //先注释。在微信上跑会出问题
          //  $("div[listItem").find('.ListArea').css('height',(wrapView.height-$("#header").height()-$("#myLoan_title").height()));//内容块占满高度
            self.loadListScroller(self.page);
            self.loadDatas(self.page);
            return this;
        },
        myLoan_info:function(e){
        	 var $dom=$(e.target);
               // if(!$dom.is("#information_Article  > div > div > div > div")){
                    $dom=$dom.parents("div[data-id]");
              //  }
                var id = $dom.attr("data-id");
				if(id){
					 DMJS.router.navigate('index/index/projectInvDetail/'+id,true);
				}
        },
      clickItem:function(e){
      		var self=this;
      		var target=e.target;
        	var item=target.dataset.item;
        	var type=item.substring(4)*1;
      		self.slider.moveToPoint(type-1);
      		self.switchs(type,'myLoan_title')
      },
        switchs:function(type,id){
        	var self=this;
        	/*var target=e.target;
        	var item=target.dataset.item;
        	var type=item.substring(4)*1;*/
        	var target=$("#"+id);
        	target.find('.active-blue').removeClass('active-blue');
        	target.children().eq(type-1).addClass('active-blue');
      	    var item=target.children().get(type-1).dataset.item;

        	//$('div[item]').addClass('hide');
        	//$('div[item='+item+']').removeClass('hide');

        	
        	if(type==self.page.type) return false;
        	self.page.type=type
        	if(!self.page.loaded[type-1]){
        		self.loadListScroller.apply(self,[self.page]);
        		self.loadDatas.apply(self,[self.page]);
        	}
        },
        repayEnd: function() {//提前还款
        	var ars=arguments;
        	var data={
        		loanID:ars[0],
        		accountAmount:ars[1],
        		loanManageAmount:ars[2],
        		loanMustMoney:ars[3],
        		loanPenalAmount:ars[4],
        		loanTotalMoney:ars[5],
        		number:ars[6],
        		sybj:ars[7],
        		sylx:ars[8],
        		isPrepayment:ars[9]
        	};
        	if(!data.isPrepayment){
        		DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"), '有逾期未还，无法提前还款！');
        		return false;
        	}
			var self = this;
			wrapView.FlipPrompt.confirm({
				title: "提前还款",
				content: _.template(loanET, data),
				FBntconfirm: "确认扣费",
				FBntcancel: "取消",
				FBntConfirmColor: "pop_btn_orange",
				autoCloseBg:'false',
			}, function() {
				self.controller.userModel.replayEnd({'data':{loanId:ars[0],currentTerm:ars[6]},'cancelLightbox':true,
		        	"noCache":true,
					"success":function(response){
						if(response.data.url){
							Native.openChildWebActivity(response.data.url);
						}else{
							DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"), '还款成功！',function(){
								self.reflush();
							});
						}
					},
					'error':function(response){
						DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"), response.description);
					}
					
		        });
			}, function() {});
			},
			repay:function(){//还款
			var ars=arguments;
        	var data={
        		loanID:ars[0],
        		loanMustMoney:ars[1],
        		loanArrMoney:ars[2],
        		currentTerm :ars[3]
        	};
			var self = this;
			wrapView.FlipPrompt.confirm({
				title: "还款",
				content: _.template(loanT, data),
				FBntconfirm: "确认扣费",
				FBntcancel: "取消",
				FBntConfirmColor: "pop_btn_orange",
				autoCloseBg:'false',
			}, function() {
				self.controller.userModel.replay({'data':{loanId:ars[0],currentTerm:ars[3]},'cancelLightbox':true,
	        	"noCache":true,
				"success":function(response){
					if(response.data.url){
	                		Native.openChildWebActivity(response.data.url);
	                }else{
				    	    DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"), '还款成功！',function(){
							     self.reflush();
						    });
					}
				},
				'error':function(response){
					DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"), response.description);
				},
				
	        	});
			}, function() {});
			},
        loadDatas:function(page,callBack){
        	
        	var self=this;
        	var type=page.type-1;
        	var __call=function(){
                    	var dom=self.$el.find("div[item='item"+page.ids[type]+"']").find(".ListArea")[0];
                 		var len=dom.children.length;
                 		if(len<10){
                 			if(len==0){
                 				dom.innerHTML=wrapView.noSearchDiv;
                 			}
            				return self.scroller[type].disablePullUpToLoadMore();
            			}else if(page.isOver[type]/*||$('#FlipPrompt')*/){
            				return self.scroller[type].disablePullUpToLoadMore();

            			}else{
            				return self.scroller[type].enablePullUpToLoadMore();
            			}
                   };
        	self.controller.userModel.myBidList({'data':{bidStatus:page.parms[type],pageIndex:page.pageIndex[page.type-1],pageSize:page.pageSize},'cancelLightbox':true,
        	"noCache":true,
			"success":function(response){
					response.loansType=page.parms[type];//根据type作为判断条件
				var $dom=self.$el.find("div[item='item"+page.ids[type]+"']").find(".ListArea");
				 if(!response.data||response.data.length<page.pageSize){
                   page.isOver[type]=true;
                    DMJS.CommonTools.popTip("已经加载完所有数据！");
                    }
                     if(page.pageIndex[type] == 1){$dom.html(_.template(listTemplate,response));}
                     else{$dom.append(_.template(listTemplate,response));}
                     page.pageIndex[type]++;
                     page.loaded[type]=true;
                     //self.loadScroller();
			},
			'error':function(response){
				DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"), response.description);
			},
			"complete":function(){
				callBack&&callBack();__call();
			}
        	});
        },
         loadListScroller: function(page){
         	
            var self = this; 
            var type=page.type-1;
            var wraper = self.$el.find("div[item='item"+page.ids[type]+"']"); 
            !self.scroller&&(self.scroller={});
            if(!self.scroller[type]){
            	 wraper.height(wrapView.height-
                 		$("#header").height()-$("#myLoan_title").height());
                 		//console.log()
            	self.scroller[type] = new PTRScroll(wraper[0], {
            		pullUpToLoadMore:!page.isOver[type],
                    hideScrollbar: true,
                    refreshContent: function(done){
                    	page.pageIndex[type]=1;
                    	page.isOver[type]=false;
                    	self.loadDatas(page,done);
                    },
                    loadMoreContent: function(done){
                    	self.loadDatas(page,done);
                    }
                },true);
            }
            else{
            	if(page.isOver[type]){
                	self.scroller[type].disablePullUpToLoadMore();
            	}else{
            		self.scroller[type].enablePullUpToLoadMore();
            	}
        }
        },
        
    });
    return myInvestmentV;
});
