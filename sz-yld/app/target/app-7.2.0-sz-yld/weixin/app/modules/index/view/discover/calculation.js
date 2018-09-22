define(['text!indexTemplate/discover/calculation.html','commonTool/validator'], function(calculationTemplate,Validator){
    var loanPersonView = DMJS.DMJSView.extend({
        id: 'calculation',
        name: 'calculation',
        tagName: 'div',
        className: "bg-c-gray", 
        events: {
        },
        init: function(options){
           	_.extend(this, options); 
           	 this.noDestroy=false;
        },
        render: function(){
            var self=this;
            self.initData();
            this.$el.html(_.template(calculationTemplate, self.data)); // 将tpl中的内容写入到 this.el 元素中  
            self.loadScroller();
            return this;
        },
        submit:function(){
        	
        },
		initData:function()
		{
//			Object {amount: "16", rate: "12", loanTime: "1", repaymentWay: "等额本息"}
            var self = this;
			 self.data.repaymentDetail = [];
	    if(this.data.repaymentWay=="1")//等额本息
			 {
			 	var rate = self.data.rate * 1;
			 	var loanTimes = self.data.loanTime * 1;
			 	var monthRate = rate/12/100;
			 	var totalAmount = self.data.amount;
			 	
			 	var temp = Math.pow((1+monthRate),loanTimes);
			 	var chbx = (totalAmount*(monthRate*temp/(temp-1))).toFixed(2);//偿还本息
			 	var totalInterest = 0;//总利息
			 	var hasRepaymentTotal = 0;//已还本金之和
			 	var interest = 0;//月利息
			 	var monthPrincipal = 0;//月供本金
			 	for(var i = 0; i < loanTimes; i++){
			 		var list = {};
			 		list.repaymentbx = chbx;
			 		
			        if(i==loanTimes-1){
				        list.monthPrincipal = (totalAmount - hasRepaymentTotal.toFixed(2)*1).toFixed(2);//偿还本金
						list.interest= (chbx - list.monthPrincipal).toFixed(2);//偿还利息
						if(list.interest<0){
							list.interest=parseFloat(0).toFixed(2);
						}
						totalInterest = totalInterest + list.interest*1;
				        list.remainPrincipal = "0.00";//剩余本金
			        }else{
			        	//月供利息=（本金-已还本金之和）*月利率
						interest= (totalAmount - hasRepaymentTotal) * monthRate;
					    list.interest = interest.toFixed(2);
						totalInterest = (totalInterest + interest).toFixed(2)*1;
						//月供本金=月供本息-月供利息
				        monthPrincipal = (chbx - interest).toFixed(2)*1;
				        list.monthPrincipal = monthPrincipal.toFixed(2);
				        hasRepaymentTotal = (hasRepaymentTotal + monthPrincipal).toFixed(2)*1;
				        //剩余本金 = totalMoney - hasRepaymentTotal
			        	list.remainPrincipal = (totalAmount - hasRepaymentTotal).toFixed(2);
			        }
			        
			 		self.data.repaymentDetail.push(list);
			 	}
			 	self.data.totalInterest = totalInterest.toFixed(2);
			 }else if(this.data.repaymentWay=="2")//每月付息，到期还本
			 {
			 	var rate = self.data.rate * 1;
			 	var loanTimes = self.data.loanTime * 1;
			 	var monthRate = rate/12/100;
			 	var totalAmount = self.data.amount*1;
			 	
			 	var interest = totalAmount * monthRate;//月利息
			 	var totalInterest = interest * loanTimes;//总利息
			 	var monthPrincipal = 0;//月供本金
			 	for(var i = 0; i < loanTimes; i++){
			 		var list = {};
			 		list.interest = interest.toFixed(2);
					if(i == loanTimes-1){
						 list.repaymentbx = (totalAmount+interest).toFixed(2);
			             list.monthPrincipal = totalAmount.toFixed(2);
			             list.remainPrincipal = "0.00";
					}else{
						 list.repaymentbx = list.interest;
						 
			             list.monthPrincipal = "0.00";
			             list.remainPrincipal = totalAmount.toFixed(2);
					}
			 		self.data.repaymentDetail.push(list);
			 	}
			 	self.data.totalInterest = totalInterest.toFixed(2);
			 }else if(this.data.repaymentWay=="3")//等额本金
			 {
			 	
			 	var rate = self.data.rate * 1;
			 	var loanTimes = self.data.loanTime * 1;
			 	var monthRate = rate/12/100;
			 	var totalAmount = self.data.amount;
			 	
			 	var monthPrincipal = totalAmount / loanTimes; //每月应偿还的本金
				var interest = 0.00; //月供利息
				var hasRepaymentTotal = 0.00; //已还总额
				var totalInterest = 0.00; //利息总额
			 	for(var i = 0; i < loanTimes; i++){
			 		var list = {};
			        //每月应还利息 = 剩余本金（本金-已还本金）* 月利率
			        interest = (totalAmount - hasRepaymentTotal) * monthRate;
					list.interest = interest.toFixed(2);
					hasRepaymentTotal = hasRepaymentTotal + monthPrincipal;
					totalInterest = totalInterest + interest;
					list.repaymentbx = (monthPrincipal + interest).toFixed(2);
			        list.monthPrincipal = monthPrincipal.toFixed(2);
			        //剩余本金 = totalMoney - hasRepaymentTotal
			        if(i==loanTimes-1){
			        	list.remainPrincipal = "0.00";
			        }else{
			        	list.remainPrincipal = (totalAmount - hasRepaymentTotal).toFixed(2);
			        }
			        
			 		self.data.repaymentDetail.push(list);
			 	}
			 	self.data.totalInterest = totalInterest.toFixed(2);
			 	
			 }else if(this.data.repaymentWay=="4"){//一次付息，到期还本
			 	var rate = self.data.rate * 1;
			 	var loanTimes = self.data.loanTime * 1;
			 	var monthRate = rate/12/100;
			 	var totalAmount = self.data.amount*1;
			 	var interest = totalAmount * monthRate;//月利息
			 	var totalInterest = (interest * loanTimes).toFixed(2);//总利息
			 	
			 	for(var i = 1; i < 3; i++){
			 		var list = {};
					if(i == 1){
						 list.repaymentbx = totalInterest;
			             list.monthPrincipal = "0.00";
			             list.interest=totalInterest;
			             list.remainPrincipal = totalAmount;
					}else{
						 list.repaymentbx = totalAmount;
			             list.monthPrincipal = totalAmount;
			             list.interest="0.00";
			             list.remainPrincipal = "0.00";
					}
			 		self.data.repaymentDetail.push(list);
			 	}
			 	
			 	self.data.totalInterest = totalInterest;
			 	
			 }else if(this.data.repaymentWay=="5")//本息到期一次付清(月)
			 {
			 	var rate = self.data.rate * 1;
			 	var loanTimes = self.data.loanTime * 1;
			 	var monthRate = rate/12/100;
			 	var totalAmount = self.data.amount*1;
			 	var interest = totalAmount * monthRate; //每月应偿还的利息
			 	var totalInterest = interest * loanTimes;//总利息
			 	var monthPrincipal = 0;//月供本金
			 	var list = {};
				list.repaymentbx = (totalAmount+totalInterest).toFixed(2);
				list.interest = totalInterest.toFixed(2);
			    list.monthPrincipal = totalAmount.toFixed(2);
			    list.remainPrincipal = "0.00";
			 	self.data.repaymentDetail.push(list);
			 	this.data.loanTime = this.data.loanTime;
			 	self.data.totalInterest = totalInterest.toFixed(2);
			 	
			 }else if(this.data.repaymentWay=="6")//本息到期一次付清(天)
			 {
			 	var rate = self.data.rate * 1;
			 	var loanTimes = self.data.loanTime * 1;
			 	var totalAmount = self.data.amount*1;
			 	var totalInterest = (rate * totalAmount * loanTimes)/100/360;//总利息
			 	 
			 	var monthPrincipal = 0;//月供本金
			 	var list = {};
				list.repaymentbx = (totalAmount+totalInterest).toFixed(2);
				list.interest = totalInterest.toFixed(2);
			    list.monthPrincipal = totalAmount.toFixed(2);
			    list.remainPrincipal = "0.00";
			    list.repaymentWay=this.data.repaymentWay;
			 	self.data.repaymentDetail.push(list);
			 	this.data.loanTime = this.data.loanTime;
			 	self.data.totalInterest = totalInterest.toFixed(2);
			 	
			 }
        },
     
    });

    return loanPersonView;
});
