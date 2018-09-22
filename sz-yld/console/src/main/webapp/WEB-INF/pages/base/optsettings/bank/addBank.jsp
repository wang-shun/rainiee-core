<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.dimeng.p2p.console.servlets.base.optsettings.bank.SearchBank" %>
<%@page import="com.dimeng.p2p.console.servlets.base.optsettings.bank.AddBank" %>
<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <%@include file="/WEB-INF/include/script.jsp"%>
</head>

<body>
<%
    CURRENT_CATEGORY = "JCXXGL";
    CURRENT_SUB_CATEGORY = "YHSZ";
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <!--加载内容-->
                <div class="p20">
                    <form action="<%=controller.getURI(request, AddBank.class) %>" method="post" class="form1">
                        <div class="border">
                            <div class="title-container"><i class="icon-i w30 h30 va-middle title-left-icon"></i>新增银行名称
                            </div>
                            <div class="content-container pl40 pt40 pr40 pb20">
                                <ul class="cell noborder yxjh ">
                                    <li class="mb10">
                                        <span class="display-ib w200 tr mr5"><span class="red">*</span>银行名称：</span>
                                        <input type="text" class="text border w300 yw_w5 required max-length-30"
                                               name="name"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("name")); %>"/>
                                        <span tip></span>
                                        <span errortip class="" style="display: none"></span>
                                    </li>
                                    <li class="mb10">
                                        <span class="display-ib w200 tr mr5"><span class="red">*</span>银行代码：</span>
                                        <input type="text" class="text border w300 yw_w5 required max-length-20"
                                               name="code"
                                               value="<%StringHelper.filterHTML(out, request.getParameter("code")); %>"/>
                                        <span tip></span>
                                        <span errortip class="" style="display: none"></span>
                                    </li>
                                    <li>
                                        <div class="pl200 ml5">
                                            <input type="submit" class="btn btn-blue2 radius-6 pl20 pr20 sumbitForme"
                                                   style="cursor: pointer;" fromname="form1" value="提交"/>
                                            <input type="button"
                                                   onclick="window.location.href='<%=controller.getURI(request, SearchBank.class)%>'"
                                                   class="btn btn-blue2 radius-6 pl20 pr20 ml40" value="取消"/>
                                        </div>
                                    </li>
                                </ul>
                            </div>
                        </div>
                    </form>
                </div>
                <!--加载内容 结束-->
            </div>
        </div>
    </div>
    <!--右边内容 结束-->
<!--内容-->
<%
    String warringMessage = controller.getPrompt(request, response, PromptLevel.WARRING);
    if (!StringHelper.isEmpty(warringMessage)) {
%>
<div class="w440 thickbox thickpos" style="margin:-80px 0 0 -220px;" id="showDiv">
    <div class="info clearfix">
        <div class="clearfix">
            <span class="fl tips"><%StringHelper.filterHTML(out, warringMessage); %></span>
        </div>
        <div class="dialog_btn"><input type="button" name="button2" onclick="$('#showDiv').hide()" value="确认"
                                       class="btn4 ml50"/></div>
    </div>
</div>
<%} %>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/validation.js"></script>
</body>
</html>