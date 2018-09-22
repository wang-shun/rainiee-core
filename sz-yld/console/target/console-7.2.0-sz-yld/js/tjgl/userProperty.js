$(function(){
	createPie("user_sex","",userSexData,"用户性别");
	createPie("user_age","",userAgeData,"用户年龄");
	createPie("user_register_source","",userRegisterSourceData,"用户终端");
	createPie("user_invest_source","",userInvestSourceData,"投资终端");
	createPie("user_type","",investLoanData,"用户分布");
});

function createPie(id, title, data,name){
	$('#'+id).highcharts({
		chart: {
			plotBackgroundColor: null,
			plotBorderWidth: null,
			plotShadow: false
		},
		title: {
			text: title,
		},
		tooltip: {
			pointFormat: '{series.name}: <b>{point.percentage:.2f}%</b>'
		},
		exporting: {
      	  enabled: true
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