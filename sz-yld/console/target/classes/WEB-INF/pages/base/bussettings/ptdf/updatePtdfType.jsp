<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.console.servlets.base.bussettings.ptdf.UpdatePtdfType"%>
<%@page import="com.dimeng.p2p.S51.entities.T5131" %>
<%@page import="com.dimeng.p2p.S51.enums.T5131_F02" %>
<html>
<link href="<%=controller.getStaticPath(request) %>/css/hhmmss.css" rel="stylesheet">
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <%@include file="/WEB-INF/include/script.jsp"%>
</head>
<body>
<%
    CURRENT_CATEGORY = "JCXXGL";
    CURRENT_SUB_CATEGORY = "PTDFGL";
    T5131 t5131 = (T5131) request.getAttribute("ptdfType");

%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <!--加载内容-->
                <div class="p20">
                    <div class="border">
                        <div class="title-container">
                            <i class="icon-i w30 h30 va-middle title-left-icon"></i>不良资产处理方案设置
                        </div>
                        <div class="content-container pl40 pt40 pr40 pb20">
                            <form action="<%=controller.getURI(request, UpdatePtdfType.class)%>" method="post"
                                  class="form1">
                                <input type="hidden" name="id" value="<%=t5131.F01%>">
                                <ul class="gray6">
                                    <li class="mb10">
                                        <span class="display-ib w200 tr mr5">&nbsp;</span>
		                    			<span class="red"></span>
                                    </li>
                                    <li class="mb10">
                                        <span class="display-ib w200 tr mr5"><span class="red"></span>不良资产处理方案设置：</span>
                                        <label><input style="margin-left: 100px;" type="radio" name="dfType"
                                               value="N" <%if (T5131_F02.N.name().equalsIgnoreCase(t5131.F02.name())) { %>
                                               checked="checked" <%} %>/> 无</label>
                                        <label><input style="margin-left: 100px;" type="radio" name="dfType"
                                               value="BJ" <%if (T5131_F02.BJ.name().equalsIgnoreCase(t5131.F02.name())) { %>
                                               checked="checked" <%} %>/> 本金垫付</label>
                                        <label><input style="margin-left: 100px;" type="radio" name="dfType"
                                               value="BX" <%if (T5131_F02.BX.name().equalsIgnoreCase(t5131.F02.name())) { %>
                                               checked="checked" <%} %>/> 本息垫付</label>
                                    </li>
                                    <li class="mb10"><span class="display-ib w200 tr mr5">&nbsp;</span></li>
                                    <li>
                                        <div class="pl200 ml5">
                                            <input type="submit" class="btn btn-blue2 radius-6 pl20 pr20 sumbitForme"
                                                   fromname="form1" value="修改"/>
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
<div class="popup_bg hide"></div>
<div id="info"></div>
<!--内容-->
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/validation.js"></script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/dialog.js"></script>
<%
    String message = controller.getPrompt(request, response, PromptLevel.INFO);
    if (!StringHelper.isEmpty(message)) {
%>
<script type="text/javascript">
    $(".popup_bg").show();
    $("#info").html(showDialogInfo('<%=message%>', "yes"));
</script>
<%
    }
%>

<%
    String errorMessage = controller.getPrompt(request, response, PromptLevel.ERROR);
    if (!StringHelper.isEmpty(errorMessage)) {
%>
<script type="text/javascript">
    $(".popup_bg").show();
    $("#info").html(showDialogInfo('<%=message%>', "wrong"));
</script>
<%
    }
%>

<%
    String warnMessage = controller.getPrompt(request, response, PromptLevel.WARRING);
    if (!StringHelper.isEmpty(warnMessage)) {
%>
<script type="text/javascript">
    $(".popup_bg").show();
    $("#info").html(showDialogInfo('<%=message%>', "wrong"));
</script>
<%
    }
%>
</body>
</html>