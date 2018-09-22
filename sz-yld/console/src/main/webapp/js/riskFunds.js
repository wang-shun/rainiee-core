$(function(){
	  
	  $('#line').highcharts({
          title: {
              text: year+'年季度风险保证金统计折线图',
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
          exporting: {
          	  enabled: true
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
              name: '支出',
              data: amountIn
          }, {
              name: '收入',
              data: amountOut
          }, {
              name: '盈亏',
              data: amountSum
          }
          /*, {
              name: '余额',
              data: amountBalance
          }*/
          ]
      });
});