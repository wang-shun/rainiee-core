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

	if (url.indexOf(protocolType) == -1) {
		url = protocolType.concat(url);
	}

%>
<html>
<head>
<meta name="viewport"
	content="width=device-width, initial-scale=1, user-scalable=no, minimum-scale=1.0, maximum-scale=1.0">
<meta content="email=no" name="format-detection" />
<meta content="telephone=no" name="format-detection" />
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
	font-family: "微软雅黑";
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

.bg-c-withe {
	background: #fff;
}

.fn-c {
	color: #333333;
}

.fn-d {
	color: #999999;
}

.bg-c-gray {
	background-color: #F8F8F8;
}

.pd-all-10 {
	padding: 1rem;
}

.line-h-20 {
	line-height: 2rem;
}

.tx-al-l {
	text-align: left;
}

.wordbreak-j {
	word-wrap: break-word;
	word-break: break-all;
	text-align: justify;
}

.mg-t-10 {
	margin-top: 1rem;
}

.mg-l-10 {
	margin-left: 1rem;
}

.bc-withe {
	background-color: white;
}
</style>
</head>
<body class="bg-c-gray">
	<div class=" pd-all-10 bc-withe">
		<div class="ub">
			<div
				style="width: 1rem; height: 1.7rem; border-left: 0.3rem solid #cc821a;"
				id="a"></div>
			<div class="line-h-20 fn-s-14 fn-c">简介:</div>
		</div>
		<div class="fn-s-12 wordbreak-j tx-al-l mg-t-10 fn-d" id="jj"></div>
	</div>
	<div class="mg-t-5 pd-all-10 bc-withe"
		style="border-top: 1px solid #E1DED9;">
		<div class="ub">
			<div
				style="width: 1rem; height: 1.7rem; border-left: 0.3rem solid #cc821a;"
				id="b"></div>
			<div class="line-h-20 fn-s-14 fn-c">倡议书:</div>
		</div>
		<div class="fn-s-12 wordbreak-j tx-al-l mg-t-10 fn-d" id="cys"></div>
	</div>
	<div class="mg-t-5 pd-all-10 bc-withe"
		style="border-top: 1px solid #E1DED9;" id="jz">
		<div class="ub">
			<div
				style="width: 1rem; height: 1.7rem; border-left: 0.3rem solid #cc821a;"
				id="c"></div>
			<div class="line-h-20 fn-s-14 fn-c">进展</div>
		</div>
		<div>
			<div
				style="border-left: 0.3rem solid #34B8FC; padding: 1rem; margin-left: 1rem;"
				id="d"></div>
		</div>
	</div>
</body>
<script type="text/javascript">
		var D=document;
		var query =GetRequest();
		var a = D.getElementById('a');
		var b = D.getElementById('b');
		var c = D.getElementById('c');
		var d = D.getElementById('d');
		var jj=D.getElementById('jj');
		var cys=D.getElementById('cys');
		var jz=D.getElementById('jz');
		
		var color = query['color'];
		a.style.borderLeft = "0.3rem solid #" + color;
		b.style.borderLeft = "0.3rem solid #" + color;
		c.style.borderLeft = "0.3rem solid #" + color;
		d.style.borderLeft = "0.3rem solid #" + color;
		
		var url = '<%=url%>';
	    var obj = {}
	    var sc = D.createElement('script');
	    sc.src = url + '/app/bid/publics/gyLoanItem.htm?callback=callback&bidId=' + query['bidId'];
	    document.body.appendChild(sc);
	 function callback(response) {
		console.log(response);
		if (!!response && response.data && response.code == "000000") {
			var data = response.data;
			// 项目信息
			jj.innerHTML = data.introduction;
			cys.innerHTML = data.advocacyContent;
			
			var str ="";
			if (data.bidProgres != null && data.bidProgres.length > 0) 
		    {
				jz.style.display="block";
				
				var length = data.bidProgres.length;
				for (var i =0; i<length; i++)
			    {
					if(data.bidProgres[i].moreUrl)
					{
						str  = str + "<div class='mg-t-10' >"
							 + "<div class='ub'>"
							 + "<div style='height: 1rem; width: 1rem; border-radius: 50%;border: 0.1rem solid #" + color + ";border-radius: 50%; background-color: #"+ color + ";margin-left: -1.7rem;' ></div>"
							 + "<div class='mg-l-10 fn-s-13 fn-c'>" + data.bidProgres[i].titleTime + "</div>"
							 + "</div>"
							 + "<div class='mg-t-10 fn-s-12'>" + data.bidProgres[i].introduction + "</div>"
							 + "<a href = " + data.bidProgres[i].moreUrl + ">查看更多</a></div></div>";
					}
					else
					{
						str  = str + "<div class='mg-t-10' >"
						 + "<div class='ub'>"
						 + "<div style='height: 1rem; width: 1rem; border-radius: 50%;border: 0.1rem solid #" + color + ";border-radius: 50%; background-color: #"+ color + ";margin-left: -1.7rem;' ></div>"
						 + "<div class='mg-l-10 fn-s-13 fn-c'>" + data.bidProgres[i].titleTime + "</div>"
						 + "</div>"
						 + "<div class='mg-t-10 fn-s-12'>" + data.bidProgres[i].introduction + "</div></div>";
					}
				}
				
				d.innerHTML=str;
			} else {
				jz.style.display="none";
			}
		} else {
			console.log(response.description);
		}
	}
	function GetRequest() {
		var url = location.search; //获取url中"?"符后的字串
		var theRequest = new Object();
		if (url.indexOf("?") != -1) {
			var str = url.substr(1);
			strs = str.split("&");
			for (var i = 0; i < strs.length; i++) {
				theRequest[strs[i].split("=")[0]] = (strs[i].split("=")[1]);
			}
		}
		return theRequest;
	}
</script>

</html>
