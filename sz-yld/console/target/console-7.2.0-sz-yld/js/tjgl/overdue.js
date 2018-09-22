$(function () {
    $('#column1').highcharts({
        chart: {
            type: 'column'
        },
        exporting:{
            enabled: true
        },
        title: {
            text: '<strong>逾期人数及金额统计</strong>'
        },      
        xAxis: {
            categories: [
                y+'-01',
                y+'-02',
                y+'-03',
                y+'-04',
                y+'-05',
                y+'-06',
                y+'-07',
                y+'-08',
                y+'-09',
                y+'-10',
                y+'-11',
                y+'-12'
            ]
        },
        /*yAxis: {
            min: 0,
            title: {
                text: '金额（元）'
            }
        },*/
        yAxis: [{
        	min: 0,
            title: {
                text: '金额（元）'
            }
        }, {
        	min: 0,
        	opposite: true,
        	allowDecimals:false,
            title: {
                text: '人数（人）'
            }
        }],
        tooltip: {
            formatter: function () {
                var s = '<b>' + this.x + '</b>';
                $.each(this.points, function () {
                    s += '<br/>' + this.series.name + ': '; 
                    if(this.series.name == '人数'){
                        s += this.y + '（人）';
                    }else{
                        s += Highcharts.numberFormat(this.y,2) + '（元）';
                    }
                });
                return s;
            },
            shared: true
        },
        credits: {
            enabled: false
        },
        plotOptions: {
            column: {
                pointPadding: 0.2,
                borderWidth: 0
            }
        },
        series: [{
        	name: '金额',
            data: yqjeData
        }, {
            name: '人数',
            data: yqrsData,
            yAxis: 1
        }]
    });
});				
