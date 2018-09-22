define(['text!indexTemplate/discover/calculator.html','commonTool/validator'], function(calculatorTemplate,Validator){
    var loanPersonView = DMJS.DMJSView.extend({
        id: 'financialCalculator',
        name: 'financialCalculator',
        tagName: 'div',
        className: "bg-c-gray", 
        events: {
        	'input #rate':"calculateFees"
        },
        init: function(options){
           	_.extend(this, options); 
           	 this.noDestroy=false;
        },
//      data:{},
        render: function(){
            var self=this;
            this.$el.html(_.template(calculatorTemplate, self.data)); // 将tpl中的内容写入到 this.el 元素中  
            self.initData();
            return this;
        },
        submit:function(){
        	var self=this;
        	if(!Validator.check($("#"+self.id))) {return false}
        	var params = self.$el.getFormValue();
//      	params =  {amount: "16", rate: "12", loanTime: "12", repaymentWay: "1"};
        	self.data = params;
        	DMJS.router.navigate("index/index/calculation", true);
        },
		initData:function()
		{
			//初始化借款期限
//			var optionVals=[],optionText=[];
//			for(var i=1;i<37;i++){
//				optionVals.push(i);
//				optionText.push(i+"个月");
//			}
//			DMJS.CommonTools.showList({
//               'inputID':"loanTime",
//               'id':"select-loanTime",
//               'optionVals':optionVals,
//               'optionText':optionText
//          },function(){});
             //初始化还款方式
            var optionVals1=['1','2','3','4','5','6'],optionText1=['等额本息','每月付息，到期还本','等额本金','一次付息，到期还本','本息到期一次付清(按月)','本息到期一次付清(按天)'];
			DMJS.CommonTools.showList({
                 'inputID':"repaymentWay",
                 'id':"select-repaymentWay",
                 'optionVals':optionVals1,
                 'optionText':optionText1
            },function(e){
            	if(e[0].selectedIndex=='4'){
            		$('#loanTime').attr({'placeholder':'请输入1~365','validator':'notEmpty;val[1,365]'});
            		$('#select-loanTime').text('天');
            	}else{
            		$('#select-loanTime').text('月');
            		$('#loanTime').attr({'placeholder':'请输入1~36','validator':'notEmpty;val[1,36]'});
            	}
            });
             
       },
       "calculateFees":function(e){
			var self=this;
        	var __val=$(e.target).val();
  	     	__val=__val.replace(/[^\d\.]/g,"").replace(/\.{2,}/,".").replace(/^[0|\.]/g,'');
	   		if(__val.split(".").length>=2){
      		var __vals=__val.split(".");
      		var __vals_last=__vals[0];
      		var xhd=__vals[1];
      		__val=__vals_last+"."+xhd.substring(0,2)
	  		 }
             amount=__val*1;
           	 $(e.target).val(__val);
        },
     
    });

    return loanPersonView;
});
