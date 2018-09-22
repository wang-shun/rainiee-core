<%@page import="com.dimeng.framework.resource.ResourceProvider"%>
<%@page import="com.dimeng.framework.config.ConfigureProvider"%>
<%@page import="com.dimeng.framework.resource.ResourceRegister"%>
<%@page import="com.dimeng.p2p.variables.defines.SystemVariable"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	final ResourceProvider resourceProvider = ResourceRegister
			.getResourceProvider(application);
	final ConfigureProvider configureProvider = resourceProvider
			.getResource(ConfigureProvider.class);
	final String protocolType = configureProvider.format(SystemVariable.SITE_REQUEST_PROTOCOL);
	String url = configureProvider.format(SystemVariable.SITE_DOMAIN);
	if (url.indexOf(protocolType) == -1) 
	{
		url = protocolType.concat(url);
	}
	
%>
<html>
<head>
<meta name="viewport"
	content="width=device-width, initial-scale=1, user-scalable=no, minimum-scale=1.0, maximum-scale=1.0">
<meta content="email=no" name="format-detection" />
<meta content="telephone=no" name="format-detection" />
<script type="text/javascript" src="js/Chart.bundle.js"></script>
<style type="text/css">
@charset "utf-8";

* {
	margin: 0;
	padding: 0;
}

body {
	height: 100%;
	padding: 0px;
	margin: 0px;
	font-size: 100%;
	color: #666;
	width: 100%;
	height: 100%;
	font-family: "微软雅黑";
}

@media screen and (min-width:0px) and (max-width:240px) {
	body {
		font-size: xx-small;
	}
	html {
		font-size: 9px;
	}
}

@media screen and (min-width:241px) and (max-width:384px) {
	body {
		font-size: x-small;
	}
	html {
		font-size: 11px;
	}
}

@media screen and (min-width:385px) and (max-width:480px) {
	body {
		font-size: small;
	}
	html {
		font-size: 13px;
	}
}

@media screen and (min-width:481px) and (max-width:640) {
	body {
		font-size: medium;
	}
	html {
		font-size: 16px;
	}
}

@media screen and (min-width:640px) {
	body {
		font-size: large;
	}
	html {
		font-size: 19px;
	}
}
/*字体大小*/
.fn-s-11 {
	font-size: 1.1rem;
}

.fn-s-12 {
	font-size: 1.2rem;
}

.fn-s-13 {
	font-size: 1.3rem;
}

.fn-s-15 {
	font-size: 1.5rem;
}

.fn-s-14 {
	font-size: 1.4rem;
}

.fn-s-17 {
	font-size: 1.7rem;
}

.fn-s-19 {
	font-size: 1.9rem;
}

.fn-s-20 {
	font-size: 2rem;
}

.fn-s-25 {
	font-size: 2.5rem;
}

.fn-w-b {
	font-weight: bold;
}

.fn-c-black { #666;
	
}

.fn-c-blue {
	color: #34B8FC;
}

.ub-ac {
	-webkit-box-align: center;
	box-align: center;
}

.ub-ae {
	-webkit-box-align: end;
	box-align: end;
}

.ub-pc {
	-webkit-box-pack: center;
	box-pack: center;
}

.ub-pe {
	-webkit-box-pack: end;
	box-pack: end;
}

.ub-pj {
	-webkit-box-pack: justify;
	box-pack: justify;
}

.ub-ver {
	-webkit-box-orient: vertical;
	box-orient: vertical;
}

.ub-ba {
	-webkit-box-orient: block-axis;
	box-orient: block-axis;
}

.ub-bl {
	/* Firefox */
	-moz-box-lines: multiple;
	/* Safari and Chrome */
	-webkit-box-lines: multiple;
	/* W3C */
	box-lines: multiple;
}

.lub-hori {
	-webkit-box-orient: horizontal;
	box-orient: horizontal;
}

