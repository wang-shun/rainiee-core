<%@page import="com.dimeng.p2p.console.servlets.info.yygl.yqlj.SearchYqlj"%>
<%@page import="com.dimeng.p2p.console.servlets.info.yygl.yqlj.AddYqlj" %>
<%@page import="com.dimeng.p2p.common.FormToken"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <%@include file="/WEB-INF/include/meta.jsp" %>
    <%@include file="/WEB-INF/include/style.jsp" %>
    <%@include file="/WEB-INF/include/script.jsp"%>
</head>

<body>
<%
    CURRENT_CATEGORY = "XCGL";
    CURRENT_SUB_CATEGORY = "YQLJ";
%>
<div class="right-container">
    <div class="viewFramework-body">
      <div class="viewFramework-content">
                <div class="p20">
                    <form name="example" method="post" action="<%=controller.getURI(request, AddYqlj.class)%>"
                          class="form1">
                          <%=FormToken.hidden(serviceSession.getSession()) %>
                        <div class="border">
                            <div class="title-container"><i class="icon-i w30 h30 va-middle title-left-icon"></i>新增友情链接
                            </div>
                            <div class="content-container pl40 pr40 pb20">
                                <ul class="gray6">
                                    <li class="mb10"><span class="display-ib w200 tr mr5">&nbsp;</span>
                                    </li>
                                    <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>名称：</span>
                                        <input type="text" class="text border w300 yw_w4 required max-length-30"
                                               name="name"
                                               value="<%StringHelper.filterHTML(out,request.getParameter("name"));%>"/>
                                        <span tip>最大30个字</span>
                                        <span errortip class="" style="display: none"></span>
                                    </li>
                                    <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>链接地址：</span>
                                        <input type="text" class="text border w300 yw_w4 required" name="url"
                                               value="<%StringHelper.filterHTML(out,request.getParameter("url"));%>"/>
                                        <span tip></span>
                                        <span errortip class="" style="display: none"></span>
                                    </li>
                                    <li class="mb10"><span class="display-ib w200 tr mr5"><em class="red pr5">*</em>排序值：</span>
                                        <input type="text" class="text border w300 yw_w4 required isint max-length-10"
                                               name="sortIndex"
                                               value="<%StringHelper.filterHTML(out,request.getParameter("sortIndex"));%>"/>
                                        <span tip>说明：排序值代表优先级，值越小优先级越高（最高优先级是0）。</span>
                                        <span errortip class="" style="display: none"></span>
                                    </li>

                                    <li>
                                        <div class="pl200 ml5">
                                            <input type="submit" class="btn btn-blue2 radius-6 pl20 pr20 sumbitForme"
                                                   style="cursor: pointer;" fromname="form1" value="确认"/>
                                            <input type="button" value="取消" class="btn btn-blue2 radius-6 pl20 pr20 ml40"
                                                   onclick="window.location.href='<%=controller.getURI(request, SearchYqlj.class) %>'">
                                        </div>
                                </ul>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
<div class="popup_bg hide"></div>
<div id="info"></div>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/dialog.js"></script>
<script type="text/javascript" src="<%=controller.getStaticPath(request)%>/js/validation.js"></script>
<%
    String warringMessage = controller.getPrompt(request, response, PromptLevel.WARRING);
    if (!StringHelper.isEmpty(warringMessage)) {
%>
<script type="text/javascript">
    $("#info").html(showDialogInfo("<%=warringMessage%>", "wrong"));
    $("div.popup_bg").show();
</script>
<%
    }
%>

<%
    String infoMessage = controller.getPrompt(request, response, PromptLevel.INFO);
    if (!StringHelper.isEmpty(infoMessage)) {
%>
<script type="text/javascript">
    $("#info").html(showDialogInfo("<%=infoMessage%>", "yes"));
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
    $("#info").html(showSuccInfo("<%=errorMessage%>", "wrong","<%=controller.getURI(request, SearchYqlj.class)%>"));
    $("div.popup_bg").show();
</script>
<%
    }
%>
</body>
</html>