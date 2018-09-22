$(function(){
	  
	  $('#line').highcharts({
          title: {
              text: '',
              x: -20 
          },
          subtitle: {
              text: '',
              x: -20
          },
          xAxis: {
              categories: ['一季度','二季度','三季度','四季度']
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
          credits: {
              enabled: false
          },
          tooltip: {
              valueSuffix: '元'
          },
          legend: {
              layout: 'vertical',
              align: 'right',
              verticalAlign: 'middle',
              borderWidth: 0
          },
          series: [{
              name: '收入',
              data: amountIn
          }, {
              name: '支出',
              data: amountOut
          }, {
              name: '盈亏',
              data: amountSum
          }
          ]
      });
});