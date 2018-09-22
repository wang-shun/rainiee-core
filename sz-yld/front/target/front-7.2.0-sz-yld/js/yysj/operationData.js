$(function () {
	createColumn();
	createPie("investUserType","投资/借款用户分布",investLoan);
	createPie("investUserAge","平台用户年龄分布",ageData);
	createPie("loanProjectTerm","项目期限分布",timeLimtsData);
	createPie("loanProjectType","项目类型分布",projectTypeData);
	//createCirclePie("loanOverdueRate","借款逾期率","#50B432",loanOverdueData);
});

Highcharts.setOptions({  
    colors: ['#058DC7', '#50B432', '#ED561B','#DDDF00', '#24CBE5', '#64E572', '#FF9655', '#FFF263', '#6AF9C4']  
});  

function createColumn(){
	$('#investTotal').highcharts({
        chart: {
            type: 'column',
            zoomType: 'xy',
            margin: [ 50, 50, 100, 80]
        },
        title: {
            text: ''
        },
        credits: {
			enabled: false
		},
        xAxis: {
            categories: monthCategary
        },
        yAxis: {
            min: 0,
            title: {
                text: '金额(元)'
            },
            labels: {
                formatter: function() {
                	return this.value/10000 +'万';
                }
            },
            
        },
        plotOptions: {
            column: {
            	pointPadding: 0.2,
				borderWidth: 0
            }
        },
        legend: {
            enabled: false
        },
        tooltip: {
            pointFormat: '当月累计投资: <b>{point.y:.2f}</b>',
            shared: true,
			useHTML: true
        },
        series: [{
            name: '累计投资总额',
            data: monthlyData
        }]
    });
}

function createPie(id, name, data){
	$('#'+id).highcharts({
		chart: {
			plotBackgroundColor: null,
			plotBorderWidth: null,
			plotShadow: false
		},
		title: {
			text: name,
		},
		tooltip: {
			pointFormat: '{series.name}: <b>{point.percentage:.2f}%</b>'
		},
		credits: {
			enabled: false
		},
		legend: {
            layout: 'vertical',
            align: 'right',
            verticalAlign: 'top',
            borderWidth: 0
        },
		plotOptions: {
			pie: {
                allowPointSelect: true,
                cursor: 'pointer',
                dataLabels: {
                    enabled: false
                },
                showInLegend: true
            },
            series: {
	            colorByPoint: true
	        }
		},
		series: [{
			type: 'pie',
			name: name,
			data: data
		}]
	});
}

function createCirclePie(id,color, name, data){
	$('#'+id).highcharts({
		chart: {
			plotBackgroundColor: null,
			plotBorderWidth: null,
			plotShadow: false
		},
		title: {
			text: '2%',
			verticalAlign:'middle',
			y:-18,
			useHTML:true
		},
		credits: {
			enabled: false
		},
		tooltip: {
    	    pointFormat: '',
    	    enable:false
        },
		plotOptions: {
			pie: {
                dataLabels: {
                    enabled: false
                },
                innerSize:"99%",
                colors:['#fff',color]
            },
            series: {
	            colorByPoint: true
	        }
		},
		series: [{
			type: 'pie',
			name: name,
			data: [0.07,0.93]
		}]
	});
}

function fmoney(s, n) {
    n = n > 0 && n <= 20 ? n : 2;
    var f = s < 0 ? "-" : ""; //判断是否为负数
    s = parseFloat((Math.abs(s) + "").replace(/[^\d\.-]/g, "")).toFixed(n) + "";//取绝对值处理, 更改这里n数也可确定要保留的小数位
    var l = s.split(".")[0].split("").reverse(),
            r = s.split(".")[1];
    var t = "";
    for (var i = 0; i < l.length; i++) {
        t += l[i] + ((i + 1) % 3 == 0 && (i + 1) != l.length ? "," : "");
    }
    return f + t.split("").reverse().join("") + "." + r.substring(0, 2);//保留2位小数  如果要改动 把substring 最后一位数改动就可
}