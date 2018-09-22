<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.util.ObjectHelper" %>
<%@page import="com.dimeng.p2p.console.servlets.info.yygl.yysjgl.UpdateOperateData" %>
<%@page import="java.util.List" %>
<%@page import="com.dimeng.p2p.S61.entities.T6197" %>
<%@page import="com.dimeng.p2p.S61.entities.T6196" %>

<html>
<link href="<%=controller.getStaticPath(request) %>/css/hhmmss.css"
      rel="stylesheet">
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <%@include file="/WEB-INF/include/script.jsp"%>
</head>
<body>
<%
    CURRENT_CATEGORY = "XCGL";
    CURRENT_SUB_CATEGORY = "YYSJGL";
    T6196 t6196 = ObjectHelper.convert(request.getAttribute("t6196"), T6196.class);
    List<T6197> t6197List = ObjectHelper.convert(request.getAttribute("t6197List"), List.class);
%>

<div class="right-container">
  <div class="viewFramework-body">
    <div class="viewFramework-content"> 
      <form action="<%=controller.getURI(request, UpdateOperateData.class)%>" method="post" class="form1" onsubmit="return checkCondition();">
      <!--账号管理-->
      <div class="p20">
        <div class="border">
          <div class="title-container"><i class="icon-i w30 h30 va-middle title-left-icon"></i>运营数据初始值设置</div>
          <div class="content-container pl40 pt30 pr40 pb30">
            <ul class="gray6 input-list-container clearfix">
              <li><span class="display-ib mr5"><em class="red pr5">*</em>累计投资金额</span>
                <input class="text border pl5 mr20 required" name="F02" value="<%=t6196.F02%>" maxlength="15" type="text" onKeyUp="value=value.replace(/\D/g,'')">
                <div class="red pr30 mh30"></div>
              </li>
              <li><span class="display-ib mr5"><em class="red pr5">*</em>累计成交笔数</span>
                <input class="text border pl5 mr20 required" name="F05" value="<%=t6196.F05%>" maxlength="9" type="text" onKeyUp="value=value.replace(/\D/g,'')">
                <div class="red pr30 mh30"></div>
              </li>
              <li><span class="display-ib mr5"><em class="red pr5">*</em>注册用户数</span>
                <input class="text border pl5 mr20 required" name="F03" value="<%=t6196.F03%>" maxlength="9" type="text" onKeyUp="value=value.replace(/\D/g,'')">
                <div class="red pr30 mh30"></div>
              </li>
              </ul>
              <ul class="gray6 input-list-container clearfix">
              <li><span class="display-ib mr5"><em class="red pr5">*</em>借贷余额</span>
                <input class="text border pl5 mr20 required" name="F31" value="<%=t6196.F31%>" maxlength="15" type="text" onKeyUp="value=value.replace(/\D/g,'')">
                <div class="red pr30 mh30"></div>
              </li>
      		 <li><span class="display-ib mr5"><em class="red pr5">*</em>借贷余额笔数</span>
                <input class="text border pl5 mr20 required" name="F32" value="<%=t6196.F32%>" maxlength="9" type="text" onKeyUp="value=value.replace(/\D/g,'')">
                <div class="red pr30 mh30"></div>
              </li>
              <li><span class="display-ib mr5"><em class="red pr5">*</em>利息余额</span>
                <input class="text border pl5 mr20 required" name="F33" value="<%=t6196.F33%>" maxlength="15" type="text" onKeyUp="value=value.replace(/\D/g,'')">
                <div class="red pr30 mh30"></div>
              </li>
              </ul>
              <ul class="gray6 input-list-container clearfix">
              <li><span class="display-ib mr5"><em class="red pr5">*</em>累计出借人数量</span>
                <input class="text border pl5 mr20 required" name="F34" value="<%=t6196.F34%>" maxlength="9" type="text" onKeyUp="value=value.replace(/\D/g,'')">
                <div class="red pr30 mh30"></div>
              </li>
              <li><span class="display-ib mr5"><em class="red pr5">*</em>当前出借人数量</span>
                <input class="text border pl5 mr20 required" name="F35" value="<%=t6196.F35%>" maxlength="9" type="text" onKeyUp="value=value.replace(/\D/g,'')">
                <div class="red pr30 mh30"></div>
              </li>
              <li><span class="display-ib mr5"><em class="red pr5">*</em>累计赚取收益</span>
                <input class="text border pl5 mr20 required" name="F04" value="<%=t6196.F04%>" maxlength="15" type="text" onKeyUp="value=value.replace(/\D/g,'')">
                 <div class="red pr30 mh30"></div>
              </li>
              </ul>
              <ul class="gray6 input-list-container clearfix">
              <li><span class="display-ib mr5"><em class="red pr5">*</em>累计借款人数量</span>
                <input class="text border pl5 mr20 required" name="F36" value="<%=t6196.F36%>" maxlength="9" type="text" onKeyUp="value=value.replace(/\D/g,'')">
                <div class="red pr30 mh30"></div>
              </li>
              <li><span class="display-ib mr5"><em class="red pr5">*</em>当前借款人数量</span>
                <input class="text border pl5 mr20 required" name="F37" value="<%=t6196.F37%>" maxlength="9" type="text" onKeyUp="value=value.replace(/\D/g,'')">
                <div class="red pr30 mh30"></div>
              </li>
              <li><span class="display-ib mr5"><em class="red pr5">*</em>关联关系借款金额</span>
                <input class="text border pl5 mr20 required" name="F40" value="<%=t6196.F40%>" maxlength="15" type="text" onKeyUp="value=value.replace(/\D/g,'')">
                 <div class="red pr30 mh30"></div>
              </li>
              </ul>
              <ul class="gray6 input-list-container clearfix">
              <li><span class="display-ib mr5"><em class="red pr5">*</em>最大单一借款人待还金额占比</span>
                <input class="text border pl5 required" name="F38" value="<%=t6196.F38%>" maxlength="8" type="text" mtest="/^(-)?\d*(\d|(\.[0-9]{1,2}))$/" mtestmsg="请输入有效的数字"><span class="pl5 mr20">%</span>
                <div class="red pr30 mh30"></div>
              </li>
              <li><span class="display-ib mr5"><em class="red pr5">*</em>前十大借款人待还金额占比</span>
                <input class="text border pl5 required" name="F39" value="<%=t6196.F39%>" maxlength="8" type="text" mtest="/^(-)?\d*(\d|(\.[0-9]{1,2}))$/" mtestmsg="请输入有效的数字"><span class="pl5 mr20">%</span>
                <div class="red pr30 mh30"></div>
              </li>
              <li><span class="display-ib mr5"><em class="red pr5">*</em>关联关系借款笔数</span>
                <input class="text border pl5 mr20 required" name="F41" value="<%=t6196.F41%>" maxlength="9" type="text" onKeyUp="value=value.replace(/\D/g,'')">
                 <div class="red pr30 mh30"></div>
              </li>
            </ul>
             <!--累计投资金额柱状图-->
            <div class="children-title-container mt20">
              <h3 class="h30 lh30 gray6 border-b-s fb"><em class="red pr5">*</em>累计投资金额柱状图</h3>
            </div>
            <div class="mt20 table-container">
          <table class="table table-style gray6 tc">
            <thead>
              <tr class="tc">
                <td>月/年</td>
                <%
                int size = t6197List.size();
                for(int i=0;i<size;i++){ 
                %>
                <td><%=DateParser.format(t6197List.get(size-1-i).F03,"MM/yy") %></td>
                <%} %>
                </tr>
            </thead>
            <tbody class="f12">
              <tr class="title tc">
                <th>金额</th>
                <%
                
                for(int i=0;i<size;i++){ 
                %>
                <th><input type="text" class="text border h28 lh28 w60 pl5 pr5 reqAmount isNaN" value="<%=t6197List.get(size-1-i).F02 %>" maxlength="15" name="amounts" onKeyUp="value=value.replace(/\D/g,'')"/></th>
                <%} %>
              </tr>
            </tbody>
          </table>
        </div>
            <div class="red pr30 mh30 error_info"></div>
            
            <!--用户数分布-->
            <div class="children-title-container mt20">
              <h3 class="h30 lh30 gray6 border-b-s fb">投资/借款用户分布</h3>
            </div>
            
            <div class="pt30">
            <ul class="gray6 input-list-container clearfix">
              <li><span class="display-ib mr5"><em class="red pr5">*</em>投资用户数</span>
                <input class="text border pl5 mr20 required" name="F06" value="<%=t6196.F06%>" maxlength="9" type="text" onKeyUp="value=value.replace(/\D/g,'')">
                <div class="red pr30 mh30"></div>
              </li>
              <li><span class="display-ib mr5"><em class="red pr5">*</em>借款用户数</span>
                <input class="text border pl5 mr20 required" name="F07" value="<%=t6196.F07%>" maxlength="9" type="text" onKeyUp="value=value.replace(/\D/g,'')">
                <div class="red pr30 mh30"></div>
              </li>
            </ul>
            </div>
            
             <!--投资用户年龄分布-->
            <div class="children-title-container mt20">
              <h3 class="h30 lh30 gray6 border-b-s fb">平台用户年龄分布</h3>
            </div>
            
            <div class="pt30">
            <ul class="gray6 input-list-container clearfix">
              <li><span class="display-ib mr5"><em class="red pr5">*</em>90后</span>
                <input class="text border pl5 mr20 required" name="F08" value="<%=t6196.F08%>" maxlength="9" type="text" onKeyUp="value=value.replace(/\D/g,'')">
                <div class="red pr30 mh30"></div>
              </li>
              <li><span class="display-ib mr5"><em class="red pr5">*</em>80后</span>
                <input class="text border pl5 mr20 required" name="F09" value="<%=t6196.F09%>" maxlength="9" type="text" onKeyUp="value=value.replace(/\D/g,'')">
                <div class="red pr30 mh30"></div>
              </li>
              <li><span class="display-ib mr5"><em class="red pr5">*</em>70后</span>
                <input class="text border pl5 mr20 required" name="F10" value="<%=t6196.F10%>" maxlength="9" type="text" onKeyUp="value=value.replace(/\D/g,'')">
                <div class="red pr30 mh30"></div>
              </li>
              <li><span class="display-ib mr5"><em class="red pr5">*</em>60后</span>
                <input class="text border pl5 mr20 required" name="F11" value="<%=t6196.F11%>" maxlength="9" type="text" onKeyUp="value=value.replace(/\D/g,'')">
                <div class="red pr30 mh30"></div>
              </li>
              <li><span class="display-ib mr5"><em class="red pr5">*</em>其他</span>
                <input class="text border pl5 mr20 required" name="F12" value="<%=t6196.F12%>" maxlength="9" type="text" onKeyUp="value=value.replace(/\D/g,'')">
                <div class="red pr30 mh30"></div>
              </li>
            </ul>
            </div>
            
            
             <!--项目期限分布-->
            <div class="children-title-container mt20">
              <h3 class="h30 lh30 gray6 border-b-s fb">项目期限分布</h3>
            </div>
            
            <div class="pt30">
            <ul class="gray6 input-list-container clearfix">
              <li><span class="display-ib mr5"><em class="red pr5">*</em>1-3个月</span>
                <input class="text border pl5 required" name="F13" value="<%=t6196.F13%>" maxlength="15" type="text" onKeyUp="value=value.replace(/\D/g,'')">
                  <span class="pl5 mr20">元</span>
                <div class="red pr30 mh30"></div>
              </li>
              <li><span class="display-ib mr5"><em class="red pr5">*</em>4-6个月</span>
                <input class="text border pl5 required" name="F14" value="<%=t6196.F14%>" maxlength="15" type="text" onKeyUp="value=value.replace(/\D/g,'')">
                  <span class="pl5 mr20">元</span>
                <div class="red pr30 mh30"></div>
              </li>
              <li><span class="display-ib mr5"><em class="red pr5">*</em>7-9个月</span>
                <input class="text border pl5 required" name="F15" value="<%=t6196.F15%>" maxlength="15" type="text" onKeyUp="value=value.replace(/\D/g,'')">
                  <span class="pl5 mr20">元</span>
                <div class="red pr30 mh30"></div>
              </li>
              <li><span class="display-ib mr5"><em class="red pr5">*</em>10-12个月</span>
                <input class="text border pl5 required" name="F16" value="<%=t6196.F16%>" maxlength="15" type="text" onKeyUp="value=value.replace(/\D/g,'')">
                  <span class="pl5 mr20">元</span>
                <div class="red pr30 mh30"></div>
              </li>
              <li><span class="display-ib mr5"><em class="red pr5">*</em>12-24个月</span>
                <input class="text border pl5 required" name="F17" value="<%=t6196.F17%>" maxlength="15" type="text" onKeyUp="value=value.replace(/\D/g,'')">
                  <span class="pl5 mr20">元</span>
                <div class="red pr30 mh30"></div>
              </li>
              <li><span class="display-ib mr5"><em class="red pr5">*</em>24个月以上</span>
                <input class="text border pl5 required" name="F18" value="<%=t6196.F18%>" maxlength="15" type="text" onKeyUp="value=value.replace(/\D/g,'')">
                  <span class="pl5 mr20">元</span>
                <div class="red pr30 mh30"></div>
              </li>
              <li><span class="display-ib mr5"><em class="red pr5">*</em>其他(天标)</span>
                <input class="text border pl5 required" name="F30" value="<%=t6196.F30%>" maxlength="15" type="text" onKeyUp="value=value.replace(/\D/g,'')">
                  <span class="pl5 mr20">元</span>
                <div class="red pr30 mh30"></div>
              </li>
            </ul>
            </div>
            
            <!--项目类型分布-->
            <div class="children-title-container mt20">
              <h3 class="h30 lh30 gray6 border-b-s fb">项目类型分布</h3>
            </div>
            
            <div class="pt30">
            <ul class="gray6 input-list-container clearfix">
              <li><span class="display-ib mr5"><em class="red pr5">*</em>担保标</span>
                <input class="text border pl5 required" name="F19" value="<%=t6196.F19%>" maxlength="15" type="text" onKeyUp="value=value.replace(/\D/g,'')">
                  <span class="pl5 mr20">元</span>
                <div class="red pr30 mh30"></div>
              </li>
              <li><span class="display-ib mr5"><em class="red pr5">*</em>抵押认证标</span>
                <input class="text border pl5 required" name="F20" value="<%=t6196.F20%>" maxlength="15" type="text" onKeyUp="value=value.replace(/\D/g,'')">
                  <span class="pl5 mr20">元</span>
                <div class="red pr30 mh30"></div>
              </li>
              <li><span class="display-ib mr5"><em class="red pr5">*</em>实地认证标</span>
                <input class="text border pl5 required" name="F21" value="<%=t6196.F21%>" maxlength="15" type="text" onKeyUp="value=value.replace(/\D/g,'')">
                  <span class="pl5 mr20">元</span>
                <div class="red pr30 mh30"></div>
              </li>
              <li><span class="display-ib mr5"><em class="red pr5">*</em>信用认证标</span>
                <input class="text border pl5 required" name="F22" value="<%=t6196.F22%>" maxlength="15" type="text" onKeyUp="value=value.replace(/\D/g,'')">
                  <span class="pl5 mr20">元</span>
                <div class="red pr30 mh30"></div>
              </li>
            </ul>
            </div>
            
            <!--其他数据-->
            <div class="children-title-container mt20">
              <h3 class="h30 lh30 gray6 border-b-s fb">平台风险管控</h3>
            </div>
            
            <div class="pt30">
            <%-- <ul class="gray6 input-list-container clearfix">
              <li><span class="display-ib mr5 w160 tr"><em class="red pr5">*</em>累计代偿金额</span>
                <input class="text border pl5 mr20 required" name="F23" value="<%=t6196.F23%>" maxlength="15" type="text" onKeyUp="value=value.replace(/\D/g,'')">
                <div class="red pr30 mh30"></div>
              </li>
              <li><span class="display-ib mr5 w200 tr"><em class="red pr5">*</em>最大单户借款余额占比</span>
                <input class="text border pl5 required" name="F24" value="<%=t6196.F24%>" maxlength="8" type="text" mtest="/^(-)?\d*(\d|(\.[0-9]{1,2}))$/" mtestmsg="请输入有效的数字"><span class="pl5 mr20">%</span>
                <div class="red pr30 mh30"></div>
              </li>
              <li><span class="display-ib mr5 w200 tr"><em class="red pr5">*</em>最大10户借款余额占比</span>
                <input class="text border pl5 required" name="F25" value="<%=t6196.F25%>" maxlength="8" type="text" mtest="/^(-)?\d*(\d|(\.[0-9]{1,2}))$/" mtestmsg="请输入有效的数字"><span class="pl5 mr20">%</span>
                <div class="red pr30 mh30"></div>
              </li>
            </ul> --%>
            
            <ul class="gray6 input-list-container clearfix">
              <li><span class="display-ib mr5 w160 tr"><em class="red pr5">*</em>逾期金额</span>
                <input class="text border pl5 mr20 required" name="F26" value="<%=t6196.F26%>" maxlength="15" type="text" onKeyUp="value=value.replace(/\D/g,'')">
                <div class="red pr30 mh30"></div>
              </li>
              <li><span class="display-ib mr5 w200 tr"><em class="red pr5">*</em>金额逾期率</span>
                <input class="text border pl5 required" name="F27" value="<%=t6196.F27%>" maxlength="8" type="text" mtest="/^(-)?\d*(\d|(\.[0-9]{1,2}))$/" mtestmsg="请输入有效的数字"><span class="pl5 mr20">%</span>
                <div class="red pr30 mh30"></div>
              </li>
              <li><span class="display-ib mr5 w200 tr"><em class="red pr5">*</em>逾期90天（不含）以上金额</span>
                <input class="text border pl5 required" name="F44" value="<%=t6196.F44%>" maxlength="15" type="text" onKeyUp="value=value.replace(/\D/g,'')">
                <div class="red pr30 mh30"></div>
              </li>
            </ul>
            <ul class="gray6 input-list-container clearfix">  
              <li><span class="display-ib mr5 w160 tr"><em class="red pr5">*</em>逾期笔数</span>
                <input class="text border pl5 mr20 required" name="F42" value="<%=t6196.F42%>"  maxlength="9" type="text" onKeyUp="value=value.replace(/\D/g,'')">
                <div class="red pr30 mh30"></div>
              </li>
              <li><span class="display-ib mr5 w200 tr"><em class="red pr5">*</em>项目逾期率</span>
                <input class="text border pl5 required" name="F43" value="<%=t6196.F43%>" maxlength="8" type="text" mtest="/^(-)?\d*(\d|(\.[0-9]{1,2}))$/" mtestmsg="请输入有效的数字"><span class="pl5 mr20">%</span>
                <div class="red pr30 mh30"></div>
              </li>
              <li><span class="display-ib mr5 w200 tr"><em class="red pr5">*</em>逾期90天（不含）以上笔数</span>
                <input class="text border pl5 required" name="F45" value="<%=t6196.F45%>" maxlength="9" type="text" onKeyUp="value=value.replace(/\D/g,'')">
                <div class="red pr30 mh30"></div>
              </li>
             </ul>
             <ul class="gray6 input-list-container clearfix"> 
              <li><span class="display-ib mr5 w200 tr"><em class="red pr5">*</em>累计代偿金额</span>
                 <input class="text border pl5 mr20 required" name="F23" value="<%=t6196.F23%>" maxlength="15" type="text" onKeyUp="value=value.replace(/\D/g,'')">
                <div class="red pr30 mh30"></div>
              </li>
              <li><span class="display-ib mr5 w200 tr"><em class="red pr5">*</em>累计代偿笔数</span>
                <input class="text border pl5 required" name="F46" value="<%=t6196.F46%>" maxlength="9" type="text" onKeyUp="value=value.replace(/\D/g,'')">
                <div class="red pr30 mh30"></div>
              </li>
              <%-- <li><span class="display-ib mr5 w200 tr"><em class="red pr5">*</em>借款坏账率</span>
                <input class="text border pl5 required" name="F28" value="<%=t6196.F28%>" maxlength="8" type="text" mtest="/^(-)?\d*(\d|(\.[0-9]{1,2}))$/" mtestmsg="请输入有效的数字"><span class="pl5 mr20">%</span>
                <div class="red pr30 mh30"></div>
              </li> --%>
            </ul>
            </div>
            
            <div class="tc f16 mt20">
              <input type="submit" fromname="form1" class="btn btn-blue2 radius-6 pl20 pr20 sumbitForme" value="保存">
            </div>
          </div>
        </div>
      </div>
      </form>
      
    </div>
  </div>
