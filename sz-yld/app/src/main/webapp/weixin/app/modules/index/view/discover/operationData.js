define(['text!indexTemplate/discover/operationData.html','highCharts'], function(operationDataTemplate,highCharts){
    var operationDataView = DMJS.DMJSView.extend({
        id: 'operationDataContent',
        name: 'operationDataContent',
        tagName: 'div',
        className: "operationDataContent", 
        events: {
               
        },
        init: function(options){
           	_.extend(this,options); 
        },
        render: function(){
            var self=this; 
           
            self.operationData();
            return this;
        },
        operationData:function(){
        	var self=this;
        	
        	self.controller.indexModel.operationData({
        		data:{},
                "noCache": true,
                cancelLightbox: true,
                "success": function(response) { 
                	var yyData=response.data;
                	self.$el.html(_.template(operationDataTemplate, response.data)); // 将tpl中的内容写入到 this.el 元素中
             	   self.upDownSlide($('#Jupdown'),yyData);
             	  $(".total").text($('#Jupdown').children('div').length);
                },
                 'error':function(response){DMJS.CommonTools.alertCommon(Config.lang("alertCommonTitle"), response&&response.description);}
            });
	
        },
        upDownSlide:function(slideId,yyData){
        	var self=this;
        	var Y=0,slideH=wrapView.height -$("#header").height(),flag=0,slideL=slideId.children('div').length;
        	slideId.children('div').height(slideH);
        	slideId.bind('touchstart',function(e){
        		Y=e.touches[0].clientY;
				e.preventDefault();
				e.returnValue=false;
        	});
        	
        	slideId.bind('touchend',function(e){
        		var width=e.changedTouches[0].clientY-Y;
				console.log(width);
				this.style.webkitTransitionDuration="800ms";
				if(width>100){
					if(flag!=0){
						flag--;
						$('.txt').text(flag+1);
						
						self.choosePage(flag+1,yyData);
						$('.lastImg').hide();
						$(".showImg").addClass('footImg');
					}else{
						return false;
					}
					this.style.webkitTransform='translate(0px, -'+(flag*slideH)+'px) scale(1) translateZ(0px)';	
				}
				else if(Y-e.changedTouches[0].clientY>100){
				
					if(flag<slideL-1){
			 				flag++;
			 				$('.txt').text(flag+1);
			 				self.choosePage(flag+1,yyData);
			 				if(flag+1==slideL){
			 					$(".showImg").removeClass('footImg');
			 					$('.lastImg').show();
			 				}
							this.style.webkitTransform='translate(0px, -'+(flag*slideH)+'px) scale(1) translateZ(0px)';	
						
					}else{
						
						return false;
					}
				}
		 	
        	});
        },
        
    //根据flag的值来渲染页面  
     choosePage : function(flag,yyData){
     	var self=this;
     	switch (flag)
			{
			case 2:
			  self.toDrawLine(yyData);
			  break;
			case 3:
			  self.toDrawInvestment(yyData);
			  break;
			case 4:
			 self.toDrawAge(yyData);
			  break;
			case 5:
			 self.toDrawTimeLimts(yyData);
			  break;
			case 6:
			  self.toDrawProjectType(yyData);
			  break;
			}
     },
 
 	/*
 	 * 投资金额走势----折线统计图
 	 * 
 	 */
 	toDrawLine : function(yyData){
 		var self=this;
 		 var times=yyData.times;
        var totalInvests=yyData.totalInvests;
        var tempInvests=[],tempTimes=[];
        for(var i=0;i<times.length;i++){
        	tempTimes[i]=times[i];
        	tempInvests[i]=parseFloat(totalInvests[i]);
        }
	jQuery('#container').highcharts({
		    chart: {
				type: 'line',
			},
			title: {
				text: '<b>半年累计投资金额走势</b>',
				useHTML:true,
				margin:50
			},
			
			xAxis: {
				categories: tempTimes
			},
			yAxis: {
				title: {
					text: '金额(元)'
				},
				labels:{
					formatter:function(){
						return this.value/10000+'万';
					}
				},
				plotLines: [{
					value: 0,
					width: 1,
					color: '#808080'
				}]
			},
			
			tooltip: {
				valueSuffix: '万元'
			},
			legend: {
				enabled:false,
				layout: 'vertical',
				align: 'right',
				verticalAlign: 'middle',
				borderWidth: 0
			},
			exporting:{
                    enabled:false
                },
                credits: {
                    enabled: false
                },
			series: [{
				name: '投资金额',
				data: tempInvests
			}]
		});
 	},
 	/*
 	 * 投资人/借款人分布----饼状图
 	 */
 	toDrawInvestment : function(yyData){
 		var self=this;
 		 var totalInvestment=yyData.totalInvestment;//投资人
	    var totalLoan=yyData.totalLoan;//借款人
        	
        jQuery('#containerOne').highcharts({
            chart: {
                plotBackgroundColor: null,
                plotBorderWidth: null,
                plotShadow: false,
                type: 'pie',
               
            },
             credits:{
             	enabled:false
             },
            title: {
                text: '<b>投资人/借款人分布</b>',
                useHTML:true
            },
            tooltip: {
                pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
            },
            plotOptions: {
                pie: {
                    allowPointSelect: true,
                    cursor: 'pointer',
                    dataLabels: {
                        enabled: false
                    },
                    showInLegend: true
                }
            },
            legend: {
	            align: 'left',
	            verticalAlign: 'top',
	            x:0,
	            y: 50
        	},
            series: [{
                name: '人数比例',
                colorByPoint: true,
                data: [{
                    name: '投资人',
                    y: totalInvestment
                }, {
                    name: '借款人',
                    y: totalLoan
                   
                }]
            }]
        });
 	},
 	
 	/*
 	 * 平台用户年龄分布----饼状图
 	 */
 	toDrawAge : function(yyData){
 		var self=this;
 		 var ageType=yyData.ageType;
        var ageData=yyData.ageData;
        var temp=[];
        for(var i=0;i<ageType.length;i++){
        	temp[i]={name:ageType[i],y:ageData[i]}
        }
        
        jQuery('#containerTwo').highcharts({
            chart: {
                plotBackgroundColor: null,
                plotBorderWidth: null,
                plotShadow: false,
                type: 'pie'
            },
            title: {
                text: '<b>平台用户年龄分布</b>',
                useHTML:true
            },
            credits:{
             	enabled:false
             },
            tooltip: {
                pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
            },
            plotOptions: {
                pie: {
                    allowPointSelect: true,
                    cursor: 'pointer',
                    dataLabels: {
                        enabled: false
                    },
                    showInLegend: true
                }
            },
            legend: {
	            align: 'left',
	            verticalAlign: 'top',
	            x:0,
	            y: 50
        	},
            series: [{
                name: '年龄比例',
                colorByPoint: true,
                data: temp
            }]
        });
 	},
 	/*
 	 * 项目期限分布----饼状图
 	 */
 	toDrawTimeLimts: function(yyData){
 		var self=this;
 		var timeLimtsType=yyData.timeLimtsType;
        var timeLimtsData=yyData.timeLimtsData;
        var tempTime=[];
        for(var i=0;i<timeLimtsType.length;i++){
        	tempTime[i]={name:timeLimtsType[i],y:timeLimtsData[i]}
        }
        jQuery('#containerThree').highcharts({
            chart: {
                plotBackgroundColor: null,
                plotBorderWidth: null,
                plotShadow: false,
                type: 'pie'
            },
            title: {
                text: '<b>项目期限分布</b>',
                useHTML:true
            },
            credits:{
             	enabled:false
             },
            tooltip: {
                pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
            },
            plotOptions: {
                pie: {
                    allowPointSelect: true,
                    cursor: 'pointer',
                    dataLabels: {
                        enabled: false
                    },
                    showInLegend: true
                }
            },
            legend: {
	            align: 'left',
	            verticalAlign: 'top',
	            x:0,
	            y: 50
        	},
            series: [{
                name: '项目比例',
                colorByPoint: true,
                data: tempTime
            }]
        });
 	},
 	/*
 	 * 项目类型分布----饼状图
 	 */
 	toDrawProjectType : function(yyData){
 		var self=this;
 		var projectType=yyData.projectTypeType;
		var projectTypeData=yyData.projectTypeData;
		var tempProject=[];
        for(var i=0;i<projectType.length;i++){
        	tempProject[i]={name:projectType[i],y:projectTypeData[i]}
        }
		jQuery('#containerFour').highcharts({
            chart: {
                plotBackgroundColor: null,
                plotBorderWidth: null,
                plotShadow: false,
                type: 'pie'
            },
            title: {
                text: '<b>项目类型分布</b>',
                useHTML:true
            },
            credits:{
             	enabled:false
             },
            tooltip: {
                pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
            },
            plotOptions: {
                pie: {
                    allowPointSelect: true,
                    cursor: 'pointer',
                    dataLabels: {
                        enabled: false
                    },
                    showInLegend: true
                }
            },
            legend: {
	            align: 'left',
	            verticalAlign: 'top',
	            x:0,
	            y: 50
        	},
            series: [{
                name: '类型比例',
                colorByPoint: true,
                data: tempProject
            }]
        });
 	},
 	
		chart:function(times,datas){
			//处理datas 转成整形\
			var arr=[];
			if(Array.isArray(datas)) 
			{
				arr=datas.map(function(item){
					return item*1;
				})
			}
			//转下单位
			var b=arr.some(function(item){
				if(item>10000){
					return true;
				}
			})
			if(b){
				arr=datas.map(function(item){
					return (item/10000).toFixed(2);
					
				});
				$("#countSSS").html('累计投资金额(万元)');
			}
			
		var barChartData = {
		labels : times,
		datasets : [
			{
				fillColor : "rgba(151,187,205,0.5)",
				strokeColor : "rgba(151,187,205,0.8)",
				highlightFill : "rgba(151,187,205,0.75)",
				highlightStroke : "rgba(151,187,205,1)",
				data : arr
			}
			
		]
	}
		var ctx = document.getElementById("canvas").getContext("2d");
		window.myBar = new Chart(ctx).Bar(barChartData, {
			responsive : true
		});
		}
    });

    return operationDataView;
});



