<%@page import="com.dimeng.framework.resource.ResourceProvider"%>
<%@page import="com.dimeng.framework.config.ConfigureProvider"%>
<%@page import="com.dimeng.framework.resource.ResourceRegister"%>
<%@page import="com.dimeng.p2p.variables.defines.SystemVariable"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
final ResourceProvider resourceProvider = ResourceRegister.getResourceProvider(application);
final ConfigureProvider configureProvider = resourceProvider.getResource(ConfigureProvider.class);
final String protocolType = configureProvider.format(SystemVariable.SITE_REQUEST_PROTOCOL);
String url = configureProvider.format(SystemVariable.SITE_DOMAIN);

if (url.indexOf(protocolType) == -1)
{
	url = protocolType.concat(url);
}

%>
<html>
	<head>
		<meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no, minimum-scale=1.0, maximum-scale=1.0">
		<meta content="email=no"  name="format-detection" />
        <meta content="telephone=no" name="format-detection" />
	<style type="text/css">
	@charset "utf-8";
*{margin:0;padding:0;}
body{
	height: 100%;
	padding:0px;
	margin:0px;
	font-size: 100%;
	color:#666;
	width:100%;
	height: 100%;
	font-family: "微软雅黑";
}	
@media screen  and (min-width:0px)   and (max-width:240px){
	body{font-size:xx-small;}
	html{font-size: 9px;}
}
@media screen  and (min-width:241px) and (max-width:384px){
	body{font-size:x-small;}
	html{font-size: 11px;}
}
@media screen  and (min-width:385px) and (max-width:480px){
	body{font-size:small;}
	html{font-size: 13px;}
}
@media screen  and  (min-width:481px) and (max-width:640){
	body{font-size:medium;}
	html{font-size: 16px;}
}
@media screen  and  (min-width:640px) {
	body{font-size:large;}
	html{font-size: 19px;}
}
/*字体大小*/
.fn-s-11{ font-size: 1.1rem;}
.fn-s-12{ font-size: 1.2rem;}
.fn-s-13{ font-size: 1.3rem;}
.fn-s-15{ font-size: 1.5rem;}
.fn-s-14{ font-size: 1.4rem;}
.fn-s-17{ font-size: 1.7rem;}
.fn-s-19{ font-size: 1.9rem;}
.fn-s-20{ font-size: 2rem;}
.fn-s-25{ font-size:2.5rem;}

.fn-w-b{font-weight:bold ;}	
.ub-ac
{
	-webkit-box-align:center;
	box-align:center;
}
.ub-ae
{
	-webkit-box-align:end;
	box-align:end;
}

.ub-pc
{
	-webkit-box-pack:center;
	box-pack:center;
}
.ub-pe
{
	-webkit-box-pack:end;
	box-pack:end;
}
.ub-pj
{
	-webkit-box-pack:justify;
	box-pack:justify;
}

.ub-ver
{
	-webkit-box-orient:vertical;
	box-orient:vertical;
}
.ub-ba{
-webkit-box-orient:	block-axis;
box-orient:block-axis;
}
.ub-bl{
	/* Firefox */

-moz-box-lines:multiple;

/* Safari and Chrome */

-webkit-box-lines:multiple;

/* W3C */

box-lines:multiple;
}
.lub-hori
{
	-webkit-box-orient:horizontal;
	box-orient:horizontal;
}

.ub-f1
{
	
	-webkit-box-flex: 1; 
	box-flex: 1;
}

.ub-f2
{
	
	-webkit-box-flex: 2;
	box-flex: 2; 
}

.ub-f3
{
	
	-webkit-box-flex: 3;
	box-flex: 3;
}

.ub-f4
{
	
	-webkit-box-flex: 4;
	box-flex: 4;
}

.ub-f6
{
	
	-webkit-box-flex: 6;
	box-flex: 6; 
}
.ub
{
	display: -webkit-box !important;
	display: box !important;
	position:relative;
}
.bg-c-gray{
	background-color: #F8F8F8;
}
.bg-c-withe{
	background: #fff;
}
.fn-c-black{
	#666;
}
.xm{
	padding:1rem 1rem 1rem 1.5rem; font-size: 1.5rem;background-color: #e9e9e9;
	margin-top:1rem;
	color:#666666;
}

