$(function(){
	if(year >0) {
		showQuarter();
	}else{
		showAllUser();
		createPie("invest_loan","投资/借款用户分布饼状图",investLoan);
		createPie("age_ranage","平台用户年龄分布饼状图",ageData);
	}
});

function showQuarter(){
	var categories1 = ['一季度','二季度','三季度','四季度'];
	var data =  [
		{
			name: '平台自然人',
			data: [],
			stack: 0
		}, {
			name: '平台法人',
			data: [],
			stack: 0},
		{
			name: '新增平台自然人',
			data: [],
			stack: 1
		}, {
			name: '新增平台法人',
			data: [],
			stack: 1
		}];
	data[0].data = q0;
	data[1].data = q1;
	data[2].data = q2;
	data[3].data = q3;
	create('quarter', categories1, data);

	var categories2 = ['一月','二月','三月','四月','五月','六月','七月','八月','九月','十月','十一月','十二月'];
	data[0].data = m0;
	data[1].data = m1;
	data[2].data = m2;
	data[3].data = m3;
	create('month', categories2, data);
}

function showAllUser(){
	var categories1 = ['全部'];
	var data =  [
		{
			name: '平台自然人',
			data: [],
			stack: 0
		}, {
			name: '平台法人',
			data: [],
			stack: 0}
		];
	data[0].data = a0;
	data[1].data = a1;
	create('quarter', categories1, data);
}

function create(id, categories, data){
	$('#'+id).highcharts({
        chart: {
            type: 'column'
        },
		exporting:{
			enabled: true
		},
        title: {
            text: ''
        },
        xAxis: {
            categories: categories
        },
        yAxis: {
            min: 0,
            title: {
                text: '数量(人)'
            }
        },
        credits: {
        	enabled: false
        },
        legend: {
        	enabled: true
        },
        plotOptions: {
            column: {
                stacking: 'normal'
            }
        },
        series: data
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
			text: name
		},
		tooltip: {
			pointFormat: '{series.name}: <b>{point.percentage:.2f}%</b>'
		},
		credits: {
			enabled: false
		},
		plotOptions: {
			pie: {
				allowPointSelect: true,
				cursor: 'pointer',
				dataLabels: {
					enabled: true,
					color: '#000000',
					connectorColor: '#000000',
					format: '<b>{point.name}</b>: {point.percentage:.2f} %'
				}
			}
		},
		series: [{
			type: 'pie',
			name: name,
			data: data
		}]
	});
}