</div>
    <!--右边内容 结束-->
<!--内容-->
<div id="info"></div>
<div class="popup_bg" style="display: none;"></div>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/dialog.js"></script>
<%
    String message = controller.getPrompt(request, response, PromptLevel.INFO);
    if (!StringHelper.isEmpty(message)) {
%>

<script type="text/javascript">
    $("#info").html(showDialogInfo("<%=message%>", "yes"));
    $("div.popup_bg").show();
</script>
<%
    }
%>

<%
    String errorMessage = controller.getPrompt(request, response, PromptLevel.ERROR);
    if (!StringHelper.isEmpty(errorMessage)) {

%>
<script type="text/javascript">
    $("#info").html(showDialogInfo("<%=errorMessage%>", "wrong"));
    $("div.popup_bg").show();

</script>
<%
    }
%>

<%
    String warnMessage = controller.getPrompt(request, response, PromptLevel.WARRING);
    if (!StringHelper.isEmpty(warnMessage)) {
%>
<script type="text/javascript">
    $("#info").html(showDialogInfo("<%=warnMessage%>", "wrong"));
    $("div.popup_bg").show();
</script>
<%
    }
%>
<script type="text/javascript">
var flgs = true;
$(function () {
	 $submit = $(".sumbitForme");
	 $intext = $('input[type="text"]');
	 $submit.click(function () {
		 flgs = true;
		 var cname = $(this).attr("fromname");
		 $form_intext = $('.' + cname + ' input[type="text"]');
		 $form_intext.each(function () {
	            return checkText($(this));
	     });
		 return flgs;
	 });
	 $intext.focus(function () {
	     $(this).blur(function () {
	         return checkText($(this));
	     });
	 });
});

