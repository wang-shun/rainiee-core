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
              categories: ['一月','二月','三月','四月','五月','六月','七月','八月','九月','十月','十一月','十二月']
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