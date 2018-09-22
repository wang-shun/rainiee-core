<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.front.servlets.credit.sl.Sjsl" %>
<%@page import="com.dimeng.p2p.front.servlets.credit.sl.Sfzsl" %>
<%@page import="com.dimeng.p2p.front.servlets.credit.sl.Xybgsl" %>
<%@page import="com.dimeng.p2p.account.user.service.RzxxManage" %>
<%@page import="com.dimeng.p2p.S51.entities.T5123" %>
<%@page import="com.dimeng.p2p.S51.enums.T5123_F03" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=<%=resourceProvider.getCharset() %>" />
    <meta name="keywords" content="个人信用贷款，个人信用贷款申请，小额信用贷款"/>
    <meta name="description" content="<%=configureProvider.getProperty(SystemVariable.SITE_NAME)%>为您提供最新的个人信用贷款产品,在线快速申请,<%=configureProvider.getProperty(SystemVariable.SITE_NAME)%>免费为你审核评估,专业团队为您量身定制信用贷款方案,额度高，利息低，流程简单！"/>
    <meta http-equiv="X-UA-Compatible" content="IE=EmulateIE8"/>
    <meta http-equiv="Pragma" CONTENT="no-cache"/>
    <meta http-equiv="Cache-Control" CONTENT="no-cache"/>
    <meta http-equiv="Expires" CONTENT="0"/>
    <title>个人信用贷款_小额信用贷款_个人信用借款申请-<%=configureProvider.getProperty(SystemVariable.SITE_NAME)%></title>
    <%@include file="/WEB-INF/include/style.jsp" %>
</head>
<body class="dbd-body">
<%@include file="/WEB-INF/include/header.jsp" %>
<!--banner-->
<div class="dbd-banner">
<div class="dbd-banner-center">
<div class="pic"><img src="<%=controller.getStaticPath(request)%>/images/banner_pic.png"></div>
<div class="con-container">
	<h3><span class="title"><%=configureProvider.getProperty(SystemVariable.SITE_NAME)%>信用贷</span></h3>
	<%configureProvider.format(out,HTMLVariable.LOAN_PRODUCTS_XJD); %>
    <div class="tr">
    	<%
             if (dimengSession == null || !dimengSession.isAuthenticated()) {
         %>
         <a href="<%=controller.getViewURI(request,com.dimeng.p2p.front.servlets.credit.xjd.Xjd.class)%>"
            class="btn">立即申请</a>
         <%
         } else {
             UserInfoManage userInfoManage = serviceSession.getService(UserInfoManage.class);
             T6110 t6110_menu = userInfoManage.getUserInfo(serviceSession.getSession().getAccountId());
             T6110_F06 t6110_f06 = t6110_menu.F06;
             if (t6110_f06 == T6110_F06.ZRR && T6110_F07.HMD != t6110_menu.F07) {
         %>
         <a href="<%=controller.getViewURI(request,com.dimeng.p2p.front.servlets.credit.xjd.Xjd.class)%>"
            class="btn">立即申请</a>
         <%
                 }
             }
         %>
    </div>
</div>
</div>
</div>
<!--banner end-->
<!--内容-->
<div class="dbd-container dbd-container-bg">
<div class="dbd-content gray6">