//校验输入文本框
function checkText($eve) {
	flgs = true;
    if ($eve == undefined) {
        return false;
    }
    if ($eve.attr("class") == undefined) {
        return false;
    }
    var msg = $eve.attr("class");
    var value = $eve.val();
    if(msg.indexOf("isNaN") !=-1){
		if(isNaN($.trim(value))){
			$(".error_info").html("只能输入数字！");
			flgs = false;
			return false;
		}
		$(".error_info").html("");
	}    
    if(msg.indexOf("required") != -1){
    	var $div = $eve.nextAll("div");
    	if ($.trim(value) == "") {
    		$div.html("不能为空！");
    		flgs = false;
            return false;
        }
    	$div.html("");
    }
    if(msg.indexOf("reqAmount") != -1){
    	if ($.trim(value) == "") {
    		$(".error_info").html("不能为空！");
    		flgs = false;
            return false;
        }
    	$(".error_info").html("");
    }
   	var mtest = $eve.attr("mtest");
    if (mtest != undefined && mtest.length > 0) {
    	var myreg = eval(mtest);
    	var $div = $eve.nextAll("div");
        if (!myreg.test(value)) {
    		$div.html($eve.attr("mtestmsg"));
    		flgs = false;
            return false;
        }
        $div.html("");
    }
    return flgs;
}


</script>
</body>
</html>