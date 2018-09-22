<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.console.servlets.system.ywtg.sms.SmsList"%>
<%@page import="com.dimeng.p2p.console.servlets.system.ywtg.sms.AddSms" %>
<%@page import="com.dimeng.p2p.console.servlets.system.ywtg.sms.ImportSms" %>
<%@page import="com.dimeng.p2p.common.enums.SendType" %>
<%@page import="com.dimeng.util.ObjectHelper" %>
<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <%@include file="/WEB-INF/include/script.jsp"%>
</head>
<body>
    <%
		CURRENT_CATEGORY = "XTGL";
		CURRENT_SUB_CATEGORY = "DXTG";
		String[] mobiles = ObjectHelper.convertArray(request.getAttribute("mobiles"), String.class);
		String sendType=request.getParameter("sendType");
		String errorMessage = controller.getPrompt(request, response, PromptLevel.WARRING);
		if(StringHelper.isEmpty(sendType)){
			sendType=SendType.ZDR.name();
		}
	%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <!--加载内容 开始-->
                <div class="p20">
                    <!--切换栏目-->
                    <form action="<%=controller.getURI(request, AddSms.class)%>" method="post" class="form1" id="form1">
                        <div class="border">
                            <div class="title-container"><i class="icon-i w30 h30 va-middle title-left-icon"></i>短信推广
                            </div>
                            <div class="tab-content-container p20">
                                <div class="tab-item">
                                    <ul class="gray6">
                                        <li class="mb10"><span class="display-ib w200 tr mr5 fl"><em
                                                class="red pr5">*</em>发送对象：</span>

                                            <div class="pl200">
                                                <%for (SendType type : SendType.values()) {%>
                                                <input type="radio" value="<%=type.name()%>" name="sendType"
                                                       onclick="showSms('<%=type%>')"
                                                        <%if (type.name().equals(sendType)) {%> checked="checked"
                                                        <%}%> /><%=type.getName()%><span class="ml50"></span><%}%>
                                            </div>
                                        </li>
                                        <li class="mb10" id="smsLi"><span class="display-ib w200 tr mr5 fl"><em
                                                class="red pr5">*</em>手机号码：</span>

                                            <div class="pl200 clearfix">
                                            	<textarea name="mobile" id="sms" cols="45" rows="5"
                                            		placeholder="发送给多人请用空格隔开，如：13800138001 13800138002"
                                                    class="ww50 h120 border p5 fl required"><%
                                                	if (mobiles != null) {
                                                    	for (String s : mobiles) {
                                            		%>
                                            			<%=s + "\n"%>
                                            			<%}
                                                	}else{StringHelper.filterHTML(out, request.getParameter("mobile"));} %></textarea>
                                                <a href="javascript:void(0);" id="importMobile"
                                                   class="btn-blue2 btn white radius-6 pl20 pr20 ml20">导入号码</a>
                                            	<span tip></span>
                                                <span errortip class="" style="display: none"></span>
                                            </div>
                                        </li>
                                        <li class="mb10" id="importinfo">
                                        	<span class="display-ib w200 tr mr5 fl">&nbsp;</span>
                                        	<%if(!StringHelper.isEmpty(errorMessage)){%>
							                <div class="pl200 clearfix red"><%=errorMessage %></div>
							                <%}else{ %>
                                            <div class="pl200 clearfix">支持导入csv、txt格式数据</div>
                                            <%} %>
                                        </li>
                                        <li class="mb10"><span class="display-ib w200 tr mr5 fl"><em
                                                class="red pr5">*</em>短信内容：</span>
                                            <div class="pl200">
                                                <textarea rows="7" cols="45" maxlength="200" name="content"
                                                          class="border required max-length-200"><%StringHelper.filterHTML(out, request.getParameter("content"));%></textarea>
                                                <span tip></span>
                                                <span errortip class="" style="display: none"></span>
                                            </div>
                                        </li>
                                        <li class="mb10">
                                            <div class="pl200">
                                                <input type="submit" value="确定"
                                                       class="btn-blue2 btn white radius-6 pl20 pr20 sumbitForme"
                                                       fromname="form1"/>
                                                <input type="button" value="取消"
                                                       class="btn btn-blue2 radius-6 pl20 pr20 ml40"
                                                       onclick="window.location.href='<%=controller.getURI(request, SmsList.class)%>'">
                                            </div>
                                        </li>
                                    </ul>
                                </div>
                            </div>
                        </div>
                    </form>
                </div>
                <!--加载内容 结束-->
            </div>
        </div>
    </div>
    <!--右边内容 结束-->

<div id="import" style="display: none">
    <div class="popup-box">
        <div class="popup-title-container">
            <h3 class="pl20 f18">导入</h3>
            <a class="icon-i popup-close2" href="javascript:void(0);"
               onclick="javascript:document.getElementById('import').style.display='none';return false;"></a>
        </div>
        <form action="<%=controller.getURI(request, ImportSms.class)%>" id="import_form" method="post"
              enctype="multipart/form-data" class="form2">
            <div class="popup-content-container pt20 ob20 clearfix">
                <div class="mb40 gray6">
                    <ul>
                        <li class="mb10"><span class="display-ib tr mr5"><em class="f24 gray3">导入手机号：</em></span>
                            <input class="text border w150 pl5" type="file" name="file" value="导入手机号"/>
                        </li>
                    </ul>
                </div>
                
                <div class="tc f16">
                    <a href="javascript:$('#import_form').submit();"
                       class="btn-blue2 btn white radius-6 pl20 pr20">提交</a>
                    <a class="btn btn-blue2 radius-6 pl20 pr20 ml40" href="javascript:void(0);"
                       onclick="javascript:document.getElementById('import').style.display='none';return false;">取消</a>
                </div>
            </div>
        </form>
    </div>
    <div class="popup_bg"></div>
</div>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/validation.js"></script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/dialog.js"></script>
<%
    String warringMessage = controller.getPrompt(request, response, PromptLevel.ERROR);
    if (!StringHelper.isEmpty(warringMessage)) {
%>
<div class="popup-box" id="closeInfoShow">
    <div class="popup-title-container">
        <h3 class="pl20 f18">提示</h3>
        <a class="icon-i popup-close2" href="javascript:void(0);" onclick="closeInfoShow()"></a>
    </div>
    <div class="popup-content-container pt20 pb20 clearfix">
        <div class="tc mb20 mt40"><span class="icon-i w30 h30 va-middle radius-wrong-icon"></span><span
                class="f20 h30 va-middle ml10"><%StringHelper.filterHTML(out, warringMessage); %></span></div>
        <div class="tc f16"><a href="javascript:void(0);" onclick="closeInfoShow();"
                               class="btn-blue2 btn white radius-6 pl20 pr20">确定</a></div>
    </div>
</div>
<div class="popup_bg"></div>
<%}%>
<script type="text/javascript">
    $("#importMobile").click(function () {
        $("#import").show();
    });
    $("#close").click(function () {
        $("#import").hide();
    });
    function showSms(type) {
        if (type == '<%=SendType.ZDR%>') {
            $("#smsLi").show();
            $("#importinfo").show();
            $("#sms").attr("value", "")
        } else if (type == '<%=SendType.SY%>') {
            $("#smsLi").hide();
            $("#importinfo").hide();
            $("#sms").attr("value", "all")
        }
    }
    function closeInfoShow() {
        $("#closeInfoShow").hide();
        $(".popup_bg").hide();
    }
</script>