.ld{
	padding:0.5rem 0.5rem 0.5rem 1.5rem; font-size: 1.3rem;
	word-break: break-word;
	text-align:justify;
	color: #999999;
}
.rd{
	padding:0.5rem 1rem 0.5rem 0.5rem; font-size: 1.2rem;
	word-break: break-word;
	text-align:justify;
	color: #999999;
}
.dy{
	padding:0.5rem 1rem 0.5rem 1.5rem; font-size: 1.2rem;
	word-break: break-word;
	text-align:justify;
	min-height: 5rem;
}
.bd-b{
	border-bottom: 1px solid #e4e0dd;
}
.pd-b-2{padding-bottom: 2rem;}
.udis{
	display: none;
}
	</style>
	</head>
	<body class="bg-c-gray" >
		<div class="bg-c-withe bd-b pd-b-2 udis" id="qyxx">
			<div class="xm">企业信息</div>
			<div class="ub ">
				<div class="ld">注册年限:</div>
				<div class="rd ub-f1" id="regYear"></div>
			</div>
			<div class="ub ">
				<div class="ld">注册资金:</div>
				<div class="rd ub-f1" id="regAmount"></div>
			</div>
			<div class="ub">
				<div class="ld">资产净值:</div>
				<div class="rd ub-f1" id="earnAmount"></div>
			</div>
			<div class="ub">
				<div class="ld">上年度经营现金流入:</div>
				<div class="rd ub-f1" id="cash"></div>
			</div>
			<div class="ub">
				<div class="ld">行业:</div>
				<div class="rd ub-f1" id="business"></div>
			</div>
			<div class="ub">
				<div class="ld">经营情况:</div>
				<div class="rd ub-f1" id="operation"></div>
			</div>
			<div class="ub">
				<div class="ld">涉诉情况:</div>
				<div class="rd ub-f1" id="complaints"></div>
			</div>
			<div class="ub">
				<div class="ld">征信记录:</div>
				<div class="rd ub-f1" id="credit"></div>
			</div>
		</div>
		<div class="bg-c-withe bd-b pd-b-2">
			<div class="xm">项目信息</div>
			<div class="ub ">
				<div class="ld">融资方:</div>
				<div class="rd ub-f1" id="qyName"></div>
			</div>
			<div class="ub ">
				<div class="ld">信用等级:</div>
				<div class="rd ub-f1" id="xyLevel"></div>
			</div>
			<div class="ub">
				<div class="ld">本期融资金额:</div>
				<div class="rd ub-f1" id="amount"></div>
			</div>
			<div class="ub">
				<div class="ld">项目区域:</div>
				<div class="rd ub-f1" id="area"></div>
			</div>
			<div class="ub">
				<div class="ld" style= "display:none" id="repayId">还款日期:</div>
				<div class="rd ub-f1" style= "display:none" id="repayDate"></div>
			</div>
			<div class="ub">
				<div class="ld">年化利率:</div>
				<div class="rd ub-f1" id="rate"></div>
			</div>
			<div class="ub">
				<div class="ld">投资截止日期:</div>
				<div class="rd ub-f1" id="endDate"></div>
			</div>

			<!-- <div class="ub">
				<div class="ld">借款用途:</div>
				<div class="rd ub-f1" id="bidUse"></div>
			</div>
			<div class="ub">
				<div class="ld">还款来源:</div>
				<div class="rd ub-f1" id="repaySource"></div>
			</div> -->
			<div class="ub">
				<div class="ld">借款描述:</div>
				<div class="rd ub-f1" id="desc"></div>
			</div>
		</div>
		<div class="bg-c-withe bd-b pd-b-2 udis" id="dbxx">
			<div class="xm">担保信息</div>
			<div class="ub ">
				<div class="ld">担保机构:</div>
				<div class="rd ub-f1" id="dbjg"></div>
			</div>
			<div class="ub">
				<div class="ld" >担保机构介绍:</div>
				<div class="rd ub-f1" id="dbdesc"></div>
			</div>
			<div class="ub">
				<div class="ld">担保情况:</div>
				<div class="rd ub-f1" id="dbinfo"></div>
			</div>
			<!-- <div class="ub">
				<div class="ld">风险控制措施:</div>
				<div class="rd ub-f1" id="fkcs"></div>
			</div>
			<div class="ub">
				<div class="ld">反担保情况:</div>
				<div class="rd ub-f1" id="fdbinfo"></div>
			</div> -->
			
			
		</div>
		<div class="bg-c-withe bd-b pd-b-2 udis" id="dyxx">
			<div class="xm">抵押信息</div>
			<div class="">
				<div class="dy" id="dys"></div>
			</div>
		</div>
	</body>
	<script type="text/javascript">
		var D=document;
		var query =GetRequest();
	//console.log(query);
		var dyxx=D.getElementById('dyxx');
		var dbxx=D.getElementById('dbxx');
		var repayId=D.getElementById('repayId');
		var repayd=D.getElementById('repayDate');
		var qyxx=D.getElementById('qyxx');
	var obj=
	{
		'qyName':D.getElementById('qyName'),
		'xyLevel':D.getElementById('xyLevel'),
		'area':D.getElementById('area'),
		'repayDate':D.getElementById('repayDate'),
		'rate':D.getElementById('rate'),
		'amount':D.getElementById('amount'),
		'endDate':D.getElementById('endDate'),
		/* 'bidUse':D.getElementById('bidUse'),
		'repaySource':D.getElementById('repaySource'), */
		'desc':D.getElementById('desc'),
		'dbjg':D.getElementById('dbjg'),
		'dbdesc':D.getElementById('dbdesc'),
		'dbinfo':D.getElementById('dbinfo'),
		'fkcs':D.getElementById('fkcs'),
		'fdbinfo':D.getElementById('fdbinfo'),
		'dys':D.getElementById('dys'),
		'regYear':D.getElementById('regYear'),
		'regAmount':D.getElementById('regAmount'),
		'earnAmount':D.getElementById('earnAmount'),
		'cash':D.getElementById('cash'),
		'business':D.getElementById('business'),
		'operation':D.getElementById('operation'),
		'complaints':D.getElementById('complaints'),
		'credit':D.getElementById('credit')
	}

			var sc=D.createElement('script');
			sc.src= '<%=url%>' + '/app/bid/publics/bidItem.htm?callback=callback&bidId='+query['bidId'];
			document.body.appendChild(sc);
			function callback(response){
				console.log(response);
				if(!!response&&response.data&&response.code=="000000"){
					var data=response.data;
					// 企业信息
					if(data.isQy == 'S')
					{
						qyxx.style.display="block";
						obj['regYear'].innerHTML=data.regYear +" 年";
						obj['regAmount'].innerHTML=data.regAmount +" 万元";
						obj['earnAmount'].innerHTML=data.earnAmount +" 万元";
						obj['cash'].innerHTML=data.cash +" 万元";
						obj['business'].innerHTML=data.business;
						obj['operation'].innerHTML=data.operation == null ? "" : innerHTML=data.operation;
						obj['complaints'].innerHTML=data.complaints == null ? "" : innerHTML=data.complaints;
						obj['credit'].innerHTML=data.credit == null ? "" : innerHTML=data.credit;
					}
					
					// 项目信息
					obj['qyName'].innerHTML=data.qyName;
					obj['xyLevel'].innerHTML=data.xyLevel;
					
					obj['amount'].innerHTML=data.amount + "元";
					obj['area'].innerHTML=data.area;
					
					if (data.repayDate != null && data.repayDate !="") {
						repayId.style.display="block";
						repayd.style.display="block";
						obj['repayDate'].innerHTML=data.repayDate;
					}
					
					obj['rate'].innerHTML= accMul(data.rate, 100).toFixed(2) + "%";
					obj['endDate'].innerHTML=data.endDate;
					/* obj['bidUse'].innerHTML=data.bidUse;
					obj['repaySource'].innerHTML=data.repaySource; */
					obj['desc'].innerHTML=data.desc;
					// 担保信息
					if(data.isDd == 'S')
					{
					  dbxx.style.display="block";
					  obj['dbjg'].innerHTML=data.dbjg;
					  obj['dbdesc'].innerHTML=data.dbdesc;
					  /* obj['bidUse'].innerHTML=data.bidUse; */
					  obj['dbinfo'].innerHTML=data.dbinfo;
					  //obj['fkcs'].innerHTML=data.fkcs;
					  //obj['fdbinfo'].innerHTML=data.fdbinfo;
					}
					
					// 抵押信息
					if (data.dys != null && data.dys.length > 0)
					{
					  dyxx.style.display="block";
					  var str="";
					  for(var i=0;i<data.dys.length;i++){
						  str+=data.dys[i].dyName;
					  }
					  obj['dys'].innerHTML=str;
					}
				}
				else{
					console.log(response.description);
				}
			}
			
			function accMul(arg1,arg2)
			{
			   var m=0,
			   s1=arg1.toString(),
			   s2=arg2.toString();
			   try{
				   m+=s1.split(".")[1].length;
			   } catch (e){
				   
			   }
			   try{
				   m+=s2.split(".")[1].length;
			   } catch (e){
				   
			   }
			   return Number(s1.replace(".",""))*Number(s2.replace(".",""))/Math.pow(10,m)
			}
			
			function GetRequest() {
			 var url = location.search; //获取url中"?"符后的字串
			 var theRequest = new Object();
			 if (url.indexOf("?") != -1) {
			  var str = url.substr(1);
			  strs = str.split("&");
			  for(var i = 0; i < strs.length; i ++) {
			   theRequest[strs[i].split("=")[0]]=(strs[i].split("=")[1]);
			  }
			 }
			 return theRequest;
			}
	</script>
	
</html>
