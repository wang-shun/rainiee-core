<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.console.servlets.base.malloptsettings.condition.ViewSetCondition"%>
<%@page import="com.dimeng.p2p.console.servlets.base.malloptsettings.scoreset.ViewSetScore" %>
<%@page import="com.dimeng.p2p.repeater.score.entity.SetScore" %>
<%@page import="com.dimeng.util.ObjectHelper" %>
<%@page import="java.util.List" %>
<%@page import="com.dimeng.p2p.S63.entities.T6353" %>
<%@ page import="com.dimeng.p2p.console.servlets.base.malloptsettings.condition.DelSetCondition" %>
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
    CURRENT_CATEGORY = "JCXXGL";
    CURRENT_SUB_CATEGORY = "SCSXTJSZ";
    List<T6353> scoreRangeList = ObjectHelper.convert(request.getAttribute("scoreRangeList"), List.class);
    List<T6353> amountRangeList = ObjectHelper.convert(request.getAttribute("amountRangeList"), List.class);
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <!--加载内容-->
                <div class="p20">
                    <div class="border">
                        <div class="title-container">
                            <i class="icon-i w30 h30 va-middle title-left-icon"></i>商城筛选条件设置
                        </div>
                        <div class="content-container pl40 pt40 pr40 pb20">
                            <form action="<%=controller.getURI(request, ViewSetCondition.class)%>" method="post" class="form1" onsubmit="return checkCondition();">
                               <%--<div class="pl200 ml5 info orange" id="info_msg">
                                <%StringHelper.filterHTML(out, controller.getPrompt(request, response, PromptLevel.ERROR));%>
                                <%StringHelper.filterHTML(out, controller.getPrompt(request, response, PromptLevel.INFO));%>
                                </div>--%>
                                <div class="pat_title clearfix bold ml100">积分范围设置</div>
                                <ul class="gray6 pt20" id="firstScoreRange">
                                    <li class="mb10">
										<span class="display-ib w200 tr mr5">&nbsp;</span>
										<input name="minScore" style="display:none;" type="text" value="0"/>
                                        <input name="maxScore" type="text" maxlength="11" class="text border w150 yw_w5 required" <%if(null != scoreRangeList){%> value="<%=scoreRangeList.get(0).F03%>" <%}%> onKeyUp="value=value.replace(/\\D/g,'')" mtest="/^((([1-9][0-9]*)|0)|((([1-9][0-9]*)|0)))$/" mtestmsg="必须为整数"/>
                                        <span class="ml20">以下</span>
                                        <span tip></span>
                                        <span errortip class="" style="display: none"></span>
                                    </li>
                                </ul>
                                <ul class="gray6" id="scoreRangeUl">
                                    <%if(null != scoreRangeList){
                                          for(int i=1;i<scoreRangeList.size()-1;i++){
                                    %>
                                    <li class="mb10">
										<span class="display-ib w200 tr mr5">&nbsp;</span>
										<input name="minScore" type="text" maxlength="11" class="text border w150 yw_w5 required" value="<%=scoreRangeList.get(i).F02%>" onKeyUp="value=value.replace(/\\D/g,'')" mtest="/^((([1-9][0-9]*)|0)|((([1-9][0-9]*)|0)))$/" mtestmsg="必须为整数"/>
										<span class="ml5 mr5">至</span>
										<input name="maxScore" type="text" maxlength="11" class="text border w150 yw_w5 required" value="<%=scoreRangeList.get(i).F03%>" onKeyUp="value=value.replace(/\\D/g,'')" mtest="/^((([1-9][0-9]*)|0)|((([1-9][0-9]*)|0)))$/" mtestmsg="必须为整数"/>
                                        <a href="javascript:void(0);" onclick="delLi(this,<%=scoreRangeList.get(i).F01%>);" class="ml10 display-ib va-middle w30 h30 icon-i remove-radiusbtn-icon"></a>
                                        <span tip></span>
                                        <span errortip class="" style="display: none"></span>
                                    </li>
                                    <%}}%>
                                </ul>
                                <ul class="gray6" id="lastScoreRange">
                                    <li class="mb10">
										<span class="display-ib w200 tr mr5">&nbsp;</span>
                                        <input name="minScore" type="text" maxlength="11" class="text border w150 yw_w5 required" <%if(null != scoreRangeList){%> value="<%=scoreRangeList.get(scoreRangeList.size()-1).F02%>" <%}%> onKeyUp="value=value.replace(/\\D/g,'')" mtest="/^((([1-9][0-9]*)|0)|((([1-9][0-9]*)|0)))$/" mtestmsg="必须为整数"/>
                                        <input name="maxScore" style="display:none;" type="text" value="2147483647"/>
                                        <span class="ml20">以上</span><a id="addScoreRange" href="javascript:void(0);" class="ml10 display-ib va-middle w30 h30 icon-i add-radiusbtn-icon"></a>
                                        <span tip></span>
                                        <span errortip class="" style="display: none"></span>
                                    </li>
                                </ul>
                                <%if(BooleanParser.parse(configureProvider.getProperty(MallVariavle.ALLOWS_THE_BALANCE_TO_BUY))){%>
                                <div class="pat_title clearfix bold ml100 pt20">价格范围设置</div>
                                <ul class="gray6 pt20" id="firstAmountRange">
                                    <li class="mb10">
										<span class="display-ib w200 tr mr5">&nbsp;</span>
										<input name="minAmount" style="display:none;" type="text" value="0"/>
                                        <input name="maxAmount" type="text" maxlength="11" class="text border w150 yw_w5 required" <%if(null != amountRangeList){%> value="<%=amountRangeList.get(0).F03%>" <%}%> onKeyUp="value=value.replace(/\\D/g,'')" mtest="/^((([1-9][0-9]*)|0)|((([1-9][0-9]*)|0)))$/" mtestmsg="必须为整数"/>
                                        <span class="ml5">元</span>
                                        <span class="ml20">以下</span>
                                        <span tip></span>
                                        <span errortip class="" style="display: none"></span>
                                    </li>
                                </ul>
                                <ul class="gray6" id="amountRangeUl">
                                    <%if(null != amountRangeList){
                                          for(int i=1;i<amountRangeList.size()-1;i++){
                                    %>
                                    <li class="mb10">
										<span class="display-ib w200 tr mr5">&nbsp;</span>
										<input name="minAmount" type="text" maxlength="11" class="text border w150 yw_w5 required" value="<%=amountRangeList.get(i).F02%>" onKeyUp="value=value.replace(/\\D/g,'')" mtest="/^((([1-9][0-9]*)|0)|((([1-9][0-9]*)|0)))$/" mtestmsg="必须为整数"/>
										<span class="ml5 mr5">至</span>
										<input name="maxAmount" type="text" maxlength="11" class="text border w150 yw_w5 required" value="<%=amountRangeList.get(i).F03%>" onKeyUp="value=value.replace(/\\D/g,'')" mtest="/^((([1-9][0-9]*)|0)|((([1-9][0-9]*)|0)))$/" mtestmsg="必须为整数"/>
                                        <span class="ml5">元</span><a href="javascript:void(0);" onclick="delLi(this,<%=amountRangeList.get(i).F01%>);" class="ml10 display-ib va-middle w30 h30 icon-i remove-radiusbtn-icon"></a>
                                        <span tip></span>
                                        <span errortip class="" style="display: none"></span>
                                    </li>
                                    <%}}%>
                                </ul>
                                <ul class="gray6" id="lastAmountRange">
                                    <li class="mb10">
										<span class="display-ib w200 tr mr5">&nbsp;</span>
                                        <input name="minAmount" type="text" maxlength="11" class="text border w150 yw_w5 required" <%if(null != amountRangeList){%> value="<%=amountRangeList.get(amountRangeList.size()-1).F02%>" <%}%> onKeyUp="value=value.replace(/\\D/g,'')" mtest="/^((([1-9][0-9]*)|0)|((([1-9][0-9]*)|0)))$/" mtestmsg="必须为整数"/>
                                        <input name="maxAmount" style="display:none;" type="text" value="2147483647"/>
                                        <span class="ml5">元</span>
                                        <span class="ml20">以上</span><a id="addAmountRange" href="javascript:void(0);" class="ml10 display-ib va-middle w30 h30 icon-i add-radiusbtn-icon"></a>
                                        <span tip></span>
                                        <span errortip class="" style="display: none"></span>
                                    </li>
                                </ul>
                                <%}%>
                                <ul class="gray6 pt10">
                                    <li>
                                        <div class="pl200 ml5">
                                            <input type="submit" class="btn btn-blue2 radius-6 pl20 pr20 sumbitForme"
                                                   fromname="form1" value="提交"/>
                                        </div>
                                    </li>
                                </ul>
                            </form>
                        </div>
                    </div>
                </div>
                <!--加载内容 结束-->
            </div>
        </div>
    </div>
    <!--右边内容 结束-->
<!--内容-->
<div id="info"></div>
<div class="popup_bg" style="display: none;"></div>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/dialog.js"></script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/mall/viewSetCondition.js"></script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/validation.js"></script>
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
    $("#info").html(showDialogInfo("<%=warnMessage%>", "question"));
    $("div.popup_bg").show();
</script>
<%
    }
%>

<script type="text/javascript">
    var delT6353URL = '<%=controller.getURI(request, DelSetCondition.class)%>';

	
</script>

</body>
</html>