.ub-f1 {
	-webkit-box-flex: 1;
	box-flex: 1;
}

.ub-f2 {
	-webkit-box-flex: 2;
	box-flex: 2;
}

.ub-f3 {
	-webkit-box-flex: 3;
	box-flex: 3;
}

.ub-f4 {
	-webkit-box-flex: 4;
	box-flex: 4;
}

.ub-f6 {
	-webkit-box-flex: 6;
	box-flex: 6;
}

.ub {
	display: -webkit-box !important;
	display: box !important;
	position: relative;
}

.bg-c-gray {
	background-color: #F8F8F8;
}

a {
	text-decoration: none;
}

.pd-all-10 {
	padding: 1rem;
}

.chart-legend li span {
    display: inline-block;
    width: 12px;
    height: 12px;
    margin-right: 5px;
}
</style>
</head>

<div id="canvas-holder" style="width:100%">
        <canvas id="chart-area" width="100%" height="130%" />
</div>

<script type="text/javascript">
	var jsonp=
	{
		el:"",
		func:{},
		connect:function(url,parms,succ,erro)
		{
			try
			{
				var sc=document.createElement('script');
				sc.src=url+'?callback=getData&'+parms;
				document.body.appendChild(sc);
				el=sc;
				succ ? this.func['succ']=succ : null;
				erro ? this.func['err']=erro : null;
			}
			catch(e)
			{
				this.error();
			}
		},
		getData:function(reponse)
		{
			el.remove();
			this.success(reponse);
		},
		success:function(reponse)
		{
			this.func['succ']&&this.func['succ'](reponse);
			this.func['succ']=undefined;
		},
		error:function(reponse)
		{
			this.func['err']&&this.func['err'](reponse);
			this.func['err']=undefined;
		}
	}
	
	function getData(reponse)
	{
		jsonp.getData(reponse);
	}
	
	getChart();
	
	function chart(colors,types,datas)
	{
		var config = 
		{
	        type: 'pie',
	        data: 
	        {
	        	datasets: 
	        	[{
	                data: 
	                [
						datas[0],
						datas[1],
						datas[2],
						datas[3],
						datas[4],
						datas[5]
	                ],
	                backgroundColor: 
	                [
						colors[0],
						colors[1],
						colors[2],
						colors[3],
						colors[4],
						colors[5]
	                ],
	            }],
	            labels: 
	            [
					types[0],
					types[1],
					types[2],
					types[3],
					types[4],
					types[5]
	            ]
	        },
	        options: 
	        {
	            responsive: true,
	            legend: {
	                position: 'top',
	                labels: {
	        			boxWidth: 40,
	        			padding: 20,
	        			fontSize: 15
	                }
	            },
	            title: {
	                display: true,
	                text: '项目期限分布',
	                fontSize: 20
	            }
	        }
		}
		
		var ctx = document.getElementById("chart-area").getContext("2d");
		var myChart = new Chart(ctx, config); 
	}
	
	function getChart()
	{
		jsonp.connect('<%=url%>' + '/app/platinfo/operationData.htm',
			new Date().getTime(), function(response) 
			{
				if (response && response.code == "000000" && response.data) 
				{
					var colors = ['#058DC7', '#50B432', '#ED561B','#DDDF00', '#24CBE5', '#64E572', '#FF9655', '#FFF263', '#6AF9C4'];
					chart(colors, response.data.timeLimtsType, response.data.timeLimtsData);
				} 
				else 
				{
					jsonp.error();
				}
			}
		, null);
	}
	
	function GetRequest() 
	{
		var url = location.search; //获取url中"?"符后的字串
		var theRequest = new Object();
		if (url.indexOf("?") != -1) 
		{
			var str = url.substr(1);
			strs = str.split("&");
			for (var i = 0; i < strs.length; i++) 
			{
				theRequest[strs[i].split("=")[0]] = (strs[i].split("=")[1]);
			}
		}
		return theRequest;
	}
</script>

</html>