define(['text!userTemplate/personal/cashTransfer.html','commonTool/tool'
], function(cashTransferTemplate, tool) {
    var cashTransferView = DMJS.DMJSView.extend({
        id: 'cashTransferViewContent',
        name: 'cashTransferViewContent',
        tagName: 'div',
        className: "cashTransferViewContent",
        events: {
        	
        },
        init: function(options) {
            var self = this;
            _.extend(self, options);
        },
        render: function() {
            var self = this;
            this.noDestroy=false;
           	this.$el.html(_.template(cashTransferTemplate,{}));
           	this.cteateDate();
            return this;
        },
        cteateDate:function(){
        	var D=document,count;
        	var dateList=D.getElementById('dateList');
        	var date=new Date();
        	var day=date.getDate();//天
        	var month=date.getMonth()+1;//月
        	var year=date.getFullYear();//年
        	var days= new Date(year,month,0).getDate();//获取当前月份天数
        	//console.log(new Date(year,0,1).toLocaleDateString())
        	var week=new Date(year,month-1,0).getDay();//当前月份第一天是星期几？
        	console.log('星期'+week);
        	var dd=new Date();
        	var days_bf=new Date(dd.getFullYear(),dd.getMonth(),0).getDate();//获取上个月天数;
        	//console.log(new Date(y,dd.getMonth(),0).toLocaleDateString())
  			var obj=this.getDate(days_bf,week+1,days,year,month);
            count=Math.ceil(obj.day.length/7);//计算出有几行
        	
        	var div=D.createElement('div');
        	
        	for(var i=0;i<=count;i++){
        		var child=D.createElement('div');
        		child.className='ub ub-ac';
        		for(var j=i*7;j<(i+1)*7;j++){
        			if(j>obj.length-1){continue;}
        	//每个日期DIV
        	var cl= D.createElement('div');
        		cl.className=obj.style[j];
				cl.innerHTML=obj.day[j]
				cl.setAttribute('data-riqi',obj.date[j]);
        		child.appendChild(cl);
        	}
        	div.appendChild(child);
        	}
        	dateList.appendChild(div);
        	//title-date
        	D.getElementById('title-date').innerHTML=year+' 年 '+month+" 月";
        },
        getDate:function(days_bf,week,days,year,month){
        	
        	var arr=[],arr2=[],arr3=[],date=new Date(year,month,0),riqi,nextDate=new Date(year,month+1,0);
        	console.log(date.toLocaleString());
        	var index=1;
        	if(week!=7){
        		 date.setMonth(date.getMonth()-1);
        		 console.log(date.toLocaleString());
        		 var y=date.getFullYear();
        		 var m=date.getMonth()+1;
        		for(var i=0;i<week;i++){
        			// 0 1 2 3 4 5 6
        		arr.push(days_bf+1-week);
        		arr2.push(y+"-"+m+"-"+(days_bf-week));
        		arr3.push('ub-f1 ct-date-gq');
        		days_bf++;
        	}
        	}
        	for(var i=1;i<=7-week;i++){
        			arr.push(i);
        			arr2.push(year+"-"+month+"-"+i);
        			arr3.push('ub-f1 ct-date');
        			}
        	for(var i=7-week;i<days;i++){
        		arr.push(i+1);
        		arr2.push(year+"-"+month+"-"+(i+1));
        		arr3.push('ub-f1 ct-date');
        	}
        
        	var lost=7-arr.length%7;
        	console.log(nextDate.toLocaleString());
        	for(var i=1;i<=lost;i++){
        		arr.push(i);
        		arr2.push(nextDate.getFullYear()+"-"+nextDate.getMonth()+"-"+i);
        		arr3.push('ub-f1 ct-date-gq');
        	}
        	if(arr.length!=arr2.length)
        	{console.log('出错了,长度不一致');return [];}
        	return {day:arr,date:arr2,length:arr.length,style:arr3};
        }
       
    });
    return cashTransferView;
});