<!--申请条件-->
	<div class="item-box mt75">
    <div class="title-pic"><img src="<%=controller.getStaticPath(request)%>/images/dbd-icon-1.png"></div>
     <div class="title">申请条件</div>
     <div>
     <%configureProvider.format(out,HTMLVariable.LOAN_APPLY_CONDITION_XJD); %>
     </div>
    </div>
   <!--申请条件 end-->
   
   <!--借款方式-->
	<div class="item-box mt70 clearfix">
    <div class="title-pic"><img src="<%=controller.getStaticPath(request)%>/images/dbd-icon-2.png"></div>
     <div class="title">借款方式</div>
     <div class="dbd-left-box">
     <ul class="f16">
         <li class="mb20"><h3 class="bold">借款额度</h3>
        <p><%=Formater.formatAmount(DoubleParser.parse(configureProvider.getProperty(SystemVariable.CREDIT_LOAN_AMOUNT_MIN))) %>-
                        <%=Formater.formatAmount(DoubleParser.parse(configureProvider.getProperty(SystemVariable.CREDIT_LOAN_AMOUNT_MAX))) %>元</p>
        </li>
        <li class="mb20"><h3 class="bold">借款年化利率</h3>
        <p><%=Formater.formatRate(DoubleParser.parse(configureProvider.getProperty(SystemVariable.CREDIT_LOAN_RATE_MIN)))%>
           -
           <%=Formater.formatRate(DoubleParser.parse(configureProvider.getProperty(SystemVariable.CREDIT_LOAN_RATE_MAX)))%>
        </p>
        </li>
        
        <li class="mb20"><h3 class="bold">借款期限</h3>
        <p><%configureProvider.format(out,SystemVariable.CREDIT_LOAN_QX_MIN); %>-<%configureProvider.format(out,SystemVariable.CREDIT_LOAN_QX_MAX); %>个月</p>
        </li>
        
        <li class="mb20"><h3 class="bold">审核时间</h3>
        <p><%configureProvider.format(out,SystemVariable.CREDIT_LOAN_SHSJ_MIN); %>-<%configureProvider.format(out,SystemVariable.CREDIT_LOAN_SHSJ_MAX); %>个工作日</p>
        </li>
     </ul>
     </div>
     <div class="dbd-right-box f16">
     <h3 class="bold">还款方式</h3>
   <ul > 
   <%if(Boolean.parseBoolean(configureProvider.getProperty(SystemVariable.CREDIT_IS_SHOW_DEBX))){ %>
    <li>◆ 等额本息；</li> 
    <%} %>
    <%if(Boolean.parseBoolean(configureProvider.getProperty(SystemVariable.CREDIT_IS_SHOW_MYFX))){ %>
    <li>◆ 每月付息,到期还本；</li>
    <%} %>
    <%if(Boolean.parseBoolean(configureProvider.getProperty(SystemVariable.CREDIT_IS_SHOW_YCFQ))){ %>
    <li>◆ 本息到期一次付清；</li>
    <%} %>
    <%if(Boolean.parseBoolean(configureProvider.getProperty(SystemVariable.CREDIT_IS_SHOW_DEBJ))){ %>
    <li>◆ 等额本金；</li>
    <%} %> 
    </ul>
     </div>
     
    </div>
   <!--借款方式 end--> 
    
</div>
</div>
<div class="dbd-container">
<div class="dbd-content gray6">
<!--申请材料-->
	<div class="item-box">
    <div class="title-pic"><img src="<%=controller.getStaticPath(request)%>/images/dbd-icon-3.png"></div>
     <div class="title">申请材料</div>
     <div class="dbd-table">
     <table class="table">
     <thead><tr><th>必要材料</th><th>非必要材料</th></tr></thead>
     <tbody>
     <tr><td>
     <ul>
     <%
          RzxxManage rzxxManage = serviceSession.getService(RzxxManage.class);
              T5123[] mustRZInfoArray = rzxxManage.getT5123(T5123_F03.S);
              if(mustRZInfoArray!=null && mustRZInfoArray.length>0)
              {
                  for(T5123 t5123:mustRZInfoArray)
                  {
      %>
      	<li>◆<%=t5123.F02 %></li>
      <%
      	} 
      }
      else
      {
      %>
     	<li>◆ 暂无数据</li>
      <%} %>
     </ul>
     </td><td>
     <ul>
     <%
              T5123[] rzInfoArray = rzxxManage.getT5123(T5123_F03.F);
              if(rzInfoArray!=null && rzInfoArray.length>0)
              {
                  for(T5123 t5123:rzInfoArray)
                  {
      %>
      	<li>◆<%=t5123.F02 %></li>
      <%
      	} 
      }
      else
      {
      %>
     	<li>◆ 暂无数据</li>
      <%} %>
     </ul>
     </td></tr>
     </tbody>
     </table>
     </div>
    </div>
   <!--申请材料 end-->
</div>
</div>
<!--内容 end-->



    <%String errorMess = controller.getPrompt(request, response, PromptLevel.ERROR);
        if(!StringHelper.isEmpty(errorMess)){
    %>
    <div id="error_div">
        <div class="popup_bg"></div>
        <div class="dialog">
            <div class="title"><a href="javascript:void(-1);" onclick="hidebg('error_div');" class="out"></a>提示</div>
            <div class="content">
                <div class="tip_information">
                    <div class="doubt"></div>
                    <div class="tips">
                        <span class="f20 gray3"><%=errorMess%></span>
                    </div>
                </div>
                <div class="tc mt20">
                    <%if(errorMess.indexOf("存在逾期借款") > -1){ %>
		            	<a href="/user/credit/repaying.html" class="btn01">确定</a>
		            <%}else{ %>
		                <a href="javascript:toValidate('error_div');" class="btn01">确定</a>
		            <%} %>
                </div>
            </div>
        </div>
    </div>
    <%} %>
<%@include file="/WEB-INF/include/footer.jsp" %>
<script type="text/javascript">
    function hidebg(id) {
        $("#" + id).hide();
    }
    function toValidate(id) {
    	var $obj = $("#to_validate");
    	if($obj.length > 0 && $obj.attr("href")){
    		window.location.href = $obj.attr("href");
    	}else{
    		hidebg(id);
    	}
        
    }
</script>
</body>
</html>
