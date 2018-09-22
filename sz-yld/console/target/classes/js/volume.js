$(function(){
	if(y>0){
		var data = [{name: '', data: []},{name: '', data: []}];
		data[0].name = y-1+'年度';
		data[1].name = y+'年度';
		data[0].data = lastData1;
		data[1].data = columnData1;
		var monStr = ['一月','二月','三月','四月','五月','六月','七月','八月','九月','十月','十一月','十二月'];
		createColumn('column1', data, monStr);
		data[0].data = lastData2;
		data[1].data = columnData2;
		createColumn('column2', data, monStr);
	}else{
		var data = [{name: '', data: []}];
		data[0].name = '累计成交金额';
		data[0].data = columnData1;
		createColumn('column1', data, monthData);
	}
	
	createPie('pie1', '按标的属性', data1);
	createPie('pie2', '按期限', data2);
	if(data3.length > 0){
		createPie('pie3', '按地域', data3);
	}
});

function createColumn(id, data, monthData){
	var pf = '';
	var yTitle = '';
	if('column1' == id){
		pf = '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' +
			'<td style="padding:0"><b>{point.y:.2f} 元</b></td></tr>';
		yTitle = '金额（元）';
	}else {
		pf = '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' +
			'<td style="padding:0"><b>{point.y:.0f} 笔</b></td></tr>';
		yTitle = '交易（笔）';
	}

	$('#'+id).highcharts({
		chart: {
			type: 'column'
		},
		title: {
			text: ''
		},
		credits: {
			enabled: false
		},
		xAxis: {
			categories:monthData
		},
		yAxis: {
			min: 0,
			title: {
				text: yTitle
			}
		},
		tooltip: {
			headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
			pointFormat: pf,
			footerFormat: '</table>',
			shared: true,
			useHTML: true
		},
		plotOptions: {
			column: {
				pointPadding: 0.2,
				borderWidth: 0
			}
		},
		series: data
	});
}
function createPie(id, name, data){
	 $('#'+id).highcharts({
	        chart: {
	            plotBackgroundColor: null,
	            plotBorderWidth: 0,
	            plotShadow: false
	        },
	        title: {
	            text: '',
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
	                    distance: 20,
	                    style: {
	                        color: '#000000'
	                    },
						connectorColor: '#000000',

	                },
	                startAngle: -180,
	                endAngle: 180
	            }
	        },
	        series: [{
				type: 'pie',
				name: name,
				data: data
	        }]
	    });
}