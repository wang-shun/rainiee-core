<!DOCTYPE html>
<!--
To change this license header, choose License Headers in Project Properties.
To change this template file, choose Tools | Templates
and open the template in the editor.
-->
<%@page import="com.dimeng.framework.resource.ResourceProvider"%>
<%@page import="com.dimeng.framework.config.ConfigureProvider"%>
<%@page import="com.dimeng.framework.resource.ResourceRegister"%>
<%@page import="com.dimeng.p2p.variables.defines.SystemVariable"%>
<%@page import="org.bouncycastle.util.encoders.Base64"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<html>
    <head>
		<meta charset="UTF-8">
		<meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no, minimum-scale=1.0, maximum-scale=1.0">
		<meta name="manifest" content="version.json|manifest.json" />
		<meta content="email=no" name="format-detection" />
		<meta content="telephone=no" name="format-detection" />
		
<style type="text/css">
html{
	font-size: 10px;
}
body{
	margin: 0;
	padding: 0;
	color: #8c8c8c;
	width: 100%;
	height: 100%;
	font-family: "Hiragino","Microsoft YaHei";
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
	/*html{font-size: 13px;}*/html{font-size: 12px;}
}
@media screen  and  (min-width:481px) and (max-width:640){
	body{font-size:medium;}
	/*html{font-size: 16px;}*/html{font-size: 14px;}
}
@media screen  and  (min-width:640px) {
	body{font-size:large;}
	/*html{font-size: 19px;}*/html{font-size: 16px;}
}
.hei-ih{ height:inherit}
.ub{
	display: -webkit-box;
	display: -moz-box;
}
.width100{width:100%}
.width45{width:45%}
.ub-as{-webkit-box-align: start;-moz-box-align: start;}
.ub-ac{-webkit-box-align: center;-moz-box-align: center;}
.ub-ae{-webkit-box-align: end;-moz-box-align: end;}
.ub-ps{-webkit-box-pack: start;-moz-box-pack: start;}
.ub-pc{-webkit-box-pack: center;-webkit-box-pack: center;}
.ub-pe{-webkit-box-pack: end;-webkit-box-pack: end;}
.ub-pj{-webkit-box-pack: justify;-webkit-box-pack: justify;}
.ub-f1{-webkit-box-flex: 1;-moz-box-flex: 1;}
a{text-decoration: none;}
.mg-t-2{
	margin-top: 2rem;
}
.mg-t-6{
	margin-top: 6rem;
}
.mg-t-8{
	margin-top: 8rem;
}

.pd-all-5{padding: .5rem;}
.pd-bt-5{padding: .5rem 0;}
.pd-bt-8{padding: .8rem 0;}
.fn-s-13{font-size: 1.3rem;}
.fn-s-18{font-size: 1.8rem;}
.fn-s-20{font-size: 20rem;}
.fn-c-white{color: white;}
.fn-c-gray{color: #666666;}
.fn-c-red{color: red;}
.tx-a-c{text-align: center;}
.bg-c-org{
	background: #FF7F38;;
}
.bg-c-blue{
	background: #34B8FC;;
}
.img-suc{
	height: 7rem;
	width: 100%;
	background-image:url('../../../image/success.png');
	-webkit-background-size: 7rem 7rem;
    background-size: 7rem 7rem;
    background-repeat: no-repeat;
    background-position:center ;
}
 .img-err{
 	height: 7rem;
	width: 100%;
	background-image:url('../../../image/error.png');
	-webkit-background-size: 7rem 7rem;
    background-size: 7rem 7rem;
    background-repeat: no-repeat;
    background-position:center ;
 }
.radius{
	-webkit-border-radius: 3px;
	border-radius: 3px;
	-o-border-radius: 3px;
	-moz-border-radius: 3px;
}
</style>
	</head>
<body>
	<div class="back_color_f">
		<%if("000000".equals(request.getParameter("code"))){%>
			<%if(request.getAttribute("type").equals("提现")){ 
				if("success".equals(request.getAttribute("description"))){%>
				    <div class="ub mg-t-6 img-suc"></div>
					<div class="mg-t-2 pd-all-5 fn-s-18 tx-a-c fn-c-gray">恭喜您<%=request.getAttribute("type") %>成功！</div>
				<%}else{%>
			
			<div class="ub mg-t-6 img-suc"></div>
			<div class="mg-t-2 pd-all-5 fn-s-18 tx-a-c fn-c-gray">提现申请成功，请耐心等待！</div>
			<%}}else{ %>
			
			<div class="ub mg-t-6 img-suc"></div>
			<div class="mg-t-2 pd-all-5 fn-s-18 tx-a-c fn-c-gray">恭喜您<%=request.getAttribute("type") %>成功！</div>
					
		<%}}else{
		    String description=request.getParameter("description");
			if(StringUtils.isNotEmpty(description)){
			    description=new String(Base64.decode(description), "utf-8");
			}
		%>
			<div class="ub mg-t-6 img-err"></div>
			<div class="mg-t-2 pd-all-5 fn-s-18 tx-a-c fn-c-gray"><%=request.getAttribute("type") %>失败！</div>
			<div class="mg-t-2 pd-all-5 fn-s-13 tx-a-c fn-c-red">错误信息：<%=description %></div>
			<%-- 	<div class="fail">
					<span class="ub sub" style="color:red;"><%=request.getAttribute("type") %>失败!</span>
					<span class="ub">错误信息：<%=description %></span>
				</div> --%>
		<%} %>
		<div class="ub  ub-ac ub-pc fn-s-18 mg-t-6">
				<div class="radius  bg-c-org width45 " style="margin-right: 0.5rem;"><a href="<%=request.getContextPath() %>/weixin/index.html#user/personal/userInfo" class="ub width100 ub-pc pd-bt-8 fn-c-white">完成</a></div>
				<div class="radius  bg-c-blue width45 " style="margin-left: 0.5rem;"><a href="<%=request.getContextPath() %>/weixin/index.html#index/index/index" class="ub width100 ub-pc pd-bt-8 fn-c-white">首页</a></div>
		</div>
	</div>
</body>
</html>