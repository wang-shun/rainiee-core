<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>页面不存在</title>
<%@include file="/WEB-INF/include/meta.jsp"%>
<link rel="stylesheet" type="text/css" href="<%=controller.getStaticPath(request)%>/css/common.css"/>
	<link rel="stylesheet" type="text/css" href="<%=controller.getStaticPath(request)%>/css/base.css"/>
</head>
<body>
<div class="w1002">
	<div class="tc pt100 mt50">
		<img src="<%=controller.getStaticPath(request)%>/images/404.jpg">
	</div>
	 <div class="f26 border_b pb20 tc mt40">
	 	没有找到你要的页面，<a href="<%configureProvider.format(out,URLVariable.INDEX);%>" class="red" >点击这里</a>跳转到首页
	 </div>
	 <div class="mt20 tc f16">
	 	<a href="<%configureProvider.format(out,URLVariable.INDEX);%>" class="blue">返回首页</a> 页面将在<span class="red">3</span>秒后自动跳转到首页
	 </div>
</div>
<script type="text/javascript">
setTimeout(function() {location.href="<%configureProvider.format(out,URLVariable.INDEX);%>";},3000);
</script>
</body>
</html>
