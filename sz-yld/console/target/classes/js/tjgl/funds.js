$(function(){
	  $('#income').highcharts({
          title: {
              text: ''
          },
          chart: {
              type: 'column'
          },
          exporting: {
        	  enabled: true
          },
          yAxis: {
              title: {
                  text: '金额（元）'
              },
              plotLines: [{
                  value: 0,
                  width: 1,
                  color: '#808080'
              }]
          },
          xAxis: {
              categories: ['']
          },
          tooltip: {
              valueSuffix: '元',
              headerFormat: ''
          },
          credits: {
              enabled: false
          },
          series: incomeData
      });
	  
	  $('#expenditure').highcharts({
          title: {
              text: ''
          },
          chart: {
              type: 'column'
          },
          exporting: {
        	  enabled: true
          },
          yAxis: {
              title: {
                  text: '金额（元）'
              },
              plotLines: [{
                  value: 0,
                  width: 1,
                  color: '#808080'
              }]
          },
          xAxis: {
              categories: ['']
          },
          tooltip: {
              valueSuffix: '元',
              headerFormat: ''
          },
          credits: {
              enabled: false
          },
          series: expenditureData
      });
});